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


public class Test2_06RepararBicicleta {

    private Sistema s;
    private Retorno retorno;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();
    }

    @Test
    public void repararBicicletaExito() {
        // Registrar bicicleta y ponerla en mantenimiento
        s.registrarBicicleta("ABC123", "URBANA");
        Bicicleta bici = s.getDeposito().buscar("ABC123");
        bici.setEstado(EstadoBicicleta.Mantenimiento);

        retorno = s.repararBicicleta("ABC123");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(EstadoBicicleta.Disponible, bici.getEstado());
        assertNull(bici.getMotivoMantenimiento());
    }

    @Test
    public void repararBicicletaError1() {
        retorno = s.repararBicicleta(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.repararBicicleta("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void repararBicicletaError2() {
        retorno = s.repararBicicleta("XYZ999"); // bicicleta no registrada
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void repararBicicletaError3() {
        s.registrarBicicleta("DEF456", "MOUNTAIN");
        // bicicleta está disponible, no en mantenimiento
        retorno = s.repararBicicleta("DEF456");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
    
    
    //test profe
     @Test
    public void repararBicicleta_Ok_DesdeMantenimiento() {
        s.registrarBicicleta("R00001", "URBANA");
        s.registrarBicicleta("R00002", "URBANA");
        s.marcarEnMantenimiento("R00001", "cadena");
        retorno = s.repararBicicleta("R00001");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Reparar otra vez -> ya no está en mantenimiento => ERROR_3
        retorno = s.repararBicicleta("R00001");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
}

