//package weaver.interfaces.hbky.oa.services;
//
//import weaver.general.*;
//import weaver.workflow.webservices.WorkflowBaseInfo;
//import weaver.workflow.webservices.WorkflowRequestInfo;
//import weaver.workflow.webservices.WorkflowServiceImpl;
//
//public class WorkflowNewServiceImpl extends BaseBean implements
//		WorkflowNewService {
//	public WorkflowNewServiceImpl() {
//		workflowServiceImpl = new WorkflowServiceImpl();
//	}
//
//	private WorkflowServiceImpl workflowServiceImpl;
//
//	/**
//	 * 代办列表：getToDoWorkflowRequestList 根据参数条件获取需处理任务列表排除创建节点任务
//	 *
//	 * Specified by: getToDoWorkflowRequestList(...) in WorkflowNewService
//	 * Parameters: pageNo 当前页数 pageSize 每页的分页数量 recordCount 总行数 workcode 用户工号
//	 * conditions 查询流程的条件,为字符串数组类型 Returns: WorkflowRequestInfo []：请求基本信息列表
//	 */
//	@Overridezhege
//	public WorkflowRequestInfo[] getToDoWorkflowRequestList(int pageNo,
//			int pageSize, int recordCount, String workcode, String[] conditions) {
//
//		int userid = getUserID(workcode);
//		return workflowServiceImpl.getToDoWorkflowRequestList(pageNo, pageSize,
//				recordCount, userid, conditions);
//	}
//
//	@Override
//	public int getToDoWorkflowRequestCount(String workcode, String[] conditions) {
//
//		int userid = getUserID(workcode);
//		return workflowServiceImpl.getToDoWorkflowRequestCount(userid,
//				conditions);
//	}
//
//	@Override
//	public WorkflowRequestInfo[] getCCWorkflowRequestList(int pageNo,
//			int pageSize, int recordCount, String workcode, String[] conditions) {
//
//		int userid = getUserID(workcode);
//		return workflowServiceImpl.getCCWorkflowRequestList(pageNo, pageSize,
//				recordCount, userid, conditions);
//
//	}
//
//	@Override
//	public int getCCWorkflowRequestCount(String workcode, String[] conditions) {
//
//		int userid = getUserID(workcode);
//		return workflowServiceImpl
//				.getCCWorkflowRequestCount(userid, conditions);
//	}
//
//	@Override
//	public WorkflowRequestInfo[] getHendledWorkflowRequestList(int pageNo,
//			int pageSize, int recordCount, String workcode, String[] conditions) {
//
//		int userid = getUserID(workcode);
//		return workflowServiceImpl.getHendledWorkflowRequestList(pageNo,
//				pageSize, recordCount, userid, conditions);
//	}
//
//	@Override
//	public int getHendledWorkflowRequestCount(String workcode,
//			String[] conditions) {
//
//		int userid = getUserID(workcode);
//		return workflowServiceImpl.getHendledWorkflowRequestCount(userid,
//				conditions);
//
//	}
//
//	@Override
//	public WorkflowRequestInfo[] getProcessedWorkflowRequestList(int pageNo,
//			int pageSize, int recordCount, String workcode, String[] conditions) {
//
//		int userid = getUserID(workcode);
//		return workflowServiceImpl.getProcessedWorkflowRequestList(pageNo,
//				pageSize, recordCount, userid, conditions);
//
//	}
//
//	@Override
//	public int getProcessedWorkflowRequestCount(String workcode,
//			String[] conditions) {
//
//		int userid = getUserID(workcode);
//		return workflowServiceImpl.getProcessedWorkflowRequestCount(userid,
//				conditions);
//
//	}
//
//	@Override
//	public WorkflowRequestInfo[] getMyWorkflowRequestList(int pageNo,
//			int pageSize, int recordCount, String workcode, String[] conditions) {
//
//		int userid = getUserID(workcode);
//		return workflowServiceImpl.getMyWorkflowRequestList(pageNo, pageSize,
//				recordCount, userid, conditions);
//
//	}
//
//	@Override
//	public int getMyWorkflowRequestCount(String workcode, String[] conditions) {
//
//		int userid = getUserID(workcode);
//		return workflowServiceImpl
//				.getMyWorkflowRequestCount(userid, conditions);
//
//	}
//
//	@Override
//	public int getAllWorkflowRequestCount(String workcode, String[] conditions) {
//
//		int userid = getUserID(workcode);
//		return workflowServiceImpl.getAllWorkflowRequestCount(userid,
//				conditions);
//
//	}
//
//	@Override
//	public WorkflowRequestInfo[] getAllWorkflowRequestList(int pageNo,
//			int pageSize, int recordCount, String workcode, String[] conditions) {
//
//		int userid = getUserID(workcode);
//		return workflowServiceImpl.getAllWorkflowRequestList(pageNo, pageSize,
//				recordCount, userid, conditions);
//
//	}
//
//	@Override
//	public WorkflowRequestInfo getWorkflowRequest(int requestid,
//			String workcode, int fromrequestid) {
//
//		int userid = getUserID(workcode);
//		return workflowServiceImpl.getWorkflowRequest(requestid, userid,
//				fromrequestid);
//	}
//
//	@Override
//	public String submitWorkflowRequest(
//			WorkflowRequestInfo workflowrequestinfo, int requestid,
//			String workcode, String type, String remark) {
//
//		int userid = getUserID(workcode);
//		return workflowServiceImpl.submitWorkflowRequest(workflowrequestinfo,
//				requestid, userid, type, remark);
//
//	}
//
//	@Override
//	public String forwardWorkflowRequest(int requestid, String forwardoperator,
//			String remark, String workcode, String clientip) {
//		int userid = getUserID(workcode);
//		return workflowServiceImpl.forwardWorkflowRequest(requestid,
//				forwardoperator, remark, userid, clientip);
//
//	}
//
//	@Override
//	public WorkflowBaseInfo[] getCreateWorkflowTypeList(int pageNo,
//			int pageSize, int recordCount, String workcode, String[] conditions) {
//		int userid = getUserID(workcode);
//		return workflowServiceImpl.getCreateWorkflowTypeList(pageNo, pageSize,
//				recordCount, userid, conditions);
//
//	}
//
//	@Override
//	public int getCreateWorkflowTypeCount(String workcode, String[] conditions) {
//
//		int userid = getUserID(workcode);
//		return workflowServiceImpl.getCreateWorkflowTypeCount(userid,
//				conditions);
//
//	}
//
//	@Override
//	public String doCreateWorkflowRequest(
//			WorkflowRequestInfo workflowrequestinfo, String workcode) {
//		int userid = getUserID(workcode);
//		return workflowServiceImpl.doCreateWorkflowRequest(workflowrequestinfo,
//				userid);
//
//	}
//
//	@Override
//	public boolean deleteRequest(int requestid, String workcode) {
//		int userid = getUserID(workcode);
//		return workflowServiceImpl.deleteRequest(requestid, userid);
//
//	}
//
//	/**
//	 * 返回用户ID
//	 *
//	 * @param workcode
//	 *            用户编号
//	 * @return
//	 */
//	private int getUserID(String workcode) {
//		String userid = workflowServiceImpl.getUserId("workcode", workcode);
//		int useridi = Util.getIntValue(userid, 0);
//		if (useridi <= 0) {
//			System.out.println(workcode + "用户编号存在问题");
//			return 0;
//		}
//		return useridi;
//	}
//
//}
