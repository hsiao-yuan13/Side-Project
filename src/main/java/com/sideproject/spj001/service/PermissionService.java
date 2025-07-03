package com.sideproject.spj001.service;

import com.sideproject.spj001.dao.PermissionRepository;
import com.sideproject.spj001.entity.PermissionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("permissionService")
public class PermissionService {
    @Autowired
    PermissionRepository repository;

    public void addPermission(PermissionVO permissionVO){
        repository.save(permissionVO);
    }

    public void updatePermmsion(PermissionVO permissionVO){
        repository.save(permissionVO);
    }

    public List<PermissionVO> getAll(){
        return repository.findAll();
    }

//    public boolean hasAccess(Integer jobId, String reqURI){
//        List<PermissionVO> list = repository.findByJobId(jobId);
//        return list.stream().anyMatch(p -> reqURI.startsWith(p.getFunc().getFuncPath()));
//    }
}
