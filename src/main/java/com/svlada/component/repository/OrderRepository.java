package com.svlada.component.repository;

import com.svlada.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository extends JpaRepository<Order, Long>  , JpaSpecificationExecutor<Order> {

}
