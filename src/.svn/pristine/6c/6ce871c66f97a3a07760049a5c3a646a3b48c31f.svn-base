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
// * 报销写入使用记录 * 在线自定义action接口
// */
//public class Action20220530080918 extends BaseBean implements Action {
//    /**
//     * 流程路径节点后选择aciton后,会在节点提交后执行此方法。
//     */
//    public String execute(RequestInfo request) {
//        RequestManager requestManager = request.getRequestManager();
//        try {
//            int requestid = requestManager.getRequestid();
//            String tableName = requestManager.getBillTableName();
//            RecordSet rs = new RecordSet();
//            RecordSet rs2 = new RecordSet();
//            RecordSet rs3 = new RecordSet();
//            String bzsql = "select id ,xmmc from " + tableName + " where requestid  = " + requestid;
//            rs.execute(bzsql);
//            rs.next();
//            String billid = rs.getString("id");//主表id
//            String xmmc = rs.getString("xmmc");
//            String sql = "select jzbh,jzkm,kmh,jzxy,jzbhwb,jzdjl,je from " + tableName + "_dt4 where mainid = " + billid;
//            rs.execute(sql);
//            String error = "";
//            int i = 0;
//            while (rs.next()) {
//                String jzbh = rs.getString("jzbh");
//                double je = Util.getDoubleValue(rs.getString("je"));
//                double syje = 0;
//                String sqlt = "select syje from xmzjly_view where id = " + jzbh;
//                rs2.execute(sqlt);
//                double zje=0.00;
//                String sqltest="select 	je from uf_xmbxsyjl where lc=?";
//                rs3.executeQuery(sqltest,requestid);
//                while(rs3.next()){
//                    double jecur=rs3.getDouble("je");
//                    zje-=jecur;
//                }
//                if (rs2.next()) {
//                    syje = Util.getDoubleValue(rs2.getString("syje"), 0);
//                    syje+=zje;
//                }
//                if (je > syje) {
//                    error += "第" + (i + 1) + "行数据您申请的报销" + je + "元,进账金额仅剩余" + syje + "元,请重新调整!";
//                }
//                i++;
//            }
//            if (!"".equals(error)) {
//                throw new Exception(error);
//            }
//            String xmbh = getxmcode(xmmc);
//            sql = "select id,jzbh,jzkm,kmh,jzxy,jzbhwb,jzdjl,je from " + tableName + "_dt4 where mainid = " + billid;
//            rs.execute(sql);
//            while (rs.next()) {
//                String jzbh = rs.getString("jzbh");
//                String jzkm = rs.getString("jzkm");
//                String kmh = rs.getString("kmh");
//                String jzxy = rs.getString("jzxy");
//                String jzdjl = rs.getString("jzdjl");
//                String mxid = rs.getString("id");
//                double je = Util.getDoubleValue(rs.getString("je"));
//                ModeRightInfo modeRightInfo = new ModeRightInfo();
//                User user = requestManager.getUser();
//                SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyy-MM-dd");
//                SimpleDateFormat hhmmss = new SimpleDateFormat("HH:mm:ss");
//                String uuid = UUID.randomUUID().toString();
//                String insertsql = "INSERT INTO uf_xmbxsyjl( mxid,REQUESTID, JE, ZT, JZBH, JZKM, KMH, JZXY, JZDJL, FORMMODEID, MODEDATACREATER, MODEDATACREATERTYPE," + " MODEDATACREATEDATE, MODEDATACREATETIME, MODEDATAMODIFIER, MODEDATAMODIFYDATETIME, MODEUUID, LC, XM,xmbh) " + "VALUES " + "( " +mxid+" , "+ requestid + ", '-" + je + "', '0', '" + jzbh + "', '" + jzkm + "', '" + kmh + "', '" + jzxy + "', '" + jzdjl + "', '10001', '" + user.getUID() + "', '0', '" + yyyymmdd.format(new Date()) + "', '" + hhmmss.format(new Date()) + "', NULL, NULL, '" + uuid + "', '" + requestid + "', '" + xmmc + "','" + xmbh + "')";
//                System.out.println(insertsql);
//                rs.executeUpdate(insertsql);
//                String sqlsel = "select id from uf_xmbxsyjl where MODEUUID = '" + uuid + "'";
//                rs2.execute(sqlsel);
//                int id = 0;
//                rs2.next();
//                id = Util.getIntValue(rs2.getString("id"), 0);
//                if (id > 0) {
//                    modeRightInfo.setNewRight(true);
//                    modeRightInfo.editModeDataShare(user.getUID(), 10001, id);
//                }
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