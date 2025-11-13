/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tads;

/**
 *
 * @author ljgp2
 */
public class NodoSE<T> {
    private T dato;
    private NodoSE<T> siguiente;

    public NodoSE(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public T getDato() { return dato; }
    public void setDato(T dato) { this.dato = dato; }

    public NodoSE<T> getSiguiente() { return siguiente; }
    public void setSiguiente(NodoSE<T> siguiente) { this.siguiente = siguiente; }
}
