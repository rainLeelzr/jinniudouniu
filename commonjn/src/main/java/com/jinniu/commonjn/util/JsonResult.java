package com.jinniu.commonjn.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JsonResult {
	private static final int SUCCESS=1;
	private static final int FAILED=2;
	
	private int status =SUCCESS;
	
	private List<ErrorCode> message=new ArrayList<ErrorCode>();
	
	private Object data;
	
	private Map<String, Object> extra = new TreeMap<String, Object>();
	
	public JsonResult() {}
	
	public JsonResult(List<ErrorCode> messages) {
		this.status = FAILED;
		this.message = messages;
	}
	
	public JsonResult(ErrorCode errorCode) {
		List<ErrorCode> errorCodes = new ArrayList<ErrorCode>();
		errorCodes.add(errorCode);
		this.message = errorCodes;
		this.status = FAILED;
	}
	
	
	public JsonResult(Object data) {
		this.status = SUCCESS;
		this.data = data;
	}
	
	public JsonResult(int status) {
		this.status = status;
	}
	
	public JsonResult(int status, Object data) {
		this.status = SUCCESS;
		this.data = data;
	}
	
	public JsonResult(int status, Object data, List<ErrorCode> messages) {
		this.data = data;
		this.status = status;
		this.message = messages;
	}
	
	public Object getData() {
		return null==data?new Object():data;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<ErrorCode> getMessage() {
		return message;
	}
	public void setMessage(List<ErrorCode> message) {
		this.message = message;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public void addErrorCode(ErrorCode errorCode){
		status = FAILED;
		message.add(errorCode);
	}
	
	public boolean validResult(){
		return status == SUCCESS;
	}
	
	public String toString()
	{
		return JsonUtil.toJson(this);
	}
	public Map<String, Object> getExtra() {
		return extra;
	}
	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}

}
