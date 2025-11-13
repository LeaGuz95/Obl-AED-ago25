package tads;

import tads.Exceptions.DatoInvalidoException;
import tads.Exceptions.PilaVaciaException;



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *

 */
public class PilaSE<T> {
    private NodoSE<T> tope;

    public PilaSE() {
        tope = null;
    }

    // Apila un elemento
    public void apilar(T dato) {
        if (dato == null) throw new DatoInvalidoException();
        NodoSE<T> nuevo = new NodoSE<>(dato);
        nuevo.setSiguiente(tope);
        tope = nuevo;
    }

    // Desapila el elemento del tope
    public T desapilar() {
        if (estaVacia()) throw new PilaVaciaException();
        T dato = tope.getDato();
        tope = tope.getSiguiente();
        return dato;
    }

    // Retorna el dato del tope sin desapilar
    public T tope() {
        if (estaVacia()) throw new PilaVaciaException();
        return tope.getDato();
    }

    // Retorna si la pila está vacía
    public boolean estaVacia() {
        return tope == null;
    }

    // 🔹 Devuelve el nodo para recorrerlo en la GUI
    public NodoSE<T> getTope() {
        return tope;
    }

    // 🔹 Método para obtener todos los elementos de la pila en otra ListaSE (para GUI)
    public ListaSE<T> obtenerElementos() {
        ListaSE<T> elementos = new ListaSE<>();
        NodoSE<T> actual = tope;
        while (actual != null) {
            elementos.adicionar(actual.getDato());
            actual = actual.getSiguiente();
        }
        return elementos;
    }

}


