package com.sideproject.spj001.service;

import com.sideproject.spj001.dao.OrderItemRepository;
import com.sideproject.spj001.entity.OrderItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orderItemService")
public class OrderItemService {
    @Autowired
    OrderItemRepository repository;

    public List<OrderItemVO> getOneOrderItem(Integer orderNo){
        return repository.findByProductOrder_OrderNo(orderNo);
    }
}
