package weaver.interfaces.jjc.k3client.cw.dto;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

public class K3PaymentNoteModel {
    @JSONField(ordinal = 1, name = "FID")
    int FID = 0;
    @JSONField(ordinal = 2, name = "FBillTypeID")
    Object FBillTypeID = new Object();
    @JSONField(ordinal = 3, name = "FDATE")
    String FDATE = "";
    @JSONField(ordinal = 4, name = "FCONTACTUNITTYPE")
    String FCONTACTUNITTYPE = "BD_Empinfo";
    @JSONField(ordinal = 5, name = "FCONTACTUNIT")
    Object FCONTACTUNIT = new Object();
    @JSONField(ordinal = 6, name = "FRECTUNITTYPE")
    String FRECTUNITTYPE = "BD_Customer";
    @JSONField(ordinal = 7, name = "FRECTUNIT")
    Object FRECTUNIT = new Object();
    @JSONField(ordinal = 8, name = "FISINIT")
    Boolean FISINIT = false;
    @JSONField(ordinal = 9, name = "FCURRENCYID")
    Object FCURRENCYID = new Object();
    @JSONField(ordinal = 10, name = "FEXCHANGERATE")
    double FEXCHANGERATE = 1.0;
    @JSONField(ordinal = 11, name = "FSETTLERATE")
    double FSETTLERATE = 1.0;
    @JSONField(ordinal = 12, name = "FSETTLEORGID")
    Object FSETTLEORGID = new Object();
    @JSONField(ordinal = 13, name = "FPURCHASEORGID")
    Object FPURCHASEORGID = new Object();
    @JSONField(ordinal = 14, name = "FDOCUMENTSTATUS")
    String FDOCUMENTSTATUS = "";
    @JSONField(ordinal = 15, name = "FBUSINESSTYPE")
    String FBUSINESSTYPE = "";
    @JSONField(ordinal = 16, name = "FCancelStatus")
    String FCancelStatus = "";
    @JSONField(ordinal = 17, name = "FPAYORGID")
    Object FPAYORGID = new Object();
    @JSONField(ordinal = 18, name = "FISSAMEORG")
    boolean FISSAMEORG = true;
    @JSONField(ordinal = 19, name = "FIsCredit")
    boolean FIsCredit = false;
    @JSONField(ordinal = 20, name = "FSETTLECUR")
    Object FSETTLECUR = new Object();
    @JSONField(ordinal = 21, name = "FIsWriteOff")
    boolean FIsWriteOff = false;
    @JSONField(ordinal = 22, name = "FREALPAY")
    boolean FREALPAY = false;
    @JSONField(ordinal = 23, name = "FREMARK")
    String FREMARK = "";
    @JSONField(ordinal = 24, name = "FBookingDate")
    String FBookingDate = "";
    @JSONField(ordinal = 25, name = "FISCARRYRATE")
    boolean FISCARRYRATE = false;
    @JSONField(ordinal = 26, name = "FSETTLEMAINBOOKID")
    Object FSETTLEMAINBOOKID = new Object();
    @JSONField(ordinal = 27, name = "F_qqqq_Text3")
    String F_qqqq_Text3 = "";
    @JSONField(ordinal = 28, name = "F_qqqq_Text5")
    String F_qqqq_Text5 = "";
    @JSONField(ordinal = 29, name = "F_qqqq_Text6")
    String F_qqqq_Text6 = "";
    @JSONField(ordinal = 30, name = "F_qqqq_Text4")
    String F_qqqq_Text4 = "";
    @JSONField(ordinal = 31, name = "F_qqqq_Text7")
    String F_qqqq_Text7 = "";
    @JSONField(ordinal = 32, name = "FPAYBILLENTRY")
    List<K3PaymentNoteModelTRY> FPAYBILLENTRY = new ArrayList<>();

    /**
     * 获取 @JSONField(ordinal = 1name = "FID")
     *
     * @return FID @JSONField(ordinal = 1name = "FID")
     */
    public int getFID() {
        return this.FID;
    }

