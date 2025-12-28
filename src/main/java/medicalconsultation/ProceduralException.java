package medicalconsultation;

// Error de orden en la secuencia de eventos
public class ProceduralException extends Exception {
    public ProceduralException() {
        super("Operación no válida en el estado actual de la sesión.");
    }
    public ProceduralException(String message) {
        super(message);
    }
}
