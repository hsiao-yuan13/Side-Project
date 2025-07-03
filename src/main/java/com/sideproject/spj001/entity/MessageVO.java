package com.sideproject.spj001.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
public class MessageVO implements java.io.Serializable{
    @Id
    @Column(name = "msgNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer msgNo;

//    fk
     @ManyToOne
     @JoinColumn(name = "sellerId", referencedColumnName = "sellerId")
     private SellerVO seller;

//     fk
     @ManyToOne
     @JoinColumn(name = "memId", referencedColumnName = "memId")
     private MemVO mem;

    @Column(name = "msgName", nullable = false)
    private String msgName;

    @Column(name = "msgDetail", nullable = false)
    private String msgDetail;

    @PastOrPresent
    @Column(name = "msgTime", nullable = false)
    private LocalDateTime msgTime;

    @Column(name = "msgStatus", nullable = false)
    private String msgStatus;


     public MessageVO(){

     }


    public Integer getMsgNo() {
        return msgNo;
    }

    public void setMsgNo(Integer msgNo) {
        this.msgNo = msgNo;
    }

    public SellerVO getSeller() {
        return seller;
    }

    public void setSeller(SellerVO seller) {
        this.seller = seller;
    }

    public MemVO getMem() {
        return mem;
    }

    public void setMem(MemVO mem) {
        this.mem = mem;
    }

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }

    public String getMsgDetail() {
        return msgDetail;
    }

    public void setMsgDetail(String msgDetail) {
        this.msgDetail = msgDetail;
    }

    public LocalDateTime getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(LocalDateTime msgTime) {
        this.msgTime = msgTime;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }


    @Override
    public String toString() {
        return "MessageVO{" +
                "msgNo=" + msgNo +
                ", seller=" + seller +
                ", mem=" + mem +
                ", msgName='" + msgName + '\'' +
                ", msgDetail='" + msgDetail + '\'' +
                ", msgTime=" + msgTime +
                ", msgStatus='" + msgStatus + '\'' +
                '}';
    }
}
