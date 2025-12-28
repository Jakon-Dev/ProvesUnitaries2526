package medicalconsultation;

import data.HealthCardID;
import data.InvalidFormatException;
import data.NullValueException;
import data.ProductID;
import medicalconsultation.doubles.DecisionMakingAIStub;
import medicalconsultation.doubles.HealthNationalServiceStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConsultationTerminalTest {

    private ConsultationTerminal terminal;
    private HealthCardID cip;
    private ProductID prodID;

    @BeforeEach
    void setUp() throws InvalidFormatException, NullValueException {
        terminal = new ConsultationTerminal();

        terminal.setHns(new HealthNationalServiceStub());
        terminal.setDecisionAI(new DecisionMakingAIStub());

        cip = new HealthCardID("1111222233334444");
        prodID = new ProductID("123456789012");
    }

    @Test
    void testCompleteProcessAndSend() throws Exception {

        terminal.initRevision(cip, "Infección");
        terminal.initMedicalPrescriptionEdition();

        String[] instructions = {"DURINGMEALS", "7", "500", "8", "HOUR", "Tomar con abundante agua"};
        terminal.enterMedicineWithGuidelines(prodID, instructions);

        Date futureDate = new Date(System.currentTimeMillis() + 604800000L); // +7 días
        terminal.enterTreatmentEndingDate(futureDate);

        terminal.finishMedicalPrescriptionEdition();
        terminal.stampeeSignature();

        MedicalPrescription result = terminal.sendHistoryAndPrescription();

        assertThat("La prescripción devuelta debe existir", result, is(notNullValue()));
        assertThat("El CIP debe coincidir con el del paciente", result.getCip(), is(equalTo(cip)));
    }

    @Test
    void testAIIntegrationWithStub() throws Exception {
        terminal.initRevision(cip, "Gripe");
        terminal.initMedicalPrescriptionEdition();

        terminal.callDecisionMakingAI();
        terminal.askAIForSuggest("Tratamiento sugerido");
        terminal.extractGuidelinesFromSugg();

        String[] instruc = {"AFTERMEALS", "10", "1", "8", "HOUR", "Instrucciones IA"};

        assertThrows(ProductAlreadyInPrescriptionException.class, () -> {
            terminal.enterMedicineWithGuidelines(prodID, instruc);
        }, "Debería lanzar excepción porque el producto ya fue añadido por el stub de la IA");
    }

    @Test
    void testStateControlTransitionException() throws Exception {
        terminal.initRevision(cip, "Dolor");

        String[] instruc = {"BEFOREBREAKFAST", "1", "1", "24", "HOUR", "Test"};

        assertThrows(ProceduralException.class, () -> {
            terminal.enterMedicineWithGuidelines(prodID, instruc);
        }, "Debe proteger el flujo impidiendo añadir medicinas sin estar en modo edición");
    }

    @Test
    void testInvalidEndingDate() throws Exception {
        terminal.initRevision(cip, "Control");
        terminal.initMedicalPrescriptionEdition();

        Date pastDate = new Date(System.currentTimeMillis() - 1000000L);

        assertThrows(IncorrectEndingDateException.class, () -> {
            terminal.enterTreatmentEndingDate(pastDate);
        }, "El sistema debe validar que la fecha de tratamiento sea futura");
    }
}