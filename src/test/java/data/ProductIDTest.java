package data;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for ProductID.
 * Focus: Validating that the ID is exactly 12 digits (numeric).
 */
public class ProductIDTest {
    
    // TEST 1: NULL SAFETY
    @Test
    @DisplayName("Lanza excepción si el ProductID es null")
    public void constructorShouldThrowExceptionOnNull() {
        assertThrows(NullValueException.class, () -> new ProductID(null));
    }

    // TEST 2: FORMAT VALIDATION (Business Rules)
    @Test
    @DisplayName("Lanza excepción si el formato no son 12 dígitos")
    public void constructorShouldThrowExceptionOnInvalidFormat() {
        // Case A: Too short (5 digits). Must fail.
        assertThrows(InvalidFormatException.class, () -> new ProductID("12345"));
        
        // Case B: Contains letters (ABC...). Must be numeric only. Must fail.
        assertThrows(InvalidFormatException.class, () -> new ProductID("ABC456789012"));
    }

    // TEST 3: SUCCESS CASE
    @Test
    @DisplayName("Crea instancia correctamente con 12 dígitos")
    public void shouldCreateInstanceWithValidData() throws Exception {
        // A valid 12-digit numeric string
        String validUPC = "123456789012";
        
        ProductID pid = new ProductID(validUPC);
        
        // Check that the object holds the correct string
        assertEquals(validUPC, pid.getCode());
    }
}