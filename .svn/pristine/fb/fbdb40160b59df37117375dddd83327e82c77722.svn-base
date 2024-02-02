package weaver.ofs.interfaces;

import com.alibaba.fastjson.JSONObject;
import com.api.formmode.page.util.Util;
import com.weaver.general.BaseBean;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.ofs.interfaces.SendRequestStatusDataInterfaces;
import weaver.ofs.interfaces.TestSendImpl;
import weaver.workflow.request.todo.DataObj;
import weaver.workflow.request.todo.RequestStatusObj;
import net.sf.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;

import weaver.workflow.webservices.*;

/**
 * 本示例，只将接受的数据打印在log/ecology中，具体的接口调用操作，请在相关数据遍历中执行。
 * 实现weaver.ofs.interfaces.SendRequestStatusDataInterfaces接口中SendRequestStatusData
 */
public class sendTodoListRest implements SendRequestStatusDataInterfaces {

    private static final Log log = LogFactory.getLog(TestSendImpl.class);

    /**
     * 后台设置id
     */
    public String id ;
    /**
     * 设置的系统编号
     */
    public String syscode ;
    /**
     * 服务器URL
     */
    public String serverurl ;
    /**
     * 流程白名单
     */
    public ArrayList<String> workflowwhitelist ;
    /**
     * 人员白名单
     */
    public ArrayList<String> userwhitelist ;


    public String getId() {
        return id;
    }
    public String getSyscode() {
        return syscode;
    }
    public String getServerurl() {
        return serverurl;
    }
    public ArrayList<String> getWorkflowwhitelist() {
        return workflowwhitelist;
    }
    public ArrayList<String> getUserwhitelist() {
        return userwhitelist;
    }

