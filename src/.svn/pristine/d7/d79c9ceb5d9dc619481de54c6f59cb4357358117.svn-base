package com.engine.qysCallBack.web;

import com.alibaba.fastjson.JSONObject;
import com.engine.common.util.ParamUtil;
import com.engine.common.util.ServiceUtil;
import com.engine.integration.service.TodoCenterService;
import com.engine.integration.service.impl.TodoCenterServiceImpl;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.workflow.qiyuesuo.QYSInterface;
import weaver.workflow.qiyuesuo.bean.QYSResponse;
import weaver.workflow.qiyuesuo.companyAuth.QYSCompanyAuthInterface;
import weaver.workflow.qiyuesuo.constant.QYSCategoryType;
import weaver.workflow.qiyuesuo.constant.QYSConstant;
import weaver.workflow.qiyuesuo.sealApply.QYSSealApplyInterface;
import weaver.workflow.qiyuesuo.util.QYSUtil;
import weaver.workflow.qiyuesuo.util.QYSWorkflowUtil;
import weaver.integration.logging.Log4JLogger;
import weaver.integration.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: WEAVER
 * @BelongsPackage: com.engine.qysCallBack
 * @Author: luotianchen
 * @CreateTime: 2024-01-26  10:45
 * @Description: TODO
 * @Version: 1.0
 */

