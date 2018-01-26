package com.jack.view;

import com.jack.xml.XmlParse;

public class UrlBasedViewResolver {
	//redirect��ʽ��ת��URL��ǰ׺
	private static final String REDIRECT_URL_PREFIX="redirect:";
	//forward��ʽ��ת��URL��ǰ׺
	private static final String FORWARD_URL_PREFIX="forward:";
	//��ͼ�ļ�������
	private String contentTypeString;
	//��ͼ�ļ���ǰ׺
	private String prefix="";
	//��ͼ�ļ��ĺ�׺
	private String suffix ="";
	public String getContentTypeString() {
		return contentTypeString;
	}
	public void setContentTypeString(String contentTypeString) {
		this.contentTypeString = contentTypeString;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	public String createView(String viewName){
		XmlParse xmlParse = new XmlParse();
		String realPath="";
		if(viewName != null && !viewName.equals("")){
			realPath = REDIRECT_URL_PREFIX+prefix+viewName+suffix;
		}
		return realPath;
		
	}
	
}


















