package sistemaAutogestion;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import dominio.Bicicleta;

public class Test2_07EliminarEstacion {

    private Sistema s;
    private Retorno retorno;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();

        // Estaciones comunes para las pruebas
        s.registrarEstacion("EST1", "Centro", 3);
        s.registrarEstacion("EST2", "Cordón", 2);
    }

    @Test
    public void eliminarEstacionExito() {
        retorno = s.eliminarEstacion("EST1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Verificar que realmente no exista más 
        assertNull(s.getEstaciones().buscar("EST1"));
    }

    @Test
    public void eliminarEstacionError1() {
        retorno = s.eliminarEstacion(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.eliminarEstacion("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void eliminarEstacionError2() {
        retorno = s.eliminarEstacion("NOEXISTE");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void eliminarEstacionError3_BicicletasEnAnclajes() {
        // registrar bici y asignarla a la estación
        s.registrarBicicleta("B00001", "URBANA");

        // NO registrar usuarios, así la bici permanece anclada
        retorno = s.asignarBicicletaAEstacion("B00001", "EST1");

        // verificar que la asignación fue OK
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // eliminar la estación debería fallar porque tiene bicicletas ancladas
        retorno = s.eliminarEstacion("EST1");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    @Test
    public void eliminarEstacionError3_ColaDeEspera() {
       
        s.registrarUsuario("12345678", "Juan Perez");
        retorno = s.alquilarBicicleta("12345678", "EST1");
   
        assertEquals(Retorno.Resultado.ERROR_3, s.eliminarEstacion("EST1").getResultado());
    }

    @Test
    public void eliminarEstacion_Ok_LuegoIntentarDeNuevo() {
        retorno = s.eliminarEstacion("EST2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Intentar eliminar otra vez → ya no existe
        retorno = s.eliminarEstacion("EST2");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }
}
