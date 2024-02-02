package weaver.interfaces.jjc.oaclient.cw.dto;

/**
 * 付款信息
 */
public class PaymentInformationDto {
    //id
    private int id;
    //收款人
    private String skr;
    //收款账号
    private String skzh;
    //开户行
    private String skzh1;
    //收款金额
    private double skje;
    //累加收款金额
    private double lskje;
    //预算科目
    private String kmh;
    //实际金额
    private double sjje;
    //流程明细id
    private int lcmxid;
    //资金来源id
    private int zjlyid;
    //支出科目
    private String zckm;

    /**
     * 获取 id
     *
     * @return id id
     */
    public int getId() {
        return this.id;
    }

    /**
     * 设置 id
     *
     * @param id id
     */
    public void setId(int id) {
        this.id = id;
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
     * @return skzh1 开户行
     */
    public String getSkzh1() {
        return this.skzh1;
    }

    /**
     * 设置 开户行
     *
     * @param skzh1 开户行
     */
    public void setSkzh1(String skzh1) {
        this.skzh1 = skzh1;
    }

    /**
     * 获取 收款金额
     *
     * @return skje 收款金额
     */
    public double getSkje() {
        return this.skje;
    }

    /**
     * 设置 收款金额
     *
     * @param skje 收款金额
     */
    public void setSkje(double skje) {
        this.skje = skje;
    }

    /**
     * 获取 累加收款金额
     *
     * @return lskje 累加收款金额
     */
    public double getLskje() {
        return this.lskje;
    }

    /**
     * 设置 累加收款金额
     *
     * @param lskje 累加收款金额
     */
    public void setLskje(double lskje) {
        this.lskje = lskje;
    }

    /**
     * 获取 预算科目
     *
     * @return kmh 预算科目
     */
    public String getKmh() {
        return this.kmh;
    }

    /**
     * 设置 预算科目
     *
     * @param kmh 预算科目
     */
    public void setKmh(String kmh) {
        this.kmh = kmh;
    }

    /**
     * 获取 实际金额
     *
     * @return sjje 实际金额
     */
    public double getSjje() {
        return this.sjje;
    }

    /**
     * 设置 实际金额
     *
     * @param sjje 实际金额
     */
    public void setSjje(double sjje) {
        this.sjje = sjje;
    }

    /**
     * 获取 流程明细id
     *
     * @return lcmxid 流程明细id
     */
    public int getLcmxid() {
        return this.lcmxid;
    }

    /**
     * 设置 流程明细id
     *
     * @param lcmxid 流程明细id
     */
    public void setLcmxid(int lcmxid) {
        this.lcmxid = lcmxid;
    }

    /**
     * 获取 资金来源id
     *
     * @return zjlyid 资金来源id
     */
    public int getZjlyid() {
        return this.zjlyid;
    }

    /**
     * 设置 资金来源id
     *
     * @param zjlyid 资金来源id
     */
    public void setZjlyid(int zjlyid) {
        this.zjlyid = zjlyid;
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
