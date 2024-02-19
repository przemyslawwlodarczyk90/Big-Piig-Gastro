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

  public Page<OrderDTO> findAllOrders(Pageable pageable) {
    return orderRepository.findAll(pageable)
            .map(this::convertToOrderDTO);
  }

  public OrderDTO findOrderDTOById(Long id) {
    return orderRepository.findById(id)
            .map(this::convertToOrderDTO)
            .orElse(null);
  }

  public OrderDTO saveOrderDTO(OrderDTO orderDTO) {
    Order order = convertToOrder(orderDTO);
    if (orderDTO.userId() != null) {
      User user = userRepository.findById(orderDTO.userId())
              .orElseThrow(() -> new RuntimeException("User not found"));
      order.setAccountHolder(user);
    }

    if (orderDTO.shippingAddress() != null) {
    }

    order = orderRepository.save(order);
    return convertToOrderDTO(order);
  }

  public List<OrderDTO> findAllOrdersByUserId(long userId) {
    return orderRepository.findByAccountHolder_Id(userId).stream()
            .map(this::convertToOrderDTO)
            .collect(Collectors.toList());
  }

  public boolean updateOrderDTO(Long id, OrderDTO orderDTO) {
    Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new OrderUpdateException("Order not found"));
    updateOrderData(existingOrder, orderDTO);
    existingOrder = orderRepository.save(existingOrder);
    return existingOrder != null;
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
            order.getAccountHolder().getId(),
            statusName,
            order.getDateCreated(),
            order.getSentAt(),
            order.getTotalPrice(),
            lineOfOrdersDTO,
            shippingAddressDTO);
  }

  public boolean updateOrderStatus(Long id, String orderStatus) {
    Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    existingOrder.setOrderStatus(OrderStatus.valueOf(orderStatus.toUpperCase()));
    existingOrder = orderRepository.save(existingOrder);
    return existingOrder != null;
  }
  private Order convertToOrder(OrderDTO orderDTO) {
    Order order = new Order();
    return order;
  }


  private void updateOrderData(Order order, OrderDTO orderDTO) {
  }


  private Address convertToAddress(AddressDTO addressDTO) {
    Address address = new Address();
    return address;
  }

  public OrderDTO createOrderDTO(Long userId, String orderStatus, List<LineOfOrderDTO> lineOfOrders, AddressDTO shippingAddress) {
    LocalDate now = LocalDate.now();
    BigDecimal totalPrice = calculateTotalPrice(lineOfOrders);

    return new OrderDTO(null, userId, orderStatus, now, null, totalPrice, lineOfOrders, shippingAddress);
  }

  private BigDecimal calculateTotalPrice(List<LineOfOrderDTO> lineOfOrders) {
    return BigDecimal.ZERO;
  }


  public Map<String, Object> findAllOrdersWithPagination(Pageable pageable) {
    Page<OrderDTO> orderPage = orderRepository.findAll(pageable)
            .map(this::convertToOrderDTO);

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

  public Model prepareEditOrderFormModel(Long orderId, Model model) throws OrderNotFoundException {
    OrderDTO orderDTO = findOrderDTOById(orderId);
    model.addAttribute("order", orderDTO);
    model.addAttribute("statuses", OrderStatus.values());
    return model;
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