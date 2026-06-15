package seeya.insight.app.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.security.MessageDigest;

public class SecureCodingUtil {
	public static String XssScriptingCheck(String content){
		// 크로스 사이트 스크립팅
		if(content != null)
		{
			content = content.replaceAll("&","&amp;");
			content = content.replaceAll("<","&lt;");
			content = content.replaceAll(">","&gt;");
			content = content.replaceAll("\"","&quot;");
			
			return content;
		}
		else{
			return null;
		}
	}
	
	// 위험한 형식 파일 업로드 체크
	public static boolean fileUploadCheck(HttpServletRequest request, long MAX_FILE_SIZE){
		MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
		String next = (String) mRequest.getFileNames().next();
		MultipartFile file = mRequest.getFile(next);
		
		if ( file == null )
			return false;
		
		// 업로드 파일 크기를 제한한다.
		long size = file.getSize();
		if ( size > MAX_FILE_SIZE ) return false;
		
		// MultipartFile로 부터 file을 얻음
		String fileName = file.getOriginalFilename().toLowerCase();

		// 화이트 리스트 방식으로 업로드 파일의 확장자를 체크한다.
		if ( fileName != null )
		{
			if(fileName.endsWith(".doc")||fileName.endsWith(".hwp")||
					fileName.endsWith(".pdf") || fileName.endsWith(".xls") ){
				return true;
			}
			else {
				return false;
			}
		}else{
			return false;
		}
	}
	
	// 디렉토리 경로 조작 방지
	public static String pathTraversalCheck(String fileName){
		if ( fileName != null && !"".equals(fileName)){
			fileName = fileName.replaceAll("/", "");
			fileName = fileName.replaceAll("\\", "");
			fileName = fileName.replaceAll(".", "");
			fileName = fileName.replaceAll("&", "");
			
			return fileName;
		}
		else{
			return null;
		}
	}
	
	// 변환된 기능 문자열 복호화
	public static String deConvertStr(String text){
		String convertText = text;
		
		convertText = convertText.replaceAll("&lt;", "<");
		convertText = convertText.replaceAll("&gt;", ">");
		convertText = convertText.replaceAll("&#35;", "#");
		convertText = convertText.replaceAll("&#39;", "'");
		convertText = convertText.replaceAll("&quot;", "\"");
		
		return convertText;
	}
	
    
    /**
     * 비밀번호를 암호화하는 기능(복호화가 되면 안되므로 SHA-256 인코딩 방식 적용)
     * 
     * @param password 암호화될 패스워드
     * @param id salt로 사용될 사용자 ID 지정
     * @return
     * @throws Exception
     */
    public static String encryptPassword(String password, String id) throws Exception {

		if (password == null) {
		    return "";
		}
	
		byte[] hashValue = null; // 해쉬값
	
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		
		md.reset();
		md.update(id.getBytes());
		
		hashValue = md.digest(password.getBytes());
	
		return new String(Base64.encodeBase64(hashValue));
    }
    
    /**
     * 비밀번호를 암호화하는 기능(복호화가 되면 안되므로 SHA-256 인코딩 방식 적용)
     * @param data 암호화할 비밀번호
     * @param salt Salt
     * @return 암호화된 비밀번호
     * @throws Exception
     */
    public static String encryptPassword(String password, byte[] salt) throws Exception {

		if (password == null) {
		    return "";
		}
	
		byte[] hashValue = null; // 해쉬값
	
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		
		md.reset();
		md.update(salt);
		
		hashValue = md.digest(password.getBytes());
	
		return new String(Base64.encodeBase64(hashValue));
    }
    
    /**
     * 비밀번호를 암호화된 패스워드 검증(salt가 사용된 경우만 적용).
     * 
     * @param data 원 패스워드
     * @param encoded 해쉬처리된 패스워드(Base64 인코딩)
     * @return
     * @throws Exception
     */
    public static boolean checkPassword(String password, String encoded, byte[] salt) throws Exception {
    	byte[] hashValue = null; // 해쉬값
    	
    	MessageDigest md = MessageDigest.getInstance("SHA-256");
    	
    	md.reset();
    	md.update(salt);
    	hashValue = md.digest(password.getBytes());
    	
    	return MessageDigest.isEqual(hashValue, Base64.decodeBase64(encoded.getBytes()));
    }
}
