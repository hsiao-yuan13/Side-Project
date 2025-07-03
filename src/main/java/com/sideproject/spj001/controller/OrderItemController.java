package com.sideproject.spj001.controller;

import com.sideproject.spj001.entity.OrderItemVO;
import com.sideproject.spj001.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/frontend")
public class OrderItemController {
    @Autowired
    OrderItemService orderItemSvc;
//============================frontend/mem===========================
//列出特定訂單明細
    @PostMapping("/mem/listOrderItem")
    public String showOrderItem(@RequestParam("orderNo") Integer orderNo, Model model) throws IOException{
        List<OrderItemVO> list = orderItemSvc.getOneOrderItem(orderNo);

        model.addAttribute("orderListData", list);
        return "frontend/mem/listOrderItem";
    }
//============================frontend/seller===========================
//列出特定訂單明細
@PostMapping("/seller/order")
public String getOrderItem(@RequestParam("orderNo") Integer orderNo, Model model) throws IOException{
    List<OrderItemVO> list = orderItemSvc.getOneOrderItem(orderNo);

    model.addAttribute("orderListData", list);
    return "frontend/seller/order";
}
}