    /**
     * 设置 @JSONField(ordinal = 1name = "FID")
     *
     * @param FID @JSONField(ordinal = 1name = "FID")
     */
    public void setFID(int FID) {
        this.FID = FID;
    }

    /**
     * 获取 @JSONField(ordinal = 2name = "FBillTypeID")
     *
     * @return FBillTypeID @JSONField(ordinal = 2name = "FBillTypeID")
     */
    public Object getFBillTypeID() {
        return this.FBillTypeID;
    }

    /**
     * 设置 @JSONField(ordinal = 2name = "FBillTypeID")
     *
     * @param FBillTypeID @JSONField(ordinal = 2name = "FBillTypeID")
     */
    public void setFBillTypeID(Object FBillTypeID) {
        this.FBillTypeID = FBillTypeID;
    }

    /**
     * 获取 @JSONField(ordinal = 3name = "FDATE")
     *
     * @return FDATE @JSONField(ordinal = 3name = "FDATE")
     */
    public String getFDATE() {
        return this.FDATE;
    }

    /**
     * 设置 @JSONField(ordinal = 3name = "FDATE")
     *
     * @param FDATE @JSONField(ordinal = 3name = "FDATE")
     */
    public void setFDATE(String FDATE) {
        this.FDATE = FDATE;
    }

    /**
     * 获取 @JSONField(ordinal = 4name = "FCONTACTUNITTYPE")
     *
     * @return FCONTACTUNITTYPE @JSONField(ordinal = 4name = "FCONTACTUNITTYPE")
     */
    public String getFCONTACTUNITTYPE() {
        return this.FCONTACTUNITTYPE;
    }

    /**
     * 设置 @JSONField(ordinal = 4name = "FCONTACTUNITTYPE")
     *
     * @param FCONTACTUNITTYPE @JSONField(ordinal = 4name = "FCONTACTUNITTYPE")
     */
    public void setFCONTACTUNITTYPE(String FCONTACTUNITTYPE) {
        this.FCONTACTUNITTYPE = FCONTACTUNITTYPE;
    }

    /**
     * 获取 @JSONField(ordinal = 5name = "FCONTACTUNIT")
     *
     * @return FCONTACTUNIT @JSONField(ordinal = 5name = "FCONTACTUNIT")
     */
    public Object getFCONTACTUNIT() {
        return this.FCONTACTUNIT;
    }

    /**
     * 设置 @JSONField(ordinal = 5name = "FCONTACTUNIT")
     *
     * @param FCONTACTUNIT @JSONField(ordinal = 5name = "FCONTACTUNIT")
     */
    public void setFCONTACTUNIT(Object FCONTACTUNIT) {
        this.FCONTACTUNIT = FCONTACTUNIT;
    }

    /**
     * 获取 @JSONField(ordinal = 6name = "FRECTUNITTYPE")
     *
     * @return FRECTUNITTYPE @JSONField(ordinal = 6name = "FRECTUNITTYPE")
     */
    public String getFRECTUNITTYPE() {
        return this.FRECTUNITTYPE;
    }

    /**
     * 设置 @JSONField(ordinal = 6name = "FRECTUNITTYPE")
     *
     * @param FRECTUNITTYPE @JSONField(ordinal = 6name = "FRECTUNITTYPE")
     */
    public void setFRECTUNITTYPE(String FRECTUNITTYPE) {
        this.FRECTUNITTYPE = FRECTUNITTYPE;
    }

    /**
     * 获取 @JSONField(ordinal = 7name = "FRECTUNIT")
     *
     * @return FRECTUNIT @JSONField(ordinal = 7name = "FRECTUNIT")
     */
    public Object getFRECTUNIT() {
        return this.FRECTUNIT;
    }

    /**
     * 设置 @JSONField(ordinal = 7name = "FRECTUNIT")
     *
     * @param FRECTUNIT @JSONField(ordinal = 7name = "FRECTUNIT")
     */
    public void setFRECTUNIT(Object FRECTUNIT) {
        this.FRECTUNIT = FRECTUNIT;
    }

