package medicalconsultation;

import data.*;
import services.*;

import java.net.ConnectException;
import java.util.Date;
import java.util.List;

public class ConsultationTerminal {
    // Dependencias (Servicios Externos)
    private HealthNationalService hns;
    private DecisionMakingAI decisionAI;

    private HealthCardID currentCIP;
    private String currentIllness;
    private MedicalHistory currentHistory;
    private MedicalPrescription currentPrescription;

    private enum SessionState {
        IDLE,
        REVISION_STARTED,
        PRESCRIPTION_EDIT,
        PRESCRIPTION_END,
        SIGNED,
        COMPLETED
    }

    private SessionState state = SessionState.IDLE;

    public ConsultationTerminal() {
    }

    public void setHns(HealthNationalService hns) {
        this.hns = hns;
    }

    public void setDecisionAI(DecisionMakingAI decisionAI) {
        this.decisionAI = decisionAI;
    }

    public void initRevision(HealthCardID cip, String illness)
            throws ConnectException, HealthCardIDException, AnyCurrentPrescriptionException, ProceduralException, IncorrectParametersException {

        if (state != SessionState.IDLE && state != SessionState.COMPLETED) {
            if (state != SessionState.IDLE) throw new ProceduralException("Debe finalizar la sesión anterior.");
        }

        this.currentCIP = cip;
        this.currentIllness = illness;

        this.currentHistory = hns.getMedicalHistory(cip);

        this.currentPrescription = hns.getMedicalPrescription(cip, illness);

        this.state = SessionState.REVISION_STARTED;
    }

    public void enterMedicalAssessmentInHistory(String assess) throws ProceduralException {
        if (state != SessionState.REVISION_STARTED) {
            throw new ProceduralException("No se ha iniciado la revisión correctamente.");
        }
        currentHistory.addMedicalHistoryAnnotations(assess);
    }

    public void initMedicalPrescriptionEdition() throws ProceduralException {
        if (state != SessionState.REVISION_STARTED) {
            throw new ProceduralException("Debe iniciar la revisión antes de editar la prescripción.");
        }

        if (this.currentPrescription == null) {
            this.currentPrescription = new MedicalPrescription(currentCIP, 0, currentIllness); // Doctor ID mock
        }

        this.state = SessionState.PRESCRIPTION_EDIT;
    }


    public void enterMedicineWithGuidelines(ProductID prodID, String[] instruc)
            throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException, ProceduralException {

        if (state != SessionState.PRESCRIPTION_EDIT) {
            throw new ProceduralException("La sesión no está en modo edición de prescripción.");
        }

        createMedPrescriptionLine(prodID, instruc);
    }

    public void modifyDoseInLine(ProductID prodID, float newDose)
            throws ProductNotInPrescriptionException, ProceduralException {

        if (state != SessionState.PRESCRIPTION_EDIT) {
            throw new ProceduralException("La sesión no está en modo edición de prescripción.");
        }

        currentPrescription.modifyDoseInLine(prodID, newDose);
    }

    public void removeLine(ProductID prodID) throws ProductNotInPrescriptionException, ProceduralException {
        if (state != SessionState.PRESCRIPTION_EDIT) {
            throw new ProceduralException("La sesión no está en modo edición de prescripción.");
        }

        currentPrescription.removeLine(prodID);
    }

    public void enterTreatmentEndingDate(Date date) throws IncorrectEndingDateException, ProceduralException {
        if (state != SessionState.PRESCRIPTION_EDIT) {
            throw new ProceduralException("Debe estar editando la prescripción para finalizarla.");
        }

        if (date.before(new Date())) {
            throw new IncorrectEndingDateException("La fecha de fin no puede ser anterior a hoy.");
        }

        setPrescDateAndEndDate(date);

        this.state = SessionState.PRESCRIPTION_END;
    }


    public void finishMedicalPrescriptionEdition() throws ProceduralException {
        if (state != SessionState.PRESCRIPTION_END) {
            throw new ProceduralException("Debe establecer la fecha de fin antes de terminar la edición.");
        }
    }

    public void stampeeSignature() throws eSignatureException, ProceduralException, NullValueException {
        if (state != SessionState.PRESCRIPTION_END) {
            throw new ProceduralException("La prescripción no está lista para firmar.");
        }

        DigitalSignature signature = new DigitalSignature(new byte[]{1, 2, 3}); // Mock signature
        currentPrescription.setEsign(signature);
        this.state = SessionState.SIGNED;
    }


    public MedicalPrescription sendHistoryAndPrescription()
            throws ConnectException, HealthCardIDException, AnyCurrentPrescriptionException,
            NotCompletedMedicalPrescription, ProceduralException {

        if (state != SessionState.SIGNED) {
            throw new ProceduralException("La prescripción debe estar firmada antes de enviarse.");
        }

        MedicalPrescription result = hns.sendHistoryAndPrescription(
                currentCIP,
                currentHistory,
                currentIllness,
                currentPrescription
        );

        this.state = SessionState.IDLE;
        return result;
    }

    public void printMedicalPrescrip() throws printingException {}

    public void callDecisionMakingAI() throws AIException, ProceduralException {
        if (state != SessionState.PRESCRIPTION_EDIT) {
            throw new ProceduralException("La IA solo está disponible durante la edición.");
        }
        decisionAI.initDecisionMakingAI();
    }

    public void askAIForSuggest(String prompt) throws BadPromptException, ProceduralException {
        if (state != SessionState.PRESCRIPTION_EDIT) {
            throw new ProceduralException("La IA solo está disponible durante la edición.");
        }
        String response = decisionAI.getSuggestions(prompt);
    }

    public void extractGuidelinesFromSugg()
            throws ProceduralException, ProductAlreadyInPrescriptionException,
            ProductNotInPrescriptionException, IncorrectTakingGuidelinesException, InvalidFormatException, NullValueException {

        if (state != SessionState.PRESCRIPTION_EDIT) {
            throw new ProceduralException("La IA solo está disponible durante la edición.");
        }

        String lastAiResponse = "Respuesta simulada";
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

    // internal operations
    private void createMedPrescriptionLine(ProductID prodID, String[] instruc)
            throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {
        currentPrescription.addLine(prodID, instruc);
    }

    private void setPrescDateAndEndDate(Date date) {
        currentPrescription.setPrescDate(new Date()); // Fecha actual
        currentPrescription.setEndDate(date);
    }
}