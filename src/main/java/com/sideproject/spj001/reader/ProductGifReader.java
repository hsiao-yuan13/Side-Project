package com.sideproject.spj001.reader;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sideproject.spj001.service.ProductService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ProductGifReader extends HttpServlet{
	
	@Autowired
	ProductService productSvc;
	
	@GetMapping("/reader/ProductGifReader")
	public void productGifReader(@RequestParam("productId") Integer productId, HttpServletRequest req, HttpServletResponse res)throws IOException {
		res.setContentType("image/gif");
		ServletOutputStream out = res.getOutputStream();
		
		try {
			out.write(productSvc.getOneProduct(productId).getProductPic());
		}catch(Exception e) {
			InputStream in = getServletContext().getResourceAsStream("resources/NoData/nopic.jpg");
			byte[] buf = new byte[in.available()];
			in.read(buf);
			out.write(buf);
			in.close();
		}
	}

}
