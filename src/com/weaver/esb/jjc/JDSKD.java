package com.weaver.esb.jjc;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import weaver.conn.RecordSet;
import weaver.general.Util;

import java.text.SimpleDateFormat;
import java.util.*;

public class JDSKD {

    /**
     * @param:  param(Map collections)
     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
     */
    public Map execute(Map<String,Object> params) {
        OAStatementsDao2 oaStatementsDao = new OAStatementsDao2();
        String data = Util.null2String(params.get("data"));
        JSONObject dataobj =JSONObject.parseObject(data);
        JSONObject statementsobj=dataobj.getJSONObject("statementsobj");
        Map<String,Object> ret = new HashMap<>();

        OAStatementsBill oaStatementsBill = oaStatementsDao.getOAStatementsBill(statementsobj);

        if (!"0".equals(oaStatementsBill.getSfxydjdcwxt())) {
            ret.put("code","1");
            ret.put("msg","不需要生成收款单，跳过");
            return ret;
        }

        JSONArray statementsdt2arr=dataobj.getJSONArray("statementsdt2arr");
        List<OAStatementsDto2> oaStatementsDto2List = oaStatementsDao.getOAStatementsDto2List(statementsdt2arr);
        K3StatementsBill k3StatementsBill = new K3StatementsBill();

        K3StatementsModel k3StatementsModel = getK3StatementsModel(oaStatementsBill);

        List<K3StatementsModelTRY> keStatementsModelTRYList = getK3StatementsModelTRYList(oaStatementsDto2List, oaStatementsBill);
        k3StatementsModel.setFRECEIVEBILLENTRY(keStatementsModelTRYList);
        k3StatementsBill.setModel(k3StatementsModel);
        String k3StatementsBillobj=JSON.toJSONString(k3StatementsBill);
        ret.put("code","1");
        ret.put("k3StatementsBillobj",k3StatementsBillobj);
        ret.put("k3StatementsBill",k3StatementsBill);
        return ret;

    }

    public List<K3StatementsModelTRY> getK3StatementsModelTRYList(List<OAStatementsDto2> oaStatementsDto2List, OAStatementsBill oaStatementsBill) {
        List<K3StatementsModelTRY> keStatementsModelTRYList = new ArrayList<>();
        OAStatementsDto2 oaStatementsDto2=oaStatementsDto2List.get(0);
//        for (OAStatementsDto2 oaStatementsDto2 : oaStatementsDto2List) {
        K3StatementsModelTRY k3StatementsModelTRY = new K3StatementsModelTRY();
        JSONObject FSETTLETYPEID = new JSONObject();
        FSETTLETYPEID.put("FNumber", "JSFS04_SYS");
        k3StatementsModelTRY.setFSETTLETYPEID(FSETTLETYPEID);
        JSONObject FPURPOSEID = new JSONObject();
        FPURPOSEID.put("FNumber", "SFKYT099_SYS");
        k3StatementsModelTRY.setFPURPOSEID(FPURPOSEID);
        k3StatementsModelTRY.setFRECTOTALAMOUNTFOR(Util.getDoubleValue(oaStatementsBill.getJzje(), 0.0));
        k3StatementsModelTRY.setFRECAMOUNTFOR_E(Util.getDoubleValue(oaStatementsBill.getJzje(), 0.0));
        JSONObject FACCOUNTID = new JSONObject();
        FACCOUNTID.put("FNumber", oaStatementsBill.getJzzh1());
        k3StatementsModelTRY.setFACCOUNTID(FACCOUNTID);
        k3StatementsModelTRY.setFSETTLENO("");
        k3StatementsModelTRY.setFCOMMENT(oaStatementsBill.getBz());

        k3StatementsModelTRY.setFRECAMOUNT_E(Util.getDoubleValue(oaStatementsBill.getJzje(), 0.0));

        k3StatementsModelTRY.setFPOSTDATE(oaStatementsBill.getJzrq() + " 00:00:00");
        JSONObject F_qqqq_Base = new JSONObject();
        F_qqqq_Base.put("FNUMBER", oaStatementsDto2.getKmh());
        k3StatementsModelTRY.setF_qqqq_Base(F_qqqq_Base);
        k3StatementsModelTRY.setF_qqqq_Text(oaStatementsBill.getHkdw());//汇款单位

        k3StatementsModelTRY.setF_qqqq_Text1(oaStatementsBill.getHkyx());//汇款银行
        keStatementsModelTRYList.add(k3StatementsModelTRY);
//        }
        return keStatementsModelTRYList;
    }

