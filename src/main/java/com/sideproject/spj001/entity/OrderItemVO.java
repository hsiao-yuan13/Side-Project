package com.sideproject.spj001.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "orderitem")
public class OrderItemVO implements java.io.Serializable{
    @Id
    @Column(name = "itemSeq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemSeq;

//    fk
    @ManyToOne
    @JoinColumn(name ="orderNo", referencedColumnName = "orderNo", nullable = false)
    private ProductOrderVO productOrder;

//    fk
    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "productId", nullable = false)
    private ProductVO product;

    @Positive
    @Column(name = "productQty", nullable = false)
    private Integer productQty;

    @Positive
    @Column(name = "subTotal", nullable = false)
    private Integer subTotal;



    public OrderItemVO(){

    }


    public Integer getItemSeq() {
        return itemSeq;
    }

    public void setItemSeq(Integer itemSeq) {
        this.itemSeq = itemSeq;
    }

    public ProductOrderVO getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(ProductOrderVO productOrder) {
        this.productOrder = productOrder;
    }

    public ProductVO getProduct() {
        return product;
    }

    public void setProduct(ProductVO product) {
        this.product = product;
    }

    public Integer getProductQty() {
        return productQty;
    }

    public void setProductQty(Integer productQty) {
        this.productQty = productQty;
    }

    public Integer getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Integer subTotal) {
        this.subTotal = subTotal;
    }


    @Override
    public String toString() {
        return "OrderItemVO{" +
                "itemSeq=" + itemSeq +
                ", productOrder=" + productOrder +
                ", product=" + product +
                ", productQty=" + productQty +
                ", subTotal=" + subTotal +
                '}';
    }
}
