package com.svlada.endpoint.dto;


import java.io.Serializable;

public class OrderDto implements Serializable{

    private Long id;

    private Long addressId;

    private OrderItemDto[] orderItemDtos;

    private Long totalMoney;

    public Long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Long totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public OrderItemDto[] getOrderItemDtos() {
        return orderItemDtos;
    }

    public void setOrderItemDtos(OrderItemDto[] orderItemDtos) {
        this.orderItemDtos = orderItemDtos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
