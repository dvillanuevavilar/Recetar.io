package es.uvigo.esei.dm1516.p10.Mapper;

import android.app.ProgressDialog;
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
import java.net.URL;

public class DataFetcher extends AsyncTask<String, Void, Boolean> {
    private Main main;
    private ProgressDialog progressDialog;
    private boolean mensaje;

    public DataFetcher(Main mainActivity, boolean mensaje) {
        this.main = mainActivity;
        this.mensaje = mensaje;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mensaje) {
            progressDialog = ProgressDialog.show(main, "Actualizando...", "Espere por favor.");
        }
    }

    @Override
    public Boolean doInBackground(String... email) {
        boolean toret = false;
        InputStream in = null;
        JSONObject jsonObject;
        JSONArray jsonArray_users;
        JSONArray jsonArray_recetas;
        JSONArray jsonArray_favs;

        try {
            URL url = new URL("http://recetario.hol.es/selects.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(2500);
            conn.setConnectTimeout(2500);
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
                usuario = new Usuario(obj.getString("email"), obj.getString("nombre"), null);
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
                        obj.getString("usuario_email"),
                        obj.getString("imagen"));
                mydb.insertarReceta(receta);
            }

            //Insert-favs
            if (!email[0].isEmpty()) {
                jsonArray_favs = jsonObject.getJSONArray("favoritas");
                for (int i = 0; i < jsonArray_favs.length(); i++) {
                    JSONObject obj = jsonArray_favs.getJSONObject(i);
                    if (obj.getString("usuario_email").equals(email[0])) {
                        mydb.insertarFavorita(obj.getString("receta_idReceta"), obj.getString("usuario_email"));
                    }
                }
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
        if (mensaje) {
            progressDialog.dismiss();
        }
        if (aBoolean) {
            if (mensaje) {
                Toast.makeText(main, "Actualizado", Toast.LENGTH_SHORT).show();
            }
            main.updateRecetasList();
        } else if (mensaje) {
            Toast.makeText(main, "Error en actualización", Toast.LENGTH_SHORT).show();
        }
    }
}


