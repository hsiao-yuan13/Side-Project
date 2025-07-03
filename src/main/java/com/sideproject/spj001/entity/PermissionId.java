package com.sideproject.spj001.entity;

import java.io.Serializable;
import java.util.Objects;

public class PermissionId implements Serializable {
    private static final long serialVersionUID = 1L; // 確保類的序列化一致性

    private Integer func;
    private Integer job;

    public PermissionId(){

    }

    public PermissionId(Integer func, Integer job) {
        this.func = func;
        this.job = job;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionId that = (PermissionId) o;
        return Objects.equals(func, that.func) && Objects.equals(job, that.job);
    }

    @Override
    public int hashCode() {
        return Objects.hash(func, job);
    }
}
