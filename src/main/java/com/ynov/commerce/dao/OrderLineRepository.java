package com.ynov.commerce.dao;

import com.ynov.commerce.entities.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {}
