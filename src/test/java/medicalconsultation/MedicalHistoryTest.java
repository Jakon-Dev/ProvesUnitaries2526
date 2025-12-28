package medicalconsultation;

import data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the {@link MedicalPrescription} class.
 * This class verifies the lifecycle and management of medical prescriptions,
 * including the addition, modification, and removal of prescription lines,
 * as well as error handling for invalid data or duplicate entries.
 */
class MedicalHistoryTest {
    private MedicalPrescription prescription;
    private HealthCardID cip;
    private ProductID prodID;
    private final String illness = "Diabetes";

    /**
     * Initializes the test environment before each execution.
     * Creates a standard {@link HealthCardID}, a {@link ProductID}, and a
     * new {@link MedicalPrescription} instance to ensure test isolation.
     * * @throws Exception if there is an error during object instantiation.
     */
    @BeforeEach
    void setUp() throws Exception {
        cip = new HealthCardID("ABCJ123456789012");
        prodID = new ProductID("123456789012");
        prescription = new MedicalPrescription(cip, 101, illness);
    }

    /**
     * Verifies that a valid medication line can be added to the prescription.
     * Validates that the prescription correctly stores the patient's CIP,
     * the illness description, and automatically assigns a prescription date.
     * * @throws Exception if the line addition fails.
     */
    @Test
    void testAddLine() throws Exception {
        String[] instructions = {"AFTERMEALS", "30.0", "1.0", "8.0", "HOUR", "Tomar con agua"};
        prescription.addLine(prodID, instructions);

        assertAll("AddLine",
                () -> assertEquals(cip, prescription.getCip()),
                () -> assertEquals(illness, prescription.getIllness()),
                () -> assertNotNull(prescription.getPrescDate())
        );
    }

    /**
     * Ensures that the system prevents adding the same product to a prescription twice.
     * Confirms that a {@link ProductAlreadyInPrescriptionException} is thrown
     * upon a duplicate entry.
     * * @throws Exception if the first addLine operation fails.
     */
    @Test
    void testAddLineDuplicate() throws Exception {
        String[] instructions = {"BEFORELUNCH", "1.0", "1.0", "1.0", "DAY", "Nota"};
        prescription.addLine(prodID, instructions);
        assertThrows(ProductAlreadyInPrescriptionException.class, () -> prescription.addLine(prodID, instructions));
    }

    /**
     * Validates that the system rejects instruction arrays that do not meet
     * the required data structure.
     * Expects an {@link IncorrectTakingGuidelinesException} when the
     * instructions array is too short.
     */
    @Test
    void testAddLineInvalidGuidelines() {
        String[] shortInstructions = {"DURINGLUNCH", "1.0"};
        assertThrows(IncorrectTakingGuidelinesException.class, () -> prescription.addLine(prodID, shortInstructions));
    }

    /**
     * Confirms that the dose for an existing product in the prescription
     * can be successfully updated.
     * * @throws Exception if the initial line addition or dose modification fails.
     */
    @Test
    void testModifyDose() throws Exception {
        String[] instructions = {"AFTERDINNER", "10.0", "100.0", "12.0", "HOUR", "Instruccion"};
        prescription.addLine(prodID, instructions);
        assertDoesNotThrow(() -> prescription.modifyDoseInLine(prodID, 200.0f));
    }

    /**
     * Verifies that a medication line can be removed from the prescription.
     * Also checks that attempting to remove a non-existent product results
     * in a {@link ProductNotInPrescriptionException}.
     * * @throws Exception if the initial line addition fails.
     */
    @Test
    void testRemoveLine() throws Exception {
        String[] instructions = {"DURINGMEALS", "5.0", "1.0", "1.0", "WEEK", "Borrar"};
        prescription.addLine(prodID, instructions);
        assertDoesNotThrow(() -> prescription.removeLine(prodID));
        assertThrows(ProductNotInPrescriptionException.class, () -> prescription.removeLine(prodID));
    }

    /**
     * Tests the system's robustness against invalid enumeration strings (e.g., dayMoment).
     * Verifies that an {@link IncorrectTakingGuidelinesException} is thrown (wrapping the
     * original IllegalArgumentException) when a malformed enum constant is provided.
     */
    @Test
    void testInvalidEnumConstant() {
        String[] badInstructions = {"AFTER_MEALS", "1.0", "1.0", "1.0", "HOUR", "Error"};

        assertThrows(IncorrectTakingGuidelinesException.class, () ->
                prescription.addLine(prodID, badInstructions)
        );
    }
}
