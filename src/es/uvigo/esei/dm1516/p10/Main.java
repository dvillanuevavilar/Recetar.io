package es.uvigo.esei.dm1516.p10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import es.uvigo.esei.dm1516.p10.Core.App;
import es.uvigo.esei.dm1516.p10.Mapper.DataFetcher;
import es.uvigo.esei.dm1516.p10.Mapper.retrieveDataUser;
import es.uvigo.esei.dm1516.p10.Model.Receta;
import es.uvigo.esei.dm1516.p10.Model.Usuario;

import java.util.ArrayList;

public class Main extends Activity {
    private static final int REQUEST_CODE = 1;
    private static final int REFRESH_SQL = -100;
    private GrupoDeItems primeros, segundos, postres;
    private AdaptadorSeccion adapter;
    private static Usuario currentUser;
    private boolean mostrarFav = false;
    private boolean primerInicio = true;
    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ExpandableListView lista = (ExpandableListView) this.findViewById(R.id.listViewexp);

        SparseArray<GrupoDeItems> secciones = new SparseArray<GrupoDeItems>();
        adapter = new AdaptadorSeccion(this, secciones);
        lista.setAdapter(adapter);

        primeros = new GrupoDeItems("Primer Plato");
        segundos = new GrupoDeItems("Segundo Plato");
        postres = new GrupoDeItems("Postre");
        secciones.append(0, primeros);
        secciones.append(1, segundos);
        secciones.append(2, postres);

        currentUser = null;
        if (Main.this.estadoConexion()) {
            Main.this.updateSQLite(false);
        } else {
            Main.this.updateRecetasList();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        //Cargamos usuario si existe
        SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
        Boolean estado = prefs.getBoolean("estado", false);
        if (estado) {
            String email = prefs.getString("email", "");
            String nombre = prefs.getString("nombre", "");
            String contrasenha = prefs.getString("contrasenha", "");
            Usuario userToLogin = new Usuario(email, nombre, contrasenha);
            if (primerInicio) {
                if (estadoConexion()) {
                    new retrieveDataUser(Main.this).execute(userToLogin.getEmail(), userToLogin.getContrasenha());
                } else {
                    Toast.makeText(this, R.string.noposiblelogin, Toast.LENGTH_SHORT).show();
                }
                primerInicio = false;
            }
        }
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
    public boolean onPrepareOptionsMenu(Menu menu) {
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
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            //Sincronizar
            case R.id.mainMenuItemOpt1:
                Main.this.updateSQLite(true);
                break;

            //Favoritos
            case R.id.mainMenuItemOpt2:
                mostrarFav = !mostrarFav;
                Main.this.updateRecetasList();
                break;

            //Crear receta
            case R.id.mainMenuItemOpt3:
                if (estadoConexion()) {
                    Intent intentCrearReceta = new Intent(Main.this, CrearReceta.class);
                    Main.this.startActivityForResult(intentCrearReceta, REQUEST_CODE);
                } else {
                    Toast.makeText(this, R.string.needConnection, Toast.LENGTH_SHORT).show();
                }
                break;

            //Cerrar sesion
            case R.id.mainMenuItemOpt4:
                currentUser = null;
                mostrarFav = false;
                onStop();
                onStart();
                Toast.makeText(this, R.string.desconected, Toast.LENGTH_SHORT).show();
                this.onPrepareOptionsMenu(this.menu);
                this.updateRecetasList();
                break;

            //Sincronizar
            case R.id.mainMenu2ItemOpt1:
                Main.this.updateSQLite(true);
                break;

            //Login
            case R.id.mainMenu2ItemOpt2:
                if (estadoConexion()) {
                    Intent intentLogin2 = new Intent(Main.this, Login.class);
                    Main.this.startActivityForResult(intentLogin2, REQUEST_CODE);
                } else {
                    Toast.makeText(this, R.string.needConnection, Toast.LENGTH_SHORT).show();
                }
                break;

            //Registro
            case R.id.mainMenu2ItemOpt3:
                if (estadoConexion()) {
                    Intent intentRegistro = new Intent(Main.this, Registro.class);
                    Main.this.startActivityForResult(intentRegistro,REQUEST_CODE);
                } else {
                    Toast.makeText(this, R.string.needConnection, Toast.LENGTH_SHORT).show();
                }
                break;

            //About
            case R.id.mainMenuItemOpt5:
                if (estadoConexion()) {
                    Main.this.startActivity(new Intent(Main.this, About.class));
                } else {
                    Toast.makeText(this, R.string.needConnection, Toast.LENGTH_SHORT).show();
                }
                break;

            //About
            case R.id.mainMenu2ItemOpt4:
                if (estadoConexion()) {
                    Main.this.startActivity(new Intent(Main.this, About.class));
                } else {
                    Toast.makeText(this, R.string.needConnection, Toast.LENGTH_SHORT).show();
                }
                break;

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if ((resultCode == RESULT_OK) && (requestCode == REQUEST_CODE)) {
            setCurrentUser(new Usuario(data.getExtras().getString("email"), data.getExtras().getString("nombre"), data.getExtras().getString("pass")));
            primerInicio = false;
        }

        if ((resultCode == REFRESH_SQL) && (requestCode == REQUEST_CODE)) {
            updateSQLite(false);
        }
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

    public void updateSQLite(boolean mensaje) {
        if (Main.this.estadoConexion()) {
            if (currentUser != null) {
                new DataFetcher(Main.this, mensaje).execute(currentUser.getEmail());
            } else {
                new DataFetcher(Main.this, mensaje).execute("");
            }
        } else {
            Toast.makeText(this, R.string.needConnection, Toast.LENGTH_SHORT).show();
        }
    }

    public void updateRecetasList() {
        ArrayList<Receta> recetas;
        if (mostrarFav) {
            recetas = ((App) this.getApplication()).getDb().listarRecetasFavoritas(currentUser.getEmail());
            Main.this.setTitle(R.string.favoritas);
        } else {
            recetas = ((App) this.getApplication()).getDb().listarRecetas();
            Main.this.setTitle(R.string.recetas);
        }

        primeros.clear();
        segundos.clear();
        postres.clear();
        for (Receta r : recetas) {
            switch (r.getSeccion()) {
                case "Primer plato":
                    primeros.add(r);
                    break;
                case "Segundo plato":
                    segundos.add(r);
                    break;
                default:
                    postres.add(r);
                    break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    public static Usuario getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Usuario user) {
        currentUser = user;
        this.updateSQLite(false);
        this.onPrepareOptionsMenu(menu);
    }

}