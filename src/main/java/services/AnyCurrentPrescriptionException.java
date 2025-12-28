package services;

public class AnyCurrentPrescriptionException extends Exception {
    public AnyCurrentPrescriptionException() {
        super("No existe ninguna prescripción médica activa del paciente para esa enfermedad.");
    }

    public AnyCurrentPrescriptionException(String message) {
        super(message);
    }
}
