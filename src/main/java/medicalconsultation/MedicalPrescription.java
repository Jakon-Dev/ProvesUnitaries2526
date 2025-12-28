package medicalconsultation;

import data.DigitalSignature;
import data.HealthCardID;
import data.ProductID;
import data.ePrescripCode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MedicalPrescription {
    private final HealthCardID cip;
    private final int membShipNumb;
    private final String illness;
    private ePrescripCode prescCode;
    private Date prescDate;
    private Date endDate;
    private DigitalSignature esign;

    private final Map<ProductID, TakingGuideline> prescriptionLines;

    public MedicalPrescription(HealthCardID cip, int membShipNumb, String illness) {
        this.cip = cip;
        this.membShipNumb = membShipNumb;
        this.illness = illness;
        this.prescriptionLines = new HashMap<>();
        this.prescDate = new Date();
    }

    public void addLine(ProductID prodID, String[] instruc)
            throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {

        if (prescriptionLines.containsKey(prodID)) {
            throw new ProductAlreadyInPrescriptionException();
        }

        if (instruc == null || instruc.length < 6) {
            throw new IncorrectTakingGuidelinesException();
        }

        TakingGuideline guideline = new TakingGuideline(
                dayMoment.valueOf(instruc[0]),
                Float.parseFloat(instruc[1]),
                Float.parseFloat(instruc[2]),
                Float.parseFloat(instruc[3]),
                FqUnit.valueOf(instruc[4]),
                instruc[5]
        );

        prescriptionLines.put(prodID, guideline);
    }

    public void modifyDoseInLine(ProductID prodID, float newDose)
            throws ProductNotInPrescriptionException {

        TakingGuideline line = prescriptionLines.get(prodID);
        if (line == null) {
            throw new ProductNotInPrescriptionException();
        }

        line.getPosology().setDose(newDose);
    }

    public void removeLine(ProductID prodID) throws ProductNotInPrescriptionException {
        if (!prescriptionLines.containsKey(prodID)) {
            throw new ProductNotInPrescriptionException();
        }
        prescriptionLines.remove(prodID);
    }

    public void setPrescCode(ePrescripCode prescCode) { this.prescCode = prescCode; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public void setEsign(DigitalSignature esign) { this.esign = esign; }
    public void setPrescDate(Date date) { this.prescDate = date; }

    // Getters necesarios para el servicio externo
    public HealthCardID getCip() { return cip; }
    public String getIllness() { return illness; }
}