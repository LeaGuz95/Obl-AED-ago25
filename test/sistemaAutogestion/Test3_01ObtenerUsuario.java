package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_01ObtenerUsuario {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    @Test
    public void obtenerUsuarioOk() {
        s.registrarUsuario("12345678", "Usuario01");
        retorno = s.obtenerUsuario("12345678");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Usuario01#12345678", retorno.getValorString());
    }

    @Test
    public void obtenerUsuarioError01() {
        // Parámetro null o vacío
        retorno = s.obtenerUsuario(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.obtenerUsuario("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void obtenerUsuarioError02() {
        // Formato de cédula inválido
        retorno = s.obtenerUsuario("1234");      // menos de 8 dígitos
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.obtenerUsuario("abcdefgh");  // letras
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.obtenerUsuario("1234567a");  // combinación inválida
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void obtenerUsuarioError03() {
        // Usuario no existe
        retorno = s.obtenerUsuario("87654321"); // cedula válida pero no registrada
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
    
    //test profe
    @Test
    public void obtenerUsuario_Ok() {
        s.registrarUsuario("11112222", "Beatriz");
         s.registrarUsuario("11112223", "Carla");
        retorno = s.obtenerUsuario("11112222");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Beatriz#11112222", retorno.getValorString());
    }
}

