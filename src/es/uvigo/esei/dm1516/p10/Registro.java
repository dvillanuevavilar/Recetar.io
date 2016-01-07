package es.uvigo.esei.dm1516.p10;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import es.uvigo.esei.dm1516.p10.Core.App;
import es.uvigo.esei.dm1516.p10.Mapper.InsertsConnection;
import es.uvigo.esei.dm1516.p10.Model.Usuario;

import java.net.MalformedURLException;
import java.net.URL;

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

                if (etPass.getText().toString().equals(etPass2.getText().toString())) {
                    if (etPass.getText().toString().length() < 5) {
                        Toast.makeText(getApplicationContext(), R.string.lenghtPass, Toast.LENGTH_SHORT).show();
                    } else if (!Usuario.validateEmail(etCorreo.getText().toString())) {
                        Toast.makeText(getApplicationContext(), R.string.incorrectEmail, Toast.LENGTH_SHORT).show();
                    } else if (etNombre.getText().toString().length() < 1) {
                        Toast.makeText(getApplicationContext(), R.string.fieldNotEmpty, Toast.LENGTH_SHORT).show();
                    } else {
                        Usuario usuario = new Usuario(etCorreo.getText().toString(), etNombre.getText().toString(), etPass.getText().toString());
                        if (Registro.this.registrarUsuario(usuario)) {
                            Toast.makeText(getApplicationContext(), R.string.registerCorrect, Toast.LENGTH_SHORT).show();
                            Registro.this.finish();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.userExists, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.passNotEquals, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean registrarUsuario(Usuario user) {
        if (!((App) this.getApplication()).getDb().existeUsuario(user.getEmail())) {
            try {
                new InsertsConnection("usuario", user).execute(new URL("http://recetario.hol.es/insert-user.php"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }
}
