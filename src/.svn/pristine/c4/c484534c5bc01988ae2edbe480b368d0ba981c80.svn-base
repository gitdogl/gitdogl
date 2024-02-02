//package weaver.interfaces.jjc.oaclient.cw.action;
//
//import weaver.interfaces.jjc.oaclient.cw.service.ReimbursementService;
//import weaver.interfaces.workflow.action.Action;
//import weaver.soa.workflow.request.RequestInfo;
//import weaver.workflow.request.RequestManager;
//
///**
// * 22年 差旅费报销生成付款单据
// */
//public class ReimbursementAction implements Action {
//    @Override
//    public String execute(RequestInfo requestInfo) {
//        RequestManager requestManager = requestInfo.getRequestManager();
//        try {
//            ReimbursementService oaStatementsService = new ReimbursementService();
//            oaStatementsService.exec(requestInfo.getRequestid(), requestManager.getBillTableName());
//        } catch (Exception e) {
//            System.out.println("发生问题请求ID：" + requestManager.getRequestid());
//            e.printStackTrace();
//            requestManager.setMessageid("60001");
//            requestManager.setMessagecontent(e.getMessage());
//            return Action.FAILURE_AND_CONTINUE;
//        }
//        return Action.SUCCESS;
//    }
//}
