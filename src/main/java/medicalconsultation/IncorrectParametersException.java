package medicalconsultation;

public class IncorrectParametersException extends Exception {
    public IncorrectParametersException() {
        super("Error en los parámetros proporcionados.");
    }

    public IncorrectParametersException(String message) {
        super(message);
    }
}
