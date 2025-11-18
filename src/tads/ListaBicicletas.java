/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tads;
import tads.NodoSE;

import dominio.Bicicleta;
import dominio.EstadoBicicleta;

/**
 *
 * @author ljgp2
 */

public class ListaBicicletas {
    private ListaSE<Bicicleta> bicicletas;
     private NodoSE<Bicicleta> inicio;

    public ListaBicicletas() {
        bicicletas = new ListaSE<>();
    }

    public boolean estaVacia() {
        return bicicletas.vacia();
    }

    public void agregar(Bicicleta bici) {
        if (bici != null) bicicletas.adicionar(bici);
    }

    public Bicicleta sacar(String codigo) {
        for (int i = 0; i < bicicletas.longitud(); i++) {
            try {
                Bicicleta b = bicicletas.obtener(i);
                if (b.getCodigo().equals(codigo)) {
                    bicicletas.eliminar(i);
                    return b;
                }
            } catch (Exception e) { }
        }
        return null;
    }

    public Bicicleta buscar(String codigo) {
        for (int i = 0; i < bicicletas.longitud(); i++) {
            try {
                Bicicleta b = bicicletas.obtener(i);
                if (b.getCodigo().equals(codigo)) return b;
            } catch (Exception e) { }
        }
        return null;
    }

    public int contar() {
        return bicicletas.longitud();
    }

    public String listar() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bicicletas.longitud(); i++) {
            try {
                if (sb.length() > 0) sb.append("|");
                sb.append(bicicletas.obtener(i).getCodigo());
            } catch (Exception e) { }
        }
        return sb.toString();
    }

    public String listarConEstado() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bicicletas.longitud(); i++) {
            try {
                Bicicleta b = bicicletas.obtener(i);
                if (sb.length() > 0) sb.append("|");
                sb.append(b.getCodigo()).append("#")
                  .append(b.getTipo()).append("#")
                  .append(b.getEstado());
            } catch (Exception e) { }
        }
        return sb.toString();
    }

    // --- ESTA ERA LA PARTE ROTA ---
    // Recorremos por ListaSE en vez de usar "primero"
    public Bicicleta buscarDisponible() {
        for (int i = 0; i < bicicletas.longitud(); i++) {
            try {
                Bicicleta b = bicicletas.obtener(i);
                if (b.getEstado() == EstadoBicicleta.Disponible)
                    return b;
            } catch (Exception e) { }
        }
        return null;
    }

    public int cantidadDisponibles() {
    int c = 0;
    NodoSE<Bicicleta> act = this.getLista().getInicio(); // o getInicio(), según tu implementación

    while (act != null) {
        Bicicleta b = act.getDato();
        if (b.getEstado() == EstadoBicicleta.Disponible)
            c++;
        act = act.getSiguiente();
    }

    return c;
}

    
    public ListaSE<Bicicleta> getLista() {
        return bicicletas;
    }
    
    public void insertarOrdenado(Bicicleta b) {
        NodoSE<Bicicleta> nuevo = new NodoSE<>(b);

        // insertar al inicio
        if (inicio == null || inicio.getDato().compareTo(b) > 0) {
            nuevo.setSiguiente(inicio);
            inicio = nuevo;
            return;
        }

        NodoSE<Bicicleta> act = inicio;

        // avanzar mientras el siguiente sea menor
        while (act.getSiguiente() != null &&
               act.getSiguiente().getDato().compareTo(b) < 0) {
            act = act.getSiguiente();
        }

        nuevo.setSiguiente(act.getSiguiente());
        act.setSiguiente(nuevo);
    }
}





