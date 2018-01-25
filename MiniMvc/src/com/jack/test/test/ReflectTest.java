package com.jack.test.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Test;


public class ReflectTest {

	@Test
	public void test(){
		try {
			Class<?> clazz = Class.forName("com.jack.controller.CustomerController");
			Object object = clazz.newInstance();
			Constructor<?>[] constructor = object.getClass().getDeclaredConstructors();
			for (Constructor<?> constructor2 : constructor) {
				//System.out.println(constructor2);
			}
			
			Constructor<?> constructor2 =clazz.getConstructor();
			constructor2.newInstance(null);
			System.out.println(constructor2);
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				//System.out.println(method);
			}
			Method method =clazz.getMethod("sayHello", null);
			System.out.println(method);
			
			Field[] fields = clazz.getFields();
			for (Field field : fields) {
				System.out.println(field);
			}
			Field[] fieldss = clazz.getDeclaredFields();
			for (Field field : fieldss) {
				System.out.println(field);
			}
			Field field = clazz.getDeclaredField("customerServiceI");
			System.out.println(field.getType().getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
