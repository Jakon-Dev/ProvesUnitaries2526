package services;

import data.HealthCardID;
import data.HealthCardIDException;
import medicalconsultation.IncorrectParametersException;
import medicalconsultation.MedicalHistory;
import medicalconsultation.MedicalPrescription;

import java.net.ConnectException;

public interface HealthNationalService {
    MedicalHistory getMedicalHistory (HealthCardID cip)
            throws ConnectException, HealthCardIDException, IncorrectParametersException;

    MedicalPrescription getMedicalPrescription (HealthCardID cip, String illness)
            throws ConnectException, HealthCardIDException, AnyCurrentPrescriptionException;

    MedicalPrescription sendHistoryAndPrescription (HealthCardID cip, MedicalHistory hce, String illness, MedicalPrescription mPresc)
            throws ConnectException, HealthCardIDException, AnyCurrentPrescriptionException, NotCompletedMedicalPrescription;

    MedicalPrescription generateTreatmCodeAndRegister (MedicalPrescription ePresc)
            throws ConnectException;
}