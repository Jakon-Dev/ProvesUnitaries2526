package services;

import data.InvalidFormatException;
import data.NullValueException;

import java.util.List;

public interface DecisionMakingAI {
    void initDecisionMakingAI() throws AIException;
    String getSuggestions(String prompt) throws BadPromptException;
    List<Suggestion> parseSuggest (String aiAnswer) throws InvalidFormatException, NullValueException;
}

