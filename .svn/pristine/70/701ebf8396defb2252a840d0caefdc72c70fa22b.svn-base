package com.engine.workflow.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloudstore.dev.api.bean.SplitPageBean;
import com.engine.workflow.constant.PageUidConst;
import com.engine.workflow.util.GetCustomLevelUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.User;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 获取默认排序设置
 */
public class OrderByListUtil {
    private User user;

    public OrderByListUtil(User user) {
        this.user = user;
    }

    /**
     * 获取默认设置
     *
     * @param request
     * @param dataKey
     * @return
     */
    public Map<String, Object> getAllOrderByList(HttpServletRequest request, String dataKey) {
        Map<String, Object> maplist = new HashMap<String, Object>();
        int uid = this.user.getUID();
        List<Map<String, Object>> sysdata = new ArrayList<Map<String, Object>>();

        try {

            SplitPageBean bean = new SplitPageBean(request, dataKey, "RootMap", "head");
            String pageUid = "";
            if (null != bean.getRootMap()) {
                pageUid = Util.null2String(bean.getRootMap().getString("pageUid"));
            }
            if (StringUtils.isBlank(pageUid)) {
                maplist.put("errMsg", "pageUid is null");
                return maplist;
            }

            //List<UserDefCol> userDefCols =  new Dao_TableSqlServer().getUserDefColumns(pageUid, uid);//读取数据库数据
            //TODO 需要合并这两条数据
            JSONArray unchoosedColumns = bean.getUnchoosedColumns();//获取tabString中未展示的数据
            JSONArray choosedColumns = bean.getChoosedColumns(); //tabString中展示的数据集合

            //第一步：需要把tabString中的数据整合  **这里不需要考虑数据重复问题  准备好所有的数据
            Map<String, JSONObject> allcols = Maps.newHashMap();
            int countnum = 0;
            for (int i = 0; i < unchoosedColumns.size(); i++) {
                JSONObject col = (JSONObject) unchoosedColumns.get(i);
                if (col.get("orderkey") == null || "".equals(col.get("orderkey"))) continue;
                col.put("orders", countnum++);
                col.put("orderType", "0");//默认倒序
                col.put("name", col.get("title"));//对应名称
                col.put("id", col.get("dataIndex"));//对应id
                allcols.put(col.getString("dataIndex"), col);
            }
            for (int i = 0; i < choosedColumns.size(); i++) {
                JSONObject col = (JSONObject) choosedColumns.get(i);
                if (col.get("orderkey") == null || "".equals(col.get("orderkey"))) continue;
                col.put("orders", countnum++);
                col.put("orderType", "0");//默认倒序
                col.put("name", col.get("title"));//对应名称
                col.put("id", col.get("dataIndex"));//对应id
                allcols.put(col.getString("dataIndex"), col);
            }

            //第二步：获取用户保存的数据，正序倒序
            List<Map<String, Object>> list = getSettingOrderList(uid, pageUid);

            //第三步：修改判断哪些数据是用户设置了的
            String selectedKeys = "";
            for (Map<String, Object> map : list) {
                selectedKeys += "," + map.get("columnkey");//待修改
                String columnkey = map.get("columnkey").toString();
                String ordertype = map.get("ordertype").toString();
                if (!allcols.containsKey(columnkey)) continue;//数据不包含，跳过
                JSONObject col = allcols.get(columnkey);
                col.put("orderType", ordertype);//更新ordertype，用户选择的otdertype
                allcols.put(columnkey, col);
            }

            //第五步：选中数据修剪
            selectedKeys = selectedKeys.length() > 0 ? selectedKeys.replaceFirst(",", "") : selectedKeys;

            //第四步:数据组装  剔除特殊数据也在这里
            for (Map.Entry<String, JSONObject> ent : allcols.entrySet()) {
                JSONObject col = ent.getValue();
                if ("".equals(selectedKeys.trim()) && ("receivedate".equals(ent.getKey()) || "requestlevel".equals(ent.getKey()))) {
                    col.put("orderType", "0");
                }
                sysdata.add(ent.getValue());
            }

            if ("".equals(selectedKeys)) {
                selectedKeys = "requestlevel,receivedate";
            }
            maplist.put("iniddata", sysdata);
            maplist.put("selectedKeys", selectedKeys.split(","));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maplist;
    }

    /**
     * 获取选中的数据集合
     *
     * @return
     */
    public List<Map<String, Object>> getSettingOrderList(int userid, String pageid) {
        String sql = "select id,columnkey, ordertype, sortorder, orderkey from user_defaultorder_setting " +
                "where  pageid=? and userid = ? order by sortorder";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql, pageid, userid);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        Set<String> set = new HashSet<>();//用来第一次去重
        String columnkey = "";
        while (rs.next()) {
            map = new HashMap<String, Object>();
            columnkey = Util.null2String(rs.getString("columnkey")).trim();
            if (set.contains(columnkey.toLowerCase())) continue;//不区分大小写
            set.add(columnkey.toLowerCase());
            map.put("id", Util.null2String(rs.getString("id")));
            map.put("columnkey", columnkey);
            map.put("ordertype", Util.null2String(rs.getString("ordertype")));
            map.put("sortorder", Util.null2String(rs.getString("sortorder")));
            map.put("orderkey", Util.null2String(rs.getString("orderkey")));
            list.add(map);
        }
        return list;
    }

