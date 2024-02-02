//package weaver.interfaces.schedule;
//
//import java.text.NumberFormat;
//import java.text.SimpleDateFormat;
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
//import weaver.interfaces.schedule.ThreadCust.MyTask;
//
///**
// *
// * @author mac
// */
//
//public class BaoljtGetPayOrderStatusOutMsgSchedule extends BaseCronJob {
//
//	/**
//	 * ��ʱ����
//	 */
//	public void execute() {
//		BaseBean baseBean = new BaseBean();
//		baseBean.writeLog("!~~~~~~~~~~~~~~~~~~~~~~schedule begin~~~~~~~~~~~~~~~~~~~~~~~~");
//		String currentDateTime = TimeUtil.getCurrentTimeString();
////		MyTask task = new MyTask(currentDateTime);
////		ThreadCust.executor.execute(task);
////		ThreadCust.type="4";
//		exedata();
//		baseBean.writeLog("!~~~~~~~~~~~~~~~~~~~~~~schedule end~~~~~~~~~~~~~~~~~~~~~~~~");
//	}
//
//	public void exedata() {
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
//			//��֤��Ϣ
//			//"BATCH_NO": "112020031800001", //���֣�ÿ����������κŶ����ظ�,����ϵͳ�������+������+00001����
//			int rqxh=-1;
//			//��ȡ�������
//			String  log_tablename =Util.null2String(baseBean.getPropValue("bljt20200318", "log_tablename"));
//			String  maintablename =Util.null2String(baseBean.getPropValue("bljt20200318", "tablename"));
//			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
//
//			String sql ="select * from "+maintablename+" where  RET_CODE='1'  and ( PAYSTATE='0' or PAYSTATE='2' or PAYSTATE='3' or PAYSTATE is null )";
//			baseBean.writeLog("sql:"+sql);
//			rs.execute(sql);
//			while(rs.next()) {
//				String log_sql ="select (max(dtqqxh)+1) as rqxh  from "+log_tablename+" where rq='"+sdf1.format(new Date())+"'";
//				rs3.execute(log_sql);
//				if(rs3.next()) {
//					rqxh = Util.getIntValue(rs3.getString("rqxh"),1);
//				}else {
//					rqxh = 1;
//				}
//				String syscode =Util.null2String(baseBean.getPropValue("bljt20200318", "syscode"));
//				NumberFormat formatter = NumberFormat.getNumberInstance();
//				formatter.setMinimumIntegerDigits(5);
//				formatter.setGroupingUsed(false);
//				String rqxhtemp = formatter.format(rqxh);
//				String BATCH_NO = syscode + sdf0.format(new Date()) + rqxhtemp;
//				json0.put("BATCH_NO", BATCH_NO);
//				//"BIZCODE": "PAYINTER", //�����������ֽӿڸ���:PAYINTER,���״̬����:PAYRETURN,;
//				json0.put("BIZCODE", "PAYRESULTRETURN");
//				//"SESSIONID": "OA/��Դ��¼SessionID",--ʱ����� ��ȷ���룬 17 λ�� �����κ��ַ���
//				long currentDateTime = System.currentTimeMillis();
//				json0.put("SESSIONID", currentDateTime);
//				//"REST_USER": "ABC", //�˴�Ϊ�������ʽ�ϵͳ��ע������
//				String REST_USER =Util.null2String(baseBean.getPropValue("bljt20200318", "REST_USER"));
//				json0.put("REST_USER", REST_USER);
//				//"CHANNEL": "G6", //?����Դϵͳ(OA/��Դ��)
//				String CHANNEL =Util.null2String(baseBean.getPropValue("bljt20200318", "CHANNEL"));
//				json0.put("CHANNEL", CHANNEL);
//				//�˴���USER+PWD����MD5�㷨���ܺ�ת��д,OA/��Դ�����ݶ�
//				//"REST_PWD": "0EE1EF4C3AE4F95E301F9D3F7DF95696"
//				String PWD =Util.null2String(baseBean.getPropValue("bljt20200318", "PWD"));
//				String REST_PWD = BaoljtUtil.standardMD5(REST_USER+PWD).toUpperCase();
//				json0.put("REST_PWD", REST_PWD);
//				String currentdate = sdf.format(new Date());
//
//
//				//"DATAS":[{"ORG_ERP_ID":"MY","ORG_ERP_DETAIL_ID":"8a7139f7-4275-ea11-80b8-0a94ef7517dd","PAYID":"PTMS75888","RET_CODE":"1","RET_MSG":""}]
//				json1.put("ORG_ERP_ID", REST_USER);
//				json1.put("ORG_ERP_DETAIL_ID", Util.null2String(rs.getString("ORG_ERP_DETAIL_ID")));
//				json1.put("PAYID", Util.null2String(rs.getString("PAYID")));
//				json1.put("RET_CODE", Util.null2String(rs.getString("RET_CODE")));
//				json1.put("RET_MSG", null);
//				jsonarr0.add(json1);
//				json0.put("DATAS", jsonarr0);
//
//
//				baseBean.writeLog("������"+json0.toString());
//				String rtjson = setData(json0.toString());
//				baseBean.writeLog("���ؽ����"+rtjson);
//				JSONObject rejson = JSONObject.fromObject(rtjson);
//				JSONObject head = rejson.getJSONObject("HEAD");
//				String EsbCode = head.getString("EsbCode");
//				String EsbDesc = head.getString("EsbDesc");
//
//				if("0000000".equals(EsbCode)) {
//					//
//					JSONObject body = rejson.getJSONObject("BODY");
//					JSONArray datasarr = body.getJSONArray("DATAS");
//					for(int i=0 ;i<datasarr.size();i++) {
//						JSONObject tempjson = (JSONObject)datasarr.get(i);
//						String ORG_ERP_DETAIL_ID = tempjson.getString("ORG_ERP_DETAIL_ID");
//						//��������
//						//						String sqlupdate = "update "+maintablename+" set RET_MSG=?=?,ORG_ERP_ID=? where ORG_ERP_DETAIL_ID='"+ORG_ERP_DETAIL_ID+"'" ;
//						//						String [] params = new String[3];
//						//						params[0]= tempjson.getString("RET_MSG");
//						//						params[1]= tempjson.getString("RET_CODE");
//						//						params[2]= tempjson.getString("ORG_ERP_ID");
//						//						rs2.executeUpdate(sqlupdate, params);
//					}
//
//				}else {
//					//��������
//
//				}
//			}
//
//
//		} catch (Exception e) {
//			baseBean.writeLog(e);
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * ����ӿ�
//	 */
//	public String setData(String temp) {
//		String msg ="";
//		//���ýӿڿ�ʼ
//		BaseBean baseBean = new BaseBean();
//		String  address =Util.null2String(baseBean.getPropValue("bljt20200318", "address_test"));
//		TxServiceGatewayProxy txServiceGatewayProxy = new TxServiceGatewayProxy();
//		txServiceGatewayProxy.setEndpoint(address);
//		try {
//			msg = txServiceGatewayProxy.send(temp);
//			System.out.println(msg);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			msg="{\"HEAD\":{\"EsbCode\":\"-1\",\"EsbDesc\":\"���ýӿڱ���"+e.getMessage()+"\"},\"BODY\":{}";
//		}
//		//���ýӿڽ���
//		return msg;
//
//	}
//}
