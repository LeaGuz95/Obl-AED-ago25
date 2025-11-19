/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaAutogestion;


import static org.junit.Assert.*;

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

    // ============================================================
    // 1) Caso básico — un usuario tiene más alquileres
    // ============================================================
    @Test
    public void test_UsuarioMayor_CasoBasico() {

        // registrarUsuario(ci, nombre)
        s.registrarUsuario("33333333", "Luis");
        s.registrarUsuario("11111111", "Juan");
        s.registrarUsuario("22222222", "Ana");

        // registrarEstacion(nombre, barrio, capacidad)
        s.registrarEstacion("Est1", "Centro", 10); // capacidad suficiente

        // registrar bicicletas con códigos de 6 dígitos
        s.registrarBicicleta("B00001", "URBANA");
        s.registrarBicicleta("B00002", "URBANA");
        s.registrarBicicleta("B00003", "URBANA");
        s.registrarBicicleta("B00004", "URBANA");
        s.registrarBicicleta("B00005", "URBANA");
        s.registrarBicicleta("B00006", "URBANA");

        // asignar bicicletas a estación
        s.asignarBicicletaAEstacion("B00001", "Est1");
        s.asignarBicicletaAEstacion("B00002", "Est1");
        s.asignarBicicletaAEstacion("B00003", "Est1");
        s.asignarBicicletaAEstacion("B00004", "Est1");
        s.asignarBicicletaAEstacion("B00005", "Est1");
        s.asignarBicicletaAEstacion("B00006", "Est1");

        // simular alquileres
        s.alquilarBicicleta("33333333", "Est1"); // Luis 1
        s.alquilarBicicleta("33333333", "Est1"); // Luis 2
        s.alquilarBicicleta("33333333", "Est1"); // Luis 3

        s.alquilarBicicleta("11111111", "Est1"); // Juan 1
        s.alquilarBicicleta("11111111", "Est1"); // Juan 2

        s.alquilarBicicleta("22222222", "Est1"); // Ana 1

        // verificar usuarioMayor
        Retorno r = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("33333333", r.getValorString());
    }

    // ============================================================
    // 2) Desempate — misma cantidad → gana CI más chica
    // ============================================================
    @Test
    public void test_UsuarioMayor_Desempate() {

        s.registrarUsuario("12345678", "Luis");
        s.registrarUsuario("87654321", "Ana");

        s.registrarEstacion("E1", "Centro", 5);

        // registrar bicicletas con códigos de 6 dígitos
        s.registrarBicicleta("B00001", "URBANA");
        s.registrarBicicleta("B00002", "URBANA");
        s.registrarBicicleta("B00003", "URBANA");
        s.registrarBicicleta("B00004", "URBANA");

        s.asignarBicicletaAEstacion("B00001", "E1");
        s.asignarBicicletaAEstacion("B00002", "E1");
        s.asignarBicicletaAEstacion("B00003", "E1");
        s.asignarBicicletaAEstacion("B00004", "E1");

        // Ambos alquilan la misma cantidad
        s.alquilarBicicleta("12345678", "E1");
        s.alquilarBicicleta("12345678", "E1");

        s.alquilarBicicleta("87654321", "E1");
        s.alquilarBicicleta("87654321", "E1");

        // verificar usuarioMayor → cédula más chica gana
        Retorno r = s.usuarioMayor();
        assertEquals(Retorno.Resultado.OK, r.getResultado());
        assertEquals("12345678", r.getValorString());
    }
}