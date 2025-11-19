/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaAutogestion;

import dominio.Estacion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_09UsuariosEsperaAlquiler {

    private Sistema s;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();

        // Registrar estación
        s.registrarEstacion("Est1", "Centro", 3);

        // Registrar usuarios
        s.registrarUsuario("00000001", "Pepe");
        s.registrarUsuario("00000002", "Ana");
        s.registrarUsuario("00000003", "Luis");
    }

    @Test
    public void testUsuariosEnEsperaVacia() {
        Retorno r = s.usuariosEnEspera("Est1");
        assertEquals("", r.getValorString());
    }

    @Test
    public void testUsuariosEnEsperaConUsuarios() {
        // Agregar usuarios a la cola de espera de alquiler
        Estacion est = s.getEstaciones().buscar("Est1");
        est.getEsperaAlquiler().encolar(s.getUsuarios().buscar("00000001"));
        est.getEsperaAlquiler().encolar(s.getUsuarios().buscar("00000002"));
        est.getEsperaAlquiler().encolar(s.getUsuarios().buscar("00000003"));

        Retorno r = s.usuariosEnEspera("Est1");
        assertEquals("00000001|00000002|00000003", r.getValorString());
    }

    @Test
    public void testUsuariosEnEsperaEstacionInexistente() {
        Retorno r = s.usuariosEnEspera("Est999");
        assertEquals(Retorno.Resultado.ERROR_1, r.getResultado());
    }
}