package es.uvigo.esei.dm1516.p10.Core;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import es.uvigo.esei.dm1516.p10.Model.Receta;
import es.uvigo.esei.dm1516.p10.Model.Usuario;

import java.sql.Blob;
import java.util.ArrayList;

/**
 * Created by Diego on 26/11/2015.
 */
public class SqlIO extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "recetario_db";
    public static int DATABASE_VERSION = 3;

    public SqlIO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*@Override
    public void onOpen(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL("DELETE FROM favoritas WHERE usuario IS NULL OR receta IS NULL");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return;
    }*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS usuario("
                    + "email string(30) PRIMARY KEY,"
                    + "nombre string(45) NOT NULL,"
                    + "contrasenha string(20) NOT NULL"
                    + ")");
            db.execSQL("CREATE TABLE IF NOT EXISTS receta("
                    + "idReceta string(10) PRIMARY KEY,"
                    + "titulo string(50) NOT NULL,"
                    + "tiempo int NOT NULL,"
                    + "dificultad string(15) NOT NULL,"
                    + "numComensales int NOT NULL,"
                    + "ingredientes text NOT NULL,"
                    + "elaboracion text NOT  NULL,"
                    + "autor string(20) NOT NULL,"
                    + "seccion string(20) NOT NULL,"
                    //+ "imagen blob,"
                    + "usuario_email string(30) NOT NULL,"
                    + "FOREIGN KEY(usuario_email) REFERENCES usuario(email)"
                    + ")");

            db.execSQL("CREATE TABLE IF NOT EXISTS favoritas("
                    + "receta_idReceta string(10),"
                    + "usuario_email string(30),"
                    + "PRIMARY KEY(receta_idReceta, usuario_email),"
                    + "FOREIGN KEY(receta_idReceta) REFERENCES receta(idReceta)"
                    + "FOREIGN KEY(usuario_email) REFERENCES usuario(email)"
                    + ")");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.beginTransaction();
            //db.execSQL("DROP DATABASE IF EXISTS recetario");
            db.execSQL("DROP TABLE IF EXISTS favoritas");
            db.execSQL("DROP TABLE IF EXISTS receta");
            db.execSQL("DROP TABLE IF EXISTS usuario");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        this.onCreate(db);
    }

    public boolean existeReceta(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM receta WHERE idReceta=?",
                new String[]{id});
        if (cursor.moveToFirst()) {
            return true;
        } else
            return false;
    }

    public ArrayList<Receta> listarRecetas() {
        ArrayList<Receta> lista = new ArrayList<>();
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM receta", null);
        if (cursor.moveToFirst()) {
            do {
                lista.add(new Receta(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3),
                        cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8)));
            } while (cursor.moveToNext());
        }

        return lista;
    }

    public Receta recetaPorId(String id) {
        Receta receta = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM receta WHERE idReceta = ?",
                new String[]{id});

        if(cursor.moveToFirst()){
            receta = new Receta(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3),
                    cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
        }
        return receta;
    }

    /*public byte[] imagenPorIdReceta(String id){
        byte[] imagen = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT imagen FROM receta WHERE idReceta = ?",
                new String[]{id});

        if(cursor.moveToFirst()){
            imagen = cursor.getBlob(0);
        }
        return imagen;
        *//*Para luego recuperar la imagen
        byte[] blob=c.getBlob("yourcolumnname");
        Bitmap bmp=BitmapFactory.decodeByteArray(blob,0,blob.length);
        ImageView image=new ImageView(this);
        image.setImageBitmap(bmp);*//*
    }*/

    public void insertarReceta(Receta receta, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("INSERT INTO receta(idReceta,titulo,tiempo,dificultad,numComensales,ingredientes,elaboracion,autor,seccion,usuario_email) VALUES (?,?,?,?,?,?,?,?,?,?)",
                    new String[]{receta.getIdReceta(),receta.getTitulo(), Integer.toString(receta.getTiempo()), receta.getDificultad(), Integer.toString(receta.getNumComensales()),
                            receta.getIngredientes(),receta.getElaboracion(),receta.getAutor(),receta.getSeccion(),email});
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
        return;
    }

    public boolean existeUsuario(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuario WHERE email=?",
                new String[]{email});
        if (cursor.moveToFirst()) {
            return true;
        } else
            return false;
    }

    public boolean comprobarLogin(String email, String contrasenha){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuario WHERE email=? and contrasenha=?",
                new String[]{email,contrasenha});
        if (cursor.moveToFirst()) {
            return true;
        } else
            return false;
    }

    public void insertarUsuario(Usuario user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("INSERT INTO usuario(email, nombre, contrasenha) VALUES(?,?,?)",
                    new String[] {user.getEmail(), user.getNombre(), user.getContrasenha()});
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
        return;
    }

    public void marcarFavorita(String email, String idReceta){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("INSERT INTO favoritas(receta_idReceta, usuario_email ) VALUES(?,?)",
                    new String[] {idReceta, email});
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
        return;
    }

}
