package es.uvigo.esei.dm1516.p10;

import android.app.Activity;
import android.content.Intent;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import es.uvigo.esei.dm1516.p10.Model.Receta;

public class AdaptadorSeccion extends BaseExpandableListAdapter {
    private final SparseArray<GrupoDeItems> grupos;
    public LayoutInflater inflater;
    public Activity activity;

    // Constructor
    public AdaptadorSeccion(Activity act, SparseArray<GrupoDeItems> grupos) {
        activity = act;
        this.grupos = grupos;
        inflater = act.getLayoutInflater();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.items, parent, false);
        }
        GrupoDeItems grupo = (GrupoDeItems) getGroup(groupPosition);
        TextView tvSeccion = (TextView) convertView.findViewById(R.id.tvTituloItem);
        TextView tvCountRecetas = (TextView) convertView.findViewById(R.id.tvCountRecetas);

        tvSeccion.setText(grupo.getNombreGrupo());
        tvCountRecetas.setText(String.valueOf(grupo.getCountRecetas()));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Receta children = (Receta) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.subitems, parent, false);
        }

        TextView tvTitulo = (TextView) convertView.findViewById(R.id.tvTituloSubItem);
        TextView tvTiempo = (TextView) convertView.findViewById(R.id.tvTiempoSubitems);
        TextView tvDificultad = (TextView) convertView.findViewById(R.id.tvDificultadSubitems);
        TextView tvNumComensales = (TextView) convertView.findViewById(R.id.tvNumComensalesSubItems);
        TextView tvAutor = (TextView) convertView.findViewById(R.id.tvAutorSuItems);

        tvTitulo.setText(children.getTitulo());
        tvTiempo.setText(String.valueOf(children.getTiempo()));
        tvDificultad.setText(children.getDificultad());
        tvNumComensales.setText(String.valueOf(children.getNumComensales()));
        tvAutor.setText(children.getAutor());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Seleccionado id "+ children.getIdReceta(), Toast.LENGTH_SHORT).show();
                Intent intentVerReceta = new Intent("intent.action.VIEWRECETA");
                intentVerReceta.putExtra("titulo", children.getTitulo());
                intentVerReceta.putExtra("tiempo", String.valueOf(children.getTiempo()));
                intentVerReceta.putExtra("numComensales", String.valueOf(children.getNumComensales()));
                intentVerReceta.putExtra("dificultad", children.getDificultad());
                intentVerReceta.putExtra("autor", children.getAutor());
                intentVerReceta.putExtra("ingredientes", children.getIngredientes());
                intentVerReceta.putExtra("elaboracion", children.getElaboracion());
                activity.startActivity(intentVerReceta);
            }
        });
        return convertView;
    }

    // Nos devuelve los datos asociados a un subitem en base
    // a la posición
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return grupos.get(groupPosition).get(childPosition);
    }

    // Devuelve el id de un item o subitem en base a la
    // posición de item y subitem
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    // Nos devuelve la cantidad de subitems que tiene un ítem
    @Override
    public int getChildrenCount(int groupPosition) {
        return grupos.get(groupPosition).size();
    }

    //Los datos de un ítem especificado por groupPosition
    @Override
    public Object getGroup(int groupPosition) {
        return grupos.get(groupPosition);
    }

    //La cantidad de ítem que tenemos definidos
    @Override
    public int getGroupCount() {
        return grupos.size();
    }

    //Método que se invoca al contraer un ítem
    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    //Método que se invoca al expandir un ítem
    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    //Devuelve el id de un ítem
    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //Nos informa si es seleccionable o no un ítem o subitem
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}