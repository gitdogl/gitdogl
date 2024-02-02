//package com.weaver.esb.package_20230615042534;
//
//import com.alibaba.fastjson.JSONArray;
//import weaver.conn.RecordSet;
//import weaver.general.Util;
//
//import java.util.*;
//
//public class class_20230615042534 {
//
//
//    RecordSet rs = new RecordSet();
//
//    /**
//     * @param: param(Map collections)
//     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
//     */
//    public Map execute(Map<String, Object> params) {
//        // 示例：data：定义的请求数据，code:定义的响应数据
//        String requestid = Util.null2String(params.get("requestid"));//流程编号
//        String fyfl = Util.null2String(params.get("fyfl"));//费用分类
//        String isys = Util.null2String(params.get("isys"));//是否验证预算
//        String lclx = Util.null2String(params.get("lclx"));//流程类型
//        Map<String, String> ret = new HashMap<>();
//
//        int requestidint=Util.getIntValue(requestid,-1);
//        if(requestidint<=0)
//        {
//            ret.put("code", "1");
//            ret.put("msg", "验证失败：相关请求异常" + requestid);
//            return ret;
//        }
//        if("".equals(isys))
//        {
//            isys="1";
//        }
//        if("".equals(isys))
//        {
//            ret.put("code", "0");
//            ret.put("msg", "验证通过不需要验证预算跳过");
//            return ret;
//        }
//
//        if("".equals(fyfl))
//        {
//            ret.put("code", "1");
//            ret.put("msg", "验证失败：相关请求异常" + requestid+"流程分类不能为空");
//            return ret;
//        }
//        if("0".equals(fyfl)||"3".equals(fyfl))
//        {
//            ret.put("code", "0");
//            ret.put("msg", "验证通过费用类型不控制预算");
//            return ret;
//
//        }
//        String tablename=getBillTableNameByrequestid(requestid);
//
//        if("".equals(tablename))
//        {
//            ret.put("code", "1");
//            ret.put("msg", "验证失败：相关请求异常" + requestid+"不存在概数据存储表");
//            return ret;
//        }
//        if("".equals(lclx))
//        {
//            ret.put("code", "1");
//            ret.put("msg", "验证失败：相关请求异常" + requestid+"流程类型不能为空");
//            return ret;
//        }
//
//        //
//        // 处理预算
//        StringBuffer sql = new StringBuffer();
//
//        String error = "";
//        String gcmc = "";
//        if("0".equals(lclx))
//        {
//            //员工差旅费验证
//            if("1".equals(fyfl))
//            {
//                //差旅费项目预算校验
//                sql = new StringBuffer("  select case when ys.wbsmc is null then xmmc else  ys.wbsmc end name " +
//                        "     ,NVL(bxje,0)-NVL(syys,0)+NVL(bccxje,0) cbsj  from   (    select xmmc,  sum(NVL(xj,0)) bxje " +
//                        "     from " + tablename + "_dt1   where  xmmc !=' '  and   " +
//                        "     mainid in (select id from " + tablename + " where requestid='")
//                        .append(requestid).append("') group by xmmc )" +
//                                "      hbhhz left outer join V_XMYSZX ys on hbhhz.xmmc=ys.wbsbm"+
//                                " left outer join (select d.xmjkcxje jkxmjd , sum(NVL(d.bccxje,0)) bccxje  from "+tablename+" m,"+tablename+"_dt2 d" +
//                                " where m.id=d.mainid and m.fyfl=1 and m.requestid='"+requestid+"' group by d.xmjkcxje ) bccx on bccx.jkxmjd=ys.wbsbm "+
//                                " where NVL(bxje,0)-NVL(bccxje,0)>NVL(syys,0)");
//                rs.execute(sql.toString());
//                ret.put(fyfl+"xmyzsql=", sql.toString());
//                while (rs.next()) {
//                    error += Util.null2String(rs.getString("name")) +"超预算金额："+ Util.null2String(rs.getString("cbsj")) +"<br>";
//                }
//
//            }
//            else if ("2".equals(fyfl))
//            {
//                //专项预算
//                sql = new StringBuffer("  select case when ys.wbsmc is null then hbhhz.nbdd else  ys.wbsmc end name " +
//                        "     ,NVL(bxje,0)-NVL(syys,0)+NVL(bccxje,0) cbsj  from   (    select nbdd,  sum(NVL(xj,0)) bxje " +
//                        "     from " + tablename + "_dt1   where  nbdd !=' '  and   " +
//                        "     mainid in (select id from " + tablename + " where requestid='")
//                        .append(requestid).append("') group by nbdd )" +
//                                "      hbhhz left outer join V_XMYSZX ys on hbhhz.nbdd=ys.wbsbm"+
//                                " left outer join (select d.nbdd, sum(NVL(d.bccxje,0)) bccxje  from "+tablename+" m,"+tablename+"_dt2 d" +
//                                " where m.id=d.mainid and m.fyfl=2 and m.requestid='"+requestid+"' group by d.nbdd ) bccx on bccx.nbdd=ys.wbsbm "+
//                                " where NVL(bxje,0)-NVL(bccxje,0)>NVL(syys,0)");
//                rs.execute(sql.toString());
//                ret.put(fyfl+"nbddsql=", sql.toString());
//                while (rs.next()) {
//                    error += Util.null2String(rs.getString("name")) +"超预算金额："+ Util.null2String(rs.getString("cbsj")) +"<br>";
//                }
//            }
//
//
//
//
//
//
//        }
//        else if("1".equals(lclx)){
//            //员工非差旅
//
//            if("1".equals(fyfl))
//            {
//                sql = new StringBuffer(" select case when ys.wbsmc is null then hbhhz.xmjd else  ys.wbsmc end name  ,NVL(bxje,0)-NVL(syys,0)+NVL(bccxje,0) cbsj  from   (    select xmjd,  sum(NVL(bxje,0)) bxje from "+tablename
//                        +"_dt1   where  xmjd !=' '  and   mainid in (select id from "+tablename
//                        +" where requestid='")
//                        .append(requestid).append("') group by xmjd ) hbhhz left outer join V_XMYSZX ys on hbhhz.xmjd=ys.wbsbm"+
//                                " left outer join (select d.jkxmjd, sum(NVL(d.bccxje,0)) bccxje  from "+tablename+" m,"+tablename+"_dt2 d" +
//                                " where m.id=d.mainid and m.fyfl=1 and m.requestid='"+requestid+"' group by jkxmjd ) bccx on bccx.jkxmjd=ys.wbsbm "+
//                                " where NVL(bxje,0)-NVL(bccxje,0)>NVL(syys,0)");
//                rs.execute(sql.toString());
//                ret.put(fyfl+"xmyzsql=", sql.toString());
//                while (rs.next()) {
//                    error += Util.null2String(rs.getString("name")) +"超预算金额："+ Util.null2String(rs.getString("cbsj")) +"<br>";
//                }
//
//
//            }
//            else if ("2".equals(fyfl))
//            {
//                //专项预算
//                sql = new StringBuffer(" select case when ys.wbsmc is null then hbhhz.nbdd else  ys.wbsmc end name  ,NVL(bxje,0)-NVL(syys,0)+NVL(bccxje,0) cbsj  from   (    select nbdd,  sum(NVL(bxje,0)) bxje from "+tablename
//                        +"_dt1   where  nbdd !=' '  and   mainid in (select id from "+tablename
//                        +" where requestid='")
//                        .append(requestid).append("') group by nbdd ) hbhhz left outer join V_XMYSZX ys on hbhhz.nbdd=ys.wbsbm"+
//                                " left outer join (select d.nbdd, sum(NVL(d.bccxje,0)) bccxje  from "+tablename+" m,"+tablename+"_dt2 d" +
//                                " where m.id=d.mainid and m.fyfl=2 and m.requestid='"+requestid+"' group by d.nbdd ) bccx on bccx.nbdd=ys.wbsbm "+
//                                " where NVL(bxje,0)-NVL(bccxje,0)>NVL(syys,0)");
//                rs.execute(sql.toString());
//                ret.put(fyfl+"xmyzsql=", sql.toString());
//                while (rs.next()) {
//                    error += Util.null2String(rs.getString("name")) +"超预算金额："+ Util.null2String(rs.getString("cbsj")) +"<br>";
//                }
//            }
//
//
//
//        }
//        else if("2".equals(lclx)){
//            //员工借款
//
//            if("1".equals(fyfl))
//            {
//                sql = new StringBuffer(" select case when ys.wbsmc is null then xmwbsjd else  ys.wbsmc end name  ,NVL(bxje,0)-NVL(syys,0) cbsj  from   (    select xmwbsjd,  sum(NVL(jkje,0)) bxje from "+tablename
//                        +"_dt1  a  ,"+tablename
//                        +" b where a.mainid=b.id and b.requestid='")
//                        .append(requestid).append("'  and b.xmwbsjd is not null  group by xmwbsjd ) hbhhz left outer join V_XMYSZX ys on hbhhz.xmwbsjd=ys.wbsbm where NVL(bxje,0)>NVL(syys,0)");
//                rs.execute(sql.toString());
//                ret.put(lclx+"="+fyfl+"xmyzsql=", sql.toString());
//                while (rs.next()) {
//                    error += Util.null2String(rs.getString("name")) +"超预算金额："+ Util.null2String(rs.getString("cbsj")) +"<br>";
//                }
//
//
//            }
//            else if ("2".equals(fyfl))
//            {
//                //专项预算
//                sql = new StringBuffer(" select case when ys.wbsmc is null then nbdd else  ys.wbsmc end name  ,NVL(bxje,0)-NVL(syys,0) cbsj  from   (    select nbdd,  sum(NVL(jkje,0)) bxje from "+tablename
//                        +"_dt1  a ,"+tablename
//                        +" b where a.mainid=b.id and  b.requestid='")
//                        .append(requestid).append("' and  b.nbdd is null   group by nbdd ) hbhhz left outer join V_XMYSZX ys on hbhhz.nbdd=ys.wbsbm where NVL(bxje,0)>NVL(syys,0)");
//                rs.execute(sql.toString());
//                ret.put(lclx+"-"+fyfl+"xmyzsql=", sql.toString());
//                while (rs.next()) {
//                    error += Util.null2String(rs.getString("name")) +"超预算金额："+ Util.null2String(rs.getString("cbsj")) +"<br>";
//                }
//            }
//        }
//        else if("3".equals(lclx)){
////            物品领用
//
//                if("1".equals(fyfl))
//                {
//                    sql = new StringBuffer(" select case when ys.wbsmc is null then xmjd else  ys.wbsmc end name  ,NVL(bxje,0)-NVL(syys,0) cbsj  from   (    select xmjd,  sum(NVL(sjje,0)) bxje from "+tablename
//                            +"_dt1  a  ,"+tablename
//                            +" b where a.mainid=b.id and b.requestid='")
//                            .append(requestid).append("'  group by xmjd ) hbhhz left outer join V_XMYSZX ys on hbhhz.xmjd=ys.wbsbm where NVL(bxje,0)>NVL(syys,0)");
//                    rs.execute(sql.toString());
//                    ret.put(lclx+"="+fyfl+"xmyzsql=", sql.toString());
//                    while (rs.next()) {
//                        error += Util.null2String(rs.getString("name")) +"超预算金额："+ Util.null2String(rs.getString("cbsj")) +"<br>";
//                    }
//
//
//                }
//                else if ("2".equals(fyfl))
//                {
//                    //专项预算
//                    sql = new StringBuffer(" select case when ys.wbsmc is null then nbdd else  ys.wbsmc end name  ,NVL(bxje,0)-NVL(syys,0) cbsj  from   (    select nbdd,  sum(NVL(sjje,0)) bxje from "+tablename
//                            +"_dt1  a ,"+tablename
//                            +" b where a.mainid=b.id and  b.requestid='")
//                            .append(requestid).append("'   group by nbdd ) hbhhz left outer join V_XMYSZX ys on hbhhz.nbdd=ys.wbsbm where NVL(bxje,0)>NVL(syys,0)");
//                    rs.execute(sql.toString());
//                    ret.put(lclx+"-"+fyfl+"xmyzsql=", sql.toString());
//                    while (rs.next()) {
//                        error += Util.null2String(rs.getString("name")) +"超预算金额："+ Util.null2String(rs.getString("cbsj")) +"<br>";
//                    }
//                }
//
//            }
//
//
//
//
//
//
//        // ……
//
//        if ("".equals(error)) {
//            ret.put("code", "0");
//            ret.put("msg", "验证通过");
//        } else {
//            ret.put("code", "1");
//            ret.put("msg", "验证失败：" + error);
//        }
//        return ret;
//
//    }
//    public  String getBillTableNameByrequestid(String requestid) {
//        String tablename = "";
//        if (!"".equals(requestid)) {
//            String select_data = "select tablename from workflow_bill where id in (select formid from workflow_base where id  in (select workflowid from workflow_requestbase where requestid = ?))";
//            if (rs.executeQuery(select_data, new Object[]{requestid}) && rs.next()) {
//                tablename = weaver.general.Util.null2String(rs.getString(1));
//            }
//        }
//
//        return tablename;
//    }
//
//
//}
