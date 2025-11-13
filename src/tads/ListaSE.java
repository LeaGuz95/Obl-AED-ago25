/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tads;

import tads.Exceptions.DatoInvalidoException;
import tads.Exceptions.ListaVaciaException;
import tads.Exceptions.PosicionInvalidaException;

/**
 *
 * @author ljgp2
 */
public class ListaSE<T> implements ILista<T> {

    private NodoSE<T> inicio;
    private int cantidad;

    public ListaSE() {
        this.inicio = null;
        this.cantidad = 0;
    }

    @Override
    public void adicionar(T x) {
        if (x == null)
            throw new DatoInvalidoException();

        NodoSE<T> nuevo = new NodoSE<>(x);
        if (vacia()) {
            inicio = nuevo;
        } else {
            NodoSE<T> aux = inicio;
            while (aux.getSiguiente() != null) {
                aux = aux.getSiguiente();
            }
            aux.setSiguiente(nuevo);
        }
        cantidad++;
    }

    @Override
    public void insertar(T x, int pos) {
        if (x == null)
            throw new DatoInvalidoException();
        if (pos < 0 || pos > cantidad)
            throw new PosicionInvalidaException();

        NodoSE<T> nuevo = new NodoSE<>(x);
        if (pos == 0) {
            nuevo.setSiguiente(inicio);
            inicio = nuevo;
        } else {
            NodoSE<T> aux = inicio;
            for (int i = 0; i < pos - 1; i++) {
                aux = aux.getSiguiente();
            }
            nuevo.setSiguiente(aux.getSiguiente());
            aux.setSiguiente(nuevo);
        }
        cantidad++;
    }

    @Override
    public T obtener(int pos) {
        if (vacia())
            throw new ListaVaciaException();
        if (pos < 0 || pos >= cantidad)
            throw new PosicionInvalidaException();

        NodoSE<T> aux = inicio;
        for (int i = 0; i < pos; i++) {
            aux = aux.getSiguiente();
        }
        return aux.getDato();
    }

    @Override
    public void eliminar(int pos) {
        if (vacia())
            throw new ListaVaciaException();
        if (pos < 0 || pos >= cantidad)
            throw new PosicionInvalidaException();

        if (pos == 0) {
            inicio = inicio.getSiguiente();
        } else {
            NodoSE<T> aux = inicio;
            for (int i = 0; i < pos - 1; i++) {
                aux = aux.getSiguiente();
            }
            aux.setSiguiente(aux.getSiguiente().getSiguiente());
        }
        cantidad--;
    }

    @Override
    public int longitud() {
        return cantidad;
    }

    @Override
    public boolean vacia() {
        return inicio == null;
    }

    // Getter protegido para recorrer la lista desde otras estructuras
    public NodoSE<T> getInicio() {
        return inicio;
    }
}

