//
///*
// *
// * Copyright (c) 2001-2006 泛微软件.
// * 泛微协同商务系统,版权所有.
// *
// */
//
//package weaver.workflow.request;
///**
// * Title:        根据设置，把代办事宜转给代理人
//
// * @author:      ben
// * @version:     1.0
// * Description:  根据设置，把代办事宜转给代理人
//
// */
//import java.util.*;
//
//import weaver.workflow.request.RequestAddShareInfo;
//import weaver.workflow.workflow.WorkflowVersion;
//import weaver.conn.*;
//import weaver.general.*;
//import weaver.hrm.User;
//import weaver.interfaces.erp.PostWorkflowInfo;
//import weaver.system.ThreadWork;
//import weaver.systeminfo.SystemEnv;
//import weaver.workflow.msg.PoppupRemindInfoUtil;
//import weaver.docs.docs.DocViewer;
//public class WorkflowAgentOp extends BaseBean implements ThreadWork{
//	private User user;
//	private PoppupRemindInfoUtil pop;
//	private String currentDay;
//	private DocViewer docViewer;
//	private wfAgentCondition wfAgentCondition;
//	/**
//	 * 生成代理
//	 */
//    public void doThreadWork ()
//
//    {
//
//
//    	RecordSet RecordSet = new RecordSet();
//	   	RecordSet RecordSetu= new RecordSet();
//	   	RecordSet RecordSetd= new RecordSet();
//	   	RecordSet rs= new RecordSet();
//	   	RecordSet rs2= new RecordSet();
//	   	RecordSet rs3= new RecordSet();
//		int agenterId;
//		int beagenterId;
//		int beagentwfid;
//
//		String conditionkeyid;
//		try
//		{
//			 String currentDate=TimeUtil.getCurrentTimeString();
//			//过期的代理收回 MYQ 2008.1.8 START
//			//System.out.println("时间到，开始收回");
//	    	if (RecordSet.getDBType().equals("sqlserver")){
//	    		RecordSet.execute("select t2.* from workflow_agent t1,workflow_agentConditionSet t2 where  t1.agentid = t2.agentid  and t1.workflowid= t2.workflowid and t1.beagenterid=t2.bagentuid and t1.agenttype='1' and (t1.endDate is not null and t1.endDate!='') and (t1.endDate+' '+t1.endtime<'"+currentDate+"')  order by t1.agentid asc ");
//		    }else{
//		    	RecordSet.execute("select t2.* from workflow_agent t1,workflow_agentConditionSet t2 where  t1.agentid = t2.agentid  and t1.workflowid= t2.workflowid and t1.beagenterid=t2.bagentuid and t1.agenttype='1' and (t1.endDate is not null ) and (t1.endDate||' '||t1.endtime<'"+currentDate+"') order by t1.agentid asc ");
//		    }
//		    while (RecordSet.next()){
//		    	//收回流转中的代理
//		    	String	agentid=RecordSet.getString("agentid");
//		    	String	id=RecordSet.getString("id");
//		    	String aid = RecordSet.getString("agentuid");
//		    	String beaid = RecordSet.getString("bagentuid");
//		    	int workflowid = RecordSet.getInt("workflowId");
//		    	String versionsIds =  WorkflowVersion.getAllVersionStringByWFIDs("" + workflowid);
//
//		    	rs.executeSql("select * from workflow_currentoperator where isremark in ('0','1','5','7','8','9')   and userid = " + aid + " and agentorbyagentid = " + beaid + " and agenttype = '2' and workflowId in ("+ versionsIds+")");
//		    	String updateSQL = "";
//		    	while(rs.next()){
//		    		int coid1= Util.getIntValue(rs.getString("id"));
//		    		String tmprequestid=rs.getString("requestid");
//	   				String tmpisremark=rs.getString("isremark");
//	   				int currentnodeid = rs.getInt("nodeid");//流程当前所在节点
//	   				int tmpuserid=rs.getInt("userid");
//	   				int tmpgroupid=rs.getInt("groupid");
//	   				String agentorbyagentid = rs.getString("agentorbyagentid");
//	   				String tmpusertype=rs.getString("usertype");
//	   				String selectsql = "select id from workflow_currentoperator where requestid = " + tmprequestid + " and isremark = '2' and userid = " + rs.getString("agentorbyagentid") + " and agenttype = '1' "+
//										    " and agentorbyagentid = " + rs.getString("userid") +" and usertype=0 and groupid="+tmpgroupid+" and nodeid="+currentnodeid;
//	   				RecordSetd.execute(selectsql);
//	   				if(RecordSetd.next()){
//	   				   int coid2 = Util.getIntValue(RecordSetd.getString("id"));
//	   				   updateSQL = "update workflow_currentoperator set isremark = '" + tmpisremark + "', agenttype ='0', agentorbyagentid = -1 " +
//										    " where id = " + coid2;
//						RecordSetu.executeSql(updateSQL);//被代理人重新获得任务
//						//失效的代理人删除
//						RecordSetu.executeSql("delete workflow_currentoperator where id="+coid1);//td2302 xwj
//
//						//流程代理收回操作人需要查看到流程
//						RecordSetd.executeSql("select id from workflow_currentoperator where requestid ="+tmprequestid+" and userid="+tmpuserid+" and usertype="+tmpusertype+" order by id desc ");
//				    if(RecordSetd.next()){
//				        String agentcurrid = RecordSetd.getString("id");
//				    	RecordSetu.executeSql("update workflow_currentoperator set islasttimes=0 where requestid=" +tmprequestid + " and userid=" + tmpuserid);
//				    	RecordSetu.executeSql("update workflow_currentoperator set islasttimes=1 where requestid=" +tmprequestid + " and userid=" + tmpuserid + " and id = " + agentcurrid);
//						RecordSetu.executeSql("update workflow_forward set beforwardid = " + coid2 + " where requestid="+tmprequestid+" and beforwardid="+coid1);
//						RecordSetu.executeSql("update workflow_forward set forwardid = " + coid2 + " where requestid="+tmprequestid+" and forwardid="+coid1);
//				    }
//				    //回收代理人文档权限
//				    RecordSetu.executeSql("select distinct docid,sharelevel from Workflow_DocShareInfo where requestid="+tmprequestid+" and userid="+aid+" and beAgentid="+beaid);
//				    boolean hasrow=false;
//				    ArrayList docslist=new ArrayList();
//				    ArrayList sharlevellist=new ArrayList();
//				    while(RecordSetu.next()){
//				       hasrow=true;
//				       docslist.add(RecordSetu.getString("docid"));
//				       sharlevellist.add(RecordSetu.getString("sharelevel"));
//				    }
//				    if(hasrow){
//				       RecordSetu.executeSql("delete Workflow_DocShareInfo where requestid="+tmprequestid+" and userid="+aid+" and beAgentid="+beaid);
//				    }
//				    for(int j=0;j<docslist.size();j++){
//				       RecordSetu.executeSql("select Max(sharelevel) sharelevel from Workflow_DocShareInfo where docid="+docslist.get(j)+" and userid="+aid);
//				       if(RecordSetu.next()){
//				          int sharelevel=Util.getIntValue(RecordSetu.getString("sharelevel"),0);
//				          if(sharelevel>0){
//				              RecordSetd.executeSql("update DocShare set sharelevel="+sharelevel+" where sharesource=1 and docid="+docslist.get(j)+" and userid="+aid+" and sharelevel>"+sharelevel);
//				          }else{
//				              RecordSetd.executeSql("delete DocShare where sharesource=1 and docid="+docslist.get(j)+" and userid="+aid);
//				          }
//				       }else{
//				          RecordSetd.executeSql("delete DocShare where sharesource=1 and docid="+docslist.get(j)+" and userid="+aid);
//				       }
//				       //重新赋予被代理人文档权限
//				       RecordSetd.executeSql("update DocShare set sharelevel="+sharlevellist.get(j)+" where sharesource=1 and docid="+docslist.get(j)+" and userid="+beaid);
//				       docViewer.setDocShareByDoc((String)docslist.get(j));
//				    }
//	   			  }
//
//	   				new BaseBean().writeLog("==========代理自动收回入口==========");
//	   				PostWorkflowInfo postWorkflowInfo = new PostWorkflowInfo();
//	   				postWorkflowInfo.deleteUserToDo(tmprequestid + "", tmpuserid + "");
//	   				writeLog("==========统一待办(WorkflowAgentOp.java --> deleteUserToDo1)==========");
//	   				postWorkflowInfo.deleteUserToDo(tmprequestid + "", agentorbyagentid);
//	   				writeLog("==========统一待办(WorkflowAgentOp.java --> deleteUserToDo2)==========");
//		    	}
//				rs2.executeSql(" update workflow_agentConditionSet set agenttype='0' where id ='"+id+"' ");
//				//检查明细是否都无效了
//				String sql="select 1 from workflow_agentConditionSet where agenttype = '1' and  agentid='"+agentid+"'  " ;
//				rs2.executeSql(sql);
//				if(!rs2.next()){
//					rs3.executeSql(" update workflow_agent  set agenttype='0'  where agentid='"+agentid+"'");
//				}
//		    }
//
//
//    	//过期的代理收回 MYQ 2008.1.8 END
//
//
//	    //===流程自动代理开始
//	    if (rs3.getDBType().equals("sqlserver"))
//	    {
//	    	RecordSet.execute("select t2.* from workflow_agent  t1,workflow_agentConditionSet t2 where  t1.agentid = t2.agentid  and t1.workflowid= t2.workflowid and t1.beagenterid=t2.bagentuid and t1.agenttype = '1'  and isnull(t1.isSet,'0')!='1' and t1.ispending='1'  and (t1.beginDate is not null and t1.beginDate!='') and (t1.beginDate+' '+t1.begintime<='"+currentDate+"') and (t1.endDate is null or t1.endDate='' or (t1.endDate+' '+t1.endtime>'"+currentDate+"')) order by t1.agentid asc  ");
//    	}
//		else
//		{
//			RecordSet.execute("select t2.* from workflow_agent t1,workflow_agentConditionSet t2 where  t1.agentid = t2.agentid  and t1.workflowid= t2.workflowid and t1.beagenterid=t2.bagentuid and t1.agenttype = '1'  and nvl(t1.isSet,'0')!='1' and t1.ispending='1'  and (t1.beginDate is not null ) and (t1.beginDate||' '||t1.begintime<='"+currentDate+"') and (t1.endDate is null or t1.endDate='' or (t1.endDate||' '||t1.endtime>'"+currentDate+"')) order by t1.agentid asc ");
//		}
//	    //查询出所有开启，已有待办事宜代理开关且代理时间已经开始的流程
//		while (RecordSet.next())
//    	{
//		   int agentId=RecordSet.getInt("agentid");
//           agenterId=RecordSet.getInt("agentuid");
//		   beagenterId=RecordSet.getInt("bagentuid");
//		   beagentwfid=RecordSet.getInt("workflowid");
//		   conditionkeyid=RecordSet.getString("conditionkeyid");
//           setAgent(agenterId,beagenterId,beagentwfid,agentId,conditionkeyid);
//           RecordSet.execute("update workflow_agentConditionSet set isSet='1' where agentid='"+agentId+"'");
//    	}
//	}
//    catch (Exception e)
//	{
//    		e.printStackTrace();
//	}
//      //rs.writeLog("============================本次扫描已结束（WorkflowAgentOp）==================");
//    }
//
//	public WorkflowAgentOp()
//	{
//        //System.out.print("agent.....");
//		wfAgentCondition=new wfAgentCondition();
//		currentDay=TimeUtil.getCurrentDateString();
//		pop=new PoppupRemindInfoUtil();
//		user=new User();
//		user.setUid(-100);
//		user.setLogintype("1");
//		docViewer = new DocViewer();
//	}
//	/**
//	 * 根据设置把被代理人的代办事宜转给代理人
//	 * @param agenterId 代理人
//     * @param beagenterId  被代理人
//	 * @param beagentwfid  代理流程
//	 */
//	public void setAgent(int agenterId,int beagenterId,int beagentwfid,int agentid,String conditionkeyid) throws Exception
//	{
//		RecordSet RecordSetu= new RecordSet();
//	   	RecordSet RecordSetd= new RecordSet();
//	   	RecordSet rs= new RecordSet();
//		char separ = Util.getSeparator();
//		String versionsIds =  WorkflowVersion.getAllVersionStringByWFIDs("" + beagentwfid);
//        String sql="select a.id,a.requestid,a.groupid,a.workflowid,a.workflowtype,a.usertype,a.nodeid,a.showorder,b.isbill,a.groupdetailid,a.isremark from workflow_currentoperator a,workflow_base b where a.workflowid=b.id and a.usertype=0 and a.userid = " + beagenterId + " and a.isremark in ('0','1') and agenttype ='0' and agentorbyagentid ='-1' and workflowid in ("+versionsIds +")";
//        rs.executeSql(sql);
//        while(rs.next()){
//        	//满足代理条件才可以进行代理，否则不添加任务
//        	if(wfAgentCondition.isagentcondite(Util.null2String(rs.getString("requestid")), ""+beagentwfid, ""+beagenterId, ""+agentid,""+conditionkeyid)){
//	            //让代理人获得任务
//	            String Procpara = rs.getString("requestid") + separ + ""+agenterId + separ + rs.getString("groupid") + separ + rs.getString("workflowid") + separ + rs.getString("workflowtype") + separ  + rs.getString("usertype") + separ + rs.getString("isremark") + separ+ rs.getString("nodeid") + separ + ""+beagenterId + separ + "2" + separ + rs.getString("showorder")+separ+rs.getInt("groupdetailid");
//	            //处理转发代理的问题
//	            int coid1 = Util.getIntValue(rs.getString("id"));
//
//                //QC152344,防止代理抄送引起的问题
//	            wfAgentCondition.wfCurrentOperatorAgent(RecordSetu,rs.getInt("requestid"),rs.getInt("nodeid"), rs.getString("usertype"), String.valueOf(beagenterId),false);
//                RecordSetu.executeProc("workflow_CurrentOperator_I", Procpara);
//	            if("1".equals(rs.getString("isremark"))){
//		            int coid2 = 0;
//		            RecordSetu.execute("select max(id) as id from workflow_currentoperator where requestid="+rs.getString("requestid"));
//		            if(RecordSetu.next()){
//		            	coid2 = Util.getIntValue(RecordSetu.getString("id"));
//		            }
//		            RecordSetu.executeSql("update workflow_forward set beforwardid = " + coid2 + " where requestid="+rs.getString("requestid")+" and beforwardid="+coid1);
//					RecordSetu.executeSql("update workflow_forward set forwardid = " + coid2 + " where requestid="+rs.getString("requestid")+" and forwardid="+coid1);
//
//					//将当前节点的签字意见权限也同时给代理人，只处理转发的
//		            String reqid = rs.getString("requestid");
//		            String nid = rs.getString("nodeid");
//		            String qsql = " select logid from workflow_logviewusers where userid = " + beagenterId
//		            			+ " and exists (select 1 from workflow_requestLog where workflow_requestLog.requestid = " + reqid
//		            			+ " and workflow_requestLog.nodeid = " + nid
//		            			+ " and workflow_requestLog.logtype = '7' and workflow_logviewusers.logid = workflow_requestLog.logid)";
//		            RecordSet rs4 = new RecordSet();
//		            RecordSet rs5 = new RecordSet();
//		            rs4.executeSql(qsql);
//		            while(rs4.next()){
//		            	String logid = rs4.getString("logid");
//		            	if(!"".equals(logid) && !"-1".equals(logid)){
//		            		String isql = "insert into workflow_logviewusers (logid,userid) values (" + logid + "," + agenterId + ")";
//		            		rs5.executeSql(isql);
//		            	}
//		            }
//	            }
//
//	            RequestAddShareInfo shareinfo = new RequestAddShareInfo();
//	            shareinfo.setRequestid(rs.getInt("requestid"));
//	            shareinfo.SetWorkFlowID(rs.getInt("workflowid"));
//	            shareinfo.SetNowNodeID(rs.getInt("nodeid"));
//	            shareinfo.SetNextNodeID(rs.getInt("nodeid"));
//	            shareinfo.setIsbill(rs.getInt("isbill"));
//	            shareinfo.setUser(user);
//	            shareinfo.SetIsWorkFlow(1);
//	            shareinfo.setHaspassnode(false);
//	            shareinfo.addShareInfo();
//	            //根据流程来设置代理人的状态
//	            sql="update workflow_currentoperator set isremark = '2', agenttype ='1', agentorbyagentid ="+agenterId + " where usertype=0 and requestid='"+rs.getString("requestid")+"' and  userid = " + beagenterId + " and (isremark = '0' or isremark = '1') and agenttype ='0' and agentorbyagentid ='-1' and workflowid in ("+versionsIds +")";
//	            RecordSetd.executeSql(sql);
//	    	}
//
//        }
//
//	}
//
//}