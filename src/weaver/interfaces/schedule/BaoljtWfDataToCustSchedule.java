//package weaver.interfaces.schedule;
//
//import java.text.DecimalFormat;
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
//public class BaoljtWfDataToCustSchedule extends BaseCronJob {
//
//	/**
//	 * ��ʱ����
//	 */
//	public void execute() {
//		BaseBean baseBean = new BaseBean();
//		baseBean.writeLog("!~~~~~~~~~~~~~~~~~~~~~~schedule begin~~~~~~~~~~~~~~~~~~~~~~~~");
//		String currentDateTime = TimeUtil.getCurrentTimeString();
//		MyTask task = new MyTask(currentDateTime);
//		ThreadCust.executor.execute(task);
//		ThreadCust.type="0";
//		exeData();
//		baseBean.writeLog("!~~~~~~~~~~~~~~~~~~~~~~schedule end~~~~~~~~~~~~~~~~~~~~~~~~");
//	}
//
//	/**
//	 *
//	 * @return
//	 */
//	public boolean exeData() {
//		boolean flag =true;
//		BaseBean baseBean = new BaseBean();
//		try {
//
//			RecordSet rs = new RecordSet();
//			RecordSet rs1 = new RecordSet();
//			RecordSet rs2 = new RecordSet();
//			RecordSet rs3 = new RecordSet();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
//			String  log_tablename =Util.null2String(baseBean.getPropValue("bljt20200318", "log_tablename"));
//			String  maintablename = Util.null2String(baseBean.getPropValue("bljt20200318", "tablename"));
//			//select * from uf_datapushinfo where RET_CODE !='1' or RET_CODE is null or ( RET_CODE='1' and (PAYSTATE='0' or PAYSTATE='2' or PAYSTATE='3' or PAYSTATE is null ))
//			//String sql ="select * from "+maintablename+" where RET_CODE !='1' or RET_CODE is null or (RET_CODE='1'  and ( PAYSTATE='0' or PAYSTATE='2' or PAYSTATE='3' )  )";
//			//String sql ="select * from "+maintablename+" where RET_CODE !='1'  or RET_CODE is null or RET_CODE ='' ";
//			String sql ="select * from "+maintablename+" where RET_CODE not in ('1','2') or RET_CODE is null or RET_CODE ='' ";
//			baseBean.writeLog("sql:"+sql);
//			rs.execute(sql);
//			JSONObject json0=null;
//			JSONObject json1=null;
//			JSONArray jsonarr0 =null;
//			String msg="";
//			while(rs.next()) {
//				json0 =  new JSONObject();
//				json1 =  new JSONObject();
//				jsonarr0= new JSONArray();
//				msg="";
//				int rqxh=-1;
//				//��ȡ�������
//				String log_sql ="select (max(dtqqxh)+1) as rqxh  from "+log_tablename+" where rq='"+sdf1.format(new Date())+"'";
//				rs3.execute(log_sql);
//				if(rs3.next()) {
//					rqxh = Util.getIntValue(rs3.getString("rqxh"),1);
//				}else {
//					rqxh = 1;
//				}
//
//				//��֤��Ϣ
//				//"BATCH_NO": "112020031800001", //���֣�ÿ����������κŶ����ظ�,����ϵͳ�������+������+00001����
//				String ORG_ERP_ID =Util.null2String(baseBean.getPropValue("bljt20200318", "ORG_ERP_ID"));
//				NumberFormat formatter = NumberFormat.getNumberInstance();
//				formatter.setMinimumIntegerDigits(5);
//				formatter.setGroupingUsed(false);
//				String rqxhtemp = formatter.format(rqxh);
//				String BATCH_NO = ORG_ERP_ID + sdf.format(new Date()) + rqxhtemp;
//				json0.put("BATCH_NO", BATCH_NO);
//				//"BIZCODE": "PAYINTER", //�����������ֽӿڸ���:PAYINTER,���״̬����:PAYRETURN,;
//				json0.put("BIZCODE", "PAYINTER");
//				//"SESSIONID": "OA/��Դ��¼SessionID",--ʱ����� ��ȷ���룬 17 �� �����κ��ַ���
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
//				//��������
//				//String WF_NUMBER = Util.null2String(rs.getString("WF_NUMBER"));
//				//if("".equals(WF_NUMBER)) {
//				//	msg +="�����̱��WF_NUMBER���ֶ�ֵΪ��;";
//				//}
//				//json0.put("BATCH_NO",WF_NUMBER );
//
//				//"PAY_NAME": "�����˾",
//				String PAY_NAME = Util.null2String(rs.getString("PAY_NAME"));
//				if("".equals(PAY_NAME)) {
//					msg +="�������˾PAY_NAME���ֶ�ֵΪ��;";
//				}
//				json1.put("PAY_NAME",PAY_NAME);
//
//				//"PAY_BANK": "���������",
//				String PAY_BANK = Util.null2String(rs.getString("PAY_BANK"));
//				if("".equals(PAY_BANK)) {
//					msg +="�����������PAY_BANK���ֶ�ֵΪ��;";
//				}
//				json1.put("PAY_BANK",PAY_BANK);
//
//				//"PAY_ACNT_NO":"����˺�",
//				String PAY_ACNT_NO = Util.null2String(rs.getString("PAY_ACNT_NO"));
//				if("".equals(PAY_ACNT_NO)) {
//					msg +="������˺�PAY_ACNT_NO���ֶ�ֵΪ��;";
//				}
//				json1.put("PAY_ACNT_NO",PAY_ACNT_NO);
//
//				//"RECE_ACC_NAME":"��Ӧ������",
//				String RECE_ACC_NAME = Util.null2String(rs.getString("RECE_ACC_NAME"));
//				if("".equals(RECE_ACC_NAME)) {
//					msg +="����Ӧ������RECE_ACC_NAME���ֶ�ֵΪ��;";
//				}
//				json1.put("RECE_ACC_NAME",RECE_ACC_NAME);
//
//				//"RECE_OPBANK_NAME":"�տ������",
//				String RECE_OPBANK_NAME = Util.null2String(rs.getString("RECE_OPBANK_NAME"));
//				if("".equals(RECE_OPBANK_NAME)) {
//					msg +="���տ������RECE_OPBANK_NAME���ֶ�ֵΪ��;";
//				}
//				json1.put("RECE_OPBANK_NAME",RECE_OPBANK_NAME);
//
//				//"RECE_ACC_NO":"�տ�˺�",
//				String RECE_ACC_NO = Util.null2String(rs.getString("RECE_ACC_NO"));
//				if("".equals(RECE_ACC_NO)) {
//					msg +="���տ�˺�RECE_ACC_NO���ֶ�ֵΪ��;";
//				}
//				json1.put("RECE_ACC_NO",RECE_ACC_NO);
//
//				//"CURRENCY_NO":"����",
//				String CURRENCY_NO = Util.null2String(rs.getString("CURRENCY_NO"));
//				if("".equals(CURRENCY_NO)) {
//					msg +="������CURRENCY_NO���ֶ�ֵΪ��;";
//				}
//				json1.put("CURRENCY_NO",CURRENCY_NO);
//
//				//"PAY_TYPE":"��������",
//				String PAY_TYPE = Util.null2String(rs.getString("PAY_TYPE"));
//				if("".equals(PAY_TYPE)) {
//					msg +="����������PAY_TYPE���ֶ�ֵΪ��;";
//				}
//				json1.put("PAY_TYPE",PAY_TYPE);
//
//				//"ORG_ERP_NUM":"Ӧ������",
//				String ORG_ERP_NUM = Util.null2String(rs.getString("ORG_ERP_NUM"));
//				if("".equals(ORG_ERP_NUM)) {
//					msg +="��Ӧ������ORG_ERP_NUM���ֶ�ֵΪ��;";
//				}
//				json1.put("ORG_ERP_NUM",ORG_ERP_NUM);
//
//				//"ORG_ERP_ID":"��Դϵͳ",
//				//String ORG_ERP_ID = Util.null2String(rs.getString("ORG_ERP_ID"));
//				//String ORG_ERP_ID =Util.null2String(baseBean.getPropValue("bljt20200318", "ORG_ERP_ID"));
//				if("".equals(REST_USER)) {
//					msg +="����ԴϵͳORG_ERP_ID���ֶ�ֵΪ��;";
//				}
//				json1.put("ORG_ERP_ID",REST_USER);
//
//				//"ORG_ERP_DETAIL_ID":"Ψһ����",
//				String ORG_ERP_DETAIL_ID = Util.null2String(rs.getString("ORG_ERP_DETAIL_ID"));
//				if("".equals(ORG_ERP_DETAIL_ID)) {
//					msg +="��Ψһ����ORG_ERP_DETAIL_ID���ֶ�ֵΪ��;";
//				}
//				json1.put("ORG_ERP_DETAIL_ID",ORG_ERP_DETAIL_ID);
//
//				//"CONTRACT_NUMBER":"��ͬ����",
//				String CONTRACT_NUMBER = Util.null2String(rs.getString("CONTRACT_NUMBER"));
//				if("".equals(CONTRACT_NUMBER)) {
//					//msg +="����ͬ����CONTRACT_NUMBER���ֶ�ֵΪ��;";
//				}
//				json1.put("CONTRACT_NUMBER",CONTRACT_NUMBER);
//
//				//"ORG_PAY_ID":"����������",
//				String ORG_PAY_ID = Util.null2String(rs.getString("ORG_PAY_ID"));
//				if("".equals(ORG_PAY_ID)) {
//					//msg +="������������ORG_PAY_ID���ֶ�ֵΪ��;";
//				}
//				json1.put("ORG_PAY_ID",ORG_PAY_ID);
//
//				//"CONTRACT_NAME":"��ͬ����",
//				String CONTRACT_NAME = Util.null2String(rs.getString("CONTRACT_NAME"));
//				if("".equals(CONTRACT_NAME)) {
//					//msg +="����ͬ����CONTRACT_NAME���ֶ�ֵΪ��;";
//				}
//				json1.put("CONTRACT_NAME",CONTRACT_NAME);
//
//				//"CONTRACT_AMOUNT":"��ͬ���",
//				String CONTRACT_AMOUNT = Util.null2String(rs.getString("CONTRACT_AMOUNT"));
//				if("".equals(CONTRACT_AMOUNT)) {
//					//msg +="����ͬ���CONTRACT_AMOUNT���ֶ�ֵΪ��;";
//				}
//				json1.put("CONTRACT_AMOUNT",CONTRACT_AMOUNT);
//
//				//"APPLICATION_AMOUNT":"�����",
//				String APPLICATION_AMOUNT = Util.null2String(rs.getString("APPLICATION_AMOUNT"));
//				if("".equals(APPLICATION_AMOUNT)) {
//					msg +="�������APPLICATION_AMOUNT���ֶ�ֵΪ��;";
//				}
//				json1.put("APPLICATION_AMOUNT",APPLICATION_AMOUNT);
//
//				//"APPROVAL_AMOUNT":"�������",
//				String APPROVAL_AMOUNT = Util.null2String(rs.getString("APPROVAL_AMOUNT"));
//				if("".equals(APPROVAL_AMOUNT)) {
//					msg +="���������APPROVAL_AMOUNT���ֶ�ֵΪ��;";
//				}
//				json1.put("APPROVAL_AMOUNT",APPROVAL_AMOUNT);
//
//				//"ACC_PAY_AMOUNT":"�ۼ�Ӧ�������",
//				String ACC_PAY_AMOUNT = Util.null2String(rs.getString("ACC_PAY_AMOUNT"));
//				if("".equals(ACC_PAY_AMOUNT)) {
//					//msg +="���ۼ�Ӧ�������ACC_PAY_AMOUNT���ֶ�ֵΪ��;";
//				}
//				json1.put("ACC_PAY_AMOUNT",APPROVAL_AMOUNT);
//
//				//"ACC_OCC_RATIO":"�ۼƷ�������",
//				String ACC_OCC_RATIO = Util.null2String(rs.getString("ACC_OCC_RATIO"));
//				if("".equals(ACC_OCC_RATIO)) {
//					//msg +="���ۼƷ�������ACC_OCC_RATIO���ֶ�ֵΪ��;";
//				}
//				json1.put("ACC_OCC_RATIO",ACC_OCC_RATIO);
//
//				//"CON_ACC_THIS_AMOUNT":"��ͬ�����ۼƷ�����",
//				String CON_ACC_THIS_AMOUNT = Util.null2String(rs.getString("CON_ACC_THIS_AMOUNT"));
//				if("".equals(CON_ACC_THIS_AMOUNT)) {
//					//msg +="����ͬ�����ۼƷ�����CON_ACC_THIS_AMOUNT���ֶ�ֵΪ��;";
//				}
//				json1.put("CON_ACC_THIS_AMOUNT",CON_ACC_THIS_AMOUNT);
//
//				//"CON_ACC_RATIO":"��ͬ�����ۼƷ�������",
//				String CON_ACC_RATIO = Util.null2String(rs.getString("CON_ACC_RATIO"));
//				if("".equals(CON_ACC_RATIO)) {
//					//msg +="����ͬ�����ۼƷ�������CON_ACC_RATIO���ֶ�ֵΪ��;";
//				}
//				json1.put("CON_ACC_RATIO",CON_ACC_RATIO);
//
//				//"PAY_TYPE_CONFIRMATION":"���ʽ",
//				String PAY_TYPE_CONFIRMATION = Util.null2String(rs.getString("PAY_TYPE_CONFIRMATION"));
//				if("".equals(PAY_TYPE_CONFIRMATION)) {
//					msg +="�����ʽPAY_TYPE_CONFIRMATION���ֶ�ֵΪ��;";
//				}
//				json1.put("PAY_TYPE_CONFIRMATION",PAY_TYPE_CONFIRMATION);
//
//				//"PAY_AMOUNT":"���θ�����(ʵ�ʸ�����)",
//				String PAY_AMOUNT = Util.null2String(rs.getString("PAY_AMOUNT"));
//				if("".equals(PAY_AMOUNT)) {
//					msg +="�����θ�����(ʵ�ʸ�����)PAY_AMOUNT���ֶ�ֵΪ��;";
//				}else{
//					double d =Double.parseDouble(PAY_AMOUNT);
//					DecimalFormat formt =new DecimalFormat("0.00");
//					PAY_AMOUNT =formt.format(d);
//				}
//				json1.put("PAY_AMOUNT",PAY_AMOUNT);
//
//				//"EXECUTION_PROGRESS":"ִ�н���",
//				String EXECUTION_PROGRESS = Util.null2String(rs.getString("EXECUTION_PROGRESS"));
//				if("".equals(EXECUTION_PROGRESS)) {
//					msg +="��ִ�н���EXECUTION_PROGRESS���ֶ�ֵΪ��;";
//				}
//				json1.put("EXECUTION_PROGRESS",EXECUTION_PROGRESS);
//
//				//"PAYMENT_BASIS":"��������",
//				String PAYMENT_BASIS = Util.null2String(rs.getString("PAYMENT_BASIS"));
//				if("".equals(PAYMENT_BASIS)) {
//					msg +="����������PAYMENT_BASIS���ֶ�ֵΪ��;";
//				}
//				json1.put("PAYMENT_BASIS",PAYMENT_BASIS);
//
//				//"APPROVAL_DATE":"���������������",
//				String APPROVAL_DATE = Util.null2String(rs.getString("APPROVAL_DATE"));
//				if("".equals(APPROVAL_DATE)) {
//					msg +="�����������������APPROVAL_DATE���ֶ�ֵΪ��;";
//				}
//				json1.put("APPROVAL_DATE",APPROVAL_DATE);
//
//				//"PAY_DATE":"���𸶿���������",
//				String PAY_DATE = Util.null2String(rs.getString("PAY_DATE"));
//				if("".equals(PAY_DATE)) {
//					msg +="�����𸶿���������PAY_DATE���ֶ�ֵΪ��;";
//				}
//				json1.put("PAY_DATE",PAY_DATE);
//
//				//"REMARKS":"��ע",
//				String REMARKS = Util.null2String(rs.getString("REMARKS"));
//				if("".equals(REMARKS)) {
//					//msg +="����עREMARKS���ֶ�ֵΪ��;";
//				}
//				json1.put("REMARKS",REMARKS);
//
//				//"ABSTRACT":"�����",
//				String ABSTRACT = Util.null2String(rs.getString("ABSTRACT"));
//				if("".equals(ABSTRACT)) {
//					msg +="�������ABSTRACT���ֶ�ֵΪ��;";
//				}
//				json1.put("ABSTRACT",ABSTRACT);
//
//				//"BILL_TYPE":"Ʊ������",
//				String BILL_TYPE = Util.null2String(rs.getString("BILL_TYPE"));
//				if("".equals(BILL_TYPE)) {
//					//msg +="��Ʊ������BILL_TYPE���ֶ�ֵΪ��;";
//				}
//				json1.put("BILL_TYPE",BILL_TYPE);
//
//				//"WAYSINCOME":"���շ�ʽ",
//				String WAYSINCOME = Util.null2String(rs.getString("WAYSINCOME"));
//				if("".equals(WAYSINCOME)) {
//					//msg +="�����շ�ʽWAYSINCOME���ֶ�ֵΪ��;";
//				}
//				json1.put("WAYSINCOME",WAYSINCOME);
//
//				//"FINGERPRINT_CODE":"ָ����",
//				//String FINGERPRINT_CODE = Util.null2String(rs.getString("FINGERPRINT_CODE"));
//				String fc=PAY_NAME+RECE_ACC_NAME+RECE_ACC_NO+PAY_AMOUNT;
//				baseBean.writeLog("����ǰָ���룺"+fc);
//				String FINGERPRINT_CODE = BaoljtUtil.standardMD5(fc).toUpperCase();
//				baseBean.writeLog("���ܺ�ָ���룺"+FINGERPRINT_CODE);
//				if("".equals(FINGERPRINT_CODE)) {
//					msg +="��ָ����FINGERPRINT_CODE���ֶ�ֵΪ��;";
//				}
//				json1.put("FINGERPRINT_CODE",FINGERPRINT_CODE);
//				String mainid = Util.null2String(rs.getString("id"));
//
//				String dtmsg ="";
//
//				//����1�������Ŀ����֧��Ϣ�� begin
//				String dt1tablename = maintablename+"_dt1";
//				String sqldt1 = "select * from "+dt1tablename+" where mainid="+mainid;
//				rs1.execute(sqldt1);
//				JSONArray jsonarr1 = new JSONArray();
//				while(rs1.next()) {
//					JSONObject jsontemp =  new JSONObject();
//					//"REAL_ESTATE_PROJECT_CODE": "���ز���Ŀ",
//					jsontemp.put("REAL_ESTATE_PROJECT_CODE", Util.null2String(rs1.getString("REAL_ESTATE_PROJECT_CODE")));
//					//"REAL_ESTATE_PROJECT_CODE_NO":"���ز���Ŀ����",
//					jsontemp.put("REAL_ESTATE_PROJECT_CODE_NO", Util.null2String(rs1.getString("REAL_ESTATE_PROJECT_CODE_NO")));
//					//"INCOME_EXPENDITURE_ITEMS_CODE":"��֧��Ŀ",
//					jsontemp.put("INCOME_EXPENDITURE_ITEMS_CODE", Util.null2String(rs1.getString("INCOME_EXPENDITURE_ITEMS_CODE")));
//					//"INCOME_EXPENDITURE_ITEMS_CODE_NO":"��֧��Ŀ����",
//					jsontemp.put("INCOME_EXPENDITURE_ITEMS_CODE_NO", Util.null2String(rs1.getString("INCOME_EXPENDITURE_ITEMS_CODE_NO")));
//					//"ACCOUNTING_SUBJECTS":"��ƿ�Ŀ",
//					jsontemp.put("ACCOUNTING_SUBJECTS", Util.null2String(rs1.getString("ACCOUNTING_SUBJECTS")));
//					//"ACCOUNTING_SUBJECTS_NO":"��ƿ�Ŀ����",
//					jsontemp.put("ACCOUNTING_SUBJECTS_NO", Util.null2String(rs1.getString("ACCOUNTING_SUBJECTS_NO")));
//					//"ACCOUNTING_ABSTRACT":"��Ƶ���ժҪ",
//					jsontemp.put("ACCOUNTING_ABSTRACT", Util.null2String(rs1.getString("ACCOUNTING_ABSTRACT")));
//					//"ACCOUNTING_ABSTRACT_NO":"��Ƶ���ժҪ����"
//					jsontemp.put("ACCOUNTING_ABSTRACT_NO", Util.null2String(rs1.getString("ACCOUNTING_ABSTRACT_NO")));
//					jsonarr1.add(jsontemp);
//				}
//				json1.put("DETAIL1", jsonarr1);
//				//����1�������Ŀ����֧��Ϣ�� end
//
//				//����2����Ʊ��Ϣ��begin
//				String dt2tablename =maintablename+"_dt2";
//				String sqldt2="select * from "+dt2tablename+" where mainid ="+mainid;
//				rs1.execute(sqldt2);
//				jsonarr1 = new JSONArray();
//				while(rs1.next()) {
//					JSONObject jsontemp =  new JSONObject();
//					//"INVOICE_CODE" : "��Ʊ����",
//					jsontemp.put("INVOICE_CODE", Util.null2String(rs1.getString("INVOICE_CODE")));
//					//"INVOICE_NO": "��Ʊ����",
//					jsontemp.put("INVOICE_NO", Util.null2String(rs1.getString("INVOICE_NO")));
//					//"PAPER_DREW_DATE":"��Ʊ����",
//					jsontemp.put("PAPER_DREW_DATE", Util.null2String(rs1.getString("PAPER_DREW_DATE")));
//					//"AMOUNT_WITH_TAX":"��Ʊ��˰���",
//					jsontemp.put("AMOUNT_WITH_TAX", Util.null2String(rs1.getString("AMOUNT_WITH_TAX")));
//					//"AMOUNT_NO_TAX":"��Ʊ����˰���"
//					jsontemp.put("AMOUNT_NO_TAX", Util.null2String(rs1.getString("AMOUNT_NO_TAX")));
//					jsonarr1.add(jsontemp);
//				}
//				json1.put("DETAIL2", jsonarr1);
//				//����2����Ʊ��Ϣ��end
//
//				//����3���������뵥������¼��Ϣ��begin
//				String dt3tablename =maintablename+"_dt3";
//				String sqldt3="select * from "+dt3tablename+" where mainid ="+mainid;
//				rs1.execute(sqldt3);
//				jsonarr1 = new JSONArray();
//				while(rs1.next()) {
//					JSONObject jsontemp =  new JSONObject();
//					//"STEP_NO":"��������",
//					jsontemp.put("STEP_NO", Util.null2String(rs1.getString("STEP_NO")));
//					//"APPROVER":"������",
//					jsontemp.put("APPROVER", Util.null2String(rs1.getString("APPROVER")));
//					//"APPROVER_COMPANY":"������������˾",
//					jsontemp.put("APPROVER_COMPANY", Util.null2String(rs1.getString("APPROVER_COMPANY")));
//					//"PROCESS":"��������",
//					jsontemp.put("PROCESS", Util.null2String(rs1.getString("PROCESS")));
//					//"APPROVAL_TIME":"����ʱ��",
//					jsontemp.put("APPROVAL_TIME", Util.null2String(rs1.getString("APPROVAL_TIME")));
//					//"APPROVAL_RESULT":"�������",
//					jsontemp.put("APPROVAL_RESULT", Util.null2String(rs1.getString("APPROVAL_RESULT")));
//					//"APPROVAL_OPINION":"�������"
//					jsontemp.put("APPROVAL_OPINION", Util.null2String(rs1.getString("APPROVAL_OPINION")));
//					jsonarr1.add(jsontemp);
//				}
//				json1.put("DETAIL3", jsonarr1);
//				//����3���������뵥������¼��Ϣ��end
//
//				//����4���������뵥˰����Ϣ�� begin
//				String dt4tablename =maintablename+"_dt4";
//				String sqldt4="select * from "+dt4tablename+" where mainid ="+mainid;
//				rs1.execute(sqldt4);
//				jsonarr1 = new JSONArray();
//				while(rs1.next()) {
//					JSONObject jsontemp =  new JSONObject();
//					//"COMMODITY_NAME":"��Ʒ��ϸ",
//					jsontemp.put("COMMODITY_NAME", Util.null2String(rs1.getString("COMMODITY_NAME")));
//					//"ITEM_NAME":"��Ʒ���������",
//					jsontemp.put("ITEM_NAME", Util.null2String(rs1.getString("ITEM_NAME")));
//					//"PAYMENT_AMOUNT_WITH_TAX":"�ƻ��������˰��",
//					jsontemp.put("PAYMENT_AMOUNT_WITH_TAX", Util.null2String(rs1.getString("PAYMENT_AMOUNT_WITH_TAX")));
//					//"TAX_RATE":"˰��",
//					jsontemp.put("TAX_RATE", Util.null2String(rs1.getString("TAX_RATE")));
//					//"PAYMENT_AMOUNT_WITHOUT_TAX":"�ƻ����������˰��",
//					jsontemp.put("PAYMENT_AMOUNT_WITHOUT_TAX", Util.null2String(rs1.getString("PAYMENT_AMOUNT_WITHOUT_TAX")));
//					//"TAX_AMOUNT":"�ƻ�˰��"
//					jsontemp.put("TAX_AMOUNT", Util.null2String(rs1.getString("TAX_AMOUNT")));
//					jsonarr1.add(jsontemp);
//				}
//				json1.put("DETAIL4", jsonarr1);
//				//����4���������뵥˰����Ϣ�� end
//
//				//����5���������뵥�ۿ���Ϣ�� begin
//				String dt5tablename =maintablename+"_dt5";
//				String sqldt5="select * from "+dt5tablename+" where mainid ="+mainid;
//				rs1.execute(sqldt5);
//				jsonarr1 = new JSONArray();
//				while(rs1.next()) {
//					JSONObject jsontemp =  new JSONObject();
//					//"REASON":"�ۿ�ԭ��",
//					jsontemp.put("REASON", Util.null2String(rs1.getString("REASON")));
//					//"AMOUNT":"���οۿ���"
//					jsontemp.put("AMOUNT", Util.null2String(rs1.getString("AMOUNT")));
//
//					jsonarr1.add(jsontemp);
//				}
//				json1.put("DETAIL5", jsonarr1);
//				//����5���������뵥�ۿ���Ϣ�� end
//
//				//����6��Ӧ����˰����Ϣ��begin
//				String dt6tablename =maintablename+"_dt6";
//				String sqldt6="select * from "+dt6tablename+" where mainid ="+mainid;
//				rs1.execute(sqldt6);
//				jsonarr1 = new JSONArray();
//				while(rs1.next()) {
//					JSONObject jsontemp =  new JSONObject();
//					//"PAYMENT_AMOUNT_WITH_TAX":"Ӧ�����������˰��",
//					jsontemp.put("PAYMENT_AMOUNT_WITH_TAX", Util.null2String(rs1.getString("PAYMENT_AMOUNT_WITH_TAX")));
//					//"TAX_RATE":"Ӧ����˰��",
//					jsontemp.put("TAX_RATE", Util.null2String(rs1.getString("TAX_RATE")));
//					//"PAYMENT_AMOUNT_WITHOUT_TAX":"Ӧ�������������˰��",
//					jsontemp.put("PAYMENT_AMOUNT_WITHOUT_TAX", Util.null2String(rs1.getString("PAYMENT_AMOUNT_WITHOUT_TAX")));
//					//"TAX_AMOUNT":"Ӧ����˰��"
//					jsontemp.put("TAX_AMOUNT", Util.null2String(rs1.getString("TAX_AMOUNT")));
//					jsonarr1.add(jsontemp);
//				}
//				json1.put("DETAIL5", jsonarr1);
//				//����6��Ӧ����˰����Ϣ��end
//
//				//����7����ͬ������Ŀ��Ϣ��begin
//				String dt7tablename =maintablename+"_dt7";
//				String sqldt7="select * from "+dt7tablename+" where mainid ="+mainid;
//				rs1.execute(sqldt7);
//				jsonarr1 = new JSONArray();
//				while(rs1.next()) {
//					JSONObject jsontemp =  new JSONObject();
//					//"PROJECT_CODE":"��Ŀ���",
//					jsontemp.put("PROJECT_CODE", Util.null2String(rs1.getString("PROJECT_CODE")));
//					//"PROJECT_NAME":"��Ŀ����"
//					jsontemp.put("PROJECT_NAME", Util.null2String(rs1.getString("PROJECT_NAME")));
//					jsonarr1.add(jsontemp);
//				}
//				json1.put("DETAIL7", jsonarr1);
//				//����7����ͬ������Ŀ��Ϣ��end
//
//				//����8��������begin
//				String dt8tablename =maintablename+"_dt8";
//				String sqldt8="select * from "+dt8tablename+" where mainid ="+mainid;
//				rs1.execute(sqldt8);
//				jsonarr1 = new JSONArray();
//				while(rs1.next()) {
//					JSONObject jsontemp =  new JSONObject();
//					//"ANNEX_ID":"����Ψһid",
//					jsontemp.put("ANNEX_ID", Util.null2String(rs1.getString("ANNEX_ID")));
//					//"ANNEX_NAME":"������",
//					jsontemp.put("ANNEX_NAME", Util.null2String(rs1.getString("ANNEX_NAME")));
//					//"ROUTE":"·��"
//					jsontemp.put("ROUTE", Util.null2String(rs1.getString("ROUTE")));
//					jsonarr1.add(jsontemp);
//				}
//				json1.put("DETAIL8", jsonarr1);
//				//����8��������end
//
//				jsonarr0.add(json1);
//				json0.put("DATAS", jsonarr0);
//
//				baseBean.writeLog("������"+json0.toString());
//				baseBean.writeLog("У��msg��"+msg);
//
//				if("".equals(msg)) {
//					//���ӿ�
//					setData(json0.toString(),maintablename,mainid,rqxh);
//				}else {
//					//��������
//					String sqlupdate = "update "+maintablename+" set RET_CODE=?,RET_MSG=? where id="+mainid ;
//					baseBean.writeLog("ʧ�ܣ�"+sqlupdate);
//					String [] params = new String[2];
//					params[0]= "0";
//					params[1]= msg;
//					rs2.executeUpdate(sqlupdate, params);
//				}
//			}
//
//		} catch (Exception e) {
//			baseBean.writeLog(e);
//			e.printStackTrace();
//			flag =false;
//		}
//		return flag;
//	}
//
//	/**
//	 * ���ø���ӿ�
//	 * @return
//	 */
//	public void setData(String temp,String maintablename,String mainid,int rqxh) {
//		Object lock = new Object();
//		String msg ="";
//		RecordSet rs2 = new RecordSet();
//		//���ýӿڿ�ʼ
//		BaseBean baseBean = new BaseBean();
//
//		String  address =Util.null2String(baseBean.getPropValue("bljt20200318", "address_test"));
//
//		baseBean.writeLog("��ʼ���ӿ�");
//		TxServiceGatewayProxy txServiceGatewayProxy = new TxServiceGatewayProxy();
//		txServiceGatewayProxy.setEndpoint(address);
//		try {
//			synchronized (lock) {
//				baseBean.writeLog("�ӿڵ�ַ==>"+address);
//				msg = txServiceGatewayProxy.send(temp);
//				baseBean.writeLog("�ӿڷ���msg��"+msg);
//				//System.out.println(msg);
//			}
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			msg="{\"HEAD\":{\"EsbCode\":\"-1\",\"EsbDesc\":\"���ýӿڱ���"+e.getMessage()+"\"},\"BODY\":{}";
//			baseBean.writeLog("�ӿ��쳣msg��"+msg);
//		}
//
//		JSONObject rejson = JSONObject.fromObject(msg);
//		JSONObject head = rejson.getJSONObject("HEAD");
//		String EsbCode = head.getString("EsbCode");
//		String EsbDesc = head.getString("EsbDesc");
//		if("0000000".equals(EsbCode)) {
//			//
//			JSONObject body = rejson.getJSONObject("BODY");
//			JSONArray datasarr = body.getJSONArray("DATAS");
//			for(int i=0 ;i<datasarr.size();i++) {
//				JSONObject tempjson = (JSONObject)datasarr.get(i);
//
//				//��������
//				String sqlupdate = " update "+maintablename+" set RET_CODE=?,RET_MSG=? where id="+mainid ;
//				baseBean.writeLog("�ɹ���"+sqlupdate);
//				String [] params = new String[2];
//				params[0]= tempjson.getString("RET_CODE");
//				params[1]= tempjson.getString("RET_MSG");
//				rs2.executeUpdate(sqlupdate, params);
//			}
//
//		}else if("-1".equals(EsbCode)) {
//			//��������
//			String sqlupdate = "update "+maintablename+" set RET_CODE=?,RET_MSG=? where id="+mainid ;
//			baseBean.writeLog("ʧ�ܣ�"+sqlupdate);
//			String [] params = new String[2];
//			params[0]= "0";
//			params[1]= EsbDesc;
//			rs2.executeUpdate(sqlupdate, params);
//
//		}else {
//			//��������
//			String sqlupdate = "update "+maintablename+" set RET_CODE=?,RET_MSG=? where id="+mainid ;
//			baseBean.writeLog("ʧ�ܣ�"+sqlupdate);
//			String [] params = new String[2];
//			params[0]= "0";
//			params[1]= EsbDesc;
//			rs2.executeUpdate(sqlupdate, params);
//
//		}
//
//		setlog("PAYINTER", rqxh, temp, msg);
//		//���ýӿڽ���
//		baseBean.writeLog("���ӿڽ���");
//	}
//
//	public void setlog(String rqfn,int rqxh,String cs,String rt) {
//		RecordSet rs1 = new RecordSet();
//		RecordSet rs2 = new RecordSet();
//		RecordSet rs3 = new RecordSet();
//		//���ýӿڿ�ʼ
//		BaseBean baseBean = new BaseBean();
//		String  log_tablename =Util.null2String(baseBean.getPropValue("bljt20200318", "log_tablename"));
//		int  log_modeid =Util.getIntValue(baseBean.getPropValue("bljt20200318", "log_modeid"));
//		int  log_formid =Util.getIntValue(baseBean.getPropValue("bljt20200318", "log_formid"));
//		FxExcelModeDataIdUpdate modeDataIdUpdate = new FxExcelModeDataIdUpdate();
//		String currentDateTime = TimeUtil.getCurrentTimeString();
//		String currentdate = currentDateTime.substring(0, 10);
//		String currenttime = currentDateTime.substring(11,16);
//
//		int id_temp=-1;
//		id_temp = modeDataIdUpdate.getModeDataNewId(log_tablename,log_modeid, 1, 0, currentdate, currenttime);
//		if(id_temp>0) {
//			String [] parmas = new String [7];
//			parmas[0] = rqfn;
//			parmas[1] = currentDateTime;
//			parmas[2] = currentdate;
//			parmas[3] = currenttime;
//			parmas[4] = rqxh+"";
//			parmas[5] = cs;
//			parmas[6] = rt;
//			String updatesql =" update "+log_tablename+" set qqjkff=?,qqsj=?,rq=?,sj=?,dtqqxh=?,qccs=?,fhjg=? where id="+id_temp;
//			baseBean.writeLog("updatesql:"+updatesql);
//			rs1.executeUpdate(updatesql, parmas);
//
//			// ������������ݵ�Ĭ��Ȩ
//			ModeRightInfo ModeRightInfo = new ModeRightInfo();
//			ModeRightInfo.setNewRight(true);
//			ModeRightInfo.editModeDataShare(1, log_modeid, id_temp);
//		}
//	}
//}
