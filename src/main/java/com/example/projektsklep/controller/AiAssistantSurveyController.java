package com.example.projektsklep.controller;

import com.example.projektsklep.service.AIAssistantSurveyService;
import com.example.projektsklep.utils.AiAssistantSurveyResponse;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class AiAssistantSurveyController {
    private final AIAssistantSurveyService aiAssistantSurveyService;
    private final OpenAiService openAiService;

    public AiAssistantSurveyController(AIAssistantSurveyService aiAssistantSurveyService, OpenAiService openAiService) {
        this.aiAssistantSurveyService = aiAssistantSurveyService;
        this.openAiService = openAiService;
    }

    @GetMapping("/survey")
    public String showSurveyForm() {
        return "ai_assistant_survey_form";
    }

    @PostMapping("/survey")
    public String handleSurveySubmit(@ModelAttribute AiAssistantSurveyResponse surveyResponse, Model model) {
        try {
            String prompt = aiAssistantSurveyService.processSurveyResponse(surveyResponse);
            if (prompt == null) {
                System.err.println("Prompt jest null.");
                model.addAttribute("error", "Brak wygenerowanego promptu.");
                return "ai_assistant_survey_form";
            }

            CompletionRequest completionRequest = CompletionRequest.builder()
                    .prompt(prompt)
                    .model("gpt-3.5-turbo-instruct")
                    .temperature(0.5)
                    .maxTokens(150)
                    .build();
            CompletionResult completionResult = openAiService.createCompletion(completionRequest);

            if (completionResult == null || completionResult.getChoices() == null || completionResult.getChoices().isEmpty()) {
                System.err.println("Odpowiedź z OpenAI jest null lub pusta.");
                model.addAttribute("error", "Nie otrzymano odpowiedzi od API OpenAI.");
                return "ai_assistant_survey_form";
            }

            String responseText = completionResult.getChoices().get(0).getText();
            if (responseText == null) {
                System.err.println("Tekst odpowiedzi jest null.");
                model.addAttribute("error", "Brak tekstu w odpowiedzi od API OpenAI.");
                return "ai_assistant_survey_form";
            }

            // Przetwarzanie odpowiedzi na listę elementów
            List<String> shoppingList = Arrays.asList(responseText.split(" - "));
            model.addAttribute("shoppingList", shoppingList);
        } catch (Exception e) {
            System.err.println("Wystąpił błąd podczas komunikacji z OpenAI API: " + e.getMessage());
            model.addAttribute("error", "Przepraszamy, wystąpił problem podczas generowania odpowiedzi. Spróbuj ponownie później.");
            return "ai_assistant_survey_form";
        }
        return "ai_assistant_survey_result";
    }
}