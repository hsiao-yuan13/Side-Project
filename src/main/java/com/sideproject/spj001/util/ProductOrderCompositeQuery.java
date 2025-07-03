package com.sideproject.spj001.util;

import com.sideproject.spj001.entity.*;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProductOrderCompositeQuery {
    public static Predicate get_predicate_for_merchOrder(CriteriaBuilder builder, Root<ProductOrderVO> root, String columnName, String value){
        Predicate predicate = null;

        if("merchNo".equals(columnName)){
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        }else if("pickupOption".equals(columnName) || "paymentType".equals(columnName) || "receiptStatus".equals(columnName)){
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        }else if("orderDate".equals(columnName)){
            predicate = builder.equal(root.get(columnName), java.sql.Date.valueOf(value));
        }else if("memId".equals(columnName) || "sellerId".equals(columnName)){
            MemVO memVO = new MemVO();
            memVO.setMemId(Integer.valueOf(value));
            predicate = builder.equal(root.get("memVO"), memVO);


            SellerVO sellerVO = new SellerVO();
            sellerVO.setSellerId(Integer.valueOf(value));
            predicate = builder.equal(root.get("sellerVO"), sellerVO);
        }

        return predicate;
    }
    @SuppressWarnings("unchecked")
    public static List<ProductOrderVO> getAllc(Integer sellerId, Map<String, String[]> map, Session session){
        Transaction tx = session.beginTransaction();
        List<ProductOrderVO> list = null;
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<ProductOrderVO> criteriaQuery = builder.createQuery(ProductOrderVO.class);
            Root<ProductOrderVO> root = criteriaQuery.from(ProductOrderVO.class);

            List<Predicate> predicateList = new ArrayList<>();

//            只顯示特定sellerId的order
            predicateList.add(builder.equal(root.get("sellerId"), sellerId));

            Set<String> keys = map.keySet();
            int count = 0;
            for(String key : keys){
                String value = map.get(key)[0];
                if(value != null && value.trim().length() != 0 && !"action".equals(key)){
                    count++;
                    predicateList.add(get_predicate_for_merchOrder(builder, root, key, value.trim()));
                    System.out.println("有送出查詢資料的欄位數count = " + count);
                }
            }
            System.out.println("predicateList.size()="+predicateList.size());
            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            criteriaQuery.orderBy(builder.asc(root.get("merchNo")));
            Query query = session.createQuery(criteriaQuery);
            list = query.getResultList();

            tx.commit();
        }catch (RuntimeException ex){
            if(tx != null){
                tx.rollback();
            }else{
                throw ex;
            }
        }finally {
            session.close();
        }

        return list;
    }
}
