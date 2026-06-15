package seeya.insight.app.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.PngImage;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PDFDownloader {
	private final static Logger logger = LoggerFactory.getLogger(PDFDownloader.class);
	private static PDFDownloader pdfDownloader 	= new PDFDownloader();
	
	private PDFDownloader(){
	}
	
	public static PDFDownloader getInstance(){
		return pdfDownloader;
	}
	
	public void download(HttpServletRequest request, HttpServletResponse response, String strFileName){
		try{
			String fileName = "";
			String data = request.getParameter("screenData"); 
			String width = request.getParameter("screenWidth"); 
			String height = request.getParameter("screenHeight");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.KOREA);
			
		    data = data.replaceAll("data:image/png;base64,", ""); 
		    byte[] file = Base64.decodeBase64(data.getBytes()); 
		    ByteArrayInputStream is = new ByteArrayInputStream(file);
		    fileName = strFileName + "_" +sdf.format(System.currentTimeMillis())+".pdf";
		        
        	response.setContentType("application/octet-stream");
	        if(!fileName.isEmpty()&&fileName!=null){
	        	response.setHeader("Content-Disposition", "attachment; filename="+fileName);
	        }else{
	        	response.setHeader("Content-Disposition", "attachment; filename=screen_" + sdf.format(System.currentTimeMillis()) + ".pdf");
	        }

	        //Create Document Object
		    Document screenPDF=new Document();

		    //Create PdfWriter for Document to hold physical file
		    //Change the PDF file path to suit to your needs
		    PdfWriter.getInstance(screenPDF, response.getOutputStream());
	        Rectangle rect = new Rectangle(Integer.parseInt(width),Integer.parseInt(height));
	        rect.setBackgroundColor(new BaseColor(0, 0, 0));
	        screenPDF.setPageSize(rect);
	        screenPDF.setMargins(0, 0, 0, 0);
	        screenPDF.open();
	        
		    //Get the PNG image to Convert to PDF
		    //getImage of PngImage class is a static method
		    //Edit the file location to suit to your needs
		    Image screenShot=PngImage.getImage(is);
		    //Add image to Document
		    /*convertPngToPdf.add(convertBmp1);*/
		    screenPDF.add(screenShot);
		    //Close Document
		    screenPDF.close();
		    logger.info("Converted and stamped PNG Image in a PDF Document Using iText and Java");
	        
	        response.flushBuffer(); 
	    }catch(IOException e) { 
	        logger.debug("detail", e);
	    }catch(DocumentException de){
	    	logger.debug("detail", de);
	    }
	}
}
