//package weaver.interfaces.ht.oa.Action.Service;
//
//import org.apache.log4j.Logger;
//import weaver.conn.RecordSet;
//
//import weaver.interfaces.workflow.action.Action;
//import weaver.soa.workflow.request.MainTableInfo;
//import weaver.soa.workflow.request.Property;
//import weaver.soa.workflow.request.RequestInfo;
//import weaver.workflow.request.RequestManager;
//
///**
// * 合同修改附件水印
// */
//public class ContractModificationAnnexAction implements Action {
//    public String execute(RequestInfo requestInfo) {
//        RequestManager requestManager = requestInfo.getRequestManager();
//        String requestid = "";
//
//        try {
//            requestid = requestInfo.getRequestid();
//            String tableName = requestManager.getBillTableName();
//            MainTableInfo mainTableInfo = requestInfo.getMainTableInfo();
//            Property[] propertys = mainTableInfo.getProperty();
//            String fjid = "";
//            String fjid2 = "";
//            String fjid3 = "";
//            String bh = "";
//            for (Property property : propertys) {
//                if (property.getName().equals("htbghwb")) {
//                    fjid = property.getValue();
//                }else if (property.getName().equals("bghwmht")) {
//                    fjid2 = property.getValue();
//                }else if (property.getName().equals("bcxysc")) {
//                    fjid3 = property.getValue();
//                }else if (property.getName().equals("bh")) {
//                    bh = property.getValue();
//                }
//            }
//            RecordSet rs = new RecordSet();
//            DocToFTP docToFTP = new DocToFTP();
//            if (!"".equals(fjid)){
//                String[] fjids = fjid.split(",");
//                int zhid1 = docToFTP.getOAdoc(
//                        Integer.parseInt(fjids[fjids.length - 1]), "sysadmin",
//                        "Ustc_2018",requestid,"",true,bh);
//                String sql = "update "+tableName+" set htbghzzbb = "+zhid1+"  where requestid = "+requestid;
//                rs.executeUpdate(sql);
//            }
//
//            if (!"".equals(fjid2)){
//                String[] fjid2s = fjid2.split(",");
//                int zhid2 = docToFTP.getOAdoc(
//                        Integer.parseInt(fjid2s[fjid2s.length - 1]), "sysadmin",
//                        "Ustc_2018",requestid,"",true,bh);
//                String sql = "update "+tableName+" set bghwmhtzzbb = "+zhid2+" where requestid = "+requestid;
//                rs.executeUpdate(sql);
//            }
//            if (!"".equals(fjid3)){
//                String[] fjid3s = fjid3.split(",");
//                int zhid3 = docToFTP.getOAdoc(
//                        Integer.parseInt(fjid3s[fjid3s.length - 1]), "sysadmin",
//                        "Ustc_2018",requestid,"",true,bh);
//                String sql = "update "+tableName+" set bcxyzzbb = "+zhid3+" where requestid = "+requestid;
//                rs.executeUpdate(sql);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            requestManager.setMessageid("60001");
//            requestManager.setMessagecontent(e.getMessage());
//            return Action.FAILURE_AND_CONTINUE;
//        }
//        return Action.SUCCESS;
//    }
//}
