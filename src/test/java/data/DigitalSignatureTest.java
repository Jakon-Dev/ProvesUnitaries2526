package data;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

public class DigitalSignatureTest {
    @Test
    @DisplayName("Debe lanzar NullValueException si la firma es null")
    public void shouldThrowExceptionWhenSignatureIsNull() {
        assertThrows(NullValueException.class, () -> new DigitalSignature(null));
    }

    @Test
    @DisplayName("Debe garantizar la inmutabilidad copiando el array")
    public void shouldBeImmutable() throws NullValueException {
        byte[] original = {1, 2, 3};
        DigitalSignature sig = new DigitalSignature(original);
        original[0] = 9;
        assertNotEquals(original[0], sig.getSignature()[0]);
    }
}
