package weaver.interfaces.jjc.k3client.cw.dto;


import com.alibaba.fastjson.annotation.JSONField;

public class K3PaymentNoteModelTRY {
    @JSONField(
            ordinal = 1,
            name = "FSETTLETYPEID"
    )
    Object FSETTLETYPEID = new Object();
    @JSONField(
            ordinal = 2,
            name = "FPURPOSEID"
    )
    Object FPURPOSEID = new Object();
    @JSONField(
            ordinal = 3,
            name = "FPAYTOTALAMOUNTFOR"
    )
    double FPAYTOTALAMOUNTFOR = 0.0D;
    @JSONField(
            ordinal = 4,
            name = "FPAYAMOUNTFOR_E"
    )
    double FPAYAMOUNTFOR_E = 0.0D;

    @JSONField(
            ordinal = 5,
            name = "FSETTLEDISTAMOUNTFOR"
    )
    double FSETTLEDISTAMOUNTFOR = 0.0D;

    @JSONField(
            ordinal = 6,
            name = "FSETTLEPAYAMOUNTFOR"
    )
    double FSETTLEPAYAMOUNTFOR = 0.0D;


    @JSONField(
            ordinal = 7,
            name = "FREALPAYAMOUNTFOR_D"
    )
    double FREALPAYAMOUNTFOR_D = 0.0D;
    @JSONField(
            ordinal = 8,
            name = "FACCOUNTID"
    )
    Object FACCOUNTID = new Object();
    @JSONField(
            ordinal = 9,
            name = "FOPPOSITEBANKACCOUNT"
    )
    String FOPPOSITEBANKACCOUNT = "";
    @JSONField(
            ordinal = 10,
            name = "FOPPOSITECCOUNTNAME"
    )
    String FOPPOSITECCOUNTNAME = "";
    @JSONField(
            ordinal = 11,
            name = "FOPPOSITEBANKNAME"
    )
    String FOPPOSITEBANKNAME = "";

    @JSONField(
            ordinal = 12,
            name = "FCOMMENT"
    )
    String FCOMMENT = "";

    @JSONField(
            ordinal = 13,
            name = "FRecType"
    )
    String FRecType = "";
    @JSONField(
            ordinal = 14,
            name = "FCNAPS"
    )
    String FCNAPS = "";
    @JSONField(
            ordinal = 15,
            name = "FPAYAMOUNT_E"
    )
    double FPAYAMOUNT_E = 0.0D;
    @JSONField(
            ordinal = 16,
            name = "FPOSTDATE"
    )
    String FPOSTDATE = "";
    @JSONField(
            ordinal = 17,
            name = "FRuZhangType"
    )
    String FRuZhangType = "";

    @JSONField(
            ordinal = 18,
            name = "FPayType"
    )
    String FPayType = "";
    @JSONField(
            ordinal = 19,
            name = "FTaxAmt"
    )
    double FTaxAmt = 0.0D;
    @JSONField(
            ordinal = 20,
            name = "FBankDetail"
    )
    Object FBankDetail = new Object();
    @JSONField(
            ordinal = 21,
            name = "F_qqqq_Base"
    )
    Object F_qqqq_Base = new Object();
    @JSONField(
            ordinal = 22,
            name = "F_qqqq_Text"
    )
    String F_qqqq_Text = "";
    @JSONField(
            ordinal = 23,
            name = "F_qqqq_Text1"
    )
    String F_qqqq_Text1 = "";
    @JSONField(
            ordinal = 24,
            name = "F_qqqq_Text2"
    )
    String F_qqqq_Text2 = "";

    /**
     * 获取 @JSONField(ordinal = 1 name = "FSETTLETYPEID")
     *
     * @return FSETTLETYPEID @JSONField(ordinal = 1 name = "FSETTLETYPEID")
     */
    public Object getFSETTLETYPEID() {
        return FSETTLETYPEID;
    }
    /**
     * 设置 @JSONField(ordinal = 1 name = "FSETTLETYPEID")
     *
     * @return FSETTLETYPEID @JSONField(ordinal = 1 name = "FSETTLETYPEID")
     */
    public void setFSETTLETYPEID(Object FSETTLETYPEID) {
        this.FSETTLETYPEID = FSETTLETYPEID;
    }

