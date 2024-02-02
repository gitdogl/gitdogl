package com.weaver.esb.package_20231204111117;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import microsoft.exchange.webservices.data.misc.IFunctions;
import weaver.conn.RecordSet;
import weaver.formmode.virtualform.UUIDPKVFormDataSave;
import weaver.general.Util;

import java.text.SimpleDateFormat;
import java.util.*;

public class class_20231204111117 {
    RecordSet rs = new RecordSet();
    String error="";
    Map<String,Object> ret = new HashMap<>();
    HashMap<String,String> dfmap = new HashMap<>();
    /**
     * @param:  param(Map collections)
     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
     */
    public Map execute(Map<String,Object> params) {
        String formmodeid = Util.null2String(params.get("formmodeid"));

        ArrayList<RYGZ> rygzList = new ArrayList<>();
        // 获取当前年份
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR)-1;
        String sql="select xm,sum(gwgz) gwgz,sum(glgz) glgz,sum(blgz) blgz,sum(bfgz) bfgz,sum(jxgz) jxgz,sum(sbhjgr) sbhjgr,sum(zfgjjgr) zfgjjgr,sum(qynjgr) qynjgr,sum(gzsds) gzsds from uf_gzx2023 where xm in(select XM from uf_2023dyxx where jflx=1) and SUBSTR(rq,1,4)='"+year+"' group by xm";
        if(rs.execute(sql)){
            while(rs.next()){
                RYGZ rygz = new RYGZ();
                String xm=rs.getString("xm");
                String dzz=getxmDzz(xm);
                String sj=getDzzSj(dzz);
                String zzwy=getDzzZZwy(dzz);
                rygz.setId(rs.getString("xm"));
                rygz.setName(esbexcuteSql("select lastname from hrmresource where id=?",rs.getString("xm"),"lastname"));
                rygz.setRq(year+"");
                rygz.setGwgz(rs.getDouble("gwgz"));
                rygz.setGlgz(rs.getDouble("glgz"));
                rygz.setBlgz(rs.getDouble("blgz"));
                rygz.setBfgz(rs.getDouble("bfgz"));
                rygz.setJxgz(rs.getDouble("jxgz"));
                rygz.setSbhj(rs.getDouble("sbhjgr"));
                rygz.setZfgjj(rs.getDouble("zfgjjgr"));
                rygz.setQynj(rs.getDouble("qynjgr"));
                rygz.setGzsds(rs.getDouble("gzsds"));
                rygz.setZgz(0);
                rygz.sumzgz();
                ret.put("zgz",rygz.getGwgz()+rygz.getGlgz()+rygz.getBlgz()+rygz.getBfgz()+rygz.getJxgz()-rygz.getSbhj()-rygz.getZfgjj()-rygz.getQynj()-rygz.getGzsds());
                rygz.setYxyf(getMonth(rygz.getId(),year+""));
                rygz.avgDfjs();
                rygz.setDzz(dzz);
                rygz.setSj(sj);
                rygz.setZzwy(zzwy);
                rygzList.add(rygz);
            }
        }else{
            error+="sql"+sql+"执行失败</br>";
        }


        List<Map<String, Object>> reslist = new ArrayList<Map<String, Object>>();
        reslist=doInsertPartyAmount(rygzList,formmodeid);



