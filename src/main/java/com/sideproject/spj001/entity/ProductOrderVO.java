package com.sideproject.spj001.entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "productorder")
public class ProductOrderVO implements java.io.Serializable{
    @Id
    @Column(name = "orderNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderNo;

//    OrderItemVO fk
    @OneToMany(mappedBy = "productOrder", cascade = CascadeType.ALL)
    private Set<OrderItemVO> items;

//    fk
    @ManyToOne
    @JoinColumn(name = "memId", referencedColumnName = "memId", nullable = false)
    private MemVO mem;

//    fk
    @ManyToOne
    @JoinColumn(name = "sellerId", referencedColumnName = "sellerId", nullable = false)
    private SellerVO seller;

    @PastOrPresent
    @Column(name = "orderDate", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "orderStatus", nullable = false)
    private String orderStatus;

    @Positive
    @Column(name = "total", nullable = false)
    private Integer total;
    
    
    @Column(name = "merchantTradeNo", unique = true)
    private String merchantTradeNo;
    
    @Column(name = "tradeNo")
    private String tradeNo;
    
    @Column(name = "payment")
    private String payment;
    
    @Column(name = "paymentStatus")
    private String paymentStatus;



    public ProductOrderVO(){

    }


    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Set<OrderItemVO> getItems() {
        return items;
    }

    public void setItems(Set<OrderItemVO> items) {
        this.items = items;
    }

    public MemVO getMem() {
        return mem;
    }

    public void setMem(MemVO mem) {
        this.mem = mem;
    }

    public SellerVO getSeller() {
        return seller;
    }

    public void setSeller(SellerVO seller) {
        this.seller = seller;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
    


	public String getMerchantTradeNo() {
		return merchantTradeNo;
	}


	public void setMerchantTradeNo(String merchantTradeNo) {
		this.merchantTradeNo = merchantTradeNo;
	}


	public String getTradeNo() {
		return tradeNo;
	}


	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}


	public String getPayment() {
		return payment;
	}


	public void setPayment(String payment) {
		this.payment = payment;
	}


	public String getPaymentStatus() {
		return paymentStatus;
	}


	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}


	
}
