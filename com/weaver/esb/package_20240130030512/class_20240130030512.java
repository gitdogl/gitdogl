package com.weaver.esb.package_20240130030512;

import java.time.LocalDate;
import java.util.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.common.protocol.types.Field;


public class class_20240130030512 {


    /**
     *  接口请求数据（可获取接口不同类别的数据）
     *  优先级：url>request>header (request参数中可以获取到 URL 类别的参数，获取不到 header 类别的参数)
     *  contextParams.get("url") ：URL地址参数
     *  contextParams.get("request") ：表单参数
     *  contextParams.get("header") ：Header参数
     *
     */
    private Map<String,Map<String,String>> contextParams = new HashMap<>();

    /**
     * 上下文数据（可获取转换规则映射位置的其它同级参数）
     * allParams.get("a")：获取同级别名为a的参数值
     */
    private Map<String,String> allParams = new HashMap<>();

    /**
     * @param:  param Map collections
     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
     */
    public String execute(Map<String,String> params) {
        String data = params.get("data");
        String[] arr=data.split(",");
        String jsonstr="{\"CTRL\":{\"SYSID\":\"BPM\",\"REVID\":\"HSP\",\"FUNID\":\"ZINF0042\",\"INFID\":\"2CEA7FEB90CC1EDEA7E02B0F9B344DBA\",\"KEYID\":\"\",\"UNAME\":\"BPM_DJ01\",\"DATUM\":\"00000000\",\"UZEIT\":\"000000\",\"ENDAT\":\"00000000\",\"ENUZE\":\"000000\",\"TIMESTAMP\":0,\"MSGTY\":\"\",\"MSAGE\":\"\"},\"DATA\":{\"IT_PERNR\":[],\"PNPPABRJ\":\"\",\"PNPPABRP\":\"\",\"USERNAME\":\"BPM_DJ01\"}}";
        JSONObject parseObject=JSONObject.parseObject(jsonstr);
        JSONObject data1=parseObject.getJSONObject("DATA");
        JSONArray PERNR = new JSONArray();
        LocalDate currentDate = LocalDate.now();
        LocalDate lastMonthDate = currentDate.minusMonths(1);

        String lastMonth = String.valueOf(lastMonthDate.getMonthValue());
        String lastYear = String.valueOf(lastMonthDate.getYear());
        for(String str:arr){
            PERNR.add(str);
        }
        data1.put("IT_PERNR",PERNR);
        data1.put("PNPPABRJ",lastMonth);
        data1.put("PNPPABRP",lastYear);
        parseObject.put("DATA",data1);
        return JSONObject.toJSONString(parseObject);
    }
}
