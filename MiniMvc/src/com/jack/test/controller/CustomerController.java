package com.jack.test.controller;


import com.jack.anno.Autowired;
import com.jack.anno.Controller;
import com.jack.anno.RequestMapping;
import com.jack.test.service.CustomerDao;

@Controller("customerController")
public class CustomerController {

	@Autowired
	private CustomerDao customerServiceI;
	
	@RequestMapping("/sayHello")
	public void sayHello(){
		customerServiceI.sayHello();
	}
}
