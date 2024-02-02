/* 
 *
 * Copyright (C) 1999-2012 IFLYTEK Inc.All Rights Reserved. 
 * 
 * FileName：ApplicationBuyCheckingAction.java
 * 
 * Description：提交前验证Action
 * 
 * History：
 * Version   Author      Date            Operation 
 * 1.0	  jfzhao   2015年1月10日下午2:52:20	       Create	
 */
package weaver.interfaces.hbky.oa.meet.action;

import org.apache.log4j.Logger;

import weaver.interfaces.hbky.oa.meet.service.MeetingService;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import weaver.workflow.request.RequestManager;

/**
 * 会议接口
 * 
 * @author 刘卫
 * 
 * @version 1.0
 * 
 */
public class SPMeetingAction implements Action {
	/**
	 * 日志
	 */
	private static Logger logger = Logger
			.getLogger(SPMeetingAction.class);

	/**
	 * @param requestInfo
	 *            流程对象
	 * @return 是否执行成功
	 */
	public String execute(RequestInfo requestInfo) {
		System.out.println("视屏会议推送开始" );
		RequestManager requestManager = requestInfo.getRequestManager();
		String requestid = "";
		try {
			requestid = requestInfo.getRequestid();
			System.out.println("视屏会议推送开始" + requestid);
			MeetingService meeting = new MeetingService();
			meeting.SySPMeeting(requestid);
		} catch (Exception e) {
			System.out.println("发生问题请求ID：" + requestid);
			e.printStackTrace();
			requestManager.setMessageid("70304");
			requestManager.setMessagecontent(e.getMessage());
			logger.error("会议确认：" + requestid + "问题" + e.getMessage());
			return Action.FAILURE_AND_CONTINUE;
		}		
		return Action.SUCCESS;
	}

}
