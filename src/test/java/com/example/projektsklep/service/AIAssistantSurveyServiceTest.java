package com.example.projektsklep.service;

import com.example.projektsklep.utils.AiAssistantSurveyResponse;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AIAssistantSurveyServiceTest {

    @Mock
    private OpenAiService openAiService;

    @InjectMocks
    private AIAssistantSurveyService aiAssistantSurveyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateShoppingList_ReturnsCorrectList() throws Exception {

        CompletionChoice choice = new CompletionChoice();
        choice.setText("Mleko - Chleb - Masło");
        CompletionResult completionResult = new CompletionResult();
        completionResult.setChoices(List.of(choice));

        when(openAiService.createCompletion(any(CompletionRequest.class))).thenReturn(completionResult);


        AiAssistantSurveyResponse response = new AiAssistantSurveyResponse();
        response.setCuisineType("polska");
        response.setRestaurantSize("mały");
        response.setBudget("średni");
        response.setFurniturePreferences("nowoczesne");
        response.setDecorationDetails("tak");


        List<String> actualList = aiAssistantSurveyService.generateShoppingList(response);


        assertEquals(List.of("Mleko", "Chleb", "Masło"), actualList);
    }
}