    public K3StatementsModel getK3StatementsModel(OAStatementsBill oaStatementsBill) {
        K3StatementsModel k3StatementsModel = new K3StatementsModel();
        k3StatementsModel.setFID(0);
        JSONObject FPAYUNITTYPE = new JSONObject();
        FPAYUNITTYPE.put("FNUMBER", "SKDLX07_SYS");
        k3StatementsModel.setFBillTypeID(FPAYUNITTYPE);

        JSONObject FPAYUNIT = new JSONObject();
        FPAYUNIT.put("FNumber", "CUST0001");

        k3StatementsModel.setFPAYUNIT(FPAYUNIT);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(date));
        k3StatementsModel.setFDATE(formatter.format(date) + " 00:00:00");
        k3StatementsModel.setFCONTACTUNITTYPE("BD_Customer");

        JSONObject FCONTACTUNIT = new JSONObject();
        FCONTACTUNIT.put("FNumber", "CUST0001");

        k3StatementsModel.setFCONTACTUNIT(FCONTACTUNIT);

        JSONObject FCURRENCYID = new JSONObject();
        FCURRENCYID.put("FNumber", "PRE001");
        k3StatementsModel.setFCURRENCYID(FCURRENCYID);

        JSONObject FPAYORGID = new JSONObject();
        FPAYORGID.put("FNumber", "100");
        k3StatementsModel.setFPAYORGID(FPAYORGID);

        k3StatementsModel.setFSETTLERATE(1.0);

        JSONObject FSETTLEORGID = new JSONObject();
        FSETTLEORGID.put("FNumber", "100");
        k3StatementsModel.setFSETTLEORGID(FSETTLEORGID);
        JSONObject FSALEORGID = new JSONObject();
        FSALEORGID.put("FNumber", "100");
        k3StatementsModel.setFSALEORGID(FSALEORGID);
        k3StatementsModel.setFDOCUMENTSTATUS("Z");
        k3StatementsModel.setFBUSINESSTYPE("1");
        k3StatementsModel.setFISINIT(false);
        k3StatementsModel.setFEXCHANGERATE(1.0);
        k3StatementsModel.setFCancelStatus("A");

        JSONObject FSETTLECUR = new JSONObject();
        FSETTLECUR.put("FNUMBER", "PRE001");
        k3StatementsModel.setFSETTLECUR(FSETTLECUR);

        k3StatementsModel.setFISB2C(false);
        k3StatementsModel.setFIsWriteOff(false);

        k3StatementsModel.setFREMARK(oaStatementsBill.getFy());

        JSONObject FSETTLEMAINBOOKID = new JSONObject();
        FSETTLEMAINBOOKID.put("FNUMBER", "PRE001");
        k3StatementsModel.setFSETTLEMAINBOOKID(FSETTLEMAINBOOKID);

        k3StatementsModel.setFISCARRYRATE(false);
        return k3StatementsModel;
    }


}
class OAStatementsDao2 {
    RecordSet rs = new RecordSet();

    /**
     * 返回进账单主表数据
     *
     * @param
     * @return
     */
    public OAStatementsBill getOAStatementsBill(JSONObject statementsobj) {
        OAStatementsBill oaStatementsBill = new OAStatementsBill();
        if (statementsobj != null) {
            oaStatementsBill.setId(statementsobj.getString("id"));//id
            oaStatementsBill.setJzrq(statementsobj.getString("jzrq"));//进账日期
            oaStatementsBill.setJzdbh(statementsobj.getString("jzdbh"));//进账单编号
            oaStatementsBill.setJzje(statementsobj.getString("jzje"));//进账总额
            oaStatementsBill.setBz(statementsobj.getString("bz"));//币种
            oaStatementsBill.setHkdw(statementsobj.getString("hkdw"));//汇款单位
            oaStatementsBill.setHkyx(statementsobj.getString("hkyx"));//汇款银行
            oaStatementsBill.setYsfj(statementsobj.getString("ysfj"));//原始附件
            oaStatementsBill.setFy(statementsobj.getString("fy"));//附言
            oaStatementsBill.setScfj(statementsobj.getString("scfj"));//上传附件
            oaStatementsBill.setJzzh(statementsobj.getString("jzzh"));//进账账户
            oaStatementsBill.setJzzh1(statementsobj.getString("jzzh1"));//进账账号
            oaStatementsBill.setYqrje(statementsobj.getString("yqrje"));//已确认金额
            oaStatementsBill.setDqrje(statementsobj.getString("dqrje"));//待确认金额
            oaStatementsBill.setSfxydjdcwxt(statementsobj.getString("sfxydjdcwxt"));//是否需要对接到财务系统
        }
        return oaStatementsBill;
    }