    /**
     * 获取 @JSONField(ordinal = 2 name = "FPURPOSEID")
     *
     * @return FPURPOSEID @JSONField(ordinal = 2 name = "FPURPOSEID")
     */
    public Object getFPURPOSEID() {
        return FPURPOSEID;
    }
    /**
     * 设置 @JSONField(ordinal = 2 name = "FPURPOSEID")
     *
     * @return FPURPOSEID @JSONField(ordinal = 2 name = "FPURPOSEID")
     */
    public void setFPURPOSEID(Object FPURPOSEID) {
        this.FPURPOSEID = FPURPOSEID;
    }
    /**
     * 获取 @JSONField(ordinal = 3 name = "FPAYTOTALAMOUNTFOR")
     *
     * @return FPAYTOTALAMOUNTFOR @JSONField(ordinal = 3 name = "FPAYTOTALAMOUNTFOR")
     */
    public double getFPAYTOTALAMOUNTFOR() {
        return FPAYTOTALAMOUNTFOR;
    }
    /**
     * 设置 @JSONField(ordinal = 3 name = "FPAYTOTALAMOUNTFOR")
     *
     * @return FPAYTOTALAMOUNTFOR @JSONField(ordinal = 3 name = "FPAYTOTALAMOUNTFOR")
     */
    public void setFPAYTOTALAMOUNTFOR(double FPAYTOTALAMOUNTFOR) {
        this.FPAYTOTALAMOUNTFOR = FPAYTOTALAMOUNTFOR;
    }
    /**
     * 获取 @JSONField(ordinal = 4 name = "FPAYAMOUNTFOR_E")
     *
     * @return FPAYAMOUNTFOR_E @JSONField(ordinal = 4 name = "FPAYAMOUNTFOR_E")
     */
    public double getFPAYAMOUNTFOR_E() {
        return FPAYAMOUNTFOR_E;
    }
    /**
     * 设置 @JSONField(ordinal = 4 name = "FPAYAMOUNTFOR_E")
     *
     * @return FPAYAMOUNTFOR_E @JSONField(ordinal = 4 name = "FPAYAMOUNTFOR_E")
     */
    public void setFPAYAMOUNTFOR_E(double FPAYAMOUNTFOR_E) {
        this.FPAYAMOUNTFOR_E = FPAYAMOUNTFOR_E;
    }
    /**
     * 获取 @JSONField(ordinal = 5 name = "FSETTLEDISTAMOUNTFOR")
     *
     * @return FSETTLEDISTAMOUNTFOR @JSONField(ordinal = 5 name = "FSETTLEDISTAMOUNTFOR")
     */
    public double getFSETTLEDISTAMOUNTFOR() {
        return FSETTLEDISTAMOUNTFOR;
    }
    /**
     * 设置 @JSONField(ordinal = 5 name = "FSETTLEDISTAMOUNTFOR")
     *
     * @return FSETTLEDISTAMOUNTFOR @JSONField(ordinal = 5 name = "FSETTLEDISTAMOUNTFOR")
     */
    public void setFSETTLEDISTAMOUNTFOR(double FSETTLEDISTAMOUNTFOR) {
        this.FSETTLEDISTAMOUNTFOR = FSETTLEDISTAMOUNTFOR;
    }
    /**
     * 获取 @JSONField(ordinal = 6 name = "FSETTLEPAYAMOUNTFOR")
     *
     * @return FSETTLEPAYAMOUNTFOR @JSONField(ordinal = 6 name = "FSETTLEPAYAMOUNTFOR")
     */
    public double getFSETTLEPAYAMOUNTFOR() {
        return FSETTLEPAYAMOUNTFOR;
    }
    /**
     * 设置 @JSONField(ordinal = 6 name = "FSETTLEPAYAMOUNTFOR")
     *
     * @return FSETTLEPAYAMOUNTFOR @JSONField(ordinal = 6 name = "FSETTLEPAYAMOUNTFOR")
     */
    public void setFSETTLEPAYAMOUNTFOR(double FSETTLEPAYAMOUNTFOR) {
        this.FSETTLEPAYAMOUNTFOR = FSETTLEPAYAMOUNTFOR;
    }
    /**
     * 获取 @JSONField(ordinal = 7 name = "FREALPAYAMOUNTFOR_D")
     *
     * @return FREALPAYAMOUNTFOR_D @JSONField(ordinal = 7 name = "FREALPAYAMOUNTFOR_D")
     */
    public double getFREALPAYAMOUNTFOR_D() {
        return FREALPAYAMOUNTFOR_D;
    }
    /**
     * 设置 @JSONField(ordinal = 7 name = "FREALPAYAMOUNTFOR_D")
     *
     * @return FREALPAYAMOUNTFOR_D @JSONField(ordinal = 7 name = "FREALPAYAMOUNTFOR_D")
     */
    public void setFREALPAYAMOUNTFOR_D(double FREALPAYAMOUNTFOR_D) {
        this.FREALPAYAMOUNTFOR_D = FREALPAYAMOUNTFOR_D;
    }
    /**
     * 获取 @JSONField(ordinal = 8 name = "FACCOUNTID")
     *
     * @return FACCOUNTID @JSONField(ordinal = 8 name = "FACCOUNTID")
     */
    public Object getFACCOUNTID() {
        return FACCOUNTID;
    }
    /**
     * 设置 @JSONField(ordinal = 8 name = "FACCOUNTID")
     *
     * @return FACCOUNTID @JSONField(ordinal = 8 name = "FACCOUNTID")
     */
    public void setFACCOUNTID(Object FACCOUNTID) {
        this.FACCOUNTID = FACCOUNTID;
    }
    /**
     * 获取 @JSONField(ordinal = 9 name = "FOPPOSITEBANKACCOUNT")
     *
     * @return FOPPOSITEBANKACCOUNT @JSONField(ordinal = 9 name = "FOPPOSITEBANKACCOUNT")
     */
    public String getFOPPOSITEBANKACCOUNT() {
        return FOPPOSITEBANKACCOUNT;
    }
    /**
     * 设置 @JSONField(ordinal = 9 name = "FOPPOSITEBANKACCOUNT")
     *
     * @return FOPPOSITEBANKACCOUNT @JSONField(ordinal = 9 name = "FOPPOSITEBANKACCOUNT")
     */
    public void setFOPPOSITEBANKACCOUNT(String FOPPOSITEBANKACCOUNT) {
        this.FOPPOSITEBANKACCOUNT = FOPPOSITEBANKACCOUNT;
    }
    /**
     * 获取 @JSONField(ordinal = 10 name = "FOPPOSITECCOUNTNAME")
     *
     * @return FOPPOSITECCOUNTNAME @JSONField(ordinal = 10 name = "FOPPOSITECCOUNTNAME")
     */
    public String getFOPPOSITECCOUNTNAME() {
        return FOPPOSITECCOUNTNAME;
    }
    /**
     * 设置 @JSONField(ordinal = 10 name = "FOPPOSITECCOUNTNAME")
     *
     * @return FOPPOSITECCOUNTNAME @JSONField(ordinal = 10 name = "FOPPOSITECCOUNTNAME")
     */
    public void setFOPPOSITECCOUNTNAME(String FOPPOSITECCOUNTNAME) {
        this.FOPPOSITECCOUNTNAME = FOPPOSITECCOUNTNAME;
    }
    /**
     * 获取 @JSONField(ordinal = 11 name = "FOPPOSITEBANKNAME")
     *
     * @return FOPPOSITEBANKNAME @JSONField(ordinal = 4 name = "FOPPOSITEBANKNAME")
     */
    public String getFOPPOSITEBANKNAME() {
        return FOPPOSITEBANKNAME;
    }
    /**
     * 设置 @JSONField(ordinal = 11 name = "FOPPOSITEBANKNAME")
     *
     * @return FOPPOSITEBANKNAME @JSONField(ordinal = 4 name = "FOPPOSITEBANKNAME")
     */
    public void setFOPPOSITEBANKNAME(String FOPPOSITEBANKNAME) {
        this.FOPPOSITEBANKNAME = FOPPOSITEBANKNAME;
    }
    /**
     * 获取 @JSONField(ordinal = 12 name = "FCOMMENT")
     *
     * @return FCOMMENT @JSONField(ordinal = 12 name = "FCOMMENT")
     */
    public String getFCOMMENT() {
        return FCOMMENT;
    }
    /**
     * 设置 @JSONField(ordinal = 12 name = "FCOMMENT")
     *
     * @return FCOMMENT @JSONField(ordinal = 12 name = "FCOMMENT")
     */
    public void setFCOMMENT(String FCOMMENT) {
        this.FCOMMENT = FCOMMENT;
    }
    /**
     * 获取 @JSONField(ordinal = 13 name = "FRecType")
     *
     * @return FRecType @JSONField(ordinal = 4 name = "FRecType")
     */
    public String getFRecType() {
        return FRecType;
    }
    /**
     * 设置 @JSONField(ordinal = 13 name = "FRecType")
     *
     * @return FRecType @JSONField(ordinal = 4 name = "FRecType")
     */
    public void setFRecType(String FRecType) {
        this.FRecType = FRecType;
    }
    /**
     * 获取 @JSONField(ordinal = 14 name = "FCNAPS")
     *
     * @return FCNAPS @JSONField(ordinal = 14 name = "FCNAPS")
     */
    public String getFCNAPS() {
        return FCNAPS;
    }
    /**
     * 设置 @JSONField(ordinal = 14 name = "FCNAPS")
     *
     * @return FCNAPS @JSONField(ordinal = 14 name = "FCNAPS")
     */
    public void setFCNAPS(String FCNAPS) {
        this.FCNAPS = FCNAPS;
    }
    /**
     * 获取 @JSONField(ordinal = 15 name = "FPAYAMOUNT_E")
     *
     * @return FPAYAMOUNT_E @JSONField(ordinal = 15 name = "FPAYAMOUNT_E")
     */
    public double getFPAYAMOUNT_E() {
        return FPAYAMOUNT_E;
    }
    /**
     * 设置 @JSONField(ordinal = 15 name = "FPAYAMOUNT_E")
     *
     * @return FPAYAMOUNT_E @JSONField(ordinal = 15 name = "FPAYAMOUNT_E")
     */
    public void setFPAYAMOUNT_E(double FPAYAMOUNT_E) {
        this.FPAYAMOUNT_E = FPAYAMOUNT_E;
    }
    /**
     * 获取 @JSONField(ordinal = 16 name = "FPOSTDATE")
     *
     * @return FPOSTDATE @JSONField(ordinal = 16 name = "FPOSTDATE")
     */
    public String getFPOSTDATE() {
        return FPOSTDATE;
    }
    /**
     * 设置 @JSONField(ordinal = 16 name = "FPOSTDATE")
     *
     * @return FPOSTDATE @JSONField(ordinal = 16 name = "FPOSTDATE")
     */
    public void setFPOSTDATE(String FPOSTDATE) {
        this.FPOSTDATE = FPOSTDATE;
    }
    /**
     * 获取 @JSONField(ordinal = 17 name = "FRuZhangType")
     *
     * @return FRuZhangType @JSONField(ordinal = 17 name = "FRuZhangType")
     */
    public String getFRuZhangType() {
        return FRuZhangType;
    }
    /**
     * 设置 @JSONField(ordinal = 17 name = "FRuZhangType")
     *
     * @return FRuZhangType @JSONField(ordinal = 17 name = "FRuZhangType")
     */
    public void setFRuZhangType(String FRuZhangType) {
        this.FRuZhangType = FRuZhangType;
    }
    /**
     * 获取 @JSONField(ordinal = 18 name = "FPayType")
     *
     * @return FPayType @JSONField(ordinal = 18 name = "FPayType")
     */
    public String getFPayType() {
        return FPayType;
    }
    /**
     * 设置 @JSONField(ordinal = 18 name = "FPayType")
     *
     * @return FPayType @JSONField(ordinal = 18 name = "FPayType")
     */
    public void setFPayType(String FPayType) {
        this.FPayType = FPayType;
    }
    /**
     * 获取 @JSONField(ordinal = 19 name = "FTaxAmt")
     *
     * @return FTaxAmt @JSONField(ordinal = 19 name = "FTaxAmt")
     */
    public double getFTaxAmt() {
        return FTaxAmt;
    }
    /**
     * 设置 @JSONField(ordinal = 19 name = "FTaxAmt")
     *
     * @return FTaxAmt @JSONField(ordinal = 4 name = "FTaxAmt")
     */
    public void setFTaxAmt(double FTaxAmt) {
        this.FTaxAmt = FTaxAmt;
    }
    /**
     * 获取 @JSONField(ordinal = 20 name = "FBankDetail")
     *
     * @return FBankDetail @JSONField(ordinal = 20 name = "FBankDetail")
     */
    public Object getFBankDetail() {
        return FBankDetail;
    }
    /**
     * 设置 @JSONField(ordinal = 20 name = "FBankDetail")
     *
     * @return FBankDetail @JSONField(ordinal =20 name = "FBankDetail")
     */
    public void setFBankDetail(Object FBankDetail) {
        this.FBankDetail = FBankDetail;
    }
    /**
     * 获取 @JSONField(ordinal = 21 name = "F_qqqq_Base")
     *
     * @return F_qqqq_Base @JSONField(ordinal = 21 name = "F_qqqq_Base")
     */
    public Object getF_qqqq_Base() {
        return F_qqqq_Base;
    }
    /**
     * 设置 @JSONField(ordinal = 21 name = "F_qqqq_Base")
     *
     * @return F_qqqq_Base @JSONField(ordinal = 21 name = "F_qqqq_Base")
     */
    public void setF_qqqq_Base(Object f_qqqq_Base) {
        F_qqqq_Base = f_qqqq_Base;
    }
    /**
     * 获取 @JSONField(ordinal = 22 name = "F_qqqq_Text")
     *
     * @return F_qqqq_Text @JSONField(ordinal = 22 name = "F_qqqq_Text")
     */
    public String getF_qqqq_Text() {
        return F_qqqq_Text;
    }
    /**
     * 设置 @JSONField(ordinal = 22 name = "F_qqqq_Text")
     *
     * @return F_qqqq_Text @JSONField(ordinal = 22 name = "F_qqqq_Text")
     */
    public void setF_qqqq_Text(String f_qqqq_Text) {
        F_qqqq_Text = f_qqqq_Text;
    }
    /**
     * 获取 @JSONField(ordinal = 23 name = "F_qqqq_Text1")
     *
     * @return F_qqqq_Text1 @JSONField(ordinal = 23 name = "F_qqqq_Text1")
     */
    public String getF_qqqq_Text1() {
        return F_qqqq_Text1;
    }
    /**
     * 设置 @JSONField(ordinal = 23 name = "F_qqqq_Text1")
     *
     * @return F_qqqq_Text1 @JSONField(ordinal = 23 name = "F_qqqq_Text1")
     */
    public void setF_qqqq_Text1(String f_qqqq_Text1) {
        F_qqqq_Text1 = f_qqqq_Text1;
    }
    /**
     * 获取 @JSONField(ordinal = 24 name = "F_qqqq_Text2")
     *
     * @return F_qqqq_Text2 @JSONField(ordinal = 24 name = "F_qqqq_Text2")
     */
    public String getF_qqqq_Text2() {
        return F_qqqq_Text2;
    }
    /**
     * 设置 @JSONField(ordinal = 24 name = "F_qqqq_Text2")
     *
     * @return F_qqqq_Text2 @JSONField(ordinal = 24 name = "F_qqqq_Text2")
     */
    public void setF_qqqq_Text2(String f_qqqq_Text2) {
        F_qqqq_Text2 = f_qqqq_Text2;
    }
}
