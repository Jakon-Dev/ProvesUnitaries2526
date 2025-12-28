package data;

import java.util.Objects;

/**
 * Represents a Universal Product Code (UPC) for a medicinal product.
 */
public final class ProductID {

    // The internal code string
    private final String code;

    /**
     * Constructs a new ProductID.
     *
     * @param code The string representation of the product code.
     * @throws NullValueException If the provided code is null.
     * @throws InvalidFormatException If the code does not match the required pattern (12 digits).
     */
    public ProductID(String code) throws NullValueException, InvalidFormatException {
        if (code == null) {
            throw new NullValueException("El código de producto no puede ser nulo.");
        }
        // Validates that the code consists exactly of 12 numerical digits
        if (!code.matches("^\\d{12}$")) {
            throw new InvalidFormatException("El ProductID debe contener exactamente 12 dígitos.");
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
        ProductID productID = (ProductID) o;
        return code.equals(productID.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "ProductID{" + "code='" + code + '\'' + '}';
    }
}