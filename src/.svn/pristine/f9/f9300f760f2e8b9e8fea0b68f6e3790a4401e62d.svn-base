package weaver.interfaces.zkd.dn2023.oa.dao;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.zkd.dn2023.bhl.entity.DataCountBill;
import weaver.interfaces.zkd.dn2023.oa.entity.OAReceiveEntity;
import weaver.interfaces.zkd.dn2023.oa.util.BaseDao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: ZKD_DA_2023
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.oa.dao
 * @Author: hf_zhangyuan
 * @CreateTime: 2023-03-09  17:41
 * @Description: 获取收文数据
 * @Version: 1.0
 */

public class ReceiveDao {
    public OAReceiveEntity getReceiveMes( String tableName,String requestId) {
        OAReceiveEntity oaReceiveEntity=new OAReceiveEntity();
        String sql="select swrq,lwdw,lwwh,gwzl,swbh,jjcd,mj,gwsx,fj,bz,cyfwy,cyfwe,gwzlxx from "+tableName+" where requestid="+requestId;
        RecordSet rs = new RecordSet();
            System.out.println(sql);
         rs.execute(sql);
            if (rs.next()) {
                oaReceiveEntity.setSwrq(rs.getString("swrq"));
                oaReceiveEntity.setLwdw(rs.getString("lwdw"));
                oaReceiveEntity.setLwwh(rs.getString("lwwh"));
                oaReceiveEntity.setGwzl(rs.getString("gwzl"));
                oaReceiveEntity.setSwbh(rs.getString("swbh"));
                oaReceiveEntity.setJjcd(rs.getString("jjcd"));
                oaReceiveEntity.setMj(rs.getString("mj"));
                oaReceiveEntity.setGwsx(rs.getString("gwsx"));
                oaReceiveEntity.setFj(rs.getString("fj"));
                oaReceiveEntity.setBz(rs.getString("bz"));
                oaReceiveEntity.setCyfwy(rs.getString("cyfwy"));
                oaReceiveEntity.setCyfwe(rs.getString("cyfwe"));
                oaReceiveEntity.setGwzlxx(rs.getString("gwzlxx"));
            }

        return oaReceiveEntity;
    }


    public String getReceiveCount() {
        String count="";
        String date=getCurDate();
        String sql="select count(1) zs from uf_datsjl where bm='formtable_main_3' and tsrq=?";
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
        String sql="select id,lc,sjzt,sbyy from uf_datsjl where bm='formtable_main_3' and tsrq=?";
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
