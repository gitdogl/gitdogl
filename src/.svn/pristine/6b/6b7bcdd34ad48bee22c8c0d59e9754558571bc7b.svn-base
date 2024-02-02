//package weaver.interfaces.ht.oa.Action.Service;
//
//import org.apache.log4j.Logger;
//
//import weaver.conn.RecordSet;
//import weaver.general.BaseBean;
//import weaver.hrm.User;
//import weaver.interfaces.kd.oa.base.BaseDao;
//import weaver.interfaces.workflow.action.Action;
//import weaver.soa.workflow.request.MainTableInfo;
//import weaver.soa.workflow.request.Property;
//import weaver.soa.workflow.request.RequestInfo;
//import weaver.workflow.request.RequestManager;
//
///**
// * 国外合同
// * @author 张远
// *
// */
//public class AbroadContractAction implements Action {
//	public String execute(RequestInfo requestInfo) {
//		RequestManager requestManager = requestInfo.getRequestManager();
//		String requestid = "";
//
//		try {
//			User u = requestManager.getUser();
//			requestid = requestInfo.getRequestid();
//			MainTableInfo mainTableInfo = requestInfo.getMainTableInfo();
//			Property[] propertys = mainTableInfo.getProperty();
//
//			String fjid = "";
//			String fjid2 = "";
//			String bh = "";
//			for (Property property : propertys) {
//				if (property.getName().equals("dlht")) {
//					fjid = property.getValue();
//				}
//				if (property.getName().equals("wmht")) {
//					fjid2 = property.getValue();
//				}
//				if (property.getName().equals("bh")) {
//					bh = property.getValue();
//				}
//			}
//
//
//
//
//			String[] fjids = fjid.split(",");
//
//			String[] fjid2s = fjid2.split(",");
//
//
//			DocToFTP docToFTP = new DocToFTP();
//			int zhid1 = docToFTP.getOAdoc(
//			Integer.parseInt(fjids[fjids.length - 1]), "sysadmin",
//			"Ustc_2018",requestid,"",true,bh);
//
//			int zhid2 = docToFTP.getOAdoc(
//					Integer.parseInt(fjid2s[fjid2s.length - 1]), "sysadmin",
//					"Ustc_2018",requestid,"",true,bh);
//			BaseDao base = new BaseDao();
//			String tableName = base.getTableNameByResquestID(requestid);
//			RecordSet rs = new RecordSet();
//
//			rs.executeSql("Update "+tableName+" set htwjzzbb = '" + zhid1
//					+ "',wmhtzzbb = '" + zhid2
//					+ "' where requestid = " + requestid);
//			rs.next();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			requestManager.setMessageid("60001");
//			requestManager.setMessagecontent(e.getMessage());
//			return Action.FAILURE_AND_CONTINUE;
//		}
//		return Action.SUCCESS;
//	}
//}
