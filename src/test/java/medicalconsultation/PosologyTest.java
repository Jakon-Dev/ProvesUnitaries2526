package medicalconsultation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the {@link Posology} class.
 * Validates data encapsulation, equality logic, and string representation
 * of medication dosage and frequency.
 */
public class PosologyTest {
    private Posology posology;
    private final float INITIAL_DOSE = 500.0f;
    private final float INITIAL_FREQ = 8.0f;
    private final FqUnit INITIAL_UNIT = FqUnit.HOUR;

    /**
     * Initializes a Posology object with standard constants for testing.
     */
    @BeforeEach
    void setUp() {
        posology = new Posology(INITIAL_DOSE, INITIAL_FREQ, INITIAL_UNIT);
    }

    /**
     * Validates that the constructor assigns values correctly to all fields.
     */
    @Test
    void constructorShouldAssignValues() {
        assertAll("Constructor",
                () -> assertEquals(INITIAL_DOSE, posology.getDose(), "La dosis no coincide"),
                () -> assertEquals(INITIAL_FREQ, posology.getFreq(), "La frecuencia no coincide"),
                () -> assertEquals(INITIAL_UNIT, posology.getFreqUnit(), "La unidad no coincide")
        );
    }

    /**
     * Verifies that field updates via setters are reflected accurately with delta precision.
     */
    @Test
    void settersShouldUpdateFields() {
        posology.setDose(1000.0f);
        posology.setFreq(12.0f);

        assertAll("Setters",
                () -> assertEquals(1000.0f, posology.getDose(), 0.001f),
                () -> assertEquals(12.0f, posology.getFreq(), 0.001f)
        );
    }

    /**
     * Tests value-based equality and ensures that equal objects share the same HashCode.
     */
    @Test
    void equalsShouldReturnTrueForSameValues() {
        Posology samePosology = new Posology(INITIAL_DOSE, INITIAL_FREQ, INITIAL_UNIT);

        assertEquals(posology, samePosology, "Objetos con mismos valores deberían ser iguales");
        assertEquals(posology.hashCode(), samePosology.hashCode(), "HashCodes deben coincidir");
    }

    /**
     * Ensures the toString method provides a readable representation containing
     * all critical class fields.
     */
    @Test
    void toStringShouldContainFieldInformation() {
        String result = posology.toString();

        assertAll("Contenido de toString",
                () -> assertTrue(result.contains("dose=" + INITIAL_DOSE)),
                () -> assertTrue(result.contains("freq=" + INITIAL_FREQ)),
                () -> assertTrue(result.contains("freqUnit=" + INITIAL_UNIT))
        );
    }
}
