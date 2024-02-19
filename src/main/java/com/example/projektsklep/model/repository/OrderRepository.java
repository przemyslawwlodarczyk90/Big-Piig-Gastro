package com.example.projektsklep.model.repository;


import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {


    List<Order> findByAccountHolder_Id(Long accountHolderId);


    List<Order> findAllByOrderStatus(OrderStatus orderStatus);


    Optional<Order> findById(long id);


    Order save(Order order);


    void delete(Order order);


    Page<Order> findAll(Pageable pageable);


}