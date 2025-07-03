package com.sideproject.spj001.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sideproject.spj001.dao.ProductRepository;
import com.sideproject.spj001.entity.ProductVO;
import com.sideproject.spj001.util.ProductCompositeQuery;

@Service("productService")
public class ProductService {
    @Autowired
    ProductRepository repository;

    @Autowired
    private SessionFactory sessionFactory;

    public void addProduct(ProductVO productVO){
        repository.save(productVO);
    }

    public void updateProduct(ProductVO productVO) {

        repository.save(productVO);
    }

    public ProductVO getOneProduct(Integer productId){
        Optional<ProductVO> optional = repository.findById(productId);
        return optional.orElse(null);
    }

    public List<ProductVO> getAll(){
        return repository.findAll();
    }

    public List<ProductVO> showAll(String productStatus, Map<String, String[]> map){
        return ProductCompositeQuery.getShopAllc(productStatus, map, sessionFactory.openSession());
    }

    public List<ProductVO> getAll(Integer sellerId, Map<String, String[]> map){
        return ProductCompositeQuery.getSellerAllc(sellerId, map, sessionFactory.openSession());
    }

//    cart取得productPrice
    public Integer getProductPrice(Integer productId){
        return repository.findById(productId).map(ProductVO::getProductPrice).orElse(0);
    }
    
//    取得一組productIds的商品資料
    public Map<Integer, ProductVO> getProductsByIds(Set<Integer> productIds){
    	List<ProductVO> products = repository.findAllById(productIds);
    	return products.stream().collect(Collectors.toMap(ProductVO::getProductId, p -> p ));
    }
}
