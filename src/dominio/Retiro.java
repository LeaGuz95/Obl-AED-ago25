/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 *
 * @author ljgp2
 */


public class Retiro {
    private Bicicleta bicicleta;
    private Usuario usuario;
    private Estacion estacionOrigen;

    public Retiro(Bicicleta bicicleta, Usuario usuario, Estacion estacionOrigen) {
        this.bicicleta = bicicleta;
        this.usuario = usuario;
        this.estacionOrigen = estacionOrigen;
    }

    // Getters
    public Bicicleta getBicicleta() {
        return bicicleta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Estacion getEstacionOrigen() {
        return estacionOrigen;
    }

    // Opcional: toString para imprimir los retiros deshechos
    @Override
    public String toString() {
        return bicicleta.getCodigo() + "#" + usuario.getCedula() + "#" + estacionOrigen.getNombre();
    }
}

