package com.jack.test.service.impl;


import com.jack.anno.Service;
import com.jack.test.service.CustomerDao;

@Service("customerService")
public class CustomerDaoImpl implements CustomerDao {

	@Override
	public void sayHello() {
		System.out.println("Hello Anno");
	}

}
