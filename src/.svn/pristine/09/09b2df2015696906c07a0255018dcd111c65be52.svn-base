//package weaver.interfaces.schedule;
//
//import java.math.BigDecimal;
//import java.text.NumberFormat;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//import com.nstc.fdlk.webservice.TxServiceGatewayProxy;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import weaver.conn.RecordSet;
//import weaver.general.BaseBean;
//import weaver.general.TimeUtil;
//import weaver.general.Util;
//
///**
// *
// * @author mac
// */
//
//public class BaoljtGetPayOrderStatusSchedule extends BaseCronJob {
//
//	public String fromdate ;
//
//	/**
//	 * 定时任务
//	 */
//	public void execute() {
//		BaseBean baseBean = new BaseBean();
//		baseBean.writeLog("!~~~~~~~~~~~~~~~~~~~~~~schedule begin~~~~~~~~~~~~~~~~~~~~~~~~");
//		String currentDateTime = TimeUtil.getCurrentTimeString();
////		MyTask task = new MyTask(currentDateTime);
////		ThreadCust.executor.execute(task);
////		ThreadCust.type="1";
//		RecordSet rs =new RecordSet();
//		String  maintablename =Util.null2String(baseBean.getPropValue("bljt20200318", "tablename"));
//		String currentdate =TimeUtil.getCurrentDateString();
//		String begindate =TimeUtil.dateAdd(currentdate,-7);
//		if(!"".equals(Util.null2String(fromdate))){
//			begindate =fromdate;
//		}
//		rs.executeQuery("select ORG_ERP_DETAIL_ID from "+maintablename +" where modedatacreatedate between ? and ? and CONVERT(varchar,RET_MSG) not in ('已回调资金接口') ",begindate,currentdate);
//		baseBean.writeLog("!~~~~~~~~~~~~~~~~~~~~~~BaoljtGetPayOrderStatusSchedule ,begindate="+begindate+" and enddate ="+currentdate+" ~~~~~~~~~~~~~~~~~~~~~~~~");
//		while(rs.next()){
//			String orderid =rs.getString("ORG_ERP_DETAIL_ID");
//			baseBean.writeLog("!~~~~~~~~~~~~~~~~~~~~~~BaoljtGetPayOrderStatusSchedule ,orderid="+orderid+"~~~~~~~~~~~~~~~~~~~~~~~~");
//			exedata(orderid);
//			try {
//				Thread.sleep(3500);
//			} catch (InterruptedException e) {
//				new BaseBean().writeLog(e);
//			}
//		}
//
//		baseBean.writeLog("!~~~~~~~~~~~~~~~~~~~~~~schedule end~~~~~~~~~~~~~~~~~~~~~~~~");
//	}
//
//	public void exedata( String orderid) {
//		BaseBean baseBean = new BaseBean();
//		try {
//
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyyMMdd");
//			RecordSet rs = new RecordSet();
//			RecordSet rs1 = new RecordSet();
//			RecordSet rs2 = new RecordSet();
//			RecordSet rs3 = new RecordSet();
//			JSONObject json0 =  new JSONObject();
//			JSONObject json1 =  new JSONObject();
//			JSONArray jsonarr0 = new JSONArray();
//			//验证信息
//			//"BATCH_NO": "112020031800001", //数字，每次请求的批次号都不重复,根据系统分配编码+年月日+00001排序
//			int rqxh=-1;
//			//获取请求序号
//			String  log_tablename =Util.null2String(baseBean.getPropValue("bljt20200318", "log_tablename"));
//			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
//			String log_sql ="select (max(dtqqxh)+1) as rqxh  from "+log_tablename+" where rq='"+sdf1.format(new Date())+"'";
//			rs3.execute(log_sql);
//			if(rs3.next()) {
//				rqxh = Util.getIntValue(rs3.getString("rqxh"),1);
//			}else {
//				rqxh = 1;
//			}
//			String syscode =Util.null2String(baseBean.getPropValue("bljt20200318", "syscode"));
//			NumberFormat formatter = NumberFormat.getNumberInstance();
//			formatter.setMinimumIntegerDigits(5);
//			formatter.setGroupingUsed(false);
//			String rqxhtemp = formatter.format(rqxh);
//			String BATCH_NO = syscode + sdf0.format(new Date()) + rqxhtemp;
//			json0.put("BATCH_NO", BATCH_NO);
//			//"BIZCODE": "PAYINTER", //代表下面哪种接口付款:PAYINTER,付款单状态反馈:PAYRETURN,;
//			json0.put("BIZCODE", "PAYRETURN");
//			//"SESSIONID": "OA/明源登录SessionID",--时间戳， 精确到秒， 17 位， 不带任何字符；
//			long currentDateTime = System.currentTimeMillis();
//			json0.put("SESSIONID", currentDateTime);
//			//"REST_USER": "ABC", //此处为请求方在资金系统中注册的身份
//			String REST_USER =Util.null2String(baseBean.getPropValue("bljt20200318", "REST_USER"));
//			json0.put("REST_USER", REST_USER);
//			//"CHANNEL": "G6", //?数据源系统(OA/明源等)
//			String CHANNEL =Util.null2String(baseBean.getPropValue("bljt20200318", "CHANNEL"));
//			json0.put("CHANNEL", CHANNEL);
//			//此处用USER+PWD进行MD5算法加密后转大写,OA/明源密码暂定
//			//"REST_PWD": "0EE1EF4C3AE4F95E301F9D3F7DF95696"
//			String PWD =Util.null2String(baseBean.getPropValue("bljt20200318", "PWD"));
//			String REST_PWD = BaoljtUtil.standardMD5(REST_USER+PWD).toUpperCase();
//			json0.put("REST_PWD", REST_PWD);
//
//			String currentdate = sdf.format(new Date());
//			Calendar ca = Calendar.getInstance();
//			ca.setTime(new Date());
//			ca.add(Calendar.DATE, -7);
//			//String before1date = fromdate.equals("")?sdf.format(ca.getTime()):fromdate;
//			String before1date =sdf.format(ca.getTime());
//			if(!"".equals(Util.null2String(fromdate))){
//				before1date =fromdate;
//			}
//
//			json1.put("I_BEGDA", before1date);
//			json1.put("I_ENDDA", currentdate);
//			json1.put("ORG_ERP_DETAIL_ID", orderid);
//			jsonarr0.add(json1);
//			json0.put("DATAS", jsonarr0);
//
//
//			baseBean.writeLog("参数："+json0.toString());
//			String rtjson = setData(json0.toString());
//			baseBean.writeLog("返回结果："+rtjson);
//			JSONObject rejson = JSONObject.fromObject(rtjson);
//			JSONObject head = rejson.getJSONObject("HEAD");
//			String EsbCode = head.getString("EsbCode");
//			String EsbDesc = head.getString("EsbDesc");
//			String  maintablename =Util.null2String(baseBean.getPropValue("bljt20200318", "tablename"));
//			if("0000000".equals(EsbCode)) {
//				//
//				JSONObject body = rejson.getJSONObject("BODY");
//				JSONArray datasarr = body.getJSONArray("DATAS");
//				for(int i=0 ;i<datasarr.size();i++) {
//					JSONObject tempjson = (JSONObject)datasarr.get(i);
//					String ORG_ERP_DETAIL_ID = tempjson.getString("ORG_ERP_DETAIL_ID");
//					String PAYID =Util.null2String(tempjson.getString("PAYID"));
//					//更新数据
//					String sqlupdate = "update "+maintablename+" set ORG_ERP_ID=?,PAYID=?,OR_PAY_SUCCESSFUL_ID=?,PAYACCOUNT=?,PAYAMOUNT=?," +
//							"PAYSTATE=?,PAYDATE=?,REASON=?,TEXTVALUE1=?,TEXTVALUE2=?,RET_CODE=? where ORG_ERP_DETAIL_ID='"+ORG_ERP_DETAIL_ID+"'" ;
//					String [] params = new String[11];
//					params[0]= tempjson.getString("ORG_ERP_ID");
//					params[1]= tempjson.getString("PAYID");
//					String SUCCESSFUL_ID=tempjson.getString("OR_PAY_SUCCESSFUL_ID");
//					params[2]= SUCCESSFUL_ID;
//					params[3]= tempjson.getString("PAYACCOUNT");
//					params[4]= Util.null2String(new BigDecimal(tempjson.getString("PAYAMOUNT")));
//					params[5]= tempjson.getString("PAYSTATE");
//					params[6]= tempjson.getString("PAYDATE");
//					params[7]= tempjson.getString("REASON");
//					params[8]= tempjson.getString("TEXTVALUE1");
//					params[9]= tempjson.getString("TEXTVALUE2");
//					params[10]= "1";
//
//					boolean flag =rs2.executeUpdate(sqlupdate, params);
//					if(flag){ //回调同步结果
//						syncData(PAYID,ORG_ERP_DETAIL_ID,"1");
//					}else{
//						syncData(PAYID,ORG_ERP_DETAIL_ID,"0");
//					}
//				}
//
//			}else {
//				//更新数据
//
//			}
//
//		} catch (Exception e) {
//			baseBean.writeLog(e);
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * NC接口调用
//	 */
//	public String setData(String temp) {
//		String msg ="";
//		//调用接口开始
//		BaseBean baseBean = new BaseBean();
//		String  address =Util.null2String(baseBean.getPropValue("bljt20200318", "address_test"));
//		TxServiceGatewayProxy txServiceGatewayProxy = new TxServiceGatewayProxy();
//		txServiceGatewayProxy.setEndpoint(address);
//		try {
//			msg = txServiceGatewayProxy.send(temp);
//			//System.out.println(msg);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			msg="{\"HEAD\":{\"EsbCode\":\"-1\",\"EsbDesc\":\"调用接口报错："+e.getMessage()+"\"},\"BODY\":{}";
//		}
//		//调用接口结束
//		return msg;
//
//	}
//
//	public String syncData(String payid,String detailid,String retcode){
//
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		SimpleDateFormat sdf0 = new SimpleDateFormat("yyyyMMdd");
//		RecordSet rs = new RecordSet();
//		RecordSet rs1 = new RecordSet();
//		RecordSet rs2 = new RecordSet();
//		RecordSet rs3 = new RecordSet();
//		JSONObject json0 =  new JSONObject();
//		JSONObject json1 =  new JSONObject();
//		JSONArray jsonarr0 = new JSONArray();
//		//验证信息
//		//"BATCH_NO": "112020031800001", //数字，每次请求的批次号都不重复,根据系统分配编码+年月日+00001排序
//		int rqxh=-1;
//		//获取请求序号
//		String  log_tablename =Util.null2String(new BaseBean().getPropValue("bljt20200318", "log_tablename"));
//		String  maintablename =Util.null2String(new BaseBean().getPropValue("bljt20200318", "tablename"));
//		String syscode =Util.null2String(new BaseBean().getPropValue("bljt20200318", "syscode"));
//		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
//
//		NumberFormat formatter = NumberFormat.getNumberInstance();
//		formatter.setMinimumIntegerDigits(5);
//		formatter.setGroupingUsed(false);
//		String rqxhtemp = formatter.format(rqxh);
//		String BATCH_NO = syscode + sdf0.format(new Date()) + rqxhtemp;
//		json0.put("BATCH_NO", BATCH_NO);
//		//"BIZCODE": "PAYINTER", //代表下面哪种接口付款:PAYINTER,付款单状态反馈:PAYRETURN,;
//		json0.put("BIZCODE", "PAYRESULTRETURN");
//		//"SESSIONID": "OA/明源登录SessionID",--时间戳， 精确到秒， 17 位， 不带任何字符；
//		long currentDateTime = System.currentTimeMillis();
//		json0.put("SESSIONID", currentDateTime);
//		//"REST_USER": "ABC", //此处为请求方在资金系统中注册的身份
//		String REST_USER =Util.null2String(new BaseBean().getPropValue("bljt20200318", "REST_USER"));
//		json0.put("REST_USER", REST_USER);
//		//"CHANNEL": "G6", //?数据源系统(OA/明源等)
//		String CHANNEL =Util.null2String(new BaseBean().getPropValue("bljt20200318", "CHANNEL"));
//		json0.put("CHANNEL", CHANNEL);
//		//此处用USER+PWD进行MD5算法加密后转大写,OA/明源密码暂定
//		//"REST_PWD": "0EE1EF4C3AE4F95E301F9D3F7DF95696"
//		String PWD =Util.null2String(new BaseBean().getPropValue("bljt20200318", "PWD"));
//		String REST_PWD = BaoljtUtil.standardMD5(REST_USER+PWD).toUpperCase();
//		json0.put("REST_PWD", REST_PWD);
//
//		json1.put("ORG_ERP_ID", REST_USER);
//		json1.put("ORG_ERP_DETAIL_ID", detailid);
//		json1.put("PAYID", payid);
//		json1.put("RET_CODE", retcode);
//		json1.put("RET_MSG", REST_USER+"_"+TimeUtil.getCurrentDateString()+"_"+TimeUtil.getCurrentTimeString()+"_回调资金接口");
//		jsonarr0.add(json1);
//		json0.put("DATAS", jsonarr0);
//		new BaseBean().writeLog("参数："+json0.toString());
//		String rtjson = setData(json0.toString());
//		new BaseBean().writeLog("返回结果："+rtjson);
//		JSONObject rejson = JSONObject.fromObject(rtjson);
//		JSONObject head = rejson.getJSONObject("HEAD");
//		String EsbCode = head.getString("EsbCode");
//		String EsbDesc = head.getString("EsbDesc");
//
//		if("0000000".equals(EsbCode)) {
//			//
//			JSONObject body = rejson.getJSONObject("BODY");
//			JSONArray datasarr = body.getJSONArray("DATAS");
//			for(int i=0 ;i<datasarr.size();i++) {
//				JSONObject tempjson = (JSONObject)datasarr.get(i);
//				String ORG_ERP_DETAIL_ID = tempjson.getString("ORG_ERP_DETAIL_ID");
//				//TODO 记录回调状态
//				String sqlupdate = "update "+maintablename+" set RET_CODE='1',RET_MSG ='已回调资金接口' where ORG_ERP_DETAIL_ID='"+ORG_ERP_DETAIL_ID+"'" ;
//				boolean flag =rs2.execute(sqlupdate);
//
//			}
//
//		}
//
//		return "";
//	}
//
//	public String getFromdate() {
//		return fromdate;
//	}
//
//	public void setFromdate(String fromdate) {
//		this.fromdate = fromdate;
//	}
//}
