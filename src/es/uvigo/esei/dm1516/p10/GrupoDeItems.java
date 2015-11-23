package es.uvigo.esei.dm1516.p10;

import java.util.ArrayList;
import java.util.List;

public class GrupoDeItems {
    private String nombreGrupo;
    private int countRecetas;
    public final List<String> children = new ArrayList<String>();

    public GrupoDeItems(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public String getNombreGrupo(){
        return nombreGrupo;
    }

    public Integer getCountRecetas(){
        return children.size();
    }
}
