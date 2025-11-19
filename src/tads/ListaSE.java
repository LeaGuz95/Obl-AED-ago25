/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tads;


import tads.Exceptions.DatoInvalidoException;
import tads.Exceptions.ListaVaciaException;
import tads.Exceptions.PosicionInvalidaException;

import java.util.Comparator;

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

    public NodoSE<T> getInicio() {
        return inicio;
    }

    public ListaSE<T> getLista() {
        return this;
    }

    


    // ============================================================
    //                     MERGE SORT PÚBLICO
    // ============================================================

    public void ordenar(Comparator<T> cmp) {
        if (inicio == null || inicio.getSiguiente() == null)
            return;
        inicio = mergeSort(inicio, cmp);
    }


    // ============================================================
    //                  MERGE SORT IMPLEMENTACIÓN
    // ============================================================

    private NodoSE<T> mergeSort(NodoSE<T> head, Comparator<T> cmp) {
        if (head == null || head.getSiguiente() == null)
            return head;

        NodoSE<T> mid = getMiddle(head);
        NodoSE<T> rightStart = mid.getSiguiente();
        mid.setSiguiente(null);  

        NodoSE<T> left = mergeSort(head, cmp);
        NodoSE<T> right = mergeSort(rightStart, cmp);

        return merge(left, right, cmp);
    }

    private NodoSE<T> merge(NodoSE<T> a, NodoSE<T> b, Comparator<T> cmp) {
        if (a == null) return b;
        if (b == null) return a;

        NodoSE<T> result;

        if (cmp.compare(a.getDato(), b.getDato()) <= 0) {
            result = a;
            result.setSiguiente(merge(a.getSiguiente(), b, cmp));
        } else {
            result = b;
            result.setSiguiente(merge(a, b.getSiguiente(), cmp));
        }

        return result;
    }

    private NodoSE<T> getMiddle(NodoSE<T> head) {
        if (head == null)
            return head;

        NodoSE<T> slow = head;
        NodoSE<T> fast = head.getSiguiente();

        while (fast != null && fast.getSiguiente() != null) {
            slow = slow.getSiguiente();
            fast = fast.getSiguiente().getSiguiente();
        }

        return slow;
    }
    
    // ============================================================
    //                  bubbleSort 
    // ============================================================
    
    public void bubbleSort(Comparator<T> cmp) {
    if (inicio == null || inicio.getSiguiente() == null) return;

    boolean huboCambio;
    do {
        huboCambio = false;
        NodoSE<T> actual = inicio;
        NodoSE<T> siguiente = inicio.getSiguiente();

        while (siguiente != null) {
            if (cmp.compare(actual.getDato(), siguiente.getDato()) > 0) {
                // swap de datos (más simple que re-enlazar nodos)
                T tmp = actual.getDato();
                actual.setDato(siguiente.getDato());
                siguiente.setDato(tmp);
                huboCambio = true;
            }
            actual = siguiente;
            siguiente = siguiente.getSiguiente();
        }
    } while (huboCambio);
    
    
    }   
    
    public void insertionSort(Comparator<T> cmp) {
        if (inicio == null || inicio.getSiguiente() == null)
            return;

        NodoSE<T> sorted = null; // nueva lista ordenada
        NodoSE<T> actual = inicio;

        while (actual != null) {
            NodoSE<T> siguiente = actual.getSiguiente();

            // insertar "actual" en la lista sorted
            if (sorted == null || cmp.compare(actual.getDato(), sorted.getDato()) < 0) {
                actual.setSiguiente(sorted);
                sorted = actual;
            } else {
                NodoSE<T> aux = sorted;
                while (aux.getSiguiente() != null &&
                       cmp.compare(actual.getDato(), aux.getSiguiente().getDato()) >= 0) {
                    aux = aux.getSiguiente();
                }
                actual.setSiguiente(aux.getSiguiente());
                aux.setSiguiente(actual);
            }
            actual = siguiente;
        }

        inicio = sorted;
    }
    
    public void selectionSort(Comparator<T> cmp) {
    if (inicio == null || inicio.getSiguiente() == null)
        return;

    NodoSE<T> aux = inicio;

    while (aux != null) {
        NodoSE<T> min = aux;
        NodoSE<T> r = aux.getSiguiente();

        while (r != null) {
            if (cmp.compare(r.getDato(), min.getDato()) < 0) {
                min = r;
            }
            r = r.getSiguiente();
        }

        // swap datos (no nodos)
        if (min != aux) {
            T temp = aux.getDato();
            aux.setDato(min.getDato());
            min.setDato(temp);
        }

        aux = aux.getSiguiente();
    }
}


}