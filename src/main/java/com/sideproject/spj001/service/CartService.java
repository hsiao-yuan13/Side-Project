package com.sideproject.spj001.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.sideproject.spj001.entity.CartVO;
import com.sideproject.spj001.entity.ProductOrderVO;
import com.sideproject.spj001.entity.ProductVO;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class CartService {
	 @Autowired
	 private RedisTemplate<String, Object> redisTemplate;
	 
	 @Autowired
	 private ProductService productSvc;
	    
	 @Autowired
	 private ProductOrderService productOrderSvc;

//	    商品加入購物車
	public void addToCart(CartVO cartVO) {
	    String cartKey = cartVO.getRedisKey();
	    
//	    使用RedisTemplatet操作Hash，HashOperations<key, hashKey, hashValue>
	    HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();
	    
//	    如果購物車已存在該商品
	    Boolean hasCartItem = redisTemplate.hasKey(cartKey);
	    if(Boolean.TRUE.equals(hasCartItem)) {
//	    	增加其數量
	    	Integer currentQty = (Integer) hashOps.get(cartKey, "productQty");
	    	Integer newQty = currentQty + cartVO.getProductQty();
	    	hashOps.put(cartKey, "productQty", newQty);
	    }else {
//	    	若不存在則新增該商品
	        hashOps.put(cartKey, "productName", cartVO.getProductName());
	        hashOps.put(cartKey, "productPrice", cartVO.getProductPrice());
	        hashOps.put(cartKey, "productQty", cartVO.getProductQty());
	        hashOps.put(cartKey, "sellerId", cartVO.getSellerId());
	    }
	    redisTemplate.expire(cartKey, Duration.ofDays(90));
	}


//	    取得購物車
	public List<CartVO> getCart(Integer memId) {
//		將cart所有商品集合成一個List
		List<CartVO> cartList = new ArrayList<>();
//		將cart所有商品的所有cartKey集合成一個Set，作為取得所有商品資料的判斷條件
		Set<String> cartKeys = redisTemplate.keys("cart:" + memId + ":*");
		
//		遍歷Set<carKeys>5中所有cartKey
		for(String cartKey : cartKeys) {
			HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();
//			取得商品資料，將其轉換成CartVO物件
			Integer productId = Integer.valueOf(cartKey.split(":")[2]);  // 解析 cartKey 來取得 productId		
			String productName = (String)hashOps.get(cartKey, "productName");
			Integer productPrice = (Integer)hashOps.get(cartKey, "productPrice");
			Integer productQty = (Integer)hashOps.get(cartKey, "productQty");
			Integer sellerId = (Integer)hashOps.get(cartKey, "sellerId");
//			創建CartVO物件，設置其value
			CartVO cartVO = new CartVO();
			cartVO.setMemId(memId);
			cartVO.setProductId(productId);
			cartVO.setProductName(productName);
			cartVO.setProductPrice(productPrice);
			cartVO.setProductQty(productQty);
			cartVO.setSellerId(sellerId);
//			將CartVO加入cart
			cartList.add(cartVO);
		}
		return cartList;
	}
	    
	    
	//變更商品數量
	public void updateQty(CartVO cartVO){
	  String cartKey = cartVO.getRedisKey();
	  HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

	  Integer newQty = cartVO.getProductQty();
	//  System.out.println(newQty);
	  if(newQty <= 0){
//	    若數量<0則移除商品
		  redisTemplate.delete(cartKey);
		}else{
		    hashOps.put(cartKey, "productQty", newQty);
		    redisTemplate.expire(cartKey, Duration.ofDays(90));
		}

	}



	    
//	    移除商品
	    public void removeFromCart(CartVO cartVO){
	        String cartKey = cartVO.getRedisKey();
	        
	        if(redisTemplate.hasKey(cartKey)){
	        	redisTemplate.delete(cartKey);
	        }
	    }

//	    清空購物車
	    public void clearCart(Integer memId){
	        Set<String>cartKeys = redisTemplate.keys("cart:" + memId + ":*");
	        
	        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

	        
	        for(String cartKey : cartKeys) {
	        	hashOps.delete(cartKey);
	        }
	    }
	    
//==================================checkout===========================

//顯示商家分組清單
	    public Map<Integer, Map<String, Object>> getGroupSeller(Integer memId){
	    	List<CartVO> checkoutList = getCart(memId);
		    
//	    	收集所有productIds
	    	Set<Integer> productIds = checkoutList.stream().map(CartVO::getProductId).collect(Collectors.toSet());
	    	
//	    	查出所有productIds的資料
	    	Map<Integer, ProductVO> productMap = productSvc.getProductsByIds(productIds);
	    	
//	    	分組
	    	Map<Integer, Map<String, Object>> groupMap = new LinkedHashMap<>();
		    
		    for(CartVO item : checkoutList) {
		    	ProductVO productVO = productMap.get(item.getProductId());
//		    	安全檢查
		    	if(productVO == null) {
		    		continue;
		    	}
		    	Integer sellerId = item.getSellerId();
		    	String sellerName = productVO.getSeller().getSellerName();
		    	
		    	groupMap.putIfAbsent(sellerId, new HashMap<>());
		    	Map<String, Object> sellerData = groupMap.get(sellerId);
		    	
		    	sellerData.putIfAbsent("sellerName", sellerName);
		    	sellerData.putIfAbsent("items", new ArrayList<CartVO>());
		    	
		    	List<CartVO> sellerItems = (List<CartVO>)sellerData.get("items");
		    	sellerItems.add(item);
		    	
		    }
		    return groupMap;
	    }
	    
	    
//	點擊確認結帳   
	    public String ecpayCheckout(Integer memId, Integer sellerId, String itemName, String totalAmountStr) {
//	    	取得該會員購物車
	    	List<CartVO> cartList = getCart(memId);
	    	
//	    	取得所選商家購買清單
	    	List<CartVO> selectedCartList = cartList.stream().filter(cart -> cart.getSellerId().equals(sellerId)).collect(Collectors.toList());
	    		    	
	    	if(selectedCartList.isEmpty()) {
	    		throw new IllegalArgumentException("選擇的該商家購買清單錯誤");
	    	}
	    	
//	    	建立訂單
	    	ProductOrderVO productOrderVO = productOrderSvc.addOrder(memId, selectedCartList);
	    	
//			進入綠界
	    	AllInOne all = new AllInOne("test");
	    	
	    		AioCheckOutALL obj = new AioCheckOutALL();
		    	obj.setMerchantTradeNo(productOrderVO.getMerchantTradeNo());
		    	obj.setMerchantTradeDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
		    	obj.setTotalAmount(totalAmountStr);
		    	obj.setTradeDesc("商城商品結帳");
		    	obj.setItemName(itemName);
		    	obj.setReturnURL("http://localhost:8080/frontend/cart/ecpayReturn");
		    	obj.setClientBackURL("http://localhost:8080/frontend/shop/shop");
		    	obj.setNeedExtraPaidInfo("N");
		    	
		    	String ecpayForm = all.aioCheckOut(obj, null);
	    	
	    	
	    	return ecpayForm;
	    }
	    
	    
	//  綠界付款結果回傳
		  public String ecpayReturn(HttpServletRequest req) {
		  	String rtnCode = req.getParameter("RtnCode");
		  	String merchantTradeNo = req.getParameter("MerchantTradeNo");
		  	String tradeNo = req.getParameter("TradeNo");
		  	String paymentType = req.getParameter("PaymentType");
		  	
		  	
		  	if("1".equals(rtnCode)) {
		  		ProductOrderVO productOrderVO = productOrderSvc.findByMerchantTradeNo(merchantTradeNo);
		  		
		  		if(productOrderVO != null) {
		  			productOrderVO.setPaymentStatus("已付款");
		  			productOrderVO.setOrderStatus("未出貨");
		  			productOrderVO.setTradeNo(tradeNo);
		  			productOrderVO.setPayment(paymentType);
		  			productOrderSvc.update(productOrderVO);
		  			
		  			clearCheckedOutItems(productOrderVO.getMem().getMemId(), productOrderVO.getSeller().getSellerId());
		  		}
		  		return "1|OK";
		  	}else {
		  		return "0|FAILURE";
		  	}
		  }
		  
//		  結帳後清除以結帳商品
		  public void clearCheckedOutItems(Integer memId, Integer sellerId) {
			  Set<String> cartKeys = redisTemplate.keys("cart:" + memId + ":*");
			  
			  for(String cartKey : cartKeys) {
				  HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();
				  Integer itemSellerId = (Integer) hashOps.get(cartKey, "sellerId");
			  
			  
				  if(itemSellerId != null && itemSellerId.equals(sellerId)) {
					  redisTemplate.delete(cartKey);
				  }
				  
			  }
		  }
}






 