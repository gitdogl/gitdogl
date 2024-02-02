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
//public class JinDieXiaoShouDingDan {
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
//        String formid = "SAL_SaleOrder";
//        String data = "";
//        JSONObject dataobj = new JSONObject(true);
//        dataobj.put("IsAutoSubmitAndAudit", "true");
//
//        String FBillTypeID = Util.null2String(ysdataobj.getString("FBillTypeID")); //单据类型（必填）
//        String FBillNo = Util.null2String(ysdataobj.getString("FBillNo")); //单据编码
//        String FDATE = Util.null2String(ysdataobj.getString("FDate")); //日期(必填项)
//        String FCustId = Util.null2String(ysdataobj.getString("FCustId")); //供应商
//        String FHeadDeliveryWay = Util.null2String(ysdataobj.getString("FHeadDeliveryWay")); //采购部门
//        String FReceiveId = Util.null2String(ysdataobj.getString("FReceiveId")); //采购员
//        String FSaleDeptId = Util.null2String(ysdataobj.getString("FSaleDeptId")); //供货方
//        String FSalerId = Util.null2String(ysdataobj.getString("FSalerId")); //供货方
//        String FSettleId = Util.null2String(ysdataobj.getString("FSettleId")); //供货方地址
//        String FChargeId = Util.null2String(ysdataobj.getString("FChargeId")); //结算方
//        String FISINIT = Util.null2String(ysdataobj.getString("FISINIT")); //收款方
//        String FIsMobile = Util.null2String(ysdataobj.getString("FIsMobile")); //修改变更操作
//        String FIsUseOEMBomPush = Util.null2String(ysdataobj.getString("FIsUseOEMBomPush")); //变更状态
//        String F_POKG_Text = Util.null2String(ysdataobj.getString("F_POKG_Text")); //验收方式
//        String F_POKG_Text1 = Util.null2String(ysdataobj.getString("F_POKG_Text1")); //验收方式
//        String F_POKG_Text2 = Util.null2String(ysdataobj.getString("F_POKG_Text2")); //验收方式
//        String F_POKG_Text3 = Util.null2String(ysdataobj.getString("F_POKG_Text3")); //验收方式
//
//
//        JSONObject FPOOrderFinancet = JSONObject.parseObject(ysdataobj.getString("FSaleOrderFinance"));
//        String FSettleCurrId = Util.null2String(ysdataobj.getString("FSettleCurrId")); //邮箱
//        String FRecConditionId = Util.null2String(ysdataobj.getString("FRecConditionId")); //合同号
//        String FIsPriceExcludeTax = Util.null2String(ysdataobj.getString("FIsPriceExcludeTax")); //大船名
//        String FSettleModeId = Util.null2String(ysdataobj.getString("FSettleModeId")); //农残
//
//        String FIsIncludedTax = Util.null2String(FPOOrderFinancet.getString("FIsIncludedTax")); //汇率
//        String FExchangeTypeId = Util.null2String(FPOOrderFinancet.getString("FExchangeTypeId")); //定价时点
//        String FExchangeRate = Util.null2String(FPOOrderFinancet.getString("FExchangeRate")); //定价时点
//        String FMarginLevel = Util.null2String(FPOOrderFinancet.getString("FMarginLevel")); //含税
//        String FMargin = Util.null2String(FPOOrderFinancet.getString("FMargin")); //价外税
//        String FOverOrgTransDirect = Util.null2String(FPOOrderFinancet.getString("FOverOrgTransDirect")); //本位币
//        String FAllDisCount = Util.null2String(FPOOrderFinancet.getString("FAllDisCount")); //单次预付额度汇率
//
//        JSONObject Model = new JSONObject(true);
//        Model.put("FID", "0"); //实体主键
//        if ("".equals(FBillTypeID)) {
//            FBillTypeID = "XSDD01_SYS";//单据类型
//        }
//
//
//        Model.put("FBillTypeID", getFNumber(FBillTypeID));//单据类型（必填）
//        Model.put("FBillNo", FBillNo);//合同名称(必填项)
//        if (!"".equals(FDATE)) {
//            Model.put("FDate", FDATE);//日期
//        }
//        Model.put("FCustId", getFNumber(FCustId));//CRM客户
//        Model.put("FHeadDeliveryWay", getFNumber(FHeadDeliveryWay));//CRM客户
//        Model.put("FReceiveId", getFNumber(FReceiveId));//客户
//        Model.put("FSaleDeptId", getFNumber(FSaleDeptId));//销售部门
//        Model.put("FSalerId", getFNumber(FSalerId));//销售部门
//        Model.put("FSettleId", getFNumber(FSettleId));//客户
//        Model.put("FChargeId", getFNumber(FChargeId));//客户
//        Model.put("FISINIT", FISINIT);//客户
//        Model.put("FIsMobile", FIsMobile);//客户
//        Model.put("FIsUseOEMBomPush", FIsUseOEMBomPush);//客户
//        Model.put("F_POKG_Text", F_POKG_Text);//客户
//        Model.put("F_POKG_Text1", F_POKG_Text1);//客户
//        Model.put("F_POKG_Text2", F_POKG_Text2);//客户
//        Model.put("F_POKG_Text3", F_POKG_Text3);//客户
//
//        JSONObject FPOOrderFinance = new JSONObject(true);
//        FPOOrderFinance.put("FSettleCurrId", getFNumber(FSettleCurrId));
//        FPOOrderFinance.put("FRecConditionId", getFNumber(FRecConditionId));
//        FPOOrderFinance.put("FIsPriceExcludeTax", FIsPriceExcludeTax);
//        FPOOrderFinance.put("FSettleModeId", getFNumber(FSettleModeId));
//        FPOOrderFinance.put("FIsIncludedTax", FIsIncludedTax);
//        FPOOrderFinance.put("FExchangeTypeId", getFNumber(FExchangeTypeId));
//        FPOOrderFinance.put("FExchangeRate", Double.parseDouble(FExchangeRate));
//        FPOOrderFinance.put("FMarginLevel", FMarginLevel);
//        FPOOrderFinance.put("FMargin", FMargin);
//        FPOOrderFinance.put("FOverOrgTransDirect", FOverOrgTransDirect);
//        FPOOrderFinance.put("FAllDisCount", FAllDisCount);
//        Model.put("FPOOrderFinance", FPOOrderFinance);
//
//        ret.put("结果", Model.toString());
//        JSONArray jsonArray = new JSONArray();//明细
//
//        JSONArray jsonArrayDt = ysdataobj.getJSONArray("FSaleOrderEntry");//获取OA合同清单项目明细
//        if (jsonArrayDt != null) {
//            for (int i = 0; i < jsonArrayDt.size(); i++) {
//                JSONObject jsonObject = jsonArrayDt.getJSONObject(i);
//                JSONObject FPBXQEntityDetail = new JSONObject(true);//合同清单
//
//                String FRowType = Util.null2String(jsonObject.getString("FRowType")); //委外产品类型
//                String FMaterialId = Util.null2String(jsonObject.getString("FMaterialId")); //物料编码
//                String FUnitID = Util.null2String(jsonObject.getString("FUnitID")); //采购单位
//                String FQty = Util.null2String(jsonObject.getString("FQty")); //采购单位
//                String FPriceUnitId = Util.null2String(jsonObject.getString("FPriceUnitId")); //计价单位
//                String FPrice = Util.null2String(jsonObject.getString("FPrice")); //交货日期
//                String FTaxPrice = Util.null2String(jsonObject.getString("FTaxPrice")); //税率%
//                String FIsFree = Util.null2String(jsonObject.getString("FIsFree")); //需求部门
//                String FEntryTaxRate = Util.null2String(jsonObject.getString("FEntryTaxRate")); //收料部门
//                String FDeliveryDate = Util.null2String(jsonObject.getString("FDeliveryDate")); //是否赠品
//                String FOwnerTypeId = Util.null2String(jsonObject.getString("FOwnerTypeId")); //备注
//                String FOwnerId = Util.null2String(jsonObject.getString("FOwnerId")); //供应商物料编码
//                String FEntryNote = Util.null2String(jsonObject.getString("FEntryNote")); //供应商物料名称
//                String FReserveType = Util.null2String(jsonObject.getString("FReserveType")); //库存单位
//                String FStockUnitID = Util.null2String(jsonObject.getString("FStockUnitID")); //供应商批号
//                String FOUTLMTUNIT = Util.null2String(jsonObject.getString("FOUTLMTUNIT")); //控制交货数量
//                String FOutLmtUnitID = Util.null2String(jsonObject.getString("FOutLmtUnitID")); //控制交货时间
//                String FISMRP = Util.null2String(jsonObject.getString("FISMRP")); //最早交货日期
//
//
//                FPBXQEntityDetail.put("FRowType", FRowType);//清单项目、必填
//                FPBXQEntityDetail.put("FMaterialId", getFNumber(FMaterialId));//数量
//                FPBXQEntityDetail.put("FUnitID", getFNumber(FUnitID));//税率
//                FPBXQEntityDetail.put("FQty", FQty);//税率
//                FPBXQEntityDetail.put("FPriceUnitId", getFNumber(FPriceUnitId));//含税单价
//                FPBXQEntityDetail.put("FPrice", FPrice);//税额
//                FPBXQEntityDetail.put("FTaxPrice", FTaxPrice);//价税合计
//                FPBXQEntityDetail.put("FIsFree", FIsFree);//价税合计
//                FPBXQEntityDetail.put("FEntryTaxRate", FEntryTaxRate);//价税合计
//                FPBXQEntityDetail.put("FDeliveryDate", FDeliveryDate);//价税合计
//                FPBXQEntityDetail.put("FOwnerTypeId", FOwnerTypeId);//价税合计
//                FPBXQEntityDetail.put("FOwnerId", getFNumber(FOwnerId));//价税合计
//                FPBXQEntityDetail.put("FEntryNote", FEntryNote);//价税合计
//                FPBXQEntityDetail.put("FReserveType", FReserveType);//价税合计
//                FPBXQEntityDetail.put("FStockUnitID", getFNumber(FStockUnitID));//价税合计
//                FPBXQEntityDetail.put("FOUTLMTUNIT", FOUTLMTUNIT);//价税合计
//                FPBXQEntityDetail.put("FOutLmtUnitID", getFNumber(FOutLmtUnitID));//价税合计
//                FPBXQEntityDetail.put("FISMRP", FISMRP);//价税合计
//
//
//                JSONArray FEntryDeliveryPlan = new JSONArray();//明细
//
//                JSONArray FenjsonArrayDt = jsonObject.getJSONArray("FOrderEntryPlan");//获取OA合同清单项目明细
//                if (FenjsonArrayDt != null) {
//                    for (int j = 0; j < FenjsonArrayDt.size(); j++) {
//                        JSONObject FenjsonObject = FenjsonArrayDt.getJSONObject(j);
//                        JSONObject FenJsonResult = new JSONObject(true);//合同清单
//
//                        String FPlanDate = Util.null2String(FenjsonObject.getString("FPlanDate")); //费用项目
//                        String FPlanQty = Util.null2String(FenjsonObject.getString("FPlanQty")); //费用项目
//
//                        FenJsonResult.put("FPlanDate", FPlanDate);
//                        FenJsonResult.put("FPlanQty", FPlanQty);
//                        FEntryDeliveryPlan.add(FenJsonResult);
//                    }
//                }
//                FPBXQEntityDetail.put("FOrderEntryPlan", FEntryDeliveryPlan);
//                jsonArray.add(FPBXQEntityDetail);
//            }
//        }
//
//        Model.put("FSaleOrderEntry", jsonArray);//合同清单
//
//
//        JSONArray jsonArrayt = new JSONArray();//明细
//
//        JSONArray jsonArraytDt = ysdataobj.getJSONArray("FSaleOrderPlan");//获取OA合同清单项目明细
//        if (jsonArraytDt != null) {
//            for (int i = 0; i < jsonArraytDt.size(); i++) {
//                JSONObject jsonObject = jsonArraytDt.getJSONObject(i);
//                JSONObject FPBXQEntityDetail = new JSONObject(true);//合同清单
//
//                String FNeedRecAdvance = Util.null2String(jsonObject.getString("FNeedRecAdvance")); //委外产品类型
//                String FRecAdvanceRate = Util.null2String(jsonObject.getString("FRecAdvanceRate")); //委外产品类型
//                String FIsOutStockByRecamount = Util.null2String(jsonObject.getString("FIsOutStockByRecamount")); //委外产品类型
//
//
//                FPBXQEntityDetail.put("FNeedRecAdvance", FNeedRecAdvance);//清单项目、必填
//                FPBXQEntityDetail.put("FRecAdvanceRate", FRecAdvanceRate);//清单项目、必填
//                FPBXQEntityDetail.put("FIsOutStockByRecamount", FIsOutStockByRecamount);//清单项目、必填
//
//                jsonArrayt.add(FPBXQEntityDetail);
//            }
//        }
//
//        Model.put("FSaleOrderPlan", jsonArrayt);
//
//        JSONArray jsonArrayta = new JSONArray();//明细
//
//        JSONArray jsonArraytDta = ysdataobj.getJSONArray("FSalOrderTrace");//获取OA合同清单项目明细
//        if (jsonArraytDta != null) {
//            for (int i = 0; i < jsonArraytDta.size(); i++) {
//                JSONObject jsonObject = jsonArraytDta.getJSONObject(i);
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
//                jsonArrayta.add(FPBXQEntityDetail);
//            }
//        }
//        Model.put("FSalOrderTrace", jsonArrayta);
//
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
