package data;

import java.util.Objects;

/**
 * Represents the unique electronic prescription code.
 * This is a Value Object that encapsulates the prescription identifier string.
 */
public final class ePrescripCode {

    // The internal code string
    private final String code;

    /**
     * Constructs a new ePrescripCode.
     *
     * @param code The string representation of the prescription code.
     * @throws NullValueException If the provided code is null.
     * @throws InvalidFormatException If the code does not match the required pattern (10 alphanumeric characters).
     */
    public ePrescripCode(String code) throws NullValueException, InvalidFormatException {
        if (code == null) {
            throw new NullValueException("El código de prescripción no puede ser nulo.");
        }
        // Validates that the code consists exactly of 10 alphanumeric characters
        if (!code.matches("^[a-zA-Z0-9]{10}$")) {
            throw new InvalidFormatException("El ePrescripCode debe tener 10 caracteres alfanuméricos.");
        }
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ePrescripCode that = (ePrescripCode) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "ePrescripCode{" + "code='" + code + '\'' + '}';
    }
}