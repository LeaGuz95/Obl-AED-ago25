/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tads.Exceptions;

/**
 *
 * @author ljgp2
 */
public class PilaVaciaException extends RuntimeException {
    public PilaVaciaException() {
        super("La pila está vacía");
    }
}

