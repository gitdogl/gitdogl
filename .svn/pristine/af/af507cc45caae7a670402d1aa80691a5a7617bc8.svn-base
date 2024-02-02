package weaver.interfaces.jjc.oaclient.cw.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import weaver.general.Util;
import weaver.interfaces.jjc.k3client.cw.dto.K3StatementsBill;
import weaver.interfaces.jjc.k3client.cw.dto.K3StatementsModel;
import weaver.interfaces.jjc.k3client.cw.dto.K3StatementsModelTRY;
import weaver.interfaces.jjc.k3client.cw.service.K3StatementsService;
import weaver.interfaces.jjc.oaclient.cw.dao.ReceiptsDao;
import weaver.interfaces.jjc.oaclient.cw.dto.ReceiptsBill;
import weaver.interfaces.jjc.oaclient.cw.dto.ReceiptsDt8;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReceiptsService {
    K3StatementsService k3StatementsService = new K3StatementsService();
    ReceiptsDao receiptsDao = new ReceiptsDao();

    public void exec(String requestid, String tableName) throws Exception {
        ReceiptsBill receiptsBill = receiptsDao.getReceiptsBill(requestid, tableName);
        List<ReceiptsDt8> receiptsDt8s = receiptsDao.getReceiptsDt8s(receiptsBill.getId(), tableName);
        K3StatementsBill k3StatementsBill = new K3StatementsBill();
        K3StatementsModel k3StatementsModel = getK3StatementsModel(receiptsBill);
        List<K3StatementsModelTRY> keStatementsModelTRYList = getK3StatementsModelTRYList(receiptsDt8s, receiptsBill);
        k3StatementsModel.setFRECEIVEBILLENTRY(keStatementsModelTRYList);
        k3StatementsBill.setModel(k3StatementsModel);
        String fhz = k3StatementsService.exec(k3StatementsBill);
        JSONObject fhzjson = JSONObject.parseObject(fhz);
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

    public List<K3StatementsModelTRY> getK3StatementsModelTRYList(List<ReceiptsDt8> receiptsDt8s, ReceiptsBill receiptsBill) {
        List<K3StatementsModelTRY> keStatementsModelTRYList = new ArrayList<>();
        for (ReceiptsDt8 receiptsDt8 : receiptsDt8s) {
            K3StatementsModelTRY k3StatementsModelTRY = new K3StatementsModelTRY();
            JSONObject FSETTLETYPEID = new JSONObject();
            FSETTLETYPEID.put("FNumber", "JSFS04_SYS");
            k3StatementsModelTRY.setFSETTLETYPEID(FSETTLETYPEID);
            JSONObject FPURPOSEID = new JSONObject();
            FPURPOSEID.put("FNumber", "SFKYT099_SYS");
            k3StatementsModelTRY.setFPURPOSEID(FPURPOSEID);
            k3StatementsModelTRY.setFRECTOTALAMOUNTFOR(Util.getDoubleValue(receiptsDt8.getJzje(), 0.0));
            k3StatementsModelTRY.setFRECAMOUNTFOR_E(Util.getDoubleValue(receiptsDt8.getJzje(), 0.0));
            JSONObject FACCOUNTID = new JSONObject();
            FACCOUNTID.put("FNumber", receiptsBill.getJzzh());
            k3StatementsModelTRY.setFACCOUNTID(FACCOUNTID);
            k3StatementsModelTRY.setFSETTLENO("");
            k3StatementsModelTRY.setFCOMMENT(receiptsBill.getBz());

            k3StatementsModelTRY.setFRECAMOUNT_E(Util.getDoubleValue(receiptsDt8.getJzje(), 0.0));

            k3StatementsModelTRY.setFPOSTDATE(receiptsBill.getJzdrq() + " 00:00:00");
            JSONObject F_qqqq_Base = new JSONObject();
            F_qqqq_Base.put("FNUMBER", receiptsDt8.getKmh());
            k3StatementsModelTRY.setF_qqqq_Base(F_qqqq_Base);
            k3StatementsModelTRY.setF_qqqq_Text(receiptsBill.getHkdw());//汇款单位

            k3StatementsModelTRY.setF_qqqq_Text1(receiptsBill.getHkyx());//汇款银行

            keStatementsModelTRYList.add(k3StatementsModelTRY);
        }
        return keStatementsModelTRYList;
    }

    public K3StatementsModel getK3StatementsModel(ReceiptsBill receiptsBill) {
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

        k3StatementsModel.setFREMARK(receiptsBill.getBz());

        JSONObject FSETTLEMAINBOOKID = new JSONObject();
        FSETTLEMAINBOOKID.put("FNUMBER", "PRE001");
        k3StatementsModel.setFSETTLEMAINBOOKID(FSETTLEMAINBOOKID);

        k3StatementsModel.setFISCARRYRATE(false);
        return k3StatementsModel;
    }
}
