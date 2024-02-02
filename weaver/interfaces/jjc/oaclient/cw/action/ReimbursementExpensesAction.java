package weaver.interfaces.jjc.oaclient.cw.action;


import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import weaver.workflow.request.RequestManager;

public class ReimbursementExpensesAction implements Action {
    @Override
    public String execute(RequestInfo requestInfo) {
        RequestManager requestManager = requestInfo.getRequestManager();
        try {
            RecordSet rs = new RecordSet();
            RecordSet rs2 = new RecordSet();
            String requestid = requestInfo.getRequestid();
            String tableName = requestManager.getBillTableName();
            String sql = "select id from " + tableName + " where requestid = " + requestid;
            rs.execute(sql);
            int id = 0;
            if (rs.next()) {
                id = rs.getInt("id");
            }
            sql = "select dzfp from " + tableName + "_dt1 where mainid = " + id;
            rs.execute(sql);
            while (rs.next()) {
                String dzfp = Util.getPointValue(rs.getString("dzfp"));
                if (!"".equals(dzfp)) {
                    String sql2 = "update uf_dzfp set zt = 0  where id in (" + dzfp + ")";
                    rs2.executeUpdate(sql2);
                }
            }
        } catch (Exception e) {
            System.out.println("发生问题请求ID：" + requestManager.getRequestid());
            e.printStackTrace();
            requestManager.setMessageid("60001");
            requestManager.setMessagecontent(e.getMessage());
            return Action.FAILURE_AND_CONTINUE;
        }
        return Action.SUCCESS;
    }
}
