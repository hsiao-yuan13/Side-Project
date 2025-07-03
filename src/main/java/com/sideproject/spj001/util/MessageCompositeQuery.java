package com.sideproject.spj001.util;

import com.sideproject.spj001.entity.MemVO;
import com.sideproject.spj001.entity.MessageVO;
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

public class MessageCompositeQuery {
    public static Predicate get_predicate_for_message(CriteriaBuilder builder, Root<MessageVO> root, String columnName, String value){
        Predicate predicate = null;

        if("msgNo".equals(columnName)){
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        }else if("msgName".equals(columnName) || "msgStatus".equals(columnName)){
            predicate = builder.like(root.get(columnName), "%" +  value + "%");
        }else if("msgTime".equals(columnName)){
            predicate = builder.equal(root.get(columnName), java.sql.Date.valueOf(value));
        }else if("memId".equals(columnName)){
            MemVO memVO = new MemVO();
            memVO.setMemId(Integer.valueOf(value));
            predicate = builder.equal(root.get("memVO"), memVO);
        }

        return predicate;
    }

    public static List<MessageVO> getAllc(Integer sellerId, Map<String, String[]> map, Session session){
        Transaction tx = session.beginTransaction();
        List<MessageVO> list = null;
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<MessageVO> criteriaQuery = builder.createQuery(MessageVO.class);
            Root<MessageVO> root = criteriaQuery.from(MessageVO.class);

            List<Predicate> predicateList = new ArrayList<>();

            predicateList.add(builder.equal(root.get("sellerId"), sellerId));

            Set<String> keys = map.keySet();
            int count = 0;
            for(String key : keys) {
                String value = map.get(key)[0];
                if (value != null && value.trim().length() != 0 && !"action".equals(key)) {
                    count++;
                    predicateList.add(get_predicate_for_message(builder, root, key, value.trim()));
                    System.out.println("有送出查詢資料的藍位數count = " + count);
                }
            }
            System.out.println("predicateList.size()="+predicateList.size());
            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            criteriaQuery.orderBy(builder.asc(root.get("magNo")));
            Query query = session.createQuery(criteriaQuery);
            list = query.getResultList();

            tx.commit();
        }catch (RuntimeException ex) {
            if (tx != null) {
                tx.rollback();
            } else {
                throw ex;
            }
        }finally{
            session.close();
        }

        return list;
    }
}