    public List<OAStatementsDto2> getOAStatementsDto2List(JSONArray statementsdt2arr) {
        List<OAStatementsDto2> oaStatementsDto2List = new ArrayList<>();
        OAStatementsDto2 oaStatementsDto2 = new OAStatementsDto2();
        if(statementsdt2arr!=null) {
            for (int i=0;i<statementsdt2arr.size();i++) {
                JSONObject statementsdt2obj=statementsdt2arr.getJSONObject(i);
                oaStatementsDto2.setId(statementsdt2obj.getString("id"));//id
                oaStatementsDto2.setMainid(statementsdt2obj.getString("mainid"));//mainid
                oaStatementsDto2.setJzkm(statementsdt2obj.getString("jzkm"));//入账科目
                oaStatementsDto2.setKmh(statementsdt2obj.getString("kmh"));//科目号
                oaStatementsDto2.setJzje(statementsdt2obj.getString("jzje"));//进账金额
                oaStatementsDto2List.add(oaStatementsDto2);
            }
        }

        return oaStatementsDto2List;

    }
}


class OAStatementsBill {

    /**
     * id
     */
    private String id;
    /**
     * 进账日期
     */
    private String jzrq;
    /**
     * 进账单编号
     */
    private String jzdbh;
    /**
     * 进账总额
     */
    private String jzje;
    /**
     * 币种
     */
    private String bz;
    /**
     * 汇款单位
     */
    private String hkdw;
    /**
     * 汇款银行
     */
    private String hkyx;
    /**
     * 原始附件
     */
    private String ysfj;
    /**
     * 附言
     */
    private String fy;
    /**
     * 上传附件
     */
    private String scfj;
    /**
     * 进账账户
     */
    private String jzzh;
    /**
     * 进账账号
     */
    private String jzzh1;
    /**
     * 已确认金额
     */
    private String yqrje;
    /**
     * 待确认金额
     */
    private String dqrje;
    /**
     * 是否需要对接到财务系统
     */
    private String sfxydjdcwxt;

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
     * 获取 进账日期
     *
     * @return jzrq 进账日期
     */
    public String getJzrq() {
        return this.jzrq;
    }

    /**
     * 设置 进账日期
     *
     * @param jzrq 进账日期
     */
    public void setJzrq(String jzrq) {
        this.jzrq = jzrq;
    }

    /**
     * 获取 进账单编号
     *
     * @return jzdbh 进账单编号
     */
    public String getJzdbh() {
        return this.jzdbh;
    }

    /**
     * 设置 进账单编号
     *
     * @param jzdbh 进账单编号
     */
    public void setJzdbh(String jzdbh) {
        this.jzdbh = jzdbh;
    }

    /**
     * 获取 进账总额
     *
     * @return jzje 进账总额
     */
    public String getJzje() {
        return this.jzje;
    }

    /**
     * 设置 进账总额
     *
     * @param jzje 进账总额
     */
    public void setJzje(String jzje) {
        this.jzje = jzje;
    }

    /**
     * 获取 币种
     *
     * @return bz 币种
     */
    public String getBz() {
        return this.bz;
    }

    /**
     * 设置 币种
     *
     * @param bz 币种
     */
    public void setBz(String bz) {
        this.bz = bz;
    }

    /**
     * 获取 汇款单位
     *
     * @return hkdw 汇款单位
     */
    public String getHkdw() {
        return this.hkdw;
    }

    /**
     * 设置 汇款单位
     *
     * @param hkdw 汇款单位
     */
    public void setHkdw(String hkdw) {
        this.hkdw = hkdw;
    }

    /**
     * 获取 汇款银行
     *
     * @return hkyx 汇款银行
     */
    public String getHkyx() {
        return this.hkyx;
    }

    /**
     * 设置 汇款银行
     *
     * @param hkyx 汇款银行
     */
    public void setHkyx(String hkyx) {
        this.hkyx = hkyx;
    }

    /**
     * 获取 原始附件
     *
     * @return ysfj 原始附件
     */
    public String getYsfj() {
        return this.ysfj;
    }

    /**
     * 设置 原始附件
     *
     * @param ysfj 原始附件
     */
    public void setYsfj(String ysfj) {
        this.ysfj = ysfj;
    }

    /**
     * 获取 附言
     *
     * @return fy 附言
     */
    public String getFy() {
        return this.fy;
    }

    /**
     * 设置 附言
     *
     * @param fy 附言
     */
    public void setFy(String fy) {
        this.fy = fy;
    }

