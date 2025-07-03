package com.sideproject.spj001.controller;

import com.sideproject.spj001.entity.EmpVO;
import com.sideproject.spj001.service.EmpService;
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
@RequestMapping("/backend/Emp")
public class EmpController {
    @Autowired
    EmpService empSvc;

//    新增員工
    @GetMapping("/addEmp")
    public String addEmp(Model model){
        EmpVO empVO = new EmpVO();
        model.addAttribute("empVO", empVO);
        return "backend/emp/addEmp";
    }

    @PostMapping("/insertEmp")
    public String insertEmp(@Valid EmpVO empVO, BindingResult result, Model model){
        empSvc.addEmp(empVO);
        List<EmpVO> list = empSvc.getAll();
        model.addAttribute("empListData", list);
        model.addAttribute("success", "員工新增成功");
        return "redirect:/EmpManage";
    }

//    更新員工資料
    @PostMapping("getOne_for_update")
    public String getOneEmp(@RequestParam("empId") Integer empId, Model model) throws IOException {
        EmpVO empVO = empSvc.getOneEmp(empId);

        model.addAttribute("empVO", empVO);
        return "backend/emp/updateEmp";
    }

    @PostMapping("/updateEmp")
    public String updateEmp(@Valid EmpVO empVO, BindingResult result, Model model) throws IOException{
        empSvc.updateEmp(empVO);

        model.addAttribute("success", "修改成功");
        empVO = empSvc.getOneEmp(empVO.getEmpId());
        model.addAttribute("empVO", empVO);
        return "backend/emp/empManage";
    }
//    查詢員工
    @PostMapping
    public String listAllEmp(HttpServletRequest req, Model model){
        Map<String, String[]> map = req.getParameterMap();
        List<EmpVO> list = empSvc.getAll(map);

        model.addAttribute("empListData", list);
        return "backend/emp/empManage";
    }


}
