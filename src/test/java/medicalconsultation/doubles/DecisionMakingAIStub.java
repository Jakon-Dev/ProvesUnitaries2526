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

public class DecisionMakingAIStub implements DecisionMakingAI {

    @Override
    public void initDecisionMakingAI() {
    }

    @Override
    public String getSuggestions(String prompt) {
        return "Respuesta simulada de la IA";
    }

    @Override
    public List<Suggestion> parseSuggest(String aiAnswer) throws InvalidFormatException, NullValueException {
        List<Suggestion> suggestions = new ArrayList<>();

        ProductID prodID = new ProductID("123456789012");
        TakingGuideline guideline = new TakingGuideline(
                dayMoment.AFTERMEALS, 10, 1, 8, FqUnit.HOUR, "Instrucciones IA"
        );

        suggestions.add(new Suggestion(Suggestion.SuggType.ADDITION, prodID, guideline));
        return suggestions;
    }
}