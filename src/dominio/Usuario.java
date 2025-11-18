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
    private Bicicleta bicicletaActual; 

    public Usuario(String cedula, String nombre) {
        this.cedula = cedula;
        this.nombre = nombre;
         this.cantidadAlquileres = 0; 
          this.bicicletaActual = null;
    }

    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }
    public Bicicleta getBicicletaActual() {
    return bicicletaActual;
}

public void setBicicletaActual(Bicicleta bicicletaActual) {
    this.bicicletaActual = bicicletaActual;
}
    @Override
    public int compareTo(Usuario otro) {
 
        return this.cedula.compareTo(otro.cedula);
    }

    @Override
    public String toString() {
        return cedula + " - " + nombre;
    }
}
