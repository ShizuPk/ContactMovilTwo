package com.databit.contactmoviltwo.Modelo;

public class UsuarioModel {
    private String nombre;
    private String apellido;
    private String correo;
    private String nombreUsuario;

    public UsuarioModel() {
        // Constructor vacío requerido para Firebase
    }

    public UsuarioModel(String nombre, String apellido, String correo, String nombreUsuario) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }
}
