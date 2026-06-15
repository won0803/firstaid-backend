package seeya.insight.dto.common;

import lombok.Data;

@Data
public class ResultDTO {
	private String res 				= ""; 			// 처리 결과
	private String msg				= "";			// 결과 메시지
	private String url				= "";			// 이동 페이지
	private Object data				= null;			// 리턴될 결과 데이터
	
}
