package com.sideproject.spj001.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "emp")
public class EmpVO implements java.io.Serializable{
    @Id
    @Column(name = "empId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer empId;

    @NotBlank(message = "密碼不能為空")
    @Column(name = "empPassword", nullable = false)
    private String empPassword;

    @NotBlank(message = "姓名不能為空")
    @Column(name = "empName", nullable = false)
    private String empName;

    @Email(message = "請輸入有效Email")
    @NotBlank(message = "Email不能為空")
    @Column(name = "empEmail", nullable = false, unique = true)
    private String empEmail;

    @Pattern(regexp = "09\\d{8}", message = "手機號瑪格式錯誤")
    @Column(name = "empMobile", nullable = false, unique = true)
    private String empMobile;

    @NotBlank(message = "地址不能為空")
    @Column(name = "empAddr", nullable = false)
    private String empAddr;

    @PastOrPresent
    @Column(name = "hireDate", nullable = false)
    private LocalDateTime hireDate;

//    fk
    @ManyToOne
    @JoinColumn(name = "jobId", referencedColumnName = "jobId", nullable = false)
    private JobVO job;

    @Column(name = "empStatus", nullable = false)
    private String empStatus;



    public EmpVO(){

    }


    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpPassword() {
        return empPassword;
    }

    public void setEmpPassword(String empPassword) {
        this.empPassword = empPassword;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getEmpMobile() {
        return empMobile;
    }

    public void setEmpMobile(String empMobile) {
        this.empMobile = empMobile;
    }

    public String getEmpAddr() {
        return empAddr;
    }

    public void setEmpAddr(String empAddr) {
        this.empAddr = empAddr;
    }

    public LocalDateTime getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDateTime hireDate) {
        this.hireDate = hireDate;
    }

    public JobVO getJob() {
        return job;
    }

    public void setJob(JobVO job) {
        this.job = job;
    }

    public String getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(String empStatus) {
        this.empStatus = empStatus;
    }


    @Override
    public String toString() {
        return "EmpVO{" +
                "empId=" + empId +
                ", empPassword='" + empPassword + '\'' +
                ", empName='" + empName + '\'' +
                ", empEmail='" + empEmail + '\'' +
                ", empMobile='" + empMobile + '\'' +
                ", empAddr='" + empAddr + '\'' +
                ", hireDate=" + hireDate +
                ", job=" + job +
                ", empStatus='" + empStatus + '\'' +
                '}';
    }
}
