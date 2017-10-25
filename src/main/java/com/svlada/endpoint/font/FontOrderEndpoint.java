package com.svlada.endpoint.font;

import com.svlada.common.request.CustomResponse;
import com.svlada.common.utils.OrderUtil;
import com.svlada.component.repository.OrderRepository;
import com.svlada.component.repository.ProductRepository;
import com.svlada.endpoint.dto.OrderDto;
import com.svlada.endpoint.dto.OrderItemDto;
import com.svlada.entity.Order;
import com.svlada.entity.OrderItem;
import com.svlada.entity.product.Product;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.svlada.common.request.CustomResponseBuilder.success;

@RestController
@RequestMapping("/api/font/order")
public class FontOrderEndpoint {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;

    @ApiOperation(value = "创建订单", notes = "配送记录，订单详情，微信预支付订单号,商户订单号等")
    @ApiImplicitParam(name = "id", value = "地址记录ID", paramType = "path", required = true, dataType = "Long")
    @PostMapping(value = "/create")
    public CustomResponse create(@RequestBody OrderDto orderDto) {
        //配送地址记录生成

        //生成业务订单
        Order order = new Order();
        String orderCode = OrderUtil.genOrderCode();
        order.setOrderCode(orderCode);
        order.setCreateDate(new Date());
        Set<OrderItem> orderItems = new HashSet<>();
        for (OrderItemDto orderItemDto:orderDto.getOrderItemDtos()){
            Long productId = orderItemDto.getProductId();
            Product product = productRepository.findOne(productId);
            if (product!=null){
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setOrder(order);
                orderItem.setNumber(orderItemDto.getNumber());
                orderItem.setUnitPrice(orderItemDto.getUnitPrice());
                orderItem.setItemTotalMoney(orderItemDto.getItemTotalMoney());
                orderItem.setName(orderItemDto.getName());
                orderItems.add(orderItem);
            }
        }
        orderRepository.save(order);

        //生成微信订单

        return success();
    }

    @GetMapping("/get/{id}")
    public CustomResponse get(@PathVariable("id") Long id){
        Order order = orderRepository.findOne(id);
        return success(order);
    }

    @PutMapping(value = "/update")
    public CustomResponse update(@RequestBody OrderDto dto) {
        Order order = orderRepository.findOne(dto.getId());
        orderRepository.save(order);
        return success();
    }

    @PutMapping(value = "/delete")
    public CustomResponse delete(@RequestBody OrderDto dto) {
        Order order= orderRepository.findOne(dto.getId());
        orderRepository.save(order);
        return success();
    }

}
