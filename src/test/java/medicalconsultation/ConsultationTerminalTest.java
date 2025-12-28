package medicalconsultation;

import data.HealthCardID;
import data.InvalidFormatException;
import data.NullValueException;
import data.ProductID;
import medicalconsultation.doubles.DecisionMakingAIStub;
import medicalconsultation.doubles.HealthNationalServiceStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the ConsultationTerminal controller.
 * Following the requirements, input events are tested as a sequence
 * to verify correct state progression and procedural integrity.
 */
class ConsultationTerminalTest {

    private ConsultationTerminal terminal;
    private HealthCardID cip;
    private ProductID prodID;

    /**
     * Set up the test environment by injecting stubs for external services.
     * This isolates the terminal from real remote connections or complex AI parsing.
     */
    @BeforeEach
    void setUp() throws InvalidFormatException, NullValueException {
        terminal = new ConsultationTerminal();

        // Injecting the test doubles (Stubs)
        terminal.setHns(new HealthNationalServiceStub());
        terminal.setDecisionAI(new DecisionMakingAIStub());

        // Standard IDs used across multiple tests
        cip = new HealthCardID("1111222233334444");
        prodID = new ProductID("123456789012");
    }

    /**
     * Verifies the "Happy Path": initializing a revision, adding medicine,
     * setting a date, signing, and transmitting to the SNS.
     */
    @Test
    void testCompleteProcessAndSend() throws Exception {
        // 1. Initialize session
        terminal.initRevision(cip, "Infección");
        terminal.initMedicalPrescriptionEdition();

        // 2. Modify treatment
        String[] instructions = {"DURINGMEALS", "7", "500", "8", "HOUR", "Tomar con abundante agua"};
        terminal.enterMedicineWithGuidelines(prodID, instructions);

        // 3. Finalize and Sign
        Date futureDate = new Date(System.currentTimeMillis() + 604800000L); // Current time + 7 days
        terminal.enterTreatmentEndingDate(futureDate);
        terminal.finishMedicalPrescriptionEdition();
        terminal.stampeeSignature();

        // 4. Remote registration
        MedicalPrescription result = terminal.sendHistoryAndPrescription();

        // Assertions
        assertThat("The returned prescription should not be null", result, is(notNullValue()));
        assertThat("The CIP in the result must match the patient being treated", result.getCip(), is(equalTo(cip)));
    }

    /**
     * Verifies that suggestions provided by the DecisionMakingAIStub are
     * correctly extracted and integrated into the prescription.
     */
    @Test
    void testAIIntegrationWithStub() throws Exception {
        terminal.initRevision(cip, "Gripe");
        terminal.initMedicalPrescriptionEdition();

        // Interact with AI
        terminal.callDecisionMakingAI();
        terminal.askAIForSuggest("Tratamiento sugerido");
        terminal.extractGuidelinesFromSugg();

        // Verification: If the AI Stub added the product, trying to add it again manually
        // should trigger an exception (ProductAlreadyInPrescriptionException).
        String[] instruc = {"AFTERMEALS", "10", "1", "8", "HOUR", "Instrucciones IA"};

        assertThrows(ProductAlreadyInPrescriptionException.class, () -> {
            terminal.enterMedicineWithGuidelines(prodID, instruc);
        }, "The system should prevent adding a product that was already inserted by the AI suggestions");
    }

    /**
     * Verifies that the controller enforces the correct order of events.
     * Adding medicine without first entering "Edition Mode" must fail.
     */
    @Test
    void testStateControlTransitionException() throws Exception {
        terminal.initRevision(cip, "Dolor");

        String[] instruc = {"BEFOREBREAKFAST", "1", "1", "24", "HOUR", "Test"};

        // Expected to fail because initMedicalPrescriptionEdition() was never called
        assertThrows(ProceduralException.class, () -> {
            terminal.enterMedicineWithGuidelines(prodID, instruc);
        }, "The system must protect the workflow by blocking medication entry outside of edition mode");
    }

    /**
     * Validates that the system rejects treatment ending dates that are in the past.
     */
    @Test
    void testInvalidEndingDate() throws Exception {
        terminal.initRevision(cip, "Control");
        terminal.initMedicalPrescriptionEdition();

        Date pastDate = new Date(System.currentTimeMillis() - 1000000L); // 1000 seconds in the past

        assertThrows(IncorrectEndingDateException.class, () -> {
            terminal.enterTreatmentEndingDate(pastDate);
        }, "The system must validate that the treatment date is a future date");
    }
}