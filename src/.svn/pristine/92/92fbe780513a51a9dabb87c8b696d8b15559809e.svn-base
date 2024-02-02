package com.weaver.esb.package_20240129031557;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.doc.center.cmd.dbsearch.RecommendedReadDocsCmd;
import weaver.conn.RecordSet;
import weaver.formmode.virtualform.UUIDPKVFormDataSave;
import weaver.general.Util;

import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @BelongsProject: WEAVER
 * @BelongsPackage: com.weaver.esb
 * @Author: luotianchen
 * @CreateTime: 2024-01-29  15:17
 * @Description: TODO
 * @Version: 1.0
 */

public class class_20240129031557 {
    public Map execute(Map<String,Object> params) {
        String data = Util.null2String(params.get("data"));
        String workcodes = Util.null2String(params.get("workcodes"));
        String year = Util.null2String(params.get("year"));
        String month = Util.null2String(params.get("month"));
        String tablename = Util.null2String(params.get("tablename"));
        String formmodeid = Util.null2String(params.get("formmodeid"));
        Map<String, Object> ret = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(data);

        String[] workcode=workcodes.split(",");
        for(String wc:workcode){
            String id=getryid(wc);
            insOrUpdate(tablename,formmodeid,id,wc,year,month,"","");
        }
        JSONObject ctrlobj = jsonObject.getJSONObject("CTRL");
        String MSGTY = ctrlobj.getString("MSGTY");
        if("S".equals(MSGTY)){
            JSONObject DATA = jsonObject.getJSONObject("DATA");
            //工资明细
            JSONArray oT_SALARY = DATA.getJSONArray("OT_SALARY");
            if(oT_SALARY!=null){
                if(oT_SALARY.size()>0){
                    for (int i = 0; i < oT_SALARY.size(); i++) {
                        JSONObject obj1 = oT_SALARY.getJSONObject(i);
                        String PERNR=obj1.getString("PERNR");
                        String id=getryid(PERNR);
                        String LGART=obj1.getString("LGART");
                        String LGART_VALUE=obj1.getString("LGART_VALUE");
                        insOrUpdate(tablename,formmodeid,id,PERNR,year,month,LGART,LGART_VALUE);
                    }
                }
            }
        }
        ret.put("JsonObj", jsonObject);
        ret.put("code", "S");
        return ret;
    }
    private String getryid(String wc) {
        RecordSet rs = new RecordSet();
        rs.executeQuery("select id  from hrmresource where workcode=?",wc);
        if(rs.next()) {
            return rs.getString("id");
        }
        return "";
    }

    private boolean insOrUpdate(String tablename,String formmodeid,String id, String wc, String year, String month,
                                String gzItem,String gzAmount) {
        RecordSet rs = new RecordSet();
        RecordSet rs1 = new RecordSet();
        rs.executeQuery("select 1 from "+tablename+" where xm=? and rq=?",id,year+"-"+month+"-"+"15");
        if(rs.next()){
            rs1.executeQuery("select gzbzdm from uf_gzitem where gzitem=?",gzItem);
            rs1.next();
            String gzbzdm=rs1.getString("gzbzdm");
            String updatesql="update "+tablename+" set "+gzbzdm+"='"+gzAmount+"' where xm=? and rq=?";
            if(id!=""&& id!=null ){
                return rs1.executeUpdate(updatesql,id,year+"-"+month+"-"+"15");
            }else{
                System.out.println("工号"+wc+"没有对应的id");
                return true;
            }
        }else{
            UUIDPKVFormDataSave var64 = new UUIDPKVFormDataSave();
            String modeuuid = (String) var64.generateID((Map) null);
            String sql = "insert into "+tablename+"(xm,rq,formmodeid,modedatacreater,modedatacreatertype," +
                    "modedatacreatedate,modedatacreatetime,modeuuid) values(?,?, "+formmodeid + ",1,0,'" + this.getCurrentDate() + "','" + this.getCurrentTime() + "','" + modeuuid + "')";
            Object[] values = new Object[2];
            values[0]=id;
            values[1]=year+"-"+month+"-"+"15";
            if(id!=""&& id!=null ){
                return rs1.executeUpdate(sql, values);
            }else{
                System.out.println("工号"+wc+"没有对应的id");
                return true;
            }
        }
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
