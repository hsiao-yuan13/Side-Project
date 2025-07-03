package com.sideproject.spj001.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "seller")
public class SellerVO implements java.io.Serializable{
    @Id
    @Column(name = "sellerId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sellerId;

//    ProductVO fk
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private Set<ProductVO> products;

//    MessageVO fk
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private Set<MessageVO> msgs;

//    ProductOrderVO fk
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private Set<ProductOrderVO> orders;

    @NotBlank(message = "帳號不能為空")
    @Column(name = "sellerAccount", nullable = false, unique = true)
    private String sellerAccount;

    @NotBlank(message = "密碼不能為空")
    @Column(name = "sellerPassword", nullable = false)
    private String sellerPassword;

    @NotBlank(message = "名稱不能為空")
    @Column(name = "sellerName", nullable = false)
    private String sellerName;

    @PastOrPresent
    @Column(name = "establishedDate", nullable = false)
    private LocalDateTime establishedDate;

    @Email(message = "請輸入有效的Email")
    @NotBlank(message = "Email不能為空")
    @Column(name = "sellerEmail", nullable = false, unique = true)
    private String sellerEmail;

    @Pattern(regexp = "09\\d{8}", message = "手機號碼格式錯誤")
    @Column(name = "sellerMobile", nullable = false, unique = true)
    private String sellerMobile;

    @NotBlank(message = "地址不能為空")
    @Column(name = "sellerAddr", nullable = false)
    private String sellerAddr;

    @Column(name = "sellerStatus", nullable = false)
    private String sellerStatus;

   public SellerVO(){

   }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Set<ProductVO> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductVO> products) {
        this.products = products;
    }

    public Set<MessageVO> getMsgs() {
        return msgs;
    }

    public void setMsgs(Set<MessageVO> msgs) {
        this.msgs = msgs;
    }

    public Set<ProductOrderVO> getOrders() {
        return orders;
    }

    public void setOrders(Set<ProductOrderVO> orders) {
        this.orders = orders;
    }

    public String getSellerAccount() {
        return sellerAccount;
    }

    public void setSellerAccount(String sellerAccount) {
        this.sellerAccount = sellerAccount;
    }

    public String getSellerPassword() {
        return sellerPassword;
    }

    public void setSellerPassword(String sellerPassword) {
        this.sellerPassword = sellerPassword;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public LocalDateTime getEstablishedDate() {
        return establishedDate;
    }

    public void setEstablishedDate(LocalDateTime establishedDate) {
        this.establishedDate = establishedDate;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getSellerMobile() {
        return sellerMobile;
    }

    public void setSellerMobile(String sellerMobile) {
        this.sellerMobile = sellerMobile;
    }

    public String getSellerAddr() {
        return sellerAddr;
    }

    public void setSellerAddr(String sellerAddr) {
        this.sellerAddr = sellerAddr;
    }

    public String getSellerStatus() {
        return sellerStatus;
    }

    public void setSellerStatus(String sellerStatus) {
        this.sellerStatus = sellerStatus;
    }

//    @Override
//    public String toString() {
//        return "SellerVO{" +
//                "sellerId=" + sellerId +
//                ", products=" + products +
//                ", msgs=" + msgs +
//                ", orders=" + orders +
//                ", sellerAccount='" + sellerAccount + '\'' +
//                ", sellerPassword='" + sellerPassword + '\'' +
//                ", sellerName='" + sellerName + '\'' +
//                ", establishedDate=" + establishedDate +
//                ", sellerEmail='" + sellerEmail + '\'' +
//                ", sellerMobile='" + sellerMobile + '\'' +
//                ", sellerAddr='" + sellerAddr + '\'' +
//                ", sellerStatus='" + sellerStatus + '\'' +
//                '}';
//    }
}