    /**
     * 获取 @JSONField(ordinal = 8name = "FISINIT")
     *
     * @return FISINIT @JSONField(ordinal = 8name = "FISINIT")
     */
    public Boolean getFISINIT() {
        return this.FISINIT;
    }

    /**
     * 设置 @JSONField(ordinal = 8name = "FISINIT")
     *
     * @param FISINIT @JSONField(ordinal = 8name = "FISINIT")
     */
    public void setFISINIT(Boolean FISINIT) {
        this.FISINIT = FISINIT;
    }

    /**
     * 获取 @JSONField(ordinal = 9name = "FCURRENCYID")
     *
     * @return FCURRENCYID @JSONField(ordinal = 9name = "FCURRENCYID")
     */
    public Object getFCURRENCYID() {
        return this.FCURRENCYID;
    }

    /**
     * 设置 @JSONField(ordinal = 9name = "FCURRENCYID")
     *
     * @param FCURRENCYID @JSONField(ordinal = 9name = "FCURRENCYID")
     */
    public void setFCURRENCYID(Object FCURRENCYID) {
        this.FCURRENCYID = FCURRENCYID;
    }

    /**
     * 获取 @JSONField(ordinal = 10name = "FEXCHANGERATE")
     *
     * @return FEXCHANGERATE @JSONField(ordinal = 10name = "FEXCHANGERATE")
     */
    public double getFEXCHANGERATE() {
        return this.FEXCHANGERATE;
    }

    /**
     * 设置 @JSONField(ordinal = 10name = "FEXCHANGERATE")
     *
     * @param FEXCHANGERATE @JSONField(ordinal = 10name = "FEXCHANGERATE")
     */
    public void setFEXCHANGERATE(double FEXCHANGERATE) {
        this.FEXCHANGERATE = FEXCHANGERATE;
    }

    /**
     * 获取 @JSONField(ordinal = 11name = "FSETTLERATE")
     *
     * @return FSETTLERATE @JSONField(ordinal = 11name = "FSETTLERATE")
     */
    public double getFSETTLERATE() {
        return this.FSETTLERATE;
    }

    /**
     * 设置 @JSONField(ordinal = 11name = "FSETTLERATE")
     *
     * @param FSETTLERATE @JSONField(ordinal = 11name = "FSETTLERATE")
     */
    public void setFSETTLERATE(double FSETTLERATE) {
        this.FSETTLERATE = FSETTLERATE;
    }

    /**
     * 获取 @JSONField(ordinal = 12name = "FSETTLEORGID")
     *
     * @return FSETTLEORGID @JSONField(ordinal = 12name = "FSETTLEORGID")
     */
    public Object getFSETTLEORGID() {
        return this.FSETTLEORGID;
    }

    /**
     * 设置 @JSONField(ordinal = 12name = "FSETTLEORGID")
     *
     * @param FSETTLEORGID @JSONField(ordinal = 12name = "FSETTLEORGID")
     */
    public void setFSETTLEORGID(Object FSETTLEORGID) {
        this.FSETTLEORGID = FSETTLEORGID;
    }

    /**
     * 获取 @JSONField(ordinal = 13name = "FPURCHASEORGID")
     *
     * @return FPURCHASEORGID @JSONField(ordinal = 13name = "FPURCHASEORGID")
     */
    public Object getFPURCHASEORGID() {
        return this.FPURCHASEORGID;
    }

    /**
     * 设置 @JSONField(ordinal = 13name = "FPURCHASEORGID")
     *
     * @param FPURCHASEORGID @JSONField(ordinal = 13name = "FPURCHASEORGID")
     */
    public void setFPURCHASEORGID(Object FPURCHASEORGID) {
        this.FPURCHASEORGID = FPURCHASEORGID;
    }

    /**
     * 获取 @JSONField(ordinal = 14name = "FDOCUMENTSTATUS")
     *
     * @return FDOCUMENTSTATUS @JSONField(ordinal = 14name = "FDOCUMENTSTATUS")
     */
    public String getFDOCUMENTSTATUS() {
        return this.FDOCUMENTSTATUS;
    }

