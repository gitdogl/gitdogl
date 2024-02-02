package com.weaver.esb.package_20240110112229;

import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;

import java.text.SimpleDateFormat;
import java.util.*;

public class class_20240110112229 {
String error="";
    /**
     * @param:  param(Map collections)
     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
     */
    public Map execute(Map<String,Object> params) {
        //获取当前年度
        String year=getYear();
        Map<String,Object> ret = new HashMap<>();
        JSONObject data = new JSONObject();

        //AM0102资产拨交流程（2014）formtable_main_215，已归档，formtable_main_215_dt1的总行数
        String gdzcndCount=getTotal("select count(1) zs from formtable_main_215_dt1 where mainid in(select id from " +
                "formtable_main_215 where requestid in(select requestid from workflow_requestbase where " +
                "currentcodetype=3 and TO_CHAR(TO_DATE(createdate, 'YYYY-MM-DD'), 'YYYY')='"+year+"'))");

        data.put("gdzcndCount",gdzcndCount);
        if("".equals(error)){
            ret.put("resultCode","S");
            ret.put("data",data);
        }else{
            ret.put("resultCode","E");
            ret.put("resultMsg","查询失败");
            ret.put("error",error);
        }
        return ret;
    }

    private String getTotal(String sql){
        RecordSet rs = new RecordSet();
        if(rs.executeQuery(sql)&&rs.next()){
            return rs.getString("zs");
        }else{
            error+=sql+"执行失败</br>";
        }
        return "0";
    }

    private String getYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year = sdf.format(new Date());
        return year;
    }
}