package com.weaver.esb.package_20240111031225;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.Util;

import java.util.*;

public class class_20240111031225 {

    /**
     * @param:  param(Map collections)
     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
     */
    public Map execute(Map<String,Object> params) {
        String gsids = Util.null2String(params.get("data"));
        String[] companyList=gsids.split(",");
        JSONArray resarr = new JSONArray();
        ArrayList<String> strings = new ArrayList<>();
        for(String compid:companyList){
            InserthrmList(strings,compid);  // 这个方法应该是将compid加入到strings列表中，但未给出实现，所以假设它的功能。
        }

        int batchSize = 1;  // 定义批量大小为1
        int batchCount = 0;  // 定义批量计数器
        StringBuilder sb = new StringBuilder();  // 用于拼接字符串的StringBuilder对象

        for (int i = 0; i < strings.size(); i++) {
            if (batchCount == batchSize) {  // 当达到批量大小时，创建一个新的JSONObject并放入resarr
                JSONObject jsonObj = new JSONObject();
                String ry="";
                if(sb.length()!=0){
                    ry=sb.substring(0,sb.length()-1).toString();
                }
                jsonObj.put("ry", ry);  // 将拼接的字符串放入JSONObject中
                resarr.add(jsonObj);  // 将JSONObject放入resarr
                batchCount = 0;  // 重置批量计数器
                sb.setLength(0);  // 重置StringBuilder对象
            }
            sb.append(strings.get(i)).append(",");  // 将当前数据拼接到StringBuilder中，并用逗号隔开
            batchCount++;  // 增加批量计数器
        }
        if (batchCount > 0) {  // 如果还有剩余的数据未放入批量中，则创建一个新的JSONObject并放入resarr
            JSONObject jsonObj = new JSONObject();
            String ry="";
            if(sb.length()!=0){
                ry=sb.substring(0,sb.length()-1).toString();
            }
            jsonObj.put("ry", ry);  // 将剩余数据放入JSONObject中
            resarr.add(jsonObj);  // 将JSONObject放入resarr
        }
        Map<String, Object> ret = new HashMap<>();
        ret.put("code", "1");
        ret.put("res", resarr);
        return ret;
    }

    private void InserthrmList(ArrayList<String> list,String compid) {
        RecordSet rs = new RecordSet();
        String sql="select workcode from hrmresource where subcompanyid1=?";
        rs.executeQuery(sql,compid);
        while(rs.next()){
            String workcode=rs.getString("workcode");
            if(!"".equals(workcode)){
                list.add(workcode);
            }
        }
    }

}
