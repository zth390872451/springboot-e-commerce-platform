package com.svlada.entity;import javax.persistence.*;import java.io.Serializable;/** * 订单配送表 */@Entity@Table(name="ORDER_SHIP")public class OrderShip implements Serializable {	private static final long serialVersionUID = 1L;	@Id	@Column(name="ID")	@GeneratedValue(strategy = GenerationType.AUTO)	private Long id;	@OneToOne	@JoinColumn(name = "order_id")//外键关联Order表	private Order order;	private String consigneeName;//收货人	private String consigneeAddress;//详细地址	private String province;//	private String provinceCode;//省份代码	private String city;//城市	private String cityCode;//城市代码	private String area;//区域	private String areaCode;//区域代码	private String phone;//座机	private String tel;//手机	private String zip;//邮编	private String sex;//性别	private String remark;//备注	public Long getId() {		return id;	}	public void setId(Long id) {		this.id = id;	}	public Order getOrder() {		return order;	}	public void setOrder(Order order) {		this.order = order;	}	public String getConsigneeName() {		return consigneeName;	}	public void setConsigneeName(String consigneeName) {		this.consigneeName = consigneeName;	}	public String getConsigneeAddress() {		return consigneeAddress;	}	public void setConsigneeAddress(String consigneeAddress) {		this.consigneeAddress = consigneeAddress;	}	public String getProvince() {		return province;	}	public void setProvince(String province) {		this.province = province;	}	public String getProvinceCode() {		return provinceCode;	}	public void setProvinceCode(String provinceCode) {		this.provinceCode = provinceCode;	}	public String getCity() {		return city;	}	public void setCity(String city) {		this.city = city;	}	public String getCityCode() {		return cityCode;	}	public void setCityCode(String cityCode) {		this.cityCode = cityCode;	}	public String getArea() {		return area;	}	public void setArea(String area) {		this.area = area;	}	public String getAreaCode() {		return areaCode;	}	public void setAreaCode(String areaCode) {		this.areaCode = areaCode;	}	public String getPhone() {		return phone;	}	public void setPhone(String phone) {		this.phone = phone;	}	public String getTel() {		return tel;	}	public void setTel(String tel) {		this.tel = tel;	}	public String getZip() {		return zip;	}	public void setZip(String zip) {		this.zip = zip;	}	public String getSex() {		return sex;	}	public void setSex(String sex) {		this.sex = sex;	}	public String getRemark() {		return remark;	}	public void setRemark(String remark) {		this.remark = remark;	}}