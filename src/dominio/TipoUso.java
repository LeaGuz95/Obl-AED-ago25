/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 *
 * @author ljgp2
 */
public class TipoUso implements Comparable<TipoUso> {
    private TipoBicicleta tipo;
    private int cantidadAlquileres;

    public TipoUso(TipoBicicleta tipo) {
        this.tipo = tipo;
        this.cantidadAlquileres = 0;
    }

    public TipoBicicleta getTipo() {
        return tipo;
    }

    public int getCantidadAlquileres() {
        return cantidadAlquileres;
    }

    public void incrementar(int cantidad) {
        this.cantidadAlquileres += cantidad;
    }

    @Override
    public int compareTo(TipoUso otra) {
        // primero por cantidad descendente
        if (this.cantidadAlquileres != otra.cantidadAlquileres)
            return otra.cantidadAlquileres - this.cantidadAlquileres;
        // si es empate, por tipo alfabético
        return this.tipo.name().compareTo(otra.tipo.name());
    }
    
    
}
