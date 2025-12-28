package services;

public class NotCompletedMedicalPrescription extends Exception {
    public NotCompletedMedicalPrescription() {
        super("La prescripción médica está sin completar.");
    }

    public NotCompletedMedicalPrescription(String message) {
        super(message);
    }
}
