package com.svlada.endpoint.backstage;

import com.svlada.common.request.CustomResponse;
import com.svlada.component.repository.OrderRepository;
import com.svlada.endpoint.dto.OrderDto;
import com.svlada.endpoint.dto.OrderSearchDto;
import com.svlada.endpoint.dto.ProductInfoDescDto;
import com.svlada.endpoint.dto.builder.ProductInfoBuilder;
import com.svlada.entity.Order;
import com.svlada.entity.product.Product;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.svlada.common.request.CustomResponseBuilder.success;

@RestController
@RequestMapping("/api/back/order")
public class BackOrderEndpoint {

    @Autowired
    private OrderRepository orderRepository;

    @ApiOperation(value="订单信息列表", notes="订单信息列表分页")
    @ApiImplicitParams({
    })
    @PostMapping(value = "/list")
    public CustomResponse list(@RequestBody OrderSearchDto dto,@PageableDefault(value = 10, sort = {"saleCount"}, direction = Sort.Direction.DESC)
            Pageable pageable) {
        PageRequest pageRequest = null;
        pageRequest = new PageRequest(0,10, Sort.Direction.DESC,"saleCount");
        Page<Order> orderPage = orderRepository.findAll(getSpecification(dto), pageable);
        Page<OrderDto> dtoPage = orderPage.map(new Converter<Order, OrderDto>() {
            @Override
            public OrderDto convert(Order entity) {
                OrderDto orderDto = new OrderDto();
                return orderDto;
            }
        });
        return success(dtoPage);
    }

    private Specification<Order> getSpecification(OrderSearchDto orderSearchDto){
        return new Specification<Order>(){
            @Override
            public Predicate toPredicate(Root<Order> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(!StringUtils.isEmpty(orderSearchDto.getPayStatus())){
                    Predicate predicate = cb.equal(root.get("payStatus").as(Integer.class),orderSearchDto.getPayStatus());
                    predicates.add(predicate);
                }
                if(!StringUtils.isEmpty(orderSearchDto.getOrderCode())){
                    Predicate predicate = cb.equal(root.get("orderCode").as(String.class),orderSearchDto.getOrderCode());
                    predicates.add(predicate);
                }
                if(!StringUtils.isEmpty(orderSearchDto.getWechatCode())){
                    Predicate predicate = cb.equal(root.get("wechatCode").as(String.class),orderSearchDto.getWechatCode());
                    predicates.add(predicate);
                }
                if(!StringUtils.isEmpty(orderSearchDto.getStartDate()) && !StringUtils.isEmpty(orderSearchDto.getStartDate())){
                    Predicate predicate =  cb.between(root.<Date>get("insDate"),orderSearchDto.getStartDate(),orderSearchDto.getEndDate());
                    predicates.add(predicate);
                }
                Predicate[] pre = new Predicate[predicates.size()];
                return query.where(predicates.toArray(pre)).getRestriction();
            }
        };
    }

}
