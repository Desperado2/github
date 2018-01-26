package com.jack.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;



/**
 * map ��  Javabean�໥ת��������
 * @author Jackm
 *
 */
public class MapAndJavaBeanCovertUtils {

	/**
	 * javabean����ת��Ϊmap����
	 * @param object Ҫת���Ķ���
	 * @return  ת�����map
	 * @throws Exception
	 */
	public static  Map<String, Object> objectToMap(Object object) throws Exception{
		 Map<String,Object> map = new HashMap<String,Object>();
		 if(object == null)
			 return null;
		 //�����ȡ���������������
		 Field[] fields = object.getClass().getDeclaredFields();
		 for (Field field : fields) {
			//����˽�����Կ��Բ���
			 field.setAccessible(true);
			//��ȡ��������
			 String name = field.getName();
			//��ȡ���Ե�ֵ
			 Object value = field.get(object);
			 map.put(name, value);
		}
		return map;	
	}
	
	/**
	 * ��map����װ��Ϊָ����Java�����
	 * @param map  Ҫת�е�map
	 * @param clazz   ҪתΪ��Java�����class
	 * @return  ת����ɵĶ���
	 * @throws Exception
	 */
	public static Object mapToObject(Map<String, Object> map,Class<?> clazz) throws Exception{
		if(map.size() <=0)
			return null;
			//��ȡ����
		Object object = clazz.newInstance();
		//��ȡ�����������
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			//��ȡ���Ե��޶���
			int mod = field.getModifiers();
			//�ж��Ƿ�Ϊstatic �� final
			if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
				continue;
			}
			//����field�ɲ���
			field.setAccessible(true);
			//�����Ը�ֵ
			field.set(object, map.get(field.getName()));
		}
		
		return object;
	}
}
