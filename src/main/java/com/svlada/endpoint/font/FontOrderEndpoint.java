package com.svlada.endpoint.font;

import com.svlada.common.WebUtil;
import com.svlada.common.request.CustomResponse;
import com.svlada.common.request.CustomResponseStatus;
import com.svlada.common.utils.CommonUtil;
import com.svlada.common.utils.OrderShipUtil;
import com.svlada.common.utils.OrderUtil;
import com.svlada.component.repository.*;
import com.svlada.component.service.OrderServiceImpl;
import com.svlada.component.service.TradeService;
import com.svlada.endpoint.dto.OrderDto;
import com.svlada.endpoint.dto.OrderItemDto;
import com.svlada.endpoint.dto.TradeDTO;
import com.svlada.entity.*;
import com.svlada.entity.product.Product;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.svlada.common.request.CustomResponseBuilder.fail;
import static com.svlada.common.request.CustomResponseBuilder.success;
import static com.svlada.entity.product.Product.STATUS_DOWN;

@RestController
@RequestMapping("/api/font/order")
public class FontOrderEndpoint {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private OrderShipRepository orderShipRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ShopCartRepository shopCartRepository;

    @Autowired
    private OrderServiceImpl orderService;

    @ApiOperation(value = "创建订单", notes = "配送记录，订单详情，微信预支付订单号,商户订单号等")
    @ApiImplicitParam()
    @PostMapping(value = "/create")
    public CustomResponse create(@RequestBody OrderDto orderDto, HttpServletRequest request) {
        //配送地址记录生成
        Address address = addressRepository.findOne(orderDto.getAddressId());
        if (address == null) {
            return fail(CustomResponseStatus._40000, "ID对应的记录不存在!ID=" + orderDto.getAddressId());
        }
        User user = WebUtil.getCurrentUser();
        Long userId = user.getId();

        if (orderDto.getFromCart()) {
            List<Long> productIds = new ArrayList<>();
            Long[] array = new Long[productIds.size()];
            for (OrderItemDto orderItemDto : orderDto.getOrderItemDtos()) {
                Long productId = orderItemDto.getProductId();
                productIds.add(productId);
            }
            //删除购物车中被购买的商品
            productIds.toArray(array);
            shopCartRepository.deleteAllByUserIdAndProductIdIn(userId, array);
        }

        //生成业务订单记录
        Order order = new Order();
        order.setUser(WebUtil.getCurrentUser());
        order.setCreateDate(new Date());
        Set<Product> products = new HashSet<>();
        String goodsName = "";

        Set<OrderItem> orderItems = new HashSet<>();
        for (OrderItemDto orderItemDto : orderDto.getOrderItemDtos()) {
            Long productId = orderItemDto.getProductId();
            Product product = productRepository.findOne(productId);
            if (product != null) {
                if (STATUS_DOWN.equals(product.getStatus())) {
                    return fail(CustomResponseStatus._40000, "商品已经下架!");
                }
                if (product.getStock() < orderItemDto.getNumber()) {
                    return fail(CustomResponseStatus._40000, "商品库存不足!");
                }
                if (!product.getNowPrice().equals(orderItemDto.getUnitPrice())) {
                    return fail(CustomResponseStatus._40000);
                }
                //库存数量-number
                product.setStock(product.getStock() - orderItemDto.getNumber());
                products.add(product);

                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setNumber(orderItemDto.getNumber());
                orderItem.setUnitPrice(orderItemDto.getUnitPrice());
                orderItem.setItemTotalMoney(orderItemDto.getItemTotalMoney());
                orderItem.setName(product.getName());
                orderItem.setFee(orderItem.getNumber() * orderItem.getUnitPrice());
                orderItems.add(orderItem);

                goodsName = goodsName + product.getName() + " ";
            }
        }

        String body = "符嗲嗲商城-" + goodsName;
        String details = "用户购买的商品有:" + goodsName;
        //订单记录对象
        String outTradeNo = OrderUtil.genOrderCode();
        order.setOutTradeNo(outTradeNo);
        order.setItems(orderItems);
        order.setTotalMoney(orderDto.getTotalMoney());
        order.setBody(body);
        order.setDetails(details);
        order.setPayStatus(Order.pay_status_init);

        OrderShip orderShip = OrderShipUtil.getOrderShip(orderDto, address);
        //存储更新操作
        orderService.save(products,orderItems,order,orderShip);

        String ip = CommonUtil.getIpAddr(request);
        //生成微信预支付订单的编号：prepayId
        Integer paymentAmount = order.getTotalMoney().intValue();//支付金额
        String wxpayNotifyUrl = "http://120.77.215.74/callBack/wx";
        String prepayId = new Random().nextInt() + "";
        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setOutTradeNo(prepayId);
//        TradeDTO tradeDTO = new WxClient().unifiedOrder(outTradeNo, body,details, paymentAmount, ip, wxpayNotifyUrl);
        if (tradeDTO != null) {
            //微信订单号生成 成功，业务订单号初始化状态,尚未支付
            Map<String, Object> result = new HashMap<>();
            result.put("prepayId", prepayId);
            return success(result);
        }
        return fail();
    }


    @GetMapping("/get/status/{outTradeNum}")
    public CustomResponse status(@PathVariable("outTradeNum") String outTradeNum) {
        Order order = orderRepository.findOneByOutTradeNo(outTradeNum);
        if (order == null) {
            return fail(CustomResponseStatus._40401, "记录不存在!");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        /*
        map.put("paymentDate", order.getPaymentDate());
        map.put("outTradeNo", order.getWxpayNotify().getOutTradeNo());
        */
        map.put("payStatus", order.getPayStatus());
        return success(order.getPayStatus());
    }


    @GetMapping("/get/{id}")
    public CustomResponse get(@PathVariable("id") Long id) {
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
        Order order = orderRepository.findOne(dto.getId());
        orderRepository.save(order);
        return success();
    }

}
