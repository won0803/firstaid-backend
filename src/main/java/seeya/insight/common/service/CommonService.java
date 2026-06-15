package seeya.insight.common.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import seeya.insight.app.util.Common;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*******************************************************
 * @CLASSENAME  : seeya.insight.common.service
 * @AUTHOR      : 이병덕
 * @SINCE       : 2023.05.03
 * @VERSION     : 
 * @DESCRIPTION : 
 *******************************************************/
public interface CommonService {

    /**
     *
     * 임시폴더로 파일업로드
     * @param request
     * @return
     * @throws Exception
     */
    public String tempFileUpload(HttpServletRequest request) throws Exception;

    /**
     * 파일의 속성(mime-type) 체크
     */
    public boolean checkMimeType(String fileMime, String fileExtension) throws Exception;

    /**
     * 임시폴더 안에 있는 파일 목록 가져오기
     */
    @SuppressWarnings("static-access")
    public List<Map<String, Object>> getFileList(HttpServletRequest request) throws Exception;

    /**
     * 임시폴더 초기화
     */
    public String tempDeleteFolder(Map<String, Object> map) throws Exception;

    /**
     * 임시폴더 안에 있는 파일 중 1개 파일 삭제
     */
    public String tempDeleteFile(Map<String, Object> map) throws Exception;

    /**
     * 다운로드할 파일 정보
     */
    public Map<String, Object> getFileInfo(Map<String, Object> map) throws Exception;

    /**
     * 파일을 저장할 기본 경로
     * @return
     */
    @SuppressWarnings("static-access")
    public String getBaseDir();

    /**
     * 파일존재여부 체크
     * @param filePath
     * @param fileName
     * @return
     * @throws Exception
     */
    public boolean checkFile(String filePath, String fileName) throws Exception;

    /**
     * 파일 다운로드 : 공통
     * @param map
     * @param response
     * @return
     * @throws Exception
     */
    public String downloadFile(Map<String,Object> map , HttpServletResponse response) throws Exception;

    /**
     * 에러페이지 내용
     */
    public Map<String, Object> getErrorPage(String errCode) throws Exception;

    /**
     * 오류 메시지
     */
    public String getErrMessage(String errCode) throws Exception;

    /**
     * 엑셀파일 저장 경로
     */
    @SuppressWarnings("static-access")
    public String getExcelDir();

    /**
     * 로그인용 로그 메시지
     */
    public String getLoginLogMessage(String errCode) throws Exception;

    /**
     * 권한설정값 가져오기
     */
    public Map<String, Object> getPerm(Map<String, Object> map) throws Exception;

    /**
     * URI로 메뉴코드 가져오기
     */
    public String getMenuCode(String menuURL) throws Exception;

    /**
     *
     * 사이드 메뉴 가져오기
     * @param request
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getSideMenu(HttpServletRequest request) throws Exception;

    /**
     * 게시판 설정값 가져오기
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getBoardSetting(String boardCode) throws Exception;

    /**
     * 코드목록 : 각 페이지에서 사용
     */
    public List<Map<String, Object>> getCodeList(String Code) throws Exception;

    /**
     * 페이지 타이틀
     */
    public String getPageTitle(HttpServletRequest request);

    /**
     * 환경설정 가져오기
     */
    public String getConfigureValue(String Code) throws Exception;

    /**
     *
     * 사용자 로그 기록
     * @param map
     * @throws Exception
     */
    public void regLog(Map<String, Object> map) throws Exception;

    /**
     *
     * 코드리스트의 코드그룹명
     * @param code
     * @return
     * @throws Exception
     */
    public String getCodeGroupName(String code) throws Exception;

    /**
     * 특정페이지  권한에따른   MANAGE_YN  특정페이지 관리여부
     * 사용처 예) : 민감데이터 페이지 관리자가 로그인시 관리여부 확인 하여 권한이 있는사람만 민감데이터 알림여부 하기위함
     * @param map
     * @return
     * @throws Exception
     *
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> checkManageYn(Map<String, Object> map) throws Exception;

  /**
   * 격자 영역 사고 빈도 데이터 조회 (추가)
   * @param map (region, startDate, endDate)
   * @return
   * @throws Exception
   */
  List<Map<String, Object>> getGridStatistics(Map<String, Object> map) throws Exception;

  List<Map<String, Object>> getGridDetail(Map<String, Object> map) throws Exception;

  // 기존 코드 아래에 추가
  List<Map<String, Object>> getSymptomStats(Map<String, Object> map) throws Exception;
  List<Map<String, Object>> getLocationStats(Map<String, Object> map) throws Exception;


  // 기존 코드 하단에 추가
  /**
   * 지점 정보 저장
   * @param map
   * @return
   * @throws Exception
   */
  int insertPointData(Map<String, Object> map) throws Exception;

  /**
   * 지점 목록 조회
   * @param map
   * @return
   * @throws Exception
   */
  List<Map<String, Object>> getPointList(Map<String, Object> map) throws Exception;

  /**
   * 지점 상세 통계 정보 조회 (추가)
   */
  Map<String, Object> getPointDetailStats(Map<String, Object> map) throws Exception;

  /**
   * 지점 일별 그래프 데이터 조회 (추가)
   */
  List<Map<String, Object>> getPointDailyStats(Map<String, Object> map) throws Exception;

  /**
   * 지점 삭제 (추가)
   */
  int deletePointData(Map<String, Object> map) throws Exception;
}

