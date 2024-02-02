package weaver.interfaces.jjc.oaclient.cw.action;

import weaver.interfaces.jjc.oaclient.cw.service.OAStatementsService2;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import weaver.workflow.request.RequestManager;


public class OAStatementsLcAction implements Action {
    @Override
    public String execute(RequestInfo requestInfo) {
        RequestManager requestManager = requestInfo.getRequestManager();
        try {
            OAStatementsService2 oaStatementsService = new OAStatementsService2();
            oaStatementsService.exec(requestInfo.getRequestid(), requestManager.getBillTableName());
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
