package com.sideproject.spj001.controller;

import com.sideproject.spj001.entity.*;
import com.sideproject.spj001.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class MessageController {
    @Autowired
    MessageService messageSvc;
//============================frontend/mem===========================
//    我的通知
    @PostMapping("/mem/myMessage")
    public String memMessage(HttpSession session, Model model){
        Integer memId = (Integer)session.getAttribute("memId");
        if(memId == null){
            return "redirect:/login";
        }

        List<MessageVO> list = messageSvc.showAll(memId);

        model.addAttribute("messageListData", list);
        return "frontend/mem/myMessage";
    }
//============================frontend/seller===========================
//    發送通知
    @GetMapping("/seller/addMessage")
    public String addMessage(Model model){
        MessageVO messageVO = new MessageVO();
        model.addAttribute("messageVO", messageVO);
        return "frontend/seller/addMessage";
    }

    @PostMapping("/seller/messageManage")
    public String insertMessage(@Valid MessageVO messageVO, BindingResult result, Model model){
        messageSvc.addMessage(messageVO);
        List<MessageVO> list = messageSvc.getAll();
        model.addAttribute("messageListData", list);
        model.addAttribute("success", "通知發送成功");
        return "redirect:/seller/messageManage";
    }
//    查詢通知
    @PostMapping("/seller/MessageManage")
    public String ListAllMessage(HttpSession session, HttpServletRequest req, Model model){
        Integer sellerId = (Integer)session.getAttribute("sellerId");
        if(sellerId == null){
            return "redirect:/login";
        }

        Map<String, String[]> map = req.getParameterMap();
        List<MessageVO> list = messageSvc.getAll(sellerId, map);

        model.addAttribute("messageListDate", list);
        return "frontend/sell/messageManage";
    }
//    刪除通知(不顯示)
    @PostMapping("/seller/listOneMessage")
    public String getOneMessage(@RequestParam("msgNo") Integer msgNo, Model model) throws IOException{
        MessageVO messageVO = messageSvc.getOneMessage(Integer.valueOf(msgNo));

        model.addAttribute("messageVO", messageVO);
        return "frontend/seller/listOneMessage";
    }

    public String deleteMessage(@RequestParam("msgNo") Integer msgNo, RedirectAttributes redirectAttributes) throws IOException{
        messageSvc.updateMessageStatus(msgNo, "不顯示");

        redirectAttributes.addFlashAttribute("success", "刪除訊息成功");
        return "redirect:/seller/messageManage";
    }
}
