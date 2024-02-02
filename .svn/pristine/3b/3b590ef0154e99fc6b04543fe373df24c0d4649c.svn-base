package com.engine.workflow.cmd.requestList;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.workflow.entity.requestList.ListInfoEntity;
import com.engine.workflow.util.OrderByListUtil;
import weaver.conn.RecordSet;
import weaver.general.PageIdConst;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 待办列表获取默认规则数据
 */
public class GetDefaultListCmd extends AbstractCommonCommand<Map<String,Object>> {

    private HttpServletRequest request;
    @Override
    public BizLogContext getLogContext() {
        return null;
    }
    /**
     * 列表上一些可以个性化的信息， 供个性化使用（后续可继续完善）
     */
    private ListInfoEntity listInfoEntity;

    public GetDefaultListCmd(HttpServletRequest request, User user){
        this.request = request;
        this.user = user;
        this.listInfoEntity = new ListInfoEntity();
    }

    public Map<String, Object> execute(CommandContext commandContext) {

        int userid = this.user.getUID();
        OrderByListUtil obu = new OrderByListUtil(this.user);
        Map<String, Object> maplist = obu.getAllOrderByList(this.request, request.getParameter("dataKey"));
        return maplist;
    }

}
