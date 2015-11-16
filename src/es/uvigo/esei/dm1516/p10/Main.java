package es.uvigo.esei.dm1516.p10;

import android.app.Activity;
import android.content.pm.FeatureInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        TextView tvRegistro = (TextView) this.findViewById(R.id.tvRegistro);

        tvRegistro.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(getApplicationContext(), "Entro", Toast.LENGTH_LONG).show();
                Main.this.getLayoutInflater().inflate(R.layout.register, v);
                return false;
            }
        });


    }
}
