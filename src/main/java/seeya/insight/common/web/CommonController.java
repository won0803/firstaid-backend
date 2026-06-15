package seeya.insight.common.web;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import seeya.insight.app.util.CommandMap;
import seeya.insight.app.util.Common;
import seeya.insight.app.util.excel.ExcelUtil;
import seeya.insight.common.service.CommonService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*******************************************************
 * @CLASSENAME  : seeya.insight.common.web.CommonController
 * @AUTHOR      : 이병덕
 * @SINCE       : 2023.05.03
 * @DESCRIPTION : 시스템 전반에서 공통으로 사용되는 파일 관리, 엑셀 다운로드,
 * 그리고 격자/지점 통계 분석 관련 API를 처리하는 컨트롤러입니다.
 *******************************************************/
@Controller
@Log4j2
public class CommonController {

  @Autowired
  private CommonService commonService;

  @Autowired
  private ExcelUtil excelUtil;


  /**
   * [AJAX] 임시 폴더로 파일 업로드
   * 사용자가 파일을 선택했을 때 서버의 임시 디렉토리에 파일을 저장합니다.
   */
  @RequestMapping(value="/ajax/tempFileUpload.do")
  public ModelAndView tempFileUpload(HttpServletRequest request) throws Exception{
    ModelAndView mv = new ModelAndView("jsonView");
    mv.addObject("errCode" , commonService.tempFileUpload(request));
    return mv;
  }

  /**
   * [AJAX] 파일 목록 가져오기
   * 특정 게시물이나 경로에 첨부된 파일들의 리스트를 조회합니다.
   */
  @RequestMapping(value="/ajax/getFileList.do")
  public ModelAndView getFileList(HttpServletRequest request) throws Exception{
    ModelAndView mv = new ModelAndView("jsonView");
    List<Map<String,Object>> list = commonService.getFileList(request);
    mv.addObject("fileList" , list);
    return mv;
  }

  /**
   * [AJAX] 임시 폴더 삭제
   * 파일 업로드 프로세스 중 취소 시 생성된 임시 폴더 전체를 삭제합니다.
   */
  @RequestMapping(value="/ajax/tempDeleteFolder.do")
  public ModelAndView tempDeleteFolder(CommandMap commandMap) throws Exception{
    ModelAndView mv = new ModelAndView("jsonView");
    mv.addObject("errCode" , commonService.tempDeleteFolder(commandMap.getMap()));
    return mv;
  }

  /**
   * [AJAX] 임시 폴더 내 특정 파일 삭제
   * 임시 업로드된 파일 중 사용자가 제외한 특정 파일만 삭제합니다.
   */
  @RequestMapping(value="/ajax/tempDeleteFile.do")
  public ModelAndView tempDeleteFile(CommandMap commandMap) throws Exception{
    ModelAndView mv = new ModelAndView("jsonView");
    mv.addObject("errCode" , commonService.tempDeleteFile(commandMap.getMap()));
    return mv;
  }

  /**
   * [AJAX] 게시판 첨부파일 존재 여부 체크
   * 다운로드 전 실제 물리 경로에 파일이 존재하는지 검증합니다.
   */
  @RequestMapping(value="/ajax/common/checkAttachment.do")
  public ModelAndView checkAttachment(CommandMap commandMap , HttpServletRequest request) throws Exception {
    ModelAndView mv = new ModelAndView("jsonView");
    boolean error = false;
    String strm = (String) commandMap.getMap().get("strm");

    Map<String, Object> map = null;

    // 허용된 게시판 구분값 확인
    if ("Board".equals(strm) || "ProofForm".equals(strm) || "ProofFile".equals(strm) || "AForm".equals(strm)) {
      map = commonService.getFileInfo(commandMap.getMap());
    } else {
      error = true;
    }

    if (map == null) error = true;

    if(!error) {
      String FileName = (String) map.get("FILE_NM");
      String FileDir  = (String) map.get("FILE_DIR");
      String baseDir  = (String) commonService.getBaseDir();

      FileDir = baseDir + FileDir;

      // 물리 파일 존재 여부 최종 확인
      if (commonService.checkFile(FileDir, FileName)) {
        mv.addObject("errCode" , "SUCCESS");
      }else{
        mv.addObject("errCode" , "FAIL");
      }
    } else {
      mv.addObject("errCode" , "FAIL");
    }

    return mv;
  }

