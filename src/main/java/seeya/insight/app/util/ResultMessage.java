package seeya.insight.app.util;

public enum ResultMessage {
	SUCCESS("정상적으로 처리 되었습니다.")
	, FAIL("처리에 실패 하였습니다.")
	, ERROR("처리 도중 오류가 발생하였습니다.")
	, LOGIN_ERROR("로그인 정보가 올바르지 않습니다.")
	, LOGIN_PASS_WRONG_CNT("비밀번호가 5회 이상 잘못 입력되어 사용이 정지 되었습니다.")
	, LOGIN_DONT_CONNECT("미접속 150일이 경과하여 계정사용이 정지 되었습니다.")
	, LOGIN_DONT_CHG_PASS("비밀번호가 90일동안 변경되지 않았습니다.")
	, LOGIN_ACCESS_IP_DENIDED("접속 가능한 IP가 아닙니다.")
	, AGENT_CONTOL_WEBSTATE_SAME_URL("이미 등록된 URL입니다.")
	, CONTROL_PROGRAM_SAME_NAME("같은이름의 파일이 존재합니다.")
	, EXCEL_FILE_NOT_EXIST("엑셀파일을 선택 해 주세요.")
	, EXCEL_UPLOAD_PROC_ERROR("엑셀파일을 업로드중 오류가 발생하였습니다.")
	, EXCEL_NOT_SUPPORT_ERROR("올바른 엑셀 파일이 아닙니다.")
	, MEMBER_SAME_lOGIN_ID_URL("이미 등록된 ID입니다.")
	, FILE_ID_NOT_EXIST("파일 ID 값이 존재 하지 않습니다.")
	, FILE_NAME_IS_EXIST("이미 등록된 파일이 존재 합니다.")
	, FILE_INFO_NOT_EXIST("다운로드 하려는 파일에 대한 정보가 없습니다.")
	, FILE_SIZE_OVERFLOW("파일사이즈가 10메가를 초과할 수 없습니다.")
	, FILE_SURFFIX_NOT_SUPPORT("업로드 할 수 없는 파일 확장자 입니다.")
	, URL_SAME_EXIST("이미 등록된 URL입니다.")
	, CONTENT_SAME_EXIST("이미 등록된 콘텐츠 입니다.")
	, CONTENT_SPECIAL_EXIST("확장자에 특수문자를 넣을 수 없습니다.")
	, KEYWORD_EXIST("이미 등록된 키워드 입니다.")
	, PATH_EXIST("중복된 경로가 존재합니다.")
	, UPDATE_NOT_SUPPORT("수정 할 수 없는 데이터 입니다.")
	, DELETE_NOT_SUPPORT("삭제 할 수 없는 데이터가 포함되어 있습니다.")
	, CONNECTION_URL_SYNTAX_ERROR("데이터 요청 URL 연결 형식이 잘못 되었습니다.")
	, CLIENT_PROTOCOL_ERROR("연결 프로토콜에 오류가 발생 하였습니다.")
	, EXIST_PROTECT_DOMAIN_GRP("이미 등록된 보호도메인 그룹 입니다.")
	, EXIST_BYPASS_GRP("이미 등록된 우회 그룹 입니다.")
	, OUT_OF_DATE_5DAY("검색기간이 벗어났습니다.(1개월)")
	, EXIST_CONNECT_IP("이미 등록된 IP 입니다.")
	;
	
	public String msg = "";
	
	private ResultMessage(String msg){
		this.msg = msg;
	}
}
