package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_02RegistrarEstacion {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    @Test
    public void registrarEstacionOk() {
        retorno = s.registrarEstacion("Estacion01", "Centro", 5);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    public void registrarEstacionError01() {
        retorno = s.registrarEstacion("", "Centro", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEstacion("Estacion01", "", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEstacion("   ", "Centro", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEstacion("Estacion01", "   ", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEstacion(null, "Centro", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEstacion("Estacion01", null, 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void registrarEstacionError02() {
        retorno = s.registrarEstacion("Estacion01", "Centro", 0);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarEstacion("Estacion01", "Centro", -10);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void registrarEstacionError03() {
        s.registrarEstacion("Estacion01", "Centro", 5);
        retorno = s.registrarEstacion("Estacion01", "Centro", 5);
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());

    }

    //testeos del profe
    
    @Test
    public void registrarEstacion_OK_Multiples() {
        // Capacidad mínima válida (=1) y distintas estaciones deben registrarse OK
        retorno = s.registrarEstacion("E1", "Centro", 1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarEstacion("E2", "Cordón", 10);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        
        retorno = s.registrarEstacion("E3", "Ciudad Vieja", 10);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }
    
     @Test
    public void registrarEstacion_Error01_ParametrosNulosOVacios() {
        // nombre vacío / nulo / en blanco
        retorno = s.registrarEstacion("", "Centro", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEstacion("   ", "Centro", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEstacion(null, "Centro", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        // barrio vacío / nulo / en blanco
        retorno = s.registrarEstacion("Estacion01", "", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEstacion("Estacion01", "   ", 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEstacion("Estacion01", null, 5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void registrarEstacion_Error02_CapacidadNoPositiva() {
        retorno = s.registrarEstacion("Estacion01", "Centro", 0);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarEstacion("Estacion02", "Centro", -3);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void registrarEstacion_Error03_DuplicadoPorNombre() {
        // Nombre único: si ya existe "EstacionX", cualquier intento con el mismo nombre debe fallar,
        // aunque cambien barrio o capacidad.
        s.registrarEstacion("EstacionX", "Centro", 5);
        s.registrarEstacion("E1", "Centro", 1);

        retorno = s.registrarEstacion("EstacionX", "Centro", 5);
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());

        retorno = s.registrarEstacion("EstacionX", "Pocitos", 7);
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }


}
