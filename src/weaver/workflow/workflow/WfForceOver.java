///*
//*
//* Copyright (c) 2001-2006 泛微软件.
//* 泛微协同商务系统,版权所有.
//*
//*/
//package weaver.workflow.workflow;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import weaver.common.StringUtil;
//import weaver.conn.RecordSet;
//import weaver.conn.RecordSetTrans;
//import weaver.crm.Maint.CustomerInfoComInfo;
//import weaver.file.FileUpload;
//import weaver.fna.general.FnaCommon;
//import weaver.general.BaseBean;
//import weaver.general.GCONST;
//import weaver.general.TimeUtil;
//import weaver.general.Util;
//import weaver.hrm.HrmUserVarify;
//import weaver.hrm.User;
//import weaver.hrm.attendance.manager.HrmAttVacationManager;
//import weaver.hrm.resource.ResourceComInfo;
//import weaver.interfaces.erp.PostWorkflowInfo;
//import weaver.systeminfo.SystemEnv;
//import weaver.workflow.msg.PoppupRemindInfoUtil;
//import weaver.workflow.request.ComparatorUtilBean;
//import weaver.workflow.request.RequestAddShareInfo;
//import weaver.workflow.request.RequestNodeFlow;
//import weaver.workflow.request.RequestOperationLogManager;
//import weaver.workflow.request.RequestRemarkRight;
//import weaver.workflow.request.SendMsgAndMail;
//import weaver.workflow.request.SubWorkflowTriggerService;
//import weaver.workflow.request.WFLinkInfo;
//import weaver.workflow.request.WFUrgerManager;
//
///**
// * Description:流程的强制归档
// * @author zjf
// * @version 1.0
// */
//public class WfForceOver extends BaseBean {
//	private String remark;
//    private String annexdocids;
//    private String signdocids;
//    private String signworkflowids;
//    private int requestLogId;
//	private SendMsgAndMail sendMsgAndMail = null;
//    private String remarkLocation = "";
//	public WfForceOver() {
//		sendMsgAndMail = new SendMsgAndMail();
//		remark = "";
//        annexdocids="";
//        signdocids="";
//        signworkflowids="";
//        requestLogId=0;
//	}
//
//	public void doForceOver(ArrayList requestids,User user){
//		doForceOver(requestids,null,null,user);
//
//	}
//	public void doForceOver(
//			ArrayList requestids,
//			HttpServletRequest request,
//			HttpServletResponse response) {
//		doForceOver(requestids,request,response,null);
//	}
//
//
//	/**
//	 * Description: 执行流程的强制归档
//	 * @param requestids 流程ID串
//	 * @param request  servelet变量
//	 * @param response servelet变量
//	 */
//	public void doForceOver(
//		ArrayList requestids,
//		HttpServletRequest request,
//		HttpServletResponse response,User userParam) {
//	    RecordSet rs = new RecordSet();
//	    RecordSet rs1 = new RecordSet();
//	    RecordSetTrans rst = new RecordSetTrans();
//		RecordSet rs_1 = new RecordSet();
//		int wfForceOverLogic = 0;
//		String fnasql = "select wfForceOverLogic from FnaSystemSet";
//		rs_1.executeSql(fnasql);
//        if(rs_1.next()){
//        	wfForceOverLogic = Util.getIntValue(rs_1.getString("wfForceOverLogic"), 0);
//        }
//
//		String requestid = "";
//		Hashtable operatorsht = new Hashtable();
//		FileUpload fu =null;
//		String remoteAddr ="";
//		User user =null;
//		if(userParam==null){
//		  fu = new FileUpload(request);
//		  remoteAddr = request.getRemoteAddr();
//		  user = HrmUserVarify.getUser(request, response);
//		}else{
//			user=userParam;
//		}
//		int currNodeId = -1;
//		int currNodeType = -1;
//		int operNodeId = -1;
//		String billtablename = "";
//		String requestname = "";
//		int formid = -1;
//		int isbill = -1;
//		int billid = -1;
//		int totalgroups = 0;
//		int currentNodeId = -1;
//		int currentNodeType = -1;
//		int wfid = -1;
//		int wftype = -1;
//		int requestlevel = -1;
//		int creater = -1;
//		int creatertype = -1;
//		int userid = user.getUID();
//		int usertype = (user.getLogintype().equals("1")) ? 0 : 1;
//		int isreopen = 0;
//		int isreject = 0;
//		PoppupRemindInfoUtil poppupRemindInfoUtil = new PoppupRemindInfoUtil();
//		boolean hasnextnodeoperator = false;
//
//		String Procpara = "";
//		Calendar today = Calendar.getInstance();
//		String CurrentDate =
//			Util.add0(today.get(Calendar.YEAR), 4)
//				+ "-"
//				+ Util.add0(today.get(Calendar.MONTH) + 1, 2)
//				+ "-"
//				+ Util.add0(today.get(Calendar.DAY_OF_MONTH), 2);
//
//		String CurrentTime =
//			Util.add0(today.get(Calendar.HOUR_OF_DAY), 2)
//				+ ":"
//				+ Util.add0(today.get(Calendar.MINUTE), 2)
//				+ ":"
//				+ Util.add0(today.get(Calendar.SECOND), 2);
//		char flag = Util.getSeparator();
//		RequestRemarkRight remarkRight = new RequestRemarkRight();
//		for (int i = 0; i < requestids.size(); i++) {
//			requestid = (String) requestids.get(i);
//			if(operNodeId<0){//强制归档保留归档前节点的id,方便查询代理关系
//				rs.executeSql("select currentnodeid from workflow_requestbase where requestid="+requestid);
//				if(rs.next()){
//					operNodeId = rs.getInt("currentnodeid");
//				}
//			}
//			rs.executeProc("workflow_Requestbase_SByID", requestid + "");
//			if (rs.next()) {
//				requestname = rs.getString("requestname");
//				currNodeId = rs.getInt("currentnodeid");
//				requestlevel = rs.getInt("requestlevel");
//				wfid = rs.getInt("workflowid");
//				creater = Util.getIntValue(rs.getString("creater"), 0);
//				creatertype = Util.getIntValue(rs.getString("creatertype"), 0);
//				currNodeType =
//					Util.getIntValue(rs.getString("currentnodetype"), 0);
//			}
//
//			int rolmIsRemark = -1;
//			String src = "forceover";
//			RequestOperationLogManager rolm = new RequestOperationLogManager(Util.getIntValue(requestid), currNodeId, rolmIsRemark, user.getUID(), user.getType(), CurrentDate, CurrentTime, src);
//			//开始记录日志
//			rolm.flowTransStartBefore();
//
//			rs.executeSql(
//				"select nodeid from workflow_flownode where workflowid = "
//					+ wfid + " and nodetype = 3");
//			if (rs.next()) {
//				currentNodeId = rs.getInt("nodeid");
//				currentNodeType = 3;
//			}
//			rs.executeSql(
//				"select workflowtype from workflow_base where id = " + wfid);
//			if (rs.next()) {
//				wftype = rs.getInt("workflowtype");
//			}
//
//			rs.executeProc("workflow_Workflowbase_SByID", "" + wfid);
//			if (rs.next()) {
//				isbill = Util.getIntValue(rs.getString("isbill"), -1);
//				formid = Util.getIntValue(rs.getString("formid"), -1);
//			}
//
//			if (isbill == 1) {
//				rs.executeSql(
//					"select tablename from workflow_bill where id = " + formid);
//				if (rs.next())
//					billtablename = rs.getString("tablename");
//			}
//
//			if (isbill == 1 && !"".equals(billtablename)) {
//				rs.executeSql(
//					"select id from "
//						+ billtablename
//						+ " where requestid = "
//						+ requestid);
//				if (rs.next()) {
//					billid = rs.getInt("id");
//				}
//			}
//
//			if (currentNodeType == 0) {
//				isreopen = 1;
//			}
//
//			int prenodeid = 0;
//			String prenodetype = "0";
//
//			if(rs.getDBType().equals("oracle")){
//				rs.executeSql(
//						"select * from workflow_nodelink where wfrequestid is null and workflowid = "
//						+ wfid
//						+ " and destnodeid = "
//						+ currentNodeId
//						+ " and ((isreject <>'1' and (dbms_lob.getlength(condition) is null or dbms_lob.getlength(condition) = 0)) or (isreject is null and condition is null)) order by nodepasstime,id");
//			}else{
//				rs.executeSql(
//					"select * from workflow_nodelink where wfrequestid is null and workflowid = "
//						+ wfid
//						+ " and destnodeid = "
//						+ currentNodeId
//						+ " and ((isreject <>'1' and condition is NOT null AND datalength(condition) = 0) or (isreject is null and condition is null)) order by nodepasstime,id");
//			}
//			if (rs.next()) {
//				prenodeid = rs.getInt("nodeid");
//				isreject = Util.getIntValue(rs.getString("isreject"), 0);
//			}
//
//			rs.executeSql(
//				"select * from workflow_flownode where workflowid = "
//					+ wfid
//					+ " and nodeid = "
//					+ prenodeid);
//			if (rs.next()) {
//				prenodetype = rs.getString("nodetype");
//			}
//
//			try {
//				RequestNodeFlow requestNodeFlow = new RequestNodeFlow();
//				ResourceComInfo resourceComInfo = new ResourceComInfo();
//
//				//是否开启子流程全部归档才能提交,子流程归档时调用
//				if(WFSubDataAggregation.checkSubProcessSummary(Integer.parseInt(requestid))){
//					String cmainRequestId = SubWorkflowTriggerService.getMainRequestId(Integer.parseInt(requestid));
//					if (cmainRequestId != null && !cmainRequestId.isEmpty()) {
//						WFSubDataAggregation.addMainRequestDetail(cmainRequestId,requestid,user);
//					}
//				}
//
//				//QC151617
//				CustomerInfoComInfo crmComInfo = new CustomerInfoComInfo();
//
//				//按照归档节点操作人条件设置强制归档接收人
//				requestNodeFlow.setRequestid(Integer.parseInt(requestid));
//				requestNodeFlow.setNodeid(prenodeid);
//				requestNodeFlow.setNodetype(prenodetype);
//				requestNodeFlow.setWorkflowid(wfid);
//				requestNodeFlow.setUserid(userid);
//				requestNodeFlow.setUsertype(usertype);
//				requestNodeFlow.setCreaterid(creater);
//				requestNodeFlow.setCreatertype(creatertype);
//				requestNodeFlow.setIsbill(isbill);
//				requestNodeFlow.setBillid(billid);
//				requestNodeFlow.setBilltablename(billtablename);
//				requestNodeFlow.setIsreject(isreject);
//				requestNodeFlow.setIsreopen(isreopen);
//				requestNodeFlow.setForceOver(true);
//				requestNodeFlow.setForceOverNodeId(currentNodeId);
//				requestNodeFlow.setRecordSet(rs);
//				hasnextnodeoperator = requestNodeFlow.getNextNodeOperator();
//
//				if (hasnextnodeoperator) {
//					operatorsht = requestNodeFlow.getOperators();
//				} else { //当运算归档节点接收人为空时，同时归档至流程创建人与当前操作人处
//					ArrayList arr = new ArrayList();
//					arr.add(creater + "_" + creatertype+"_0");
//					operatorsht.put("1", arr);
//					if (!(creater+"_"+creatertype).equals(userid+"_"+usertype)) {
//						arr = new ArrayList();
//						arr.add(userid + "_0"+"_"+"-1");
//						operatorsht.put("2", arr);
//					}
//				}
//
//				//更新 workflow_requestbase
//								float nodepasstime = 0;
//								String status =
//									SystemEnv.getHtmlLabelName(18360, user.getLanguage());
//								rs1.executeProc(
//									"workflow_NodeLink_SPasstime",
//									"" + currentNodeId + flag + "0");
//								if (rs1.next()) {
//									nodepasstime =
//										Util.getFloatValue(rs1.getString("nodepasstime"), -1);
//								}
//
//								String sql =
//									" update workflow_requestbase set "
//										+ " lastnodeid = "
//										+ currNodeId
//										+ " ,lastnodetype = '"
//										+ currNodeType
//										+ "' ,currentnodeid = "
//										+ currentNodeId
//										+ " ,currentnodetype = '"
//										+ currentNodeType
//										+ "' ,status = '"
//										+ status
//										+ "' "
//										+ " ,passedgroups = 0"
//										+ " ,totalgroups = "
//										+ operatorsht.size()
//										+ " ,lastoperator = "
//										+ userid
//										+ " ,lastoperatedate = '"
//										+ CurrentDate
//										+ "' "
//										+ " ,lastoperatetime = '"
//										+ CurrentTime
//										+ "' "
//										+ " ,lastoperatortype = "
//										+ usertype
//										+ " ,nodepasstime = "
//										+ nodepasstime
//										+ " ,nodelefttime = "
//										+ nodepasstime
//										+ " where requestid = "
//										+ requestid;
//
//								rs1.executeSql(sql);
//				                //更新当前节点表
//                                WFLinkInfo wflinkinfo=new WFLinkInfo();
//                                int nodeattr=wflinkinfo.getNodeAttribute(currentNodeId);
//                                rs1.executeSql("delete from workflow_nownode where requestid="+requestid);
//                                rs1.executeSql("insert into workflow_nownode(requestid,nownodeid,nownodetype,nownodeattribute) values("+requestid+","+currentNodeId+","+currentNodeType+","+nodeattr+")");
//								//添加归档节点操作人,考虑代理
//								String agentSQL = "";
//								boolean isbeAgent = false;
//								String agenterId = "";
//								String beginDate = "";
//								String beginTime = "";
//								String endDate = "";
//								String endTime = "";
//								String currentDate = "";
//								String currentTime = "";
//								String agenttype = "";
//				               //更新新到流程提醒信息
//								rs1.executeSql("select userid ,usertype from  workflow_currentoperator   where requestid = "
//								+ requestid
//								+ " and nodeid = "
//								+ currNodeId
//								+ " and isremark in ('0','1','8','9','7')");
//								while (rs1.next())
//								{
//									poppupRemindInfoUtil.updatePoppupRemindInfo(rs1.getInt(1),0,""+rs1.getInt(2),Integer.parseInt(requestid));
//
//								}
//				                 rs1.executeSql(
//									"update workflow_currentoperator set isremark = '2'  where requestid = "
//										+ requestid
//										+ " and nodeid = "
//										+ currNodeId
//										+ " and isremark in ('0','8','9','7')");
//
//				                String  beForwardids = "";
//				                rs1.executeSql("select BeForwardid from workflow_Forward w1,workflow_currentoperator w2 "+
//				                			   " where w1.requestid='"+requestid+"' and w2.isremark='1' and w1.BeForwardid=w2.id and (w1.IsBeForwardPending=0 or (w1.IsBeForwardPending=1 and w2.viewtype=-2))");
//				                while(rs1.next()){
//				                	beForwardids += Util.null2String(rs1.getString("BeForwardid"))+",";
//				                }
//				                if(!"".equals(beForwardids)){
//				                	beForwardids = beForwardids.substring(0,beForwardids.length()-1);
//				                	rs1.execute("update workflow_currentoperator set isremark = '2'  where requestid = "+requestid+" and id in ("+beForwardids+")");
//				                }
//
//
//				                List poppuplist=new ArrayList();
//								int showorder = 0;
//								String tempHrmIds = "";
//								TreeMap map = new TreeMap(new ComparatorUtilBean());
//								Enumeration tempKeys = operatorsht.keys();
//								while (tempKeys.hasMoreElements()) {
//									String tempKey = (String) tempKeys.nextElement();
//									ArrayList tempoperators =
//										(ArrayList) operatorsht.get(tempKey);
//									map.put(tempKey, tempoperators);
//								}
//								Iterator iterator = map.keySet().iterator();
//								while (iterator.hasNext()) {
//									String operatorgroup = (String) iterator.next();
//									ArrayList operators =
//										(ArrayList) operatorsht.get(operatorgroup);
//									for (int h = 0; h < operators.size(); h++) {
//										showorder++;
//										String operatorandtype = (String) operators.get(h);
//										String[] operatorandtypes =
//											Util.TokenizerString2(operatorandtype, "_");
//										String opertor = operatorandtypes[0];
//										String opertortype = operatorandtypes[1];
//										int groupdetailIds = Util.getIntValue(operatorandtypes[2],-1);
//										isbeAgent = false;
//
//										agentSQL =
//											" select agentorbyagentid,agenttype from workflow_currentoperator where userid="
//												+ opertor
//												+ " and agenttype='2' and requestid="
//												+ requestid
//												+ " and nodeid="
//												+ operNodeId;
//
//										rs1.execute(agentSQL);
//
//										if (rs1.next()) {
//											isbeAgent = true;
//											agenterId = String.valueOf(user.getUID());//如果存在代理关系,则当前操作者就是代理人
//											opertor = rs1.getString("agentorbyagentid");//agenttype=2为代理人记录,agentorbyagentid为被代理人
//											currentDate = TimeUtil.getCurrentDateString();
//											currentTime =
//												(TimeUtil.getCurrentTimeString()).substring(
//													11,
//													19);
//											agenttype = rs1.getString("agenttype");
//										}
//
//										if (isbeAgent) {
//											//设置被代理人已操作
//											Procpara =
//												""
//													+ requestid
//													+ flag
//													+ opertor
//													+ flag
//													+ operatorgroup
//													+ flag
//													+ wfid
//													+ flag
//													+ wftype
//													+ flag
//													+ opertortype
//													+ flag
//													+ "2"
//													+ flag
//													+ currentNodeId
//													+ flag
//													+ agenterId
//													+ flag
//													+ "1"
//													+ flag
//													+ showorder
//											        +flag
//											        +groupdetailIds;
//											rs1.executeProc(
//												"workflow_CurrentOperator_I",
//												Procpara);
//											//设置代理人
//											Procpara =
//												""
//													+ requestid
//													+ flag
//													+ agenterId
//													+ flag
//													+ operatorgroup
//													+ flag
//													+ wfid
//													+ flag
//													+ wftype
//													+ flag
//													+ opertortype
//													+ flag
//													+ "0"
//													+ flag
//													+ currentNodeId
//													+ flag
//													+ opertor
//													+ flag
//													+ "2"
//													+ flag
//													+ showorder
//													+flag
//												    +groupdetailIds;
//											rs1.executeProc(
//												"workflow_CurrentOperator_I",
//												Procpara);
//										} else {
//											Procpara =
//												""
//													+ requestid
//													+ flag
//													+ opertor
//													+ flag
//													+ operatorgroup
//													+ flag
//													+ wfid
//													+ flag
//													+ wftype
//													+ flag
//													+ opertortype
//													+ flag
//													+ "0"
//													+ flag
//													+ currentNodeId
//													+ flag
//													+
//													- 1
//													+ flag
//													+ "0"
//													+ flag
//													+ showorder
//													+flag
//												    +groupdetailIds;
//											rs1.executeProc(
//												"workflow_CurrentOperator_I",
//												Procpara);
//										}
//
//										if (!isbeAgent) {
//										    //QC151617
////											tempHrmIds
////												+= Util.toScreen(
////													resourceComInfo.getResourcename(opertor),
////													user.getLanguage())
////												+ ",";
//										    //QC151617 判断用户类型，而不是当前用户类型
//                                            //if(usertype==0){
//                                            if(opertortype.equals("0")){
//                                                tempHrmIds
//                                                += Util.toScreen(
//                                                    resourceComInfo.getResourcename(opertor),
//                                                    user.getLanguage())
//                                                + ",";
//                                            }else{
//                                                tempHrmIds
//                                                += Util.toScreen(
//                                                    crmComInfo.getCustomerInfoname(opertor),
//                                                    user.getLanguage())
//                                                + ",";
//                                            }
//										} else {
//                                            //QC151617
//                                            //if(usertype==0){
//                                            if(opertortype.equals("0")){
//    											tempHrmIds
//    												+= Util.toScreen(
//    													resourceComInfo.getResourcename(opertor),
//    													user.getLanguage())
//    												+ "->"
//    												+ Util.toScreen(
//    													resourceComInfo.getResourcename(agenterId),
//    													user.getLanguage())
//    												+ ",";
//                                            }
//										}
//
//									//已完成流程信息数量
//									if (!isbeAgent)
//									{
//								    	//poppupRemindInfoUtil.insertPoppupRemindInfo(Integer.parseInt(opertor),1,""+opertortype,Integer.parseInt(requestid));
//								    	 Map mappo=new HashMap();
//								    	 mappo.put("userid",""+Integer.parseInt(opertor));
//								    	 mappo.put("type","1");
//								    	 mappo.put("logintype",""+opertortype);
//								    	 mappo.put("requestid",""+Integer.parseInt(requestid));
//								    	 mappo.put("requestname","");
//								    	 mappo.put("workflowid","-1");
//								    	 mappo.put("creater","");
//									     poppuplist.add(mappo);
//									}
//									else
//									{
//									//poppupRemindInfoUtil.insertPoppupRemindInfo(Integer.parseInt(agenterId),1,""+opertortype,Integer.parseInt(requestid));
//									 Map mappo=new HashMap();
//							    	 mappo.put("userid",""+Integer.parseInt(agenterId));
//							    	 mappo.put("type","1");
//							    	 mappo.put("logintype",""+opertortype);
//							    	 mappo.put("requestid",""+Integer.parseInt(requestid));
//							    	 mappo.put("requestname","");
//							    	 mappo.put("workflowid","-1");
//							    	 mappo.put("creater","");
//								     poppuplist.add(mappo);
//									}
//									}
//
//
//								}
//								try{
//								 poppupRemindInfoUtil.insertPoppupRemindInfo(poppuplist);
//								 }catch(Exception e){
//									 	writeLog(e);
//								 }
//
//								if (currentNodeType == 3) {
//									rs1.executeSql(
//										"update  workflow_currentoperator  set isremark='4'  where isremark='0' and requestid = "
//											+ requestid);
//									rs1.executeSql(
//										"update  workflow_currentoperator  set iscomplete=1  where requestid = "
//											+ requestid);
//									rst.setAutoCommit(false);
//									//发送短信
//									sendMsgAndMail.sendMsg(rst,Integer.parseInt(requestid),currentNodeId,user,"submit",""+currentNodeType);
//									//邮件提醒
//									sendMsgAndMail.sendMail(rst,wfid,Integer.parseInt(requestid),currentNodeId,request,fu,true,"submit",""+currentNodeType,user);
//									//微信提醒(QC:98106)
//									sendMsgAndMail.sendChats(rst, wfid, Integer.parseInt(requestid), currentNodeId, user, "submit", ""+currentNodeType);
//
//									rst.commit();
//								}
//
//								//任务强制归档后，保留强制归档的日志记录：“强制归档” 类型代码为 "e"
//				rs1.executeSql(
//									"select agentorbyagentid, agenttype, showorder from workflow_currentoperator where userid = "
//										+ user.getUID()
//										+ " and nodeid = "
//										+ currNodeId
//										+ " and isremark in ('0','1','4','8','9','7') and requestid = "
//										+ requestid);
//								if (rs1.next()) {
//									showorder = rs1.getInt("showorder");
//								}
//								remark = Util.null2String(remark);//TD10308 为防止remark可能为null，插入数据库变成“null”的字符串
//
//								agentSQL = "select agentorbyagentid,agenttype from workflow_currentoperator where userid="
//									+user.getUID()
//									+" and agenttype='2' and requestid="
//									+requestid
//									+" and nodeid="
//									+operNodeId;
//
//								rs1.execute(agentSQL);
//								if (rs1.next()) {
//									isbeAgent = true;
//									agenterId = rs1.getString("agentorbyagentid");
//									agenttype = rs1.getString("agenttype");
//								}
//								Procpara =
//									requestid
//										+ ""
//										+ flag
//										+ wfid
//										+ ""
//										+ flag
//										+ currNodeId
//										+ ""
//										+ flag
//										+ "e"
//										+ flag
//										+ CurrentDate
//										+ flag
//										+ CurrentTime
//										+ flag
//										+ userid
//										+ flag
//										+ ""+remark
//										+ flag
//										+ remoteAddr
//										+ flag
//										+ usertype
//										+ flag
//										+ "0"
//										+ flag
//										+ tempHrmIds.trim()
//										+ flag
//										+
//										(isbeAgent?agenterId:"-1")
//										+ flag
//										+ (isbeAgent?agenttype:"0")
//										+ flag
//										+ showorder+flag+annexdocids+flag+requestLogId+ flag + signdocids+flag+signworkflowids + flag+ remarkLocation+flag +'0' + flag+ 0+ flag + 0;
//				rs1.executeProc("workflow_RequestLog_Op", Procpara);
//
//            	//added by wcd 2015-09-14
//            	new HrmAttVacationManager().handle(StringUtil.parseToInt(requestid), wfid, 2);
//
//				//特殊处理(费用流程，在强制归档时，同时，已经配置生效、释放冻结预算)
//				try {
//	        		FnaCommon fnaCommon = new FnaCommon();
//	        		fnaCommon.doWfForceOver(Util.getIntValue(requestid, 0), wfForceOverLogic, false);
//	            }catch(Exception easi) {
//	            	new BaseBean().writeLog(easi);
//				}
//
//				//TD8826 在流程强制归档时加入权限修改
//				try {
//	                RequestAddShareInfo shareinfo = new RequestAddShareInfo();
//	                shareinfo.setRequestid(Util.getIntValue(requestid));
//	                shareinfo.SetWorkFlowID(wfid);
//	                shareinfo.SetNowNodeID(currNodeId);
//	                shareinfo.SetNextNodeID(currentNodeId);
//	                shareinfo.setIsbill(isbill);
//					shareinfo.setUser(user);
//	                shareinfo.SetIsWorkFlow(1);
//	                shareinfo.setBillTableName(billtablename);
//	                shareinfo.setHaspassnode(true);
//					shareinfo.addShareInfo();
//	            }catch(Exception easi) {
//				}
//			} catch (Exception e) {
//				writeLog(e);
//			}
//
//			//是否反馈
//			String isfeedback="";//当前节点是否反馈
//			rs1.executeSql("select isfeedback from workflow_flownode where workflowid="+wfid+" and nodeid="+currNodeId);
//			if(rs1.next()){
//			    isfeedback=Util.null2String(rs1.getString("isfeedback"));
//			}
//			String ifchangstatus=Util.null2String(getPropValue(GCONST.getConfigFile() , "ecology.changestatus"));//总开关
//			if (!ifchangstatus.equals("")&&isfeedback.equals("1")){
//			    rs1.executeSql("update workflow_currentoperator set viewtype =-1  where needwfback='1' and requestid=" + requestid + " and viewtype=-2");
//			}
//
//			//强制归档时，删除签字意见权限控制
//			remarkRight.setRequestid(Util.getIntValue(requestid,-1));
//			remarkRight.deleteAllRight();
//			//根据请求id删除数据
//			WFUrgerManager wFUrgerManager = new WFUrgerManager();
//			wFUrgerManager.deleteUrgerByRequestid(Integer.valueOf(requestid));
//
//			// 统一待办提醒
//			PostWorkflowInfo postWorkflowInfo = new PostWorkflowInfo();
//			postWorkflowInfo.operateToDo(requestid);
//			writeLog("==========统一待办(WfForceOver.java --> operateToDo)==========");
//
//			rolm.flowTransSubmitAfter();
//		}
//	}
//
//	/**
//	 * Description:判断是否归档
//	 * @param requestid 流程id
//	 * @return 是否归档
//	 */
//	public boolean isOver(int requestid) {
//		boolean flag = false;
//		RecordSet rs = new RecordSet();
//		rs.executeSql(
//			"select currentnodetype from workflow_requestbase where requestid = "
//				+ requestid);
//		if (rs.next()) {
//			if ("3".equals(rs.getString("currentnodetype"))) {
//				flag = true;
//			}
//		}
//		return flag;
//	}
//
//   /**
//	* Description:判断某人是否为某流程当前节点的当前或历史操作人,不考虑被转发人
//	* @param requestid 流程id
//	* @param userid 用户id
//	* @return 是否为某流程当前节点的当前或历史操作人
//	*/
//	public boolean isNodeOperator(int requestid, int userid) {
//		boolean flag = false;
//		int currnodeid = -1;
//		RecordSet rs = new RecordSet();
//       //modify by mackjoe 屏蔽掉老的获取当前节点方式，采用新的获取当前节点方式。
////		rs.executeProc("workflow_Requestbase_SByID", requestid + "");
////		if (rs.next()) {
////			currnodeid = Util.getIntValue(rs.getString("currentnodeid"), 0);
////		}
//		rs.executeSql(
//			"select * from workflow_currentoperator where requestid = "
//				+ requestid
//				+ " and isremark in ('0','2','7') and nodeid in(select nownodeid from workflow_nownode where requestid="
//				+ requestid
//				+ ") and userid = "
//				+ userid);
//		if (rs.next()) {
//			flag = true;
//		}
//		return flag;
//	}
//
//   /**
//	* Description:判断某人是否为某流程指定节点的当前或历史操作人,不考虑被转发人
//	* @param requestid 流程id
//    * @param nodeid 当前节点
//	* @param userid 用户id
//    * @return 是否为某流程指定节点的当前或历史操作人
//	*/
//	public boolean isNodeOperator(int requestid,int nodeid, int userid) {
//		boolean flag = false;
//		RecordSet rs = new RecordSet();
//		rs.executeSql(
//			"select * from workflow_currentoperator where requestid = "
//				+ requestid
//				+ " and isremark in ('0','2') and nodeid = "
//				+ nodeid
//				+ " and userid = "
//				+ userid);
//		if (rs.next()) {
//			flag = true;
//		}
//		return flag;
//	}
//
//    /**
//     * 获得签字意见
//     * @return
//     */
//    public String getRemark() {
//        return remark;
//    }
//
//    /**
//     * 设置签字意见
//     * @param remark
//     */
//    public void setRemark(String remark) {
//        this.remark = remark;
//    }
//
//    /**
//     * 获得签字意见相关附件
//     * @return
//     */
//    public String getAnnexdocids() {
//        return annexdocids;
//    }
//
//    /**
//     * 设置签字意见相关附件
//     * @param annexdocids
//     */
//    public void setAnnexdocids(String annexdocids) {
//        this.annexdocids = annexdocids;
//    }
//
//    /**
//     * 获得签字意见相关文档
//     * @return
//     */
//    public String getSigndocids() {
//        return signdocids;
//    }
//
//    /**
//     * 设置签字意见相关文档
//     * @param signdocids
//     */
//    public void setSigndocids(String signdocids) {
//        this.signdocids = signdocids;
//    }
//
//    /**
//     * 获得签字意见相关流程
//     * @return
//     */
//    public String getSignworkflowids() {
//        return signworkflowids;
//    }
//
//    /**
//     * 设置签字意见相关流程
//     * @param signworkflowids
//     */
//    public void setSignworkflowids(String signworkflowids) {
//        this.signworkflowids = signworkflowids;
//    }
//
//    /**
//     * 获得流转意见id
//     * @return
//     */
//    public int getRequestLogId() {
//        return requestLogId;
//    }
//
//    /**
//     * 设置流转意见id
//     * @param requestLogId
//     */
//    public void setRequestLogId(int requestLogId) {
//        this.requestLogId = requestLogId;
//    }
//
//    public String getRemarkLocation() {
//        return remarkLocation;
//    }
//
//    public void setRemarkLocation(String remarkLocation) {
//        this.remarkLocation = remarkLocation;
//    }
//}
