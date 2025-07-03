package com.sideproject.spj001.util;

import com.sideproject.spj001.entity.SellerVO;
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

public class   SellerCompositeQuery {
    public static Predicate get_predicate_for_seller(CriteriaBuilder builder, Root<SellerVO> root, String columnName, String value){
        Predicate predicate = null;

        if("sellerId".equals(columnName)){
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        }else if("sellerAccount".equals(columnName) || "sellerName".equals(columnName) || "sellerEmail".equals(columnName) || "sellerMobile".equals(columnName) || "sellerStatus".equals(columnName)){
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        }

        return predicate;
    }
    @SuppressWarnings("unchecked")
    public static List<SellerVO> getAllc(Map<String, String[]> map, Session session){
        Transaction tx = session.beginTransaction();
        List<SellerVO> list = null;
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<SellerVO> criteriaQuery = builder.createQuery(SellerVO.class);
            Root<SellerVO> root = criteriaQuery.from(SellerVO.class);

            List<Predicate> predicateList = new ArrayList<Predicate>();

            Set<String> keys = map.keySet();
            int count = 0;
            for(String key : keys){
                String value = map.get(key)[0];
                if(value != null && value.trim().length() != 0 && !"action".equals(key)){
                    count++;
                    predicateList.add(get_predicate_for_seller(builder, root, key, value.trim()));
                    System.out.println("有送出查詢資料的欄位數count = " + count);
                }
            }
            System.out.println("predicateList.size()="+predicateList.size());
            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            criteriaQuery.orderBy(builder.asc(root.get("sellerId")));
            Query query = session.createQuery(criteriaQuery);
            list = query.getResultList();

            tx.commit();
        }catch(RuntimeException ex){
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
