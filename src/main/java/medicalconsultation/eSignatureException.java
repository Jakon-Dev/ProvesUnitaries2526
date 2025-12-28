package medicalconsultation;

// Error al firmar digitalmente
public class eSignatureException extends Exception {
    public eSignatureException() {
        super("Se ha producido un problema al estampar la firma electrónica.");
    }

    public eSignatureException(String message) {
        super(message);
    }
}

