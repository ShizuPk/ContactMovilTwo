package com.databit.contactmoviltwo;
import android.os.Parcel;

import java.io.Serializable;
public class Contact {
    private String apellido;
    private String correo;
    private String nombre;
    private String nombreUsuario;

    // Constructor vacío requerido para Firebase
    public Contact() {
    }

    public Contact(String apellido, String correo, String nombre, String nombreUsuario) {
        this.apellido = apellido;
        this.correo = correo;
        this.nombre = nombre;
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
