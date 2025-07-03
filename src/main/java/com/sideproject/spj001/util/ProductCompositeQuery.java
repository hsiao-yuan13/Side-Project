package com.sideproject.spj001.util;

import com.sideproject.spj001.entity.ProductVO;
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

public class ProductCompositeQuery {
    public static Predicate getPredicate_for_product(CriteriaBuilder builder, Root<ProductVO> root, String columnName, String value){
        Predicate predicate = null;

        if("productId".equals(columnName) || "stock".equals(columnName)){
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        }else if("productName".equals(columnName) || "productStatus".equals(columnName)){
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        }

        return predicate;
    }
    @SuppressWarnings("unchecked")
    public static List<ProductVO> getShopAllc(String productStatus, Map<String, String[]> map, Session session){
        Transaction tx = session.beginTransaction();
        List<ProductVO> list = null;
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<ProductVO> criteriaQuery = builder.createQuery(ProductVO.class);
            Root<ProductVO> root = criteriaQuery.from(ProductVO.class);

            List<Predicate> predicateList = new ArrayList<Predicate>();

//            顯示指定productStatus商品
            predicateList.add(builder.equal(root.get("productStatus"), productStatus));

            Set<String> keys = map.keySet();
            int count = 0;
            for(String key : keys){
                String value = map.get(key)[0];
                if(value != null && value.trim().length() != 0 && !"action".equals(key)){
                    count++;
                    predicateList.add(getPredicate_for_product(builder, root, key, value.trim()));
                    System.out.println("有送出查詢資料的欄位數count = " + count);
                }
            }
            System.out.println("predicateList.size()="+predicateList.size());
            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            criteriaQuery.orderBy(builder.asc(root.get("productId")));
            Query query = session.createQuery(criteriaQuery);
            list = query.getResultList();

            tx.commit();
        }catch (RuntimeException ex){
            if (tx != null){
                tx.rollback();
            }else {
                throw ex;
            }
        }finally {
            session.close();
        }

        return  list;
    }

    public static List<ProductVO> getSellerAllc(Integer sellerId, Map<String, String[]> map, Session session) {
        Transaction tx = session.beginTransaction();
        List<ProductVO> list = null;
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<ProductVO> criteriaQuery = builder.createQuery(ProductVO.class);
            Root<ProductVO> root = criteriaQuery.from(ProductVO.class);

            List<Predicate> predicateList = new ArrayList<Predicate>();

//            只顯示特定商家商品
            predicateList.add(builder.equal(root.get("sellerId"), sellerId));

            Set<String> keys = map.keySet();
            int count = 0;
            for (String key : keys) {
                String value = map.get(key)[0];
                if (value != null && value.trim().length() != 0 && !"action".equals(key)) {
                    count++;
                    predicateList.add(getPredicate_for_product(builder, root, key, value.trim()));
                    System.out.println("有送出查詢資料的欄位數count = " + count);
                }
            }
            System.out.println("predicateList.size()=" + predicateList.size());
            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            criteriaQuery.orderBy(builder.asc(root.get("productId")));
            Query query = session.createQuery(criteriaQuery);
            list = query.getResultList();

            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null) {
                tx.rollback();
            } else {
                throw ex;
            }
        } finally {
            session.close();
        }

        return list;
    }
    }
