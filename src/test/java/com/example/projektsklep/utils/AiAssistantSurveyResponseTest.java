package com.example.projektsklep.utils;

import com.example.projektsklep.service.AIAssistantSurveyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AIAssistantSurveyResponseTest {

    private AIAssistantSurveyService service;

    @BeforeEach
    void setUp() {
        service = new AIAssistantSurveyService(null);
    }

    @Test
    void processSurveyResponse_ReturnsCorrectPrompt() {
        AiAssistantSurveyResponse response = new AiAssistantSurveyResponse();
        response.setCuisineType("polska");
        response.setRestaurantSize("mały");
        response.setBudget("średni");
        response.setFurniturePreferences("nowoczesne");
        response.setDecorationDetails("tak");


        String actualPrompt = service.processSurveyResponse(response);
        String expectedPrompt = "Pomóż mi stworzyć biznes plan - listę zakupów dla nowo otwieranej restauracji." +
                "Typ kuchni: Polska kuchnia. Wielkość lokalu: mały. Budżet: Średni. " +
                "Preferencje meblowe: Nowoczesne meble. Dekoracje: Zależy na dekoracjach. Wyniki podaje w bardzo ładnej liście. " +
                "Weź pod uwagę wszystkie aspekty o których piszę poniżej. Nawet wielkość lokalu. " +
                "Staraj się podać przedmioty do kupienia nawet z ilością. Dopisz też parę zdań od siebie nt. urządzania lokalu.";


        assertEquals(expectedPrompt, actualPrompt);
    }}


