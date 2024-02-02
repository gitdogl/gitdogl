//package com.engine.interfaces.zxl.utils.file;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import weaver.conn.RecordSet;
//import weaver.conn.RecordSetTrans;
//import weaver.formmode.data.ModeDataIdUpdate;
//import weaver.general.BaseBean;
//import weaver.general.GCONST;
//import weaver.general.TimeUtil;
//import weaver.general.Util;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class DocSaveService extends ToolUtil {
//    String className = this.getClass().getName();
//
//    RecordSetTrans rsts = null;
//    String tablename = "";
//    String requestId;
//    String workflowId;
//
//    protected String[] baseArray = new String[4];
//
//    //自定义参数值
//    private String cusparam = "";
//    private int userid = 0;
//
//    /**
//     * @param: param(Map collections)
//     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
//     */
//    public Map execute(Map<String, Object> params) {
//        // 示例：data：定义的请求数据，code:定义的响应数据
//        requestId = Util.null2String(params.get("requestid"));
//        cusparam = Util.null2String(params.get("cusparam"));
//        userid = Util.getIntValue(Util.null2String(params.get("userid")));
//
//        if (this.rsts == null) {
//            this.rsts = new RecordSetTrans();
//        }
//
//        initParam();
//        return handle();
//
//    }
//
//    private void initParam() {
//
//
//        String select_base_sql = "select * from workflow_requestbase where requestid = ?";
//
//        try {
//            if (this.rsts == null) {
//                this.rsts = new RecordSetTrans();
//            }
//
//            String request_name = "";
//            String request_mark = "";
//            if (this.rsts.executeQuery(select_base_sql, new Object[]{this.requestId})) {
//                while (this.rsts.next()) {
//                    request_name = Util.null2String(this.rsts.getString("requestname"));
//                    request_mark = Util.null2String(this.rsts.getString("requestmark"));
//                    this.workflowId = Util.null2String(this.rsts.getString("workflowid"));
//                }
//            }
//            if ("".equals(this.tablename)) {
//                this.tablename = getBillTableNameByWorkflowId(this.workflowId);
//            }
//
//
//            this.baseArray[0] = this.requestId;
//            this.baseArray[1] = request_name;
//            this.baseArray[2] = request_mark;
//            this.baseArray[3] = Util.null2String(this.userid);
//            this.writeNewDebuggerLog(this.className, "main_requestname:[" + request_name + "],main_requestmark:[" + request_mark + "],workflowid:[" + this.workflowId + "],requestid:[" + this.requestId + "],tablename:[" + this.tablename + "]");
//        } catch (Exception var4) {
//            var4.printStackTrace();
//            this.writeNewDebuggerLog(this.className, "get workflow dataset error:[" + var4.getMessage() + "/" + var4.toString() + "]");
//        }
//
//    }
//
//    private Map<String, String> handle() {
//        Map<String, String> ret = new HashMap<>();
//        // TODO Auto-generated method stub
//        try {
//
//            this.writeNewDebuggerLog(className, "------------------" + className + " BEGIN-----------------");
//
//            boolean cansubmit = true;
//
//            VoucherLogger vlog = new VoucherLogger();
//            vlog.setOperator(this.userid);
//            vlog.setOperatedatetime(TimeUtil.getCurrentTimeString());
//            vlog.setWorkflowid(this.workflowId);
//            vlog.setRequestid(this.requestId);
//            vlog.setRequestmark(this.baseArray[2]);
//            ;
//
//            this.writeNewDebuggerLog(className, "cusparam:[" + cusparam + "]");
//
//            //获取当前流程对应的配置信息
//            VoucherUtil vu = new VoucherUtil();
//            Map<String, Object> cmap = vu.getconfig(this.workflowId, cusparam);
//
//            if (cmap != null) {
//                //获取其对应的数据条件
//                String condition = (String) cmap.get("datacondition");
//
//                String eventid = (String) cmap.get("eventid");
//
//                String select_wf_main = "select * from " + tablename + " where requestid = ?";
//
//                if (!"".equals(condition)) {
//                    select_wf_main += " and " + condition;
//                }
//
//                this.writeNewDebuggerLog(className, "Select Workflow Main Data SQL :[" + select_wf_main + "]");
//
//                int backfieldid = (Integer) cmap.get("backfield");
//                int configid = (Integer) cmap.get("mainkeyid");
//                vlog.setConfigid(configid);
//
//                //回写的字段名称
//                String backfieldname = vu.getFieldNameByFieldid_Single(backfieldid);
//
//                RecordSet rs = new RecordSet();
//
//                //需传输的JSON字符串
//                JSONObject jsonobj = new JSONObject();
//
//                //表头配置信息集合
//                List<Map<String, Object>> head_list = (List<Map<String, Object>>) cmap.get("head_list");
//
//                int mainkeyid = 0;
//                if (rs.executeQuery(select_wf_main, requestId)) {
//                    if (rs.next()) {
//                        mainkeyid = Util.getIntValue(rs.getString("id"), 0);
//
//                        jsonobj = vu.genarate_json(baseArray, head_list, rs, null, 0, "", 0);
//                    }
//                }
//
//                //明细序列
//                String detailIndex = (String) cmap.get("detailIndex");
//
//                //普通借方
//                List<Map<String, Object>> com_debit_list = (List<Map<String, Object>>) cmap.get("com_debit_list");
//                //税额借方
//                List<Map<String, Object>> tax_debit_list = (List<Map<String, Object>>) cmap.get("tax_debit_list");
//                //普通贷方
//                List<Map<String, Object>> com_loan_list = (List<Map<String, Object>>) cmap.get("com_loan_list");
//
//                //冲销贷方字段配置集合
//                List<Map<String, Object>> cx_loan_list = (List<Map<String, Object>>) cmap.get("cx_loan_list");
//                //中转借方字段配置集合
//                List<Map<String, Object>> zz_debit_list = (List<Map<String, Object>>) cmap.get("zz_debit_list");
//                //中转贷方字段配置集合
//                List<Map<String, Object>> zz_loan_list = (List<Map<String, Object>>) cmap.get("zz_loan_list");
//
//
//                this.writeNewDebuggerLog(className, "detailIndex:[" + detailIndex + "],backfieldname:[" + backfieldname + "]");
//
//                net.sf.json.JSONArray detailArray = new net.sf.json.JSONArray();
//
//                int rowIndex = 1;
//                if (mainkeyid > 0) {
//                    if ("".equals(detailIndex) || "0".equals(detailIndex)) {//说明所有借贷方皆来自主表记录
//                        //普通借方
//                        if (com_debit_list != null && com_debit_list.size() > 0) {
//                            JSONObject com_debit_obj = vu.genarate_json(baseArray, com_debit_list, rs, null, 1, "", rowIndex++);
//                            if (com_debit_obj != null && Util.getDoubleValue(Util.null2String(com_debit_obj.get("WRBTR")), 0) != 0) {
//                                detailArray.add(com_debit_obj);
//                            }
//                        }
//                        //税额借方
//                        if (tax_debit_list != null && tax_debit_list.size() > 0) {
//                            JSONObject tax_debit_obj = vu.genarate_json(baseArray, tax_debit_list, rs, null, 1, "", rowIndex++);
//                            if (tax_debit_obj != null && Util.getDoubleValue(Util.null2String(tax_debit_obj.get("WRBTR")), 0) != 0) {
//                                detailArray.add(tax_debit_obj);
//                            }
//                        }
//                        //冲销贷方
//                        if (cx_loan_list != null && cx_loan_list.size() > 0) {
//                            JSONObject cx_loan_obj = vu.genarate_json(baseArray, cx_loan_list, rs, null, 1, "", rowIndex++);
//                            if (cx_loan_obj != null && Util.getDoubleValue(Util.null2String(cx_loan_obj.get("WRBTR")), 0) != 0) {
//                                detailArray.add(cx_loan_obj);
//                            }
//                        }
//                        //中转借方
//                        if (zz_debit_list != null && zz_debit_list.size() > 0) {
//                            JSONObject zz_debit_obj = vu.genarate_json(baseArray, zz_debit_list, rs, null, 1, "", rowIndex++);
//                            if (zz_debit_obj != null && Util.getDoubleValue(Util.null2String(zz_debit_obj.get("WRBTR")), 0) != 0) {
//                                detailArray.add(zz_debit_obj);
//                            }
//                        }
//                        //中转贷方
//                        if (zz_loan_list != null && zz_loan_list.size() > 0) {
//                            JSONObject zz_loan_obj = vu.genarate_json(baseArray, zz_loan_list, rs, null, 1, "", rowIndex++);
//                            if (zz_loan_obj != null && Util.getDoubleValue(Util.null2String(zz_loan_obj.get("WRBTR")), 0) != 0) {
//                                detailArray.add(zz_loan_obj);
//                            }
//                        }
//
//
//                    } else {//说明借方来自明细记录
//                        String[] detailindexArray = com.weaver.general.Util.TokenizerString2(detailIndex, ",");
//
//                        RecordSet rs_detail = new RecordSet();
//
//                        for (String detailRow : detailindexArray) {//遍历明细序列
//                            //查询明细表记录
//                            String dtTableName = tablename + "_dt" + detailRow;
//                            String select_detail = "select * from " + dtTableName + " where mainid = ?";
//
//                            if (rs_detail.executeQuery(select_detail, mainkeyid)) {
//                                while (rs_detail.next()) {
//                                    this.writeNewDebuggerLog(className, "rowIndex:[" + rowIndex + "]");
//
//                                    //普通借方
//                                    if (com_debit_list != null && com_debit_list.size() > 0) {
//                                        JSONObject com_debit_obj = vu.genarate_json(baseArray, com_debit_list, rs, rs_detail, 1, dtTableName, rowIndex++);
//                                        if (com_debit_obj != null && Util.getDoubleValue(Util.null2String(com_debit_obj.get("WRBTR")), 0) != 0) {
//                                            detailArray.add(com_debit_obj);
//                                        }
//                                    }
//                                    //税额借方
//                                    if (tax_debit_list != null && tax_debit_list.size() > 0) {
//                                        JSONObject tax_debit_obj = vu.genarate_json(baseArray, tax_debit_list, rs, rs_detail, 1, dtTableName, rowIndex++);
//                                        if (tax_debit_obj != null && Util.getDoubleValue(Util.null2String(tax_debit_obj.get("WRBTR")), 0) != 0) {
//                                            detailArray.add(tax_debit_obj);
//                                        }
//                                    }
//
//                                    //冲销贷方字段配置集合
//                               /* if(cx_loan_list != null && cx_loan_list.size() > 0){
//                                    JSONObject cx_loan_obj = vu.genarate_json(baseArray, cx_loan_list, rs, rs_detail,1,dtTableName,rowIndex++);
//                                    if(cx_loan_obj!=null) {
//                                        detailArray.add(cx_loan_obj);
//                                    }
//                                }
//                                *
//                                */
//                                    //中转借方字段配置集合
//                                    if (zz_debit_list != null && zz_debit_list.size() > 0) {
//                                        JSONObject zz_debit_obj = vu.genarate_json(baseArray, zz_debit_list, rs, rs_detail, 1, dtTableName, rowIndex++);
//                                        if (zz_debit_obj != null && Util.getDoubleValue(Util.null2String(zz_debit_obj.get("WRBTR")), 0) != 0) {
//                                            detailArray.add(zz_debit_obj);
//                                        }
//                                    }
//                                    //中转贷方字段配置集合
//                                    if (zz_loan_list != null && zz_loan_list.size() > 0) {
//                                        JSONObject zz_loan_obj = vu.genarate_json(baseArray, zz_loan_list, rs, rs_detail, 1, dtTableName, rowIndex++);
//                                        if (zz_loan_obj != null && Util.getDoubleValue(Util.null2String(zz_loan_obj.get("WRBTR")), 0) != 0) {
//                                            detailArray.add(zz_loan_obj);
//                                        }
//                                    }
//
//                                    //冲销贷方字段配置集合
////                                    if (cx_loan_list != null && cx_loan_list.size() > 0) {
////                                        JSONObject cx_loan_obj = vu.genarate_json(baseArray, cx_loan_list, rs, null, 1, "", rowIndex++);
////                                        if (cx_loan_obj != null && Util.getDoubleValue(Util.null2String(cx_loan_obj.get("WRBTR")), 0) != 0) {
////                                            detailArray.add(cx_loan_obj);
////                                        }
////                                    }
//
//
//                                }
//                            }
//                        }
//
//                        //冲销贷方字段配置集合
//                        if (cx_loan_list != null && cx_loan_list.size() > 0) {
//                            JSONObject cx_loan_obj = vu.genarate_json(baseArray, cx_loan_list, rs, null, 1, "", rowIndex++);
//                            if (cx_loan_obj != null && Util.getDoubleValue(Util.null2String(cx_loan_obj.get("WRBTR")), 0) != 0) {
//                                detailArray.add(cx_loan_obj);
//                            }
//                        }
//
//
//                    }
//
//                    JSONObject com_loan_obj = vu.genarate_json(baseArray, com_loan_list, rs, null, 1, "", rowIndex++);
//                    detailArray.add(com_loan_obj);
//
//                    jsonobj.put("T_ITEM", detailArray);
//
//                    this.writeNewDebuggerLog(className, "Send JSON Data:" + jsonobj.toString());
//
//                    //待发送的JSON字符串
//                    vlog.setSenddata(jsonobj.toString());
//                    String message = "接口测试类";
//                    JSONObject resultobj = vu.sendPost_New(eventid, jsonobj);
//
//                    if (resultobj != null) {
//                        this.writeNewDebuggerLog(className, "Receive JSON Data:" + resultobj.toString());
//
//                        vlog.setReceivedata(resultobj.toString());
//                        String code = Util.null2String(resultobj.get("code"));
//                        String msg = Util.null2String(resultobj.get("msg"));
//                        if ("100".equals(code)) {
//                            String data = Util.null2String(resultobj.get("data"));
//                            JSONObject dataobj = JSONObject.parseObject(data);
//                            String EV_STATUS = dataobj.getString("EV_STATUS");
//                            if ("S".equals(EV_STATUS)) {//说明成功
//                                vlog.setResult(1);
//
//                                cansubmit = true;
//                                //获取返回成功的凭证号
//                                String EV_BELNR = Util.null2String(dataobj.getString("EV_BELNR"));
//                                String EV_MESSAGE = Util.null2String(dataobj.getString("EV_MESSAGE"));
//                                if (!"".equals(backfieldname)) {
//                                    String update_sql = "update " + tablename + " set " + backfieldname + "='" + EV_BELNR + "' where requestid = ?";
//
//                                    rs.executeUpdate(update_sql, requestId);
//                                }
//                                message = EV_MESSAGE;
//                                vlog.setMessage(message);
//
//                            } else {//说明失败
//                                vlog.setResult(2);
//                                message = resultobj.get("msg").toString();
//                                vlog.setMessage(message);
//                                cansubmit = false;
//                            }
//                        } else {
//                            //报错
//                            //说明失败
//                            vlog.setResult(2);
//                            message = msg;
//                            vlog.setMessage(message);
//                            cansubmit = false;
//                        }
//
//
//                    }
//                } else {
//                    vlog.setResult(0);
//                    vlog.setMessage("该流程数据不满足自定义条件:[" + condition + "]！");
//                }
//            } else {
//                vlog.setResult(0);
//                vlog.setMessage("该流程当前参数值:[" + this.cusparam + "]对应的配置不存在！");
//            }
//
//            //写入接口调用日志
//            vu.writeinterfacelog(vlog);
//
//            if (!"".equals(vlog.getMessage()) && !"凭证创建成功".equals(vlog.getMessage())) {
//                cansubmit = false;
//            }
//
//
//            if (!cansubmit) {
//                ret.put("code", "0");
//                ret.put("msg", "相关请求：" + requestId + vlog.getMessage());
//
//                return ret;
//            }
//
//            this.writeNewDebuggerLog(className, "------------------" + className + " END-----------------");
//            ret.put("code", "1");
//            ret.put("msg", "相关请求：" + requestId + "处理成功");
//            return ret;
//        } catch (Exception s) {
//            s.printStackTrace();
//            ret.put("code", "0");
//            ret.put("msg", "相关请求：" + requestId + s.getMessage());
//            return ret;
//        }
//    }
//
//
//}
//
//class ToolUtil extends BaseBean {
//    //是否开启调试模式
//    boolean isDebug = false;
//
//    //日志层级 （0：调试[Debugger] 1：警告[Warning] 2：错误[Error]
//    private int logLevel = -1;
//
//    private static RecordSet rs = new RecordSet();
//
//    /**
//     * 构造方法
//     */
//    public ToolUtil() {
//        // TODO Auto-generated constructor stub
//        //是否开启日志模式
//        String isopen = getSystemParamValue("Debug_Mode");
//
//        //输出日志级别
//        logLevel = Util.getIntValue(getSystemParamValue("Logger_Level"), -1);
//
//        if ("1".equals(isopen)) {
//            isDebug = true;
//        }
//    }
//
//    /**
//     * 根据流程类型ID获取其对应的表单名称
//     *
//     * @param workflowid
//     * @return
//     */
//    public static String getBillTableNameByWorkflowId(String workflowid) {
//        String tablename = "";
//
//        if (!"".equals(workflowid)) {
//            String select_data = "select tablename from workflow_bill where id in (select formid from workflow_base where id = ?)";
//
//            if (rs.executeQuery(select_data, workflowid)) {
//                if (rs.next()) {
//                    tablename = Util.null2String(rs.getString(1));
//                }
//            }
//        }
//
//        return tablename;
//    }
//
//    /**
//     * 查询满足模糊查询的所有标识集合
//     *
//     * @param likestr 模糊条件
//     * @return
//     */
//    public Map<String, String> getSystemParamValueMap(String likestr) {
//        return getSystemParamList(likestr);
//    }
//
//    /**
//     * 查询系统中所有参数配置
//     *
//     * @return
//     */
//    public Map<String, String> getAllSystemParamValue() {
//        return getSystemParamList("");
//    }
//
//
//    /**
//     * 获取参数集合
//     *
//     * @param likestr 模糊查询的条件
//     * @return 集合
//     */
//    private Map<String, String> getSystemParamList(String likestr) {
//        Map<String, String> param_map = new HashMap<String, String>();
//
//        String select_sql = "select uuid,paramvalue from uf_systemconfig";
//
//        RecordSet rs = new RecordSet();
//
//        if (!"".equals(likestr)) {
//            select_sql += " where uuid like '%" + likestr + "%'";
//        }
//
//        if (rs.execute(select_sql)) {
//            while (rs.next()) {
//                String uuid = Util.null2String(rs.getString(1));
//                String paramvalue = Util.null2String(rs.getString(2));
//
//                param_map.put(uuid, paramvalue);
//            }
//        }
//
//        return param_map;
//
//    }
//
//    /**
//     * 获取系统参数设置值
//     *
//     * @param uuid
//     * @return
//     */
//    public String getSystemParamValue(String uuid) {
//        String paramvalue = "";
//
//        if (!"".equals(uuid)) {
//            String select_sql = "select paramvalue from uf_systemconfig where uuid = '" + uuid + "'";
//
//            RecordSet rs = new RecordSet();
//            rs.executeQuery(select_sql);
//            if (rs.next()) {
//                paramvalue = Util.null2String(rs.getString(1));
//            }
//        }
//
//        return paramvalue;
//    }
//
//    /**
//     * 用数据库值，根据规则转换，获取其最终结果
//     *
//     * @param cus_sql 自定义转换的SQL
//     * @param value   参数值
//     * @return
//     */
//    public String getValueByChangeRule(String cus_sql, String value) {
//
//        return getValueByChangeRule(cus_sql, value, "");
//    }
//
//    /**
//     * 用数据库值，根据规则转换，获取其最终结果
//     *
//     * @param cus_sql   自定义转换的SQL
//     * @param value     参数值
//     * @param requestid 流程请求ID
//     * @return
//     */
//    public String getValueByChangeRule(String cus_sql, String value, String requestid) {
//        String endValue = "";
//
//        cus_sql = cus_sql.replace("&nbsp;", " ");
//        cus_sql = cus_sql.replaceAll("ｓｅｌｅｃｔ ", " select ");
//        cus_sql = cus_sql.replaceAll(" ｉｎ ", " in ");
//        cus_sql = cus_sql.replaceAll(" ａｎｄ ", " and ");
//        cus_sql = cus_sql.replaceAll(" ｏｒ ", " or ");
//        cus_sql = cus_sql.replaceAll(" ｊｏｉｎ ", " join ");
//        //参数进行替换
//        String sqlString = cus_sql.replace("{?requestid}", requestid);
//
//        sqlString = sqlString.replace("?", value);
//
//        RecordSet rs = new RecordSet();
//
//        if (rs.executeQuery(sqlString)) {
//            rs.next();
//
//            endValue = Util.null2String(rs.getString(1));
//        }
//
//        return endValue;
//    }
//
//    /**
//     * 用数据库值，根据规则转换，获取其最终结果
//     *
//     * @param cus_sql   自定义转换的SQL
//     * @param value     参数值
//     * @param requestid 流程请求ID
//     * @return
//     */
//    public String getValueByChangeRule(String cus_sql, String value, String requestid, int detailKeyvalue, String userid) {
//        String endValue = "";
//
//        cus_sql = cus_sql.replace("&nbsp;", " ");
//        cus_sql = cus_sql.replace("{?dt.id}", String.valueOf(detailKeyvalue));
//        cus_sql = cus_sql.replaceAll("ｓｅｌｅｃｔ ", " select ");
//        cus_sql = cus_sql.replaceAll(" ｉｎ ", " in ");
//        cus_sql = cus_sql.replaceAll(" ａｎｄ ", " and ");
//        cus_sql = cus_sql.replaceAll(" ｏｒ ", " or ");
//        cus_sql = cus_sql.replaceAll(" ｊｏｉｎ ", " join ");
//        cus_sql = cus_sql.replace("{?userid}", String.valueOf(userid));
//        //参数进行替换
//        String sqlString = cus_sql.replace("{?requestid}", requestid);
//
//        sqlString = sqlString.replace("?", value);
//
//        if (detailKeyvalue > 0) {
//            this.writeLog("执行的转换后的SQL:[" + sqlString + "]");
//        }
//
//        RecordSet rs = new RecordSet();
//
//        if (rs.executeQuery(sqlString)) {
//            rs.next();
//
//            endValue = Util.null2String(rs.getString(1));
//        }
//
//        return endValue;
//    }
//
//
//    /**
//     * 用数据库值，根据规则转换，获取其最终结果
//     *
//     * @param cus_sql 自定义转换的SQL
//     * @param value   参数值
//     * @param
//     * @return
//     */
//    public String getValueByChangeRule_SingleParam(String cus_sql, String value) {
//        String endValue = "";
//
//        cus_sql = cus_sql.replace("&nbsp;", " ");
//        cus_sql = cus_sql.replaceAll("ｓｅｌｅｃｔ ", " select ");
//        cus_sql = cus_sql.replaceAll(" ｉｎ ", " in ");
//        cus_sql = cus_sql.replaceAll(" ａｎｄ ", " and ");
//        cus_sql = cus_sql.replaceAll(" ｏｒ ", " or ");
//        cus_sql = cus_sql.replaceAll(" ｊｏｉｎ ", " join ");
//        RecordSet rs = new RecordSet();
//
//        if (rs.executeQuery(cus_sql, value)) {
//            rs.next();
//
//            endValue = Util.null2String(rs.getString(1));
//        }
//
//        return endValue;
//    }
//
//    /**
//     * 根据字段ID获取其对应的字段名称
//     *
//     * @param fieldid
//     * @return
//     */
//    public String getFieldNameByFieldid_Single(int fieldid) {
//        if (fieldid > 0) {
//            return getFieldNameByFieldid_Single(String.valueOf(fieldid));
//        } else {
//            return "";
//        }
//    }
//
//    /**
//     * 根据字段ID获取其对应的字段名称
//     *
//     * @param fieldid
//     * @return
//     */
//    public String getFieldNameByFieldid_Single(String fieldid) {
//        String fieldname = "";
//
//        if (!"".equals(fieldid)) {
//
//            if (fieldid.startsWith(",")) {
//                fieldid = fieldid.substring(1);
//            }
//
//            if (fieldid.endsWith(",")) {
//                fieldid = fieldid.substring(0, fieldid.length() - 1);
//            }
//
//            String select_sql = "select fieldname from workflow_billfield where id in (" + fieldid + ")";
//
//            RecordSet rs = new RecordSet();
//
//            if (rs.executeQuery(select_sql)) {
//                while (rs.next()) {
//
//                    fieldname += "," + Util.null2String(rs.getString(1));
//                }
//            }
//        }
//
//        if (fieldname.startsWith(",")) {
//            fieldname = fieldname.substring(1);
//        }
//
//        return fieldname;
//    }
//
//    /**
//     * 输出调试日志
//     *
//     * @param className 类名称
//     * @param logstr    日志信息
//     */
//    public void writeDebuggerLog(String className, String logstr) {
//        if (logLevel >= 0) {
//            writeNewLog(className, logstr);
//        }
//    }
//
//    /**
//     * 输出警告日志
//     *
//     * @param className 类名称
//     * @param logstr    日志信息
//     */
//    public void writeWarningLog(String className, String logstr) {
//        if (logLevel >= 1) {
//            writeNewLog(className, logstr);
//        }
//    }
//
//    /**
//     * 输出错误日志
//     *
//     * @param className 类名称
//     * @param logstr    日志信息
//     */
//    public void writeErrorLog(String className, String logstr) {
//        if (logLevel >= 2) {
//            writeNewLog(className, logstr);
//        }
//    }
//
//
//    /**
//     * 日志输出
//     *
//     * @param logstr
//     */
//    public void writeDebugLog(Object logstr) {
//        if (isDebug) {
//            this.writeLog(logstr.toString());
//        }
//    }
//
//    /**
//     * 日志输出
//     *
//     * @param logstr
//     */
//    public void writeNewDebuggerLog(Object o, Object logstr) {
//        if (isDebug) {
//            writeNewLog(o.toString(), logstr.toString());
//        }
//    }
//
//    /**
//     * 写入同步的日志文件
//     *
//     * @param
//     */
//    public void writeNewLog(String o, String s) {
//        try {
//            String filename = "cus_" + TimeUtil.getCurrentDateString() + "_ecology.log";
//
//
//            String folder = GCONST.getRootPath() + "log" + File.separatorChar + "jee";
//
//            //this.writeDebugLog("folder:[" + folder + "]");
//
//            File f = new File(folder);
//
//            // 创建文件夹
//            if (!f.exists()) {
//                f.mkdirs();
//            }
//
//            f = new File(folder + File.separatorChar + filename);
//
//            if (!f.exists()) {//文件不存在，则直接创建
//                f.createNewFile();
//            }
//
//            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, true)));
//
//            out.write("[" + o.getClass() + "][" + TimeUtil.getCurrentTimeString() + "]:" + s + "\r\n");
//
//            //关闭写入流
//            out.close();
//
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            this.writeDebugLog("创建日志文件存在异常:[" + e.getMessage() + "/" + e.toString() + "]");
//        }
//    }
//
//    /**
//     * @return the isDebug
//     */
//    public boolean isDebug() {
//        return isDebug;
//    }
//
//    /**
//     * @param isDebug the isDebug to set
//     */
//    public void setDebug(boolean isDebug) {
//        this.isDebug = isDebug;
//    }
//}
//
///**
// * 凭证操作日志工具类
// */
//class VoucherLogger {
//
//    //流程类型
//    private String workflowid = "";
//    //流程编号
//    private String requestmark = "";
//    //请求ID
//    private String requestid = "";
//    //操作日期时间
//    private String operatedatetime = "";
//    //操作人
//    private int operator = 0;
//    //操作结果 0-未处理   1-处理成功 2-处理失败
//    private int result = 0;
//    //失败原因
//    private String message = "";
//    //发送的数据
//    private String senddata = "";
//    //接受的数据
//    private String receivedata = "";
//    //对应的配置数据ID
//    private int configid = 0;
//
//
//    public String getWorkflowid() {
//        return workflowid;
//    }
//
//    public void setWorkflowid(String workflowid) {
//        this.workflowid = workflowid;
//    }
//
//    public String getRequestid() {
//        return requestid;
//    }
//
//    public void setRequestid(String requestid) {
//        this.requestid = requestid;
//    }
//
//    public String getOperatedatetime() {
//        return operatedatetime;
//    }
//
//    public void setOperatedatetime(String operatedatetime) {
//        this.operatedatetime = operatedatetime;
//    }
//
//    public int getOperator() {
//        return operator;
//    }
//
//    public void setOperator(int operator) {
//        this.operator = operator;
//    }
//
//    public int getResult() {
//        return result;
//    }
//
//    public void setResult(int result) {
//        this.result = result;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public String getSenddata() {
//        return senddata;
//    }
//
//    public void setSenddata(String senddata) {
//        this.senddata = senddata;
//    }
//
//    public String getReceivedata() {
//        return receivedata;
//    }
//
//    public void setReceivedata(String receivedata) {
//        this.receivedata = receivedata;
//    }
//
//    /**
//     * @return the requestmark
//     */
//    public String getRequestmark() {
//        return requestmark;
//    }
//
//    /**
//     * @param requestmark the requestmark to set
//     */
//    public void setRequestmark(String requestmark) {
//        this.requestmark = requestmark;
//    }
//
//    /**
//     * @return the configid
//     */
//    public int getConfigid() {
//        return configid;
//    }
//
//    /**
//     * @param configid the configid to set
//     */
//    public void setConfigid(int configid) {
//        this.configid = configid;
//    }
//}
//
//
///**
// * 阳光电源
// * SAP凭证工具类
// *
// * @author bleach
// * @date 2020-07-24
// */
//class VoucherUtil extends ToolUtil {
//    //当前类名称
//    private static String className = "VoucherUtil";
//    //凭证日志表名称
//    private static String VoucherLogger_Mode_Table = "uf_sapvoucher_log";
//
//    //凭证日志建模模块ID
//    private int Voucher_Mode_ID = Util.getIntValue(this.getSystemParamValue("VoucherLogger_ModeID"), 0);
//
//    /**
//     * 根据流程类型ID获取其对应的配置
//     *
//     * @param workflowid 流程类型ID
//     * @return 返回配置信息
//     */
//    public Map<String, Object> getconfig(String workflowid, String cusparam) {
//        Map<String, Object> c_map = new HashMap<String, Object>();
//
//        //获取当前流程对应的所有版本流程ID
//        String allwfid = WorkflowVersion.getAllVersionStringByWFIDs(workflowid);
//
//        String select_main = "select * from uf_sap_voucher where wfid in (" + allwfid + ")";
//
//        if (!"".equals(cusparam)) {
//            select_main += " and cusparam = '" + cusparam + "'";
//        }
//
//        this.writeNewDebuggerLog(className, "查询配置主表:[" + select_main + "]");
//
//        RecordSet rs = new RecordSet();
//
//        int mainkeyid = 0;
//        if (rs.executeQuery(select_main)) {
//            if (rs.next()) {
//                mainkeyid = Util.getIntValue(rs.getString("id"), 0);
//
//                String datacondition = Util.null2String(rs.getString("datacondition"));
//                String eventid = Util.null2String(rs.getString("eventid"));
//                String detailIndex = Util.null2String(rs.getString("detailIndex"));
//
//                if (!"".equals(datacondition)) {
//                    datacondition = datacondition.replace("&nbsp;", " ").replace("<br>", " ");
//                }
//
//                int backfieldid = Util.getIntValue(rs.getString("backfield"), 0);
//
//                c_map.put("datacondition", datacondition);
//                c_map.put("backfield", backfieldid);
//                c_map.put("detailIndex", detailIndex);
//                c_map.put("eventid", eventid);
//                c_map.put("mainkeyid", mainkeyid);
//            }
//        }
//
//        this.writeNewDebuggerLog(className, "mainkeyid:[" + mainkeyid + "],[" + (mainkeyid > 0) + "]");
//        if (mainkeyid > 0) {
//
//            //表体字段配置集合
//            List<Map<String, Object>> head_list = new ArrayList<Map<String, Object>>();
//            //普通借方字段配置集合
//            List<Map<String, Object>> com_debit_list = new ArrayList<Map<String, Object>>();
//            //税额借方字段配置集合
//            List<Map<String, Object>> tax_debit_list = new ArrayList<Map<String, Object>>();
//            //普通贷方字段配置集合
//            List<Map<String, Object>> com_loan_list = new ArrayList<Map<String, Object>>();
//            //冲销贷方字段配置集合
//            List<Map<String, Object>> cx_loan_list = new ArrayList<Map<String, Object>>();
//            //中转借方字段配置集合
//            List<Map<String, Object>> zz_debit_list = new ArrayList<Map<String, Object>>();
//            //中转贷方字段配置集合
//            List<Map<String, Object>> zz_loan_list = new ArrayList<Map<String, Object>>();
//
//
//            //for(int k = 1; k <= 2 ;k++){
//            //查询明细数据
//            String select_detail = "select dt.*,wb.fieldname,wb.viewtype,wb.detailtable from uf_sap_voucher_dt1 dt " +
//                    "left join workflow_billfield wb on wb.id = dt.oafieldid where dt.mainid = ?";
//
//            this.writeNewDebuggerLog(className, "查询明细:[" + select_detail + "],[" + mainkeyid + "]");
//
//            if (rs.executeQuery(select_detail, mainkeyid)) {
//                while (rs.next()) {
//                    //JSON节点名称
//                    String jsonname = Util.null2String(rs.getString("jsonname"));
//                    //OA流程字段
//                    String fieldname = Util.null2String(rs.getString("fieldname"));
//                    //流程字段所属
//                    int viewtype = Util.getIntValue(rs.getString("viewtype"), 0);
//                    //明细字段表名
//                    String detailtable = Util.null2String(rs.getString("detailtable"));
//                    //转换规则
//                    int changerule = Util.getIntValue(rs.getString("changerule"), 0);
//                    //自定义规则
//                    String cussql = Util.null2String(rs.getString("cussql"));
//
//
//                    Map<String, Object> detailmap = new HashMap<String, Object>();
//                    detailmap.put("jsonname", jsonname);
//                    detailmap.put("fieldname", fieldname);
//                    detailmap.put("viewtype", viewtype);
//                    detailmap.put("detailtable", detailtable);
//                    detailmap.put("changerule", changerule);
//                    detailmap.put("cussql", cussql);
//
//                    int direction = Util.getIntValue(rs.getString("direction"), 0);
//                    detailmap.put("direction", direction);
//
//                    //特殊属性
//                    int specialAttr = Util.getIntValue(rs.getString("specialattr"), 0);
//                    detailmap.put("specialAttr", specialAttr);
//
//                    switch (direction) {
//                        case 1://普通借方
//                            com_debit_list.add(detailmap);
//                            break;
//                        case 2://税额借方
//                            tax_debit_list.add(detailmap);
//                            break;
//                        case 3://普通贷方
//                            com_loan_list.add(detailmap);
//                            break;
//                        case 4://所有借方
//                            com_debit_list.add(detailmap);
//                            tax_debit_list.add(detailmap);
//                            zz_debit_list.add(detailmap);
//                            break;
//                        case 5://所有借贷方
//                            com_debit_list.add(detailmap);
//                            tax_debit_list.add(detailmap);
//                            com_loan_list.add(detailmap);
//                            cx_loan_list.add(detailmap);
//                            zz_loan_list.add(detailmap);
//                            zz_debit_list.add(detailmap);
//                            break;
//                        case 6://冲销贷方
//                            cx_loan_list.add(detailmap);
//                            break;
//                        case 7://所有贷方
//                            cx_loan_list.add(detailmap);
//                            com_loan_list.add(detailmap);
//                            zz_loan_list.add(detailmap);
//                            break;
//                        case 8://中转借方
//                            zz_debit_list.add(detailmap);
//                            break;
//                        case 9://中转贷方
//                            zz_loan_list.add(detailmap);
//                            break;
//                        case 0://表头
//                            head_list.add(detailmap);
//                            break;
//                        default:
//                            head_list.add(detailmap);
//                            break;
//                    }
//                }
//            }
//            //  }
//
//            c_map.put("head_list", head_list);
//            c_map.put("com_debit_list", com_debit_list);
//            c_map.put("tax_debit_list", tax_debit_list);
//            c_map.put("com_loan_list", com_loan_list);
//            c_map.put("cx_loan_list", cx_loan_list);
//            c_map.put("zz_debit_list", zz_debit_list);
//            c_map.put("zz_loan_list", zz_loan_list);
//
//        } else {
//            return null;
//        }
//
//        return c_map;
//    }
//
//    /**
//     * 将凭证调用日志写入建模表中
//     *
//     * @param logger
//     */
//    public void writeinterfacelog(VoucherLogger logger) {
//
//        ModeDataIdUpdate mdu = ModeDataIdUpdate.getInstance();
//        int dataid = mdu.getModeDataNewId(VoucherLogger_Mode_Table, Voucher_Mode_ID, 1, 1, TimeUtil.getCurrentDateString(), TimeUtil.getOnlyCurrentTimeString());
//
//        if (dataid > 0) {//说明获取数据ID成功
//
//            String senddata = logger.getSenddata();
//
//            String prettysend = "";
//
//            if (!"".equals(senddata)) {//美化发送格式
//                JSONObject object = JSONObject.parseObject(senddata);
//                prettysend = JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
//                prettysend = prettysend.replace("'", "''").replace("?", "");
//            }
//
//            String receivedata = logger.getReceivedata();
//            String prettyreceive = "";
//            if (!"".equals(receivedata)) {
//                JSONObject object = JSONObject.parseObject(receivedata);
//                prettyreceive = JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
//                prettyreceive = prettyreceive.replace("'", "''").replace("?", "");
//            }
//
//
//            String update_sql = "update " + VoucherLogger_Mode_Table + " set wfid = '" + logger.getWorkflowid() + "',relate_reqid = '" + logger.getRequestid() + "'," +
//                    "operatedatetime = '" + logger.getOperatedatetime() + "',result = '" + logger.getResult() + "',wfcode = '" + logger.getRequestmark() + "'," +
//                    "senddata = '" + prettysend + "',receivedata = '" + prettyreceive + "',configid = '" + logger.getConfigid() + "'," +
//                    "message = '" + logger.getMessage().replace("'", "''").replace("?", "") + "' where id = ?";
//
//            // this.writeDebuggerLog(className,"更新日志操作SQL:[" + update_sql + "]！");
//
//            RecordSet rs = new RecordSet();
//
//            boolean issuccess = rs.executeUpdate(update_sql, dataid);
//
//            if (issuccess) {
//                //数据权限重构
//                ModeRightInfo mri = new ModeRightInfo();
//                mri.rebuildModeDataShareByEdit(1, Voucher_Mode_ID, dataid);
//            }
//        }
//
//
//    }
//
//    /**
//     * 根据字段配置生成JSON对象
//     *
//     * @param baseArray 流程基础信息
//     * @param datalist  字段详细配置集合
//     * @param rs        主表数据集
//     * @param rs_detail 明细表某行数据集
//     * @param jsonType  JSON字段类型 0-表头  1-表体
//     * @param dtTable   来源明细表名称
//     * @param index     明细序列
//     * @return
//     */
//    JSONObject genarate_json(String[] baseArray, List<Map<String, Object>> datalist, RecordSet rs, RecordSet rs_detail, int jsonType, String dtTable, int index) {
//        JSONObject jsonObject = new JSONObject();
//
//        if (datalist != null && datalist.size() > 0) {
//            for (Map<String, Object> detailmap : datalist) {
//                //JSON节点名称
//                String jsonname = Util.null2String(detailmap.get("jsonname"));
//                //OA字段名称
//                String fieldname = Util.null2String(detailmap.get("fieldname"));
//                //OA字段所在表
//                int viewtype = (Integer) detailmap.get("viewtype");
//                //OA字段所在表名称
//                String detailtable = Util.null2String(detailmap.get("detailtable"));
//                //转换规则
//                int changerule = (Integer) detailmap.get("changerule");
//                //自定义规则
//                String cussql = Util.null2String(detailmap.get("cussql"));
//
//                if (jsonType == 0 && viewtype > 0) {//JSON配置为表头，但配置字段为OA明细表，则不符合规则直接跳过
//                    continue;
//                }
//
//                if (jsonType == 1) {//表体配置
//                    if (viewtype == 1 && !detailtable.equalsIgnoreCase(dtTable)) {//OA字段为明细表字段，且所在的明细表名与当前数据集明细表不一致，则直接跳过
//                        continue;
//                    }
//                }
//
//                //字段值
//                String fieldvalue = "";
//                if (!"".equals(fieldname)) {
//                    if (viewtype == 1 && rs_detail != null) {
//                        fieldvalue = Util.null2String(rs_detail.getString(fieldname));
//                    } else {
//                        fieldvalue = Util.null2String(rs.getString(fieldname));
//                    }
//                }
//
//
//                switch (changerule) {
//                    case 1://流程请求ID
//                        fieldvalue = baseArray[0];
//                        break;
//                    case 2://流程标题
//                        fieldvalue = baseArray[1];
//                        break;
//                    case 3://流程编号
//                        fieldvalue = baseArray[2];
//                        break;
//                    case 4://当前日期
//                        fieldvalue = TimeUtil.getCurrentDateString();
//                        break;
//                    case 5://当前日期时间
//                        fieldvalue = TimeUtil.getOnlyCurrentTimeString();
//                        break;
//                    case 6://固定值
//                        fieldvalue = cussql;
//                        break;
//                    case 7://明细序列
//                        fieldvalue = String.valueOf(index);
//                        break;
//                    case 8://自定义规则
//                        int detailkeyid = 0;
//                        if (rs_detail != null && cussql.indexOf("{?dt.id}") > -1) {
//                            detailkeyid = Util.getIntValue(rs_detail.getString("id"), 0);
//                        }
//
//                        fieldvalue = getValueByChangeRule(cussql, fieldvalue, baseArray[0], detailkeyid, baseArray[3]);
//                        break;
//                    default://不转换
//                        break;
//                }
//
//
//                if (detailmap.containsKey("specialAttr")) {
//                    //特殊属性
//                    int specialAttr = (Integer) detailmap.get("specialAttr");
//
//                    if (specialAttr == 1) {//若为金额字段
//
//                        double amount = Util.getDoubleValue(fieldvalue, 0.0);
//                        if (amount == 0.0) {
//                            return null;
//                        }
//                    }
//                }
//
//                if (jsonname.indexOf("T_ITEM.") > -1) {
//                    //明细字段去除明细标识
//                    jsonname = jsonname.substring(7, jsonname.length());
//                }
//
//
//                jsonObject.put(jsonname, fieldvalue);
//            }
//        }
//
//        return jsonObject;
//    }
//
//    /**
//     * 发送
//     *
//     * @param
//     * @param
//     * @return
//     */
//    public JSONObject sendPost_New(String eventid, JSONObject jsonobj) {
//        try {
//
//            if ("".equals(eventid)) {
//                eventid = "IF007";
//            }
//            String eventkey = eventid;// "PZSCTS"; //事件标识
//            String params = jsonobj.toString();// "{"body":"{\"Model\":{\"FAccountBookID\":\"?\",\"FDate\":\"?\",\"FVOUCHERGROUPID\":\"?\",\"F_ora_Text2\":\"?\",\"FEntity\":[{\"FEntryID\":\"?\",\"FEXPLANATION\":\"?\",\"FACCOUNTID\":\"?\",\"FDetailID\":{\"FFLEX9\":\"?\",\"FFLEX10\":\"?\",\"FFLEX11\":\"?\",\"FFLEX12\":\"?\",\"FFLEX4\":\"?\",\"FFLEX13\":\"?\",\"FFLEX5\":\"?\",\"FF100004\":\"?\",\"FFLEX6\":\"?\",\"FF100005\":\"?\",\"FFLEX7\":\"?\",\"FF100006\":\"?\",\"FFLEX8\":\"?\",\"FF100007\":\"?\",\"FF100009\":\"?\",\"FF100010\":\"?\"},\"FCURRENCYID\":\"?\",\"FEXCHANGERATETYPE\":\"?\",\"FEXCHANGERATE\":\"?\",\"FAMOUNTFOR\":\"?\",\"FDEBIT\":\"?\",\"FCREDIT\":\"?\",\"FDC\":\"?\",\"FSettleTypeID\":\"?\",\"FAmount\":\"?\",\"FSETTLENO\":\"?\",\"FACCOUNTNAME\":\"?\",\"FCASHFLOWITEM\":\"?\",\"FISMULTICOLLECT\":\"?\",\"FAcctFullName\":\"?\",\"FQty\":\"?\",\"FPrice\":\"?\",\"FUnitId\":\"?\",\"FAcctUnitQty\":\"?\",\"FBaseUnitQty\":\"?\",\"FOldEntryId\":\"?\",\"FEXPORTENTRYID\":\"?\",\"FBUSNO\":\"?\",\"FDETAILIDCOMBINATION\":\"?\",\"F_ora_Date\":\"?\",\"F_ora_Text\":\"?\"}],\"szqy\":\"?\"}}","xglc":"?"}"; //事件请求参数
//
////EsbService其他方法及说明见ESB API接口说明文档
//            EsbService service = EsbClient.getService(); //获取 ESB 服务
//            String result = service.execute(eventkey, params);
//
//            this.writeDebuggerLog(className, "result:[" + result + "]");
//
//            if (!"".equals(result)) {
//                JSONObject resultobj = JSONObject.parseObject(result.toString());
//                return resultobj;
//            } else {
//                return null;
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            this.writeDebuggerLog(className, "POST异常1:[" + ex.getMessage() + "/" + ex.toString() + "]");
//        }
//        return null;
//
//    }
//}
