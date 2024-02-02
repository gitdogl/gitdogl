package weaver.interfaces.zkd.dn2023.bhl.entity;

import java.util.List;

/**
 * @BelongsProject: ZKD_DA_2023
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.bhl.entity
 * @Author: hf_zhangyuan
 * @CreateTime: 2023-03-07  14:34
 * @Description: 业务数据结构
 * @Version: 1.0
 */

public class DataBill {
    /**原数据标识*/private String origin_id;
    /**收文/发文目录数据*/private Object arch_data;
    /**原件数据*/private List<OriginFileDto> origin_files;
    /**业务流转数据*/private List<BussDataDto> buss_datas;

    /**
     * 获取 原数据标识
     *
     * @return origin_id 原数据标识
     */
    public String getOrigin_id() {
        return this.origin_id;
    }

    /**
     * 设置 原数据标识
     *
     * @param origin_id 原数据标识
     */
    public void setOrigin_id(String origin_id) {
        this.origin_id = origin_id;
    }



    /**
     * 获取 原件数据
     *
     * @return origin_files 原件数据
     */
    public List<OriginFileDto> getOrigin_files() {
        return this.origin_files;
    }

    /**
     * 设置 原件数据
     *
     * @param origin_files 原件数据
     */
    public void setOrigin_files(List<OriginFileDto> origin_files) {
        this.origin_files = origin_files;
    }

    /**
     * 获取 业务流转数据
     *
     * @return buss_datas 业务流转数据
     */
    public List<BussDataDto> getBuss_datas() {
        return this.buss_datas;
    }

    /**
     * 设置 业务流转数据
     *
     * @param buss_datas 业务流转数据
     */
    public void setBuss_datas(List<BussDataDto> buss_datas) {
        this.buss_datas = buss_datas;
    }

    /**
     * 获取 收文发文目录数据
     *
     * @return arch_data 收文发文目录数据
     */
    public Object getArch_data() {
        return this.arch_data;
    }

    /**
     * 设置 收文发文目录数据
     *
     * @param arch_data 收文发文目录数据
     */
    public void setArch_data(Object arch_data) {
        this.arch_data = arch_data;
    }
}
