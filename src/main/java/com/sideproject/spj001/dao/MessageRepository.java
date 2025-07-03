package com.sideproject.spj001.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sideproject.spj001.entity.MessageVO;

public interface MessageRepository extends JpaRepository<MessageVO, Integer>{

    List<MessageVO> findByMem_MemId(Integer memId);

}
