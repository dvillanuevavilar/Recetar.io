package es.uvigo.esei.dm1516.p10.Mapper;

import android.os.AsyncTask;
import android.util.Log;
import es.uvigo.esei.dm1516.p10.Model.Receta;
import es.uvigo.esei.dm1516.p10.Model.Usuario;

import java.io.BufferedOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class InsertsConnection extends AsyncTask<URL, Void, Void> {

    private String tipoInsercion;
    private Usuario user;
    private Receta receta;
    private String email;
    private int idReceta;

    //Constructor para insertar usuario
    public InsertsConnection(String tipoInsercion, Usuario user) {
        this.tipoInsercion = tipoInsercion;
        this.user = user;
    }

    //Constructor para insertar receta
    public InsertsConnection(String tipoInsercion, Receta receta) {
        this.tipoInsercion = tipoInsercion;
        this.receta = receta;
        this.email = email;
    }

    //Constructor para insertar o borrar favorita
    public InsertsConnection(String tipoInsercion, String email, int idReceta) {
        this.email = email;
        this.idReceta = idReceta;
        this.tipoInsercion = tipoInsercion;
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public Void doInBackground(URL... urls) {
        HttpURLConnection con = null;

        try {
            String data = "";
            if (tipoInsercion == "usuario") {
                data = "email=" + URLEncoder.encode(user.getEmail(), "utf-8") + "&nombre=" + URLEncoder.encode(user.getNombre(), "utf-8") +
                        "&contrasenha=" + URLEncoder.encode(user.getContrasenha(), "utf-8");
            } else if (tipoInsercion == "receta") {
                data = "titulo=" + URLEncoder.encode(receta.getTitulo(), "utf-8") +
                        "&tiempo=" + URLEncoder.encode(Integer.toString(receta.getTiempo()), "utf-8") +
                        "&dificultad=" + URLEncoder.encode(receta.getDificultad(), "utf-8") +
                        "&imagen=" +URLEncoder.encode("sdf", "utf-8") +
                        "&numComensales=" +URLEncoder.encode(Integer.toString(receta.getNumComensales()), "utf-8") +
                        "&ingredientes="+ URLEncoder.encode(receta.getIngredientes(), "utf-8") +
                        "&elaboracion=" +URLEncoder.encode(receta.getElaboracion(), "utf-8") +
                        "&seccion=" + URLEncoder.encode(receta.getSeccion(), "utf-8") +
                        "&fecha=" + URLEncoder.encode("0000-00-00", "utf-8") +
                        "&usuario_email=" + URLEncoder.encode(receta.getAutor(), "utf-8") +
                        "&imagen=" + URLEncoder.encode(receta.getImagen(),"utf-8");
            } else if(tipoInsercion == "favorita"){
                data = "receta_idReceta=" + URLEncoder.encode(Integer.toString(idReceta), "utf-8") +
                        "&usuario_email=" + URLEncoder.encode(email, "utf-8");
            }

            con = (HttpURLConnection) urls[0].openConnection();

            //Activar POST
            con.setDoOutput(true);

            //Tamaño previamente conocido
            con.setFixedLengthStreamingMode(data.length());

            // Establecer application/x-www-form-urlencoded debido al formato clave-valor
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            OutputStream out = new BufferedOutputStream(con.getOutputStream());
            out.write(data.getBytes());
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                con.disconnect();
        }

        return null;
    }

    @Override
    public void onPostExecute(Void result) {

    }
}
