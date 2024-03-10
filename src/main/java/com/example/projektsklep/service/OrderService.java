package com.example.projektsklep.service;

import com.example.projektsklep.exception.OrderNotFoundException;
import com.example.projektsklep.exception.UserNotFoundException;
import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.LineOfOrderDTO;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.repository.OrderRepository;
import com.example.projektsklep.model.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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




  public Optional<OrderDTO> findOrderDTOById(Long id) {
    return orderRepository.findById(id)
            .map(this::convertToOrderDTO);

  }

  public List<OrderDTO> findAllOrdersByUserId(long userId) {
    return orderRepository.findByAccountHolder_Id(userId).stream()
            .map(this::convertToOrderDTO)
            .collect(Collectors.toList());
  }




  public List<OrderDTO> findAllOrdersByStatus(OrderStatus orderStatus) {
    return orderRepository.findAllByOrderStatus(orderStatus).stream()
            .map(this::convertToOrderDTO)
            .collect(Collectors.toList());
  }

  public List<OrderDTO> findOrdersForUserEmail(String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("Użytkownik o podanym adresie e-mail nie istnieje."));
    return findAllOrdersByUserId(user.getId());
  }


  private OrderDTO convertToOrderDTO(Order order) {
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

  public void updateOrderStatus(Long id, String orderStatus) {
    Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    existingOrder.setOrderStatus(OrderStatus.valueOf(orderStatus.toUpperCase()));
    existingOrder = orderRepository.save(existingOrder);
  }


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


  public void prepareEditOrderFormModel(Long orderId, Model model) {
    OrderDTO orderDTO = findOrderDTOById(orderId).orElseThrow(() -> new OrderNotFoundException("Zamówienie o ID " + orderId + " nie zostało znalezione"));
    model.addAttribute("order", orderDTO);
    model.addAttribute("statuses", OrderStatus.values());
  }

  public void prepareOrdersByStatusModel(OrderStatus orderStatus, Model model) {
    if (orderStatus == null) {
      orderStatus = OrderStatus.NEW_ORDER;
    }

    List<OrderDTO> orders = findAllOrdersByStatus(orderStatus);
    model.addAttribute("orders", orders);
    model.addAttribute("selectedStatus", orderStatus.name());
  }



}