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

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.hbky.oa.meet.service.MeetingService;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.Property;
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
public class OAMeetingAction implements Action {
	/**
	 * 日志
	 */
	private static Logger logger = Logger.getLogger(OAMeetingAction.class);

	/**
	 * @param requestInfo
	 *            流程对象
	 * @return 是否执行成功
	 */
	public String execute(RequestInfo requestInfo) {
		System.out.println("OA参会人调整");
		RequestManager requestManager = requestInfo.getRequestManager();
		String requestid = "";
		try {
			requestid = requestInfo.getRequestid();
			System.out.println("OA参会人调整" + requestid);
			String chr = "";
			// 取主表数据
			Property[] properties = requestInfo.getMainTableInfo()
					.getProperty();// 获取表单主字段信息
			for (int i = 0; i < properties.length; i++) {
				String name = properties[i].getName().toLowerCase();// 主字段名称
				String value = Util.null2String(properties[i].getValue());// 主字段对应的值
				// System.out.println(name + " " + value);

				if ("hrmmembers".equals(name)) {
					chr = value;
				}
			}
			RecordSet rs = new RecordSet();

			// 插入会议参会人
			String sql = " update meeting set  hrmmembers= case when nvl(to_char(hrmmembers),'')='' then (select  case when nvl(dbms_lob.substr(hrmmembers),'')='' then '' else dbms_lob.substr(hrmmembers) end     chr  from formtable_main_42 where requestid='"
					+ requestid
					+ "' "
					+ " ) else to_char(hrmmembers)||(select  case when nvl(dbms_lob.substr(hrmmembers),'')='' then '' else ','||dbms_lob.substr(hrmmembers) end     chr  from formtable_main_42 where requestid='"
					+ requestid
					+ "' "
					+ ") end  where requestid in (select mainrequestid from workflow_subwfrequest where subrequestid='"
					+ requestid + "')";
			rs.execute(sql);

			String sqlhy = " select id from meeting where requestid in (select mainrequestid from workflow_subwfrequest where subrequestid='"
					+ requestid + "') ";
			rs.execute(sqlhy);
			String meetingid = "0";
			if (rs.next()) {
				meetingid = rs.getString("id");
			}

			for (String chry : chr.split(",")) {

				//插入参会人
				String sxr = "select count(id) count from  meeting_member2 where meetingid='"
						+ meetingid
						+ "' and membertype=1  and memberid='"
						+ chry + "'";
				rs.execute(sxr);
				if (rs.next()) {
					if ("0".equals(rs.getString("count"))) {
						//不存在插入
						String insql="insert into meeting_member2 (meetingid,membertype,memberid,membermanager) values ('"+meetingid+"',1,'"
						+ chry + "','"
						+ chry + "')";
						rs.execute(insql);

					}
				}
				
				//插入签到记录
				String sxrqd = "select count(*) cout from meeting_sign where meetingid='"
						+ meetingid
						+ "'  and userid='"
						+ chry + "'";
				rs.execute(sxrqd);
				if (rs.next()) {
					if ("0".equals(rs.getString("cout"))) {
						//不存在插入
						String insql="insert into meeting_sign (meetingid,attendtype,userid,flag) values('"
						+ meetingid
						+ "',1,'"
						+ chry + "',0)";
						rs.execute(insql);

					}
				}
				

			}

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
