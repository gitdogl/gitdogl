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
//import weaver.formmode.setup.ModeRightInfo;
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
//public class BaoljtGetStaffOfbankNumSchedule extends BaseCronJob {
//
//	/**
//	 * ��ʱ����
//	 */
//	public void execute() {
//		BaseBean baseBean = new BaseBean();
//		baseBean.writeLog("!~~~~~~~~~~~~~~~~~~~~~~schedule begin~~~~~~~~~~~~~~~~~~~~~~~~");
//
//		String currentDateTime = TimeUtil.getCurrentTimeString();
////		MyTask task = new MyTask(currentDateTime);
////		ThreadCust.executor.execute(task);
////		ThreadCust.type="3";
//		exedata();
//		baseBean.writeLog("!~~~~~~~~~~~~~~~~~~~~~~schedule end~~~~~~~~~~~~~~~~~~~~~~~~");
//	}
//
//	public void exedata() {
//		BaseBean baseBean = new BaseBean();
//		try {
//			int totalpage =Util.getIntValue(baseBean.getPropValue("bljt20200318", "totalpage"));
//			for(int t=1;t<=totalpage;t++) {
//
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				SimpleDateFormat sdf0 = new SimpleDateFormat("yyyyMMdd");
//				RecordSet rs = new RecordSet();
//				RecordSet rs1 = new RecordSet();
//				RecordSet rs2 = new RecordSet();
//				RecordSet rs3 = new RecordSet();
//				JSONObject json0 =  new JSONObject();
//				JSONObject json1 =  new JSONObject();
//				JSONArray jsonarr0 = new JSONArray();
//				//��֤��Ϣ
//				//"BATCH_NO": "112020031800001", //���֣�ÿ����������κŶ����ظ�,����ϵͳ�������+������+00001����
//				int rqxh=-1;
//				//��ȡ�������
//				String  log_tablename =Util.null2String(baseBean.getPropValue("bljt20200318", "log_tablename"));
//				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
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
//				json0.put("BIZCODE", "CNAPS");
//				//"SESSIONID": "OA/��Դ��¼SessionID",--ʱ����� ��ȷ���룬 17 λ�� �����κ��ַ���
//				long currentDatetime = System.currentTimeMillis();
//				json0.put("SESSIONID", currentDatetime);
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
//
//				String currdate = sdf.format(new Date());
//
//				json1.put("PAGING", t);
//				jsonarr0.add(json1);
//				json0.put("DATAS", jsonarr0);
//
//				baseBean.writeLog("������"+json0.toString());
//				String rtjson = setData(json0.toString());
//				baseBean.writeLog("���ؽ����"+rtjson);
//				JSONObject rejson = JSONObject.fromObject(rtjson);
//				JSONObject head = rejson.getJSONObject("HEAD");
//				String EsbCode = head.getString("EsbCode");
//				String EsbDesc = head.getString("EsbDesc");
//				String  maintablename =Util.null2String(baseBean.getPropValue("bljt20200318", "tablename"));
//				if("0000000".equals(EsbCode)) {
//					//
//					JSONObject body = rejson.getJSONObject("BODY");
//					JSONArray datasarr = body.getJSONArray("DATAS");
//					for(int i=0 ;i<datasarr.size();i++) {
//						JSONObject tempjson = (JSONObject)datasarr.get(i);
//						String  staff_tablename =Util.null2String(baseBean.getPropValue("bljt20200318", "staff_tablename"));
//						int  staff_modeid =Util.getIntValue(baseBean.getPropValue("bljt20200318", "staff_modeid"));
//						int  staff_formid =Util.getIntValue(baseBean.getPropValue("bljt20200318", "staff_formid"));
//						FxExcelModeDataIdUpdate modeDataIdUpdate = new FxExcelModeDataIdUpdate();
//						String currentDateTime = TimeUtil.getCurrentTimeString();
//						String currentdate = currentDateTime.substring(0, 10);
//						String currenttime = currentDateTime.substring(11,16);
//
//						int id_temp=-1;
//						String sqltemp = "select id from "+staff_tablename+" where CNAPS='"+tempjson.getString("CNAPS")+"' and OPBANKNAME='"+tempjson.getString("OPBANKNAME")+"' ";
//						rs.execute(sqltemp);
//						if(rs.next()) {
//							id_temp = Util.getIntValue(rs.getString("id"));
//						}else {
//							id_temp = modeDataIdUpdate.getModeDataNewId(staff_tablename,staff_modeid, 1, 0, currentdate, currenttime);
//							if(id_temp>0) {
//								//��������
//								String sqlupdate = "update "+staff_tablename+" set OPBANKNAME=?,CNAPS=? where id="+id_temp ;
//								String [] params = new String[2];
//								params[0]= tempjson.getString("OPBANKNAME");
//								params[1]= tempjson.getString("CNAPS");
//								rs2.executeUpdate(sqlupdate, params);
//								// ������������ݵ�Ĭ��Ȩ
//								ModeRightInfo ModeRightInfo = new ModeRightInfo();
//								ModeRightInfo.setNewRight(true);
//								ModeRightInfo.editModeDataShare(1, staff_modeid, id_temp);
//							}
//						}
//					}
//
//				}else {
//					//��������
//
//				}
//			}
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
