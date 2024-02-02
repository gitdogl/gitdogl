package com.weaver.esb.package_20231218034237;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.formmode.virtualform.UUIDPKVFormDataSave;
import weaver.general.Util;

import java.text.SimpleDateFormat;
import java.util.*;

public class class_20231218034237 {
    RecordSet rs = new RecordSet();
    Map<String, Object> ret = new HashMap<>();
    String error = "";
    String formmodeid = "";
    HashMap<String, String> dfmap = new HashMap<>();

    /**
     * @param: param(Map collections)
     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
     */
    public Map execute(Map<String, Object> params) {
        formmodeid = Util.null2String(params.get("formmodeid"));
        JSONArray listarr = getDFjsarr();
        ret.put("listarr",listarr);
        List<Map<String, Object>> res=doInsertPartyAmount(listarr, formmodeid);
        if("".equals(error)){
            ret.put("code","S");
            ret.put("msg","执行完成");
            ret.put("list",res);
        }else{
            ret.put("code","E");
            ret.put("msg",error);
        }
        return ret;
    }

    private JSONArray getDFjsarr() {
        RecordSet rs = new RecordSet();
        JSONArray resarr = new JSONArray();
        String month=getCurrentMonth();
        String sql = "select * from uf_dfjs where SUBSTR(rq,6,2)='"+month+"'";
        if (rs.executeQuery(sql)) {
            while(rs.next()){
                JSONObject resobj = new JSONObject();
                resobj.put("xm",rs.getString("xm"));
                resobj.put("rq",rs.getString("rq"));
                resobj.put("yjje",getPartyAmount(rs.getDouble("dfjs"))+"");
                resobj.put("dzz",rs.getString("dzz"));
                resobj.put("sj",rs.getString("sj"));
                resobj.put("zzwy",rs.getString("zzwy"));
                resarr.add(resobj);
            }
        } else {
            error += "sql:" + sql + "执行失败</br>";
        }
        return resarr;
    }

    private List<Map<String, Object>> doInsertPartyAmount(JSONArray listarr, String formmodeid) {
        if ("".equals(formmodeid)) {
            formmodeid = "48848";
        }
        //获取list
//        ret.put("listarr", listarr);
        List<String> dflist = new ArrayList<>();
        dflist = getDFList();// 获取项目列表原记录
        List<Map<String, Object>> newlist = new ArrayList<>();

        Map<String, Object> newdfmap = new HashMap<>();
        if (listarr != null && listarr.size() > 0) {
            for (int i = 0; i < listarr.size(); i++) {
                JSONObject dfobj = listarr.getJSONObject(i);
                if (dfobj == null) {
                    continue;
                }
                //项目信息
                /**
                 * ∟dfdata	json	否	党费台账
                 */
                String xm = Util.null2String(dfobj.get("xm"));//	姓名
                String yjrq = Util.null2String(dfobj.get("rq"));//	日期
                String yjje = Util.null2String(dfobj.get("yjje"));//	应缴金额
                String dzz = Util.null2String(dfobj.get("dzz"));//	党组织
                String sj = Util.null2String(dfobj.get("sj"));//	书记
                String zzwy = Util.null2String(dfobj.get("zzwy"));//	组织委员
                //不存在 新增
                Object[] prams = new Object[6];
                prams[0] = xm;//姓名
                prams[1] = yjrq;//应缴日期
                prams[2] = yjje;//应缴金额
                prams[3] = dzz;//党组织
                prams[4] = sj;//书记
                prams[5] = zzwy;//组织委员
                newdfmap = new HashMap<>();
                if (dflist.indexOf(xm + yjrq) > -1) {
                    String id = dfmap.get(xm + yjrq);
                    String upsql = "xm=?,yjrq=?,yjje=?,dzz=?,sj=?,zzwy=?";
                    //存在 更新
                    String updatesql = "update uf_dfyjjl set " + upsql + "  where id='" + id + "'";
                    boolean flag = rs.executeUpdate(updatesql, prams);
                    newdfmap.put("sql", updatesql + prams);
                    newdfmap.put("xm", xm);

                    if (!flag) {
                        newdfmap.put("status", "E");
                        newdfmap.put("msg", "更新失败");
                        error += "xm :" + xm + "更新失败";

                    } else {
                        newdfmap.put("status", "S");
                        newdfmap.put("msg", "更新成功");
                    }


                } else {


                    UUIDPKVFormDataSave var64 = new UUIDPKVFormDataSave();
                    String modeuuid = (String) var64.generateID((Map) null);
                    String value = "?,?,?,?,?,?,";
                    String fields = "xm,yjrq,yjje,dzz,sj,zzwy,";
                    String insertsql = "insert into uf_dfyjjl  (" + fields + "formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,modeuuid) values (" + value + " " + formmodeid + ",1,0,'" + this.getCurrentDate() + "','" + this.getCurrentTime() + "','" + modeuuid + "')";
                    boolean flag = rs.executeUpdate(insertsql, prams);
                    newdfmap.put("sql", insertsql + prams);
                    newdfmap.put("xm", xm);
                    if (!flag) {
                        newdfmap.put("status", "E");
                        newdfmap.put("msg", "插入失败");
                        error += "xm :" + xm + "插入失败";

                    } else {
                        newdfmap.put("status", "S");
                        newdfmap.put("msg", "插入成功");
                    }
                    //插入缓存
                    insertsql = "select id from uf_dfyjjl where xm  = ? and yjrq=?";
                    flag = rs.executeQuery(insertsql, xm + yjrq);
                    String id = "";
                    if (rs.next()) {
                        id = Util.null2String(rs.getString("id"));
                    }
                    dfmap.put(Util.null2String(xm), id);

                }
                newlist.add(newdfmap);


            }
        }
        return newlist;
    }

    /**
     * 获取已存在党费信息
     *
     * @return
     */
    private List<String> getDFList() {
        List<String> dflist = new ArrayList<>();
        String sqlqzyj = "select xm ,id,yjrq,yjje  from uf_dfyjjl";
        //获取正文

        rs.executeQuery(sqlqzyj.toString());
        while (rs.next()) {
            dflist.add(Util.null2String(rs.getString("xm")) + Util.null2String(rs.getString("yjrq")));
            dfmap.put(Util.null2String(rs.getString("xm")) + Util.null2String(rs.getString("yjrq")), Util.null2String(rs.getString("id")));
        }
        return dflist;
    }


    //根据基数计算党费
    private double getPartyAmount(double baseAmount) {
        double partyAmount = 0;
        if (baseAmount <= 3000) {
            partyAmount = baseAmount * 0.005;
        } else if (baseAmount > 3000 && baseAmount <= 5000) {
            partyAmount = baseAmount * 0.01;
        } else if (baseAmount > 5000 && baseAmount <= 10000) {
            partyAmount = baseAmount * 0.015;
        } else if (baseAmount > 10000) {
            partyAmount = baseAmount * 0.02;
        } else {
            error += "党费计算失败</br>";
        }
        return partyAmount;
    }

    private String getCurrentDate() {
        SimpleDateFormat var1 = new SimpleDateFormat("yyyy-MM-dd");
        return var1.format(new Date());
    }

    private String getCurrentMonth() {
        SimpleDateFormat var1 = new SimpleDateFormat("MM");
        return var1.format(new Date());
    }

    private String getCurrentTime() {
        SimpleDateFormat var1 = new SimpleDateFormat("HH:mm:ss");
        return var1.format(new Date());
    }
}
