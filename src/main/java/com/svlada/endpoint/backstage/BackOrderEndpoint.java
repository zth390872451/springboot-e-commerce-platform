package com.svlada.endpoint.backstage;

import com.svlada.common.request.CustomResponse;
import com.svlada.common.utils.DateUtils;
import com.svlada.component.repository.OrderRepository;
import com.svlada.endpoint.dto.OrderInfoDto;
import com.svlada.endpoint.dto.OrderSearchDto;
import com.svlada.entity.Order;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
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
    @GetMapping(value = "/list")
    public CustomResponse list(@RequestParam(name = "wechatCode", required = false) String wechatCode,
                               @RequestParam(name = "orderCode", required = false) String orderCode,
                               @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern=DateUtils.FULL_DATE_FORMAT) Date startDate,
                               @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern=DateUtils.FULL_DATE_FORMAT) Date endDate,
                               @RequestParam(name = "payStatus", required = false) Integer payStatus,
                               @PageableDefault(value = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                       Pageable pageable) {
        OrderSearchDto dto = new OrderSearchDto(wechatCode,orderCode, startDate,endDate,payStatus);
        PageRequest pageRequest = null;
        pageRequest = new PageRequest(0,10, Sort.Direction.DESC,"id");
        Page<Order> orderPage = orderRepository.findAll(getSpecification(dto), pageable);
        Page<OrderInfoDto> dtoPage = orderPage.map(entity -> {
            OrderInfoDto orderInfoDto = new OrderInfoDto();
            orderInfoDto.setBody(entity.getBody());
            orderInfoDto.setDetails(entity.getDetails());
            orderInfoDto.setId(entity.getId());
            orderInfoDto.setOutTradeNo(entity.getOutTradeNo());
            orderInfoDto.setPaymentDate(DateUtils.getFormatDate(entity.getPaymentDate(),DateUtils.FULL_DATE_FORMAT));
            orderInfoDto.setPayStatus(entity.getPayStatus());
            orderInfoDto.setTotalMoney(entity.getTotalMoney());
            if (entity.getWxpayNotify()!=null){
                orderInfoDto.setTransactionId(entity.getWxpayNotify().getTransactionId());
            }
            return orderInfoDto;
        });
        return success(dtoPage);
    }

    private Specification<Order> getSpecification(OrderSearchDto orderSearchDto){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(!StringUtils.isEmpty(orderSearchDto.getPayStatus())){
                Predicate predicate = cb.equal(root.get("payStatus").as(Integer.class),orderSearchDto.getPayStatus());
                predicates.add(predicate);
            }
            if(!StringUtils.isEmpty(orderSearchDto.getOrderCode())){
                Predicate predicate = cb.equal(root.get("outTradeNo").as(String.class),orderSearchDto.getOrderCode());
                predicates.add(predicate);
            }
            if(!StringUtils.isEmpty(orderSearchDto.getWechatCode())){
                Predicate predicate = cb.equal(root.get("wechatCode").as(String.class),orderSearchDto.getWechatCode());
                predicates.add(predicate);
            }
            if(!StringUtils.isEmpty(orderSearchDto.getStartDate()) && !StringUtils.isEmpty(orderSearchDto.getStartDate())){
                Predicate predicate =  cb.between(root.<Date>get("paymentDate"),orderSearchDto.getStartDate(),orderSearchDto.getEndDate());
                predicates.add(predicate);
            }
            Predicate[] pre = new Predicate[predicates.size()];
            return query.where(predicates.toArray(pre)).getRestriction();
        };
    }

}
