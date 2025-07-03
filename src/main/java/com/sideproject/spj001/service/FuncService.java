package com.sideproject.spj001.service;

import com.sideproject.spj001.dao.FuncRepository;
import com.sideproject.spj001.entity.FuncVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("funcService")
public class FuncService {
    @Autowired
    FuncRepository repository;

    public void addFunc(FuncVO funcVO){
        repository.save(funcVO);
    }

    public void updateFunc(FuncVO funcVO){
        repository.save(funcVO);
    }

    public List<FuncVO> getAll(){
        return repository.findAll();
    }
}
