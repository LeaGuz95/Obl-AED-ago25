/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_04InformacionMapa {

    private Sistema s;
    private Retorno retorno;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();
    }

    @Test
    public void mapaVacio() {
        String[][] mapa = new String[0][0];
        retorno = s.informaciónMapa(mapa);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("0#ambas|no existe", retorno.getValorString());
    }

    @Test
    public void maximoFilaYAscendentes() {
        String[][] mapa = {
            {"E", null, "E", null},
            {"E", "E", "E", "E"},
            {null, "E", "E", "E"}
        };
 
        retorno = s.informaciónMapa(mapa);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("4#fila|no existe", retorno.getValorString());
    }

    @Test
    public void maximoColumnaYFila() {
        String[][] mapa = {
            {"E", null, null, null, null},
            {"E", "E", null, null, null},
            {"E", "E", "E", null, null},
            {"E", "E", "E", "E", null},
            {"E", "E", "E", "E", "E"},
        };
     

        retorno = s.informaciónMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("5#ambas|no existe", retorno.getValorString());
    }


    
    @Test
    public void tresColumnasConsecutivasAscendentes() {
       
        String[][] mapaFinal = {
            {null, null, "E"},
            {null, "E", "E"},
            {"E", "E", "E"}
        };
       

        Retorno retorno = s.informaciónMapa(mapaFinal);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("3#ambas|si existe", retorno.getValorString());
    }
    
    //test 
    @Test
    public void informacionMapa_Ok_MaxAmbas_y_NoExisteAscendente() {
        String[][] mapa = new String[][]{
            {"E", "E", "", ""}, // fila 0: 2
            {"", "E", "E", ""}, // fila 1: 2
            {"", "", "", ""} // fila 2: 0
        };

        retorno = s.informaciónMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertTrue(retorno.getValorString().startsWith("2#ambas"));
        assertTrue(retorno.getValorString().endsWith("|no existe"));
    }

}
