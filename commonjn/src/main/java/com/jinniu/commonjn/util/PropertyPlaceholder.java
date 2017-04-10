package com.jinniu.commonjn.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 加密工具栏
 * 
 * @author Jan
 * 
 */
public class PropertyPlaceholder extends PropertyPlaceholderConfigurer {
	 private static Map<String,String> propertyMap;

	protected void processProperties(
			ConfigurableListableBeanFactory beanFactory, Properties props)
			throws BeansException {
//		  propertyMap = new HashMap<String, String>();
//	        for (Object key : props.keySet()) {
//	            String keyStr = key.toString();
//	            String value = props.getProperty(keyStr);
//	           // System.out.print("111111111--"+value);
//	            propertyMap.put(keyStr, value);
//	        	
//	        }
//	        
//		String password = props.getProperty("jdbc.password");
//		if (password != null) {
//			
//			String strEnc = RSAUtils.decrypt(password, true);
//		//System.out.println("解密--"+strEnc);
//			props.setProperty("jdbc.password", strEnc);// 赋值
//
//	      }
		super.processProperties(beanFactory, props);
	}
	
	public static Object getProperty(String name) {
        return propertyMap.get(name);
    }
}

