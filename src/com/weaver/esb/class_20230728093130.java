package com.weaver.esb.package_20230728093130;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.formmode.virtualform.UUIDPKVFormDataSave;
import weaver.general.Util;

import java.text.SimpleDateFormat;
import java.util.*;

public class class_20230728093130 {
    RecordSet RS = new RecordSet();
    RecordSet rs = new RecordSet();
    //正常状态机票信息
    private List<String> flightorderlist;
    //机票改签信息
    private List<String> flightorderchangelist;
    //酒店住宿信息
    private List<String> hotelorderlist;
    //火车票信息
    private List<String> trainorderlist;
    //汽车票信息
    private List<String> carorderlist;

    //正常状态机票信息
    private Map<String, String> flightordermap;
    //机票改签信息
    private Map<String, String> flightorderchangemap;
    //酒店住宿信息
    private Map<String, String> hotelordermap;
    //火车票信息
    private Map<String, String> trainordermap;
    //汽车票信息
    private Map<String, String> carordermap;

    private String xmformmodeid;

    /**
     * @param: param(Map collections)
     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
     */
    public Map execute(Map<String, Object> params) {
        boolean sqlflag = true;
        String errors = "";
        List<Map<String, Object>> newlist = new ArrayList<>();
        Map<String, Object> ret = new HashMap<>();
        // 示例：data：定义的请求数据，code:定义的响应数据
        String list = Util.null2String(params.get("splitOrderInfoList"));
        xmformmodeid = Util.null2String(params.get("xmformmodeid"));
        if ("".equals(xmformmodeid)) {
            xmformmodeid = "32501";
        }

        //获取list
        JSONArray listarr = JSONArray.parseArray(list);

        flightordermap = new HashMap<>();
        flightorderchangemap = new HashMap<>();
        hotelordermap = new HashMap<>();
        trainordermap = new HashMap<>();
        carordermap = new HashMap<>();

        flightorderlist = new ArrayList<>();
        flightorderchangelist = new ArrayList<>();
        hotelorderlist = new ArrayList<>();
        trainorderlist = new ArrayList<>();
        carorderlist = new ArrayList<>();
        flightorderlist = getFlightorderList();// 获取机票信息原纪录
        flightorderchangelist = getflightOrderChangeList();// 获取机票改签信息原记录
        hotelorderlist = getHotelOrderList();// 获取酒店订单信息原记录
        trainorderlist = getTrainOrderList();// 获取火车票信息原记录
        carorderlist = getCarOrderList();// 获取火车票信息原记录

        if (listarr != null && listarr.size() > 0) {
            for (int i = 0; i < listarr.size(); i++) {
                JSONObject listobj = listarr.getJSONObject(i);
                if (listobj == null) {
                    continue;
                }
                //订单号
                String journeyNo=listobj.getString("journeyNo");
                String mainid=getJourneyNoMainid(journeyNo);
                //插入明细4，5正常和改签状态机票信息
                JSONArray flightOrderInfoList = listobj.getJSONArray("flightOrderInfoList");
                if (flightOrderInfoList != null && flightOrderInfoList.size() > 0) {
                    for (int j = 0; j < flightOrderInfoList.size(); j++) {
                        /**
                         *   ∟flightOrderInfoList	jsonarray	否	机票台账
                         *     ∟passengerInfo	jsonobj	否	乘客信息
                         *       ∟passengerName	string	否	乘客姓名
                         *       ∟outEmployeeId	string	否	乘客id
                         *       ∟seatClass	string	否	舱位等级
                         *     ∟flightOrderInfo	jsonobj	否  机票订单信息
                         *       ∟orderBaseInfo	jsonobj	否	订单基础信息
                         *         ∟orderSerialNo	string	否	订单号
                         *         ∟orderStatus	string	否	订单状态
                         *         ∟orderStatusDes	string	否	订单状态描述
                         *         ∟bookDate	string	否	下单时间
                         *         ∟journeyNo	string	否	行程单号
                         *         ∟bookerEmployeeName	string	否	预订人姓名
                         *         ∟bookerEmployeeId	string	否	预订人id
                         *         ∟travelBeginDate	string	否	行程开始时间
                         *         ∟travelEndDate	string	否	行程结束时间
                         *         ∟orderAmount	string	否	订单金额
                         *         ∟diffPrice	string	否	订单金额变更值
                         *       ∟segmentInfoList	jsonarr	否	行程目的地信息
                         *         ∟flightNo	string	否	flightNo
                         *         ∟departCity	string	否	出发城市
                         *         ∟departTime	string	否	出发时间
                         *         ∟arriveTime	string	否	到达时间
                         *         ∟arriveCity	string	否	到达城市
                         */
                        Map<String, Object> newwlmap = new HashMap<>();
                        JSONObject flightOrderInfoobj = flightOrderInfoList.getJSONObject(j);
                        //乘客信息
                        JSONObject passengerInfo = flightOrderInfoobj.getJSONObject("passengerInfo");
                        String passengerName = Util.null2String(passengerInfo.get("passengerName"));
                        String outEmployeeId = Util.null2String(passengerInfo.get("outEmployeeId"));
                        String seatClass = Util.null2String(passengerInfo.get("seatClass"));
                        //订单基础信息
                        JSONObject flightOrderInfo = flightOrderInfoobj.getJSONObject("flightOrderInfo");
                        JSONObject orderBaseInfo = flightOrderInfo.getJSONObject("orderBaseInfo");
                        String orderSerialNo1 = Util.null2String(orderBaseInfo.get("orderSerialNo"));
                        String orderStatus = Util.null2String(orderBaseInfo.get("orderStatus"));
                        String orderStatusDes = Util.null2String(orderBaseInfo.get("orderStatusDes"));
                        String bookDate = Util.null2String(orderBaseInfo.get("bookDate"));
                        //String journeyNo = Util.null2String(orderBaseInfo.get("passengerName"));
                        String bookerEmployeeName = Util.null2String(orderBaseInfo.get("bookerEmployeeName"));
                        String bookerEmployeeId = Util.null2String(orderBaseInfo.get("bookerEmployeeId"));
                        String travelBeginDate = Util.null2String(orderBaseInfo.get("travelBeginDate"));
                        String travelEndDate = Util.null2String(orderBaseInfo.get("travelEndDate"));
                        String orderAmount = Util.null2String(orderBaseInfo.get("orderAmount"));
                        String diffPrice = Util.null2String(orderBaseInfo.get("diffPrice"));
                        //行程目的地信息
                        JSONObject segmentInfoobj = flightOrderInfo.getJSONArray("segmentInfoList").getJSONObject(0);
                        String flightNo = Util.null2String(segmentInfoobj.get("flightNo"));
                        String departCity = Util.null2String(segmentInfoobj.get("departCity"));
                        String departTime = Util.null2String(segmentInfoobj.get("departTime"));
                        String arriveTime = Util.null2String(segmentInfoobj.get("arriveTime"));
                        String arriveCity = Util.null2String(segmentInfoobj.get("arriveCity"));



                        Object[] prams = new Object[19];
                        prams[0] = passengerName;
                        prams[1] = outEmployeeId;
                        prams[2] = seatClass;
                        prams[3] = orderSerialNo1;
                        prams[4] = orderStatus;
                        prams[5] = orderStatusDes;
                        prams[6] = bookDate;
                        prams[7] = journeyNo;
                        prams[8] = bookerEmployeeName;
                        prams[9] = bookerEmployeeId;
                        prams[10] = travelBeginDate;
                        prams[11] = travelEndDate;
                        prams[12] = orderAmount;
                        prams[13] = diffPrice;
                        prams[14] = flightNo;
                        prams[15] = departCity;
                        prams[16] = departTime;
                        prams[17] = arriveTime;
                        prams[18] = arriveCity;

                        if (flightorderlist.indexOf(orderSerialNo1) > -1) {
                            String id = flightordermap.get(orderSerialNo1);
                            //存在 更新
                            String updatesql = "update uf_cctz_dt4 set passengerName='" + passengerName
                                    + "',mainid='" + mainid+ "',outEmployeeId='" + outEmployeeId + "',seatClass='" + seatClass + "',orderSerialNo='" + orderSerialNo1 +"',orderStatus='" + orderStatus + "',orderStatusDes='" + orderStatusDes + "' ,bookDate='" + bookDate + "',journeyNo='" + journeyNo   + "',bookerEmployeeName='" + bookerEmployeeName  + "',bookerEmployeeId='" + bookerEmployeeId   + "',travelBeginDate='" + travelBeginDate + "',travelEndDate='" + travelEndDate   + "',orderAmount='" + orderAmount   + "',diffPrice='" + diffPrice   + "',flightNo='" + flightNo + "',departCity='" + departCity   + "',departTime='" + departTime + "',arriveTime='" + arriveTime + "',arriveCity='" + arriveCity +  "' where id='" + id +"'";
                            boolean flag = rs.executeSql(updatesql);
                            newwlmap.put("sql", updatesql + prams.toString());
                            newwlmap.put("lx", "机票(正常)");
                            newwlmap.put("orderSerialNo1", orderSerialNo1);
                            if ("".equals(orderSerialNo1)) {
                                sqlflag = false;
                                newwlmap.put("status", "E");
                                newwlmap.put("msg", "更新失败，主键为空！");
                                errors += "更新失败，主键为空!\n";
                            } else {
                                if (!flag) {
                                    sqlflag = false;
                                    newwlmap.put("status", "E");
                                    newwlmap.put("msg", "更新失败");
                                    errors += "机票(正常)单号:" + orderSerialNo1 + "更新失败\n";
                                } else {
                                    newwlmap.put("status", "S");
                                    newwlmap.put("msg", "更新成功");

                                }
                            }
                        } else {
                            //不存在 新增
                            String value = "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
                            String paramkey="mainid,passengerName,outEmployeeId,seatClass,orderSerialNo,orderStatus,orderStatusDes,bookDate,journeyNo,bookerEmployeeName,bookerEmployeeId,travelBeginDate,travelEndDate,orderAmount,diffPrice,flightNo,departCity,departTime,arriveTime,arriveCity";
                            String insertsql = "insert into uf_cctz_dt4  ("+paramkey+") values ("
                                    + mainid+","+value+")";
                            boolean flag = rs.executeUpdate(insertsql, prams);
                            newwlmap.put("sql", insertsql + prams.toString());
                            newwlmap.put("lx", "机票(正常)");
                            newwlmap.put("orderSerialNo1", orderSerialNo1);
                            if ("".equals(orderSerialNo1)) {
                                sqlflag = false;
                                newwlmap.put("status", "E");
                                newwlmap.put("msg", "插入失败，主键为空！");
                                errors += "插入失败，主键为空!\n";
                            } else {
                                if (!flag) {
                                    sqlflag = false;
                                    newwlmap.put("status", "E");
                                    newwlmap.put("msg", "插入失败");
                                    errors += "机票(正常)单号:" + orderSerialNo1 + "插入失败\n";
                                } else {
                                    newwlmap.put("status", "S");
                                    newwlmap.put("msg", "插入成功");
                                }
                            }
                            //插入缓存
                            insertsql = "select id from uf_cctz_dt4 where orderSerialNo  = ?";
                            flag = rs.executeQuery(insertsql, orderSerialNo1);
                            if (!flag) {
                                sqlflag = false;
                                newwlmap.put("status", "E");
                                newwlmap.put("msg", "插入缓存失败");
                                errors += "机票(正常)单号:" + orderSerialNo1 + "插入缓存失败\n";
                            } else {
                                newwlmap.put("status", "S");
                                newwlmap.put("msg", "插入缓存成功");
                            }
                            String id = "";
                            if (rs.next()) {
                                id = Util.null2String(rs.getString("id"));
                            }
                            flightordermap.put(Util.null2String(orderSerialNo1), id);
                            flightorderlist.add(Util.null2String(orderSerialNo1));
                        }
                        newlist.add(newwlmap);


                        //插入明细4改签状态机票信息
                        JSONArray flightOrderChangedInfos = flightOrderInfoobj.getJSONArray("flightOrderChangedInfos");
                        if (flightOrderChangedInfos != null && flightOrderChangedInfos.size() > 0) {
                            for (int k = 0; k < flightOrderChangedInfos.size(); k++) {
                                /**
                                 *   ∟flightOrderChangedInfos	jsonarr	否	改签状态机票信息
                                 *     ∟orderBaseInfo	jsonobj	否	订单基本信息
                                 *       ∟orderSerialNo	string	否	订单号
                                 *       ∟orderStatus	string	否	订单状态
                                 *       ∟orderStatusDes	string	否	订单状态描述
                                 *       ∟bookDate	string	否	下单时间
                                 *       ∟journeyNo	string	否	行程单号
                                 *       ∟bookerEmployeeName	string	否	预订人姓名
                                 *       ∟bookerEmployeeId	string	否	预订人id
                                 *       ∟travelBeginDate	string	否	行程开始时间
                                 *       ∟travelEndDate	string	否	行程结束时间
                                 *       ∟orderAmount	string	否	订单金额
                                 *       ∟diffPrice	string	否	订单金额变更值
                                 *     ∟segmentInfoList	jsonarr	否	改签机票订单目的地信息
                                 *       ∟flightNo	string	否	flightNo
                                 *       ∟departCity	string	否	出发城市
                                 *       ∟departTime	string	否	出发时间
                                 *       ∟arriveTime	string	否	到达时间
                                 *       ∟arriveCity	string	否	到达城市
                                 */
                                Map<String, Object> newwlmap1 = new HashMap<>();
                                JSONObject flightOrderChangedInfo = flightOrderChangedInfos.getJSONObject(j);
                                //订单基本信息
                                JSONObject chg_orderBaseInfo = flightOrderChangedInfo.getJSONObject("orderBaseInfo");
                                String orderSerialNo2 = Util.null2String(chg_orderBaseInfo.get("orderSerialNo"));
                                String chg_orderStatus = Util.null2String(chg_orderBaseInfo.get("orderStatus"));
                                String chg_orderStatusDes = Util.null2String(chg_orderBaseInfo.get("ssxm"));
                                String chg_bookDate = Util.null2String(chg_orderBaseInfo.get("bookDate"));
                                String chg_journeyNo = Util.null2String(chg_orderBaseInfo.get("journeyNo"));
                                String chg_bookerEmployeeName = Util.null2String(chg_orderBaseInfo.get("bookerEmployeeName"));
                                String chg_bookerEmployeeId = Util.null2String(chg_orderBaseInfo.get("bookerEmployeeId"));
                                String chg_travelBeginDate = Util.null2String(chg_orderBaseInfo.get("travelBeginDate"));
                                String chg_travelEndDate = Util.null2String(chg_orderBaseInfo.get("travelEndDate"));
                                String chg_orderAmount = Util.null2String(chg_orderBaseInfo.get("orderAmount"));
                                String chg_diffPrice = Util.null2String(chg_orderBaseInfo.get("diffPrice"));
                                //改签机票订单目的地信息
                                JSONObject chg_segmentInfoObj = flightOrderChangedInfo.getJSONArray("segmentInfoList").getJSONObject(0);
                                String chg_flightNo = Util.null2String(chg_segmentInfoObj.get("flightNo"));
                                String chg_departCity = Util.null2String(chg_segmentInfoObj.get("departCity"));
                                String chg_departTime = Util.null2String(chg_segmentInfoObj.get("departTime"));
                                String chg_arriveTime = Util.null2String(chg_segmentInfoObj.get("arriveTime"));
                                String chg_arriveCity = Util.null2String(chg_segmentInfoObj.get("arriveCity"));


                                Object[] chg_prams = new Object[16];
                                chg_prams[0] = orderSerialNo2;
                                chg_prams[1] = chg_orderStatus;
                                chg_prams[2] = chg_orderStatusDes;
                                chg_prams[3] = chg_bookDate;
                                chg_prams[4] = chg_journeyNo;
                                chg_prams[5] = chg_bookerEmployeeName;
                                chg_prams[6] = chg_bookerEmployeeId;
                                chg_prams[7] = chg_travelBeginDate;
                                chg_prams[8] = chg_travelEndDate;
                                chg_prams[9] = chg_orderAmount;
                                chg_prams[10] = chg_diffPrice;
                                chg_prams[11] = chg_flightNo;
                                chg_prams[12] = chg_departCity;
                                chg_prams[13] = chg_departTime;
                                chg_prams[14] = chg_arriveTime;
                                chg_prams[15] = chg_arriveCity;
                                if (flightorderchangelist.indexOf(orderSerialNo2) > -1) {
                                    String id = flightorderchangemap.get(orderSerialNo2);
                                    //存在 更新
                                    String updatesql = "update uf_cctz_dt5 set mainid='"+mainid+"',orderSerialNo=?, orderStatus=?, orderStatusDes=?, bookDate=?, journeyNo=?, bookerEmployeeName=?, bookerEmployeeId=?, travelBeginDate=?, travelEndDate=?, orderAmount=?, diffPrice=?, flightNo=?, departCity=?, departTime=?, arriveTime=?, arriveCity=?  where id='" + id +"'";
                                    boolean flag = rs.executeUpdate(updatesql, chg_prams);
                                    newwlmap1.put("sql", updatesql + chg_prams.toString());
                                    newwlmap1.put("lx", "机票(改签)");
                                    newwlmap1.put("orderSerialNo2", orderSerialNo2);
                                    if ("".equals(orderSerialNo2)) {
                                        sqlflag = false;
                                        newwlmap1.put("status", "E");
                                        newwlmap1.put("msg", "更新失败，主键为空！");
                                        errors += "更新失败，主键为空!\n";
                                    } else {
                                        if (!flag) {
                                            sqlflag = false;
                                            newwlmap1.put("status", "E");
                                            newwlmap1.put("msg", "更新失败");
                                            errors += "机票(改签)单号:" + orderSerialNo2 + "更新失败\n";
                                        } else {
                                            newwlmap1.put("status", "S");
                                            newwlmap1.put("msg", "更新成功");
                                        }
                                    }
                                } else {
                                    //不存在 新增
                                    String value = "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
                                    String chgpramvalue="mainid,orderSerialNo,orderStatus,orderStatusDes,bookDate,journeyNo,bookerEmployeeName,bookerEmployeeId,travelBeginDate,travelEndDate,orderAmount,diffPrice,flightNo,departCity,departTime,arriveTime,arriveCity";
                                    String insertsql = "insert into uf_cctz_dt5  ("+chgpramvalue+") values (" +mainid+","+ value + " " + ")";
                                    boolean flag = rs.executeUpdate(insertsql, chg_prams);
                                    newwlmap1.put("sql", insertsql + chg_prams.toString());
                                    newwlmap1.put("lx", "机票(改签)");
                                    newwlmap1.put("orderSerialNo2", orderSerialNo2);
                                    if ("".equals(orderSerialNo2)) {
                                        sqlflag = false;
                                        newwlmap1.put("status", "E");
                                        newwlmap1.put("msg", "插入失败，主键为空！");
                                        errors += "插入失败，主键为空!\n";
                                    } else {
                                        //插入缓存
                                        if (flag) {
                                            String selectsql = "select id from uf_cctz_dt5 where orderSerialNo=?";
                                            RS.executeQuery(selectsql, orderSerialNo2);
                                            String id = "";
                                            if (RS.next()) {
                                                id = Util.null2String(RS.getString("id"));
                                            }
                                            flightorderchangemap.put(Util.null2String(orderSerialNo2), id);
                                            newwlmap1.put("status", "S");
                                            newwlmap1.put("msg", "插入成功");
                                            flightorderchangelist.add(Util.null2String(orderSerialNo2));
                                        } else {
                                            sqlflag = false;
                                            newwlmap1.put("status", "E");
                                            newwlmap1.put("msg", "插入失败");
                                            errors += "机票(改签)单号:" + orderSerialNo2 + "缓存插入失败\n";
                                        }
                                    }
                                }
                                newlist.add(newwlmap1);
                            }
                        }


                    }
                }



                //插入明细6酒店入住信息
                JSONArray hotelOrderInfoList = listobj.getJSONArray("hotelOrderInfoList");
                if (hotelOrderInfoList != null && hotelOrderInfoList.size() > 0) {
                    for (int j = 0; j < hotelOrderInfoList.size(); j++) {
                        /**
                         *
                         *   ∟hotelOrderInfoList	jsonarr	否	酒店信息
                         *     ∟orderBaseInfo	jsonobj	否	订单基础信息
                         *       ∟orderSerialNo	string	否	订单号
                         *       ∟orderStatus	string	否	订单状态
                         *       ∟orderStatusDes	string	否	订单状态描述
                         *       ∟bookDate	string	否	下单时间
                         *       ∟journeyNo	string	否	行程单号
                         *       ∟bookerEmployeeName	string	否	预订人姓名
                         *       ∟bookerEmployeeId	string	否	预订人id
                         *       ∟travelBeginDate	string	否	酒店入住时间
                         *       ∟travelEndDate	string	否	酒店离店时间
                         *       ∟orderAmount	string	否	订单金额
                         *       ∟diffPrice	string	否	订单金额变更值
                         *     ∟hotelPassengerInfos	jsonarr	否	酒店订单乘客信息
                         *       ∟passengerName	string	否	入住人
                         *       ∟outEmployeeId	string	否	入住人id
                         *     ∟hotelName	string	否	酒店名
                         *     ∟hotelCityName	string	否	酒店城市
                         *     ∟roomName	string	否	房型
                         *     ∟bedType	string	否	床型
                         *     ∟breakfast	string	否	早餐
                         *     ∟roomDays	string	否	间夜数
                         *     ∟dayPrice	string	否	单价
                         */
                        Map<String, Object> newwlmap = new HashMap<>();
                        //酒店基本信息
                        JSONObject hotelOrderInfoObj = hotelOrderInfoList.getJSONObject(j);
                        String hotelName = Util.null2String(hotelOrderInfoObj.get("hotelName"));
                        String hotelCityName = Util.null2String(hotelOrderInfoObj.get("hotelCityName"));
                        String roomName = Util.null2String(hotelOrderInfoObj.get("roomName"));
                        String bedType = Util.null2String(hotelOrderInfoObj.get("bedType"));
                        String breakfast = Util.null2String(hotelOrderInfoObj.get("breakfast"));
                        String roomDays = Util.null2String(hotelOrderInfoObj.get("roomDays"));
                        String dayPrice = Util.null2String(hotelOrderInfoObj.get("dayPrice"));
                        //酒店订单基本信息
                        JSONObject orderBaseInfo=hotelOrderInfoObj.getJSONObject("orderBaseInfo");
                        String orderSerialNo=orderBaseInfo.getString("orderSerialNo");
                        String orderStatus=orderBaseInfo.getString("orderStatus");
                        String orderStatusDes=orderBaseInfo.getString("orderStatusDes");
                        String bookDate=orderBaseInfo.getString("bookDate");
                        // String journeyNo=orderBaseInfo.getString("journeyNo");
                        String bookerEmployeeName=orderBaseInfo.getString("bookerEmployeeName");
                        String bookerEmployeeId=orderBaseInfo.getString("bookerEmployeeId");
                        String travelBeginDate=orderBaseInfo.getString("travelBeginDate");
                        String travelEndDate=orderBaseInfo.getString("travelEndDate");
                        String orderAmount=orderBaseInfo.getString("orderAmount");
                        String diffPrice=orderBaseInfo.getString("diffPrice");
                        //酒店入住人信息
                        JSONArray hotelPassengerInfos = hotelOrderInfoObj.getJSONArray("hotelPassengerInfos");
                        JSONObject hotelPassengerInfo=new JSONObject();
                        if (hotelPassengerInfos != null && hotelPassengerInfos.size() > 0) {
                            hotelPassengerInfo = hotelPassengerInfos.getJSONObject(0);
                            // 在这里处理 hotelPassengerInfo 对象
                        }
                        String passengerName=hotelPassengerInfo.getString("passengerName");
                        String outEmployeeId=hotelPassengerInfo.getString("outEmployeeId");

                        Object[] prams = new Object[20];
                        prams[0] = hotelName;
                        prams[1] = hotelCityName;
                        prams[2] = roomName;
                        prams[3] = bedType;
                        prams[4] = breakfast;
                        prams[5] = roomDays;
                        prams[6] = dayPrice;
                        prams[7] = orderSerialNo;
                        prams[8] = orderStatus;
                        prams[9] = orderStatusDes;
                        prams[10] = bookDate;
                        prams[11] = journeyNo;
                        prams[12] = bookerEmployeeName;
                        prams[13] = bookerEmployeeId;
                        prams[14] = travelBeginDate;
                        prams[15] = travelEndDate;
                        prams[16] = orderAmount;
                        prams[17] = diffPrice;
                        prams[18] = passengerName;
                        prams[19] = outEmployeeId;

                        if (hotelorderlist.indexOf(orderSerialNo) > -1) {
                            String id = hotelordermap.get(orderSerialNo);
                            ret.put("hotelordermap", hotelordermap);
                            //存在 更新
                            String updatesql = "update uf_cctz_dt6 set mainid='"+mainid+"',hotelName=?,hotelCityName=?,roomName=?,bedType=?,breakfast=?,roomDays=?,dayPrice=?,orderSerialNo=?,orderStatus=?,orderStatusDes=?,bookDate=?,journeyNo=?,bookerEmployeeName=?,bookerEmployeeId=?,travelBeginDate=?,travelEndDate=?,orderAmount=?,diffPrice=?,passengerName=?,outEmployeeId=? where id='" + id + "'";
                            boolean flag = rs.executeUpdate(updatesql, prams);
                            newwlmap.put("sql", updatesql + prams.toString());
                            newwlmap.put("lx", "酒店");
                            newwlmap.put("orderSerialNo", orderSerialNo);
                            if ("".equals(orderSerialNo)) {
                                sqlflag = false;
                                newwlmap.put("status", "E");
                                newwlmap.put("msg", "更新失败，主键为空！");
                                errors += "更新失败，主键为空!\n";
                            } else {
                                if (!flag) {
                                    sqlflag = false;
                                    newwlmap.put("status", "E");
                                    newwlmap.put("msg", "更新失败");
                                    errors += "酒店订单编号:" + orderSerialNo + "更新失败\n";
                                } else {
                                    newwlmap.put("status", "S");
                                    newwlmap.put("msg", "更新成功");
                                }
                            }
                        } else {
                            //不存在 新增
                            String value = "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
                            String hotelparamvalue="mainid,hotelName,hotelCityName,roomName,bedType,breakfast,roomDays,dayPrice,orderSerialNo,orderStatus,orderStatusDes,bookDate,journeyNo,bookerEmployeeName,bookerEmployeeId,travelBeginDate,travelEndDate,orderAmount,diffPrice,passengerName,outEmployeeId";
                            String insertsql = "insert into uf_cctz_dt6  ("+hotelparamvalue+") values ("+ mainid  +","+ value+ ")";
                            boolean flag = rs.executeUpdate(insertsql, prams);
                            newwlmap.put("sql", insertsql + prams.toString());
                            newwlmap.put("lx", "酒店");
                            newwlmap.put("orderSerialNo", orderSerialNo);
                            //插入缓存
                            if ("".equals(orderSerialNo)) {
                                sqlflag = false;
                                newwlmap.put("status", "E");
                                newwlmap.put("msg", "插入失败，主键为空！");
                                errors += "插入失败，主键为空!\n";
                            } else {
                                if (flag) {
                                    String selectsql = "select id from uf_cctz_dt6 where orderSerialNo  = ?";
                                    RS.executeQuery(selectsql, orderSerialNo);
                                    String id = "";
                                    if (RS.next()) {
                                        id = Util.null2String(RS.getString("id"));
                                    }
                                    hotelordermap.put(Util.null2String(orderSerialNo), mainid);
                                    hotelorderlist.add(Util.null2String(orderSerialNo));
                                    newwlmap.put("status", "S");
                                    newwlmap.put("msg", "插入成功");
                                } else {
                                    sqlflag = false;
                                    newwlmap.put("status", "E");
                                    newwlmap.put("msg", "插入失败");
                                    errors += "酒店订单编号:" + orderSerialNo + "插入失败\n";
                                }
                            }
                        }
                        newlist.add(newwlmap);
                    }
                }

                ////插入明细7火车票订单信息
                JSONArray trainOrderInfoList = listobj.getJSONArray("trainOrderInfoList");
                if (trainOrderInfoList != null && trainOrderInfoList.size() > 0) {
                    for (int j = 0; j < trainOrderInfoList.size(); j++) {
                        /**
                         *
                         ∟trainOrderInfoList	jsonarr	否	火车票订单信息
                         ∟orderBaseInfo	jsonobj	否	订单基础信息
                         ∟orderSerialNo	string	否	订单号
                         ∟orderStatus	string	否	订单状态
                         ∟orderStatusDes	string	否	订单状态描述
                         ∟bookDate	string	否	下单时间
                         ∟journeyNo	string	否	行程单号
                         ∟bookerEmployeeName	string	否	预订人姓名
                         ∟bookerEmployeeId	string	否	预订人id
                         ∟travelBeginDate	string	否	行程开始时间
                         ∟travelEndDate	string	否	行程结束时间
                         ∟orderAmount	string	否	订单金额
                         ∟diffPrice	string	否	订单金额变更
                         ∟passengerInfo	jsonobj	否	乘客信息
                         ∟orderTicket	jsonarr	否	订票信息（废弃）
                         ∟trainNo	string
                         ∟fromStation	string
                         ∟toStation	string
                         ∟fromCityName	string
                         ∟toCityName	string
                         ∟orderAmount	string
                         ∟seatClass	string
                         ∟orderStatus	string
                         ∟orderStatusDes	string
                         ∟travelBeginDate	string
                         ∟travelEndDate	string
                         ∟passengerName	string	否	乘客姓名
                         ∟outEmployeeId	string	否	乘客id
                         ∟seatClass	string	否	座位等级
                         ∟trainNo	string	否	车次
                         ∟fromStation	string	否	出发站点
                         ∟toStation	string	否	到达站点
                         ∟fromCityName	string	否	出发城市
                         ∟toCityName	string	否	到达城市
                         */
                        Map<String, Object> newwlmap = new HashMap<>();
                        //火车票基础信息
                        JSONObject trainOrderInfoobj = trainOrderInfoList.getJSONObject(j);
                        String trainNo = Util.null2String(trainOrderInfoobj.get("trainNo"));
                        String fromStation = Util.null2String(trainOrderInfoobj.get("fromStation"));
                        String toStation = Util.null2String(trainOrderInfoobj.get("toStation"));
                        String fromCityName = Util.null2String(trainOrderInfoobj.get("fromCityName"));
                        String toCityName = Util.null2String(trainOrderInfoobj.get("toCityName"));
                        //火车订单基础信息
                        JSONObject orderBaseInfo=trainOrderInfoobj.getJSONObject("orderBaseInfo");
                        String orderSerialNo = Util.null2String(orderBaseInfo.get("orderSerialNo"));
                        String orderStatus = Util.null2String(orderBaseInfo.get("orderStatus"));
                        String orderStatusDes = Util.null2String(orderBaseInfo.get("orderStatusDes"));
                        String bookDate = Util.null2String(orderBaseInfo.get("bookDate"));
//                        String journeyNo = Util.null2String(orderBaseInfo.get("orderSerialNo"));
                        String bookerEmployeeName = Util.null2String(orderBaseInfo.get("bookerEmployeeName"));
                        String bookerEmployeeId = Util.null2String(orderBaseInfo.get("bookerEmployeeId"));
                        String travelBeginDate = Util.null2String(orderBaseInfo.get("travelBeginDate"));
                        String travelEndDate = Util.null2String(orderBaseInfo.get("travelEndDate"));
                        String orderAmount = Util.null2String(orderBaseInfo.get("orderAmount"));
                        String diffPrice = Util.null2String(orderBaseInfo.get("diffPrice"));
                        //乘客信息
                        JSONObject passengerInfo=trainOrderInfoobj.getJSONObject("passengerInfo");
                        String passengerName = "";
                        String outEmployeeId = "";
                        String seatClass = "";
                        if(passengerInfo!=null&&passengerInfo.size()>0) {
                            passengerName = Util.null2String(passengerInfo.get("passengerName"));
                            outEmployeeId = Util.null2String(passengerInfo.get("outEmployeeId"));
                            seatClass = Util.null2String(passengerInfo.get("seatClass"));
                        }

                        Object[] prams = new Object[19];
                        prams[0] = orderSerialNo;
                        prams[1] = orderStatus;
                        prams[2] = orderStatusDes;
                        prams[3] = bookDate;
                        prams[4] = journeyNo;
                        prams[5] = bookerEmployeeName;
                        prams[6] = bookerEmployeeId;
                        prams[7] = travelBeginDate;
                        prams[8] = travelEndDate;
                        prams[9] = orderAmount;
                        prams[10] = diffPrice;
                        prams[11] = passengerName;
                        prams[12] = outEmployeeId;
                        prams[13] = seatClass;
                        prams[14] = trainNo;
                        prams[15] = fromStation;
                        prams[16] = toStation;
                        prams[17] = fromCityName;
                        prams[18] = toCityName;

                        if (trainorderlist.indexOf(orderSerialNo) > -1) {
                            String id = trainordermap.get(orderSerialNo);
                            //存在 更新
                            String updatesql = "update uf_cctz_dt7 set mainid='"+mainid+"',orderSerialNo=?,orderStatus=?,orderStatusDes=?,bookDate=?,journeyNo=?,bookerEmployeeName=?,bookerEmployeeId=?,travelBeginDate=?,travelEndDate=?,orderAmount=?,diffPrice=?,passengerName=?,outEmployeeId=?,seatClass=?,trainNo=?,fromStation=?,toStation=?,fromCityName=?,toCityName=? where id='" + id + "'";
                            boolean flag = rs.executeUpdate(updatesql, prams);
                            newwlmap.put("sql", updatesql + prams.toString());
                            newwlmap.put("lx", "火车");
                            newwlmap.put("orderSerialNo", orderSerialNo);
                            if ("".equals(orderSerialNo)) {
                                sqlflag = false;
                                newwlmap.put("status", "E");
                                newwlmap.put("msg", "更新失败，主键为空！");
                                errors += "更新失败，主键为空!\n";
                            } else {
                                if (!flag) {
                                    sqlflag = false;
                                    newwlmap.put("status", "E");
                                    newwlmap.put("msg", "更新失败");
                                    errors += "火车订单编号:" + orderSerialNo + "更新失败\n";
                                } else {
                                    newwlmap.put("status", "S");
                                    newwlmap.put("msg", "更新成功");
                                }
                            }
                        } else {
                            //不存在 新增
                            String value = "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
                            String trainpramvalue="mainid,orderSerialNo,orderStatus,orderStatusDes,bookDate,journeyNo,bookerEmployeeName,bookerEmployeeId,travelBeginDate,travelEndDate,orderAmount,diffPrice,passengerName,outEmployeeId,seatClass,trainNo,fromStation,toStation,fromCityName,toCityName";
                            String insertsql = "insert into uf_cctz_dt7  ("+trainpramvalue+") values (" + mainid+","+value +")";
                            boolean flag = rs.executeUpdate(insertsql, prams);
                            newwlmap.put("sql", insertsql + prams.toString());
                            newwlmap.put("lx", "火车");
                            newwlmap.put("orderSerialNo", orderSerialNo);
                            //插入缓存
                            if ("".equals(orderSerialNo)) {
                                sqlflag = false;
                                newwlmap.put("status", "E");
                                newwlmap.put("msg", "插入失败，主键为空！");
                                errors += "插入失败，主键为空!\n";
                            } else {
                                if (flag) {
                                    String selectsql = "select id from uf_cctz_dt7 where orderSerialNo=?";
                                    RS.executeQuery(selectsql, orderSerialNo);
                                    String id = "";
                                    if (RS.next()) {
                                        id = Util.null2String(RS.getString("id"));
                                    }
                                    trainordermap.put(orderSerialNo, id);
                                    trainorderlist.add(orderSerialNo);
                                    newwlmap.put("status", "S");
                                    newwlmap.put("msg", "插入成功");
                                } else {
                                    sqlflag = false;
                                    newwlmap.put("status", "E");
                                    newwlmap.put("msg", "插入失败");
                                    errors += "火车订单编号:" + orderSerialNo + "插入失败\n";
                                }
                            }
                        }
                        newlist.add(newwlmap);
                    }
                }

                ////插入明细8用车信息
                JSONArray carOrderInfoList = listobj.getJSONArray("carOrderInfoList");
                if (carOrderInfoList != null && carOrderInfoList.size() > 0) {
                    for (int j = 0; j < carOrderInfoList.size(); j++) {
                        /**
                         *
                         ∟carOrderInfoList	jsonarr	否	汽车票订单信息
                           ∟orderBaseInfo	jsonobj	否	订单基础信息
                             ∟orderSerialNo	string	否	订单号
                             ∟orderStatus	string	否	订单状态
                             ∟orderStatusDes	string	否	订单状态描述
                             ∟bookDate	string	否	下单时间
                             ∟journeyNo	string	否	行程单号
                             ∟bookerEmployeeName	string	否	预订人姓名
                             ∟bookerEmployeeId	string	否	预订人id
                             ∟travelBeginDate	string	否	行程开始时间
                             ∟travelEndDate	string	否	行程结束时间
                             ∟orderAmount	string	否	订单金额
                             ∟diffPrice	string	否	订单金额变更
                           ∟passengerInfoList	jsonarr	否	乘客信息
                             ∟passengerName	string	否	乘客姓名
                             ∟outEmployeeId	string	否	乘客id
                             ∟seatClass	string	否	车型
                           ∟sceneName	string	否	用车场景
                           ∟flightNo	string	否	关联航班
                           ∟fromStation	string	否	出发站点
                           ∟toStation	string	否	到达站点
                           ∟fromCityName	string	否	出发城市
                           ∟toCityName	string	否	到达城市
                           ∟startAddressDetail	string	否	开始用车详细地址
                           ∟endAddressDetail	string	否	结束用车详细地址
                           ∟startCityName	string	否	开始城市
                           ∟endCityName	string	否	结束城市
                           ∟reason	string	否	取消原因
                           ∟carSupplier	string	否	汽车供应商
                         */
                        Map<String, Object> newwlmap = new HashMap<>();
                        //火车票基础信息
                        JSONObject carOrderInfoobj = carOrderInfoList.getJSONObject(j);
                        String sceneName = Util.null2String(carOrderInfoobj.get("sceneName"));
                        String flightNo = Util.null2String(carOrderInfoobj.get("flightNo"));
                        String fromStation = Util.null2String(carOrderInfoobj.get("fromStation"));
                        String toStation = Util.null2String(carOrderInfoobj.get("toStation"));
                        String fromCityName = Util.null2String(carOrderInfoobj.get("fromCityName"));
                        String toCityName = Util.null2String(carOrderInfoobj.get("toCityName"));
                        String startAddressDetail = Util.null2String(carOrderInfoobj.get("startAddressDetail"));
                        String endAddressDetail = Util.null2String(carOrderInfoobj.get("endAddressDetail"));
                        String startCityName = Util.null2String(carOrderInfoobj.get("startCityName"));
                        String endCityName = Util.null2String(carOrderInfoobj.get("endCityName"));
                        String reason = Util.null2String(carOrderInfoobj.get("reason"));
                        String carSupplier = Util.null2String(carOrderInfoobj.get("carSupplier"));
                        //火车订单基础信息
                        JSONObject orderBaseInfo=carOrderInfoobj.getJSONObject("orderBaseInfo");
                        String orderSerialNo = Util.null2String(orderBaseInfo.get("orderSerialNo"));
                        String orderStatus = Util.null2String(orderBaseInfo.get("orderStatus"));
                        String orderStatusDes = Util.null2String(orderBaseInfo.get("orderStatusDes"));
                        String bookDate = Util.null2String(orderBaseInfo.get("bookDate"));
//                        String journeyNo = Util.null2String(orderBaseInfo.get("orderSerialNo"));
                        String bookerEmployeeName = Util.null2String(orderBaseInfo.get("bookerEmployeeName"));
                        String bookerEmployeeId = Util.null2String(orderBaseInfo.get("bookerEmployeeId"));
                        String travelBeginDate = Util.null2String(orderBaseInfo.get("travelBeginDate"));
                        String travelEndDate = Util.null2String(orderBaseInfo.get("travelEndDate"));
                        String orderAmount = Util.null2String(orderBaseInfo.get("orderAmount"));
                        String diffPrice = Util.null2String(orderBaseInfo.get("diffPrice"));
                        //乘客信息
                        JSONArray passengerInfoList=carOrderInfoobj.getJSONArray("passengerInfoList");
                        String passengerName = "";
                        String outEmployeeId = "";
                        String seatClass = "";
                        if(passengerInfoList!=null&&passengerInfoList.size()>0) {
                            JSONObject passengerInfo=passengerInfoList.getJSONObject(0);
                            if(passengerInfo!=null&&passengerInfo.size()>0) {
                                passengerName = Util.null2String(passengerInfo.get("passengerName"));
                                outEmployeeId = Util.null2String(passengerInfo.get("outEmployeeId"));
                                seatClass = Util.null2String(passengerInfo.get("seatClass"));
                            }
                        }


                        Object[] prams = new Object[26];
                        prams[0] = orderSerialNo;
                        prams[1] = orderStatus;
                        prams[2] = orderStatusDes;
                        prams[3] = bookDate;
                        prams[4] = journeyNo;
                        prams[5] = bookerEmployeeName;
                        prams[6] = bookerEmployeeId;
                        prams[7] = travelBeginDate;
                        prams[8] = travelEndDate;
                        prams[9] = orderAmount;
                        prams[10] = diffPrice;
                        prams[11] = passengerName;
                        prams[12] = outEmployeeId;
                        prams[13] = seatClass;
                        prams[14] = sceneName;
                        prams[15] = flightNo;
                        prams[16] = fromStation;
                        prams[17] = toStation;
                        prams[18] = fromCityName;
                        prams[19] = toCityName;
                        prams[20] = startAddressDetail;
                        prams[21] = endAddressDetail;
                        prams[22] = startCityName;
                        prams[23] = endCityName;
                        prams[24] = reason;
                        prams[25] = carSupplier;

                        if (carorderlist.indexOf(orderSerialNo) > -1) {
                            String id = carordermap.get(orderSerialNo);
                            //存在 更新
                            String updatesql = "update uf_cctz_dt8 set mainid='"+mainid+"',orderSerialNo=?,orderStatus=?,orderStatusDes=?,bookDate=?,journeyNo=?,bookerEmployeeName=?,bookerEmployeeId=?,travelBeginDate=?,travelEndDate=?,orderAmount=?,diffPrice=?,passengerName=?,outEmployeeId=?,seatClass=?,sceneName=?,flightNo=?,fromStation=?,toStation=?,fromCityName=?,toCityName=?,startAddressDetail=?,endAddressDetail=?,startCityName=?,endCityName=?,reason=?,carSupplier=? where id='" + id + "'";
                            boolean flag = rs.executeUpdate(updatesql, prams);
                            newwlmap.put("sql", updatesql + prams.toString());
                            newwlmap.put("lx", "汽车");
                            newwlmap.put("orderSerialNo", orderSerialNo);
                            if ("".equals(orderSerialNo)) {
                                sqlflag = false;
                                newwlmap.put("status", "E");
                                newwlmap.put("msg", "更新失败，主键为空！");
                                errors += "更新失败，主键为空!\n";
                            } else {
                                if (!flag) {
                                    sqlflag = false;
                                    newwlmap.put("status", "E");
                                    newwlmap.put("msg", "更新失败");
                                    errors += "汽车订单编号:" + orderSerialNo + "更新失败\n";
                                } else {
                                    newwlmap.put("status", "S");
                                    newwlmap.put("msg", "更新成功");
                                }
                            }
                        } else {
                            //不存在 新增
                            String value = "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
                            String trainpramvalue="mainid,orderSerialNo,orderStatus,orderStatusDes,bookDate,journeyNo,bookerEmployeeName,bookerEmployeeId,travelBeginDate,travelEndDate,orderAmount,diffPrice,passengerName,outEmployeeId,seatClass,sceneName,flightNo,fromStation,toStation,fromCityName,toCityName,startAddressDetail,endAddressDetail,startCityName,endCityName,reason,carSupplier ";
                            String insertsql = "insert into uf_cctz_dt8  ("+trainpramvalue+") values (" + mainid+","+value +")";
                            boolean flag = rs.executeUpdate(insertsql, prams);
                            newwlmap.put("sql", insertsql + prams.toString());
                            newwlmap.put("lx", "汽车");
                            newwlmap.put("orderSerialNo", orderSerialNo);
                            //插入缓存
                            if ("".equals(orderSerialNo)) {
                                sqlflag = false;
                                newwlmap.put("status", "E");
                                newwlmap.put("msg", "插入失败，主键为空！");
                                errors += "插入失败，主键为空!\n";
                            } else {
                                if (flag) {
                                    String selectsql = "select id from uf_cctz_dt8 where orderSerialNo=?";
                                    RS.executeQuery(selectsql, orderSerialNo);
                                    String id = "";
                                    if (RS.next()) {
                                        id = Util.null2String(RS.getString("id"));
                                    }
                                    carordermap.put(orderSerialNo, id);
                                    carorderlist.add(orderSerialNo);
                                    newwlmap.put("status", "S");
                                    newwlmap.put("msg", "插入成功");
                                } else {
                                    sqlflag = false;
                                    newwlmap.put("status", "E");
                                    newwlmap.put("msg", "插入失败");
                                    errors += "汽车订单编号:" + orderSerialNo + "插入失败\n";
                                }
                            }
                        }
                        newlist.add(newwlmap);
                    }
                }


            }
        }
        if (sqlflag) {
            ret.put("code", "S");
            ret.put("msg", "成功");
            ret.put("list", newlist);
            return ret;
        } else {
            ret.put("code", "E");
            ret.put("msg", errors);
            ret.put("list", newlist);
            return ret;
        }

    }

