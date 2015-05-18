package com.taesua.admeet.admeet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Encarna on 24/04/2015.
 */
public class MenuAdapter extends ArrayAdapter {

    private Context context;
    private String[] datos;

    public MenuAdapter(Context context, String[] datos) {
        super(context, R.layout.listview_item_menu, datos);


        this.context = context;
        this.datos = datos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // En primer lugar "inflamos" una nueva vista, que será la que se
        // mostrará en la celda del ListView. Para ello primero creamos el
        // inflater, y después inflamos la vista.
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.listview_item_menu, null);

        // Recogemos el TextView para mostrar el nombre y establecemos el nombre
        TextView nombre = (TextView) item.findViewById(R.id.textviewNombre);
        nombre.setText(datos[position]);

        // Devolvemos la vista para que se muestre en el ListView.
        return item;
    }

}