    /**
     * 设置 @JSONField(ordinal = 14name = "FDOCUMENTSTATUS")
     *
     * @param FDOCUMENTSTATUS @JSONField(ordinal = 14name = "FDOCUMENTSTATUS")
     */
    public void setFDOCUMENTSTATUS(String FDOCUMENTSTATUS) {
        this.FDOCUMENTSTATUS = FDOCUMENTSTATUS;
    }

    /**
     * 获取 @JSONField(ordinal = 15name = "FBUSINESSTYPE")
     *
     * @return FBUSINESSTYPE @JSONField(ordinal = 15name = "FBUSINESSTYPE")
     */
    public String getFBUSINESSTYPE() {
        return this.FBUSINESSTYPE;
    }

    /**
     * 设置 @JSONField(ordinal = 15name = "FBUSINESSTYPE")
     *
     * @param FBUSINESSTYPE @JSONField(ordinal = 15name = "FBUSINESSTYPE")
     */
    public void setFBUSINESSTYPE(String FBUSINESSTYPE) {
        this.FBUSINESSTYPE = FBUSINESSTYPE;
    }

    /**
     * 获取 @JSONField(ordinal = 16name = "FCancelStatus")
     *
     * @return FCancelStatus @JSONField(ordinal = 16name = "FCancelStatus")
     */
    public String getFCancelStatus() {
        return this.FCancelStatus;
    }

    /**
     * 设置 @JSONField(ordinal = 16name = "FCancelStatus")
     *
     * @param FCancelStatus @JSONField(ordinal = 16name = "FCancelStatus")
     */
    public void setFCancelStatus(String FCancelStatus) {
        this.FCancelStatus = FCancelStatus;
    }

    /**
     * 获取 @JSONField(ordinal = 17name = "FPAYORGID")
     *
     * @return FPAYORGID @JSONField(ordinal = 17name = "FPAYORGID")
     */
    public Object getFPAYORGID() {
        return this.FPAYORGID;
    }

    /**
     * 设置 @JSONField(ordinal = 17name = "FPAYORGID")
     *
     * @param FPAYORGID @JSONField(ordinal = 17name = "FPAYORGID")
     */
    public void setFPAYORGID(Object FPAYORGID) {
        this.FPAYORGID = FPAYORGID;
    }

    /**
     * 获取 @JSONField(ordinal = 18name = "FISSAMEORG")
     *
     * @return FISSAMEORG @JSONField(ordinal = 18name = "FISSAMEORG")
     */
    public boolean isFISSAMEORG() {
        return this.FISSAMEORG;
    }

    /**
     * 设置 @JSONField(ordinal = 18name = "FISSAMEORG")
     *
     * @param FISSAMEORG @JSONField(ordinal = 18name = "FISSAMEORG")
     */
    public void setFISSAMEORG(boolean FISSAMEORG) {
        this.FISSAMEORG = FISSAMEORG;
    }

    /**
     * 获取 @JSONField(ordinal = 19name = "FIsCredit")
     *
     * @return FIsCredit @JSONField(ordinal = 19name = "FIsCredit")
     */
    public boolean isFIsCredit() {
        return this.FIsCredit;
    }

    /**
     * 设置 @JSONField(ordinal = 19name = "FIsCredit")
     *
     * @param FIsCredit @JSONField(ordinal = 19name = "FIsCredit")
     */
    public void setFIsCredit(boolean FIsCredit) {
        this.FIsCredit = FIsCredit;
    }

    /**
     * 获取 @JSONField(ordinal = 20name = "FSETTLECUR")
     *
     * @return FSETTLECUR @JSONField(ordinal = 20name = "FSETTLECUR")
     */
    public Object getFSETTLECUR() {
        return this.FSETTLECUR;
    }

    /**
     * 设置 @JSONField(ordinal = 20name = "FSETTLECUR")
     *
     * @param FSETTLECUR @JSONField(ordinal = 20name = "FSETTLECUR")
     */
    public void setFSETTLECUR(Object FSETTLECUR) {
        this.FSETTLECUR = FSETTLECUR;
    }

