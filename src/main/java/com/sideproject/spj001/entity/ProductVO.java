package com.sideproject.spj001.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "product")
public class ProductVO implements java.io.Serializable{
    @Id
    @Column(name = "productId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

//    fk
    @ManyToOne
    @JoinColumn(name = "sellerId", referencedColumnName = "sellerId")
    private SellerVO seller;

//    OrderItemVO fk
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<OrderItemVO> items;

    @NotBlank(message = "商品名稱不能為空")
    @Column(name = "productName", nullable = false)
    private String productName;

    @NotNull(message = "商品圖片不能為空")
    @Lob
    @Column(name = "productPic", nullable = false)
    private byte[] productPic;

    @Column(name = "productInfo")
    private String productInfo;

    @Positive(message = "商品價格必須大於0")
    @Column(name = "productPrice", nullable = false)
    private Integer productPrice;

    @PositiveOrZero(message = "庫存數量不能為負數")
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "productStatus", nullable = false)
    private String productStatus;


    public ProductVO(){

    }


    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public SellerVO getSeller() {
        return seller;
    }

    public void setSeller(SellerVO seller) {
        this.seller = seller;
    }

    public Set<OrderItemVO> getItems() {
        return items;
    }

    public void setItems(Set<OrderItemVO> items) {
        this.items = items;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public byte[] getProductPic() {
        return productPic;
    }

    public void setProductPic(byte[] productPic) {
        this.productPic = productPic;
    }

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }


//    @Override
//    public String toString() {
//        return "ProductVO{" +
//                "productId=" + productId +
//                ", seller=" + seller +
//                ", items=" + items +
//                ", productName='" + productName + '\'' +
//                ", productPic=" + Arrays.toString(productPic) +
//                ", productInfo='" + productInfo + '\'' +
//                ", productPrice=" + productPrice +
//                ", stock=" + stock +
//                ", productStatus='" + productStatus + '\'' +
//                '}';
//    }
}
