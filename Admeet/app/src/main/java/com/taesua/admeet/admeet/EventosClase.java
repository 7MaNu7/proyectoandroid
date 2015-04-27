package com.taesua.admeet.admeet;

/**
 * Created by Encarna on 24/04/2015.
 */
public class EventosClase {
    private String nombre;
    private String categoria;
    private String fecha;
    private int drawableImageID;
    private Integer asistentes;
    private Integer maxasistentes;

    public EventosClase(String nombre, String categoria, Integer asis, Integer maxasis, String fecha) {
        this.nombre = nombre;
        this.categoria = categoria;
        setImagenPorCategoria();
        this.asistentes = asis;
        this.maxasistentes = maxasis;
        this.fecha = fecha;
    }

    public String getFecha() { return fecha; }

    public Integer getAsistentes() { return asistentes; }

    public Integer getMaxAsistentes() { return maxasistentes; }

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
        if(categoria.equals("Deportes"))
            this.drawableImageID = R.drawable.deportes1;
        else if(categoria.equals("Fiesta"))
            this.drawableImageID = R.drawable.fiesta1;
        else if(categoria.equals("Cultura"))
            this.drawableImageID = R.drawable.cultura4;
        else if(categoria.equals("Juegos de mesa"))
            this.drawableImageID = R.drawable.juegosdemesa1;
        else
            this.drawableImageID = R.drawable.logo2;
    }

}