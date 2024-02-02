//package com.weaver.esb.package_20240111045358;
//
//import weaver.conn.RecordSet;
//import weaver.general.Util;
//
//import java.util.*;
//
//public class class_20240111045358 {
//
//    /**
//     * @param:  param(Map collections)
//     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
//     */
//    public Map execute(Map<String,Object> params) {
//        String id = Util.null2String(params.get("id"));
//        String param = Util.null2String(params.get("param"));
//        Map<String,String> ret = new HashMap<>();
//        String ryids=getrystr(param);
//        String[] ryidarr=ryids.split(",");
//        if(checkRight(ryidarr,id)){
//            ret.put("code","S");
//        }else{
//            ret.put("code","E");
//            ret.put("msg","无权限访问");
//        }
//        return ret;
//    }
//
//    private String getrystr(String param) {
//        RecordSet rs = new RecordSet();
//        String qxstr="";
//        rs.executeQuery("select qx from uf_pageConfig where param=?",param);
//        if(rs.next()){
//            qxstr=rs.getString("qx");
//        }
//        return qxstr;
//    }
//
//    private boolean checkRight(String[] ryidarr,String id) {
//        return Arrays.asList(ryidarr).contains(id)||Arrays.asList(ryidarr).contains("1");
//    }
//}
