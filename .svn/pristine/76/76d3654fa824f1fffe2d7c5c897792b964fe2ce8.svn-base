//package weaver.interfaces.ht.oa.Action.Service;
//
//import weaver.conn.RecordSet;
//import weaver.interfaces.workflow.action.Action;
//import weaver.soa.workflow.request.MainTableInfo;
//import weaver.soa.workflow.request.Property;
//import weaver.soa.workflow.request.RequestInfo;
//import weaver.workflow.request.RequestManager;
//
///**
// * 合同变更生成编号
// */
//public class ContractModificationNumberingAction implements Action {
//    @Override
//    public String execute(RequestInfo requestInfo) {
//        RequestManager requestManager = requestInfo.getRequestManager();
//        String requestid = "";
//        RecordSet rs = new RecordSet();
//        try {
//            requestid = requestInfo.getRequestid();
//            String tableName = requestManager.getBillTableName();
//            MainTableInfo mainTableInfo = requestInfo.getMainTableInfo();
//            Property[] propertys = mainTableInfo.getProperty();
//            String htbh = "";
//            for (Property property : propertys) {
//                if (property.getName().equals("htbh")) {
//                    htbh = property.getValue();
//                }
//            }
//            String[] htbhs = htbh.split("-");
//            String sql = "select count(1) as sl  from " + tableName + " where htbh = '" + htbh+"'";
//            rs.execute(sql);
//            int sl = 0;
//            if (rs.next()) {
//                sl = rs.getInt("sl");
//            }
//            htbhs[htbhs.length-1] = sl+"";
//            String newhtbh = "";
//            for(int i = 0 ; i <htbhs.length ; i++){
//                if (i == htbhs.length-1){
//                    newhtbh += htbhs[i];
//                }else{
//                    newhtbh += htbhs[i]+"-";
//                }
//            }
//            sql = "update "+tableName+" set bh = '"+newhtbh+"' where requestid = "+requestid;
//            rs.executeUpdate(sql);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            requestManager.setMessageid("60001");
//            requestManager.setMessagecontent(e.getMessage());
//            return Action.FAILURE_AND_CONTINUE;
//        }
//        return Action.SUCCESS;
//    }
//}
