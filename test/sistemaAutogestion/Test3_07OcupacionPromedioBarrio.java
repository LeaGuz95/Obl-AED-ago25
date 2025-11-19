/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaAutogestion;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ljgp2
 */
public class Test3_07OcupacionPromedioBarrio {

    private Sistema s;
    private Retorno retorno;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();
    }

    @Test
    public void test3_07_OcupacionPromedioBarrio() {

        // Caso ERROR: no hay estaciones
        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        // ---- Construcción de escenario ----

        // Barrio: Aguada
        s.registrarEstacion("E1", "Aguada", 10);
        s.registrarEstacion("E2", "Aguada", 20);

        // Barrio: Pocitos
        s.registrarEstacion("E3", "Pocitos", 10);

        // Bicis para ocupar anclajes
        s.registrarBicicleta("B00001", "URBANA");
        s.registrarBicicleta("B00002", "URBANA");
        s.registrarBicicleta("B00003", "URBANA");
        s.registrarBicicleta("B00004", "URBANA");

        // Ocupamos 5 anclajes en Aguada (E1 + E2)
        s.asignarBicicletaAEstacion("B00001", "E1"); // 1
        s.asignarBicicletaAEstacion("B00002", "E1"); // 2
        s.asignarBicicletaAEstacion("B00003", "E2"); // 3
        s.asignarBicicletaAEstacion("B00004", "E2"); // 4

        s.registrarBicicleta("B00005", "URBANA");
        s.asignarBicicletaAEstacion("B00005", "E2"); // 5

        // Ocupamos 3 anclajes en Pocitos (E3)
        s.registrarBicicleta("B00006", "URBANA");
        s.registrarBicicleta("B00007", "URBANA");
        s.registrarBicicleta("B00008", "URBANA");

        s.asignarBicicletaAEstacion("B00006", "E3");
        s.asignarBicicletaAEstacion("B00007", "E3");
        s.asignarBicicletaAEstacion("B00008", "E3");

        // ---- Expected ----
        // Aguada: 5 / 30 = 16.66 → 17
        // Pocitos: 3 / 10 = 30.0 → 30

        retorno = s.ocupacionPromedioXBarrio();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        String esperado = "Aguada#17|Pocitos#30";
        assertEquals(esperado, retorno.getValorString());
    }
}

