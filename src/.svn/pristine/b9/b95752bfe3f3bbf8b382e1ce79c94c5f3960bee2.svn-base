//package com.engine.customization.demo.service.impl;
//
//import com.alibaba.fastjson.JSONObject;
//import com.engine.common.util.ParamUtil;
//import com.engine.workflow.constant.menu.SystemMenuType;
//import com.engine.workflow.constant.requestForm.RequestMenuType;
//import com.engine.workflow.entity.requestForm.RightMenu;
//import com.sun.tools.corba.se.idl.constExpr.Equal;
//import com.weaver.general.BaseBean;
//import com.weaverboot.frame.ioc.anno.classAnno.WeaIocReplaceComponent;
//import com.weaverboot.frame.ioc.anno.methodAnno.WeaReplaceAfter;
//import com.weaverboot.frame.ioc.anno.methodAnno.WeaReplaceBefore;
//import com.weaverboot.frame.ioc.handler.replace.weaReplaceParam.impl.WeaAfterReplaceParam;
//import com.weaverboot.frame.ioc.handler.replace.weaReplaceParam.impl.WeaBeforeReplaceParam;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpRequest;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import weaver.conn.RecordSet;
//import weaver.hrm.HrmUserVarify;
//import weaver.hrm.User;
//import weaver.systeminfo.SystemEnv;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.util.*;
//
///**
// * <Description> <br>
// * @author han.mengyu <br>
// * @version 1.0 <br>
// * @createDate 2021/12/29 <br>
// * @see com.engine.customization.demo <br>
// */
//@WeaIocReplaceComponent("submitRequestService") //如不标注名称，则按类的全路径注入
//public class doSubmitRequestCmd {
////    private static final Logger LOGGER = LoggerFactory.getLogger("customlog");
//
//    //这是接口前置方法，这个方法会在接口执行前执行
//    //前值方法必须用@WeaReplaceBefore,这里面有两个参数，第一个叫value，是你的api地址
//    //第二个参数叫order，如果你有很多方法拦截的是一个api，那么这个就决定了执行顺序
//    //前置方法的参数为WeaBeforeReplaceParam 这个类，里面有四个参数，request，response，请求参数的map，api的地址
////    @WeaReplaceBefore(value = "/api/workflow/paService/submitRequest",order = 1,description = "submitRequest接口前置方法")
////    public void rightMenuBefore(WeaBeforeReplaceParam beforeReplaceParam) throws IOException {
////        HttpServletRequest request = beforeReplaceParam.getRequest();
////        StringBuilder buffer = new StringBuilder();
////        BufferedReader reader = request.getReader();
////        String line;
////        while ((line = reader.readLine()) != null) {
////            buffer.append(line);
////        }
////        String requestBody = buffer.toString();
////        System.out.println("---------------------------");
////        System.out.println("测试拦截提交方法");
////        System.out.println("--------> get request json is :" + requestBody);
////        System.out.println("---------------------------");
////
////        JSONObject reqobj = JSONObject.parseObject(requestBody);
////        Map<String, Object> reqmap = new HashMap<>(reqobj);
////        System.out.println("获取到的map是" + reqmap);
////        String requestId = (String) reqmap.get("requestId");
////        RecordSet rs = new RecordSet();
////        String sql = "select workflowid from workflow_requestbase where requestid=?";
////        rs.executeQuery(sql, requestId);
////        if (rs.next()) {
////            String workflowid = rs.getString("workflowid");
////            System.out.println(workflowid);
////            if ("960".equals(workflowid)) {
////                request.setAttribute("requestId", requestId);
////                beforeReplaceParam.setRequest(request);
////            }
////        }
////    }
//
//
//
//    //这个是接口后置方法，大概的用法跟前置方法差不多，稍有差别
//    //注解名称为WeaReplaceAfter
//    //返回类型必须为String
//    //参数叫WeaAfterReplaceParam，这个类前四个参数跟前置方法的那个相同，不同的是多了一个叫data的String，这个是那个接口执行完返回的报文
//    //你可以对那个报文进行操作，然后在这个方法里return回去
//    @WeaReplaceAfter(value = "/api/workflow/paService/submitRequest",order = 1,description = "rightMenu接口ioc方式添加按钮")
//    public String submitAfter(WeaAfterReplaceParam weaAfterReplaceParam) throws IOException {
//        HttpServletRequest request=weaAfterReplaceParam.getRequest();
//        StringBuilder buffer = new StringBuilder();
//        BufferedReader reader = request.getReader();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            buffer.append(line);
//        }
//        String requestBody = buffer.toString();
//
//        System.out.println("---------------------------");
//        System.out.println("测试拦截提交方法");
//        System.out.println("--------> get request json is :" + requestBody);
//        System.out.println("---------------------------");
//
//
//        JSONObject reqobj = JSONObject.parseObject(requestBody);
//        Map<String, Object> reqmap = new HashMap<>(reqobj);
//        System.out.println("获取到的map是" + reqmap);
//        String result="";
//        String requestId = (String) reqmap.get("requestId");
//        RecordSet rs = new RecordSet();
//        String sql = "select workflowid from workflow_requestbase where requestid=?";
//        rs.executeQuery(sql, requestId);
//        if (rs.next()) {
//            String workflowid = rs.getString("workflowid");
//            System.out.println(workflowid);
//            BaseBean bb = new BaseBean();
//            String curworkflowid=bb.getPropValue("workflowprop","workflowid");
//            System.out.println("workflowid的数据是"+workflowid);
//            if (curworkflowid.equals(workflowid)) {
//                Map<String, String> curreqmap = new HashMap<>();
//                curreqmap.put("requestId",requestId);
//                String url=weaAfterReplaceParam.getApiUrl();
//                if(!"".equals(reqmap.get("mainData"))||reqmap.get("mainData")!=null) {
//                    String maindata = (String) reqmap.get("mainData");
//                    curreqmap.put("mainData",maindata);
//                }
//                if(!"".equals(reqmap.get("detailData"))||reqmap.get("detailData")!=null) {
//                    String detailData = (String) reqmap.get("detailData");
//                    curreqmap.put("detailData",detailData);
//                }
//                if(!"".equals(reqmap.get("otherParams"))||reqmap.get("otherParams")!=null) {
//                    curreqmap.put("otherParams",(String) reqmap.get("otherParams"));
//                }
//                if(!"".equals(reqmap.get("remark"))||reqmap.get("remark")!=null) {
//                    curreqmap.put("remark",(String) reqmap.get("remark"));
//                }
//
//
//                String appid=request.getHeader("appid");
//                String token=request.getHeader("token");
//                String userid=request.getHeader("userid");
//                Map<String, String> curreqheadmap = new HashMap<>();
//                curreqheadmap.put("appid",appid);
//                curreqheadmap.put("token",token);
//                curreqheadmap.put("userid",userid);
//                System.out.println("curreqmap的数据是"+curreqmap);
//                System.out.println("curreqheadmap的数据是"+curreqheadmap);
//                 result=httpPostForm(url,curreqmap,curreqheadmap,null);
//                System.out.println("最终的result"+result);
//            }
//        }
//
//
//        return JSONObject.toJSONString(result);
//    }
//
//
//    public static String httpPostForm(String url, Map<String, String> params, Map<String, String> headers, String encode) {
//
//        if (encode == null) {
//            encode = "utf-8";
//        }
//
//        String content = null;
//        CloseableHttpResponse httpResponse = null;
//        CloseableHttpClient closeableHttpClient = null;
//        try {
//
//            closeableHttpClient = HttpClients.createDefault();
//            HttpPost httpost = new HttpPost(url);
//
//            //设置header
//            if (headers != null && headers.size() > 0) {
//                for (Map.Entry<String, String> entry : headers.entrySet()) {
//                    httpost.setHeader(entry.getKey(), entry.getValue());
//                }
//            }
//            //组织请求参数
//            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
//            if (params != null && params.size() > 0) {
//                Set<String> keySet = params.keySet();
//                for (String key : keySet) {
//                    paramList.add(new BasicNameValuePair(key, params.get(key)));
//                }
//            }
//            httpost.setEntity(new UrlEncodedFormEntity(paramList, encode));
//
//
//            httpResponse = closeableHttpClient.execute(httpost);
//            HttpEntity entity = httpResponse.getEntity();
//            content = EntityUtils.toString(entity, encode);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                httpResponse.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        try {  //关闭连接、释放资源
//            closeableHttpClient.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return content;
//    }
//}
//
//
