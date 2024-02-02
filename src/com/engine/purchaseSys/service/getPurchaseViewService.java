package com.engine.purchaseSys.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.kernel.pdf.colorspace.PdfSpecialCs;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;

import java.util.regex.Pattern;

/**
 * @BelongsProject: WEAVER
 * @BelongsPackage: com.engine.purchaseSys.service
 * @Author: luotianchen
 * @CreateTime: 2023-12-19  17:25
 * @Description: TODO
 * @Version: 1.0
 */

public class getPurchaseViewService {
    private  String pageNo;
    private  String pageSize;
    public getPurchaseViewService(String pageNo,String pageSize) {
        this.pageNo=pageNo;
        this.pageSize=pageSize;
    }
    public JSONArray execute() {
        JSONArray resarr = new JSONArray();
        BaseBean bb = new BaseBean();
        bb.writeLog("----------------------------------------进入获取采购信息------------------------------------");
        RecordSetDataSource rs = new RecordSetDataSource("CGXT");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT 唯一标识, 物资名称, 采购数量, 预算金额, 预算币种, 采购金额, 采购币种, 指标卡号, 联系人, 联系方式, 拟采用方式, 申报单位, 申报人工资号, 申报人姓名, 外贸代理机构, 采购编号, 供应商, 生产商, 产地, 规格型号, 通过日期 FROM (SELECT 唯一标识, 物资名称, 采购数量, 预算金额, 预算币种, 采购金额, 采购币种, 指标卡号, 联系人, 联系方式, 拟采用方式, 申报单位, 申报人工资号, 申报人姓名, 外贸代理机构, 采购编号, 供应商, 生产商, 产地, 规格型号, 通过日期, ROW_NUMBER() OVER (ORDER BY 唯一标识) AS RowNum FROM PurchaseView) AS SubQuery ");
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
                    resobj.put("id",Util.null2String(rs.getString("唯一标识")));
                    resobj.put("wzmc",Util.null2String(rs.getString("物资名称")));
                    resobj.put("cgsl",Util.null2String(rs.getString("采购数量")));
                    resobj.put("ysje",Util.null2String(rs.getString("预算金额")));
                    resobj.put("ysbz",Util.null2String(rs.getString("预算币种")));
                    resobj.put("cgje",Util.null2String(rs.getString("采购金额")));
                    resobj.put("cgbz",Util.null2String(rs.getString("采购币种")));
                    resobj.put("zbkh",Util.null2String(rs.getString("指标卡号")));
                    resobj.put("lxr",Util.null2String(rs.getString("联系人")));
                    resobj.put("lxfs",Util.null2String(rs.getString("联系方式")));
                    resobj.put("ncyfs",Util.null2String(rs.getString("拟采用方式")));
                    resobj.put("sbdw",Util.null2String(rs.getString("申报单位")));
                    resobj.put("sbrgzh",Util.null2String(rs.getString("申报人工资号")));
                    resobj.put("sbrxm",Util.null2String(rs.getString("申报人姓名")));
                    resobj.put("wmdljg",Util.null2String(rs.getString("外贸代理机构")));
                    resobj.put("cgbh",Util.null2String(rs.getString("采购编号")));
                    resobj.put("gys",Util.null2String(rs.getString("供应商")));
                    resobj.put("scs",Util.null2String(rs.getString("生产商")));
                    resobj.put("cd",Util.null2String(rs.getString("产地")));
                    resobj.put("ggxh",Util.null2String(rs.getString("规格型号")));
                    resobj.put("tgrq",Util.null2String(rs.getString("通过日期")));
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
