package medicalconsultation;

import java.util.Objects;

/**
 * Represents the dosage and frequency specifications for a medication.
 * This class serves as a data structure to define how much of a product should be
 * administered and how often, using specific time units.
 */
public class Posology {
    /** The quantity or amount of the medication to be taken per administration. */
    private float dose;

    /** The numerical value representing the frequency of administration. */
    private float freq;

    /** The unit of time associated with the frequency (e.g., HOUR, DAY, WEEK). */
    private FqUnit freqUnit;

    /**
     * Constructs a new Posology instance with specific dosage and frequency details.
     * * @param d The dose amount.
     * @param f The frequency value.
     * @param u The time unit for the frequency.
     */
    public Posology(float d, float f, FqUnit u) {
        this.dose = d;
        this.freq = f;
        this.freqUnit = u;
    }

    /** @return The current dose amount. */
    public float getDose() {
        return dose;
    }

    /** @param dose The new dose amount to be set. */
    public void setDose(float dose) {
        this.dose = dose;
    }

    /** @return The current frequency value. */
    public float getFreq(){
        return freq;
    }

    /** @param freq The new frequency value to be set. */
    public void setFreq(float freq) {
        this.freq = freq;
    }

    /** @return The {@link FqUnit} currently assigned. */
    public FqUnit getFreqUnit() {
        return freqUnit;
    }

    /** @param freqUnit The new time unit to be assigned. */
    public void setFreqUnit(FqUnit freqUnit) {
        this.freqUnit = freqUnit;
    }

    /**
     * Returns a string representation of the Posology object.
     * Useful for logging and debugging medical records.
     * * @return A string containing dose, frequency, and frequency unit.
     */
    @Override
    public String toString() {
        return "Posology{" +
                "dose=" + dose +
                ", freq=" + freq +
                ", freqUnit=" + freqUnit +
                '}';
    }

    /**
     * Compares this Posology instance with another object for equality.
     * Two Posology objects are considered equal if their dose, frequency,
     * and time units are identical.
     * * @param o The object to be compared.
     * @return true if the objects are of the same class and have identical values; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posology posology = (Posology) o;
        return  Float.compare(posology.dose, dose) == 0 &&
                Float.compare(posology.freq, freq) == 0 &&
                freqUnit == posology.freqUnit;
    }

    /**
     * Generates a hash code for this Posology instance based on its fields.
     * Consistent with the {@link #equals(Object)} implementation.
     * * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(dose, freq, freqUnit);
    }
}