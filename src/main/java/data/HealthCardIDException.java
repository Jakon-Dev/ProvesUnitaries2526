package data;

public class HealthCardIDException extends Exception {
    public HealthCardIDException() {
        super("El identificador del paciente (HealthCardID) es inválido o no está registrado en el HNS.");
    }

    public HealthCardIDException(String message) {
        super(message);
    }
}

