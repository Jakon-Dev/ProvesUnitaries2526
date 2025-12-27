package data;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ePrescripCodeTest {
    @Test
    @DisplayName("Lanza excepción si el código de prescripción es null")
    public void constructorShouldThrowExceptionOnNull() {
        assertThrows(NullValueException.class, () -> new ePrescripCode(null));
    }

    @Test
    @DisplayName("Lanza excepción si el formato no es alfanumérico de 10 caracteres")
    public void constructorShouldThrowExceptionOnInvalidFormat() {
        assertThrows(InvalidFormatException.class, () -> new ePrescripCode("INVALID-ID")); // Caracter especial
        assertThrows(InvalidFormatException.class, () -> new ePrescripCode("123")); // Muy corto
    }

    @Test
    @DisplayName("Crea instancia correctamente con formato válido")
    public void shouldCreateInstanceWithValidData() throws Exception {
        String validCode = "RX12345678";
        ePrescripCode epc = new ePrescripCode(validCode);
        assertEquals(validCode, epc.getCode());
    }
}