    /**
     * 实现消息推送的具体方法
     * @param datas 传入的请求数据对象数据集
     */
    public void SendRequestStatusData(ArrayList<DataObj> datas) {

        for(DataObj dobj : datas){
            ArrayList<RequestStatusObj> donedatas = dobj.getDonedatas();
            if(donedatas.size()>0) {//处理推送的已办数据
                for (RequestStatusObj rso : donedatas) {
                    RecordSet rs = new RecordSet();
                    String currworkcode="";
                    rs.executeQuery("select workcode from hrmresource where id='"+Util.null2String(rso.getUser().getUID())+"'");
                    if (rs.next()) {currworkcode=rs.getString("workcode");}
                    JSONObject ybobj = new JSONObject();
                    ybobj.put("itemID", rso.getRequestid()+currworkcode);
                    ybobj.put("systemId", "064c65f2-04e6-32d5-3655-258a00ae50ae");
                    String res = doPost("http://10.1.18.121:81/api/esb/execute/64caed2d-ab47-4116-b1be-6caec02a2fa1/SCDB", JSONObject.toJSONString(ybobj));
                }
            }
            ArrayList<RequestStatusObj> deldatas = dobj.getDeldatas();
            if(deldatas.size()>0){//处理推送的删除数据
                for (RequestStatusObj rso : deldatas) {
                    RecordSet rs = new RecordSet();
                    String currworkcode="";
                    rs.executeQuery("select workcode from hrmresource where id='"+Util.null2String(rso.getUser().getUID())+"'");
                    if (rs.next()) {currworkcode=rs.getString("workcode");}
                    JSONObject scobj = new JSONObject();
                    scobj.put("itemID", rso.getRequestid()+currworkcode);
                    scobj.put("systemId", "064c65f2-04e6-32d5-3655-258a00ae50ae");
                    String res = doPost("http://10.1.18.121:81/api/esb/execute/64caed2d-ab47-4116-b1be-6caec02a2fa1/SCDB", JSONObject.toJSONString(scobj));
                }
            }
            ArrayList<RequestStatusObj> tododatas = dobj.getTododatas();
            if(tododatas.size()>0){//处理推送的待办数据
                for(RequestStatusObj rso : tododatas){//遍历当前发送的待办数据
                    RecordSet rs = new RecordSet();
                    String workcode="";
                    String lastname="";
                    String currworkcode="";
                    rs.executeQuery("select workcode,lastname from hrmresource where id='"+Util.null2String(rso.getCreatorid())+"'");
                    if(rs.next()){workcode=rs.getString("workcode");lastname=rs.getString("lastname");}
                    rs.executeQuery("select workcode from hrmresource where id='"+Util.null2String(rso.getUser().getUID())+"'");
                    if (rs.next()) {currworkcode=rs.getString("workcode");}
                    JSONObject tydbobj=new JSONObject();
                    tydbobj.put("systemID","064c65f2-04e6-32d5-3655-258a00ae50ae");
                    tydbobj.put("systemName","YWKSDJ");
                    tydbobj.put("itemType","48");
                    tydbobj.put("itemID",Util.null2String(rso.getRequestid())+currworkcode);
                    tydbobj.put("itemName",Util.null2String(rso.getRequestnamenew()));
                    tydbobj.put("activityName",rso.getNodename());
                    tydbobj.put("startedDateTime",todate(rso.getSendTime()));
                    tydbobj.put("receiveTime",todate(rso.getSendTime()));
                    tydbobj.put("creator",workcode);
                    tydbobj.put("creatorName",lastname);
                    tydbobj.put("sourceUserID",currworkcode);
                    tydbobj.put("state","0");
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                    String formattedDate = currentDate.format(formatter);
                    BaseBean bb = new BaseBean();
                    String ip=bb.getPropValue("toSSOConf","ip");
                    String gopage="/interface/loginssoresultdb.jsp%3Fstate%3D3|workcode%3D"+formattedDate+currworkcode+"|requestid%3D"+rso.getRequestid();
                    try {
                        gopage = URLEncoder.encode(gopage, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    tydbobj.put("itemUrl",ip+"/SSOLogin.aspx?toSystemID=064c65f2-04e6-32d5-3655-258a00ae50ae&gopage="+gopage);
                    tydbobj.put("secLevel","0");
                    tydbobj.put("description",ip+"/SSOLogin.aspx?toSystemID=064c65f2-04e6-32d5-3655-258a00ae50ae&gopage="+gopage);
//                    tydbobj.put("description2","http://10.1.9.43/SSOLogin.aspx?toSystemID=064c65f2-04e6-32d5-3655-258a00ae50ae&gopage=/interface/loginssoresultdb.jsp%3Fstate%3D4|workcode%3D"+formattedDate+currworkcode);
//                    tydbobj.put("attr_1","http://10.1.9.43/SSOLogin.aspx?toSystemID=064c65f2-04e6-32d5-3655-258a00ae50ae&gopage=/interface/loginssoresultdb.jsp%3Fstate%3D4|workcode%3D"+formattedDate+currworkcode);
//                    tydbobj.put("attr_2","http://10.1.9.43/SSOLogin.aspx?toSystemID=064c65f2-04e6-32d5-3655-258a00ae50ae&gopage=/interface/loginssoresultdb.jsp%3Fstate%3D4|workcode%3D"+formattedDate+currworkcode);
//                    tydbobj.put("attr_3","http://10.1.9.43/SSOLogin.aspx?toSystemID=064c65f2-04e6-32d5-3655-258a00ae50ae&gopage=/interface/loginssoresultdb.jsp%3Fstate%3D4|workcode%3D"+formattedDate+currworkcode);
                    log.error("统一待办的JSON数据为：\n"+formatJson(tydbobj.toString()));
                    log.error("Cid的值为"+rso.getCid());
                    String res=doPost("http://10.1.18.121:81/api/esb/execute/64caed2d-ab47-4116-b1be-6caec02a2fa1/TSDB",JSONObject.toJSONString(tydbobj));
                    log.error("统一待办的返回数据为：\n"+res);
                }
            }
        }

    }

    /**
     * 格式化JSON格式输出
     *
     * @param jsonStr
     * @return 返回指定格式化的数据
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr))
            return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        boolean isInQuotationMarks = false;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '"':
                    if (last != '\\'){
                        isInQuotationMarks = !isInQuotationMarks;
                    }
                    sb.append(current);
                    break;
                case '{':
                case '[':
                    sb.append(current);
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent++;
                        addIndentBlank(sb, indent);
                    }
                    break;
                case '}':
                case ']':

                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent--;
                        addIndentBlank(sb, indent);
                    }
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 添加space(缩进)
     * @param sb
     * @param indent
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

    public String todate(String timestamp){
        Instant instant = Instant.ofEpochMilli(Long.parseLong(timestamp));
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = zonedDateTime.format(formatter);
        return formattedDate;
    }

    public String doPost(String requestUrl, String param) {
        InputStream inputStream = null;
        try {
            HttpClient httpClient = new HttpClient();
            PostMethod postMethod = new PostMethod(requestUrl);
            // 设置请求头  Content-Type
            postMethod.setRequestHeader("Content-Type", "application/json");
            //Base64加密方式认证方式下的basic auth
            //          postMethod.setRequestHeader("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString(("OA2POD:Sap12345").getBytes()));
            RequestEntity requestEntity = new StringRequestEntity(param, "application/json", "UTF-8");
            postMethod.setRequestEntity(requestEntity);
            httpClient.executeMethod(postMethod);// 执行请求
            inputStream = postMethod.getResponseBodyAsStream();// 获取返回的流
            BufferedReader br = null;
            StringBuffer buffer = new StringBuffer();
            // 将返回的输入流转换成字符串
            br = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
            }
            System.out.print("接口返回内容为:" + buffer);
            return buffer.toString();
        } catch (Exception e) {
            System.out.print("请求异常" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}