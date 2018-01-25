package com.jack.controller;

import com.jack.Service.impl.CustomerDaoImpl;
import com.jack.anno.Autowired;
import com.jack.anno.Controller;
import com.jack.anno.RequestMapping;

@Controller("customerController")
public class CustomerController {

	@Autowired
	private CustomerDaoImpl customerServiceI;
	
	@RequestMapping("/sayHello")
	public void sayHello(){
		customerServiceI.sayHello();
	}
}
