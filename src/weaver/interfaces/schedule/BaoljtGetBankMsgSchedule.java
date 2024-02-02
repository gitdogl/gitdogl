//package weaver.interfaces.schedule;
//
//import java.text.NumberFormat;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import com.nstc.fdlk.webservice.TxServiceGatewayProxy;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import weaver.conn.RecordSet;
//import weaver.formmode.setup.ModeRightInfo;
//import weaver.general.BaseBean;
//import weaver.general.TimeUtil;
//import weaver.general.Util;
//
///**
// *
// * @author mac
// */
//
//public class BaoljtGetBankMsgSchedule extends BaseCronJob {
//
//    public String begindate;
//    public String enddate;
//    /**
//     * ��ʱ����
//     */
//    public void execute() {
//        BaseBean baseBean = new BaseBean();
//        baseBean.writeLog("!~~~~~~~~~~~~~~~~~~~~~~schedule begin~~~~~~~~~~~~~~~~~~~~~~~~");
//        //String currentDateTime = TimeUtil.getCurrentTimeString();
//        //MyTask task = new MyTask(currentDateTime);
//        //ThreadCust.executor.execute(task);
//        //ThreadCust.type="2";
//        exedata(begindate,enddate);
//        baseBean.writeLog("!~~~~~~~~~~~~~~~~~~~~~~schedule end~~~~~~~~~~~~~~~~~~~~~~~~");
//    }
//
//    public boolean exedata(String fromdate,String todate) {
//        BaseBean baseBean = new BaseBean();
//        boolean flag =true;
//        String cltname ="";
//        RecordSet rs1 = new RecordSet();
//        String sql ="select cltname from uf_frzt where status='1' ";
//        rs1.executeQuery(sql,new Object[0]);
//        while (rs1.next()) {
//            cltname =rs1.getString("cltname");
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                SimpleDateFormat sdf0 = new SimpleDateFormat("yyyyMMdd");
//                RecordSet rs = new RecordSet();
//                RecordSet rs2 = new RecordSet();
//                RecordSet rs3 = new RecordSet();
//                JSONObject json0 = new JSONObject();
//                JSONObject json1 = new JSONObject();
//                JSONArray jsonarr0 = new JSONArray();
//                //��֤��Ϣ
//                //"BATCH_NO": "112020031800001", //���֣�ÿ����������κŶ����ظ�,����ϵͳ�������+������+00001����
//                int rqxh = -1;
//                //��ȡ�������
//                String log_tablename = Util.null2String(baseBean.getPropValue("bljt20200318", "log_tablename"));
//
//                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
//                String log_sql = "select (max(dtqqxh)+1) as rqxh  from " + log_tablename + " where rq='" + sdf1.format(new Date()) + "'";
//                rs3.execute(log_sql);
//                if (rs3.next()) {
//                    rqxh = Util.getIntValue(rs3.getString("rqxh"), 1);
//                } else {
//                    rqxh = 1;
//                }
//                String syscode = Util.null2String(baseBean.getPropValue("bljt20200318", "syscode"));
//                NumberFormat formatter = NumberFormat.getNumberInstance();
//                formatter.setMinimumIntegerDigits(5);
//                formatter.setGroupingUsed(false);
//                String rqxhtemp = formatter.format(rqxh);
//                String BATCH_NO = syscode + sdf0.format(new Date()) + rqxhtemp;
//                json0.put("BATCH_NO", BATCH_NO);
//                //"BIZCODE": "PAYINTER", //�����������ֽӿڸ���:PAYINTER,���״̬����:PAYRETURN,;
//                json0.put("BIZCODE", "ACCOUNTETURN");
//                //"SESSIONID": "OA/��Դ��¼SessionID",--ʱ����� ��ȷ���룬 17 λ�� �����κ��ַ���
//                long currentDatetime = System.currentTimeMillis();
//                json0.put("SESSIONID", currentDatetime);
//                //"REST_USER": "ABC", //�˴�Ϊ�������ʽ�ϵͳ��ע������
//                String REST_USER = Util.null2String(baseBean.getPropValue("bljt20200318", "REST_USER"));
//                json0.put("REST_USER", REST_USER);
//                //"CHANNEL": "G6", //?����Դϵͳ(OA/��Դ��)
//                String CHANNEL = Util.null2String(baseBean.getPropValue("bljt20200318", "CHANNEL"));
//                json0.put("CHANNEL", CHANNEL);
//                //�˴���USER+PWD����MD5�㷨���ܺ�ת��д,OA/��Դ�����ݶ�
//                //"REST_PWD": "0EE1EF4C3AE4F95E301F9D3F7DF95696"
//                String PWD = Util.null2String(baseBean.getPropValue("bljt20200318", "PWD"));
//                String REST_PWD = BaoljtUtil.standardMD5(REST_USER + PWD).toUpperCase();
//                json0.put("REST_PWD", REST_PWD);
//
//                String currdate = sdf.format(new Date());
//                Calendar ca = Calendar.getInstance();
//                ca.setTime(new Date());
//                ca.add(Calendar.DATE, -1);
//                String before1date = sdf.format(ca.getTime());
//
//                if (!"".equals(Util.null2String(fromdate))) {
//                    before1date = fromdate;
//                }
//
//                if (!"".equals(Util.null2String(todate))) {
//                    currdate = todate;
//                }
//
//                json1.put("CLTNAME", cltname);
//                json1.put("U_BEGDA", before1date);
//                json1.put("U_ENDDA", currdate);
//                jsonarr0.add(json1);
//                json0.put("DATAS", jsonarr0);
//
//                baseBean.writeLog("������" + json0.toString());
//                String rtjson = setData(json0.toString());
//                baseBean.writeLog("���ؽ����" + rtjson);
//                JSONObject rejson = JSONObject.fromObject(rtjson);
//                JSONObject head = rejson.getJSONObject("HEAD");
//                String EsbCode = head.getString("EsbCode");
//                String EsbDesc = head.getString("EsbDesc");
//                String maintablename = Util.null2String(baseBean.getPropValue("bljt20200318", "tablename"));
//                if ("0000000".equals(EsbCode)) {
//                    //
//                    JSONObject body = rejson.getJSONObject("BODY");
//                    JSONArray datasarr = body.getJSONArray("DATAS");
//                    for (int i = 0; i < datasarr.size(); i++) {
//                        JSONObject tempjson = (JSONObject) datasarr.get(i);
//
//                        String bank_tablename = Util.null2String(baseBean.getPropValue("bljt20200318", "bank_tablename"));
//                        int bank_modeid = Util.getIntValue(baseBean.getPropValue("bljt20200318", "bank_modeid"));
//                        int bank_formid = Util.getIntValue(baseBean.getPropValue("bljt20200318", "bank_formid"));
//                        FxExcelModeDataIdUpdate modeDataIdUpdate = new FxExcelModeDataIdUpdate();
//                        String currentDateTime = TimeUtil.getCurrentTimeString();
//                        String currentdate = currentDateTime.substring(0, 10);
//                        String currenttime = currentDateTime.substring(11, 16);
//
//                        int id_temp = -1;
//
//                        String sqltemp = "select id from " + bank_tablename + " where ACCOUNTNO='" + tempjson.getString("ACCOUNTNO") + "' ";
//                        rs.execute(sqltemp);
//                        if (rs.next()) {
//                            id_temp = Util.getIntValue(rs.getString("id"));
//                        } else {
//                            id_temp = modeDataIdUpdate.getModeDataNewId(bank_tablename, bank_modeid, 1, 0, currentdate, currenttime);
//                        }
//                        if (id_temp > 0) {
//                            //��������
//                            String sqlupdate = "update " + bank_tablename + " set ACCOUNTNO=?,ACCOUNTNAME=?,CLTNAME=?,CLTNAME_UP=?,OPBANKNAME=?,CNAPS=?,CURRENCYNO=?,ACCOUNT_STATE=?,IS_PAY_ACCOUNT=?,ACCOUNT_DATE=? where id=" + id_temp;
//                            String[] params = new String[10];
//                            params[0] = tempjson.getString("ACCOUNTNO");
//                            params[1] = tempjson.getString("ACCOUNTNAME");
//                            params[2] = tempjson.getString("CLTNAME");
//                            params[3] = tempjson.getString("CLTNAME_UP");
//                            params[4] = tempjson.getString("OPBANKNAME");
//                            params[5] = tempjson.getString("CNAPS");
//                            params[6] = tempjson.getString("CURRENCYNO");
//                            params[7] = tempjson.getString("ACCOUNT_STATE");
//                            params[8] = tempjson.getString("IS_PAY_ACCOUNT");
//                            params[9] = tempjson.getString("ACCOUNT_DATE");
//                            rs2.executeUpdate(sqlupdate, params);
//                            // ������������ݵ�Ĭ��Ȩ
//                            ModeRightInfo ModeRightInfo = new ModeRightInfo();
//                            ModeRightInfo.setNewRight(true);
//                            ModeRightInfo.editModeDataShare(1, bank_modeid, id_temp);
//                        }
//
//                    }
//
//                } else {
//                    //��������
//                }
//                Thread.sleep(3500);
//
//            } catch (Exception e) {
//                flag = false;
//                baseBean.writeLog(e);
//                e.printStackTrace();
//            }
//        }
//        return flag;
//    }
//
//    /**
//     * ����ӿ�
//     */
//    public String setData(String temp) {
//        String msg ="";
//        //���ýӿڿ�ʼ
//        BaseBean baseBean = new BaseBean();
//        String  address =Util.null2String(baseBean.getPropValue("bljt20200318", "address_test"));
//        TxServiceGatewayProxy txServiceGatewayProxy = new TxServiceGatewayProxy();
//        txServiceGatewayProxy.setEndpoint(address);
//        try {
//            msg = txServiceGatewayProxy.send(temp);
//            System.out.println(msg);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            msg="{\"HEAD\":{\"EsbCode\":\"-1\",\"EsbDesc\":\"���ýӿڱ���"+e.getMessage()+"\"},\"BODY\":{}";
//        }
//        //���ýӿڽ���
//        return msg;
//
//    }
//
//    public String getBegindate() {
//        return begindate;
//    }
//
//    public void setBegindate(String begindate) {
//        this.begindate = begindate;
//    }
//
//    public String getEnddate() {
//        return enddate;
//    }
//
//    public void setEnddate(String enddate) {
//        this.enddate = enddate;
//    }
//}
