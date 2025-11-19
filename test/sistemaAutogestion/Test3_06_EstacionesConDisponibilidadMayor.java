/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaAutogestion;

/**
 *
 * @author ljgp2
 */
import dominio.Estacion;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import tads.NodoSE;

public class Test3_06_EstacionesConDisponibilidadMayor {

    private Sistema s;
    private Retorno retorno;

@Before
public void setUp() {
    s = new Sistema();
    s.crearSistemaDeGestion();

    // Crear estaciones
    s.registrarEstacion("Est1", "Centro", 5);
    s.registrarEstacion("Est2", "Cordón", 10);
    s.registrarEstacion("Est3", "Tres Cruces", 3);

    // Registrar bicicletas
    s.registrarBicicleta("AAA111", "URBANA");
    s.registrarBicicleta("BBB222", "URBANA");
    s.registrarBicicleta("CCC333", "URBANA");
    s.registrarBicicleta("DDD444", "URBANA");

    // Asignar bicicletas a estaciones SIN entregar automáticamente a usuarios
    Estacion est1 = s.getEstaciones().buscar("Est1");
    Estacion est2 = s.getEstaciones().buscar("Est2");
    Estacion est3 = s.getEstaciones().buscar("Est3");

    est1.anclarBicicleta(s.getDeposito().buscar("AAA111"));
    est2.anclarBicicleta(s.getDeposito().buscar("BBB222"));
    est2.anclarBicicleta(s.getDeposito().buscar("CCC333"));
    est2.anclarBicicleta(s.getDeposito().buscar("DDD444"));
    // Est3 queda vacía
}

@Test
public void testEstacionesConDisponibilidad() {
    // Queremos estaciones con más de 2 bicis disponibles
    retorno = s.estacionesConDisponibilidad(2);

    // Debe retornar OK
    assertEquals(Retorno.Resultado.OK, retorno.getResultado());

    // Solo Est2 tiene >2 bicis disponibles → valorInt = 1
    assertEquals(1, retorno.getValorEntero());
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
