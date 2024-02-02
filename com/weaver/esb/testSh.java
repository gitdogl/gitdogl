package com.weaver.esb;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * Online custom action interface
 */
public class testSh extends BaseBean implements Action {
    /**
     * After selecting aciton after the process path node, this method will be executed after the node is submitted.
     */
    public String execute(RequestInfo request) {
        String requestid = request.getRequestid();
        RecordSet rs = new RecordSet();
        String isinvalid = "";
        String sql = "select isinvalid from workflow_requestoperatelog where requestid='" + requestid + "'";
        while (rs.execute(sql)){
            isinvalid = rs.getString("isinvalid");
        }
        writeLog("isinvalid的值是" + isinvalid);
        if ("1".equals(isinvalid)) {
            String udpatesql = "update uf_fm_c_fksqtz set blzt='3' where c_lcbt='" + requestid + "'";
            rs.executeUpdate(udpatesql, new Object[0]);
        }


        return Action.SUCCESS;
    }
}
