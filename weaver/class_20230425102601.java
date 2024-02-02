package com.weaver.esb.package_20230425102601;

import weaver.conn.RecordSet;
import weaver.general.Util;

import java.util.*;

public class class_20230425102601 {
    RecordSet rs = new RecordSet();

    /**
     * @param: param(Map collections)
     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
     */
    public Map execute(Map<String, Object> params) {
        // 示例：data：定义的请求数据，code:定义的响应数据
        // String data = params.get("data");
        // ……
        String requestid = Util.null2String(params.get("requestid"));// 标题
        String tablename = Util.null2String(params.get("tablename"));// 标题
        String isys = Util.null2String(params.get("isys"));// 标题
        if ("".equals(tablename)) {
            tablename =getBillTableNameByrequestid(requestid);
        }
        if ("".equals(isys)) {
            isys = "1";
        }
        Map<String, String> ret = new HashMap<>();
        // ret.put("code",1);
        try {

            // 项目差旅费
            // 处理预算
            StringBuffer sql = new StringBuffer(
                    "delete   from  " + tablename + "_dt1 where mainid in (select id from " + tablename + " where requestid='")
                    .append(requestid).append("')");
            rs.execute(sql.toString());

            // --插入明细1
            sql = new StringBuffer(
                    "");
            sql.append(" insert into  "+tablename+"_dt1 ( mainid,xmbh,xmjl,zy, fycdbm,yskm,xmmc,nbdd, bxjtje,shjtje ,xj,jxszc,fycdlx,sapcbzx )\n" +
                    "  select  mainid,xmbh,xmjl, '交通费+住宿费+其他费用' zy, fycdbm,yskm,xmjd,nbdd, nvl(sum(nvl(bhsje,0)),0) bhsje ,nvl(sum(nvl(se,0)),0) se    ,nvl(sum(nvl(bhsje,0)),0) +nvl(sum(nvl(se,0)),0) fpje , nvl(sum( nvl(jxszc,0)),0)  jxszc ,1 ,sapcbzx \n" +
                    "   from ( select mainid  ,xmbh,xmjl, '交通费' zy,  fycdbm ,sapcbzx, fycdfb,yskm,xmjd,nbdd, nvl(sum(nvl(bhsje,0)),0) bhsje ,nvl(sum(nvl(se,0)+nvl(bhsjen,0)),0) \n" +
                    "   se, nvl(sum( nvl(jxszc,0)),0)  jxszc\n" +
                    "    from "+tablename+"_dt4 \n" +
                    "    where mainid in (select id from " + tablename + " where requestid='").append(requestid).append("')  \n" +
                    "     group by mainid,  fycdbm,sapcbzx,fycdfb,yskm ,xmbh,xmjl,xmjd,nbdd \n" +
                    "     union all select mainid  ,xmbh,xmjl,  '住宿费' zy,  fycdbm ,sapcbzx, fycdfb,yskm ,xmmc,nbdd,\n" +
                    "      nvl(sum(nvl(bhsje,0)),0) bhsje ,nvl(sum(nvl(se,0)),0) se, nvl(sum( nvl(jxszc,0)),0) \n" +
                    "       jxszc from "+tablename+"_dt5 where mainid in (select id from " + tablename + " \n" +
                    "       where requestid='").append(requestid).append("')   group by mainid,  fycdbm,sapcbzx,fycdfb,yskm  ,xmbh,xmjl,xmmc,nbdd \n" +
                    "       union all select mainid ,xmbh,xmjl, '其他费' zy,  fycdbm,sapcbzx, fycdfb,yskm ,xmmc,nbdd,\n" +
                    "        nvl(sum(nvl(bhsje,0)),0) bhsje ,nvl(sum(nvl(se,0)),0) se, nvl(sum( nvl(jxszc,0)),0) \n" +
                    "         jxszc from "+tablename+"_dt6 where mainid in (select id from " + tablename + " \n" +
                    "         where requestid='").append(requestid).append("')   group by mainid,  fycdbm ,sapcbzx,fycdfb,yskm  ,xmbh,xmjl ,xmmc,nbdd ) \n" +
                    "         where mainid in (select id from " + tablename + " where requestid='").append(requestid).append("')  \n" +
                    "          group by mainid,  fycdbm ,sapcbzx,fycdfb,yskm,xmbh,xmjl ,xmjd,nbdd ");
            rs.execute(sql.toString());
            ret.put("dt1sql", sql.toString());
            // 超标处理

            // --插入明细1
            sql = new StringBuffer(
                    "update "+tablename+"_dt1 set bxjtje=bxjtje+(select ");
            sql.append("  cebz  from " + tablename + " where requestid='")
                    .append(requestid).append("' ) ,xj=xj+(select ");
            sql.append(" cebz  from " + tablename + " where requestid='")
                    .append(requestid).append("' ) ");
            sql.append(
                            " where id in (select min(id) from "+tablename+"_dt1 where   mainid in (select id from " + tablename + " where requestid='")
                    .append(requestid).append("') ");
            sql.append(" ) ");
            ;
            rs.execute(sql.toString());

            //更新预算
            sql = new StringBuffer(
                    " update "+tablename+"_dt1 set syys =( select SYYS from V_XMYSZX where V_XMYSZX.WBSBM="+tablename+"_dt1.xmmc) " +
                            "   , jkwhje =( select SPZJE from V_XMYSZX where V_XMYSZX.WBSBM="+tablename+"_dt1.xmmc) " +
                            "  , lsyy =( select YfSJE from V_XMYSZX where V_XMYSZX.WBSBM="+tablename+"_dt1.xmmc) " +
                            " where mainid in (select id from " + tablename + " where requestid='")
                    .append(requestid).append("'  and fyfl=1 ) ");


            rs.execute(sql.toString());
            ret.put("dt1upsqlxm", sql.toString());


            sql = new StringBuffer(
                    " update "+tablename+"_dt1 set syys =( select SYYS from V_XMYSZX where V_XMYSZX.WBSBM="+tablename+"_dt1.nbdd) " +
                            "   , jkwhje =( select SPZJE from V_XMYSZX where V_XMYSZX.WBSBM="+tablename+"_dt1.nbdd) " +
                            "  , lsyy =( select YfSJE from V_XMYSZX where V_XMYSZX.WBSBM="+tablename+"_dt1.nbdd) " +
                            " where mainid in (select id from " + tablename + " where requestid='")
                    .append(requestid).append("'  and fyfl=2 ) ");


            rs.execute(sql.toString());
            ret.put("dt1upsqlxm", sql.toString());

            //更新预算结束




            // 处理ERP
            sql = new StringBuffer(
                    "delete   from  "+tablename+"_dt7 where mainid in (select id from " + tablename + " where requestid='")
                    .append(requestid).append("')");
            rs.execute(sql.toString());

            // --插入明细1
            sql = new StringBuffer(
                    "");
            sql.append(" insert into  "+tablename+"_dt7 ( mainid,zy, fyhjkm,je,ywbm,sapcbzx,nbdd,xmjd) \n" +
                    " select mainid,zy, fyhjkm,   sum( je) je,ywbm ,sapcbzx,nbdd ,xmjd  from (\n" +
                    "\n" +
                    "\n" +
                    "select mainid, a.fyhjkm,nbdd,xmjd, '付'||b.lastname||'报'||c.sqsy || '' zy, nvl(sum(nvl(bhsje,0)),0) je\n" +
                    ",a.fycdbm ywbm ,sapcbzx\n" +
                    " from "+tablename+"_dt4   a\n" +
                    " left outer join hrmresource  b on a.bxr=b.id\n" +
                    " left outer join " + tablename + " c on a.mainid=c.id\n" +
                    "  where  c.requestid='").append(requestid).append("'\n" +
                    "group by mainid, fyhjkm,  b.lastname,c.sqsy ,a.xmjd,a.fycdbm ,a.sapcbzx,a.nbdd\n" +
                    //  "union all\n" +
                    //  "select mainid,e.jxskm fyhjkm,a.nbdd,xmjd, '付'||b.lastname||'报'||c.name || ''||'专票税额' zy,nvl(sum(nvl(se,0)),0) se,a.fycdbm ywbm ,a.sapcbzx\n" +
                    //   " from "+tablename+"_dt4  a\n" +
                    //  " left outer join hrmresource  b on a.bxr=b.id\n" +
                    //  " left outer join fnabudgetfeetype c on a.yskm=c.id\n" +
                    //  " left outer join " + tablename + " d on d.id=a.mainid\n" +
                    // " left outer join uf_erp_zt e on e.zj=d.zt\n" +
                    //  "  where  d.requestid='").append(requestid).append("' and se>0\n" +
                    //   "group by mainid, e.jxskm,  b.lastname,c.name ,a.xmjd,a.fycdbm,a.sapcbzx,a.nbdd\n" +
                    //   "union all\n" +
                    //    "select mainid, e.jxskm fyhjkm,a.nbdd, xmjd,'付'||b.lastname||'报'||c.name || ''||'火车票' zy, nvl(sum(nvl(bhsjen,0)),0)  se,a.fycdbm ywbm,sapcbzx\n" +
                    //   " from "+tablename+"_dt4  a\n" +
                    //   " left outer join hrmresource  b on a.bxr=b.id\n" +
                    //   " left outer join fnabudgetfeetype c on a.yskm=c.id\n" +
                    //    " left outer join " + tablename + " d on d.id=a.mainid\n" +
                    //   " left outer join uf_erp_zt e on e.zj=d.zt\n" +
                    //   "  where  d.requestid='").append(requestid).append("' and se>0\n" +
                    //   "group by mainid, e.jxskm,  b.lastname,c.name ,a.xmjd,a.fycdbm ,a.sapcbzx,a.nbdd\n" +
                    " union all " +
                    "select mainid,'2221010700' fyhjkm ,a.nbdd,xmjd, '付'||b.lastname||'报'||c.name || ''||'进项税转出' zy, nvl(sum( nvl(jxszc,0)),0)  jxszc,a.fycdbm ywbm,sapcbzx\n" +
                    "from "+tablename+"_dt4  a\n" +
                    " left outer join hrmresource  b on a.bxr=b.id\n" +
                    " left outer join fnabudgetfeetype c on a.yskm=c.id\n" +
                    " left outer join " + tablename + " d on d.id=a.mainid\n" +
                    // " left outer join uf_erp_zt e on e.zj=d.zt\n" +
                    " where  d.requestid='").append(requestid).append("' and jxszc>0\n" +
                    "group by mainid, b.lastname,c.name ,a.xmjd,a.fycdbm,a.sapcbzx,a.nbdd\n" +
                    "union all " +
                    "select mainid, fyhjkm,a.nbdd,xmmc, '付'||b.lastname||'报'||c.name || ''||'' zy, nvl(sum(nvl(bhsje,0)),0) bhsje,a.fycdbm ywbm,sapcbzx\n" +
                    " from "+tablename+"_dt5  a\n" +
                    " left outer join hrmresource  b on a.bxr=b.id\n" +
                    " left outer join fnabudgetfeetype c on a.yskm=c.id\n" +
                    " left outer join " + tablename + " d on d.id=a.mainid\n" +
                    " where  d.requestid='").append(requestid).append("' and bhsje>0\n" +
                    "group by mainid, fyhjkm,  b.lastname,c.name ,a.xmmc,a.fycdbm,a.sapcbzx,a.nbdd " +
                    // "union all\n" +
                    // "select mainid, e.jxskm  fyhjkm,a.nbdd,xmmc, '付'||b.lastname||'报'||c.name || ''||'专票税额' zy,nvl(sum(nvl(se,0)),0) se,a.fycdbm ywbm,sapcbzx\n" +
                    //  " from "+tablename+"_dt5  a\n" +
                    // " left outer join hrmresource  b on a.bxr=b.id\n" +
                    //  " left outer join fnabudgetfeetype c on a.yskm=c.id\n" +
                    //  "  left outer join " + tablename + " d on d.id=a.mainid\n" +
                    //  " left outer join uf_erp_zt e on e.zj=d.zt\n" +
                    //   "  where  d.requestid='").append(requestid).append("' and se>0\n" +
                    //  "group by mainid,e.jxskm,  b.lastname,c.name ,a.xmmc,a.fycdbm,a.sapcbzx,a.nbdd\n" +
                    " union all " +
                    " select mainid, '2221010700' fyhjkm,a.nbdd,xmmc, '付'||b.lastname||'报'||c.name || ''||'进项税转出' zy, nvl(sum( nvl(jxszc,0)),0)  jxszc,a.fycdbm ywbm,sapcbzx\n" +
                    " from "+tablename+"_dt5  a\n" +
                    " left outer join hrmresource  b on a.bxr=b.id\n" +
                    " left outer join fnabudgetfeetype c on a.yskm=c.id\n" +
                    " left outer join " + tablename + " d on d.id=a.mainid\n" +
                    //  " left outer join uf_erp_zt e on e.zj=d.zt\n" +
                    " where  d.requestid='").append(requestid).append("' and jxszc>0\n" +
                    "group by mainid,  b.lastname,c.name ,a.xmmc,a.fycdbm,a.sapcbzx,a.nbdd\n" +
                    " union all " +
                    " select mainid, fyhjkm ,a.nbdd,xmmc, '付'||b.lastname||'报'||c.name || ''||'' zy, nvl(sum(nvl(bhsje,0)),0) bhsje,a.fycdbm ywbm,sapcbzx\n" +
                    " from "+tablename+"_dt6  a\n" +
                    " left outer join hrmresource  b on a.bxr=b.id\n" +
                    " left outer join fnabudgetfeetype c on a.yskm=c.id\n" +
                    " left outer join " + tablename + " d on d.id=a.mainid\n" +
                    " where  d.requestid='").append(requestid).append("' and bhsje>0\n" +
                    "group by mainid, fyhjkm,  b.lastname,c.name ,a.xmmc,a.fycdbm ,a.sapcbzx,a.nbdd\n" +
                    //  "union all\n" +
                    //  "select mainid, e.jxskm fyhjkm,a.nbdd,xmmc,  '付'||b.lastname||'报'||c.name || ''||'专票税额' zy,nvl(sum(nvl(se,0)),0) se,a.fycdbm ywbm ,sapcbzx\n" +
                    //  " from "+tablename+"_dt6 a\n" +
                    // " left outer join hrmresource  b on a.bxr=b.id\n" +
                    /// " left outer join fnabudgetfeetype c on a.yskm=c.id\n" +
                    // "  left outer join " + tablename + " d on d.id=a.mainid\n" +
                    //  " left outer join uf_erp_zt e on e.zj=d.zt\n" +
                    // " where  d.requestid='").append(requestid).append("' and se>0\n" +
                    //  "group by mainid, e.jxskm,  b.lastname,c.name ,a.xmmc,a.fycdbm ,a.sapcbzx,a.nbdd\n" +
                    " union all " +
                    "select mainid, '2221010700' fyhjkm, a.nbdd,xmmc, '付'||b.lastname||'报'||c.name || ''||'进项税转出' zy\n" +
                    ", nvl(sum( nvl(jxszc,0)),0)  jxszc ,a.fycdbm ywbm ,sapcbzx\n" +
                    "from " +
                    ""+tablename+"_dt6  a\n" +
                    " left outer join hrmresource  b on a.bxr=b.id\n" +
                    " left outer join fnabudgetfeetype c on a.yskm=c.id\n" +
                    " left outer join " + tablename + " d on d.id=a.mainid\n" +
                    // " left outer join uf_erp_zt e on e.zj=d.zt\n" +
                    " where  d.requestid='").append(requestid).append("' and jxszc>0\n" +
                    "group by mainid,  b.lastname,c.name ,a.xmmc,a.fycdbm ,a.sapcbzx,a.nbdd\n" +
                    " ) erp \n" +
                    " group by mainid, fyhjkm ,sapcbzx,  zy ,ywbm,nbdd,xmjd ");
            rs.execute(sql.toString());
            ret.put("dt7sql", sql.toString());
            // 超标补助
            sql = new StringBuffer(
                    "update "+tablename+"_dt7 set je=je+(select  ");
            sql.append(" cebz  from " + tablename + " where requestid='")
                    .append(requestid).append("' ) ");
            sql.append(
                            " where id in (select min(id) from "+tablename+"_dt7 where  fyhjkm not in ('22210101','22210108') and  mainid in (select id from " + tablename + " where requestid='")
                    .append(requestid).append("') ");
            sql.append(" ) ");
            rs.execute(sql.toString());
            ret.put("dt7upsql", sql.toString());

            //添加预算校验
            String error = "";
            if("1".equals(isys))
            {

                //项目预算管控
                sql = new StringBuffer("  select case when ys.wbsmc is null then xmmc else  ys.wbsmc end name " +
                        "     ,NVL(bxje,0)-NVL(syys,0)+NVL(bccxje,0) cbsj  from   (    select xmmc,  sum(NVL(xj,0)) bxje " +
                        "     from " + tablename + "_dt1   where  xmmc !=' '  and   " +
                        "     mainid in (select id from " + tablename + " where requestid='")
                        .append(requestid).append("' and fyfl=1  ) group by xmmc )" +
                                "      hbhhz left outer join V_XMYSZX ys on hbhhz.xmmc=ys.wbsbm " +
                                " left outer join (select d.xmjd jkxmjd , sum(NVL(d.bccxje,0)) bccxje  from "+tablename+" m,"+tablename+"_dt2 d" +
                                " where m.id=d.mainid and m.fyfl=1 and m.requestid='"+requestid+"' group by d.xmjd ) bccx on bccx.jkxmjd=ys.wbsbm "+
                                "where NVL(bxje,0)-NVL(bccxje,0)>NVL(syys,0)");
                rs.execute(sql.toString());
                ret.put("xmyzsql=", sql.toString());
                while (rs.next()) {
                    error += Util.null2String(rs.getString("name")) +"超预算金额："+ Util.null2String(rs.getString("cbsj")) +"<br>";
                }

                //专项预算
                sql = new StringBuffer("  select case when ys.wbsmc is null then hbhhz.nbdd else  ys.wbsmc end name " +
                        "     ,NVL(bxje,0)-NVL(syys,0)+NVL(bccxje,0) cbsj  from   (    select nbdd,  sum(NVL(xj,0)) bxje " +
                        "     from " + tablename + "_dt1   where  nbdd !=' '  and   " +
                        "     mainid in (select id from " + tablename + " where requestid='")
                        .append(requestid).append("' and fyfl=2 ) group by nbdd )" +
                                "      hbhhz left outer join V_XMYSZX ys on hbhhz.nbdd=ys.wbsbm" +
                                " left outer join (select d.nbdd, sum(NVL(d.bccxje,0)) bccxje  from "+tablename+" m,"+tablename+"_dt2 d" +
                                " where m.id=d.mainid and m.fyfl=1 and m.requestid='"+requestid+"' group by d.nbdd ) bccx on bccx.nbdd=ys.wbsbm "+
                                " where NVL(bxje,0)-NVL(bccxje,0)>NVL(syys,0)");
                rs.execute(sql.toString());
                ret.put("nbddsql=", sql.toString());
                while (rs.next()) {
                    error += Util.null2String(rs.getString("name")) +"超预算金额："+ Util.null2String(rs.getString("cbsj")) +"<br>";
                }

            }

            if (!"".equals(error)) {
                ret.put("code", "0");
                ret.put("msg", "项目预算校验失败：" + error);

            } else {


                ret.put("code", "1");
                ret.put("msg", "ok");
            }

        } catch (Exception s) {
            ret.put("code", "0");
            ret.put("msg", "错误" + s.getMessage());
        }
        // ret.put("code",1);
        return ret;

    }

    public  String getBillTableNameByrequestid(String requestid) {
        String tablename = "";
        if (!"".equals(requestid)) {
            String select_data = "select tablename from workflow_bill where id in (select formid from workflow_base where id  in (select workflowid from workflow_requestbase where requestid = ?))";
            if (rs.executeQuery(select_data, new Object[]{requestid}) && rs.next()) {
                tablename = weaver.general.Util.null2String(rs.getString(1));
            }
        }

        return tablename;
    }
}
