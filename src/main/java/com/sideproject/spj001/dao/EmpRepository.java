package com.sideproject.spj001.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sideproject.spj001.entity.EmpVO;

public interface EmpRepository extends JpaRepository<EmpVO, Integer>{

}
