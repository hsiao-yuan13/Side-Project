package com.sideproject.spj001.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sideproject.spj001.entity.SellerVO;
import com.sideproject.spj001.service.SellerService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/frontend/seller")
public class SellerLoginController {
	@Autowired
	private SellerService sellerSvc;
	
	@GetMapping("/sellerLogin")
	public String loginPage() {
		return "/frontend/seller/sellerLogin";
	}
	
	@PostMapping("/loginSeller")
	public String loginSeller(@RequestParam("sellerAccount") String sellerAccount, @RequestParam("sellerPassword") String sellerPassword, HttpSession session, Model model) {
		SellerVO sellerVO = sellerSvc.login(sellerAccount, sellerPassword);
		
		if(sellerVO == null) {
			model.addAttribute("errorMsg", "商家帳號密碼錯誤");
			return "sellerLogin";
		}
		
		session.setAttribute("loginSeller", sellerVO);
		session.setAttribute("sellerId", sellerVO.getSellerId());
		
		return "redirect:/index";
	}

}