  /**
   * [다운로드] 일반 첨부파일 다운로드
   * DB에서 파일 정보를 조회하여 실제 파일을 브라우저로 전송합니다.
   */
  @RequestMapping(value="/common/goDownloadFile.do")
  public ModelAndView downloadFile(CommandMap commandMap , HttpServletResponse response , HttpServletRequest request) throws Exception{

    String result = "";
    Map<String,Object> map = commonService.getFileInfo(commandMap.getMap());

    String FileName         = (String) map.get("FILE_NM");
    String originalFileName = (String) map.get("ORIGINAL_NAME");
    String FileDir          = (String) map.get("FILE_DIR");
    String baseDir          = (String) commonService.getBaseDir();

    FileDir = baseDir + FileDir;

    Map<String, Object> downloadMap = new HashMap<String, Object>();
    downloadMap.put("FileName"         , FileName);
    downloadMap.put("originalFileName" , originalFileName);
    downloadMap.put("FileDir"          , FileDir);

    result = commonService.downloadFile(downloadMap , response);

    if (result.equals("SUCCESS")) {
      return null;
    }else{
      ModelAndView mv = new ModelAndView("/err_page");
      mv.addObject("err", commonService.getErrorPage("E782"));
      return mv;
    }
  }

  /**
   * [다운로드] 파일명과 경로를 직접 지정하여 다운로드
   * 별도의 정보 조회 없이 전달된 경로 정보를 바탕으로 즉시 다운로드합니다.
   */
  @RequestMapping(value="/common/goDownloadNameDir.do")
  public ModelAndView goDownloadNameDir(CommandMap commandMap , HttpServletResponse response , HttpServletRequest request) throws Exception{

    String result = "";
    String FileName         = (String) commandMap.getMap().get("fileName");
    String FileDir          = (String) commandMap.getMap().get("fileDir");
    String baseDir          = (String) commonService.getBaseDir();

    FileDir = baseDir + FileDir;

    Map<String, Object> downloadMap = new HashMap<String, Object>();
    downloadMap.put("FileName"         , FileName);
    downloadMap.put("originalFileName" , FileName);
    downloadMap.put("FileDir"          , FileDir);

    result = commonService.downloadFile(downloadMap , response);

    if (result.equals("SUCCESS")) {
      return null;
    }else{
      ModelAndView mv = new ModelAndView("/err_page");
      mv.addObject("err", commonService.getErrorPage("E782"));
      return mv;
    }
  }

  /**
   * [다운로드] 생성된 엑셀 파일 다운로드
   * 서버에 생성된 엑셀 파일을 읽어 byte 배열로 변환 후 전송합니다.
   */
  @RequestMapping(value="/common/downloadExcelFile.do")
  public void downloadExcelFile(CommandMap commandMap , HttpServletResponse response , HttpServletRequest request) throws Exception{

    String FileName         = (String) commandMap.getMap().get("excelName");
    String originalFileName = (String) commandMap.getMap().get("category") + "_" + FileName;
    String FileDir          = (String) commonService.getExcelDir();

    byte fileByte[] = FileUtils.readFileToByteArray(new File(FileDir + FileName));

    response.setContentType("application/octet-stream");
    response.setContentLength(fileByte.length);

    response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(originalFileName,"UTF-8")+"\";");
    response.setHeader("Content-Transfer-Encoding", "binary");
    response.getOutputStream().write(fileByte);

