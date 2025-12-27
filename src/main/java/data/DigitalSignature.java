package data;

import java.util.Arrays;

/**
 * The doctor's digital signature.
 */
public final class DigitalSignature {
    private final byte[] signature;

    public DigitalSignature(byte[] signature) throws NullValueException {
        if (signature == null) {
            throw new NullValueException("La firma digital no puede ser nula.");
        }
        this.signature = Arrays.copyOf(signature, signature.length);
    }

    public byte[] getSignature() {
        return Arrays.copyOf(signature, signature.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DigitalSignature that = (DigitalSignature) o;
        return Arrays.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(signature);
    }

    @Override
    public String toString() {
        return "DigitalSignature{" + "signatureLength=" + signature.length + '}';
    }
}