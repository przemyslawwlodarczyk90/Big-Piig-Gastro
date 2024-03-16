package com.example.projektsklep.controller;


import com.example.projektsklep.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AdminController.class)
@WithMockUser
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ProducerService producerService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductService productService;



    @Test
    void showAdminPanel_ShouldReturnAdminPanelView() throws Exception {
        mockMvc.perform(get("/admin/panel"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminPanel"));
    }

    @Test
    void showUserSearchForm_ShouldReturnUserSearchFormView() throws Exception {
        mockMvc.perform(get("/admin/user_search"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin_user_search"));
    }

    @WithMockUser(value = "admin", roles = "ADMIN")
    @Test
    void showUserOrders_ShouldReturnUserOrdersView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/user_search")
                        .param("lastName", "Smith")
                        .with(csrf())) // Adding CSRF token
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin_user_list"));
    }

    @Test
    void userDetails_ShouldRedirectWhenUserNotFound() throws Exception {
        Mockito.when(userService.findUserById(Mockito.anyLong())).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/admin/user_details/{userId}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user_search"));
    }

    @WithMockUser(value = "admin", roles = "ADMIN")
    @Test
    void saveAuthor_ShouldRedirectWhenValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/author")
                        .param("name", "Author Name")
                        .with(csrf())) // Adding CSRF token
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/authors"));
    }


}