    /**
     * 获取 上传附件
     *
     * @return scfj 上传附件
     */
    public String getScfj() {
        return this.scfj;
    }

    /**
     * 设置 上传附件
     *
     * @param scfj 上传附件
     */
    public void setScfj(String scfj) {
        this.scfj = scfj;
    }

    /**
     * 获取 进账账户
     *
     * @return jzzh 进账账户
     */
    public String getJzzh() {
        return this.jzzh;
    }

    /**
     * 设置 进账账户
     *
     * @param jzzh 进账账户
     */
    public void setJzzh(String jzzh) {
        this.jzzh = jzzh;
    }

    /**
     * 获取 进账账号
     *
     * @return jzzh1 进账账号
     */
    public String getJzzh1() {
        return this.jzzh1;
    }

    /**
     * 设置 进账账号
     *
     * @param jzzh1 进账账号
     */
    public void setJzzh1(String jzzh1) {
        this.jzzh1 = jzzh1;
    }

    /**
     * 获取 已确认金额
     *
     * @return yqrje 已确认金额
     */
    public String getYqrje() {
        return this.yqrje;
    }

    /**
     * 设置 已确认金额
     *
     * @param yqrje 已确认金额
     */
    public void setYqrje(String yqrje) {
        this.yqrje = yqrje;
    }

    /**
     * 获取 待确认金额
     *
     * @return dqrje 待确认金额
     */
    public String getDqrje() {
        return this.dqrje;
    }

    /**
     * 设置 待确认金额
     *
     * @param dqrje 待确认金额
     */
    public void setDqrje(String dqrje) {
        this.dqrje = dqrje;
    }

    /**
     * 获取 是否需要对接到财务系统
     *
     * @return sfxydjdcwxt 是否需要对接到财务系统
     */
    public String getSfxydjdcwxt() {
        return this.sfxydjdcwxt;
    }

    /**
     * 设置 是否需要对接到财务系统
     *
     * @param sfxydjdcwxt 是否需要对接到财务系统
     */
    public void setSfxydjdcwxt(String sfxydjdcwxt) {
        this.sfxydjdcwxt = sfxydjdcwxt;
    }
}


class OAStatementsDto2 {

    /**
     * id
     */
    private String id;
    /**
     * mainid
     */
    private String mainid;
    /**
     * 入账科目
     */
    private String jzkm;
    /**
     * 科目号
     */
    private String kmh;
    /**
     * 进账金额
     */
    private String jzje;

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
     * 获取 入账科目
     *
     * @return jzkm 入账科目
     */
    public String getJzkm() {
        return this.jzkm;
    }

    /**
     * 设置 入账科目
     *
     * @param jzkm 入账科目
     */
    public void setJzkm(String jzkm) {
        this.jzkm = jzkm;
    }

    /**
     * 获取 科目号
     *
     * @return kmh 科目号
     */
    public String getKmh() {
        return this.kmh;
    }

    /**
     * 设置 科目号
     *
     * @param kmh 科目号
     */
    public void setKmh(String kmh) {
        this.kmh = kmh;
    }

    /**
     * 获取 进账金额
     *
     * @return jzje 进账金额
     */
    public String getJzje() {
        return this.jzje;
    }

    /**
     * 设置 进账金额
     *
     * @param jzje 进账金额
     */
    public void setJzje(String jzje) {
        this.jzje = jzje;
    }
}

class K3StatementsModel {
    @JSONField(ordinal = 1, name = "FID")
    int FID = 0;
    @JSONField(ordinal = 2, name = "FBillTypeID")
    Object FBillTypeID = new Object();
    @JSONField(ordinal = 3, name = "FDATE")
    String FDATE = "";
    @JSONField(ordinal = 4, name = "FCONTACTUNITTYPE")
    String FCONTACTUNITTYPE = "BD_Customer";
    @JSONField(ordinal = 5, name = "FCONTACTUNIT")
    Object FCONTACTUNIT = new Object();

    @JSONField(ordinal = 6, name = "FPAYUNITTYPE")
    String FPAYUNITTYPE = "BD_Customer";
    @JSONField(ordinal = 7, name = "FPAYUNIT")
    Object FPAYUNIT = new Object();

