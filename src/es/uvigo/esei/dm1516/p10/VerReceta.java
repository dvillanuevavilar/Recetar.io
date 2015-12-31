package es.uvigo.esei.dm1516.p10;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.*;
import es.uvigo.esei.dm1516.p10.Core.App;
import es.uvigo.esei.dm1516.p10.Mapper.InsertsConnection;

import java.net.MalformedURLException;
import java.net.URL;

public class VerReceta extends Activity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.view_receta);

        ActionBar actBar = this.getActionBar();
        if (actBar != null) {
            actBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView tvTitulo = (TextView) this.findViewById(R.id.tvTitulo);
        TextView tvTiempo = (TextView) this.findViewById(R.id.tvTiempo);
        TextView tvNumComensales = (TextView) this.findViewById(R.id.tvComensales);
        TextView tvDificultad = (TextView) this.findViewById(R.id.tvDificultad);
        TextView tvAutor = (TextView) this.findViewById(R.id.tvAutor);
        TextView tvIngredientes = (TextView) this.findViewById(R.id.tvIngredientes);
        TextView tvElaboracion = (TextView) this.findViewById(R.id.tvElaboracion);
        ToggleButton btnFav = (ToggleButton) this.findViewById(R.id.btnFav);
        ImageView imageView = (ImageView) this.findViewById(R.id.idImgVista);

        String titulo = (String) this.getIntent().getExtras().get("titulo");
        String tiempo = (String) this.getIntent().getExtras().get("tiempo");
        String numComensales = (String) this.getIntent().getExtras().get("numComensales");
        String dificultad = (String) this.getIntent().getExtras().get("dificultad");
        String autor = (String) this.getIntent().getExtras().get("autor");
        String ingredientes = (String) this.getIntent().getExtras().get("ingredientes");
        String elaboracion = (String) this.getIntent().getExtras().get("elaboracion");
        int idReceta = (int) this.getIntent().getExtras().get("idReceta");
        String imagen = ((App) getApplication()).getDb().imagenPorReceta(idReceta);


        if (Main.getCurrentUser() != null) {
            if (((App) getApplication()).getDb().comprobarFavorita(Main.getCurrentUser().getEmail(), idReceta)) {
                btnFav.setChecked(true);
            } else {
                btnFav.setChecked(false);
            }
        }

        tvTitulo.setText(titulo);
        tvTiempo.setText(tiempo);
        tvNumComensales.setText(numComensales);
        tvDificultad.setText(dificultad);
        tvAutor.setText(autor);
        tvIngredientes.setText(ingredientes);
        tvElaboracion.setText(elaboracion);
        imageView.setImageBitmap(StringToBitMap(imagen));

        btnFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (Main.getCurrentUser() != null) {
                    String emailActual = Main.getCurrentUser().getEmail();
                    ConnectivityManager connMgr = (ConnectivityManager) VerReceta.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        try {
                            new InsertsConnection("favorita", emailActual, idReceta).execute(new URL("http://recetario.hol.es/insert-fav.php"));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(VerReceta.this, "Necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                        btnFav.setChecked(false);
                    }
                } else {
                    Toast.makeText(VerReceta.this, "Necesitas estar logueado", Toast.LENGTH_SHORT).show();
                    btnFav.setChecked(false);
                }
            }
        });
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return true;
    }

}
