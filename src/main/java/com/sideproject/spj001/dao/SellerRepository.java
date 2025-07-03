package com.sideproject.spj001.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sideproject.spj001.entity.SellerVO;

public interface SellerRepository extends JpaRepository<SellerVO, Integer>{

	SellerVO findBySellerAccount(String sellerAccount);

}
