package com.weaver.esb.jjc;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.formmode.page.util.Util;
import weaver.interfaces.jjc.oaclient.cw.dao.BaseDao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

public class FKDLJCL_WFT {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    /**
     * @param:  param(Map collections)
     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
     */
    public Map execute(Map<String,Object> params) {
        String Data = Util.null2String(params.get("Data"));
        JSONObject dataobj=JSONObject.parseObject(Data);
        Map<String,Object> ret = new HashMap<>();
//        主表塞入hashmap
        HashMap<String,String> reimbursementBill = new HashMap<>();

        reimbursementBill.put("Id", dataobj.getString("id"));
        reimbursementBill.put("Requestid", dataobj.getString("requestid"));
        reimbursementBill.put("Dcryxxan", dataobj.getString("dcryxxan"));
        reimbursementBill.put("Mxdr", dataobj.getString("mxdr"));
        reimbursementBill.put("Lcbh", dataobj.getString("lcbh"));
        reimbursementBill.put("Sqrq", dataobj.getString("sqrq"));
        reimbursementBill.put("Bxry", dataobj.getString("bxry"));
        reimbursementBill.put("Bxfs", dataobj.getString("bxfs"));
        reimbursementBill.put("Zxdw", dataobj.getString("zxdw"));
        reimbursementBill.put("Xgfj", dataobj.getString("xgfj"));
        reimbursementBill.put("Skjehjy", dataobj.getString("skjehjy"));
        reimbursementBill.put("Skjehjdx", dataobj.getString("skjehjdx"));
        reimbursementBill.put("Bxyt", dataobj.getString("bxyt"));
        reimbursementBill.put("Xclw", dataobj.getString("xclw"));
        reimbursementBill.put("Ysr", dataobj.getString("ysr"));
        reimbursementBill.put("Ysrgh", dataobj.getString("ysrgh"));
        reimbursementBill.put("Xmfzr", dataobj.getString("xmfzr"));
        reimbursementBill.put("Xmfzrgh", dataobj.getString("xmfzrgh"));
        reimbursementBill.put("Scqtcl", dataobj.getString("scqtcl"));
        reimbursementBill.put("Cxjehjy", dataobj.getString("cxjehjy"));
        reimbursementBill.put("Thjehjy", dataobj.getString("thjehjy"));
        reimbursementBill.put("Bxjehjy", dataobj.getString("bxjehjy"));
        reimbursementBill.put("Xmbh", dataobj.getString("xmbh"));
        reimbursementBill.put("K3fkdid", dataobj.getString("k3fkdid"));
        reimbursementBill.put("Sfxyhq", dataobj.getString("sfxyhq"));
        reimbursementBill.put("Xzhqr", dataobj.getString("xzhqr"));
        reimbursementBill.put("Sfxyxgje", dataobj.getString("sfxyxgje"));
        reimbursementBill.put("Yt", dataobj.getString("yt"));
        reimbursementBill.put("Sfxymsczrhq", dataobj.getString("sfxymsczrhq"));
        reimbursementBill.put("Xzmsczr", dataobj.getString("xzmsczr"));
        reimbursementBill.put("Sfxyhqe", dataobj.getString("sfxyhqe"));
        reimbursementBill.put("Xzhqre", dataobj.getString("xzhqre"));
        reimbursementBill.put("Xmmc", dataobj.getString("xmmc"));
        reimbursementBill.put("Sfndys", dataobj.getString("sfndys"));
        reimbursementBill.put("Ndyse", dataobj.getString("ndyse"));
        reimbursementBill.put("Xmmcwb", dataobj.getString("xmmcwb"));
        reimbursementBill.put("Lsbxje", dataobj.getString("lsbxje"));
        reimbursementBill.put("Ljbxzehbc", dataobj.getString("ljbxzehbc"));
        reimbursementBill.put("Ryfffhj", dataobj.getString("ryfffhj"));
        reimbursementBill.put("Fymxbxhj", dataobj.getString("fymxbxhj"));
        reimbursementBill.put("Jkjehj", dataobj.getString("jkjehj"));
        reimbursementBill.put("Mx2szhj", dataobj.getString("mx2szhj"));
        reimbursementBill.put("Sfrygz", dataobj.getString("sfrygz"));
        reimbursementBill.put("Ycryxx", dataobj.getString("ycryxx"));
        reimbursementBill.put("Ycskmx", dataobj.getString("ycskmx"));
        reimbursementBill.put("Ffhjdx", dataobj.getString("ffhjdx"));
        reimbursementBill.put("Zcjehj", dataobj.getString("zcjehj"));
        reimbursementBill.put("Zcjehjdx", dataobj.getString("zcjehjdx"));
        reimbursementBill.put("Zdylj", dataobj.getString("zdylj"));
        reimbursementBill.put("Bxyt1", dataobj.getString("bxyt1"));
        reimbursementBill.put("Hqlcqqid", dataobj.getString("hqlcqqid"));

//       dt4结算方式塞入list
        JSONArray Dt4=dataobj.getJSONArray("Dt4");
        List<HashMap<String,String>> reimbursementDt4s = new ArrayList<>();
        if(Dt4!=null) {
            Iterator<Object> it4 = Dt4.iterator();
            while (it4.hasNext()) {
                HashMap<String, String> reimbursementDt4 = new HashMap();
                JSONObject Dt4obj = (JSONObject) it4.next();
                reimbursementDt4.put("Jzkm", Dt4obj.getString("jzkm"));
                reimbursementDt4.put("Kmh", Dt4obj.getString("kmh"));
                reimbursementDt4.put("Jzxy", Dt4obj.getString("jzxy"));
                reimbursementDt4.put("Jzbhwb", Dt4obj.getString("jzbhwb"));
                reimbursementDt4.put("Jzdjl", Dt4obj.getString("jzdjl"));
                reimbursementDt4.put("Je", Dt4obj.getString("je"));
                reimbursementDt4.put("Jzbh", Dt4obj.getString("jzbjzbhhwb"));
                reimbursementDt4.put("Zckm", Dt4obj.getString("zckm"));
                reimbursementDt4s.add(reimbursementDt4);
            }
        }

//       dt5校内人员明细塞入list
        JSONArray Dt5=dataobj.getJSONArray("Dt5");
        List<HashMap<String,String>> reimbursementDt5s = new ArrayList<>();
        if(Dt5!=null) {
            Iterator<Object> it5 = Dt5.iterator();
            while (it5.hasNext()) {
                HashMap<String, String> reimbursementDt5 = new HashMap();
                JSONObject Dt5obj = (JSONObject) it5.next();
                reimbursementDt5.put("Skr", Dt5obj.getString("xm"));
                reimbursementDt5.put("Khx", Dt5obj.getString("yxjkhx"));
                reimbursementDt5.put("Skjey", Dt5obj.getString("shje"));
                reimbursementDt5.put("Se", Dt5obj.getString("ks"));
                reimbursementDt5.put("Skzh", Dt5obj.getString("yxkh"));
                reimbursementDt5.put("Lhh", Dt5obj.getString("lxh"));
                reimbursementDt5s.add(reimbursementDt5);
            }
        }

//       dt7校外人员明细塞入list
        JSONArray Dt7=dataobj.getJSONArray("Dt7");
        if(Dt7!=null) {
            Iterator<Object> it7 = Dt7.iterator();
            while (it7.hasNext()) {
                HashMap<String, String> reimbursementDt7 = new HashMap();
                JSONObject Dt7obj = (JSONObject) it7.next();
                reimbursementDt7.put("Skr", Dt7obj.getString("xm"));
                reimbursementDt7.put("Khx", Dt7obj.getString("khx"));
                reimbursementDt7.put("Skjey", Dt7obj.getString("shje"));
                reimbursementDt7.put("Se", Dt7obj.getString("ks"));
                reimbursementDt7.put("Skzh", Dt7obj.getString("yxkh"));
                reimbursementDt7.put("Lhh", Dt7obj.getString("lxh"));
                reimbursementDt5s.add(reimbursementDt7);
            }
        }

//     明细2，4 金额分摊
        ret.put("reimbursementDt4s",reimbursementDt4s);
        List<HashMap<String,String>> oaPaymentNoteDto2s = getOAPaymentNoteDto2(reimbursementDt4s, reimbursementDt5s);
        ret.put("oaPaymentNoteDto2s",oaPaymentNoteDto2s);
//        组合成金蝶报文
        List<HashMap<String,String>> k3PaymentNoteModelTRIES = getK3PaymentNoteModelTRYList(oaPaymentNoteDto2s);
        Map<String,Object> k3PaymentNoteModel = getK3PaymentNoteModel(reimbursementBill);
        k3PaymentNoteModel.put("FPAYBILLENTRY",k3PaymentNoteModelTRIES);
        ret.put("FPAYBILLENTRY",k3PaymentNoteModelTRIES);
        HashMap<String,Object> k3PaymentNoteBill = new HashMap();
        k3PaymentNoteBill.put("Model",k3PaymentNoteModel);

        String k3PaymentNoteBillobj=JSONObject.toJSON(k3PaymentNoteBill).toString();



        ret.put("code","1");
        ret.put("k3PaymentNoteBillobj",k3PaymentNoteBillobj);
        return ret;

    }


