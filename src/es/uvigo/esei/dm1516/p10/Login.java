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

public class Login extends Activity {
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        EditText etCorreo = (EditText) this.findViewById(R.id.etCorreoL);
        EditText etPass = (EditText) this.findViewById(R.id.etPwdL);

        Button btnLogin = (Button) this.findViewById(R.id.btIniciar);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se enviaran los datos para comprobar si son correctos
                Login.this.finish();
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
