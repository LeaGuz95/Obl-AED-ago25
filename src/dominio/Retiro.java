/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 *
 * @author ljgp2
 */


import java.time.LocalDateTime;

public class Retiro {
    private Bicicleta bicicleta;
    private Usuario usuario;
    private Estacion estacionOrigen;
    private LocalDateTime fecha; 

    public Retiro(Bicicleta bicicleta, Usuario usuario, Estacion estacionOrigen) {
        this.bicicleta = bicicleta;
        this.usuario = usuario;
        this.estacionOrigen = estacionOrigen;
        this.fecha = LocalDateTime.now(); // asigna la fecha y hora actual
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

    public LocalDateTime getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        return bicicleta.getCodigo() + "#" + usuario.getCedula() + "#" 
               + estacionOrigen.getNombre() + "#" + fecha;
    }
}


