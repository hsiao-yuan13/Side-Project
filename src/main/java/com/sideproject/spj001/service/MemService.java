package com.sideproject.spj001.service;

import com.sideproject.spj001.dao.MemRepository;
import com.sideproject.spj001.entity.MemVO;
import com.sideproject.spj001.util.MemCompositeQuery;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("memService")
public class MemService {
    @Autowired
    MemRepository repository;

    @Autowired
    private SessionFactory sessionFactory;

    public void addMem(MemVO memVO){
        repository.save(memVO);
    }

    public void updateMem(MemVO memVO){
        repository.save(memVO);
    }

    public void deleteMem(Integer memId){
        if(repository.existsById(memId)){
            repository.deleteById(memId);
        }
    }

    public MemVO getOneMem(Integer memId){
        Optional<MemVO> optional = repository.findById(memId);
        return optional.orElse(null);
    }

    public List<MemVO> getAll(){
        return repository.findAll();
    }

    public List<MemVO> getAll(Map<String, String[]> map) {
        return MemCompositeQuery.getAllc(map, sessionFactory.openSession());
    }


    public MemVO login(String memAccount, String memPassword) {
        MemVO memVO = repository.findByMemAccount(memAccount);
        if (memVO != null && memPassword.equals(memVO.getMemPassword())) {
            return memVO; // 登入成功
        }
        return null; // 登入失敗
    }
}
