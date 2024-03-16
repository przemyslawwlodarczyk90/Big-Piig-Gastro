package com.example.projektsklep.controller;

import com.example.projektsklep.service.AIAssistantSurveyService;
import com.example.projektsklep.utils.AiAssistantSurveyResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AiAssistantSurveyController.class)
class AiAssistantSurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AIAssistantSurveyService aiAssistantSurveyService;

    @Test
    @WithMockUser(username = "user")
    void showSurveyForm_ShouldReturnSurveyFormView() throws Exception {
        mockMvc.perform(get("/survey"))
                .andExpect(status().isOk())
                .andExpect(view().name("ai_assistant_survey_form"));
    }

    @Test
    @WithMockUser(username = "user")
    void handleSurveySubmit_ShouldAddShoppingListToModel() throws Exception {
        AiAssistantSurveyResponse surveyResponse = new AiAssistantSurveyResponse();

        given(aiAssistantSurveyService.generateShoppingList(surveyResponse)).willReturn(Arrays.asList("Mleko", "Chleb", "Masło"));

        mockMvc.perform(post("/survey")
                        .flashAttr("surveyResponse", surveyResponse)
                        .with(csrf())) // Dodanie tokena CSRF
                .andExpect(status().isOk())
                .andExpect(view().name("ai_assistant_survey_result"))
                .andExpect(model().attributeExists("shoppingList"))
                .andExpect(model().attribute("shoppingList", Arrays.asList("Mleko", "Chleb", "Masło")));
    }

    @Test
    @WithMockUser(username = "user")
    void handleSurveySubmit_WithError_ShouldReturnFormViewWithError() throws Exception {
        AiAssistantSurveyResponse surveyResponse = new AiAssistantSurveyResponse();


        given(aiAssistantSurveyService.generateShoppingList(surveyResponse)).willThrow(new Exception("Testowy błąd"));

        mockMvc.perform(post("/survey")
                        .flashAttr("surveyResponse", surveyResponse)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("ai_assistant_survey_form"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Przepraszamy, wystąpił problem. Spróbuj ponownie później."));
    }
}