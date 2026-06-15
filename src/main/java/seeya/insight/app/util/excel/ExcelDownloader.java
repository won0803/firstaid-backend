package seeya.insight.app.util.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExcelDownloader {
	private final static Logger logger = LoggerFactory.getLogger(ExcelDownloader.class);
	private static ExcelDownloader excelDownloader 	= new ExcelDownloader();
	/* Property(외부) 파일에서 가져오는 값이 SecureCoding에 걸린다면 해당값을 properties 파일에서 가져오지 말고 여기다기 직접 지정해야함. */
	
	private ExcelDownloader(){
	}
	
	public static ExcelDownloader getInstance(){
		return excelDownloader;
	}
	
	public void download(HttpServletResponse response, String[] titleArray, String[] sort, List<Map<String, String>> list,  String excelFileName){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.KOREA);
		OutputStream fileOutput = null;
		
		// 응답 헤더 설정
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + excelFileName + "_" + sdf.format(System.currentTimeMillis()) + ".xls");
		
		// Excel Write 
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Sheet1");
		
		// Font 설정. 
		HSSFFont font = workbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		
		// 제목의 스타일 지정 
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setFont(font);
		
		// 타이틀 내용 생성
		HSSFRow row = sheet.createRow(0);
		for(int i=0; i<titleArray.length; i++){
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(titleArray[i]);
			cell.setCellStyle(titleStyle);
		}
		
		//내용 스타일 지정
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		
		//내용중 가운데 정렬 추가
		HSSFCellStyle styleCenter = workbook.createCellStyle();
		styleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleCenter.setFont(font);
		
		for (int i=0; i<list.size(); i++){
			row = sheet.createRow(i+1);
			Map<String, String> rowData = list.get(i);
			for(int j=0; j<sort.length; j++){
				HSSFCell cell = row.createCell(j);
				cell.setCellValue(rowData.get(sort[j]));
				cell.setCellStyle(styleCenter);
			}
		} 
		
		//엑셀 파일을 만듬
		try{
			fileOutput = response.getOutputStream(); 
			workbook.write(fileOutput);
			fileOutput.close();
		}
		catch(IOException ioe){
			logger.debug("detail", ioe);
		}
	}
}
