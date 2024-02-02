package weaver.interfaces.jjc.oaclient.cw.action;

import weaver.interfaces.jjc.oaclient.cw.service.ReceiptsService;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import weaver.workflow.request.RequestManager;

/**
 * 2022  收款单
 */
public class ReceiptsAction implements Action {
    @Override
    public String execute(RequestInfo requestInfo) {
        RequestManager requestManager = requestInfo.getRequestManager();
        try {
            ReceiptsService receiptsService = new ReceiptsService();
            receiptsService.exec(requestInfo.getRequestid(), requestManager.getBillTableName());
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
