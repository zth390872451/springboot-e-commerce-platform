package com.svlada.endpoint.backstage;

import com.svlada.common.request.CustomResponse;
import com.svlada.component.repository.OrderRepository;
import com.svlada.endpoint.dto.OrderDto;
import com.svlada.entity.Order;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.svlada.common.request.CustomResponseBuilder.success;

@RestController
@RequestMapping("/api/back/order")
public class OrderEndpoint {

    @Autowired
    private OrderRepository orderRepository;

    @ApiOperation(value="订单信息列表", notes="订单信息列表分页")
    @ApiImplicitParams({
    })
    @PostMapping(value = "/list")
    public CustomResponse list(@RequestBody OrderDto dto) {
        Order order = new Order();
        orderRepository.save(order);
        return success();
    }

}
