package com.svlada.component.repository;

import com.svlada.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRepository
 *
 * @author vladimir.stankovic
 *
 * Aug 16, 2016
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
