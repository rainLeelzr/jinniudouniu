package com.jinniu.commonjn.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 
* @Title: JsonHelper.java
* @Package com.alliance.util
* @Description: json解析工具，用于object与json之间的转换
* @author chenwenhao 
* @date 2017-1-13 上午9:44:17
 */
public class JsonHelper {
	
	private static final Logger log = LoggerFactory.getLogger(JsonHelper.class);

	private static ObjectMapper mapper;
	static{
		mapper = new ObjectMapper();
		//忽略不存在的属性
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	
	/**
	 * 
	* @Title: JsonHelper.java
	* @Package com.alliance.util
	* @Description: 把一个对象转成json字符串
	* @author chenwenhao 
	* @date 2017-1-13 上午9:51:18
	 */
	public static String toJson(Object obj){
		String result = null;
		try{
			if(obj!=null){
				result = mapper.writeValueAsString(obj);
			}
			return result;
		}catch (Exception e) {
			log.info("error converting object to json : {}",obj);
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 
	* @Title: JsonHelper.java
	* @Package com.alliance.util
	* @Description: 通过字符串转成对象
	* @author chenwenhao 
	* @date 2017-1-13 上午10:01:59
	 */
	public static <T> T fromJson(String json , Class<T> clazz){
		T result = null;
		try{
			if(StringUtils.isNotBlank(json)){
				result = mapper.readValue(json, clazz);
			}
			return result;
		}catch (Exception e) {
			log.info("error converting json to object : {}",json);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	* @Title: fromJson
	* @Description: 处理对象集合的情况
	* @param @param json
	* @param @param tr
	* @param @return    设定文件
	* @return T    返回类型
	* @author chenwenhao
	* @throws
	 */
	public static <T> T fromJson(String json , TypeReference<T> tr){
		T result = null;
		try{
			if(StringUtils.isNotBlank(json)){
				result = mapper.readValue(json, tr);
			}
			return result;
		}catch (Exception e) {
			log.info("error converting json to object : {}",json);
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
//		//处理简单类型的集合
//		List<Integer> list = new ArrayList<Integer>();
//		list.add(22);
//		list.add(33);
//		list.add(44);
//		String jsonString = JsonHelper.toJson(list);
//		System.out.println("jsonString ："+jsonString);
//		list = JsonHelper.fromJson(jsonString, List.class);
//		System.out.println("list : "+list);
		
//		//处理对象集合List
//		List<TestClass> list = new ArrayList<TestClass>();
//		list.add(new TestClass());
//		list.add(new TestClass());
//		list.add(new TestClass());
//		String jsonString = JsonHelper.toJson(list);
//		System.out.println("jsonString ："+jsonString);
//		list = JsonHelper.fromJson(jsonString, new TypeReference<List<TestClass>>() {});
//		System.out.println("list : "+list);
		
		//处理对象集合Map
		Map<String,TestClass> map = new HashMap<String, TestClass>();
		map.put("11",new TestClass());
		map.put("22",new TestClass());
		map.put("33",new TestClass());
		String jsonString = JsonHelper.toJson(map);
		System.out.println("jsonString ："+jsonString);
		map = JsonHelper.fromJson(jsonString, new TypeReference<Map<String,TestClass>>() {});
		System.out.println("map : "+map);
		
	}
	
	private static class TestClass{
		private Integer id;
		@SuppressWarnings("unused")
		public Integer getId() {
			return id;
		}
		@SuppressWarnings("unused")
		public void setId(Integer id) {
			this.id = id;
		}	
	}
	
}

	

