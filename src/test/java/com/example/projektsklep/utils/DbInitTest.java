package com.example.projektsklep.utils;

import com.example.projektsklep.model.entities.role.Role;
import com.example.projektsklep.model.enums.AdminOrUser;
import com.example.projektsklep.model.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Optional;
import static org.mockito.Mockito.*;

class DbInitTest {
    @Test
    void restartDoesNotDuplicateRolesOrDemoData() {
        var users = mock(UserRepository.class);
        var roles = mock(RoleRepository.class);
        var categories = mock(CategoryRepository.class);
        var products = mock(ProductRepository.class);
        when(roles.findByRoleType(any())).thenAnswer(call -> Optional.of(Role.fromAdminOrUser(call.getArgument(0))));
        when(users.count()).thenReturn(1L);
        var init = new DbInit(users, roles, mock(PasswordEncoder.class), categories,
                mock(ProducerRepository.class), products, mock(OrderRepository.class));
        ReflectionTestUtils.setField(init, "demoDataEnabled", true);
        ReflectionTestUtils.invokeMethod(init, "postConstruct");
        ReflectionTestUtils.invokeMethod(init, "postConstruct");
        verify(roles, never()).save(any());
        verify(users, never()).save(any());
        verifyNoInteractions(categories, products);
    }

    @Test
    void demoAccountsAreDisabledByDefault() {
        var users = mock(UserRepository.class);
        var roles = mock(RoleRepository.class);
        when(roles.findByRoleType(any())).thenReturn(Optional.empty());
        var init = new DbInit(users, roles, mock(PasswordEncoder.class), mock(CategoryRepository.class),
                mock(ProducerRepository.class), mock(ProductRepository.class), mock(OrderRepository.class));
        ReflectionTestUtils.invokeMethod(init, "postConstruct");
        verify(roles, times(2)).save(any());
        verifyNoInteractions(users);
    }
}
