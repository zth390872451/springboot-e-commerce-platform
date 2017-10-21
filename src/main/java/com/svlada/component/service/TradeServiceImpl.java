package com.svlada.component.service;

import com.svlada.component.service.define.TradeService;
import com.svlada.endpoint.dto.TradeDTO;
import com.svlada.entity.WxpayNotify;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class TradeServiceImpl implements TradeService{



    @Override
    public TradeDTO wxpay(double balance, String subject, String body, Long price) {
        return null;
    }

    @Override
    public void processWxpayNotify(WxpayNotify wxpayNotify) {

    }
}
