/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tads;

import dominio.Estacion;

/**
 *
 * @author ljgp2
 */
public class ListaEstaciones {
    private ListaSE<Estacion> estaciones;

    public ListaEstaciones() {
        estaciones = new ListaSE<>();
    }

    public boolean existeEstacion(String nombre) {
        for (int i = 0; i < estaciones.longitud(); i++) {
            try {
                if (estaciones.obtener(i).getNombre().equalsIgnoreCase(nombre)) return true;
            } catch (Exception e) { }
        }
        return false;
    }

    public void insertarOrdenado(Estacion nueva) {
        if (nueva == null) return;

        if (estaciones.vacia() || nueva.compareTo(estaciones.obtener(0)) < 0) {
            try {
                estaciones.insertar(nueva, 0);
            } catch (Exception e) { }
        } else {
            for (int i = 0; i < estaciones.longitud(); i++) {
                try {
                    Estacion actual = estaciones.obtener(i);
                    if (i + 1 == estaciones.longitud() || nueva.compareTo(estaciones.obtener(i + 1)) < 0) {
                        estaciones.insertar(nueva, i + 1);
                        break;
                    }
                } catch (Exception e) { }
            }
        }
    }

    public Estacion buscar(String nombre) {
        for (int i = 0; i < estaciones.longitud(); i++) {
            try {
                Estacion e = estaciones.obtener(i);
                if (e.getNombre().equalsIgnoreCase(nombre)) return e;
            } catch (Exception e) { }
        }
        return null;
    }

    public String listar() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < estaciones.longitud(); i++) {
            try {
                Estacion e = estaciones.obtener(i);
                if (sb.length() > 0) sb.append("|");
                sb.append(e.getNombre()).append("#").append(e.getBarrio())
                  .append("[").append(e.getAnclajes().contar())
                  .append("/").append(e.getCapacidad()).append("]");
            } catch (Exception e) { }
        }
        return sb.toString();
    }

    public NodoSE<Estacion> getPrimero() {
        try {
            return estaciones.obtener(0) != null 
                ? new NodoSE<>(estaciones.obtener(0)) 
                : null;
        } catch (Exception e) {
            return null;
        }
    }

    public ListaSE<Estacion> getLista() {
        return estaciones;
    }
    
   
}



