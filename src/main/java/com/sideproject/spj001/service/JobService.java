package com.sideproject.spj001.service;

import com.sideproject.spj001.dao.JobRepository;
import com.sideproject.spj001.entity.JobVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("jobService")
public class JobService {
    @Autowired
    JobRepository repository;

    public void addJob(JobVO jobVO){
        repository.save(jobVO);
    }

    public void update(JobVO jobVO){
        repository.save(jobVO);
    }

    public List<JobVO> getAll(){
        return repository.findAll();
    }
}