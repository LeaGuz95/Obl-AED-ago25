/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaAutogestion;

/**
 *
 * @author ljgp2
 */
import dominio.Usuario;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class Test2_11DeshacerUltimosRetiros {

    private Sistema s;
    private Retorno retorno;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();

        // Estaciones
        s.registrarEstacion("EST1", "Centro", 3);
        s.registrarEstacion("EST2", "Cordón", 2);

        // Bicicletas en depósito
        s.registrarBicicleta("B001", "URBANA");
        s.registrarBicicleta("B002", "URBANA");
        s.registrarBicicleta("B003", "URBANA");

        // Usuarios
        s.registrarUsuario("100", "Ana");
        s.registrarUsuario("200", "Luis");
        s.registrarUsuario("300", "Sara");

        // Asignar bicis a EST1 para poder alquilar
        s.asignarBicicletaAEstacion("B001", "EST1");
        s.asignarBicicletaAEstacion("B002", "EST1");
        s.asignarBicicletaAEstacion("B003", "EST1");

        // Generar RETIROS (quedan en historial)
        s.alquilarBicicleta("100", "EST1"); // Retiro 1
        s.alquilarBicicleta("200", "EST1"); // Retiro 2
        s.alquilarBicicleta("300", "EST1"); // Retiro 3
    }

    @Test
    public void deshacerRetirosError1() {
        retorno = s.deshacerUltimosRetiros(0);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.deshacerUltimosRetiros(-10);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void deshacerRetirosOK_2() {
        retorno = s.deshacerUltimosRetiros(2);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Se deshacen los últimos 2 retiros: primero Sara, luego Luis
        assertEquals("B003#300#EST1|B002#200#EST1", retorno.getValorString());

        // Validación básica: usuarios sin bici
        Usuario u1 = s.getUsuarios().buscar("300");
        Usuario u2 = s.getUsuarios().buscar("200");

        assertNull(u1.getBicicletaActual());
        assertNull(u2.getBicicletaActual());
    }

    @Test
    public void deshacerRetirosOK_TodosLosDisponibles() {
        retorno = s.deshacerUltimosRetiros(10); // hay solo 3

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Deben aparecer los 3 retiros en orden LIFO
        assertEquals(
            "B003#300#EST1|B002#200#EST1|B001#100#EST1",
            retorno.getValorString()
        );
    }
}
