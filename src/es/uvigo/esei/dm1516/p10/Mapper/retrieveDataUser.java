package es.uvigo.esei.dm1516.p10.Mapper;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import es.uvigo.esei.dm1516.p10.Login;
import es.uvigo.esei.dm1516.p10.Main;
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

public class retrieveDataUser extends AsyncTask<String, Void, Boolean> {
    private Login login;
    private Main main;
    private Usuario currentUser;
    private ProgressDialog progressDialog;


    public retrieveDataUser(Login activity) {
        this.login = activity;
    }

    public retrieveDataUser(Main activity) {
        this.main = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (main != null) {
            progressDialog = ProgressDialog.show(main, "Iniciando sesión...", "Espere por favor.");
        } else {
            progressDialog = ProgressDialog.show(login, "Iniciando sesión...", "Espere por favor.");
        }
    }

    @Override
    public Boolean doInBackground(String... userToSearch) {
        boolean toret = false;
        InputStream in = null;
        JSONObject jsonObject;
        JSONArray jsonArray_users;

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
            jsonObject = new JSONObject(this.getStringFromStream(in));

            //Search user
            jsonArray_users = jsonObject.getJSONArray("usuarios");

            for (int i = 0; i < jsonArray_users.length() && !toret; i++) {
                JSONObject obj = jsonArray_users.getJSONObject(i);

                if (obj.getString("email").equals(userToSearch[0])) {
                    if (obj.getString("contrasenha").equals(userToSearch[1])) {
                        currentUser = new Usuario(obj.getString("email"), obj.getString("nombre"), obj.getString("contrasenha"));
                        toret = true;
                    } else {
                        in.close();
                        return false;
                    }
                }
            }

        } catch (IOException exc) {
            Log.d("retrieveDataUser", "error i/o " + exc.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException exc) {
                    Log.d("retrieveDataUser", "Impossible to close server data");
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
            Log.e("retrieveDataUser", "Impossible convert stream to string");
        }
        return toret.toString();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        progressDialog.dismiss();
        if (login != null) {
            if (aBoolean) {
                login.loginUser(currentUser);
            } else {
                login.loginUser(null);
            }
        } else {
            main.setCurrentUser(currentUser);
        }
    }
}

