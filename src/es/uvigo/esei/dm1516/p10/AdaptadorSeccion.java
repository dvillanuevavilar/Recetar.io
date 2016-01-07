package es.uvigo.esei.dm1516.p10;

import android.app.Activity;
import android.content.Intent;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import es.uvigo.esei.dm1516.p10.Core.App;
import es.uvigo.esei.dm1516.p10.Model.Receta;

public class AdaptadorSeccion extends BaseExpandableListAdapter {
    private final SparseArray<GrupoDeItems> grupos;
    public LayoutInflater inflater;
    public Activity activity;
    private static final int REQUEST_CODE = 1;

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
        String nombreAutor = ((App) activity.getApplication()).getDb().obtenerNombre(children.getAutor());

        tvTitulo.setText(children.getTitulo());
        tvTiempo.setText(String.valueOf(children.getTiempo()));
        tvDificultad.setText(children.getDificultad());
        tvNumComensales.setText(String.valueOf(children.getNumComensales()));
        tvAutor.setText(nombreAutor);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVerReceta = new Intent(activity, VerReceta.class);
                intentVerReceta.putExtra("idReceta", children.getIdReceta());
                intentVerReceta.putExtra("titulo", children.getTitulo());
                intentVerReceta.putExtra("tiempo", String.valueOf(children.getTiempo()));
                intentVerReceta.putExtra("numComensales", String.valueOf(children.getNumComensales()));
                intentVerReceta.putExtra("dificultad", children.getDificultad());
                intentVerReceta.putExtra("autor", nombreAutor);
                intentVerReceta.putExtra("ingredientes", children.getIngredientes());
                intentVerReceta.putExtra("elaboracion", children.getElaboracion());
                activity.startActivityForResult(intentVerReceta, REQUEST_CODE);
            }
        });
        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return grupos.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return grupos.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return grupos.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return grupos.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}