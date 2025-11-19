/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_09UsuariosEsperaAlquiler {

    private Sistema s;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();
    }

    @Test
    public void test_UsuariosEnEspera_OK() {

        // Estación sin bicis → todos los alquileres pasan a espera
        s.registrarEstacion("Est1", "Centro", 2);

        s.registrarUsuario("111", "Juan");
        s.registrarUsuario("222", "Ana");
        s.registrarUsuario("333", "Luis");

        // Intentos de alquiler sin bicis → van a la cola de espera
        s.alquilarBicicleta("111", "Est1");
        s.alquilarBicicleta("222", "Est1");
        s.alquilarBicicleta("333", "Est1");

        Retorno r = s.usuariosEnEspera("Est1");

        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("111|222|333", r.getValorString());
    }

    @Test
    public void test_UsuariosEnEspera_EstacionNoExiste() {

        Retorno r = s.usuariosEnEspera("NoExiste");

        // La letra dice "ERROR" → en tu Retorno eso es ERROR_1
        assertEquals(Retorno.Resultado.ERROR_1, r.getResultado());
    }

    @Test
    public void test_UsuariosEnEspera_Vacia() {
        s.registrarEstacion("Est1", "Centro", 2);

        Retorno r = s.usuariosEnEspera("Est1");

        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("", r.getValorString());
    }
}
