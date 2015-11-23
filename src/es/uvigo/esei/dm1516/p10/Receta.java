package es.uvigo.esei.dm1516.p10;

import android.text.format.Time;

/**
 * Created by diego on 23/11/15.
 */
public class Receta {
    private String idReceta;
    private String titulo;
    private Time tiempo;
    private String dificultad;
    private int numComensales;
    private String ingredientes;
    private String elaboracion;
    private String autor;
    private String seccion;


    public Receta() {
    }

    public Receta(String idReceta, String titulo, Time tiempo, String dificultad, int numComensales, String ingredientes, String elaboracion, String autor, String seccion) {
        this.idReceta = idReceta;
        this.titulo = titulo;
        this.tiempo = tiempo;
        this.dificultad = dificultad;
        this.numComensales = numComensales;
        this.ingredientes = ingredientes;
        this.elaboracion = elaboracion;
        this.autor = autor;
        this.seccion = seccion;
    }

    public String getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(String idReceta) {
        this.idReceta = idReceta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Time getTiempo() {
        return tiempo;
    }

    public void setTiempo(Time tiempo) {
        this.tiempo = tiempo;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public int getNumComensales() {
        return numComensales;
    }

    public void setNumComensales(int numComensales) {
        this.numComensales = numComensales;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getElaboracion() {
        return elaboracion;
    }

    public void setElaboracion(String elaboracion) {
        this.elaboracion = elaboracion;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }
}
