package medicalconsultation;

// El producto no existe al intentar modificarlo o borrarlo
public class ProductNotInPrescriptionException extends Exception {
    public ProductNotInPrescriptionException() {
        super("El producto no forma parte de la prescripción médica.");
    }

    public ProductNotInPrescriptionException(String message) {
        super(message);
    }
}
