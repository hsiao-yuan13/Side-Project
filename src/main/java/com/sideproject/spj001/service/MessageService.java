package com.sideproject.spj001.service;

import com.sideproject.spj001.dao.MessageRepository;
import com.sideproject.spj001.entity.*;
import com.sideproject.spj001.util.MessageCompositeQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("messageService")
public class MessageService {
    @Autowired
    MessageRepository repository;

    @Autowired
    SessionFactory sessionFactory;

    public void addMessage(MessageVO messageVO){
        repository.save(messageVO);
    }

    public MessageVO getOneMessage(Integer msgNo){
        Optional<MessageVO> optional = repository.findById(msgNo);
        return optional.orElse(null);
    }
    public void updateMessageStatus(Integer msgNo, String msgStatus){
        MessageVO messageVO = getOneMessage(msgNo);

        if(msgNo != null){
            messageVO.setMsgStatus(msgStatus);
            repository.save(messageVO);
        }else {
            System.out.println("訊息找不到，無法刪除");
        }
    }

    public List<MessageVO> getAll(){
        return repository.findAll();
    }

    public List<MessageVO> getAll(Integer sellerId, Map<String, String[]> map){
        return MessageCompositeQuery.getAllc(sellerId, map, sessionFactory.openSession());
    }

    public List<MessageVO> showAll(Integer memId){
        return repository.findByMem_MemId(memId);
    }
}
