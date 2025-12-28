package services;

public class AIException extends Exception {
    public AIException() {
        super("Ha ocurrido un problema de funcionamiento en la invocación de la IA.");
    }

    public AIException(String message) {
        super(message);
    }
}


