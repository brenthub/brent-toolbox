package cn.brent.toolbox.util;

import java.lang.reflect.Field;
import java.util.HashMap;

public class BeanUtil {

	public static HashMap<String,Field> getClassFields(Class<?> clz){
		HashMap<String,Field> map=new HashMap<String,Field>();
		while(clz!=null){
			Field[] fields=clz.getDeclaredFields();
			for(Field f:fields){
				map.put(f.getName(), f);
			}
			
			clz=clz.getSuperclass();
		}
		return map;
	}
}
