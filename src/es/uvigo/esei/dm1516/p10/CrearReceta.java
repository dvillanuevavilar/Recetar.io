package es.uvigo.esei.dm1516.p10;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.*;
import es.uvigo.esei.dm1516.p10.Core.App;
import es.uvigo.esei.dm1516.p10.Mapper.InsertsConnection;
import es.uvigo.esei.dm1516.p10.Model.Receta;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import static es.uvigo.esei.dm1516.p10.Main.*;

public class CrearReceta extends Activity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_receta);

        Button btGuardar = (Button) this.findViewById(R.id.btGuardar);
        EditText etTitulo = (EditText) this.findViewById(R.id.etTitulo);
        EditText etSeccion = (EditText) this.findViewById(R.id.etSeccion);
        EditText etDificultad = (EditText) this.findViewById(R.id.etDificultad);
        EditText etTiempo = (EditText) this.findViewById(R.id.etTiempo);
        EditText etNumComensales = (EditText) this.findViewById(R.id.etNumComensales);
        EditText etIngredientes = (EditText) this.findViewById(R.id.etIngredientes);
        EditText etElaboracion = (EditText) this.findViewById(R.id.etElaboracion);

        /* Alert Dialog Seccion */
        etSeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] secciones = {"Primer plato", "Segundo plato", "Postre"};
                AlertDialog.Builder dialogSeccion = new AlertDialog.Builder(CrearReceta.this);
                dialogSeccion.setTitle("Selecciona una sección");
                dialogSeccion.setSingleChoiceItems(secciones, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                etSeccion.setText(secciones[0]);
                                break;
                            case 1:
                                etSeccion.setText(secciones[1]);
                                break;
                            case 2:
                                etSeccion.setText(secciones[2]);
                                break;
                        }
                    }
                });
                dialogSeccion.setPositiveButton("Aceptar", null);
                dialogSeccion.create().show();
            }
        });

        /* Alert Dialog Dificultad */
        etDificultad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] dificulades = {" Fácil ", " Medio ", " Dificil "};
                AlertDialog.Builder dialogDificultad = new AlertDialog.Builder(CrearReceta.this);
                dialogDificultad.setTitle("Selecciona un nivel de dificultad");
                dialogDificultad.setSingleChoiceItems(dificulades, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                etDificultad.setText(dificulades[0]);
                                break;
                            case 1:
                                etDificultad.setText(dificulades[1]);
                                break;
                            case 2:
                                etDificultad.setText(dificulades[2]);
                                break;
                        }
                    }
                });
                dialogDificultad.setPositiveButton("Aceptar", null);
                dialogDificultad.create().show();
            }
        });

        /* Crear receta */
        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = etTitulo.getText().toString();
                int tiempo = -1;
                if (etTiempo.getText().toString().length() > 0) {
                    tiempo = Integer.parseInt(etTiempo.getText().toString());
                }
                String dificultad = etDificultad.getText().toString();
                int numComensales = -1;
                if (etNumComensales.getText().toString().length() > 0) {
                    numComensales = Integer.parseInt(etNumComensales.getText().toString());
                }
                String ingredientes = etIngredientes.getText().toString();
                String elaboracion = etElaboracion.getText().toString();
                String seccion = etSeccion.getText().toString();
                String autor = Main.getCurrentUser().getNombre();
                String email = Main.getCurrentUser().getEmail();

                Receta receta = new Receta(0, titulo, tiempo, dificultad, numComensales, ingredientes, elaboracion, autor, seccion);

                if (receta.getTitulo().length() > 0 && receta.getSeccion().length() > 0 && receta.getDificultad().length() > 0
                        && receta.getIngredientes().length() > 0 && receta.getElaboracion().length() > 0
                        && Integer.toString(receta.getTiempo()).length() > 0 && receta.getTiempo() > 0
                        && Integer.toString(receta.getNumComensales()).length() > 0 && receta.getNumComensales() > 0) {

                    //((App) getApplication()).getDb().insertarReceta(receta, email);
                    try {
                        new InsertsConnection("receta", receta, email).execute(new URL("http://recetario.hol.es/insert-receta.php"));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    CrearReceta.this.finish();
                } else {
                    Toast.makeText(CrearReceta.this, "No puede haber campos vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
