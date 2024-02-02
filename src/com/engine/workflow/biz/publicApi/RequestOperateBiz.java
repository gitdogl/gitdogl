package com.engine.workflow.biz.publicApi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.doc.detail.service.DocAccService;
import com.api.doc.detail.service.DocSaveService;
import com.api.workflow.constant.RequestAuthenticationConstant;
import com.api.workflow.constant.RequestAuthenticationEnum;
import com.api.workflow.service.RequestAuthenticationService;
import com.engine.common.util.ServiceUtil;
import com.engine.core.exception.ECException;
import com.engine.hrm.biz.HrmClassifiedProtectionBiz;
import com.engine.workflow.biz.WorkflowBaseBiz;
import com.engine.workflow.biz.freeNode.FreeNodeBiz;
import com.engine.workflow.biz.requestFlow.DetailDataModifyTrackBiz;
import com.engine.workflow.biz.requestFlow.MainDataModifyTrackBiz;
import com.engine.workflow.biz.requestFlow.SaveDetailFormDatasBiz;
import com.engine.workflow.biz.requestFlow.SaveMainFormDatasBiz;
import com.engine.workflow.biz.requestForm.*;
import com.engine.workflow.biz.requestSubmit.RequestOperationBiz;
import com.engine.workflow.biz.workflowCore.RequestBaseBiz;
import com.engine.workflow.biz.workflowCore.RequestFlowBiz;
import com.engine.workflow.constant.PAResponseCode;
import com.engine.workflow.constant.ReqFlowFailMsgType;
import com.engine.workflow.constant.RequestLogType;
import com.engine.workflow.constant.SelectNextFlowMode;
import com.engine.workflow.entity.core.*;
import com.engine.workflow.entity.publicApi.PAResponseEntity;
import com.engine.workflow.entity.publicApi.ReqOperateRequestEntity;
import com.engine.workflow.entity.publicApi.WorkflowDetailTableInfoEntity;
import com.engine.workflow.entity.requestForm.FieldInfo;
import com.engine.workflow.entity.requestForm.ReqFlowFailMsgEntity;
import com.engine.workflow.entity.requestForm.TableInfo;
import com.engine.workflow.service.RequestFlowService;
import com.engine.workflow.service.impl.RequestFlowServiceImpl;
import com.engine.workflow.service.impl.RequestManagerServiceImpl;
import com.engine.workflow.util.ChuanyueUtil;
import com.engine.workflow.util.CommonUtil;
import com.engine.workflow.util.HttpServletRequestUtil;
import com.engine.workflow.util.WfDataCorrectionUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import weaver.conn.RecordSet;
import weaver.cpt.util.CptWfUtil;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.hrm.resource.ResourceComInfo;
import weaver.share.ShareManager;
import weaver.soa.workflow.FileProcessor;
import weaver.soa.workflow.bill.BillBgOperation;
import weaver.workflow.logging.Logger;
import weaver.workflow.logging.LoggerFactory;
import weaver.workflow.request.*;
import weaver.workflow.webservices.WorkflowRequestTableField;
import weaver.workflow.webservices.WorkflowRequestTableRecord;
import weaver.workflow.workflow.WorkflowBillComInfo;
import weaver.workflow.workflow.WorkflowVersion;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class RequestOperateBiz {

    private final static Logger log = LoggerFactory.getLogger(RequestOperateBiz.class);

    //意见
    public final static String OTHERPARAM_NEEDBACK = "needback";
    //提交指定节点
    public final static String OTHERPARAM_SUBMITTONODEID = "SubmitToNodeid";
    //退回
    public final static String OTHERPARAM_REJECTTONODEID = "RejectToNodeid";
    public final static String OTHERPARAM_REJECTTYPE = "RejectToType";
    public final static String OTHERPARAM_SUBMITDIRECT = "isSubmitDirect";

    //校验表单必填
    public final static String OTHERPARAM_JUDGEFORMMUSTINPUT = "judgeFormMustInput";

    /**
     * 基本信息验证、权限校验
     *
     * @param user
     * @param requestid
     * @param otherParams
     * @return
     */
    public static PAResponseEntity verifyBefore(User user, int requestid, Map <String, Object> otherParams) {
        PAResponseEntity pAEntity = new PAResponseEntity();
        if (user == null) {
            pAEntity.setCode(PAResponseCode.USER_EXCEPTION);
            return pAEntity;
        }
        if (requestid <= 0) {
            pAEntity.setCode(PAResponseCode.PARAM_ERROR);
            pAEntity.getErrMsg().put("errorParam_requestid", requestid);
            return pAEntity;
        }
        Map <String, String> authParams = new HashMap <>();
        Iterator <String> it = otherParams.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            authParams.put(key, Util.null2String(otherParams.get(key)));
        }

        //判断权限
        RequestAuthenticationService authService = new RequestAuthenticationService();
        authService.setUser(user);
        if (!authService.verify(authParams, requestid)) {
            pAEntity.setCode(PAResponseCode.NO_PERMISSION);
            return pAEntity;
        }

        //流程密级
        boolean isOpenSec = HrmClassifiedProtectionBiz.isOpenClassification();
        if (HrmClassifiedProtectionBiz.isOpenClassification()) {
            if (isOpenSec && !new RequestSecLevelBiz().hasWfRight(requestid, user, otherParams)) {
                pAEntity.setCode(PAResponseCode.NO_PERMISSION);
                pAEntity.getErrMsg().put("right_secLevel", "" + weaver.systeminfo.SystemEnv.getHtmlLabelName(10005565, weaver.general.ThreadVarLanguage.getLang()) + "");
                return pAEntity;
            }
        }

        Map <String, Object> authInfo = authService.getAuthInfo();
        RequestAuthenticationEnum authEnum = (RequestAuthenticationEnum) authInfo.get(RequestAuthenticationConstant.KEY_AUTHORITYTYPE);
        if ("1".equals(Util.null2String(otherParams.get("isintervenor"))) && authEnum != RequestAuthenticationEnum.INTERVENOR) {
            pAEntity.setCode(PAResponseCode.NO_PERMISSION);
            pAEntity.getErrMsg().put("msgInfo", "" + weaver.systeminfo.SystemEnv.getHtmlLabelName(24533, weaver.general.ThreadVarLanguage.getLang()) + "" + user.getLastname() + "" + weaver.systeminfo.SystemEnv.getHtmlLabelName(10005566, weaver.general.ThreadVarLanguage.getLang()) + "");
            pAEntity.getErrMsg().put("otherParam", JSONObject.toJSONString(otherParams));
            return pAEntity;
        }
        return pAEntity;
    }

    /**
     * 新建流程校验必填参数是否有值
     *
     * @return
     */
    public static PAResponseEntity newReqVerifyParam(ReqOperateRequestEntity requestParam, User user) {
        PAResponseEntity pAEntity = new PAResponseEntity();
        if (user == null) {
            pAEntity.setCode(PAResponseCode.USER_EXCEPTION);
            return pAEntity;
        }
        if (requestParam.getWorkflowId() <= 0) {
            pAEntity.getErrMsg().put("workflowId", requestParam.getWorkflowId());
        }
        if ("".equals(requestParam.getRequestName())) {
            pAEntity.getErrMsg().put("requestName", requestParam.getRequestName());
        }

        if (pAEntity.getErrMsg().size() > 0) {
            pAEntity.setCode(PAResponseCode.PARAM_ERROR);
            return pAEntity;
        }
        int workflowid = Util.getIntValue(WorkflowVersion.getActiveVersionWFID(String.valueOf(requestParam.getWorkflowId())));
        RecordSet rs = new RecordSet();
        rs.executeQuery("select islockworkflow,isvalid from workflow_base where id = ?", workflowid);
        if (rs.next()) {
            String islockworkflow = rs.getString("islockworkflow");
            String isvalid = rs.getString("isvalid");

            if ("0".equals(isvalid) || "2".equals(isvalid) || "1".equals(islockworkflow)) {
                pAEntity.getErrMsg().put("msg", "" + weaver.systeminfo.SystemEnv.getHtmlLabelName(33569, weaver.general.ThreadVarLanguage.getLang()) + "" + workflowid + "" + weaver.systeminfo.SystemEnv.getHtmlLabelName(10005567, weaver.general.ThreadVarLanguage.getLang()) + "");
                pAEntity.setCode(PAResponseCode.NO_PERMISSION);
                return pAEntity;
            }
        } else {
            pAEntity.getErrMsg().put("msg", "" + weaver.systeminfo.SystemEnv.getHtmlLabelName(33569, weaver.general.ThreadVarLanguage.getLang()) + "" + workflowid + "" + weaver.systeminfo.SystemEnv.getHtmlLabelName(10003760, weaver.general.ThreadVarLanguage.getLang()) + "");
            pAEntity.setCode(PAResponseCode.PARAM_ERROR);
            return pAEntity;
        }
        requestParam.setWorkflowId(workflowid);
        //判断是否有创建流程权限
        int isVerifyPer = Util.getIntValue(Util.null2String(requestParam.getOtherParams().get("isVerifyPer")), 1);
        if (isVerifyPer != 0) {
            if (!new ShareManager().hasWfCreatePermission(user, requestParam.getWorkflowId())) {
                pAEntity.setCode(PAResponseCode.NO_PERMISSION);
                pAEntity.getErrMsg().put("msg", "" + weaver.systeminfo.SystemEnv.getHtmlLabelName(24533, weaver.general.ThreadVarLanguage.getLang()) + "" + user.getLastname() + "" + weaver.systeminfo.SystemEnv.getHtmlLabelName(10005568, weaver.general.ThreadVarLanguage.getLang()) + "");
            }
        }
        return pAEntity;
    }

    /**
     * 初始化RequestInfo
     *
     * @param user
     * @return
     */
    public static RequestInfoEntity initRequestInfo(ReqOperateRequestEntity requestParam, User user) {
        RequestInfoEntity reqEntity = RequestBaseBiz.loadRequestInfo(requestParam.getRequestId());
        RecordSet rs = new RecordSet();
        WFLinkInfo wfLinkInfo = new WFLinkInfo();
        //流程基本信息
        int workflowId = requestParam.getRequestId() < 0 ? requestParam.getWorkflowId() : reqEntity.getWorkflowId();
        reqEntity.setWorkflowId(workflowId);
        if (requestParam.getRequestName()!=null&&!"".equals(requestParam.getRequestName())) reqEntity.setRequestName(requestParam.getRequestName());
        String requestLevel = Util.null2s(requestParam.getRequestLevel(), "0");
        reqEntity.setRequestLevel(requestLevel);

        WorkflowBaseInfoEntity baseInfoEntity = WorkflowBaseBiz.initBaseInfo(String.valueOf(workflowId));
        reqEntity.setWorkflowBaseInfo(baseInfoEntity);

        reqEntity.setLanguageid(user.getLanguage());
        //表单信息
        String billTableName = "";
        MainTableInfoEntity mainTableInfoEntity = new MainTableInfoEntity();
        int isBill = Util.getIntValue(baseInfoEntity.getIsBill());
        if (isBill == 1) {
            WorkflowBillComInfo billComInfo = new WorkflowBillComInfo();
            billTableName = billComInfo.getTablename(baseInfoEntity.getFormId());
        } else {
            billTableName = "workflow_form";
        }
        mainTableInfoEntity.setBillid(Util.getIntValue(baseInfoEntity.getFormId()));
        mainTableInfoEntity.setTableDbName(billTableName);
        reqEntity.setMainTableInfoEntity(mainTableInfoEntity);
        String submitNodeid = Util.null2String(requestParam.getOtherParams().get("submitNodeId"));
        String nodetype="";
        int nodeid = -1;
        if (requestParam.getRequestId() > 0) {
            String sql = com.api.workflow.util.ServiceUtil.calculateCurrentNodeSql(String.valueOf(requestParam.getRequestId()), user.getUID(), user.getType());
            if(submitNodeid!=""){
                sql = sql.substring(0,sql.indexOf("order by orderisremark,id"));
                sql +=" and nodeid = "+submitNodeid+" order by orderisremark,id";
            }
            rs.executeQuery(sql);
            while (rs.next()) {//a代理b,a提交到a,a->b,b意见征询出去 使用if会导致取错节点导致a无权限结束征询
                String isremark = Util.null2String(rs.getString("isremark"));
                String tempnodeid = Util.null2String(rs.getString("nodeid"));
                reqEntity.setCurrentNodeId(rs.getString("nodeid"));
                reqEntity.setIsremark(rs.getInt("isremark"));
                reqEntity.setTakisremark(rs.getInt("takisremark"));
                reqEntity.setCurrentOperateId(rs.getInt("id"));
                requestParam.getOtherParams().put("agenttype", rs.getString("agenttype"));
                requestParam.getOtherParams().put("agentorbyagentid", rs.getString("agentorbyagentid"));
                reqEntity.setExtendNodeId(String.valueOf(FreeNodeBiz.getExtendNodeId(rs.getInt("nodeid"))));

                if (isremark.equals("1") || isremark.equals("5") || isremark.equals("7") || isremark.equals("8") || isremark.equals("9") || (isremark.equals("0") && !nodetype.equals("3")) || isremark.equals("11")) {
                    nodeid = Util.getIntValue(tempnodeid);
                    nodetype = wfLinkInfo.getNodeType(nodeid);
                    break;
                }
            }
            reqEntity.setRequestId(String.valueOf(requestParam.getRequestId()));
        }
        return reqEntity;
    }


    /**
     * 流程数据保存
     *
     * @param reqEntity
     * @param user
     * @param requestParam
     * @return
     */
    public static Map <String, Object> saveRequestInfo(RequestInfoEntity reqEntity, User user, ReqOperateRequestEntity requestParam,ReqFlowFailMsgEntity reqFailMsg) throws Exception {
        RecordSet rs = new RecordSet();
        WorkflowBaseInfoEntity baseInfoEntity = reqEntity.getWorkflowBaseInfo();
        MainTableInfoEntity mainTableInfoEntity = reqEntity.getMainTableInfoEntity();
        int isBill = Util.getIntValue(baseInfoEntity.getIsBill());
        int mainDataId = -1;
        if (isBill == 1) {
            if (!"".equals(mainTableInfoEntity.getTableDbName())) {
                rs.executeQuery("select * from " + mainTableInfoEntity.getTableDbName() + " where requestid =  ?", Util.getIntValue(reqEntity.getRequestId()));
                if (rs.next()) {
                    mainDataId = rs.getInt("id");
                }
            }
        }
        mainTableInfoEntity.setBillid(mainDataId);
        Map <String, Object> otherParams = requestParam.getOtherParams();
        Map <String, Object> errMsg = new HashMap <>();
        List <WorkflowRequestTableField> mainData = requestParam.getMainData();
        RequestFlowService flowService = ServiceUtil.getService(RequestFlowServiceImpl.class, user);
        if (!otherParams.containsKey("needModifyLog")) otherParams.put("needModifyLog", false);
        //获取默认附件上传目录
        Map <String, String> fieldUploadDocCategory = getFieldUploadDocCategory(reqEntity.getWorkflowId());

        int workflowid = Util.getIntValue(baseInfoEntity.getWorkflowId());
        int requestid = Util.getIntValue(reqEntity.getRequestId());
        int billid = Util.getIntValue(baseInfoEntity.getFormId());
        int nodeid = Util.getIntValue(reqEntity.getCurrentNodeId());
        boolean isTrack = false;
        rs.executeQuery("select ismodifylog from workflow_base where id=?", workflowid);
        new BaseBean().writeLog("RequestOperateBiz otherParams.needModifyLog:"+otherParams.get("needModifyLog"));
        new BaseBean().writeLog("RequestOperateBiz workflowid:"+workflowid);
        if (rs.next()) {
            isTrack = "1".equals(rs.getString("ismodifylog"));
            otherParams.put("needModifyLog", true);
        }

        if (mainData != null && mainData.size() > 0) {
            SaveMainFormDatasBiz.initMainTableInfo(reqEntity);
            Map <String, FieldInfoEntity> fieldInfoMap = getFieldMap(reqEntity.getMainTableInfoEntity().getFieldInfos());
            List <String> maineditfields = new ArrayList <>();
            Map <String, Object> tempMainDatas = new HashMap <>();
            for (WorkflowRequestTableField field : mainData) {
                Map <String, Object> valueObj = new HashMap <>();
                FieldInfoEntity fieldInfo = fieldInfoMap.get(field.getFieldName());
                if (fieldInfo == null) {
                    errMsg.put("error_param_" + field.getFieldName(), field.getFieldValue());
                    continue;
                }
                String fieldValue = field.getFieldValue();
                fieldValue = uploadFile(fieldInfo, fieldValue, fieldUploadDocCategory, user, errMsg);
                valueObj.put("value", fieldValue);
                tempMainDatas.put(field.getFieldName(), valueObj);
                maineditfields.add(fieldInfo.getFieldId());
            }
            reqEntity.getMainTableInfoEntity().setDatas(tempMainDatas);
            reqEntity.setMaineditfields(maineditfields);

            if (errMsg.size() > 0) {
                reqFailMsg.setMsgType(ReqFlowFailMsgType.SAVE_MAIN_TABLE_FAIL);
                return errMsg;
            }
            try {
                //初始化必要数据
                flowService.saveMainFormDatas(reqEntity, otherParams);
            } catch (Exception e) {
                log.error("save main form failed! requestid:~~~~"+requestParam.getRequestId()+"~~~~"+e);
                reqFailMsg.setMsgType(ReqFlowFailMsgType.SAVE_MAIN_TABLE_FAIL);
                throw new ECException("save main form error !");
            }
        }

        //保存明细表数据
        List <WorkflowDetailTableInfoEntity> detailData = requestParam.getDetailData();

        if (detailData != null && detailData.size() > 0) {
            SaveDetailFormDatasBiz.initDetailTableInfo(reqEntity);
            List <String> detaileditfields = new ArrayList <>();
            for (WorkflowDetailTableInfoEntity detailTableInfo : detailData) {
                DetailTableInfoEntity dtableEntity = getDetailTableInfoEntity(reqEntity, detailTableInfo.getTableDBName());
                if (dtableEntity == null) {
                    errMsg.put("error_param_detailData_tableName", detailTableInfo.getTableDBName());
                    reqFailMsg.setMsgType(ReqFlowFailMsgType.SAVE_DETAIL_TABLE_FAIL);
                    return errMsg;
                }
                String detailTableName = detailTableInfo.getTableDBName();
                String deleteKeys = detailTableInfo.getDeleteKeys();
                String mainTable = detailTableName.substring(0, detailTableName.lastIndexOf("_"));

                if (deleteKeys != null && !deleteKeys.equals("")) {
                    boolean isPer = judgeDetailDelPer(Util.getIntValue(reqEntity.getRequestId()), deleteKeys, detailTableName, mainTable);
                    if (!"1".equals(dtableEntity.getIsDelete()) || !isPer) {
                        reqFailMsg.setMsgType(ReqFlowFailMsgType.SAVE_DETAIL_TABLE_FAIL);
                        errMsg.put("msg_detailData_tableName", "" + weaver.systeminfo.SystemEnv.getHtmlLabelName(130658, weaver.general.ThreadVarLanguage.getLang()) + "" + detailTableInfo.getTableDBName() + "" + weaver.systeminfo.SystemEnv.getHtmlLabelName(10005569, weaver.general.ThreadVarLanguage.getLang()) + "");
                        return errMsg;
                    }
                }

                List <DetailRowInfoEntity> rowInfos = new ArrayList <DetailRowInfoEntity>();
                dtableEntity.setDetailRowInfos(rowInfos);
                dtableEntity.setDeleteIds(Util.null2String(detailTableInfo.getDeleteKeys()));
                Map <String, FieldInfoEntity> fieldInfoMap = getFieldMap(dtableEntity.getFieldInfos());
                WorkflowRequestTableRecord[] detailReocrd = detailTableInfo.getWorkflowRequestTableRecords();

                //计算该明细表，新增明细字段，更新明细字段数据，更新行数
                List <String> insertFields = Lists.newArrayList();
                List <String> updateFields = Lists.newArrayList();
                List <String> updateRowIds = Lists.newArrayList();
                for (int i = 0; i < detailReocrd.length; i++) {
                    WorkflowRequestTableField[] rowFields = detailReocrd[i].getWorkflowRequestTableFields();
                    int recordOrder = detailReocrd[i].getRecordOrder();
                    if (recordOrder <= 0) continue;
                    updateRowIds.add(String.valueOf(recordOrder));
                    for (WorkflowRequestTableField field : rowFields) {
                        String fieldName = field.getFieldName();
                        if (!updateFields.contains(fieldName)) {
                            updateFields.add(fieldName);
                        }
                    }
                }

                List <FieldInfoEntity> fieldInfos = dtableEntity.getFieldInfos();
                for (FieldInfoEntity fieldInfoEntity : fieldInfos) {
                    insertFields.add(fieldInfoEntity.getFieldName());
                }

                Map<String, Map<String, Object>> detailDatas = loadDetailData(updateFields, updateRowIds, dtableEntity);
                log.error("~~~rest创建流程~~~解析明细:"+detailDatas);
                //遍历明细数据
                for (int i = 0; i < detailReocrd.length; i++) {
                    DetailRowInfoEntity rowEntity = new DetailRowInfoEntity();
                    int recordOrder = detailReocrd[i].getRecordOrder();
                    rowEntity.setRowIndex(i);
                    rowEntity.setId(recordOrder);
                    Map <String, Object> rowData = new HashMap <String, Object>();
                    WorkflowRequestTableField[] rowFields = detailReocrd[i].getWorkflowRequestTableFields();
                    if (rowFields==null){
                        log.error("~~~rest创建流程~~~传入的明细表:"+detailTableInfo.getTableDBName()+"的第"+(i+1)+"行数据中，字段信息为空。");
                        continue;
                    }
                    for (WorkflowRequestTableField rowItem : rowFields) {
                        FieldInfoEntity fieldInfo = fieldInfoMap.get(rowItem.getFieldName());
                        if (fieldInfo == null) {
                            errMsg.put("errParam_detail_" + rowItem.getFieldName(), rowItem.getFieldValue());
                            continue;
                        }
                        String fieldValue = rowItem.getFieldValue();
                        fieldValue = uploadFile(fieldInfo, fieldValue, fieldUploadDocCategory, user, errMsg);
                        Map <String, Object> fieldValueObj = new HashMap <String, Object>();
                        fieldValueObj.put("value", fieldValue);
                        rowData.put(rowItem.getFieldName(), fieldValueObj);
                        if (recordOrder > 0 && !detaileditfields.contains(fieldInfo.getFieldId())) {
                            detaileditfields.add(fieldInfo.getFieldId());
                        }
                    }

                    //解决更新明细行数据时，不同行传字段值不同导致更新失败问题
                    //补全缺少的明细数据
                    if (recordOrder > 0) {
                        for (String fieldName : updateFields) {
                            if (rowData.get(fieldName) == null) {
                                Map <String, Object> fieldValueObj = detailDatas.get(recordOrder + "_" + fieldName);
                                if (fieldValueObj == null) {
                                    errMsg.put("errParam_detail_" + recordOrder + "_" + fieldName, "fieldvalue is null");
                                    reqFailMsg.setMsgType(ReqFlowFailMsgType.SAVE_DETAIL_TABLE_FAIL);
                                    return errMsg;
                                }
                                rowData.put(fieldName, fieldValueObj);
                            }
                        }
                    } else {
                        for (String fieldName : insertFields) {
                            if (rowData.get(fieldName) == null) {
                                Map <String, Object> fieldValueObj = new HashMap <>();
                                fieldValueObj.put("value", "");
                                rowData.put(fieldName, fieldValueObj);
                            }
                        }
                    }

                    rowEntity.setDatas(rowData);
                    rowInfos.add(rowEntity);
                    if (errMsg.size() > 0) {
                        reqFailMsg.setMsgType(ReqFlowFailMsgType.SAVE_DETAIL_TABLE_FAIL);
                        return errMsg;
                    }
                }
            }
            List <DetailTableInfoEntity> detailTableInfos = reqEntity.getDetailTableInfos();
            for (DetailTableInfoEntity dtableEntity : detailTableInfos) {
                if (dtableEntity.getDetailRowInfos() == null) {
                    List <DetailRowInfoEntity> rowInfos = new ArrayList <>();
                    dtableEntity.setDetailRowInfos(rowInfos);
                    dtableEntity.setDeleteIds("");
                }
            }

            reqEntity.setDetaileditfields(detaileditfields);
            try {
                flowService.saveDetailFormDatas(reqEntity, otherParams);
            } catch (Exception e) {
                reqFailMsg.setMsgType(ReqFlowFailMsgType.SAVE_DETAIL_TABLE_FAIL);
                Map <String, Object> msgInfo = reqFailMsg.getMsgInfo();
                throw new Exception("save detail form error!");
            }
        }
        return errMsg;
    }

    /**
     * 加载明细行数据
     *
     * @param updateFields
     * @param updateRowIds
     * @param dtableEntity
     * @return
     */
    private static Map <String, Map <String, Object>> loadDetailData(List <String> updateFields, List <String> updateRowIds,
                                                                     DetailTableInfoEntity dtableEntity) {
        Map <String, Map <String, Object>> detailDatas = new HashMap <>();
        if (updateFields.size() > 0 && updateRowIds.size() > 0) {
            String detailTableName = dtableEntity.getTableDbName();
            RecordSet rs = new RecordSet();
            rs.executeQuery("select id," + StringUtils.join(updateFields, ",") + " from " + detailTableName + " where id in (" + StringUtils.join(updateRowIds, ",") + ") ");
            while (rs.next()) {
                int rowid = rs.getInt("id");
                for (String fieldName : updateFields) {
                    String fieldValue = Util.null2String(rs.getString(fieldName));
                    Map <String, Object> fieldValueObj = new HashMap <>();
                    fieldValueObj.put("value", fieldValue);
                    detailDatas.put(rowid + "_" + fieldName, fieldValueObj);
                }
            }
        }
        return detailDatas;
    }


    /**
     * 上传附件
     *
     * @param fieldInfo
     * @param fieldValue
     * @param fieldUploadDocCategory
     * @param user
     * @return
     * @throws Exception
     */
    public static String uploadFile(FieldInfoEntity fieldInfo, String fieldValue, Map <String, String> fieldUploadDocCategory, User user, Map <String, Object> errMsg) throws Exception {
        String fieldid = fieldInfo.getFieldId();
        String docCategory = Util.null2String(fieldUploadDocCategory.get(fieldid));
        if ("".equals(docCategory)) docCategory = Util.null2String(fieldUploadDocCategory.get("docCategory_def"));
        //附件上传
        if ("6".equals(fieldInfo.getFieldHtmlType()) && !"".equals(Util.null2String(fieldValue).trim())) {
            //支持直接传附件id，已","分割 start
            String[] tempFieldValue = fieldValue.split(",");
            String newFieldValue = "";
            for (String tmpValue : tempFieldValue) {
                int fileid = Util.getIntValue(tmpValue);
                if (fileid == -1) {
                    continue;
                }
                newFieldValue += tmpValue + ",";
            }
            if (newFieldValue.endsWith(",")) {
                newFieldValue = newFieldValue.substring(0, newFieldValue.length() - 1);
                return newFieldValue;
            }
            //end
            //校验是否有设置附件上传目录
            if ("".equals(docCategory)) {
                errMsg.put("field_" + fieldInfo.getFieldName(), "" + weaver.systeminfo.SystemEnv.getHtmlLabelName(10005572, weaver.general.ThreadVarLanguage.getLang()) + "");
                return "";
            }

            String[] categoryArr = Util.splitString(docCategory, ",");
            int secid = -1;
            if (categoryArr.length == 3) {
                secid = Util.getIntValue(categoryArr[2]);
            }
            if (secid <= 0) return "";
            DocAccService docAccService = new DocAccService();
            DocSaveService docSaveService = new DocSaveService();
            List <Integer> tempFieldVal = new ArrayList <>();
            JSONArray valArr = JSONObject.parseArray(fieldValue);
            if (valArr.size() > 0) {
                for (int i = 0; i < valArr.size(); i++) {
                    JSONObject item = (JSONObject) valArr.get(i);
                    String filePath = Util.null2String(item.get("filePath"));
                    String fileName = Util.null2String(item.get("fileName"));
                    int annexId = 0;
                    if (filePath.startsWith("http") || filePath.startsWith("file:")) {
                        annexId = docAccService.getFileByUrl(filePath, fileName);//ftp附件走这个方法取不到，换成FileProcess的方法
                    }else if(filePath.startsWith("ftp")){
                        FileProcessor fileProcessor = new FileProcessor();
                        annexId = fileProcessor.Process(filePath,docCategory,user,fileName);
                    } else if (filePath.startsWith("base64:")) {
                        String base64Data = filePath.replace("base64:", "");
                        base64Data = base64Data.replaceAll(" ", "+");
                        annexId = docAccService.getFileByBase64(base64Data, fileName);
                    }
                    if (annexId > 0) {
                        int docId = 0;
                        if(filePath.startsWith("ftp")){
                            docId = annexId;//如果是ftp附件,fileProcess获取到的已经是docId了
                        }else {
                            docId = docSaveService.accForDoc(secid, annexId, user);
                        }
                        if (docId < 0) {
                            errMsg.put("field_" + fieldInfo.getFieldName(), "" + weaver.systeminfo.SystemEnv.getHtmlLabelName(10005573, weaver.general.ThreadVarLanguage.getLang()) + "fieldid" + fieldid + ",docId:" + docId + ",fileValue:" + fieldValue);
                        } else {
                            tempFieldVal.add(docId);
                        }
                    } else {
                        errMsg.put("field_" + fieldInfo.getFieldName(), "" + weaver.systeminfo.SystemEnv.getHtmlLabelName(10005574, weaver.general.ThreadVarLanguage.getLang()) + "," + weaver.systeminfo.SystemEnv.getHtmlLabelName(10005575, weaver.general.ThreadVarLanguage.getLang()) + "fieldid:" + fieldid + ",annexId:" + annexId + ",fileValue:" + fieldValue);
                        continue;
                    }
                }
            }
            if (tempFieldVal.size() > 0) {
                return StringUtils.join(tempFieldVal, ",");
            }
        }
        return fieldValue;
    }

    public static Map <String, FieldInfoEntity> getFieldMap(List <FieldInfoEntity> fieldInfos) {
        Map <String, FieldInfoEntity> fieldMap = new HashMap <>();
        if (fieldInfos == null) return fieldMap;
        for (FieldInfoEntity fieldInfoEntity : fieldInfos) {
            fieldMap.put(fieldInfoEntity.getFieldName(), fieldInfoEntity);
        }
        return fieldMap;
    }

    /**
     * @param reqEntity
     * @param detailDBTable
     * @return
     */
    public static DetailTableInfoEntity getDetailTableInfoEntity(RequestInfoEntity reqEntity, String detailDBTable) {
        List <DetailTableInfoEntity> detailTBEntitys = reqEntity.getDetailTableInfos();
        for (DetailTableInfoEntity detailTableInfoEntity : detailTBEntitys) {
            if (detailTableInfoEntity.getTableDbName().equalsIgnoreCase(detailDBTable)) {
                return detailTableInfoEntity;
            }
        }
        return null;
    }

    /**
     * 初始化流程信息
     *
     * @param user
     * @param reqEntity
     * @return
     */
    public static int initRequestBaseInfo(User user, RequestInfoEntity reqEntity, ReqOperateRequestEntity requestParam) {
        RequestIdUpdate requestIdUpdate = new RequestIdUpdate();
        WorkflowBaseInfoEntity baseInfoEntity = reqEntity.getWorkflowBaseInfo();
        MainTableInfoEntity mainTableInfoEntity = reqEntity.getMainTableInfoEntity();
        int workflowId = reqEntity.getWorkflowId();
        int isBill = Util.getIntValue(baseInfoEntity.getIsBill());
        int formId = Util.getIntValue(baseInfoEntity.getFormId());
        int[] rvalue = requestIdUpdate.getRequestNewId(isBill == 1 ? mainTableInfoEntity.getTableDbName() : "");
        int requestid = rvalue[0];
        int billid = rvalue[1];
        RequestOperationBiz.addWorkflowCount(reqEntity.getWorkflowId(), user);
        RecordSet rs = new RecordSet();
        rs.executeUpdate("insert into workflow_form (requestid,billformid,billid) values(?,?,?)", requestid, formId, billid); // 初始化请求信息表
        int nodeid = 0;
        String[] currentDt = CommonUtil.getCurrentDate();
        String currentDate = currentDt[0];
        String currentTime = currentDt[1];

        rs.executeQuery("select nodeid from workflow_flownode where workflowid = ? and nodetype = '0'", workflowId);
        if (rs.next()) {
            nodeid = rs.getInt("nodeid");
        }

        reqEntity.setCurrentNodeId(String.valueOf(nodeid));
        reqEntity.setExtendNodeId(String.valueOf(FreeNodeBiz.getExtendNodeId(nodeid)));
        reqEntity.setCurrentNodeType("0");
        reqEntity.setIsremark(0);

        List <Object> requestBaseInsertParams = new ArrayList <>();
        requestBaseInsertParams.add(requestid);
        requestBaseInsertParams.add(workflowId);
        requestBaseInsertParams.add(nodeid);
        requestBaseInsertParams.add("0");
        requestBaseInsertParams.add(nodeid);
        requestBaseInsertParams.add("0");
        requestBaseInsertParams.add("");
        requestBaseInsertParams.add(0);
        requestBaseInsertParams.add(0);
        requestBaseInsertParams.add(reqEntity.getRequestName());
        requestBaseInsertParams.add(user.getUID());
        requestBaseInsertParams.add(currentDate);
        requestBaseInsertParams.add(currentTime);
        requestBaseInsertParams.add(user.getUID());
        requestBaseInsertParams.add(currentDate);
        requestBaseInsertParams.add(currentTime);
        requestBaseInsertParams.add("0");
        requestBaseInsertParams.add("0");
        requestBaseInsertParams.add("0");
        requestBaseInsertParams.add("-1");
        requestBaseInsertParams.add("-1");
        requestBaseInsertParams.add("");
        requestBaseInsertParams.add("");
        requestBaseInsertParams.add("");
        requestBaseInsertParams.add("");
        requestBaseInsertParams.add("");
        requestBaseInsertParams.add(Util.null2String(reqEntity.getMessageType()));
        requestBaseInsertParams.add(Util.null2String(reqEntity.getChatsType()));

        rs.executeProc("workflow_requestbase_insertnew", CommonUtil.joinProcedureParams(requestBaseInsertParams));

        Map <String, Object> otherParams = requestParam.getOtherParams();
        String requestSecLevel = Util.null2s(Util.null2String(otherParams.get("requestSecLevel")), "4");
        String requestSecValidity = Util.null2String(otherParams.get("requestSecValidity"));
        int messageType = Util.getIntValue(Util.null2String(otherParams.get("messageType")), 0);
        int requestLevel = Util.getIntValue(requestParam.getRequestLevel(), 0);

        String updateSql = "update workflow_requestbase set messageType = ?,requestLevel = ? where requestid = ?";
        rs.executeUpdate(updateSql, messageType, requestLevel, requestid);
        RequestSecLevelBiz secLevelBiz = new RequestSecLevelBiz();
        secLevelBiz.updateSecLevelByRequestId(requestid+"", requestSecLevel+"#"+requestSecValidity, user);

        List <Object> currentOperatorInsertParams = new ArrayList <>();
        currentOperatorInsertParams.add(requestid);
        currentOperatorInsertParams.add(user.getUID());
        currentOperatorInsertParams.add(0);
        currentOperatorInsertParams.add(workflowId);
        currentOperatorInsertParams.add(baseInfoEntity.getWorkflowTypeId());
        currentOperatorInsertParams.add(0);
        currentOperatorInsertParams.add(0);
        currentOperatorInsertParams.add(nodeid);
        currentOperatorInsertParams.add(-1);
        currentOperatorInsertParams.add("0");
        currentOperatorInsertParams.add(-1);
        currentOperatorInsertParams.add(0);
        currentOperatorInsertParams.add(currentDate);
        currentOperatorInsertParams.add(currentTime);
        rs.executeProc("workflow_CurrentOperator_I2", CommonUtil.joinProcedureParams(currentOperatorInsertParams));
        rs.executeUpdate("delete from workflow_nownode where requestid=?", requestid);
        rs.executeUpdate("insert into workflow_nownode(requestid,nownodeid,nownodetype,nownodeattribute) values(?,?,0,0)", requestid, nodeid);
        return requestid;
    }

    /**
     * 流转至下一节点
     *
     * @param reqEntity
     * @param user
     * @param requestParam
     * @return
     */
    public static boolean flowNextNode(RequestInfoEntity reqEntity, User user, ReqOperateRequestEntity requestParam,ReqFlowFailMsgEntity reqFailMsg) {
        boolean flowFlag = false;
        try {
            Map <String, Object> otherParams = requestParam.getOtherParams();
            int requestid = Util.getIntValue(reqEntity.getRequestId());
            WorkflowBaseInfoEntity baseInfoEntity = reqEntity.getWorkflowBaseInfo();
            MainTableInfoEntity mainTableInfoEntity = reqEntity.getMainTableInfoEntity();
            int workflowId = reqEntity.getWorkflowId();
            int isBill = Util.getIntValue(baseInfoEntity.getIsBill());
            int formId = Util.getIntValue(baseInfoEntity.getFormId());
            int nodeId = 0;
            int currentOperateId = 0;
            int isremark = -1;
            int takisRemark = -1;
            String takEnd = String.valueOf(otherParams.get("takEnd"));

            RecordSet rs = new RecordSet();
//            rs.executeQuery(com.api.workflow.util.ServiceUtil.calculateCurrentNodeSql(reqEntity.getRequestId(), user.getUID(), user.getType()));
            RequestAuthenticationService authenticationService = new RequestAuthenticationService();
            authenticationService.setUser(user);
            authenticationService.getRequestUserRight(null, requestid);
            Map<String, Object> authInfo = authenticationService.getAuthInfo();
            isremark = Util.getIntValue(Util.null2String(authInfo.get("isremarkForRM")));
            currentOperateId = Util.getIntValue(Util.null2String(authInfo.get("wfcurrrid")));
            nodeId = Util.getIntValue(Util.null2String(authInfo.get("nodeid")));
            takisRemark = Util.getIntValue(Util.null2String(authInfo.get("takisremark")));

            if ("intervenor".equals(reqEntity.getSrc())) {
                nodeId = Util.getIntValue(reqEntity.getCurrentNodeId());
            }

            NodeInfoEntity nodeInfoEntity = com.engine.workflow.biz.workflowCore.WorkflowBaseBiz.getNodeInfo(nodeId);
            //批注、回复
            if (isremark == 1 || isremark == 8 || isremark == 9 || isremark == 11) {
                rs.executeQuery("select takid from workflow_currentoperator where id = ?",currentOperateId);
                rs.next();
                int takId = rs.getInt("takid");
                reqEntity.setCurrentOperateId(currentOperateId);
                flowFlag = reqComment(reqEntity, user, isremark, nodeId, requestParam.getRemark(), nodeInfoEntity.getNodetype(), takisRemark, takId);
            } else if ("1".equals(takEnd)) {
                if (takisRemark == -2 && isremark == 0) {
                    TakEndBiz takEndBiz = new TakEndBiz();
                    takEndBiz.setParams(otherParams);
                    flowFlag = takEndBiz.doTakEnd(requestid, user);
                }
            } else {
                RequestManager requestManager = new RequestManager();
                requestManager.setUser(user);
                requestManager.setSrc(reqEntity.getSrc());
                requestManager.setIscreate("");
                requestManager.setRequestid(requestid);
                requestManager.setWorkflowid(workflowId);
                requestManager.setWorkflowtype(baseInfoEntity.getWorkflowTypeId());
                requestManager.setIsremark(0);
                requestManager.setFormid(formId);
                requestManager.setIsbill(isBill);
                requestManager.setBillid(mainTableInfoEntity != null ? mainTableInfoEntity.getBillid() : -1);
                requestManager.setNodeid(nodeId);
                requestManager.setNodetype(Util.null2String(nodeInfoEntity.getNodetype()));
                requestManager.setRequestname(Util.null2String(reqEntity.getRequestName()));
                requestManager.setRequestlevel(reqEntity.getRequestLevel());
                requestManager.setRemark(Util.null2String(requestParam.getRemark()));
                requestManager.setMessageType(reqEntity.getMessageType());
                requestManager.setNeedwfback(Util.null2String(otherParams.get(OTHERPARAM_NEEDBACK)));
                requestManager.setReqFailMsg(reqFailMsg);
                requestManager.setOtherParams(otherParams);
                //流程退回
                if ("reject".equals(reqEntity.getSrc())) {
                    requestManager.setRejectToType(Util.getIntValue(Util.null2String(otherParams.get(OTHERPARAM_REJECTTYPE))));
                    requestManager.setRejectToNodeid(Util.getIntValue(Util.null2String(otherParams.get(OTHERPARAM_REJECTTONODEID))));

                    //取退回提交方式
                    String isSubmitDirectNode = nodeInfoEntity.getIsSubmitDirectNode();
                    String isSubmitDirect = "0";
                    if ("1".equals(isSubmitDirectNode)) {
                        isSubmitDirect = "1";
                    } else if ("2".equals(isSubmitDirectNode)) {
                        String isSubmitDirectTmp = Util.null2String(otherParams.get(OTHERPARAM_SUBMITDIRECT));
                        if (!("0".equals(isSubmitDirectTmp) || "1".equals(isSubmitDirectTmp))) {
                            int isSubmitDirectNodeDeft = 0;////退回直达本节点 用户选择默认处理方式
                            int extendnodeid = FreeNodeBiz.getExtendNodeId(nodeId);
                            rs.executeQuery("select isSubmitDirectNodeDeft from workflow_flownode where workflowid=? and nodeid=?", reqEntity.getWorkflowId(), extendnodeid);
                            if (rs.next()) {
                                isSubmitDirect = Util.null2s(rs.getString("isSubmitDirectNodeDeft"), "0");
                            }
                        } else {
                            isSubmitDirect = isSubmitDirectTmp;
                        }
                    }
                    requestManager.setIsSubmitDirect(isSubmitDirect);
                }

                if ("intervenor".equals(reqEntity.getSrc())) {
                    int submitNodeId = requestParam.getSubmitNodeId();
                    NodeInfoEntity intervenorNodeEntity = com.engine.workflow.biz.workflowCore.WorkflowBaseBiz.getNodeInfo(submitNodeId);
                    String tempSubmitNodeId = submitNodeId + "_" + intervenorNodeEntity.getNodetype() + "_" + intervenorNodeEntity.getNodeAttribute();
                    requestManager.setSubmitNodeId(tempSubmitNodeId);
                    requestManager.setIntervenorid(requestParam.getIntervenorid());
                    requestManager.setSignType(requestParam.getSignType());
                    requestManager.setEnableIntervenor(requestParam.isEnableIntervenor() ? 1 : 0);
                }

                //提交到指定节点
                int submitToNodeId = 0;
                if (otherParams.containsKey(OTHERPARAM_SUBMITTONODEID)) {
                    submitToNodeId = Util.getIntValue(Util.null2s(Util.null2String(otherParams.get(OTHERPARAM_SUBMITTONODEID)), "0"));
                }
                if ("submit".equals(reqEntity.getSrc()) && !(FreeNodeBiz.isFreeNode(submitToNodeId) || submitToNodeId > 0)) {
                    Map <String, String> submitToNodeInfo = RequestFlowBiz.getSubmitToNodeInfo(reqEntity);
                    if (submitToNodeInfo != null && "1".equals(submitToNodeInfo.get("isSubmitDirectNode"))) {
                        submitToNodeId = Util.getIntValue(submitToNodeInfo.get("lastnodeid"));
                    }
                }
                requestManager.setSubmitToNodeid(submitToNodeId);

                //指定流转
                if("submit".equals(reqEntity.getSrc())) {
                    boolean isSelectNextFlow = "1".equals(Util.null2String(otherParams.get("selectNextFlow")));
                    String selectNodeFlow_nodeIds = Util.null2String(otherParams.get("selectNodeFlow_nodeIds"));
                    SelectNextFlowMode selectNextFlowMode = SelectNextFlowMode.getModeType(Util.getIntValue(Util.null2String(otherParams.get("selectNextFlowMode"))));
                    requestManager.setSelectNextFlow(isSelectNextFlow);
                    requestManager.setSelectNodeFlowNodeIds(selectNodeFlow_nodeIds);
                    requestManager.setSelectNextFlowMode(selectNextFlowMode);
                }

                //提交、退回才执行节点后附加操作
                if (submitToNodeId == 0 && ("submit".equals(reqEntity.getSrc()) || "reject".equals(reqEntity.getSrc()))) {
                    try {
                        RequestCheckAddinRules requestCheckAddinRules = new RequestCheckAddinRules();
                        requestCheckAddinRules.resetParameter();
                        requestCheckAddinRules.setTrack(false);
                        requestCheckAddinRules.setStart(false);
                        requestCheckAddinRules.setNodeid(nodeId);
                        requestCheckAddinRules.setRequestid(requestid);
                        requestCheckAddinRules.setWorkflowid(workflowId);
                        requestCheckAddinRules.setObjid(nodeId);
                        // 1: 节点自动赋值 0 :出口自动赋值
                        requestCheckAddinRules.setObjtype(1);
                        requestCheckAddinRules.setIsbill(isBill);
                        requestCheckAddinRules.setFormid(formId);
                        requestCheckAddinRules.setIspreadd("0");
                        requestCheckAddinRules.setRequestManager(requestManager);
                        requestCheckAddinRules.setUser(user);
                        requestCheckAddinRules.checkAddinRules();

                        if (requestCheckAddinRules.getDoDmlResult().startsWith("-1")) {
                            reqFailMsg.setMsgType(ReqFlowFailMsgType.AFTER_NODE_OPERATE_EX_FAIL);
                            return flowFlag;
                        }
                    } catch (Exception e) {
                        reqFailMsg.setMsgType(ReqFlowFailMsgType.AFTER_NODE_OPERATE_EX_FAIL);
                        return flowFlag;
                    }
                }

                String updateSql = "update workflow_requestbase set lastOperator = ?,lastOperateDate = ?,lastOperateTime = ? where requestid = ?";
                rs.executeUpdate(updateSql, user.getUID(), requestManager.getCurrentDate(), requestManager.getCurrentTime(), requestid);

                updateReqManagerFieldValue(requestid, formId, isBill, user, currentOperateId);
                BillBgOperation billBgOperation = null;
                if (isBill == 1 && formId > 0) {
                    billBgOperation = getBillBgOperation(requestManager);
                }

                if (billBgOperation != null) {
                    billBgOperation.billDataEdit();
                }
                flowFlag = ServiceUtil.getService(RequestManagerServiceImpl.class, user).flowNextNode(requestManager);
                if (!flowFlag) {
                    String message = requestManager.getMessage();
                    String messagecontent = requestManager.getMessagecontent();
                    reqFailMsg.setMsgInfo(SubmitErrorMsgBiz.getMsgInfo(null,user,message,messagecontent));
                    log.error("流程wbservice提交失败requestid:" + requestid + ",userid:" + user.getUID() + ",msg:" + requestManager.getMessagecontent());
                    return false;
                }
                reqFailMsg.getOtherParams().put("doAutoApprove",requestManager.getNodeInfoCache().size() > 0 ? "1" : "0");
                try {
//                    if (flowFlag)
//                        requestRemind(requestid, workflowId, String.valueOf(user.getUID()), reqEntity.getSrc(), -1, -1);
                    if (billBgOperation != null) {
                        billBgOperation.setFlowStatus(flowFlag);
                        flowFlag = billBgOperation.billExtOperation();
                    }
                } catch (Exception e) {
                    log.error(e);
                    log.info("流程已成功流转");
                }
            }
            RequestFormBiz.updateRequestInfo(requestid, user, false, false, false, "3".equals(nodeInfoEntity.getNodetype()), requestParam.getClientIp());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
        return flowFlag;
    }

    /**
     * 批注、回复
     *
     * @param reqEntity
     * @param user
     * @param isremark
     * @param currentnodeid
     * @param remark
     * @param currentnodetype
     * @return
     */
    public static boolean reqComment(RequestInfoEntity reqEntity, User user, int isremark, int currentnodeid, String remark, int currentnodetype, int takIsRemark, int takId) {
        int requestId = Util.getIntValue(reqEntity.getRequestId());
        int userType = "1".equals(user.getLogintype()) ? 0 : 1;
        int userId = user.getUID();
        int workflowId = reqEntity.getWorkflowId();
        boolean returnFlag = false;
        RecordSet rs = new RecordSet();
        RecordSet rs1 = new RecordSet();
        List<Object> procParam = new ArrayList<>();
        //抄送（不提交）查看页面即更新为已办事宜。
        boolean needSaveLog = false;
        if (isremark == 8) {
            procParam.add(requestId);
            procParam.add(userId);
            procParam.add(userType);
            returnFlag = rs.executeProc("workflow_CurrentOperator_Copy", CommonUtil.joinProcedureParams(procParam));
        } else if (isremark == 1 || isremark == 9 || isremark == 11) {
            RequestLogType logType = (isremark == 1 && takIsRemark == 2) ? RequestLogType.REPLY : RequestLogType.ANNOTATION;
            rs.executeQuery("select hasccback from workflow_nodecustomrcmenu where hasccback='1' and wfid= ? and nodeid= ? ", reqEntity.getWorkflowId(), currentnodeid);
            boolean needBack = rs.next();
            if (isremark == 1) {

                procParam.add(requestId);
                procParam.add(userId);
                procParam.add(userType);
                if (takIsRemark == 2) {
                    int multiTakLevel = 0;
                    int beforwardid = 0;
                    int forwardid = 0;
                    int beforwardNodeId = 0;
                    String forwardids = "";
                    String[] currentDt = CommonUtil.getCurrentDate();
                    String currentdate = Util.null2String(currentDt[0]);
                    String currenttime = Util.null2String(currentDt[1]);
                    ChuanyueUtil.executeProc4Chuanyue(workflowId, requestId, currentnodeid, user.getUID(), user.getType(), currentdate, currenttime, "0");

                    String taksql = "select id,takid,multiTakLevel, nodeid from workflow_currentoperator where requestid= " + requestId + " and nodeid in ( " + currentnodeid + ") and userid = " + userId
                            + " and preisremark='1' and takisremark = 2 order by userid,id";
                    rs.executeQuery(taksql);
                    while (rs.next()) {
                        multiTakLevel = Util.getIntValue(rs.getString("multiTakLevel"), 0);
                        beforwardid = Util.getIntValue(rs.getString("id"));
                        beforwardNodeId = Util.getIntValue(rs.getString("nodeid"));
                        forwardid = Util.getIntValue(rs.getString("takid"));

                        if (forwardid > 0) {
                            String sumBeforWardSql = "select * from workflow_currentoperator where requestid= " + requestId + " and nodeid = " + beforwardNodeId
                                    + " and ((isremark=1 and preisremark='1') or istakout = 1) and takisremark = 2 and (id in (select beforwardid from workflow_forward where requestid=" + requestId + " and forwardid="
                                    + forwardid + " and beforwardid<>" + beforwardid + ") or takid = " + forwardid + ")";
                            rs1.executeSql(sumBeforWardSql);
                            if (!rs1.next()) {
                                forwardids += forwardid + ",";
                                String uptaksql2 = "update workflow_currentoperator set takisremark=0 where requestid= " + requestId + " and nodeid = " + beforwardNodeId
                                        + " and isremark = 0 and takisremark = -2 and id=" + forwardid;
                                rs1.executeSql(uptaksql2);

                                if (multiTakLevel > 0) {
                                    TakReplyBiz takOptionBiz = new TakReplyBiz(requestId, workflowId, user);
                                    takOptionBiz.doTakingReply(takId, multiTakLevel, reqEntity.getCurrentOperateId());
                                } else {
                                    //判断是否意见征询都回复了
//                        rs.executeQuery("select count(id) from workflow_currentoperator where takid = ? and isremark = 1 and takisremark = 2", takId);
//                        int consultationCount = rs.next() ? rs.getInt(1) : 1;
//                        if (consultationCount == 1) {
//                            rs.executeUpdate("update workflow_currentoperator set takisremark = 0 where requestid = ? and id = ?", requestId, takId);
//                        }
                                    int forwardUserid = 0;
                                    rs1.executeQuery("select userid from workflow_currentoperator where id = ?", forwardid);
                                    if (rs1.next()) {
                                        forwardUserid = Util.getIntValue(rs1.getString("userid"), 0);
                                    }

                                    //调整islasttimes，让流程回到待办
                                    rs1.executeQuery("select id from workflow_currentoperator where islasttimes = 1 and requestid = ? and userid = ?", requestId, forwardUserid);
                                    int count = rs1.getCounts();//一个requestid，userid存在islasttimes = 1的记录数量，只要不等于1说明需要修复
                                    int forwardid_temp = -1;//islasttimes = 1的记录与征询人的这条记录不相同，则需要修复
                                    if (rs1.next()) {
                                        forwardid_temp = rs1.getInt("id");
                                    }
                                    //增加需要进入修复逻辑的条件（增加条件的原因是因为很多客户再执行这段逻辑时，执行完第一句时，执行第二句就死锁了，导致出问题了）
                                    if (count != 1 || forwardid_temp != forwardid) {
                                        rs1.executeUpdate("update workflow_currentoperator set islasttimes = 0 where requestid = ? and userid = ? and id != ?", requestId, forwardUserid, forwardid);
                                        rs1.executeUpdate("update workflow_currentoperator set islasttimes = 1 where id = ?", forwardid);
                                    }
                                }
                            }
                        }
                    }
                    WfDataCorrectionUtils.correctTakData(requestId,forwardids);
                    needSaveLog = true;
                } else {
                    WFForwardManager wfForwardManager = new WFForwardManager();
                    wfForwardManager.setCurrentNodeId(currentnodeid);
                    wfForwardManager.setNodeid(currentnodeid);
                    wfForwardManager.setIsremark(isremark + "");
                    wfForwardManager.setTakIsremark(takIsRemark + "");
                    wfForwardManager.setRequestid(requestId);
                    wfForwardManager.setBeForwardid(reqEntity.getCurrentOperateId());
                    needSaveLog = wfForwardManager.getCanSubmit();
                }
                returnFlag = rs.executeProc(needBack ? "workflow_CurOpe_UbyForward" : "workflow_CurOpe_UbyForwardNB", CommonUtil.joinProcedureParams(procParam));
            } else if (isremark == 9) {
                needSaveLog = true;
                procParam.add(requestId);
                procParam.add(userId);
                procParam.add(userType);
                procParam.add(isremark);
                returnFlag = rs.executeProc(needBack ? "workflow_CurOpe_UbySend" : "workflow_CurOpe_UbySendNB", CommonUtil.joinProcedureParams(procParam));
            } else if (isremark == 11) {
                needSaveLog = ChuanyueUtil.isSubmitSignByRequestId(workflowId, requestId, currentnodeid, userId, userType, String.valueOf(isremark));//传阅是否可提交签字意见
                logType = RequestLogType.COMMENT;
                returnFlag = ChuanyueUtil.executeProc4Chuanyue(reqEntity.getWorkflowId(), requestId, currentnodeid, userId, userType);
            }

            if (needSaveLog) {
                RequestBaseBiz.operateFeedBack(requestId, reqEntity.getWorkflowId(), currentnodeid, userId, remark, "", "");
                RequestLog requestLog = new RequestLog();
                requestLog.setIsremark(isremark);
                requestLog.setCurrId(reqEntity.getCurrentOperateId());
                requestLog.saveLog(reqEntity.getWorkflowId(), requestId, currentnodeid, logType.getKey(), remark, user);
            }
        }
        if (currentnodetype == 3) {
            rs.executeUpdate("update workflow_currentoperator set iscomplete=1 where requestid= ? and userid= ? and usertype= ?", requestId, userId, userType);
        }
        return returnFlag;
    }


    /**
     * @param reqId
     * @param wfId
     * @param createrId
     * @param submitType
     * @param sms
     * @param email
     */
    private static void requestRemind(int reqId, int wfId, String createrId, String submitType, int sms, int email) {
        try {
            User user = User.getUser(Util.getIntValue(createrId, -1), 0);
            RequestRemindBiz requestRemindBiz = new RequestRemindBiz(user);
            requestRemindBiz.requestSubmitRemind4WebService(reqId, wfId, submitType, sms, email);

        } catch (Exception e) {
            log.error("RequestOperateBiz--requestRemind-err" + e.getMessage());
        }
    }

    /**
     * 获取BillBgOperation
     *
     * @param requestManager 请求管理对象
     * @return BillBgOperation
     */
    private static BillBgOperation getBillBgOperation(RequestManager requestManager) {
        BillBgOperation billBgOperation = null;
        String operationpage = "";

        try {
            RecordSet rs = new RecordSet();
            int formid = requestManager.getFormid();

            rs.executeProc("bill_includepages_SelectByID", formid + "");
            if (rs.next()) {
                operationpage = Util.null2String(rs.getString("operationpage")).trim();
                if (operationpage.indexOf(".jsp") >= 0) {
                    operationpage = operationpage.substring(0, operationpage.indexOf(".jsp"));
                } else {
                    operationpage = null;
                }
            }

            if (operationpage != null && !"".equals(operationpage)) {
                operationpage = "weaver.soa.workflow.bill." + operationpage;
                Class operationClass = Class.forName(operationpage);
                billBgOperation = (BillBgOperation) operationClass.newInstance();
                billBgOperation.setRequestManager(requestManager);
            }
        } catch (Exception e) {
            log.error(e);
            return null;
        }

        return billBgOperation;
    }

    /**
     * 更新manager字段
     *
     * @return
     */
    public static boolean updateReqManagerFieldValue(int requestid, int formId, int isBill, User user,
                                                     int currentOperateId) {
        RecordSet rs = new RecordSet();
        String formtablename = "";
        String usermanager = user.getManagerid();
        rs.executeQuery("select userid,agenttype,agentorbyagentid from workflow_currentoperator where id = ?", currentOperateId);
        if (rs.next()) {
            if ("2".equals(Util.null2String(rs.getString("agenttype")))) {
                try {
                    ResourceComInfo resourceComInfo = new ResourceComInfo();
                    usermanager = resourceComInfo.getManagerID(rs.getString("agentorbyagentid"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (isBill == 1) {
            rs.executeQuery(" select tablename from workflow_bill a where exists (select 1 from workflow_billfield b where b.billid = a.id and b.billid  = ? and b.fieldname = 'manager')", formId);
            if (rs.next()) {
                formtablename = Util.null2String(rs.getString("tablename"));
            }
        } else {
            rs.executeQuery("select fieldname from workflow_formfield a left join workflow_formdict b on a.fieldid  = b.id where a.formid  = ? and b.fieldname ='manager'", formId);
            if (rs.next()) {
                formtablename = "workflow_form";
            }
        }
        if (usermanager == null || "".equals(usermanager)) {
            usermanager = "0";
        }
        if (!"".equals(formtablename)) {
            return rs.executeUpdate("update " + formtablename + " set manager = ?  where requestid  = ?", usermanager, requestid);
        }

        return true;
    }


    /**
     * @param formid
     * @param workflowid
     */
    public static void drawBackSpecialTreatment(int formid, int workflowid) {
        RecordSet rs = new RecordSet();
        // 这里判断是否是自定义资产的流程，如果是，就撤回
        CptWfUtil cptWfUtil = new CptWfUtil();
        String cptwftype = cptWfUtil.getWftype("" + workflowid);
        if (!"".equals(cptwftype) && !"apply".equalsIgnoreCase(cptwftype) && !"applyuse".equalsIgnoreCase(cptwftype)) {
            rs.execute("update CptCapital set frozennum = 0 where isdata='2' and  frozennum > 0");
            CptWfUtil cwu = new CptWfUtil();
            cwu.DoFrozenCpt_new();
        } else if (formid == 18 || formid == 19 || formid == 201 || formid == 220 || formid == 221 || formid == 222 || formid == 224) {
            rs.execute("update CptCapital set frozennum = 0 where isdata='2' and  frozennum > 0");
            CptWfUtil cwu = new CptWfUtil();
            cwu.DoFrozenCpt_new();
        }
    }

    /**
     * request对象转成流程对象
     *
     * @param request
     * @return
     */
    public static ReqOperateRequestEntity request2Entity(HttpServletRequest request) {
        ReqOperateRequestEntity operateRequestEntity = new ReqOperateRequestEntity();
        //优先判断传的body格式是否是json类型
        try{
            String jsonStr = HttpServletRequestUtil.request2Json(request);
            if (StringUtils.isNotEmpty(jsonStr)){
                log.info("流程rest接口,调用参数如下:"+jsonStr);
                //如果jsonStr中包含requestid这个参数名，则将requestid后面的值改成int类型
                // 解析JSON字符串为JSONObject对象
                JSONObject jsonObject = JSONObject.parseObject(jsonStr);

                // 检查JSON对象中是否包含requestid参数
                if (jsonObject.containsKey("requestid")) {
                    // 将requestid参数值转换为整数类型
                    int requestId = Integer.parseInt(jsonObject.getString("requestid"));
                    jsonObject.put("requestid", requestId);
                }
                // 将修改后的JSONObject对象转换回字符串形式
                String modifiedJsonStr = jsonObject.toString();


                operateRequestEntity = HttpServletRequestUtil.json2Obj(modifiedJsonStr, ReqOperateRequestEntity.class);
                operateRequestEntity.setClientIp(request.getRemoteAddr());
                return operateRequestEntity;
            }
        }
        catch (Exception e){
            log.error("HttpServletRequestUtil调用异常!",e);
            e.printStackTrace();
        }


        try {
            int workflowId = Util.getIntValue(request.getParameter("workflowId"));
            int requestId = Util.getIntValue(request.getParameter("requestId"));
            String requestName = Util.null2String(request.getParameter("requestName"));
            int userId = Util.getIntValue(request.getParameter("userId"));
            int forwardFlag = Util.getIntValue(request.getParameter("forwardFlag"));
            String forwardResourceIds = Util.null2String(request.getParameter("forwardResourceIds"));
            String mainDataStr = Util.null2String(request.getParameter("mainData"));
            int isremind = Util.getIntValue(Util.null2String(request.getParameter("isremind")), 1);
            if (!"".equals(mainDataStr)) {
                try {
                    List <WorkflowRequestTableField> mainData = JSONObject.parseArray(mainDataStr, WorkflowRequestTableField.class);
                    operateRequestEntity.setMainData(mainData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String detailDataStr = Util.null2String(request.getParameter("detailData"));
            if (!"".equals(detailDataStr)) {
                try {
                    List <WorkflowDetailTableInfoEntity> detailData = JSONObject.parseArray(detailDataStr, WorkflowDetailTableInfoEntity.class);
                    operateRequestEntity.setDetailData(detailData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String remark = Util.null2String(request.getParameter("remark"));
            String requestLevel = Util.null2String(request.getParameter("requestLevel"));
            String otherParamStr = Util.null2String(request.getParameter("otherParams"));
            if (!"".equals(otherParamStr)) {
                try {
                    Map <String, Object> otherParams = JSONObject.parseObject(otherParamStr, Map.class);
                    operateRequestEntity.setOtherParams(otherParams);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            operateRequestEntity.setWorkflowId(workflowId);
            operateRequestEntity.setRequestId(requestId);
            operateRequestEntity.setRequestName(requestName);
            operateRequestEntity.setUserId(userId);
            operateRequestEntity.setRemark(remark);
            operateRequestEntity.setRequestLevel(requestLevel);
            operateRequestEntity.setForwardFlag(forwardFlag);
            operateRequestEntity.setForwardResourceIds(forwardResourceIds);
            operateRequestEntity.setClientIp(request.getRemoteAddr());
            operateRequestEntity.setIsremind(isremind);
            //干预参数
            int submitNodeId = Util.getIntValue(Util.null2String(request.getParameter("submitNodeId")));
            if (submitNodeId > 0 || FreeNodeBiz.isFreeNode(submitNodeId)) {
                operateRequestEntity.setSubmitNodeId(submitNodeId);
                operateRequestEntity.setEnableIntervenor("1".equals(Util.null2s(request.getParameter("enableIntervenor"), "1")));
                operateRequestEntity.setSignType(Util.getIntValue(Util.null2String(request.getParameter("SignType")), 0));
                operateRequestEntity.setIntervenorid(Util.null2String(request.getParameter("Intervenorid")));
            }
            log.info("流程rest接口,调用参数如下: workflowId:"+workflowId+"  requestId:"+requestId+"  requestName:"+requestName+"  userId:"+userId+"  forwardFlag:"+forwardFlag+"  forwardResourceIds:"+forwardResourceIds+"remark:"+remark+
                    "  submitNodeId:"+submitNodeId+"  mainDataStr:"+mainDataStr+"  detailDataStr:"+detailDataStr+"  otherParamStr:"+otherParamStr);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
        return operateRequestEntity;
    }

    /**
     * 获取流程附件上传目录
     *
     * @param workflowid
     * @return
     */
    public static Map <String, String> getFieldUploadDocCategory(int workflowid) {
        Map <String, String> fieldUploadDocCategory = new HashMap <>();
        RecordSet rs = new RecordSet();
        String docCategory_def = "";
        rs.executeQuery("select catelogtype,docCategory,selectedCateLog,limitvalue from workflow_base where id= ?", workflowid);
        if (rs.next()) {
            String catelogType_def = rs.getString("catelogType");
            if ("0".equals(catelogType_def)) {
                docCategory_def = rs.getString("docCategory");
                if ("".equals(docCategory_def) || ",,".equals(docCategory_def)) docCategory_def = "";
            }
        }
        fieldUploadDocCategory.put("docCategory_def", docCategory_def);
        rs.executeQuery("select catelogType,docCategory,fieldid from workflow_fileupload where workflowid= ?", workflowid);
        if (rs.next()) {
            int catelogType_temp = Util.getIntValue(rs.getString("catelogType"), 0);
            String fieldid = rs.getString("fieldid");
            if (catelogType_temp == 0) {
                String docCategory = rs.getString("docCategory");
                if ("".equals(docCategory) || ",,".equals(docCategory)) docCategory = "";
                fieldUploadDocCategory.put(fieldid, docCategory);
            }
        }
        return fieldUploadDocCategory;
    }

    /**
     * @param nodeid
     * @param reqEntity
     * @return
     */
    public static boolean judgeReqNode(int nodeid, RequestInfoEntity reqEntity) {
        NodeInfoEntity nodeEntity = com.engine.workflow.biz.workflowCore.WorkflowBaseBiz.getNodeInfo(nodeid);
        if (nodeEntity == null) return false;
        return FreeNodeBiz.isFreeNode(nodeid) ? Util.getIntValue(reqEntity.getRequestId()) == nodeEntity.getRequestid() : nodeEntity.getWorkflowid() == reqEntity.getWorkflowId();
    }

    /**
     * @param requestId
     * @param deleteKeys
     * @param DetailTable
     * @param mainTable
     * @return
     */
    public static boolean judgeDetailDelPer(int requestId, String deleteKeys, String DetailTable, String mainTable) {
        RecordSet rs = new RecordSet();
        try {
            if (deleteKeys != null) {
                String[] deleteArr = deleteKeys.split(",");
                String sql = "select 1 from " + DetailTable + " d," + mainTable + " m where m.id = d.mainid " +
                        "and m.requestid  = " + requestId + " and d.id in (" + deleteKeys + ")";
                rs.executeQuery(sql);
                if (rs.getCounts() < deleteArr.length) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean judgeDetailEditPer(int requestId, String recordOrder, String DetailTable, String mainTable) {
        RecordSet rs = new RecordSet();
        try {
            if (recordOrder != null && !recordOrder.equals("")) {
                String sql = "select 1 from " + DetailTable + " d," + mainTable + " m where m.id = d.mainid " +
                        "and m.requestid  = " + requestId + " and d.id in (" + recordOrder + ")";
                rs.executeQuery(sql);
                if (!rs.next()) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 后端校验表单必填，暂未处理显示属性联动
     *
     * @param reqEntity
     * @param
     * @return true 通过  false 有必填字段未必填
     */
    public static boolean judgeFormMustInput(RequestInfoEntity reqEntity, ReqFlowFailMsgEntity reqFailMsg) throws Exception{
        Map <String, TableInfo> nodeFormInfo = RequestFormBiz.getNodeFormInfo(reqEntity);
        boolean judgeFlag = true;
        if (nodeFormInfo == null) {
            log.info("");
            return judgeFlag;
        }
        int requestId = Util.getIntValue(reqEntity.getRequestId());
        RecordSet rs = new RecordSet();
        //校验主表是否必填
        TableInfo mainTableInfo = nodeFormInfo.get("main");
        Map <String, FieldInfo> fieldInfoMap = mainTableInfo.getFieldinfomap();
        int mainDataKeyId = -1;
        if (fieldInfoMap != null && fieldInfoMap.size() > 0) {
            Iterator <Map.Entry <String, FieldInfo>> it = fieldInfoMap.entrySet().iterator();
            List <String> fields = new ArrayList <>();
            Map <String, String> fieldMap = new HashMap <>();
            while (it.hasNext()) {
                Map.Entry <String, FieldInfo> item = it.next();
                String fieldId = item.getKey();
                FieldInfo fieldInfo = item.getValue();
                if(Util.getIntValue(fieldId) < 0) continue;
                if (fieldInfo.getViewattr() == 3) {
                    fields.add(fieldInfo.getFieldname());
                    fieldMap.put(fieldInfo.getFieldname(), fieldId);
                }
            }
            fields.add("id");
            if (fields.size() > 0) {
                rs.executeQuery("select " + StringUtils.join(fields, ",") + " from " + mainTableInfo.getTablename() + " where requestid = ?", requestId);
                while (rs.next()) {
                    mainDataKeyId = rs.getInt("id");
                    for (String fieldname : fields) {
                        String fieldvalue = rs.getString(fieldname);
                        if (fieldvalue == null || fieldvalue.length() == 0) {
                            judgeFlag = false;
                            String fieldId = fieldMap.get(fieldname);
                            reqFailMsg.setMsgType(ReqFlowFailMsgType.SAVE_MAIN_TABLE_FAIL);
                            Map <String, Object> keyParameters = reqFailMsg.getKeyParameters();
                            keyParameters.put("nodeid",reqEntity.getCurrentNodeId());
                            setFieldMustInputMsg(reqFailMsg.getMsgInfo(), mainTableInfo, fieldInfoMap.get(fieldId));
                            return judgeFlag;
                        }
                    }
                }
            }
        }
        String detailkeyfield = mainTableInfo.getDetailkeyfield();
        if("requestid".equalsIgnoreCase(detailkeyfield)) mainDataKeyId = requestId;
        //有明细表
        if (mainDataKeyId > 0 && nodeFormInfo.size() > 1) {
            Iterator<Map.Entry<String,TableInfo>> it = nodeFormInfo.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String,TableInfo> item = it.next();
                String key = item.getKey();
                if("main".equals(key)) continue;
                TableInfo detailTable = item.getValue();
                judgeFlag = judgeItemTableMustInpuf(rs,detailTable,detailkeyfield,mainDataKeyId,reqFailMsg);
                if(!judgeFlag) {
                    return judgeFlag;
                }
            }
        }
        return judgeFlag;
    }

    private static boolean judgeItemTableMustInpuf(RecordSet rs,TableInfo detailTable,String detailkeyfield,int mainDataKeyId,ReqFlowFailMsgEntity reqFailMsg){
        Map <String, FieldInfo> fieldInfoMap = detailTable.getFieldinfomap();
        Iterator <Map.Entry <String, FieldInfo>> it = fieldInfoMap.entrySet().iterator();
        List <String> fields = new ArrayList <>();
        Map <String, String> fieldMap = new HashMap <>();
        while (it.hasNext()) {
            Map.Entry <String, FieldInfo> item = it.next();
            String fieldId = item.getKey();
            FieldInfo fieldInfo = item.getValue();
            if(Util.getIntValue(fieldId) < 0) continue;
            if (fieldInfo.getViewattr() == 3) {
                fields.add(fieldInfo.getFieldname());
                fieldMap.put(fieldInfo.getFieldname(), fieldId);
            }
        }
        fields.add(detailkeyfield);
        if (fields.size() > 0) {
            rs.executeQuery("select " + StringUtils.join(fields, ",") + " from " + detailTable.getTablename() + " where "+detailkeyfield+" = ?", mainDataKeyId);
            if(detailTable.getDetailtableattr() != null) {
                if(detailTable.getDetailtableattr().getIsneed() == 1 && rs.getCounts() == 0)  {
                    reqFailMsg.setMsgType(ReqFlowFailMsgType.JUDGE_MUST_INPUT);
                    Map <String, Object> keyParameters = reqFailMsg.getKeyParameters();
                    keyParameters.put("mainDataKeyId",detailTable.getTablename());
                    keyParameters.put("detail_need_add",detailTable.getDetailtableattr().getIsneed());
                    reqFailMsg.getMsgInfo().put("tablename",detailTable.getTablename());
                    reqFailMsg.getMsgInfo().put("count",0);
                    return false;
                }
            }
            while (rs.next()) {
                mainDataKeyId = rs.getInt("id");
                for (String fieldname : fields) {
                    String fieldvalue = rs.getString(fieldname);
                    if (fieldvalue == null || fieldvalue.length() == 0) {
                        String fieldId = fieldMap.get(fieldname);
                        reqFailMsg.setMsgType(ReqFlowFailMsgType.JUDGE_MUST_INPUT);
                        setFieldMustInputMsg(reqFailMsg.getMsgInfo(), detailTable, fieldInfoMap.get(fieldId));
                        return false;
                    }
                }
            }
        }
        return true;
    }


    private static void setFieldMustInputMsg(Map <String, Object> resultMsg, TableInfo mainTableInfo, FieldInfo fieldInfo) {
        resultMsg.put("tablename", mainTableInfo.getTablename());
        resultMsg.put("tableindex",mainTableInfo.getTableindex());
        resultMsg.put("fieldId", fieldInfo.getFieldid());
        resultMsg.put("fieldname", fieldInfo.getFieldname());
        resultMsg.put("fieldlable",fieldInfo.getFieldlabel());
    }

    /**
     * 检查当前记录是否处于意见征询出去的状态
     *
     * @param id
     * @return
     */
    public static Integer checkIsTakOut(int requestid, int id, Integer num) {
        RecordSet rs = new RecordSet();
        rs.executeQuery("select id,isremark from workflow_currentoperator where requestid = ? and takid = ? and takisremark = '2'", requestid, id);
        while (rs.next()) {
            int tempId = rs.getInt(1);
            String tempIsremark = rs.getString(2);
            if ("1".equals(tempIsremark)) {//存在未回复的记录
                num++;
            } else {
                num = checkIsTakOut(requestid, tempId, num);
            }
        }
        return num;
    }
}


