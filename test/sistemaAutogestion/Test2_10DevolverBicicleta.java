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
import dominio.Usuario;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_10DevolverBicicleta {

    private Sistema s;
    private Retorno retorno;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();

        // Estaciones
        s.registrarEstacion("EST1", "Centro", 1);
        s.registrarEstacion("EST2", "Cordón", 2);

        // Bicicletas
        s.registrarBicicleta("B00001", "URBANA");
        s.registrarBicicleta("B00002", "MOUNTAIN");

        // Usuarios
        s.registrarUsuario("12345678", "Ana");
        s.registrarUsuario("87654321", "Luis"); 
    }

    @Test
    public void devolverBicicletaError1_ParamNull() {
        retorno = s.devolverBicicleta(null, "EST1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.devolverBicicleta("100", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void devolverBicicletaError2_UsuarioNoExiste() {
        retorno = s.devolverBicicleta("999", "EST1");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void devolverBicicletaError2_SinBici() {
       
        retorno = s.devolverBicicleta("12345678", "EST1");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void devolverBicicletaError3_EstacionNoExiste() {
        // Asignar bici y alquilar
        s.asignarBicicletaAEstacion("B00001", "EST1");
        s.alquilarBicicleta("100", "EST1");

        retorno = s.devolverBicicleta("100", "NOEXISTE");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
//ERROR
  @Test
    public void devolverBicicletaOK_Simple() {
        // Obtener la estación
        Estacion est1 = s.getEstaciones().buscar("EST1");

        // Limpiar posibles usuarios en espera
        while (!est1.getEsperaAlquiler().estaVacia()) {
            est1.getEsperaAlquiler().desencolar();
        }

        // Asignar la bicicleta a la estación
        s.asignarBicicletaAEstacion("B00001", "EST1");

        // Alquilar la bici con cédula válida
        retorno = s.alquilarBicicleta("12345678", "EST1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        Usuario u = s.getUsuarios().buscar("12345678");
        assertNotNull(u.getBicicletaActual());

        // Devolver la bicicleta
        retorno = s.devolverBicicleta("12345678", "EST1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Verificar que usuario ya no tiene bici
        assertNull(u.getBicicletaActual());

        // Verificar que la estación ahora tiene 1 bici disponible
        assertEquals(1, est1.cantidadDisponibles());
    }

//ERROR
    @Test
    public void devolverBicicleta_OK_ConUsuarioEnEspera() {
        // Registrar usuarios con cédula válida
        s.registrarUsuario("12345678", "Ana");
        s.registrarUsuario("87654321", "Luis");

        // Asignar bici y alquilar
        s.asignarBicicletaAEstacion("B00001", "EST1");
        s.alquilarBicicleta("12345678", "EST1");

        // Usuario en espera de alquiler
        Estacion est = s.getEstaciones().buscar("EST1");
        est.getEsperaAlquiler().encolar(s.getUsuarios().buscar("87654321"));

        retorno = s.devolverBicicleta("12345678", "EST1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Usuario 12345678 ya no tiene bici
        assertNull(s.getUsuarios().buscar("12345678").getBicicletaActual());

        // Usuario 87654321 recibió la bici automáticamente
        Usuario u2 = s.getUsuarios().buscar("87654321");
        assertNotNull(u2.getBicicletaActual());
        assertEquals("B00001", u2.getBicicletaActual().getCodigo());

        // No quedan bicicletas disponibles
        assertEquals(0, est.cantidadDisponibles());
    }

  @Test
    public void devolverBicicleta_UsuarioEnEsperaAnclaje() {
        // Registrar usuario
        s.registrarUsuario("12345678", "Ana");

        // Registrar segunda bici
        s.registrarBicicleta("B00002", "URBANA");

        // Asignar bicis a estaciones
        s.asignarBicicletaAEstacion("B00001", "EST1"); // EST1 con capacidad 1, ya está llena
        s.asignarBicicletaAEstacion("B00002", "EST2"); // segunda bici en otra estación

        // Usuario alquila la bici de EST2
        retorno = s.alquilarBicicleta("12345678", "EST2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Intento de devolver en EST1 (llena) → usuario debe ir a espera de anclaje
        retorno = s.devolverBicicleta("12345678", "EST1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        Estacion est = s.getEstaciones().buscar("EST1");

        // Verificar que el usuario está en espera de anclaje
        assertEquals(1, est.getEsperaAnclaje().longitud());

        // Usuario aún no tiene bicicleta
        assertNull(s.getUsuarios().buscar("12345678").getBicicletaActual());
    }


}
