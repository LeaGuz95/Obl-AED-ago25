/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 *
 * @author ljgp2
 */
public class Usuario implements Comparable<Usuario> {
    private String cedula;
    private String nombre;
    private int cantidadAlquileres;


    public Usuario(String cedula, String nombre) {
        this.cedula = cedula;
        this.nombre = nombre;
    }

    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }

    @Override
    public int compareTo(Usuario otro) {
 
        return this.cedula.compareTo(otro.cedula);
    }

    @Override
    public String toString() {
        return cedula + " - " + nombre;
    }
}
