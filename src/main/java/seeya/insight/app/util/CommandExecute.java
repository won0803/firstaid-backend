package seeya.insight.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class CommandExecute {
	private static final Logger logger = LoggerFactory.getLogger(CommandExecute.class);
	private static Runtime rt 		= Runtime.getRuntime();
	private static Process proc 	= null;
	private static InputStream is 	= null;
	private static StringBuffer sb	= new StringBuffer();
	private static byte[] buffer 	= new byte[1024];
	private static int size 		= 0;
	
	/** 
	 * 1. 개요 : 문자열 형태로 된 명령어를 수행한다. 
	 * 2. 입력데이터 : String(명령어)
	 * 3. 출력데이터 : 결과 데이터(String)
	 * 4. 최초작성일 : 2017. 7. 25.
	 */
	public static String execute(String command){
		try{
			if(command != null || "".equals(command)){
				proc = rt.exec(new String[]{"/bin/bash", "-c", command});
				
				proc.waitFor();
				is = proc.getInputStream();
				
				// 버퍼 초기화
				sb.setLength(0);
				
				while((size = is.read(buffer, 0, buffer.length)) != -1){
					sb.append(new String(buffer, 0, size));
				}
				
				// 열려있는 Stream 모두 제거
				is.close();
				proc.getErrorStream().close();
				proc.getInputStream().close();
				proc.getOutputStream().close();
				
				logger.debug("실행 명령어 : " + command);
				logger.debug("실행 결과 : " + sb.toString());
			}
		}
		catch(IOException ioe){
			logger.debug("detail", ioe);
		}
		catch(InterruptedException ie){
			logger.debug("detail", ie);
		}
		
		return sb.toString();
	}
	
	/** 
	 * 1. 개요 : 문자열 배열 형태로 된 명령어를 수행한다. 
	 * 2. 입력데이터 : String(명령어)
	 * 3. 출력데이터 : 결과 데이터(String)
	 * 4. 최초작성일 : 2017. 7. 25.
	 */
	public static String execute(String[] command){
		try{
			if(command != null || "".equals(command)){
				proc = rt.exec(command);
				
				proc.waitFor();
				is = proc.getInputStream();
				
				// 버퍼 초기화
				sb.setLength(0);
				
				while((size = is.read(buffer, 0, buffer.length)) != -1){
					sb.append(new String(buffer, 0, size));
				}
				
				// 열려있는 Stream 모두 제거
				is.close();
				proc.getErrorStream().close();
				proc.getInputStream().close();
				proc.getOutputStream().close();
				
				logger.debug("실행 결과 : " + sb.toString());
			}
		}
		catch(IOException ioe){
			logger.debug("detail", ioe);
		}
		catch(InterruptedException ie){
			logger.debug("detail", ie);
		}
		
		return sb.toString();
	}
	
	/** 
	 * 1. 개요 : 문자열 형태로 된 명령어를 수행한다. 
	 * 2. 입력데이터 : String(명령어)
	 * 3. 출력데이터 : 결과 데이터(String)
	 * 4. 최초작성일 : 2017. 7. 25.
	 */
	public static String executeNoWait(String command){
		try{
			if(command != null || "".equals(command)){
				proc = rt.exec(command);
				
				//proc.waitFor();
				is = proc.getInputStream();
				
				// 버퍼 초기화
				sb.setLength(0);
				
				while((size = is.read(buffer, 0, buffer.length)) != -1){
					sb.append(new String(buffer, 0, size));
				}
				
				// 열려있는 Stream 모두 제거
				is.close();
				proc.getErrorStream().close();
				proc.getInputStream().close();
				proc.getOutputStream().close();
			}
		}
		catch(IOException ioe){
			logger.debug("detail", ioe);
		}
		
		return sb.toString();
	}
	
	/** 
	 * 1. 개요 : 문자열 배열 형태로 된 명령어를 수행한다. 
	 * 2. 입력데이터 : String(명령어)
	 * 3. 출력데이터 : 결과 데이터(String)
	 * 4. 최초작성일 : 2017. 7. 25.
	 */
	public static String executeNoWait(String[] command){
		try{
			if(command != null || "".equals(command)){
				proc = rt.exec(command);
				
				//proc.waitFor();
				is = proc.getInputStream();
				
				// 버퍼 초기화
				sb.setLength(0);
				
				while((size = is.read(buffer, 0, buffer.length)) != -1){
					sb.append(new String(buffer, 0, size));
				}
				
				// 열려있는 Stream 모두 제거
				is.close();
				proc.getErrorStream().close();
				proc.getInputStream().close();
				proc.getOutputStream().close();
			}
		}
		catch(IOException ioe){
			logger.debug("detail", ioe);
		}
		
		return sb.toString();
	}
}
