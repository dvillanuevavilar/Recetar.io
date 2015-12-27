package es.uvigo.esei.dm1516.p10;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.*;
import es.uvigo.esei.dm1516.p10.Core.App;
import es.uvigo.esei.dm1516.p10.Mapper.DataFetcher;
import es.uvigo.esei.dm1516.p10.Mapper.retrieveDataUser;
import es.uvigo.esei.dm1516.p10.Model.Receta;
import es.uvigo.esei.dm1516.p10.Model.Usuario;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Main extends Activity {
    private static final int REQUEST_CODE = 1;
    private SparseArray<GrupoDeItems> secciones;
    private AdaptadorSeccion adapter;
    private static Usuario currentUser;
    private boolean mostrarFav = false;
    private boolean mostrarMen = false;
    private boolean primerInicio = true;
    private Menu menu;

    public static Usuario getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(){
        currentUser=null;
        Toast.makeText(this,"Usuario no válido",Toast.LENGTH_SHORT);
    }

    public boolean estadoConexion() {
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void updateStatus() {
        if (estadoConexion()) {
            if (currentUser != null) {
                new DataFetcher(Main.this).execute(currentUser.getEmail());
            }
            else{
                new DataFetcher(Main.this).execute("");
            }
        } else {
            Toast.makeText(this, "Necesitas conexión a internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        secciones = new SparseArray<GrupoDeItems>();

        ExpandableListView lista = (ExpandableListView) this.findViewById(R.id.listViewexp);
        adapter = new AdaptadorSeccion(this, secciones);
        lista.setAdapter(adapter);

        currentUser = null;
        mostrarMen=true;
        Main.this.updateStatus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if ((resultCode == RESULT_OK) && (requestCode == REQUEST_CODE)) {
            currentUser = new Usuario(data.getExtras().getString("email"), data.getExtras().getString("nombre"), data.getExtras().getString("pass"));
            this.onPrepareOptionsMenu(this.menu);
            Main.this.updateStatus();
        }
        if (resultCode == -100) {
            Main.this.updateStatus();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences usuario = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = usuario.edit();
        //Si el usuario no ciera sesión lo almacenamos
        if (currentUser != null) {
            editor.clear();
            editor.putBoolean("estado", true);
            editor.putString("email", currentUser.getEmail());
            editor.putString("contrasenha", currentUser.getContrasenha());
            editor.putString("nombre", currentUser.getNombre());
            editor.apply();
        } else {
            editor.clear();
            editor.putBoolean("estado", false);
            editor.apply();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        GrupoDeItems primeros = new GrupoDeItems("Primer Plato");
        GrupoDeItems segundos = new GrupoDeItems("Segundo Plato");
        GrupoDeItems postres = new GrupoDeItems("Postre");


        ArrayList<Receta> recetas = ((App) this.getApplication()).getDb().listarRecetas();
        if (mostrarFav) {
            recetas = ((App) this.getApplication()).getDb().listarRecetasFavoritas(currentUser.getEmail());
        }
        for (Receta r : recetas) {
            if (r.getSeccion().equals("Primer plato")) {
                primeros.add(r);
            } else if (r.getSeccion().equals("Segundo plato")) {
                segundos.add(r);
            } else {
                postres.add(r);
            }
        }
        secciones.append(0, primeros);
        secciones.append(1, segundos);
        secciones.append(2, postres);
        adapter.notifyDataSetChanged();

        //Cargamos usuario si existe
        SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
        Boolean estado = prefs.getBoolean("estado", false);
        if (estado == true) {
            String email = prefs.getString("email", "");
            String nombre = prefs.getString("nombre", "");
            String contrasenha = prefs.getString("contrasenha", "");
            currentUser = new Usuario(email, nombre, contrasenha);
            if(primerInicio == true) {
                new retrieveDataUser(Main.this).execute(currentUser.getEmail(), currentUser.getContrasenha());
                primerInicio=false;
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //menu.clear();
        if (currentUser != null) {
            menu.setGroupEnabled(R.id.grupo2, false);
            menu.setGroupVisible(R.id.grupo2, false);
            menu.setGroupEnabled(R.id.grupo1, true);
            menu.setGroupVisible(R.id.grupo1, true);
        } else {
            menu.setGroupEnabled(R.id.grupo1, false);
            menu.setGroupVisible(R.id.grupo1, false);
            menu.setGroupEnabled(R.id.grupo2, true);
            menu.setGroupVisible(R.id.grupo2, true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.main_menu, menu);
        this.menu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.mainMenuItemOpt1:
                //Sincronizar
                mostrarMen=true;
                Main.this.updateStatus();
                break;
            case R.id.mainMenuItemOpt2:
                //Favoritos
                mostrarFav = !mostrarFav;
                Main.this.onStart();
                break;
            case R.id.mainMenuItemOpt3:
                //Crear receta
                if (estadoConexion()) {
                    Intent intentCrearReceta = new Intent(Main.this, CrearReceta.class);
                    Main.this.startActivityForResult(intentCrearReceta, REQUEST_CODE);
                } else {
                    Toast.makeText(this, "Necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.mainMenuItemOpt4:
                //Cerrar sesion
                currentUser = null;
                mostrarFav = false;
                onStop();
                onStart();
                Toast.makeText(this, "Desconectado", Toast.LENGTH_SHORT).show();
                this.onPrepareOptionsMenu(this.menu);
                break;
            case R.id.mainMenu2ItemOpt1:
                //Sincronizar
                mostrarMen=true;
                Main.this.updateStatus();
                break;
            case R.id.mainMenu2ItemOpt2:
                //Intent de login
                Intent intentLogin2 = new Intent(Main.this, Login.class);
                Main.this.startActivityForResult(intentLogin2, REQUEST_CODE);
                break;
            case R.id.mainMenu2ItemOpt3:
                //Registro
                if (estadoConexion()) {
                    Intent intentRegistro = new Intent(Main.this, Registro.class);
                    Main.this.startActivityForResult(intentRegistro, REQUEST_CODE);
                } else {
                    Toast.makeText(this, "Necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    public void setMensaje(String mensaje){
        if(mostrarMen){
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        }
        mostrarMen=false;
    }

}