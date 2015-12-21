package es.uvigo.esei.dm1516.p10.Mapper;

import android.os.AsyncTask;
import android.util.Log;
import es.uvigo.esei.dm1516.p10.Core.App;
import es.uvigo.esei.dm1516.p10.Main;
import es.uvigo.esei.dm1516.p10.Model.Receta;
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
    private String time;

    public DataFetcher(Main mainActivity) {
        this.main = mainActivity;
    }

    @Override
    public Boolean doInBackground(URL... url) {
        boolean toret = false;
        InputStream is = null;

        try {
            HttpURLConnection conn = (HttpURLConnection) url[0].openConnection();
            conn.setReadTimeout(1000 /* millisegundos */);
            conn.setConnectTimeout(1000 /* millisegundos */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // Obtener la respuesta del servidor
            conn.connect();
            int codigoRespuesta = conn.getResponseCode();
            is = conn.getInputStream();

            JSONObject jsonObject = new JSONObject(this.getStringFromStream(is));
            JSONArray jsonArray = jsonObject.getJSONArray("recetas");

            Receta receta ;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                receta = new Receta(0, obj.getString("titulo"),
                        Integer.valueOf(obj.getString("tiempo")),
                        obj.getString("dificultad"),
                        Integer.valueOf(obj.getString("numComensales")), "aaaaa", "bbb", "autor", "sections");
                ((App) main.getApplication()).getDb().insertarReceta(receta, "rosa@receta.es");
            }

            Log.d("json", String.valueOf(jsonArray.length()));
            toret = true;
        } catch (ProtocolException exc) {
            Log.d("TimeFetcher", "error i/o " + exc.getMessage());
        } catch (IOException exc) {
            Log.d("TimeFetcher", "error i/o " + exc.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException exc) {
                    Log.d("TimeFetcher", "Impossible to close server data");
                }
            }
        }

        return toret;
    }

    private String getStringFromStream(InputStream is) {
        StringBuilder toret = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                toret.append(line);
            }
        } catch (IOException exc) {
            Log.e("Main", "Impossible convert stream to string");
        }
        return toret.toString();
    }

    /*@Override
    public void onPostExecute(Boolean result) {
        this.main.setTime(this.time);
        this.main.setStatus("Connection OK");
    }

    @Override
    public void onPreExecute() {
        this.main.setStatus("Connecting...");
    }*/

}


