//package com.weaver.esb.package_20230707032628;
//
//import java.util.*;
//
//import static weaver.sms.system.straycat.StrayCatSmsServiceImpl.MD5;
//
//public class class_20230707032628 {
//
//    /**
//     * @param:  param(Map collections)
//     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
//     */
//    public Map execute(Map<String,Object> params) {
//        String Secret="zzgJ7ekxcwRTeyg4zpFN";
//        String POST_JSON = "{\"request\":{\"body\":{\"accessToken\":\"898d358adaa5d9cf07855300c0679698504830eab0f07f39a3cc9e84ee6b605bc37489696e25b361a205d06960e20c99ff35e9c47e18b503f187b95bb38f48fe378207a5ef99eeb660574fff7daf74cc82c6c61fe0ca6ae4cc72019e45a1c30661150eff71b5b404b65ba228c4e56a119784f387113fa4385ffcbf6a998c3b631d4e8a719bf40c03788d01b7fe17f70732dca653c48992fc1f9f28b91efe3709628f0ef9c721f624\",\"isEncrypt\":false,\"random\":\"126b83e6-16ab-434c-90d7-85f7fb7eb431\",\"timestamp\":\"1688715086000\",\"param\":{\"outEmployeeId\":\"test001\",\"employeeType\":0,\"surName\":\"周\",\"givenName\":\"杰伦\",\"enSurName\":\"zhou\",\"enGivenName\":\"jielun\",\"gender\":1,\"mobile\":\"18888888888\",\"email\":\"123456789@qq.com\",\"idCard\":\"110000197901181122\",\"reservationType\":0,\"positionName\":\"测试\",\"positionLevelName\":\"L8\",\"departName\":\"测试部\",\"workingState\":1,\"birthday\":\"1990-01-01\",\"credentialExpireDate\":\"1990-01-01\",\"credentialType\":1,\"addTravellerAllowed\":0,\"employeeCode\":\"1111\",\"costCenter\":\"测试部\",\"preTravelPolicyList\":[{\"policyCode\":\"国内机票差旅政策\",\"productTypeId\":1}],\"remarks\":{\"remark1\":\"备注1\",\"remark2\":\"备注2\",\"remark3\":\"备注3\",\"remark4\":\"备注4\"},\"baseCity\":\"北京\"},\"costCenterList\":[{\"costCenterName\":\"华东大区\",\"costCenterCode\":\"\",\"costCenterTypeName\":\"大区\",\"costCenterTypeCode\":\"costCenter\",\"isDefault\":true},{\"costCenterName\":\"南京公司\",\"costCenterCode\":\"\",\"costCenterTypeName\":\"所属公司\",\"costCenterTypeCode\":\"costCenter2\",\"isDefault\":true},{\"costCenterName\":\"财务部\",\"costCenterCode\":\"\",\"costCenterTypeName\":\"费用部门\",\"costCenterTypeCode\":\"costCenter3\",\"isDefault\":true},{\"costCenterName\":\"研发部\",\"costCenterCode\":\"\",\"costCenterTypeName\":\"费用部门\",\"costCenterTypeCode\":\"costCenter3\",\"isDefault\":false},{\"costCenterName\":\"销售部\",\"costCenterCode\":\"\",\"costCenterTypeName\":\"费用部门\",\"costCenterTypeCode\":\"costCenter3\",\"isDefault\":false}]}}}";
//        Map<String,String> ret = new HashMap<>();
//        String POSTDATA_MD5 = MD5(POST_JSON).toLowerCase();
//        String FinallySign = MD5((POSTDATA_MD5 + Secret)).toLowerCase();
//        ret.put("code","1");
//        ret.put("FinallySign",FinallySign);
//        return ret;
//    }
//}
