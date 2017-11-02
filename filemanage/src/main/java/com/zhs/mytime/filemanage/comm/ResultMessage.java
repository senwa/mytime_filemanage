package com.zhs.mytime.filemanage.comm;

public class ResultMessage{
	
	public final static int SUCCESS = 1;
	public final static int FAIL = 0;
	
	private String message;
	private String cause;
	private int result;
	private Object extData;
	
	public ResultMessage(int result) {
		this.result = result;
	}
	
	public ResultMessage(int result,String message) {
		this.message = message;
		this.result = result;
	}
	
	
	public ResultMessage(int result,String message, String cause) {
		this.message = message;
		this.cause = cause;
		this.result = result;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}


	public Object getExtData() {
		return extData;
	}

	public void setExtData(Object extData) {
		this.extData = extData;
	}
	
	@Override
	public String toString() {
		return "ResultMessage [message=" + message + ", cause=" + cause + ", result=" + result + "]";
	}
	
}