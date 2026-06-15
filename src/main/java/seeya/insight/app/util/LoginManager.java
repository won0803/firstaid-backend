package seeya.insight.app.util;

import java.util.HashMap;
import java.util.Map;

/*
* session이 끊어졌을때를 처리하기 위해 사용
* static메소드에서는 static만사용 하므로static으로 선언한다.
*/
public class LoginManager {
    private static LoginManager loginManager = new LoginManager();
    
    //로그인한 접속자를 담기위한 해시테이블
    private static Map<String, String> loginUsers = new HashMap<String, String>();
    
    /*
     * 싱글톤 패턴 사용
     */
    public static LoginManager getInstance(){
    	return loginManager;
    }
     
    public static Map<String, String> getLoginMap(){
    	return loginUsers;
    }
}