    @JSONField(ordinal = 8, name = "FCURRENCYID")
    Object FCURRENCYID = new Object();
    @JSONField(ordinal = 9, name = "FPAYORGID")
    Object FPAYORGID = new Object();
    @JSONField(ordinal = 10, name = "FSETTLERATE")
    double FSETTLERATE = 1.0;
    @JSONField(ordinal = 11, name = "FSETTLEORGID")
    Object FSETTLEORGID = new Object();
    @JSONField(ordinal = 12, name = "FSALEORGID")
    Object FSALEORGID = new Object();
    @JSONField(ordinal = 13, name = "FDOCUMENTSTATUS")
    String FDOCUMENTSTATUS = "Z";
    @JSONField(ordinal = 14, name = "FBUSINESSTYPE")
    String FBUSINESSTYPE = "1";
    @JSONField(ordinal = 15, name = "FISINIT")
    boolean FISINIT = false;
    @JSONField(ordinal = 16, name = "FEXCHANGERATE")
    double FEXCHANGERATE = 1.0;
    @JSONField(ordinal = 17, name = "FCancelStatus")
    String FCancelStatus = "A";
    @JSONField(ordinal = 18, name = "FSETTLECUR")
    Object FSETTLECUR = new Object();
    @JSONField(ordinal = 19, name = "FISB2C")
    boolean FISB2C = false;
    @JSONField(ordinal = 20, name = "FIsWriteOff")
    boolean FIsWriteOff = false;
    @JSONField(ordinal = 21, name = "FIsWriteOff")
    String FREMARK = "";
    @JSONField(ordinal = 22, name = "FSETTLEMAINBOOKID")
    Object FSETTLEMAINBOOKID = new Object();
    @JSONField(ordinal = 23, name = "FISCARRYRATE")
    boolean FISCARRYRATE = false;
    @JSONField(ordinal = 24, name = "FRECEIVEBILLENTRY")
    List<K3StatementsModelTRY> FRECEIVEBILLENTRY = new ArrayList<>();

    public Object getFCONTACTUNIT() {
        return FCONTACTUNIT;
    }

    public void setFCONTACTUNIT(Object FCONTACTUNIT) {
        this.FCONTACTUNIT = FCONTACTUNIT;
    }

    public Object getFPAYUNIT() {
        return FPAYUNIT;
    }

    public void setFPAYUNIT(Object FPAYUNIT) {
        this.FPAYUNIT = FPAYUNIT;
    }

    public String getFREMARK() {
        return FREMARK;
    }

    public void setFREMARK(String FREMARK) {
        this.FREMARK = FREMARK;
    }

    public int getFID() {
        return FID;
    }

    public void setFID(int FID) {
        this.FID = FID;
    }

    public Object getFBillTypeID() {
        return FBillTypeID;
    }

    public void setFBillTypeID(Object FBillTypeID) {
        this.FBillTypeID = FBillTypeID;
    }

    public String getFDATE() {
        return FDATE;
    }

    public void setFDATE(String FDATE) {
        this.FDATE = FDATE;
    }

    public String getFCONTACTUNITTYPE() {
        return FCONTACTUNITTYPE;
    }

    public void setFCONTACTUNITTYPE(String FCONTACTUNITTYPE) {
        this.FCONTACTUNITTYPE = FCONTACTUNITTYPE;
    }

    public String getFPAYUNITTYPE() {
        return FPAYUNITTYPE;
    }

    public void setFPAYUNITTYPE(String FPAYUNITTYPE) {
        this.FPAYUNITTYPE = FPAYUNITTYPE;
    }

    public Object getFCURRENCYID() {
        return FCURRENCYID;
    }

    public void setFCURRENCYID(Object FCURRENCYID) {
        this.FCURRENCYID = FCURRENCYID;
    }

    public Object getFPAYORGID() {
        return FPAYORGID;
    }

    public void setFPAYORGID(Object FPAYORGID) {
        this.FPAYORGID = FPAYORGID;
    }

    public double getFSETTLERATE() {
        return FSETTLERATE;
    }

    public void setFSETTLERATE(double FSETTLERATE) {
        this.FSETTLERATE = FSETTLERATE;
    }

    public Object getFSETTLEORGID() {
        return FSETTLEORGID;
    }

    public void setFSETTLEORGID(Object FSETTLEORGID) {
        this.FSETTLEORGID = FSETTLEORGID;
    }

    public Object getFSALEORGID() {
        return FSALEORGID;
    }

    public void setFSALEORGID(Object FSALEORGID) {
        this.FSALEORGID = FSALEORGID;
    }

    public String getFDOCUMENTSTATUS() {
        return FDOCUMENTSTATUS;
    }

    public void setFDOCUMENTSTATUS(String FDOCUMENTSTATUS) {
        this.FDOCUMENTSTATUS = FDOCUMENTSTATUS;
    }

    public String getFBUSINESSTYPE() {
        return FBUSINESSTYPE;
    }

