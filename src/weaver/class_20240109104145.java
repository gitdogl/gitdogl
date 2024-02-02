package com.weaver.esb.package_20240109104145;

import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;

import java.text.SimpleDateFormat;
import java.util.*;

public class class_20240109104145 {
    String error="";
    /**
     * @param:  param(Map collections)
     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
     */
    public Map execute(Map<String,Object> params) {
        JSONObject data = new JSONObject();
        //获取当前年度
        String year=getYear();
        //固定资产总数--取固定资产非报废总数
        //固定资产为信息类资产档案uf_gdzcda中资产类型不等于4的
        //非报废：资产状态zczt不等于2的资产
        //固定资产一条数据对应一个，取资产id的个数即可
        String gdzcCount=getTotal("select count(1) zs from uf_gdzcda where gdzclx!='4' and zczt!='2'");

        //年度采购数量--入库登记次数
        //信息类资产档案uf_gdzcda中资产类型不等于4的，创建日期是本年的
        String ndcgCount=getTotal("select count(1) zs from uf_gdzcda where gdzclx!='4' and TO_CHAR(TO_DATE" +
                "(modedatacreatedate, 'YYYY-MM-DD'), 'YYYY')='"+year+"'");

        //年度处置量--AM0077资产处置通知流程数量，非无形资产
        //固定资产报废通知单formtable_main_120，是否为无形资产
        //sfwwxzc=1，本年已归档的流程数量
        String ndczCount=getTotal("select count(1) zs from formtable_main_120 where sfwwxzc=1 and requestid in" +
                "(select requestid from workflow_requestbase where currentnodetype=3 and TO_CHAR(TO_DATE(createdate, 'YYYY-MM-DD'), 'YYYY')" +
                "='"+year+"')");

        //固定资产维修记录uf_gdzcwxjl，本年总次数，一条数据对应一次
        String gdzcwxCount=getTotal("select count(1) zs from uf_gdzcwxjl where TO_CHAR(TO_DATE(SQRQ, 'YYYY-MM-DD'), 'YYYY') = '"+year+"'");

        //总数--无形资产总数
        //无形资产为信息类资产档案uf_gdzcda中资产类型等于4的，取资产id的个数
        String wxzcCount=getTotal("select count(1) zs from uf_gdzcda where gdzclx=4");

        //本年新增数量--无形资产台账创建日期是本年的
        //信息类资产档案uf_gdzcda中资产类型等于4的，创建日期是本年的
        String zcxzCount=getTotal("select count(1) zs from uf_gdzcda where gdzclx=4 and TO_CHAR(TO_DATE" +
                "(modedatacreatedate, 'YYYY-MM-DD'), 'YYYY')='"+year+"'");

        //年度处置量--AM0077资产处置通知流程数量，无形资产
        //固定资产报废通知单formtable_main_120，是否为无形资产
        //sfwwxzc=0，本年已归档的流程数量
        String wxndczCount=getTotal("select count(1) zs from formtable_main_120 where sfwwxzc=0 and requestid in" +
                "(select requestid from workflow_requestbase where currentnodetype=3 and TO_CHAR(TO_DATE(createdate, 'YYYY-MM-DD'), 'YYYY')" +
                "='"+year+"')");

        //库存总数
        //低值易耗品实时库存表中在库数量ZKSL
        String kcCount=getTotal("SELECT" +
                " sum(nvl ( b.rksl, 0 )- nvl ( c.lysl, 0 )) zksl " +
                " FROM" +
                " uf_XXLDZ01 a" +
                " LEFT JOIN ( SELECT sum( rksl ) rksl, pl FROM uf_dzyhrk GROUP BY pl ) b ON a.id = b.pl" +
                " LEFT JOIN ( SELECT sum( sjlysl ) lysl, pl FROM uf_dzyhlyjl GROUP BY pl ) c ON a.id = c.pl");

        //本年领用总数
        //低值易耗领用记录uf_dzyhlyjl，申请日期sqrq是本年的，实际领用数量sjlysl之和
        String bnlyCount=getTotal("select sum(sjlysl) from uf_dzyhlyjl where TO_CHAR(TO_DATE(sqrq, 'YYYY-MM-DD'), 'YYYY')='"+year+"'");


        Map<String,Object> ret = new HashMap<>();
        data.put("gdzcCount",gdzcCount);
        data.put("ndcgCount",ndcgCount);
        data.put("ndczCount",ndczCount);
        data.put("gdzcwxCount",gdzcwxCount);
        data.put("wxzcCount",wxzcCount);
        data.put("zcxzCount",zcxzCount);
        data.put("wxndczCount",wxndczCount);
        data.put("kcCount",kcCount);
        data.put("bnlyCount",bnlyCount);
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

    private String getYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year = sdf.format(new Date());
        return year;
    }

    private String getTotal(String sql){
        RecordSet rs = new RecordSet();
        if(rs.executeQuery(sql)&&rs.next()){
            return "".equals(rs.getString("zs"))?"0":rs.getString("zs");
        }else{
            error+=sql+"执行失败</br>";
        }
        return "0";
    }

}
