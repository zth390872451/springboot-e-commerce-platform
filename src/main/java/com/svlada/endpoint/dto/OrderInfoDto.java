package com.svlada.endpoint.dto;

import java.io.Serializable;
import java.util.Date;

public class OrderInfoDto implements Serializable{
    /**
     * 订单ID
     */
    private Long id;
    /**
     * 微信支付订单号
     */
    private String transactionId;
    /**
     * 商品名称
     */
    private String body;
    /**
     * 商品描述(该笔订单的备注、描述、明细等)
     */
    private String details;

    /**
     * 交易支付时间
     */
    private String paymentDate;
    /**
     * 支付金额
     */
    private Long totalMoney;

    /**
     * 订单号唯一[商户订单号]
     */
    private String outTradeNo;

    private Integer payStatus;//订单的支付状态 0：尚未支付 1：支付成功 2:订单支付超时(失效) 3:支付失败[暂时无用]

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


    public Long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Long totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }
}
