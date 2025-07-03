package com.sideproject.spj001.controller;

import com.sideproject.spj001.entity.MemVO;
import com.sideproject.spj001.service.MemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/frontend/mem")
public class MemLoginController {
    @Autowired
    private MemService memSvc;

    @GetMapping("/memLoginPage")
    public String showMemLoginPage(){
        return "/frontend/mem/memLoginPage";
    }

    @PostMapping("/loginMem")
    public String loginMem(@RequestParam("memAccount") String memAccount, @RequestParam("memPassword") String memPassword, HttpSession session, Model model){
//        驗證帳密
        MemVO memVO = memSvc.login(memAccount, memPassword);

        if(memVO == null){
//        	登入失敗
            model.addAttribute("errorMsg", "會員帳號密碼錯誤");
            return "/frontend/mem/memLoginPage";
        }
//        登入成功(將memId存入session)
        session.setAttribute("loginMem", memVO);
        session.setAttribute("memId", memVO.getMemId());
//        System.out.println("memId 存入 session: " + session.getAttribute("memId"));

        String redirectURL = (String)session.getAttribute("redirectAfterLogin");
        if(redirectURL != null) {
        	session.removeAttribute("redirectAfterLogin");
        	return "redirect:" + redirectURL;
        }
        
        return "redirect:/frontend/index";
    }

//    會員登出
    @PostMapping("/logoutMem")
    public String logout(HttpSession session){
//        清除session
        session.invalidate();
        return "redirect:/frontend/mem/memLoginPage";
    }
}