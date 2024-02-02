//package com.weaver.esb.package_20230720043213;
//
//import com.api.formmode.page.util.Util;
//import weaver.conn.RecordSet;
//import weaver.conn.RecordSetDataSource;
//
//import java.util.*;
//
//public class class_20230720043213 {
//
//    /**
//     * @param:  param(Map collections)
//     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
//     */
//    public Map execute(Map<String,Object> params) {
//        String requestid= Util.null2String(params.get("requestid"));
//        String tablename= Util.null2String(params.get("tablename"));
//        RecordSetDataSource RS = new RecordSetDataSource("Archives");
//        String qzh="";
//        String fonds_code="";
//        String fonds_name="";
//        RecordSet rs = new RecordSet();
//        Map<String,String> ret = new HashMap<>();
//        rs.executeQuery("select qzh from "+tablename+" where requestid=?",requestid);
//        if(rs.next()){
//            qzh=rs.getString("qzh") ;
//        }
//        RS.execute("select fonds_code,fonds_name from uf_arc_fonds where id='"+qzh+"'");
//        if(RS.next()){
//            fonds_code=RS.getString("fonds_code");
//            fonds_name=RS.getString("fonds_name");
//        }
//        boolean flag1=rs.executeUpdate("update "+tablename+" set qzbh=?",fonds_code);
//        boolean flag2=rs.executeUpdate("update "+tablename+" set qzmc=?",fonds_name);
//        if(flag1&&flag2){
//            ret.put("code","1");
//        }else{
//            ret.put("code","0");
//        }
//
//        return ret;
//
//    }
//}
