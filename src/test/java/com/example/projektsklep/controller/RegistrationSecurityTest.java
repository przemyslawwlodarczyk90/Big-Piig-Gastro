package com.example.projektsklep.controller;

import com.example.projektsklep.model.dto.*;
import com.example.projektsklep.model.enums.AdminOrUser;
import com.example.projektsklep.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RegistrationSecurityTest {
    @Test
    void forgedAdminRoleIsIgnored() throws Exception {
        var users = mock(UserService.class);
        var mvc = MockMvcBuilders.standaloneSetup(new UserListingAndRegistringController(users)).build();
        mvc.perform(post("/users/new").param("roleType", "ADMIN")
                        .param("firstName", "Test").param("lastName", "User")
                        .param("email", "test@example.com").param("password", "secret"))
                .andExpect(redirectedUrl("/users/registrationSuccess"));
        verify(users).saveUser(any(UserDTO.class), isNull(), eq(AdminOrUser.USER));
    }
}