        if("".equals(error)){
            ret.put("code","S");
            ret.put("msg","执行完成");
            ret.put("list",reslist);
        }else{
            ret.put("code","E");
            ret.put("msg",error);
        }
        return ret;

    }

    private String getDzzZZwy(String dzz) {
        RecordSet rs = new RecordSet();
        String sql="select zzwy from uf_dzzxx2023 where id=?";
        rs.executeQuery(sql,dzz);
        if(rs.next()){
            return rs.getString("zzwy");
        }else{
            error+=sql+"执行失败";
        }
        return "";
    }

    private String getDzzSj(String dzz) {
        RecordSet rs = new RecordSet();
        String sql="select sj from uf_dzzxx2023 where id=?";
        rs.executeQuery(sql,dzz);
        if(rs.next()){
            return rs.getString("sj");
        }else{
            error+=sql+"执行失败";
        }
        return "";
    }

    private String getxmDzz(String xm) {
        RecordSet rs = new RecordSet();
        String sql="select DZZ from uf_2023dyxx where XM=?";
        rs.executeQuery(sql,xm);
        if(rs.next()){
            return rs.getString("DZZ");
        }else{
            error+=sql+"执行失败";
        }
        return "";
    }

    //写入党费基数表
    private List<Map<String, Object>> doInsertPartyAmount(ArrayList<RYGZ> rygzArrayList,String formmodeid) {
        if ("".equals(formmodeid)) {
            formmodeid = "48851";
        }
        //获取list
        JSONArray listarr=new JSONArray();
        listarr.addAll(rygzArrayList);

        ret.put("listarr",listarr);
        List<String> dflist = new ArrayList<>();
        dflist = getDFList();// 获取项目列表原记录
        List<Map<String, Object>> newlist =new ArrayList<>();



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
                String xm = Util.null2String(dfobj.get("id"));//	姓名
                String rq = getCurrentDate();//	应缴日期
                String dfjs = Util.null2String(dfobj.get("dfjs"));//	党费基数
                String dzz = Util.null2String(dfobj.get("dzz"));//	党组织
                String sj = Util.null2String(dfobj.get("sj"));//	书记
                String zzwy = Util.null2String(dfobj.get("zzwy"));//	组织委员
                //不存在 新增
                Object[] prams = new Object[6];
                prams[0] = xm;//姓名
                prams[1] = rq;//日期
                prams[2] = dfjs;//党费基数
                prams[3] = dzz;//党组织
                prams[4] = sj;//书记
                prams[5] = zzwy;//组织委员
                newdfmap = new HashMap<>();
                if (dflist.indexOf(xm+rq) > -1) {
                    String id = dfmap.get(xm+rq);
                    String upsql = "xm=?,rq=?,dfjs=?,dzz=?,sj=?,zzwy=?";
                    //存在 更新
                    String updatesql = "update uf_dfjs set " + upsql + "  where id='" + id + "'";
                    boolean flag = rs.executeUpdate(updatesql, prams);
                    newdfmap.put("sql", updatesql+prams);
                    newdfmap.put("xm",xm);

                    if (!flag) {
                        newdfmap.put("status", "E");
                        newdfmap.put("msg", "更新失败");
                        error+="xm :"+xm +"更新失败";

                    }
                    else {
                        newdfmap.put("status", "S");
                        newdfmap.put("msg", "更新成功");
                    }


                } else {


                    UUIDPKVFormDataSave var64 = new UUIDPKVFormDataSave();
                    String modeuuid = (String) var64.generateID((Map) null);
                    String value = "?,?,?,?,?,?,";
                    String fields = "xm,rq,dfjs,dzz,sj,zzwy,";
                    String insertsql = "insert into uf_dfjs  (" + fields + "formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,modeuuid) values (" + value + " " + formmodeid + ",1,0,'" + this.getCurrentDate() + "','" + this.getCurrentTime() + "','" + modeuuid + "')";
                    boolean flag = rs.executeUpdate(insertsql, prams);
                    newdfmap.put("sql", insertsql+prams);
                    newdfmap.put("xm",xm);
                    if (!flag) {
                        newdfmap.put("status", "E");
                        newdfmap.put("msg", "插入失败");
                        error+="xm :"+xm +"插入失败";

                    }
                    else {
                        newdfmap.put("status", "S");
                        newdfmap.put("msg", "插入成功");
                    }
                    //插入缓存
                    insertsql = "select id from uf_dfjs where xm  = ? and rq=?";
                    flag = rs.executeQuery(insertsql, xm+rq);
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
//    private List<Map<String, Object>> doInsertPartyAmount(ArrayList<RYGZ> rygzArrayList,String formmodeid) {
//        if ("".equals(formmodeid)) {
//            formmodeid = "43325";
//        }
//        //获取list
//        JSONArray listarr=new JSONArray();
//        listarr.addAll(rygzArrayList);
//
//        ret.put("listarr",listarr);
//        List<String> dflist = new ArrayList<>();
//        dflist = getDFList();// 获取项目列表原记录
//        List<Map<String, Object> > newlist =new ArrayList<>() ;
//
//
//        Map<String, Object> newdfmap = new HashMap<>();
//        if (listarr != null && listarr.size() > 0) {
//            for (int i = 0; i < listarr.size(); i++) {
//                JSONObject dfobj = listarr.getJSONObject(i);
//                if (dfobj == null) {
//                    continue;
//                }
//                //项目信息
//                /**
//                 * ∟dfdata	json	否	党费台账
//                 */
//                String xm = Util.null2String(dfobj.get("id"));//	姓名
//                String yjrq = getCurrentDate();//	应缴日期
//                String yjje = Util.null2String(dfobj.get("zgz"));//	应缴金额
//                //不存在 新增
//                Object[] prams = new Object[3];
//                prams[0] = xm;//姓名
//                prams[1] = yjrq;//应缴日期
//                prams[2] = yjje;//应缴金额
//                newdfmap = new HashMap<>();
//                if (dflist.indexOf(xm+yjrq) > -1) {
//                    String id = dfmap.get(xm+yjrq);
//                    String upsql = "xm=?,yjrq=?,yjje=?";
//                    //存在 更新
//                    String updatesql = "update uf_dfyjjl set " + upsql + "  where id='" + id + "'";
//                    boolean flag = rs.executeUpdate(updatesql, prams);
//                    newdfmap.put("sql", updatesql+prams);
//                    newdfmap.put("xm",xm);
//
//                    if (!flag) {
//                        newdfmap.put("status", "E");
//                        newdfmap.put("msg", "更新失败");
//                        error+="xm :"+xm +"更新失败";
//
//                    }
//                    else {
//                        newdfmap.put("status", "S");
//                        newdfmap.put("msg", "更新成功");
//                    }
//
//
//                } else {
//
//
//                    UUIDPKVFormDataSave var64 = new UUIDPKVFormDataSave();
//                    String modeuuid = (String) var64.generateID((Map) null);
//                    String value = "?,?,?,";
//                    String fields = "xm,yjrq,yjje,";
//                    String insertsql = "insert into uf_dfyjjl  (" + fields + "formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,modeuuid) values (" + value + " " + formmodeid + ",1,0,'" + this.getCurrentDate() + "','" + this.getCurrentTime() + "','" + modeuuid + "')";
//                    boolean flag = rs.executeUpdate(insertsql, prams);
//                    newdfmap.put("sql", insertsql+prams);
//                    newdfmap.put("xm",xm);
//                    if (!flag) {
//                        newdfmap.put("status", "E");
//                        newdfmap.put("msg", "插入失败");
//                        error+="xm :"+xm +"插入失败";
//
//                    }
//                    else {
//                        newdfmap.put("status", "S");
//                        newdfmap.put("msg", "插入成功");
//                    }
//                    //插入缓存
//                    insertsql = "select id from uf_dfyjjl where xm  = ? and yjrq=?";
//                    flag = rs.executeQuery(insertsql, xm+yjrq);
//                    String id = "";
//                    if (rs.next()) {
//                        id = Util.null2String(rs.getString("id"));
//                    }
//                    dfmap.put(Util.null2String(xm), id);
//
//                }
//                newlist.add(newdfmap);
//
//
//            }
//        }
//        return newlist;
//    }
    /**
     * 获取已存在党费信息
     *
     * @return
     */
    private List<String> getDFList() {
        List<String> dflist = new ArrayList<>();
        String sqlqzyj = "select xm ,id,rq,dfjs  from uf_dfjs";
        //获取正文

        rs.executeQuery(sqlqzyj.toString());
        while (rs.next()) {
            dflist.add(Util.null2String(rs.getString("xm"))+Util.null2String(rs.getString("rq")));
            dfmap.put(Util.null2String(rs.getString("xm"))+Util.null2String(rs.getString("rq")), Util.null2String(rs.getString("id")));
        }
        return dflist;
    }


    //根据基数计算党费
    private double getPartyAmount(double baseAmount){
        double partyAmount=0;
        if(baseAmount<=3000){
            partyAmount=baseAmount*0.005;
        }
        else if(baseAmount>3000&&baseAmount<=5000){
            partyAmount=baseAmount*0.01;
        }
        else if(baseAmount>5000&&baseAmount<=10000){
            partyAmount=baseAmount*0.015;
        }
        else if(baseAmount>10000){
            partyAmount=baseAmount*0.02;
        }else{
            error+="党费计算失败</br>";
        }
        return partyAmount;
    }
    //计算上年绩效工资>0的月数
    private int getMonth(String id,String year){
        String sl=esbexcuteSql("select count(1) zs from uf_gzx2023 where jxgz>0 and xm=? and SUBSTR(rq,1,4)='"+year+"'",id,"zs");
        return Integer.parseInt(sl);
    }

    private String esbexcuteSql(String sql,String param,String resultname) {
        RecordSet RS = new RecordSet();
        String result="";
        if(RS.executeQuery(sql,param)&&RS.next()){
            result=RS.getString(resultname);
        }else{
            error+="sql "+sql+"执行失败</br>";
            return null;
        }
        return result;
    }

    private String getCurrentDate() {
        SimpleDateFormat var1 = new SimpleDateFormat("yyyy-MM-dd");
        return var1.format(new Date());
    }

    private String getCurrentTime() {
        SimpleDateFormat var1 = new SimpleDateFormat("HH:mm:ss");
        return var1.format(new Date());
    }



}


class RYGZ{
    //人员id
    private String id;
    //人员姓名
    private String name;
    //日期
    private String rq;
    //岗位工资
    private double gwgz;
    //工龄工资
    private double glgz;
    //保留工资
    private double blgz;
    //补发工资
    private double bfgz;
    //绩效工资
    private double jxgz;
    //社保合计(个人)
    private double sbhj;
    //住房公积金(个人)
    private double zfgjj;
    //企业年金(个人)
    private double qynj;
    //工资所得税
    private double gzsds;
    //当月总工资
    private double zgz;
    //年度缴纳党费基数
    private double dfjs;
    //上年有效的月份数
    private int yxyf;
    //党组织
    private String dzz;
    //书记
    private String sj;
    //组织委员
    private String zzwy;

    public String getDzz() {
        return dzz;
    }

    public void setDzz(String dzz) {
        this.dzz = dzz;
    }

    public String getSj() {
        return sj;
    }

    public void setSj(String sj) {
        this.sj = sj;
    }

    public String getZzwy() {
        return zzwy;
    }

    public void setZzwy(String zzwy) {
        this.zzwy = zzwy;
    }

    public void sumzgz(){
        //总工资计算
        this.setZgz(this.gwgz+this.glgz+this.blgz+this.bfgz+this.jxgz-this.sbhj-this.zfgjj-this.qynj-this.gzsds);
    }

    public void avgDfjs(){
        this.setDfjs(this.getZgz()/this.getYxyf());
    }

    public int getYxyf() {
        return yxyf;
    }

    public void setYxyf(int yxyf) {
        this.yxyf = yxyf;
    }

    public double getDfjs() {
        return dfjs;
    }

    public void setDfjs(double dfjs) {
        this.dfjs = dfjs;
    }

    public double getZgz() {
        return zgz;
    }


    public void setZgz(double zgz) {
        this.zgz = zgz;
    }

    public String getRq() {
        return rq;
    }

    public void setRq(String rq) {
        this.rq = rq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGwgz() {
        return gwgz;
    }

    public void setGwgz(double gwgz) {
        this.gwgz = gwgz;
    }

    public double getGlgz() {
        return glgz;
    }

    public void setGlgz(double glgz) {
        this.glgz = glgz;
    }

    public double getBlgz() {
        return blgz;
    }

    public void setBlgz(double blgz) {
        this.blgz = blgz;
    }

    public double getBfgz() {
        return bfgz;
    }

    public void setBfgz(double bfgz) {
        this.bfgz = bfgz;
    }

    public double getJxgz() {
        return jxgz;
    }

    public void setJxgz(double jxgz) {
        this.jxgz = jxgz;
    }

    public double getSbhj() {
        return sbhj;
    }

    public void setSbhj(double sbhj) {
        this.sbhj = sbhj;
    }

    public double getZfgjj() {
        return zfgjj;
    }

    public void setZfgjj(double zfgjj) {
        this.zfgjj = zfgjj;
    }

    public double getQynj() {
        return qynj;
    }

    public void setQynj(double qynj) {
        this.qynj = qynj;
    }

    public double getGzsds() {
        return gzsds;
    }

    public void setGzsds(double gzsds) {
        this.gzsds = gzsds;
    }
}
