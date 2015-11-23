package es.uvigo.esei.dm1516.p10;

import java.util.ArrayList;
import java.util.List;

public class GrupoDeItems {
    private String nombreGrupo;
    private int countRecetas;
    private final List<Receta> children = new ArrayList<Receta>();

    public GrupoDeItems(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public String getNombreGrupo(){
        return nombreGrupo;
    }

    public Integer getCountRecetas(){
        return children.size();
    }

    public void add(Receta rec){
        children.add(rec);
    }

    public Object get(int pos){
        return children.get(pos);
    }

    public int size(){
        return children.size();
    }
}
