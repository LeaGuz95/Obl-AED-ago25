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

public class Test2_03TestRegistrarUsuario {

    private Sistema s;
    private Retorno retorno;

    @Before
    public void setUp() {
        s = new Sistema(); 
        s.crearSistemaDeGestion();
    }

    @Test
    public void registrarUsuarioExitoso() {
        retorno = s.registrarUsuario("12345678", "Juan Perez");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    public void registrarUsuarioError1() {
        // Parámetros null o vacíos
        retorno = s.registrarUsuario(null, "Juan Perez");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarUsuario("12345678", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarUsuario("", "Juan Perez");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarUsuario("12345678", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void registrarUsuarioError2() {
        // Cédula con formato inválido
        retorno = s.registrarUsuario("1234", "Juan Perez"); // menos de 8 dígitos
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarUsuario("ABCDEFGH", "Juan Perez"); // no numérica
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void registrarUsuarioError3() {
        // Usuario duplicado
        s.registrarUsuario("12345678", "Juan Perez"); // primero lo registro
        retorno = s.registrarUsuario("12345678", "Otro Nombre");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
    
    //test profe
    @Test
    public void registrarUsuario_Ok() {
        retorno = s.registrarUsuario("12345678", "Ana");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarUsuario("12345671", "Pedro");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarUsuario("42345678", "Martina");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }


    @Test
    public void registrarUsuario_Error02_FormatoCedula() {
        retorno = s.registrarUsuario("1234567", "Ana");     // 7 dígitos
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
        retorno = s.registrarUsuario("123456789", "Ana");   // 9 dígitos
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
        retorno = s.registrarUsuario("12A45678", "Ana");    // no numérica
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void registrarUsuario_Error03_Duplicado() {
        s.registrarUsuario("87654321", "Luis");
        s.registrarUsuario("87654324", "Pedro");
        s.registrarUsuario("47654321", "Marcelo");
        retorno = s.registrarUsuario("87654321", "Luis");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
}
