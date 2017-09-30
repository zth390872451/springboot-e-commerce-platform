package com.svlada.user.repository;

import com.svlada.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRepository
 *
 * @author vladimir.stankovic
 *
 * Aug 16, 2016
 */
public interface OrderItemlRepository extends JpaRepository<OrderItem, Long> {

}
