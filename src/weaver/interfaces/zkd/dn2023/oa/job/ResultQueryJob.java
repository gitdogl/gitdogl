package weaver.interfaces.zkd.dn2023.oa.job;

import weaver.conn.RecordSet;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.zkd.dn2023.bhl.serivce.DatePushService;

import java.util.Map;

/**
 * @BelongsProject: ZKD_DA_2023
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.oa.job
 * @Author: hf_zhangyuan
 * @CreateTime: 2023-04-03  14:23
 * @Description: 档案归档结果查询
 * @Version: 1.0
 */

public class ResultQueryJob extends BaseCronJob {

    @Override
    public void execute() {
        DatePushService datePushService = new DatePushService();
        RecordSet rs = new RecordSet();
        RecordSet rs2 = new RecordSet();
        String sql = "select lc from uf_datsjl where sjzt = 0";
        rs.execute(sql);
        while (rs.next()){
            String lc = rs.getString("lc");
            String tableName = rs.getString("bm");
            String type = "";
            if (tableName.equals("formtable_main_2")){
                type = "1";
            }else if(tableName.equals("formtable_main_3")) {
                type = "2";
            }else{
                type = "3";
            }
            Map<String,String> map =  datePushService.getQuery(type,lc);
            if (!map.get("code").equals("0")){
                String upsql = "update uf_datsjl set sjzt = 2 where lc = ? ";
                rs2.executeUpdate(upsql,lc);
            }else{
                String upsql = "update uf_datsjl set sjzt = 3,sbyy = ? where lc = ? ";
                rs2.executeUpdate(upsql,map.get("msg"),lc);
            }

        }

    }



}
