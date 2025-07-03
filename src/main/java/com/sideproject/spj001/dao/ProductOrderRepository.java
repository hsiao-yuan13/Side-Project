package com.sideproject.spj001.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sideproject.spj001.entity.ProductOrderVO;

public interface ProductOrderRepository extends JpaRepository<ProductOrderVO, Integer>{

    List<ProductOrderVO> findByMem_MemId(Integer memId);

	Optional<ProductOrderVO> findByMerchantTradeNo(String merchantTradeNo);

}
