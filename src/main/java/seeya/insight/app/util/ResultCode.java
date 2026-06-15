package seeya.insight.app.util;

public enum ResultCode {
	SUCCESS("SUCCESS")
	, FAIL("FAIL")
	, ERROR("ERROR");
	
	public String code = "";
	
	private ResultCode(String code){
		this.code = code;
	}
}
