package medicalconsultation;

import data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the {@link MedicalPrescription} class.
 * Focuses on the lifecycle of a prescription: adding medication lines,
 * handling duplicates, modifying dosages, and enforcing validation rules.
 */
public class MedicalPrescriptionTest {
    private MedicalPrescription prescription;
    private HealthCardID cip;
    private ProductID prodID;
    private final String illness = "Diabetes";

    /**
     * Sets up a fresh prescription instance and common identifiers (CIP, ProductID)
     * before each test case.
     */
    @BeforeEach
    void setUp() throws Exception {
        cip = new HealthCardID("ABCJ123456789012");
        prodID = new ProductID("123456789012");
        prescription = new MedicalPrescription(cip, 101, illness);
    }

    /**
     * Verifies that a valid medication line can be added and that
     * prescription metadata remains consistent.
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
     * Ensures that adding the same product twice to a prescription
     * triggers a {@link ProductAlreadyInPrescriptionException}.
     */
    @Test
    void testAddLineDuplicate() throws Exception {
        String[] instructions = {"BEFORELUNCH", "1.0", "1.0", "1.0", "DAY", "Nota"};
        prescription.addLine(prodID, instructions);
        assertThrows(ProductAlreadyInPrescriptionException.class, () -> prescription.addLine(prodID, instructions));
    }

    /**
     * Validates that the system rejects instruction arrays with insufficient data points.
     */
    @Test
    void testAddLineInvalidGuidelines() {
        String[] shortInstructions = {"DURINGLUNCH", "1.0"};
        assertThrows(IncorrectTakingGuidelinesException.class, () -> prescription.addLine(prodID, shortInstructions));
    }

    /**
     * Confirms that the dosage for an existing medication line can be successfully updated.
     */
    @Test
    void testModifyDose() throws Exception {
        String[] instructions = {"AFTERDINNER", "10.0", "100.0", "12.0", "HOUR", "Instruccion"};
        prescription.addLine(prodID, instructions);
        assertDoesNotThrow(() -> prescription.modifyDoseInLine(prodID, 200.0f));
    }

    /**
     * Verifies the removal of a medication line and ensures subsequent
     * removal attempts throw a {@link ProductNotInPrescriptionException}.
     */
    @Test
    void testRemoveLine() throws Exception {
        String[] instructions = {"DURINGMEALS", "5.0", "1.0", "1.0", "WEEK", "Borrar"};
        prescription.addLine(prodID, instructions);
        assertDoesNotThrow(() -> prescription.removeLine(prodID));
        assertThrows(ProductNotInPrescriptionException.class, () -> prescription.removeLine(prodID));
    }

    /**
     * Tests that providing an invalid String for enum conversion (e.g., dayMoment)
     * throws the standard IllegalArgumentException.
     */
    @Test
    void testInvalidEnumConstant() {
        String[] badInstructions = {"AFTER_MEALS", "1.0", "1.0", "1.0", "HOUR", "Error"};

        assertThrows(IncorrectTakingGuidelinesException.class, () ->
                prescription.addLine(prodID, badInstructions)
        );
    }
}
