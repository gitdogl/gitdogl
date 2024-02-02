//package weaver.interfaces.workflow.action.javacode;
//
//import weaver.conn.RecordSet;
//import weaver.formmode.setup.ModeRightInfo;
//import weaver.general.Util;
//import weaver.hrm.User;
//import weaver.interfaces.workflow.action.Action;
//import weaver.general.BaseBean;
//import weaver.general.BaseBean;
//import weaver.soa.workflow.request.RequestInfo;
//import weaver.workflow.request.RequestManager;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.UUID;
//
///**
// * 报销发起对项目余额进行控制 * 在线自定义action接口
// */
//public class Action20220531010441 extends BaseBean implements Action {
//    /**
//     * 流程路径节点后选择aciton后,会在节点提交后执行此方法。
//     */
//    public String execute(RequestInfo request) {
//        RequestManager requestManager = request.getRequestManager();
//        try {
//            int requestid = requestManager.getRequestid();
//            String tableName = requestManager.getBillTableName();
//            String billid=getBillId(requestid,tableName);
//            String sql = "select xmmc,bxjehjy from " + tableName + " where requestid = " + requestid;
//            RecordSet rs = new RecordSet();
//            RecordSet rs2 = new RecordSet();
////            RecordSet rs3 = new RecordSet();
////            double zje=0.00;
//            rs.execute(sql);
//            rs.next();
//            String xmmc = rs.getString("xmmc");
//            double bxjehjy = Util.getDoubleValue(rs.getString("bxjehjy"));
//            sql = "select syje from xm_view where id = " + xmmc;
//            rs.execute(sql);
////            String sqlzje="select je from uf_bxjldxm where lc=?";
////            rs3.executeQuery(sqlzje,requestid);
////            while (rs3.next()){
////                zje-=rs3.getDouble("je");
////            }
//            double syje = 0;
//            if (rs.next()) {
//                syje = Util.getDoubleValue(rs.getString("syje"), 0);
//                //  syje+=zje;
//            }
//            if (bxjehjy > syje) {
//                throw new Exception("您的报销总额" + bxjehjy + "元,项目余额还剩" + syje + "元,余额不足,请调整!");
//            }
//            String xmcode = getxmcode(xmmc);
//            ModeRightInfo modeRightInfo = new ModeRightInfo();
//            User user = requestManager.getUser();
//            SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyy-MM-dd");
//            SimpleDateFormat hhmmss = new SimpleDateFormat("HH:mm:ss");
//            String uuid = UUID.randomUUID().toString();
//            String insertsql = "INSERT INTO uf_bxjldxm( REQUESTID, XM, JE, ZT, FORMMODEID, MODEDATACREATER, MODEDATACREATERTYPE, MODEDATACREATEDATE," +
//                    " MODEDATACREATETIME, MODEUUID, LC,xmbh) VALUES ( '"+requestid+"', '"+xmmc+"', '-"+bxjehjy+"', '0', " +
//                    "'10501', '"+user.getUID()+"', '0', '"+yyyymmdd.format(new Date())+"', '"+hhmmss.format(new Date())+"', '"+uuid+"', '"+requestid+"','"+xmcode+"')";
//            rs.executeUpdate(insertsql);
//            String sqlsel = "select id from uf_bxjldxm where MODEUUID = '" + uuid + "'";
//            rs2.execute(sqlsel);
//            int id = 0;
//            rs2.next();
//            id = Util.getIntValue(rs2.getString("id"), 0);
//            if (id > 0) {
//                modeRightInfo.setNewRight(true);
//                modeRightInfo.editModeDataShare(user.getUID(), 10501, id);
//            }
//        } catch (Exception e) {
//            System.out.println("发生问题请求ID：" + requestManager.getRequestid());
//            e.printStackTrace();
//            requestManager.setMessageid("60001");
//            requestManager.setMessagecontent(e.getMessage());
//            return Action.FAILURE_AND_CONTINUE;
//        }
//        return Action.SUCCESS;
//    }
//
//    private String getBillId(int requestid,String tableName) {
//        String sql = "select id from "+tableName+" where requestid = ?";
//        RecordSet rs = new RecordSet();
//        rs.executeQuery(sql,requestid);
//        rs.next();
//        String id = Util.null2String(rs.getString("id"));
//        return id;
//    }
//
//    /**
//     * 获取项目编号     * @param xmid     * @return
//     */
//    private String getxmcode(String xmid) {
//        String xmcode = "";
//        String sql = "select xmbh from uf_xmkp where id = " + xmid;
//        RecordSet rs = new RecordSet();
//        rs.execute(sql);
//        rs.next();
//        xmcode = Util.null2String(rs.getString("xmbh"));
//        return xmcode;
//    }
//}