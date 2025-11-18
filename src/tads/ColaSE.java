/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tads;

import tads.Exceptions.ColaVaciaException;
import tads.Exceptions.DatoInvalidoException;


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
        if (dato == null) throw new DatoInvalidoException();
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
    
    public int longitud() {
    int count = 0;
    NodoSE<T> aux = frente;
    while (aux != null) {
        count++;
        aux = aux.getSiguiente();
    }
    return count;
}
    
public T verPrimero() {
    if (estaVacia()) throw new ColaVaciaException();
    return frente.getDato();
}

public void vaciar() {
    frente = null;
    fondo = null;
}


public ListaSE<T> obtenerElementos() {
    ListaSE<T> elementos = new ListaSE<>();
    NodoSE<T> aux = frente;
    while (aux != null) {
        elementos.adicionar(aux.getDato());
        aux = aux.getSiguiente();
    }
    return elementos;
}

public void recorrer(java.util.function.Consumer<T> action) {
    NodoSE<T> aux = frente;
    while (aux != null) {
        action.accept(aux.getDato());
        aux = aux.getSiguiente();
    }
}


    public boolean estaVacia() {
        return frente == null;
    }


    public NodoSE<T> getFrente() {
        return frente;
    }


    public NodoSE<T> getFondo() {
        return fondo;
    }
}

