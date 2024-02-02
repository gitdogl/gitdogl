//package com.weaver.esb.package_20230912101308;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.tencentcloudapi.cvm.v20170312.models.StartInstancesRequest;
//import weaver.conn.RecordSet;
//import weaver.general.Util;
//
//import java.util.*;
//
//public class class_20230912101308 {
//
//    /**
//     * @param:  param(Map collections)
//     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
//     */
//    public Map execute(Map<String,Object> params) {
//         String mx3 = Util.null2String(params.get("mx3"));
//        JSONArray mx3arr = JSONArray.parseArray(mx3);
//        HashSet<String> skrs = new HashSet<>();
//        Map<String,String> ret = new HashMap<>();
//        String error="";
//        for(int i=0;i< mx3arr.size();i++){
//          JSONObject mx3obj=mx3arr.getJSONObject(i);
//          String REVNAM=mx3obj.getString("REVNAM");
//          if(skrs.contains(REVNAM)){
//                  error+="收款人名称"+REVNAM+"重复！</br>";
//          }else{
//              skrs.add(REVNAM);
//          }
//        }
//        if("".equals(error)){
//            ret.put("code","1");
//        }else{
//            ret.put("code","0");
//            ret.put("msg",error);
//        }
//
//
//         return ret;
//
//    }
//}