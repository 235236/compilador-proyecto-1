package test.compiladores;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import compiladores.AnalizadorSintactico;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author David Alberto Torres Real 235236
 */
public class AnalizadorSintacticoTest {
    
    private ByteArrayOutputStream salida;
    private InputStream entrada;
    
//    @BeforeClass
//    public static void setUpClass() {
//    }
//    
//    @AfterClass
//    public static void tearDownClass() {
//    }
    
    /**
     * Antes de ejecutar una prueba se cambian las entradas y salidas por
     * variables de la prueba.
     */
    @Before
    public void setUp() {
        this.entrada = System.in;
        this.salida = new ByteArrayOutputStream();
        System.setOut(new PrintStream(salida));
    }
    
    /**
     * Después de ejecutar una prueba vuelve a colocar las entradas y salidas
     * correctamente.
     */
    @After
    public void tearDown() {
        System.setIn(this.entrada);
        System.setOut(null);
    }
    
    /**
     * Prueba que verifica que la estructura de una variable se encuentre
     * dentro de < y >, que su nombre sea uno o mas caracteres de letras ó
     * numeros siempre y cuando el primer caracter sea una letra.
     * 
     * @throws IOException cuando las entradas ó salidas son incorrectas.
     */
    @Test
    public void verificarVariable() throws IOException {
        String exp = "<abc>::=<def>;";
        this.setData(exp);
        AnalizadorSintactico analizador = new AnalizadorSintactico();
        analizador.iniciarAnalisis();
        assertTrue(salida.toString().trim().equals("<abc>"));
    }

    /**
     * Asigna un valor o valores al compilador simulando entrada por consola.
     *
     * @param data una cadena a evaluar por el compilador.
     */
    private void setData(String data) {
        System.setIn(new ByteArrayInputStream(String.format("%s", data).getBytes()));
    }
}