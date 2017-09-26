package com.svlada.profile.endpoint.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(description = "产品信息Dto")
public class ProductInfoDescDto implements Serializable{

    @ApiModelProperty(notes="商品名称")
    @NotNull(message = "不能为空")
    private String name;// 商品名称

    @ApiModelProperty(notes="商品的唯一编号")
    @NotNull(message = "不能为空")
    private String code;// 商品编号

    @ApiModelProperty(notes="商品页面标题")
    @NotNull(message = "不能为空")
    private String title;//商品页面标题

    @ApiModelProperty(notes="商品页面简介")
    @NotNull(message = "不能为空")
    private String introduce;// 商品简介

    @ApiModelProperty(notes="商品页面描述")
    @NotNull(message = "能为空")
    private String description;//页面描述

    @ApiModelProperty(notes="商品页面标题")
    @NotNull(message = "不能为空")
    private String searchKey;//搜索关键字

    @ApiModelProperty(notes="商品定价")
    @NotNull(message = "不能为空")
    private Long price;

    @ApiModelProperty(notes="商品现价")
    @NotNull(message = "不能为空")
    private Long nowPrice;

    @ApiModelProperty(notes="商品库存")
    @NotNull(message = "不能为空")
    private Long  stock;

    @ApiModelProperty(notes="商品所属类别ID")
    @NotNull(message = "不能为空")
    private Long categoryId;//商品类别ID


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(Long nowPrice) {
        this.nowPrice = nowPrice;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
