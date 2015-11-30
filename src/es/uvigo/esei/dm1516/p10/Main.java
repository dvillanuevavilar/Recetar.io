package es.uvigo.esei.dm1516.p10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import es.uvigo.esei.dm1516.p10.Core.App;
import es.uvigo.esei.dm1516.p10.Core.SqlIO;
import es.uvigo.esei.dm1516.p10.Model.Receta;
import es.uvigo.esei.dm1516.p10.Model.Usuario;

import java.util.ArrayList;

public class Main extends Activity {
    private SparseArray<GrupoDeItems> secciones;
    private AdaptadorSeccion adapter;
    private Usuario currentUser = new Usuario("usuario@example.com","usuarioPrueba","abc123.");


    public void crearDatos() {
        Receta rc1 = new Receta(0, "Tortilla francesa", 5, "Facil", 2, "Huevos", "Batir y freir", "Juan", "Primer plato");
        Receta rc2 = new Receta(0, "Tortilla espa\u00f1ola", 6, "Facil", 2, "Huevos y patatas", "Batir, pelar y freir", "Rosa", "Primer plato");
        Receta rc3 = new Receta(0, "Carne asada", 3, "Media", 5, "Carne", "Asar la carne", "Juan", "Segundo plato");
        Receta rc4 = new Receta(0, "Pollo asado", 2, "Media", 4, "Pollo", "Asar el pollo", "Rosa", "Segundo plato");
        Receta rc5 = new Receta(0, "Tarta de queso", 1, "Dificil", 2, "Queso y tarta", "Abrir la nevera", "Rosa", "Postre");
        Receta rc6 = new Receta(0, "Tarta helada", 3, "Dificil", 3, "Tarta y hielo", "Abrir el congelador", "Juan", "Postre");

        Usuario usr1 = new Usuario("juan@receta.es", "Juan Rodr�guez", "abc123.");
        Usuario usr2 = new Usuario("rosa@receta.es", "Rosa Lois", "abc123.");

        if(!((App) this.getApplication()).getDb().existeUsuario(usr1.getEmail())){
            ((App) this.getApplication()).getDb().insertarUsuario(usr1);
        }
        if(!((App) this.getApplication()).getDb().existeUsuario(usr2.getEmail())){
            ((App) this.getApplication()).getDb().insertarUsuario(usr2);
        }

        if(!((App) this.getApplication()).getDb().existeReceta(rc1.getIdReceta())){
            ((App) this.getApplication()).getDb().insertarReceta(rc1,usr1.getEmail());
        }
        if(!((App) this.getApplication()).getDb().existeReceta(rc2.getIdReceta())){
            ((App) this.getApplication()).getDb().insertarReceta(rc2,usr1.getEmail());
        }
        if(!((App) this.getApplication()).getDb().existeReceta(rc3.getIdReceta())){
            ((App) this.getApplication()).getDb().insertarReceta(rc3,usr1.getEmail());
        }
        if(!((App) this.getApplication()).getDb().existeReceta(rc4.getIdReceta())){
            ((App) this.getApplication()).getDb().insertarReceta(rc4,usr2.getEmail());
        }
        if(!((App) this.getApplication()).getDb().existeReceta(rc5.getIdReceta())){
            ((App) this.getApplication()).getDb().insertarReceta(rc5,usr2.getEmail());
        }
        if(!((App) this.getApplication()).getDb().existeReceta(rc6.getIdReceta())){
            ((App) this.getApplication()).getDb().insertarReceta(rc6,usr2.getEmail());
        }

        /*GrupoDeItems primeros = new GrupoDeItems("Primer Plato");
        primeros.add(rc1);
        primeros.add(rc2);
        secciones.append(0, primeros);
        GrupoDeItems segundos = new GrupoDeItems("Segundo Plato");
        segundos.add(rc3);
        segundos.add(rc4);
        secciones.append(1, segundos);
        GrupoDeItems postres = new GrupoDeItems("Postre");
        postres.add(rc5);
        postres.add(rc6);
        secciones.append(2, postres);*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        secciones = new SparseArray<GrupoDeItems>();

        Intent intentVerReceta = new Intent(Main.this, VerReceta.class);
        Intent intentLogin = new Intent(Main.this, Login.class);
        Main.this.startActivity(intentLogin);

        //crearDatos();

        ExpandableListView lista = (ExpandableListView) this.findViewById(R.id.listViewexp);
        adapter = new AdaptadorSeccion(this, secciones);
        lista.setAdapter(adapter);
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onStart(){
        super.onStart();
        GrupoDeItems primeros = new GrupoDeItems("Primer Plato");
        GrupoDeItems segundos = new GrupoDeItems("Segundo Plato");
        GrupoDeItems postres = new GrupoDeItems("Postre");


        ArrayList<Receta> recetas = ((App) this.getApplication()).getDb().listarRecetas();
        for(Receta r : recetas){
            if(r.getSeccion().equals("Primer plato")){
                primeros.add(r);
            }else if(r.getSeccion().equals("Segundo plato")){
                segundos.add(r);
            }else {
                postres.add(r);
            }
        }
        secciones.append(0, primeros);
        secciones.append(1, segundos);
        secciones.append(2, postres);
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
                break;
            case R.id.mainMenuItemOpt2:
                Intent intentLogin = new Intent(Main.this, Login.class);
                Main.this.startActivity(intentLogin);
                break;
            case R.id.mainMenuItemOpt3:
                Intent intentCrearReceta = new Intent(Main.this, CrearReceta.class);
                Main.this.startActivity(intentCrearReceta);
                break;
        }
        return true;
    }
}
