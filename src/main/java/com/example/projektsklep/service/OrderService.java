package com.example.projektsklep.service;

import com.example.projektsklep.exception.OrderNotFoundException;
import com.example.projektsklep.exception.OrderUpdateException;
import com.example.projektsklep.exception.UserNotFoundException;
import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.LineOfOrderDTO;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.repository.OrderRepository;
import com.example.projektsklep.model.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;


  public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
    this.orderRepository = orderRepository;
    this.userRepository = userRepository;
  }

  // Metoda zwracająca wszystkie zamówienia w formie stronicowanej.
  public Page<OrderDTO> findAllOrders(Pageable pageable) {
    return orderRepository.findAll(pageable)
            .map(this::convertToOrderDTO); // Konwersja zamówień na DTO.
  }

  // Wyszukiwanie DTO zamówienia po jego ID.
  public Optional<OrderDTO> findOrderDTOById(Long id) {
    return orderRepository.findById(id)
            .map(this::convertToOrderDTO);// Konwersja znalezionego zamówienia na DTO.
          // Zwrócenie null, jeśli zamówienie nie zostało znalezione.
  }

  // Zapis nowego zamówienia z DTO.
  public OrderDTO saveOrderDTO(OrderDTO orderDTO) {
    Order order = convertToOrder(orderDTO); // Konwersja DTO na encję.

    // Przypisanie użytkownika do zamówienia, jeśli podano userId.
    if (orderDTO.userId() != null) {
      User user = userRepository.findById(orderDTO.userId())
              .orElseThrow(() -> new RuntimeException("User not found")); // Rzucenie wyjątku, jeśli użytkownik nie istnieje.
      order.setAccountHolder(user);
    }

    // Przetwarzanie adresu wysyłkowego nie zostało zaimplementowane.

    order = orderRepository.save(order); // Zapis zamówienia w bazie danych.
    return convertToOrderDTO(order); // Zwrócenie zapisanego zamówienia jako DTO.
  }

  // Pobranie wszystkich zamówień danego użytkownika.
  public List<OrderDTO> findAllOrdersByUserId(long userId) {
    return orderRepository.findByAccountHolder_Id(userId).stream()
            .map(this::convertToOrderDTO) // Konwersja każdego zamówienia na DTO.
            .collect(Collectors.toList()); // Zbieranie wyników do listy.
  }

  // Aktualizacja zamówienia na podstawie DTO.
  public boolean updateOrderDTO(Long id, OrderDTO orderDTO) {
    Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new OrderUpdateException("Order not found")); // Wyjątek, jeśli nie znaleziono zamówienia.
    updateOrderData(existingOrder, orderDTO); // Aktualizacja danych zamówienia.
    existingOrder = orderRepository.save(existingOrder); // Zapis zmienionego zamówienia.
    return existingOrder != null; // Sprawdzenie, czy zapis się powiódł.
  }

  // Wyszukanie wszystkich zamówień o określonym statusie.
  public List<OrderDTO> findAllOrdersByStatus(OrderStatus orderStatus) {
    return orderRepository.findAllByOrderStatus(orderStatus).stream()
            .map(this::convertToOrderDTO) // Konwersja zamówień na DTO.
            .collect(Collectors.toList()); // Zbieranie wyników do listy.
  }

  // Wyszukiwanie zamówień dla użytkownika o podanym adresie email.
  public List<OrderDTO> findOrdersForUserEmail(String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("Użytkownik o podanym adresie e-mail nie istnieje.")); // Wyjątek, jeśli użytkownik nie istnieje.
    return findAllOrdersByUserId(user.getId()); // Zwrócenie zamówień danego użytkownika.
  }

  // Konwersja encji Order na DTO.
  private OrderDTO convertToOrderDTO(Order order) {
    // Deklaracja i przypisanie wartości do zmiennej statusName.
    // Zakładamy, że status zamówienia może być null, więc używamy operatora "?:" do obsługi tego przypadku.
    String statusName = (order.getOrderStatus() != null) ? order.getOrderStatus().name() : "UNDEFINED";

    List<LineOfOrderDTO> lineOfOrdersDTO = Optional.ofNullable(order.getLineOfOrders())
            .orElseGet(Collections::emptyList)
            .stream()
            .map(line -> new LineOfOrderDTO(
                    line.getId(),
                    line.getProduct().getId(),
                    line.getQuantity(),
                    line.getUnitPrice()))
            .collect(Collectors.toList());

    AddressDTO shippingAddressDTO = Optional.ofNullable(order.getShippingAddress())
            .map(address -> new AddressDTO(
                    address.getId(),
                    address.getStreet(),
                    address.getCity(),
                    address.getPostalCode(),
                    address.getCountry()))
            .orElse(null);

    // Użycie zmiennej statusName podczas tworzenia nowego obiektu OrderDTO.
    return new OrderDTO(
            order.getId(),
            order.getAccountHolder() != null ? order.getAccountHolder().getId() : null, // Bezpieczne odwołanie do getAccountHolder().
            statusName,
            order.getDateCreated(),
            order.getSentAt(),
            order.getTotalPrice(),
            lineOfOrdersDTO,
            shippingAddressDTO);
  }
  // Aktualizacja statusu zamówienia.
  public boolean updateOrderStatus(Long id, String orderStatus) {
    Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found")); // Wyjątek, jeśli zamówienie nie istnieje.
    existingOrder.setOrderStatus(OrderStatus.valueOf(orderStatus.toUpperCase())); // Ustawienie nowego statusu zamówienia.
    existingOrder = orderRepository.save(existingOrder); // Zapis zmienionego zamówienia.
    return existingOrder != null; // Sprawdzenie, czy zapis się powiódł.
  }

  // Konwersja z OrderDTO do encji Order. Aktualnie metoda jest pusta i wymaga implementacji.
  private Order convertToOrder(OrderDTO orderDTO) {
    Order order = new Order();
    // Tutaj powinna znaleźć się logika konwersji.
    return order;
  }

  // Aktualizacja danych zamówienia. Metoda jest pusta i wymaga implementacji.
  private void updateOrderData(Order order, OrderDTO orderDTO) {
    // Tutaj powinna znaleźć się logika aktualizacji danych zamówienia.
  }

  // Konwersja z AddressDTO do encji Address. Metoda jest pusta i wymaga implementacji.
  private Address convertToAddress(AddressDTO addressDTO) {
    Address address = new Address();
    // Tutaj powinna znaleźć się logika konwersji.
    return address;
  }

  // Tworzenie OrderDTO na podstawie danych wejściowych.
  public OrderDTO createOrderDTO(Long userId, String orderStatus, List<LineOfOrderDTO> lineOfOrders, AddressDTO shippingAddress) {
    LocalDate now = LocalDate.now(); // Pobranie aktualnej daty.
    BigDecimal totalPrice = calculateTotalPrice(lineOfOrders); // Obliczenie całkowitej ceny zamówienia.

    // Zwrócenie nowo utworzonego DTO zamówienia.
    return new OrderDTO(null, userId, orderStatus, now, null, totalPrice, lineOfOrders, shippingAddress);
  }

  // Obliczanie całkowitej ceny zamówienia na podstawie linii zamówień.
  private BigDecimal calculateTotalPrice(List<LineOfOrderDTO> lineOfOrders) {
    // Aktualnie metoda zwraca 0 i wymaga implementacji.
    return BigDecimal.ZERO;
  }

  // Metoda zwracająca dane do paginacji oraz listę zamówień jako DTO.
  public Map<String, Object> findAllOrdersWithPagination(Pageable pageable) {
    Page<OrderDTO> orderPage = orderRepository.findAll(pageable)
            .map(this::convertToOrderDTO); // Konwersja zamówień na DTO dla stronicowania.

    // Przygotowanie i zwrócenie mapy z danymi do paginacji.
    Map<String, Object> response = new HashMap<>();
    response.put("orderPage", orderPage);
    response.put("currentPage", orderPage.getNumber());
    response.put("totalItems", orderPage.getTotalElements());
    response.put("totalPages", orderPage.getTotalPages());
    response.put("hasPrevious", orderPage.hasPrevious());
    response.put("hasNext", orderPage.hasNext());
    response.put("firstPage", 0);
    response.put("lastPage", orderPage.getTotalPages() - 1);
    response.put("nextPage", orderPage.hasNext() ? orderPage.getNumber() + 1 : orderPage.getNumber());
    response.put("previousPage", orderPage.hasPrevious() ? orderPage.getNumber() - 1 : orderPage.getNumber());

    return response;
  }

  // Metoda serwisu przygotowująca model dla formularza edycji zamówienia.
  public Model prepareEditOrderFormModel(Long orderId, Model model) {
    // Pobiera DTO zamówienia na podstawie ID. Jeśli nie znajdzie zamówienia, rzuca wyjątek OrderNotFoundException.
    OrderDTO orderDTO = findOrderDTOById(orderId).orElseThrow(() -> new OrderNotFoundException("Zamówienie o ID " + orderId + " nie zostało znalezione"));
    model.addAttribute("order", orderDTO);
    // Dodaje do modelu listę możliwych statusów zamówienia.
    model.addAttribute("statuses", OrderStatus.values());
    return model;
  }

  // Przygotowanie modelu dla listy zamówień według statusu.
  public void prepareOrdersByStatusModel(OrderStatus orderStatus, Model model) {
    if (orderStatus == null) {
      orderStatus = OrderStatus.NEW_ORDER; // Ustawienie domyślnego statusu, jeśli nie podano.
    }

    List<OrderDTO> orders = findAllOrdersByStatus(orderStatus); // Pobranie zamówień o podanym statusie.
    model.addAttribute("orders", orders); // Dodanie listy zamówień do modelu.
    model.addAttribute("selectedStatus", orderStatus.name()); // Dodanie wybranego statusu do modelu.
  }



}