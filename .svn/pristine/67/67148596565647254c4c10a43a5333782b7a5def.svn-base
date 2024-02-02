package weaver.interfaces.jjc.oaclient.cw.action;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.jjc.oaclient.cw.util.MaillUtil;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import weaver.workflow.request.RequestManager;

import java.lang.reflect.Method;

/**
 * 报销到达财务节点发送邮件到
 * Created with IntelliJ IDEA.
 * User: 张远
 * Date: 2021/4/7
 * Time: 15:11
 * Description: No Description
 */
public class SendReimbursementEmailAction implements Action {
    @Override
    public String execute(RequestInfo requestInfo) {
        RequestManager requestManager = requestInfo.getRequestManager();
        try {
            RecordSet rs = new RecordSet();
            RecordSet rs2 = new RecordSet();
            String requestid = requestInfo.getRequestid();
            String tableName = requestManager.getBillTableName();
            MaillUtil maillUtil = new MaillUtil();
            String defmailserver = "";//服务地址
            String defmailfrom = "";//邮箱地址
            String defmailpassword = "";//邮件密码
            String infoSql = "select defmailserver,defmailfrom,defmailpassword from systemset";
            rs.execute(infoSql);
            if (rs.next()) {
                defmailserver = rs.getString("defmailserver");
                defmailfrom = rs.getString("defmailfrom");
                defmailpassword = rs.getString("defmailpassword");
            }

            Class var4 = null;
            Method var5 = null;
            Object var6 = null;
            try {
                var4 = Class.forName("weaver.email.EmailEncoder");
                var6 = var4.newInstance();
                var5 = var4.getMethod("DecoderPassword", String.class);
                defmailpassword = Util.null2String(var5.invoke(var6, defmailpassword));
            } catch (Exception var18) {
            }

            String sql = "select b.email from " + tableName + "  a left join hrmresource b on a.bxry = b.id where a.requestid = " + requestid;
            rs.execute(sql);
            String mail = "";
            if (rs.next()) {
                mail = rs.getString("email");
            }
            if (!"".equals(mail)) {
                maillUtil.testSend(mail, "smtp", defmailserver, defmailfrom, defmailpassword);
            }


        } catch (Exception e) {
            System.out.println("发生问题请求ID：" + requestManager.getRequestid());
//            e.printStackTrace();
            requestManager.setMessageid("60001");
            requestManager.setMessagecontent(e.getMessage());
            return Action.SUCCESS;
        }
        return Action.SUCCESS;
    }
}