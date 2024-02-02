package com.engine.purchaseSys.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.engine.purchaseSys.service.getProvinceViewService;
import com.engine.purchaseSys.service.getPurchaseViewService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

/**
 * @BelongsProject: WEAVER
 * @BelongsPackage: com.engine.purchaseSys.web
 * @Author: luotianchen
 * @CreateTime: 2023-12-28  21:32
 * @Description: TODO
 * @Version: 1.0
 */

public class getProvinceViewAction {
    @POST
    @Path("/getProvinceView")
    public String getProvinceView(@Context HttpServletRequest req, @Context HttpServletResponse res){
        String pageNo="1";
        String pageSize="10";
        pageNo=req.getParameter("pageNo");
        pageSize=req.getParameter("pageSize");
        getProvinceViewService getProvinceViewService = new getProvinceViewService(pageNo,pageSize);
        JSONArray resarr=getProvinceViewService.execute();
        JSONObject resobj = new JSONObject();
        resobj.put("data",resarr);
        return String.valueOf(resobj);
    }
}
