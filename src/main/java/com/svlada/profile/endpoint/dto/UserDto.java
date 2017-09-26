package com.svlada.profile.endpoint.dto;


import java.io.Serializable;

public class UserDto implements Serializable{

    private Long id;

    private String username;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
