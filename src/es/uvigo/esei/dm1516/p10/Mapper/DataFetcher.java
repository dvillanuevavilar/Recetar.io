package es.uvigo.esei.dm1516.p10.Mapper;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import es.uvigo.esei.dm1516.p10.Core.App;
import es.uvigo.esei.dm1516.p10.Core.SqlIO;
import es.uvigo.esei.dm1516.p10.Main;
import es.uvigo.esei.dm1516.p10.Model.Receta;
import es.uvigo.esei.dm1516.p10.Model.Usuario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;

public class DataFetcher extends AsyncTask<URL, Void, Boolean> {
    private Main main;

    public DataFetcher(Main mainActivity) {
        this.main = mainActivity;
    }

    @Override
    public Boolean doInBackground(URL... url) {
        boolean toret = false;
        InputStream in = null;
        JSONObject jsonObject;
        JSONArray jsonArray_users;
        JSONArray jsonArray_recetas;
        JSONArray jsonArray_favs;

        try {
            HttpURLConnection conn = (HttpURLConnection) url[0].openConnection();
            conn.setReadTimeout(1500);
            conn.setConnectTimeout(1500);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            //Connect to server
            conn.connect();
            in = conn.getInputStream();

            //Update SQLite
            SqlIO mydb = ((App) main.getApplication()).getDb();
            mydb.burnData();
            jsonObject = new JSONObject(this.getStringFromStream(in));

            //Insert-users
            jsonArray_users = jsonObject.getJSONArray("usuarios");
            Usuario usuario;
            for (int i = 0; i < jsonArray_users.length(); i++) {
                JSONObject obj = jsonArray_users.getJSONObject(i);
                usuario = new Usuario(obj.getString("email"), obj.getString("nombre"), obj.getString("contrasenha"));
                mydb.insertarUsuario(usuario);
            }

            //Insert-recetas
            jsonArray_recetas = jsonObject.getJSONArray("recetas");
            Receta receta;
            for (int i = 0; i < jsonArray_recetas.length(); i++) {
                JSONObject obj = jsonArray_recetas.getJSONObject(i);
                receta = new Receta(Integer.valueOf(obj.getString("idReceta")),
                        obj.getString("titulo"),
                        Integer.valueOf(obj.getString("tiempo")),
                        obj.getString("dificultad"),
                        Integer.valueOf(obj.getString("numComensales")),
                        obj.getString("ingredientes"),
                        obj.getString("elaboracion"),
                        obj.getString("seccion"),
                        obj.getString("usuario_email"));
                mydb.insertarReceta(receta);
            }

            //Insert-favs
            jsonArray_favs = jsonObject.getJSONArray("favoritas");
            for (int i = 0; i < jsonArray_favs.length(); i++) {
                JSONObject obj = jsonArray_favs.getJSONObject(i);
                mydb.insertarFavorita(obj.getString("receta_idReceta"), obj.getString("usuario_email"));
            }

            toret = true;
        } catch (IOException exc) {
            Log.d("DataFetcher", "error i/o " + exc.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException exc) {
                    Log.d("DataFetcher", "Impossible to close server data");
                }
            }
        }

        return toret;
    }

    private String getStringFromStream(InputStream in) {
        StringBuilder toret = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                toret.append(line);
            }
        } catch (IOException exc) {
            Log.e("DataFetcher", "Impossible convert stream to string");
        }
        return toret.toString();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean) {
            Toast.makeText(main, "Actualizado", Toast.LENGTH_SHORT).show();
            main.onStart();
        } else
            Toast.makeText(main, "Error en actualización", Toast.LENGTH_SHORT).show();
    }
}


