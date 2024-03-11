package com.example.projektsklep.service;

import com.example.projektsklep.utils.AiAssistantSurveyResponse;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class AIAssistantSurveyService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(apiKey.trim());
    }

    public String processSurveyResponse(AiAssistantSurveyResponse response) {
        StringBuilder prompt = new StringBuilder("Pomóż mi stworzyć biznes plan - listę zakupów dla nowo otwieranej restauracji. ");
        prompt.append("Typ kuchni: ").append(mapCuisineType(response.getCuisineType())).append(". ");
        prompt.append("Wielkość lokalu: ").append(response.getRestaurantSize()).append(". ");
        prompt.append("Budżet: ").append(mapBudget(response.getBudget())).append(". ");
        prompt.append("Preferencje meblowe: ").append(mapFurniturePreferences(response.getFurniturePreferences())).append(". ");
        prompt.append("Dekoracje: ").append(mapDecorationDetails(response.getDecorationDetails())).append(".");
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

