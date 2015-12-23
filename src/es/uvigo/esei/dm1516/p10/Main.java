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

    public static Usuario getCurrentUser() {
        return currentUser;
    }

    public void crearDatos() {
        Receta rc1 = new Receta(0, "Tortilla francesa", 5, "Facil", 2, "Huevos", "Batir y freir", "Primer plato", "rosa@receta.es");
        Receta rc2 = new Receta(0, "Tortilla espa\u00f1ola", 6, "Facil", 2, "Huevos y patatas", "Batir, pelar y freir", "Primer plato", "rosa@receta.es");
        Receta rc3 = new Receta(0, "Carne asada", 3, "Media", 5, "Carne", "Asar la carne", "Segundo plato", "rosa@receta.es");
        Receta rc4 = new Receta(0, "Pollo asado", 2, "Media", 4, "Pollo", "Asar el pollo", "Segundo plato", "rosa@receta.es");
        Receta rc5 = new Receta(0, "Tarta de queso", 1, "Dificil", 2, "Queso y tarta", "Abrir la nevera", "Postre", "rosa@receta.es");
        Receta rc6 = new Receta(0, "Tarta helada", 3, "Dificil", 3, "Tarta y hielo", "Abrir el congelador", "Postre", "rosa@receta.es");

        rc6.setIngredientes("Lorem ipsum ad his scripta blandit partiendo, eum fastidii accumsan euripidis in, eum liber hendrerit an. Qui ut wisi vocibus suscipiantur, quo dicit ridens inciderint id. Quo mundi lobortis reformidans eu, legimus senserit definiebas an eos. Eu sit tincidunt incorrupte definitionem, vis mutat affert percipit cu, eirmod consectetuer signiferumque eu per. In usu latine equidem dolores. Quo no falli viris intellegam, ut fugit veritus placerat per.");
        rc6.setElaboracion("Lorem ipsum ad his scripta blandit partiendo, eum fastidii accumsan euripidis in, eum liber hendrerit an. Qui ut wisi vocibus suscipiantur, quo dicit ridens inciderint id. Quo mundi lobortis reformidans eu, legimus senserit definiebas an eos. Eu sit tincidunt incorrupte definitionem, vis mutat affert percipit cu, eirmod consectetuer signiferumque eu per. In usu latine equidem dolores. Quo no falli viris intellegam, ut fugit veritus placerat per.");

        Usuario usr1 = new Usuario("juan@receta.es", "Juan Rodr�guez", "abc123.");
        Usuario usr2 = new Usuario("rosa@receta.es", "Rosa Lois", "abc123.");

        if (!((App) this.getApplication()).getDb().existeUsuario(usr1.getEmail())) {
            ((App) this.getApplication()).getDb().insertarUsuario(usr1);
        }
        if (!((App) this.getApplication()).getDb().existeUsuario(usr2.getEmail())) {
            ((App) this.getApplication()).getDb().insertarUsuario(usr2);
        }

        if (!((App) this.getApplication()).getDb().existeReceta(rc1.getIdReceta())) {
            ((App) this.getApplication()).getDb().insertarReceta(rc1);
        }
        if (!((App) this.getApplication()).getDb().existeReceta(rc2.getIdReceta())) {
            ((App) this.getApplication()).getDb().insertarReceta(rc2);
        }
        if (!((App) this.getApplication()).getDb().existeReceta(rc3.getIdReceta())) {
            ((App) this.getApplication()).getDb().insertarReceta(rc3);
        }
        if (!((App) this.getApplication()).getDb().existeReceta(rc4.getIdReceta())) {
            ((App) this.getApplication()).getDb().insertarReceta(rc4);
        }
        if (!((App) this.getApplication()).getDb().existeReceta(rc5.getIdReceta())) {
            ((App) this.getApplication()).getDb().insertarReceta(rc5);
        }
        if (!((App) this.getApplication()).getDb().existeReceta(rc6.getIdReceta())) {
            ((App) this.getApplication()).getDb().insertarReceta(rc6);
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

    public void updateStatus() {

        if (estadoConexion()) {
            try {
                new DataFetcher(Main.this).execute(new URL("http://recetario.hol.es/selects.php"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
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
        //crearDatos();

        ExpandableListView lista = (ExpandableListView) this.findViewById(R.id.listViewexp);
        adapter = new AdaptadorSeccion(this, secciones);
        lista.setAdapter(adapter);

        Main.this.updateStatus();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if ((resultCode == RESULT_OK) && (requestCode == REQUEST_CODE)) {
            currentUser = new Usuario(data.getExtras().getString("email"), data.getExtras().getString("nombre"), data.getExtras().getString("pass"));
        }
        if ((requestCode == REQUEST_CODE) && (resultCode == -100)) {
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
        if(mostrarFav){
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.mainMenuItemOpt1:
                //Sincronizar
                Main.this.updateStatus();
                break;
            case R.id.mainMenuItemOpt2:
                //Favoritos
                mostrarFav=!mostrarFav;
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
                mostrarFav=false;
                onStop();
                onStart();
                Toast.makeText(this, "Desconectado", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mainMenu2ItemOpt1:
                //Sincronizar
                Main.this.updateStatus();
                break;
            case R.id.mainMenu2ItemOpt2:
                //Intent de login
                Main.this.updateStatus();
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

}