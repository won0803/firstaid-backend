package seeya.insight.app.util;

import org.springframework.web.multipart.MultipartFile;
import seeya.insight.dto.common.ResultDTO;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUploader {
	private static FileUploader fileUploader 	= new FileUploader();
	private MultipartFile uploadFile 	= null;
	private String realSavePath 		= null;
	private String realSaveFileName 	= null;
	private File saveDir 				= null;
	private final long MAX_SIZE			= 10 * 1024 * 1024;				// 10MB
	private ResultDTO result			= new ResultDTO();
	
	private FileUploader(){
	}
	
	public static FileUploader getInstance(){
		return fileUploader;
	}
	
	public ResultDTO upload(){
		if(uploadFile==null || uploadFile.isEmpty()){
            result.setRes(ResultCode.FAIL.code);
            result.setMsg(ResultMessage.EXCEL_FILE_NOT_EXIST.msg);
            return result;
        }
		saveDir = new File(realSavePath);
		File destFile = new File(realSavePath + File.separator + realSaveFileName);		
		File[] fileList = saveDir.listFiles();
		
		// 저장하려는 파일과 같은 파일이 존재하는지 체크한다.
		for(File file : fileList){
			if(realSaveFileName.equals(file.getName())){
				result.setRes(ResultCode.FAIL.code);
	            result.setMsg(ResultMessage.CONTROL_PROGRAM_SAME_NAME.msg);
	            return result;
			}
		}
		
		try{
			uploadFile.transferTo(destFile);
			
			result.setRes(ResultCode.SUCCESS.code);
			result.setMsg(ResultMessage.SUCCESS.msg);
            return result;
        }catch(IllegalStateException | IOException e){
            result.setRes(ResultCode.FAIL.code);
            result.setMsg(ResultMessage.EXCEL_UPLOAD_PROC_ERROR.msg);
            return result;
        }catch(Exception e){
        	result.setRes(ResultCode.FAIL.code);
        	result.setMsg(ResultMessage.EXCEL_UPLOAD_PROC_ERROR.msg);
        	return result;
        }
	}
	
	public ResultDTO setMultipartFile(MultipartFile uploadFile){
		// 여기서 업로드 파일 사이즈 및 파일 확장자를 체크 한다.
		long size = uploadFile.getSize();
		String fileName = uploadFile.getOriginalFilename().toLowerCase();
		
		if(size > MAX_SIZE){
			result.setRes(ResultCode.FAIL.code);
            result.setMsg(ResultMessage.FILE_SIZE_OVERFLOW.msg);
            return result;
		}
		
		if(!(fileName.toLowerCase().endsWith("doc") || fileName.toLowerCase().endsWith("hwp") || fileName.toLowerCase().endsWith("pdf")
			|| fileName.toLowerCase().endsWith("xls") || fileName.toLowerCase().endsWith("zip") || fileName.toLowerCase().endsWith("xlsx"))){
			result.setRes(ResultCode.FAIL.code);
            result.setMsg(ResultMessage.FILE_SURFFIX_NOT_SUPPORT.msg);
            return result;
		}
			
		this.uploadFile = uploadFile;
		
		result.setRes(ResultCode.SUCCESS.code);
		result.setMsg(ResultMessage.SUCCESS.msg);
		return result;
	}
	
	public void setRealSavePath(String realSavePath){
		this.realSavePath = realSavePath;
	}
	
	public void setRealSaveFileName(String realSaveFileName){
		this.realSaveFileName = realSaveFileName;
	}
	
	public String getUuid(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
