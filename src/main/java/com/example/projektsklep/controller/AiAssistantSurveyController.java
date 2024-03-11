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
        return "ai_assistant_survey_form"; // nazwa pliku HTML formularza ankiety
    }

    @PostMapping("/survey")
    public String handleSurveySubmit(@ModelAttribute AiAssistantSurveyResponse surveyResponse, Model model) {
        String prompt = aiAssistantSurveyService.processSurveyResponse(surveyResponse);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .model("gpt-3.5-turbo")
                .build();
        CompletionResult completionResult = openAiService.createCompletion(completionRequest);

        model.addAttribute("aiResponse", completionResult.getChoices().get(0).getText());
        return "ai_assistant_survey_result";
    }

}