    public void setFBUSINESSTYPE(String FBUSINESSTYPE) {
        this.FBUSINESSTYPE = FBUSINESSTYPE;
    }

    public boolean isFISINIT() {
        return FISINIT;
    }

    public void setFISINIT(boolean FISINIT) {
        this.FISINIT = FISINIT;
    }

    public double getFEXCHANGERATE() {
        return FEXCHANGERATE;
    }

    public void setFEXCHANGERATE(double FEXCHANGERATE) {
        this.FEXCHANGERATE = FEXCHANGERATE;
    }

    public String getFCancelStatus() {
        return FCancelStatus;
    }

    public void setFCancelStatus(String FCancelStatus) {
        this.FCancelStatus = FCancelStatus;
    }

    public Object getFSETTLECUR() {
        return FSETTLECUR;
    }

    public void setFSETTLECUR(Object FSETTLECUR) {
        this.FSETTLECUR = FSETTLECUR;
    }

    public boolean isFISB2C() {
        return FISB2C;
    }

    public void setFISB2C(boolean FISB2C) {
        this.FISB2C = FISB2C;
    }

    public boolean isFIsWriteOff() {
        return FIsWriteOff;
    }

    public void setFIsWriteOff(boolean FIsWriteOff) {
        this.FIsWriteOff = FIsWriteOff;
    }

    public Object getFSETTLEMAINBOOKID() {
        return FSETTLEMAINBOOKID;
    }

    public void setFSETTLEMAINBOOKID(Object FSETTLEMAINBOOKID) {
        this.FSETTLEMAINBOOKID = FSETTLEMAINBOOKID;
    }

    public boolean isFISCARRYRATE() {
        return FISCARRYRATE;
    }

    public void setFISCARRYRATE(boolean FISCARRYRATE) {
        this.FISCARRYRATE = FISCARRYRATE;
    }

    public List<K3StatementsModelTRY> getFRECEIVEBILLENTRY() {
        return FRECEIVEBILLENTRY;
    }

    public void setFRECEIVEBILLENTRY(List<K3StatementsModelTRY> FRECEIVEBILLENTRY) {
        this.FRECEIVEBILLENTRY = FRECEIVEBILLENTRY;
    }
}
class K3StatementsBill {
    @JSONField(ordinal = 1, name = "NeedUpDateFields")
    List<String> NeedUpDateFields = new ArrayList<>();
    @JSONField(ordinal = 2, name = "NeedReturnFields")
    List<String> NeedReturnFields = new ArrayList<>();
    @JSONField(ordinal = 3, name = "IsDeleteEntry")
    String IsDeleteEntry = "true";
    @JSONField(ordinal = 4, name = "SubSystemId")
    String SubSystemId = "";
    @JSONField(ordinal = 5, name = "IsVerifyBaseDataField")
    String IsVerifyBaseDataField = "false";
    @JSONField(ordinal = 6, name = "IsEntryBatchFill")
    String IsEntryBatchFill = "true";
    @JSONField(ordinal = 7, name = "ValidateFlag")
    String ValidateFlag = "true";
    @JSONField(ordinal = 8, name = "NumberSearch")
    String NumberSearch = "true";
    @JSONField(ordinal = 9, name = "InterationFlags")
    String InterationFlags = "";
    @JSONField(ordinal = 10, name = "Model")
    K3StatementsModel Model = new K3StatementsModel();

    /**
     * 获取
     *
     * @return NeedUpDateFields
     */
    public List<String> getNeedUpDateFields() {
        return this.NeedUpDateFields;
    }

    /**
     * 设置
     *
     * @param NeedUpDateFields
     */
    public void setNeedUpDateFields(List<String> NeedUpDateFields) {
        this.NeedUpDateFields = NeedUpDateFields;
    }

    /**
     * 获取
     *
     * @return NeedReturnFields
     */
    public List<String> getNeedReturnFields() {
        return this.NeedReturnFields;
    }

    /**
     * 设置
     *
     * @param NeedReturnFields
     */
    public void setNeedReturnFields(List<String> NeedReturnFields) {
        this.NeedReturnFields = NeedReturnFields;
    }

    /**
     * 获取
     *
     * @return IsDeleteEntry
     */
    public String getIsDeleteEntry() {
        return this.IsDeleteEntry;
    }

    /**
     * 设置
     *
     * @param IsDeleteEntry
     */
    public void setIsDeleteEntry(String IsDeleteEntry) {
        this.IsDeleteEntry = IsDeleteEntry;
    }

