package es.uvigo.esei.dm1516.p10;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.*;
import es.uvigo.esei.dm1516.p10.Core.App;
import es.uvigo.esei.dm1516.p10.Mapper.InsertsConnection;
import es.uvigo.esei.dm1516.p10.Model.Receta;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Blob;
import java.util.Calendar;

import static es.uvigo.esei.dm1516.p10.Main.*;

public class CrearReceta extends Activity {
    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;
    private Bitmap imagen;

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

        //Imagen

        Button btnGal = (Button) CrearReceta.this.findViewById(R.id.idImgGal);
        Button btnCam = (Button) CrearReceta.this.findViewById(R.id.idImgCam);

        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PICTURE);
            }
        });

        btnGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_PICTURE);
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
                String email = Main.getCurrentUser().getEmail();

                Receta receta = new Receta(0, titulo, tiempo, dificultad, numComensales, ingredientes, elaboracion, seccion, email, BitMapToString(imagen));

                if (receta.getTitulo().length() > 0 && receta.getSeccion().length() > 0 && receta.getDificultad().length() > 0
                        && receta.getIngredientes().length() > 0 && receta.getElaboracion().length() > 0
                        && Integer.toString(receta.getTiempo()).length() > 0 && receta.getTiempo() > 0
                        && Integer.toString(receta.getNumComensales()).length() > 0 && receta.getNumComensales() > 0
                        && receta.getImagen().length() > 0) {

                    try {
                        new InsertsConnection("receta", receta).execute(new URL("http://recetario.hol.es/insert-receta.php"));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    CrearReceta.this.setResult(-100);
                    CrearReceta.this.finish();
                } else {
                    Toast.makeText(CrearReceta.this, "No puede haber campos vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE) {
            if (data != null) {
                ImageView iv = (ImageView) CrearReceta.this.findViewById(R.id.idImgView);
                iv.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
                imagen = (Bitmap) data.getParcelableExtra("data");
            }
        } else if (requestCode == SELECT_PICTURE) {
            ImageView iv = (ImageView) CrearReceta.this.findViewById(R.id.idImgView);
            if (data != null) {
                Uri selectedImage = data.getData();
                InputStream is;
                try {
                    is = getContentResolver().openInputStream(selectedImage);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    Bitmap bitmap = BitmapFactory.decodeStream(bis);
                    iv.setImageBitmap(bitmap);
                    imagen = bitmap;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        } else {
            return "error de imagen";
        }
    }
}
