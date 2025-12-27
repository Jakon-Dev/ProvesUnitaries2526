package data;

import java.util.Objects;

public final class ProductID {
    private final String code;

    public ProductID(String code) throws NullValueException, InvalidFormatException {
        if (code == null) {
            throw new NullValueException("El código de producto no puede ser nulo.");
        }
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