    /**
     * 获取
     *
     * @return SubSystemId
     */
    public String getSubSystemId() {
        return this.SubSystemId;
    }

    /**
     * 设置
     *
     * @param SubSystemId
     */
    public void setSubSystemId(String SubSystemId) {
        this.SubSystemId = SubSystemId;
    }

    /**
     * 获取
     *
     * @return IsVerifyBaseDataField
     */
    public String getIsVerifyBaseDataField() {
        return this.IsVerifyBaseDataField;
    }

    /**
     * 设置
     *
     * @param IsVerifyBaseDataField
     */
    public void setIsVerifyBaseDataField(String IsVerifyBaseDataField) {
        this.IsVerifyBaseDataField = IsVerifyBaseDataField;
    }

    /**
     * 获取
     *
     * @return IsEntryBatchFill
     */
    public String getIsEntryBatchFill() {
        return this.IsEntryBatchFill;
    }

    /**
     * 设置
     *
     * @param IsEntryBatchFill
     */
    public void setIsEntryBatchFill(String IsEntryBatchFill) {
        this.IsEntryBatchFill = IsEntryBatchFill;
    }

    /**
     * 获取
     *
     * @return ValidateFlag
     */
    public String getValidateFlag() {
        return this.ValidateFlag;
    }

    /**
     * 设置
     *
     * @param ValidateFlag
     */
    public void setValidateFlag(String ValidateFlag) {
        this.ValidateFlag = ValidateFlag;
    }

    /**
     * 获取
     *
     * @return NumberSearch
     */
    public String getNumberSearch() {
        return this.NumberSearch;
    }

    /**
     * 设置
     *
     * @param NumberSearch
     */
    public void setNumberSearch(String NumberSearch) {
        this.NumberSearch = NumberSearch;
    }

    /**
     * 获取
     *
     * @return InterationFlags
     */
    public String getInterationFlags() {
        return this.InterationFlags;
    }

    /**
     * 设置
     *
     * @param InterationFlags
     */
    public void setInterationFlags(String InterationFlags) {
        this.InterationFlags = InterationFlags;
    }

    /**
     * 获取 @JSONField(ordinal = 10)
     *
     * @return Model @JSONField(ordinal = 10)
     */
    public K3StatementsModel getModel() {
        return this.Model;
    }

    /**
     * 设置 @JSONField(ordinal = 10)
     *
     * @param Model @JSONField(ordinal = 10)
     */
    public void setModel(K3StatementsModel Model) {
        this.Model = Model;
    }
}

class K3StatementsModelTRY {
    @JSONField(ordinal = 1, name = "FSETTLETYPEID")
    Object FSETTLETYPEID = new Object();
    @JSONField(ordinal = 2, name = "FPURPOSEID")
    Object FPURPOSEID = new Object();
    @JSONField(ordinal = 3, name = "FRECTOTALAMOUNTFOR")
    double FRECTOTALAMOUNTFOR = 0.0;
    @JSONField(ordinal = 4, name = "FRECAMOUNTFOR_E")
    double FRECAMOUNTFOR_E = 0.0;
    @JSONField(ordinal = 5, name = "FACCOUNTID")
    Object FACCOUNTID = new Object();
    @JSONField(ordinal = 6, name = "FSETTLENO")
    String FSETTLENO = "";
    @JSONField(ordinal = 7, name = "FCOMMENT")
    String FCOMMENT = "";
    @JSONField(ordinal = 8, name = "FRECAMOUNT_E")
    double FRECAMOUNT_E = 0.0;
    @JSONField(ordinal = 9, name = "FPOSTDATE")
    String FPOSTDATE = "";
    @JSONField(ordinal = 10, name = "F_qqqq_Base")
    Object F_qqqq_Base = new Object();
    @JSONField(ordinal = 11, name = "F_qqqq_Text")
    String F_qqqq_Text = "";
    @JSONField(ordinal = 12, name = "F_qqqq_Text1")
    String F_qqqq_Text1 = "";

    /**
     * 获取
     *
     * @return FSETTLETYPEID
     */
    public Object getFSETTLETYPEID() {
        return this.FSETTLETYPEID;
    }

    /**
     * 设置
     *
     * @param FSETTLETYPEID
     */
    public void setFSETTLETYPEID(Object FSETTLETYPEID) {
        this.FSETTLETYPEID = FSETTLETYPEID;
    }

    /**
     * 获取
     *
     * @return FPURPOSEID
     */
    public Object getFPURPOSEID() {
        return this.FPURPOSEID;
    }

    /**
     * 设置
     *
     * @param FPURPOSEID
     */
    public void setFPURPOSEID(Object FPURPOSEID) {
        this.FPURPOSEID = FPURPOSEID;
    }

