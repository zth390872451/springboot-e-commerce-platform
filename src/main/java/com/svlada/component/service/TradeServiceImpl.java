package com.svlada.component.service;

import com.svlada.component.repository.OrderItemRepository;
import com.svlada.component.repository.OrderRepository;
import com.svlada.component.repository.WxpayNotifyRepository;
import com.svlada.common.utils.wx.TimeUtil;
import com.svlada.endpoint.dto.TradeDTO;
import com.svlada.entity.Order;
import com.svlada.entity.OrderItem;
import com.svlada.entity.WxpayNotify;
import com.svlada.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Set;

import static com.svlada.entity.Order.pay_status_init;
import static com.svlada.entity.Order.pay_status_success;

@Transactional
@Service
public class TradeServiceImpl implements TradeService{

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private WxpayNotifyRepository wxpayNotifyRepository;
    @Override
    public TradeDTO wxpay(double balance, String subject, String body, Long price) {
        return null;
    }

    @Override
    public void processWxpayNotify(WxpayNotify wxpayNotify) {
        Order order = orderRepository.findOneByOutTradeNo(wxpayNotify.getOutTradeNo());
        if (order!=null && order.getPayStatus().equals(pay_status_init)){
            order.setPayStatus(pay_status_success);
            WxpayNotify orderWxpayNotify = order.getWxpayNotify();
            if (orderWxpayNotify==null){//没有关联关系,建立
                orderWxpayNotify = wxpayNotify;

            }
            //保存回调记录
            wxpayNotifyRepository.save(wxpayNotify);
            //更新订单的状态
            order.setWxpayNotify(wxpayNotify);
            order.setPayStatus(pay_status_success);
            order.setPaymentDate(TimeUtil.getDateStrTo(wxpayNotify.getTimeEnd(), TimeUtil.FORMAT_YMD_HMS));
            orderRepository.save(order);
            //更新库存、销量()
            Set<OrderItem> items = order.getItems();
            for (OrderItem orderItem:items){
                Product product = orderItem.getProduct();
                product.setStock(product.getStock()-orderItem.getNumber());
                product.setSaleCount(product.getSaleCount()+orderItem.getNumber());
            }
        }
    }
}
