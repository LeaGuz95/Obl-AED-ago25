/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaAutogestion;

/**
 *
 * @author ljgp2
 */
import static org.junit.Assert.*;
import org.junit.Test;

public class Test3_06_EstacionesConDisponibilidadMayor {
//Arreglar
    @Test
    public void test_OK() {
        Sistema s = new Sistema();
        s.crearSistemaDeGestion();

        // Crear estaciones
        s.registrarEstacion("Est1", "Centro", 5);   // capacidad 5
        s.registrarEstacion("Est2", "Cordón", 10);  // capacidad 10
        s.registrarEstacion("Est3", "Tres Cruces", 3); // capacidad 3

        // Registrar bicicletas
        s.registrarBicicleta("AAA111", "URBANA");
        s.registrarBicicleta("BBB222", "URBANA");
        s.registrarBicicleta("CCC333", "URBANA");
        s.registrarBicicleta("DDD444", "URBANA");

        // Est1 recibe 1 bici → disponibles = 4
        s.asignarBicicletaAEstacion("AAA111", "Est1");

        // Est2 recibe 3 bicis → disponibles = 7
        s.asignarBicicletaAEstacion("BBB222", "Est2");
        s.asignarBicicletaAEstacion("CCC333", "Est2");
        s.asignarBicicletaAEstacion("DDD444", "Est2");

        // Est3 no recibe bicis → disponibles = 3

        // Queremos estaciones con disponibilidad > 3
        Retorno ret = s.estacionesConDisponibilidad(3);

        assertEquals(Retorno.Resultado.OK, ret.getResultado());

        // Est1 → 4 disponibles → cuenta
        // Est2 → 7 disponibles → cuenta
        // Est3 → 3 disponibles → NO cuenta
        assertEquals(2, ret.getValorEntero());
    }


    @Test
    public void test_ERROR1_parametroInvalido() {
        Sistema s = new Sistema();
        s.crearSistemaDeGestion();

        // n <= 1 → ERROR1
        assertEquals(Retorno.Resultado.ERROR_1,
                s.estacionesConDisponibilidad(1).getResultado());

        assertEquals(Retorno.Resultado.ERROR_1,
                s.estacionesConDisponibilidad(0).getResultado());

        assertEquals(Retorno.Resultado.ERROR_1,
                s.estacionesConDisponibilidad(-5).getResultado());
    }
}
