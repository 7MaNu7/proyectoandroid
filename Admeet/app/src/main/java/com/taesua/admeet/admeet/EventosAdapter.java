package com.taesua.admeet.admeet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Encarna on 24/04/2015.
 */
public class EventosAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<EventosClase> datos;

    public EventosAdapter(Context context, String[] datos, String[] categorias) {
        super(context, R.layout.listview_item, datos);
        //Guardamos parametros en variables
        this.context = context;

        ArrayList<EventosClase> datos1 = new ArrayList<EventosClase>();
        for(int i=0;i<datos.length;i++)
        {
            datos1.add(i, new EventosClase(datos[i],categorias[i]));
        }

        this.datos = datos1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // En primer lugar "inflamos" una nueva vista, que será la que se
        // mostrará en la celda del ListView. Para ello primero creamos el
        // inflater, y después inflamos la vista.
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.listview_item, null);

        // A partir de la vista, recogeremos los controles que contiene para
        // poder manipularlos.
        // Recogemos el ImageView y le asignamos una foto.
        ImageView imagen = (ImageView) item.findViewById(R.id.imgAnimal);
        imagen.setImageResource(datos.get(position).getDrawableImageID());
        System.out.println("ESTOS SON LOS ID DE LAS IMAGENES");
        System.out.println(":::::::::::::::::"+datos.get(position).getDrawableImageID()+"::::::::::::::::::::::::");
        //imagen.setImageResource(R.drawable.imagen4);
       // ImageView imageView = new ImageView(this);
        //imageView.setImageBitmap(getRoundedCornerBitmap(.getDrawable(R.id.image), true));
       // imagen.setImageBitmap(getRoundedCornerBitmap(this.getResources().getDrawable(R.id.image), true), true));



        // Recogemos el TextView para mostrar el nombre y establecemos el
        // nombre.
        TextView nombre = (TextView) item.findViewById(R.id.tvContent);
        nombre.setText(datos.get(position).getCategoria());//getNombre());

        // Recogemos el TextView para mostrar el número de celda y lo
        // establecemos.
        TextView numCelda = (TextView) item.findViewById(R.id.tvField);
        numCelda.setText(datos.get(position).getNombre());//(String.valueOf(position));



        // Devolvemos la vista para que se muestre en el ListView.
        return item;
    }


    public static Bitmap getRoundedCornerBitmap( Drawable drawable, boolean square) {
        int width = 0;
        int height = 0;

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap() ;

        if(square){
            if(bitmap.getWidth() < bitmap.getHeight()){
                width = bitmap.getWidth();
                height = bitmap.getWidth();
            } else {
                width = bitmap.getHeight();
                height = bitmap.getHeight();
            }
        } else {
            height = bitmap.getHeight();
            width = bitmap.getWidth();
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        final float roundPx = 90;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
