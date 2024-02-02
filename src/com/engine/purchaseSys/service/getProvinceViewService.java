package com.engine.purchaseSys.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;

import java.util.regex.Pattern;

/**
 * @BelongsProject: WEAVER
 * @BelongsPackage: com.engine.purchaseSys.service
 * @Author: luotianchen
 * @CreateTime: 2023-12-28  21:31
 * @Description: TODO
 * @Version: 1.0
 */

public class getProvinceViewService {
    private  String pageNo;
    private  String pageSize;
    public getProvinceViewService(String pageNo,String pageSize) {
        this.pageNo=pageNo;
        this.pageSize=pageSize;
    }
    public JSONArray execute() {
        JSONArray resarr = new JSONArray();
        BaseBean bb = new BaseBean();
        bb.writeLog("----------------------------------------进入获取所属省份------------------------------------");
        RecordSet rs = new RecordSet();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT S FROM (SELECT S, ROW_NUMBER() OVER (ORDER BY S) AS RowNum FROM UF_SFDQ) AS SubQuery ");
        int pageNums=1;
        int pageSizes=10;
        if (!"".equals(pageNo) && !"".equals(pageSize)&&isNumeric(pageNo)&&isNumeric(pageSize)) {
            pageNums = Integer.parseInt(pageNo);
            pageSizes = Integer.parseInt(pageSize);
        }
        sb.append("WHERE RowNum BETWEEN "+(pageNums-1)*pageSizes+" AND "+pageNums*pageSizes);
        if(rs.execute(sb.toString())){
            int count=rs.getColCounts();
            while(rs.next()){
                JSONObject resobj = new JSONObject();
                resobj.put("S", Util.null2String(rs.getString("S")));
                resarr.add(resobj);
            }
        }else{
            bb.writeLog("sql:"+sb+" 执行失败！");
            return null;
        }
        return resarr;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[0-9]+$");
        if("".equals(str)||str==null){
            return false;
        }
        return pattern.matcher(str).matches();
    }
}