    public List<Map<String, Object>> getSettingOrderList(String pageid) {
        return getSettingOrderList(this.user.getUID(), pageid);
    }

    public static String appendRequestIdOrderBy(String orderBy) {
        return appendRequestIdOrderBy(orderBy,"");
    }

    /**
     * 添加requestid排序
     * @param orderBy
     * @return
     */
    public static String appendRequestIdOrderBy(String orderBy,String tableAlias) {
        orderBy = Util.null2String(orderBy);
        String appendRequestid = "".equals(tableAlias)?"requestid":tableAlias+".requestid";
        if(orderBy.toLowerCase().indexOf("requestid") == -1) {
            if(orderBy.trim().length() > 0) {
                orderBy += " ,"+appendRequestid+" desc ";
            } else {
                orderBy += " "+appendRequestid+" desc ";
            }
        }
        return orderBy;
    }


    /**
     * 获取排序规则
     * @param userid
     * @param pageid
     * @return
     */
    public String getMyOrderByStr(int userid, String pageid) {
        List<Map<String, Object>> list = getSettingOrderList(userid, pageid);
        StringBuilder myorder = new StringBuilder();
        Map<String, String> orderMap = new LinkedHashMap<String, String>();
        for (Map<String, Object> map : list) {//拼装排序规则
            String ordertype = map.get("ordertype").toString(),
                    columnkey = Util.null2String(map.get("columnkey")).toLowerCase(),
                    orderkey = Util.null2String(map.get("orderkey"));
            columnkey = "requestlevel".equalsIgnoreCase(columnkey) ? " requestlevelorder " : columnkey;//特殊处理紧急程度

            String[] arr = orderkey.split(",", -1);
            for (String orderStr : arr) {
                orderStr = Util.null2String(orderStr).trim().toLowerCase();
                if (!"".equals(orderStr)) {
                    if (!orderMap.containsKey(orderStr)) {
                        orderMap.put(orderStr, ordertype);
                    }
                }
            }
        }

        for (Iterator<String> iter = orderMap.keySet().iterator(); iter.hasNext(); ) {
            String key = iter.next();
            String value = orderMap.get(key);
            String orderValue = "0".equals(value) ? "desc " : "asc ";
            myorder.append("," + key + " " + orderValue);
        }

//        myorder.append(getOrderByDetail(orderkey, ordertype));
        //已经存在接收日志，则不单独处理
        if ("".equals(myorder.toString().trim())) {
            insetDefatltOrder(pageid);//如果没有任何排序则 强制插入默认排序数据
            myorder.append(", receivedate desc, receivetime desc");//添加必须要的排序，放在最后接收日期以及接收时间
        }
        String orders = myorder.toString().trim();

        while(orders.startsWith(",")) {//把第一个‘，’修剪掉
            orders = orders.substring(1).trim();
        }

        if(PageUidConst.WF_LIST_DOING.equals(pageid) && "".equals(orders)) {//如果为空
            orders = " receivedate desc, receivetime desc";
        }


        return  orders;
    }

    //插入默认的数据
    public boolean insetDefatltOrder(String pageUid) {
        String insert_sql = "insert into user_defaultorder_setting(userid, usertype, columnkey, ordertype, sortorder, pageid, orderkey) " +
                " values(?,?,?,?, ?,?,?)";
        RecordSet rs = new RecordSet();
        int ordernumber = 0;
        try {
            rs.executeUpdate(insert_sql, this.user.getUID(), this.user.getType(), "receivedate", '0', ordernumber++, pageUid, "receivedate,receivetime");
//            rs.executeUpdate(insert_sql,this.user.getUID() ,this.user.getType(), "requestlevel" , '0', ordernumber++,pageUid, "requestlevelorder");
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    private boolean isContaintText(String order, String text) {
        if (order == null || "".equals(order.trim())) {
            return false;
        }
        if (text == null || "".equals(text.trim())) {
            return false;
        }
        if (order.indexOf(text.trim()) != -1) {
            return true;
        }
        return false;
    }

    public String getOrderByDetail(String orderkey, String ordertype) {
        String[] strs = orderkey.split(",");
        String orderby = "";
        for (String str : strs) {
            orderby += "1".equals(ordertype) ? ", " + str + " asc" : ", " + str + " desc";
        }
        return orderby;
    }

    /**
     * 使用case...when的方式进行设置orderby
     * @return
     */
    public String getOrderByFrom(int language) {
        List<Map<String, Object>> list = GetCustomLevelUtil.getAllLevel(null, language);

        StringBuffer sb = new StringBuffer(" (case requestlevel  ");
        StringBuffer sb1 = new StringBuffer("");
        for (Map<String, Object> map : list) {
            sb1.append(" when " + map.get("id") + " then " + map.get("showorder"));
        }
        if ("".equals(sb1.toString().trim())) {//判断有无数据没有数据则不拼接
            return "";
        }
        sb.append(sb1);
        sb.append(" else -1 end ) as requestlevelorder, ");
        return sb.toString();
    }

}
