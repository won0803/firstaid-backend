package seeya.insight.app.util.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @className : ExcelFileType
 * @description : 엑셀파일을 읽어 Workbook 객체에 리턴
 * 
 * @author 정보보호 이병덕
 * @since 2017.10.24
 * @version 1.0
 * @see
 * 
 * - Copyright (C) by SEEYA ALL right reserved.
 */
public class ExcelFileType {
    /**
     * 엑셀파일(.xls, .xlsx을 읽어 Workbook 객체를 리턴한다.
     * @param filePath
     * @return
     */
    public static Workbook getWorkbook(String filePath) {
        /*
        FileInputStream은 파일의 경로에 있는 파일을 읽어 Byte로 가져온다.
        파일이 존재하지 않으면 RuntimeException 발생.
         */
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage() , e);
        }

        Workbook wb = null;

        /*
        파일의 확장자를 체크해서 xls이며 HSSFWorkbook에, xlsx이면 XSSFWorkbook에 각각 초기화 한다.
         */
        if (filePath.toUpperCase().endsWith(".XLS")) {
            try {
                wb = new HSSFWorkbook(fis);
            }catch (IOException e) {
                throw new RuntimeException(e.getMessage() , e);
            }
        }else if (filePath.toUpperCase().endsWith(".XLSX")) {
            try {
                wb = new XSSFWorkbook(fis);
            }catch (IOException e) {
                throw new RuntimeException(e.getMessage() , e);
            }
        }

        return wb;
    }
}
