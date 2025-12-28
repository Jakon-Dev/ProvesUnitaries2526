package data;

import java.util.Arrays;

/**
 * Represents the doctor's digital signature.
 * Stores the signature as a byte array. This class ensures immutability via defensive copying.
 */
public final class DigitalSignature {

    // The signature data in bytes
    private final byte[] signature;

    /**
     * Constructs a new DigitalSignature.
     *
     * @param signature The byte array representing the signature.
     * @throws NullValueException If the provided signature array is null.
     */
    public DigitalSignature(byte[] signature) throws NullValueException {
        if (signature == null) {
            throw new NullValueException("La firma digital no puede ser nula.");
        }
        // Creates a defensive copy of the array to prevent external modification of the internal state
        this.signature = Arrays.copyOf(signature, signature.length);
    }

    /**
     * Retrieves the digital signature.
     *
     * @return A copy of the signature byte array.
     */
    public byte[] getSignature() {
        // Returns a defensive copy to ensure the internal array remains immutable
        return Arrays.copyOf(signature, signature.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DigitalSignature that = (DigitalSignature) o;
        // Uses Arrays.equals for correct content comparison of arrays
        return Arrays.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        // Uses Arrays.hashCode to generate a hash based on array content
        return Arrays.hashCode(signature);
    }

    @Override
    public String toString() {
        return "DigitalSignature{" + "signatureLength=" + signature.length + '}';
    }
}