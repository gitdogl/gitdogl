package weaver.interfaces.jjc.oaclient.cw.dto;

public class ReimbursementDt2 {

    /**
     * mainid
     */
    private String mainid;
    /**
     * id
     */
    private String id;
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
     * 收款金额（元）
     */
    private String skjey;
    /**
     * 税额
     */
    private String se;
    /**
     * 联行号
     */
    private String lhh;

    /**
     * 获取联行号
     */
    public String getLhh() {
        return lhh;
    }

    /**
     * 设置联行号
     */
    public void setLhh(String lhh) {
        this.lhh = lhh;
    }

    /**
     * 获取税额
     */
    public String getSe() {
        return se;
    }

    /**
     * 设置税额
     */
    public void setSe(String se) {
        this.se = se;
    }

    /**
     * 获取 mainid
     *
     * @return mainid mainid
     */
    public String getMainid() {
        return this.mainid;
    }

    /**
     * 设置 mainid
     *
     * @param mainid mainid
     */
    public void setMainid(String mainid) {
        this.mainid = mainid;
    }

    /**
     * 获取 id
     *
     * @return id id
     */
    public String getId() {
        return this.id;
    }

    /**
     * 设置 id
     *
     * @param id id
     */
    public void setId(String id) {
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
     * 获取 收款金额（元）
     *
     * @return skjey 收款金额（元）
     */
    public String getSkjey() {
        return this.skjey;
    }

    /**
     * 设置 收款金额（元）
     *
     * @param skjey 收款金额（元）
     */
    public void setSkjey(String skjey) {
        this.skjey = skjey;
    }
}
