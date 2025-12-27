package data;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class HealthCardIDTest {

    @Test
    @DisplayName("Debe lanzar NullValueException si el código es null")
    public void shouldThrowExceptionWhenCodeIsNull() {
        assertThrows(NullValueException.class, () -> new HealthCardID(null));
    }

    @Test
    @DisplayName("Debe lanzar InvalidFormatException si el formato es incorrecto")
    public void shouldThrowExceptionWhenFormatIsInvalid() {
        assertThrows(InvalidFormatException.class, () -> new HealthCardID("ABC123"));
        assertThrows(InvalidFormatException.class, () -> new HealthCardID("A!C1234567890123"));
        assertThrows(InvalidFormatException.class, () -> new HealthCardID("ABC123456789123456789123456789"));

    }

    @Test
    @DisplayName("Debe crear la instancia si el código es válido")
    public void shouldCreateInstanceWithValidCode() throws Exception {
        String validCode = "ABCDE12345678901";
        HealthCardID id = new HealthCardID(validCode);
        assertEquals(validCode, id.getPersonalID());
    }
}
