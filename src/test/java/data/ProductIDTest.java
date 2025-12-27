package data;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ProductIDTest {
    @Test
    @DisplayName("Lanza excepción si el ProductID es null")
    public void constructorShouldThrowExceptionOnNull() {
        assertThrows(NullValueException.class, () -> new ProductID(null));
    }

    @Test
    @DisplayName("Lanza excepción si el formato no son 12 dígitos")
    public void constructorShouldThrowExceptionOnInvalidFormat() {
        assertThrows(InvalidFormatException.class, () -> new ProductID("12345"));
        assertThrows(InvalidFormatException.class, () -> new ProductID("ABC456789012"));
    }

    @Test
    @DisplayName("Crea instancia correctamente con 12 dígitos")
    public void shouldCreateInstanceWithValidData() throws Exception {
        String validUPC = "123456789012";
        ProductID pid = new ProductID(validUPC);
        assertEquals(validUPC, pid.getCode());
    }
}
