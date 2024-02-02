package com.weaver.esb.package_20240131091919;

import weaver.conn.RecordSet;
import weaver.general.Util;

import java.util.*;

public class class_20240131091919 {

    /**
     * @param:  param(Map collections)
     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
     */
    public Map execute(Map<String,Object> params) {
        String requestid = Util.null2String(params.get("requestid"));
        RecordSet rs = new RecordSet();
        Map<String,String> ret = new HashMap<>();
        rs.executeQuery("select 1 from workflow_requestbase where currentnodetype=3 and requestid=?",requestid);
        if(rs.next()){
            ret.put("code","S");
        }else{
            ret.put("code","E");
            ret.put("msg","流程未归档");
        }
         return ret;
    }
}