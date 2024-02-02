//package com.weaver.esb.package_20240106060844;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import org.mapdb.Atomic;
//import weaver.conn.RecordSet;
//import weaver.general.Util;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.temporal.ChronoUnit;
//
//import java.util.*;
//
//public class class_20240106060844 {
//String error="";
//    /**
//     * @param:  param(Map collections)
//     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
//     */
//    public Map execute(Map<String,Object> params) {
//        String item = Util.null2String(params.get("item"));
//        JSONArray itemarr = JSONArray.parseArray(item);
//        for(int i=0;i<itemarr.size();i++){
//            JSONObject itemobj=itemarr.getJSONObject(i);
//            String jd=itemobj.getString("jd");//据点
//            String gkdm=itemobj.getString("gkdm");//顾客代码
//            String cqff=itemobj.getString("cqff");//偿却方法
//            String dqfs=itemobj.getString("dqfs");//打切方式
//            String djllspf=itemobj.getString("djllspf");//单价联络书品番
//            String ks=itemobj.getString("ks");//开始日期
//            String js=itemobj.getString("js");//结束日期
//            double whsje=itemobj.getDouble("whsje");//未回收金额
//            double dqje=itemobj.getDouble("dqje");//打切金额
//            List<String> qjmxids = new ArrayList<>();//期间明细id
//            String lastMonth="";//模具费明细id
//            //常规打切
//            if("2".equals(cqff)&&"2".equals(dqfs)&&whsje>0){
//                lastMonth=getmjlastMonth(djllspf,jd,gkdm);
//                updateInvalid(djllspf,lastMonth);
//                qjmxids=getysmxid(djllspf,jd,gkdm);
//                //获取到打切金额以后平均分配给qjmxids里面的每一个数据，如果有余数放到最后一个数据上面
//                double averageAmount = dqje / qjmxids.size();
//                double remainder = dqje % qjmxids.size();
//                for (int j = 0; j < qjmxids.size(); j++) {
//                    if (j == qjmxids.size()package weaver.interfaces.workflow.action.javacode;
//
//import com.alibaba.fastjson.JSONObject;
//import weaver.interfaces.workflow.action.Action;
//import weaver.general.BaseBean;
//import weaver.general.BaseBean;
//import weaver.soa.workflow.request.RequestInfo;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.methods.RequestEntity;
//import org.apache.commons.httpclient.methods.StringRequestEntity;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
///**
// * 在线自定义action接口
// */
//public class Action20240110061652 extends BaseBean implements Action{
//    /**
//     * 流程路径节点后选择aciton后,会在节点提交后执行此方法。
//     */
//    public String execute(RequestInfo request) {
//        //   JSONObject data = new JSONObject();
//        //   data.put("contractId","123");
//        //  data.put("advice","同意");
//        //   data.put("checkCode","123123123");
//        //   String param= JSONObject.toJSONString(data);
//        //   writeLog(param);
//        //  String result=doPost("http://cggl.ustc.edu.cn/sfw2/e?page=contract.purchase.open.approve&window_=json",param);
//        writeLog("result=123");
//        return Action.SUCCESS;
//    }
//
////    public String doPost(String requestUrl, String param) {
////        InputStream inputStream = null;
////        try {
////            HttpClient httpClient = new HttpClient();
////            PostMethod postMethod = new PostMethod(requestUrl);
////            // 设置请求头  Content-Type
////            postMethod.setRequestHeader("Content-Type", "application/json");
////            //Base64加密方式认证方式下的basic auth
//////            postMethod.setRequestHeader("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString(("OA2POD:Sap12345").getBytes()));
////            RequestEntity requestEntity = new StringRequestEntity(param, "application/json", "UTF-8");
////            postMethod.setRequestEntity(requestEntity);
////            httpClient.executeMethod(postMethod);// 执行请求
////            inputStream = postMethod.getResponseBodyAsStream();// 获取返回的流
////            BufferedReader br = null;
////            StringBuffer buffer = new StringBuffer();
////            // 将返回的输入流转换成字符串
////            br = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
////            String temp;
////            while ((temp = br.readLine()) != null) {
////                buffer.append(temp);
////            }
////            System.out.print("接口返回内容为:" + buffer);
////            return buffer.toString();
////        } catch (Exception e) {
////            System.out.print("请求异常" + e.getMessage());
////            throw new RuntimeException(e.getMessage());
////        } finally {
////            if (inputStream != null) {
////                try {
////                    inputStream.close();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
////        }
////    }
//} - 1) {
//                        updateQjmxidAmount(qjmxids.get(j), averageAmount + remainder);
//                    } else {
//                        updateQjmxidAmount(qjmxids.get(j), averageAmount);
//                    }
//                }
//                int zyf=getMonthsDifference(ks,js);
//                updateMJFLL(jd,gkdm,djllspf,js,zyf);
//            }else if("2".equals(cqff)&&"1".equals(dqfs)){
//                //打切转移
//            }
//        }
//        Map<String,String> ret = new HashMap<>();
//        ret.put("code","1");
//        return ret;
//    }
//
//    /**
//     * @description:更新已回收最后一月之后的应收明细数据为无效
//     * @author: gitdog
//     * @date: 2024/1/7 12:03
//     * @param: djllspf
//     * @param: lastmonth
//     * @return:
//     **/
//    public void  updateInvalid(String djllspf,String lastmonth){
//        RecordSet rs = new RecordSet();
//        String sql="update uf_qjcqysmx set sjzt=1 where djllspf=? and ny>?";
//        if(!rs.executeUpdate(sql,djllspf,lastmonth)){error+=sql+":执行失败</br>";}
//    }
//
//    /**
//     * @description:获取模具台账本品番最后一个月份
//     * @author: gitdog
//     * @date: 2024/1/7 12:05
//     * @param: djllspf
//     * @param: jd
//     * @param: gkdm
//     * @return: ny
//     **/
//    public String getmjlastMonth(String djllspf,String jd,String gkdm){
//        RecordSet rs = new RecordSet();
//        String sql="select top(1) ny from uf_mjfyhsmx where djllspf=? and jd=? and gkdm=? order by ny desc";
//        rs.executeQuery(sql,djllspf,jd,gkdm);
//        rs.next();
//        return rs.getString("ny");
//    }
//    private void updateQjmxidAmount(String s, double averageAmount) {
//        RecordSet rs = new RecordSet();
//        String sql="update uf_qjcqysmx set yhse=yhse+"+averageAmount+" where id=?";
//        rs.executeUpdate(sql,s);
//    }
//    public List<String> getysmxid(String djllspf,String jd,String gkdm){
//        RecordSet rs = new RecordSet();
//        ArrayList<String> ids = new ArrayList<>();
//        String sql="select id from uf_qjcqysmx where djllspf=? and jd=? and gkdm=? and sjzt=0 order by ny";
//        rs.executeQuery(sql,djllspf,jd,gkdm);
//        while(rs.next()){
//            ids.add(rs.getString("id"));
//        }
//        return ids;
//    }
//
//    //常规打切更新模具费履历表
//    private void updateMJFLL(String jd,String gkdm,String djllspf,String lastDate,int zyf,String params) {
//        RecordSet rs = new RecordSet();
//        RecordSet rs1 = new RecordSet();
//        RecordSet rs2 = new RecordSet();
//        String sql="select max(id) from uf_mjfllb where jd=?,gkdm=?,djllspf=?";
//        rs.executeQuery(sql,jd,gkdm,djllspf);
//        if(rs.next()){
//            String id=rs.getString("id");
//            rs1.executeQuery("select * from uf_mjfllb where id=?");
//            rs1.next();
//            String ks=rs1.getString("hsksrq");
//            String js=rs1.getString("hsjsrq");
//            zyf+=getMonthsDifference(ks,js);
//            rs2.executeUpdate("update uf_mjfllb set hsjsrq=?,hszys=? where id=?",lastDate,zyf,id);
//        }else{
//            //插入
//            rs1.executeUpdate("insert into uf_mjfllb("+params+") values()");
//        }
//    }
//
//    private int getMonthsDifference(String date1, String date2) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate startDate = LocalDate.parse(date1, formatter);
//        LocalDate endDate = LocalDate.parse(date2, formatter);
//
//        long monthsDiff = ChronoUnit.MONTHS.between(startDate, endDate);
//        return (int) Math.abs(monthsDiff);
//    }
//
//
//}
