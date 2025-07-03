package com.sideproject.spj001.entity;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash
public class CartVO implements Serializable {
    private Integer memId;
    private Integer productId;
    private String productName;
	private Integer productPrice;
	private Integer productQty;
	private Integer sellerId;
	
	public CartVO(Integer memId, Integer productId, String productName,
			Integer productPrice, Integer productQty, Integer sellerId) {
		super();
		this.memId = memId;
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productQty = productQty;
		this.sellerId = sellerId;
	}
	
	public CartVO() {
		
	}
	
//	Redis Key
	public String getRedisKey() {
		return "cart:" + memId + ":" + productId;
	}
	
	public Integer getMemId() {
		return memId;
	}
	public void setMemId(Integer memId) {
		this.memId = memId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getProductQty() {
		return productQty;
	}
	public void setProductQty(Integer productQty) {
		this.productQty = productQty;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public Integer getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(Integer productPrice) {
		this.productPrice = productPrice;
	}

	public Integer getSellerId() {
		return sellerId;
	}

	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}
	
	
   
}
