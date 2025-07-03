package com.sideproject.spj001.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "permission")
@IdClass(PermissionVO.class) //使用複合主鍵
public class PermissionVO implements java.io.Serializable{
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcId", referencedColumnName = "funcId", nullable = false)
    private FuncVO func;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobId", referencedColumnName = "jobId", nullable = false)
    private JobVO job;



    public PermissionVO() {

    }


    public FuncVO getFunc() {
        return func;
    }

    public void setFunc(FuncVO func) {
        this.func = func;
    }

    public JobVO getJob() {
        return job;
    }

    public void setJob(JobVO job) {
        this.job = job;
    }


    @Override
    public String toString() {
        return "PermissionVO{" +
                "func=" + func +
                ", job=" + job +
                '}';
    }
}

