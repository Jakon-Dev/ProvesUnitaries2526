package medicalconsultation;

// El producto ya existe en la lista al intentar añadirlo
public class ProductAlreadyInPrescriptionException extends Exception {
    public ProductAlreadyInPrescriptionException() {
        super("El producto ya se encuentra en la prescripción médica.");
    }

    public ProductAlreadyInPrescriptionException(String message) {
        super(message);
    }
}
