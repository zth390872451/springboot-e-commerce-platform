package com.svlada.entity;

import com.svlada.entity.product.Product;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="Activity")
public class Activity implements Serializable{

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    private Date startDate;

    private Date endDate;

    private String content;

    private Boolean status;//状态

    @OneToMany(cascade={CascadeType.ALL})
    @JoinColumn(name="product_id")
    private List<Product> productList;//参与活动的产品

    private Integer maxSaleCount;//个人最大限购数量

    private Integer category;//活动类别

    public static final Boolean STATUS_EXPIRE = false;
    public static final Boolean STATUS_NORMAL = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Integer getMaxSaleCount() {
        return maxSaleCount;
    }

    public void setMaxSaleCount(Integer maxSaleCount) {
        this.maxSaleCount = maxSaleCount;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
}
