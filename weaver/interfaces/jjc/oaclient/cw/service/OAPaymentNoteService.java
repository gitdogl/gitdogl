package weaver.interfaces.jjc.oaclient.cw.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.jjc.k3client.cw.dto.K3PaymentNoteBill;
import weaver.interfaces.jjc.k3client.cw.dto.K3PaymentNoteModel;
import weaver.interfaces.jjc.k3client.cw.dto.K3PaymentNoteModelTRY;
import weaver.interfaces.jjc.k3client.cw.service.K3PaymentNoteService;
import weaver.interfaces.jjc.oaclient.cw.dao.BaseDao;
import weaver.interfaces.jjc.oaclient.cw.dao.OAPaymentNoteDao;
import weaver.interfaces.jjc.oaclient.cw.dto.OAPaymentNoteBill;
import weaver.interfaces.jjc.oaclient.cw.dto.OAPaymentNoteDto2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OAPaymentNoteService {

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    OAPaymentNoteDao oaPaymentNoteDao = new OAPaymentNoteDao();
    K3PaymentNoteService k3PaymentNoteService = new K3PaymentNoteService();

    public void exec(String requestid, String tableName) throws Exception {
        OAPaymentNoteBill oaPaymentNoteBill = oaPaymentNoteDao.getOAPaymentNoteBill(requestid, tableName);
        List<OAPaymentNoteDto2> oaPaymentNoteDto2List = oaPaymentNoteDao.getOAPaymentNoteDto2List(requestid);
        K3PaymentNoteBill k3PaymentNoteBill = new K3PaymentNoteBill();
        K3PaymentNoteModel k3PaymentNoteModel = getK3PaymentNoteModel(oaPaymentNoteBill);
        List<K3PaymentNoteModelTRY> k3PaymentNoteModelTRIES = getK3PaymentNoteModelTRYList(oaPaymentNoteDto2List);
        if (k3PaymentNoteModelTRIES.size() == 0) {
            return;
        }
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
        } else {
            int id = result.getIntValue("Id");

            RecordSet rs = new RecordSet();
            String sql = "update " + tableName + " set k3fkdid = " + id + " where requestid = " + requestid;
//            rs.executeUpdate(sql);


        }

    }


    private List<K3PaymentNoteModelTRY> getK3PaymentNoteModelTRYList(List<OAPaymentNoteDto2> oaPaymentNoteDto2List) {
        List<K3PaymentNoteModelTRY> k3PaymentNoteModelTRIES = new ArrayList<>();
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

            k3PaymentNoteModelTRY.setFPAYTOTALAMOUNTFOR(Util.getDoubleValue(oaPaymentNoteDto2.getJe(), 0.0));
            k3PaymentNoteModelTRY.setFPAYAMOUNTFOR_E(Util.getDoubleValue(oaPaymentNoteDto2.getJe(), 0.0));
            k3PaymentNoteModelTRY.setFSETTLEPAYAMOUNTFOR(Util.getDoubleValue(oaPaymentNoteDto2.getJe(), 0.0));
            k3PaymentNoteModelTRY.setFREALPAYAMOUNTFOR_D(Util.getDoubleValue(oaPaymentNoteDto2.getJe(), 0.0));
            JSONObject FACCOUNTID = new JSONObject();
            FACCOUNTID.put("FNumber", "182703469395");
            k3PaymentNoteModelTRY.setFACCOUNTID(FACCOUNTID);
            k3PaymentNoteModelTRY.setFCOMMENT("");
            k3PaymentNoteModelTRY.setFRecType("1");
            k3PaymentNoteModelTRY.setFPAYAMOUNT_E(Util.getDoubleValue(oaPaymentNoteDto2.getJe(), 0.0));
            k3PaymentNoteModelTRY.setFPOSTDATE(df.format(new Date()) + " 00:00:00");
            k3PaymentNoteModelTRY.setFRuZhangType("1");
            k3PaymentNoteModelTRY.setFPayType("A");
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


    private K3PaymentNoteModel getK3PaymentNoteModel(OAPaymentNoteBill oaPaymentNoteBill) {
        K3PaymentNoteModel k3PaymentNoteModel = new K3PaymentNoteModel();
//        String sqrGh = BaseDao.getUserById(Util.getIntValue(oaPaymentNoteBill.getBxry(),0)).get;
        String sqrGh = BaseDao.getWorkCode(Util.getIntValue(oaPaymentNoteBill.getBxry(), 0));
        String sqrname = BaseDao.getLastName(Util.getIntValue(oaPaymentNoteBill.getBxry(), 0));
        String bmName = BaseDao.getDepartmentName(Util.getIntValue(oaPaymentNoteBill.getBxry(), 0));
        String lcbt = BaseDao.getRequestname(oaPaymentNoteBill.getRequestid());
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
        k3PaymentNoteModel.setFREMARK(oaPaymentNoteBill.getBxsy());
        k3PaymentNoteModel.setFBookingDate(df.format(new Date()) + " 00:00:00");
        k3PaymentNoteModel.setFISCARRYRATE(false);
        JSONObject FSETTLEMAINBOOKID = new JSONObject();
        FSETTLEMAINBOOKID.put("FNUMBER", "PRE001");
        k3PaymentNoteModel.setFSETTLEMAINBOOKID(FSETTLEMAINBOOKID);
        k3PaymentNoteModel.setF_qqqq_Text3(oaPaymentNoteBill.getBh());//流程编号
        k3PaymentNoteModel.setF_qqqq_Text5(oaPaymentNoteBill.getXmbh());//项目编号
        k3PaymentNoteModel.setF_qqqq_Text6(bmName);//部门名称
        k3PaymentNoteModel.setF_qqqq_Text4(lcbt);//报销用途

        oaPaymentNoteBill.getXgxm();
        return k3PaymentNoteModel;
    }

}
