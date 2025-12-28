package medicalconsultation;

import data.*;
import services.*;

import java.net.ConnectException;
import java.util.Date;
import java.util.List;

/**
 * Controller class acting as a Facade (GRASP Pattern) for the "Supervise Treatment" use case.
 * It serves as the entry point for system events, coordinating interactions between
 * domain entities and external services like the SNS and Decision-Making AI.
 */
public class ConsultationTerminal {
    // --- External Service Dependencies (Injected) ---
    private HealthNationalService hns;
    private DecisionMakingAI decisionAI;

    // --- Current Session State Data ---
    private HealthCardID currentCIP;
    private String currentIllness;
    private MedicalHistory currentHistory;
    private MedicalPrescription currentPrescription;

    /**
     * Internal state machine to enforce the correct order of operations
     * as defined in the system sequence diagram (DSS).
     */
    private enum SessionState {
        IDLE,               // Initial state
        REVISION_STARTED,   // History and prescription loaded
        PRESCRIPTION_EDIT,  // Modification phase active
        PRESCRIPTION_END,   // Ending date established
        SIGNED,             // Digital signature applied
        COMPLETED           // Process finished
    }

    private SessionState state = SessionState.IDLE;

    public ConsultationTerminal() {
    }

    // --- Dependency Injection Setters ---

    public void setHns(HealthNationalService hns) {
        this.hns = hns;
    }

    public void setDecisionAI(DecisionMakingAI decisionAI) {
        this.decisionAI = decisionAI;
    }

    // --- Use Case Input Events ---

    /**
     * Initializes the patient revision. Downloads medical history and existing
     * prescriptions from the National Health Service.
     * * @param cip Patient's health card ID.
     * @param illness The specific illness being supervised.
     * @throws ProceduralException If a revision is already in progress.
     */
    public void initRevision(HealthCardID cip, String illness)
            throws ConnectException, HealthCardIDException, AnyCurrentPrescriptionException, ProceduralException, IncorrectParametersException {

        if (state != SessionState.IDLE && state != SessionState.COMPLETED) {
            throw new ProceduralException("Debe finalizar la sesión anterior antes de iniciar una nueva.");
        }

        this.currentCIP = cip;
        this.currentIllness = illness;

        // Interaction with remote services (SNS)
        this.currentHistory = hns.getMedicalHistory(cip);
        this.currentPrescription = hns.getMedicalPrescription(cip, illness);

        this.state = SessionState.REVISION_STARTED;
    }

    /**
     * Adds medical assessments or notes to the patient's record.
     * Must be called after initRevision.
     */
    public void enterMedicalAssessmentInHistory(String assess) throws ProceduralException {
        if (state != SessionState.REVISION_STARTED) {
            throw new ProceduralException("No se ha iniciado la revisión correctamente.");
        }
        currentHistory.addMedicalHistoryAnnotations(assess);
    }

    /**
     * Signals the start of the prescription editing process.
     * If no prescription exists, a new instance is created.
     */
    public void initMedicalPrescriptionEdition() throws ProceduralException {
        if (state != SessionState.REVISION_STARTED) {
            throw new ProceduralException("Debe iniciar la revisión antes de editar la prescripción.");
        }

        if (this.currentPrescription == null) {
            // Logic to initialize a new prescription if one doesn't exist
            this.currentPrescription = new MedicalPrescription(currentCIP, 0, currentIllness);
        }

        this.state = SessionState.PRESCRIPTION_EDIT;
    }

    /**
     * Adds a new medication line with specific administration guidelines.
     * * @param prodID The product code (UPC).
     * @param instruc Array of strings containing: [moment, duration, dose, frequency, unit, extra notes].
     */
    public void enterMedicineWithGuidelines(ProductID prodID, String[] instruc)
            throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException, ProceduralException {

        if (state != SessionState.PRESCRIPTION_EDIT) {
            throw new ProceduralException("La sesión no está en modo edición de prescripción.");
        }

        createMedPrescriptionLine(prodID, instruc);
    }

    /**
     * Modifies the dose of an existing medication line in the current prescription.
     */
    public void modifyDoseInLine(ProductID prodID, float newDose)
            throws ProductNotInPrescriptionException, ProceduralException {

        if (state != SessionState.PRESCRIPTION_EDIT) {
            throw new ProceduralException("La sesión no está en modo edición de prescripción.");
        }

        currentPrescription.modifyDoseInLine(prodID, newDose);
    }

    /**
     * Removes a specific medication line from the current prescription.
     */
    public void removeLine(ProductID prodID) throws ProductNotInPrescriptionException, ProceduralException {
        if (state != SessionState.PRESCRIPTION_EDIT) {
            throw new ProceduralException("La sesión no está en modo edición de prescripción.");
        }

        currentPrescription.removeLine(prodID);
    }

