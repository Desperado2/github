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
	 * 默认获取配置文件，xml配置文件必须方法classpath路径下，并且名称为minimvc.xml
	 * @return
	 */
	public Map<String,Object> getConfig(){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			//获取xml配置文件，创建inputstream
			InputStream inputStream = this.getClass().getClassLoader().getResource("minimvc.xml").openStream();
			//根据xml对象的对象的class创建JASB
			JAXBContext jaxbContext = JAXBContext.newInstance(Beans.class);
			//解析xml
			Unmarshaller unmarshaller =jaxbContext.createUnmarshaller();
			Beans bean= (Beans) unmarshaller.unmarshal(inputStream);
			map = MapAndJavaBeanCovertUtils.objectToMap(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 根据自定义的xml文件名称获取配置
	 * @param xmlName  配置文件的名称
	 * @return
	 */
	public Map<String,Object> getConfig(String xmlName){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			//获取xml配置文件，创建inputstream
			InputStream inputStream = this.getClass().getClassLoader().getResource(xmlName).openStream();
			//根据xml对象的对象的class创建JASB
			JAXBContext jaxbContext = JAXBContext.newInstance(Beans.class);
			//解析xml
			Unmarshaller unmarshaller =jaxbContext.createUnmarshaller();
			Beans bean= (Beans) unmarshaller.unmarshal(inputStream);
			map = MapAndJavaBeanCovertUtils.objectToMap(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
