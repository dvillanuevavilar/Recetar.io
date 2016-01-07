package es.uvigo.esei.dm1516.p10;


import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

public class About extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.about);

        ActionBar actBar = this.getActionBar();
        if (actBar != null) {
            actBar.setDisplayHomeAsUpEnabled(true);
        }

        WebView wvView = (WebView) this.findViewById(R.id.webView);
        wvView.loadUrl("http://recetario.hol.es/about.php");
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
