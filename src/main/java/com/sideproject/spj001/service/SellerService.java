package com.sideproject.spj001.service;

import com.sideproject.spj001.dao.SellerRepository;
import com.sideproject.spj001.entity.*;
import com.sideproject.spj001.util.SellerCompositeQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("sellerService")
public class SellerService {
    @Autowired
    SellerRepository repository;

    @Autowired
    private SessionFactory sessionFactory;

    public void addSeller(SellerVO sellerVO){
        repository.save(sellerVO);
    }

    public void updateSeller(SellerVO sellerVO){
        repository.save(sellerVO);
    }

//    public void deleteSeller(Integer sellerId){
//        if(repository.existsById(sellerId)){
//            repository.deleteBySellerId(sellerId);
//        }
//    }

    public SellerVO getOneSeller(Integer sellerId){
        Optional<SellerVO> optional = repository.findById(sellerId);
        return optional.orElse(null);
    }

//    public SellerVO getOneSeller(String sellerMobile){
//        Optional<SellerVO> optional = repository.findByMobile(sellerMobile);
//        return optional.orElse(null);
//    }

    public List<SellerVO> getAll(){
        return repository.findAll();
    }

    public List<SellerVO> getAll(Map<String, String[]> map) {
        return SellerCompositeQuery.getAllc(map, sessionFactory.openSession());
    }
    
    
    
    public SellerVO login(String sellerAccount, String sellerPassword) {
    	SellerVO sellerVO = repository.findBySellerAccount(sellerAccount);
    	if(sellerVO != null && sellerPassword.equals(sellerVO.getSellerPassword())) {
    		return sellerVO;
    	}
    	return null;
    }
}
