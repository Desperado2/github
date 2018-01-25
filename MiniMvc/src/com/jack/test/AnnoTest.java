package com.jack.test;


import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.jack.anno.Autowired;
import com.jack.anno.Controller;
import com.jack.anno.RequestMapping;
import com.jack.anno.Service;
import com.jack.controller.CustomerController;

public class AnnoTest {
	
	private List<String> packageNames = new ArrayList<>();
	private Map<String,Object> classMap = new HashMap<String,Object>();
	private Map<String,Object> methodMap = new HashMap<String,Object>();
	@Test
	public void testAnno(){
		
		scanPackage("com.jack");
		try {
			saveClass();
		} catch (Exception e) {
			e.printStackTrace();
		}
		handlerMap();
		ioc();
		Method eMethod =(Method) methodMap.get("/customerController/sayHello");
		CustomerController eController = (CustomerController) classMap.get("customerController");
		try {
			eMethod.invoke(eController, null);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	

	
	private void scanPackage(String packageName){
		String packageName1 = replacePackageName(packageName);
		URL url = this.getClass().getClassLoader().getResource(packageName1);	
		String pathFile= url.getFile();
		File file = new File(pathFile);
		String[] files = file.list();
		for(String filePath : files){
			File eachFile = new File(pathFile+"/"+filePath);
			if(eachFile.isDirectory()){
				scanPackage(packageName+"."+eachFile.getName());
			}else{
				packageNames.add(packageName+"."+eachFile.getName());	
			}
		}
	}
	

	private String replacePackageName(String packageName){
		return packageName.replaceAll("\\.", "/");
	}
	
	
	private void saveClass() throws Exception{
		if(packageNames.size() <=0){
			return;
		}
		for(String pkn:packageNames){
			Class<?> object = Class.forName(pkn.replace(".class", "").trim());
			if(object.isAnnotationPresent(Controller.class)){
				Object instance = object.newInstance();
				Controller controller = object.getAnnotation(Controller.class);
				String key = controller.value();
				classMap.put(key, instance);
			}else if(object.isAnnotationPresent(Service.class)){
				Object instance = object.newInstance();
				Service service = object.getAnnotation(Service.class);
				String key = service.value();
				classMap.put(key, instance);
			}else{
				continue;
			}
		}		
	}
	
	private void handlerMap(){
		if(classMap.size() <=0){
			return;
		}
		for (Map.Entry<String,Object> entry : classMap.entrySet()) {
			if(entry.getValue().getClass().isAnnotationPresent(Controller.class)){
				Controller controller = entry.getValue().getClass().getAnnotation(Controller.class);
				String value = controller.value();
				Method[] methods = entry.getValue().getClass().getMethods();
				for (Method method : methods) {
					if(method.isAnnotationPresent(RequestMapping.class)){
						RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
						String rvalue = requestMapping.value();
						methodMap.put("/"+value+rvalue, method);
					}else{
						continue;
					}
				}
			}else{
				continue;
			}
		}
	}
	
	private void ioc(){
		if(classMap.size() <=0){
			return;
		}
		for (Map.Entry<String, Object> entry : classMap.entrySet()) {
			Field[] fileds =entry.getValue().getClass().getDeclaredFields();
			for (Field field : fileds) {
				field.setAccessible(true);
				if(field.isAnnotationPresent(Autowired.class)){
					try {
						field.setAccessible(true);
						Class<?> obj = field.getType();
						String value="";
						if(obj.isAnnotationPresent(Controller.class)){
							value= obj.getAnnotation(Controller.class).value();
						}else if(obj.isAnnotationPresent(Service.class)){
							value= obj.getAnnotation(Service.class).value();
						}
						field.set(entry.getValue(), classMap.get(value));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
















