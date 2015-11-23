package es.uvigo.esei.dm1516.p10;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import es.uvigo.esei.dm1516.p10.Model.Receta;
import es.uvigo.esei.dm1516.p10.Model.Usuario;

public class VerReceta extends Activity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_receta);

        TextView tvTitulo = (TextView) this.findViewById(R.id.tvTitulo);
        TextView tvTiempo = (TextView) this.findViewById(R.id.tvTiempo);
        TextView tvNumComensales = (TextView) this.findViewById(R.id.tvComensales);
        TextView tvDificultad = (TextView) this.findViewById(R.id.tvDificultad);
        TextView tvAutor = (TextView) this.findViewById(R.id.tvAutor);
        TextView tvIngredientes = (TextView) this.findViewById(R.id.tvIngredientes);
        TextView tvElaboracion = (TextView) this.findViewById(R.id.tvElaboracion);

        String titulo = (String) this.getIntent().getExtras().get("titulo");
        String tiempo = (String) this.getIntent().getExtras().get("tiempo");
        String numComensales = (String) this.getIntent().getExtras().get("numComensales");
        String dificultad = (String) this.getIntent().getExtras().get("dificultad");
        String autor = (String) this.getIntent().getExtras().get("autor");
        String ingredientes = (String) this.getIntent().getExtras().get("ingredientes");
        String elaboracion = (String) this.getIntent().getExtras().get("elaboracion");

        tvTitulo.setText(titulo);
        tvTiempo.setText(tiempo);
        tvNumComensales.setText(numComensales);
        tvDificultad.setText(dificultad);
        tvAutor.setText(autor);
        tvIngredientes.setText(ingredientes);
        tvElaboracion.setText(elaboracion);
    }

}
