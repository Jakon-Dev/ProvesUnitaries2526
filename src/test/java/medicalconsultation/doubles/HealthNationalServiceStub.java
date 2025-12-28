package medicalconsultation.doubles;

import data.HealthCardID;
import medicalconsultation.IncorrectParametersException;
import medicalconsultation.MedicalHistory;
import medicalconsultation.MedicalPrescription;
import services.HealthNationalService;

/**
 * Test Stub for the HealthNationalService.
 * This class provides canned responses to isolate the ConsultationTerminal from
 * external dependencies during unit testing. It avoids complex logic or real
 * network connections.
 */
public class HealthNationalServiceStub implements HealthNationalService {

    /**
     * Simulates retrieving a patient's medical history.
     * Returns a new MedicalHistory instance associated with the provided CIP
     * and a mock doctor membership number (12345).
     * @param cip The patient's health card identifier.
     * @return A hardcoded MedicalHistory object.
     */
    @Override
    public MedicalHistory getMedicalHistory(HealthCardID cip) throws IncorrectParametersException {
        return new MedicalHistory(cip, 12345);
    }

    /**
     * Simulates retrieving an existing medical prescription for a specific illness.
     * @param cip The patient's health card identifier.
     * @param illness The name of the condition.
     * @return A hardcoded MedicalPrescription object.
     */
    @Override
    public MedicalPrescription getMedicalPrescription(HealthCardID cip, String illness) {
        return new MedicalPrescription(cip, 12345, illness);
    }

    /**
     * Simulates the transmission of clinical data to the remote service.
     * In this stub, it simply echoes back the prescription provided as an argument
     * to indicate a successful transfer.
     * @param cip Patient identifier.
     * @param hce The updated medical history.
     * @param illness The condition being treated.
     * @param mPresc The current prescription to be registered.
     * @return The same prescription object to simulate a successful response.
     */
    @Override
    public MedicalPrescription sendHistoryAndPrescription(HealthCardID cip, MedicalHistory hce, String illness, MedicalPrescription mPresc) {
        return mPresc;
    }

    /**
     * Internal simulation of generating a treatment code and registering it.
     * @param ePresc The prescription to register.
     * @return The prescription, representing a successful registration.
     */
    @Override
    public MedicalPrescription generateTreatmCodeAndRegister(MedicalPrescription ePresc) {
        return ePresc;
    }
}