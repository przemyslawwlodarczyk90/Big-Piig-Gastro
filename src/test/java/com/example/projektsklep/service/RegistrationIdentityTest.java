package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.*;
import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.role.Role;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.AdminOrUser;
import com.example.projektsklep.model.repository.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationIdentityTest {
    @Test
    void registrationCannotOverwriteAnExistingUserOrAddress() {
        var users = mock(UserRepository.class);
        var addresses = mock(AddressRepository.class);
        var addressService = mock(AddressService.class);
        var roles = mock(RoleRepository.class);
        var passwords = mock(PasswordEncoder.class);
        var dto = new AddressDTO(99L, "Street", "City", "00-001", "PL");
        when(addressService.convertToEntity(dto)).thenAnswer(call -> new Address(99L, "Street", "City", "00-001", "PL"));
        when(addresses.save(any())).thenAnswer(call -> call.getArgument(0));
        when(users.save(any())).thenAnswer(call -> call.getArgument(0));
        when(roles.findByRoleType(AdminOrUser.USER)).thenReturn(Optional.of(Role.fromAdminOrUser(AdminOrUser.USER)));
        when(passwords.encode("secret")).thenReturn("encoded");
        var service = new UserService(users, addressService, addresses, roles, passwords);
        service.saveUser(UserDTO.builder().id(42L).firstName("Test").lastName("User")
                .email("new@example.com").password("secret").address(dto).build(), dto, AdminOrUser.USER);
        var user = ArgumentCaptor.forClass(User.class);
        verify(users).save(user.capture());
        assertNull(user.getValue().getId());
        assertEquals("encoded", user.getValue().getPasswordHash());
        var address = ArgumentCaptor.forClass(Address.class);
        verify(addresses).save(address.capture());
        assertNull(address.getValue().getId());
    }
}