    /**
     * Establishes the treatment ending date. Transitions the state to allow signing.
     */
    public void enterTreatmentEndingDate(Date date) throws IncorrectEndingDateException, ProceduralException {
        if (state != SessionState.PRESCRIPTION_EDIT) {
            throw new ProceduralException("Debe estar editando la prescripción para establecer la fecha de fin.");
        }

        if (date.before(new Date())) {
            throw new IncorrectEndingDateException("La fecha de fin no puede ser anterior a la actual.");
        }

        setPrescDateAndEndDate(date);
        this.state = SessionState.PRESCRIPTION_END;
    }

    /**
     * Concludes the editing phase. Validates that the ending date was previously set.
     */
    public void finishMedicalPrescriptionEdition() throws ProceduralException {
        if (state != SessionState.PRESCRIPTION_END) {
            throw new ProceduralException("Debe establecer la fecha de fin antes de finalizar la edición.");
        }
        // State transition could occur here depending on final UI requirements
    }

    /**
     * Incorporates the doctor's electronic signature into the prescription.
     */
    public void stampeeSignature() throws eSignatureException, ProceduralException, NullValueException {
        if (state != SessionState.PRESCRIPTION_END) {
            throw new ProceduralException("La prescripción no está lista para ser firmada.");
        }

        // Simulating the retrieval of a digital signature
        DigitalSignature signature = new DigitalSignature(new byte[]{1, 2, 3});
        currentPrescription.setEsign(signature);
        this.state = SessionState.SIGNED;
    }

    /**
     * Transmits the updated history and signed prescription to the HNS for registration.
     * * @return The validated MedicalPrescription updated with the HNS-generated treatment code.
     */
    public MedicalPrescription sendHistoryAndPrescription()
            throws ConnectException, HealthCardIDException, AnyCurrentPrescriptionException,
            NotCompletedMedicalPrescription, ProceduralException {

        if (state != SessionState.SIGNED) {
            throw new ProceduralException("La prescripción debe estar firmada antes de enviarse al SNS.");
        }

        MedicalPrescription result = hns.sendHistoryAndPrescription(
                currentCIP,
                currentHistory,
                currentIllness,
                currentPrescription
        );

        this.state = SessionState.IDLE; // Reset session after successful transmission
        return result;
    }

    // --- AI Integration Events ---

    /**
     * Invokes the Decision Support AI to help adjust the treatment.
     */
    public void callDecisionMakingAI() throws AIException, ProceduralException {
        if (state != SessionState.PRESCRIPTION_EDIT) {
            throw new ProceduralException("La IA solo puede invocarse durante la edición del tratamiento.");
        }
        decisionAI.initDecisionMakingAI();
    }

    /**
     * Submits a prompt to the AI and receives textual suggestions.
     */
    public void askAIForSuggest(String prompt) throws BadPromptException, ProceduralException {
        if (state != SessionState.PRESCRIPTION_EDIT) {
            throw new ProceduralException("Debe estar en modo edición para dialogar con la IA.");
        }
        decisionAI.getSuggestions(prompt);
    }

    /**
     * Requests the AI to parse its response into a structured list of suggestions
     * (Insert, Modify, Delete) and applies them to the current prescription.
     */
    public void extractGuidelinesFromSugg()
            throws ProceduralException, ProductAlreadyInPrescriptionException,
            ProductNotInPrescriptionException, IncorrectTakingGuidelinesException, InvalidFormatException, NullValueException {

        if (state != SessionState.PRESCRIPTION_EDIT) {
            throw new ProceduralException("La extracción de pautas requiere estar en modo edición.");
        }

        // In a real scenario, this would use the last AI response
        String lastAiResponse = "AI_RESPONSE_PLACEHOLDER";
        List<Suggestion> suggestions = decisionAI.parseSuggest(lastAiResponse);

        for (Suggestion sugg : suggestions) {
            switch (sugg.getType()) {
                case ADDITION:
                    TakingGuideline tg = sugg.getTakingGuideline();
                    String[] instructions = new String[]{
                            tg.getdMoment().toString(),
                            String.valueOf(tg.getDuration()),
                            String.valueOf(tg.getPosology().getDose()),
                            String.valueOf(tg.getPosology().getFreq()),
                            tg.getPosology().getFreqUnit().toString(),
                            tg.getInstructions()
                    };
                    this.enterMedicineWithGuidelines(sugg.getProductID(), instructions);
                    break;

                case MODIFICATION:
                    float newDose = sugg.getTakingGuideline().getPosology().getDose();
                    this.modifyDoseInLine(sugg.getProductID(), newDose);
                    break;

                case ELIMINATION:
                    this.removeLine(sugg.getProductID());
                    break;
            }
        }
    }

    // --- Internal Operations ---

    private void createMedPrescriptionLine(ProductID prodID, String[] instruc)
            throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {
        currentPrescription.addLine(prodID, instruc);
    }

    private void setPrescDateAndEndDate(Date date) {
        currentPrescription.setPrescDate(new Date()); // Current date
        currentPrescription.setEndDate(date);
    }

    /**
     * Order print treatment sheet (Simulated).
     */
    public void printMedicalPrescrip() throws printingException {}
}