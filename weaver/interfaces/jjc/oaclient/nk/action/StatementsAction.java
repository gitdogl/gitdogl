package weaver.interfaces.jjc.oaclient.nk.action;

import weaver.conn.RecordSet;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import weaver.workflow.request.RequestManager;

public class StatementsAction implements Action {
    @Override
    public String execute(RequestInfo requestInfo) {
        RequestManager requestManager = requestInfo.getRequestManager();
        try {
            String tableName = requestManager.getBillTableName();
            int requestid = requestManager.getRequestid();

            RecordSet rs = new RecordSet();
            RecordSet rs2 = new RecordSet();
            RecordSet rs3 = new RecordSet();
            String sql = "select  *  from " + tableName + " where requestid = " + requestid;
            rs.execute(sql);
            int id = 0;
            int jzsrmc = 0;
            if (rs.next()) {
                id = rs.getInt("id");
                jzsrmc = rs.getInt("jzsrmc");
            }
            sql = "select * from " + tableName + "_dt2 where mainid = " + id;
            rs.execute(sql);

            while (rs.next()) {
                String jzdbhjxq = rs.getString("jzdbhjxq");
                String jzd = rs.getString("jzd");
                String sjqrje = rs.getString("sjqrje");
                String mxid = rs.getString("id");
                String sql2 = "select mainid from uf_jzd_dt2 where id = " + jzdbhjxq;
                rs2.execute(sql2);
                String mainid = "";
                if (rs2.next()) {
                    mainid = rs2.getString("mainid");
                }

                sql2 = "select id from uf_jzd_dt1 where jzsrid = " + mxid;
                rs2.execute(sql2);
                if (rs2.next()) {
                    String sql3 = "update uf_jzd_dt1 set jzxmmc='" + jzsrmc + "',zjlx='" + jzd + "',je='" + sjqrje + "' where jzsrid = " + mxid;
                    rs3.executeUpdate(sql3);
                } else {
                    String sql3 = "insert into uf_jzd_dt1 (mainid,jzxmmc,zjlx,je,jzsrid) values ('" + mainid + "','" + jzsrmc + "','" + jzd + "','" + sjqrje + "','" + mxid + "')";
                    System.out.println(sql3);
                    rs3.executeUpdate(sql3);
                }
                String sql3 = " select sum(je) as je from uf_jzd_dt1 where mainid = " + mainid;
                rs2.execute(sql3);
                if (rs2.next()) {
                    double jehj = rs2.getDouble("je");
                    String sql4 = "update uf_jzd set yqrje = " + jehj + " , dqrje = jzje-" + jehj + " where id = " + mainid;
                    rs3.executeUpdate(sql4);
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