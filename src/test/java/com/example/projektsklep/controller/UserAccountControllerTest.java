//package com.example.projektsklep.controller;
//
//import com.example.projektsklep.exception.UserNotFoundException;
//import com.example.projektsklep.model.dto.AddressDTO;
//import com.example.projektsklep.model.dto.OrderDTO;
//import com.example.projektsklep.model.dto.UserDTO;
//import com.example.projektsklep.service.OrderService;
//import com.example.projektsklep.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.PropertyEditorRegistry;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.validation.ObjectError;
//
//import java.beans.PropertyEditor;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class UserAccountControllerTest {
//    private UserService userService;
//    private OrderService orderService;
//    private UserAccountController userAccountController;
//
//
//    @Test
//    public void givenUserExists_whenListUserOrders_thenReturnsUserOrdersView() {
//        // Given
//        UserDTO user = new UserDTO(1L, "user@example.com", "Jan", "Kowalski", "password", null, null);
//        when(userService.findUserByEmail("user@example.com")).thenReturn(Optional.of(user));
//        List<OrderDTO> orders = List.of(new OrderDTO(1L, user, null));
//        when(orderService.findAllOrdersByUserId(1L)).thenReturn(orders);
//
//        // When
//        String viewName = userAccountController.listUserOrders(new Model() {
//            @Override
//            public Model addAttribute(String attributeName, Object attributeValue) {
//                return null;
//            }
//
//            @Override
//            public Model addAttribute(Object attributeValue) {
//                return null;
//            }
//
//            @Override
//            public Model addAllAttributes(Collection<?> attributeValues) {
//                return null;
//            }
//
//            @Override
//            public Model addAllAttributes(Map<String, ?> attributes) {
//                return null;
//            }
//
//            @Override
//            public Model mergeAttributes(Map<String, ?> attributes) {
//                return null;
//            }
//
//            @Override
//            public boolean containsAttribute(String attributeName) {
//                return false;
//            }
//
//            @Override
//            public Object getAttribute(String attributeName) {
//                return null;
//            }
//
//            @Override
//            public Map<String, Object> asMap() {
//                return null;
//            }
//        });
//
//        // Then
//        assertEquals("user_orders", viewName);
//        verify(model).addAttribute("orders", orders);
//    }
//
//    @Test
//    public void givenUserDoesNotExist_whenListUserOrders_thenThrowsUserNotFoundException() {
//        // Given
//        when(userService.findUserByEmail("user@example.com")).thenReturn(Optional.empty());
//
//        // When
//        assertThrows(UserNotFoundException.class, () -> userAccountController.listUserOrders(new Model() {
//            @Override
//            public Model addAttribute(String attributeName, Object attributeValue) {
//                return null;
//            }
//
//            @Override
//            public Model addAttribute(Object attributeValue) {
//                return null;
//            }
//
//            @Override
//            public Model addAllAttributes(Collection<?> attributeValues) {
//                return null;
//            }
//
//            @Override
//            public Model addAllAttributes(Map<String, ?> attributes) {
//                return null;
//            }
//
//            @Override
//            public Model mergeAttributes(Map<String, ?> attributes) {
//                return null;
//            }
//
//            @Override
//            public boolean containsAttribute(String attributeName) {
//                return false;
//            }
//
//            @Override
//            public Object getAttribute(String attributeName) {
//                return null;
//            }
//
//            @Override
//            public Map<String, Object> asMap() {
//                return null;
//            }
//        }));
//    }
//    @Test
//    public void givenUserExists_whenListUserOrders_thenReturnsUserOrdersView() {
//        // Given
//        UserDTO user = new UserDTO(1L, "user@example.com", "Jan", "Kowalski", "password", null, null);
//        when(userService.findUserByEmail("user@example.com")).thenReturn(Optional.of(user));
//
//        // Poprawne użycie konstruktora OrderDTO z wymaganymi argumentami
//        List<OrderDTO> orders = List.of(new OrderDTO(
//                1L,
//                user.getId(),
//                "Zamówienie w trakcie realizacji", // Przykładowy status zamówienia
//                LocalDate.now(), // Data utworzenia
//                null, // Data wysyłki (jeśli nieznana)
//                BigDecimal.valueOf(100), // Przykładowa cena całkowita
//                List.of(), // Lista pozycji zamówienia
//                null // Adres wysyłki (jeśli niedostępny)
//        ));
//        when(orderService.findAllOrdersByUserId(1L)).thenReturn(orders);
//
//        // When
//        String viewName = userAccountController.listUserOrders(new Model() {
//            @Override
//            public Model addAttribute(String attributeName, Object attributeValue) {
//                return null;
//            }
//
//            @Override
//            public Model addAttribute(Object attributeValue) {
//                return null;
//            }
//
//            @Override
//            public Model addAllAttributes(Collection<?> attributeValues) {
//                return null;
//            }
//
//            @Override
//            public Model addAllAttributes(Map<String, ?> attributes) {
//                return null;
//            }
//
//            @Override
//            public Model mergeAttributes(Map<String, ?> attributes) {
//                return null;
//            }
//
//            @Override
//            public boolean containsAttribute(String attributeName) {
//                return false;
//            }
//
//            @Override
//            public Object getAttribute(String attributeName) {
//                return null;
//            }
//
//            @Override
//            public Map<String, Object> asMap() {
//                return null;
//            }
//        }); // Przekazanie instancji Model
//
//        // Then
//        assertEquals("user_orders", viewName);
//        verify(model).addAttribute("orders", orders);
//    }
//
//
//
//    @Test
//    public void givenUserDoesNotExist_whenShowEditForm_thenThrowsUserNotFoundException() {
//        // Given
//        when(userService.findUserByEmail("user@example.com")).thenReturn(Optional.empty());
//
//        // When
//        assertThrows(UserNotFoundException.class, () -> userAccountController.showEditForm(new Model() {
//            @Override
//            public Model addAttribute(String attributeName, Object attributeValue) {
//                return null;
//            }
//
//            @Override
//            public Model addAttribute(Object attributeValue) {
//                return null;
//            }
//
//            @Override
//            public Model addAllAttributes(Collection<?> attributeValues) {
//                return null;
//            }
//
//            @Override
//            public Model addAllAttributes(Map<String, ?> attributes) {
//                return null;
//            }
//
//            @Override
//            public Model mergeAttributes(Map<String, ?> attributes) {
//                return null;
//            }
//
//            @Override
//            public boolean containsAttribute(String attributeName) {
//                return false;
//            }
//
//            @Override
//            public Object getAttribute(String attributeName) {
//                return null;
//            }
//
//            @Override
//            public Map<String, Object> asMap() {
//                return null;
//            }
//        }));
//    }
//    @Test
//    public void givenValidUserData_whenUpdateProfileAndAddress_thenUpdatesUserProfileAndAddressAndRedirectsToUserPanel() {
//        // Given
//        UserDTO userDTO = new UserDTO(1L, "user@example.com", "Jan", "Kowalski", "password", null, null);
//        AddressDTO addressDTO = new AddressDTO(null, "ulica", "miasto", "kod", "kraj");
//        when(userService.findUserByEmail("user@example.com")).thenReturn(Optional.of(userDTO));
//
//        // When
//        String viewName = userAccountController.updateProfileAndAddress(userDTO, new BindingResult() {
//            @Override
//            public String getObjectName() {
//                return null;
//            }
//
//            @Override
//            public void reject(String errorCode, Object[] errorArgs, String defaultMessage) {
//
//            }
//
//            @Override
//            public void rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage) {
//
//            }
//
//            @Override
//            public List<ObjectError> getGlobalErrors() {
//                return null;
//            }
//
//            @Override
//            public List<FieldError> getFieldErrors() {
//                return null;
//            }
//
//            @Override
//            public Object getFieldValue(String field) {
//                return null;
//            }
//
//            @Override
//            public String toString() {
//                return null;
//            }
//
//            @Override
//            public Object getTarget() {
//                return null;
//            }
//
//            @Override
//            public Map<String, Object> getModel() {
//                return null;
//            }
//
//            @Override
//            public Object getRawFieldValue(String field) {
//                return null;
//            }
//
//            @Override
//            public PropertyEditor findEditor(String field, Class<?> valueType) {
//                return null;
//            }
//
//            @Override
//            public PropertyEditorRegistry getPropertyEditorRegistry() {
//                return null;
//            }
//
//            @Override
//            public String[] resolveMessageCodes(String errorCode) {
//                return new String[0];
//            }
//
//            @Override
//            public String[] resolveMessageCodes(String errorCode, String field) {
//                return new String[0];
//            }
//
//            @Override
//            public void addError(ObjectError error) {
//
//            }
//        }, addressDTO, new BindingResult() {
//            @Override
//            public String getObjectName() {
//                return null;
//            }
//
//            @Override
//            public void reject(String errorCode, Object[] errorArgs, String defaultMessage) {
//
//            }
//
//            @Override
//            public void rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage) {
//
//            }
//
//            @Override
//            public List<ObjectError> getGlobalErrors() {
//                return null;
//            }
//
//            @Override
//            public List<FieldError> getFieldErrors() {
//                return null;
//            }
//
//            @Override
//            public Object getFieldValue(String field) {
//                return null;
//            }
//
//            @Override
//            public String toString() {
//                return null;
//            }
//
//            @Override
//            public Object getTarget() {
//                return null;
//            }
//
//            @Override
//            public Map<String, Object> getModel() {
//                return null;
//            }
//
//            @Override
//            public Object getRawFieldValue(String field) {
//                return null;
//            }
//
//            @Override
//            public PropertyEditor findEditor(String field, Class<?> valueType) {
//                return null;
//            }
//
//            @Override
//            public PropertyEditorRegistry getPropertyEditorRegistry() {
//                return null;
//            }
//
//            @Override
//            public String[] resolveMessageCodes(String errorCode) {
//                return new String[0];
//            }
//
//            @Override
//            public String[] resolveMessageCodes(String errorCode, String field) {
//                return new String[0];
//            }
//
//            @Override
//            public void addError(ObjectError error) {
//
//            }
//        }, new Model() {
//            @Override
//            public Model addAttribute(String attributeName, Object attributeValue) {
//                return null;
//            }
//
//            @Override
//            public Model addAttribute(Object attributeValue) {
//                return null;
//            }
//
//            @Override
//            public Model addAllAttributes(Collection<?> attributeValues) {
//                return null;
//            }
//
//            @Override
//            public Model addAllAttributes(Map<String, ?> attributes) {
//                return null;
//            }
//
//            @Override
//            public Model mergeAttributes(Map<String, ?> attributes) {
//                return null;
//            }
//
//            @Override
//            public boolean containsAttribute(String attributeName) {
//                return false;
//            }
//
//            @Override
//            public Object getAttribute(String attributeName) {
//                return null;
//            }
//
//            @Override
//            public Map<String, Object> asMap() {
//                return null;
//            }
//        });
//
//        // Then
//        assertEquals("redirect:/userPanel", viewName);
//        verify(userService).updateUserProfileAndAddress(1L, userDTO, addressDTO);
//    }
//
//}