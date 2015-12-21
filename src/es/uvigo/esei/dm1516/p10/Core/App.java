package es.uvigo.esei.dm1516.p10.Core;

import android.app.Application;

public class App extends Application {
    private SqlIO sqlDb;

    @Override
    public void onCreate() {
        super.onCreate();

        this.sqlDb = new SqlIO( this.getApplicationContext() );
    }

    public SqlIO getDb() {
        return this.sqlDb;
    }

}
