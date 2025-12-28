package medicalconsultation;

import data.HealthCardID;
import java.util.Objects;

/**
 * Represents the medical history of a patient within the system.
 * It manages the patient's identification, the assigned doctor, and the cumulative history of annotations.
 */
public class MedicalHistory {

    // The unique Health Card ID of the patient
    private HealthCardID cip;

    // The membership number of the assigned family doctor
    private int membShipNumb;

    // A string containing the diverse annotations in the patient's history
    private String history;

    /**
     * Constructs a new MedicalHistory instance.
     *
     * @param cip           The HealthCardID of the patient.
     * @param memberShipNum The membership number of the doctor.
     * @throws IncorrectParametersException If the CIP is null or the membership number is negative.
     */
    public MedicalHistory(HealthCardID cip, int memberShipNum) throws IncorrectParametersException {
        // Validation: Ensure the CIP is provided
        if (cip == null) {
            throw new IncorrectParametersException("HealthCardID cannot be null");
        }
        // Validation: Ensure the doctor's ID is a valid non-negative number
        if (memberShipNum < 0) {
            throw new IncorrectParametersException("Membership number cannot be negative");
        }

        this.cip = cip;
        this.membShipNumb = memberShipNum;
        // Initialize the history as an empty string to avoid null pointer exceptions later
        this.history = "";
    }

    /**
     * Adds a new annotation to the patient's medical history.
     * It automatically handles line breaks between different annotations.
     *
     * @param annot The annotation string to be added.
     */
    public void addMedicalHistoryAnnotations(String annot) {
        // Only proceed if the annotation is not null and not empty
        if (annot != null && !annot.isEmpty()) {
            // If there is already content in the history, append a new line before the new annotation
            if (!this.history.isEmpty()) {
                this.history += "\n";
            }
            this.history += annot;
        }
    }

    /**
     * Updates the assigned family doctor for the patient.
     *
     * @param mshN The new doctor's membership number.
     */
    public void setNewDoctor(int mshN) {
        this.membShipNumb = mshN;
    }

    // --- Getters ---

    public HealthCardID getCip() {
        return cip;
    }

    public int getMembShipNumb() {
        return membShipNumb;
    }

    public String getHistory() {
        return history;
    }

    // --- Overrides for object comparison and representation ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalHistory that = (MedicalHistory) o;
        // Compare all relevant fields for equality
        return membShipNumb == that.membShipNumb &&
                Objects.equals(cip, that.cip) &&
                Objects.equals(history, that.history);
    }

    @Override
    public int hashCode() {
        // Generate a hash code based on the core fields
        return Objects.hash(cip, membShipNumb, history);
    }

    @Override
    public String toString() {
        return "MedicalHistory{" +
                "cip=" + cip +
                ", doctorNum=" + membShipNumb +
                ", history='" + history + '\'' +
                '}';
    }
}