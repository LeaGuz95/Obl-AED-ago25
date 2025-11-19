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
        s.registrarUsuario("12345678", "Juan");
        s.registrarUsuario("87654321", "Ana");


        // Bicicletas comunes
        s.registrarBicicleta("B00001", "URBANA");
        s.registrarBicicleta("B00002", "MOUNTAIN");

        // Para algunos tests será necesario asignarlas
    }

    // ------------------------------------------------------------------------
    // TESTS
    // ------------------------------------------------------------------------

   @Test
    public void alquilarBicicleta_Exito() {
        // Preparar: asignar bici a estación
        s.asignarBicicletaAEstacion("B00001", "EST1");

        // Usar usuario válido
        retorno = s.alquilarBicicleta("12345678", "EST1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Usuario debe tener la bici
        assertNotNull(s.getUsuarios().buscar("12345678").getBicicletaActual());
        assertEquals("B00001", s.getUsuarios().buscar("12345678").getBicicletaActual().getCodigo());

        // La bici ya no debe estar en anclajes
        assertNull(s.getEstaciones().buscar("EST1").getAnclajes().buscar("B00001"));
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

   @Test
    public void alquilarBicicleta_Error3_EstacionNoExiste() {
        retorno = s.alquilarBicicleta("12345678", "NOEXISTE");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
    @Test
    public void alquilarBicicleta_SinBicicletas_UsuarioEnCola() {
        // No asignamos ninguna bici → no hay disponibles
        retorno = s.alquilarBicicleta("12345678", "EST1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Debe haber quedado en la cola
        assertEquals(1, s.getEstaciones().buscar("EST1").getEsperaAlquiler().longitud());

        // Usuario NO debe tener bicicleta
        assertNull(s.getUsuarios().buscar("12345678").getBicicletaActual());
    }

    @Test
    public void alquilarBicicleta_ColaYAsignacionPosterior() {
        // 1) Juan pide bici → no hay → queda en cola
        retorno = s.alquilarBicicleta("12345678", "EST1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // 2) Luego llega una bicicleta a la estación
        s.asignarBicicletaAEstacion("B00001", "EST1");

        // Debe entregarse automáticamente
        assertNotNull(s.getUsuarios().buscar("12345678").getBicicletaActual());
        assertEquals("B00001", s.getUsuarios().buscar("12345678").getBicicletaActual().getCodigo());

        // La cola debe quedar vacía
        assertEquals(0, s.getEstaciones().buscar("EST1").getEsperaAlquiler().longitud());
    }

    @Test
    public void alquilarBicicleta_ColaDosUsuarios_RespetaOrden() {
        // Juan queda primero en la cola
        s.alquilarBicicleta("12345678", "EST1");

        // Ana queda detrás
        s.alquilarBicicleta("87654321", "EST1");

        // Primera bici llega → es para Juan
        s.asignarBicicletaAEstacion("B00001", "EST1");

        assertEquals("B00001", s.getUsuarios().buscar("12345678").getBicicletaActual().getCodigo());
        assertNull(s.getUsuarios().buscar("87654321").getBicicletaActual());

        // Segunda bici llega → ahora Ana
        s.asignarBicicletaAEstacion("B00002", "EST1");

        assertEquals("B00002", s.getUsuarios().buscar("87654321").getBicicletaActual().getCodigo());
    }

}
