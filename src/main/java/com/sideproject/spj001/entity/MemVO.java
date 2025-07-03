package com.sideproject.spj001.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "mem")
public class MemVO implements java.io.Serializable {
    @Id
    @Column(name = "memId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memId;

//    ProductOrderVO fk
    @OneToMany(mappedBy = "mem", cascade = CascadeType.ALL)
    private Set<ProductOrderVO> orders;

//    MessageVO fk
    @OneToMany(mappedBy = "mem", cascade = CascadeType.ALL)
    private Set<MessageVO> msgs;

    @NotBlank(message = "帳號不能為空")
    @Column(name = "memAccount", nullable = false, unique = true)
    private String memAccount;

    @NotBlank(message = "密碼不能為空")
    @Column(name = "memPassword", nullable = false)
    private String memPassword;

    @NotBlank(message = "姓名不能為空")
    @Column(name = "memName", nullable = false)
    private String memName;

    @Past
    @Column(name = "memBirthday", nullable = false)
    private Date memBirthday;

    @Email(message = "請輸入有效的Email")
    @NotBlank(message = "Email不能為空")
    @Column(name = "memEmail", nullable = false, unique = true)
    private String memEmail;

    @NotBlank(message = "地址不能為空")
    @Column(name = "memAddr", nullable = false)
    private String memAddr;

    @Pattern(regexp = "09\\d{8}", message = "手機號碼格式錯誤")
    @Column(name = "memMobile", nullable = false, unique = true)
    private String memMobile;

    @Column(name = "memStatus", nullable = false)
    private String memStatus;


    public MemVO(){

    }


    public Integer getMemId() {
        return memId;
    }

    public void setMemId(Integer memId) {
        this.memId = memId;
    }

    public Set<ProductOrderVO> getOrders() {
        return orders;
    }

    public void setOrders(Set<ProductOrderVO> orders) {
        this.orders = orders;
    }

    public Set<MessageVO> getMsgs() {
        return msgs;
    }

    public void setMsgs(Set<MessageVO> msgs) {
        this.msgs = msgs;
    }

    public String getMemAccount() {
        return memAccount;
    }

    public void setMemAccount(String memAccount) {
        this.memAccount = memAccount;
    }

    public String getMemPassword() {
        return memPassword;
    }

    public void setMemPassword(String memPassword) {
        this.memPassword = memPassword;
    }

    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    public Date getMemBirthday() {
        return memBirthday;
    }

    public void setMemBirthday(Date memBirthday) {
        this.memBirthday = memBirthday;
    }

    public String getMemEmail() {
        return memEmail;
    }

    public void setMemEmail(String memEmail) {
        this.memEmail = memEmail;
    }

    public String getMemAddr() {
        return memAddr;
    }

    public void setMemAddr(String memAddr) {
        this.memAddr = memAddr;
    }

    public String getMemMobile() {
        return memMobile;
    }

    public void setMemMobile(String memMobile) {
        this.memMobile = memMobile;
    }

    public String getMemStatus() {
        return memStatus;
    }

    public void setMemStatus(String memStatus) {
        this.memStatus = memStatus;
    }

//    @Override
//    public String toString() {
//        return "MemVO{" +
//                "memId=" + memId +
//                ", orders=" + orders +
//                ", msgs=" + msgs +
//                ", memAccount='" + memAccount + '\'' +
//                ", memPassword='" + memPassword + '\'' +
//                ", memName='" + memName + '\'' +
//                ", memBirth=" + memBirthday +
//                ", memEmail='" + memEmail + '\'' +
//                ", memAddr='" + memAddr + '\'' +
//                ", memMobile='" + memMobile + '\'' +
//                ", memStatus='" + memStatus + '\'' +
//                '}';
//    }
}
