package data;

/**
 * Represents the Personal Identifying Code (CIP) in the National Health Service.
 * It serves as a unique identifier for patients.
 */
final public class HealthCardID {

    // The internal ID string
    private final String personalID;

    /**
     * Constructs a new HealthCardID.
     *
     * @param code The string representation of the CIP.
     * @throws NullValueException If the provided code is null.
     * @throws InvalidFormatException If the code does not match the required pattern (16 alphanumeric characters).
     */
    public HealthCardID(String code) throws NullValueException, InvalidFormatException {
        if (code == null) {
            throw new NullValueException("El CIP no puede ser nulo.");
        }
        // Validates that the code consists exactly of 16 alphanumeric characters
        if (!code.matches("^[a-zA-Z0-9]{16}$")) {
            throw new InvalidFormatException("El CIP debe tener 16 caracteres alfanuméricos.");
        }
        this.personalID = code;
    }

    public String getPersonalID() {
        return personalID;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthCardID hcardID = (HealthCardID) o;
        return personalID.equals(hcardID.personalID);
    }

    @Override
    public int hashCode() {
        return personalID.hashCode();
    }

    @Override
    public String toString() {
        return "HealthCardID " + "personal code='" + personalID + '\'' + '}';
    }
}