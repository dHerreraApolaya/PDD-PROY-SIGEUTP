package modulo.encuestas.configuracion;
public class FiltroEvaluador {
        public static boolean debeVisualizar(String respuestaAnterior, String operador, String valorEsperado) {
        if (respuestaAnterior == null || operador == null || valorEsperado == null) {
            return false;
        }
     
        String respuesta = respuestaAnterior.trim();
        String valor = valorEsperado.trim();
        operador = operador.trim().toUpperCase();
        
        switch (operador) {
            case "IGUAL":
                return respuesta.equalsIgnoreCase(valor);
                
            case "DISTINTO":
                return !respuesta.equalsIgnoreCase(valor);
                
            case "MAYOR":
                try {
                    double respuestNum = Double.parseDouble(respuesta);
                    double valorNum = Double.parseDouble(valor);
                    return respuestNum > valorNum;
                } catch (NumberFormatException e) {
                    return false;
                }
                
            case "MENOR":
                try {
                    double respuestNum = Double.parseDouble(respuesta);
                    double valorNum = Double.parseDouble(valor);
                    return respuestNum < valorNum;
                } catch (NumberFormatException e) {
                    return false;
                }
                
            case "MAYOR_IGUAL":
                try {
                    double respuestNum = Double.parseDouble(respuesta);
                    double valorNum = Double.parseDouble(valor);
                    return respuestNum >= valorNum;
                } catch (NumberFormatException e) {
                    return false;
                }
                
            case "MENOR_IGUAL":
                try {
                    double respuestNum = Double.parseDouble(respuesta);
                    double valorNum = Double.parseDouble(valor);
                    return respuestNum <= valorNum;
                } catch (NumberFormatException e) {
                    return false;
                }
                
            case "CONTIENE":
                return respuesta.toUpperCase().contains(valor.toUpperCase());
                
            default:
                return false;
        }
    }
   
    public static boolean debeVisualizar(double respuestaAnterior, String operador, double valorEsperado) {
        return debeVisualizar(String.valueOf(respuestaAnterior), operador, String.valueOf(valorEsperado));
    }

    public static boolean debeVisualizar(boolean respuestaAnterior, String operador, String valorEsperado) {
        return debeVisualizar(String.valueOf(respuestaAnterior), operador, valorEsperado);
    }
  
    public static boolean operadorValido(String operador) {
        if (operador == null) return false;
        String op = operador.trim().toUpperCase();
        return op.equals("IGUAL") || 
               op.equals("DISTINTO") || 
               op.equals("MAYOR") || 
               op.equals("MENOR") ||
               op.equals("MAYOR_IGUAL") ||
               op.equals("MENOR_IGUAL") ||
               op.equals("CONTIENE");
    }
 
    public static String getDescripcionFiltro(int idPregunta, String operador, String valor) {
        String operadorDesc = getDescripcionOperador(operador);
        return String.format("Mostrar solo si pregunta %d es %s '%s'", 
                           idPregunta, operadorDesc, valor);
    }
   
    public static String getDescripcionOperador(String operador) {
        if (operador == null) return "DESCONOCIDO";
        
        switch (operador.trim().toUpperCase()) {
            case "IGUAL":
                return "igual a";
            case "DISTINTO":
                return "diferente a";
            case "MAYOR":
                return "mayor que";
            case "MENOR":
                return "menor que";
            case "MAYOR_IGUAL":
                return "mayor o igual a";
            case "MENOR_IGUAL":
                return "menor o igual a";
            case "CONTIENE":
                return "contiene";
            default:
                return operador;
        }
    }
}
