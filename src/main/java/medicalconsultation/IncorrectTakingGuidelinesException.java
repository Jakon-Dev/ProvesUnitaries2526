package medicalconsultation;

// Formato incorrecto en pautas o posología
public class IncorrectTakingGuidelinesException extends Exception {
    public IncorrectTakingGuidelinesException() {
        super("El formato de la pauta o posología es incorrecto, o bien la información es incompleta.");
    }

    public IncorrectTakingGuidelinesException(String message) {
        super(message);
    }
}
