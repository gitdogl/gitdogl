package com.engine.purchaseSys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.engine.purchaseSys.service.getPurchaseViewService;

/**
 * @BelongsProject: WEAVER
 * @BelongsPackage: com.engine.purchaseSys.web
 * @Author: luotianchen
 * @CreateTime: 2023-12-19  17:25
 * @Description: TODO
 * @Version: 1.0
 */
public class getPurchaseViewAction {
    @POST
    @Path("/getPurchaseView")
    public String getPurchaseView(@Context HttpServletRequest req, @Context HttpServletResponse res){
        String pageNo="1";
        String pageSize="10";
        pageNo=req.getParameter("pageNo");
        pageSize=req.getParameter("pageSize");
        getPurchaseViewService getPurchaseViewService = new getPurchaseViewService(pageNo,pageSize);
        JSONArray resarr=getPurchaseViewService.execute();
        JSONObject resobj = new JSONObject();
        resobj.put("data",resarr);
        return String.valueOf(resobj);
    }
}
