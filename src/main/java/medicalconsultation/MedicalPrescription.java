package medicalconsultation;

import data.DigitalSignature;
import data.HealthCardID;
import data.ProductID;
import data.ePrescripCode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a medical prescription for a specific patient and illness.
 * This class manages the lifecycle of the prescription, including adding,
 * modifying, or removing medication lines (TakingGuidelines).
 */
public class MedicalPrescription {

    // The patient's Health Card ID
    private final HealthCardID cip;

    // The membership number of the prescribing doctor
    private final int membShipNumb;

    // The illness associated with this prescription
    private final String illness;

    // The unique code assigned to this prescription (set later by the system)
    private ePrescripCode prescCode;

    // The date when the prescription was created
    private Date prescDate;

    // The date when the treatment ends
    private Date endDate;

    // The digital signature of the doctor (stamped at the end of the process)
    private DigitalSignature esign;

    // A map holding the medication lines, keyed by the ProductID
    private final Map<ProductID, TakingGuideline> prescriptionLines;

    /**
     * Initializes a new MedicalPrescription.
     *
     * @param cip          The HealthCardID of the patient.
     * @param membShipNumb The membership number of the doctor.
     * @param illness      The name or description of the illness being treated.
     */
    public MedicalPrescription(HealthCardID cip, int membShipNumb, String illness) {
        this.cip = cip;
        this.membShipNumb = membShipNumb;
        this.illness = illness;
        this.prescriptionLines = new HashMap<>();
        // Sets the current date as the prescription creation date
        this.prescDate = new Date();
    }

    /**
     * Adds a new medication line to the prescription.
     * Parses the instruction array to create a TakingGuideline object.
     *
     * @param prodID  The unique identifier of the medicinal product.
     * @param instruc An array of Strings containing guideline details in this specific order:
     * [0]: DayMoment (e.g., "BEFOREBREAKFAST")
     * [1]: Duration (float)
     * [2]: Dose (float)
     * [3]: Frequency (float)
     * [4]: Frequency Unit (e.g., "HOUR")
     * [5]: Specific instructions (String)
     * @throws ProductAlreadyInPrescriptionException If the product is already in the prescription list.
     * @throws IncorrectTakingGuidelinesException    If the instruction array is null, incomplete, or contains invalid formats.
     */
    public void addLine(ProductID prodID, String[] instruc)
            throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {

        // Check if the product is already part of the prescription
        if (prescriptionLines.containsKey(prodID)) {
            throw new ProductAlreadyInPrescriptionException();
        }

        // Validate that the instruction array has enough elements to build a guideline
        if (instruc == null || instruc.length < 6) {
            throw new IncorrectTakingGuidelinesException();
        }

        try {
            // Parse the string array to create the domain object
            TakingGuideline guideline = new TakingGuideline(
                    dayMoment.valueOf(instruc[0]),      // Enum parsing
                    Float.parseFloat(instruc[1]),       // Duration parsing
                    Float.parseFloat(instruc[2]),       // Dose parsing
                    Float.parseFloat(instruc[3]),       // Frequency parsing
                    FqUnit.valueOf(instruc[4]),         // Unit Enum parsing
                    instruc[5]                          // Instructions text
            );

            // Add the valid guideline to the map
            prescriptionLines.put(prodID, guideline);

        } catch (IllegalArgumentException | NullPointerException e) {
            // Catch parsing errors (e.g., invalid number format or invalid enum name)
            throw new IncorrectTakingGuidelinesException();
        }
    }

    /**
     * Modifies the dosage of an existing medication line.
     *
     * @param prodID  The ID of the product to modify.
     * @param newDose The new dosage value to set.
     * @throws ProductNotInPrescriptionException If the product is not found in the current prescription.
     */
    public void modifyDoseInLine(ProductID prodID, float newDose)
            throws ProductNotInPrescriptionException {

        TakingGuideline line = prescriptionLines.get(prodID);

        // Validation: Ensure the product exists before modifying
        if (line == null) {
            throw new ProductNotInPrescriptionException();
        }

        // Navigate to the Posology object and update the dose
        line.getPosology().setDose(newDose);
    }

    /**
     * Removes a medication line from the prescription.
     *
     * @param prodID The ID of the product to remove.
     * @throws ProductNotInPrescriptionException If the product is not found in the current prescription.
     */
    public void removeLine(ProductID prodID) throws ProductNotInPrescriptionException {
        // Validation: Ensure the product exists before removing
        if (!prescriptionLines.containsKey(prodID)) {
            throw new ProductNotInPrescriptionException();
        }
        prescriptionLines.remove(prodID);
    }

    // --- Setters ---

    public void setPrescCode(ePrescripCode prescCode) {
        this.prescCode = prescCode;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setEsign(DigitalSignature esign) {
        this.esign = esign;
    }

    public void setPrescDate(Date date) {
        this.prescDate = date;
    }

    // --- Getters (Required for external services) ---

    public HealthCardID getCip() {
        return cip;
    }

    public String getIllness() {
        return illness;
    }

    public Date getPrescDate() {
        return prescDate;
    }
}