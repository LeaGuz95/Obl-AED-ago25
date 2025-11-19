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
import dominio.Bicicleta;

public class Test2_08AsignarBicicletaEstacion {

    private Sistema s;
    private Retorno retorno;

    @Before
    public void setUp() {
        s = new Sistema();
        s.crearSistemaDeGestion();

        // Estaciones comunes para las pruebas
        s.registrarEstacion("EST1", "Centro", 2);
        s.registrarEstacion("EST2", "Cordón", 1);

        // Bicicletas comunes para pruebas
        s.registrarBicicleta("B00001", "URBANA");
        s.registrarBicicleta("B00002", "MOUNTAIN");
        s.registrarBicicleta("B00003", "URBANA");

    }

    // -------------------------------
    //   TESTS
    // -------------------------------
        @Test
        public void asignarBicicleta_Exito() {
            retorno = s.asignarBicicletaAEstacion("B00001", "EST1");
            assertEquals(Retorno.Resultado.OK, retorno.getResultado());

            assertNotNull(
                s.getEstaciones().buscar("EST1")
                 .getAnclajes().buscar("B00001")
            );
        }


    @Test
    public void asignarBicicleta_Error1() {
        retorno = s.asignarBicicletaAEstacion(null, "EST1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("", "EST1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("B00001", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("B00001", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    //arreglar
    @Test
    public void asignarBicicleta_Error2_BiciNoExisteODisponible() {
        // bici no existe
        retorno = s.asignarBicicletaAEstacion("NOEXISTE", "EST1");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        // asignarla una vez → OK
        s.asignarBicicletaAEstacion("B00001", "EST1");

        // volver a asignarla → ya no está disponible
        retorno = s.asignarBicicletaAEstacion("B00001", "EST2");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }
    
    @Test
    public void asignarBicicleta_Error3_EstacionNoExiste() {
        retorno = s.asignarBicicletaAEstacion("B00001", "NOEXISTE");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
    @Test
    public void asignarBicicleta_Error4_SinAnclajesLibres() {
        // EST2 solo tiene 1 anclaje
        s.asignarBicicletaAEstacion("B00001", "EST2");

        // Intentar asignar otra → no hay lugar
        retorno = s.asignarBicicletaAEstacion("B00002", "EST2");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }

    @Test
    public void asignarBicicleta_DesdeOtraEstacion() {
        // Asignar a EST1
        s.asignarBicicletaAEstacion("B00003", "EST1");

        // Mover a EST2
        retorno = s.asignarBicicletaAEstacion("B00003", "EST2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // verificar que salió de EST1
        assertNull(s.getEstaciones().buscar("EST1")
                    .getAnclajes().buscar("B00003"));

        // verificar que entró a EST2
        assertNotNull(s.getEstaciones().buscar("EST2")
                       .getAnclajes().buscar("B00003"));
    }

}

