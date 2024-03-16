package com.example.projektsklep.controller;

import com.example.projektsklep.service.AIAssistantSurveyService;
import com.example.projektsklep.utils.AiAssistantSurveyResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class AiAssistantSurveyController {
    private final AIAssistantSurveyService aiAssistantSurveyService;

    public AiAssistantSurveyController(AIAssistantSurveyService aiAssistantSurveyService) {
        this.aiAssistantSurveyService = aiAssistantSurveyService;
    }

    @GetMapping("/survey")
    public String showSurveyForm() {
        return "ai_assistant_survey_form";
    }

    @PostMapping("/survey")
    public String handleSurveySubmit(@ModelAttribute AiAssistantSurveyResponse surveyResponse, Model model) {
        try {
            List<String> shoppingList = aiAssistantSurveyService.generateShoppingList(surveyResponse);
            model.addAttribute("shoppingList", shoppingList);
        } catch (Exception e) {
            System.err.println("Wystąpił błąd: " + e.getMessage());
            model.addAttribute("error", "Przepraszamy, wystąpił problem. Spróbuj ponownie później.");
            return "ai_assistant_survey_form";
        }
        return "ai_assistant_survey_result";
    }
}