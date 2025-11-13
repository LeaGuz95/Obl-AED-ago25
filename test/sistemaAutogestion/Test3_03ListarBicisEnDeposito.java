/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaAutogestion;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import dominio.Bicicleta;
import dominio.EstadoBicicleta;

public class Test3_03ListarBicisEnDeposito {

    private Sistema s;
    private Retorno retorno;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();
    }

    @Test
    public void listarBicisVacio() {
        retorno = s.listarBicisEnDeposito();
        
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void listarBicisOK() {
        // Registrar bicicletas
        s.registrarBicicleta("ABC123", "URBANA");
        s.registrarBicicleta("DEF456", "ELECTRICA");
        s.registrarBicicleta("GHI789", "MOUNTAIN");

        // Simular mantenimiento
        Bicicleta bici = s.getDeposito().buscar("DEF456");
        bici.setEstado(EstadoBicicleta.Mantenimiento);

        retorno = s.listarBicisEnDeposito();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        String resultado = retorno.getValorString();
        // Datos de las bicicletas
        assertTrue(resultado.contains("ABC123#URBANA#Disponible"));
        assertTrue(resultado.contains("DEF456#ELECTRICA#Mantenimiento"));
        assertTrue(resultado.contains("GHI789#MOUNTAIN#Disponible"));

        // El orden esperado
        String esperado = "ABC123#URBANA#Disponible|DEF456#ELECTRICA#Mantenimiento|GHI789#MOUNTAIN#Disponible";
        assertEquals(esperado, resultado);
    }
    
    
    //test profe
    
     @Test
    public void listarBicisEnDeposito_Ok_OrdenIngresoYEstados() {
        s.registrarBicicleta("A00001", "URBANA");    // Disponible (depósito)
        s.registrarBicicleta("A00002", "ELECTRICA"); // Disponible (depósito)
        s.registrarBicicleta("A00003", "MOUNTAIN");  // Disponible (depósito)

        // Marcar una en mantenimiento (sigue en depósito)
        s.marcarEnMantenimiento("A00002", "batería");

        retorno = s.listarBicisEnDeposito();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("A00001#URBANA#Disponible|A00002#ELECTRICA#Mantenimiento|A00003#MOUNTAIN#Disponible",
                retorno.getValorString());
    }
}
