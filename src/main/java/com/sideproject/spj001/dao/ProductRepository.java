package com.sideproject.spj001.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sideproject.spj001.entity.ProductVO;

public interface ProductRepository extends JpaRepository<ProductVO, Integer>{

}
