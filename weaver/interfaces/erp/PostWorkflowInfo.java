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
//public class PostWorkflowInfo extends BaseBean {
//
//	/**
//	 * 操作统一待办库(待办,已办)
//	 * ERP流程状态，0：待办，1：已办，4：办结
//	 * @param requestid
//	 */
//	public void operateToDo(String requestid) {
//		writeLog("--------------------推送待办信息到ERP待办库Start(requestid：" + requestid + ")--------------------");
//
//		// 开启统一待办
//		String isopen = Util.null2String(Prop.getPropValue("ERPSetting", "isopen"));
//		// OA系统ID(ERP提供)
//		String systemID = Util.null2String(Prop.getPropValue("ERPSetting", "systemID"));
//		// OA系统名称(ERP提供)
//		String systemName = Util.null2String(Prop.getPropValue("ERPSetting", "systemName"));
//		// OA待办流程列表页面
//		String pc_list = Util.null2String(Prop.getPropValue("ERPSetting", "pc_list"));
//		if (!"true".equalsIgnoreCase(isopen)) {
//			writeLog("----------在ERPSetting.properties配置文件中没有开启统一待办提醒功能！----------");
//			return;
//		}
//		if (requestid == null || "".equals(requestid)) {
//			writeLog("----------requestid为空！----------");
//			return;
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
//			rs.executeSql("select * from workflow_requestbase where requestid = " + requestid);
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
//					rs1.executeSql("select * from workflow_base where id = " + workflowid);
//					if (rs1.next()) {
//						workflowname = Util.null2String(rs1.getString("workflowname"));
//					}
//				}
//				requestname = Util.null2String(rs.getString("requestname"));
//				createdatetime = Util.null2String(rs.getString("createdate") + " " + rs.getString("createtime"));
//			}
//
//			if (!"".equals(workflowid)) {
//				// 公文管理下的流程才推送到ERP待办库
//				rs2.executeSql("select * from workflow_base where workflowtype = (select id from workflow_type where typename in ('公文管理')) and id = " + workflowid);
//				if (rs2.next()) {
//					writeLog("-----流程ID：" + workflowid + "，流程名称：" + workflowname + "，请求ID：" + requestid + "，创建人ID：" + createrid + "，创建人姓名：" + creatername + "-----");
//
//					// 删除
//					deleteToDo(requestid);
//
//					ERPHelper erpHelper = new ERPHelper();
//
//					rs3.executeSql("select * from workflow_currentoperator where requestid = " + requestid + " order by id desc ");
//					while (rs3.next()) {
//						// 主键
//						String id = Util.null2String(rs3.getString("id"));
//						// 接收人
//						String receiverid = Util.null2String(rs3.getString("userid"));
//						// 接收人姓名
//						String receivername = "";
//						if (!"".equals(receiverid)) {
//							Map<String, String> utemp = getResouceLoginidAndName(receiverid);
//							if (utemp != null) {
//								receivername = utemp.get("lastname");
//							}
//						}
//						// 节点ID
//						String nodeid = Util.null2String(rs3.getString("nodeid"));
//						// 节点名称
//						String nodename = "";
//						rs4.executeSql("select nodename from workflow_nodebase where id = " + nodeid);
//						if (rs4.next()) {
//							nodename = Util.null2String(rs4.getString("nodename"));
//						}
//						// 接收时间
//						String receivedatetime = Util.null2String(rs3.getString("receivedate") + " " + rs3.getString("receivetime"));
//						// PC端地址
//						String pc_url = getWf_Url(requestid);
//						// 最后一次操作记录：1
//						String islasttimes = Util.null2String(rs3.getString("islasttimes"));
//						// 操作类型，0：未操作、1：转发、2：已操作、4：归档、8：抄送(不需提交)、9：抄送(需提交)
//						String isremark = Util.null2String(rs3.getString("isremark"));
//						writeLog("-----接收人ID：" + receiverid + "，接收人姓名：" + receivername + "，isremark：" + isremark + "-----");
//
//						// 传入ERP
//						if ("1".equals(islasttimes)) {
//							if ("0".equals(isremark) || "1".equals(isremark) || "8".equals(isremark) || "9".equals(isremark)) {// 待办
//								try {
//									Object[] params = new Object[] {systemID, systemName, "01", requestid + "_" + id, requestname, nodename, createdatetime, receivedatetime, createrid, creatername, receiverid, "", pc_url, "0", "0", "", pc_list, workflowname, "", "", ""};
//									String returnInfo = erpHelper.sendTODO(params);
//									writeLog("-----operateToDo(待办)，" + parseResponseData(returnInfo) + "-----");
//								} catch (Exception e) {
//									writeLog("-----operateToDo(待办)，插入信息出错：-----", e);
//								}
//							} else if ("2".equals(isremark)) {// 已办
//								try {
//									Object[] params = new Object[] {systemID, systemName, "01", requestid + "_" + id, requestname, nodename, createdatetime, receivedatetime, createrid, creatername, receiverid, "", pc_url, "1", "0", "", pc_list, workflowname, "", "", ""};
//									String returnInfo = erpHelper.sendTODO(params);
//									writeLog("-----operateToDo(已办)，" + parseResponseData(returnInfo) + "-----");
//								} catch (Exception e) {
//									writeLog("-----operateToDo(已办)，插入信息出错：-----" + e);
//								}
//							} else if ("4".equals(isremark)) {// 办结
//								try {
//									Object[] params = new Object[] {systemID, systemName, "01", requestid + "_" + id, requestname, nodename, createdatetime, receivedatetime, createrid, creatername, receiverid, "", pc_url, "4", "0", "", pc_list, workflowname, "", "", ""};
//									String returnInfo = erpHelper.sendTODO(params);
//									writeLog("-----operateToDo(办结)，" + parseResponseData(returnInfo) + "-----");
//								} catch (Exception e) {
//									writeLog("-----operateToDo(办结)，插入信息出错：-----" + e);
//								}
//							}
//						}
//
//						// 插入中间表
//						rs5.executeSql("insert into erp_todolog(id, workflowid, requestid, userid, islasttimes, isremark) values(" + id + ", " + workflowid + ", " + requestid + ", " + receiverid + ", " + islasttimes + ", '" + isremark + "') ");
//					}
//				}
//			}
//		} catch (Exception e) {
//			writeLog("----------推送待办信息到ERP待办库出错：----------", e);
//		}
//		writeLog("--------------------推送待办信息到ERP待办库End(requestid：" + requestid + ")--------------------");
//	}
//
//	/**
//	 *
//	 * 征询改待办
//	 * ERP流程状态，0：待办，1：已办，4：办结
//	 * @param requestid
//	 * @param id
//	 */
//	public void operateZXToDo(String requestid, int id) {
//		writeLog("----------征询改待办Start(requestid：" + requestid + ")----------");
//
//		// 开启统一待办
//		String isopen = Util.null2String(Prop.getPropValue("ERPSetting", "isopen"));
//		// OA系统ID(ERP提供)
//		String systemID = Util.null2String(Prop.getPropValue("ERPSetting", "systemID"));
//		// OA系统名称(ERP提供)
//		String systemName = Util.null2String(Prop.getPropValue("ERPSetting", "systemName"));
//		// OA待办流程列表页面
//		String pc_list = Util.null2String(Prop.getPropValue("ERPSetting", "pc_list"));
//		if (!"true".equalsIgnoreCase(isopen)) {
//			writeLog("----------在ERPSetting.properties配置文件中没有开启统一待办提醒功能！----------");
//			return;
//		}
//		if (requestid == null || "".equals(requestid)) {
//			writeLog("----------requestid为空！----------");
//			return;
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
//			rs.executeSql("select * from workflow_requestbase where requestid = " + requestid);
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
//					rs1.executeSql("select * from workflow_base where id = " + workflowid);
//					if (rs1.next()) {
//						workflowname = Util.null2String(rs1.getString("workflowname"));
//					}
//				}
//				requestname = Util.null2String(rs.getString("requestname"));
//				createdatetime = Util.null2String(rs.getString("createdate") + " " + rs.getString("createtime"));
//			}
//
//			if (!"".equals(workflowid)) {
//				// 公文管理下的流程
//				rs2.executeSql("select * from workflow_base where workflowtype = (select id from workflow_type where typename in ('公文管理')) and id = " + workflowid);
//				if (rs2.next()) {
//					writeLog("-----流程ID：" + workflowid + "，流程名称：" + workflowname + "，请求ID：" + requestid + "，创建人ID：" + createrid + "，创建人姓名：" + creatername + "-----");
//
//					ERPHelper erpHelper = new ERPHelper();
//
//					rs3.executeSql("select * from workflow_currentoperator where requestid = " + requestid + " and islasttimes = 1 and id = " + id + " order by id ");
//					while(rs3.next()) {
//						// 接收人
//						String receiverid = Util.null2String(rs3.getString("userid"));
//						// 接收人姓名
//						String receivername = "";
//						if (!"".equals(receiverid)) {
//							Map<String, String> utemp = getResouceLoginidAndName(receiverid);
//							if (utemp != null) {
//								receivername = utemp.get("lastname");
//							}
//						}
//						// 节点ID
//						String nodeid = Util.null2String(rs3.getString("nodeid"));
//						// 节点名称
//						String nodename = "";
//						rs4.executeSql("select nodename from workflow_nodebase where id = " + nodeid);
//						if (rs4.next()) {
//							nodename = Util.null2String(rs4.getString("nodename"));
//						}
//						// 接收时间
//						String receivedatetime = Util.null2String(rs3.getString("receivedate") + " " + rs3.getString("receivetime"));
//						// PC端地址
//						String pc_url = getWf_Url(requestid);
//						// 操作类型，0：未操作、1：转发、2：已操作、4：归档、8：抄送(不需提交)、9：抄送(需提交)
//						String isremark = Util.null2String(rs3.getString("isremark"));
//						writeLog("-----接收人ID：" + receiverid + "，接收人姓名：" + receivername + "，isremark：" + isremark + "-----");
//
//						if ("0".equals(isremark) || "1".equals(isremark) || "8".equals(isremark) || "9".equals(isremark)) {// 待办
//							try {
//								Object[] params = new Object[] {systemID, systemName, "01", requestid + "_" + id, requestname, nodename, createdatetime, receivedatetime, createrid, creatername, receiverid, "", pc_url, "0", "0", "", pc_list, workflowname, "", "", ""};
//								String returnInfo = erpHelper.sendTODO(params);
//								writeLog("-----operateZXToDo(待办)，" + parseResponseData(returnInfo) + "-----");
//							} catch (Exception e) {
//								writeLog("-----operateZXToDo(待办)，插入信息(征询改待办)出错：-----", e);
//							}
//						} else if ("2".equals(isremark)) {// 已办
//							try {
//								Object[] params = new Object[] {systemID, systemName, "01", requestid + "_" + id, requestname, nodename, createdatetime, receivedatetime, createrid, creatername, receiverid, "", pc_url, "1", "0", "", pc_list, workflowname, "", "", ""};
//								String returnInfo = erpHelper.sendTODO(params);
//								writeLog("-----operateZXToDo(已办)，" + parseResponseData(returnInfo) + "-----");
//							} catch (Exception e) {
//								writeLog("-----operateZXToDo(已办)，插入信息(征询改待办)出错：-----", e);
//							}
//						} else if ("4".equals(isremark)) {// 办结
//							try {
//								Object[] params = new Object[] {systemID, systemName, "01", requestid + "_" + id, requestname, nodename, createdatetime, receivedatetime, createrid, creatername, receiverid, "", pc_url, "4", "0", "", pc_list, workflowname, "", "", ""};
//								String returnInfo = erpHelper.sendTODO(params);
//								writeLog("-----operateZXToDo(办结)，" + parseResponseData(returnInfo) + "-----");
//							} catch (Exception e) {
//								writeLog("-----operateZXToDo(办结)，插入信息(征询改待办)出错：-----", e);
//							}
//						}
//
//						// 插入中间表
//						rs5.executeSql("insert into erp_todolog(id, workflowid, requestid, userid, islasttimes, isremark) values(" + id + ", " + workflowid + ", " + requestid + ", " + receiverid + ", 1, '" + isremark + "') ");
//					}
//				}
//			}
//		} catch(Exception e) {
//			writeLog("----------征询改待办出错：----------", e);
//		}
//		writeLog("----------征询改待办End(requestid：" + requestid + ")----------");
//	}
//
//	/**
//	 *
//	 * 查看变已办情景处理
//	 * ERP流程状态，0：待办，1：已办，4：办结
//	 * @param requestid
//	 * @param userid
//	 */
//	public void ViewToHandle(String requestid, String userid) {
//		writeLog("----------查看变已办处理Start(requestid：" + requestid + "，userid：" + userid + ")----------");
//
//		// 开启统一待办
//		String isopen = Util.null2String(Prop.getPropValue("ERPSetting", "isopen"));
//		// OA系统ID(ERP提供)
//		String systemID = Util.null2String(Prop.getPropValue("ERPSetting", "systemID"));
//		if (!"true".equalsIgnoreCase(isopen)) {
//			writeLog("----------在ERPSetting.properties配置文件中没有开启统一待办提醒功能！----------");
//			return;
//		}
//		if (requestid == null || "".equals(requestid)) {
//			writeLog("----------requestid为空！----------");
//			return;
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
//			rs.executeSql("select * from workflow_requestbase where requestid = " + requestid);
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
//					rs1.executeSql("select * from workflow_base where id = " + workflowid);
//					if (rs1.next()) {
//						workflowname = Util.null2String(rs1.getString("workflowname"));
//					}
//				}
//			}
//
//			if (!"".equals(workflowid)) {
//				// 公文管理下的流程
//				rs2.executeSql("select * from workflow_base where workflowtype = (select id from workflow_type where typename in ('公文管理')) and id = " + workflowid);
//				if (rs2.next()) {
//					writeLog("-----流程ID：" + workflowid + "，流程名称：" + workflowname + "，请求ID：" + requestid + "，创建人ID：" + createrid + "，创建人姓名：" + creatername + "-----");
//
//					ERPHelper erpHelper = new ERPHelper();
//
//					rs3.executeSql("select * from workflow_currentoperator where requestid = " + requestid + " and userid = " + userid + " and islasttimes = 1 order by id desc ");
//					while (rs3.next()) {
//						// 主键
//						String id = Util.null2String(rs3.getString("id"));
//						// 接收人
//						String receiverid = Util.null2String(rs3.getString("userid"));
//						// 接收人姓名
//						String receivername = "";
//						if (!"".equals(receiverid)) {
//							Map<String, String> utemp = getResouceLoginidAndName(receiverid);
//							if (utemp != null) {
//								receivername = utemp.get("lastname");
//							}
//						}
//						// 操作类型，0：未操作、1：转发、2：已操作、4：归档、8：抄送(不需提交)、9：抄送(需提交)
//						String isremark = Util.null2String(rs3.getString("isremark"));
//
//						writeLog("-----接收人ID：" + receiverid + "，接收人姓名：" + receivername + "，isremark：" + isremark + "-----");
//
//						// 更新状态
//						try {
//							Object[] params = new Object[] {requestid + "_" + id, systemID, "1"};
//							String returnInfo = erpHelper.updateTODO(params);
//							writeLog("-----ViewToHandle，" + parseResponseData(returnInfo) + "-----");
//						} catch (Exception e) {
//							writeLog("-----ViewToHandle，更新状态(查看变已办)出错：-----", e);
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			writeLog("----------更新状态(查看变已办)出错：----------", e);
//		}
//		writeLog("----------查看变已办处理End(requestid：" + requestid + "，userid：" + userid + ")----------");
//	}
//
//	/**
//	 * 操作统一待办库(根据userid删除流程)
//	 *
//	 * @param requestid
//	 */
//	public void deleteUserToDo(String requestid, String userid) {
//		writeLog("----------根据requestid和userid删除ERP待办库中OA流程Start(requestid：" + requestid + "，userid：" + userid + ")----------");
//
//		// 开启统一待办
//		String isopen = Util.null2String(Prop.getPropValue("ERPSetting", "isopen"));
//		// OA系统ID(ERP提供)
//		String systemID = Util.null2String(Prop.getPropValue("ERPSetting", "systemID"));
//		if (!"true".equalsIgnoreCase(isopen)) {
//			writeLog("----------在ERPSetting.properties配置文件中没有开启统一待办提醒功能！----------");
//			return;
//		}
//		if (requestid == null || "".equals(requestid)) {
//			writeLog("----------requestid为空！----------");
//			return;
//		}
//
//		try {
//			RecordSet rs = new RecordSet();
//			RecordSet rs1 = new RecordSet();
//
//			rs.executeSql("select * from erp_todolog where requestid = " + requestid + " and userid = " + userid);
//			while (rs.next()) {
//				ERPHelper erpHelper = new ERPHelper();
//				// 主键
//				String id = Util.null2String(rs.getString("id"));
//				// 最后一次操作记录：1
//				String islasttimes = Util.null2String(rs.getString("islasttimes"));
//
//				String returnInfo = "";
//				if ("1".equals(islasttimes)) {
//					Object[] params = new Object[] {requestid + "_" + id, systemID};
//					returnInfo = erpHelper.deleteTODO(params);
//				}
//
//				rs1.executeSql("delete from erp_todolog where id = " + id);
//				writeLog("-----deleteUserToDo，" + parseResponseData(returnInfo) + "(requestid：" + requestid + "，id：" + id + ")-----");
//			}
//		} catch (Exception e) {
//			writeLog("----------根据requestid和userid删除ERP待办库中OA流程出错：----------", e);
//		}
//		writeLog("----------根据requestid和userid删除ERP待办库中OA流程End(requestid：" + requestid + "，userid：" + userid + ")----------");
//	}
//
//	/**
//	 *
//	 * 操作统一待办库(根据requestid删除流程)
//	 * @param requestid
//	 */
//	public void deleteToDo(String requestid) {
//		writeLog("----------根据requestid删除ERP待办库中OA流程Start(requestid：" + requestid + ")----------");
//
//		// 开启统一待办
//		String isopen = Util.null2String(Prop.getPropValue("ERPSetting", "isopen"));
//		// OA系统ID(ERP提供)
//		String systemID = Util.null2String(Prop.getPropValue("ERPSetting", "systemID"));
//		if (!"true".equalsIgnoreCase(isopen)) {
//			writeLog("----------在ERPSetting.properties配置文件中没有开启统一待办提醒功能！----------");
//			return;
//		}
//		if (requestid == null || "".equals(requestid)) {
//			writeLog("----------requestid为空！----------");
//			return;
//		}
//
//		try {
//			RecordSet rs = new RecordSet();
//			RecordSet rs1 = new RecordSet();
//
//			rs.executeSql("select * from erp_todolog where requestid = " + requestid);
//			while (rs.next()) {
//				ERPHelper erpHelper = new ERPHelper();
//				// 主键
//				String id = Util.null2String(rs.getString("id"));
//				// 最后一次操作记录：1
//				String islasttimes = Util.null2String(rs.getString("islasttimes"));
//
//				String returnInfo = "";
//				if ("1".equals(islasttimes)) {
//					Object[] params = new Object[] {requestid + "_" + id, systemID};
//					returnInfo = erpHelper.deleteTODO(params);
//				}
//
//				rs1.executeSql("delete from erp_todolog where id = " + id);
//				writeLog("-----deleteToDo，" + parseResponseData(returnInfo) + "(requestid：" + requestid + "，id：" + id + ")-----");
//			}
//		} catch (Exception e) {
//			writeLog("----------根据requestid删除ERP待办库中OA流程出错：----------", e);
//		}
//		writeLog("----------根据requestid删除ERP待办库中OA流程End(requestid：" + requestid + ")----------");
//	}
//
//	/**
//	 * 强制归档，先删除，再推送
//	 *
//	 * @param requestid
//	 */
//	public void drawBackWF(String requestid) {
//		writeLog("----------强制归档，先删除，再推送Start(requestid：" + requestid + ")----------");
//		// 先删除
//		deleteToDo(requestid);
//		// 再推送
//		operateToDo(requestid);
//		writeLog("----------强制归档，先删除，再推送End(requestid：" + requestid + ")----------");
//	}
//
//	/**
//	 *
//	 * 获取PC端打开流程地址
//	 * @param id
//	 * @param requestid
//	 * @return
//	 */
//	private String getWf_Url(String requestid) {
//		String pc_url = Util.null2String(Prop.getPropValue("ERPSetting", "pc_url")).trim();
//		String tempurl = pc_url + requestid;
//		return tempurl;
//	}
//
//	/**
//	 *
//	 * 解析ERP返回数据
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
//		String  sqlrtx = "select id, loginid, lastname, email, password from hrmresource where id = '" + userid + "' ";
//		RecordSet resrs = new RecordSet();
//		RecordSet rs = new RecordSet();
//		resrs.executeSql(sqlrtx);
//		if (resrs.next()) {
//			String loginid = resrs.getString("loginid");
//			String lastname = resrs.getString("lastname");
//			String email = resrs.getString("email");
//			String password = resrs.getString("password");
//			resinfo.put("loginid", loginid);
//			resinfo.put("lastname", lastname);
//			resinfo.put("email", email);
//			resinfo.put("password", password);
//			return resinfo;
//		} else {
//			sqlrtx = "select id, loginid, lastname, email, password from HrmResourceManager where id = '" + userid + "' ";
//			rs.executeSql(sqlrtx);
//			if(rs.next()) {
//				String loginid = rs.getString("loginid");
//				String lastname = rs.getString("lastname");
//				String email = rs.getString("email");
//				String password = rs.getString("password");
//				resinfo.put("loginid", loginid);
//				resinfo.put("lastname", lastname);
//				resinfo.put("email", email);
//				resinfo.put("password", password);
//				return resinfo;
//			} else {
//				return null;
//			}
//		}
//	}
//
//}
