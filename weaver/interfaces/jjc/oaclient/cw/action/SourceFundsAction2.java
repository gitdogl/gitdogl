package weaver.interfaces.jjc.oaclient.cw.action;

import weaver.interfaces.jjc.oaclient.cw.service.SourceFundsService2;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import weaver.workflow.request.RequestManager;


public class SourceFundsAction2 implements Action {
    @Override
    public String execute(RequestInfo requestInfo) {
        RequestManager requestManager = requestInfo.getRequestManager();
        try {
            SourceFundsService2 sourceFundsService = new SourceFundsService2();
            sourceFundsService.exec(requestInfo.getRequestid(), requestManager.getBillTableName());
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
