package medicalconsultation;

/**
 * Represents the specific guidelines for administering a medication within a prescription.
 * This class encapsulates the timing of the dose, total treatment duration,
 * detailed posology, and patient instructions.
 */
public class TakingGuideline {
    /** The specific moment of the day to take the medication. */
    private dayMoment dMoment;

    /** The total duration of the medical treatment. */
    private float duration;

    /** The dosage and frequency details, stored in a {@link Posology} object. */
    private Posology posology;

    /** Specific administration instructions for the patient. */
    private String instructions;

    /**
     * Constructs a new TakingGuideline with the specified parameters.
     * This constructor internally initializes a new {@link Posology} instance
     * using the dose, frequency, and unit provided.
     * * @param dM The moment of the day (e.g., AFTERMEALS).
     * @param du The total duration of the guideline.
     * @param d  The individual dose amount.
     * @param f  The frequency of administration.
     * @param fu The unit of time for the frequency (e.g., HOUR, DAY).
     * @param i  Textual instructions for the patient.
     */
    public TakingGuideline(dayMoment dM, float du, float d, float f, FqUnit fu, String i) {
        this.dMoment = dM;
        this.duration = du;
        this.posology = new Posology(d, f, fu);
        this.instructions = i;
    }

    /** @return The current day moment for the administration. */
    public dayMoment getdMoment() { return dMoment; }

    /** @param dMoment The new day moment to be set. */
    public void setdMoment(dayMoment dMoment) { this.dMoment = dMoment; }

    /** @return The treatment duration. */
    public float getDuration() { return duration; }

    /** @param duration The new duration to be set. */
    public void setDuration(float duration) { this.duration = duration; }

    /** @return The {@link Posology} object containing dose and frequency. */
    public Posology getPosology() { return posology; }

    /** @param posology The new Posology instance to associate with this guideline. */
    public void setPosology(Posology posology) { this.posology = posology; }

    /** @return The patient instructions. */
    public String getInstructions() { return instructions; }

    /** @param instructions The new instructions to be provided to the patient. */
    public void setInstructions(String instructions) { this.instructions = instructions; }
}