    private List<HashMap<String,String>> getOAPaymentNoteDto2(List<HashMap<String,String>> reimbursementDt4s, List<HashMap<String,String>> reimbursementDt2s) {
        List<HashMap<String,String>> oaPaymentNoteDto2s = new ArrayList<>();
        for (int i = 0; i < reimbursementDt2s.size(); i++) {
            double shje = weaver.general.Util.getDoubleValue(reimbursementDt2s.get(i).get("Skjey"), 0);//获取税后金额
            double se = weaver.general.Util.getDoubleValue(reimbursementDt2s.get(i).get("Se"), 0);//获取税额
            double xyje = shje + se;//总额
//            for (HashMap<String,String> reimbursementDt4 : reimbursementDt4s) {
//                double yssyje = Double.parseDouble(reimbursementDt4.get("Je"));
//                if (yssyje <= 0) continue;
//                //如果明细4的支出金额>=明细2某一行的金额，则在明细4扣除明细2的金额，并重新给明细4金额赋值，向金蝶传输明细2本次付款总额和税后金额
//                if (yssyje >= xyje) {
//                    System.out.println("进入税额获取");
//                    System.out.println("税额是" + se);
//                    System.out.println("未税是" + shje);
//                    System.out.println("总额是" + xyje);
//                    HashMap<String,String> oaPaymentNoteDto2 = new HashMap();
//                    oaPaymentNoteDto2.put("Je",xyje + "");
//                    oaPaymentNoteDto2.put("Wsje",shje + "");
//                    oaPaymentNoteDto2.put("Khx", reimbursementDt2s.get(i).get("Khx"));
//                    oaPaymentNoteDto2.put("Skr", reimbursementDt2s.get(i).get("Skr"));
//                    oaPaymentNoteDto2.put("Zckm", reimbursementDt4.get("Zckm"));
//                    oaPaymentNoteDto2.put("Skzh", reimbursementDt2s.get(i).get("Skzh"));
//                    oaPaymentNoteDto2.put("Se", reimbursementDt2s.get(i).get("Se"));
//                    oaPaymentNoteDto2.put("Lhh", reimbursementDt2s.get(i).get("Lhh"));
//                    oaPaymentNoteDto2s.add(oaPaymentNoteDto2);
//                    reimbursementDt4.put("Je",sub(yssyje, xyje)+"");
//                    break;
//                    //如果明细4金额<明细2的金额，就在明细二人员金额里面扣减明细4所有金额，然后给金蝶传输总金额和税后金额（此时是明细4金额总额和明细4金额乘税额在明细2中占的百分比）
//                } else {
//                    System.out.println("未进入税额获取");
            // xyje = sub(xyje, yssyje);
//                    double se_percent=se/xyje;
//                    double shje_percent=shje/xyje;
//                    se=format2double(sub(se,yssyje*se_percent));
//                    shje=format2double(sub(shje,yssyje*shje_percent));
//                    xyje=se+shje;
//                    yssyje=format2double(yssyje*shje_percent)+format2double(yssyje*se_percent);
//                    reimbursementDt4.put("Je","0");
            HashMap<String,String> oaPaymentNoteDto2 = new HashMap();
//                    oaPaymentNoteDto2.put("Wsje", String.valueOf(format2double(yssyje * shje_percent)));
//                    oaPaymentNoteDto2.put("Je", String.valueOf(yssyje));
            oaPaymentNoteDto2.put("Je",xyje + "");
            oaPaymentNoteDto2.put("Wsje",shje + "");
            oaPaymentNoteDto2.put("Khx", reimbursementDt2s.get(i).get("Khx"));
            oaPaymentNoteDto2.put("Skr", reimbursementDt2s.get(i).get("Skr"));
            if(reimbursementDt4s == null || reimbursementDt4s.isEmpty()) {
                oaPaymentNoteDto2.put("Zckm","1122");
            }else{
                oaPaymentNoteDto2.put("Zckm", reimbursementDt4s.get(0).get("Zckm"));
            }
            oaPaymentNoteDto2.put("Skzh", reimbursementDt2s.get(i).get("Skzh"));
            oaPaymentNoteDto2.put("Se", se+"");
            oaPaymentNoteDto2.put("Lhh", reimbursementDt2s.get(i).get("Lhh"));
            oaPaymentNoteDto2s.add(oaPaymentNoteDto2);
//                    continue;
        }
        return oaPaymentNoteDto2s;
    }

