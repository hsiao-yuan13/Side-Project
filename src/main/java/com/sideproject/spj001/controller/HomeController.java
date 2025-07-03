package com.sideproject.spj001.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/fanWorld")
    public String home() {
        return "frontend/index"; 
    }
	
	@GetMapping("/frontend/index")
	public String index() {
		return "frontend/index";
	}
}
