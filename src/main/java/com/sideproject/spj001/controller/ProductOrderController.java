package com.sideproject.spj001.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sideproject.spj001.entity.ProductOrderVO;
import com.sideproject.spj001.service.CartService;
import com.sideproject.spj001.service.ProductOrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/frontend")
public class ProductOrderController {
    @Autowired
    ProductOrderService productOrderSvc;
    
    @Autowired
    CartService cartSvc;

    //============================frontend/mem===========================

    //會員所有訂單
    @PostMapping("/mem/myOrder")
    public String memOrder(HttpSession session, Model model) {
        Integer memId = (Integer) session.getAttribute("memId");
        if (memId == null) {
            return "redirect:/login";
        }

        List<ProductOrderVO> list = productOrderSvc.showAll(memId);

        model.addAttribute("merchOrderListData", list);
        return "frontend/mem/myOrder";
    }

    //取消訂單
    @PostMapping("/mem/myOrder/delete")
    public String getOneOrder(@RequestParam("orderNo") Integer orderNo, Model model) throws IOException {
        ProductOrderVO productOrderVO = productOrderSvc.getOneOrder(Integer.valueOf(orderNo));

        model.addAttribute("merchOrderVO", productOrderVO);
        return "frontend/mem/listOneOrder";
    }

    @PostMapping("/mem/listOneOrder")
    public String cancelOrder(@RequestParam("orderNo") Integer orderNo, RedirectAttributes redirectAttributes) throws IOException {
    	productOrderSvc.updateOrderStatus(orderNo, "已取消");

        redirectAttributes.addFlashAttribute("success", "取消成功");
        return "redirect:/mem/myOrder";
    }

    //============================frontend/seller===========================
//查詢訂單
    @PostMapping("/seller/orderList")
    public String listAllOrder(HttpSession session, HttpServletRequest req, Model model) {
        Integer sellerId = (Integer) session.getAttribute("sellerId");
        if (sellerId == null) {
            return "redirect:/login";
        }

        Map<String, String[]> map = req.getParameterMap();
        List<ProductOrderVO> list = productOrderSvc.getAll(sellerId, map);

        model.addAttribute("merchOrderListData", list);
        return "frontend/seller/order";
    }
//出貨

    @PostMapping("/seller/orderList/ship")
    public String getOrderDetail(@RequestParam("orderNo") String orderNo, Model model) throws IOException {
        ProductOrderVO productOrderVO = productOrderSvc.getOneOrder(Integer.valueOf(orderNo));

        model.addAttribute("merchOrderVO", productOrderVO);
        return "frontend/seller/order";
    }

    //    出貨完成
    @PostMapping("/seller/order/prepared")
    public String orderPrepared(@RequestParam("orderNo") Integer orderNo, RedirectAttributes redirectAttributes) throws IOException {
    	productOrderSvc.updateOrderStatus(orderNo, "已出貨");

        redirectAttributes.addFlashAttribute("success", "備單完成");
        return "redirect://seller/orderList";
    }

    //完成訂單
    @PostMapping("/seller/order/complete")
    public String orderComplete(@RequestParam("orderNo") Integer orderNo, RedirectAttributes redirectAttributes) throws IOException {
    	productOrderSvc.updateOrderStatus(orderNo, "已領貨");

        redirectAttributes.addFlashAttribute("success", "訂單完成");
        return "redirect://seller/orderList";
    }
}
