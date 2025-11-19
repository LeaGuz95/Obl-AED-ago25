/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaAutogestion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author ljgp2
 */





public class Test3_08RankingPorTipoUso {

    private Sistema s;
    private Retorno r;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();
    }

    @Test
    public void testRankingTiposPorUso() {

        // --- Registrar bicicletas ---
        s.registrarBicicleta("AAAA11", "URBANA");
        s.registrarBicicleta("BBBB22", "URBANA");
        s.registrarBicicleta("CCCC33", "MOUNTAIN");
        s.registrarBicicleta("DDDD44", "ELECTRICA");

        // --- Registrar estación ---
        s.registrarEstacion("Est1", "Centro", 10);

        // --- Mover todas a estación ---
        s.asignarBicicletaAEstacion("AAAA11", "Est1");
        s.asignarBicicletaAEstacion("BBBB22", "Est1");
        s.asignarBicicletaAEstacion("CCCC33", "Est1");
        s.asignarBicicletaAEstacion("DDDD44", "Est1");

        // --- Registrar usuarios ---
        s.registrarUsuario("11111111", "Ana");
        s.registrarUsuario("22222222", "Beto");
        s.registrarUsuario("33333333", "Carlos");

        // --- Generar alquileres ---
        // URBANA: 2 alquileres
        s.alquilarBicicleta("11111111", "Est1");
        s.devolverBicicleta("11111111", "Est1");

        s.alquilarBicicleta("22222222", "Est1");
        s.devolverBicicleta("22222222", "Est1");

        // MOUNTAIN: 1 alquiler
        s.alquilarBicicleta("33333333", "Est1");
        s.devolverBicicleta("33333333", "Est1");

        // ELECTRICA: 0 alquileres

        // --- Ejecutar ranking ---
        r = s.rankingTiposPorUso();

        assertEquals(Retorno.Resultado.OK, r.getResultado());

        // URBANA (2) | MOUNTAIN (1) | ELECTRICA (0)
        assertEquals("URBANA#2|MOUNTAIN#1|ELECTRICA#0", r.getValorString());
    }
}