    /**
     * 获取
     *
     * @return FRECTOTALAMOUNTFOR
     */
    public double getFRECTOTALAMOUNTFOR() {
        return this.FRECTOTALAMOUNTFOR;
    }

    /**
     * 设置
     *
     * @param FRECTOTALAMOUNTFOR
     */
    public void setFRECTOTALAMOUNTFOR(double FRECTOTALAMOUNTFOR) {
        this.FRECTOTALAMOUNTFOR = FRECTOTALAMOUNTFOR;
    }

    /**
     * 获取
     *
     * @return FRECAMOUNTFOR_E
     */
    public double getFRECAMOUNTFOR_E() {
        return this.FRECAMOUNTFOR_E;
    }

    /**
     * 设置
     *
     * @param FRECAMOUNTFOR_E
     */
    public void setFRECAMOUNTFOR_E(double FRECAMOUNTFOR_E) {
        this.FRECAMOUNTFOR_E = FRECAMOUNTFOR_E;
    }

    /**
     * 获取
     *
     * @return FACCOUNTID
     */
    public Object getFACCOUNTID() {
        return this.FACCOUNTID;
    }

    /**
     * 设置
     *
     * @param FACCOUNTID
     */
    public void setFACCOUNTID(Object FACCOUNTID) {
        this.FACCOUNTID = FACCOUNTID;
    }

    /**
     * 获取
     *
     * @return FSETTLENO
     */
    public String getFSETTLENO() {
        return this.FSETTLENO;
    }

    /**
     * 设置
     *
     * @param FSETTLENO
     */
    public void setFSETTLENO(String FSETTLENO) {
        this.FSETTLENO = FSETTLENO;
    }

    /**
     * 获取
     *
     * @return FCOMMENT
     */
    public String getFCOMMENT() {
        return this.FCOMMENT;
    }

    /**
     * 设置
     *
     * @param FCOMMENT
     */
    public void setFCOMMENT(String FCOMMENT) {
        this.FCOMMENT = FCOMMENT;
    }

    /**
     * 获取
     *
     * @return FRECAMOUNT_E
     */
    public double getFRECAMOUNT_E() {
        return this.FRECAMOUNT_E;
    }

    /**
     * 设置
     *
     * @param FRECAMOUNT_E
     */
    public void setFRECAMOUNT_E(double FRECAMOUNT_E) {
        this.FRECAMOUNT_E = FRECAMOUNT_E;
    }

    /**
     * 获取
     *
     * @return FPOSTDATE
     */
    public String getFPOSTDATE() {
        return this.FPOSTDATE;
    }

    /**
     * 设置
     *
     * @param FPOSTDATE
     */
    public void setFPOSTDATE(String FPOSTDATE) {
        this.FPOSTDATE = FPOSTDATE;
    }

    /**
     * 获取
     *
     * @return F_qqqq_Base
     */
    public Object getF_qqqq_Base() {
        return this.F_qqqq_Base;
    }

    /**
     * 设置
     *
     * @param F_qqqq_Base
     */
    public void setF_qqqq_Base(Object F_qqqq_Base) {
        this.F_qqqq_Base = F_qqqq_Base;
    }

    /**
     * 获取 @JSONField(ordinal = 11name = "F_qqqq_Text")
     *
     * @return F_qqqq_Text @JSONField(ordinal = 11name = "F_qqqq_Text")
     */
    public String getF_qqqq_Text() {
        return this.F_qqqq_Text;
    }

    /**
     * 设置 @JSONField(ordinal = 11name = "F_qqqq_Text")
     *
     * @param F_qqqq_Text @JSONField(ordinal = 11name = "F_qqqq_Text")
     */
    public void setF_qqqq_Text(String F_qqqq_Text) {
        this.F_qqqq_Text = F_qqqq_Text;
    }

    /**
     * 获取 @JSONField(ordinal = 12name = "F_qqqq_Text1")
     *
     * @return F_qqqq_Text1 @JSONField(ordinal = 12name = "F_qqqq_Text1")
     */
    public String getF_qqqq_Text1() {
        return this.F_qqqq_Text1;
    }

    /**
     * 设置 @JSONField(ordinal = 12name = "F_qqqq_Text1")
     *
     * @param F_qqqq_Text1 @JSONField(ordinal = 12name = "F_qqqq_Text1")
     */
    public void setF_qqqq_Text1(String F_qqqq_Text1) {
        this.F_qqqq_Text1 = F_qqqq_Text1;
    }
}






