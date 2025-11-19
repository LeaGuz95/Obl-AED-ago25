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

public class Test2_10DevolverBicicleta {

    private Sistema s;
    private Retorno retorno;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();

        // Estaciones para usar en las pruebas
        s.registrarEstacion("EST1", "Centro", 1);
        s.registrarEstacion("EST2", "Cordón", 2);

        // Bicis
        s.registrarBicicleta("B00001", "URBANA");
        s.registrarBicicleta("B00002", "MOUNTAIN");

        // Usuarios
        s.registrarUsuario("100", "Ana");
        s.registrarUsuario("200", "Luis");
    }

    @Test
    public void devolverBicicletaError1() {
        retorno = s.devolverBicicleta(null, "EST1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.devolverBicicleta("100", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void devolverBicicletaError2_UsuarioNoExiste() {
        retorno = s.devolverBicicleta("999", "EST1");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void devolverBicicletaError2_NoTieneBici() {
        retorno = s.devolverBicicleta("100", "EST1");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }
//ARREGLAR
    @Test
    public void devolverBicicletaError3_EstacionNoExiste() {
        // Darle bici primero
        s.asignarBicicletaAEstacion("B001", "EST1");
        s.alquilarBicicleta("100", "EST1");

        retorno = s.devolverBicicleta("100", "NOEXISTE");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
//ARREGLAR
    @Test
    public void devolverBicicletaOK_Simple() {
        // Preparar: poner bici y alquilarla
        s.asignarBicicletaAEstacion("B00001", "EST1");
        s.alquilarBicicleta("100", "EST1");

        // Devolverla
        retorno = s.devolverBicicleta("100", "EST1");

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Verificar que el usuario ya no tiene bici
        assertNull(s.getUsuarios().buscar("100").getBicicletaActual());
    }
//ARREGLAR
    @Test
    public void devolverBicicleta_EsperaPorAnclaje() {
        // EST1 tiene solo 1 anclaje

        // Asignar 1 bici y alquilarla → queda sin anclajes libres
        s.asignarBicicletaAEstacion("B001", "EST1");
        s.alquilarBicicleta("100", "EST1");

        // Asignar otra bici a otra estación y alquilarla
        s.asignarBicicletaAEstacion("B002", "EST2");
        s.alquilarBicicleta("200", "EST2");

        // Luis quiere devolver en EST1 (pero no hay lugar → va a la cola de espera de anclaje)
        retorno = s.devolverBicicleta("200", "EST1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Ahora Ana devuelve su bici → libera anclaje
        retorno = s.devolverBicicleta("100", "EST1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Luis debería haber anclado automáticamente
        assertNull(s.getUsuarios().buscar("200").getBicicletaActual());
    }
//ARREGLAR
    @Test
    public void devolverBicicleta_EntregaAutomaticaAlEsperando() {
        // Un usuario espera por alquiler en EST1

        s.asignarBicicletaAEstacion("B001", "EST1");

        s.registrarUsuario("300", "Marta");

        // Marta pide alquiler en EST1 → queda en cola
        s.alquilarBicicleta("300", "EST1");

        // Ana alquila B001 en EST1
        s.alquilarBicicleta("100", "EST1");

        // Ana devuelve: la bici NO queda disponible,
        // sino que pasa directo al usuario en la cola (Marta)
        retorno = s.devolverBicicleta("100", "EST1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Marta ahora tiene bici
        assertNotNull(s.getUsuarios().buscar("300").getBicicletaActual());
    }
}
