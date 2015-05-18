package com.taesua.admeet.admeet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hector on 16/05/2015.
 */
public class ComentariosAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<ComentariosClase> datos;

    public ComentariosAdapter(Context context, String[] nombres, String[] comentarios) {
        super(context, R.layout.listview_item_comentarios, nombres);

        this.context = context;
        ArrayList<ComentariosClase> datos1 = new ArrayList<ComentariosClase>();
        System.out.println("77777777777777777777777777777777777777777777777  "+nombres.length);
        for(int i=0;i<nombres.length;i++)
            datos1.add(i, new ComentariosClase(nombres[i], comentarios[i]));

        this.datos = datos1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("33333333333333333333333333333333333333333333333");
        // En primer lugar "inflamos" una nueva vista, que será la que se
        // mostrará en la celda del ListView. Para ello primero creamos el
        // inflater, y después inflamos la vista.
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.listview_item_comentarios, null);

        // A partir de la vista, recogeremos los controles que contiene para
        // poder manipularlos.

        System.out.println("-.-.-.-.-.-.--.-.-.-.-.-.-Mira este comentario:" + datos.get(position).getNombre());

        // Recogemos el TextView para mostrar el nombre y establecemos el nombre
        TextView nombre = (TextView) item.findViewById(R.id.textviewNombre);
        nombre.setText(datos.get(position).getNombre());
        TextView comentario = (TextView) item.findViewById(R.id.textViewComentario);
        comentario.setText(datos.get(position).getComentario());

        // Devolvemos la vista para que se muestre en el ListView.
        return item;
    }



}
