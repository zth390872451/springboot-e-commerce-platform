package com.svlada.endpoint.dto;


import java.io.Serializable;

public class OrderDto implements Serializable{

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}