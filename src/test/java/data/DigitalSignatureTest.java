package data;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the DigitalSignature class.
 * Focus: Handling nulls and ensuring the byte array is immutable (cannot be changed from outside).
 */
public class DigitalSignatureTest {

    // TEST 1: NULL SAFETY
    @Test
    @DisplayName("Debe lanzar NullValueException si la firma es null")
    public void shouldThrowExceptionWhenSignatureIsNull() {
        // Explanation: The constructor is expected to fail if we pass 'null'.
        // We use assertThrows to verify that the specific custom exception (NullValueException) is raised.
        assertThrows(NullValueException.class, () -> new DigitalSignature(null));
    }

    // TEST 2: IMMUTABILITY (CRITICAL SECURITY TEST)
    @Test
    @DisplayName("Debe garantizar la inmutabilidad copiando el array")
    public void shouldBeImmutable() throws NullValueException {
        // 1. Arrange: Create a raw byte array [1, 2, 3]
        byte[] original = {1, 2, 3};
        
        // 2. Act: Create the DigitalSignature object using that array
        DigitalSignature sig = new DigitalSignature(original);
        
        // 3. Sabotage: Modify the 'original' array from the outside (change index 0 to 9)
        original[0] = 9;
        
        // 4. Assert: The internal signature inside the object 'sig' must NOT have changed.
        // If sig.getSignature()[0] is still 1, the class successfully created a defensive copy.
        // If it is 9, the class is broken (mutable).
        assertNotEquals(original[0], sig.getSignature()[0]);
    }
}