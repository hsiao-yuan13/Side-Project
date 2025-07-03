package com.sideproject.spj001.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Entity
@Table(name = "func")
public class FuncVO implements java.io.Serializable{
    @Id
    @Column(name = "funcId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer funcId;

//    PermissionVO fk
    @OneToMany(mappedBy = "func", cascade = CascadeType.ALL)
    private Set<PermissionVO> permissions;

    @NotBlank(message = "功能名稱不得為空")
    @Column(name = "funcName", nullable = false, unique = true)
    private String funcName;

    @NotBlank(message = "功能介紹不得為空")
    @Column(name = "funcDetail", nullable = false)
    private String funcDetail;


    public FuncVO(){

    }


    public Integer getFuncId() {
        return funcId;
    }

    public void setFuncId(Integer funcId) {
        this.funcId = funcId;
    }

    public Set<PermissionVO> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionVO> permissions) {
        this.permissions = permissions;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getFuncDetail() {
        return funcDetail;
    }

    public void setFuncDetail(String funcDetail) {
        this.funcDetail = funcDetail;
    }


    @Override
    public String toString() {
        return "FuncVO{" +
                "funcId=" + funcId +
                ", permissions=" + permissions +
                ", funcName='" + funcName + '\'' +
                ", funcDetail='" + funcDetail + '\'' +
                '}';
    }
}
