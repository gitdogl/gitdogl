//package weaver.interfaces.workflow.action.javacode;
//
//import weaver.conn.RecordSet;
//import weaver.general.BaseBean;
//import weaver.general.Util;
//import weaver.interfaces.workflow.action.Action;
//import weaver.soa.workflow.request.RequestInfo;
//import weaver.workflow.request.RequestManager;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 在线自定义action接口
// */
//public class Action20211025090933 extends BaseBean implements Action {
//    /**
//     * 流程路径节点后选择aciton后,会在节点提交后执行此方法。
//     */
//    public String execute(RequestInfo request) {
//        RequestManager requestManager = request.getRequestManager();
//        String requestId = request.getRequestid();
//        String tablename = request.getRequestManager().getBillTableName();
//        RecordSet rs = new RecordSet();
//        RecordSet rs2 = new RecordSet();
//        rs.execute("select id from " + tablename + " where requestid =  " + requestId);
//        rs.next();
//        String mainid = rs.getString("id");
//        rs.executeUpdate("delete from " + tablename + "_dt8 where mainid = " + mainid);
//
//        String mx = "4,5,6";
//        String[] mxarr = mx.split(",");
//        List<String> xgfpList = new ArrayList<>();
//        for (String mxh : mxarr) {
//            rs.execute("select xgfp from " + tablename + "_dt" + mxh + " where mainid = " + mainid);
//            writeLog("select xgfp from " + tablename + "_dt" + mxh + " where mainid = " + mainid);
//            while (rs.next()) {
//                String xgfpStr = Util.null2String(rs.getString("xgfp"));
//                if (!"".equals(xgfpStr)) {
//                    String[] xgfp = rs.getString("xgfp").split(",");
//                    for (String fp : xgfp) {
//                        if (!"".equals(fp)) {
//                            xgfpList.add(fp);
//                        }
//                    }
//                }
//            }
//        }
//        String error = "";
//        for (String fp : xgfpList) {
//            if (!"".equals(fp)) {
//
//
//                String sql = "select * from FnaInvoiceLedger where id = " + fp;
//                writeLog(sql);
//                rs.execute(sql);
//                if (rs.next()) {
//                    String fprq = Util.null2String(rs.getString("BILLINGDATE"));
//                    String fplx = Util.null2String(rs.getString("INVOICETYPE"));
//                    String hsje = Util.null2String(rs.getString("TAXINCLUDEDPRICE"));
//                    String se = Util.null2String(rs.getString("TAX"));
//                    String sl = Util.null2String(rs.getString("TAXRATE"));
//                    String fptp = Util.null2String(rs.getString("IMAGEID"));
//                    String fpdwb = Util.null2String(rs.getString("IMAGEID"));
//                    String bhsje = Util.null2String(rs.getString("PRICEWITHOUTTAX"));
//                    String fpdm = Util.null2String(rs.getString("INVOICECODE"));
//                    String fphm = Util.null2String(rs.getString("INVOICENUMBER"));
//                    if ("".equals(fpdwb)) {
//                        error += "发票号码:" + fphm + "没找到发票图片!请检查.";
//                        requestManager.setMessageid("60001");
//                        requestManager.setMessagecontent(error);
//                        return Action.FAILURE_AND_CONTINUE;
//                    }
//                    String sql2 = "select IMAGEFILENAME from docImageFile where IMAGEFILEID = " + fpdwb;
//                    writeLog(sql2);
//                    rs2.execute(sql2);
//                    rs2.next();
//                    String fppdfwj = Util.null2String(rs2.getString("IMAGEFILENAME"));
//                    String sql3 = "insert into " + tablename + "_dt8 (mainid,fph,fprq,fplx,hsje,se,sl,fptp,fpdwb,bhsje,fppdfwj,fpdm,fphm) VALUES ('" + mainid + "','" + fp + "','" + fprq + "','" + fplx + "','" + hsje + "','" + se + "','" + sl + "','" + fptp + "','" + fpdwb + "','" + bhsje + "','" + fppdfwj + "','" + fpdm + "','" + fphm + "')";
//                    rs2.executeUpdate(sql3);
//                } else {
//                    error += "发票表未找到id为:" + fp + "的发票,请清空所有选择发票重新选择!";
//                    requestManager.setMessageid("60001");
//                    requestManager.setMessagecontent(error);
//                    return Action.FAILURE_AND_CONTINUE;
//                }
//
//            }
//        }
//
//        return Action.SUCCESS;
//    }
//}