    private List<HashMap<String,String>> getK3PaymentNoteModelTRYList(List<HashMap<String,String>> oaPaymentNoteDto2List) {
        List<HashMap<String, String>> k3PaymentNoteModelTRIES = new ArrayList<>();

        for (HashMap<String, String> oaPaymentNoteDto2 : oaPaymentNoteDto2List) {
            if (weaver.general.Util.getDoubleValue(oaPaymentNoteDto2.get("Je"), 0.0) <= 0) {

                    HashMap<String, String> k3PaymentNoteModelTRY = new HashMap<>();
                    JSONObject FSETTLETYPEID = new JSONObject();
                    FSETTLETYPEID.put("FNumber", "JSFS10_SYS");
                    k3PaymentNoteModelTRY.put("FSETTLETYPEID", FSETTLETYPEID.toString());

                    JSONObject FPURPOSEID = new JSONObject();
                    FPURPOSEID.put("FNumber", "SFKYT10_SYS");
                    k3PaymentNoteModelTRY.put("FPURPOSEID", FPURPOSEID.toString());
                    k3PaymentNoteModelTRIES.add(k3PaymentNoteModelTRY);

                continue;
            }

            HashMap<String, String> k3PaymentNoteModelTRY = new HashMap<>();

            JSONObject FSETTLETYPEID = new JSONObject();
            FSETTLETYPEID.put("FNumber", "JSFS04_SYS");
            k3PaymentNoteModelTRY.put("FSETTLETYPEID", FSETTLETYPEID.toString());

            JSONObject FPURPOSEID = new JSONObject();
            FPURPOSEID.put("FNumber", "SFKYT10_SYS");
            k3PaymentNoteModelTRY.put("FPURPOSEID", FPURPOSEID.toString());

            k3PaymentNoteModelTRY.put("FPAYTOTALAMOUNTFOR", String.valueOf(weaver.general.Util.getDoubleValue(oaPaymentNoteDto2.get("Wsje"), 0.0)));
            k3PaymentNoteModelTRY.put("FPAYAMOUNTFOR_E", String.valueOf(weaver.general.Util.getDoubleValue(oaPaymentNoteDto2.get("Je"), 0.0)));
            k3PaymentNoteModelTRY.put("FSETTLEDISTAMOUNTFOR", "0.0");
            k3PaymentNoteModelTRY.put("FSETTLEPAYAMOUNTFOR", String.valueOf(weaver.general.Util.getDoubleValue(oaPaymentNoteDto2.get("Wsje"), 0.0)));
            k3PaymentNoteModelTRY.put("FREALPAYAMOUNTFOR_D", String.valueOf(weaver.general.Util.getDoubleValue(oaPaymentNoteDto2.get("Wsje"), 0.0)));

            JSONObject FACCOUNTID = new JSONObject();
            FACCOUNTID.put("FNumber", "182703469395");
            k3PaymentNoteModelTRY.put("FACCOUNTID", FACCOUNTID.toString());

            k3PaymentNoteModelTRY.put("FOPPOSITEBANKACCOUNT", oaPaymentNoteDto2.get("Skzh"));
            k3PaymentNoteModelTRY.put("FOPPOSITECCOUNTNAME", oaPaymentNoteDto2.get("Skr"));
            k3PaymentNoteModelTRY.put("FOPPOSITEBANKNAME", oaPaymentNoteDto2.get("Khx"));
            k3PaymentNoteModelTRY.put("FCOMMENT", "");
            k3PaymentNoteModelTRY.put("FRecType", "1");
            k3PaymentNoteModelTRY.put("FCNAPS", oaPaymentNoteDto2.get("Lhh"));
            k3PaymentNoteModelTRY.put("FPAYAMOUNT_E", String.valueOf(weaver.general.Util.getDoubleValue(oaPaymentNoteDto2.get("Je"), 0.0)));
            k3PaymentNoteModelTRY.put("FPOSTDATE", df.format(new Date()) + " 00:00:00");
            k3PaymentNoteModelTRY.put("FRuZhangType", "1");
            k3PaymentNoteModelTRY.put("FPayType", "A");
            k3PaymentNoteModelTRY.put("FTaxAmt", String.valueOf(weaver.general.Util.getDoubleValue(oaPaymentNoteDto2.get("Se"), 0.0)));

            JSONObject FBankDetail = new JSONObject();
            FBankDetail.put("FNumber", oaPaymentNoteDto2.get("Khx"));
            k3PaymentNoteModelTRY.put("FBankDetail", FBankDetail.toString());

            JSONObject F_qqqq_Base = new JSONObject();
            F_qqqq_Base.put("FNUMBER", oaPaymentNoteDto2.get("Zckm"));
            k3PaymentNoteModelTRY.put("F_qqqq_Base", F_qqqq_Base.toString());

            k3PaymentNoteModelTRY.put("F_qqqq_Text", oaPaymentNoteDto2.get("Skr"));
            k3PaymentNoteModelTRY.put("F_qqqq_Text1", oaPaymentNoteDto2.get("Skzh"));
            k3PaymentNoteModelTRY.put("F_qqqq_Text2", oaPaymentNoteDto2.get("Khx"));

            k3PaymentNoteModelTRIES.add(k3PaymentNoteModelTRY);
        }

        return k3PaymentNoteModelTRIES;
    }



