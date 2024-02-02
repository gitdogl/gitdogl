package weaver.interfaces.zkd.dn2023.bhl.entity;

/**
 * @BelongsProject: ZKD_DA_2023
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.bhl.entity
 * @Author: hf_zhangyuan
 * @CreateTime: 2023-03-07  14:50
 * @Description: 流转过程
 * @Version: 1.0
 */

public class BussDataDto {
    /**业务流转顺序*/private String action_sort;
    /**人员或机构名称*/private String person;
    /**流转时间*/private String date_time;
    /**业务行为*/private String action;
    /**业务描述*/private String description;
    /**行为依据*/private String basis;

    /**
     * 获取 业务流转顺序
     *
     * @return action_sort 业务流转顺序
     */
    public String getAction_sort() {
        return this.action_sort;
    }

    /**
     * 设置 业务流转顺序
     *
     * @param action_sort 业务流转顺序
     */
    public void setAction_sort(String action_sort) {
        this.action_sort = action_sort;
    }

    /**
     * 获取 人员或机构名称
     *
     * @return person 人员或机构名称
     */
    public String getPerson() {
        return this.person;
    }

    /**
     * 设置 人员或机构名称
     *
     * @param person 人员或机构名称
     */
    public void setPerson(String person) {
        this.person = person;
    }

    /**
     * 获取 流转时间
     *
     * @return date_time 流转时间
     */
    public String getDate_time() {
        return this.date_time;
    }

    /**
     * 设置 流转时间
     *
     * @param date_time 流转时间
     */
    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    /**
     * 获取 业务行为
     *
     * @return action 业务行为
     */
    public String getAction() {
        return this.action;
    }

    /**
     * 设置 业务行为
     *
     * @param action 业务行为
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * 获取 业务描述
     *
     * @return description 业务描述
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * 设置 业务描述
     *
     * @param description 业务描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取 行为依据
     *
     * @return basis 行为依据
     */
    public String getBasis() {
        return this.basis;
    }

    /**
     * 设置 行为依据
     *
     * @param basis 行为依据
     */
    public void setBasis(String basis) {
        this.basis = basis;
    }
}
