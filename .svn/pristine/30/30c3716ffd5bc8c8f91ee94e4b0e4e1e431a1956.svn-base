package weaver.interfaces.zkd.dn2023.oa.dao;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.zkd.dn2023.bhl.entity.DataCountBill;
import weaver.interfaces.zkd.dn2023.oa.entity.OADispatchEntity;
import weaver.interfaces.zkd.dn2023.oa.util.BaseDao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * @BelongsProject: ZKD_DA_2023
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.oa.dao
 * @Author: hf_zhangyuan
 * @CreateTime: 2023-03-07  17:55
 * @Description: 获取发文数据类
 * @Version: 1.0
 */

public class DispatchDao {
    public OADispatchEntity getOADispatchEntitys(String tableName,String requestid){
        OADispatchEntity oaDispatchEntity = new OADispatchEntity();
        RecordSet rs = new RecordSet();
        String sql = "select a.requestid,a.cjr,a.isinsert,a.wjmj,a.jjcd,a.gwsx,a.wh,a.wjbt,a.gwzl,a.gwzlpdf,a.zw,a.xgfj,a.zbdw,a.zbdwshld,a.ngr,a.xdr,a.hqy,a.hqe,a.qfr,a.zsdw,a.csdw,a.zwpdfgs,a.hjzw,a.czlx,a.gwzlzxx,a.gwzlpdfzxx,a.cyfw,a.fwrq from "+tableName+" a  where requestid = "+requestid;
        rs.execute(sql);
        if (rs.next()){

            oaDispatchEntity.setRequestid(Util.null2String(rs.getString("requestid")));
            oaDispatchEntity.setCjr(Util.null2String(rs.getString("cjr")));
            oaDispatchEntity.setIsinsert(Util.null2String(rs.getString("isinsert")));
            oaDispatchEntity.setWjmj(Util.null2String(rs.getString("wjmj")));
            oaDispatchEntity.setJjcd(Util.null2String(rs.getString("jjcd")));
            oaDispatchEntity.setGwsx(Util.null2String(rs.getString("gwsx")));
            oaDispatchEntity.setWh(Util.null2String(rs.getString("wh")));
            oaDispatchEntity.setWjbt(Util.null2String(rs.getString("wjbt")));
            oaDispatchEntity.setGwzl(Util.null2String(rs.getString("gwzl")));
            oaDispatchEntity.setGwzlpdf(Util.null2String(rs.getString("gwzlpdf")));
            oaDispatchEntity.setZw(Util.null2String(rs.getString("zw")));
            oaDispatchEntity.setXgfj(Util.null2String(rs.getString("xgfj")));
            oaDispatchEntity.setZbdw(Util.null2String(rs.getString("zbdw")));
            oaDispatchEntity.setZbdwshld(Util.null2String(rs.getString("zbdwshld")));
            oaDispatchEntity.setNgr(Util.null2String(rs.getString("ngr")));
            oaDispatchEntity.setXdr(Util.null2String(rs.getString("xdr")));
            oaDispatchEntity.setHqy(Util.null2String(rs.getString("hqy")));
            oaDispatchEntity.setHqe(Util.null2String(rs.getString("hqe")));
            oaDispatchEntity.setQfr(Util.null2String(rs.getString("qfr")));
            oaDispatchEntity.setZsdw(Util.null2String(rs.getString("zsdw")));
            oaDispatchEntity.setCsdw(Util.null2String(rs.getString("csdw")));
            oaDispatchEntity.setZwpdfgs(Util.null2String(rs.getString("zwpdfgs")));
            oaDispatchEntity.setHjzw(Util.null2String(rs.getString("hjzw")));
            oaDispatchEntity.setCzlx(Util.null2String(rs.getString("czlx")));
            oaDispatchEntity.setGwzlzxx(Util.null2String(rs.getString("gwzlzxx")));
            oaDispatchEntity.setGwzlpdfzxx(Util.null2String(rs.getString("gwzlpdfzxx")));
            oaDispatchEntity.setCyfw(Util.null2String(rs.getString("cyfw")));
            oaDispatchEntity.setFwrq(Util.null2String(rs.getString("fwrq")));

        }
        return oaDispatchEntity;
    }


    public String getDispatchCount() {
        String count="";
        String date=getCurDate();
        String sql="select count(1) zs from uf_datsjl where bm='formtable_main_2' and tsrq=?";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,date);
        if(rs.next()){
            count=rs.getString("zs");
        }
        return count;
    }


    public List<DataCountBill> getDataCountBill(String date){
        List<DataCountBill> countBills = new ArrayList<>();
        RecordSet rs = new RecordSet();
        BaseDao baseDao = new BaseDao();
        String sql="select id,lc,sjzt,sbyy from uf_datsjl where bm='formtable_main_2' and tsrq=?";
        rs.executeQuery(sql,date);
        while(rs.next()){
            DataCountBill countBill = new DataCountBill();
            countBill.setOrigin_id(Util.null2String(rs.getString("id")));
            countBill.setTitle(Util.null2String(baseDao.getRequestName(rs.getString("lc"))));
            int status;
            String msg="";
            if(!"0".equals(rs.getString("sjzt"))&&!"1".equals(rs.getString("sjzt"))){
                status=1;
                msg="推送异常";
            }else{
                status=(rs.getInt("sjzt"));
                msg=Util.null2String(rs.getString("sbyy"));
            }
            countBill.setStatus(status);
            countBill.setFailed_msg(msg);
            countBills.add(countBill);
        }
        return countBills;
    }


    /**
     * 获取当天日期YYYY-MM-DD
     *
     * @return
     * @throws Exception
     */
    public String getCurDate(){
        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return todayStr;
    }
}
