package data;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for HealthCardID (The patient's CIP).
 * Focus: Ensuring the ID follows the strict 16-character alphanumeric standard.
 */
public class HealthCardIDTest {

    // TEST 1: NULL SAFETY
    @Test
    @DisplayName("Debe lanzar NullValueException si el código es null")
    public void shouldThrowExceptionWhenCodeIsNull() {
        // Must reject null inputs immediately.
        assertThrows(NullValueException.class, () -> new HealthCardID(null));
    }

    // TEST 2: FORMAT VALIDATION
    @Test
    @DisplayName("Debe lanzar InvalidFormatException si el formato es incorrecto")
    public void shouldThrowExceptionWhenFormatIsInvalid() {
        // Case A: Too short (only 6 chars)
        assertThrows(InvalidFormatException.class, () -> new HealthCardID("ABC123"));
        
        // Case B: Contains invalid special character ('!')
        assertThrows(InvalidFormatException.class, () -> new HealthCardID("A!C1234567890123"));
        
        // Case C: Too long (more than 16 chars)
        assertThrows(InvalidFormatException.class, () -> new HealthCardID("ABC123456789123456789123456789"));
    }

    // TEST 3: SUCCESS CASE
    @Test
    @DisplayName("Debe crear la instancia si el código es válido")
    public void shouldCreateInstanceWithValidCode() throws Exception {
        // A valid 16-character alphanumeric string
        String validCode = "ABCDE12345678901";
        
        // Creates the object and asserts the value is retrieved correctly
        HealthCardID id = new HealthCardID(validCode);
        assertEquals(validCode, id.getPersonalID());
    }
}