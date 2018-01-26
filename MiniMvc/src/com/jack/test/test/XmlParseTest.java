package com.jack.test.test;

import java.util.Map;

import org.junit.Test;

import com.jack.xml.Beans;
import com.jack.xml.Beans.View;
import com.jack.xml.XmlParse;

public class XmlParseTest {
	
	@Test
	public void testXmlParse(){
		XmlParse xmlParse = new XmlParse();
		Map<String, Object> map = xmlParse.getConfig("com/jack/test/minimvc.xml");
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if(entry.getKey().equals("view")){
				System.out.println(((View) entry.getValue()).getSuffix());
			}
			System.out.println(entry.getKey()+"--"+entry.getValue());
		}
	}
}
