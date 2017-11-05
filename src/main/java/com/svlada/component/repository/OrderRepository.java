package com.svlada.component.repository;

import com.svlada.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>  , JpaSpecificationExecutor<Order> {

    Order findOneByOutTradeNo(String outTradeNo);

    List<Order> findOneByUserId(String userId);

    List<Order> findOneByUserIdAndPayStatus(String openId, Integer payStatus);
}
