//package weaver.interfaces.erp;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.json.JSONObject;
//
//import weaver.conn.RecordSet;
//import weaver.file.Prop;
//import weaver.general.BaseBean;
//import weaver.general.Util;
//
//public class PostWorkflowInfoThread extends BaseBean {
//
//	/**
//	 * 操作统一待办库(待办,已办) ERP流程状态，0：待办，1：已办，4：办结
//	 *
//	 * @param requestid
//	 * @return
//	 */
//	public Runnable operateToDo(String requestid) {
//		writeLog("--------------------推送待办信息到ERP待办库Start(requestid："
//				+ requestid + ")--------------------");
//
//		// 开启统一待办
//		String isopen = Util.null2String(Prop.getPropValue("ERPSetting",
//				"isopen"));
//		// OA系统ID(ERP提供)
//		String systemID = Util.null2String(Prop.getPropValue("ERPSetting",
//				"systemID"));
//		// OA系统名称(ERP提供)
//		String systemName = Util.null2String(Prop.getPropValue("ERPSetting",
//				"systemName"));
//		// OA待办流程列表页面
//		String pc_list = Util.null2String(Prop.getPropValue("ERPSetting",
//				"pc_list"));
//		// OA兼职待办流程列表页面
//		String pcjz_list = Util.null2String(Prop.getPropValue("ERPSetting",
//						"pcjz_list"));
//		// OA移动端认证页面
//		String mobile_url = Util.null2String(Prop.getPropValue("ERPSetting",
//						"mobile_url"));
//		// 需要同步推送待办的流程id
//		String wfid = Util.null2String(Prop.getPropValue("ERPSetting", "wfid"));
//		if (!"true".equalsIgnoreCase(isopen)) {
//			writeLog("----------在ERPSetting.properties配置文件中没有开启统一待办提醒功能！----------");
//			return null ;
//		}
//		if (requestid == null || "".equals(requestid)) {
//			writeLog("----------requestid为空！----------");
//			return null ;
//		}
//
//		try {
//			RecordSet rs = new RecordSet();
//			RecordSet rs1 = new RecordSet();
//			RecordSet rs2 = new RecordSet();
//			RecordSet rs3 = new RecordSet();
//			RecordSet rs4 = new RecordSet();
//			RecordSet rs5 = new RecordSet();
//			// 创建人ID
//			String createrid = "";
//			// 创建人姓名
//			String creatername = "";
//			// 流程ID
//			String workflowid = "";
//			// 流程名称
//			String workflowname = "";
//			// 请求标题
//			String requestname = "";
//			// 创建时间
//			String createdatetime = "";
//
//			rs.executeSql("select * from workflow_requestbase where requestid = "
//					+ requestid);
//			if (rs.next()) {
//				createrid = Util.null2String(rs.getString("creater"));
//				if (!"".equals(createrid)) {
//					Map<String, String> utemp = getResouceLoginidAndName(createrid);
//					if (utemp != null) {
//						creatername = utemp.get("lastname");
//					}
//				}
//				workflowid = Util.null2String(rs.getString("workflowid"));
//				if (!"".equals(workflowid)) {
//					rs1.executeSql("select * from workflow_base where id = "
//							+ workflowid);
//					if (rs1.next()) {
//						workflowname = Util.null2String(rs1
//								.getString("workflowname"));
//					}
//				}
//				requestname = Util.null2String(rs.getString("requestname"));
//				createdatetime = Util.null2String(rs.getString("createdate")
//						+ " " + rs.getString("createtime"));
//			}
//
//			if (!"".equals(workflowid)) {
//				String sql = "select * from workflow_base where workflowtype = (select id from workflow_type where typename in ('公文管理')) and id = "
//						+ workflowid;
//				if (!"".equals(wfid)) {
//					sql = "select * from workflow_base where ( workflowtype = (select id from workflow_type where typename in ('公文管理')) and id = "
//							+ workflowid + ") or id in (" + wfid + ") ";
//				}
//				// 公文管理下的流程才推送到ERP待办库
//				rs2.executeSql(sql);
//				if (rs2.next()) {
//					writeLog("-----流程ID：" + workflowid + "，流程名称："
//							+ workflowname + "，请求ID：" + requestid + "，创建人ID："
//							+ createrid + "，创建人姓名：" + creatername + "-----");
//
//					// 删除
//					deleteToDo(requestid);
//
//					ERPHelper erpHelper = new ERPHelper();
//
//					rs3.executeSql("select * from workflow_currentoperator where requestid = "
//							+ requestid + " order by id desc ");
//					while (rs3.next()) {
//						// 主键
//						String id = Util.null2String(rs3.getString("id"));
//						// 接收人
//						String receiverid = Util.null2String(rs3
//								.getString("userid"));
//						//是否兼职
//                         boolean isacc=getIsAccounttype(receiverid);
//						// 接收人
//						String receiveridoa = Util.null2String(rs3
//								.getString("userid"));
//						// 接收人姓名
//						String receivername = "";
//						if (!"".equals(receiverid)) {
//							Map<String, String> utemp = getResouceLoginidAndName(receiverid);
//							if (utemp != null) {
//								receivername = utemp.get("lastname");
//							}
//						}
//						// 节点ID
//						String nodeid = Util.null2String(rs3
//								.getString("nodeid"));
//						// 节点名称
//						String nodename = "";
//						rs4.executeSql("select nodename from workflow_nodebase where id = "
//								+ nodeid);
//						if (rs4.next()) {
//							nodename = Util.null2String(rs4
//									.getString("nodename"));
//						}
//						// 接收时间
//						String receivedatetime = Util.null2String(rs3
//								.getString("receivedate")
//								+ " "
//								+ rs3.getString("receivetime"));
//						// PC端地址
//						String pc_url = getWf_Url(requestid);
//						//兼职用户
//						if(isacc)
//						{
//							pc_url = getWf_Url(requestid,receiverid,workflowid);
//						}
//
//						//移动端地址
//						String mob_url =getMoblilWf_Url(requestid);
//						// 最后一次操作记录：1
//						String islasttimes = Util.null2String(rs3
//								.getString("islasttimes"));
//						// 操作类型，0：未操作、1：转发、2：已操作、4：归档、8：抄送(不需提交)、9：抄送(需提交)
//						String isremark = Util.null2String(rs3
//								.getString("isremark"));
//						writeLog("-----接收人ID：" + receiverid + "，接收人姓名："
//								+ receivername + "，isremark：" + isremark
//								+ "-----");
//
//						// 传入ERP
//						if ("1".equals(islasttimes)) {
//
//							// 增加创建人工号转换
//							if (!"".equals(createrid)) {
//								Map<String, String> utemp = getResouceLoginidAndName(createrid);
//								if (utemp != null) {
//									createrid = utemp.get("workcode");
//								}
//							}
//							if (!"".equals(receiverid)) {
//								Map<String, String> utemp = getResouceLoginidAndName(receiverid);
//								if (utemp != null) {
//									receiverid = utemp.get("workcode");
//								}
//							}
//							writeLog("-----创建人工号：" + createrid + "-----接收人工号："
//									+ receiverid + "，接收人姓名：" + receivername
//									+ "，isremark：" + isremark + "-----");
//							// 转换工号结束
//							String erptype = "08";
//							if ("0".equals(isremark) || "1".equals(isremark)
//									|| "8".equals(isremark)
//									|| "9".equals(isremark)) {// 待办
//								try {
//									String sfcs="0";
//									if("8".equals(isremark)
//											|| "9".equals(isremark))
//									{
//										sfcs="1";
//									}
//									erptype = getERPType(workflowname, isremark);
//									Object[] params = new Object[] { systemID,
//											systemName, erptype,
//											requestid + "_" + id, requestname,
//											nodename, createdatetime,
//											receivedatetime, createrid,
//											creatername, receiverid, "",
//											pc_url, "0", "0", "", pc_list,
//											workflowname, mob_url, sfcs, "" };
//									String returnInfo = erpHelper
//											.sendTODO(params);
//									writeLog("-----operateToDo(待办)，"
//											+ parseResponseData(returnInfo)
//											+ "-----");
//								} catch (Exception e) {
//									writeLog(
//											"-----operateToDo(待办)，插入信息出错：-----",
//											e);
//									throw e;
//								}
//							} else if ("2".equals(isremark)) {// 已办
//								try {
//									erptype = getERPType(workflowname, isremark);
//									Object[] params = new Object[] { systemID,
//											systemName, erptype,
//											requestid + "_" + id, requestname,
//											nodename, createdatetime,
//											receivedatetime, createrid,
//											creatername, receiverid, "",
//											pc_url, "1", "0", "", pc_list,
//											workflowname, mob_url, "", "" };
//									String returnInfo = erpHelper
//											.sendTODO(params);
//									writeLog("-----operateToDo(已办)，"
//											+ parseResponseData(returnInfo)
//											+ "-----");
//								} catch (Exception e) {
//									writeLog("-----operateToDo(已办)，插入信息出错：-----"
//											+ e);
//									throw e;
//								}
//							} else if ("4".equals(isremark)) {// 办结
//								try {
//									erptype = getERPType(workflowname, isremark);
//									Object[] params = new Object[] { systemID,
//											systemName, erptype,
//											requestid + "_" + id, requestname,
//											nodename, createdatetime,
//											receivedatetime, createrid,
//											creatername, receiverid, "",
//											pc_url, "4", "0", "", pc_list,
//											workflowname, mob_url, "", "" };
//									String returnInfo = erpHelper
//											.sendTODO(params);
//									writeLog("-----operateToDo(办结)，"
//											+ parseResponseData(returnInfo)
//											+ "-----");
//								} catch (Exception e) {
//									writeLog("-----operateToDo(办结)，插入信息出错：-----"
//											+ e);
//									throw e;
//								}
//							}
//						}
//
//						// 插入中间表
//						rs5.executeSql("insert into erp_todolog(id, workflowid, requestid, userid, islasttimes, isremark) values("
//								+ id
//								+ ", "
//								+ workflowid
//								+ ", "
//								+ requestid
//								+ ", "
//								+ receiveridoa
//								+ ", "
//								+ islasttimes
//								+ ", '" + isremark + "') ");
//					}
//				}
//			}
//		} catch (Exception e) {
//			writeLog("----------推送待办信息到ERP待办库出错：----------", e);
//		}
//		writeLog("--------------------推送待办信息到ERP待办库End(requestid：" + requestid
//				+ ")--------------------");
//		return null ;
//	}
//
//	/**
//	 * 根据流程获取流程类型
//	 *
//	 * @param workflowname
//	 * @return
//	 */
//	private String getERPType(String workflowname, String isremark) {
//		String erptype = "08";
//		if (workflowname.indexOf("发文") > -1) {
//			erptype = "01";
//			if ("8".equals(isremark) || "9".equals(isremark)) {
//				erptype = "05";
//			}
//
//		} else if (workflowname.indexOf("收文") > -1) {
//			erptype = "04";
//			if ("8".equals(isremark) || "9".equals(isremark)) {
//				erptype = "06";
//			}
//		} else if (workflowname.indexOf("机关发电") > -1) {
//			erptype = "07";
//			if ("8".equals(isremark) || "9".equals(isremark)) {
//				erptype = "09";
//			}
//		} else if (workflowname.indexOf("督办") > -1) {
//			erptype = "02";
//
//		} else {
//			erptype = "08";
//		}
//		return erptype;
//	}
//
//	/**
//	 *
//	 * 征询改待办 ERP流程状态，0：待办，1：已办，4：办结
//	 *
//	 * @param requestid
//	 * @param id
//	 * @return
//	 */
//	public Runnable operateZXToDo(String requestid, int id) {
//		writeLog("----------征询改待办Start(requestid：" + requestid + ")----------");
//
//		// 开启统一待办
//		String isopen = Util.null2String(Prop.getPropValue("ERPSetting",
//				"isopen"));
//		// OA系统ID(ERP提供)
//		String systemID = Util.null2String(Prop.getPropValue("ERPSetting",
//				"systemID"));
//		// OA系统名称(ERP提供)
//		String systemName = Util.null2String(Prop.getPropValue("ERPSetting",
//				"systemName"));
//		// OA待办流程列表页面
//		String pc_list = Util.null2String(Prop.getPropValue("ERPSetting",
//				"pc_list"));
//		// 需要同步推送待办的流程id
//		String wfid = Util.null2String(Prop.getPropValue("ERPSetting", "wfid"));
//		if (!"true".equalsIgnoreCase(isopen)) {
//			writeLog("----------在ERPSetting.properties配置文件中没有开启统一待办提醒功能！----------");
//			return null;
//		}
//		if (requestid == null || "".equals(requestid)) {
//			writeLog("----------requestid为空！----------");
//			return null;
//		}
//
//		try {
//			RecordSet rs = new RecordSet();
//			RecordSet rs1 = new RecordSet();
//			RecordSet rs2 = new RecordSet();
//			RecordSet rs3 = new RecordSet();
//			RecordSet rs4 = new RecordSet();
//			RecordSet rs5 = new RecordSet();
//			// 创建人ID
//			String createrid = "";
//			// 创建人姓名
//			String creatername = "";
//			// 流程ID
//			String workflowid = "";
//			// 流程名称
//			String workflowname = "";
//			// 请求标题
//			String requestname = "";
//			// 创建时间
//			String createdatetime = "";
//
//			rs.executeSql("select * from workflow_requestbase where requestid = "
//					+ requestid);
//			if (rs.next()) {
//				createrid = Util.null2String(rs.getString("creater"));
//				if (!"".equals(createrid)) {
//					Map<String, String> utemp = getResouceLoginidAndName(createrid);
//					if (utemp != null) {
//						creatername = utemp.get("lastname");
//					}
//				}
//				workflowid = Util.null2String(rs.getString("workflowid"));
//				if (!"".equals(workflowid)) {
//					rs1.executeSql("select * from workflow_base where id = "
//							+ workflowid);
//					if (rs1.next()) {
//						workflowname = Util.null2String(rs1
//								.getString("workflowname"));
//					}
//				}
//				requestname = Util.null2String(rs.getString("requestname"));
//				createdatetime = Util.null2String(rs.getString("createdate")
//						+ " " + rs.getString("createtime"));
//			}
//
//			if (!"".equals(workflowid)) {
//				String sql = "select * from workflow_base where workflowtype = (select id from workflow_type where typename in ('公文管理')) and id = "
//						+ workflowid;
//				if (!"".equals(wfid)) {
//					sql = "select * from workflow_base where ( workflowtype = (select id from workflow_type where typename in ('公文管理')) and id = "
//							+ workflowid + ") or id in (" + wfid + ") ";
//				}
//				// 公文管理下的流程才推送到ERP待办库
//				rs2.executeSql(sql);
//				if (rs2.next()) {
//					writeLog("-----流程ID：" + workflowid + "，流程名称："
//							+ workflowname + "，请求ID：" + requestid + "，创建人ID："
//							+ createrid + "，创建人姓名：" + creatername + "-----");
//
//					ERPHelper erpHelper = new ERPHelper();
//
//					rs3.executeSql("select * from workflow_currentoperator where requestid = "
//							+ requestid
//							+ " and islasttimes = 1 and id = "
//							+ id
//							+ " order by id ");
//					while (rs3.next()) {
//						// 接收人
//						String receiverid = Util.null2String(rs3
//								.getString("userid"));
//						//是否兼职
//                        boolean isacc=getIsAccounttype(receiverid);
//						// 接收人
//						String receiveridoa = Util.null2String(rs3
//								.getString("userid"));
//						// 接收人姓名
//						String receivername = "";
//						if (!"".equals(receiverid)) {
//							Map<String, String> utemp = getResouceLoginidAndName(receiverid);
//							if (utemp != null) {
//								receivername = utemp.get("lastname");
//							}
//						}
//						// 节点ID
//						String nodeid = Util.null2String(rs3
//								.getString("nodeid"));
//						// 节点名称
//						String nodename = "";
//						rs4.executeSql("select nodename from workflow_nodebase where id = "
//								+ nodeid);
//						if (rs4.next()) {
//							nodename = Util.null2String(rs4
//									.getString("nodename"));
//						}
//						// 接收时间
//						String receivedatetime = Util.null2String(rs3
//								.getString("receivedate")
//								+ " "
//								+ rs3.getString("receivetime"));
//						// PC端地址
//						String pc_url = getWf_Url(requestid);
//						//兼职用户
//						if(isacc)
//						{
//							pc_url = getWf_Url(requestid,receiverid,workflowid);
//						}
//
//						//移动端地址
//						String mob_url =getMoblilWf_Url(requestid);
//						// 操作类型，0：未操作、1：转发、2：已操作、4：归档、8：抄送(不需提交)、9：抄送(需提交)
//						String isremark = Util.null2String(rs3
//								.getString("isremark"));
//						writeLog("-----接收人ID：" + receiverid + "，接收人姓名："
//								+ receivername + "，isremark：" + isremark
//								+ "-----");
//
//						// 增加创建人工号转换
//						if (!"".equals(createrid)) {
//							Map<String, String> utemp = getResouceLoginidAndName(createrid);
//							if (utemp != null) {
//								createrid = utemp.get("workcode");
//							}
//						}
//						if (!"".equals(receiverid)) {
//							Map<String, String> utemp = getResouceLoginidAndName(receiverid);
//							if (utemp != null) {
//								receiverid = utemp.get("workcode");
//							}
//						}
//						writeLog("-----创建人工号：" + createrid + "-----接收人工号："
//								+ receiverid + "，接收人姓名：" + receivername
//								+ "，isremark：" + isremark + "-----");
//						// 转换工号结束
//						String erptype = "08";
//						if ("0".equals(isremark) || "1".equals(isremark)
//								|| "8".equals(isremark) || "9".equals(isremark)) {// 待办
//							try {
//								String sfcs="0";
//								if("8".equals(isremark)
//										|| "9".equals(isremark))
//								{
//									sfcs="1";
//								}
//								erptype = getERPType(workflowname, isremark);
//								Object[] params = new Object[] { systemID,
//										systemName, erptype,
//										requestid + "_" + id, requestname,
//										nodename, createdatetime,
//										receivedatetime, createrid,
//										creatername, receiverid, "", pc_url,
//										"0", "0", "", pc_list, workflowname,
//										mob_url, sfcs, "" };
//								String returnInfo = erpHelper.sendTODO(params);
//								writeLog("-----operateZXToDo(待办)，"
//										+ parseResponseData(returnInfo)
//										+ "-----");
//							} catch (Exception e) {
//								writeLog(
//										"-----operateZXToDo(待办)，插入信息(征询改待办)出错：-----",
//										e);
//								throw e;
//							}
//						} else if ("2".equals(isremark)) {// 已办
//							try {
//								erptype = getERPType(workflowname, isremark);
//								Object[] params = new Object[] { systemID,
//										systemName, erptype,
//										requestid + "_" + id, requestname,
//										nodename, createdatetime,
//										receivedatetime, createrid,
//										creatername, receiverid, "", pc_url,
//										"1", "0", "", pc_list, workflowname,
//										mob_url, "", "" };
//								String returnInfo = erpHelper.sendTODO(params);
//								writeLog("-----operateZXToDo(已办)，"
//										+ parseResponseData(returnInfo)
//										+ "-----");
//							} catch (Exception e) {
//								writeLog(
//										"-----operateZXToDo(已办)，插入信息(征询改待办)出错：-----",
//										e);
//								throw e;
//							}
//						} else if ("4".equals(isremark)) {// 办结
//							try {
//								erptype = getERPType(workflowname, isremark);
//								Object[] params = new Object[] { systemID,
//										systemName, erptype,
//										requestid + "_" + id, requestname,
//										nodename, createdatetime,
//										receivedatetime, createrid,
//										creatername, receiverid, "", pc_url,
//										"4", "0", "", pc_list, workflowname,
//										mob_url, "", "" };
//								String returnInfo = erpHelper.sendTODO(params);
//								writeLog("-----operateZXToDo(办结)，"
//										+ parseResponseData(returnInfo)
//										+ "-----");
//							} catch (Exception e) {
//								writeLog(
//										"-----operateZXToDo(办结)，插入信息(征询改待办)出错：-----",
//										e);
//								throw e;
//							}
//						}
//
//						// 插入中间表
//						rs5.executeSql("insert into erp_todolog(id, workflowid, requestid, userid, islasttimes, isremark) values("
//								+ id
//								+ ", "
//								+ workflowid
//								+ ", "
//								+ requestid
//								+ ", "
//								+ receiveridoa
//								+ ", 1, '"
//								+ isremark
//								+ "') ");
//					}
//				}
//			}
//		} catch (Exception e) {
//			writeLog("----------征询改待办出错：----------", e);
//		}
//		writeLog("----------征询改待办End(requestid：" + requestid + ")----------");
//		return null;
//	}
//
//	/**
//	 *
//	 * 查看变已办情景处理 ERP流程状态，0：待办，1：已办，4：办结
//	 *
//	 * @param requestid
//	 * @param userid
//	 * @return
//	 */
//	public Runnable ViewToHandle(String requestid, String userid) {
//		writeLog("----------查看变已办处理Start(requestid：" + requestid + "，userid："
//				+ userid + ")----------");
//
//		// 开启统一待办
//		String isopen = Util.null2String(Prop.getPropValue("ERPSetting",
//				"isopen"));
//		// OA系统ID(ERP提供)
//		String systemID = Util.null2String(Prop.getPropValue("ERPSetting",
//				"systemID"));
//		if (!"true".equalsIgnoreCase(isopen)) {
//			writeLog("----------在ERPSetting.properties配置文件中没有开启统一待办提醒功能！----------");
//			return null;
//		}
//		if (requestid == null || "".equals(requestid)) {
//			writeLog("----------requestid为空！----------");
//			return null;
//		}
//
//		try {
//			RecordSet rs = new RecordSet();
//			RecordSet rs1 = new RecordSet();
//			RecordSet rs2 = new RecordSet();
//			RecordSet rs3 = new RecordSet();
//			// 创建人ID
//			String createrid = "";
//			// 创建人姓名
//			String creatername = "";
//			// 流程ID
//			String workflowid = "";
//			// 流程名称
//			String workflowname = "";
//
//			rs.executeSql("select * from workflow_requestbase where requestid = "
//					+ requestid);
//			if (rs.next()) {
//				createrid = Util.null2String(rs.getString("creater"));
//				if (!"".equals(createrid)) {
//					Map<String, String> utemp = getResouceLoginidAndName(createrid);
//					if (utemp != null) {
//						creatername = utemp.get("lastname");
//					}
//				}
//				workflowid = Util.null2String(rs.getString("workflowid"));
//				if (!"".equals(workflowid)) {
//					rs1.executeSql("select * from workflow_base where id = "
//							+ workflowid);
//					if (rs1.next()) {
//						workflowname = Util.null2String(rs1
//								.getString("workflowname"));
//					}
//				}
//			}
//
//			if (!"".equals(workflowid)) {
//				// 公文管理下的流程
//				rs2.executeSql("select * from workflow_base where workflowtype = (select id from workflow_type where typename in ('公文管理')) and id = "
//						+ workflowid);
//				if (rs2.next()) {
//					writeLog("-----流程ID：" + workflowid + "，流程名称："
//							+ workflowname + "，请求ID：" + requestid + "，创建人ID："
//							+ createrid + "，创建人姓名：" + creatername + "-----");
//
//					ERPHelper erpHelper = new ERPHelper();
//
//					rs3.executeSql("select * from workflow_currentoperator where requestid = "
//							+ requestid
//							+ " and userid = "
//							+ userid
//							+ " and islasttimes = 1 order by id desc ");
//					while (rs3.next()) {
//						// 主键
//						String id = Util.null2String(rs3.getString("id"));
//						// 接收人
//						String receiverid = Util.null2String(rs3
//								.getString("userid"));
//						// 接收人姓名
//						String receivername = "";
//						if (!"".equals(receiverid)) {
//							Map<String, String> utemp = getResouceLoginidAndName(receiverid);
//							if (utemp != null) {
//								receivername = utemp.get("lastname");
//							}
//						}
//						// 操作类型，0：未操作、1：转发、2：已操作、4：归档、8：抄送(不需提交)、9：抄送(需提交)
//						String isremark = Util.null2String(rs3
//								.getString("isremark"));
//
//						writeLog("-----接收人ID：" + receiverid + "，接收人姓名："
//								+ receivername + "，isremark：" + isremark
//								+ "-----");
//
//						// 更新状态
//						try {
//							Object[] params = new Object[] {
//									requestid + "_" + id, systemID, "1" };
//							String returnInfo = erpHelper.updateTODO(params);
//							writeLog("-----ViewToHandle，"
//									+ parseResponseData(returnInfo) + "-----");
//						} catch (Exception e) {
//							writeLog("-----ViewToHandle，更新状态(查看变已办)出错：-----", e);
//							throw e;
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			writeLog("----------更新状态(查看变已办)出错：----------", e);
//		}
//		writeLog("----------查看变已办处理End(requestid：" + requestid + "，userid："
//				+ userid + ")----------");
//		return null;
//	}
//
//	/**
//	 * 操作统一待办库(根据userid删除流程)
//	 *
//	 * @param requestid
//	 * @return
//	 */
//	public Runnable deleteUserToDo(String requestid, String userid) {
//		writeLog("----------根据requestid和userid删除ERP待办库中OA流程Start(requestid："
//				+ requestid + "，userid：" + userid + ")----------");
//
//		// 开启统一待办
//		String isopen = Util.null2String(Prop.getPropValue("ERPSetting",
//				"isopen"));
//		// OA系统ID(ERP提供)
//		String systemID = Util.null2String(Prop.getPropValue("ERPSetting",
//				"systemID"));
//		if (!"true".equalsIgnoreCase(isopen)) {
//			writeLog("----------在ERPSetting.properties配置文件中没有开启统一待办提醒功能！----------");
//			return null;
//		}
//		if (requestid == null || "".equals(requestid)) {
//			writeLog("----------requestid为空！----------");
//			return null;
//		}
//
//		try {
//			RecordSet rs = new RecordSet();
//			RecordSet rs1 = new RecordSet();
//
//			rs.executeSql("select * from erp_todolog where requestid = "
//					+ requestid + " and userid = " + userid);
//			while (rs.next()) {
//				ERPHelper erpHelper = new ERPHelper();
//				// 主键
//				String id = Util.null2String(rs.getString("id"));
//				// 最后一次操作记录：1
//				String islasttimes = Util.null2String(rs
//						.getString("islasttimes"));
//
//				String returnInfo = "";
//				if ("1".equals(islasttimes)) {
//					Object[] params = new Object[] { requestid + "_" + id,
//							systemID };
//					returnInfo = erpHelper.deleteTODO(params);
//				}
//
//				rs1.executeSql("delete from erp_todolog where id = " + id);
//				writeLog("-----deleteUserToDo，" + parseResponseData(returnInfo)
//						+ "(requestid：" + requestid + "，id：" + id + ")-----");
//			}
//		} catch (Exception e) {
//			writeLog("----------根据requestid和userid删除ERP待办库中OA流程出错：----------",
//					e);
//		}
//		writeLog("----------根据requestid和userid删除ERP待办库中OA流程End(requestid："
//				+ requestid + "，userid：" + userid + ")----------");
//		return null;
//	}
//
//	/**
//	 *
//	 * 操作统一待办库(根据requestid删除流程)
//	 *
//	 * @param requestid
//	 * @return
//	 */
//	public Runnable deleteToDo(String requestid) {
//		writeLog("----------根据requestid删除ERP待办库中OA流程Start(requestid："
//				+ requestid + ")----------");
//
//		// 开启统一待办
//		String isopen = Util.null2String(Prop.getPropValue("ERPSetting",
//				"isopen"));
//		// OA系统ID(ERP提供)
//		String systemID = Util.null2String(Prop.getPropValue("ERPSetting",
//				"systemID"));
//		if (!"true".equalsIgnoreCase(isopen)) {
//			writeLog("----------在ERPSetting.properties配置文件中没有开启统一待办提醒功能！----------");
//			return null;
//		}
//		if (requestid == null || "".equals(requestid)) {
//			writeLog("----------requestid为空！----------");
//			return null;
//		}
//
//		try {
//			RecordSet rs = new RecordSet();
//			RecordSet rs1 = new RecordSet();
//
//			// 批量删除
//			String jsion = "[{\"ralation\":\"and\",\"filtername\":\"itemid\",\"operate\":\"like\",\"filtervalue\":\""
//					+ requestid
//					+ "%\"},{\"ralation\":\"and\",\"filtername\":\"systemid\",\"operate\":\"=\",\"filtervalue\":\""
//					+ systemID + "\"}]";
//			try {
//				System.out.println("jsion"+jsion);
//				String returnInfo = "";
//				ERPHelper erpHelper = new ERPHelper();
//				Object[] params = new Object[] { jsion };
//				returnInfo = erpHelper.deleteTODO(params);
//			} catch (Exception e) {
//				throw e;
//			}
//
//			rs.executeSql("select * from erp_todolog where requestid = "
//					+ requestid);
//			while (rs.next()) {
//				String id = Util.null2String(rs.getString("id"));
//				/*
//				 * ERPHelper erpHelper = new ERPHelper(); // 主键 String id =
//				 * Util.null2String(rs.getString("id")); // 最后一次操作记录：1 String
//				 * islasttimes = Util.null2String(rs.getString("islasttimes"));
//				 *
//				 * String returnInfo = ""; if ("1".equals(islasttimes)) {
//				 * Object[] params = new Object[] {requestid + "_" + id,
//				 * systemID}; try{ returnInfo = erpHelper.deleteTODO(params);
//				 * }catch(Exception e) { throw e; } }
//				 */
//				rs1.executeSql("delete from erp_todolog where id = " + id);
//				// writeLog("-----deleteToDo，" + parseResponseData(returnInfo) +
//				// "(requestid：" + requestid + "，id：" + id + ")-----");
//			}
//
//		} catch (Exception e) {
//			writeLog("----------根据requestid删除ERP待办库中OA流程出错：----------", e);
//		}
//		writeLog("----------根据requestid删除ERP待办库中OA流程End(requestid：" + requestid
//				+ ")----------");
//		return null;
//	}
//
//	/**
//	 * 强制归档，先删除，再推送
//	 *
//	 * @param requestid
//	 * @return
//	 */
//	public Runnable drawBackWF(String requestid) {
//		writeLog("----------强制归档，先删除，再推送Start(requestid：" + requestid
//				+ ")----------");
//		// 先删除
//		deleteToDo(requestid);
//		// 再推送
//		operateToDo(requestid);
//		writeLog("----------强制归档，先删除，再推送End(requestid：" + requestid
//				+ ")----------");
//		return null;
//	}
//
//	/**
//	 *
//	 * 获取PC端打开流程地址
//	 *
//	 * @param id
//	 * @param requestid
//	 * @return
//	 */
//	private String getWf_Url(String requestid) {
//		String pc_url = Util.null2String(
//				Prop.getPropValue("ERPSetting", "pc_url")).trim();
//		String tempurl = pc_url + requestid;
//		return tempurl;
//	}
//
//	/**
//	 *
//	 * 获取PC端打开流程地址
//	 *
//	 * @param id
//	 * @param requestid
//	 * @return
//	 */
//	private String getWf_Url(String requestid,String userid,String wfid) {
//		String pc_url = Util.null2String(
//				Prop.getPropValue("ERPSetting", "pcjz_url")).trim();
//		System.out.println("配置读取pc_url="+pc_url);
//		if(pc_url==null||"".equals(pc_url))
//		{
//			pc_url="/SSOLogin.aspx?toSystemID=16d79677-ffd3-226a-70e1-48cd35a3d15c&gopage=%2Fworkflow%2Frequest%2FManageRequestNoForm.jsp%3Fviewdoc%3D%26fromFlowDoc%3D%26f_weaver_belongto_userid%3Dcs_userid%26f_weaver_belongto_usertype%3D0%26uselessFlag%3D%26requestid%3Dcs_requestid%26isrequest%3D0%26isovertime%3D0%26isaffirmance%3D%26reEdit%3D1%26seeflowdoc%3D0%26isworkflowdoc%3D1%26isfromtab%3Dfalse%26isSubmitDirect%3D";
//		}
//		String tempurl = pc_url.replaceAll("cs_belongto_userid", userid).replaceAll("cs_requestid", requestid).replaceAll("cs_workflowid", wfid) ;
//		System.out.println("最终tempurl="+tempurl);
//		return tempurl;
//	}
//
//	/**
//	 *
//	 * 获取PC端打开流程地址
//	 *
//	 * @param id
//	 * @param requestid
//	 * @return
//	 */
//	private String getMoblilWf_Url(String requestid) {
//		String mobile_url = Util.null2String(
//				Prop.getPropValue("ERPSetting", "mobile_url")).trim();
//		String tempurl = mobile_url + requestid;
//		return tempurl;
//	}
//
//	/**
//	 *
//	 * 解析ERP返回数据
//	 *
//	 * @param data
//	 * @return
//	 */
//	public String parseResponseData(String data) {
//		String result = "";
//		String code = "";
//		String message = "";
//		JSONObject jsonData = null;
//		try {
//			jsonData = new JSONObject(data);
//			code = jsonData.get("code").toString();
//			message = jsonData.get("data").toString();
//
//			if ("1".equals(code)) {// 成功
//				result = message;
//			} else {// 失败
//				result = "操作失败，" + message;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return result;
//	}
//
//	/**
//	 *
//	 * 获取人员相关信息
//	 *
//	 * @param userid
//	 * @return
//	 */
//	private Map<String, String> getResouceLoginidAndName(String userid) {
//		Map<String, String> resinfo = new HashMap<String, String>();
//		if (userid == null || "".equals(userid)) {
//			return null;
//		}
//
//		// 获取人员相关信息
//		String sqlrtx = "select id, loginid, lastname, email, password,workcode,accounttype,belongto from hrmresource where id = '"
//				+ userid + "' ";
//		RecordSet resrs = new RecordSet();
//		RecordSet rs = new RecordSet();
//		resrs.executeSql(sqlrtx);
//		if (resrs.next()) {
//			String loginid = resrs.getString("loginid");
//			String lastname = resrs.getString("lastname");
//			String email = resrs.getString("email");
//			String password = resrs.getString("password");
//			String workcode = resrs.getString("workcode");
//			String accounttype= resrs.getString("accounttype");
//			String belongto= resrs.getString("belongto");
//
//			if("1".equals(accounttype))
//			{
//				sqlrtx = "select id, loginid, lastname, email, password,workcode,accounttype,belongto from hrmresource where id = '"
//						+ belongto + "' ";
//				rs.executeSql(sqlrtx);
//				if (rs.next()) {
//					 loginid = rs.getString("loginid");
//					 lastname = rs.getString("lastname");
//					 email = rs.getString("email");
//					 password = rs.getString("password");
//					resinfo.put("loginid", loginid);
//					resinfo.put("lastname", lastname);
//					resinfo.put("email", email);
//					resinfo.put("password", password);
//					resinfo.put("workcode", loginid);
//					return resinfo;
//				} else {
//					return null;
//				}
//			}
//			else
//			{
//				resinfo.put("loginid", loginid);
//				resinfo.put("lastname", lastname);
//				resinfo.put("email", email);
//				resinfo.put("password", password);
//				resinfo.put("workcode", workcode);
//				return resinfo;
//			}
//
//		} else {
//			sqlrtx = "select id, loginid, lastname, password from HrmResourceManager where id = '"
//					+ userid + "' ";
//			rs.executeSql(sqlrtx);
//			if (rs.next()) {
//				String loginid = rs.getString("loginid");
//				String lastname = rs.getString("lastname");
//				String email = "";
//				String password = rs.getString("password");
//				resinfo.put("loginid", loginid);
//				resinfo.put("lastname", lastname);
//				resinfo.put("email", email);
//				resinfo.put("password", password);
//				resinfo.put("workcode", loginid);
//				return resinfo;
//			} else {
//				return null;
//			}
//		}
//	}
//
//	/**
//	 *
//	 * 获取人员是否是兼职信息
//	 *
//	 * @param userid
//	 * @return
//	 */
//	private boolean getIsAccounttype(String userid) {
//		boolean isacc=false;
//		if (userid == null || "".equals(userid)) {
//			return false;
//		}
//
//		// 获取人员相关信息
//		String sqlrtx = "select accounttype from hrmresource where id = '"
//				+ userid + "' ";
//		RecordSet resrs = new RecordSet();
//		resrs.executeSql(sqlrtx);
//		if (resrs.next()) {
//			String accounttype= resrs.getString("accounttype");
//
//			if("1".equals(accounttype))
//			{
//				return true;
//			}
//
//		}
//		return  isacc;
//	}
//
//}
