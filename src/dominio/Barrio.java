/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 *
 * @author ljgp2
 */

public class Barrio implements Comparable<Barrio> {
    private String nombre;
    private int bicisAncladas;
    private int capacidadTotal;

    public Barrio(String nombre) {
    this.nombre = nombre.trim().toUpperCase();
    this.bicisAncladas = 0;
    this.capacidadTotal = 0;
}


    public String getNombre() { return nombre; }
    public int getBicisAncladas() { return bicisAncladas; }
    public int getCapacidadTotal() { return capacidadTotal; }

    public void agregarBicis(int cantidad) { this.bicisAncladas += cantidad; }
    public void agregarCapacidad(int cantidad) { this.capacidadTotal += cantidad; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Barrio)) return false;
        Barrio b = (Barrio) o;
        return this.nombre.equalsIgnoreCase(b.nombre);
    }
    
    @Override
    public int compareTo(Barrio otra) {
        return this.nombre.compareToIgnoreCase(otra.nombre); // orden alfabético
    }
}

