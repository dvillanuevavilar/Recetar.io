package es.uvigo.esei.dm1516.p10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import es.uvigo.esei.dm1516.p10.Mapper.retrieveDataUser;
import es.uvigo.esei.dm1516.p10.Model.Usuario;

import java.util.ArrayList;

public class Login extends Activity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        EditText etCorreo = (EditText) this.findViewById(R.id.etCorreoL);
        EditText etPass = (EditText) this.findViewById(R.id.etPwdL);
        Button btnLogin = (Button) this.findViewById(R.id.btIniciar);
        TextView tvRegistro = (TextView) this.findViewById(R.id.tvRegistro);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> userToSearch = new ArrayList<String>(2);
                userToSearch.add(etCorreo.getText().toString());
                userToSearch.add(etPass.getText().toString());
                new retrieveDataUser(Login.this).execute(userToSearch);
            }
        });

        tvRegistro.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ConnectivityManager connMgr = (ConnectivityManager) Login.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    Intent intentRegistro = new Intent(Login.this, Registro.class);
                    Login.this.startActivity(intentRegistro);
                } else {
                    Toast.makeText(Login.this, "Necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    public void loginUser(Usuario currentUser){
        if (currentUser != null) {
            Intent data = new Intent();
            data.putExtra("email", currentUser.getEmail());
            data.putExtra("nombre", currentUser.getNombre());
            data.putExtra("pass", currentUser.getContrasenha());
            setResult(RESULT_OK, data);
            Toast.makeText(getApplicationContext(), "Logueado correctamente", Toast.LENGTH_SHORT).show();
            Login.this.finish();
        } else {
            Toast.makeText(getApplicationContext(), "Usuario no v\u00e1lido", Toast.LENGTH_SHORT).show();
        }
    }
}
