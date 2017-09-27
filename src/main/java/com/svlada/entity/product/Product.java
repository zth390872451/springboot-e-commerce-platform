package com.svlada.entity.product;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Product")
public class Product implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //商品价格信息
    private Long price;// 定价
    private Long nowPrice;// 现价
//    private Long unit;//商品单位。默认“item:件”
    private Long hit;// 浏览次数
    private Long stock;//库存
    private Integer status;//商品状态。1：新增，2：已上架，3：已下架

    public static final Integer status_add = 0;
    public static final Integer status_up = 1;
    public static final Integer status_down = 2;
    //商品信息
    @OneToOne
    @JoinColumn(name="category_id")
    private Category category;// 商品分类ID
    @Column(unique = true)
    private String code;//商品编号
    private String name;// 商品名称
    private String title;//商品页面标题
    private String introduce;// 商品简介
    private String description;//页面描述
    private String searchKey;//搜索关键字

    //商品图片信息
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="majorImage_id")
    private List<MajorImage> majorImages;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="detailImage_id")
    private List<DetailsImage> detailsImages;

    //搜索条件
    private Boolean isNew;// 是否新品。n：否，true：是新品上市
    private Boolean specialPrice;// 是否特价。n：否，true：是促销商品
    private Long saleCount;//销售数量
    private Boolean recommend;//true：是推荐商品
    private Boolean mailFree;//true:是卖家包邮
    private Long activityID;//null:不是 >0,则是活动商品(活动id)

    //后台录入
    private Long createID;// 录入人
    private Date createTime;// 录入时间
    private Long updateUserId;//商品修改者
    private Date updateTime;//修改时间


    private Long score;//赠送积分
    private String giftID;//赠品ID


    public static final Integer Product_status_add = 1;//新增商品
    public static final Integer Product_status_y = 2;//已上架
    public static final Integer Product_status_n = 3;//已下架

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getHit() {
        return hit;
    }

    public void setHit(Long hit) {
        this.hit = hit;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public List<MajorImage> getMajorImages() {
        return majorImages;
    }

    public void setMajorImages(List<MajorImage> majorImages) {
        this.majorImages = majorImages;
    }

    public List<DetailsImage> getDetailsImages() {
        return detailsImages;
    }

    public void setDetailsImages(List<DetailsImage> detailsImages) {
        this.detailsImages = detailsImages;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public Boolean getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(Boolean specialPrice) {
        this.specialPrice = specialPrice;
    }

    public Long getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Long saleCount) {
        this.saleCount = saleCount;
    }

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }

    public Boolean getMailFree() {
        return mailFree;
    }

    public void setMailFree(Boolean mailFree) {
        this.mailFree = mailFree;
    }

    public Long getActivityID() {
        return activityID;
    }

    public void setActivityID(Long activityID) {
        this.activityID = activityID;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public String getGiftID() {
        return giftID;
    }

    public void setGiftID(String giftID) {
        this.giftID = giftID;
    }

    public Long getCreateID() {
        return createID;
    }

    public void setCreateID(Long createID) {
        this.createID = createID;
    }

    public static Integer getProduct_status_add() {
        return Product_status_add;
    }

    public static Integer getProduct_status_y() {
        return Product_status_y;
    }

    public static Integer getProduct_status_n() {
        return Product_status_n;
    }
}
