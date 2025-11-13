/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;
/**
/**
 *
 * @author ljgp2
 */
public class Bicicleta implements Comparable<Bicicleta> {
    private String codigo;
    private TipoBicicleta tipo;
    private EstadoBicicleta estado;
    private String motivoMantenimiento; 
    private boolean enDeposito; // true si está en depósito
    private int vecesAlquilada;


    // Constructor
    public Bicicleta(String codigo, TipoBicicleta tipo) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.enDeposito = true; 
        this.estado = EstadoBicicleta.Disponible; 
        this.motivoMantenimiento = null; 
    }

    // Getters
    public String getCodigo() {
        return codigo;
    }

    public TipoBicicleta getTipo() {
        return tipo;
    }

    public boolean isEnDeposito() {
        return enDeposito;
    }

    public EstadoBicicleta getEstado() {
        return estado;
    }

    public String getMotivoMantenimiento() {
        return motivoMantenimiento;
    }

    // Setters
    public void setEnDeposito(boolean enDeposito) {
        this.enDeposito = enDeposito;
    }

    public void setEstado(EstadoBicicleta estado) {
        this.estado = estado;
    }

    public void setMotivoMantenimiento(String motivo) {
        this.motivoMantenimiento = motivo;
    }

    @Override
    public String toString() {
        String info = "Bicicleta{" +
                "codigo='" + codigo + '\'' +
                ", tipo='" + tipo + '\'' +
                ", enDeposito=" + enDeposito +
                ", estado=" + estado;
        if (motivoMantenimiento != null) {
            info += ", motivo='" + motivoMantenimiento + "'";
        }
        info += '}';
        return info;
    }
    
     @Override
    public int compareTo(Bicicleta otra) {
        if (otra == null) return 1; // consideramos null menor
        return this.codigo.compareTo(otra.codigo);
    }

}



