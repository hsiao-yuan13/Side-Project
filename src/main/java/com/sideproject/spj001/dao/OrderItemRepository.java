package com.sideproject.spj001.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sideproject.spj001.entity.OrderItemVO;

public interface OrderItemRepository extends JpaRepository<OrderItemVO, Integer>{

    List<OrderItemVO> findByProductOrder_OrderNo(Integer orderNo);

}
