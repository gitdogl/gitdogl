package weaver.interfaces.zkd.dn2023.oa.entity;

/**
 * @BelongsProject: ZKD_DA_2023
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.oa.entity
 * @Author: hf_zhangyuan
 * @CreateTime: 2023-03-07  17:52
 * @Description: TODO
 * @Version: 1.0
 */

public class OAWorkFlowRequestLogEntity {
    /**
     * 流程请求id
     */
    private String requestId;
    /**
     * 操作者
     */
    private String operator;
    /**
     * 操作审批日期
     */
    private String operateDate;
    /**
     * 操作审批时间
     */
    private String operateTime;
    /**
     * 签字类型
     */
    private String logType;
    /**
     * 签字意见
     */
    private String remark;
    /**
     * 操作者类型
     */
    private String operateType;

    /**
     * 获取 流程请求id
     *
     * @return requestId 流程请求id
     */
    public String getRequestId() {
        return this.requestId;
    }

    /**
     * 设置 流程请求id
     *
     * @param requestId 流程请求id
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * 获取 操作者
     *
     * @return operator 操作者
     */
    public String getOperator() {
        return this.operator;
    }

    /**
     * 设置 操作者
     *
     * @param operator 操作者
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * 获取 操作审批日期
     *
     * @return operateDate 操作审批日期
     */
    public String getOperateDate() {
        return this.operateDate;
    }

    /**
     * 设置 操作审批日期
     *
     * @param operateDate 操作审批日期
     */
    public void setOperateDate(String operateDate) {
        this.operateDate = operateDate;
    }

    /**
     * 获取 操作审批时间
     *
     * @return operateTime 操作审批时间
     */
    public String getOperateTime() {
        return this.operateTime;
    }

    /**
     * 设置 操作审批时间
     *
     * @param operateTime 操作审批时间
     */
    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    /**
     * 获取 签字类型
     *
     * @return logType 签字类型
     */
    public String getLogType() {
        return this.logType;
    }

    /**
     * 设置 签字类型
     *
     * @param logType 签字类型
     */
    public void setLogType(String logType) {
        this.logType = logType;
    }

    /**
     * 获取 签字意见
     *
     * @return remark 签字意见
     */
    public String getRemark() {
        return this.remark;
    }

    /**
     * 设置 签字意见
     *
     * @param remark 签字意见
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取 操作者类型
     *
     * @return operateType 操作者类型
     */
    public String getOperateType() {
        return this.operateType;
    }

    /**
     * 设置 操作者类型
     *
     * @param operateType 操作者类型
     */
    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }
}
