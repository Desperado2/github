package com.jack.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jack.anno.Autowired;
import com.jack.anno.Controller;
import com.jack.anno.RequestMapping;
import com.jack.anno.Service;
import com.jack.test.controller.CustomerController;
import com.jack.xml.XmlParse;


public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private List<String> packageNames = new ArrayList<>();
	private Map<String,Object> classMap = new HashMap<String,Object>();
	private Map<String,Object> methodMap = new HashMap<String,Object>();   
 
    public DispatcherServlet() {
        super();
    }

	
	public void init(ServletConfig config) throws ServletException {
		//获取配置文件
		XmlParse xmlParse = new XmlParse();
		Map<String, Object> configs = xmlParse.getConfig(config.getInitParameter("configxml"));
		//扫描包
		scanPackage(configs.get("scanPackage").toString());
		try {
			//实例化对象
			saveClass();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//反射方法
		handlerMap();
		//依赖注入
		ioc();
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取URL
		String reuestUrl = request.getRequestURI();
		String pName = request.getContextPath();
		//获取方法路径
		String realUrl = reuestUrl.replaceAll(pName, "");
		//获取controller方法名称
		String cName = (realUrl.split("/")[1]);
		//实例化方法
		Method method =(Method) methodMap.get(realUrl);
		//实例化controller
		Object controller =  classMap.get(cName);
		try {
			//调用方法
			method.invoke(controller, new Object[]{request,response});
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	private void scanPackage(String packageName){
		//替换包名中的 .
		String packageName1 = replacePackageName(packageName);
		//获取当前的统一资源定位符
		URL url = this.getClass().getClassLoader().getResource(packageName1);	
		//获取此URL的文件名
		String pathFile= url.getFile();
		//通过将给定路径名字符串转换成抽象路径名来创建一个新 File 实例
		File file = new File(pathFile);
		//返回由此抽象路径名所表示的目录中的文件和目录的名称所组成字符串数组。
		String[] files = file.list();
		for(String filePath : files){
			//根据目录名称和文件名称创建新的File对象
			File eachFile = new File(pathFile+"/"+filePath);
			if(eachFile.isDirectory()){
				//如果为文件夹，则将新的名称作为包名，再次调用该方法。
				scanPackage(packageName+"."+eachFile.getName());
			}else{
				//判断文件的扩展名是否为class，如果是，则将该文件的包名和文件名一起保存到集合中。
				if(getFileExtendName(eachFile.getName()).equals("class"))
					packageNames.add(packageName+"."+eachFile.getName());
				continue;
			}
		}
	}
	

	private String replacePackageName(String packageName){
		return packageName.replaceAll("\\.", "/");
	}
	
	
	private void saveClass() throws Exception{
		//判断集合是否为空
		if(packageNames.size() <=0){
			return;
		}
		for(String pkn:packageNames){
			//根据类的全限定名通过反射获取指定类的class对象
			Class<?> object = Class.forName(pkn.replace(".class", "").trim());
			//判断该类是否有controller注解
			if(object.isAnnotationPresent(Controller.class)){
				//实例化该class的类对象
				Object instance = object.newInstance();
				//获取controller注解的对象
				Controller controller = object.getAnnotation(Controller.class);
				//获取该注解的参数的值
				String key = controller.value();
				//判断注解的值是否为空
				if(key.equals("")){
					//获取该类的类名首字母小写作为值。
					key=object.getSimpleName().substring(0, 1).toLowerCase()+object.getSimpleName().substring(1);
				}
				//将类的名称和类的实例放到map对象中
				classMap.put(key, instance);
				//判断该对象是否有service注解
			}else if(object.isAnnotationPresent(Service.class)){
				//获取该类的实例
				Object instance = object.newInstance();
				//获取service的对象实例
				Service service = object.getAnnotation(Service.class);
				//获取注解的值
				String key = service.value();
				//判断注解的值是否为空
				if(key.equals("")){
					//获取该类的类名首字母小写作为值。
					key=object.getSimpleName().substring(0, 1).toLowerCase()+object.getSimpleName().substring(1);
				}
				//将类的名称和类的实例放到map对象中
				classMap.put(key, instance);
			}else{
				continue;
			}
		}		
	}
	
	private void handlerMap(){
		//判断类的集合是否为空
		if(classMap.size() <=0){
			return;
		}
		//遍历该map对象
		for (Map.Entry<String,Object> entry : classMap.entrySet()) {
			//获取该类是否有controller注解
			if(entry.getValue().getClass().isAnnotationPresent(Controller.class)){
				//获取controller注解对象
				Controller controller = entry.getValue().getClass().getAnnotation(Controller.class);
				//获取controller注解的值
				String value = controller.value();
				//判断注解的值是否为空
				if(value.equals("")){
					//获取该类的类名首字母小写作为值。
					value=entry.getValue().getClass().getSimpleName().substring(0, 1).toLowerCase()+entry.getValue().getClass().getSimpleName().substring(1);
				}
				//反射获取该类下面的所有方法
				Method[] methods = entry.getValue().getClass().getMethods();
				for (Method method : methods) {
					//判断该方法是否有RequestMapping注解
					if(method.isAnnotationPresent(RequestMapping.class)){
						//获取RequestMapping对象
						RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
						//获取注解的值
						String rvalue = requestMapping.value();
						//判断注解的值是否为空
						if(rvalue.equals("")){
							//获取该类的类名首字母小写作为值。
							rvalue=method.getClass().getSimpleName().substring(0, 1).toLowerCase()+method.getClass().getSimpleName().substring(1);
						}
						//将controller的名称和方法的名称保存到集合中
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
		//判断集合是否为空
		if(classMap.size() <=0){
			return;
		}
		//变量所有的类
		for (Map.Entry<String, Object> entry : classMap.entrySet()) {
			//获取本类下面所有的成员变量
			Field[] fileds =entry.getValue().getClass().getDeclaredFields();
			for (Field field : fileds) {
				//设置该成员变量可以编辑
				field.setAccessible(true);
				//判断该成员对象是否有Autowired注解
				if(field.isAnnotationPresent(Autowired.class)){
					try {
						//设置该成员变量可以编辑
						field.setAccessible(true);
						//获取包名
						String packString= field.getType().getPackage().getName();
						//获取实现类的类名
						String className=  field.getType().getSimpleName()+"Impl";
						//反射该类对象
						Class<?> obj = Class.forName(packString+".impl."+className);
						String value="";
						//判断该类是否有controller注解
						if(obj.isAnnotationPresent(Controller.class)){
							//获取controller的值
							value= obj.getAnnotation(Controller.class).value();
							if(value.equals("")){
								//获取该类的类名首字母小写作为值。
								value=obj.getSimpleName().substring(0, 1).toLowerCase()+obj.getSimpleName().substring(1);
							}
						//判断该类是否有service注解
						}else if(obj.isAnnotationPresent(Service.class)){
							//获取service的值
							value= obj.getAnnotation(Service.class).value();
							if(value.equals("")){
								//获取该类的类名首字母小写作为值。
								value=obj.getSimpleName().substring(0, 1).toLowerCase()+obj.getSimpleName().substring(1);
							}
						}
						//给成员变量赋值实例
						field.set(entry.getValue(), classMap.get(value));
					} catch (IllegalArgumentException | IllegalAccessException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	private String getFileExtendName(String fileName){
		//判断传入的文件名是否为空，或者长度是否小于等于0
		if(fileName == null ||fileName.length() <=0)
			return null;
		//获取文件中最后一个 . 出现的地方的下标
		int lastIndex = fileName.lastIndexOf(".");
		//判断下标是否为正常的值
		if(lastIndex > -1 && lastIndex <(fileName.length()-1))
			//截取文件的扩展名
			return fileName.substring(lastIndex+1);
		return fileName;
	}
}

