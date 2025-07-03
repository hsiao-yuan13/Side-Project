package com.sideproject.spj001.controller;

import com.sideproject.spj001.entity.MemVO;
import com.sideproject.spj001.service.MemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class MemController {
    @Autowired
    MemService memSvc;
//============================frontend===========================
//    會員註冊
    @GetMapping("/frontend/mem/memRegister")
    public String addMem(Model model){
        MemVO memVO = new MemVO();
        model.addAttribute("memVO", memVO);
        return "frontend/mem/memRegister";
    }

    @PostMapping("/frontend/mem/insertMem")
    public String insertMem(@Valid MemVO memVO, BindingResult result, Model model){
        memSvc.addMem(memVO);
        List<MemVO> list = memSvc.getAll();
        model.addAttribute("memListData", list);
        model.addAttribute("success", "會員新增成功");
        return "redirect:/mem/login";
    }

//    更新會員資料
    @PostMapping("frontend/mem/getOne_for_update")
    public String getOneMem(@RequestParam("memId") Integer memId, Model model) throws IOException {
        MemVO memVO = memSvc.getOneMem(memId);

        model.addAttribute("memVO", memVO);
        return "frontendend/mem/updateMem";
    }

    @PostMapping("frontend/mem/updateMem")
    public String updateMem(@Valid MemVO memVO, BindingResult result, Model model) throws IOException{
        memSvc.updateMem(memVO);

        model.addAttribute("success", "修改成功");
        memVO = memSvc.getOneMem(memVO.getMemId());
        model.addAttribute("memVO", memVO);
        return "frontend/mem/myMem";
    }

    //============================backend===========================
//    會員查詢
    @PostMapping("backend/mem/listMemCompositeQuery")
    public String listAllMem(HttpServletRequest req, Model model){
        Map<String, String[]> map = req.getParameterMap();
        List<MemVO> list = memSvc.getAll(map);

        model.addAttribute("memListData", list);
        return "backend/mem/listAllMem";
    }
}
