package data;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the ePrescripCode class.
 * Focus: Validating the specific string format required for prescription codes.
 */
public class ePrescripCodeTest {

    // TEST 1: NULL SAFETY
    @Test
    @DisplayName("Lanza excepción si el código de prescripción es null")
    public void constructorShouldThrowExceptionOnNull() {
        // Ensures the system doesn't crash with NullPointerException later on.
        assertThrows(NullValueException.class, () -> new ePrescripCode(null));
    }

    // TEST 2: FORMAT VALIDATION 
    @Test
    @DisplayName("Lanza excepción si el formato no es alfanumérico de 10 caracteres")
    public void constructorShouldThrowExceptionOnInvalidFormat() {
        // Case A: Contains a special character ('-'). This should fail.
        assertThrows(InvalidFormatException.class, () -> new ePrescripCode("INVALID-ID")); 
        
        // Case B: Too short (only 3 digits). This should also fail.
        assertThrows(InvalidFormatException.class, () -> new ePrescripCode("123")); 
    }

    // TEST 3: SUCCESSFUL CREATION 
    @DisplayName("Crea instancia correctamente con formato válido")
    public void shouldCreateInstanceWithValidData() throws Exception {
        // 1. Define a valid code (Alphanumeric, length 10 based on this example)
        String validCode = "RX12345678";
        
        // 2. Create the object
        ePrescripCode epc = new ePrescripCode(validCode);
        
        // 3. Verify the data was stored correctly
        assertEquals(validCode, epc.getCode());
    }
}