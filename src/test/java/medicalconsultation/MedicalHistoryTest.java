package medicalconsultation;

import data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MedicalHistoryTest {
    private MedicalPrescription prescription;
    private HealthCardID cip;
    private ProductID prodID;
    private final String illness = "Diabetes";

    @BeforeEach
    void setUp() throws Exception {
        cip = new HealthCardID("ABCJ123456789012");
        prodID = new ProductID("123456789012");
        prescription = new MedicalPrescription(cip, 101, illness);
    }

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

    @Test
    void testAddLineDuplicate() throws Exception {
        String[] instructions = {"BEFORELUNCH", "1.0", "1.0", "1.0", "DAY", "Nota"};
        prescription.addLine(prodID, instructions);
        assertThrows(ProductAlreadyInPrescriptionException.class, () -> prescription.addLine(prodID, instructions));
    }

    @Test
    void testAddLineInvalidGuidelines() {
        String[] shortInstructions = {"DURINGLUNCH", "1.0"};
        assertThrows(IncorrectTakingGuidelinesException.class, () -> prescription.addLine(prodID, shortInstructions));
    }

    @Test
    void testModifyDose() throws Exception {
        String[] instructions = {"AFTERDINNER", "10.0", "100.0", "12.0", "HOUR", "Instruccion"};
        prescription.addLine(prodID, instructions);
        assertDoesNotThrow(() -> prescription.modifyDoseInLine(prodID, 200.0f));
    }

    @Test
    void testRemoveLine() throws Exception {
        String[] instructions = {"DURINGMEALS", "5.0", "1.0", "1.0", "WEEK", "Borrar"};
        prescription.addLine(prodID, instructions);
        assertDoesNotThrow(() -> prescription.removeLine(prodID));
        assertThrows(ProductNotInPrescriptionException.class, () -> prescription.removeLine(prodID));
    }

    @Test
    void testInvalidEnumConstant() {
        String[] badInstructions = {"AFTER_MEALS", "1.0", "1.0", "1.0", "HOUR", "Error"};
        assertThrows(IllegalArgumentException.class, () -> prescription.addLine(prodID, badInstructions));
    }
}
