package es.uvigo.esei.dm1516.p10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.*;

public class Main extends Activity {
    SparseArray<GrupoDeItems> secciones = new SparseArray<GrupoDeItems>();

    public void crearDatos() {
        GrupoDeItems grupo0 = new GrupoDeItems("Postres");
        grupo0.children.add("Tarta de fresa");
        grupo0.children.add("Bizcocho");
        secciones.append(0, grupo0);
        GrupoDeItems grupo1 = new GrupoDeItems("Primeros");
        grupo1.children.add("Sopa");
        grupo1.children.add("Caldo");
        grupo1.children.add("Ensaladilla");
        secciones.append(1, grupo1);
        GrupoDeItems grupo2 = new GrupoDeItems("Segundos");
        grupo2.children.add("Jamon, queso y anana");
        grupo2.children.add("Pollo, morrones y aceitunas");
        grupo2.children.add("Carlitos");
        secciones.append(2, grupo2);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        Intent intentLogin = new Intent(Main.this, Login.class);
        Main.this.startActivity(intentLogin);

        crearDatos();

        ExpandableListView lista = (ExpandableListView) this.findViewById(R.id.listViewexp);
        AdaptadorSeccion adapter = new AdaptadorSeccion(this,secciones);
        lista.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){

            case R.id.mainMenuItemOpt1:
                break;
            case R.id.mainMenuItemOpt2:
                Intent intentLogin = new Intent(Main.this, Login.class);
                Main.this.startActivity(intentLogin);
                break;
        }
        return true;
    }
}
