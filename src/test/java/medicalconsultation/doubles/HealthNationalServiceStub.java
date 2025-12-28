package medicalconsultation.doubles;

import data.HealthCardID;
import medicalconsultation.IncorrectParametersException;
import medicalconsultation.MedicalHistory;
import medicalconsultation.MedicalPrescription;
import services.HealthNationalService;

/**
 * Doble de tipo Stub: proporciona respuestas prefijadas para aislar el SUT.
 */
public class HealthNationalServiceStub implements HealthNationalService {

    @Override
    public MedicalHistory getMedicalHistory(HealthCardID cip) throws IncorrectParametersException {
        return new MedicalHistory(cip, 12345);
    }

    @Override
    public MedicalPrescription getMedicalPrescription(HealthCardID cip, String illness) {
        return new MedicalPrescription(cip, 12345, illness);
    }

    @Override
    public MedicalPrescription sendHistoryAndPrescription(HealthCardID cip, MedicalHistory hce, String illness, MedicalPrescription mPresc) {
        return mPresc;
    }

    @Override
    public MedicalPrescription generateTreatmCodeAndRegister(MedicalPrescription ePresc) {
        return ePresc;
    }
}