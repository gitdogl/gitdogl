package weaver.interfaces.zkd.dn2023.bhl.entity;

/**
 * @BelongsProject: ZKD_DA_2023
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.bhl.entity
 * @Author: hf_zhangyuan
 * @CreateTime: 2023-03-07  14:35
 * @Description: 收文主表
 * @Version: 1.0
 */

public class SWDataBill {
    /**流程请求id*/private String sou_id;
    /**文号*/private String dabh;
    /**题名*/private String tm;
    /**年度*/private String nd;
    /**紧急程度*/private String jjcd;
    /**成文日期*/private String lwrq;
    /**公文属性*/private String gksx;
    /**责任者*/private String zrz;
    /**所属单位名称*/private String ssdwmc;
    /**归档日期*/private String gdrq;
    /**更新标识*/private String indbisnew;
    /**附件页数*/private String fjys;
    /**密级*/private String mj;
    /**公文种类*/private String gwzl;
    /**收文编号*/private String swbh;
    /**传阅范围*/private String cyfw;
    /**备注*/private String bz;

    /**
     * 获取 流程请求id
     *
     * @return sou_id 流程请求id
     */
    public String getSou_id() {
        return this.sou_id;
    }

    /**
     * 设置 流程请求id
     *
     * @param sou_id 流程请求id
     */
    public void setSou_id(String sou_id) {
        this.sou_id = sou_id;
    }

    /**
     * 获取 文号
     *
     * @return dabh 文号
     */
    public String getDabh() {
        return this.dabh;
    }

    /**
     * 设置 文号
     *
     * @param dabh 文号
     */
    public void setDabh(String dabh) {
        this.dabh = dabh;
    }

    /**
     * 获取 题名
     *
     * @return tm 题名
     */
    public String getTm() {
        return this.tm;
    }

    /**
     * 设置 题名
     *
     * @param tm 题名
     */
    public void setTm(String tm) {
        this.tm = tm;
    }

    /**
     * 获取 年度
     *
     * @return nd 年度
     */
    public String getNd() {
        return this.nd;
    }

    /**
     * 设置 年度
     *
     * @param nd 年度
     */
    public void setNd(String nd) {
        this.nd = nd;
    }

    /**
     * 获取 紧急程度
     *
     * @return jjcd 紧急程度
     */
    public String getJjcd() {
        return this.jjcd;
    }

    /**
     * 设置 紧急程度
     *
     * @param jjcd 紧急程度
     */
    public void setJjcd(String jjcd) {
        this.jjcd = jjcd;
    }

    /**
     * 获取 成文日期
     *
     * @return lwrq 成文日期
     */
    public String getLwrq() {
        return this.lwrq;
    }

    /**
     * 设置 成文日期
     *
     * @param lwrq 成文日期
     */
    public void setLwrq(String lwrq) {
        this.lwrq = lwrq;
    }

    /**
     * 获取 公文属性
     *
     * @return gksx 公文属性
     */
    public String getGksx() {
        return this.gksx;
    }

    /**
     * 设置 公文属性
     *
     * @param gksx 公文属性
     */
    public void setGksx(String gksx) {
        this.gksx = gksx;
    }

    /**
     * 获取 责任者
     *
     * @return zrz 责任者
     */
    public String getZrz() {
        return this.zrz;
    }

    /**
     * 设置 责任者
     *
     * @param zrz 责任者
     */
    public void setZrz(String zrz) {
        this.zrz = zrz;
    }

    /**
     * 获取 所属单位名称
     *
     * @return ssdwmc 所属单位名称
     */
    public String getSsdwmc() {
        return this.ssdwmc;
    }

    /**
     * 设置 所属单位名称
     *
     * @param ssdwmc 所属单位名称
     */
    public void setSsdwmc(String ssdwmc) {
        this.ssdwmc = ssdwmc;
    }

    /**
     * 获取 归档日期
     *
     * @return gdrq 归档日期
     */
    public String getGdrq() {
        return this.gdrq;
    }

    /**
     * 设置 归档日期
     *
     * @param gdrq 归档日期
     */
    public void setGdrq(String gdrq) {
        this.gdrq = gdrq;
    }

    /**
     * 获取 更新标识
     *
     * @return indbisnew 更新标识
     */
    public String getIndbisnew() {
        return this.indbisnew;
    }

    /**
     * 设置 更新标识
     *
     * @param indbisnew 更新标识
     */
    public void setIndbisnew(String indbisnew) {
        this.indbisnew = indbisnew;
    }

    /**
     * 获取 附件页数
     *
     * @return fjys 附件页数
     */
    public String getFjys() {
        return this.fjys;
    }

    /**
     * 设置 附件页数
     *
     * @param fjys 附件页数
     */
    public void setFjys(String fjys) {
        this.fjys = fjys;
    }

    /**
     * 获取 密级
     *
     * @return mj 密级
     */
    public String getMj() {
        return this.mj;
    }

    /**
     * 设置 密级
     *
     * @param mj 密级
     */
    public void setMj(String mj) {
        this.mj = mj;
    }

    /**
     * 获取 公文种类
     *
     * @return gwzl 公文种类
     */
    public String getGwzl() {
        return this.gwzl;
    }

    /**
     * 设置 公文种类
     *
     * @param gwzl 公文种类
     */
    public void setGwzl(String gwzl) {
        this.gwzl = gwzl;
    }

    /**
     * 获取 收文编号
     *
     * @return swbh 收文编号
     */
    public String getSwbh() {
        return this.swbh;
    }

    /**
     * 设置 收文编号
     *
     * @param swbh 收文编号
     */
    public void setSwbh(String swbh) {
        this.swbh = swbh;
    }

    /**
     * 获取 传阅范围
     *
     * @return cyfw 传阅范围
     */
    public String getCyfw() {
        return this.cyfw;
    }

    /**
     * 设置 传阅范围
     *
     * @param cyfw 传阅范围
     */
    public void setCyfw(String cyfw) {
        this.cyfw = cyfw;
    }

    /**
     * 获取 备注
     *
     * @return bz 备注
     */
    public String getBz() {
        return this.bz;
    }

    /**
     * 设置 备注
     *
     * @param bz 备注
     */
    public void setBz(String bz) {
        this.bz = bz;
    }
}
