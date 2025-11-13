/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tads;

import dominio.Retiro;

/**
 *
 * @author ljgp2
 */
public class PilaRetiros {
    private PilaSE<Retiro> pila;

    public PilaRetiros() {
        pila = new PilaSE<>();
    }

    public void apilar(Retiro r) {
        if (r != null) pila.apilar(r);
    }

    public Retiro desapilar() {
        if (pila.estaVacia()) return null;
        return pila.desapilar();
    }

    public boolean estaVacia() {
        return pila.estaVacia();
    }

    public NodoSE<Retiro> getTope() {
        return pila.getTope();
    }
    
    public ListaSE<Retiro> obtenerElementos() {
        return pila.obtenerElementos();
    }
    //
}
