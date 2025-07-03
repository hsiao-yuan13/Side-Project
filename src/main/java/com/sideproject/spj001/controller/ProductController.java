package com.sideproject.spj001.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sideproject.spj001.entity.ProductVO;
import com.sideproject.spj001.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/frontend")
public class ProductController {
    @Autowired
    ProductService productSvc;

    //============================shop===========================
//    所有商品介面(可查詢)
    @GetMapping("/shop/shop")
    public String listAllProduct(HttpServletRequest req, Model model){
        Map<String, String[]> map = req.getParameterMap();
        List<ProductVO> list = productSvc.showAll("上架", map);
        System.out.println(list.size());
        
        model.addAttribute("productListData", list);
        return "frontend/shop/shop";
    }

//    單一商品資訊介面
    @GetMapping("/shop/singleProduct/{productId}")
    public String SingleProduct(@PathVariable("productId") Integer productId, Model model) throws IOException{
        System.out.println("Received productId: " + productId); // 日誌輸出

    	ProductVO productVO = productSvc.getOneProduct(productId);

        model.addAttribute("productVO", productVO);
        return "frontend/shop/singleProduct";
    }

    //============================seller===========================
//    商家所有商品(可查詢)
    @PostMapping("backend/seller/product")
    public String SellerProduct(HttpSession session, HttpServletRequest req, Model model){
        Integer sellerId = (Integer)session.getAttribute("sellerId");
        if(sellerId == null){
            return "redirect:/login";
        }

        Map<String, String[]> map = req.getParameterMap();
        List<ProductVO> list = productSvc.getAll(sellerId, map);

        model.addAttribute("productListData", list);
        return "frontend/seller/product";
    }

//    新增商品
    @GetMapping("/seller/addProduct")
    public String addProduct(Model model){
        ProductVO productVO = new ProductVO();
        model.addAttribute("productVO", productVO);
        return "frontend/seller/addProduct";
    }

    @PostMapping("/seller/insertProduct")
    public String insert(@Valid ProductVO productVO, BindingResult result, Model model){
        productSvc.addProduct(productVO);
        List<ProductVO> list = productSvc.getAll();
        model.addAttribute("productListData", list);
        model.addAttribute("success", "新增成功");
        return "redirect:/seller/product";
    }

//    管理商品
    @PostMapping("/seller/product")
    public String getOne_for_update(@RequestParam("productId") String productId, Model model) throws IOException{
        ProductVO productVO = productSvc.getOneProduct(Integer.valueOf(productId));

        model.addAttribute("productVO", productVO);
        return "frontend/seller/updateProduct";
    }

    @PostMapping("/seller/updateProduct")
    public String update(@Valid ProductVO productVO, BindingResult result, Model model) throws IOException{
        productSvc.updateProduct(productVO);

        model.addAttribute("success", "修改成功");
        productVO = productSvc.getOneProduct(Integer.valueOf(productVO.getProductId()));
        model.addAttribute("productVO", productVO);
        return "frontend/seller/product";
    }
    

    
}