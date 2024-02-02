package weaver.interfaces.jjc.oaclient.cw.dto;

public class OAPaymentNoteDto2 {
    /**
     * 金额
     */
    private String je;

    /**
     * 未税金额
     */
    private String wsje;
    /**
     * 资金来源
     */
    private String zjly;
    /**
     * 收款人
     */
    private String skr;
    /**
     * 收款账号
     */
    private String skzh;
    /**
     * 开户行
     */
    private String khx;
    /**
     * 支出科目
     */
    private String zckm;

    /**
     * 税额
     */
    private String se;

    /**
     * 联行号
     */
    private String lhh;

    /**
     * 获取 联行号
     *
     * @return lhh 联行号
     */
    public String getLhh() {
        return lhh;
    }

    /**
     * 设置 联行号
     *
     * @return lhh 联行号
     */
    public void setLhh(String lhh) {
        this.lhh = lhh;
    }


    /**
     * 获取 未税金额
     *
     * @return wsje 未税金额
     */
    public String getWsje() {
        return wsje;
    }

    /**
     * 设置 未税金额
     *
     * @return wsje 未税金额
     */
    public void setWsje(String wsje) {
        this.wsje = wsje;
    }

    /**
     * 获取 税额
     *
     * @return se 税额
     */
    public String getSe() {
        return se;
    }

    /**
     * 设置 税额
     *
     * @return se 税额
     */
    public void setSe(String se) {
        this.se = se;
    }

    /**
     * 获取 金额
     *
     * @return je 金额
     */
    public String getJe() {
        return this.je;
    }

    /**
     * 设置 金额
     *
     * @param je 金额
     */
    public void setJe(String je) {
        this.je = je;
    }


    /**
     * 获取 资金来源
     *
     * @return zjly 资金来源
     */
    public String getZjly() {
        return this.zjly;
    }

    /**
     * 设置 资金来源
     *
     * @param zjly 资金来源
     */
    public void setZjly(String zjly) {
        this.zjly = zjly;
    }


    /**
     * 获取 收款人
     *
     * @return skr 收款人
     */
    public String getSkr() {
        return this.skr;
    }

    /**
     * 设置 收款人
     *
     * @param skr 收款人
     */
    public void setSkr(String skr) {
        this.skr = skr;
    }

    /**
     * 获取 收款账号
     *
     * @return skzh 收款账号
     */
    public String getSkzh() {
        return this.skzh;
    }

    /**
     * 设置 收款账号
     *
     * @param skzh 收款账号
     */
    public void setSkzh(String skzh) {
        this.skzh = skzh;
    }

    /**
     * 获取 开户行
     *
     * @return khx 开户行
     */
    public String getKhx() {
        return this.khx;
    }

    /**
     * 设置 开户行
     *
     * @param khx 开户行
     */
    public void setKhx(String khx) {
        this.khx = khx;
    }

    /**
     * 获取 支出科目
     *
     * @return zckm 支出科目
     */
    public String getZckm() {
        return this.zckm;
    }

    /**
     * 设置 支出科目
     *
     * @param zckm 支出科目
     */
    public void setZckm(String zckm) {
        this.zckm = zckm;
    }
}
