package es.uvigo.esei.dm1516.p10;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;

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

    // Nos devuelve los datos asociados a un subitem en base
    // a la posición
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return grupos.get(groupPosition).children.get(childPosition);
    }

    // Devuelve el id de un item o subitem en base a la
    // posición de item y subitem
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    // En base a la posición del item y de subitem nos devuelve
    // el objeto view correspondiente y el layout para los subitems
    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String children = (String) getChild(groupPosition, childPosition);
        TextView textvw = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.subitems, null);
        }
        textvw = (TextView) convertView.findViewById(R.id.tvSeccion);
        textvw.setText(children);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, children, Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    // Nos devuelve la cantidad de subitems que tiene un ítem
    @Override
    public int getChildrenCount(int groupPosition) {
        return grupos.get(groupPosition).children.size();
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

    //Obtenemos el layout para los ítems
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.items, parent, false);
        }
        GrupoDeItems grupo = (GrupoDeItems) getGroup(groupPosition);
        TextView tvSeccion = (TextView) convertView.findViewById(R.id.tvSeccion);
        TextView tvCountRecetas = (TextView) convertView.findViewById(R.id.tvCountRecetas);

        tvSeccion.setText(grupo.getNombreGrupo());
        tvCountRecetas.setText(String.valueOf(grupo.getCountRecetas()));
        return convertView;
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