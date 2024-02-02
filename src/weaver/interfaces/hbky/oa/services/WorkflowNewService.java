package weaver.interfaces.hbky.oa.services;

import weaver.workflow.webservices.WorkflowBaseInfo;
import weaver.workflow.webservices.WorkflowRequestInfo;
import weaver.workflow.webservices.WorkflowRequestLog;

public interface WorkflowNewService
{
	/**
	 * 代办列表：getToDoWorkflowRequestList
	 *根据参数条件获取需处理任务列表排除创建节点任务
	 * @param pageNo 当前页数
	 * @param pageSize 每页的分页数量
	 * @param recordCount 总行数
	 * @param workcode 用户工号
	 * @param conditions 查询流程的条件,为字符串数组类型
	 * @return WorkflowRequestInfo []：请求基本信息列表
	 */
    public abstract WorkflowRequestInfo[] getToDoWorkflowRequestList(int pageNo, int pageSize, int recordCount, String workcode, String[] conditions);
   /**
    * 代办数量:getToDoWorkflowRequestCount
    * 返回满足条件的用户可以得到的代办数目
    * @param workcode 用户工号
    * @param conditions 查询流程的条件,为字符串数组
    * @return
    */
    public abstract int getToDoWorkflowRequestCount(String workcode, String[] conditions);
   /**
    *  
    *  得到所有抄送流程列表getCCWorkflowRequestList根据参数条件获得抄送流程列表
    * @param pageNo 当前页数
    * @param pageSize 每页的分页数量
    * @param recordCount 总行数
    * @param workcode 用户工号
    * @param conditions 查询流程的条件,为字符串数组类型
    * @return 根据参数条件获得抄送流程列表
    */
    public abstract WorkflowRequestInfo[] getCCWorkflowRequestList(int pageNo, int pageSize, int recordCount, String workcode, String[] conditions);
   /**
    * 得到所有抄送的流程数量getCCWorkflowRequestCount
    * @param workcode 用户工号
    * @param conditions 查询条件字符串数组
    * @return
    */
    public abstract int getCCWorkflowRequestCount(String workcode, String[] conditions);
   
    /**
     * 取得已办流程列表getHendledWorkflowRequestList
     * @param pageNo 当前页数
     * @param pageSize 每页的分页数量
     * @param recordCount 总行数
     * @param workcode 用户工号
     * @param conditions 查询流程的条件,为字符串数组类型
     * @return 根据参数条件获得已办流程列表
     */
    public abstract WorkflowRequestInfo[] getHendledWorkflowRequestList(int pageNo, int pageSize, int recordCount, String workcode, String[] conditions);
   /**
    * 取得已办工作流类型数量getHendledWorkflowRequestCount

    * @param workcode 用户工号
    * @param conditions  查询条件字符串数组
    * @return
    */
    public abstract int getHendledWorkflowRequestCount(String workcode, String[] conditions);
    /**
     * 取得归档工作流类型列表
     * @param pageNo 当前页数
     * @param pageSize 每页的分页数量
     * @param recordCount 总行数
     * @param workcode 用户工号
     * @param conditions 查询流程的条件,为字符串数组类型
     * @return 根据参数条件获得归档流程列表
     */
    public abstract WorkflowRequestInfo[] getProcessedWorkflowRequestList(int pageNo, int pageSize, int recordCount, String workcode, String[] conditions);
    /**
     * 取得归档工作流类型数量

     * @param workcode 用户工号
     * @param conditions  查询条件字符串数组
     * @return
     */
    public abstract int getProcessedWorkflowRequestCount(String workcode, String[] conditions);
    /**
     * 取得已办流程列表
     * @param pageNo 当前页数
     * @param pageSize 每页的分页数量
     * @param recordCount 总行数
     * @param workcode 用户工号
     * @param conditions 查询流程的条件,为字符串数组类型
     * @return 根据参数条件获得已办流程列表
     */
    public abstract WorkflowRequestInfo[] getMyWorkflowRequestList(int pageNo, int pageSize, int recordCount, String workcode, String[] conditions);
    /**
     * 取得已办工作流类型数量

     * @param workcode 用户工号
     * @param conditions  查询条件字符串数组
     * @return
     */
    public abstract int getMyWorkflowRequestCount(String workcode, String[] conditions);
    /**
     * 取得所有数量

     * @param workcode 用户工号
     * @param conditions  查询条件字符串数组
     * @return
     */
    public abstract int getAllWorkflowRequestCount(String workcode, String[] conditions);
    /**
     * 取得所有流程列表
     * @param pageNo 当前页数
     * @param pageSize 每页的分页数量
     * @param recordCount 总行数
     * @param workcode 用户工号
     * @param conditions 查询流程的条件,为字符串数组类型
     * @return 根据参数条件获得所有流程列表
     */
    public abstract WorkflowRequestInfo[] getAllWorkflowRequestList(int pageNo, int pageSize, int recordCount, String workcode, String[] conditions);
    /**
     * 取得流程详细信息
     * @param requestid 请求id
     * @param workcode 用户工号
     * @param fromrequestid 从相关id的工作流过来
     * @return 取得流程详细信息
     */
    public abstract WorkflowRequestInfo getWorkflowRequest(int requestid, String workcode, int fromrequestid);
   
    /**
     * 流程提交
     * @param workflowrequestinfo 请求信息对象
     * @param requestid 请求id
     * @param userid 提交人ID
     * @param type 类型
     * @param remark 提交意见
     * @return 流程提交
     */
    public abstract String submitWorkflowRequest(WorkflowRequestInfo workflowrequestinfo, int requestid, String workcode, String type, String remark);
   /**
    *  流程转发：
    * @param requestid 请求id
    * @param forwardoperator 接收人id 多个用逗号分隔
    * @param remark 转发意见
    * @param workcode 用户用户
    * @param clientip 客户端ip地址
    * @return 流程转发：
    */
    public abstract String forwardWorkflowRequest(int requestid, String forwardoperator, String remark, String workcode, String clientip);
  
    /**
     * 取得已办流程列表getHendledWorkflowRequestList
     * @param pageNo 当前页数
     * @param pageSize 每页的分页数量
     * @param recordCount 总行数
     * @param workcode 用户工号
     * @param conditions 查询流程的条件,为字符串数组类型
     * @return 根据参数条件获得已办流程列表
     */
    public abstract WorkflowBaseInfo[] getCreateWorkflowTypeList(int pageNo, int pageSize, int recordCount, String workcode, String[] conditions);
    /**
     * 取得创建工作流类型数量

     * @param workcode 用户工号
     * @param conditions  查询条件字符串数组
     * @return
     */
    public abstract int getCreateWorkflowTypeCount(String workcode, String[] conditions);   
    /**
     * 创建新流程
     * @param workflowrequestinfo 请求信息对象
     * @param workcode 用户编号
     * @return 请求信息对象
     */
    public abstract String doCreateWorkflowRequest(WorkflowRequestInfo workflowrequestinfo, String workcode);
    /**
     * 创建新流程
     * @param requestid 请求信息对象
     * @param workcode 用户编号
     * @return 请求信息对象
     */
    public abstract boolean deleteRequest(int requestid, String workcode);
  
}