package com.taesua.admeet.admeet;

/**
 * Created by Encarna on 24/04/2015.
 */
public class EventosClase {
    private String nombre;
    private String categoria;
    private int drawableImageID;

    public EventosClase(String nombre, String categoria) {
        this.nombre = nombre;
        this.categoria = categoria;
        setImagenPorCategoria();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String cat) {
        this.categoria = cat;
    }

    public int getDrawableImageID() {
        return drawableImageID;
    }

    public void setDrawableImageID(int drawableImageID) {
        this.drawableImageID = drawableImageID;
    }

    public void setImagenPorCategoria()
    {
        System.out.println("ESTA ES LA CATEGORIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        System.out.println("::::::::::::::::::::::"+categoria+"::::::::::::::::::");
        if(categoria=="Deportes")
            this.drawableImageID = R.drawable.imagen1;
        else if(categoria=="Fiesta")
            this.drawableImageID = R.drawable.imagen2;
        else
            this.drawableImageID = R.drawable.imagen4;
    }

}