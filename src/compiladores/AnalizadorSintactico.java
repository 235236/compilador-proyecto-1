package compiladores;

import java.io.IOException;

/**
 * Clase principal del analizador sintactico predictivo.
 *
 * <ul>
 * <li>prog → conjProds ;</li>
 * <li>conjProds → conjProds | prod</li>
 * <li>prod → variable DEF expr ;</li>
 * <li>expr → expr ALT term | term</li>
 * <li>term → term & factor | factor</li>
 * <li>factor → {expr} | [expr] | primario</li>
 * <li>primario → (expr) | variable | terminal</li>
 * </ul>
 *
 * @author David Alberto Torres Real 235236
 * @version 1.0
 * @since 09/03/2015
 */
public class AnalizadorSintactico {

    private static final int CONCATENACION = '&';
    private static final int ALTERNACION = '|';
    private static final int LLAVE_IZQ = '{';
    private static final int LLAVE_DER = '}';
    private static final int CORCHETE_IZQ = '[';
    private static final int CORCHETE_DER = ']';
    private static final int DEFINICION = ':';
    private static final int DEFINICION2 = '=';
    private static final int FIN_PROD = ';';
    private static final int FIN_ARCH = '.';
    private static final int PANG_IZQ = '<';
    private static final int PANG_DER = '>';
    private static final int PAR_IZQ = '(';
    private static final int PAR_DER = ')';
    //Se utilizará la , (coma) en sustitución del ' (apostrofe).
    private static final int APOS = ',';

    private int token;
    private String notacion;

    /**
     * Constructor principal de la clase.
     */
    public AnalizadorSintactico() {
        this.token = -1;
        this.notacion = "";
    }

    public void iniciarAnalisis() throws IOException {
        prod();
    }

    public static void main(String[] args) {
        try {
            AnalizadorSintactico analizador = new AnalizadorSintactico();
            analizador.iniciarAnalisis();
        } catch (IOException ex) {
            System.out.println("Ocurrio una anomalia: " + ex.getMessage());
        }
    }

    private void prog() throws IOException {
        this.siguiente(this.token);
        this.conjProd();
    }

    private void conjProd() throws IOException {
        this.siguiente(this.token);
        this.prod();
    }

    /**
     * Hace referencia a una producción gramatical, verifica que se compongan
     * los elementos de las demas producciones mas el final de producción y/o
     * final de archivo.
     *
     * @throws IOException cuando no existe una entrada.
     */
    private void prod() throws IOException {
        this.siguiente(this.token);
        this.variable();
        this.siguiente(this.token);
        this.definicion();
        this.siguiente(this.token);
        this.expr();
        this.siguiente(this.token);
        if (this.token == FIN_PROD || this.token == FIN_ARCH) {
            System.out.println(this.notacion + (char) DEFINICION
                    + (char) DEFINICION + (char) DEFINICION2);
        } else {
            throw new Error(String.format(
                    "Error de Sintaxis: Se esperaba (%s) ó (%s)",
                    (char) FIN_PROD, (char) FIN_ARCH));
        }
    }

    /**
     * Verifica si una expresión es un termino ó si es una alternación entre una
     * expresión y un termino.
     *
     * @throws IOException cuando no existe una entrada valida.
     */
    private void expr() throws IOException {
        if (this.token != FIN_PROD || this.token != FIN_ARCH) {
            if (this.token == PANG_IZQ) {
                this.notacion = String.format("%s%s", this.notacion,
                        (char) this.token);
                this.siguiente(this.token);
                while (this.token != (char) ALTERNACION) {
                    this.notacion = String.format("%s%s", this.notacion,
                            (char) this.token);
                    this.siguiente(this.token);
                    if (this.token == PANG_DER) {
                        this.notacion = String.format("%s%s", this.notacion,
                                (char) this.token);
                        this.term();
                        this.notacion = String.format("%s%s", this.notacion,
                                (char) ALTERNACION);
                    }
                }
            } else {
                this.term();
            }
        }

//        this.term();
//        while (Character.isAlphabetic((char) this.token)
//                || Character.isDigit((char) this.token)) {
//            this.notacion = String.format("%s%s", this.notacion,
//                    (char) this.token);
//            this.siguiente(this.token);
//            if (this.token == ((char) ALTERNACION)) {
//                this.term();
//            }
//        }
//
//        while (this.token != ((char) ALTERNACION)) {
//            this.notacion = String.format("%s%s", this.notacion,
//                    (char) this.token);
//            this.siguiente(this.token);
//        }
//        //this.term();
//        this.notacion = String.format("%s%s", this.notacion,
//                (char) ALTERNACION);
//        this.siguiente(this.token);
    }

    /**
     * Verifica si el termino es un factor ó si es una concatenación de un
     * termino y un factor.
     *
     * @throws IOException cuando no existe una entrada valida.
     */
    private void term() throws IOException {
        this.notacion = String.format("%s%sOK", this.notacion,
                (char) this.token);
        while (this.token != ((char) CONCATENACION)) {
            this.notacion = String.format("%s%s", this.notacion,
                    (char) this.token);
            this.siguiente(this.token);
        }
        this.factor();
    }

