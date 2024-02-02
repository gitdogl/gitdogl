//package com.weaver.esb.wanshen;
//
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import weaver.general.Util;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 金蝶付款申请单合同
// */
//public class JinDieFAHUOTONGHZIDAN {
//    /**
//     * @param: param(Map collections)
//     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
//     */
//    public Map execute(Map<String, Object> params) throws Exception {
//        // 示例：data：定义的请求数据，code:定义的响应数据
//        Map<String, Object> ret = new HashMap<String, Object>();
//        String ysdata = Util.null2String(params.get("data"));
//        JSONObject ysdataobjt = JSONObject.parseObject(ysdata);
//        JSONObject ysdataobj = JSONObject.parseObject(ysdataobjt.getString("Model"));
//        ret.put("ysdata", ysdata);
//        ret.put("ysdataobj", ysdataobj);
//
//
//        //登录结果类型等于1，代表登录成功
//        String formid = "SAL_DELIVERYNOTICE";
//        String data = "";
//        JSONObject dataobj = new JSONObject(true);
//        dataobj.put("IsAutoSubmitAndAudit", "true");
//
//        String FBillTypeID = Util.null2String(ysdataobj.getString("FBillTypeID")); //单据类型（必填）
//        String FBillNo = Util.null2String(ysdataobj.getString("FBillNo")); //单据编码
//        String FDATE = Util.null2String(ysdataobj.getString("FDate")); //日期(必填项)
//        String FCustomerID = Util.null2String(ysdataobj.getString("FCustomerID")); //供应商
//        String FHeadDeliveryWay = Util.null2String(ysdataobj.getString("FHeadDeliveryWay")); //采购部门
//        String FCarrierID = Util.null2String(ysdataobj.getString("FCarrierID")); //采购员
//        String FCarriageNO = Util.null2String(ysdataobj.getString("FCarriageNO")); //供货方
//        String FDeliveryDeptID = Util.null2String(ysdataobj.getString("FDeliveryDeptID")); //供货方地址
//        String FStockerID = Util.null2String(ysdataobj.getString("FStockerID")); //结算方
//        String FReceiverID = Util.null2String(ysdataobj.getString("FReceiverID")); //收款方
//        String FSettleID = Util.null2String(ysdataobj.getString("FSettleID")); //修改变更操作
//        String FPayerID = Util.null2String(ysdataobj.getString("FPayerID")); //变更状态
//        String FOwnerTypeIdHead = Util.null2String(ysdataobj.getString("FOwnerTypeIdHead")); //验收方式
//        String F_POKG_Text = Util.null2String(ysdataobj.getString("F_POKG_Text")); //邮箱
//        String F_POKG_Text3 = Util.null2String(ysdataobj.getString("F_POKG_Text3")); //合同号
//        String F_POKG_CheckBox = Util.null2String(ysdataobj.getString("F_POKG_CheckBox")); //大船名
//        String F_POKG_Text4 = Util.null2String(ysdataobj.getString("F_POKG_Text4")); //大船名
//        String F_POKG_Text5 = Util.null2String(ysdataobj.getString("F_POKG_Text5")); //大船名
//
//        JSONObject FPOOrderFinancet = JSONObject.parseObject(ysdataobj.getString("SubHeadEntity"));
//        String FSettleCurrID = Util.null2String(FPOOrderFinancet.getString("FSettleCurrID")); //结算方式
//        String FLocalCurrID = Util.null2String(FPOOrderFinancet.getString("FLocalCurrID")); //付款条件
//        String FExchangeTypeID = Util.null2String(FPOOrderFinancet.getString("FExchangeTypeID")); //结算币别
//        String FExchangeRate = Util.null2String(FPOOrderFinancet.getString("FExchangeRate")); // 汇率类型
//        String FOverOrgTransDirect = Util.null2String(FPOOrderFinancet.getString("FOverOrgTransDirect")); //汇率
//
//        JSONObject Model = new JSONObject(true);
//        Model.put("FID", "0"); //实体主键
//        if ("".equals(FBillTypeID)) {
//            FBillTypeID = "FHTZD01_SYS";//单据类型
//        }
//
//
//        Model.put("FBillTypeID", getFNumber(FBillTypeID));//单据类型（必填）
//        Model.put("FBillNo", FBillNo);//合同名称(必填项)
//        if (!"".equals(FDATE)) {
//            Model.put("FDate", FDATE);//日期
//        }
//        Model.put("FCustomerID", getFNumber(FCustomerID));//CRM客户
//        Model.put("FHeadDeliveryWay", getFNumber(FHeadDeliveryWay));//CRM客户
//        Model.put("FCarrierID", getFNumber(FCarrierID));//客户
//        Model.put("FCarriageNO", FCarriageNO);//销售部门
//        Model.put("FDeliveryDeptID", getFNumber(FDeliveryDeptID));//客户
//        Model.put("FStockerID", getFNumber(FStockerID));//客户
//        Model.put("FReceiverID", getFNumber(FReceiverID));//客户
//        Model.put("FSettleID", getFNumber(FSettleID));//客户
//        Model.put("FPayerID", getFNumber(FPayerID));//客户
//        Model.put("FOwnerTypeIdHead", FOwnerTypeIdHead);//客户
//        Model.put("F_POKG_Text", F_POKG_Text);//客户
//        Model.put("F_POKG_Text3", F_POKG_Text3);//客户
//        Model.put("F_POKG_CheckBox", F_POKG_CheckBox);//客户
//        Model.put("F_POKG_Text4", F_POKG_Text4);//客户
//        Model.put("F_POKG_Text5", F_POKG_Text5);//客户
//
//        JSONObject FPOOrderFinance = new JSONObject(true);
//        FPOOrderFinance.put("FSettleCurrID", getFNumber(FSettleCurrID));
//        FPOOrderFinance.put("FLocalCurrID", getFNumber(FLocalCurrID));
//        FPOOrderFinance.put("FExchangeTypeID", getFNumber(FExchangeTypeID));
//        FPOOrderFinance.put("FExchangeRate", getFNumber(FExchangeRate));
//        FPOOrderFinance.put("FOverOrgTransDirect", FOverOrgTransDirect);
//        Model.put("SubHeadEntity", FPOOrderFinance);
//
//        ret.put("结果", Model.toString());
//        JSONArray jsonArray = new JSONArray();//明细
//
//        JSONArray jsonArrayDt = ysdataobj.getJSONArray("FEntity");//获取OA合同清单项目明细
//        if (jsonArrayDt != null) {
//            for (int i = 0; i < jsonArrayDt.size(); i++) {
//                JSONObject jsonObject = jsonArrayDt.getJSONObject(i);
//                JSONObject FPBXQEntityDetail = new JSONObject(true);//合同清单
//
//                String FRowType = Util.null2String(jsonObject.getString("FRowType")); //委外产品类型
//                String FMaterialID = Util.null2String(jsonObject.getString("FMaterialID")); //物料编码
//                String FUnitID = Util.null2String(jsonObject.getString("FUnitID")); //物料说明
//                String FQty = Util.null2String(jsonObject.getString("FQty")); //采购单位
//                String FDeliveryDate = Util.null2String(jsonObject.getString("FDeliveryDate")); //计价单位
//                String FStockID = Util.null2String(jsonObject.getString("FStockID")); //交货日期
//                String FSrcType = Util.null2String(jsonObject.getString("FSrcType")); //交货日期
//                String FBACKUPSTOCKID = Util.null2String(jsonObject.getString("FBACKUPSTOCKID")); //税率%
//                String FNoteEntry = Util.null2String(jsonObject.getString("FNoteEntry")); //需求部门
//                String FIsFree = Util.null2String(jsonObject.getString("FIsFree ")); //需求部门
//                String FStockStatusId = Util.null2String(jsonObject.getString("FStockStatusId")); //收料部门
//                String FOutContROL = Util.null2String(jsonObject.getString("FOutContROL")); //是否赠品
//                String FOutMaxQty = Util.null2String(jsonObject.getString("FOutMaxQty")); //备注
//                String FOutMinQty = Util.null2String(jsonObject.getString("FOutMinQty")); //供应商物料编码
//                String FBaseUnitID = Util.null2String(jsonObject.getString("FBaseUnitID")); //供应商物料名称
//                String FPriceBaseQty = Util.null2String(jsonObject.getString("FPriceBaseQty")); //库存单位
//                String FPlanDeliveryDate = Util.null2String(jsonObject.getString("FPlanDeliveryDate")); //供应商批号
//                String FSrcBillNo = Util.null2String(jsonObject.getString("FSrcBillNo")); //供应商批号
//                String FStockUnitID = Util.null2String(jsonObject.getString("FStockUnitID")); //控制交货数量
//                String FStockQty = Util.null2String(jsonObject.getString("FStockQty")); //控制交货时间
//                String FStockBaseQty = Util.null2String(jsonObject.getString("FStockBaseQty")); //最早交货日期
//                String FOwnerTypeID = Util.null2String(jsonObject.getString("FOwnerTypeID")); //最晚交货日期
//                String FOwnerID = Util.null2String(jsonObject.getString("FOwnerID")); //价格系数
//                String FOutLmtUnit = Util.null2String(jsonObject.getString("FOutLmtUnit")); //结算方式
//                String FOutLmtUnitID = Util.null2String(jsonObject.getString("FOutLmtUnitID")); //需求跟踪号
//                String F_POKG_Text1 = Util.null2String(jsonObject.getString("F_POKG_Text1")); //计划确认
//                String F_POKG_Text2 = Util.null2String(jsonObject.getString("F_POKG_Text2")); //销售单位
//                String FAllAmountExceptDisCount = Util.null2String(jsonObject.getString("FAllAmountExceptDisCount")); //交货库存状态
//                String FCheckDelivery = Util.null2String(jsonObject.getString("FCheckDelivery")); //是否可库存
//
//                FPBXQEntityDetail.put("FRowType", FRowType);//清单项目、必填
//                FPBXQEntityDetail.put("FMaterialID", getFNumber(FMaterialID));//数量
//                FPBXQEntityDetail.put("FUnitID", getFNumber(FUnitID));//单价（不含税）
//                FPBXQEntityDetail.put("FQty", FQty);//税率
//                FPBXQEntityDetail.put("FDeliveryDate", FDeliveryDate);//含税单价
//                FPBXQEntityDetail.put("FStockID", getFNumber(FStockID));//税额
//                FPBXQEntityDetail.put("FSrcType ", FSrcType);//税额
//                FPBXQEntityDetail.put("FBACKUPSTOCKID", getFNumber(FBACKUPSTOCKID));//价税合计
//                FPBXQEntityDetail.put("FNoteEntry", FNoteEntry);//价税合计
//                FPBXQEntityDetail.put("FIsFree", FIsFree);//价税合计
//                FPBXQEntityDetail.put("FStockStatusId", getFNumber(FStockStatusId));//价税合计
//                FPBXQEntityDetail.put("FOutContROL", FOutContROL);//价税合计
//                FPBXQEntityDetail.put("FOutMaxQty", FOutMaxQty);//价税合计
//                FPBXQEntityDetail.put("FOutMinQty", FOutMinQty);//价税合计
//                FPBXQEntityDetail.put("FBaseUnitID", getFNumber(FBaseUnitID));//价税合计
//                FPBXQEntityDetail.put("FPriceBaseQty", FPriceBaseQty);//价税合计
//                FPBXQEntityDetail.put("FPlanDeliveryDate", FPlanDeliveryDate);//价税合计
//                FPBXQEntityDetail.put("FSrcBillNo", FSrcBillNo);//价税合计
//                FPBXQEntityDetail.put("FStockUnitID", getFNumber(FStockUnitID));//价税合计
//                FPBXQEntityDetail.put("FStockQty", FStockQty);//价税合计
//                FPBXQEntityDetail.put("FStockBaseQty", FStockBaseQty);//价税合计
//                FPBXQEntityDetail.put("FOwnerTypeID", FOwnerTypeID);//价税合计
//                FPBXQEntityDetail.put("FOwnerID", getFNumber(FOwnerID));//价税合计
//                FPBXQEntityDetail.put("FOutLmtUnit", FOutLmtUnit);//价税合计
//                FPBXQEntityDetail.put("FOutLmtUnitID", getFNumber(FOutLmtUnitID));//价税合计
//                FPBXQEntityDetail.put("F_POKG_Text1", F_POKG_Text1);//价税合计
//                FPBXQEntityDetail.put("F_POKG_Text2", F_POKG_Text2);//价税合计
//                FPBXQEntityDetail.put("FAllAmountExceptDisCount", FAllAmountExceptDisCount);//价税合计
//                FPBXQEntityDetail.put("FCheckDelivery", FCheckDelivery);//价税合计
//
//
//                jsonArray.add(FPBXQEntityDetail);
//            }
//        }
//
//        Model.put("FEntity", jsonArray);//合同清单
//
//
//        JSONArray jsonArrayt = new JSONArray();//明细
//
//        JSONArray jsonArraytDt = ysdataobj.getJSONArray("FDeliNoticeTrace");//获取OA合同清单项目明细
//        if (jsonArraytDt != null) {
//            for (int i = 0; i < jsonArraytDt.size(); i++) {
//                JSONObject jsonObject = jsonArraytDt.getJSONObject(i);
//                JSONObject FPBXQEntityDetail = new JSONObject(true);//合同清单
//
//                String FLogComId = Util.null2String(jsonObject.getString("FLogComId")); //委外产品类型
//                String FCarryBillNo = Util.null2String(jsonObject.getString("FCarryBillNo")); //委外产品类型
//                String FPhoneNumber = Util.null2String(jsonObject.getString("FPhoneNumber")); //委外产品类型
//                String FFrom = Util.null2String(jsonObject.getString("FFrom")); //委外产品类型
//                String FTo = Util.null2String(jsonObject.getString("FTo")); //委外产品类型
//                String FDelTime = Util.null2String(jsonObject.getString("FDelTime")); //委外产品类型
//
//
//                FPBXQEntityDetail.put("FLogComId", getFCODE(FLogComId));//清单项目、必填
//                FPBXQEntityDetail.put("FCarryBillNo", FCarryBillNo);//清单项目、必填
//                FPBXQEntityDetail.put("FPhoneNumber", FPhoneNumber);//清单项目、必填
//                FPBXQEntityDetail.put("FFrom", FFrom);//清单项目、必填
//                FPBXQEntityDetail.put("FTo", FTo);//清单项目、必填
//                FPBXQEntityDetail.put("FDelTime", FDelTime);//清单项目、必填
//
//                jsonArrayt.add(FPBXQEntityDetail);
//            }
//        }
//
//        Model.put("FDeliNoticeTrace", jsonArrayt);
//        dataobj.put("Model", Model);
//
//        ret.put("code", "0");
//        ret.put("formid", formid);
//        ret.put("data", dataobj.toJSONString());
//
//        return ret;
//    }
//
//    private Map<String, String> getFNumber(String value) {
//        Map<String, String> mapfnumber = new HashMap<String, String>();
//        mapfnumber.put("FNumber", value);
//        return mapfnumber;
//    }
//
//    private Map<String, String> getFCODE(String value) {
//        Map<String, String> mapfnumber = new HashMap<String, String>();
//        mapfnumber.put("FCODE", value);
//        return mapfnumber;
//    }
//}
