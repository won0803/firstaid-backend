package seeya.insight.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileDownloader {
	private static final Logger logger = LoggerFactory.getLogger(FileDownloader.class);
	private static FileDownloader fileDownloader 	= new FileDownloader();
	private File downloadFile						= null;
	private FileInputStream fis						= null;
	private OutputStream os							= null;
	private byte[] buffer 							= new byte[4096];
	
	private FileDownloader(){
	}
	
	public static FileDownloader getInstance(){
		return fileDownloader;
	}
	
	public void download(HttpServletResponse response, String saveDir, String saveFileName, String oriFileName){
		int readSize = 0;
		
		if(saveDir == null || saveFileName == null || oriFileName == null || saveDir.isEmpty() || saveFileName.isEmpty() || oriFileName.isEmpty()){
            // throw new SeeyaCustomException(ResultMessage.FILE_INFO_NOT_EXIST.msg, "/");
			// throw new Exception(ResultMessage.FILE_INFO_NOT_EXIST.msg);
			return;
        }
		// 응답 헤더 설정
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + oriFileName);
		
		downloadFile = new File(saveDir + File.separator + saveFileName);
		
		try{
			fis = new FileInputStream(downloadFile);
			os = response.getOutputStream();
			
			while((readSize = fis.read(buffer, 0, buffer.length)) != -1){
				os.write(buffer, 0, readSize);
			}
			
			os.flush();
		}catch(IOException ioe){
			logger.debug("detail", ioe);
		}
		finally{
			try{
				if(os != null)
					os.close();
				if(fis != null)
					fis.close();
			}catch(IOException ioe2){
				logger.debug("detail", ioe2);
			}
		}
	}
	
	public void imageDownload(HttpServletResponse response, String imagePath, String imageName){
		int readSize = 0;
		String ext = "";
		File imageFile = null;
		
		if(imagePath == null || imageName == null || imagePath.isEmpty() || imageName.isEmpty()){
			// throw new SeeyaCustomException(ResultMessage.FILE_INFO_NOT_EXIST.msg, "/");
			return;
		}
		// 응답 헤더 설정
		ext = imageName.substring(imageName.lastIndexOf(".") + 1).toLowerCase();
		if("jpg".equals(ext) || "jpeg".equals(ext)){
			response.setContentType("image/jpeg");
		}else if("png".equals(ext)){
			response.setContentType("image/png");
		}else if("gif".equals(ext)){
			response.setContentType("image/gif");
		}else if("bmp".equals(ext)){
			response.setContentType("image/bmp");
		}
		
		imageFile = new File(imagePath + File.separator + imageName);
		
		logger.debug("##### 이미지 파일 경로 : " + imageFile.getAbsolutePath());
		logger.debug("##### 파일 존재여부 : " + imageFile.exists());
		
		try{
			fis = new FileInputStream(imageFile);
			os = response.getOutputStream();
			
			while((readSize = fis.read(buffer, 0, buffer.length)) != -1){
				os.write(buffer, 0, readSize);
			}
			
			os.flush();
		}catch(IOException ioe){
			logger.debug("detail", ioe);
		}
		finally{
			try{
				if(os != null)
					os.close();
				if(fis != null)
					fis.close();
			}catch(IOException ioe2){
				logger.debug("detail", ioe2);
			}
		}
	}
}
