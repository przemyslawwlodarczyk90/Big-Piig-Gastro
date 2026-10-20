package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.*;
import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.role.Role;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.AdminOrUser;
import com.example.projektsklep.model.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private final UserRepository users = mock(UserRepository.class);
    private final AddressRepository addresses = mock(AddressRepository.class);
    private final UserService service = new UserService(users, new AddressService(), addresses,
            mock(RoleRepository.class), mock(PasswordEncoder.class));

    private User user() {
        var user = new User(); user.setId(1L); user.setFirstName("Test"); user.setLastName("Buyer");
        user.setEmail("buyer@example.com"); user.setPasswordHash("private-hash");
        user.setRoles(Set.of(Role.fromAdminOrUser(AdminOrUser.USER)));
        return user;
    }

    @Test
    void userQueriesNeverExposePasswordHashes() {
        // Lista i wyszukiwanie nie ujawniaja skrotu hasla.
        var user = user(); var page = PageRequest.of(0, 10);
        when(users.findAll(page)).thenReturn(new PageImpl<>(List.of(user)));
        when(users.findById(1L)).thenReturn(Optional.of(user));
        when(users.findByLastNameIgnoreCaseContaining("buy")).thenReturn(List.of(user));
        assertNull(service.findAllUsers(page).getContent().get(0).password());
        assertEquals("buyer@example.com", service.findUserById(1L).orElseThrow().email());
        assertEquals(1, service.findUsersByLastName("buy").size());
        assertTrue(service.findUserById(99L).isEmpty());
    }

    @Test
    void profileEditCreatesMissingAddressAndPreservesIdentity() {
        // Formularz nie moze zmienic ID konta ani adresu przez podmiane ID.
        var user = user(); when(users.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(addresses.save(any())).thenAnswer(call -> call.getArgument(0));
        var edit = service.prepareUserDTOForEdit(user.getEmail());
        assertNotNull(edit.address());
        var changed = new UserDTO(99L, "New", "Name", "attacker@example.com", "ignored",
                new AddressDTO(999L, "Street", "City", "00-001", "PL"), Set.of());
        service.updateUserProfileAndAddress(user.getEmail(), changed);
        assertEquals(1L, user.getId()); assertEquals("buyer@example.com", user.getEmail());
        assertEquals("New", user.getFirstName()); assertEquals("Street", user.getAddress().getStreet());
        assertNull(user.getAddress().getId()); assertEquals("private-hash", user.getPasswordHash());
        assertEquals(AdminOrUser.USER, user.getRoles().iterator().next().getRoleType());
        assertEquals("Street", service.prepareUserDTOForEdit(user.getEmail()).address().street());
        assertEquals("Street", service.getUserDetailsForPanel(user.getEmail()).address().street());
        verify(users).save(user);
    }

    @Test
    void missingAccountsAreRejectedAndBlankFormHasNoIdentity() {
        // Nieistniejace konto nie daje pustego panelu ani edycji.
        assertThrows(RuntimeException.class, () -> service.getUserDetailsForPanel("missing"));
        assertThrows(RuntimeException.class, () -> service.prepareUserDTOForEdit("missing"));
        assertThrows(RuntimeException.class, () -> service.updateUserProfileAndAddress("missing", service.initializeNewUserDTO()));
        var initial = service.initializeNewUserDTO();
        assertNull(initial.id()); assertNull(initial.address().id()); assertTrue(initial.roles().isEmpty());
        var user = user(); when(users.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        assertNull(service.getUserDetailsForPanel(user.getEmail()).address());
        service.updateUserProfileAndAddress(user.getEmail(), new UserDTO(null, "Test", "Buyer", null, null, null, null));
        verifyNoInteractions(addresses);
    }
}
