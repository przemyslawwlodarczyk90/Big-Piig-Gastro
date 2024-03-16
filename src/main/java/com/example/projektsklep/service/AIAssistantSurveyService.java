package com.example.projektsklep.service;

import com.example.projektsklep.utils.AiAssistantSurveyResponse;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class AIAssistantSurveyService {

    private final OpenAiService openAiService;

    @Autowired
    public AIAssistantSurveyService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    public List<String> generateShoppingList(AiAssistantSurveyResponse response) throws Exception {
        String prompt = processSurveyResponse(response);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .model("gpt-3.5-turbo-instruct")
                .temperature(0.5)
                .maxTokens(150)
                .build();

        CompletionResult completionResult = openAiService.createCompletion(completionRequest);

        if (completionResult == null || completionResult.getChoices() == null || completionResult.getChoices().isEmpty()) {
            throw new Exception("Odpowiedź z OpenAI jest null lub pusta.");
        }

        String responseText = completionResult.getChoices().get(0).getText();
        if (responseText == null) {
            throw new Exception("Tekst odpowiedzi jest null.");
        }

        return Arrays.asList(responseText.split(" - "));
    }

    private String processSurveyResponse(AiAssistantSurveyResponse response) {
        StringBuilder prompt = new StringBuilder("Pomóż mi stworzyć biznes plan - listę zakupów dla nowo otwieranej restauracji.");
        prompt.append("Typ kuchni: ").append(mapCuisineType(response.getCuisineType())).append(". ");
        prompt.append("Wielkość lokalu: ").append(response.getRestaurantSize()).append(". ");
        prompt.append("Budżet: ").append(mapBudget(response.getBudget())).append(". ");
        prompt.append("Preferencje meblowe: ").append(mapFurniturePreferences(response.getFurniturePreferences())).append(". ");
        prompt.append("Dekoracje: ").append(mapDecorationDetails(response.getDecorationDetails())).append(".");
        prompt.append(" Wyniki podaje w bardzo ładnej liście. Weź pod uwagę wszystkie aspekty o których piszę poniżej. Nawet wielkość lokalu. " +
                "Staraj się podać przedmioty do kupienia nawet z ilością. Dopisz też parę zdań od siebie nt. urządzania lokalu.");
        return prompt.toString();
    }

    private String mapCuisineType(String cuisineType) {
        switch (cuisineType) {
            case "polska":
                return "Polska kuchnia";
            case "włoska":
                return "Włoska kuchnia";
            case "grecka":
                return "Grecka kuchnia";
            case "kebab":
                return "Kebab";
            case "lodziarnia":
                return "Lodziarnia";
            default:
                return "Nieokreślony typ kuchni";
        }
    }

    private String mapBudget(String budget) {
        switch (budget) {
            case "niski":
                return "Niski";
            case "średni":
                return "Średni";
            case "wysoki":
                return "Wysoki";
            default:
                return "Nieokreślony budżet";
        }
    }

    private String mapFurniturePreferences(String furniturePreferences) {
        switch (furniturePreferences) {
            case "nowoczesne":
                return "Nowoczesne meble";
            case "klasyczne":
                return "Klasyczne meble";
            case "vintage":
                return "Meble vintage";
            default:
                return "Nieokreślone preferencje meblowe";
        }
    }

    private String mapDecorationDetails(String decorationDetails) {
        return decorationDetails.equals("tak") ? "Zależy na dekoracjach" : "Nie zależy na dekoracjach";
    }
}