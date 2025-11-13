/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaAutogestion;

/**
 *
 * @author ljgp2
 */
import dominio.*;
import tads.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Test2_04RegistrarBicicleta {

    private Sistema s;
    private Retorno retorno;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion(); // inicializa deposito, usuarios y estaciones
    }

    @Test
    public void registrarBicicletaExitoso() {
        retorno = s.registrarBicicleta("ABC123", "URBANA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    public void registrarBicicletaError1() {
        // Código o tipo nulos o vacíos
        retorno = s.registrarBicicleta(null, "URBANA");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarBicicleta("ABC123", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarBicicleta("", "URBANA");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarBicicleta("ABC123", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void registrarBicicletaError2() {
        // Código con longitud incorrecta
        retorno = s.registrarBicicleta("ABC12", "URBANA");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarBicicleta("ABC1234", "URBANA");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void registrarBicicletaError3() {
        // Tipo inválido
        retorno = s.registrarBicicleta("ABC123", "INVALIDO");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    @Test
    public void registrarBicicletaError4() {
        // Bicicleta duplicada
        s.registrarBicicleta("ABC123", "URBANA"); // primero se registra
        retorno = s.registrarBicicleta("ABC123", "URBANA"); // intento duplicado
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
        
    }
    
    
    //test profe
    
    @Test
    public void registrarBicicleta_Ok() {
        retorno = s.registrarBicicleta("A00001", "URBANA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarBicicleta("B00002", "MOUNTAIN");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarBicicleta("C00003", "ELECTRICA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }


    @Test
    public void registrarBicicleta_Error02_CodigoFormato() {
        retorno = s.registrarBicicleta("ABC", "URBANA");     // < 6
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
        retorno = s.registrarBicicleta("ABCDE12", "URBANA"); // > 6
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }


   
}