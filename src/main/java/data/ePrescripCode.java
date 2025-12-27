package data;

import java.util.Objects;

/**
 * The electronic prescription code.
 */
public final class ePrescripCode {
    private final String code;

    public ePrescripCode(String code) throws NullValueException, InvalidFormatException {
        if (code == null) {
            throw new NullValueException("El código de prescripción no puede ser nulo.");
        }
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