    response.getOutputStream().flush();
    response.getOutputStream().close();
  }

  /**
   * [격자 분석] 지도 격자 영역별 사고 빈도 조회
   * 선택된 지역 및 기간 내의 격자별 출동 횟수 데이터를 가져옵니다.
   */
  @RequestMapping(value="/ajax/getGridData.do")
  public ModelAndView getGridData(CommandMap commandMap) throws Exception {
    ModelAndView mv = new ModelAndView("jsonView");
    List<Map<String, Object>> list = commonService.getGridStatistics(commandMap.getMap());
    mv.addObject("gridList", list);
    return mv;
  }

  /**
   * [격자 상세] 특정 격자 클릭 시 증상별/장소별 통계 조회
   * 선택된 격자 영역 내의 사고 원인(증상) 및 발생 장소에 대한 상세 분석 데이터를 조회합니다.
   */
  @RequestMapping(value="/ajax/getGridDetail.do")
  public ModelAndView getGridDetail(CommandMap commandMap) throws Exception {
    ModelAndView mv = new ModelAndView("jsonView");
    List<Map<String, Object>> symptomList = commonService.getSymptomStats(commandMap.getMap());
    List<Map<String, Object>> locationList = commonService.getLocationStats(commandMap.getMap());

    mv.addObject("symptomList", symptomList);
    mv.addObject("locationList", locationList);
    return mv;
  }

  /**
   * [지점 등록] 분석을 위한 검증 지점 추가
   * 사용자가 지도에서 지정한 위치와 반경 정보를 start_point_lwc 테이블에 저장합니다.
   */
  @RequestMapping(value="/ajax/savePoint.do")
  public ModelAndView savePoint(CommandMap commandMap) throws Exception {
    ModelAndView mv = new ModelAndView("jsonView");
    int resultCount = commonService.insertPointData(commandMap.getMap());

    if(resultCount > 0) {
      mv.addObject("result", "SUCCESS");
    } else {
      mv.addObject("result", "FAIL");
    }
    return mv;
  }

  /**
   * [지점 목록] 저장된 검증 지점 리스트 조회
   * 지도에 마커로 표시할 지점 목록(이름, 좌표, 반경 등)을 불러옵니다.
   */
  @RequestMapping(value="/ajax/getPointList.do")
  public ModelAndView getPointList(CommandMap commandMap) throws Exception {
    ModelAndView mv = new ModelAndView("jsonView");
    List<Map<String, Object>> list = commonService.getPointList(commandMap.getMap());
    mv.addObject("pointList", list);
    return mv;
  }

  /**
   * [지점 상세] 특정 지점의 출동 시간/거리 통계 조회
   * 지점 클릭 시 해당 반경 내의 출동 횟수, 평균/최소/최대 출동 시간 및 거리를 산출합니다.
   */
  @RequestMapping(value = "/ajax/getPointDetailStats.do")
  @ResponseBody
  public Map<String, Object> getPointDetailStats(@RequestParam Map<String, Object> params) throws Exception {
    return commonService.getPointDetailStats(params);
  }

  /**
   * [지점 차트] 지점별 일별 평균 통계 조회
   * 지점 상세 분석 창에 들어가는 일별 평균 출동 시간/거리 추이 차트 데이터를 조회합니다.
   */
  @RequestMapping(value = "/ajax/getPointDailyStats.do")
  @ResponseBody
  public List<Map<String, Object>> getPointDailyStats(@RequestParam Map<String, Object> params) throws Exception {
    return commonService.getPointDailyStats(params);
  }

  /**
   * [지점 삭제] 저장된 검증 지점 삭제
   * 사용자가 등록한 지점 정보를 DB에서 삭제합니다.
   */
  @RequestMapping(value = "/ajax/deletePoint.do")
  @ResponseBody
  public int deletePoint(@RequestParam Map<String, Object> params) throws Exception {
    return commonService.deletePointData(params);
  }

}