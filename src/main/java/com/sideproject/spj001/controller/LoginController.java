package com.sideproject.spj001.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sideproject.spj001.security.MemCustomUserDetails;
import com.sideproject.spj001.security.SellerCustomUserDetails;
import com.sideproject.spj001.service.MemService;
import com.sideproject.spj001.service.SellerService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/frontend/login")
public class LoginController {
    @Autowired
    private MemService memSvc;
    
    @Autowired
	private SellerService sellerSvc;

    @GetMapping("/memLogin")
    public String showMemLoginPage(Model model, HttpServletRequest request){
    	System.out.println("進入 memLoginPage 頁面");
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("當前認證資訊：" + auth);
        
        
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        System.out.println("csrfToken=" + csrfToken);

        if(csrfToken != null) {
        	System.out.println("csrf parameterName=" + csrfToken.getParameterName());
            System.out.println("csrf token=" + csrfToken.getToken());
        	model.addAttribute("_csrf", csrfToken);
        }
        return "frontend/login/memLoginPage";
    }

    
    @GetMapping("/memProfile")
    public String profile(@AuthenticationPrincipal MemCustomUserDetails userDetails) {
    	Integer memId = userDetails.getMemId();
    	System.out.println("當前會員ID:" + memId);
    	return "profile";
    }
   
    
    @GetMapping("/sellerLogin")
	public String showSellerLoginPage(Model model, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
		
		if(csrfToken != null) {
			model.addAttribute("_csrf",csrfToken);
		}
		return "frontend/login/sellerLoginPage";
	}
	
	@GetMapping("/sellerProfile")
	public String profile(@AuthenticationPrincipal SellerCustomUserDetails userDetails) {
		Integer sellerId = userDetails.getSellerId();
		return "profile";
	}

}