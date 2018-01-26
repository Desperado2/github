package com.jack.test.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jack.anno.Autowired;
import com.jack.anno.Controller;
import com.jack.anno.RequestMapping;
import com.jack.test.service.CustomerDao;
import com.jack.view.ModelAndView;

@Controller("customerController")
public class CustomerController {

	@Autowired
	private CustomerDao customerServiceI;
	
	@RequestMapping("/sayHello")
	public void sayHello(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		request.setAttribute("name", "ÕÅÈý");
		//return new ModelAndView("test/welcome.jsp");
		try {
			response.sendRedirect("/MiniMvc/test/welcome.jsp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
