package com.taesua.admeet.admeet;

/**
 * Created by Hector on 16/05/2015.
 */
public class ComentariosClase {
    private String nombre;
    private String comentario;

    public ComentariosClase(String nombre, String comentario) {

        this.nombre = nombre;
        this.comentario = comentario;
    }

    public String getNombre() {return nombre;}
    public String getComentario() {return comentario;}

}
