/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaAutogestion;
import dominio.Usuario;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author ljgp2
 */





import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_08RankingPorTipoUso {

   private Sistema s;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();

        // Registrar usuario para alquiler
        s.registrarUsuario("00000001", "Pepe");

        // Registrar estación
        s.registrarEstacion("Est1", "Centro", 5);
    }

    @Test
    public void testRankingBasico() {
        // Registrar bicicletas
        s.registrarBicicleta("B00001", "URBANA");
        s.registrarBicicleta("B00002", "MOUNTAIN");
        s.registrarBicicleta("B00003", "URBANA");
        s.registrarBicicleta("B00004", "ELECTRICA");
        // Asignar a estación
        s.asignarBicicletaAEstacion("B00001", "Est1");
        s.asignarBicicletaAEstacion("B00002", "Est1");
        s.asignarBicicletaAEstacion("B00003", "Est1");
        s.asignarBicicletaAEstacion("B00004", "Est1");

        // Simular alquileres usando la función pública
        s.alquilarBicicleta("00000001", "Est1"); // B00001 alquilada
        s.devolverBicicleta("00000001", "Est1"); //Regresar

        s.alquilarBicicleta("00000001", "Est1"); // B00002 alquilada
        s.devolverBicicleta("00000001", "Est1");

        // Llamar rankingTiposPorUso
        Retorno r = s.rankingTiposPorUso();

        // Esperado: 
        String valorEsperado = "URBANA#2|ELECTRICA#0|MOUNTAIN#0"; 
        assertEquals(valorEsperado, r.getValorString());
    }
}
