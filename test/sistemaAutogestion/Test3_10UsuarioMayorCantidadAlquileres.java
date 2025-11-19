/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_10UsuarioMayorCantidadAlquileres {

    private Sistema s;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();
    }

    @Test
    public void test_UsuarioMayor_OK() {

        // Usuarios
        s.registrarUsuario("333", "Luis");
        s.registrarUsuario("111", "Juan");
        s.registrarUsuario("222", "Ana");

        // Estación y bicis
        s.registrarEstacion("Est1", "Centro", 5);

        s.registrarBicicleta("B1", "URBANA");
        s.registrarBicicleta("B2", "URBANA");
        s.registrarBicicleta("B3", "URBANA");

        s.asignarBicicletaAEstacion("B1", "Est1");
        s.asignarBicicletaAEstacion("B2", "Est1");
        s.asignarBicicletaAEstacion("B3", "Est1");

        // Alquileres:
        // Juan = 2
        // Ana  = 1
        // Luis = 3 → debe ganar
        s.alquilarBicicleta("333", "Est1");
        s.alquilarBicicleta("333", "Est1");
        s.alquilarBicicleta("333", "Est1");

        s.alquilarBicicleta("111", "Est1");
        s.alquilarBicicleta("111", "Est1");

        s.alquilarBicicleta("222", "Est1");

        Retorno r = s.usuarioMayor();

        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("333", r.getValorString());
    }

    @Test
    public void test_UsuarioMayor_DesempateConCedula() {

        // Mismo número de alquileres → gana el de CI más chica
        s.registrarUsuario("500", "Luis");
        s.registrarUsuario("100", "Ana");

        s.registrarEstacion("E1", "Centro", 5);

        s.registrarBicicleta("B1", "URBANA");
        s.registrarBicicleta("B2", "URBANA");

        s.asignarBicicletaAEstacion("B1", "E1");
        s.asignarBicicletaAEstacion("B2", "E1");

        // Ambos alquilan 2 veces
        s.alquilarBicicleta("500", "E1");
        s.alquilarBicicleta("500", "E1");

        s.alquilarBicicleta("100", "E1");
        s.alquilarBicicleta("100", "E1");

        Retorno r = s.usuarioMayor();

        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("100", r.getValorString());
    }

    @Test
    public void test_UsuarioMayor_NoHayUsuarios() {

        Retorno r = s.usuarioMayor();

        // Cuando no existen usuarios → error
        assertEquals(Retorno.Resultado.ERROR_1, r.getResultado());
    }
}
