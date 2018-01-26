package com.jack.xml;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.jasper.tagplugins.jstl.core.Url;

import com.jack.util.MapAndJavaBeanCovertUtils;

public class XmlParse {

	/**
	 * Ĭ�ϻ�ȡ�����ļ���xml�����ļ����뷽��classpath·���£���������Ϊminimvc.xml
	 * @return
	 */
	public Map<String,Object> getConfig(){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			//��ȡxml�����ļ�������inputstream
			InputStream inputStream = this.getClass().getClassLoader().getResource("minimvc.xml").openStream();
			//����xml����Ķ����class����JASB
			JAXBContext jaxbContext = JAXBContext.newInstance(Bean.class);
			//����xml
			Unmarshaller unmarshaller =jaxbContext.createUnmarshaller();
			Bean bean= (Bean) unmarshaller.unmarshal(inputStream);
			map = MapAndJavaBeanCovertUtils.objectToMap(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * �����Զ����xml�ļ����ƻ�ȡ����
	 * @param xmlName  �����ļ�������
	 * @return
	 */
	public Map<String,Object> getConfig(String xmlName){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			URL url1 = this.getClass().getClassLoader().getSystemResource(xmlName);
			URL url =this.getClass().getClassLoader().getResource(xmlName);
			//��ȡxml�����ļ�������inputstream
			InputStream inputStream = this.getClass().getClassLoader().getResource(xmlName).openStream();
			//����xml����Ķ����class����JASB
			JAXBContext jaxbContext = JAXBContext.newInstance(Bean.class);
			//����xml
			Unmarshaller unmarshaller =jaxbContext.createUnmarshaller();
			Bean bean= (Bean) unmarshaller.unmarshal(inputStream);
			map = MapAndJavaBeanCovertUtils.objectToMap(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
}