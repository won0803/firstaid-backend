package seeya.insight.app.util;

public enum CommandMessage {
	KEYWORD_REG("cd /home/ccs/ccs/API;sudo python3.5 /home/ccs/ccs/API/translate.py")
	, AGENT_INIT("cd /home/ccs/ccs/API;sudo python3.5 /home/ccs/ccs/API/agentInitial.py")
	, AGENT_SHUTDOWN("cd /home/ccs/ccs/API;sudo python3.5 /home/ccs/ccs/API/agentShutdown.py")
	, CODE_UPLOAD_REC("cd /home/ccs/ccs/API;sudo python3.5 /home/ccs/ccs/API/RMS_REC.py")
	, CODE_UPLOAD_MAL("cd /home/ccs/ccs/API;sudo python3.5 /home/ccs/ccs/API/RMS_MAL.py")
	, CODE_DELETE_REC("cd /home/ccs/ccs/API;sudo python3.5 /home/ccs/ccs/API/RMS_REC_DEL.py")
	, CODE_DELETE_MAL("cd /home/ccs/ccs/API;sudo python3.5 /home/ccs/ccs/API/RMS_MAL_DEL.py")
	, TARGET_SITE_SINGLE_INPUT("cd /home/ccs/ccs/API;sudo python3.5 /home/ccs/ccs/API/url_text.py")
	, TARGET_SITE_EXCEL_INPUT("cd /home/ccs/ccs/API;sudo python3.5 /home/ccs/ccs/API/url_excel.py")
	;
	
	public String command = "";
	
	private CommandMessage(String command){
		this.command = command;
	}
}
