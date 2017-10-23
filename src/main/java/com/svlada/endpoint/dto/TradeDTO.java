package com.svlada.endpoint.dto;


public class TradeDTO {
    
    private String outTradeNo;
    
    private Object orderInfo;
    
    
    public TradeDTO(){}

    public TradeDTO(String outTradeNo, Object orderInfo) {
        this.outTradeNo = outTradeNo;
        this.orderInfo = orderInfo;
    }
    

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

	public Object getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(Object orderInfo) {
		this.orderInfo = orderInfo;
	}

    
}
