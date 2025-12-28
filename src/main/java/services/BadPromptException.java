package services;

public class BadPromptException extends Exception {
    public BadPromptException() {
        super("El prompt introducido no es suficientemente claro para la IA o presenta alguna inconsistencia.");
    }

    public BadPromptException(String message) {
        super(message);
    }
}
