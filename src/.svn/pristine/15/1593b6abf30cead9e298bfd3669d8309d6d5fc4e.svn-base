///*
//*
//* Copyright (c) 2001-2006 泛微软件.
//* 泛微协同商务系统,版权所有.
//*
//*/
//package weaver.workflow.workflow;
//
//import java.util.ArrayList;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import weaver.common.StringUtil;
//import weaver.conn.RecordSet;
//import weaver.fna.general.FnaCommon;
//import weaver.general.BaseBean;
//import weaver.general.TimeUtil;
//import weaver.general.Util;
//import weaver.hrm.HrmUserVarify;
//import weaver.hrm.User;
//import weaver.hrm.attendance.manager.HrmAttVacationManager;
//import weaver.interfaces.erp.PostWorkflowInfo;
//import weaver.systeminfo.SystemEnv;
//import weaver.workflow.msg.PoppupRemindInfoUtil;
//import weaver.workflow.request.FlowExceptionHandle;
//import weaver.workflow.request.RequestAddShareInfo;
//import weaver.workflow.request.RequestComInfo;
//import weaver.workflow.request.RequestNodeFlow;
//import weaver.workflow.request.WFLinkInfo;
//import weaver.workflow.request.WFPathUtil;
///**
// * @version 1.0
// * @author xwj
// * 流程强制收回
// */
//public class WfForceDrawBack extends BaseBean {
//    RequestComInfo requestcominfo;
//    public WfForceDrawBack() {
//        try{
//            requestcominfo = new RequestComInfo();
//        }catch(Exception e){
//            writeLog(e);
//        }
//    }
//
//    /**
//    *
//    * 流程强制收回(单个或批量)
//    * @param requestids  要收回的流程
//    * @param request  输入
//    * @param response  输出
//    * @param userid1  当前操作人
//    * @param usertype1  当前操作人类型
//    */
//    public void doForceDrawBack(
//        ArrayList requestids,
//        HttpServletRequest request,
//        HttpServletResponse response,
//        int userid1,
//        int usertype1) {
//
//        RecordSet rs = new RecordSet();
//        RecordSet rs1 = new RecordSet();
//        RecordSet rs2 = new RecordSet();
//        RecordSet rs3 = new RecordSet();
//        RecordSet rs4= new RecordSet();
//        String requestid = "";
//        User user = HrmUserVarify.getUser(request, response);
//        int currNodeId = -1;
//        int currNodeType = -1;
//        String billtablename = "";
//        int formid = -1;
//        int isbill = -1;
//        int billid = -1;
//        int wfid = -1;
//        int creater = -1;
//        int creatertype = -1;
//        int nodeid_rb = -1;
//        int nodetype_rb = -1;
//        int groupid = -1;
//        int passedgroups=0;
//        int groupdetailid=0;
//        int userid = user.getUID();
//        PoppupRemindInfoUtil poppupRemindInfoUtil = new PoppupRemindInfoUtil();
//        int usertype = (user.getLogintype().equals("1")) ? 0 : 1;
//        if (userid1 != -1) {
//            userid = userid1;
//            usertype = usertype1;
//        }
//        String sql = "";
//        for (int i = 0; i < requestids.size(); i++) {
//            requestid = (String) requestids.get(i);
//            rs.executeProc("workflow_Requestbase_SByID", requestid + "");
//
//            // 统一待办提醒
//            PostWorkflowInfo postWorkflowInfo = new PostWorkflowInfo();
//            postWorkflowInfo.deleteToDo(requestid);
//            writeLog("==========统一待办(WfForceDrawBack.java --> deleteToDo)==========");
//
//            //当前节点
//            if (rs.next()) {
//                currNodeId = rs.getInt("currentnodeid");
//                wfid = rs.getInt("workflowid");
//
//                creater = Util.getIntValue(rs.getString("creater"), 0);
//                creatertype = Util.getIntValue(rs.getString("creatertype"), 0);
//                currNodeType =
//                    Util.getIntValue(rs.getString("currentnodetype"), 0);
//            }
//
//            //子流程归档汇总标记撤销
//            rs.executeSql(" update workflow_requestbase set dataaggregated = '' where requestid = " + requestid);
//
//            boolean isover = false ;
//            rs.executeSql("select 1 from workflow_requestlog where (logtype='e' or logtype='i') and requestid="+requestid);
//            if(rs.getCounts()>0){
//                isover = true ;
//            }
//             //收回人所在节点(改成如果是流程监控人员，找到最后一个操作者节点，只可以从最后一个操作人收回
//            if (userid1 != -1) {
//                //从日志中去找最后一个操作者
//                rs.executeSql("select max(logid), operator,operatortype from workflow_requestlog where requestid="+requestid+" and (logtype='2' or logtype='0' or logtype='3') and exists(select 1 from workflow_currentoperator where requestid=workflow_requestlog.requestid and userid=workflow_requestlog.operator and usertype=workflow_requestlog.operatortype and isremark='2' and preisremark='0' and operatedate is not null and operatedate>' ') group by operator,operatortype order by max(logid) desc");
//                if(rs.next()&&!isover){
//                    userid=Util.getIntValue(rs.getString("operator"));
//                    usertype=Util.getIntValue(rs.getString("operatortype"),0);
//                }else{
//                    sql = "select userid,usertype,nodeid,groupid,groupdetailid from workflow_currentoperator where requestid = "
//                        + requestid
//                        + " and isremark = '2' and preisremark = '0' "//最后一个操作者应该是真正的节点操作者，不是转发提交或抄送提交
//                        + " and operatedate is not null and operatedate>' '"
//                        + " order by operatedate desc ,operatetime desc ";
//                    if(isover){
//                        sql = "select userid,usertype,nodeid,groupid,groupdetailid from workflow_currentoperator where requestid = "
//                            + requestid
//                            + " and isremark = '2' and preisremark = '0' "//最后一个操作者应该是真正的节点操作者，不是转发提交或抄送提交
//                            //+ " and operatedate is not null and operatedate>' '"
//                            + " order by id desc ";
//                    }
//                    rs.executeSql(sql);
//                    if(rs.next()){
//                        userid=Util.getIntValue(rs.getString("userid"));
//                        usertype=Util.getIntValue(rs.getString("usertype"),0);
//                    }
//                }
//            }
//
//                if("ORACLE".equalsIgnoreCase(rs.getDBType())){
//                    sql = "select userid,usertype,nodeid,groupid,groupdetailid from workflow_currentoperator where requestid = "
//                        + requestid
//                        + "and isremark = '2' and (preisremark = '0' or preisremark = '7') "//最后一个操作者应该是真正的节点操作者，不是转发提交或抄送提交
//                        +" and userid=" +
//                        +userid
//                        +" and usertype="+
//                        + usertype
//                        + " and operatedate is not null "
//                        + " order by operatedate desc ,operatetime desc ";
//                }else{
//                    sql = "select userid,usertype,nodeid,groupid,groupdetailid from workflow_currentoperator where requestid = "
//                                + requestid
//                                +" and isremark = '2' and (preisremark = '0' or preisremark = '7') "//最后一个操作者应该是真正的节点操作者，不是转发提交或抄送提交
//                                +" and userid=" +
//                                +userid
//                                +" and usertype="+
//                                + usertype
//                                + " and (operatedate is not null and operatedate<>'')"
//                                + " order by operatedate desc ,operatetime desc ";
//                }
//
//                if(isover){
//                    sql = "select userid,usertype,nodeid,groupid,groupdetailid from workflow_currentoperator where requestid = "
//                        + requestid
//                        +" and isremark = '2' and (preisremark = '0' or preisremark = '7') "//最后一个操作者应该是真正的节点操作者，不是转发提交或抄送提交
//                        +" and userid=" +
//                        +userid
//                        +" and usertype="+
//                        + usertype
//                        //+ " and (operatedate is not null and operatedate<>'')"
//                        + " order by id desc ";
//                }
//
//                rs.executeSql(sql);
//            if (rs.next()) {
//                nodeid_rb = rs.getInt("nodeid");
//                groupid = rs.getInt("groupid");
//                userid=rs.getInt("userid");
//                usertype=rs.getInt("usertype");
//                groupdetailid=rs.getInt("groupdetailid");
//            }
//
//            rs.executeSql("select nodetype from workflow_flownode where workflowid = "+wfid+" and nodeid = " + nodeid_rb);
//            if (rs.next()) {
//                nodetype_rb = rs.getInt("nodetype");
//            }
//            //System.out.println("nodeid_rb =" + nodeid_rb+" nodetype_rb="+nodetype_rb+" groupid="+groupid);
//            //System.out.println("nodetype_rb =" + nodetype_rb);
//            //System.out.println("groupid =" + groupid);
//            //更新workflow_requestlog 去掉最后一次提交的日志记录(无论流程收回者是否在当前节点,忽略批注)
//            sql =
//                //"select * from workflow_requestlog where logtype!='7' and requestid = "
//                //忽略抄送，modify by myq 2008.3.18       (忽略保存,TD17000)
//                "select * from workflow_requestlog where logtype!='7' and logtype!='9' and logtype!='1' and logtype!='a' and requestid = "
//                    + requestid
//                    + "  order by operatedate desc ,operatetime desc ";
//					//System.out.println("---1----requestlog---sql>>>>"+sql);
//            rs.executeSql(sql);
//            if (rs.next()) {
//                sql =
//                    "update workflow_requestlog set logtype='1' where ((logtype!='0' and logtype!='2' and logtype!='3') or (logid in(select max(logid) from workflow_requestlog where (logtype='0' or logtype='2' or logtype='3') and requestid="
//                        + requestid
//                        + " and nodeid = "
//                        + rs.getString("nodeid")
//                        + " and operator = "
//                        + rs.getString("operator")
//                        + " and operatortype = "
//                        + rs.getString("operatortype")
//                        + " and destnodeid = "
//                        + rs.getString("destnodeid")
//                        + "))) and requestid = "
//                        + requestid
//                        + " and nodeid = "
//                        + rs.getString("nodeid")
//                        + " and operator = "
//                        + rs.getString("operator")
//                        + " and operatortype = "
//                        + rs.getString("operatortype")
//                        + " and destnodeid = "
//                        + rs.getString("destnodeid");
//						//System.out.println("---2----requestlog---sql--->>>>"+sql);
//                rs1.executeSql(sql);
//            }
//
//            String preloguserid = "";
//            String prelogusertype = "";
//            String preoperatedate = "";
//            String preoperatetime = "";
//            String prepassedgroups = "";
//            String pretotalgroups = "";
//            String prelastnodeid = "";
//            String prelastnodetype = "";
//            String precurrnodeid = "";
//            String precurrnodetype = "";
//            String sqls="";
//            String prestatus = SystemEnv.getHtmlLabelName(18359, user.getLanguage());
//            String pregroupid = "";
//
//
//            //流程强制收回时删除对应节点设置的流程共享
//            //--signorder 0 非会签、 1会签 、 2依次逐个会签。表现形式，在workflow_currentoperator表中isremark都为0，且nodeid为currentnodeid
//            String wfsharecurrentnodeid = "";
//            String wfsharesignorder = "";
//            int wfsharetype = 0;
//            rs.executeSql("select currentnodeid from workflow_requestbase where requestid = "+ requestid);
//            while(rs.next()){
//                wfsharecurrentnodeid = Util.null2String(rs.getString("currentnodeid"));
//            }
//            String operators = "";
//            String nowoperator = "";
//            rs.executeSql("select userid from workflow_currentoperator where requestid = "+ requestid +" and nodeid = " +wfsharecurrentnodeid + " and isremark = '0' ");
//            while(rs.next()){
//                nowoperator = Util.null2String(rs.getString("userid"));
//                if("".equals(operators)){
//                    operators = nowoperator;
//                }else{
//                    operators += "," + nowoperator;
//                }
//            }
//            ////////////////////////////////
//            if (nodeid_rb == currNodeId) {//如果收回者在当前节点，则判断是不是非依次逐个会签，
//                rs.execute("select * from workflow_nodegroup where nodeid="+currNodeId);
//                while (rs.next())
//                {
//                    sqls="select * from workflow_groupdetail where groupid = " +rs.getString("id") ;
//                    rs1.execute(sqls);
//                    while (rs1.next())
//                    {
//                        wfsharetype=rs1.getInt("type");
//                        wfsharesignorder = Util.null2String(rs1.getInt("signorder"));
//                    }
//                }
//                if(!operators.trim().equals("")){
//                    ////////////////////////////////
//                    String deletewfsharesql = " delete Workflow_SharedScope where requestid = " + requestid +
//                                                " and operator in( "+ operators+") and currentnodeid = " + wfsharecurrentnodeid;
//                    //删除Workflow_SharedScope
//                    if(!((wfsharetype==5 || wfsharetype==50 || wfsharetype==42 || wfsharetype==51 ||wfsharetype == 6
//                            ||wfsharetype == 31||wfsharetype==32||wfsharetype==7||wfsharetype==38||wfsharetype == 40||wfsharetype == 41||wfsharetype == 17||wfsharetype == 18||wfsharetype == 36
//                            ||wfsharetype == 37||wfsharetype == 19||wfsharetype == 39||wfsharetype==15||wfsharetype==8||wfsharetype==33||wfsharetype==9||wfsharetype==10||wfsharetype==47||wfsharetype==34||wfsharetype==11||wfsharetype==12||wfsharetype==48||wfsharetype==13||wfsharetype==35||wfsharetype==14||wfsharetype==44||wfsharetype==45||wfsharetype==46||wfsharetype==16||wfsharetype==43||wfsharetype==49) && "1".equals(wfsharesignorder))){
//                        rs2.executeSql(deletewfsharesql);
//                    }
//                }
//            }else{//如果收回者不在当前节点，直接收回上一节点共享
//                ////////////////////////////////
//                if(!operators.trim().equals("")){
//                    String deletewfsharesql = " delete Workflow_SharedScope where requestid = " + requestid +
//                                                " and operator in( "+ operators+") and currentnodeid = " + wfsharecurrentnodeid;
//                    //删除Workflow_SharedScope
//                    rs2.executeSql(deletewfsharesql);
//                }
//            }
//            ///////////////end
//
//            if (nodeid_rb == currNodeId) { //收回操作者在当前节点
//                //自由节点相关信息，用于自由节点依次逐个处理强制收回
//                int isfreenode = -1;
//                String freeOperators = "";
//                rs.executeSql("SELECT IsFreeNode, operators FROM workflow_nodebase WHERE id=" + nodeid_rb);
//                if (rs.next()) {
//                    isfreenode = Util.getIntValue(Util.null2String(rs.getString("IsFreeNode")));
//                    freeOperators = Util.null2String(rs.getString("operators"));
//                }
//                //更新workflow_currentoperator
//                 //如果是依次逐个会签的情况
//                boolean needJudgeRequestException = true;
//                rs.execute("select * from workflow_nodegroup where nodeid="+nodeid_rb);
//                while (rs.next()){
//                    sqls="select * from workflow_groupdetail where groupid = " +rs.getString("id") ;
//                    rs1.execute(sqls);
//                    while (rs1.next()){
//                        int type=rs1.getInt("type");
//                        int signorder=rs1.getInt("signorder");
//                        /*
//                        if ((type==5 || type==50 || type==42 || type==51 || type==52 || type==53 || type==54 || type==55||type == 6
//                                ||type == 31||type==32||type==7||type==38||type == 40||type == 41||type == 17||type == 18||type == 36
//                                ||type == 37||type == 19||type == 39) && signorder == 2)  //依次逐个
//                        */
//                        //if ((type==5||type==50||type==42)&&signorder==2)  //依次逐个
//                       if (WFPathUtil.isContinuousProcessing(type) && signorder == 2){  //依次逐个
//                            needJudgeRequestException = false;
//                            //删除下一个操作者，修改workflow_aggentpersons表
//                            String fieldname = "";
//                            String fieldvalue = "";
//                            rs2.execute("select * from workflow_base where id="+wfid);
//                            if (rs2.next()){
//                                isbill=rs2.getInt("isbill");
//                                formid=rs2.getInt("formid");
//                            }
//                            if (isbill == 1) {
//                                rs2.executeSql("select tablename from workflow_bill where id = " + formid); // 查询工作流单据表的信息
//                                if (rs2.next())
//                                    billtablename = rs2.getString("tablename");          // 获得单据的主表
//                            }
//                            if (isbill == 0)
//                                rs2.executeSql("select fieldname from workflow_formdict where id =" + rs1.getInt("objid"));
//                            else
//                                rs2.executeSql("select fieldname from workflow_billfield where id =" + rs1.getInt("objid"));
//
//                            if (rs2.next()) fieldname = rs2.getString("fieldname");
//
//                            //自由流程查询不到
//                            if (isfreenode == 1 && !"".equals(freeOperators)) { //自由流程节点操作者
//                                fieldvalue = freeOperators;
//                            } else if (!"".equals(fieldname)) {
//                                if (isbill == 0)
//                                    rs2.executeSql("select " + fieldname + " from workflow_form where requestid=" + requestid);
//                                else
//                                    rs2.executeSql("select " + fieldname + " from " + billtablename + " where id = (select billid from workflow_form where requestid=" + requestid+")");
//
//                                if (rs2.next()) fieldvalue = Util.null2String(rs2.getString(fieldname));
//                            }
//
//                            //依次逐个处理，强制收回逻辑，抽取成私有方法传相应参数，具体逻辑未修改
//
//                            //如果是矩阵的情况下，根据矩阵id获取用户
//                            if(type == 99){
//                                RecordSet RecordSetTemp = new RecordSet();
//                                RequestNodeFlow requestnodeflow = new RequestNodeFlow();
//                                requestnodeflow.setCreaterid(creater);
//                                requestnodeflow.setIsbill(isbill);
//                                requestnodeflow.setRequestid(Integer.valueOf(requestid));
//                                requestnodeflow.setBilltablename(billtablename);
//                                if(isbill == 1){
//                                    RecordSetTemp.executeSql("select billid from workflow_form where requestid=" + requestid);
//                                    if(RecordSetTemp.next()){
//                                        billid = RecordSetTemp.getInt("billid");
//                                    }
//                                }
//                                requestnodeflow.setBillid(billid);
//                                fieldvalue = requestnodeflow.getUseridsByMatrix(RecordSetTemp,rs1.getString("id"));
//                            }
//                            if(drawBack_OrderManager(wfid, groupid, currNodeId, nodeid_rb, userid, requestid, fieldvalue))
//                                return;
//                        }
//                    }
//                }
//                if(needJudgeRequestException){      //找不到操作者请求异常特殊处理
//                    FlowExceptionHandle flowExceptionHandle = new FlowExceptionHandle();
//                    String exceptionOrderManagerOperator = flowExceptionHandle.getExceptionOrderManagerOperator(Util.getIntValue(requestid), currNodeId);
//                    if(!"".equals(exceptionOrderManagerOperator)){  //代表此节点是异常提交到指定人员依次逐个处理
//                        if(drawBack_OrderManager(wfid, groupid, currNodeId, nodeid_rb, userid, requestid, exceptionOrderManagerOperator))
//                            return;
//                    }
//                }
//
//
//                ArrayList idList = new ArrayList();
//                ArrayList useridList = new ArrayList();
//                ArrayList preisremarkList = new ArrayList();
//                ArrayList groupdetailidkList = new ArrayList();
//                RecordSet rshd = new RecordSet();
//                RecordSet rshd2 = new RecordSet();
//                RecordSet rshd3 = new RecordSet();
//                    int rshdresult = 0;
//                    int handleforwardid = -1;
//                    int behandleforwardid = -1;
//                    String sqlrshd = "select userid, id,preisremark,groupdetailid,handleforwardid from workflow_currentoperator where preisremark = '0' and handleforwardid > 0 and requestid = "
//                        + requestid
//                        + " and groupid = "
//                        + groupid
//                        + " and nodeid = "
//                        + currNodeId
//                        + " and userid="
//                            +userid  +" order by id desc";
//                            //new weaver.general.BaseBean().writeLog("--sqlrshd-375-会签-->>>"+sqlrshd);
//                    rshd.execute(sqlrshd);
//                    if(rshd.next()){
//                        rshdresult = 1;
//                        handleforwardid = Util.getIntValue(rshd.getString("handleforwardid"));
//                    }
//                    if(rshdresult > 0){
//                    sql = "select userid, id,preisremark,groupdetailid from workflow_currentoperator where preisremark = '0' and id <> "+ handleforwardid +" and requestid = "
//                        + requestid
//                        + " and groupid = "
//                        + groupid
//                        + " and nodeid = "
//                        + currNodeId +" order by id desc";
//                    }else{
//                      sql = "select handleforwardid,userid, id,preisremark,groupdetailid from workflow_currentoperator where preisremark = '0' and requestid = "
//                        + requestid
//                        + " and groupid = "
//                        + groupid
//                        + " and nodeid = "
//                        + currNodeId
//                        + " and userid="
//                            +userid  +"  order by id desc";
//                     rshd2.execute(sql);
//                     if(rshd2.next()){
//                        int nowid = Util.getIntValue(rshd2.getString("id"));
//                         sql = "select handleforwardid,userid, id,preisremark,groupdetailid from workflow_currentoperator where preisremark = '0' and requestid = "
//                        + requestid
//                        + " and groupid = "
//                        + groupid
//                        + " and nodeid = "
//                        + currNodeId
//                        + " and handleforwardid="
//                            +nowid  +"  order by id desc";
//                             rshd3.execute(sql);
//                         if(rshd3.next()){
//                             behandleforwardid = Util.getIntValue(rshd3.getString("handleforwardid"));
//                         }
//
//                    }
//                     sql = "select userid, id,preisremark,groupdetailid from workflow_currentoperator where preisremark = '0' and requestid = "
//                        + requestid
//                        + " and groupid = "
//                        + groupid
//                        + " and nodeid = "
//                        + currNodeId +" order by id desc";
//                    }
//                /*sql = "select userid, id,preisremark,groupdetailid from workflow_currentoperator where preisremark = '0' and requestid = "
//
//                        + requestid
//                        + " and groupid = "
//                        + groupid
//                        + " and nodeid = "
//                        + currNodeId +" order by id desc";   */
//                rs.execute(sql);
//                while(rs.next()){
//                    int userid_tmp = Util.getIntValue(rs.getString(1));
//                    int id_tmp = Util.getIntValue(rs.getString(2));
//                    int preisremark_tmp = Util.getIntValue(rs.getString(3));
//                    int groupdetailid_tmp = Util.getIntValue(rs.getString(4));
//                    int index_tmp = useridList.indexOf(""+userid_tmp);
//                    if(index_tmp > -1){
//
//                    }else{
//                        useridList.add(""+userid_tmp);
//                        idList.add(""+id_tmp);
//                        preisremarkList.add(""+preisremark_tmp);
//                        groupdetailidkList.add(""+groupdetailid_tmp);
//                    }
//                }
//                for(int cx=0; cx<idList.size(); cx++){
//                    int userid_tmp = Util.getIntValue((String)useridList.get(cx));
//                    int id_tmp = Util.getIntValue((String)idList.get(cx));
//                    int preisremark_tmp = Util.getIntValue((String)preisremarkList.get(cx));
//                    int groupdetailid_tmp = Util.getIntValue((String)groupdetailidkList.get(cx));
//                    if(preisremark_tmp==7){
//                        sql ="update workflow_currentoperator set isremark = '7', iscomplete=0,islasttimes = 1,isprocessed=null,isreminded=null,viewtype=0, operatedate = '',operatetime='' where preisremark='7' and requestid='"+requestid+"' and nodeid='"+currNodeId+"' and groupdetailid="+groupdetailid_tmp;
//                    }else{
//                        sql ="update workflow_currentoperator set isremark = '0', iscomplete=0,islasttimes = 1,isprocessed=null,isreminded=null,viewtype=0, operatedate = '',operatetime='' where id="+id_tmp;
//                    }
//                    rs.executeSql(sql);
//                    sql = "update workflow_currentoperator set islasttimes = 0 where id<>"+id_tmp+" and userid="+userid_tmp+" and preisremark = '0' and requestid = "
//                            + requestid
//                            + " and groupid = "
//                            + groupid
//                            + " and nodeid = "
//                            + currNodeId;
//                    rs.executeSql(sql);
//                }
//                //by ben 2006-03-27设置islasttiem,防止自身在下一节点的情况
//                //更新workflow_requstbase
//                sql =
//                    "select distinct groupid from workflow_currentoperator  where  groupdetailid>-1 and requestid = "
//                        + requestid
//                        + " and nodeid = "
//                        + currNodeId;
//                rs.executeSql(sql);
//
//                pretotalgroups = "" + rs.getCounts();
//                //如果存在依次逐个递交的情况，下面的判断不正确
//                sql =
//                    "select distinct groupid from workflow_currentoperator  where requestid = "
//                        + requestid
//                        + " and nodeid = "
//                        + currNodeId
//                        + " and isremark = '2' and groupdetailid>-1 ";
//                rs.executeSql(sql);
//                prepassedgroups = "" + rs.getCounts();
//
//                sql =
//                    "select operator,operatortype,operatedate,operatetime from workflow_requestlog where requestid = "
//                        + requestid
//                        + "  order by operatedate desc ,operatetime desc ";
//                rs.executeSql(sql);
//                if (rs.next()) {
//                    preloguserid = rs.getString("operator");
//                    prelogusertype = rs.getString("operatortype");
//                    preoperatedate = rs.getString("operatedate");
//                    preoperatetime = rs.getString("operatetime");
//                }
//                if (preloguserid.equals("")) preloguserid=""+creater;
//                if (prelogusertype.equals("")) prelogusertype=""+creatertype;
//                sql =
//                    " update workflow_requestbase set "
//                        + " status = '"
//                        + prestatus
//                        + "'"
//                        + " ,passedgroups = passedgroups-1"
//
//                        + " ,totalgroups = "
//                        + pretotalgroups
//                        + " ,lastoperator = "
//                        + preloguserid
//                        + " ,lastoperatedate = '"
//                        + preoperatedate
//                        + "' "
//                        + " ,lastoperatetime = '"
//                        + preoperatetime
//                        + "' "
//                        + " ,lastoperatortype = "
//                        + prelogusertype
//                        + " where requestid = "
//                        + requestid;
//                rs.executeSql(sql);
//
//                //把和前一个操作者相同groupid的操作人的viewtype从 -1 改为-2
//                sql =
//                    "select distinct groupid from workflow_currentoperator  where requestid = "
//                        + requestid
//                        + " and nodeid = "
//                        + currNodeId
//                        + " and isremark = '2' and userid = "
//                        + preloguserid
//                        + " and usertype = "
//                        + prelogusertype;
//                rs.executeSql(sql);
//                if (rs.next()) {
//                    pregroupid = rs.getString("groupid");
//                    sql =
//                    "update workflow_currentoperator set viewtype=-2 where requestid = "
//                        + requestid
//                        + " and groupid = "
//                        + pregroupid
//                        + " and nodeid = "
//                        + currNodeId;
//                    rs.executeSql(sql);
//                }
//                //转办强制收回时，删除被转办信息
//                if(behandleforwardid>0){
//                     sql = "delete from workflow_currentoperator where  preisremark = '0' and requestid = "
//                     + requestid
//                            + " and groupid = "
//                            + groupid
//                            + " and nodeid = "
//                            + currNodeId
//                            + " and handleforwardid = "
//                            + behandleforwardid;
//                           // new weaver.general.BaseBean().writeLog("--转办强制收回-547-sql-->>>"+sql);
//                             rs.executeSql(sql);
//                }
//            } else { //收回操作者不在当前节点
//                sql =
//                    "select  userid,usertype,wfreminduser,wfusertypes from workflow_currentoperator where requestid = "
//                    + requestid
//                    + " and nodeid = "
//                    + currNodeId
//                    +" and isremark in ('0','1','8','9','7','4') ";
//                rs2.execute(sql);
//                while (rs2.next())
//                {
//                     //更新新到流程信息数量
//
//                    poppupRemindInfoUtil.updatePoppupRemindInfo(rs2.getInt(1),0,""+rs2.getInt(2),Integer.parseInt(requestid));
//                    //删除超时提醒
//                    ArrayList wfremindusers=Util.TokenizerString(Util.null2String(rs2.getString("wfreminduser")),",");
//                    ArrayList wfusertypes=Util.TokenizerString(Util.null2String(rs2.getString("wfusertypes")),",");
//                    for(int k=0;k<wfremindusers.size();k++){
//                        poppupRemindInfoUtil.updatePoppupRemindInfo(Util.getIntValue((String)wfremindusers.get(k)),10,(String)wfusertypes.get(k),Integer.parseInt(requestid));
//                    }
//
//                }
//                //更新workflow_currentoperator
//                sql =
//                    "delete workflow_currentoperator where requestid = "
//                        + requestid
//                        + " and nodeid = "
//                        + currNodeId
//                        +" and isremark in ('0','1','8','9','7','4') ";  //add isremark控制退回的情况
//                rs.executeSql(sql);
//
//                //如果是依次逐个递交，虽然groupid一样，但是操作有别与非会签的
//                rs2.execute("select * from workflow_groupdetail where id="+groupdetailid);
//                rs2.next();
//                int signtype=rs2.getInt("type");
//                int signorder=rs2.getInt("signorder");
//
//                /*
//                if ((signtype==5 || signtype==50 || signtype==42 || signtype==51 || signtype==52 || signtype==53 || signtype==54 || signtype==55||signtype == 6
//                        ||signtype == 31||signtype==32||signtype==7||signtype==38||signtype == 40||signtype == 41||signtype == 17||signtype == 18||signtype == 36
//                        ||signtype == 37||signtype == 19||signtype == 39) && signorder == 2)
//                */
//                //if ((signtype==5||signtype==50||signtype==42)&&signorder==2)
//                if (WFPathUtil.isContinuousProcessing(signtype) && signorder == 2)
//                {
//                    sql =
//                        "update workflow_currentoperator set isremark = '0', viewtype=0, operatedate = '',operatetime='' where preisremark = '0' and requestid = "
//                            + requestid
//                            + " and groupdetailid = "
//                            + groupdetailid
//                            + " and userid="
//                            +userid
//                            + " and nodeid = "
//                            + nodeid_rb;
//
//                        rs.executeSql(sql);
//                }
//                else
//                {
//                    ArrayList idList = new ArrayList();
//                    ArrayList useridList = new ArrayList();
//                    ArrayList preisremarkList = new ArrayList();
//                    ArrayList groupdetailidkList = new ArrayList();
//
//                    boolean isCoadjunt = false;
//                    RecordSet rshd = new RecordSet();
//                    RecordSet rshd2 = new RecordSet();
//                    RecordSet rshd3 = new RecordSet();
//                    int rshdresult = 0;
//                    int handleforwardid = -1;
//                    int behandleforwardid = -1;
//                    String sqlrshd = "select userid, id,preisremark,groupdetailid,handleforwardid from workflow_currentoperator where handleforwardid > 0 and requestid = "
//                        + requestid
//                        + " and groupid = "
//                        + groupid
//                        + " and nodeid = "
//                        + nodeid_rb
//                        + " and userid="
//                            +userid;
//                    rshd.execute(sqlrshd);
//                    if(rshd.next()){
//                        rshdresult = 1;
//                        handleforwardid = Util.getIntValue(rshd.getString("handleforwardid"));
//                    }
//                    if(rshdresult > 0){
//                    sql = "select userid, id,preisremark,groupdetailid from workflow_currentoperator where id <> "+ handleforwardid +" and requestid = "
//                        + requestid
//                        + " and groupid = "
//                        + groupid
//                        + " and nodeid = "
//                        + nodeid_rb ;
//                    }else{
//                        String beforwarduserid ="-1";
//                        String beforwarduserids ="-1";
//                        sql = "select handleforwardid,userid, id,preisremark,groupdetailid from workflow_currentoperator where preisremark = '0' and requestid = "
//                        + requestid
//                        + " and groupid = "
//                        + groupid
//                        + " and nodeid = "
//                        + nodeid_rb
//                        + " and handleforwardid>0  order by id desc";
//                        //new weaver.general.BaseBean().writeLog("---661-非会签---sql---"+sql);
//                     rshd2.execute(sql);
//                     while(rshd2.next()){
//                        int nowid = Util.getIntValue(rshd2.getString("handleforwardid"));
//                         sql = "select handleforwardid,userid, id,preisremark,groupdetailid from workflow_currentoperator where preisremark = '0' and requestid = "
//                        + requestid
//                        + " and groupid = "
//                        + groupid
//                        + " and nodeid = "
//                        + nodeid_rb
//                        + " and id="
//                            +nowid  +"  order by id desc";
//                            //new weaver.general.BaseBean().writeLog("---672-非会签---sql---"+sql);
//                             rshd3.execute(sql);
//                         if(rshd3.next()){
//                             beforwarduserid = Util.null2String(rshd3.getString("userid"));
//                         }
//                         if("-1".equals(beforwarduserids)){
//                          beforwarduserids = beforwarduserid;
//                         }else{
//                            beforwarduserids = beforwarduserids+","+beforwarduserid;
//                         }
//                    }
//                    //new weaver.general.BaseBean().writeLog("---682-非会签---beforwarduserids---"+beforwarduserids);
//                     sql = "select userid, id,preisremark,groupdetailid from workflow_currentoperator where requestid = "
//                        + requestid
//                        + " and groupid = "
//                        + groupid
//                        + " and nodeid = "
//                        + nodeid_rb
//                        + "and userid not in ('"
//                        + beforwarduserids
//                        +"')";
//                    }
//                   // new weaver.general.BaseBean().writeLog("---693-非会签---sql---"+sql);
//                    rs.execute(sql);
//                    while(rs.next()){
//                        int userid_tmp = Util.getIntValue(rs.getString(1));
//                        int id_tmp = Util.getIntValue(rs.getString(2));
//                        int preisremark_tmp = Util.getIntValue(rs.getString(3));
//                        int groupdetailid_tmp = Util.getIntValue(rs.getString(4));
//                        int index_tmp = useridList.indexOf(""+userid_tmp);
//                        if(preisremark_tmp==7 && userid==userid_tmp) isCoadjunt=true;
//                        if(index_tmp > -1){
//
//                        }else{
//							//  new weaver.general.BaseBean().writeLog("---709-非会签---userid_tmp---"+userid_tmp);
//							// new weaver.general.BaseBean().writeLog("---710-非会签---id_tmp---"+id_tmp);
//                            useridList.add(""+userid_tmp);
//                            idList.add(""+id_tmp);
//                            preisremarkList.add(""+preisremark_tmp);
//                            groupdetailidkList.add(""+groupdetailid_tmp);
//                        }
//                    }
//                    for(int cx=0; cx<idList.size(); cx++){
//                        int userid_tmp = Util.getIntValue((String)useridList.get(cx));
//                        int id_tmp = Util.getIntValue((String)idList.get(cx));
//                        int preisremark_tmp = Util.getIntValue((String)preisremarkList.get(cx));
//                        int groupdetailid_tmp = Util.getIntValue((String)groupdetailidkList.get(cx));
//                        if(preisremark_tmp==7){
//                            sql ="update workflow_currentoperator set isremark = '7', iscomplete=0,islasttimes = 1,isprocessed=null,isreminded=null,viewtype=0, operatedate = '',operatetime='' where preisremark='7' and requestid='"+requestid+"' and nodeid='"+nodeid_rb+"' and groupdetailid="+groupdetailid_tmp;
//                        }else{
//                            if(isCoadjunt) continue;  //如果当前操作者是协办人，协办人强制收回不更新主办人操作状态
//							String sqltk = "select * from workflow_currentoperator where preisremark='1' and takisremark='2' and id = "+ id_tmp;
//							rs.executeSql(sqltk);
//							if(rs.next()){
//							  sql ="update workflow_currentoperator set isremark = '1', viewtype=0,iscomplete=0,islasttimes =1,isprocessed=null,isreminded=null, operatedate = '',operatetime='' where ((preisremark='1' and takisremark='2')) and id="+id_tmp;
//							} else{
//                            sql ="update workflow_currentoperator set isremark = '0', viewtype=0,iscomplete=0,islasttimes =1,isprocessed=null,isreminded=null, operatedate = '',operatetime='' where (preisremark='0' and (takisremark is null or takisremark=0))  and id="+id_tmp;
//							}
//							//new weaver.general.BaseBean().writeLog("------sql----725-->>>"+sql);
//                        }
//                        rs.executeSql(sql);
//                        sql = "update workflow_currentoperator set islasttimes = 0 where id<>"+id_tmp+" and userid="+userid_tmp+" and requestid = "
//                        + requestid
//                        + " and groupid = "
//                        + groupid
//                        + " and nodeid = "
//                        + nodeid_rb;
//                        rs.executeSql(sql);
//                    }
//                }
//                //因为退回的时候会把当前节点所有未操作的操作者设为已操作，所以如果是收回退回的操作这里要重新更新workflow_currentoperator
//                //判断是否是因为被退回而设置成已操作的
//                //首先判断是否是收回退回节点
//                rs.execute("select destnodeid from workflow_nodelink where wfrequestid is null and nodeid="+nodeid_rb+" and isreject='1'");
//                if (rs.next())
//                {
//                    if (rs.getInt(1)==currNodeId)   //表明是收回退回情况
//                    {
//                        //在日志workflow_requestlog表里不存在表示2中情况1 由于退回被设成isremrk='2' 2 由于非会签被设成isremrk='2'（不用处理）
//
//                        //先把日志workflow_requestlog表里不存在的操作者状态改成未操作
//                        rs3.execute("update  workflow_currentoperator set isremark='0' where  preisremark = '0' and requestid="+requestid+"  and nodeid="+nodeid_rb+" and groupid!="+groupid+" and userid not in (select operator from workflow_requestlog where requestid="+requestid+" and nodeid="+nodeid_rb+") ");
//                        //2 由于非会签被设成isremrk='2'
//                        rs3.execute("select *  from workflow_currentoperator where  requestid="+requestid+"  and nodeid="+nodeid_rb+" and isremark='2' and groupid!="+groupid);
//                        while (rs3.next())
//                        {
//                          boolean flags=false;
//                          rs1.execute("select * from workflow_groupdetail where id="+rs3.getInt("groupdetailid"));
//                          if (rs1.next())
//                          {
//                              int types = rs1.getInt("type");
//                              int levels = rs1.getInt("signorder");
//                              /*
//                              if ((types==5 || types==50 || types==42 || types==51 || types==52 || types==53 || types==54 || types==55||types == 6
//                                        ||types == 31||types==32||types==7||types==38||types == 40||types == 41||types == 17||types == 18||types == 36
//                                        ||types == 37||types == 19||types == 39) && levels == 2)
//                              */
//                              //if ((types == 5||types == 50||types == 42) && levels == 2)   flags=true;
//                              if ((types==5 || types==50 || types==42 || types==51 ||types == 6
//                                        ||types == 31||types==32||types==7||types==38||types == 40||types == 41||types == 17||types == 18||types == 36
//                                        ||types == 37||types == 19||types == 39 ||types==15||types==8||types==33||types==9||types==10||types==47||types==34||types==11||types==12||types==48||types==13||types==35||types==14||types==44||types==45||types==46||types==16||types==43||types==49) && levels == 2)   flags=true;
//
//
//                          }
//                           if (!flags)
//                           rs2.execute("update workflow_currentoperator set isremark='2' where  requestid="+requestid+"  and nodeid="+nodeid_rb+" and groupid="+rs3.getInt("groupid"));
//                           //System.out.print("update workflow_currentoperator set isremark='2' where  requestid="+requestid+"  and nodeid="+nodeid_rb+" and groupid="+rs3.getInt("groupid"));
//                        }
//                    }
//
//                }
//                //更新workflow_requstbase
//                sql =
//                    "select distinct groupid from workflow_currentoperator  where groupdetailid>-1 and requestid = "
//                        + requestid
//                        + " and nodeid = "
//                        + nodeid_rb;
//
//                rs.executeSql(sql);
//                pretotalgroups = "" + rs.getCounts();
//
//
//                sql =
//                    "select distinct groupid from workflow_currentoperator  where requestid = "
//                        + requestid
//                        + " and nodeid = "
//                        + nodeid_rb
//                        + " and isremark = '2' and groupdetailid>-1";
//
//                rs.executeSql(sql);
//                //prepassedgroups
//                passedgroups= rs.getCounts();
//                //如果存在依次会签的不能如此计算通过组数
//
//                //判断该人所在组是否含有依次逐个递交的组，如果有一个，则passedgroups-1
//                rs.execute("select distinct groupdetailid,groupid from workflow_currentoperator where isremark = '2' and requestid=" + requestid + "  and nodeid="+nodeid_rb);
//
//                while (rs.next())
//                {
//                    rs2.execute("select * from workflow_groupdetail where id="+rs.getInt("groupdetailid"));
//                    if (rs2.next())
//                    {   int type = rs2.getInt("type");
//                        int level = rs2.getInt("signorder");
//                        /*
//                         if ((type==5 || type==50 || type==42 || type==51 || type==52 || type==53 || type==54 || type==55||type == 6
//                                    ||type == 31||type==32||type==7||type==38||type == 40||type == 41||type == 17||type == 18||type == 36
//                                    ||type == 37||type == 19||type == 39) && level == 2)
//                        */
//                        //if ((type == 5||type == 50||type == 42) && level == 2)
//                        if (WFPathUtil.isContinuousProcessing(type) && level == 2)
//                        {    //判断是否还有剩余节点或者有其中一个节点还没操作
//                             rs3.execute("select * from workflow_agentpersons where requestid="+requestid+" and (groupdetailid="+rs.getInt("groupdetailid")+" or groupdetailid is null)");
//                             rs4.execute("select id from workflow_currentoperator where requestid="+requestid+" and nodeid="+nodeid_rb+" and isremark = '0' and groupdetailid="+rs.getInt("groupdetailid"));
//                             if ((rs3.next()&&!rs3.getString("receivedPersons").equals(""))||rs4.next())
//                             {passedgroups--;
//                             }
//                        }
//
//                    }
//
//                }
//                prepassedgroups=""+passedgroups;
//                sql =
//                    "select * from workflow_requestlog where requestid = "
//                        + requestid
//                        + " and nodeid <> "
//                        + nodeid_rb
//                        + "  order by operatedate desc ,operatetime desc ";
//
//                rs.executeSql(sql);
//                if (rs.next()) {
//                    prelastnodeid = rs.getString("nodeid");
//                    sql =
//                    "select nodetype from workflow_flownode where workflowid ="
//                        + wfid
//                        + " and nodeid ="
//                        + prelastnodeid;
//                    rs.executeSql(sql);
//                    if (rs.next()) {
//                        prelastnodetype = rs.getString("nodetype");
//                    }
//                }
//
//                precurrnodeid = "" + nodeid_rb;
//                precurrnodetype = "" + nodetype_rb;
//
//                sql =
//                    "select operator,operatortype,operatedate,operatetime from workflow_requestlog where requestid = "
//                        + requestid
//                        + "  order by operatedate desc ,operatetime desc ";
//
//                rs.executeSql(sql);
//                if (rs.next()) {
//                    preloguserid = rs.getString("operator");
//                    prelogusertype = rs.getString("operatortype");
//                    preoperatedate = rs.getString("operatedate");
//                    preoperatetime = rs.getString("operatetime");
//                }
//                //add by ben 2006-03-27
//                 if (preloguserid.equals("")) preloguserid=""+creater;
//                 if (prelogusertype.equals("")) prelogusertype=""+creatertype;
//                 if (prelastnodeid.equals("")) prelastnodeid=precurrnodeid;
//                 if (prelastnodetype.equals("")) prelastnodetype=precurrnodetype;
//                sql =
//                    " update workflow_requestbase set "
//                        + " lastnodeid = "
//                        + prelastnodeid
//                        + " ,lastnodetype = '"
//                        + prelastnodetype
//                        + "' ,currentnodeid = "
//                        + precurrnodeid
//                        + " ,currentnodetype = '"
//                        + precurrnodetype
//                        + "' ,status = '"
//                        + prestatus
//                        + "'"
//                        + " ,passedgroups = "
//                        + prepassedgroups
//                        + " ,totalgroups = "
//                        + pretotalgroups
//                        + " ,lastoperator = "
//                        + preloguserid
//                        + " ,lastoperatedate = '"
//                        + preoperatedate
//                        + "' "
//                        + " ,lastoperatetime = '"
//                        + preoperatetime
//                        + "' "
//                        + " ,lastoperatortype = "
//                        + prelogusertype
//                        + " where requestid = "
//                        + requestid;
//
//                rs.executeSql(sql);
//                //更新当前节点表
//                WFLinkInfo wflinkinfo=new WFLinkInfo();
//                int nodeattr=wflinkinfo.getNodeAttribute(Util.getIntValue(precurrnodeid));
//                rs.executeSql("delete from workflow_nownode where nownodeid ="+currNodeId+" and requestid="+requestid);
//                rs.executeSql("insert into workflow_nownode(requestid,nownodeid,nownodetype,nownodeattribute) values("+requestid+","+precurrnodeid+","+precurrnodetype+","+nodeattr+")");
//
//                //把和前一个操作者相同groupid的操作人的viewtype从 -1 改为-2
//                sql =
//                    "select distinct groupid from workflow_currentoperator  where requestid = "
//                        + requestid
//                        + " and nodeid = "
//                        + nodeid_rb
//                        + " and isremark = '2' and userid = "
//                        + preloguserid
//                        + " and usertype = "
//                        + prelogusertype;
//
//                rs.executeSql(sql);
//                if (rs.next()) {
//                    pregroupid = rs.getString("groupid");
//
//                sql =
//                    "update workflow_currentoperator set viewtype=-2 where requestid = "
//                        + requestid
//                        + " and groupid = "
//                        + pregroupid
//                        + " and nodeid = "
//                        + nodeid_rb;
//
//                rs.executeSql(sql);
//                }
//                if (currNodeType == 3) {
//                    sql = "update workflow_currentoperator set iscomplete=0 where requestid = "+ requestid;
//                    rs.executeSql(sql);
//                    sql = "delete from  workflow_currentoperator where isremark='4' and requestid = "+ requestid;
//                    rs.executeSql(sql);
//                }
//            }
//
//
//                //处理收回节点的操作者同时在已处理的其他节点的时的情况，把最后操作者的islasttimes设为1
//            ArrayList users=new ArrayList();
//
//            rs2.execute("select id, userid from workflow_currentoperator where requestid="+requestid);
//            while (rs2.next())
//            {
//            String userids=rs2.getString("userid");
//            String rid=rs2.getString("id");
//            if (users.indexOf(userids)>-1)
//            {
//                continue;
//            }
//            rs.execute("select  userid from workflow_currentoperator where requestid="+requestid+" and islasttimes=1 and  userid="+userids);
//            if (!rs.next())
//            {
//            users.add(userids);
//            rs.execute("update  workflow_currentoperator set islasttimes=1 where requestid="+requestid+" and userid="+userids+" and id="+rid );
//            }
//            }
//
//          //处理共享
//
//               rs2.execute("select * from workflow_base where id="+wfid);
//                if (rs2.next())
//                        {
//                            isbill=rs2.getInt("isbill");
//                            formid=rs2.getInt("formid");
//
//                        }
//                        if (isbill == 1) {
//                            rs2.executeSql("select tablename from workflow_bill where id = " + formid); // 查询工作流单据表的信息
//                            if (rs2.next())
//                                billtablename = rs2.getString("tablename");          // 获得单据的主表
//                        }
//                try{
//                RequestAddShareInfo shareinfo = new RequestAddShareInfo();
//                shareinfo.setRequestid(Util.getIntValue(requestid));
//                shareinfo.SetWorkFlowID(wfid);
//                shareinfo.SetNowNodeID(currNodeId);
//                if(nodeid_rb==0)
//                    shareinfo.SetNextNodeID(currNodeId);
//                else
//                    shareinfo.SetNextNodeID(nodeid_rb);
//                shareinfo.setIsbill(isbill);
//                User usernew=new User();
//                usernew.setUid(-1);
//                usernew.setLogintype("1");
//                shareinfo.setUser(usernew);
//                shareinfo.SetIsWorkFlow(1);
//                shareinfo.setBillTableName(billtablename);
//                shareinfo.setHaspassnode(true);
//
//                shareinfo.addShareInfo();
//                }
//                catch (Exception ee)
//               {}
//                try{
//                    //writeLog("当前节点类型==="+currNodeType);
//                    //writeLog("上一节点类型==="+nodetype_rb);
//                    CapitalUnfreeze(requestid,currNodeType,nodetype_rb);
//                }catch(Exception e){
//                    writeLog(e);
//                }
//
//                // 统一待办提醒
//                PostWorkflowInfo postWorkflowInfo2 = new PostWorkflowInfo();
//                postWorkflowInfo2.operateToDo(requestid);
//                writeLog("==========统一待办(WfForceDrawBack.java --> operateToDo)==========");
//        }
//
//        RecordSet rs_1 = new RecordSet();
//        HrmAttVacationManager manager = new HrmAttVacationManager();
//        for (int i = 0; i < requestids.size(); i++) {
//            requestid = (String) requestids.get(i);
//
//            int _wfid = 0;
//            int _currNodeType = 0;
//            int _formid = 0;
//            rs_1.executeProc("workflow_Requestbase_SByID", requestid + "");
//            //当前节点
//            if (rs_1.next()) {
//                _wfid = rs_1.getInt("workflowid");
//                _currNodeType = Util.getIntValue(rs_1.getString("currentnodetype"), 0);
//            }
//
//            rs_1.executeSql("select formid from workflow_base where id = "+_wfid);
//            if (rs_1.next()) {
//                _formid = rs_1.getInt("formid");
//            }
//
//            if(_currNodeType==0){
//            	//added by wcd 2015-09-14
//            	manager.handle(StringUtil.parseToInt(requestid), _wfid, 0);
//                try {
//                    FnaCommon fnaCommon = new FnaCommon();
//                    fnaCommon.doWfForceOver(Util.getIntValue(requestid, 0), 0, true);
//                }catch(Exception easi) {
//                    new BaseBean().writeLog(easi);
//                }
//            }
//        }
//    }
//
//    /**
//     * 依次逐个处理，强制收回逻辑，抽取成私有方法传相应参数，具体逻辑未修改
//     * 找不到操作者异常提交到指定人员依次处理时，强制收回时也调用此方法
//     */
//    private boolean drawBack_OrderManager(int wfid,int groupid,int currNodeId,int nodeid_rb,int userid,String requestid,String fieldvalue){
//
//        RecordSet rs2 = new RecordSet();
//        RecordSet rs3 = new RecordSet();
//        /*String sql =
//            "select  userid,usertype,wfreminduser,wfusertypes, agentorbyagentid, agenttype, groupdetailid from workflow_currentoperator where isremark = '0' and requestid = "
//                + requestid
//                + " and groupid = "
//                + groupid
//                + " and nodeid = "
//                + currNodeId;
//                */
//                String sql = "";
//                RecordSet rshd = new RecordSet();
//                    int rshdresult = 0;
//                    int handleforwardid = -1;
//                    String sqlrshd = "select  handleforwardid,userid,usertype,wfreminduser,wfusertypes, agentorbyagentid, agenttype, groupdetailid from workflow_currentoperator where preisremark = '0' and handleforwardid > 0 and requestid = "
//                        + requestid
//                        + " and groupid = "
//                        + groupid
//                        + " and nodeid = "
//                        + currNodeId
//                        + " and userid="
//                            +userid;
//                            //new weaver.general.BaseBean().writeLog("--sqlrshd-987-依次-->>>"+sqlrshd);
//                    rshd.execute(sqlrshd);
//                    if(rshd.next()){
//                        rshdresult = 1;
//                        handleforwardid = Util.getIntValue(rshd.getString("handleforwardid"));
//                    }
//                    if(rshdresult > 0){
//                    sql = "select  userid,usertype,wfreminduser,wfusertypes, agentorbyagentid, agenttype, groupdetailid from workflow_currentoperator where isremark = '0' and id <> "+ handleforwardid +" and requestid = "
//                        + requestid
//                        + " and groupid = "
//                        + groupid
//                        + " and nodeid = "
//                        + currNodeId ;
//                    }else{
//                     sql = "select  userid,usertype,wfreminduser,wfusertypes, agentorbyagentid, agenttype, groupdetailid from workflow_currentoperator where isremark = '0' and requestid = "
//                        + requestid
//                        + " and groupid = "
//                        + groupid
//                        + " and nodeid = "
//                        + currNodeId;
//                    }
//               // new weaver.general.BaseBean().writeLog("--sql-940-依次-->>>"+sql);
//        rs2.execute(sql);
//        if (rs2.next()){
//            int userid_agentpersons = Util.getIntValue(rs2.getString(1), 0);
//            int agenttype_agentpersons = Util.getIntValue(rs2.getString("agenttype"), 0);
//            int agentorbyagentid_agentpersons = Util.getIntValue(rs2.getString("agentorbyagentid"), 0);
//            int userid_groupdetailid = Util.getIntValue(rs2.getString("groupdetailid"), 0);  ///65833
//            if(agenttype_agentpersons == 2){
//                userid_agentpersons = agentorbyagentid_agentpersons;
//            }
//
//            //
//            int handledagentpersons = userid;
//            //存在代理时，获取被代理人的id
//            try{
//                String tempSql = "select agentorbyagentid from workflow_currentoperator ";
//                tempSql += " where requestid=" + requestid + " and nodeid = " + currNodeId ;
//                tempSql += "   and userid =" + userid + " and isremark = '2' ";
//                tempSql += "   and agenttype = '2' and groupid = " + groupid;
//                rs3.executeSql(tempSql);
//                if(rs3.next()){
//                    handledagentpersons = rs3.getInt("agentorbyagentid");
//                }
//
//            }catch(Exception e){
//            }
//
//        if ((handleforwardid<0&&fieldvalue.indexOf(""+handledagentpersons)>-1&&fieldvalue.indexOf(""+userid_agentpersons)>-1) || (handleforwardid>0&&fieldvalue.indexOf(""+userid_agentpersons)>-1))
//        {
//            if(agenttype_agentpersons == 2){
//                sql =
//                    "delete  from workflow_currentoperator  where requestid = "
//                        + requestid
//                        + " and groupid = "
//                        + groupid
//                        + " and nodeid = "
//                        + currNodeId
//                        +" and ((userid="+rs2.getInt(1)+" and agenttype='2') or (userid="+userid_agentpersons+" and agenttype='1'))";
//                }else{
//                    sql =
//                    "delete  from workflow_currentoperator  where requestid = "
//                        + requestid
//                        + " and groupid = "
//                        + groupid
//                        + " and nodeid = "
//                        + currNodeId
//                        +" and userid="
//                        +rs2.getInt(1)
//                        ;
//                }
//            PoppupRemindInfoUtil poppupRemindInfoUtil = new PoppupRemindInfoUtil();
//            poppupRemindInfoUtil.updatePoppupRemindInfo(rs2.getInt(1),0,""+rs2.getInt(2),Integer.parseInt(requestid));
//            //删除超时提醒
//            ArrayList wfremindusers=Util.TokenizerString(Util.null2String(rs2.getString("wfreminduser")),",");
//            ArrayList wfusertypes=Util.TokenizerString(Util.null2String(rs2.getString("wfusertypes")),",");
//            for(int k=0;k<wfremindusers.size();k++){
//                poppupRemindInfoUtil.updatePoppupRemindInfo(Util.getIntValue((String)wfremindusers.get(k)),10,(String)wfusertypes.get(k),Integer.parseInt(requestid));
//            }
//            rs3.executeSql(sql);
//            //更新新到流程信息数量
//
//            sql = "select max(id) as maxid from workflow_currentoperator where preisremark = '0' and requestid = "
//                    + requestid
//                    + " and groupid = "
//                    + groupid
//                    + " and nodeid = "
//                    + currNodeId
//                    +" and userid="
//                    +userid
//                    ;
//            rs3.executeSql(sql);
//            if(rs3.next()){
//                int maxid = Util.getIntValue(rs3.getString(1));
//                sql = "update workflow_currentoperator set isremark = '0', viewtype=0, islasttimes = 1,isprocessed=null,isreminded=null,operatedate = '',operatetime='' where id= "+maxid;
//                rs3.executeSql(sql);
//                sql = "update workflow_currentoperator set islasttimes = 0 where id<>"+maxid + " and requestid="+requestid+" and userid="+userid;
//                rs3.executeSql(sql);
//            }
//
//            //更新workflow_agentpersons表
//            //65833 begin
//            rs3.execute("select * from workflow_agentpersons where requestid="+requestid + " and groupdetailid = " + userid_groupdetailid); //65833
//            boolean flags = false; //65833
//            if (rs3.next()){
//                flags = true; //65833
//                String hrmstr=rs3.getString("receivedPersons");
//                hrmstr=""+userid_agentpersons+","+hrmstr;
//                //更新依次会签的协办人id
//                String coadjutants = rs3.getString("coadjutants");
//                coadjutants = "-1," + coadjutants;
//                sql="update workflow_agentpersons set receivedPersons='"+hrmstr+"', coadjutants ='" + coadjutants + "' where requestid="+requestid + " and groupdetailid = " + userid_groupdetailid; //65833
//                rs3.execute(sql);
//            }
//            if(!flags){
//                rs3.execute("select * from workflow_agentpersons where requestid="+requestid);
//              if (rs3.next()){
//                  String hrmstr=rs3.getString("receivedPersons");
//                  hrmstr=""+userid_agentpersons+","+hrmstr;
//                  //更新依次会签的协办人id
//                  String coadjutants = rs3.getString("coadjutants");
//                  coadjutants = "-1," + coadjutants;
//                  sql="update workflow_agentpersons set receivedPersons='"+hrmstr+"', coadjutants ='" + coadjutants + "' where requestid="+requestid;
//                  rs3.execute(sql);
//              }else{
//                sql="insert into  workflow_agentpersons (receivedPersons , requestid, groupdetailid,coadjutants) values ('"+userid_agentpersons+"',"+requestid+"',"+userid_groupdetailid+",'-1,')";
//                rs3.execute(sql);
//              }
//          }
//          //65833 end
//
//            //处理共享
//            int formid = -1;
//            int isbill = -1;
//            String billtablename="";
//            rs2.execute("select * from workflow_base where id="+wfid);
//            if (rs2.next()){
//                isbill=rs2.getInt("isbill");
//                formid=rs2.getInt("formid");
//            }
//            if (isbill == 1) {
//                rs2.executeSql("select tablename from workflow_bill where id = " + formid); // 查询工作流单据表的信息
//                if (rs2.next()){
//                    billtablename = rs2.getString("tablename");          // 获得单据的主表
//                }
//            }
//            try{
//                RequestAddShareInfo shareinfo = new RequestAddShareInfo();
//                shareinfo.setRequestid(Util.getIntValue(requestid));
//                shareinfo.SetWorkFlowID(wfid);
//                shareinfo.SetNowNodeID(currNodeId);
//                if(nodeid_rb==0)
//                    shareinfo.SetNextNodeID(currNodeId);
//                else
//                    shareinfo.SetNextNodeID(nodeid_rb);
//                shareinfo.setIsbill(isbill);
//                User usernew=new User();
//                usernew.setUid(-1);
//                usernew.setLogintype("1");
//                shareinfo.setUser(usernew);
//                shareinfo.SetIsWorkFlow(1);
//                shareinfo.setBillTableName(billtablename);
//                shareinfo.setHaspassnode(true);
//
//                shareinfo.addShareInfo();
//            }catch (Exception ee){
//                writeLog("ee="+ee);
//            }
//
//            return true;
//
//        }
//
//        }
//        return false;
//    }
//
//    //如果是资产领用流程，流程收回到创建节点，冻结的资产数量要解冻
//    public void CapitalUnfreeze(String requestid,int currNodeType,int nodetype_rb){
//        RecordSet reSet1 = new RecordSet();
//        RecordSet reSet2 = new RecordSet();
//        reSet1.executeSql(" select r.requestid,r.workflowid,r.currentnodetype from workflow_requestbase r,workflow_base b where requestid = " + requestid + " and r.workflowid=b.id and b.formid=19 and b.isbill=1" ) ;
//        while(reSet1.next()){
//            String sql=" select b.* from workflow_form w,bill_CptFetchDetail b where w.requestid ="+requestid+" and w.billid=b.cptfetchid";
//            //writeLog("sql2==="+sql);
//            reSet2.executeSql(sql) ;
//            RecordSet reSet3 = new RecordSet();
//            while(reSet2.next()){
//                String capitalid=reSet2.getString("capitalid");
//                float old_number_n = 0.0f;
//                float old_frozennum = 0.0f;
//                float new_frozennum = 0.0f;
//                reSet3.executeSql("select number_n as old_number_n from bill_CptFetchDetail where cptfetchid = (select id from bill_CptFetchMain where requestid="+requestid+") and capitalid="+capitalid);
//                //writeLog("sql3===select number_n as old_number_n from bill_CptFetchDetail where cptfetchid = (select id from bill_CptFetchMain where requestid="+requestid+") and capitalid="+capitalid);
//                if(reSet3.next())
//                    old_number_n = reSet3.getFloat("old_number_n");
//                reSet3.executeSql("select frozennum as old_frozennum from CptCapital where id="+capitalid);
//                if(reSet3.next())
//                    old_frozennum = reSet3.getFloat("old_frozennum");
//                if(nodetype_rb==0 && (currNodeType==1 || currNodeType==2)){//中间节点收回到创建节点
//                    new_frozennum = old_frozennum - old_number_n;
//                    reSet3.executeSql("update CptCapital set frozennum="+new_frozennum+" where id="+capitalid);
//                 }
//                 else if(currNodeType==0 && (nodetype_rb==1 || nodetype_rb==2)){//创建节点收回到中间节点，收回的是退回操作
//                    new_frozennum = old_frozennum + old_number_n;
//                    reSet3.executeSql("update CptCapital set frozennum="+new_frozennum+" where id="+capitalid);
//                 }
//            }
//        }
//    }
//
//     /**
//     * 流程强制退回到创建节点(单个或批量)
//     * @param requestids
//     * @param request
//     * @param response
//     * @param userid1
//     * @param usertype1
//     */
//    public void ForceDrawBackToCreater(ArrayList requestids,HttpServletRequest request,HttpServletResponse response,int userid1,int usertype1){
//        String requestid = "";
//        User user = HrmUserVarify.getUser(request, response);
//        int currNodeId = -1;
//        int currNodeType = 0;
//        String billtablename = "";
//        int formid = -1;
//        int isbill = -1;
//        int billid = -1;
//        int wfid = -1;
//        int creater = -1;
//        int creatertype = -1;
//        int nodeid_rb = -1;
//        int nodetype_rb = -1;
//        int groupid = -1;
//        int passedgroups=0;
//        int groupdetailid=0;
//        int userid = user.getUID();
//        PoppupRemindInfoUtil poppupRemindInfoUtil = new PoppupRemindInfoUtil();
//        int usertype = (user.getLogintype().equals("1")) ? 0 : 1;
//        if (userid1 != -1) {
//            userid = userid1;
//            usertype = usertype1;
//        }
//
//        RecordSet rs = new RecordSet();
//        RecordSet rs2 = new RecordSet();
//        String sql = "";
//        for (int i = 0; i < requestids.size(); i++) {
//            requestid = (String) requestids.get(i);
//            sql="select a.workflowid,a.creater,a.creatertype,b.nodeid,b.nodetype from workflow_requestbase a,workflow_flownode b where a.workflowid=b.workflowid and b.nodetype='0' and a.requestid="+requestid;
//            //System.out.println("sql:"+sql);
//            rs.executeSql(sql);
//            //创建节点
//            if (rs.next()) {
//                wfid = rs.getInt("workflowid");
//                creater = Util.getIntValue(rs.getString("creater"), 0);
//                creatertype = Util.getIntValue(rs.getString("creatertype"), 0);
//                currNodeId = rs.getInt("nodeid");
//                currNodeType =Util.getIntValue(rs.getString("nodetype"), 0);
//            }
//            //获得创建节点的id
//            sql="select id from workflow_currentoperator where requestid="+requestid+" and nodeid="+currNodeId+" and userid="+creater+" and usertype="+creatertype+" order by id";
//            //System.out.println("sql1:"+sql);
//            int id=0;
//            rs.executeSql(sql);
//            if(rs.next()){
//                id=rs.getInt(1);
//                //更新流程基本信息
//                sql=" update workflow_requestbase set "
//                        + " lastnodeid = "
//                        + currNodeId
//                        + " ,lastnodetype = '"
//                        + currNodeType
//                        + "' ,currentnodeid = "
//                        + currNodeId
//                        + " ,currentnodetype = '"
//                        + currNodeType
//                        + "' ,status = '"
//                        + SystemEnv.getHtmlLabelName(236,user.getLanguage())
//                        + "'"
//                        + " ,passedgroups = "
//                        + 0
//                        + " ,totalgroups = "
//                        + 0
//                        + " ,lastoperator = "
//                        + userid
//                        + " ,lastoperatedate = '"
//                        + TimeUtil.getCurrentDateString()
//                        + "' "
//                        + " ,lastoperatetime = '"
//                        + TimeUtil.getOnlyCurrentTimeString()
//                        + "' "
//                        + " ,lastoperatortype = "
//                        + usertype
//                        + " where requestid = "
//                        + requestid;
//                //System.out.println("sql2:"+sql);
//                rs.executeSql(sql);
//                //更新创建节点状态
//                sql="update workflow_currentoperator set isremark='0',iscomplete=0, islasttimes=1,isprocessed=null,isreminded=null,viewtype=0, operatedate = '',operatetime='' where requestid ="+requestid+" and nodeid="+currNodeId+" and userid="+creater+" and usertype="+creatertype;
//                //System.out.println("sql3:"+sql);
//                rs.executeSql(sql);
//                //删除流转日志
//                sql="delete from workflow_requestlog where requestid="+requestid;
//                //System.out.println("sql4:"+sql);
//                rs.executeSql(sql);
//                //删除除创建节点外的其它节点
//                sql="delete from workflow_currentoperator where requestid="+requestid+" and id <>"+id;
//                //System.out.println("sql5:"+sql);
//                rs.executeSql(sql);
//                //删除流程提醒
//                poppupRemindInfoUtil.deletePoppupRemindInfo(Integer.parseInt(requestid),0);
//                poppupRemindInfoUtil.deletePoppupRemindInfo(Integer.parseInt(requestid),10);
//                //新到达流程提醒
//                poppupRemindInfoUtil.addPoppupRemindInfo(creater,0,""+creatertype,Util.getIntValue(requestid),requestcominfo.getRequestname(requestid+""));
//                //处理共享
//
//               rs2.execute("select * from workflow_base where id="+wfid);
//                if (rs2.next())
//                        {
//                            isbill=rs2.getInt("isbill");
//                            formid=rs2.getInt("formid");
//
//                        }
//                        if (isbill == 1) {
//                            rs2.executeSql("select tablename from workflow_bill where id = " + formid); // 查询工作流单据表的信息
//                            if (rs2.next())
//                                billtablename = rs2.getString("tablename");          // 获得单据的主表
//                        }
//                try{
//                RequestAddShareInfo shareinfo = new RequestAddShareInfo();
//                shareinfo.setRequestid(Util.getIntValue(requestid));
//                shareinfo.SetWorkFlowID(wfid);
//                shareinfo.SetNowNodeID(currNodeId);
//                shareinfo.SetNextNodeID(currNodeId);
//                shareinfo.setIsbill(isbill);
//                shareinfo.setUser(user);
//                shareinfo.SetIsWorkFlow(1);
//                shareinfo.setBillTableName(billtablename);
//                shareinfo.setHaspassnode(false);
//
//                shareinfo.addShareInfo();
//                }
//                catch (Exception ee){ee.printStackTrace();}
//            }
//        }
//
//        RecordSet rs_1 = new RecordSet();
//        HrmAttVacationManager manager = new HrmAttVacationManager();
//        for (int i = 0; i < requestids.size(); i++) {
//            requestid = (String) requestids.get(i);
//
//            int _wfid = 0;
//            int _currNodeType = 0;
//            int _formid = 0;
//            rs_1.executeProc("workflow_Requestbase_SByID", requestid + "");
//            //当前节点
//            if (rs_1.next()) {
//                _wfid = rs_1.getInt("workflowid");
//                _currNodeType = Util.getIntValue(rs_1.getString("currentnodetype"), 0);
//            }
//
//            rs_1.executeSql("select formid from workflow_base where id = "+_wfid);
//            if (rs_1.next()) {
//                _formid = rs_1.getInt("formid");
//            }
//
//            if(_currNodeType==0){
//            	//added by wcd 2015-09-14
//            	manager.handle(StringUtil.parseToInt(requestid), _wfid, 1);
//                try {
//                    FnaCommon fnaCommon = new FnaCommon();
//                    fnaCommon.doWfForceOver(Util.getIntValue(requestid, 0), 0, true);
//                }catch(Exception easi) {
//                    new BaseBean().writeLog(easi);
//                }
//            }
//        }
//    }
//
//  /**
//    * Description: 收回权限判断，如果是流程监控人员，判断最后一个操作人后面的那个操作人是否已查看
//    * @param requestid 流程id
//    * @param userid 用户ID
//    * @param usertype 用户类型
//    * @param userid1 流程监控人ID
//    * @param usertype1 流程监控人类型
//    * @return 是否有收回权限
//    */
//    public boolean isHavePurview(
//        int requestid,
//        int userid,
//        int usertype,
//        int userid1,
//        int usertype1) {
//        boolean flag = false;
//        int wfid = -1;
//        int nodeid_rb = -1;
//        int nodetype = -1;
//        int currnodeid=-1;
//        String retract = "";
//        usertype = (usertype==1) ? 0 : 1;
//        if (userid1 != -1) {
//            userid = userid1;
//            usertype = usertype1;
//        }
//        boolean isoverrb = false ; //归档节点强制收回
//        RecordSet rs = new RecordSet();
//        if (isLastOperator(requestid, userid, usertype,userid1)) {
//            rs.executeProc("workflow_Requestbase_SByID", requestid + "");
//            if (rs.next()) {
//                wfid = rs.getInt("workflowid");
//                nodeid_rb=rs.getInt("currentnodeid");
//                nodetype=rs.getInt("currentnodetype");
//                currnodeid=nodeid_rb;
//            }
//
//            rs.executeSql("select isoverrb,isoveriv from workflow_base where id="+wfid);
//            if(rs.next()){
//                if(Util.null2String(rs.getString("isoverrb")).equals("1")){
//                    isoverrb = true ;
//                }
//            }
////          rs.executeSql(
////              "select nodeid from workflow_currentoperator where requestid = "
////                  + requestid
////                  + " and isremark='2' and userid = "
////                  + userid
////                  + " and usertype = "
////                  + usertype
////                  + " order by id desc");
//        //  if (rs.next()) {
//        //      nodeid_rb = rs.getInt("nodeid");
//        //  }
//            if (userid1 != -1) {
//                nodeid_rb=-1;
//                //监控流程不受流程里的配置影响
//                flag = true;
//            } else {
//                rs.executeSql("select * from workflow_function_manage where workflowid = "
//                            + wfid
//                            + " and operatortype = "
//                            + nodeid_rb);
//                if (rs.next()) {
//                    retract = rs.getString("retract");
//                }
//                //流程监控的时候，一旦流程回到初始，就不能回收,因为强制归档可以归档到非流程指定人处，所以流程归档后不在收回 by ben 2006-4-3
//                rs.executeSql("select * from workflow_requestlog where logtype!='7' and logtype!='9' and logtype!='1' and requestid="+requestid);
//                if (rs.next())
//                {
//                    if(!isoverrb&&nodetype==3){
//                        // 未开启流程归档节点
//                        flag = false ;
//                    }else{
//                        if ("1".equals(retract)) { //查看前收回
//                            if (!isNextOperatorView(requestid,userid,usertype,userid1)) {
//                                flag = true;
//                            }
//                        } else if ("2".equals(retract)) {
//                            flag = true;
//                        } else {
//                        }
//
//                    }
//                }
//            }
//
//        }
//      //分叉的第一个节点和合并节点不能收回
//      if(flag){
//          WFLinkInfo wflinkinfo=new WFLinkInfo();
//          ArrayList innodeids=wflinkinfo.getCannotDrowBackNode(wfid);
//          if(innodeids.indexOf(""+currnodeid)>-1) flag=false;
//      }
//
//          //排查一些无法收回的情况
//          if (flag) {
//              flag = RequestForceDrawBack.isHavePurview(userid, requestid, userid1 != -1);
//          }
//        return flag;
//
//    }
///**
// * Description: 判断下一个操作人是否查看过，如果是流程监控人员，判断最后一个操作人后面的那个操作人是否已查看
// * @param requestid 流程id
// * @param userid1 用户ID
// * @param usertype1 用户类型
// * @param userid2 用户ID，判断时候流程监控人员
// * @return 是否查看过
// */
//
//    public boolean isNextOperatorView(int requestid, int userid1, int usertype1, int userid2) {
//        boolean flag = false;
//        int userid = userid1;
//        int usertype = usertype1;
//        String datemaxcurruserid = " ";
//        String timemaxcurruserid = " ";
//        int crtnodeid = -1;
//        RecordSet rs = new RecordSet();
//        try {
//            if (userid2!=-1)  //如果是流程监控人员.判断最后一个操作人后面的那个操作人是否已查看
//            {
//                rs.executeSql("select max(logid), operator,operatortype from workflow_requestlog where requestid="+requestid+" and (logtype='2' or logtype='0') and exists(select 1 from workflow_currentoperator where requestid=workflow_requestlog.requestid and userid=workflow_requestlog.operator and usertype=workflow_requestlog.operatortype and isremark='2' and preisremark='0' and operatedate is not null and operatedate>' ') group by operator,operatortype order by max(logid) desc");
//                if(rs.next()){
//                    int tmpuserid=Util.getIntValue(rs.getString("operator"));
//                    int tmpusertype=Util.getIntValue(rs.getString("operatortype"),0);
//                    rs.executeSql(
//                        "select userid,operatedate,operatetime, nodeid from workflow_currentoperator where requestid = "
//                            + requestid
//                            + " and isremark = '2' "
//                            + " and userid="+tmpuserid+" and usertype="+tmpusertype+" and preisremark = '0' and operatedate is not null and operatedate>' '"
//                            + " order by operatedate desc ,operatetime desc");
//                    if (rs.next()) {
//                        datemaxcurruserid =
//                            Util.null2String(rs.getString("operatedate"));
//                        timemaxcurruserid =
//                            Util.null2String(rs.getString("operatetime"));
//                        userid=rs.getInt("userid");
//                        crtnodeid = rs.getInt("nodeid");
//                    }
//                }else{
//                    rs.executeSql(
//                        "select userid,operatedate,operatetime, nodeid from workflow_currentoperator where requestid = "
//                            + requestid
//                            + " and isremark = '2' "
//                            + " and preisremark = '0' and operatedate is not null and operatedate>' '"
//                            + " order by operatedate desc ,operatetime desc");
//                    if (rs.next()) {
//                        datemaxcurruserid =
//                            Util.null2String(rs.getString("operatedate"));
//                        timemaxcurruserid =
//                            Util.null2String(rs.getString("operatetime"));
//                        crtnodeid = rs.getInt("nodeid");
//                    }
//                }
//
//
//            }
//            else
//            {
//            rs.executeSql(
//                "select operatedate,operatetime, nodeid from workflow_currentoperator where requestid = "
//                    + requestid
//                    + " and isremark = '2' "
//                    + " and userid = "
//                    + userid
//                    + " and usertype = "
//                    + usertype
//                    + " and preisremark = '0' and operatedate is not null and operatedate>' '"
//                    + " order by operatedate desc ,operatetime desc");
//            if (rs.next()) {
//                datemaxcurruserid =
//                    Util.null2String(rs.getString("operatedate"));
//                timemaxcurruserid =
//                    Util.null2String(rs.getString("operatetime"));
//                crtnodeid = Util.getIntValue(Util.null2String(rs.getString("nodeid")));
//            }
//            }
//            //----------------------------------------------------------
//            // 已查看需满足以下几个条件：
//            // 1.该操作者所在节点为当前节点的下一节点（可能为多个）
//            // 2.该操作者不为当前节点操作者，且未操作（已操作则直接return），
//            // 3.该操作者接收到流程时间大于等于当前节点的操作时间（流程可能经过此节点的次数不止一次，此处取最后一次）
//            // 4.该操作者的操作时间（查看）大于等于当前节点的操作时间（已经查看）
//             //----------------------------------------------------------
//            int isSelectRejectJudge =0;
//            String isSelectRejectNodeSql =" select isSelectRejectNode from workflow_flownode where workflowid = (select workflowid  from workflow_requestbase where requestid = "+requestid+ ") and nodeid= " +crtnodeid;
//            rs.executeSql(isSelectRejectNodeSql);
//            if(rs.next()){
//                isSelectRejectJudge = rs.getInt("isSelectRejectNode");
//            }
//            String nextnodeviewSql ="";
//            //自由退回情况下不判断目标节点了。
//            if(isSelectRejectJudge==1){
//                 nextnodeviewSql = "select operatedate,operatetime from workflow_currentoperator where requestid=" + requestid
//                + " and userid!=" + userid
//                + " and isremark in ('0','4') "
//                + " and receivedate >= '" + datemaxcurruserid + "' AND receivetime >= '" + timemaxcurruserid + "'"
//                + " and (operatedate>'" + datemaxcurruserid + "' or (operatedate='" + datemaxcurruserid + "' and operatetime>'" + timemaxcurruserid + "'))"
//                ;
//            }else{
//                 nextnodeviewSql = "select operatedate,operatetime from workflow_currentoperator where requestid=" + requestid
//                + " and userid!=" + userid
//                + " and nodeid in (select destnodeid from workflow_nodelink nl ,workflow_nodebase nd where nd.id = nl.nodeid and nodeid = " + crtnodeid
//                //update by liaodong for qc72283 in 20131014 start
//                + " union select distinct destnodeid from workflow_penetrateLog where  requestid = "+ requestid
//                //end
//                + ")"
//                + " and isremark in ('0','4') "
//                + " and receivedate >= '" + datemaxcurruserid + "' AND receivetime >= '" + timemaxcurruserid + "'"
//                + " and (operatedate>'" + datemaxcurruserid + "' or (operatedate='" + datemaxcurruserid + "' and operatetime>'" + timemaxcurruserid + "'))"
//                ;
//            }
//            rs.executeSql(nextnodeviewSql);
//            if (rs.next()) {
//                flag = true;
//            }
//
//        } catch (Exception e) {
//            flag = false;
//            writeLog(e);
//
//        }
//        return flag;
//    }
//
//    /**
//     * 判断某操作人对于某流程是否是最后操作者，如果是流程监控人员，不用判断是否是最后一个操作人
//     * @param requestid 流程id
//     * @param userid1 用户ID
//     * @param usertype1 用户类型
//     * @param userid2 用户ID，判断时候流程监控人员
//     * @return 否是最后操作者
//     */
//    public boolean isLastOperator(
//        int requestid,
//        int userid1,
//        int usertype1,int userid2) {
//        boolean flag = false;
//        int userid = userid1;
//        int usertype = usertype1;
//        String datemaxcurruserid = " ";
//        String timemaxcurruserid = " ";
//        int groupid = -1;
//        int groupdetailid = -1;
//        int nodeid = 0;
//        RecordSet rs = new RecordSet();
//
//        try {
//            if (userid2!=-1)
//            {
//                flag = true;
//            }
//            else
//            {
//                String sql = ""+
//                "select groupid, groupdetailid, operatedate,operatetime,nodeid from workflow_currentoperator where requestid = "
//                    + requestid
//                    + " and ((isremark = '2' and preisremark = '0') or (isremark = '2' and preisremark = '7') or (isremark = '0' and takisremark='-2')) "
//                    + " and userid = "
//                    + userid
//                    + " and usertype = "
//                    + usertype
//                    + " and operatedate is not null and operatedate>' '"
//                    + " order by operatedate desc ,operatetime desc";
//
//                rs.executeSql(sql);
//                if (rs.next()) {
//                    datemaxcurruserid = Util.null2String(rs.getString("operatedate"));
//                    timemaxcurruserid = Util.null2String(rs.getString("operatetime"));
//                    nodeid = Util.getIntValue(rs.getString("nodeid"), 0);
//                    groupid = Util.getIntValue(rs.getString("groupid"), 0);
//                    groupdetailid = Util.getIntValue(rs.getString("groupdetailid"), 0);
//                }
//                else
//                {
//                flag = false;
//                return flag;
//                }
//
//                sql = "select operatedate,operatetime from workflow_currentoperator where requestid = "
//                        + requestid
//                        + " and ((isremark = '2' and preisremark = '0') or (isremark = '2' and preisremark = '7'))"
//                        + " and userid <> "
//                        + userid
//                        + " and (operatedate > '"
//                        + datemaxcurruserid
//                        + "' or (operatedate = '"
//                        + datemaxcurruserid
//                        + "' and operatetime > '"
//                        + timemaxcurruserid
//                        + "'))";
//
//                rs.executeSql(sql);
//                if (!rs.next()) {
//                    flag = true;
//                }
//            //如果能够找到，和当前操作者处于同一节点，但是还是代办（节点操作者）的情况，那么当前操作者也不是最后一个操作人。
//            //仅根据操作时间，可能是查看，而不是提交
//            //如果操作组都相同，认为是依次逐个提交
//            /*
//            String sql_t = "select id from workflow_currentoperator where requestid="+requestid+" and nodeid="+nodeid+" and isremark ='0'";
//            if(groupid>-1 && groupdetailid>-1){
//                sql_t += " and groupid<>"+groupid+" and groupdetailid<>"+groupdetailid;
//            }
//
//            rs.execute(sql_t);
//            if(rs.next()){
//                flag = false;
//            }
//            */
//            }
//        } catch (Exception e) {
//            flag = false;
//            writeLog(e);
//
//        }
//        return flag;
//    }
//
//    /**
//     * 判断是否是流程中间节点 退回到主干节点的
//     * @param requestid
//     * @param userid
//     * @param currentnodeid 流程当前节点
//     * @param usernodeid 用户所在节点
//     * @return
//     */
//	public boolean isBrancheRejectToMain(int requestid,int userid,int currentnodeid,int usernodeid){
//    	boolean isrejecttomain = false ;
//    	//System.out.println("requestid="+requestid+" curretnodeatr="+curretnodeatr+" usernodeatr="+usernodeatr+" isrejecttomain="+isrejecttomain);
//    	return isrejecttomain ;
//    }
//
//	public boolean checkOperatorIsremark(int requestid, int userid, int usertype, int isremark, String coadsigntype) {
//	    //新的强制收回规则：允许转发状态和抄送状态下收回
//        if ((isremark != 5) || (isremark == 7 && !"2".equals(coadsigntype))) {
//            return true;
//        }
//        return false;
//    }
//
//	public boolean checkOperatorIsremark(int requestid, int userid, int usertype, int isremark) {
//	    //新的强制收回规则：允许转发状态和抄送状态下收回
//        if (isremark != 5 && isremark != 7) {
//            return true;
//        }
//        return false;
//    }
//}
