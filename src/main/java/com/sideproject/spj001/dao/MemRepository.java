package com.sideproject.spj001.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sideproject.spj001.entity.MemVO;

public interface MemRepository extends JpaRepository<MemVO, Integer>{

    MemVO findByMemAccount(String memAccount);

}
