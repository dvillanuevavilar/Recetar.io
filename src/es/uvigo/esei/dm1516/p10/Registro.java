package es.uvigo.esei.dm1516.p10;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Registro extends Activity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Registro.this.setContentView(R.layout.register);

        EditText etNombre = (EditText) this.findViewById(R.id.etNombreR);
        EditText etCorreo = (EditText) this.findViewById(R.id.etCorreoR);
        EditText etPass = (EditText) this.findViewById(R.id.etPwdR);
        EditText etPass2 = (EditText) this.findViewById(R.id.etPwdR2);

        Button btnRegistro = (Button) this.findViewById(R.id.btRegistrar);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se comprobaran los datos y se enviarán a la base de datos luego se finaliza la actividad para volver a la principal
                Registro.this.finish();
            }
        });
    }
}
