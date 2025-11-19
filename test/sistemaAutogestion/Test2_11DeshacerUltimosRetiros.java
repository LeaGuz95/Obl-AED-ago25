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

    // Bicicletas en depósito (6 caracteres)
    s.registrarBicicleta("B00001", "URBANA");
    s.registrarBicicleta("B00002", "URBANA");
    s.registrarBicicleta("B00003", "URBANA");

    // Usuarios (8 dígitos)
    s.registrarUsuario("10000001", "Ana");
    s.registrarUsuario("20000002", "Luis");
    s.registrarUsuario("30000003", "Sara");

    // Asignar bicis a EST1 para poder alquilar
    s.asignarBicicletaAEstacion("B00001", "EST1");
    s.asignarBicicletaAEstacion("B00002", "EST1");
    s.asignarBicicletaAEstacion("B00003", "EST1");

    // Generar RETIROS (quedan en historial)
    s.alquilarBicicleta("10000001", "EST1"); // Retiro 1
    s.alquilarBicicleta("20000002", "EST1"); // Retiro 2
    s.alquilarBicicleta("30000003", "EST1"); // Retiro 3
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
        // Deshacer los últimos 2 retiros (Sara y Luis)
        retorno = s.deshacerUltimosRetiros(2);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Se deshacen los últimos 2 retiros: primero Sara, luego Luis
        String esperado = "B00003#30000003#EST1|B00002#20000002#EST1";
        assertEquals(esperado, retorno.getValorString());

        // Validación básica: usuarios sin bici
        Usuario uSara = s.getUsuarios().buscar("30000003");
        Usuario uLuis = s.getUsuarios().buscar("20000002");

        assertNull(uSara.getBicicletaActual());
        assertNull(uLuis.getBicicletaActual());

        // Ana todavía tiene su bici
        Usuario uAna = s.getUsuarios().buscar("10000001");
        assertNotNull(uAna.getBicicletaActual());
    }

    @Test
    public void deshacerRetirosOK_TodosLosDisponibles() {
        // Pedimos más retiros que los disponibles, debe deshacer todos los existentes
        retorno = s.deshacerUltimosRetiros(10); // hay solo 3

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Deben aparecer los 3 retiros en orden LIFO: último retirado primero
        String esperado = "B00003#30000003#EST1|B00002#20000002#EST1|B00001#10000001#EST1";
        assertEquals(esperado, retorno.getValorString());

        // Todos los usuarios quedan sin bici
        assertNull(s.getUsuarios().buscar("10000001").getBicicletaActual());
        assertNull(s.getUsuarios().buscar("20000002").getBicicletaActual());
        assertNull(s.getUsuarios().buscar("30000003").getBicicletaActual());
    }
}
