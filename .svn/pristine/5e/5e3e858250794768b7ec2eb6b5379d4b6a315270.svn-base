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
//	 * 定时任务
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
//				//获取请求序号
//				String log_sql ="select (max(dtqqxh)+1) as rqxh  from "+log_tablename+" where rq='"+sdf1.format(new Date())+"'";
//				rs3.execute(log_sql);
//				if(rs3.next()) {
//					rqxh = Util.getIntValue(rs3.getString("rqxh"),1);
//				}else {
//					rqxh = 1;
//				}
//
//				//验证信息
//				//"BATCH_NO": "112020031800001", //数字，每次请求的批次号都不重复,根据系统分配编码+年月日+00001排序
//				String ORG_ERP_ID =Util.null2String(baseBean.getPropValue("bljt20200318", "ORG_ERP_ID"));
//				NumberFormat formatter = NumberFormat.getNumberInstance();
//				formatter.setMinimumIntegerDigits(5);
//				formatter.setGroupingUsed(false);
//				String rqxhtemp = formatter.format(rqxh);
//				String BATCH_NO = ORG_ERP_ID + sdf.format(new Date()) + rqxhtemp;
//				json0.put("BATCH_NO", BATCH_NO);
//				//"BIZCODE": "PAYINTER", //代表下面哪种接口付款:PAYINTER,付款单状态反馈:PAYRETURN,;
//				json0.put("BIZCODE", "PAYINTER");
//				//"SESSIONID": "OA/明源登录SessionID",--时间戳， 精确到秒， 17 ， 不带任何字符；
//				long currentDateTime = System.currentTimeMillis();
//				json0.put("SESSIONID", currentDateTime);
//				//"REST_USER": "ABC", //此处为请求方在资金系统中注册的身份
//				String REST_USER =Util.null2String(baseBean.getPropValue("bljt20200318", "REST_USER"));
//				json0.put("REST_USER", REST_USER);
//				//"CHANNEL": "G6", //?数据源系统(OA/明源等)
//				String CHANNEL =Util.null2String(baseBean.getPropValue("bljt20200318", "CHANNEL"));
//				json0.put("CHANNEL", CHANNEL);
//				//此处用USER+PWD进行MD5算法加密后转大写,OA/明源密码暂定
//				//"REST_PWD": "0EE1EF4C3AE4F95E301F9D3F7DF95696"
//				String PWD =Util.null2String(baseBean.getPropValue("bljt20200318", "PWD"));
//				String REST_PWD = BaoljtUtil.standardMD5(REST_USER+PWD).toUpperCase();
//				json0.put("REST_PWD", REST_PWD);
//				//主表数据
//				//String WF_NUMBER = Util.null2String(rs.getString("WF_NUMBER"));
//				//if("".equals(WF_NUMBER)) {
//				//	msg +="【流程编号WF_NUMBER】字段值为空;";
//				//}
//				//json0.put("BATCH_NO",WF_NUMBER );
//
//				//"PAY_NAME": "付款方公司",
//				String PAY_NAME = Util.null2String(rs.getString("PAY_NAME"));
//				if("".equals(PAY_NAME)) {
//					msg +="【付款方公司PAY_NAME】字段值为空;";
//				}
//				json1.put("PAY_NAME",PAY_NAME);
//
//				//"PAY_BANK": "付款方开户行",
//				String PAY_BANK = Util.null2String(rs.getString("PAY_BANK"));
//				if("".equals(PAY_BANK)) {
//					msg +="【付款方开户行PAY_BANK】字段值为空;";
//				}
//				json1.put("PAY_BANK",PAY_BANK);
//
//				//"PAY_ACNT_NO":"付款方账号",
//				String PAY_ACNT_NO = Util.null2String(rs.getString("PAY_ACNT_NO"));
//				if("".equals(PAY_ACNT_NO)) {
//					msg +="【付款方账号PAY_ACNT_NO】字段值为空;";
//				}
//				json1.put("PAY_ACNT_NO",PAY_ACNT_NO);
//
//				//"RECE_ACC_NAME":"供应商名称",
//				String RECE_ACC_NAME = Util.null2String(rs.getString("RECE_ACC_NAME"));
//				if("".equals(RECE_ACC_NAME)) {
//					msg +="【供应商名称RECE_ACC_NAME】字段值为空;";
//				}
//				json1.put("RECE_ACC_NAME",RECE_ACC_NAME);
//
//				//"RECE_OPBANK_NAME":"收款方开户行",
//				String RECE_OPBANK_NAME = Util.null2String(rs.getString("RECE_OPBANK_NAME"));
//				if("".equals(RECE_OPBANK_NAME)) {
//					msg +="【收款方开户行RECE_OPBANK_NAME】字段值为空;";
//				}
//				json1.put("RECE_OPBANK_NAME",RECE_OPBANK_NAME);
//
//				//"RECE_ACC_NO":"收款方账号",
//				String RECE_ACC_NO = Util.null2String(rs.getString("RECE_ACC_NO"));
//				if("".equals(RECE_ACC_NO)) {
//					msg +="【收款方账号RECE_ACC_NO】字段值为空;";
//				}
//				json1.put("RECE_ACC_NO",RECE_ACC_NO);
//
//				//"CURRENCY_NO":"币种",
//				String CURRENCY_NO = Util.null2String(rs.getString("CURRENCY_NO"));
//				if("".equals(CURRENCY_NO)) {
//					msg +="【币种CURRENCY_NO】字段值为空;";
//				}
//				json1.put("CURRENCY_NO",CURRENCY_NO);
//
//				//"PAY_TYPE":"付款类型",
//				String PAY_TYPE = Util.null2String(rs.getString("PAY_TYPE"));
//				if("".equals(PAY_TYPE)) {
//					msg +="【付款类型PAY_TYPE】字段值为空;";
//				}
//				json1.put("PAY_TYPE",PAY_TYPE);
//
//				//"ORG_ERP_NUM":"应付单号",
//				String ORG_ERP_NUM = Util.null2String(rs.getString("ORG_ERP_NUM"));
//				if("".equals(ORG_ERP_NUM)) {
//					msg +="【应付单号ORG_ERP_NUM】字段值为空;";
//				}
//				json1.put("ORG_ERP_NUM",ORG_ERP_NUM);
//
//				//"ORG_ERP_ID":"来源系统",
//				//String ORG_ERP_ID = Util.null2String(rs.getString("ORG_ERP_ID"));
//				//String ORG_ERP_ID =Util.null2String(baseBean.getPropValue("bljt20200318", "ORG_ERP_ID"));
//				if("".equals(REST_USER)) {
//					msg +="【来源系统ORG_ERP_ID】字段值为空;";
//				}
//				json1.put("ORG_ERP_ID",REST_USER);
//
//				//"ORG_ERP_DETAIL_ID":"唯一编码",
//				String ORG_ERP_DETAIL_ID = Util.null2String(rs.getString("ORG_ERP_DETAIL_ID"));
//				if("".equals(ORG_ERP_DETAIL_ID)) {
//					msg +="【唯一编码ORG_ERP_DETAIL_ID】字段值为空;";
//				}
//				json1.put("ORG_ERP_DETAIL_ID",ORG_ERP_DETAIL_ID);
//
//				//"CONTRACT_NUMBER":"合同编码",
//				String CONTRACT_NUMBER = Util.null2String(rs.getString("CONTRACT_NUMBER"));
//				if("".equals(CONTRACT_NUMBER)) {
//					//msg +="【合同编码CONTRACT_NUMBER】字段值为空;";
//				}
//				json1.put("CONTRACT_NUMBER",CONTRACT_NUMBER);
//
//				//"ORG_PAY_ID":"付款申请编号",
//				String ORG_PAY_ID = Util.null2String(rs.getString("ORG_PAY_ID"));
//				if("".equals(ORG_PAY_ID)) {
//					//msg +="【付款申请编号ORG_PAY_ID】字段值为空;";
//				}
//				json1.put("ORG_PAY_ID",ORG_PAY_ID);
//
//				//"CONTRACT_NAME":"合同名称",
//				String CONTRACT_NAME = Util.null2String(rs.getString("CONTRACT_NAME"));
//				if("".equals(CONTRACT_NAME)) {
//					//msg +="【合同名称CONTRACT_NAME】字段值为空;";
//				}
//				json1.put("CONTRACT_NAME",CONTRACT_NAME);
//
//				//"CONTRACT_AMOUNT":"合同金额",
//				String CONTRACT_AMOUNT = Util.null2String(rs.getString("CONTRACT_AMOUNT"));
//				if("".equals(CONTRACT_AMOUNT)) {
//					//msg +="【合同金额CONTRACT_AMOUNT】字段值为空;";
//				}
//				json1.put("CONTRACT_AMOUNT",CONTRACT_AMOUNT);
//
//				//"APPLICATION_AMOUNT":"请款金额",
//				String APPLICATION_AMOUNT = Util.null2String(rs.getString("APPLICATION_AMOUNT"));
//				if("".equals(APPLICATION_AMOUNT)) {
//					msg +="【请款金额APPLICATION_AMOUNT】字段值为空;";
//				}
//				json1.put("APPLICATION_AMOUNT",APPLICATION_AMOUNT);
//
//				//"APPROVAL_AMOUNT":"审批金额",
//				String APPROVAL_AMOUNT = Util.null2String(rs.getString("APPROVAL_AMOUNT"));
//				if("".equals(APPROVAL_AMOUNT)) {
//					msg +="【审批金额APPROVAL_AMOUNT】字段值为空;";
//				}
//				json1.put("APPROVAL_AMOUNT",APPROVAL_AMOUNT);
//
//				//"ACC_PAY_AMOUNT":"累计应付单金额",
//				String ACC_PAY_AMOUNT = Util.null2String(rs.getString("ACC_PAY_AMOUNT"));
//				if("".equals(ACC_PAY_AMOUNT)) {
//					//msg +="【累计应付单金额ACC_PAY_AMOUNT】字段值为空;";
//				}
//				json1.put("ACC_PAY_AMOUNT",APPROVAL_AMOUNT);
//
//				//"ACC_OCC_RATIO":"累计发生比率",
//				String ACC_OCC_RATIO = Util.null2String(rs.getString("ACC_OCC_RATIO"));
//				if("".equals(ACC_OCC_RATIO)) {
//					//msg +="【累计发生比率ACC_OCC_RATIO】字段值为空;";
//				}
//				json1.put("ACC_OCC_RATIO",ACC_OCC_RATIO);
//
//				//"CON_ACC_THIS_AMOUNT":"合同本次累计发生额",
//				String CON_ACC_THIS_AMOUNT = Util.null2String(rs.getString("CON_ACC_THIS_AMOUNT"));
//				if("".equals(CON_ACC_THIS_AMOUNT)) {
//					//msg +="【合同本次累计发生额CON_ACC_THIS_AMOUNT】字段值为空;";
//				}
//				json1.put("CON_ACC_THIS_AMOUNT",CON_ACC_THIS_AMOUNT);
//
//				//"CON_ACC_RATIO":"合同本次累计发生比率",
//				String CON_ACC_RATIO = Util.null2String(rs.getString("CON_ACC_RATIO"));
//				if("".equals(CON_ACC_RATIO)) {
//					//msg +="【合同本次累计发生比率CON_ACC_RATIO】字段值为空;";
//				}
//				json1.put("CON_ACC_RATIO",CON_ACC_RATIO);
//
//				//"PAY_TYPE_CONFIRMATION":"付款方式",
//				String PAY_TYPE_CONFIRMATION = Util.null2String(rs.getString("PAY_TYPE_CONFIRMATION"));
//				if("".equals(PAY_TYPE_CONFIRMATION)) {
//					msg +="【付款方式PAY_TYPE_CONFIRMATION】字段值为空;";
//				}
//				json1.put("PAY_TYPE_CONFIRMATION",PAY_TYPE_CONFIRMATION);
//
//				//"PAY_AMOUNT":"本次付款金额(实际付款金额)",
//				String PAY_AMOUNT = Util.null2String(rs.getString("PAY_AMOUNT"));
//				if("".equals(PAY_AMOUNT)) {
//					msg +="【本次付款金额(实际付款金额)PAY_AMOUNT】字段值为空;";
//				}else{
//					double d =Double.parseDouble(PAY_AMOUNT);
//					DecimalFormat formt =new DecimalFormat("0.00");
//					PAY_AMOUNT =formt.format(d);
//				}
//				json1.put("PAY_AMOUNT",PAY_AMOUNT);
//
//				//"EXECUTION_PROGRESS":"执行进度",
//				String EXECUTION_PROGRESS = Util.null2String(rs.getString("EXECUTION_PROGRESS"));
//				if("".equals(EXECUTION_PROGRESS)) {
//					msg +="【执行进度EXECUTION_PROGRESS】字段值为空;";
//				}
//				json1.put("EXECUTION_PROGRESS",EXECUTION_PROGRESS);
//
//				//"PAYMENT_BASIS":"付款依据",
//				String PAYMENT_BASIS = Util.null2String(rs.getString("PAYMENT_BASIS"));
//				if("".equals(PAYMENT_BASIS)) {
//					msg +="【付款依据PAYMENT_BASIS】字段值为空;";
//				}
//				json1.put("PAYMENT_BASIS",PAYMENT_BASIS);
//
//				//"APPROVAL_DATE":"付款审批完成日期",
//				String APPROVAL_DATE = Util.null2String(rs.getString("APPROVAL_DATE"));
//				if("".equals(APPROVAL_DATE)) {
//					msg +="【付款审批完成日期APPROVAL_DATE】字段值为空;";
//				}
//				json1.put("APPROVAL_DATE",APPROVAL_DATE);
//
//				//"PAY_DATE":"发起付款申请日期",
//				String PAY_DATE = Util.null2String(rs.getString("PAY_DATE"));
//				if("".equals(PAY_DATE)) {
//					msg +="【发起付款申请日期PAY_DATE】字段值为空;";
//				}
//				json1.put("PAY_DATE",PAY_DATE);
//
//				//"REMARKS":"备注",
//				String REMARKS = Util.null2String(rs.getString("REMARKS"));
//				if("".equals(REMARKS)) {
//					//msg +="【备注REMARKS】字段值为空;";
//				}
//				json1.put("REMARKS",REMARKS);
//
//				//"ABSTRACT":"付款附言",
//				String ABSTRACT = Util.null2String(rs.getString("ABSTRACT"));
//				if("".equals(ABSTRACT)) {
//					msg +="【付款附言ABSTRACT】字段值为空;";
//				}
//				json1.put("ABSTRACT",ABSTRACT);
//
//				//"BILL_TYPE":"票据类型",
//				String BILL_TYPE = Util.null2String(rs.getString("BILL_TYPE"));
//				if("".equals(BILL_TYPE)) {
//					//msg +="【票据类型BILL_TYPE】字段值为空;";
//				}
//				json1.put("BILL_TYPE",BILL_TYPE);
//
//				//"WAYSINCOME":"征收方式",
//				String WAYSINCOME = Util.null2String(rs.getString("WAYSINCOME"));
//				if("".equals(WAYSINCOME)) {
//					//msg +="【征收方式WAYSINCOME】字段值为空;";
//				}
//				json1.put("WAYSINCOME",WAYSINCOME);
//
//				//"FINGERPRINT_CODE":"指纹码",
//				//String FINGERPRINT_CODE = Util.null2String(rs.getString("FINGERPRINT_CODE"));
//				String fc=PAY_NAME+RECE_ACC_NAME+RECE_ACC_NO+PAY_AMOUNT;
//				baseBean.writeLog("解密前指纹码："+fc);
//				String FINGERPRINT_CODE = BaoljtUtil.standardMD5(fc).toUpperCase();
//				baseBean.writeLog("解密后指纹码："+FINGERPRINT_CODE);
//				if("".equals(FINGERPRINT_CODE)) {
//					msg +="【指纹码FINGERPRINT_CODE】字段值为空;";
//				}
//				json1.put("FINGERPRINT_CODE",FINGERPRINT_CODE);
//				String mainid = Util.null2String(rs.getString("id"));
//
//				String dtmsg ="";
//
//				//表体1（财务科目及收支信息） begin
//				String dt1tablename = maintablename+"_dt1";
//				String sqldt1 = "select * from "+dt1tablename+" where mainid="+mainid;
//				rs1.execute(sqldt1);
//				JSONArray jsonarr1 = new JSONArray();
//				while(rs1.next()) {
//					JSONObject jsontemp =  new JSONObject();
//					//"REAL_ESTATE_PROJECT_CODE": "房地产项目",
//					jsontemp.put("REAL_ESTATE_PROJECT_CODE", Util.null2String(rs1.getString("REAL_ESTATE_PROJECT_CODE")));
//					//"REAL_ESTATE_PROJECT_CODE_NO":"房地产项目编码",
//					jsontemp.put("REAL_ESTATE_PROJECT_CODE_NO", Util.null2String(rs1.getString("REAL_ESTATE_PROJECT_CODE_NO")));
//					//"INCOME_EXPENDITURE_ITEMS_CODE":"收支项目",
//					jsontemp.put("INCOME_EXPENDITURE_ITEMS_CODE", Util.null2String(rs1.getString("INCOME_EXPENDITURE_ITEMS_CODE")));
//					//"INCOME_EXPENDITURE_ITEMS_CODE_NO":"收支项目编码",
//					jsontemp.put("INCOME_EXPENDITURE_ITEMS_CODE_NO", Util.null2String(rs1.getString("INCOME_EXPENDITURE_ITEMS_CODE_NO")));
//					//"ACCOUNTING_SUBJECTS":"会计科目",
//					jsontemp.put("ACCOUNTING_SUBJECTS", Util.null2String(rs1.getString("ACCOUNTING_SUBJECTS")));
//					//"ACCOUNTING_SUBJECTS_NO":"会计科目编码",
//					jsontemp.put("ACCOUNTING_SUBJECTS_NO", Util.null2String(rs1.getString("ACCOUNTING_SUBJECTS_NO")));
//					//"ACCOUNTING_ABSTRACT":"会计单据摘要",
//					jsontemp.put("ACCOUNTING_ABSTRACT", Util.null2String(rs1.getString("ACCOUNTING_ABSTRACT")));
//					//"ACCOUNTING_ABSTRACT_NO":"会计单据摘要编码"
//					jsontemp.put("ACCOUNTING_ABSTRACT_NO", Util.null2String(rs1.getString("ACCOUNTING_ABSTRACT_NO")));
//					jsonarr1.add(jsontemp);
//				}
//				json1.put("DETAIL1", jsonarr1);
//				//表体1（财务科目及收支信息） end
//
//				//表体2（发票信息）begin
//				String dt2tablename =maintablename+"_dt2";
//				String sqldt2="select * from "+dt2tablename+" where mainid ="+mainid;
//				rs1.execute(sqldt2);
//				jsonarr1 = new JSONArray();
//				while(rs1.next()) {
//					JSONObject jsontemp =  new JSONObject();
//					//"INVOICE_CODE" : "发票代码",
//					jsontemp.put("INVOICE_CODE", Util.null2String(rs1.getString("INVOICE_CODE")));
//					//"INVOICE_NO": "发票号码",
//					jsontemp.put("INVOICE_NO", Util.null2String(rs1.getString("INVOICE_NO")));
//					//"PAPER_DREW_DATE":"开票日期",
//					jsontemp.put("PAPER_DREW_DATE", Util.null2String(rs1.getString("PAPER_DREW_DATE")));
//					//"AMOUNT_WITH_TAX":"发票含税金额",
//					jsontemp.put("AMOUNT_WITH_TAX", Util.null2String(rs1.getString("AMOUNT_WITH_TAX")));
//					//"AMOUNT_NO_TAX":"发票不含税金额"
//					jsontemp.put("AMOUNT_NO_TAX", Util.null2String(rs1.getString("AMOUNT_NO_TAX")));
//					jsonarr1.add(jsontemp);
//				}
//				json1.put("DETAIL2", jsonarr1);
//				//表体2（发票信息）end
//
//				//表体3（付款申请单审批记录信息）begin
//				String dt3tablename =maintablename+"_dt3";
//				String sqldt3="select * from "+dt3tablename+" where mainid ="+mainid;
//				rs1.execute(sqldt3);
//				jsonarr1 = new JSONArray();
//				while(rs1.next()) {
//					JSONObject jsontemp =  new JSONObject();
//					//"STEP_NO":"审批次序",
//					jsontemp.put("STEP_NO", Util.null2String(rs1.getString("STEP_NO")));
//					//"APPROVER":"审批人",
//					jsontemp.put("APPROVER", Util.null2String(rs1.getString("APPROVER")));
//					//"APPROVER_COMPANY":"审批人所属公司",
//					jsontemp.put("APPROVER_COMPANY", Util.null2String(rs1.getString("APPROVER_COMPANY")));
//					//"PROCESS":"审批环节",
//					jsontemp.put("PROCESS", Util.null2String(rs1.getString("PROCESS")));
//					//"APPROVAL_TIME":"审批时间",
//					jsontemp.put("APPROVAL_TIME", Util.null2String(rs1.getString("APPROVAL_TIME")));
//					//"APPROVAL_RESULT":"审批结果",
//					jsontemp.put("APPROVAL_RESULT", Util.null2String(rs1.getString("APPROVAL_RESULT")));
//					//"APPROVAL_OPINION":"审批意见"
//					jsontemp.put("APPROVAL_OPINION", Util.null2String(rs1.getString("APPROVAL_OPINION")));
//					jsonarr1.add(jsontemp);
//				}
//				json1.put("DETAIL3", jsonarr1);
//				//表体3（付款申请单审批记录信息）end
//
//				//表体4（付款申请单税务信息） begin
//				String dt4tablename =maintablename+"_dt4";
//				String sqldt4="select * from "+dt4tablename+" where mainid ="+mainid;
//				rs1.execute(sqldt4);
//				jsonarr1 = new JSONArray();
//				while(rs1.next()) {
//					JSONObject jsontemp =  new JSONObject();
//					//"COMMODITY_NAME":"商品明细",
//					jsontemp.put("COMMODITY_NAME", Util.null2String(rs1.getString("COMMODITY_NAME")));
//					//"ITEM_NAME":"货品或服务名称",
//					jsontemp.put("ITEM_NAME", Util.null2String(rs1.getString("ITEM_NAME")));
//					//"PAYMENT_AMOUNT_WITH_TAX":"计划付款金额（含税）",
//					jsontemp.put("PAYMENT_AMOUNT_WITH_TAX", Util.null2String(rs1.getString("PAYMENT_AMOUNT_WITH_TAX")));
//					//"TAX_RATE":"税率",
//					jsontemp.put("TAX_RATE", Util.null2String(rs1.getString("TAX_RATE")));
//					//"PAYMENT_AMOUNT_WITHOUT_TAX":"计划付款金额（不含税）",
//					jsontemp.put("PAYMENT_AMOUNT_WITHOUT_TAX", Util.null2String(rs1.getString("PAYMENT_AMOUNT_WITHOUT_TAX")));
//					//"TAX_AMOUNT":"计划税额"
//					jsontemp.put("TAX_AMOUNT", Util.null2String(rs1.getString("TAX_AMOUNT")));
//					jsonarr1.add(jsontemp);
//				}
//				json1.put("DETAIL4", jsonarr1);
//				//表体4（付款申请单税务信息） end
//
//				//表体5（付款申请单扣款信息） begin
//				String dt5tablename =maintablename+"_dt5";
//				String sqldt5="select * from "+dt5tablename+" where mainid ="+mainid;
//				rs1.execute(sqldt5);
//				jsonarr1 = new JSONArray();
//				while(rs1.next()) {
//					JSONObject jsontemp =  new JSONObject();
//					//"REASON":"扣款原因",
//					jsontemp.put("REASON", Util.null2String(rs1.getString("REASON")));
//					//"AMOUNT":"本次扣款金额"
//					jsontemp.put("AMOUNT", Util.null2String(rs1.getString("AMOUNT")));
//
//					jsonarr1.add(jsontemp);
//				}
//				json1.put("DETAIL5", jsonarr1);
//				//表体5（付款申请单扣款信息） end
//
//				//表体6（应付单税务信息）begin
//				String dt6tablename =maintablename+"_dt6";
//				String sqldt6="select * from "+dt6tablename+" where mainid ="+mainid;
//				rs1.execute(sqldt6);
//				jsonarr1 = new JSONArray();
//				while(rs1.next()) {
//					JSONObject jsontemp =  new JSONObject();
//					//"PAYMENT_AMOUNT_WITH_TAX":"应付单付款金额（含税）",
//					jsontemp.put("PAYMENT_AMOUNT_WITH_TAX", Util.null2String(rs1.getString("PAYMENT_AMOUNT_WITH_TAX")));
//					//"TAX_RATE":"应付单税率",
//					jsontemp.put("TAX_RATE", Util.null2String(rs1.getString("TAX_RATE")));
//					//"PAYMENT_AMOUNT_WITHOUT_TAX":"应付单付款金额（不含税）",
//					jsontemp.put("PAYMENT_AMOUNT_WITHOUT_TAX", Util.null2String(rs1.getString("PAYMENT_AMOUNT_WITHOUT_TAX")));
//					//"TAX_AMOUNT":"应付单税额"
//					jsontemp.put("TAX_AMOUNT", Util.null2String(rs1.getString("TAX_AMOUNT")));
//					jsonarr1.add(jsontemp);
//				}
//				json1.put("DETAIL5", jsonarr1);
//				//表体6（应付单税务信息）end
//
//				//表体7（合同所属项目信息）begin
//				String dt7tablename =maintablename+"_dt7";
//				String sqldt7="select * from "+dt7tablename+" where mainid ="+mainid;
//				rs1.execute(sqldt7);
//				jsonarr1 = new JSONArray();
//				while(rs1.next()) {
//					JSONObject jsontemp =  new JSONObject();
//					//"PROJECT_CODE":"项目编号",
//					jsontemp.put("PROJECT_CODE", Util.null2String(rs1.getString("PROJECT_CODE")));
//					//"PROJECT_NAME":"项目名称"
//					jsontemp.put("PROJECT_NAME", Util.null2String(rs1.getString("PROJECT_NAME")));
//					jsonarr1.add(jsontemp);
//				}
//				json1.put("DETAIL7", jsonarr1);
//				//表体7（合同所属项目信息）end
//
//				//表体8（附件）begin
//				String dt8tablename =maintablename+"_dt8";
//				String sqldt8="select * from "+dt8tablename+" where mainid ="+mainid;
//				rs1.execute(sqldt8);
//				jsonarr1 = new JSONArray();
//				while(rs1.next()) {
//					JSONObject jsontemp =  new JSONObject();
//					//"ANNEX_ID":"附件唯一id",
//					jsontemp.put("ANNEX_ID", Util.null2String(rs1.getString("ANNEX_ID")));
//					//"ANNEX_NAME":"附件名",
//					jsontemp.put("ANNEX_NAME", Util.null2String(rs1.getString("ANNEX_NAME")));
//					//"ROUTE":"路径"
//					jsontemp.put("ROUTE", Util.null2String(rs1.getString("ROUTE")));
//					jsonarr1.add(jsontemp);
//				}
//				json1.put("DETAIL8", jsonarr1);
//				//表体8（附件）end
//
//				jsonarr0.add(json1);
//				json0.put("DATAS", jsonarr0);
//
//				baseBean.writeLog("参数："+json0.toString());
//				baseBean.writeLog("校验msg："+msg);
//
//				if("".equals(msg)) {
//					//调接口
//					setData(json0.toString(),maintablename,mainid,rqxh);
//				}else {
//					//更新数据
//					String sqlupdate = "update "+maintablename+" set RET_CODE=?,RET_MSG=? where id="+mainid ;
//					baseBean.writeLog("失败："+sqlupdate);
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
//	 * 调用付款接口
//	 * @return
//	 */
//	public void setData(String temp,String maintablename,String mainid,int rqxh) {
//		Object lock = new Object();
//		String msg ="";
//		RecordSet rs2 = new RecordSet();
//		//调用接口开始
//		BaseBean baseBean = new BaseBean();
//
//		String  address =Util.null2String(baseBean.getPropValue("bljt20200318", "address_test"));
//
//		baseBean.writeLog("开始调接口");
//		TxServiceGatewayProxy txServiceGatewayProxy = new TxServiceGatewayProxy();
//		txServiceGatewayProxy.setEndpoint(address);
//		try {
//			synchronized (lock) {
//				baseBean.writeLog("接口地址==>"+address);
//				msg = txServiceGatewayProxy.send(temp);
//				baseBean.writeLog("接口返回msg："+msg);
//				//System.out.println(msg);
//			}
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			msg="{\"HEAD\":{\"EsbCode\":\"-1\",\"EsbDesc\":\"调用接口报错："+e.getMessage()+"\"},\"BODY\":{}";
//			baseBean.writeLog("接口异常msg："+msg);
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
//				//更新数据
//				String sqlupdate = " update "+maintablename+" set RET_CODE=?,RET_MSG=? where id="+mainid ;
//				baseBean.writeLog("成功："+sqlupdate);
//				String [] params = new String[2];
//				params[0]= tempjson.getString("RET_CODE");
//				params[1]= tempjson.getString("RET_MSG");
//				rs2.executeUpdate(sqlupdate, params);
//			}
//
//		}else if("-1".equals(EsbCode)) {
//			//更新数据
//			String sqlupdate = "update "+maintablename+" set RET_CODE=?,RET_MSG=? where id="+mainid ;
//			baseBean.writeLog("失败："+sqlupdate);
//			String [] params = new String[2];
//			params[0]= "0";
//			params[1]= EsbDesc;
//			rs2.executeUpdate(sqlupdate, params);
//
//		}else {
//			//更新数据
//			String sqlupdate = "update "+maintablename+" set RET_CODE=?,RET_MSG=? where id="+mainid ;
//			baseBean.writeLog("失败："+sqlupdate);
//			String [] params = new String[2];
//			params[0]= "0";
//			params[1]= EsbDesc;
//			rs2.executeUpdate(sqlupdate, params);
//
//		}
//
//		setlog("PAYINTER", rqxh, temp, msg);
//		//调用接口结束
//		baseBean.writeLog("调接口结束");
//	}
//
//	public void setlog(String rqfn,int rqxh,String cs,String rt) {
//		RecordSet rs1 = new RecordSet();
//		RecordSet rs2 = new RecordSet();
//		RecordSet rs3 = new RecordSet();
//		//调用接口开始
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
//			// 处理表单建摸数据的默认权
//			ModeRightInfo ModeRightInfo = new ModeRightInfo();
//			ModeRightInfo.setNewRight(true);
//			ModeRightInfo.editModeDataShare(1, log_modeid, id_temp);
//		}
//	}
//}
