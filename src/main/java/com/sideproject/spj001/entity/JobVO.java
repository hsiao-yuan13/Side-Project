package com.sideproject.spj001.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "job")
public class JobVO implements java.io.Serializable {
    @Id
    @Column(name = "jobId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;

    //    EmpVO fk
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private Set<EmpVO> emps;

    //    PermissionVO fk
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private Set<PermissionVO> permissions;

    @Column(name = "title", nullable = false)
    private String title;


    public JobVO() {

    }


    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return "JobVO{" +
                "jobId=" + jobId +
                ", title='" + title + '\'' +
                '}';
    }
}
