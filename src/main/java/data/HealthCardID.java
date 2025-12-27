package data;

/**
 * The personal identifying code in the National Health Service.
 */
final public class HealthCardID {
    private final String personalID;

    public HealthCardID(String code) throws NullValueException, InvalidFormatException {
        if (code == null) {
            throw new NullValueException("El CIP no puede ser nulo.");
        }
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