    /**
     * 获取 @JSONField(ordinal = 21name = "FIsWriteOff")
     *
     * @return FIsWriteOff @JSONField(ordinal = 21name = "FIsWriteOff")
     */
    public boolean isFIsWriteOff() {
        return this.FIsWriteOff;
    }

    /**
     * 设置 @JSONField(ordinal = 21name = "FIsWriteOff")
     *
     * @param FIsWriteOff @JSONField(ordinal = 21name = "FIsWriteOff")
     */
    public void setFIsWriteOff(boolean FIsWriteOff) {
        this.FIsWriteOff = FIsWriteOff;
    }

    /**
     * 获取 @JSONField(ordinal = 22name = "FREALPAY")
     *
     * @return FREALPAY @JSONField(ordinal = 22name = "FREALPAY")
     */
    public boolean isFREALPAY() {
        return this.FREALPAY;
    }

    /**
     * 设置 @JSONField(ordinal = 22name = "FREALPAY")
     *
     * @param FREALPAY @JSONField(ordinal = 22name = "FREALPAY")
     */
    public void setFREALPAY(boolean FREALPAY) {
        this.FREALPAY = FREALPAY;
    }

    /**
     * 获取 @JSONField(ordinal = 23name = "FREMARK")
     *
     * @return FREMARK @JSONField(ordinal = 23name = "FREMARK")
     */
    public String getFREMARK() {
        return this.FREMARK;
    }

    /**
     * 设置 @JSONField(ordinal = 23name = "FREMARK")
     *
     * @param FREMARK @JSONField(ordinal = 23name = "FREMARK")
     */
    public void setFREMARK(String FREMARK) {
        this.FREMARK = FREMARK;
    }

    /**
     * 获取 @JSONField(ordinal = 24name = "FBookingDate")
     *
     * @return FBookingDate @JSONField(ordinal = 24name = "FBookingDate")
     */
    public String getFBookingDate() {
        return this.FBookingDate;
    }

    /**
     * 设置 @JSONField(ordinal = 24name = "FBookingDate")
     *
     * @param FBookingDate @JSONField(ordinal = 24name = "FBookingDate")
     */
    public void setFBookingDate(String FBookingDate) {
        this.FBookingDate = FBookingDate;
    }

    /**
     * 获取 @JSONField(ordinal = 25name = "FISCARRYRATE")
     *
     * @return FISCARRYRATE @JSONField(ordinal = 25name = "FISCARRYRATE")
     */
    public boolean isFISCARRYRATE() {
        return this.FISCARRYRATE;
    }

    /**
     * 设置 @JSONField(ordinal = 25name = "FISCARRYRATE")
     *
     * @param FISCARRYRATE @JSONField(ordinal = 25name = "FISCARRYRATE")
     */
    public void setFISCARRYRATE(boolean FISCARRYRATE) {
        this.FISCARRYRATE = FISCARRYRATE;
    }

    /**
     * 获取 @JSONField(ordinal = 26name = "FSETTLEMAINBOOKID")
     *
     * @return FSETTLEMAINBOOKID @JSONField(ordinal = 26name = "FSETTLEMAINBOOKID")
     */
    public Object getFSETTLEMAINBOOKID() {
        return this.FSETTLEMAINBOOKID;
    }

    /**
     * 设置 @JSONField(ordinal = 26name = "FSETTLEMAINBOOKID")
     *
     * @param FSETTLEMAINBOOKID @JSONField(ordinal = 26name = "FSETTLEMAINBOOKID")
     */
    public void setFSETTLEMAINBOOKID(Object FSETTLEMAINBOOKID) {
        this.FSETTLEMAINBOOKID = FSETTLEMAINBOOKID;
    }