public class qysCallBackAction {
    @POST
    @Path("/doSubmitCallBack")
    public String doSubmitCallBack(@Context HttpServletRequest req, @Context HttpServletResponse res) throws ServletException, IOException {
        Log4JLogger var2 = new Log4JLogger();
        var2.setClassname(qysCallBackAction.class.getCanonicalName());
        var2.init("QYSCallBack");
        Logger log = var2;
        log.info("自定义契约锁回调doPost--start");

        final QYSResponse response = new QYSResponse();

        String characterEncoding = Util.null2String(req.getCharacterEncoding());
        String contentType = Util.null2String(req.getHeader("Content-Type")).toLowerCase();
        log.info("doPost--characterEncoding:" + characterEncoding + "--contentType:" + contentType);

        final Map<String, Object> params = QYSUtil.request2Map(req); // 默认 application/x-www-form-urlencoded
        log.info("请求报文："+params);
        if (contentType.startsWith("multipart/")) { // multipart/form-data
            params.putAll(new QYSUtil().requestMultipart2Map(req));
        } else if (contentType.startsWith("application/json")) { // application/json
            params.putAll(new QYSUtil().requestJSON2Map(req));
        }

        final String callbackEventType = Util.null2String(params.get("callbackEventType")); // 契约锁集成回调标识
        final String ecologyOperate = Util.null2String(params.get("ecologyOperate")); // 契约锁集成回调操作
        log.info("doPost--callbackEventType:" + callbackEventType + "--ecologyOperate:" + ecologyOperate);

        final QYSCategoryType categoryType = QYSConstant.CALLBACK_EVENT_TYPE_PHYSICS.equalsIgnoreCase(callbackEventType) ? QYSCategoryType.PHYSICS : QYSConstant.CALLBACK_EVENT_TYPE_COMPANY_AUTH.equalsIgnoreCase(callbackEventType) ? QYSCategoryType.COMPANYAUTH : QYSCategoryType.ELECTRONIC;
        log.info("dopost--categoryType:" + categoryType.getType());
        String operatorMobile = Util.null2String(params.get("operatorMobile"));
        final int operatorUserId = QYSUtil.getUserIdByMobile(operatorMobile);
        log.info("doPost--operatorUserId:" + operatorUserId + "--operatorMobile:" + operatorMobile);
        final int bizId = Util.getIntValue(Util.null2String(params.get("bizId")), 0);
        log.info("doPost--bizId:" + bizId);

        RecordSet rs = new RecordSet();
        String SynReqMenu = "";
        //可能要改，这里在表里查是否异步，目前是否
        rs.executeQuery("select   SynReqMenu  from " + QYSConstant.TABLE_GLOBAL_CONFIG);
        while (rs.next()) {
            SynReqMenu = Util.null2String(rs.getString("SynReqMenu"));
            log.info("doPost--SynReqMenu:" + SynReqMenu);
        }
        if ("1".equals(SynReqMenu)) {
            response.setCodeAndMessage(0, "");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    callback(ecologyOperate, req, callbackEventType, params, operatorUserId, response, categoryType,
                            bizId, res, operatorMobile);
                }
            }).start();
        } else {
            QYSResponse newResponse = callback(ecologyOperate, req, callbackEventType, params, operatorUserId,
                    response, categoryType, bizId, res, operatorMobile);
            response.setCode(newResponse.getCode());
            response.setMessage(newResponse.getMessage());
        }

        log.info("doPost--end--" + response.toJSONString());
        //修改没有传递ecologyOperate时，回调直接响应成功
        if (0 == response.getCode() || ecologyOperate.isEmpty()) {
            res.setStatus(200);
        } else {
            ServletOutputStream outputStream = res.getOutputStream();
            res.setHeader("Content-type", "text/html;charset=UTF-8");
            res.setContentType("text/html; chartset=UTF-8");
            res.setStatus(500);
            outputStream.write(response.getMessage().getBytes("UTF-8"));
            //res.sendError(500, response.getMessage());
        }
        return response.toJSONString();
    }


    public QYSResponse callback(String ecologyOperate, HttpServletRequest req, String callbackEventType, Map<String,
            Object> params, int operatorUserId, QYSResponse response, QYSCategoryType categoryType, int bizId,
                                HttpServletResponse res, String operatorMobile) {
        Log4JLogger var2 = new Log4JLogger();
        var2.setClassname(qysCallBackAction.class.getCanonicalName());
        var2.init("QYSCallBack");
        Logger log = var2;

        if (!ecologyOperate.isEmpty()) {
            QYSInterface QYSInterface = null;
            QYSSealApplyInterface QYSSealApplyInterface = null;
            QYSCompanyAuthInterface QYSCompanyAuthInterface;

            User user = new User(1);
            String loingIp = "";
            try {
                loingIp = Util.null2String(req.getRemoteAddr());
            } catch (Exception e) {
                e.printStackTrace();
            }
            user.setLoginip(loingIp);

            int requestid = 0;
            String flowid = "";
            if (QYSConstant.CALLBACK_EVENT_TYPE_ELECTRONIC.equalsIgnoreCase(callbackEventType)) {// 默认电子签章
                String contractId = Util.null2String(params.get("contractId")).trim();
                log.info("doPost--contractId:" + contractId);

                if (!contractId.isEmpty()) {
                    RecordSet rs = new RecordSet();
                    //mysql写法
//                    rs.executeQuery("SELECT flowid FROM ofs_todo_data WHERE SUBSTRING_INDEX(pcurl, '/', -1) = ?",contractId);
//oracle写法
                    rs.executeQuery("select flowid from " +
                            "ofs_todo_data where SUBSTR(pcurl, INSTR(pcurl, '/', -1) + 1)=?", contractId);
                    if (rs.next()) {
                        flowid = rs.getString("flowid");
                    }
//                    QYSInterface = new QYSInterface(user, QYSLogType.CALLBACK);
//                    requestid = QYSInterface.getRequestidbyContractId(Long.valueOf(contractId));
                }
            } else {
                //
            }

            //没有requestid，换个标识符
            log.info("doPost--requestid:" + requestid);
            if (requestid <= 0 && bizId > 0) {
                requestid = bizId;
            }
            if (!"".equals(flowid)) {
                if (QYSConstant.CALLBACK_OPERATE_SUBMIT.equalsIgnoreCase(ecologyOperate) || QYSConstant.CALLBACK_OPERATE_REJECT.equalsIgnoreCase(ecologyOperate)) {
                    String remark = Util.null2String(params.get("ecologyRemark"));
                    String signedByEc = Util.null2String(params.get("signedByEc"));
                    log.info("doPost--remark:" + remark + "--signedByEc:" + signedByEc);
                    if (signedByEc.equals("1") && QYSConstant.CALLBACK_OPERATE_SUBMIT.equalsIgnoreCase(ecologyOperate)) {//从OA这边完成合同签署的时候，回调提交不执行
                        response.setCodeAndMessage(0, "");
                    } else {
                        //这里加上统一待办改已办逻辑
                        String result = "";
                        RecordSet rs = new RecordSet();
                        String sql = "select * from ofs_todo_data where flowid=?";
                        rs.executeQuery(sql, flowid);
                        int userid = 0;
                        while (rs.next()) {
                            Map<String, Object> paramMap = new HashMap<>();
                            userid = rs.getInt("userid");
                            paramMap.put("sysid", rs.getString("sysid"));
                            paramMap.put("syscode", rs.getString("syscode"));
                            paramMap.put("workflowname", rs.getString("workflowname"));
                            paramMap.put("flowid", rs.getString("flowid"));
                            paramMap.put("requestname", rs.getString("requestname"));
                            paramMap.put("isremark", "2");
                            paramMap.put("viewtype", rs.getString("viewtype"));
                            paramMap.put("nodename", rs.getString("nodename"));
                            paramMap.put("pcurl", rs.getString("pcurl"));
                            paramMap.put("appurl", rs.getString("appurl"));
                            paramMap.put("creatorid", rs.getString("creatorid"));
                            paramMap.put("creator", rs.getString("creator"));
                            paramMap.put("createdate", rs.getString("createdate"));
                            paramMap.put("createtime", rs.getString("createtime"));
                            paramMap.put("userid", rs.getString("userid"));
                            paramMap.put("receiver", rs.getString("receiver"));
                            paramMap.put("receivedate", rs.getString("receivedate"));
                            paramMap.put("receivetime", rs.getString("receivetime"));
                            paramMap.put("id", rs.getString("id"));
                            paramMap.put("operation", rs.getString("operation"));
                            paramMap.put("isauto", rs.getString("isauto"));
                            paramMap.put("createdatetime", rs.getString("createdatetime"));
                            paramMap.put("receivedatetime", rs.getString("receivedatetime"));


                            HashMap resultmap = new HashMap();

                            try {
                                User user1 = new User(userid);
                                Map sendResStr = this.getService(user1).ofsDataOperation(paramMap, user);
                                resultmap.putAll(sendResStr);
                                resultmap.put("status", "1");
                                log.info(resultmap);
//                            Map var5 = ParamUtil.request2Map(req);
                            } catch (Exception var7) {
                                resultmap.put("status", "-1");
                            }

                            if("SUCCESS".equals(result)){
                                if ("-1".equals(resultmap.get("status"))) {
                                    result = "failed";
                                }
                            }
                        }

                        if ("SUCCESS".equalsIgnoreCase(result)) {
                            response.setCodeAndMessage(0, "SUCCESS");
                        } else if ("ERRORUSER".equalsIgnoreCase(result)) {
                            response.setMessage("该签署人不是当前节点的操作者，不能提交或退回流程");
                        } else if ("failed".equals(result)) {
                        } else {
                            response.setMessage(result);
                        }
                    }
                } else {
                    response.setCodeAndMessage(0, "");
                }
            } else {
                response.setCodeAndMessage(0, "");
            }
        }
        return response;
    }

    private TodoCenterService getService(User var1) {
        return (TodoCenterService) ServiceUtil.getService(TodoCenterServiceImpl.class, var1);
    }
}
