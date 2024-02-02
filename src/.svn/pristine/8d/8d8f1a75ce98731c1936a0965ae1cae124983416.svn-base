package com.weaver.esb.package_20231113115059;

import com.alibaba.fastjson.JSONObject;
import weaver.general.Util;

import java.util.*;

public class class_20231113115059 {

    /**
     * @param:  param(Map collections)
     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
     */
    public Map execute(Map<String,Object> params) {
        //请求报文
        String dataStr = Util.null2String(params.get("data"));
        JSONObject dataObj =JSONObject.parseObject(dataStr);
        String ZYWLX= dataObj.getString("ZYWLX");
        ZYWLX="2";
        dataObj.remove("ZYWLX");
        dataObj.put("ZYWLX",ZYWLX);



        //响应报文
        Map<String,Object> ret = new HashMap<>();
        ret.put("code","1");
        ret.put("msg","成功");
        ret.put("res",dataObj);
        return ret;

    }
}