    /**
     * 获取已存在机票信息
     *
     * @return
     */
    private List<String> getFlightorderList() {
        List<String> flightorderlist = new ArrayList<>();
        String sqlqzyj = "select id ,orderSerialNo from uf_cctz_dt4 ";
        //获取正文

        rs.executeQuery(sqlqzyj.toString());
        while (rs.next()) {
            flightorderlist.add(Util.null2String(rs.getString("orderSerialNo")));
            flightordermap.put(Util.null2String(rs.getString("orderSerialNo")), Util.null2String(rs.getString("id")));
        }
        return flightorderlist;
    }

    /**
     * 获取已存在机票改签信息
     *
     * @return
     */
    private List<String> getflightOrderChangeList() {
        List<String> flightorderchangelist = new ArrayList<>();
        String sqlqzyj = "select orderSerialNo ,id from uf_cctz_dt5 ";
        //获取正文

        rs.executeQuery(sqlqzyj.toString());
        while (rs.next()) {
            flightorderchangelist.add(Util.null2String(rs.getString("orderSerialNo")));
            flightorderchangemap.put(Util.null2String(rs.getString("orderSerialNo")), Util.null2String(rs.getString("id")));
        }
        return flightorderchangelist;
    }

    /**
     * 获取已存在网络数据
     *
     * @return
     */
    private List<String> getHotelOrderList() {
        List<String> hotelorderlist = new ArrayList<>();
        String sqlqzyj = "select orderSerialNo ,id from uf_cctz_dt6";
        //获取正文

        rs.executeQuery(sqlqzyj.toString());
        while (rs.next()) {
            hotelorderlist.add(Util.null2String(rs.getString("orderSerialNo")));
            hotelordermap.put(Util.null2String(rs.getString("orderSerialNo")), Util.null2String(rs.getString("id")));
        }
        return hotelorderlist;
    }

