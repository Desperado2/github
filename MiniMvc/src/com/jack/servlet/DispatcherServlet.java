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
		//��ȡ�����ļ�
		XmlParse xmlParse = new XmlParse();
		Map<String, Object> configs = xmlParse.getConfig(config.getInitParameter("configxml"));
		//ɨ���
		scanPackage(configs.get("scanPackage").toString());
		try {
			//ʵ��������
			saveClass();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//���䷽��
		handlerMap();
		//����ע��
		ioc();
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//��ȡURL
		String reuestUrl = request.getRequestURI();
		String pName = request.getContextPath();
		//��ȡ����·��
		String realUrl = reuestUrl.replaceAll(pName, "");
		//��ȡcontroller��������
		String cName = (realUrl.split("/")[1]);
		//ʵ��������
		Method method =(Method) methodMap.get(realUrl);
		//ʵ����controller
		Object controller =  classMap.get(cName);
		try {
			//���÷���
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
		//�滻�����е� .
		String packageName1 = replacePackageName(packageName);
		//��ȡ��ǰ��ͳһ��Դ��λ��
		URL url = this.getClass().getClassLoader().getResource(packageName1);	
		//��ȡ��URL���ļ���
		String pathFile= url.getFile();
		//ͨ��������·�����ַ���ת���ɳ���·����������һ���� File ʵ��
		File file = new File(pathFile);
		//�����ɴ˳���·��������ʾ��Ŀ¼�е��ļ���Ŀ¼������������ַ������顣
		String[] files = file.list();
		for(String filePath : files){
			//����Ŀ¼���ƺ��ļ����ƴ����µ�File����
			File eachFile = new File(pathFile+"/"+filePath);
			if(eachFile.isDirectory()){
				//���Ϊ�ļ��У����µ�������Ϊ�������ٴε��ø÷�����
				scanPackage(packageName+"."+eachFile.getName());
			}else{
				//�ж��ļ�����չ���Ƿ�Ϊclass������ǣ��򽫸��ļ��İ������ļ���һ�𱣴浽�����С�
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
		//�жϼ����Ƿ�Ϊ��
		if(packageNames.size() <=0){
			return;
		}
		for(String pkn:packageNames){
			//�������ȫ�޶���ͨ�������ȡָ�����class����
			Class<?> object = Class.forName(pkn.replace(".class", "").trim());
			//�жϸ����Ƿ���controllerע��
			if(object.isAnnotationPresent(Controller.class)){
				//ʵ������class�������
				Object instance = object.newInstance();
				//��ȡcontrollerע��Ķ���
				Controller controller = object.getAnnotation(Controller.class);
				//��ȡ��ע��Ĳ�����ֵ
				String key = controller.value();
				//�ж�ע���ֵ�Ƿ�Ϊ��
				if(key.equals("")){
					//��ȡ�������������ĸСд��Ϊֵ��
					key=object.getSimpleName().substring(0, 1).toLowerCase()+object.getSimpleName().substring(1);
				}
				//��������ƺ����ʵ���ŵ�map������
				classMap.put(key, instance);
				//�жϸö����Ƿ���serviceע��
			}else if(object.isAnnotationPresent(Service.class)){
				//��ȡ�����ʵ��
				Object instance = object.newInstance();
				//��ȡservice�Ķ���ʵ��
				Service service = object.getAnnotation(Service.class);
				//��ȡע���ֵ
				String key = service.value();
				//�ж�ע���ֵ�Ƿ�Ϊ��
				if(key.equals("")){
					//��ȡ�������������ĸСд��Ϊֵ��
					key=object.getSimpleName().substring(0, 1).toLowerCase()+object.getSimpleName().substring(1);
				}
				//��������ƺ����ʵ���ŵ�map������
				classMap.put(key, instance);
			}else{
				continue;
			}
		}		
	}
	
	private void handlerMap(){
		//�ж���ļ����Ƿ�Ϊ��
		if(classMap.size() <=0){
			return;
		}
		//������map����
		for (Map.Entry<String,Object> entry : classMap.entrySet()) {
			//��ȡ�����Ƿ���controllerע��
			if(entry.getValue().getClass().isAnnotationPresent(Controller.class)){
				//��ȡcontrollerע�����
				Controller controller = entry.getValue().getClass().getAnnotation(Controller.class);
				//��ȡcontrollerע���ֵ
				String value = controller.value();
				//�ж�ע���ֵ�Ƿ�Ϊ��
				if(value.equals("")){
					//��ȡ�������������ĸСд��Ϊֵ��
					value=entry.getValue().getClass().getSimpleName().substring(0, 1).toLowerCase()+entry.getValue().getClass().getSimpleName().substring(1);
				}
				//�����ȡ������������з���
				Method[] methods = entry.getValue().getClass().getMethods();
				for (Method method : methods) {
					//�жϸ÷����Ƿ���RequestMappingע��
					if(method.isAnnotationPresent(RequestMapping.class)){
						//��ȡRequestMapping����
						RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
						//��ȡע���ֵ
						String rvalue = requestMapping.value();
						//�ж�ע���ֵ�Ƿ�Ϊ��
						if(rvalue.equals("")){
							//��ȡ�������������ĸСд��Ϊֵ��
							rvalue=method.getClass().getSimpleName().substring(0, 1).toLowerCase()+method.getClass().getSimpleName().substring(1);
						}
						//��controller�����ƺͷ��������Ʊ��浽������
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
		//�жϼ����Ƿ�Ϊ��
		if(classMap.size() <=0){
			return;
		}
		//�������е���
		for (Map.Entry<String, Object> entry : classMap.entrySet()) {
			//��ȡ�����������еĳ�Ա����
			Field[] fileds =entry.getValue().getClass().getDeclaredFields();
			for (Field field : fileds) {
				//���øó�Ա�������Ա༭
				field.setAccessible(true);
				//�жϸó�Ա�����Ƿ���Autowiredע��
				if(field.isAnnotationPresent(Autowired.class)){
					try {
						//���øó�Ա�������Ա༭
						field.setAccessible(true);
						//��ȡ����
						String packString= field.getType().getPackage().getName();
						//��ȡʵ���������
						String className=  field.getType().getSimpleName()+"Impl";
						//����������
						Class<?> obj = Class.forName(packString+".impl."+className);
						String value="";
						//�жϸ����Ƿ���controllerע��
						if(obj.isAnnotationPresent(Controller.class)){
							//��ȡcontroller��ֵ
							value= obj.getAnnotation(Controller.class).value();
							if(value.equals("")){
								//��ȡ�������������ĸСд��Ϊֵ��
								value=obj.getSimpleName().substring(0, 1).toLowerCase()+obj.getSimpleName().substring(1);
							}
						//�жϸ����Ƿ���serviceע��
						}else if(obj.isAnnotationPresent(Service.class)){
							//��ȡservice��ֵ
							value= obj.getAnnotation(Service.class).value();
							if(value.equals("")){
								//��ȡ�������������ĸСд��Ϊֵ��
								value=obj.getSimpleName().substring(0, 1).toLowerCase()+obj.getSimpleName().substring(1);
							}
						}
						//����Ա������ֵʵ��
						field.set(entry.getValue(), classMap.get(value));
					} catch (IllegalArgumentException | IllegalAccessException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	private String getFileExtendName(String fileName){
		//�жϴ�����ļ����Ƿ�Ϊ�գ����߳����Ƿ�С�ڵ���0
		if(fileName == null ||fileName.length() <=0)
			return null;
		//��ȡ�ļ������һ�� . ���ֵĵط����±�
		int lastIndex = fileName.lastIndexOf(".");
		//�ж��±��Ƿ�Ϊ������ֵ
		if(lastIndex > -1 && lastIndex <(fileName.length()-1))
			//��ȡ�ļ�����չ��
			return fileName.substring(lastIndex+1);
		return fileName;
	}
}

