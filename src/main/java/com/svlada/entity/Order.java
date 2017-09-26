package com.svlada.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="User_Order")
public class Order implements Serializable{

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
