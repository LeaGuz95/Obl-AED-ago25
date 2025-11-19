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
import org.junit.Before;
import org.junit.Test;

public class Test3_06_EstacionesConDisponibilidadMayor {

    private Sistema s;

    @Before
    public void setUp() {
        s = new Sistema();
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

        // Asignar bicicletas a estaciones
        s.asignarBicicletaAEstacion("AAA111", "Est1"); // Est1: 4 disponibles
        s.asignarBicicletaAEstacion("BBB222", "Est2"); // Est2: 9 disponibles
        s.asignarBicicletaAEstacion("CCC333", "Est2"); // Est2: 8 disponibles
        s.asignarBicicletaAEstacion("DDD444", "Est2"); // Est2: 7 disponibles
                // Est3: 3 disponibles
                
    }

    @Test
    public void test_OK() {
        // Queremos estaciones con disponibilidad > 3
        Retorno ret = s.estacionesConDisponibilidad(3);

        assertEquals(Retorno.Resultado.OK, ret.getResultado());

        // Est1 → 4 disponibles, Est2 → 7 disponibles, Est3 → 3 disponibles → cuenta 2
        assertEquals(2, ret.getValorEntero());
    }

    @Test
    public void test_ERROR1_parametroInvalido() {
        // n <= 1 → ERROR1
        assertEquals(Retorno.Resultado.ERROR_1,
                s.estacionesConDisponibilidad(1).getResultado());

        assertEquals(Retorno.Resultado.ERROR_1,
                s.estacionesConDisponibilidad(0).getResultado());

        assertEquals(Retorno.Resultado.ERROR_1,
                s.estacionesConDisponibilidad(-5).getResultado());
    }
}
