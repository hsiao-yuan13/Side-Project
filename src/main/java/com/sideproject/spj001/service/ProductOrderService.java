package com.sideproject.spj001.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sideproject.spj001.dao.ProductOrderRepository;
import com.sideproject.spj001.entity.CartVO;
import com.sideproject.spj001.entity.OrderItemVO;
import com.sideproject.spj001.entity.ProductOrderVO;
import com.sideproject.spj001.util.ProductOrderCompositeQuery;

@Service("orderService")
public class ProductOrderService {
    @Autowired
    ProductOrderRepository repository;

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private ProductService productSvc;
    
    @Autowired
    private MemService memSvc;
    
    @Autowired
    private SellerService sellerSvc;

    public ProductOrderVO addOrder(Integer memId, List<CartVO> cartList){
//    	檢查購物車是否為空
	    	if(cartList == null || cartList.isEmpty()) {
	    		throw new IllegalArgumentException("購物車沒有資料");
	    	}
	    	
//	    只會有所選商家的商品
	    	Integer sellerId = cartList.get(0).getSellerId();
    	
	    	
	    		ProductOrderVO productOrderVO = new ProductOrderVO();
	    		productOrderVO.setMem(memSvc.getOneMem(memId));
	    		productOrderVO.setSeller(sellerSvc.getOneSeller(sellerId));
	    		productOrderVO.setOrderDate(LocalDateTime.now());
	    		productOrderVO.setOrderStatus("未出貨");
	    		productOrderVO.setPaymentStatus("待付款");
	    		
	    		int orderTotal = cartList.stream().mapToInt(cartItem -> cartItem.getProductPrice() * cartItem.getProductQty()).sum();
	    		productOrderVO.setTotal(orderTotal);
	
	    		String merchantTradeNo = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
	    		productOrderVO.setMerchantTradeNo(merchantTradeNo);
	    		
	    		Set<OrderItemVO> items = new HashSet<>();
	    		for(CartVO cartItem : cartList) {
	    			OrderItemVO item = new OrderItemVO();
	    			item.setProductOrder(productOrderVO);
	    			item.setProduct(productSvc.getOneProduct(cartItem.getProductId()));
	    			item.setProductQty(cartItem.getProductQty());
	    			item.setSubTotal(cartItem.getProductPrice() * cartItem.getProductQty());
	    			items.add(item);
	    		}
	    		productOrderVO.setItems(items);
	    		
	    		repository.save(productOrderVO);
	    	
    		return productOrderVO;
    			
    }

    public ProductOrderVO getOneOrder(Integer merchOrderNo){
        Optional<ProductOrderVO> optional = repository.findById(merchOrderNo);
        return optional.orElse(null);
    }
    
    public void update(ProductOrderVO productVO) {
        if (productVO != null && productVO.getOrderNo() != null) {
            repository.save(productVO);
        } else {
            throw new IllegalArgumentException("無效的訂單資料，無法更新");
        }
    }

    public void updateOrderStatus(Integer orderNo, String orderStatus){
        ProductOrderVO productOrderVO = getOneOrder(orderNo);

        if (productOrderVO != null) {
            productOrderVO.setOrderStatus(orderStatus);
            repository.save(productOrderVO);
        } else {
            System.out.println("訂單找不到，無法更新");
        }
    }

    public List<ProductOrderVO> getAll(){
        return repository.findAll();
    }

    public List<ProductOrderVO> showAll(Integer memId){
        return repository.findByMem_MemId(memId);
    }

    public List<ProductOrderVO> getAll(Integer sellerId, Map<String, String[]> map){
        return ProductOrderCompositeQuery.getAllc(sellerId, map, sessionFactory.openSession());
    }

	public ProductOrderVO findByMerchantTradeNo(String merchantTradeNo) {
		return repository.findByMerchantTradeNo(merchantTradeNo).orElse(null);
		
	}
}
