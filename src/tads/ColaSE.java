/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tads;


/**
 *
 * @author ljgp2
 */
public class ColaSE<T> {
    private NodoSE<T> frente;
    private NodoSE<T> fondo;

    public ColaSE() {
        frente = null;
        fondo = null;
    }

    public void encolar(T dato) {
        if (dato == null) return;
        NodoSE<T> nuevo = new NodoSE<>(dato);
        if (fondo == null) {
            frente = fondo = nuevo;
        } else {
            fondo.setSiguiente(nuevo);
            fondo = nuevo;
        }
    }

    public T desencolar() {
        if (frente == null) return null;
        T dato = frente.getDato();
        frente = frente.getSiguiente();
        if (frente == null) fondo = null;
        return dato;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    // 🔹 Devuelve el nodo frontal para recorrerlo en la GUI
    public NodoSE<T> getFrente() {
        return frente;
    }

    // Devuelve el nodo fondo si necesitás recorrerlo
    public NodoSE<T> getFondo() {
        return fondo;
    }
}