    private HashMap<String,Object> getK3PaymentNoteModel(HashMap<String,String> reimbursementBill) {
        HashMap<String, Object> k3PaymentNoteModel = new HashMap<>();

        String sqrGh = BaseDao.getWorkCode(weaver.general.Util.getIntValue(reimbursementBill.get("bxry"), 0));
        String sqrname = BaseDao.getLastName(weaver.general.Util.getIntValue(reimbursementBill.get("bxry"), 0));
        String bmName = BaseDao.getDepartmentName(weaver.general.Util.getIntValue(reimbursementBill.get("bxry"), 0));
        String lcbt = BaseDao.getRequestname(reimbursementBill.get("requestid"));
        k3PaymentNoteModel.put("FID", "0");

        JSONObject FBillTypeID = new JSONObject();
        FBillTypeID.put("FNumber", "FKDLX04_SYS");
        k3PaymentNoteModel.put("FBillTypeID", FBillTypeID.toString());
        k3PaymentNoteModel.put("FDATE", df.format(new Date()) + " 00:00:00");
        k3PaymentNoteModel.put("FCONTACTUNITTYPE", "BD_Empinfo");

        JSONObject FCONTACTUNIT = new JSONObject();
        FCONTACTUNIT.put("FNumber", sqrGh);
        k3PaymentNoteModel.put("FCONTACTUNIT", FCONTACTUNIT.toString());

        k3PaymentNoteModel.put("FRECTUNITTYPE", "BD_Empinfo");

        JSONObject FRECTUNIT = new JSONObject();
        FRECTUNIT.put("FNumber", sqrGh);
        k3PaymentNoteModel.put("FRECTUNIT", FRECTUNIT.toString());

        k3PaymentNoteModel.put("F_qqqq_Text7", sqrname);
        k3PaymentNoteModel.put("FISINIT", "false");

        JSONObject FCURRENCYID = new JSONObject();
        FCURRENCYID.put("FNumber", "PRE001");
        k3PaymentNoteModel.put("FCURRENCYID", FCURRENCYID.toString());

        k3PaymentNoteModel.put("FEXCHANGERATE", "1.0");
        k3PaymentNoteModel.put("FSETTLERATE", "1.0");

        JSONObject FSETTLEORGID = new JSONObject();
        FSETTLEORGID.put("FNumber", "100");
        k3PaymentNoteModel.put("FSETTLEORGID", FSETTLEORGID.toString());

        JSONObject FPURCHASEORGID = new JSONObject();
        FPURCHASEORGID.put("FNumber", "100");
        k3PaymentNoteModel.put("FPURCHASEORGID", FPURCHASEORGID.toString());

        k3PaymentNoteModel.put("FDOCUMENTSTATUS", "Z");
        k3PaymentNoteModel.put("FBUSINESSTYPE", "3");
        k3PaymentNoteModel.put("FCancelStatus", "A");

        JSONObject FPAYORGID = new JSONObject();
        FPAYORGID.put("FNumber", "100");
        k3PaymentNoteModel.put("FPAYORGID", FPAYORGID.toString());

        k3PaymentNoteModel.put("FISSAMEORG", "true");
        k3PaymentNoteModel.put("FIsCredit", "false");

        JSONObject FSETTLECUR = new JSONObject();
        FSETTLECUR.put("FNUMBER", "PRE001");
        k3PaymentNoteModel.put("FSETTLECUR", FSETTLECUR.toString());

        k3PaymentNoteModel.put("FIsWriteOff", "false");
        k3PaymentNoteModel.put("FREALPAY", "false");
        k3PaymentNoteModel.put("FREMARK", reimbursementBill.get("bxyt"));
        k3PaymentNoteModel.put("FBookingDate", df.format(new Date()) + " 00:00:00");
        k3PaymentNoteModel.put("FISCARRYRATE", "false");

        JSONObject FSETTLEMAINBOOKID = new JSONObject();
        FSETTLEMAINBOOKID.put("FNumber", "PRE001");
        k3PaymentNoteModel.put("FSETTLEMAINBOOKID", FSETTLEMAINBOOKID.toString());

        k3PaymentNoteModel.put("F_qqqq_Text3", reimbursementBill.get("lcbh"));
        k3PaymentNoteModel.put("F_qqqq_Text5", reimbursementBill.get("xmbh"));
        k3PaymentNoteModel.put("F_qqqq_Text6", bmName);
        k3PaymentNoteModel.put("F_qqqq_Text4", lcbt);

        return k3PaymentNoteModel;
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


}
