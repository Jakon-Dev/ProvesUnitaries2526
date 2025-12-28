package medicalconsultation;

// Fecha de fin inválida (pasada o muy cercana)
public class IncorrectEndingDateException extends Exception {
    public IncorrectEndingDateException() {
        super("La fecha de finalización proporcionada no es adecuada o está situada en el pasado.");
    }

    public IncorrectEndingDateException(String message) {
        super(message);
    }
}
