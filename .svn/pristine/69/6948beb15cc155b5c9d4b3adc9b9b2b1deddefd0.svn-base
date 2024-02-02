//package com.weaver.esb.package_20240103034911;
//
//
//import com.alibaba.fastjson.JSONObject;
//import weaver.conn.RecordSet;
//import weaver.general.PasswordUtil;
//import weaver.general.Util;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.methods.RequestEntity;
//import org.apache.commons.httpclient.methods.StringRequestEntity;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.nio.charset.Charset;
//import java.util.*;
//import java.util.regex.Pattern;
//
//public class class_20240103034911 {
//
//    /**
//     * @param:  param(Map collections)
//     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
//     */
//    public Map execute(Map<String,Object> params) {
//        String loginid = Util.null2String(params.get("loginid"));
//        String pwd = Util.null2String(params.get("pwd"));
//        Map<String,Object> ret = new HashMap<>();
//
//        RecordSet rs = new RecordSet();
//        rs.isReturnDecryptData(true);
//        rs.executeQuery("select id,password,salt from hrmresource where loginid=?", loginid);
//        if (rs.next()) {
//            String id=rs.getString("id");
//            String currpwd = rs.getString("password");
//            String currsalt = rs.getString("salt");
//            if (PasswordUtil.check(pwd, currpwd, currsalt)) {
//                if("935".equals(id)&&"525".equals(id)&&"471".equals(id)&&"1018".equals(id)){
//                    String url="https://oa.helichina.com/ssologin/getToken?appid=2c8160cf-07ba-4e97-8cb9-d7be31b0b6df&loginid="+loginid;
//                    String res =doPost(url,"");
//                    if (res != null) {
//                        String token=res;
//                        if("".equals(token)){
//                            ret.put("resultCode","E");
//                            ret.put("resultMsg","token不存在");
//                        }else{
//                            if (isChi(token)){
//                                ret.put("resultCode","E");
//                                ret.put("resultMsg",token);
//                            }else{
//                                ret.put("resultCode","S");
//                                JSONObject data = new JSONObject();
//                                data.put("token",token);
//                                ret.put("data",data);
//                            }
//                        }
//                    }
//                }else{
//                    ret.put("resultCode","E");
//                    ret.put("resultMsg","登录失败，无权限访问");
//                }
//            }else{
//                ret.put("resultCode","E");
//                ret.put("resultMsg","登录失败，请检查账号密码");
//            }
//        }else{
//            rs.executeQuery("select id,password,salt from hrmresourcemanager where loginid=?", loginid);
//            if (rs.next()) {
//                String currpwd = rs.getString("password");
//                String currsalt = rs.getString("salt");
//                ret.put("currpwd",currpwd);
//                ret.put("currsalt",currsalt);
//                if (PasswordUtil.check(pwd, currpwd, currsalt)) {
//                    String url="https://oa.helichina.com/ssologin/getToken?appid=2c8160cf-07ba-4e97-8cb9-d7be31b0b6df&loginid="+loginid;
//                    String res =doPost(url,"");
//                    if (res != null) {
//                        String token=res;
//                        if("".equals(token)){
//                            ret.put("resultCode","E");
//                            ret.put("resultMsg","token不存在");
//                        }else{
//                            if (isChi(token)){
//                                ret.put("resultCode","E");
//                                ret.put("resultMsg",token);
//                            }else{
//                                ret.put("resultCode","S");
//                                JSONObject data = new JSONObject();
//                                data.put("token",token);
//                                ret.put("data",data);
//                            }
//                        }
//                    }
//                }else{
//                    ret.put("resultCode","E");
//                    ret.put("resultMsg","登录失败，请检查账号密码");
//                }
//            }else{
//                ret.put("resultCode","E");
//                ret.put("resultMsg","登录失败，请检查账号密码");
//            }
//        }
//        return ret;
//    }
//
//
//    public String doPost(String requestUrl, String param) {
//        InputStream inputStream = null;
//        try {
//            HttpClient httpClient = new HttpClient();
//            PostMethod postMethod = new PostMethod(requestUrl);
//            // 设置请求头  Content-Type
//            postMethod.setRequestHeader("Content-Type", "application/json");
//            //Base64加密方式认证方式下的basic auth
////            postMethod.setRequestHeader("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString(("OA2POD:Sap12345").getBytes()));
//            RequestEntity requestEntity = new StringRequestEntity(param, "application/json", "UTF-8");
//            postMethod.setRequestEntity(requestEntity);
//            httpClient.executeMethod(postMethod);// 执行请求
//            inputStream = postMethod.getResponseBodyAsStream();// 获取返回的流
//            BufferedReader br = null;
//            StringBuffer buffer = new StringBuffer();
//            // 将返回的输入流转换成字符串
//            br = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
//            String temp;
//            while ((temp = br.readLine()) != null) {
//                buffer.append(temp);
//            }
//            System.out.print("接口返回内容为:" + buffer);
//            return buffer.toString();
//        } catch (Exception e) {
//            System.out.print("请求异常" + e.getMessage());
//            throw new RuntimeException(e.getMessage());
//        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public boolean isChi(String token){
//        // 使用正则表达式判断是否包含中文字符
//        String regex = ".*[\\u4e00-\\u9fa5]+.*";
//        return Pattern.matches(regex, token);
//    }
//}
