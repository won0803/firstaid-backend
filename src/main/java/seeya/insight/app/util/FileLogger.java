package seeya.insight.app.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

public class FileLogger {
	
	private static File logDir 	= null;			// 로그파일 디렉토리
	private static File logFile = null;			// 로그파일 경로
	private String osType		= null;			// 운영체제 타입
	
	private SimpleDateFormat logFormat = new SimpleDateFormat("[ yyyy-MM-dd HH:mm:ss ] ", Locale.KOREA);
	private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
	private SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.KOREA);
	private SimpleDateFormat toDayFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
	
	private FileLogger(){
	}

	private FileLogger(String logDirectory){
		String strYear = yearFormat.format(System.currentTimeMillis());
		String strMonth = monthFormat.format(System.currentTimeMillis());
		String strLogDir = new File(logDirectory).getPath();
		
		String nowTime= toDayFormat.format(System.currentTimeMillis());
		
		/*os별 로그파일형식지정*/
		osType = osCheck();
		
		logDir = new File(strLogDir + File.separator + strYear + File.separator + strMonth);
		
		
		if(!logDir.exists())
			logDir.mkdirs();
		
		if(osType.equals("win")){
			
			logFile = new File(logDir.getPath() + File.separator+  nowTime+ "_log.html"); 
		}else{
			logFile = new File(logDir.getPath() + File.separator+ toDayFormat.format(System.currentTimeMillis()) + "_log");
		}
		
		try{
			if(!logFile.exists()){
				logFile.createNewFile();
				FileWriter fw  = new FileWriter(logFile);
				
				if(osType.equals("win")){
					
					fw.write(readHeader());
					
					fw.write("<input type='button' onclick='goPage("+'"'+nowTime+"_log.html?re=Y"+'"'+");' id='StBt' value='3초갱신' /> ");
					fw.write("<input type='button' onclick='goPage("+'"'+nowTime+"_log.html?re=N"+'"'+");' id='EnBt' value='정지' /> <br>");
				}
				
				fw.flush();
				fw.close();
			}
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public static FileLogger getInstance(String logDirectory){
		return new FileLogger(logDirectory);
	}
    
	public void write(String msg){
		FileWriter  fw = null;
		
    	try{
    		fw = new FileWriter(logFile, true);
    		if(osCheck().equals("win")){
    			fw.write("<br>" +logFormat.format(System.currentTimeMillis()) + msg );
    		}else{
    			fw.write(logFormat.format(System.currentTimeMillis()) + msg + "\r");
    		}
    		
    		fw.flush();
    	}
    	catch(IOException ioe){
    		ioe.printStackTrace();
    	}
    	finally{
    		try{
	    		if(fw != null)
	    			fw.close();
    		}
    		catch(IOException ioe){
    			ioe.printStackTrace();
    		}
    	}
    }
	
	public String readHeader() throws IOException{
		BufferedInputStream bis =  (BufferedInputStream)getClass().getClassLoader().getResourceAsStream("egovframework/FileLogger/LogJavascript.txt");
		StringBuffer sb = new StringBuffer();
		byte[] buffer = new byte[4096];
		int readCnt = 0;
		while((readCnt = bis.read(buffer, 0, buffer.length)) != -1){
			if(readCnt == buffer.length)
				sb.append(new String(buffer));
			else {
				sb.append(new String(Arrays.copyOf(buffer, readCnt)));
			}
		}
		return sb.toString();
	}
	
	public String osCheck(){
		String OS = System.getProperty("os.name").toLowerCase();
		
		if(OS.indexOf("win") >= 0)
			return "win";
		else if(OS.indexOf("mac") >= 0)
			return "mac";
		else if(OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0)
			return "unix";
		else if(OS.indexOf("sunos") >= 0)
			return "sunos";
		else return "";
	}
	
}
