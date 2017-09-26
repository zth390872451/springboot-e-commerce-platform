package com.svlada.user.repository;

import com.svlada.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRepository
 * 
 * @author vladimir.stankovic
 *
 * Aug 16, 2016
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

}
