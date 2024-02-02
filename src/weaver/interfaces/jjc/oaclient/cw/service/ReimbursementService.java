package weaver.interfaces.jjc.oaclient.cw.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import weaver.general.Util;
import weaver.interfaces.jjc.k3client.cw.dto.K3PaymentNoteBill;
import weaver.interfaces.jjc.k3client.cw.dto.K3PaymentNoteModel;
import weaver.interfaces.jjc.k3client.cw.dto.K3PaymentNoteModelTRY;
import weaver.interfaces.jjc.k3client.cw.service.K3PaymentNoteService;
import weaver.interfaces.jjc.oaclient.cw.dao.BaseDao;
import weaver.interfaces.jjc.oaclient.cw.dao.ReimbursementDao;
import weaver.interfaces.jjc.oaclient.cw.dto.OAPaymentNoteDto2;
import weaver.interfaces.jjc.oaclient.cw.dto.ReimbursementBill;
import weaver.interfaces.jjc.oaclient.cw.dto.ReimbursementDt2;
import weaver.interfaces.jjc.oaclient.cw.dto.ReimbursementDt4;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class ReimbursementService {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    K3PaymentNoteService k3PaymentNoteService = new K3PaymentNoteService();

    public void exec(String requestid, String tablename) throws Exception {
        ReimbursementDao reimbursementDao = new ReimbursementDao();
        ReimbursementBill reimbursementBill = reimbursementDao.getReimbursementBill(requestid, tablename);
        List<ReimbursementDt2> reimbursementDt2s = new ArrayList<>();
        if ("0".equals(reimbursementBill.getSfrygz())) {//是
            reimbursementDt2s = reimbursementDao.getReimbursementDt5List(reimbursementBill.getId(), tablename);
        } else {//
            reimbursementDt2s = reimbursementDao.getReimbursementDt2List(reimbursementBill.getId(), tablename);
        }
        List<ReimbursementDt4> reimbursementDt4s = reimbursementDao.getReimbursementDt4List(reimbursementBill.getId(), tablename);
        List<OAPaymentNoteDto2> oaPaymentNoteDto2s = getOAPaymentNoteDto2(reimbursementDt4s, reimbursementDt2s);
        K3PaymentNoteBill k3PaymentNoteBill = new K3PaymentNoteBill();
        List<K3PaymentNoteModelTRY> k3PaymentNoteModelTRIES = getK3PaymentNoteModelTRYList(oaPaymentNoteDto2s);
        K3PaymentNoteModel k3PaymentNoteModel = getK3PaymentNoteModel(reimbursementBill);
        k3PaymentNoteModel.setFPAYBILLENTRY(k3PaymentNoteModelTRIES);
        k3PaymentNoteBill.setModel(k3PaymentNoteModel);
        String fhz = k3PaymentNoteService.exec(k3PaymentNoteBill);
        System.out.println("fhz:" + fhz);
        JSONObject fhzjson = JSON.parseObject(fhz);
        System.out.println("返回数据体:" + fhzjson);
        JSONObject result = fhzjson.getJSONObject("Result");
        JSONObject responseStatus = result.getJSONObject("ResponseStatus");
        boolean isSuccess = responseStatus.getBoolean("IsSuccess");
        if (!isSuccess) {
            JSONArray errors = responseStatus.getJSONArray("Errors");
            String errStr = "";
            for (int i = 0; i < errors.size(); i++) {
                JSONObject err = errors.getJSONObject(i);
                errStr += err.getString("Message");
            }
            throw new Exception(errStr);
        }
    }

    private List<OAPaymentNoteDto2> getOAPaymentNoteDto2(List<ReimbursementDt4> reimbursementDt4s, List<ReimbursementDt2> reimbursementDt2s) {
        List<OAPaymentNoteDto2> oaPaymentNoteDto2s = new ArrayList<>();
        for (int i = 0; i < reimbursementDt2s.size(); i++) {
            double shje = Util.getDoubleValue(reimbursementDt2s.get(i).getSkjey(), 0);//获取税后金额
            double se = Util.getDoubleValue(reimbursementDt2s.get(i).getSe(), 0);//获取税额
            double xyje = shje + se;//总额
            for (ReimbursementDt4 reimbursementDt4 : reimbursementDt4s) {
                double yssyje = reimbursementDt4.getJe();
                if (yssyje <= 0) continue;
                //如果明细4的支出金额>=明细2某一行的金额，则在明细4扣除明细2的金额，并重新给明细4金额赋值，向金蝶传输明细2本次付款总额和税后金额
                if (yssyje >= xyje) {
                    System.out.println("进入税额获取");
                    System.out.println("税额是" + se);
                    System.out.println("未税是" + shje);
                    System.out.println("总额是" + xyje);
                    OAPaymentNoteDto2 oaPaymentNoteDto2 = new OAPaymentNoteDto2();
                    oaPaymentNoteDto2.setJe(xyje + "");
                    oaPaymentNoteDto2.setWsje(shje + "");
                    oaPaymentNoteDto2.setKhx(reimbursementDt2s.get(i).getKhx());
                    oaPaymentNoteDto2.setSkr(reimbursementDt2s.get(i).getSkr());
                    oaPaymentNoteDto2.setZckm(reimbursementDt4.getZckm());
                    oaPaymentNoteDto2.setSkzh(reimbursementDt2s.get(i).getSkzh());
                    oaPaymentNoteDto2.setSe(reimbursementDt2s.get(i).getSe());
                    oaPaymentNoteDto2.setLhh(reimbursementDt2s.get(i).getLhh());
                    oaPaymentNoteDto2s.add(oaPaymentNoteDto2);
                    reimbursementDt4.setJe(sub(yssyje, xyje));
                    break;
                    //如果明细4金额<明细2的金额，就在明细二人员金额里面扣减明细4所有金额，然后给金蝶传输总金额和税后金额（此时是明细4金额总额和明细4金额乘税额在明细2中占的百分比）
                } else {
                    System.out.println("未进入税额获取");
                    // xyje = sub(xyje, yssyje);
                    double se_percent=se/xyje;
                    double shje_percent=shje/xyje;
                    se=format2double(sub(se,yssyje*se_percent));
                    shje=format2double(sub(shje,yssyje*shje_percent));
                    xyje=se+shje;
                    yssyje=format2double(yssyje*shje_percent)+format2double(yssyje*se_percent);
                    reimbursementDt4.setJe(0);
                    OAPaymentNoteDto2 oaPaymentNoteDto2 = new OAPaymentNoteDto2();
                    oaPaymentNoteDto2.setWsje(format2double(yssyje*shje_percent)+"");
                    oaPaymentNoteDto2.setJe(yssyje + "");
                    oaPaymentNoteDto2.setKhx(reimbursementDt2s.get(i).getKhx());
                    oaPaymentNoteDto2.setSkr(reimbursementDt2s.get(i).getSkr());
                    oaPaymentNoteDto2.setZckm(reimbursementDt4.getZckm());
                    oaPaymentNoteDto2.setSkzh(reimbursementDt2s.get(i).getSkzh());
                    oaPaymentNoteDto2.setSe(reimbursementDt2s.get(i).getSe());
                    oaPaymentNoteDto2.setLhh(reimbursementDt2s.get(i).getLhh());
                    oaPaymentNoteDto2s.add(oaPaymentNoteDto2);
                    continue;
                }

            }


        }


        return oaPaymentNoteDto2s;
    }
    //取两位小数
    public Double format2double(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public double sub(double v1, double v2) {
        java.math.BigDecimal b1 = new java.math.BigDecimal(Double.toString(v1));
        java.math.BigDecimal b2 = new java.math.BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    private List<K3PaymentNoteModelTRY> getK3PaymentNoteModelTRYList(List<OAPaymentNoteDto2> oaPaymentNoteDto2List) {
        List<K3PaymentNoteModelTRY> k3PaymentNoteModelTRIES = new ArrayList<>();
        if(oaPaymentNoteDto2List==null){
            K3PaymentNoteModelTRY k3PaymentNoteModelTRY = new K3PaymentNoteModelTRY();
            JSONObject FSETTLETYPEID = new JSONObject();
            FSETTLETYPEID.put("FNumber", "JSFS10_SYS");
            k3PaymentNoteModelTRY.setFSETTLETYPEID(FSETTLETYPEID);
        }
        for (OAPaymentNoteDto2 oaPaymentNoteDto2 : oaPaymentNoteDto2List) {
            if (Util.getDoubleValue(oaPaymentNoteDto2.getJe(), 0.0) <= 0) {
                continue;
            }
            K3PaymentNoteModelTRY k3PaymentNoteModelTRY = new K3PaymentNoteModelTRY();
            JSONObject FSETTLETYPEID = new JSONObject();
            FSETTLETYPEID.put("FNumber", "JSFS04_SYS");
            k3PaymentNoteModelTRY.setFSETTLETYPEID(FSETTLETYPEID);

            JSONObject FPURPOSEID = new JSONObject();
            FPURPOSEID.put("FNumber", "SFKYT10_SYS");
            k3PaymentNoteModelTRY.setFPURPOSEID(FPURPOSEID);

            k3PaymentNoteModelTRY.setFPAYTOTALAMOUNTFOR(Util.getDoubleValue(oaPaymentNoteDto2.getWsje(), 0.0));
            k3PaymentNoteModelTRY.setFPAYAMOUNTFOR_E(Util.getDoubleValue(oaPaymentNoteDto2.getJe(), 0.0));
            k3PaymentNoteModelTRY.setFSETTLEDISTAMOUNTFOR(0.0D);
            k3PaymentNoteModelTRY.setFSETTLEPAYAMOUNTFOR(Util.getDoubleValue(oaPaymentNoteDto2.getWsje(), 0.0));
            k3PaymentNoteModelTRY.setFREALPAYAMOUNTFOR_D(Util.getDoubleValue(oaPaymentNoteDto2.getWsje(), 0.0));
            JSONObject FACCOUNTID = new JSONObject();
            FACCOUNTID.put("FNumber", "182703469395");
            k3PaymentNoteModelTRY.setFACCOUNTID(FACCOUNTID);
            k3PaymentNoteModelTRY.setFOPPOSITEBANKACCOUNT(oaPaymentNoteDto2.getSkzh());
            k3PaymentNoteModelTRY.setFOPPOSITECCOUNTNAME(oaPaymentNoteDto2.getSkr());
            k3PaymentNoteModelTRY.setFOPPOSITEBANKNAME(oaPaymentNoteDto2.getKhx());
            k3PaymentNoteModelTRY.setFCOMMENT("");
            k3PaymentNoteModelTRY.setFRecType("1");
            k3PaymentNoteModelTRY.setFCNAPS(oaPaymentNoteDto2.getLhh());
            k3PaymentNoteModelTRY.setFPAYAMOUNT_E(Util.getDoubleValue(oaPaymentNoteDto2.getJe(), 0.0));
            k3PaymentNoteModelTRY.setFPOSTDATE(df.format(new Date()) + " 00:00:00");
            k3PaymentNoteModelTRY.setFRuZhangType("1");
            k3PaymentNoteModelTRY.setFPayType("A");
            k3PaymentNoteModelTRY.setFTaxAmt(Util.getDoubleValue(oaPaymentNoteDto2.getSe(), 0.0));
            JSONObject FBankDetail = new JSONObject();
            FBankDetail.put("FNumber", oaPaymentNoteDto2.getKhx());
            k3PaymentNoteModelTRY.setFBankDetail(FBankDetail);
            JSONObject F_qqqq_Base = new JSONObject();
            F_qqqq_Base.put("FNUMBER", oaPaymentNoteDto2.getZckm());
            k3PaymentNoteModelTRY.setF_qqqq_Base(F_qqqq_Base);
            k3PaymentNoteModelTRY.setF_qqqq_Text(oaPaymentNoteDto2.getSkr());
            k3PaymentNoteModelTRY.setF_qqqq_Text1(oaPaymentNoteDto2.getSkzh());
            k3PaymentNoteModelTRY.setF_qqqq_Text2(oaPaymentNoteDto2.getKhx());
            k3PaymentNoteModelTRIES.add(k3PaymentNoteModelTRY);
        }

        return k3PaymentNoteModelTRIES;
    }


    private K3PaymentNoteModel getK3PaymentNoteModel(ReimbursementBill reimbursementBill) {
        K3PaymentNoteModel k3PaymentNoteModel = new K3PaymentNoteModel();
//        String sqrGh = BaseDao.getUserById(Util.getIntValue(oaPaymentNoteBill.getBxry(),0)).get;
        String sqrGh = BaseDao.getWorkCode(Util.getIntValue(reimbursementBill.getBxry(), 0));
        String sqrname = BaseDao.getLastName(Util.getIntValue(reimbursementBill.getBxry(), 0));
        String bmName = BaseDao.getDepartmentName(Util.getIntValue(reimbursementBill.getBxry(), 0));
        String lcbt = BaseDao.getRequestname(reimbursementBill.getRequestid());
        k3PaymentNoteModel.setFID(0);

        JSONObject FBillTypeID = new JSONObject();
        FBillTypeID.put("FNUMBER", "FKDLX04_SYS");
        k3PaymentNoteModel.setFBillTypeID(FBillTypeID);
        k3PaymentNoteModel.setFDATE(df.format(new Date()) + " 00:00:00");
        k3PaymentNoteModel.setFCONTACTUNITTYPE("BD_Empinfo");
        JSONObject FCONTACTUNIT = new JSONObject();
        FCONTACTUNIT.put("FNumber", sqrGh);
        k3PaymentNoteModel.setFCONTACTUNIT(FCONTACTUNIT);
        k3PaymentNoteModel.setFRECTUNITTYPE("BD_Empinfo");
        JSONObject FRECTUNIT = new JSONObject();
        FRECTUNIT.put("FNumber", sqrGh);
        k3PaymentNoteModel.setFRECTUNIT(FRECTUNIT);
        k3PaymentNoteModel.setF_qqqq_Text7(sqrname);
        k3PaymentNoteModel.setFISINIT(false);
        JSONObject FCURRENCYID = new JSONObject();
        FCURRENCYID.put("FNumber", "PRE001");
        k3PaymentNoteModel.setFCURRENCYID(FCURRENCYID);
        k3PaymentNoteModel.setFEXCHANGERATE(1.0);
        k3PaymentNoteModel.setFSETTLERATE(1.0);
        JSONObject FSETTLEORGID = new JSONObject();
        FSETTLEORGID.put("FNumber", "100");
        k3PaymentNoteModel.setFSETTLEORGID(FSETTLEORGID);
        JSONObject FPURCHASEORGID = new JSONObject();
        FPURCHASEORGID.put("FNumber", "100");
        k3PaymentNoteModel.setFPURCHASEORGID(FPURCHASEORGID);
        k3PaymentNoteModel.setFDOCUMENTSTATUS("Z");
        k3PaymentNoteModel.setFBUSINESSTYPE("3");
        k3PaymentNoteModel.setFCancelStatus("A");
        JSONObject FPAYORGID = new JSONObject();
        FPAYORGID.put("FNumber", "100");
        k3PaymentNoteModel.setFPAYORGID(FPAYORGID);

        k3PaymentNoteModel.setFISSAMEORG(true);
        k3PaymentNoteModel.setFIsCredit(false);
        JSONObject FSETTLECUR = new JSONObject();
        FSETTLECUR.put("FNUMBER", "PRE001");
        k3PaymentNoteModel.setFSETTLECUR(FSETTLECUR);
        k3PaymentNoteModel.setFIsWriteOff(false);
        k3PaymentNoteModel.setFREALPAY(false);
        k3PaymentNoteModel.setFREMARK(reimbursementBill.getBxyt());
        k3PaymentNoteModel.setFBookingDate(df.format(new Date()) + " 00:00:00");
        k3PaymentNoteModel.setFISCARRYRATE(false);
        JSONObject FSETTLEMAINBOOKID = new JSONObject();
        FSETTLEMAINBOOKID.put("FNUMBER", "PRE001");
        k3PaymentNoteModel.setFSETTLEMAINBOOKID(FSETTLEMAINBOOKID);
        k3PaymentNoteModel.setF_qqqq_Text3(reimbursementBill.getLcbh());//流程编号
        k3PaymentNoteModel.setF_qqqq_Text5(reimbursementBill.getXmbh());//项目编号
        k3PaymentNoteModel.setF_qqqq_Text6(bmName);//部门名称
        k3PaymentNoteModel.setF_qqqq_Text4(lcbt);//报销用途

        return k3PaymentNoteModel;
    }
}
