package com.sideproject.spj001.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sideproject.spj001.entity.CartVO;
import com.sideproject.spj001.security.CurrentUser;
import com.sideproject.spj001.security.MemCustomUserDetails;
import com.sideproject.spj001.service.AuthService;
import com.sideproject.spj001.service.CartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/frontend/cart")
public class CartController {
    @Autowired
    private CartService cartSvc;
    
    @Autowired
    private AuthService authSvc;
    
    @GetMapping("/currentUser")
    public CurrentUser getCurrentUser() {
    	return authSvc.getCurrentUser();
    }

    @PostMapping("/addToCart")
    @ResponseBody
    public String addToCart(@RequestBody CartVO cartVO, Authentication authentication){
    	if (authentication == null || !authentication.isAuthenticated() ||
    		    authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_MEMBER"))) {
    		    return "請先登入會員";
    		}
    	
    	MemCustomUserDetails memUser = (MemCustomUserDetails) authentication.getPrincipal();
    	cartVO.setMemId(memUser.getMemId());
    	cartSvc.addToCart(cartVO);
		return "商品已加入購物車";
    }
    

    @GetMapping("/showCart")
    @ResponseBody
    public List<CartVO> getCart(Authentication authentication){
        if(authentication == null || !authentication.isAuthenticated() || authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_MEMBER"))) {
        	return List.of();
        }
    	
    	MemCustomUserDetails memUser = (MemCustomUserDetails) authentication.getPrincipal();
        Integer memId = memUser.getMemId();
        List<CartVO> cartList = cartSvc.getCart(memId);
        System.out.println("會員ID" + memId);
        return cartList;
    }
    
    @PostMapping("/updateQty")
    @ResponseBody
    public String updateQty(@RequestBody CartVO cartVO, Authentication authentication){
    	if (authentication == null || !authentication.isAuthenticated() ||
    		    authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_MEMBER"))) {
    		    return "請先登入會員";
    		}
    	
    	MemCustomUserDetails memUser = (MemCustomUserDetails) authentication.getPrincipal();
    	cartVO.setMemId(memUser.getMemId());
    	cartSvc.updateQty(cartVO);
		return "商品數量已變更";
    }
    

    @DeleteMapping("/removeFromCart")
    @ResponseBody
    public String removeFromCart(@RequestBody CartVO cartVO, Authentication authentication){
    	if (authentication == null || !authentication.isAuthenticated() ||
    		    authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_MEMBER"))) {
    		    return "請先登入會員";
    		}
    	
    	MemCustomUserDetails memUser = (MemCustomUserDetails) authentication.getPrincipal();
    	cartVO.setMemId(memUser.getMemId());
        cartSvc.removeFromCart(cartVO);
        return "商品已移除";
    }

    @DeleteMapping("/clearCart")
    @ResponseBody
    public String clearCart(Authentication authentication){
    	if (authentication == null || !authentication.isAuthenticated() ||
    		    authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_MEMBER"))) {
    		    return "請先登入會員";
    		}
    	
    	MemCustomUserDetails memUser = (MemCustomUserDetails) authentication.getPrincipal();
        cartSvc.clearCart(memUser.getMemId());
        return "購物車已清空";
    }
    
//==================================checkout===========================
//    點擊結帳按鈕跳轉至結帳確認頁面
    @GetMapping("/checkout")
	public String checkoutPage() {
		return "frontend/cart/checkout";
	}
//    結帳商品確認清單顯示
    @GetMapping("/checkoutList")
	@ResponseBody
	public Map<Integer, Map<String, Object>> getCheckoutList(Authentication authentication){
	    MemCustomUserDetails memUser = (MemCustomUserDetails) authentication.getPrincipal();
    	return cartSvc.getGroupSeller(memUser.getMemId());
	    
	}
//	點擊確認結帳按鈕
    @PostMapping("/ecpayCheckout")
    @ResponseBody
	public String ecpayCheckout(@RequestBody Map<String, String> payload, HttpSession session, Authentication authentication) {
    	try {
    	MemCustomUserDetails memUser = (MemCustomUserDetails) authentication.getPrincipal();
    	Integer memId = memUser.getMemId();
    	if(memId == null) {
    		return "請先登入會員才能結帳";
    	}
    	
    	String itemName = payload.get("itemName");
        String totalAmount = payload.get("totalAmount");
        Integer sellerId = Integer.valueOf(payload.get("sellerId"));

        return cartSvc.ecpayCheckout(memId, sellerId,itemName, totalAmount);
    	}catch(Exception e){
    		e.printStackTrace();
    		return "系統錯誤，請稍後再試";
    	}
	}
	
//   綠界付款結果回傳
    @PostMapping("/ecpayReturn")
    public String ecpayReturn(HttpServletRequest req) {
    	return cartSvc.ecpayReturn(req);
    }

	
}
