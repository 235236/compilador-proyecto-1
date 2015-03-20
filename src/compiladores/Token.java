package compiladores;

/**
 * El modelo que contiene los elementos que conforman la expresion divididos en
 * los elementos que describe el alfabeto y la gramatica.
 *
 * @author David Alberto Torres Real 235236
 * @since 09/03/2015
 * @version 1.0
 */
public class Token {

    private Integer linea; //linea donde se encuentra
    private int token;
    private String lexema; //cadena o caracter

    /**
     * Constructor basico para generar los token's
     *
     * @param linea posicion donde se localiza el lexema.
     * @param token identificador numerico.
     * @param lexema caracter o cadena de caracteres (valor real).
     */
    public Token(Integer linea, int token, String lexema) {
        this.linea = linea;
        this.token = token;
        this.lexema = lexema;
    }

    public Integer getLinea() {
        return linea;
    }

    public void setLinea(Integer linea) {
        this.linea = linea;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }
    
    /**
     * Método que genera una cadena con los elementos del token.
     * 
     * @return cadena con el formato linea --- token --- lexema.
     */
    @Override
    public String toString() {
        return String.format("%s --- %s --- %s", 
                this.linea, this.token, this.lexema);
    }
}