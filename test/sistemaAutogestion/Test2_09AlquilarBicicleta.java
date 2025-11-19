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

public class Test2_09AlquilarBicicleta {

    private Sistema s;
    private Retorno retorno;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();

        // Estación con 2 anclajes
        s.registrarEstacion("EST1", "Centro", 2);

        // Usuarios comunes
        s.registrarUsuario("111", "Juan");
        s.registrarUsuario("222", "Ana");

        // Bicicletas comunes
        s.registrarBicicleta("B001", "URBANA");
        s.registrarBicicleta("B002", "MOUNTAIN");

        // Para algunos tests será necesario asignarlas
    }

    // ------------------------------------------------------------------------
    // TESTS
    // ------------------------------------------------------------------------
//ARREGLAR
    @Test
    public void alquilarBicicleta_Exito() {
        // Preparar: asignar bici a estación
        s.asignarBicicletaAEstacion("B001", "EST1");

        retorno = s.alquilarBicicleta("111", "EST1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Usuario debe tener la bici
        assertNotNull(s.getUsuarios().buscar("111").getBicicletaActual());
        assertEquals("B001", s.getUsuarios().buscar("111").getBicicletaActual().getCodigo());

        // La bici ya no debe estar en anclajes
        assertNull(s.getEstaciones().buscar("EST1").getAnclajes().buscar("B001"));
    }

    @Test
    public void alquilarBicicleta_Error1() {
        retorno = s.alquilarBicicleta(null, "EST1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.alquilarBicicleta("", "EST1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.alquilarBicicleta("111", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.alquilarBicicleta("111", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void alquilarBicicleta_Error2_UsuarioNoExiste() {
        retorno = s.alquilarBicicleta("999", "EST1");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }
//ARREGLAR
    @Test
    public void alquilarBicicleta_Error3_EstacionNoExiste() {
        retorno = s.alquilarBicicleta("111", "NOEXISTE");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
//ARREGLAR
    @Test
    public void alquilarBicicleta_SinBicicletas_UsuarioEnCola() {
        // No asignamos ninguna bici → no hay disponibles
        retorno = s.alquilarBicicleta("111", "EST1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Debe haber quedado en la cola
        assertEquals(1, s.getEstaciones().buscar("EST1").getEsperaAlquiler().longitud());

        // Usuario NO debe tener bicicleta
        assertNull(s.getUsuarios().buscar("111").getBicicletaActual());
    }
//ARREGLAR
    @Test
    public void alquilarBicicleta_ColaYAsignacionPosterior() {
        // 1) Juan pide bici → no hay → queda en cola
        retorno = s.alquilarBicicleta("111", "EST1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // 2) Luego llega una bicicleta a la estación
        s.asignarBicicletaAEstacion("B001", "EST1");

        // Como hay alguien esperando, debería entregarse automáticamente
        // (si tu implementación hace esto en asignarBicicletaAEstacion)
        // o debería entregarse recién cuando se llame a alquilarBicicleta nuevamente.
        // Para este test se asume que asignar entrega automáticamente.

        assertNotNull(s.getUsuarios().buscar("111").getBicicletaActual());
        assertEquals("B001", s.getUsuarios().buscar("111").getBicicletaActual().getCodigo());

        // La cola debe quedar vacía
        assertEquals(0, s.getEstaciones().buscar("EST1").getEsperaAlquiler().longitud());
    }
//ARREGLAR
    @Test
    public void alquilarBicicleta_ColaDosUsuarios_RespetaOrden() {
        // Juan pide → queda en cola
        s.alquilarBicicleta("111", "EST1");

        // Ana pide → queda en cola detrás de Juan
        s.alquilarBicicleta("222", "EST1");

        // Llega bici y se asigna al primero en la cola → Juan
        s.asignarBicicletaAEstacion("B001", "EST1");

        assertEquals("B001", s.getUsuarios().buscar("111").getBicicletaActual().getCodigo());
        assertNull(s.getUsuarios().buscar("222").getBicicletaActual());

        // Llega otra bici → Ana la recibe
        s.asignarBicicletaAEstacion("B002", "EST1");

        assertEquals("B002", s.getUsuarios().buscar("222").getBicicletaActual().getCodigo());
    }
}
