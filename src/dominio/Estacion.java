/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dominio;

import tads.ColaSE;
import tads.ListaBicicletas;
import tads.NodoSE;

public class Estacion implements Comparable<Estacion> {
    private String nombre;           
    private String barrio;           
    private int capacidad;           
    private ListaBicicletas anclajes; 
    private ColaSE<Usuario> esperaAlquiler;
    private ColaSE<Usuario> esperaAnclaje;

    public Estacion(String nombre, String barrio, int capacidad) {
        this.nombre = nombre;
        this.barrio = barrio;
        this.capacidad = capacidad;
        this.anclajes = new ListaBicicletas(); 
        this.esperaAlquiler = new ColaSE<>();
        this.esperaAnclaje = new ColaSE<>();
    }

    public boolean hayLugar() {
        return anclajes.contar() < capacidad;
    }

    public boolean anclarBicicleta(Bicicleta b) {
        if (b == null) return false;
        if (hayLugar()) {
            anclajes.agregar(b);
            b.setEnDeposito(false);
            return true;
        }
        return false;
    }

    public boolean sacarBicicleta(String codigo) {
        if (codigo == null || codigo.isEmpty()) return false;
        return anclajes.sacar(codigo) != null;
    }

    public Bicicleta buscarBicicleta(String codigo) {
        if (codigo == null) return null;
        NodoSE<Bicicleta> aux = anclajes.getPrimero();
        while (aux != null) {
            if (aux.getDato().getCodigo().equals(codigo)) return aux.getDato();
            aux = aux.getSiguiente();
        }
        return null;
    }

    @Override
    public int compareTo(Estacion otra) {
        return this.nombre.compareTo(otra.nombre);
    }

    @Override
    public String toString() {
        return nombre + " - " + barrio + " [" + anclajes.contar() + "/" + capacidad + "]";
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getBarrio() { return barrio; }
    public int getCapacidad() { return capacidad; }
    public ListaBicicletas getAnclajes() { return anclajes; }
    public ColaSE<Usuario> getEsperaAlquiler() {return esperaAlquiler;}
    public ColaSE<Usuario> getEsperaAnclaje() {return esperaAnclaje;}
}

