package com.svlada.endpoint.dto;


import java.io.Serializable;

public class ShopCartDto implements Serializable{

   private Long productId;//商品ID

   private Long number;//商品数量


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }
}
