///*
// *
// * Copyright (c) 2001-2016 泛微软件.
// * 泛微协同商务系统,版权所有.
// *
// */
//package weaver.workflow.workflow;
//
//import java.util.List;
//
//import weaver.conn.RecordSet;
//import weaver.conn.RecordSetTrans;
//import weaver.fna.general.FnaCommon;
//import weaver.general.BaseBean;
//import weaver.general.Util;
//import weaver.hrm.User;
//import weaver.hrm.attendance.manager.HrmAttVacationManager;
//import weaver.interfaces.erp.PostWorkflowInfo;
//import weaver.workflow.msg.PoppupRemindInfoUtil;
//import weaver.workflow.request.RequestAddShareInfo;
//import weaver.workflow.request.RequestOperationLogManager;
//import weaver.workflow.request.WFUrgerManager;
//import weaver.workflow.request.RequestOperationLogManager.OperateLogBean;
//import weaver.workflow.request.RequestOperationLogManager.OperateOtherInfoBean;
//import weaver.workflow.request.RequestOperationLogManager.RequestOperateEntityTableNameEnum;
//
///**
// * @version 1.0
// * @author wcc 改造后的强制收回类
// */
//public class RequestForceDrawBack extends BaseBean {
//
//    private HrmAttVacationManager manager = new HrmAttVacationManager();
//
//    public static final int OLDDATA = 0;
//
//    public static final int FAIL = -1;
//
//    public static final int SUCCESS = 1;
//
//    public static boolean isHavePurview(int userid, int requestid, boolean isAdminOperate) {
//        return isHavePurview(null, userid, requestid, isAdminOperate);
//    }
//
//    public static boolean isHavePurview(OperateLogBean olb, int userid, int requestid, boolean isAdminOperate) {
//        RecordSet rs = new RecordSet();
//
//        if (olb == null) {
//            RequestOperationLogManager rolm = new RequestOperationLogManager(requestid);
//            olb = rolm.getLastOperateLog(false);
//            if (olb == null) {
//                //老数据不增加新的验证
//                return true;
//            }
//        }
//
//        if (!isAdminOperate) {
//            if (olb.getOperatorId() != userid) {
//                return false;
//            }
//        }
//
//        List<OperateOtherInfoBean> ooibs = olb.getOperateOtherInfos();
//
//        if (ooibs == null) {
//            return true;
//        }
//
//        for (OperateOtherInfoBean ooib : ooibs) {
//            int entityType = ooib.getEntitytype();
//            int entityid = ooib.getEntityid();
//            int count = ooib.getCount();
//
//            if (entityType == RequestOperateEntityTableNameEnum.CURRENTOPERATOR.getId()) {
//                String sql = "select id from workflow_currentoperator where requestid=" + requestid + " order by id desc";
//                rs.executeSql(sql);
//                rs.next();
//
//                int rowCount = rs.getCounts();
////                int rowId = rs.getInt(1);
//
//                if (rowCount != count) {
//                    sql = "select isremark, preisremark, takisremark from workflow_currentoperator where requestid=" + requestid + " and id>" + entityid;
//                    rs.executeSql(sql);
//                    while (rs.next()) {
//                        int isremark = Util.getIntValue(rs.getString(1));
//                        int preisremark = Util.getIntValue(rs.getString(2));
//                        int takisremark = Util.getIntValue(rs.getString(3));
//                        //转发不影响强制收回， 这里添加takisremark判断，是为了将意见征询排除在外， 因为它会影响强制收回
//                        //被转发的记录， 不是意见征询， 不影响强制收回
//                        if (isremark == 1 && takisremark != 2) {
//                            continue;
//                        }
//                        //已操作的转发的记录， 不是意见征询， 不影响强制收回
//                        if ((isremark == 2 || isremark == 4) && preisremark == 1 && takisremark != 2) {
//                            continue;
//                        }
//
//                        //说明日志记录之后， workflow_currentoperator表有操作过， 且不是转发
//                        return false;
//
//                    }
//                }
//            } else if (entityType == RequestOperateEntityTableNameEnum.REQUESTLOG.getId()) {
//                String sql = "select logid from workflow_requestlog where requestid=" + requestid + " order by logid desc";
//                rs.executeSql(sql);
//                rs.next();
//
//                int rowCount = rs.getCounts();
////                int rowId = rs.getInt(1);
//
//                if (rowCount != count) {
//                    /**
//                     * 0：批准
//                     * <br>1：保存
//                     * <br>2：提交
//                     * <br>3：退回
//                     * <br>4：重新打开
//                     * <br>5：删除
//                     * <br>6：激活
//                     * <br>7：转发
//                     * <br>9：批注
//                     * <br>a：意见征询
//                     * <br>b：意见征询回复
//                     * <br>e：强制归档
//                     * <br>h：转办
//                     * <br>i：干预
//                     * <br>j：转办反馈
//                     * <br>s：督办
//                     * <br>t：抄送
//                     */
//                    sql = "select logtype from workflow_requestlog where requestid=" + requestid + " and logid>" + entityid + " and logtype in ('0', '2', '3', '4', '6', 'a', 'b', 'e',  'h', 'i', 'j')" ;
//                    rs.executeSql(sql);
//                    if (rs.next()) {
//                        return false;
//                    }
//                }
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 强制收回
//     *
//     * @param user 操作对象
//     * @param requestids 操作的流程ID集合
//     * @param isAdminOperate 是否是管理员操作
//     */
//    public void foreceDrawBack(User user, List<String> requestids, boolean isAdminOperate) {
//        for (int i = 0; i < requestids.size(); i++) {
//            // 执行强制收回动作
//            foreceDrawBack(user, Util.getIntValue(requestids.get(i)), isAdminOperate, -1, -1);
//        }
//    }
//
//    /**
//     * 强制收回
//     *
//     * @param user 操作对象
//     * @param requestid 操作的流程ID
//     * @param isAdminOperate 是否是管理员操作
//     */
//    public int foreceDrawBack(User user, int requestid, boolean isAdminOperate, int ouesrid, int ousertype) {
//
//        PoppupRemindInfoUtil poppupRemindInfoUtil = new PoppupRemindInfoUtil();
//        RecordSet rs = new RecordSet();
//        RecordSetTrans rst = new RecordSetTrans();
//        rst.setAutoCommit(false);
//        // 初始化操作记录对象
//        RequestOperationLogManager rolm = new RequestOperationLogManager(requestid);
//        OperateLogBean olb = rolm.getLastOperateLog();
//
//        if (olb == null) {
//            return OLDDATA;
//        }
//
//        // 验证是否可强制收回
//        if (!isHavePurview(olb, user.getUID(), requestid, isAdminOperate)) {
//            return FAIL;
//        }
//
//        //收回前查询：流程是否已经归档
//        boolean isover = false;
//        //收回前的当前节点ID
//        int oldcurrentNodeid = 0;
//        rs.executeProc("workflow_Requestbase_SByID", requestid + "");
//        if (rs.next()) {
//            if (rs.getInt("currentnodetype") == 3) {
//                isover = true;
//            }
//            oldcurrentNodeid = rs.getInt("currentnodeid");
//        }
//
//        String todoDeleteUserids = "";
//        // 获取操作产生的记录
//        try {
//            // 获取操作产生的人员记录，并删除这些人产生的共享信息
//            String operators = "0";
//            String curNodeIds = "0";
//            String entityids = olb.getNewRecordEntityIds(RequestOperateEntityTableNameEnum.CURRENTOPERATOR);
//            todoDeleteUserids = entityids;
//            String curoptSql = "select userid, usertype, nodeid, groupid, groupdetailid, wfreminduser, wfusertypes from workflow_currentoperator where requestid=" + requestid + " and "+Util.getSubINClause(entityids,"id","in");
//            rs.executeSql(curoptSql);
//            while (rs.next()) {
//                // 更新提醒信息
//                poppupRemindInfoUtil.updatePoppupRemindInfo(rs.getInt("usreid"), rs.getInt("usertype"), String.valueOf(rs.getInt("usertype") + 1), requestid);
//                // 删除超时提醒
//                List<String> wfremindusers = Util.TokenizerString(Util.null2String(rs.getString("wfreminduser")), ",");
//                List<String> wfusertypes = Util.TokenizerString(Util.null2String(rs.getString("wfusertypes")), ",");
//                for (int k = 0; k < wfremindusers.size(); k++) {
//                    poppupRemindInfoUtil.updatePoppupRemindInfo(Util.getIntValue((String) wfremindusers.get(k)), 10, (String) wfusertypes.get(k), requestid);
//                }
//
//                // 组装操作者人员id
//                operators += "," + Util.null2String(rs.getString("userid"));
//                // 组装节点ID
//                if (("," + curNodeIds + ",").indexOf("," + Util.null2String(rs.getString("nodeid") + ",")) == -1) {
//                    curNodeIds += "," + Util.null2String(rs.getString("nodeid"));
//                }
//            }
//            // 删除操作产生的共享信息
//            rst.executeSql(" delete Workflow_SharedScope where requestid = " + requestid + " and (" + Util.getSubINClause(operators,"operator","in") + ") and currentnodeid in ( " + curNodeIds + ")");
//
//            // 删除操作产生的workflow_currentoperator新纪录
//            rst.executeSql("delete workflow_currentoperator where requestid=" + requestid + " and "+Util.getSubINClause(entityids,"id","in"));
//
//            //删除操作产生的workflow_requestlog新纪录
//            entityids = olb.getNewRecordEntityIds(RequestOperateEntityTableNameEnum.REQUESTLOG);
//            String updateReqlogids = "0";
//            rst.executeSql("select logid from workflow_requestlog where (logtype='0' or logtype='2' or logtype='3') and requestid=" + requestid + " and logid in (" + entityids + ")");
//            while (rst.next()) {
//                updateReqlogids += "," + rst.getString(1);
//            }
//            rst.executeSql("update workflow_requestlog set logtype='1' where requestid=" + requestid + " and logid in (" + updateReqlogids + ")");
//            rst.executeSql("delete workflow_requestlog where requestid=" + requestid + " and logid in (" + entityids + ") and logid not in (" + updateReqlogids + ")");
//
//            // 删除新产生的workflow_agentpersons新纪录
//            entityids = olb.getNewRecordEntityIds(RequestOperateEntityTableNameEnum.AGENTPERSONS);
//            rst.executeSql("delete workflow_agentpersons where requestid=" + requestid + " and groupdetailid in (" + entityids + ")");
//
//            // -------------------------------------------
//            // 恢复操作修改记录
//            // start
//            // -------------------------------------------
//            // 获取workflow_currentoperator还原sql语句列表
//            List<String> updateSqllst = rolm.getUpdateSqlByModifyLog(olb, RequestOperateEntityTableNameEnum.CURRENTOPERATOR);
//            // 获取workflow_requestlog还原sql语句列表
//            updateSqllst.addAll(rolm.getUpdateSqlByModifyLog(olb, RequestOperateEntityTableNameEnum.REQUESTLOG));
//            // 获取workflow_agentpersons还原sql语句列表
//            updateSqllst.addAll(rolm.getUpdateSqlByModifyLog(olb, RequestOperateEntityTableNameEnum.AGENTPERSONS));
//            // 获取workflow_requestbase还原sql语句列表
//            updateSqllst.addAll(rolm.getUpdateSqlByModifyLog(olb, RequestOperateEntityTableNameEnum.REQUESTBASE));
//
//            // 开始执行恢复sql语句
//            // 恢复操作修改的workflow_currentoperator记录
//            // 恢复操作修改的workflow_requestlog记录
//            // 恢复操作修改的workflow_agentpersons记录
//            // 恢复操作修改的workflow_requestbase记录
//            for (String sqlstr : updateSqllst) {
//                rst.executeSql(sqlstr.toString());
//            }
//
//            //-----------------------------------------------
//            // 更新workflow_nownode表数据start
//            //-----------------------------------------------
//            //rst.executeSql("select a.nodetype, b.nodeattribute from workflow_flownode a inner join workflow_nodebase b on a.nodeid=b.id where nodeid=" + olb.getNodeId());
//            rst.executeSql("delete from workflow_nownode where nownodeid =" + oldcurrentNodeid + " and requestid=" + requestid);
//            rst.executeSql("select a.nodetype, b.nodeattribute, a.nodeid from workflow_flownode a inner join workflow_nodebase b on a.nodeid=b.id where nodeid in (select nodeid from workflow_currentoperator where requestid=" + requestid + " and isremark=0 group by nodeid)");
//            while (rst.next()) {
//                String insertNowNodeSql ="insert into workflow_nownode(requestid,nownodeid,nownodetype,nownodeattribute) " +
//                		"values(" + requestid + "," + rst.getInt("nodeid") + "," + rst.getInt("nodetype") + "," + rst.getInt("nodeattribute") + ")";
//                rst.executeSql("delete from workflow_nownode where nownodeid =" + rst.getInt("nodeid") + " and requestid=" + requestid);
//                rst.executeSql(insertNowNodeSql);
//            }
//            //-----------------------------------------------
//            // 更新workflow_nownode表数据end
//            //-----------------------------------------------
//
//            // -------------------------------------------
//            // 恢复操作修改记录
//            // end
//            // -------------------------------------------
//
//            // 重置完成， 添加强制收回日志
//            // TODO
//            // 操作记录置为无效
//            // TODO
//            rolm.setOperateInvalid(user, olb.getId());
//
//            // 删除操作详细日志记录
//            // TODO
//            // 完成，提交操作
//
//            // 删除自动批准日志
//            rst.executeUpdate("delete from workflow_approvelog where requestid = ?",requestid);
//            rst.commit();
//        } catch (Exception e) {
//            rst.rollback();
//            this.writeLog(e);
//            return FAIL;
//        }
//
//        // -------------------
//        // 重新共享
//        // start
//        // -------------------
//        rs.executeProc("workflow_Requestbase_SByID", requestid + "");
//        int workflowid = 0;
//        int currNodeType = 0;
//        int currentNodeid = 0;
//        int isbill = 1;
//        int formid = 0;
//        String billtablename = "";
//        // 查询workflowid
//        if (rs.next()) {
//            workflowid = rs.getInt("workflowid");
//            currentNodeid = rs.getInt("currentnodeid");
//            currNodeType = rs.getInt("currentnodetype");
//        }
//        rs.executeSql("select * from workflow_base where id=" + workflowid);
//        if (rst.next()) {
//            isbill = rs.getInt("isbill");
//            formid = rs.getInt("formid");
//
//        }
//        if (isbill == 1) {
//            rs.executeSql("select tablename from workflow_bill where id = " + formid); // 查询工作流单据表的信息
//            if (rst.next())
//                billtablename = rs.getString("tablename"); // 获得单据的主表
//        }
//        try {
//            RequestAddShareInfo shareinfo = new RequestAddShareInfo();
//            shareinfo.setRequestid(requestid);
//            shareinfo.SetWorkFlowID(workflowid);
//            shareinfo.SetNowNodeID(oldcurrentNodeid);
//            shareinfo.SetNextNodeID(currentNodeid);
//            shareinfo.setIsbill(isbill);
//            User usernew = new User();
//            usernew.setUid(-1);
//            usernew.setLogintype("1");
//            shareinfo.setUser(usernew);
//            shareinfo.SetIsWorkFlow(1);
//            shareinfo.setBillTableName(billtablename);
//            shareinfo.setHaspassnode(true);
//            shareinfo.addShareInfo();
//        } catch (Exception e9) {
//        }
//
//        // -------------------
//        // 重新共享
//        // end
//        // -------------------
//
//        // 其他操作
//        // TODO\
//
//
//        //子流程归档汇总标记撤销
//        rs.executeSql(" update workflow_requestbase set dataaggregated = '' where requestid = " + requestid);
//
//        try {
//            CapitalUnfreeze(String.valueOf(requestid), currNodeType, currentNodeid);
//        } catch (Exception e9) {
//            writeLog(e9);
//        }
//
//        // 财务相关
//        if (currNodeType == 0) {
//            manager.handle(requestid, workflowid, 0);
//            try {
//                FnaCommon fnaCommon = new FnaCommon();
//                fnaCommon.doWfForceOver(requestid, 0, true);
//            } catch (Exception easi) {
//                new BaseBean().writeLog(easi);
//            }
//        }
//
//        //归档节点收回重算督办
//        if (isover) {
//            WFUrgerManager wfurgerMgr = new WFUrgerManager();
//            wfurgerMgr.insertUrgerByRequestid(requestid);
//        }
//
//        // 统一待办提醒
//        try {
//	    	PostWorkflowInfo postWorkflowInfo = new PostWorkflowInfo();
//	    	postWorkflowInfo.drawBackWF(requestid + "");
//	    	writeLog("==========统一待办(RequestForceDrawBack.java --> drawBackWF)==========");
//        } catch(Exception e) {
//        	writeLog(e);
//        }
//
//        return SUCCESS;
//    }
//
//    // 如果是资产领用流程，流程收回到创建节点，冻结的资产数量要解冻
//    public void CapitalUnfreeze(String requestid, int currNodeType, int nodetype_rb) {
//        RecordSet reSet1 = new RecordSet();
//        RecordSet reSet2 = new RecordSet();
//        reSet1.executeSql(" select r.requestid,r.workflowid,r.currentnodetype from workflow_requestbase r,workflow_base b where requestid = " + requestid + " and r.workflowid=b.id and b.formid=19 and b.isbill=1");
//        while (reSet1.next()) {
//            String sql = " select b.* from workflow_form w,bill_CptFetchDetail b where w.requestid =" + requestid + " and w.billid=b.cptfetchid";
//            reSet2.executeSql(sql);
//            RecordSet reSet3 = new RecordSet();
//            while (reSet2.next()) {
//                String capitalid = reSet2.getString("capitalid");
//                float old_number_n = 0.0f;
//                float old_frozennum = 0.0f;
//                float new_frozennum = 0.0f;
//                reSet3.executeSql("select number_n as old_number_n from bill_CptFetchDetail where cptfetchid = (select id from bill_CptFetchMain where requestid=" + requestid + ") and capitalid=" + capitalid);
//                if (reSet3.next())
//                    old_number_n = reSet3.getFloat("old_number_n");
//                reSet3.executeSql("select frozennum as old_frozennum from CptCapital where id=" + capitalid);
//                if (reSet3.next())
//                    old_frozennum = reSet3.getFloat("old_frozennum");
//                if (nodetype_rb == 0 && (currNodeType == 1 || currNodeType == 2)) {// 中间节点收回到创建节点
//                    new_frozennum = old_frozennum - old_number_n;
//                    reSet3.executeSql("update CptCapital set frozennum=" + new_frozennum + " where id=" + capitalid);
//                } else if (currNodeType == 0 && (nodetype_rb == 1 || nodetype_rb == 2)) {// 创建节点收回到中间节点，收回的是退回操作
//                    new_frozennum = old_frozennum + old_number_n;
//                    reSet3.executeSql("update CptCapital set frozennum=" + new_frozennum + " where id=" + capitalid);
//                }
//            }
//        }
//    }
//}
