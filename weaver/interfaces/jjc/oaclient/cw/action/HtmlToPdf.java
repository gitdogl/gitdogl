//package weaver.interfaces.jjc.oaclient.cw.action;
//
//
//import weaver.conn.RecordSet;
//import weaver.interfaces.jjc.oaclient.cw.util.DocumentBookmarkRepalceUtil;
//import weaver.interfaces.workflow.action.Action;
//import weaver.soa.workflow.request.RequestInfo;
//import weaver.workflow.request.RequestManager;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 报销获取修改单据pdf
// */
//public class HtmlToPdf implements Action {
//
//    @Override
//    public String execute(RequestInfo requestInfo) {
//        RequestManager requestManager = requestInfo.getRequestManager();
//        try {
//            RecordSet rs = new RecordSet();
//            DocumentBookmarkRepalceUtil documentBookmarkRepalceUtil = new DocumentBookmarkRepalceUtil();
//            String dz = documentBookmarkRepalceUtil.toPdf(requestManager.getUser(), requestManager.getRequestid() + "", "23");
//            String sql = "select bh,xgqbdccpdf from " + requestManager.getBillTableName() + " where requestid = " + requestManager.getRequestid();
//            rs.execute(sql);
//            String bh = "";
//            String xgqbdccpdf = "";
//            if (rs.next()) {
//                bh = rs.getString("bh");
//                xgqbdccpdf = rs.getString("xgqbdccpdf");
//            }
//            String[] xgarr = xgqbdccpdf.split(",");
//            List<String> listStr = new ArrayList<>();
//            for (String a : xgarr) {
//                listStr.add(a);
//            }
//
//
//            int docId = documentBookmarkRepalceUtil.setOAdoc(dz, "历史单据-" + bh + "-" + xgarr.length + ".pdf", requestManager.getUser());
//
//            listStr.add(docId + "");
//            String citiesCommaSeparated = String.join(",", listStr);
//
//            rs.executeUpdate("update " + requestManager.getBillTableName() + " set xgqbdccpdf='" + citiesCommaSeparated + "' where requestid = " + requestManager.getRequestid());
//
//        } catch (Exception e) {
//            System.out.println("发生问题请求ID：" + requestManager.getRequestid());
//            e.printStackTrace();
//            requestManager.setMessageid("60001");
//            requestManager.setMessagecontent(e.getMessage());
//            return Action.FAILURE_AND_CONTINUE;
//        }
//        return Action.SUCCESS;
//    }
//}
