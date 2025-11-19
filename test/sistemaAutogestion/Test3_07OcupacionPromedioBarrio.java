/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package sistemaAutogestion;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class Test3_07OcupacionPromedioBarrio {

    private Sistema s;
    private Retorno retorno;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();

        // Crear estaciones y barrios
        s.registrarEstacion("Est1", "Aguada", 5);
        s.registrarEstacion("Est2", "Aguada", 5);
        s.registrarEstacion("Est3", "Pocitos", 10);
        s.registrarEstacion("Est4", "Pocitos", 10);

        // Registrar bicicletas y asignarlas
        s.registrarBicicleta("AAA111", "URBANA");
        s.registrarBicicleta("BBB222", "URBANA");
        s.registrarBicicleta("CCC333", "URBANA");
        s.registrarBicicleta("DDD444", "URBANA");
        s.registrarBicicleta("EEE555", "URBANA");

        s.asignarBicicletaAEstacion("AAA111", "Est1"); // Aguada: 1/5
        s.asignarBicicletaAEstacion("BBB222", "Est2"); // Aguada: 1/5 → total 2/10 = 20%
        s.asignarBicicletaAEstacion("CCC333", "Est3"); // Pocitos: 1/10
        s.asignarBicicletaAEstacion("DDD444", "Est4"); // Pocitos: 1/10 → total 2/20 = 10%
        s.asignarBicicletaAEstacion("EEE555", "Est4"); // Pocitos: 2/10 → total 3/20 = 15%
    }

    @Test
    public void test3_07_OcupacionPromedioBarrio() {
        retorno = s.ocupacionPromedioXBarrio();

        // Debe retornar OK
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // La cadena debe estar ordenada alfabéticamente por barrio
        // Aguada#20|Pocitos#15
        assertEquals("Aguada#20|Pocitos#15", retorno.getValorString());
    }
}
