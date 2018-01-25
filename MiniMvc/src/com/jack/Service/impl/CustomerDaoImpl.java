package com.jack.Service.impl;

import com.jack.Service.CustomerDao;
import com.jack.anno.Service;

@Service("customerService")
public class CustomerDaoImpl implements CustomerDao {

	@Override
	public void sayHello() {
		System.out.println("Hello Anno");
	}

}
