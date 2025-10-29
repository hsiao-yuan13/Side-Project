package com.sideproject.spj001.controller;

import com.sideproject.spj001.entity.SellerVO;
import com.sideproject.spj001.service.SellerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class SellerController {
    @Autowired
    SellerService sellerSvc;

//============================frontend===========================
//    @Bean
//  public CommandLineRunner encryptPasswords(SellerService sellerService) {
//      return args -> {
//          sellerService.encodeAllPlainPasswords();
//          System.out.println("批次加密完成 ✅");
//      };
//  }
    
//    商家註冊
    @GetMapping("frontend/seller/sellerRegister")
    public String addSeller(Model model){
        SellerVO sellerVO = new SellerVO();
        model.addAttribute("sellerVO", sellerVO);
        return "frontend/seller/sellerRegister";
    }

    @PostMapping("insertSeller")
    public String insert(@Valid SellerVO sellerVO, BindingResult result, Model model){
        sellerSvc.addSeller(sellerVO);
        List<SellerVO> list = sellerSvc.getAll();
        model.addAttribute("sellerListData", list);
        model.addAttribute("success", "新增成功");
        return "redirect:/seller/listAllSeller";
    }

//    更新商家資料
    @PostMapping("getOne_for_update")
    public String getOne_for_update(@RequestParam("sellerId") String sellerId, Model model) throws IOException{
        SellerVO sellerVO = sellerSvc.getOneSeller(Integer.valueOf(sellerId));

        model.addAttribute("sellerVO", sellerVO);
        return "frontend/seller/updateSeller";
    }

    @PostMapping("updateSeller")
    public String update(@Valid SellerVO sellerVO, BindingResult result, Model model) throws IOException{
        sellerSvc.updateSeller(sellerVO);

        model.addAttribute("success", "修改成功");
        sellerVO = sellerSvc.getOneSeller(Integer.valueOf(sellerVO.getSellerId()));
        model.addAttribute("sellerVO", sellerVO);
        return "frontend/seller/mySeller";
    }

    //============================backend===========================
//    商家查詢
    @PostMapping("listSellerCompositeQuery")
    public String listAllSeller(HttpServletRequest req, Model model){
        Map<String, String[]> map = req.getParameterMap();
        List<SellerVO> list = sellerSvc.getAll(map);

        model.addAttribute("sellerListData", list);
        return "backend/seller/listAllSeller";
    }
}