    /**
     * 获取 @JSONField(ordinal = 27name = "FPAYBILLENTRY")
     *
     * @return FPAYBILLENTRY @JSONField(ordinal = 27name = "FPAYBILLENTRY")
     */
    public List<K3PaymentNoteModelTRY> getFPAYBILLENTRY() {
        return this.FPAYBILLENTRY;
    }

    /**
     * 设置 @JSONField(ordinal = 27name = "FPAYBILLENTRY")
     *
     * @param FPAYBILLENTRY @JSONField(ordinal = 27name = "FPAYBILLENTRY")
     */
    public void setFPAYBILLENTRY(List<K3PaymentNoteModelTRY> FPAYBILLENTRY) {
        this.FPAYBILLENTRY = FPAYBILLENTRY;
    }

    /**
     * 获取 @JSONField(ordinal = 27name = "F_qqqq_Text3")
     *
     * @return F_qqqq_Text3 @JSONField(ordinal = 27name = "F_qqqq_Text3")
     */
    public String getF_qqqq_Text3() {
        return this.F_qqqq_Text3;
    }

    /**
     * 设置 @JSONField(ordinal = 27name = "F_qqqq_Text3")
     *
     * @param F_qqqq_Text3 @JSONField(ordinal = 27name = "F_qqqq_Text3")
     */
    public void setF_qqqq_Text3(String F_qqqq_Text3) {
        this.F_qqqq_Text3 = F_qqqq_Text3;
    }

    /**
     * 获取 @JSONField(ordinal = 28name = "F_qqqq_Text5")
     *
     * @return F_qqqq_Text5 @JSONField(ordinal = 28name = "F_qqqq_Text5")
     */
    public String getF_qqqq_Text5() {
        return this.F_qqqq_Text5;
    }

    /**
     * 设置 @JSONField(ordinal = 28name = "F_qqqq_Text5")
     *
     * @param F_qqqq_Text5 @JSONField(ordinal = 28name = "F_qqqq_Text5")
     */
    public void setF_qqqq_Text5(String F_qqqq_Text5) {
        this.F_qqqq_Text5 = F_qqqq_Text5;
    }

    /**
     * 获取 @JSONField(ordinal = 28name = "F_qqqq_Text6")
     *
     * @return F_qqqq_Text6 @JSONField(ordinal = 28name = "F_qqqq_Text6")
     */
    public String getF_qqqq_Text6() {
        return this.F_qqqq_Text6;
    }

    /**
     * 设置 @JSONField(ordinal = 28name = "F_qqqq_Text6")
     *
     * @param F_qqqq_Text6 @JSONField(ordinal = 28name = "F_qqqq_Text6")
     */
    public void setF_qqqq_Text6(String F_qqqq_Text6) {
        this.F_qqqq_Text6 = F_qqqq_Text6;
    }

    /**
     * 获取 @JSONField(ordinal = 28name = "F_qqqq_Text4")
     *
     * @return F_qqqq_Text4 @JSONField(ordinal = 28name = "F_qqqq_Text4")
     */
    public String getF_qqqq_Text4() {
        return this.F_qqqq_Text4;
    }

    /**
     * 设置 @JSONField(ordinal = 28name = "F_qqqq_Text4")
     *
     * @param F_qqqq_Text4 @JSONField(ordinal = 28name = "F_qqqq_Text4")
     */
    public void setF_qqqq_Text4(String F_qqqq_Text4) {
        this.F_qqqq_Text4 = F_qqqq_Text4;
    }

    /**
     * 获取 @JSONField(ordinal = 31name = "F_qqqq_Text7")
     *
     * @return F_qqqq_Text7 @JSONField(ordinal = 31name = "F_qqqq_Text7")
     */
    public String getF_qqqq_Text7() {
        return this.F_qqqq_Text7;
    }

    /**
     * 设置 @JSONField(ordinal = 31name = "F_qqqq_Text7")
     *
     * @param F_qqqq_Text7 @JSONField(ordinal = 31name = "F_qqqq_Text7")
     */
    public void setF_qqqq_Text7(String F_qqqq_Text7) {
        this.F_qqqq_Text7 = F_qqqq_Text7;
    }
}

