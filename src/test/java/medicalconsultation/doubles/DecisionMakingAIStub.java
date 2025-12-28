package medicalconsultation.doubles;

import data.InvalidFormatException;
import data.NullValueException;
import data.ProductID;
import medicalconsultation.FqUnit;
import medicalconsultation.TakingGuideline;
import medicalconsultation.dayMoment;
import services.DecisionMakingAI;
import services.Suggestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Test Stub for the DecisionMakingAI interface.
 * * This class provides fixed, predictable responses for testing purposes.
 * It allows the developer to verify how the ConsultationTerminal handles
 * AI-generated suggestions without needing a functional AI backend.
 */
public class DecisionMakingAIStub implements DecisionMakingAI {

    /**
     * Simulates the activation of the AI support system.
     * In this stub, it performs no action as there is no real external connection.
     */
    @Override
    public void initDecisionMakingAI() {
        // No implementation needed for a basic stub
    }

    /**
     * Simulates the reception of textual suggestions from a prompt.
     * * @param prompt The string input provided by the doctor.
     * @return A hardcoded response string.
     */
    @Override
    public String getSuggestions(String prompt) {
        return "Respuesta simulada de la IA";
    }

    /**
     * Simulates the parsing logic that converts a text answer into structured clinical data.
     * * This method returns a predefined list containing one "Addition" suggestion.
     * This is useful for testing if the ConsultationTerminal correctly translates
     * AI results into medical prescription lines.
     * * @param aiAnswer The text to be parsed (ignored in this stub).
     * @return A list containing a single hardcoded ADDITION suggestion.
     * @throws InvalidFormatException If formatting constraints are violated.
     * @throws NullValueException If a required value is missing.
     */
    @Override
    public List<Suggestion> parseSuggest(String aiAnswer) throws InvalidFormatException, NullValueException {
        List<Suggestion> suggestions = new ArrayList<>();

        // Create a mock product ID
        ProductID prodID = new ProductID("123456789012");

        // Define a mock taking guideline: After meals, 10 days, 1 unit every 8 hours.
        TakingGuideline guideline = new TakingGuideline(
                dayMoment.AFTERMEALS, 10, 1, 8, FqUnit.HOUR, "Instrucciones IA"
        );

        // Encapsulate into a suggestion of type ADDITION
        suggestions.add(new Suggestion(Suggestion.SuggType.ADDITION, prodID, guideline));

        return suggestions;
    }
}