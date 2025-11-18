/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tads;

import dominio.Barrio;

/**
 *
 * @author ljgp2
 */
public class ListaBarrio {
    private ListaSE<Barrio> barrios;

    public ListaBarrio() {
        barrios = new ListaSE<>();
    }

    public Barrio buscar(String nombre) {
        for (int i = 0; i < barrios.longitud(); i++) {
            try {
                Barrio b = barrios.obtener(i);
                if (b.getNombre().equalsIgnoreCase(nombre)) return b;
            } catch (Exception e) {}
        }
        return null;
    }

    public void agregarOBuscarYActualizar(String nombre, int bicis, int capacidad) {
        Barrio b = buscar(nombre);
        if (b == null) {
            b = new Barrio(nombre);
            b.agregarBicis(bicis);
            b.agregarCapacidad(capacidad);
            insertarOrdenado(b);
        } else {
            b.agregarBicis(bicis);
            b.agregarCapacidad(capacidad);
        }
    }
    
    public void ordenar() {
    if (barrios.vacia() || barrios.longitud() == 1) return;

    for (int i = 0; i < barrios.longitud() - 1; i++) {
        for (int j = i + 1; j < barrios.longitud(); j++) {
            try {
                Barrio b1 = barrios.obtener(i);
                Barrio b2 = barrios.obtener(j);
                if (b1.compareTo(b2) > 0) {
                    // intercambiar
                    barrios.insertar(b2, i);
                    barrios.eliminar(j + 1); // ajustar índice después del insert
                }
            } catch (Exception e) {}
        }
    }
}


    public void insertarOrdenado(Barrio nuevo) {
        if (nuevo == null) return;

        if (barrios.vacia() || nuevo.compareTo(barrios.obtener(0)) < 0) {
            try {
                barrios.insertar(nuevo, 0);
            } catch (Exception e) {}
        } else {
            for (int i = 0; i < barrios.longitud(); i++) {
                try {
                    Barrio actual = barrios.obtener(i);
                    if (i + 1 == barrios.longitud() || nuevo.compareTo(barrios.obtener(i + 1)) < 0) {
                        barrios.insertar(nuevo, i + 1);
                        break;
                    }
                } catch (Exception e) {}
            }
        }
    }

    public ListaSE<Barrio> getLista() {
        return barrios;
    }
}

