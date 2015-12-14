package es.uvigo.esei.dm1516.p10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import es.uvigo.esei.dm1516.p10.Core.App;

public class Login extends Activity{
    private String comprobarLogin(String email, String pass){
        if(((App) this.getApplication()).getDb().comprobarLogin(email, pass)){
            return ((App) this.getApplication()).getDb().obtenerNombre(email);
        }else {
            return "false";
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        EditText etCorreo = (EditText) this.findViewById(R.id.etCorreoL);
        EditText etPass = (EditText) this.findViewById(R.id.etPwdL);

        Button btnLogin = (Button) this.findViewById(R.id.btIniciar);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!comprobarLogin(etCorreo.getText().toString(), etPass.getText().toString()).equals("false")){
                    String n =comprobarLogin(etCorreo.getText().toString(), etPass.getText().toString());
                    Intent data = new Intent();
                    data.putExtra("email", etCorreo.getText().toString());
                    data.putExtra("nombre", n);
                    data.putExtra("pass", etPass.getText().toString());
                    setResult(RESULT_OK, data);
                    Toast.makeText(getApplicationContext(),"Logueado correctamente",Toast.LENGTH_SHORT).show();
                    Login.this.finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Usuario no v\u00e1lido",Toast.LENGTH_SHORT).show();
                }

            }
        });

        TextView tvRegistro = (TextView) this.findViewById(R.id.tvRegistro);
        tvRegistro.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intentRegistro = new Intent(Login.this, Registro.class);
                Login.this.startActivity(intentRegistro);
                return false;
            }
        });
    }
}
