package com.svlada.user.repository;

import com.svlada.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRepository
 * 
 * @author vladimir.stankovic
 *
 * Aug 16, 2016
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

}