    private List<String> getTrainOrderList() {
        List<String> trainorderlist = new ArrayList<>();
        String sqlqzyj = "select orderSerialNo ,id from uf_cctz_dt7";
        //获取正文

        rs.executeQuery(sqlqzyj.toString());
        while (rs.next()) {
            trainorderlist.add(Util.null2String(rs.getString("orderSerialNo")));
            trainordermap.put(Util.null2String(rs.getString("orderSerialNo")), Util.null2String(rs.getString("id")));
        }
        return trainorderlist;
    }

    private List<String> getCarOrderList() {
        List<String> carorderlist = new ArrayList<>();
        String sqlqzyj = "select orderSerialNo ,id from uf_cctz_dt8";
        //获取正文

        rs.executeQuery(sqlqzyj.toString());
        while (rs.next()) {
            carorderlist.add(Util.null2String(rs.getString("orderSerialNo")));
            carordermap.put(Util.null2String(rs.getString("orderSerialNo")), Util.null2String(rs.getString("id")));
        }
        return carorderlist;
    }

    private  String getJourneyNoMainid(String journeyNo){
        String sql="select id from uf_cctz where lcbh=?";
        rs.executeQuery(sql,journeyNo);
        if(rs.next()){
            return rs.getString("id");
        }else{
            return null;
        }
    }


    private String getCurrentDate() {
        SimpleDateFormat var1 = new SimpleDateFormat("yyyy-MM-dd");
        return var1.format(new Date());
    }

    private String getCurrentTime() {
        SimpleDateFormat var1 = new SimpleDateFormat("HH:mm:ss");
        return var1.format(new Date());
    }




}