    private void factor() throws IOException {
        if (this.token == (char) LLAVE_IZQ) {
            this.siguiente(this.token);
            while (this.token != LLAVE_DER) {
                this.notacion = String.format("%s%s", this.notacion,
                        (char) this.token);
                this.siguiente(this.token);
            }
            this.notacion = String.format("%s%s", this.notacion,
                    (char) LLAVE_DER);
        } else if (this.token == (char) CORCHETE_IZQ) {
            this.siguiente(this.token);
            while (this.token != CORCHETE_DER) {
                this.notacion = String.format("%s%s", this.notacion,
                        (char) this.token);
                this.siguiente(this.token);
            }
            this.notacion = String.format("%s%s", this.notacion,
                    (char) CORCHETE_DER);
        } else {
            this.primario();
        }
    }

    private void primario() throws IOException {
        if (this.token == (char) PAR_IZQ) {
            this.siguiente(this.token);
            while (this.token != (char) PAR_DER) {
                if (Character.isAlphabetic((char) this.token)
                        || Character.isDigit((char) this.token)) {
                    this.notacion = String.format("%s%s", this.notacion,
                            (char) this.token);
                    this.siguiente(this.token);
                }
            }
        } else if (this.token == PANG_IZQ) {
            this.variable();
        } else {
            this.siguiente(this.token);
            this.terminal();
        }
    }

    /**
     * Verifica que la estructura de una variable se encuentre dentro de < y
     * >, que su nombre sea uno o mas caracteres de letras o numeros siempre y
     * cuando el primer caracter sea una letra.
     *
     * @throws IOException cuando no existe una entrada.
     */
    private void variable() throws IOException {
        if (this.token == PANG_IZQ) {
            this.notacion = String.format("%s%s", this.notacion,
                    (char) this.token);
            this.siguiente(this.token);
            if (Character.isAlphabetic((char) this.token)) {
                this.notacion = String.format("%s%s", this.notacion,
                        (char) this.token);
                this.siguiente(this.token);
                while (Character.isAlphabetic((char) this.token)
                        || Character.isDigit((char) this.token)) {
                    this.notacion = String.format("%s%s", this.notacion,
                            (char) this.token);
                    this.siguiente(this.token);
                }
                if (this.token == PANG_DER) {
                    this.notacion = String.format("%s%s", this.notacion,
                            (char) this.token);
                    this.siguiente(this.token);
                } else {
                    throw new Error(String.format(
                            "Error de Sintaxis: Se esperaba (%s).",
                            (char) PANG_DER));
                }
            } else if (Character.isDigit((char) this.token)) {
                throw new Error(String.format(
                        "Error de Sintaxis, token NO valido: %s, se esperaba "
                        + "caracter.", (char) token));
            }
        } else {
            throw new Error(String.format(
                    "Error de Sintaxis: Se esperaba (%s).", (char) PANG_IZQ));
        }

    }

    /**
     * Verifica que una definición este conformada por la secuencia de
     * caracteres ::=
     *
     * @throws IOException cuando no existe una entrada.
     */
    private void definicion() throws IOException {
        if (this.token == DEFINICION) {
            this.siguiente(this.token);
            if (this.token == DEFINICION) {
                this.siguiente(this.token);
                if (this.token == DEFINICION2) {
                    this.notacion = String.format("%s%s", this.notacion,
                            (char) this.token);
                    this.siguiente(this.token);
                }
            }
        } else {
            throw new Error(String.format(
                    "Error de Sintaxis: Se esperaba (%s%s%s).",
                    (char) DEFINICION, (char) DEFINICION, (char) DEFINICION2));
        }
    }

    /**
     * Verifica que una terminal este conformada por una secuencia de caracteres
     * entre apostrofes ''
     *
     * @throws IOException cuando no existe una entrada.
     */
    private void terminal() throws IOException {
        if (this.token != APOS) {
            this.notacion = String.format("%s'", this.notacion);
            while (this.token != APOS) {
                if (Character.isAlphabetic((char) this.token)
                        || Character.isDigit((char) this.token)) {
                    this.notacion = String.format("%s%s", this.notacion,
                            (char) this.token);
                    this.siguiente(this.token);
                }
            }
            this.notacion = String.format("%s'", this.notacion);
        }
    }

    /**
     * Verifica que la secuencia de tokens sea valida y no sea corrupta.
     *
     * @param token caracter numerico.
     *
     * @throws IOException cuando no existe una entrada valida.
     */
    private void siguiente(int token) throws IOException {
        //código de llamada a siguiente token
        if (this.token == token) {
            this.token = System.in.read();
        } else {
            throw new Error(String.format(
                    "Error de Sintaxis, token NO valido: %s", (char) this.token));
        }
    }
}
