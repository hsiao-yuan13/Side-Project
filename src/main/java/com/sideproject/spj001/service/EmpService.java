package com.sideproject.spj001.service;

import com.sideproject.spj001.dao.EmpRepository;
import com.sideproject.spj001.entity.EmpVO;
import com.sideproject.spj001.util.EmpCompositeQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("empService")
public class EmpService {
    @Autowired
    EmpRepository repository;

    @Autowired
    SessionFactory sessionFactory;

    public void addEmp(EmpVO empVO){
        repository.save(empVO);
    }
    public void updateEmp(EmpVO empVO){
        repository.save(empVO);
    }

    public EmpVO getOneEmp(Integer empId){
        Optional<EmpVO> optional = repository.findById(empId);
        return optional.orElse(null);
    }

    public List<EmpVO> getAll(){
        return repository.findAll();
    }

    public List<EmpVO> getAll(Map<String, String[]> map){
        return EmpCompositeQuery.getAllc(map, sessionFactory.openSession());
    }
}
