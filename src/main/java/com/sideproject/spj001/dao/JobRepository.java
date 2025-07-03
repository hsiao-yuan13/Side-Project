package com.sideproject.spj001.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sideproject.spj001.entity.JobVO;

public interface JobRepository extends JpaRepository<JobVO, Integer>{

}
