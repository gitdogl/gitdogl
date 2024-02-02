//package com.weaver.esb.package_20230801025528;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import weaver.conn.RecordSet;
//import weaver.general.Util;
//
//import java.util.*;
//
//public class class_20230801025528 {
//    RecordSet rs = new RecordSet();
//    RecordSet RS = new RecordSet();
//
//    /**
//     * @param: param(Map collections)
//     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
//     */
//    public Map execute(Map<String, Object> params) {
//        JSONArray lcbhs = new JSONArray();
//        Map<String, Object> ret = new HashMap<>();
//        rs.executeQuery(" SELECT lcbh " +
//                " FROM uf_cctz " +
//                " WHERE NOT EXISTS ( " +
//                "  SELECT mainid " +
//                "  FROM uf_cctz_dt4 " +
//                "  WHERE uf_cctz.id = uf_cctz_dt4.mainid " +
//                " )and  " +
//                " NOT EXISTS ( " +
//                "  SELECT mainid " +
//                "  FROM uf_cctz_dt5 " +
//                "  WHERE uf_cctz.id = uf_cctz_dt5.mainid " +
//                " ) and  " +
//                " NOT EXISTS ( " +
//                "  SELECT mainid " +
//                "  FROM uf_cctz_dt6 " +
//                "  WHERE uf_cctz.id = uf_cctz_dt6.mainid " +
//                " ) and  " +
//                " NOT EXISTS ( " +
//                "  SELECT mainid " +
//                "  FROM uf_cctz_dt7 " +
//                "  WHERE uf_cctz.id = uf_cctz_dt7.mainid " +
//                " )");
//        while (rs.next()) {
//            String lcbh = rs.getString("lcbh");
//            String tablename=getBillTableNameByrequestid(lcbh);
//
//            RS.executeQuery("select cldh from "+tablename+"_dt2 where mainid in(select id from "+tablename+" where lcbh=?)",lcbh);
//            while (RS.next()){
//                String cldh=RS.getString("cldh");
//                ret.put("tablename", tablename);
//                ret.put("cldh", cldh);
//                JSONObject lcbhobj=new JSONObject();;
//                if(!"".equals(cldh)) {
//                    lcbhobj.put("cldh", cldh);
//                    lcbhobj.put("lcbh", lcbh);
//                }
//                lcbhs.add(lcbhobj);
//            }
//        }
//
//        ret.put("lcbhs", lcbhs);
//        ret.put("code", "S");
//        return ret;
//
//    }
//
//    public String getBillTableNameByrequestid(String lcbh) {
//        String tablename = "";
//        if (!"".equals(lcbh)) {
//            String select_data = "select tablename from workflow_bill where id in (select formid from workflow_base where id  in (select workflowid from workflow_requestbase where requestmark = ?))";
//            if (RS.executeQuery(select_data, lcbh) && RS.next()) {
//                tablename = Util.null2String(RS.getString(1));
//            }
//        }
//        return tablename;
//    }
//}
