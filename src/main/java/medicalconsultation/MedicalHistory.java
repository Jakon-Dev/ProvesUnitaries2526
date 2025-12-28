package medicalconsultation;

import data.HealthCardID;

import java.util.Objects;

public class MedicalHistory {
    private HealthCardID cip;
    private int membShipNumb;
    private String history;

    public MedicalHistory(HealthCardID cip, int memberShipNum) throws IncorrectParametersException {
        if (cip == null) {
            throw new IncorrectParametersException("HealthCardID cannot be null");
        }
        if (memberShipNum < 0) {
            throw new IncorrectParametersException("Membership number cannot be negative");
        }

        this.cip = cip;
        this.membShipNumb = memberShipNum;
        this.history = "";

    }

    public void addMedicalHistoryAnnotations (String annot) {
        if (annot != null && !annot.isEmpty()) {
            if (!this.history.isEmpty()) {
                this.history += "\n";
            }
            this.history += annot;
        }
    }

    public void setNewDoctor (int mshN) {
        this.membShipNumb = mshN;
    }

    public HealthCardID getCip() {
        return cip;
    }

    public int getMembShipNumb() {
        return membShipNumb;
    }

    public String getHistory() {
        return history;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalHistory that = (MedicalHistory) o;
        return membShipNumb == that.membShipNumb &&
                Objects.equals(cip, that.cip) &&
                Objects.equals(history, that.history);
    }

    @Override
    public int hashCode() {
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