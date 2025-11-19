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

public class Test3_05_ListarBicisDeEstacion {

    //ARREGLAR
     @Test
    public void test_OK() {
        Sistema s = new Sistema();
        s.crearSistemaDeGestion();

        // Crear estación (tu firma real)
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Est1", "Centro", 10).getResultado());

        // Registrar bicicletas (firma real)
        s.registrarBicicleta("AER345", "URBANA");
        s.registrarBicicleta("UYT123", "URBANA");
        s.registrarBicicleta("UTR112", "URBANA");

        // Anclar bicis en orden desordenado
        s.asignarBicicletaAEstacion("UYT123", "Est1");
        s.asignarBicicletaAEstacion("AER345", "Est1");
        s.asignarBicicletaAEstacion("UTR112", "Est1");

        // Ejecutar método
        Retorno ret = s.listarBicicletasDeEstacion("Est1");

        // Debe devolver OK
        assertEquals(Retorno.Resultado.OK, ret.getResultado());

        // Debe devolver ordenadas por código ASC
        assertEquals("AER345|UTR112|UYT123", ret.getValorString());
    }

    @Test
    public void test_ERROR1_parametroInvalido() {
        Sistema s = new Sistema();
        s.crearSistemaDeGestion();

        // nombre vacío → ERROR1
        Retorno r1 = s.listarBicicletasDeEstacion("");
        assertEquals(Retorno.Resultado.ERROR_1, r1.getResultado());

        // null → ERROR1
        Retorno r2 = s.listarBicicletasDeEstacion(null);
        assertEquals(Retorno.Resultado.ERROR_1, r2.getResultado());
    }


    @Test
    public void test_ERROR3_estacionNoExiste() {
        Sistema s = new Sistema();
        s.crearSistemaDeGestion();

        // No existe estación
        Retorno r = s.listarBicicletasDeEstacion("Est1");
        assertEquals(Retorno.Resultado.ERROR_2, r.getResultado());
    }
}
