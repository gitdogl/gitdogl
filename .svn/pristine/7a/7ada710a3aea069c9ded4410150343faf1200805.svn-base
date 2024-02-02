package com.engine.customization.ltc.workflow;//package com.weaver.esb.package_20221207092200;
//
//import cn.hutool.core.util.CharsetUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.crypto.asymmetric.KeyType;
//import cn.hutool.crypto.asymmetric.RSA;
//import cn.hutool.http.HttpRequest;
//import cn.hutool.json.JSONUtil;
//import weaver.conn.RecordSet;
//import weaver.general.Util;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
//public class class_20221207092200 {
//
//    /**
//     * @param: param(Map collections)
//     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
//     */
//    RecordSet rs = new RecordSet();
//    String address = "http://172.16.200.235:8008/";
//    /**
//     * 模拟缓存服务
//     */
//    private static final Map<String, String> SYSTEM_CACHE = new HashMap<>();
//
//    /**
//     * ecology系统发放的授权许可证(appid)
//     */
//    String APPID = "test111";
//
//    /**
////     * @param: param(Map collections)
//     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
//     */
//    public Map execute(Map<String, Object> params) {
//        Map<String, String> ret = new HashMap<>();
//        // 示例：data：定义的请求数据，code:定义的响应数据
//        String workcode = Util.null2String(params.get("workcode"));
//        //address=Util.null2String( params.get("address"));
//        //APPID=Util.null2String( params.get("APPID"));
//        String userid = workcode;
//        // String sql="select id from hrmresource where status<4 and (  mobile=? or telephone=? or workcode=? )";
//        //rs.executeQuery(sql, workcode,workcode,workcode);
//
//        //if(rs.next())
//        // {
//        //  userid=Util.null2String(rs.getString("id"));
//        //}
//        if ("".equals(userid)) {
//            ret.put("code", "0");
//            ret.put("msg", "用户" + workcode + "不存在，请检查用户信息");
//        } else {
//            ret = getHeaderMap(userid);
//
//        }
//
//
//        // ……
//
//
//        return ret;
//
//    }
//
//    /**
//     * 第一步：
//     * <p>
//     * 调用ecology注册接口,根据appid进行注册,将返回服务端公钥和Secret信息
//     */
//    public Map<String, Object> testRegist(String address) {
//
//        //获取当前系统RSA加密的公钥
//        RSA rsa = new RSA();
//        String publicKey = rsa.getPublicKeyBase64();
//        String privateKey = rsa.getPrivateKeyBase64();
//
//        // 客户端RSA私钥
//        SYSTEM_CACHE.put("LOCAL_PRIVATE_KEY", privateKey);
//        // 客户端RSA公钥
//        SYSTEM_CACHE.put("LOCAL_PUBLIC_KEY", publicKey);
//
//        //调用ECOLOGY系统接口进行注册
//        String data = HttpRequest.post(address + "/api/ec/dev/auth/regist")
//                .header("appid", APPID)
//                .header("cpk", publicKey)
//                .timeout(2000)
//                .execute().body();
//
//        // 打印ECOLOGY响应信息
//        System.out.println("testRegist()：" + data);
//        Map<String, Object> datas = JSONUtil.parseObj(data);
//
//        //ECOLOGY返回的系统公钥
//        SYSTEM_CACHE.put("SERVER_PUBLIC_KEY", StrUtil.nullToEmpty((String) datas.get("spk")));
//        //ECOLOGY返回的系统密钥
//        SYSTEM_CACHE.put("SERVER_SECRET", StrUtil.nullToEmpty((String) datas.get("secrit")));
//        return datas;
//    }
//
//
//    /**
//     * 第二步：
//     * <p>
//     * 通过第一步中注册系统返回信息进行获取token信息
//     */
//    public Map<String, Object> testGetoken(String address) {
//        // 从系统缓存或者数据库中获取ECOLOGY系统公钥和Secret信息
//        String secret = SYSTEM_CACHE.get("SERVER_SECRET");
//        String spk = SYSTEM_CACHE.get("SERVER_PUBLIC_KEY");
//
//        // 如果为空,说明还未进行注册,调用注册接口进行注册认证与数据更新
//        if (Objects.isNull(secret) || Objects.isNull(spk)) {
//            testRegist(address);
//            // 重新获取最新ECOLOGY系统公钥和Secret信息
//            secret = SYSTEM_CACHE.get("SERVER_SECRET");
//            spk = SYSTEM_CACHE.get("SERVER_PUBLIC_KEY");
//        }
//
//        // 公钥加密,所以RSA对象私钥为null
//        RSA rsa = new RSA(null, spk);
//        //对秘钥进行加密传输，防止篡改数据
//        String encryptSecret = rsa.encryptBase64(secret, CharsetUtil.CHARSET_UTF_8, KeyType.PublicKey);
//
//        //调用ECOLOGY系统接口进行注册
//        String data = HttpRequest.post(address + "/api/ec/dev/auth/applytoken")
//                .header("appid", APPID)
//                .header("secret", encryptSecret)
//                .header("time", "3600")
//                .execute().body();
//
//        System.out.println("testGetoken()：" + data);
//        Map<String, Object> datas = JSONUtil.parseObj(data);
//
//        //ECOLOGY返回的token
//        // TODO 为Token缓存设置过期时间
//        SYSTEM_CACHE.put("SERVER_TOKEN", StrUtil.nullToEmpty((String) datas.get("token")));
//
//        return datas;
//    }
//
//    /**
//     * 第三步：
//     * <p>
//     * 调用ecology系统的rest接口，请求头部带上token和用户标识认证信息
//     *
//     * @param userid ecology系统地址
//     *               <p>
//     *               注意：ECOLOGY系统所有POST接口调用请求头请设置 "Content-Type","application/x-www-form-urlencoded; charset=utf-8"
//     */
//    public Map<String, String> getHeaderMap(String userid) {
//
//        Map<String, String> ret = new HashMap<>();
//        //ECOLOGY返回的token
//        String token = SYSTEM_CACHE.get("SERVER_TOKEN");
//        if (StrUtil.isEmpty(token)) {
//            token = (String) testGetoken(address).get("token");
//        }
//
//        String spk = SYSTEM_CACHE.get("SERVER_PUBLIC_KEY");
//        //封装请求头参数
//        RSA rsa = new RSA(null, spk);
//        //对用户信息进行加密传输,暂仅支持传输OA用户ID
//        String encryptUserid = rsa.encryptBase64(userid, CharsetUtil.CHARSET_UTF_8, KeyType.PublicKey);
//
//        ret.put("appid", APPID);
//        ret.put("token", token);
//        ret.put("userid", encryptUserid);
//        ret.put("code", "1");
//        ret.put("msg", "获取成功");
//        return ret;
//    }
//}
