/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaAutogestion;

import dominio.*;
import tads.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Test2_05MarcarEnMantenimiento {

    private Sistema s;
    private Retorno retorno;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion(); // inicializa deposito, usuarios y estaciones
    }

    @Test
    public void marcarEnMantenimientoExitoso() {
        s.registrarBicicleta("ABC123", "URBANA");
        retorno = s.marcarEnMantenimiento("ABC123", "Frenos rotos");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    public void marcarEnMantenimientoError1() {
        retorno = s.marcarEnMantenimiento(null, "Motivo");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.marcarEnMantenimiento("ABC123", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.marcarEnMantenimiento("", "Motivo");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.marcarEnMantenimiento("ABC123", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void marcarEnMantenimientoError2() {
        // Bicicleta inexistente
        retorno = s.marcarEnMantenimiento("ZZZ999", "Motivo");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void marcarEnMantenimientoError4() {
        // Bicicleta ya en mantenimiento
        s.registrarBicicleta("ABC123", "URBANA");
        s.marcarEnMantenimiento("ABC123", "Frenos rotos");
        retorno = s.marcarEnMantenimiento("ABC123", "Cambio de cadena");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }


    
    @Test
    public void marcarEnMantenimientoError3Simulado() {
        s.registrarBicicleta("DEF456", "MOUNTAIN");
        // Hardcodeando alquiler
        Bicicleta bici = s.getDeposito().buscar("DEF456");
        bici.setEstado(EstadoBicicleta.Alquilada);

        retorno = s.marcarEnMantenimiento("DEF456", "Problema simulada");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
    
    
    //Test profe
    @Test
    public void marcarEnMantenimiento_Ok_y_RepeticionDaError04() {
        s.registrarBicicleta("M00001", "URBANA");
        retorno = s.marcarEnMantenimiento("M00001", "Rueda pinchada");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Marcar nuevamente -> ya en mantenimiento
        retorno = s.marcarEnMantenimiento("M00001", "otro motivo");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }
    
     /* Nota: ERROR_3 “alquilada” no se prueba en primera entrega, ya que alquilar (2.9) no está requerido. */
    
        @Test
     public void biciAlquiladaOK(){
         s.registrarUsuario("12345678", "Ana");
         s.registrarEstacion("Estacion01", "Centro", 1);
         s.registrarBicicleta("M00001", "URBANA");
         s.asignarBicicletaAEstacion("M00001", "Estacion01");
         s.alquilarBicicleta("12345678", "Estacion01");
        //System.out.println("Bici actual del usuario: " + s.getUsuarios().buscar("1234567").getBicicletaActual());

         retorno = s.marcarEnMantenimiento("M00001", "Rueda pinchada");
         assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
     }
   
}

