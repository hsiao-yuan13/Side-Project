package com.sideproject.spj001.util;

import com.sideproject.spj001.entity.EmpVO;
import com.sideproject.spj001.entity.JobVO;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Transaction;
import org.hibernate.Session;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EmpCompositeQuery {
    public static Predicate get_for_emp(CriteriaBuilder builder, Root<EmpVO> root, String columnName, String value){
        Predicate predicate = null;

        if("empId".equals(columnName)){
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        }else if("empName".equals(columnName) || "empEmail".equals(columnName) || "empStatus".equals(columnName)){
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        }else if("hiredate".equals(columnName)){
            predicate = builder.equal(root.get(columnName), java.sql.Date.valueOf(value));
        }else if("jobId".equals(columnName)){
            JobVO jobVO = new JobVO();
            jobVO.setJobId(Integer.valueOf(value));
            predicate = builder.equal(root.get("jobVO"), jobVO);
        }

        return predicate;
    }
    @SuppressWarnings("unchecked")
    public static List<EmpVO> getAllc(Map<String, String[]> map, Session session){
        Transaction tx = session.beginTransaction();
        List<EmpVO> list = null;
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<EmpVO> criteriaQuery = builder.createQuery(EmpVO.class);
            Root<EmpVO> root = criteriaQuery.from(EmpVO.class);

            List<Predicate> predicateList = new ArrayList<Predicate>();

            Set<String> keys = map.keySet();
            int count = 0;
            for(String key : keys){
                String value = map.get(key)[0];
                if(value != null && value.trim().length() != 0 && !"action".equals(key)){
                    count++;
                    predicateList.add(get_for_emp(builder, root, key, value.trim()));
                    System.out.println("有送出查詢資料的欄位數count = " + count);
                }
            }
            System.out.println("predicateList.size()="+predicateList.size());
            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            criteriaQuery.orderBy(builder.asc(root.get("empId")));
            Query query = session.createQuery(criteriaQuery);
            list = query.getResultList();

            tx.commit();;
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
