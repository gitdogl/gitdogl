//package weaver.workflow.request;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//import org.apache.commons.lang.StringUtils;
//
//import weaver.conn.ConnStatement;
//import weaver.conn.RecordSet;
//import weaver.docs.docs.DocViewer;
//import weaver.general.TimeUtil;
//import weaver.general.Util;
//import weaver.hrm.User;
//import weaver.hrm.resource.ResourceComInfo;
//import weaver.interfaces.erp.PostWorkflowInfo;
//import weaver.workflow.msg.PoppupRemindInfoUtil;
//import weaver.workflow.ruleDesign.RuleInterface;
//import weaver.workflow.workflow.WorkflowVersion;
//
///**
// * @author HuangGuanGuan
// * 获取关注者信息
// */
//public class wfAgentCondition {
//
//
//	/**
//	 * 查询流程待处理开关【根据明细去查询】
//	 *
//	 * @param agentid
//	 * @param agenttype
//	 * @return
//	 */
//	public String isProxyDeal(String agentid,String agenttype){
//		RecordSet rs = new RecordSet();
//		String retustr="0";
//		rs.executeSql(" select * from workflow_agentConditionSet where  agenttype='"+agenttype+"'  and  agentid='"+agentid+"' and isProxyDeal='1' ");
//		if(rs.next()){
//			retustr="1";
//		}
//		return retustr;
//	}
//
//
//	/**
//	 * 代理已结束时，编辑时使用
//	 * @param beginDate
//	 * @param beginTime
//	 * @param endDate
//	 * @param endTime
//	 * @param agentid
//	 * @param overlapagentstrid
//	 * @param beagenterId
//	 * @param workflowid
//	 * @param user
//	 */
//	public void SetUpdateagent(String beginDate,String beginTime,String endDate,String endTime,String agentid ,String overlapagentstrid,String beagenterId,String workflowid,User user) {
//		RecordSet rs = new RecordSet();
//		RecordSet rs4 = new RecordSet();
//		RecordSet rs1 = new RecordSet();
//		String iseditstartdate="0";
//	    String iseditstarttime="0";
//	    String iseditenddate="0";
//	    String iseditendtime="0";
//	    overlapagentstrid = "'"+StringUtils.replace(overlapagentstrid, ",", "','")+"'";
//	    if(beginDate.equals("")){
//	    	beginDate="1900-01-01";
//	    	iseditstartdate="1";
//	    }
//	    if(beginTime.equals("")){
//	    	beginTime="00:00";
//	    	iseditstarttime="1";
//	    }
//	    if(endDate.equals("")){
//	    	endDate="2099-12-31";
//	    	iseditenddate="1";
//	    }
//	    if(endTime.equals("")){
//	    	endTime="23:59";
//	    	iseditendtime="1";
//	    }
//
//		String agenttypes=this.getAgentTypeNew(beginDate, beginTime, endDate, endTime);
//
//
//		if(agenttypes.equals("1")){//代理中
//				rs4.executeSql("select workflowid,agentid,bagentuid from workflow_agentConditionSet where agentid in("+overlapagentstrid+") and agenttype='1' ");
//	    		 while(rs4.next()){
//	    			 String workflowidold=Util.null2String(rs4.getString("workflowid"));
//	    			 String agentidold=Util.null2String(rs4.getString("agentid"));
//	    		 	 String bagentuidold=Util.null2String(rs4.getString("bagentuid"));
//	    		 	 this.Agent_to_recover(bagentuidold,workflowidold,agentidold,"editAgent_over","");//收回代理
//	    		 }
//
//	    		rs.executeSql("update workflow_agent set agenttype='1' ,isSet='',beginDate='"+beginDate+"' ,beginTime='"+beginTime+"' ,endDate='"+endDate+"',endTime='"+endTime+"',iseditstartdate='"+iseditstartdate+"',iseditstarttime='"+iseditstarttime+"',iseditenddate='"+iseditenddate+"',iseditendtime='"+iseditendtime+"' where  agentid='"+agentid+"'");
//	 			rs.executeSql("UPDATE workflow_agentConditionSet set agenttype='1'  ,isset='' ,begindate='"+"+beginDate+"+"',begintime='"+beginTime+"',endDate='"+endDate+"',endTime='"+endTime+"' where agentid='"+agentid+"'");
//
//	 			this.again_agent_wf(""+beagenterId,workflowid,beginDate,beginTime,endDate,endTime,user,"",""+agentid);
//
//		}else if(agenttypes.equals("2")){//已结束
//			rs.executeSql("update workflow_agent set  beginDate='"+beginDate+"' ,beginTime='"+beginTime+"' ,endDate='"+endDate+"',endTime='"+endTime+"',iseditstartdate='"+iseditstartdate+"',iseditstarttime='"+iseditstarttime+"',iseditenddate='"+iseditenddate+"',iseditendtime='"+iseditendtime+"' where  agentid='"+agentid+"'");
//			rs.executeSql("UPDATE workflow_agentConditionSet set  begindate='"+"+beginDate+"+"',begintime='"+beginTime+"',endDate='"+endDate+"',endTime='"+endTime+"' where agentid='"+agentid+"'");
//		}else if(agenttypes.equals("3")){//未开始
//
//		 rs4.executeSql("select workflowid,agentid,bagentuid from workflow_agentConditionSet where agentid in("+overlapagentstrid+") and agenttype='1' ");
//   		 while(rs4.next()){
//   			 String workflowidold=Util.null2String(rs4.getString("workflowid"));
//   			 String agentidold=Util.null2String(rs4.getString("agentid"));
//   		 	 String bagentuidold=Util.null2String(rs4.getString("bagentuid"));
//   		 	 this.Agent_to_recover(bagentuidold,workflowidold,agentidold,"editAgent_over","");//收回代理
//   		 }
//			rs.executeSql("update workflow_agent set agenttype='1' ,isSet='',beginDate='"+beginDate+"' ,beginTime='"+beginTime+"' ,endDate='"+endDate+"',endTime='"+endTime+"',iseditstartdate='"+iseditstartdate+"',iseditstarttime='"+iseditstarttime+"',iseditenddate='"+iseditenddate+"',iseditendtime='"+iseditendtime+"' where  agentid='"+agentid+"'");
//			rs.executeSql("UPDATE workflow_agentConditionSet set agenttype='1'  ,isset='' ,begindate='"+"+beginDate+"+"',begintime='"+beginTime+"',endDate='"+endDate+"',endTime='"+endTime+"' where agentid='"+agentid+"'");
//		}
//	}
//
//
//	 /**
//	 * 检查当前代理是否已经开始代理了
//	 *
//	 * @param agenttype
//	 * @return 1 代理中 2 已结束 3 未开始
//	 */
//public String getAgentTypeNew(String beginDate,String beginTime,String endDate,String endTime) {
//			String restr = "";
//			String currentDate = TimeUtil.getCurrentDateString();
//			String currentTime = (TimeUtil.getCurrentTimeString()).substring(11, 19);
//			if (beginDate.equals("") && endDate.equals("")) {
//				restr = "1"; // 代理中
//			} else if (beginDate.equals("") && !endDate.equals("")) {
//				if (endTime.equals(""))
//					endTime = "23:59";
//				// 结束时间比当前时间晚
//				if (StringToDate(endDate + " " + endTime).after(
//						StringToDate(currentDate + " " + currentTime))) {
//					restr = "1"; // 代理中
//				} else {
//					restr = "2"; // 已结束
//				}
//
//			} else if (!beginDate.equals("") && endDate.equals("")) {
//				if (beginTime.equals(""))
//					beginTime = "00:00";
//				// 开始时间比当前时间早
//				if (StringToDate(beginDate + " " + beginTime).before(
//						StringToDate(currentDate + " " + currentTime))) {
//					restr = "1"; // 代理中
//				} else {
//					restr = "2";// 已结束
//				}
//			} else if (!beginDate.equals("") && !endDate.equals("")) {
//				if (beginTime.equals("")) {
//					beginTime = "00:00";
//				}
//				if (endTime.equals("")) {
//					endTime = "23:59";
//				}
//				if (StringToDate(beginDate + " " + beginTime).before(
//						StringToDate(currentDate + " " + currentTime))
//						&& StringToDate(endDate + " " + endTime).after(
//								StringToDate(currentDate + " " + currentTime))) {
//					restr = "1"; // 代理中
//				} else if (StringToDate(beginDate + " " + beginTime).after(
//						StringToDate(currentDate + " " + currentTime))) {
//					restr = "3"; // 未开始
//				} else if (StringToDate(endDate + " " + endTime).before(
//						StringToDate(currentDate + " " + currentTime))) {
//					restr = "2"; // 已结束
//				}
//			}
//
//	return restr;
//}
//
//	/**
//	 * 流程已结束修改
//	 * @param agentid
//	 * @param beginDate
//	 * @param beginTime
//	 * @param endDate
//	 * @param endTime
//	 * @param beagenterId
//	 * @param workflowid
//	 * @param user
//	 */
//	public void SetWorkflowAgent(String agentid,String beginDate,String beginTime,String endDate,String endTime,String beagenterId,String workflowid,User user ){
//		RecordSet rs = new RecordSet();
//		String iseditstartdate="0";
//	    String iseditstarttime="0";
//	    String iseditenddate="0";
//	    String iseditendtime="0";
//	    if(beginDate.equals("")){
//	    	beginDate="1900-01-01";
//	    	iseditstartdate="1";
//	    }
//	    if(beginTime.equals("")){
//	    	beginTime="00:00";
//	    	iseditstarttime="1";
//	    }
//	    if(endDate.equals("")){
//	    	endDate="2099-12-31";
//	    	iseditenddate="1";
//	    }
//	    if(endTime.equals("")){
//	    	endTime="23:59";
//	    	iseditendtime="1";
//	    }
//
//		String agenttypes=this.getAgentTypeNew(beginDate, beginTime, endDate, endTime);
//		if(agenttypes.equals("1")){//代理中
//
//			rs.executeSql("update workflow_agent set agenttype='1' ,isSet='',beginDate='"+beginDate+"' ,beginTime='"+beginTime+"' ,endDate='"+endDate+"',endTime='"+endTime+"',iseditstartdate='"+iseditstartdate+"',iseditstarttime='"+iseditstarttime+"',iseditenddate='"+iseditenddate+"',iseditendtime='"+iseditendtime+"' where  agentid='"+agentid+"'");
//			rs.executeSql("UPDATE workflow_agentConditionSet set agenttype='1'  ,isset='' ,begindate='"+beginDate+"',begintime='"+beginTime+"',endDate='"+endDate+"',endTime='"+endTime+"' where agentid='"+agentid+"'");
//			this.again_agent_wf(""+beagenterId,""+workflowid,beginDate,beginTime,endDate,endTime,user,"agentconditonset",""+agentid);
//
//		}else if(agenttypes.equals("2")){//已结束
//
//			rs.executeSql("update workflow_agent set  beginDate='"+beginDate+"' ,beginTime='"+beginTime+"' ,endDate='"+endDate+"',endTime='"+endTime+"',iseditstartdate='"+iseditstartdate+"',iseditstarttime='"+iseditstarttime+"',iseditenddate='"+iseditenddate+"',iseditendtime='"+iseditendtime+"' where  agentid='"+agentid+"'");
//			rs.executeSql("UPDATE workflow_agentConditionSet set  begindate='"+beginDate+"',begintime='"+beginTime+"',endDate='"+endDate+"',endTime='"+endTime+"' where agentid='"+agentid+"'");
//
//		}else if(agenttypes.equals("3")){//未开始
//
//			rs.executeSql("update workflow_agent set agenttype='1' ,isSet='',beginDate='"+beginDate+"' ,beginTime='"+beginTime+"' ,endDate='"+endDate+"',endTime='"+endTime+"',iseditstartdate='"+iseditstartdate+"',iseditstarttime='"+iseditstarttime+"',iseditenddate='"+iseditenddate+"',iseditendtime='"+iseditendtime+"' where  agentid='"+agentid+"'");
//			rs.executeSql("UPDATE workflow_agentConditionSet set agenttype='1'  ,isset='' ,begindate='"+beginDate+"',begintime='"+beginTime+"',endDate='"+endDate+"',endTime='"+endTime+"' where agentid='"+agentid+"'");
//		}else{
//			rs.executeSql("update workflow_agent set  beginDate='"+beginDate+"' ,beginTime='"+beginTime+"' ,endDate='"+endDate+"',endTime='"+endTime+"',iseditstartdate='"+iseditstartdate+"',iseditstarttime='"+iseditstarttime+"',iseditenddate='"+iseditenddate+"',iseditendtime='"+iseditendtime+"' where  agentid='"+agentid+"'");
//			rs.executeSql("UPDATE workflow_agentConditionSet set  begindate='"+beginDate+"',begintime='"+beginTime+"',endDate='"+endDate+"',endTime='"+endTime+"' where agentid='"+agentid+"'");
//		}
//
//	}
//
//
//	/**
//	 * 收回代理状态
//	 * @param bagentuid
//	 * @param agentuid
//	 */
//	public void SetbackAgent(String bagentuid,String agentuid){
//		RecordSet rs = new RecordSet();
//		RecordSet rs1= new RecordSet();
//		RecordSet rs2 = new RecordSet();
//		String currentDate=TimeUtil.getCurrentDateString();
//		String currentTime=(TimeUtil.getCurrentTimeString()).substring(11,19);
//		rs.executeSql("select * from workflow_agentConditionSet where agenttype = '1' and  bagentuid='"+bagentuid +"'"+" and agentuid ='"+agentuid+"'");
//		String agentids="";
//		while(rs.next()){
//			rs1.executeSql("update workflow_agentConditionSet set agenttype = '0' where id = '"+rs.getString("id")+"'");//收回明细
//			agentids=Util.null2String(rs.getString("agentid"));
//			String sql="select 1 from workflow_agentConditionSet where agenttype = '1' and  agentid='"+agentids+"' and agentuid!='"+agentuid+"'" ;
//			rs2.executeSql(sql);
//			if(!rs2.next()){
//				rs1.executeSql("update workflow_agent set agenttype = '0',backDate='"+currentDate+"',backTime='"+currentTime+"' where agentid='"+agentids+"'");
//			}
//		}
//	}
//
//	public String getAlloperator(Map operatorsht, Map map) {
//	    String alloperators = "";
//	    Iterator iterator = map.keySet().iterator();
//        while(iterator.hasNext()) {
//            String operatorgroup = (String) iterator.next();
//            ArrayList operators = (ArrayList) operatorsht.get(operatorgroup);
//            for (int i = 0; i < operators.size(); i++) {
//                String operatorandtype = (String) operators.get(i);
//                String[] operatorandtypes = Util.TokenizerString2(operatorandtype, "_");
//                String opertor = operatorandtypes[0];
//                String opertortype = operatorandtypes[1];
//
//                if ("0".equals(opertortype)) {
//                    alloperators += "," + opertor;
//                }
//            }
//        }
//
//        if (alloperators.length() > 1) {
//            alloperators = alloperators.substring(1);
//        }
//        return alloperators;
//	}
//
//   /**
//     * 获取指定人员的代理情况
//     *
//     * @param workflowid
//     * @param opertor
//     * @param requestid
//     * @return
//     */
//    public Map<String, String> getAgentInfoByResouce(String workflowid, String opertors, String requestid) {
//        Map<String, String> result = new HashMap<String, String>();
//        if (opertors == null || "".equals(opertors)) {
//            return result;
//        }
//
//        try {
//            RuleInterface rif = new RuleInterface();
//            RecordSet rs = new RecordSet();
//            Calendar today = Calendar.getInstance();
//            String currentdate = Util.add0(today.get(Calendar.YEAR), 4) + "-" + Util.add0(today.get(Calendar.MONTH) + 1, 2) + "-" + Util.add0(today.get(Calendar.DAY_OF_MONTH), 2);
//            String versionsIds =  WorkflowVersion.getAllVersionStringByWFIDs(workflowid);
//            versionsIds = "'"+StringUtils.replace(versionsIds, ",", "','")+"'";
//            String currenttime = Util.add0(today.get(Calendar.HOUR_OF_DAY), 2) + ":" + Util.add0(today.get(Calendar.MINUTE), 2) + ":" + Util.add0(today.get(Calendar.SECOND), 2);
//            String agentCheckSql = " select bagentuid, agentuid, agentid, conditionkeyid, conditionss from workflow_agentConditionSet where workflowId in (" + versionsIds
//                    + ") and (" + Util.getSubINClause(opertors, "bagentuid",  "IN") + ") and agenttype = '1' and isproxydeal='1'  " + " and ( ( (endDate = '" + currentdate + "' and (endTime='' or endTime is null))"
//                    + " or (endDate = '" + currentdate + "' and endTime > '" + currenttime + "' ) ) " + " or endDate > '" + currentdate + "' or endDate = '' or endDate is null)" + " and ( ( (beginDate = '" + currentdate + "' and (beginTime='' or beginTime is null))" + " or (beginDate = '"
//                    + currentdate + "' and beginTime < '" + currenttime + "' ) ) " + " or beginDate < '" + currentdate + "' or beginDate = '' or beginDate is null)  order by agentbatch asc,id asc  ";
//            rs.execute(agentCheckSql);
//
//            while (rs.next()) {
//                //被代理人
//                String bagentuid = Util.null2String(rs.getString("bagentuid"));
//                //代理人
//                String agentuid = Util.null2String(rs.getString("agentuid"));
//                //代理条件id
//                String conditionkeyid = Util.null2String(rs.getString("conditionkeyid"));
//                //代理条件实体
//                String conditionss = Util.null2String(rs.getString("conditionss"));
//                //如果存在条件，则需要进行验证，验证不通过则验证下一条代理
//                if (!"".equals(conditionkeyid) && !"".equals(conditionss)) {
//                    if (!rif.compareRuleforWF(conditionss, requestid, conditionkeyid, RuleInterface.RULESRC_AGENT)) {
//                        continue;
//                    }
//                }
//
//                String tempBagentuid = result.get(bagentuid);
//                if (tempBagentuid == null || "".equals(tempBagentuid)) {
//                    result.put(bagentuid, agentuid);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//
//    }
//
//   /**
//     * 判断当前接收人是否存在代理
//     *
//     * @param workflowid
//     * @param opertor
//     * @param requestid
//     * @return
//     */
//	public String getAgentid(String workflowid,String opertor,String requestid){
//	 String agenterId="";
//	try{
//		RecordSet rs = new RecordSet();
//		Calendar today = Calendar.getInstance();
//		String currentdate = Util.add0(today.get(Calendar.YEAR), 4) + "-" +
//				Util.add0(today.get(Calendar.MONTH) + 1, 2) + "-" +
//				Util.add0(today.get(Calendar.DAY_OF_MONTH), 2);
//
//		String currenttime = Util.add0(today.get(Calendar.HOUR_OF_DAY), 2) + ":" +
//				Util.add0(today.get(Calendar.MINUTE), 2) + ":" +
//				Util.add0(today.get(Calendar.SECOND), 2);
//			 String agentCheckSql = " select * from workflow_agentConditionSet where workflowId='"+ workflowid +"' and bagentuid='" + opertor +
//			 "' and agenttype = '1' and isproxydeal='1'  " +
//			 " and ( ( (endDate = '" + currentdate + "' and (endTime='' or endTime is null))" +
//			 " or (endDate = '" + currentdate + "' and endTime > '" + currenttime + "' ) ) " +
//			 " or endDate > '" + currentdate + "' or endDate = '' or endDate is null)" +
//			 " and ( ( (beginDate = '" + currentdate + "' and (beginTime='' or beginTime is null))" +
//			 " or (beginDate = '" + currentdate + "' and beginTime < '" + currenttime + "' ) ) " +
//			 " or beginDate < '" + currentdate + "' or beginDate = '' or beginDate is null)  order by agentbatch asc,id asc  ";
//			 rs.execute(agentCheckSql);
//			 while(rs.next()){
//			    String agentid = Util.null2String(rs.getString("agentid"));
//				String conditionkeyid = Util.null2String(rs.getString("conditionkeyid"));
//				//检查当前流程下的代理是否支持批次条件【开启流程中的代理、已经是流转中的、批次条件满足】
//				boolean isagentcond = this.isagentcondite(""+ requestid, "" + workflowid, "" + opertor,"" + agentid, "" + conditionkeyid);
//				 if(isagentcond){
//					 agenterId=Util.null2String(rs.getString("agentuid"));
//					 break;
//				 }
//			  }
//		 }catch(Exception e){
//
//		 }
//		 return agenterId;
//
//	}
//
//	/**
//	 * 获取所有的人员
//	 *
//	 * @param workflowid
//	 * @param beagenterId
//	 * @param agenterId
//	 * @return
//	 */
//	public String getMulResourcename1(String agenterId, String agentid) {
//		ResourceComInfo ResourceComInfo = null;
//		try {
//			ResourceComInfo = new ResourceComInfo();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String agenttype="";
//		RecordSet rs = new RecordSet();
//		rs.executeSql("select * from workflow_agent where agentid='"+agentid+"' ");
//		if(rs.next()){
//			agenttype=Util.null2String(rs.getString("agenttype"));
//		}
//
//		String returnstr = "";
//		rs.executeSql("select lastname from hrmresource where id in(select agentuid from workflow_agentConditionSet where agentid='"+ agentid + "'  and agenttype='"+agenttype+"' )");
//		while (rs.next()) {
//			if (returnstr.equals("")) {
//				returnstr = Util.null2String(rs.getString("lastname"));
//			} else {
//				returnstr += ","+Util.null2String(rs.getString("lastname"));
//			}
//		}
//		return returnstr;
//	}
//
//	/**
//	 * 获取最大的id
//	 *
//	 * @param workflowid
//	 * @param beagenterId
//	 * @param agenterId
//	 * @return
//	 */
//	public String getMaxAgentId(String workflowid, String beagenterId,
//			String agenterId) {
//		RecordSet rs = new RecordSet();
//		String agentid = "";
//		rs.executeSql("select agentid from workflow_Agent where workflowid='"
//				+ workflowid + "' and beagenterId='" + beagenterId
//				+ "' and agenterId='" + agenterId + "' and agenttype='1'");
//		if (rs.next()) {
//			agentid = Util.null2String(rs.getString("agentid"));
//		}
//		return agentid;
//	}
//
//
//
//	/**
//	 * 写入代理明细内容
//	 * @param group_agenterId
//	 * @param group_isCreateAgenter
//	 * @param group_isProxyDeal
//	 * @param group_isPendThing
//	 * @param group_conditionss
//	 * @param group_conditioncn
//	 * @param group_beagenterid
//	 * @param group_beginDate
//	 * @param group_beginTime
//	 * @param group_endDate
//	 * @param group_endTime
//	 * @param group_agentbatch
//	 */
//	public void insertAgentConditionSet(String group_agenterId,
//			String group_isCreateAgenter, String group_isProxyDeal,
//			String group_isPendThing, String group_conditionss,
//			String group_conditioncn, String group_beagenterid,
//			String group_beginDate, String group_beginTime,
//			String group_endDate, String group_endTime,
//			String group_agentbatch, String group_agentid,
//			String group_conditionkeyid, String workflowid,String operatorid,String operatordate,String operatortime,String isSet,String backDate,String backTime,String agenttypes,String group_ruleRelationship) {
//
//		RecordSet rs = new RecordSet();
//		String sql = "insert into workflow_agentConditionSet(agentid,bagentuid,agentuid,conditionss,conditioncn,"
//				+ "conditionkeyid,beginDate,beginTime,endDate,endTime,isCreateAgenter,isPendThing,isProxyDeal,agentbatch,workflowid,operatorid,operatordate,operatortime,isSet,backDate,backTime,agenttype,ruleRelationship)";
//		sql += "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//
//		 ConnStatement statement = null;
//         statement = new ConnStatement();
//         try {
//             statement.setStatementSql(sql);
//             statement.setString(1, group_agentid);
//             statement.setString(2, group_beagenterid);
//             statement.setString(3, group_agenterId);
//             statement.setString(4, group_conditionss);
//             statement.setString(5, Util.toScreenToEdit(group_conditioncn,7));
//             statement.setString(6, group_conditionkeyid);
//             statement.setString(7, group_beginDate);
//             statement.setString(8, group_beginTime);
//             statement.setString(9, group_endDate);
//             statement.setString(10, group_endTime);
//             statement.setString(11, group_isCreateAgenter);
//             statement.setString(12, group_isPendThing);
//             statement.setString(13, group_isProxyDeal);
//             statement.setString(14, group_agentbatch);
//             statement.setString(15, workflowid);
//             statement.setString(16, operatorid);
//             statement.setString(17, operatordate);
//             statement.setString(18, operatortime);
//             statement.setString(19, isSet);
//             statement.setString(20, backDate);
//             statement.setString(21, backTime);
//             statement.setString(22, agenttypes);
//             statement.setString(23, group_ruleRelationship);
//             statement.executeUpdate();
//           //  System.out.println("=======插入明细=========");
//         }catch(Exception e){
//        	 e.getMessage();
//         } finally {
//        	 statement.close();
//         }
//	}
//
//
//	/**
//	 * 转换日期格式
//	 *
//	 * @param s
//	 * @return
//	 */
//	private static Date StringToDate(String s) {
//		Date time = new Date();
//		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		try {
//			time = sd.parse(s);
//		} catch (Exception e) {
//			// System.out.println("输入的日期格式有误！");
//		}
//		return time;
//	}
//	 /**
//		 * 检查当前代理是否已经开始代理了
//		 *
//		 * @param agenttype
//		 * @return 1 代理中 2 已结束 3 未开始
//		 */
//	public String getAgentType(String agentid) {
//		String restr = "";
//
//		RecordSet rs = new RecordSet();
//		rs.executeSql("select begindate,begintime,endtime,enddate,agenttype from workflow_agentConditionSet where  agentid='"+agentid+"'");
//		String beginDate="";
//		String beginTime="";
//		String endDate = "";
//		String endTime="";
//		String agenttype="";
//		if(rs.next()){
//			beginDate=Util.null2String(rs.getString("begindate"));
//			beginTime=Util.null2String(rs.getString("begintime"));
//			endDate=Util.null2String(rs.getString("enddate"));
//			endTime=Util.null2String(rs.getString("endtime"));
//			agenttype=Util.null2String(rs.getString("agenttype"));
//		}
//		if(agenttype.equals("1")){
//				String currentDate = TimeUtil.getCurrentDateString();
//				String currentTime = (TimeUtil.getCurrentTimeString()).substring(11, 19);
//				if (beginDate.equals("") && endDate.equals("")) {
//					restr = "1"; // 代理中
//				} else if (beginDate.equals("") && !endDate.equals("")) {
//					if (endTime.equals(""))
//						endTime = "23:59";
//					// 结束时间比当前时间晚
//					if (StringToDate(endDate + " " + endTime).after(
//							StringToDate(currentDate + " " + currentTime))) {
//						restr = "1"; // 代理中
//					} else {
//						restr = "2"; // 已结束
//					}
//
//				} else if (!beginDate.equals("") && endDate.equals("")) {
//					if (beginTime.equals(""))
//						beginTime = "00:00";
//					// 开始时间比当前时间早
//					if (StringToDate(beginDate + " " + beginTime).before(
//							StringToDate(currentDate + " " + currentTime))) {
//						restr = "1"; // 代理中
//					} else {
//						restr = "2";// 已结束
//					}
//				} else if (!beginDate.equals("") && !endDate.equals("")) {
//					if (beginTime.equals("")) {
//						beginTime = "00:00";
//					}
//					if (endTime.equals("")) {
//						endTime = "23:59";
//					}
//					if (StringToDate(beginDate + " " + beginTime).before(
//							StringToDate(currentDate + " " + currentTime))
//							&& StringToDate(endDate + " " + endTime).after(
//									StringToDate(currentDate + " " + currentTime))) {
//						restr = "1"; // 代理中
//					} else if (StringToDate(beginDate + " " + beginTime).after(
//							StringToDate(currentDate + " " + currentTime))) {
//						restr = "3"; // 未开始
//					} else if (StringToDate(endDate + " " + endTime).before(
//							StringToDate(currentDate + " " + currentTime))) {
//						restr = "2"; // 已结束
//					}
//				}
//		}else{
//			restr = "2"; // 已结束
//		}
//		return restr;
//	}
//
//
//
//
//	/**
//	 * [标准新建代理时，去除重复]检查当前新建的代理时间范围是否在之前设置的代理时间范围内有重叠
//	 *
//	 * @return
//	 */
//	public String  getIsAgent_agein(String beagenterId,String agentId,String beginDate,String beginTime,String endDate,String endTime,
//			String workflowid,String isCreateAgenter,String isProxyDeal,String isPendThing){
//		RecordSet rs = new RecordSet();
//		TimeUtil TimeUtil=new TimeUtil();
//		String group_beginDate="";
//		String group_beginTime="";
//		String ksdate="";
//		String jsdate="";
//	    if(beginDate.equals("")){
//	    	beginDate="1900-01-01";
//	    }
//	    if(beginTime.equals("")){
//	    	beginTime="00:00";
//	    }
//	    if(endDate.equals("")){
//	    	endDate="2099-12-31";
//	    }
//	    if(endTime.equals("")){
//	    	endTime="23:59";
//	    }
//		ksdate=beginDate+" "+beginTime;
//		jsdate=endDate+" "+endTime;
//		String returnstrallid="";
//		/*
//		 * 检查当前新建的代理时间范围是否在之前设置的代理时间范围内有重叠
//		 *
//		 * 原开始日期大于新开始日期 且 新开始日期小于原结束日期 且 原结束日期小于新结束日期
//		 * 新开始日期小于原开始日期 且 结束日期大于原结束日期
//		 * 新开启日期小于原开始日期 且 原开始日期小于结束日期 且 新结束日期小于原结束日期
//		 * 原开始日期小于新开始日期 且 新结束日期小于原结束日期
//		 *
//		 */
//		String strSubClause="  and (" + Util.getSubINClause(workflowid, "workflowid", "IN") + ") " ;
//    	StringBuffer agentsql=new StringBuffer();
//    	if(rs.getDBType().equals("oracle")){
//    		agentsql.append(" 	select *");
//	    	agentsql.append("	from workflow_agentConditionSet");
//	    	agentsql.append("	where bagentuid ='"+beagenterId+"'");
//	    	agentsql.append( strSubClause );
//	    	agentsql.append("	and   agenttype=1 ");
//	    	agentsql.append("	and ((to_date(nvl(beginDate,'1900-01-01') || ' ' || nvl(beginTime,'00:00'),'yyyy-mm-dd hh24:mi')<= to_date('"+ksdate+"','yyyy-mm-dd hh24:mi')  and");
//			agentsql.append("	to_date('"+ksdate+"','yyyy-mm-dd hh24:mi')<= to_date(nvl(endDate,'2099-12-31')||' '||nvl(endTime,'23:59'),'yyyy-mm-dd hh24:mi')  and");
//			agentsql.append("	to_date(nvl(endDate,'2099-12-31') || ' ' || nvl(endTime,'23:59'),'yyyy-mm-dd hh24:mi')<=to_date('"+jsdate+"','yyyy-mm-dd hh24:mi') )");
//			agentsql.append("	or (to_date('"+ksdate+"','yyyy-mm-dd hh24:mi')<= to_date(nvl(beginDate,'1900-01-01')||' '|| nvl(beginTime,'00:00'),'yyyy-mm-dd hh24:mi')  and");
//			agentsql.append("	to_date(nvl(endDate,'2099-12-31')|| ' ' || nvl(endTime,'23:59'),'yyyy-mm-dd hh24:mi')  <= to_date('"+jsdate+"','yyyy-mm-dd hh24:mi') )");
//			agentsql.append("	or (to_date('"+ksdate+"','yyyy-mm-dd hh24:mi')  <= to_date(nvl(beginDate,'1900-01-01')|| ' ' || nvl(beginTime,'00:00'),'yyyy-mm-dd hh24:mi')  and");
//			agentsql.append("	to_date(nvl(beginDate,'1900-01-01')|| ' ' || nvl(beginTime,'00:00'),'yyyy-mm-dd hh24:mi') <= to_date('"+jsdate+"','yyyy-mm-dd hh24:mi')  and");
//			agentsql.append("	to_date('"+jsdate+"','yyyy-mm-dd hh24:mi')  <=to_date( nvl(endDate,'2099-12-31')|| ' ' || nvl(endTime,'23:59'),'yyyy-mm-dd hh24:mi') )");
//			agentsql.append("	or (to_date(nvl(beginDate,'1900-01-01')|| ' ' || nvl(beginTime,'00:00'),'yyyy-mm-dd hh24:mi')  <= to_date('"+ksdate+"','yyyy-mm-dd hh24:mi')  and");
//			agentsql.append("	to_date('"+jsdate+"','yyyy-mm-dd hh24:mi')  <= to_date(nvl(endDate,'2099-12-31')|| ' ' || nvl(endTime,'23:59'),'yyyy-mm-dd hh24:mi') ))");
//
//    	}else{
//
//	    	agentsql.append(" 	select *");
//	    	agentsql.append("	from workflow_agentConditionSet");
//	    	agentsql.append("	where bagentuid ='"+beagenterId+"'");
//	    	agentsql.append( strSubClause );
//	    	agentsql.append("	and   agenttype=1 ");
//	    	agentsql.append("	and ((beginDate + ' ' + beginTime <= '"+ksdate+"' and");
//			agentsql.append("	'"+ksdate+"' <= endDate + ' ' + endTime and");
//			agentsql.append("	endDate + ' ' + endTime <= '"+jsdate+"')");
//			agentsql.append("	or ('"+ksdate+"' <= beginDate + ' ' + beginTime and");
//			agentsql.append("	endDate + ' ' + endTime <= '"+jsdate+"')");
//			agentsql.append("	or ('"+ksdate+"' <= beginDate + ' ' + beginTime and");
//			agentsql.append("	beginDate + ' ' + beginTime <= '"+jsdate+"' and");
//			agentsql.append("	'"+jsdate+"' <= endDate + ' ' + endTime)");
//			agentsql.append("	or (beginDate + ' ' + beginTime <= '"+ksdate+"' and");
//			agentsql.append("	'"+jsdate+"' <= endDate + ' ' + endTime))");
//    	}
//    	//设置状态时需要满足以下2种情况的需要提示重叠情况
//		//1、老的代理满足【代理流程创建】关闭 【代理流程处理】开启
//		//2、老的代理满足【代理流程创建】开启【代理流程处理】开启
//
//    	//新的代理设置：【代理流程创建】开启【代理流程处理】开启
//		//1、【代理流程创建】关闭   【代理流程处理】开启
//		//2、【代理流程创建】开启	【代理流程处理】开启
//
//    	//经过分析得出 上述2种情况，其实主要判断 新代理和老的代理中是否都开启了  流程处理开关 如果都开启了则提示 否则不提示
//		if(isProxyDeal.equals("1")){//流程创建关闭，流程处理开启
//		   agentsql.append(" and exists (select 1 from workflow_agentConditionSet t2 where t2.agentid=agentId and t2.isProxyDeal='1' )  ");
//		 rs.executeSql(agentsql.toString());
//		}
//
//
//		return ""+rs.getCounts();
//	}
//
//
//	/**
//	 * 检查当前新建的代理时间范围是否在之前设置的代理时间范围内有重叠
//	 *
//	 * @return
//	 */
//	public String  getIsAgent(String agentid,String beagenterId,String agentId,String beginDate,String beginTime,String endDate,String endTime,
//			String workflowid,String isCreateAgenter,String isProxyDeal,String isPendThing){
//		RecordSet rs = new RecordSet();
//		TimeUtil TimeUtil=new TimeUtil();
//		String group_beginDate="";
//		String group_beginTime="";
//		String ksdate="";
//		String jsdate="";
//	    if(beginDate.equals("")){
//	    	beginDate="1900-01-01";
//	    }
//	    if(beginTime.equals("")){
//	    	beginTime="00:00";
//	    }
//	    if(endDate.equals("")){
//	    	endDate="2099-12-31";
//	    }
//	    if(endTime.equals("")){
//	    	endTime="23:59";
//	    }
//		ksdate=beginDate+" "+beginTime;
//		jsdate=endDate+" "+endTime;
//		String returnstrallid="";
//		/*
//		 * 检查当前新建的代理时间范围是否在之前设置的代理时间范围内有重叠
//		 *
//		 * 原开始日期大于新开始日期 且 新开始日期小于原结束日期 且 原结束日期小于新结束日期
//		 * 新开始日期小于原开始日期 且 结束日期大于原结束日期
//		 * 新开启日期小于原开始日期 且 原开始日期小于结束日期 且 新结束日期小于原结束日期
//		 * 原开始日期小于新开始日期 且 新结束日期小于原结束日期
//		 *
//		 */
//
//		String strSubClause="  and (" + Util.getSubINClause(workflowid, "workflowid", "IN") + ") " ;
//    	StringBuffer agentsql=new StringBuffer();
//
//    	if(rs.getDBType().equals("oracle")){
//    		agentsql.append(" 	select *");
//	    	agentsql.append("	from workflow_agent t");
//	    	agentsql.append("	where beagenterid ='"+beagenterId+"' ");
//	    	agentsql.append(strSubClause);
//	    	if(!agentid.equals("")){
//	    		agentsql.append("	and  agentId not in("+agentid+")");
//	    	}
//
//	    	agentsql.append("	and   agenttype=1 ");
//	    	agentsql.append("	and ((to_date(nvl(beginDate,'1900-01-01') || ' ' || nvl(beginTime,'00:00'),'yyyy-mm-dd hh24:mi')<= to_date('"+ksdate+"','yyyy-mm-dd hh24:mi')  and");
//			agentsql.append("	to_date('"+ksdate+"','yyyy-mm-dd hh24:mi')<= to_date(nvl(endDate,'2099-12-31')||' '||nvl(endTime,'23:59'),'yyyy-mm-dd hh24:mi')  and");
//			agentsql.append("	to_date(nvl(endDate,'2099-12-31') || ' ' || nvl(endTime,'23:59'),'yyyy-mm-dd hh24:mi')<=to_date('"+jsdate+"','yyyy-mm-dd hh24:mi') )");
//			agentsql.append("	or (to_date('"+ksdate+"','yyyy-mm-dd hh24:mi')<= to_date(nvl(beginDate,'1900-01-01')||' '|| nvl(beginTime,'00:00'),'yyyy-mm-dd hh24:mi')  and");
//			agentsql.append("	to_date(nvl(endDate,'2099-12-31')|| ' ' || nvl(endTime,'23:59'),'yyyy-mm-dd hh24:mi')  <= to_date('"+jsdate+"','yyyy-mm-dd hh24:mi') )");
//			agentsql.append("	or (to_date('"+ksdate+"','yyyy-mm-dd hh24:mi')  <= to_date(nvl(beginDate,'1900-01-01')|| ' ' || nvl(beginTime,'00:00'),'yyyy-mm-dd hh24:mi')  and");
//			agentsql.append("	to_date(nvl(beginDate,'1900-01-01')|| ' ' || nvl(beginTime,'00:00'),'yyyy-mm-dd hh24:mi') <= to_date('"+jsdate+"','yyyy-mm-dd hh24:mi')  and");
//			agentsql.append("	to_date('"+jsdate+"','yyyy-mm-dd hh24:mi')  <=to_date( nvl(endDate,'2099-12-31')|| ' ' || nvl(endTime,'23:59'),'yyyy-mm-dd hh24:mi') )");
//			agentsql.append("	or (to_date(nvl(beginDate,'1900-01-01')|| ' ' || nvl(beginTime,'00:00'),'yyyy-mm-dd hh24:mi')  <= to_date('"+ksdate+"','yyyy-mm-dd hh24:mi')  and");
//			agentsql.append("	to_date('"+jsdate+"','yyyy-mm-dd hh24:mi')  <= to_date(nvl(endDate,'2099-12-31')|| ' ' || nvl(endTime,'23:59'),'yyyy-mm-dd hh24:mi') ))");
//
//    	}else{
//	    	agentsql.append(" 	select *");
//	    	agentsql.append("	from workflow_agent t");
//	    	agentsql.append("	where beagenterid ='"+beagenterId+"'");
//	    	agentsql.append(strSubClause);
//	    	if(!agentid.equals("")){
//	    		agentsql.append("	and  agentId not in("+agentid+")");
//	    	}
//	    	agentsql.append("	and   agenttype=1 ");
//	    	agentsql.append("	and ((beginDate + ' ' + beginTime <= '"+ksdate+"' and");
//			agentsql.append("	'"+ksdate+"' <= endDate + ' ' + endTime and");
//			agentsql.append("	endDate + ' ' + endTime <= '"+jsdate+"')");
//			agentsql.append("	or ('"+ksdate+"' <= beginDate + ' ' + beginTime and");
//			agentsql.append("	endDate + ' ' + endTime <= '"+jsdate+"')");
//			agentsql.append("	or ('"+ksdate+"' <= beginDate + ' ' + beginTime and");
//			agentsql.append("	beginDate + ' ' + beginTime <= '"+jsdate+"' and");
//			agentsql.append("	'"+jsdate+"' <= endDate + ' ' + endTime)");
//			agentsql.append("	or (beginDate + ' ' + beginTime <= '"+ksdate+"' and");
//			agentsql.append("	'"+jsdate+"' <= endDate + ' ' + endTime))");
//    	}
//
//    	//rs.writeLog(agentsql.toString());
//		//设置状态时需要满足以下2种情况的需要提示重叠情况
//		//1、老的代理满足【代理流程创建】关闭 【代理流程处理】开启
//		//2、老的代理满足【代理流程创建】开启【代理流程处理】开启
//		if(isProxyDeal.equals("1")){//流程创建关闭，流程处理开启
//		  agentsql.append(" and exists (select 1 from workflow_agentConditionSet t2 where t2.agentid=t.agentId and t2.isProxyDeal='1' )  ");
//		  rs.executeSql(agentsql.toString());
//		}
//
//		while(rs.next()){
//			if(returnstrallid.equals("")){
//				returnstrallid=rs.getString("agentid");
//			}else{
//				returnstrallid+=","+rs.getString("agentid");
//			}
//		}
//		return returnstrallid+"_"+rs.getCounts();
//	}
//
//	/**
//	 * 获取代理主键id
//	 * @return
//	 */
//	public synchronized  int getSequenceAgentId() {
//		int agentId =0;
//		RecordSet rs = new RecordSet();
//		rs.executeSql("select * from SequenceIndex where indexdesc ='workflowagentid'");
//		if (rs.next()) {
//			agentId = Util.getIntValue(""+rs.getInt("currentid"),0);
//		}
//		rs.executeSql("update SequenceIndex set currentid ="+(agentId + 1)+" where indexdesc= 'workflowagentid' ");
//		rs.executeSql("select * from SequenceIndex where indexdesc ='workflowagentid'");
//		if (rs.next()) {
//			agentId = Util.getIntValue(""+rs.getInt("currentid"),0);
//		}
//		return agentId;
//	}
//
//
//	/**
//	 * 判断当前是否存在代理
//	 * 代理主键id
//	 * @param agentid
//	 * @return
//	 */
//	public boolean agente_xists(String agentid){
//		RecordSet rs = new RecordSet();
//		rs.executeSql("select * from workflow_Agent  where  agentId='"+agentid+"'  ");
//		if(rs.getCounts()>0){
//			return true;
//		}
//		return false;
//	}
//
//
//	/**
//	 * 流程代理【原标准代理】
//	 * @param beagenterId
//	 * @param agenterId
//	 * @param beginDate
//	 * @param beginTime
//	 * @param endDate
//	 * @param endTime
//	 * @param agentrange
//	 * @param rangetype
//	 * @param isCreateAgenter
//	 * @param isProxyDeal
//	 * @param isPendThing
//	 * @param usertype
//	 * @param user
//	 * @param type  1 [从新保存的代理设置中去除重复设置内容]   2、以新保存的代理设置替换已有重复的代理设置   3、默认代理
//	 * @return
//	 */
//	public String agentadd(String beagenterId, String agenterId,
//			String beginDate, String beginTime, String endDate, String endTime,
//			String agentrange, String rangetype, String isCreateAgenter,
//			String isProxyDeal, String isPendThing, int usertype, User user,String type,String agentid) {
//		String agentretur="";
//		RecordSet rs = new RecordSet();
//		RecordSet rs1 = new RecordSet();
//		RecordSet rs2 = new RecordSet();
//        RecordSet rsn = new RecordSet();
//
//		char separ = Util.getSeparator();
//		String currentDate=TimeUtil.getCurrentDateString();
//		String currentTime=(TimeUtil.getCurrentTimeString()).substring(11,19);
//		String Procpara="";
//		String iseditstartdate="0";
//		String iseditstarttime="0";
//		String iseditenddate="0";
//		String iseditendtime="0";
//		if(beginDate.equals("")){
//			beginDate="1900-01-01";
//			iseditstartdate="1";
//		}
//		if(beginTime.equals("")){
//			beginTime="00:00";
//			iseditstarttime="1";
//		}
//		if(endDate.equals("")){
//			endDate="2099-12-31";
//			iseditenddate="1";
//		}
//		if(endTime.equals("")){
//			endTime="23:59";
//			iseditendtime="1";
//		}
//		String sql="";
//		int beagentwfid = -1; //被代理流程ID
//		try{
//			String workflowids="";
//			Map map = new HashMap();//存T
//			Map wmap = new HashMap();//存W
//			boolean flag1 = false;
//			// 80改造流程代理逻辑
//			if(agentrange.equals("0"))
//			{
//				flag1 = true;
//				if (usertype == 0) {
//					sql = "select * from workflow_base where isvalid=1 order by workflowname ";
//					rs.executeSql(sql);
//					while (rs.next()) {
//						map.put("W"+rs.getString("id"), rs.getString("id"));
//						workflowids+=","+Util.getIntValue(rs.getString("id"));
//					}
//				} else if (usertype == 1) {
//					// 客户用户只能代理“外部访问者支持”类型的流程
//					sql = "select id from workflow_base where isvalid=1 and workflowtype=29 order by workflowname";
//					rs.executeSql(sql);
//					while (rs.next()) {
//						map.put("W"+rs.getString("id"),rs.getString("id"));
//						workflowids+=","+Util.getIntValue(rs.getString("id"));
//					}
//				}
//				sql = "";
//			}else if(agentrange.equals("1"))
//			{
//				if(!rangetype.equals(""))
//				{
//					if (!rangetype.startsWith(",")) {
//						rangetype = "," + rangetype;// 为了与其他兼容
//					}
//					workflowids = rangetype;
//					String tmpty = rangetype.substring(1);
//					String[] _tty = Util.TokenizerString2(tmpty,",");
//					for(int d=0;d<_tty.length;d++){
//						map.put("W"+_tty[d],_tty[d]);
//					}
//				}
//						flag1 = true;
//			}
//		    if(!flag1){
//		       return agentretur="1";
//		    }
//
//		    Iterator it = wmap.entrySet().iterator();
//		    while(it.hasNext()) {
//		    	Map.Entry entry = (Map.Entry) it.next();
//				String mapKey = entry.getKey().toString();
//				String mapValue = entry.getValue().toString();
//				if(map.get(mapKey)==null) {
//					map.put(mapKey, mapValue);
//				}
//		    }
//
//		    it = map.entrySet().iterator();
//		    while(it.hasNext()) {
//		    	Map.Entry entry = (Map.Entry) it.next();
//				String mapKey = entry.getKey().toString();
//				String mapValue = entry.getValue().toString();
//		        beagentwfid = Integer.parseInt(mapValue);
//
//		        //type为1表示过滤掉本次添加的代理设置中重叠的流程信息
//		        if(type.equals("1")){//验证当前流程当前时间段是否在之前设置的代理中已有重叠，如果有则跳出本次代理继续其他代理操作
//		        	String overlapAgent=this.getIsAgent_agein(""+beagenterId,"",beginDate,beginTime,endDate,endTime,""+beagentwfid,""+isCreateAgenter,""+isProxyDeal,""+isPendThing);
//		        	if(Util.getIntValue(overlapAgent, 0)>0){
//		        		continue;
//		        	}
//		        }
//
//			   // 对每一个代理进行处理
//		        /* -------------- td2483 xwj 20050812 begin------------- */
//		        int  agentId=this.getSequenceAgentId();//获取代理当前序列【原E7老方式处理】
//		    	/* -------------- td2483 xwj 20050812 end------------- */
//		        // added by cyril on 2008-12-01 for td:9684
//		        String cntSQL = "select count(1) as cnt from workflow_agent "
//						+ "where workflowid='" + beagentwfid
//						+ "' and beagenterid='" + beagenterId + "' "
//						+ "and agenterid='" + agenterId + "' ";
//						String v1 = "";
//						String v2 = "";
//						String v3 = "";
//						String v4 = "";
//				if (rs1.getDBType().equals("oracle")) {
//					if (beginDate.equals("")){
//						v1 = "IS NULL";
//					}else{
//						v1 = "='" + beginDate + "'";
//					}
//					if (beginTime.equals("")){
//						v2 = "IS NULL";
//					}else{
//						v2 = "='" + beginTime + "'";
//					}
//					if (endDate.equals("")){
//						v3 = "IS NULL";
//					}else{
//						v3 = "='" + endDate + "'";
//					}
//					if (endTime.equals("")){
//						v4 = "IS NULL";
//					}else{
//						v4 = "='" + endTime + "'";
//					}
//					cntSQL += "  and begindate " + v1 + "  and beginTime " + v2
//							+ "  and enddate " + v3 + " and endtime " + v4
//							+ "  ";
//				} else {
//					cntSQL += " and begindate='" + beginDate + "' "
//							+ " and beginTime='" + beginTime + "' "
//							+ " and enddate='"+ endDate + "' "
//							+ " and endtime='" + endTime + "' ";
//				}
//				cntSQL += "and iscreateagenter='" + isCreateAgenter + "' "
//						+ "and agenttype='1' and operatorid='" + user.getUID()
//						+ "' " + "and isset='0' and ispending='" + isPendThing
//						+ "' ";
//				rs1.executeSql(cntSQL);
//				int cnt = -1;
//				if (rs1.next()){
//					cnt = rs1.getInt("cnt");
//				}
//				String agenttypes=this.getAgentTypeNew(beginDate, beginTime, endDate, endTime);
//				if(agenttypes.equals("1")){//代理中
//					agenttypes="1";
//				}else if(agenttypes.equals("2")){//已结束
//					agenttypes="0";
//				}else if(agenttypes.equals("3")){//未开始
//					agenttypes="1";
//				}else{
//					agenttypes="1";
//				}
//				try{
//					if (cnt == 0){
//						StringBuffer agentsb = new StringBuffer();
//						agentsb.append("  insert into workflow_agent( ");
//						agentsb.append("	agentId,workflowId,beagenterId,agenterId,beginDate,beginTime,endDate,endTime,	");
//						agentsb.append("	isCreateAgenter,agenttype,operatorid,operatordate,operatortime,isSet,isPending,backDate,backTime, ");
//						agentsb.append("	isProxyDeal,iseditstartdate,iseditstarttime,iseditenddate,iseditendtime	");
//						agentsb.append(" ) ");
//						agentsb.append(" values( ");
//						agentsb.append( agentId+ ","+ beagentwfid+ ","+ beagenterId+ ","+ agenterId+ ",'"+ beginDate+ "','"+ beginTime+ "','"+ endDate+ "','"+ endTime +"',");
//						agentsb.append( isCreateAgenter+ ",'"+agenttypes+"',"+ user.getUID()+ ",'"+ currentDate+ "','"+ currentTime+ "','0','"+ isPendThing+ "','','', ");
//						agentsb.append("'"+ isProxyDeal + "','"+iseditstartdate+"','"+iseditstarttime+"','"+iseditenddate+"','"+iseditendtime+"'");
//						agentsb.append(" ) ");
//						rs1.executeSql(agentsb.toString());//插入代理信息
//						//插入明细数据
//						insertAgentConditionSet(""+agenterId,""+isCreateAgenter,""+isProxyDeal,""+isPendThing,"","",""+beagenterId,beginDate,beginTime,endDate,endTime,"0.00",""+agentId,"",""+beagentwfid,""+user.getUID(),currentDate,currentTime,"","","",agenttypes,"");
//					    // added by cyril on 2008-12-01 for td:9684
//					}
//			}catch(Exception e){
//				rs1.writeLog("====新建代理异常："+e);
//				break;
//			}
//
//				//根据开关判断是否需要将已有待办流程，代理给对应的代理人中
//				String currentDateTime=TimeUtil.getCurrentTimeString();
//				String beginTimes=beginTime;
//			    if (beginTime.equals("")){
//			    	beginTimes="00:00:00";
//			    }
//			    else{
//			    	beginTimes = beginTimes+":00";
//			    }
//				long timeIntervals=TimeUtil.timeInterval(beginDate+' '+beginTimes,currentDateTime);
//			    if(agenttypes.equals("1") && isPendThing.equals("1") && (beginDate.equals("") || timeIntervals>=0)){// 判断是否开启了已有待办事宜进行代理
//			    	String versionsIds =  WorkflowVersion.getAllVersionStringByWFIDs("" + beagentwfid);
//			        //sql="select a.id,a.requestid,a.groupid,a.workflowid,a.workflowtype,a.usertype,a.nodeid,a.showorder,a.isremark,b.isbill,a.groupdetailid,a.takisremark from workflow_currentoperator a,workflow_base b where a.workflowid=b.id and a.userid = " + beagenterId + " and a.isremark in ('0','1','5','7','8','9') and a.agenttype ='0' and a.agentorbyagentid ='-1' and a.workflowid in ("+versionsIds +")";
//					//意见征询过的，isremark=2，流程处于已办，本质是待办。
//				    sql="select a.id,a.requestid,a.groupid,a.workflowid,a.workflowtype,a.usertype,a.nodeid,a.showorder,a.isremark,b.isbill,a.groupdetailid,a.takisremark,a.id from workflow_currentoperator a,workflow_base b where a.workflowid=b.id and a.userid = " + beagenterId + " and (a.isremark in ('0','1','5','7','8','9') OR (a.isremark ='2' AND takisremark=-2) or (a.isremark ='2' AND a.takisremark = 2 and exists(select 1 from workflow_currentoperator t where t.isremark = '1' and t.takisremark = 2 and t.id = a.id and t.requestid = a.requestid and t.nodeid = a.nodeid))) and a.agenttype ='0' and a.agentorbyagentid ='-1' and a.workflowid in ("+versionsIds +")";
//			        rs.executeSql(sql);
//			        ArrayList requestids=new ArrayList();
//			        ArrayList nodeids=new ArrayList();
//			        ArrayList isbills=new ArrayList();
//			        ArrayList usertypes=new ArrayList();
//			        while(rs.next()){
//			            requestids.add(rs.getString("requestid"));
//			            nodeids.add(rs.getString("nodeid"));
//			            isbills.add(rs.getString("isbill"));
//			            usertypes.add(rs.getString("usertype"));
//			            int coid1 = Util.getIntValue(rs.getString("id"));
//			            // 让代理人获得任务
//						if(rs.getString("usertype").equals("0")){
//			                Procpara = rs.getString("requestid") + separ + ""+agenterId + separ + rs.getString("groupid") + separ + rs.getString("workflowid") + separ + rs.getString("workflowtype") + separ  + rs.getString("usertype") + separ + rs.getString("isremark") + separ+ rs.getString("nodeid") + separ + ""+beagenterId + separ + "2" + separ + rs.getString("showorder")+separ+rs.getInt("groupdetailid");
//			                rs1.executeProc("workflow_CurrentOperator_I", Procpara);
//							if(rs.getString("takisremark")!=""){
//                                 //new weaver.general.BaseBean().writeLog("huangbh rsn  in =====");
//                                 String taksql="update workflow_currentoperator set takisremark = " + rs.getString("takisremark")+ "where usertype=0 and userid = " + agenterId + " and requestid = "+rs.getString("requestid") + "and nodeid = " + rs.getString("nodeid") + " and agenttype = 2 and isremark=" + rs.getString("isremark");
//                                 rs1.executeSql(taksql);
//                                  //================================================================
//                                 //1.插入的代理记录 根据requestid/nodeid/agenttype/islasttimes/takisremark/userid查找刚刚插入的代理人的记录
//                                 String searchResultSql1="select id from workflow_currentoperator where requestid = "+rs.getString("requestid")+" and nodeid = "+rs.getString("nodeid")+" and userid = "+agenterId+" and agenttype = 2 and isremark= 0 and islasttimes =1 and takisremark =-2 ";
//                                  rsn.executeSql(searchResultSql1);
//                                //判断同时存在时，进行更新islastime的值
//                                  if(rsn.next()){
//                                      String id1 = rsn.getString("id");
//                                      //2.原来的记录
//                                      String searchResultSql2="select id from workflow_currentoperator where requestid = "+rs.getString("requestid")+" and nodeid = "+rs.getString("nodeid")+" and userid = "+agenterId+" and (isremark =1 or (isremark = '2' and exists(select 1 from workflow_currentoperator t where t.isremark = '1' and t.takisremark = 2 and t.takid = workflow_currentoperator.id and t.requestid = workflow_currentoperator.requestid and t.nodeid = workflow_currentoperator.nodeid))) and islasttimes =0 and takisremark =-2 ";
//                                      rsn.executeSql(searchResultSql2);
//
//                                      if(id1!=null && rsn.next()){
//                                          String id2 =rsn.getString("id");
//                                          if( id2!= null ){
//                                              String upRecentResultSql2 ="update workflow_currentoperator set takisremark=2,islasttimes =1  where id = "+id2 ;
//                                              rsn.executeSql(upRecentResultSql2);
//                                               String upRecentResultSql1 ="update workflow_currentoperator set islasttimes =0 where id = "+id1;
//                                              rsn.executeSql(upRecentResultSql1);
//                                          }
//                                  }
//                              }
//                            }
//							//判断当前人是否有转发 且转发记录是否已提交，若未提交则需要把转发人 修改为被代理人
//			                rs1.executeQuery("select a.isremark,a.id from workflow_currentoperator a where exists (select 1 from workflow_forward b where a.id = b.BeForwardid and b.requestid = ? and b.Forwardid = ?)", rs.getString("requestid"),coid1);
//			                while(rs1.next()){
//			                    int forwardremark = Util.getIntValue(rs1.getString("isremark"), -1);
//			                    if(forwardremark == 1){
//			                    	rs2.executeQuery("select id from workflow_currentoperator where usertype=0 and userid = " + agenterId + " and requestid = "+rs.getString("requestid") + "and nodeid = " + rs.getString("nodeid"));
//			                        int agentidtemp = 0;
//			                        if(rs2.next()){
//			                        	agentidtemp = Util.getIntValue(rs2.getString("id"));
//			                        }
//			                        rs2.executeSql("update workflow_forward set Forwardid = " + agentidtemp + " where requestid="+rs.getString("requestid")+" and BeForwardid="+rs1.getString("id"));
//			                    }
//			                }
//			                //意见征询未提交的
//			                rs1.executeQuery("select 1 from workflow_currentoperator where requestid = "+rs.getString("requestid")+" AND id="+coid1+" AND takisremark = -2");
//			                while(rs1.next()){
//			                    rs2.executeSql("update workflow_currentoperator set takisremark = null where requestid="+rs.getString("requestid")+" and id="+coid1);
//			                }
//						}
//			            if("1".equals(rs.getString("isremark")) || ("2".equals(rs.getString("isremark")) && "2".equals(rs.getString("takisremark")))){
//			            	int coid2 = 0;
//				            rs1.execute("select max(id) as id from workflow_currentoperator where requestid="+rs.getString("requestid"));
//				            if(rs1.next()){
//				            	coid2 = Util.getIntValue(rs1.getString("id"));
//				            }
//				            rs2.executeSql("update workflow_forward set beforwardid = " + coid2 + " where requestid="+rs.getString("requestid")+" and beforwardid="+coid1);
//				            rs2.executeSql("update workflow_forward set forwardid = " + coid2 + " where requestid="+rs.getString("requestid")+" and forwardid="+coid1);
//
//				            //将当前节点的签字意见权限也同时给代理人，只处理转发的
//				            String reqid = rs.getString("requestid");
//				            String nid = rs.getString("nodeid");
//				            String qsql = " select logid from workflow_logviewusers where userid = " + beagenterId
//				            			+ " and exists (select 1 from workflow_requestLog where workflow_requestLog.requestid = " + reqid
//				            			+ " and workflow_requestLog.nodeid = " + nid
//				            			+ " and workflow_requestLog.logtype = '7' and workflow_logviewusers.logid = workflow_requestLog.logid)";
//				            RecordSet rs4 = new RecordSet();
//				            RecordSet rs5 = new RecordSet();
//				            rs4.executeSql(qsql);
//				            while(rs4.next()){
//				            	String logid = rs4.getString("logid");
//				            	if(!"".equals(logid) && !"-1".equals(logid)){
//				            		String isql = "insert into workflow_logviewusers (logid,userid) values (" + logid + "," + agenterId + ")";
//				            		rs5.executeSql(isql);
//				            	}
//				            }
//                            if("1".equals(rs.getString("isremark"))) {
//                            String takid = Util.null2String(rs.getString("takid"));
//                            if(!"".equals(takid)) {
//                            	rs5.executeSql("update workflow_currentoperator set takid=" + takid + " where id=" + coid2);
//                            }
//                            }else if("2".equals(rs.getString("isremark")) && "2".equals(rs.getString("takisremark"))) {
//                            	rs5.executeSql("update workflow_currentoperator set takid=" + coid2 + " where takid=" + coid1);
//                            	rs5.executeSql("update workflow_currentoperator set preisremark='1' where id=" + coid2);
//                            }
//                        }
//                    }
//
//			        //将流程从字段的代办转到
//			        sql="update workflow_currentoperator set isremark = '2', agenttype ='1', agentorbyagentid ="+agenterId + " where usertype=0 and userid = " + beagenterId + " and (agenttype is null or agenttype='0') and isremark in ('0','1','5','7','8','9') and agenttype ='0' and agentorbyagentid ='-1' and workflowid in ("+ versionsIds + ")";
//			        rs1.executeSql(sql);
//
//			        //QC152344,防止代理抄送引起的问题
//			        for(int i = 0; i < requestids.size(); i++){
//			            if("0".equals(usertypes.get(i))){
//			                wfCurrentOperatorAgent(rs1,Util.getIntValue((String)requestids.get(i)),Util.getIntValue((String)nodeids.get(i)), (String)usertypes.get(i), beagenterId,false);
//			            }
//			        }
//			        // add by xhheng @20051011 for
//					// TD2887,调用RequestAddShareInfo处理共享
//			        RequestAddShareInfo shareinfo = new RequestAddShareInfo();
//			        for(int j=0;j<requestids.size();j++){
//			            shareinfo.setRequestid(Util.getIntValue((String)requestids.get(j)));
//			            shareinfo.SetWorkFlowID(beagentwfid);
//			            shareinfo.SetNowNodeID(Util.getIntValue((String)nodeids.get(j)));
//			            shareinfo.SetNextNodeID(Util.getIntValue((String)nodeids.get(j)));
//			            shareinfo.setIsbill(Util.getIntValue((String)isbills.get(j),0));
//			            shareinfo.setUser(user);
//			            shareinfo.SetIsWorkFlow(1);
//			            shareinfo.setHaspassnode(true);
//			            shareinfo.addShareInfoFromAgent(beagenterId);
//
//			            // 统一待办提醒
//			            PostWorkflowInfo postWorkflowInfo = new PostWorkflowInfo();
//			            postWorkflowInfo.operateToDo(requestids.get(j).toString());
//			            new weaver.general.BaseBean().writeLog("==========统一待办(wfAgentCondition.java --> operateToDo)==========");
//			        }
//			    }
//			 }
//	    }catch(Exception e){
//	    	rs2.writeLog("=========="+e.getMessage());
//	    	return	agentretur="3";
//	    	//e.getMessage();
//	    }
//		    return agentretur;
//	 }
//
//
//
//
//	/**
//	 * 重新设置代理【使用地方：编辑流程代理日期时（如果修改的日期时当前代理已经在代理中的，需要先收回在重新代理出去）】
//	 * @param beagenterId
//	 * @param agentId
//	 * @param workflowid
//	 * @param beginDate
//	 * @param beginTime
//	 * @param endDate
//	 * @param endTime
//	 * @param isCreateAgenter
//	 * @param isPendThing
//	 * @param isProxyDeal
//	 * @param user
//     * @param agentidNew 代理id
//     * @param methodtype  方法类型【编辑、添加代理条件目前是2个地方调用】
//	 */
//	public void again_agent_wf(String beagenterId,
//			String workflowid, String beginDate, String beginTime,
//			String endDate, String endTime
//			 , User user,String methodtype,String agentidNew ) {
//		RecordSet rs = new RecordSet();
//		RecordSet rs1 = new RecordSet();
//		RecordSet rs2 = new RecordSet();
//		RecordSet rs3 = new RecordSet();
//		String currentDate=TimeUtil.getCurrentDateString();
//		String currentTime=(TimeUtil.getCurrentTimeString()).substring(11,19);
//
//		String iseditstartdate="0";
//	    String iseditstarttime="0";
//	    String iseditenddate="0";
//	    String iseditendtime="0";
//
//	    if(beginDate.equals("")){
//	    	beginDate="1900-01-01";
//	    	iseditstartdate="1";
//	    }
//	    if(beginTime.equals("")){
//	    	beginTime="00:00";
//	    	iseditstarttime="1";
//	    }
//	    if(endDate.equals("")){
//	    	endDate="2099-12-31";
//	    	iseditenddate="1";
//	    }
//	    if(endTime.equals("")){
//	    	endTime="23:59";
//	    	iseditendtime="1";
//	    }
//		String agentid="";
//		if(agentidNew.equals("")&&methodtype.equals("agentconditonset")){//编辑代理时使用
//			agentid=""+this.getSequenceAgentId();//获取代理当前序列
//		}else{
//			agentid=agentidNew;
//		}
//		String sqlNew="";
//		if(methodtype.equals("agentconditonset")){//增加代理条件设置时，需要添加代理任务
//			sqlNew="select * from workflow_agentConditionSet where agenttype='1'  and workflowid='"
//				+ workflowid + "' and bagentuid='" + beagenterId + "' and agentid='"+agentid+"' order by agentbatch asc ";
//		}
//		else if(methodtype.equals("editAgentNew")){//增加代理条件设置时，需要添加代理任务
//		  sqlNew="select * from workflow_agentConditionSet where Recoverstate='1'  and  agenttype='0' and workflowid='"
//				+ workflowid + "' and bagentuid='" + beagenterId + "'  order by agentbatch asc ";
//		}
//		else{
//		 	sqlNew="select * from workflow_agentConditionSet where agenttype='1'  and workflowid='"
//				+ workflowid + "' and bagentuid='" + beagenterId + "' and agentid='"+agentid+"' order by agentbatch asc ";
//		}
//
//		rs2.executeSql(sqlNew);
//		while(rs2.next()){ //
//			String id=Util.null2String(rs2.getString("id"));
//			String bagentuid=Util.null2String(rs2.getString("bagentuid"));
//			String agentuid=Util.null2String(rs2.getString("agentuid"));
//			String conditionss=Util.null2String(rs2.getString("conditionss"));
//			String conditioncn=Util.null2String(rs2.getString("conditioncn"));
//			String conditionkeyid=Util.null2String(rs2.getString("conditionkeyid"));
//			String ruleRelationship=Util.null2String(rs2.getString("ruleRelationship"));
//			String isCreateAgenterNew=Util.null2String(rs2.getString("isCreateAgenter"));
//			String isProxyDealNew=Util.null2String(rs2.getString("isProxyDeal"));
//			String isPendThingNew=Util.null2String(rs2.getString("isPendThing"));
//			String agentbatch=Util.null2String(rs2.getString("agentbatch"));
//			String isSet=Util.null2String(rs2.getString("isSet"));
//			String backDate=Util.null2String(rs2.getString("isSet"));
//			String backTime=Util.null2String(rs2.getString("backTime"));
//			//有没有代理记录，有标示已经插入过一条。否则反之
//			if(!methodtype.equals("editAgent") && !methodtype.equals("editAgentNew")){
//				if(!this.agente_xists(agentid)){//编辑的时候新保存替换已有的流程，不需要重新插入主代理表
//					String agentsql="insert into workflow_agent(agentId,workflowId,beagenterId,agenterId,beginDate,beginTime,endDate,endTime,isCreateAgenter,agenttype,operatorid,operatordate,operatortime,isSet,isPending,backDate,backTime,isProxyDeal,iseditstartdate,iseditstarttime,iseditenddate,iseditendtime) values "
//									+ "("
//									+ agentid
//									+ ","
//									+ workflowid
//									+ ","
//									+ bagentuid
//									+ ","
//									+ agentuid
//									+ ",'"
//									+ beginDate
//									+ "','"
//									+ beginTime
//									+ "','"
//									+ endDate
//									+ "','"
//									+ endTime
//									+ "',"
//									+ isCreateAgenterNew
//									+ ",'1',"
//									+ user.getUID()
//									+ ",'"
//									+ currentDate
//									+ "','"
//									+ currentTime
//									+ "','0','"
//									+ isPendThingNew
//									+ "','','','" + isProxyDealNew + "','"+iseditstartdate+"','"+iseditstarttime+"','"+iseditenddate+"','"+iseditendtime+"')";//
//					rs1.executeSql(agentsql);
//				}
//			}else{
//				String editString = "update workflow_agent set  endDate='"+endDate+"',endTime='"+endTime+"' ," + " beginDate='"+ beginDate +"'" + ",beginTime='" + beginTime + "' ,iseditstartdate='"+iseditstartdate+"',iseditstarttime='"+iseditstarttime+"',iseditenddate='"+iseditenddate+"',iseditendtime='"+iseditendtime+"'  where agentId=" + agentid;
//				rs3.executeSql(editString);
//				//System.out.println(editString);
//			}
//			//==============特殊处理地方===============
//			if(methodtype.equals("editAgent") || methodtype.equals("editAgentNew")){//此处只能在编辑的时候使用
//				this.insertAgentConditionSet(""+agentuid,""+isCreateAgenterNew,""+isProxyDealNew,""+isPendThingNew,conditionss,conditioncn,beagenterId,beginDate,beginTime,endDate,endTime,agentbatch,""+agentid,conditionkeyid,""+workflowid,""+user.getUID(),currentDate,currentTime,isSet,backDate,backTime,"1",ruleRelationship);
//			    //rs.executeSql("delete workflow_agentConditionSet  where  id='"+id+"' ");
//				rs3.executeSql("delete workflow_agentConditionSet  where  id='"+id+"' ");
//			}
//			//==============特殊处理地方===end==========
//		 	String currentDateTime=TimeUtil.getCurrentTimeString();
//			String beginTimes=beginTime;
//		    if (beginTime.equals("")) beginTimes="00:00:00";
//		    else beginTimes = beginTimes+":00";
//			long timeIntervals=TimeUtil.timeInterval(beginDate+' '+beginTimes,currentDateTime);
//		   // System.out.println("=======isPendThingNew:"+isPendThingNew+"===timeIntervals:"+timeIntervals);
//		    if(isPendThingNew.equals("1")&&(beginDate.equals("")||timeIntervals>=0)){//判断是否开启了已有待办事宜进行代理
//		      String versionsIds =  WorkflowVersion.getAllVersionStringByWFIDs(workflowid);
//		      String  sql="select a.id,a.requestid,a.groupid,a.workflowid,a.workflowtype,a.usertype,a.nodeid,a.showorder,a.isremark,b.isbill,a.groupdetailid " +
//		      		"from workflow_currentoperator a,workflow_base b where a.workflowid=b.id and a.userid = " + beagenterId + " and a.isremark in ('0','1','5','7','8','9') and a.agenttype ='0' and a.agentorbyagentid ='-1' and a.workflowid in ("+versionsIds + ")";
//		      rs.executeSql(sql);
//		      ArrayList requestids=new ArrayList();
//		      ArrayList nodeids=new ArrayList();
//		      ArrayList isbills=new ArrayList();
//		      char separ = Util.getSeparator();
//		      String Procpara = "";
//		      boolean istrueagent=false;
//		      while(rs.next()){
//		        	//满足代理条件才可以进行代理，否则不添加任务
//			    	if(this.isagentcondite(Util.null2String(rs.getString("requestid")), workflowid, bagentuid, agentid,conditionkeyid)){
//		    		    requestids.add(rs.getString("requestid"));
//			            nodeids.add(rs.getString("nodeid"));
//			            isbills.add(rs.getString("isbill"));
//			            int coid1 = Util.getIntValue(rs.getString("id"));
//			    		istrueagent=true;
//			    		//让代理人获得任务
//						if(rs.getString("usertype").equals("0")){
//                            //QC152344,防止代理抄送引起的问题
//						    wfCurrentOperatorAgent(rs1,rs.getInt("requestid"),rs.getInt("nodeid"), rs.getString("usertype"), beagenterId,false);
//                            Procpara = rs.getString("requestid") + separ + ""+agentuid + separ + rs.getString("groupid") + separ + rs.getString("workflowid") + separ + rs.getString("workflowtype") + separ  + rs.getString("usertype") + separ + rs.getString("isremark") + separ+ rs.getString("nodeid") + separ + ""+beagenterId + separ + "2" + separ + rs.getString("showorder")+separ+rs.getInt("groupdetailid");
//                            rs1.executeProc("workflow_CurrentOperator_I", Procpara);
//						}
//			            if("1".equals(rs.getString("isremark"))){
//				            int coid2 = 0;
//				            rs1.execute("select max(id) as id from workflow_currentoperator where requestid="+rs.getString("requestid"));
//				            if(rs1.next()){
//				            	coid2 = Util.getIntValue(rs1.getString("id"));
//				            }
//				            rs3.executeSql("update workflow_forward set beforwardid = " + coid2 + " where requestid="+rs.getString("requestid")+" and beforwardid="+coid1);
//				            rs3.executeSql("update workflow_forward set forwardid = " + coid2 + " where requestid="+rs.getString("requestid")+" and forwardid="+coid1);
//			            }
//			    	}
//		        }
//
//		        try {
//		        	//add by xhheng @20051011 for TD2887,调用RequestAddShareInfo处理共享
//			        RequestAddShareInfo shareinfo = new RequestAddShareInfo();
//			        for(int j=0;j<requestids.size();j++){
//			            //将满足代理任务的流程值为已办
//			            sql="update workflow_currentoperator set isremark = '2', agenttype ='1', agentorbyagentid ="+agentuid + " where requestid='"+requestids.get(j)+"' and usertype=0 and userid = " + beagenterId + " and (agenttype is null or agenttype='0') and isremark in ('0','1','5','7','8','9') and agenttype ='0' and agentorbyagentid ='-1' and workflowid in ("+versionsIds + ")";
//				        rs1.executeSql(sql);
//			            shareinfo.setRequestid(Util.getIntValue((String)requestids.get(j)));
//			            shareinfo.SetWorkFlowID(Util.getIntValue(workflowid));
//			            shareinfo.SetNowNodeID(Util.getIntValue((String)nodeids.get(j)));
//			            shareinfo.SetNextNodeID(Util.getIntValue((String)nodeids.get(j)));
//			            shareinfo.setIsbill(Util.getIntValue((String)isbills.get(j),0));
//			            shareinfo.setUser(user);
//			            shareinfo.SetIsWorkFlow(1);
//			            shareinfo.setHaspassnode(true);
//			            shareinfo.addShareInfoFromAgent(beagenterId);
//			        }
//		        }catch (Exception e) {
//					e.printStackTrace();
//				}
//		    }
//		}
//	}
//
//	/**
//	 * 收回流程代理功能【使用地方：流程修改代理日期时 】
//	 * @param beaid 被代理人
//	 * @param auid	 代理人
//	 * @param aid	 流程代理主键
//	 * @param workflowid	流程id
//	 */
//	public void Agent_to_recover(String beaid,String workflowid,String agentid,String source,String agenterIdNew){
//
//		RecordSet rs = new RecordSet();
//		RecordSet rs1 = new RecordSet();
//		RecordSet rs2 = new RecordSet();
//		RecordSet rs3 = new RecordSet();
//		RecordSet rs4 = new RecordSet();
//		DocViewer DocViewer=new DocViewer();
//		PoppupRemindInfoUtil PoppupRemindInfoUtil=new PoppupRemindInfoUtil();
//		String currentDate=TimeUtil.getCurrentDateString();
//		String currentTime=(TimeUtil.getCurrentTimeString()).substring(11,19);
//
//		//先设置代理设置为无效操作
//		if(source.equals("agentrecoverold")){//此处主要是添加代理时，先收回【以新保存的代理设置替换已有重复的代理设置-出现时间范围重叠】
//			rs.executeSql("update workflow_agentConditionSet set agenttype='0'  where  bagentuid ='"+beaid+"'  and workflowid='"+workflowid+"' and agentid='"+agentid+"' and  isProxyDeal='1' ");
//			String sql="select 1 from workflow_agentConditionSet where agenttype = '1' and  agentid='"+agentid+"' " ;
//			rs4.executeSql(sql);
//			if(!rs4.next()){
//				rs1.executeSql(" update workflow_agent  set agenttype='0'   where agentid='"+agentid+"'");
//			}
//		}else if(source.equals("agentcondit")){//此收回主要做流程添加代理明细是使用
//			rs.executeSql("update workflow_agentConditionSet set agenttype = '0'  where  bagentuid ='"+beaid+"'  and workflowid='"+workflowid+"' and agentid='"+agentid+"'");
//		}else if(source.equals("agentcondittype")){//此收回主要做流程添加代理明细是使用
//			rs.executeSql("update workflow_agentConditionSet set agenttype = '0'  where  bagentuid ='"+beaid+"'  and workflowid='"+workflowid+"' and agentid='"+agentid+"' and isProxyDeal='1' ");
//			String sql="select 1 from workflow_agentConditionSet where agenttype = '1' and  agentid='"+agentid+"'  " ;
//			rs2.executeSql(sql);
//			if(!rs2.next()){
//				rs1.executeSql(" update workflow_agent  set agenttype='0'  where agentid='"+agentid+"'");
//			}
//		}else if(source.equals("editrecover")){//   编辑使用[不是重叠类型 但在代理中]
//			rs.executeSql("update workflow_agentConditionSet set agenttype = '0',Recoverstate='1' where  agenttype = '1' and agentid='"+agentid+"'");
//		} else if(source.equals("editAgent_cf")){// 编辑（重复叠加-有效）使用
//			rs.executeSql("update workflow_agentConditionSet set agenttype = '0'  where  bagentuid ='"+beaid+"'  and workflowid='"+workflowid+"' and agentid='"+agentid+"' and isProxyDeal='1' ");
//			String sql="select 1 from workflow_agentConditionSet where agenttype = '1' and  agentid='"+agentid+"'  " ;
//			rs2.executeSql(sql);
//			if(!rs2.next()){
//				rs1.executeSql(" update workflow_agent  set agenttype='0' where agentid='"+agentid+"'");
//			}
//		}else if(source.equals("editAgent_over")){//   编辑（已结束）使用
//			rs.executeSql("update workflow_agentConditionSet set agenttype = '0'  where  bagentuid ='"+beaid+"'  and workflowid='"+workflowid+"' and agentid='"+agentid+"' and isProxyDeal='1' ");
//			String sql="select 1 from workflow_agentConditionSet where agenttype = '1' and  agentid='"+agentid+"'  " ;
//			rs2.executeSql(sql);
//			if(!rs2.next()){
//				rs1.executeSql(" update workflow_agent  set agenttype='0'  where agentid='"+agentid+"'");
//			}
//		}else{
//			rs.executeSql("update set agenttype='0' workflow_agentConditionSet  where  agenttype = '1' and agentid='"+agentid+"' and  isProxyDeal='1'");//插入完成之后 删除掉
//			String sql="select 1 from workflow_agentConditionSet where agenttype = '1' and  agentid='"+agentid+"'  " ;
//			rs2.executeSql(sql);
//			if(!rs2.next()){
//				rs.executeSql(" update workflow_agent  set agenttype='0'    where agentid='"+agentid+"'");
//			}else{
//			}
//		}
//
//		String sql="select * from workflow_agentConditionSet where agentid='"+agentid+"'  and  isProxyDeal='1' ";
//		rs.executeSql(sql);
//		while(rs.next()){
//			String aid=Util.null2String(rs.getString("agentuid"));
//			String agentcoudid=Util.null2String(rs.getString("id"));
//			String updateSQL = "";
//		 	//查询出流程当前操作者表中的【满足3个条件：代理人 和被代理人 、流程】的所有待办流程
//			String versionsIds =  WorkflowVersion.getAllVersionStringByWFIDs(workflowid);
//			rs1.executeSql("select * from workflow_currentoperator where isremark in ('0','1','5','7','8','9') and userid = " + aid + " and agentorbyagentid = " + beaid + " and agenttype = '2' and workflowid in ( " + versionsIds + ")");//td2302 xwj
//			while(rs1.next()){
//				int wfcoid = Util.getIntValue(rs1.getString("id"));
//		   		String tmprequestid=rs1.getString("requestid");
//		   		String tmpisremark=rs1.getString("isremark");
//		   		int tmpgroupid=rs1.getInt("groupid");
//		   		int currentnodeid = rs1.getInt("nodeid");//流程当前所在节点
//		   		int tmpuserid=rs1.getInt("userid");
//		  		String tmpusertype=rs1.getString("usertype");
//		   		int tmppreisremark=Util.getIntValue(rs1.getString("preisremark"),0);
//                String tmptakisremark=Util.null2String(rs1.getString("takisremark"),"null");
//				int upcoid = 0;
//				//查询出被代理人当前节点代理出去的流程
//				rs2.execute("select id from workflow_currentoperator where requestid = " + tmprequestid + " and isremark = '2' and userid = " + rs1.getString("agentorbyagentid") + " and agenttype = '1'  and agentorbyagentid = " + tmpuserid+" and usertype=0 and groupid="+tmpgroupid+" and nodeid="+currentnodeid);
//				if(rs2.next()){
//					upcoid = Util.getIntValue(rs2.getString("id"), 0);
//					//修改掉被代理人的操作状态。【以前代理出去了isremark为2 ，收回需要改为代理人目前的状态】 并且将代理关联信息字段去掉【让被代理人重获任务流程】
//                    updateSQL = "update workflow_currentoperator set isremark = '" + tmpisremark + "',preisremark='"+tmppreisremark+"', agenttype ='0', agentorbyagentid = -1  where id = " + upcoid;
//					rs3.executeSql(updateSQL);  //被代理人重新获得任务
//					//代理人的流程需要清理掉【即：失效的代理人删除】
//					rs3.executeSql("delete workflow_currentoperator where id="+wfcoid);
//					rs3.executeSql("update workflow_forward set beforwardid = " + upcoid + " where requestid="+tmprequestid+" and beforwardid="+wfcoid);
//					rs3.executeSql("update workflow_forward set forwardid = " + upcoid + " where requestid="+tmprequestid+" and forwardid="+wfcoid);
//				}
//				//消息提醒
//		   		PoppupRemindInfoUtil.updatePoppupRemindInfo(tmpuserid,10,tmpusertype,Util.getIntValue(tmprequestid));
//		   		PoppupRemindInfoUtil.updatePoppupRemindInfo(tmpuserid,0,tmpusertype,Util.getIntValue(tmprequestid));
//
//		   		//流程代理收回导致操作人查不到流程【将islasttimes值改为1】
//		   		rs3.executeSql("select id from workflow_currentoperator where isremark in ('0','1','5','7','8','9') and requestid ="+tmprequestid+" and userid="+tmpuserid+" and usertype="+tmpusertype+" order by id desc ");
//		   		if(rs3.next()){
//		       		rs2.executeSql("update workflow_currentoperator set islasttimes=1 where requestid=" +tmprequestid + " and userid=" + tmpuserid + " and id = " + rs3.getString("id"));
//		   		}
//                //QC152344,防止代理抄送引起的问题
//                wfCurrentOperatorAgent(rs2,rs.getInt("requestid"),rs.getInt("nodeid"), rs.getString("usertype"), beaid,false);
//
//		   		//回收代理人文档权限
//		   		rs2.executeSql("select distinct docid,sharelevel from Workflow_DocShareInfo where requestid="+tmprequestid+" and userid="+aid+" and beAgentid="+beaid);
//		   		boolean hasrow=false;
//		   		ArrayList docslist=new ArrayList();
//		   		ArrayList sharlevellist=new ArrayList();
//		   		while(rs2.next()){
//		       		hasrow=true;
//		       		docslist.add(rs2.getString("docid"));
//		       		sharlevellist.add(rs2.getString("sharelevel"));
//		   		}
//		   		if(hasrow){
//		       		rs2.executeSql("delete Workflow_DocShareInfo where requestid="+tmprequestid+" and userid="+aid+" and beAgentid="+beaid);
//		   		}
//		   		for(int j=0;j<docslist.size();j++){
//		       		rs3.executeSql("select Max(sharelevel) sharelevel from Workflow_DocShareInfo where docid="+docslist.get(j)+" and userid="+aid);
//		       		if(rs3.next()){
//		          		int sharelevel=Util.getIntValue(rs3.getString("sharelevel"),0);
//		          		if(sharelevel>0){
//		              		rs.executeSql("update DocShare set sharelevel="+sharelevel+" where sharesource=1 and docid="+docslist.get(j)+" and userid="+aid+" and sharelevel>"+sharelevel);
//		          		}else{
//		              		rs.executeSql("delete DocShare where sharesource=1 and docid="+docslist.get(j)+" and userid="+aid);
//		          		}
//		       		}else{
//		          		rs.executeSql("delete DocShare where sharesource=1 and docid="+docslist.get(j)+" and userid="+aid);
//		       		}
//		       		//重新赋予被代理人文档权限
//		       		rs.executeSql("update DocShare set sharelevel="+sharlevellist.get(j)+" where sharesource=1 and docid="+docslist.get(j)+" and userid="+beaid);
//		       		try {
//						DocViewer.setDocShareByDoc((String)docslist.get(j));
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//		   		}
//		  	}
//			if(!source.equals("editrecover") ){
//				rs.executeSql(" update workflow_agentConditionSet  set agenttype='0'  where id='"+agentcoudid+"'");//插入完成之后 删除掉
//			}
//		 }
//
//		//System.out.println(source+"==============代理收回结束===============");
//	}
//
//	/**
//	 * 是否满足批次条件
//	 *
//	 * @return
//	 */
//	public boolean isagentcondite(String requestid,String workflowid,String bagentuid,String agentid,String conditionkeyid){
//		RecordSet rs = new RecordSet();
//		RuleInterface rif = new RuleInterface();
//		boolean newcop=true;
//		String sql="select * from workflow_agentConditionSet where conditionkeyid='"+conditionkeyid+"' and workflowid='"+workflowid+"' and bagentuid='"+bagentuid+"' and agentid='"+agentid+"' ";
//		rs.executeSql(sql);//agentuid,conditionss,conditionkeyid
//		String agentuid="";
//		String conditionss="";
//		if(rs.next()){
//			agentuid=Util.null2String(rs.getString("agentuid"));
//			conditionss=Util.null2String(rs.getString("conditionss"));
//			if(!conditionss.equals("")){//如果改条件为空 则直接返回true
//			    newcop = rif.compareRuleforWF(conditionss, requestid+"", conditionkeyid+"", RuleInterface.RULESRC_AGENT);
//			}else{
//				newcop=true;
//			}
//		}
//		return newcop;
//	}
//
//
//	/**
//	 * 删除代理条件[添加代理条件时使用]
//	 * @param agentid
//	 * @param workflowid
//	 */
//	public void deleteConditSet(String agentid,String workflowid){
//		RecordSet rs = new RecordSet();
//		String retustr=this.getAgentType(""+agentid);
//			if(retustr.equals("1")){//代理中[代理中的流程，需要将代理中的先收回 然后再重新代理一下]
//				rs.executeSql("select * from workflow_agentConditionSet  where agenttype='1' and agentid='"+agentid+"' and workflowid='"+workflowid+"'");
//				while(rs.next()){
//					String bagentuid=Util.null2String(rs.getString("bagentuid"));
//					String agentuid=Util.null2String(rs.getString("agentuid"));
//				    this.Agent_to_recover(bagentuid, workflowid, agentid,"agentcondit",agentuid);//收回已经在代理中的流程
//				}
//			  //删除代理设置【其实此处无需删除，收回之后流程代理就会设置为无效状态-（本次现加上）】
//			  rs.executeSql("delete from workflow_agentConditionSet where agentid='"+agentid+"'  ");
//			}else if(retustr.equals("3")){//未开始
//			  rs.executeSql("delete from workflow_agentConditionSet where agentid='"+agentid+"'  ");
//			}else {//2已结束
//				//已结束的暂时不用处理
//				 rs.executeSql("delete from workflow_agentConditionSet where agentid='"+agentid+"'  ");
//			}
//	}
//
//	/**
//	 * 是否显示收回全部代理
//	 * @return
//	 */
//	public boolean isshowAgent(String agentid){
//		RecordSet rs = new RecordSet();
//		rs.executeSql("select * from workflow_agentConditionSet  where agenttype='1' and agentid='"+agentid+"'");
//		if(rs.getCounts()>1){
//			return false;
//		}
//		return true;
//	}
//	public void FormatDM(){
//
//
//
//	}
//
//    /**
//     * 重写插入节点操作者的方法(被代理人，未操作的情况下不将islasttimes设定为0)
//     * @param rst
//     * @param opertor
//     * @param operatorgroup
//     * @param opertortype
//     * @param agenterId
//     * @param showorder
//     * @return
//     */
//    public boolean wfCurrentOperatorAgent(RecordSet rst,
//            int requestid,
//            int nodeid,
//            String opertortype,
//            String agenterId,
//            boolean bakcFlag){
//        boolean returnFlag = false;
//
//        try {
//            if(requestid > 0 &&  !"".equals(agenterId)){
//                //判断当前操作者的状态是否为被代理人，是否未操作
//                String execSql = " select * from workflow_currentoperator where requestid = ? ";
//                execSql += " AND userid = ? ";
//                execSql += " AND usertype = ? ";
//                execSql += " AND nodeid = ? ";
//                execSql += " AND isremark = 0 ";
//                execSql += " AND agenttype = 2 ";
//                rst.executeQuery(execSql,requestid,agenterId,opertortype,nodeid);
//                if(rst.next()){
//                    //判断是否取消代理
//                    if(bakcFlag){
//                        execSql = " update workflow_currentoperator ";
//                        execSql += "     set islasttimes = 0 ";
//                        execSql += "   where requestid = ? ";
//                        execSql += " AND userid = ? ";
//                        execSql += " AND usertype = ? ";
//                        execSql += " AND nodeid = ? ";
//                        execSql += " AND isremark = 0 ";
//                        execSql += " AND agenttype = 2 ";
//                        rst.executeQuery(execSql,requestid,agenterId,opertortype,nodeid);
//                        execSql = " update workflow_currentoperator ";
//                        execSql += "     set islasttimes = 1 ";
//                        execSql += "   where requestid = ? ";
//                        execSql += " AND userid = ? ";
//                        execSql += " AND usertype = ? ";
//                        execSql += " AND nodeid = ? ";
//                        execSql += " AND isremark = 2 ";
//                        execSql += " AND agenttype = 1 ";
//                        rst.executeQuery(execSql,requestid,agenterId,opertortype,nodeid);
//                    }else{
//                        execSql = " update workflow_currentoperator ";
//                        execSql += "     set islasttimes = 1 ";
//                        execSql += "   where requestid = ? ";
//                        execSql += " AND userid = ? ";
//                        execSql += " AND usertype = ? ";
//                        execSql += " AND nodeid = ? ";
//                        execSql += " AND isremark = 0 ";
//                        execSql += " AND agenttype = 2 ";
//                        rst.executeQuery(execSql,requestid,agenterId,opertortype,nodeid);
//                        execSql = " update workflow_currentoperator ";
//                        execSql += "     set islasttimes = 0 ";
//                        execSql += "   where requestid = ? ";
//                        execSql += " AND userid = ? ";
//                        execSql += " AND usertype = ? ";
//                        execSql += " AND nodeid = ? ";
//                        execSql += " AND isremark = 2 ";
//                        execSql += " AND agenttype = 1 ";
//                        rst.executeQuery(execSql,requestid,agenterId,opertortype,nodeid);
//                    }
//
//                    returnFlag = true;
//                }else{
//                    return false;
//                }
//            }else{
//                return false;
//            }
//        } catch (Exception e) {
//            return false;
//        }
//        return returnFlag;
//    }
//
//	/**
//     * 代理取消后，更新workflow_requestlog表中被代理人未操作对应的接收人显示名
//     * @param rst
//     * @param opertor
//     * @param operatorgroup
//     * @param opertortype
//     * @param agenterId
//     * @param showorder
//     * @return
//     */
//    public boolean wfCancleOperatorAgent(RecordSet rst,
//            int requestid,
//            int nodeid,
//            String opertortype,
//            String beagentid,
//            boolean bakcFlag){
//        boolean returnFlag = false;
//        int zdlcoid = 0;
//        String recepsonid="";
//        String recepson="";
//        if(rst.next()){
//            zdlcoid = Util.getIntValue(rst.getString("logid"), 0);
//            //取接收人和接收人id
//            String receivesql = "select cast(receivedpersonids as varchar(4000)) receivedpersonids,cast(receivedPersons as varchar(4000)) receivedPersons from workflow_requestLog where LOGID="+zdlcoid;
//            rst.executeSql(receivesql);
//            if(rst.next()){
//                recepsonid = rst.getString("receivedpersonids");
//                recepson = rst.getString("receivedPersons");
//
//                //拆分人和人id，因为是一一对应，所以取id来判断
//                String[] receid = recepsonid.split(",");
//                String[] rece = recepson.split(",");
//
//                for(int i = 0; i < receid.length; i++){
//                    if(receid[i].equals(beagentid) && rece[i].indexOf("->") > -1){//被代理人在哪个 数组中
//                        rece[i]  = rece[i].substring(0,rece[i].indexOf("->"));//截取字符串中对应的->前面的字符串
//                    }
//                }
//
//                //把receivedPersons数组组合成字符串
//                String sb ="";
//                for(int j = 0; j < rece.length; j++){
//                    sb = sb + rece[j] + ",";
//                }
//            String zdls = sb.toString();
//            String uzdlsql = "update workflow_requestLog SET receivedPersons= '" + zdls + "' where requestid = " + requestid +" and destnodeid= "+nodeid;
//            rst.executeSql(uzdlsql);
//            }
//        }
//
//        return returnFlag;
//    }
//
//}
