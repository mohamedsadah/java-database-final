package com.project.code.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.code.Model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}


