package medicalconsultation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the {@link TakingGuideline} class.
 * Verifies the correct initialization of medical guidelines, the state changes via setters,
 * and the management of the internal Posology object.
 */
public class TakingGuidelineTest {
    private TakingGuideline guideline;

    private final dayMoment MOMENT = dayMoment.AFTERMEALS;
    private final float DURATION = 10.0f;
    private final float DOSE = 1.0f;
    private final float FREQ = 8.0f;
    private final FqUnit FREQ_UNIT = FqUnit.HOUR;
    private final String INSTRUCTIONS = "Tomar con abundante agua";

    /**
     * Initializes a default TakingGuideline instance before each test.
     */
    @BeforeEach
    void setUp() {
        guideline = new TakingGuideline(
                MOMENT,
                DURATION,
                DOSE,
                FREQ,
                FREQ_UNIT,
                INSTRUCTIONS
        );
    }

    /**
     * Verifies that the constructor correctly maps all parameters to their respective fields,
     * including the automatic creation of the internal Posology object.
     */
    @Test
    void testConstructorInitializesFieldsCorrectly() {
        assertAll("Verificación de campos del constructor",
                () -> assertEquals(MOMENT, guideline.getdMoment(), "El momento del día no coincide"),
                () -> assertEquals(DURATION, guideline.getDuration(), "La duración no coincide"),
                () -> assertEquals(INSTRUCTIONS, guideline.getInstructions(), "Las instrucciones no coinciden"),
                () -> assertNotNull(guideline.getPosology(), "El objeto Posology interno no debería ser nulo")
        );

        assertAll("Verificación de Posology interno",
                () -> assertEquals(DOSE, guideline.getPosology().getDose(), "La dosis en Posology es incorrecta"),
                () -> assertEquals(FREQ, guideline.getPosology().getFreq(), "La frecuencia en Posology es incorrecta"),
                () -> assertEquals(FREQ_UNIT, guideline.getPosology().getFreqUnit(), "La unidad de frecuencia en Posology es incorrecta")
        );
    }

    /**
     * Validates that setter methods correctly modify the guideline's state.
     */
    @Test
    void testSettersModifyState() {
        guideline.setDuration(5.0f);
        assertEquals(5.0f, guideline.getDuration(), "La duración no se actualizó correctamente");

        guideline.setdMoment(dayMoment.BEFOREBREAKFAST);
        assertEquals(dayMoment.BEFOREBREAKFAST, guideline.getdMoment(), "El momento del día no se actualizó");

        guideline.setInstructions("Nueva instrucción");
        assertEquals("Nueva instrucción", guideline.getInstructions(), "Las instrucciones no se actualizaron");
    }

    /**
     * Ensures that replacing the Posology instance updates the guideline
     * and maintains object identity.
     */
    @Test
    void testSetPosologyReplacesInstance() {
        Posology newPosology = new Posology(2.0f, 12.0f, FqUnit.HOUR);

        guideline.setPosology(newPosology);

        assertAll("Reemplazo de Posology",
                () -> assertSame(newPosology, guideline.getPosology(), "Debe ser exactamente la misma instancia (assertSame)"),
                () -> assertEquals(2.0f, guideline.getPosology().getDose(), "La dosis de la nueva instancia no coincide")
        );
    }
}
