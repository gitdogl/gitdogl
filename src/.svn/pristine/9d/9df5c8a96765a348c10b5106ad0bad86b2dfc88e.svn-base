package weaver.formmode.interfaces.action;

import com.api.cube.bean.WorkflowToModeLogInfo;
import com.api.cube.util.CubeCipherUitl;
import com.engine.common.biz.EncryptConfigBiz;
import com.engine.cube.biz.BrowserHelper;
import com.engine.cube.util.AddSeclevelUtil;
import com.engine.cube.util.SaveConditionUtil;
import com.engine.cube.biz.CodeBuilder;
import com.engine.cube.util.WorkFlowToModeStatusUtil;
import com.weaver.formmodel.util.StringHelper;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.EncodingUtils;
import weaver.conn.RecordSet;
import weaver.formmode.browser.FormModeBrowserUtil;
import weaver.formmode.customjavacode.CustomJavaCodeRun;
import weaver.formmode.data.FieldInfo;
import weaver.formmode.data.ModeDataIdUpdate;
import weaver.formmode.interfaces.InterfacesUtil;
import weaver.formmode.log.FormmodeLog;
import weaver.formmode.service.FormInfoService;
import weaver.formmode.service.WorkFlowToModeLogService;
import weaver.formmode.setup.ModeSetUtil;
import weaver.formmode.tree.CustomTreeData;
import weaver.formmode.util.WorkflowToModeUtil;
import weaver.formmode.view.ModeViewLog;
import weaver.formmode.virtualform.VirtualFormHandler;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.interfaces.datasource.DataSource;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.*;
import weaver.systeminfo.SystemEnv;
import weaver.workflow.form.FormManager;
import weaver.workflow.request.RequestManager;
import weaver.workflow.webservices.*;
import weaver.workflow.workflow.BillComInfo;
import weaver.workflow.workflow.WorkflowBillComInfo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Types;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 流程数据转为卡片数据
 * @author cwj
 *
 */
public class WorkflowToMode extends FormmodeLog implements Action {
	private static final ExecutorService actionThreadPool = Executors.newFixedThreadPool(4);
	private Log log = LogFactory.getLog(WorkflowToMode.class.getName());
	private DataSource ds;
	private int logsources=0;
	private String ip="";
	private String execStatus="1"; //成功
	private String executeSuccess="{389417}"; //执行成功！
	private String execError="{389418}"; //执行失败！
	private String execSeparator="-----------------------------------------------"; //分隔符
	public String getExecStatus() {
		return execStatus;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setLogsources(int logsources) {
		this.logsources = logsources;
	}

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}
	private String add_update_id = "";
	private List<Integer> patchadd_id = new ArrayList<Integer>();

	private int wf_formid = 0;
	private int requestid = 0;
	private int wf_creater = 0;
	private String wf_creatDate = "";
	private String wf_nodeName = "";
	private String wf_nodeLinkName = "";
	private Integer wf_createrDepartment = 0;
	private int wf_isbill = 0;
	private User wf_user = null;
	private String wf_ip = "";

	private int m_formid = 0;
	private int m_isbill = 1;

	private HashMap mf_wf_map = null;//模块字段_流程字段
	private static ModeDataIdUpdate ModeDataIdUpdate = new ModeDataIdUpdate();
	private boolean mtotIsUpdate = false;//主表操作方式是否为更新
	private String maintableopttype = "1"; //执行方式，1-插入，2-更新，3-批量插入，4-插入并更新
	private String maintableupdatecondition = "";//主表更新条件
	private Map<String, String> maintableupdateconditionMap = new HashMap<String, String>();//主表更新条件,字段对应的值
	//private List<String> updateConditionFieldList = new ArrayList<String>();
	//private List<String> updateConditionValueList = new ArrayList<String>();
	private String updateConditionFieldEmptyMessage = "";
	private String maintablewherecondition = "";//主表比对条件
	private Map<String,Object> mucDetailTableMap = null;//触发表单为明细表时，所包含的流程明细表字段信息
	private List<String> wf_mtuc_list = null;//主表更新条件中的流程字段
	private Map<String,Object> detailtableoptMap = null;//子表操作设置信息
	private String statusflag = "";
	private String messagecontent = "";
	private VerifyData4Wf2Mode verifyData=null;
	private ArrayList<HashMap<String,Object>> rightArray = new ArrayList<HashMap<String,Object>>();
	private int curSetid = 0;
	private int nodeid;
	private int nodelinkid;
	private String workflowtomodename = "";
	private boolean verifyResult=false;
	private boolean conditionFieldHasEmpty=false;
	private boolean bathAddStop=false;
	private String bathAddMessage = "";

	private Map<String,Integer> createBillMap = new HashMap<String,Integer>();//创建模块数据时，记录创建人
	private String isinsert = "0"; //更新不成功是否插入   0不插入 1 插入
	private HashMap mainmsg_Map = new HashMap();
	private HashMap mainSuccessmsg_Map = new HashMap();
	private HashMap mainbillid_Map = new HashMap();
	private Map<String, String> wfiMainfieldmap = new HashMap<String, String>();//流程转数据关系到的流程主字段
	private List<String> wfiDetailfields = new ArrayList<String>();//流程转数据关系到的流程明细表名
	private List<String> updateModebyDetail_errorlog = null;//转数据根据明细表更新存储错误日志信息
	private List<String> updateModebyDetail_SuccessfulLog = null;//转数据根据明细表更新存储成功日志信息
	private List<String> billidList = null;//明细表触发，保存billid
	private List<String> elseSqlList = null;//赋值表达式else对应的sql
	private Map<String,Boolean> field_assign_exp = null;//存储 cube_field_assign_exp 表条件是否满足
	private Map<String,String> field_newValue = null;//字段赋值 field - 解析后的新值
	private Map<String,Map<String,Object>> fieldMap =null; // 存储 满足当前条件(cube_field_assign_exp)的 字段的赋值信息(cube_fieldexpinfo)
	private int  wtlid = 0; //转数据日志id
	private String formtype = "";
	private int resetdataid = 0;
	private String tablename = "";
	private List<String> conditionLog = null;//转数据条件表达式日志
	public int getNodeid() {
		return nodeid;
	}

	public void setNodeid(int nodeid) {
		this.nodeid = nodeid;
	}

	public int getNodelinkid() {
		return nodelinkid;
	}

	public void setNodelinkid(int nodelinkid) {
		this.nodelinkid = nodelinkid;
	}

	private int actionid = 0;
	private Map defaultValueMap = null;
	/**
	 * 清除map的缓存
	 *
	 */
	public void initParameter(){
		mf_wf_map = new HashMap();
		mtotIsUpdate = false;
		maintableopttype = "1";
		maintableupdatecondition = "";
		maintablewherecondition = "";
		mucDetailTableMap = new HashMap<String,Object>();
		wf_mtuc_list = new ArrayList<String>();
		detailtableoptMap = new HashMap<String,Object>();
		statusflag = Action.SUCCESS;
		messagecontent = "";
		defaultValueMap = new HashMap<String, Object>();
		this.workflowtomodename = "";
		mainmsg_Map.clear();
		mainbillid_Map.clear();
		mainSuccessmsg_Map.clear();
		updateModebyDetail_errorlog = new ArrayList<String>();
		updateModebyDetail_SuccessfulLog =  new ArrayList<String>();
		billidList = new ArrayList<String>();
		elseSqlList = new ArrayList<String>();
		field_assign_exp=new HashMap<String,Boolean>();//存储
		field_newValue = new HashMap<String,String>();
		fieldMap =new HashMap<String,Map<String,Object>>();
		conditionLog =new ArrayList<String>();
	}

	/**
	 * 执行action操作
	 * @param request
	 * @return String
	 */
	public String execute (RequestInfo request){
		writeLog("进入流程转数据execute:"+request.getRequestid());
		verifyData = new VerifyData4Wf2Mode();
		verifyData.setDmltype(1);//都按照更新处理
		try {
			statusflag = executeTrans(request);
			writeLog("进入流程转数据executeTrans:"+request.getRequestid()+",statusflag:"+statusflag);
			//boolean result = verifyData.verifyData();
			//writeLog("进入流程转数据verifyData:"+request.getRequestid()+",result:"+result);
//			if(conditionFieldHasEmpty){
//				statusflag = Action.FAILURE_AND_CONTINUE;
//				request.getRequestManager().setMessagecontent(updateConditionFieldEmptyMessage);
//			}
			if(verifyResult) {
				statusflag = Action.FAILURE_AND_CONTINUE;
				request.getRequestManager().setMessagecontent(verifyData.getMessages());
			}
			if(bathAddStop){
				statusflag = Action.FAILURE_AND_CONTINUE;
				request.getRequestManager().setMessagecontent(bathAddMessage);
			}
		} catch (Exception e) {
			writeLog("流程转数据异常进入catch,reuqestid:"+request.getRequestid());
			this.writeLog(e);
			statusflag = Action.FAILURE_AND_CONTINUE;
		}finally{
			writeLog("流程转数据进入finally,reuqestid1:"+request.getRequestid());
			if(Action.SUCCESS.equals(statusflag)) {
				writeLog("流程转数据进入finally,reuqestid2:"+request.getRequestid());
				doAfterCommit();
			}
		}
		writeLog("流程转数据return  statusflag:"+statusflag+",requestid:"+request.getRequestid());
		return statusflag;
	}

	public ArrayList<HashMap<String, Object>> getRightArray() {
		return rightArray;
	}

	public void setRightArray(ArrayList<HashMap<String, Object>> rightArray) {
		this.rightArray = rightArray;
	}

	public void doAfterCommit(){
		WorkflowToModeAfter workflowToModeAfter = new WorkflowToModeAfter(wf_user,this);
		workflowToModeAfter.setAction("doAfter");
		workflowToModeAfter.setIp(wf_ip);
		actionThreadPool.execute(workflowToModeAfter);
	}

	public int getActionid() {
		return actionid;
	}

	public void setActionid(int actionid) {
		this.actionid=actionid;
	}

	/**
	 * 执行action操作
	 * @param request
	 * @return String
	 */
	private String executeTrans(RequestInfo request) throws Exception{
		//能进入到这里,说明流程那边的流程转数据肯定开启了,同步建模流程转数据开关
		WorkFlowToModeStatusUtil.ansyStatusByActionid(actionid+"", "1");
		String seclevelWorkflow=request.getSecLevel();
		String secValidityWorkflow = request.getSecValidity();

		RecordSet rs3 = new RecordSet();
		String baseStr = "WorkflowToMode===requestid==="+request.getRequestid();
		RequestManager RequestManager = request.getRequestManager();
		int currentnodeid = RequestManager.getNodeid();//当前节点id
		int nextnodeid = RequestManager.getNextNodeid();//下一个节点id
		int workflowid = RequestManager.getWorkflowid();//流程类型
		int workflowExport = 0; //流程当前出口
		int wf_modeid = 0;
		wf_formid = RequestManager.getFormid();//流程表单id
		requestid = RequestManager.getRequestid();//流程id
		wf_creater = RequestManager.getCreater();//流程创建人
		wf_isbill = RequestManager.getIsbill();//是否为单据
		wf_user = RequestManager.getUser();
		wf_ip = "".equals(this.ip)?RequestManager.getIp():this.ip;//IP地址
		detailFieldValuesMaps = new HashMap();//所有明细表的值
		//接口创建流程 RequestManager信息不对
		if(wf_user == null || wf_formid == 0 || currentnodeid == 0){
			wf_user = new User(wf_creater);
			RecordSet rs = new RecordSet();
			rs.executeSql("select * from workflow_base a left join workflowactionset b on a.id = b.workflowid where  b.interfaceid = 'WorkflowToMode' and a.id ="+workflowid);
			if(rs.next()){
				wf_formid = rs.getInt("formid");
				wf_isbill = rs.getInt("isbill");
				this.nodeid = rs.getInt("nodeid");
				this.nodelinkid = rs.getInt("nodelinkid");
				currentnodeid = this.nodeid;
				nextnodeid = this.nodelinkid;
			}
		}

		verifyData.setUser(wf_user);
		int triggerNodeId = this.nodeid;
		int triggerType = 0;//节点后
		RecordSet rs = new RecordSet();
		RecordSet rs1 = new RecordSet();
		RecordSet rsdetail = new RecordSet();
		RecordSet rs2 = new RecordSet();
		RecordSet rs4 = new RecordSet();
		ModeSetUtil ms = new ModeSetUtil();
		String sql = "";
		boolean isnodeafter = true;//代表节点后调用该action
		String ispreadd = request.getIspreadd();
		if(ispreadd.equals("1")){//节点前调用
			isnodeafter = false;
			triggerType = 1;
			//triggerNodeId = nextnodeid;
		}

		//封装流程创建人部门，创建时间信息
		sql = "select * from workflow_requestbase where requestid = ?";
		rs3.executeQuery(sql,request.getRequestid());
		if(rs3.next()){
			wf_creatDate = Util.null2String(rs3.getString("createdate"));
		}
		sql = "select *from hrmresource where id = ?";
		rs3.executeQuery(sql,wf_creater);
		if(rs3.next()){
			wf_createrDepartment = Util.getIntValue(rs3.getString("departmentid"));
		}
		//流程创建人为系统管理员时，部门要置为null，否则展示的是/
		if(wf_createrDepartment==0) wf_createrDepartment=null;

		//封装流程当前节点名称，出口名称
		RecordSet recordSet = new RecordSet();
		if(this.nodeid!=0){
			String sql1 = "select nodename from workflow_nodebase where id=?";
			recordSet.executeQuery(sql1,this.nodeid);
			while (recordSet.next()){
				wf_nodeName = Util.null2String(recordSet.getString("nodename"));//当前触发转数据的节点名称
			}
		}
		int nodeLinkId = this.nodelinkid;
		if(nodeLinkId!=0 && !"".equals(nodeLinkId)){
			String sql2 = "select linkname from workflow_nodelink where id=?";
			recordSet.executeQuery(sql2,nodeLinkId);
			while (recordSet.next()){
				wf_nodeLinkName = Util.null2String(recordSet.getString("linkname"));
			}
		}

		workflowExport = this.nodelinkid;

		sql = "select * from workflow_bill  where id = "+wf_formid;
		rs3.executeSql(sql);
		if(rs3.next()){
			tablename = Util.null2String(rs3.getString("tablename")).toLowerCase();
		}

		writeLog(baseStr+",isnodeafter:"+isnodeafter+"	currentnodeid:"+currentnodeid+"		nextnodeid:"+nextnodeid);

		//重新组装流程的值
		getWorkflowDataValue(request);
		//获取流程相关字段
		getWfFieldInfo(wf_isbill,wf_formid);

//		if(this.nodeid > 0){
//			sql = "select id,workflowtomodename,modeid,workflowid,modecreater,modecreaterfieldid,workflowExport,triggerNodeId,triggerType,formtype,maintableopttype,maintableupdatecondition,maintablewherecondition,basedfield,conditiontype,conditionsql,conditiontext,resetdataid from mode_workflowtomodeset " +
//					"where isenable='1' and triggerNodeId = " + triggerNodeId + " and triggerType = " + triggerType + " and workflowid = " + workflowid;
//		}else{
//			sql = "select id,workflowtomodename,modeid,workflowid,modecreater,modecreaterfieldid,workflowExport,triggerNodeId,triggerType,formtype,maintableopttype,maintableupdatecondition,maintablewherecondition,basedfield,conditiontype,conditionsql,conditiontext,resetdataid from mode_workflowtomodeset " +
//								"where isenable='1' and  workflowExport="+workflowExport+" and workflowid = " + workflowid;
//		}

		//写入转数据日志
		WorkFlowToModeLogService workFlowToModeLogService = new WorkFlowToModeLogService();
		WorkflowToModeLogInfo workflowToModeLogInfo = new WorkflowToModeLogInfo();
		String tableName="workflowtomodelog";
		int wtlid = ModeDataIdUpdate.getWorkFlowTomodeNewIdByUUID(tableName, null);
		int workflowtomodesetid = 0;
		this.wtlid = wtlid;
		RecordSet rs5=new RecordSet();
		rs.executeQuery("Select id,modeid from mode_workflowtomodeset where actionid= ?", this.actionid);
		if (rs.next()) {
			workflowtomodesetid = rs.getInt(1);
			wf_modeid = rs.getInt(2);
		}
		Calendar today = Calendar.getInstance();
		workflowToModeLogInfo.setId(wtlid);
		workflowToModeLogInfo.setWorkflowtomodesetid(workflowtomodesetid);
		workflowToModeLogInfo.setModeid(wf_modeid);
		workflowToModeLogInfo.setMainid(0);
		workflowToModeLogInfo.setWorkflowid(RequestManager.getWorkflowid());
		workflowToModeLogInfo.setRequestid(RequestManager.getRequestid());
		workflowToModeLogInfo.setRequestName(RequestManager.getRequestname());
		workflowToModeLogInfo.setActionid(this.actionid);
		workflowToModeLogInfo.setTriggertype(triggerType);//触发类型
		workflowToModeLogInfo.setWorkflowexport(workflowExport);//出口
		workflowToModeLogInfo.setTriggerNodeId(triggerNodeId);
		if(workflowExport!=0){
			workflowToModeLogInfo.setTriggerMethod(2);//出口触发
		}else{
			workflowToModeLogInfo.setTriggerMethod(1);//节点触发
		}
		workflowToModeLogInfo.setCirculationdate(Util.add0(today.get(Calendar.YEAR), 4) + "-" +
				Util.add0(today.get(Calendar.MONTH) + 1, 2) + "-" +
				Util.add0(today.get(Calendar.DAY_OF_MONTH), 2));
		workflowToModeLogInfo.setCirculationtime(Util.add0(today.get(Calendar.HOUR_OF_DAY), 2) + ":" +
				Util.add0(today.get(Calendar.MINUTE), 2) + ":" +
				Util.add0(today.get(Calendar.SECOND), 2));
		if(wf_ip.equals("0")){
			wf_ip="127.0.0.1";
		}
		workflowToModeLogInfo.setLogsources(this.logsources);// logsources 日志来源，0：流程流转  1：历史数据写入
		workflowToModeLogInfo.setIp(wf_ip);
		workflowToModeLogInfo.setOperator(wf_user.getUID());//当前操作人
		workflowToModeLogInfo.setLogtype(0);//触发,刚进入给触发状态
		workFlowToModeLogService.updatelog(workflowToModeLogInfo);

		sql = "select id,mainid,workflowtomodename,modeid,workflowid,modecreater,modecreaterfieldid,workflowExport,triggerNodeId,triggerType,formtype,maintableopttype,maintableupdatecondition,maintablewherecondition,basedfield,conditiontype,conditionsql,conditiontext,resetdataid,isinsert from mode_workflowtomodeset where isenable='1' and  actionid = " + this.actionid;
		boolean f = rs.executeSql(sql);
		if(!f){
			//	writeLog(baseStr+"===sql错误===>"+sql);
			execStatus="2";
			workFlowToModeLogService.updateSuccesslogByWtlid(wtlid,execStatus,execError+"<br/>"+execSeparator+"<br/>{389382}<br/>"+execSeparator+"<br/>{389389}:<br/><span style=\"color:red; word-wrap: break-word;\">"+sql+"</span>","");
		}
		int index = 0;
		if(rs.getCounts()==0){
			//	rs4.writeLog("按照条件未找到相关数据接口，sql"+sql);
			execStatus="2";
			workFlowToModeLogService.updateSuccesslogByWtlid(wtlid,execStatus,execError+"<br/>"+execSeparator+"<br/>{389390}<br/>"+execSeparator+"<br/>{389389}:<br/><span style=\"color:red; word-wrap: break-word;\">"+sql+"</span>","");
			return Action.SUCCESS;
		}
		while(rs.next()){
			index++;
			int m_billid = 0;
			List<String> mainbillids = new ArrayList<String>();
			resetdataid = rs.getInt("resetdataid");
			initParameter();
			int mainidtemp = rs.getInt("mainid");
			int id = rs.getInt("id");
			if(mainidtemp > 0){
				id = mainidtemp;//取主数据的id
				mainidtemp = rs.getInt("id");//保存当前数据的id
			}
			this.curSetid = id;
			int modeid = rs.getInt("modeid");

			rs4.executeSql("select isenable from mode_workflowtomodeset where modeid in (select id from modeinfo where isdelete = 1 and id ="+modeid+")");
			boolean flag1 = true;
			while(rs4.next()){
				int isenable = rs4.getInt("isenable");
				if(isenable==1){
					//調用關閉接口
					ms.updateEnable(modeid);
					if(index==rs.getCounts()){
						return Action.SUCCESS;
					}else{
						flag1 = false;
						break;
					}
				}
			}
			if(!flag1){
				continue;
			}


			int modecreater = rs.getInt("modecreater");
			int modecreaterfieldid = rs.getInt("modecreaterfieldid");
			String formtype = Util.null2String(rs.getString("formtype"));
			this.formtype = formtype;
			String conditionsql = Util.null2String(rs.getString("conditionsql"));
			boolean isdetailform = false;//触发表单是否为子表
			this.workflowtomodename = Util.null2String(Util.formatMultiLang(rs.getString("workflowtomodename"), ""+wf_user.getLanguage()));
			if(formtype.indexOf("detail")>-1){
				isdetailform = true;
			}

			if(!StringHelper.isEmpty(conditionsql)){
				//触发表单为主表，先检测触发条件，为明细时，针对每一条明细进行检测
				Map param = new HashMap();
				param.put("requestid", request.getRequestid());
				param.put("isdetailform", isdetailform+"");
				param.put("formtype", formtype);
				JSONObject json = checkConditionSQL(id, wf_user, param);
				String checkSql = json.getString("sql");
				int count = json.getInt("count");
				if(count<=0){
					String flag = json.getString("flag");
					String msg = "";
					if(flag.equals("0")){
						msg = SystemEnv.getHtmlLabelName(503535,weaver.general.Util.threadVarLanguage())+",";//触发条件存在语法错误
					}
					execStatus="2";
					//	writeLog(baseStr+",流程转数据【"+this.workflowtomodename+"】不满足触发条件而终止,流程转数据设置id为："+id+","+msg+"对应SQL为："+checkSql);
					workFlowToModeLogService.updateSuccesslogByWtlid(wtlid,execStatus,execError+"<br/>"+execSeparator+"<br/>{389391}"+msg+"<br/>"+execSeparator+"<br/>{389389}:<br/><span style=\"color:red; word-wrap: break-word;\">"+checkSql+"</span>","");
					continue;
				}
			}



			isinsert =  Util.null2s(rs.getString("isinsert"), "0");
			maintableopttype = Util.null2s(rs.getString("maintableopttype"), "1");
			verifyData.setMaintableopttype(maintableopttype);
			mtotIsUpdate = "2".equals(maintableopttype);
			maintableupdatecondition = Util.toScreenToEdit(rs.getString("maintableupdatecondition"),wf_user.getLanguage()).trim();
			maintableupdatecondition=Util.toExcelData(maintableupdatecondition);
			maintablewherecondition = Util.null2String(rs.getString("maintablewherecondition")); //模块字段=形式
			//如果"4".equals(maintableopttype)，也就是为插入并更新的时候，根据当前的值去做一个判断来给mtotIsUpdate赋值是插入还是更新。并增加条件给maintableupdatecondition
			if ("4".equals(maintableopttype)) { //插入并更新
				String requestid = Util.null2String(request.getRequestid());
				//判断requestid的对应字段是否加密
				RecordSet rSet = new RecordSet();
				rSet.executeQuery("select a.id,a.billid from workflow_billfield a  left join mode_workflowtomodesetdetail b on a.id=b.modefieldid where b.mainid=? and b.wffieldid = -2", id);
				if(rSet.next()){
					String modefieldid = Util.null2String(rSet.getString("id"));
					String formid = Util.null2String(rSet.getString("billid"));
					rSet.executeQuery("select 1 from ModeFormFieldEncrypt where formid = ? and fieldid=?", formid,modefieldid);
					if(rSet.next()){
						requestid = CubeCipherUitl.encrypt(requestid);
					}
				}
				maintablewherecondition = maintablewherecondition.replace("-2", "'"+requestid+"'");
				maintablewherecondition = maintablewherecondition.replace("~*~", " and ");
				mtotIsUpdate = true;
				maintableupdatecondition = maintablewherecondition;//这个从配置字段
			}
			List<String> mucDetailFieldNameList = new ArrayList<String>();//主表更新条件中流程明细字段
			if(mtotIsUpdate){
				wf_mtuc_list = getFieldNames(maintableupdatecondition);
				String mucDetailTableName = "";//主表更新条件中流程明细表名
				for(int i=0;i<wf_mtuc_list.size();i++){
					String wFieldName = wf_mtuc_list.get(i);

					if(isdetailform){
						if(wFieldName.indexOf(".")>-1){
							String[] wMainFieldArr = wFieldName.split("\\.");
							String mucDetailTableNameTmp = wMainFieldArr[0];
							String mucDetailFieldName = wMainFieldArr[1];
							if(!"".equals(mucDetailTableName)&&!mucDetailTableName.equals(mucDetailTableNameTmp))continue;//如果包含不同的流程子表，则条件设置错误，肯定无法处理
							mucDetailTableName = mucDetailTableNameTmp;
							if(!mucDetailFieldNameList.contains(mucDetailFieldName)){
								mucDetailFieldNameList.add(mucDetailFieldName);
							}
						}else{
							String wFieldValue = Util.null2String(mainFieldValuesMap.get(wFieldName));
							if(!wFieldValue.trim().equals("")){
								maintableupdatecondition = maintableupdatecondition.replaceAll("\\$"+wFieldName+"\\$", wFieldValue.replace("\'","''"));
								maintableupdatecondition =  maintableupdatecondition.trim();
							}
							maintableupdateconditionMap.put(wFieldName, wFieldValue);
						}
					}else{
						String tempFieldname = wFieldName.toLowerCase();
						if(tempFieldname.indexOf(".")>-1){
							String[] arr = tempFieldname.split("\\.");
							String tempTableName = arr[0];
							String tempFieldName = arr[1];
							if(tablename.toLowerCase().equals(tempTableName)){
								tempFieldname = tempFieldName;
							}
						}
						String wFieldValue = Util.null2String(mainFieldValuesMap.get(tempFieldname));
						if(!wFieldValue.trim().equals("")){//如果更新条件为空不替换
							maintableupdatecondition = maintableupdatecondition.replaceAll("\\$"+wFieldName+"\\$", wFieldValue.replace("\'","''"));
							maintableupdatecondition =  maintableupdatecondition.trim();
						}
						maintableupdateconditionMap.put(wFieldName, wFieldValue);
					}
				}
				if(isdetailform){
					mucDetailTableMap.put("wdetailtablename", mucDetailTableName);
					mucDetailTableMap.put("wdetailfieldnamelist", mucDetailFieldNameList);
					if ("2".equals(maintableopttype)) {
						setDetailTableMap(rs1, id);
					}
				}else{
					setDetailTableMap(rs1, id);
				}
			}
			verifyData.setWf_mtuc_list(wf_mtuc_list);
			verifyData.setMaintableupdateconditionMap(maintableupdateconditionMap);
			verifyData.setMaintableupdatecondition(maintableupdatecondition);
			sql = "select mainid,modefieldid,wffieldid,defaultvalue,b.fieldname,b.detailtable from mode_workflowtomodesetdetail m left join workflow_billfield b on m.wffieldid=b.id where mainid = " + id;
			rsdetail.executeSql(sql);
			while(rsdetail.next()){
				int wffieldid = rsdetail.getInt("wffieldid");
				int modefieldid = rsdetail.getInt("modefieldid");
				String defaultValue = Util.null2String(rsdetail.getString("defaultvalue"));
				String fieldname = Util.null2String(rsdetail.getString("fieldname"));
				String detailtable = Util.null2String(rsdetail.getString("detailtable"));
				if(!"".equals(defaultValue)){
					defaultValueMap.put(String.valueOf(modefieldid), defaultValue);
				}
				if (!"".equals(fieldname)) {
					if ("".equals(detailtable)) {
						MainTableInfo minfo = request.getMainTableInfo();
						List pl = getPropertyByName(minfo, fieldname);
						Iterator iter = pl.iterator();
						if (iter.hasNext()){
							Property p = (Property) iter.next();
							wfiMainfieldmap.put(wffieldid+"", p.getValue());
						}
					}else {
						wfiDetailfields.add(detailtable);
					}
				}
				if(wffieldid==0)continue;
				mf_wf_map.put(String.valueOf(modefieldid), String.valueOf(wffieldid));
			}
			wfiMainfieldmap.put("-1", request.getDescription());//流程标题
			wfiMainfieldmap.put("requestname", request.getDescription());//流程标题
			wfiMainfieldmap.put("-2", request.getRequestid());//流程id\
			wfiMainfieldmap.put("requestid", request.getRequestid());//流程id
			wfiMainfieldmap.put("-3", String.valueOf(wf_creater));//流程创建人
			wfiMainfieldmap.put("-200", String.valueOf(wf_creatDate));//流程创建时间
			wfiMainfieldmap.put("-201", String.valueOf(wf_createrDepartment));//流程创建人部门
			wfiMainfieldmap.put("-202", String.valueOf(wf_nodeName));//流程节点名称
			wfiMainfieldmap.put("-203", String.valueOf(wf_nodeLinkName));//流程出口名称
			//获得模块相关信息
			getModeInfo(modeid);
			//获得模块相关字段信息
			getModeFieldInfo(m_formid,m_isbill);
			if(!isdetailform){//主表触发

				Map param = new HashMap();
				param.put("formtype", formtype);
				//循环执行条件表达式
				checkCondition(curSetid,param);
				if(!field_assign_exp.isEmpty()){
					for(String mainid:field_assign_exp.keySet()){
						//this.field_newValue=getFieldExpInfo(id);
						//获取当前满足条件的 字段赋值信息
						fieldMap = getFieldInfo(mainid);
					}
				}
			}
			//获得模块的 WorkflowRequestInfo
			WorkflowRequestInfo wri = getWorkflowRequestInfo(modeid,modecreater,modecreaterfieldid);
			if(conditionLog.size()>0){
				//写入公式日志
				writeConditionAssignmentLog("");
				return  Action.SUCCESS;
			}
			//获得模块重复验证的 WorkflowRequestInfo
			WorkflowRequestInfo wri1 = getWorkflowRequestInfo1(modeid,modecreater,modecreaterfieldid);
//			if(mtotIsUpdate && !"4".equals(maintableopttype)){
//				conditionFieldHasEmpty = updateConditionFieldHasEmpty(maintableupdateconditionMap);
//				if(conditionFieldHasEmpty){//如果更新条件为空，流程可以提交，不执行流程转数据
//					execStatus="2";
//					workFlowToModeLogService.updateSuccesslogByWtlid(wtlid,execStatus,execError+"<br/>"+execSeparator+"<br/>"+updateConditionFieldEmptyMessage,"");//验证重复字段，有数据重复时，日志显示失败的日志
//					//return "-3";
//                    return Action.SUCCESS;
//				}
//			}
			Operate opt=new Operate();
			opt.type = this.workflowtomodename;
			opt.workflowtomodeset = this.curSetid;
			verifyData.setOpt(opt);
			verifyData.addVerify(wri1,m_formid,7,opt);
			//明细表触发更新主表，条件中含有明细字段情况下，需要循环重复验证
			if(isdetailform && mucDetailFieldNameList.size()>0 && mtotIsUpdate){
				String formindex = wf_formid+"";
				formindex = formindex.replace("-", "");
				formindex = formindex.replace("+", "");
				String detailindex = formtype.replace("detail", "");
				String detailtableName = "";
				if("".equals(tablename)){
					detailtableName = "formtable_main_"+formindex+"_dt"+detailindex;
				}else{
					detailtableName = tablename+"_dt"+detailindex;
				}
				ArrayList<HashMap> list = (ArrayList)detailFieldValuesMaps.get(detailtableName.toLowerCase());
				List<Map<String,String>>  detailFieldValues = new ArrayList<>();
				if(list != null) {
					for (HashMap rowMap : list) {
						Map<String, String> detailFieldValue = new HashMap<>();
						//String mainTableTempCondition = maintableupdatecondition;
						String mucDetailTableName = Util.null2String(mucDetailTableMap.get("wdetailtablename"));
						if (!"".equals(mucDetailTableName) && detailtableName.equals(mucDetailTableName)) {
							if (mucDetailFieldNameList != null && mucDetailFieldNameList.size() > 0) {
								for (int i = 0; i < mucDetailFieldNameList.size(); i++) {
									String wDetailFieldName = mucDetailFieldNameList.get(i);
									String wDetailFieldValue = Util.null2String(rowMap.get(wDetailFieldName.toLowerCase()));
									if (!wDetailFieldValue.trim().equals("")) {
										//mainTableTempCondition=mainTableTempCondition.replaceAll("\\$"+mucDetailTableName+"\\."+wDetailFieldName+"\\$", wDetailFieldValue.replace("\'","''"));
										detailFieldValue.put("\\$" + mucDetailTableName + "\\." + wDetailFieldName + "\\$", wDetailFieldValue.replace("\'", "''"));
									}
								}
							}
						}
						detailFieldValues.add(detailFieldValue);
					/*verifyData.setMaintableupdatecondition(mainTableTempCondition);
					verifyResult=verifyData.verifyData();
					if(verifyResult){
						break;
					}*/
					}
				}
				verifyData.setDetailFieldValues(detailFieldValues);
				verifyResult=verifyData.verifyData();
			}else{
				verifyResult=verifyData.verifyData();
			}
			String verifylog = verifyData.getMessages();
			if(verifyResult){
				execStatus="2";
				workFlowToModeLogService.updateSuccesslogByWtlid(wtlid,execStatus,execError+"<br/>"+execSeparator+"<br/>"+verifylog,"");//验证重复字段，有数据重复时，日志显示失败的日志
				return "-3";
			}
			writeLog("wri.getCreatorId():"+wri.getCreatorId());
			//把模块数据转换成RequestInfo
			RequestInfo RequestInfo = toRequestInfo(wri);
			RequestInfo.setSecLevel(seclevelWorkflow);//设置密级
			RequestInfo.setSecValidity(secValidityWorkflow);//密级时间时间

			if(formtype.indexOf("detail")>-1){//明细表的方式
				String formindex = wf_formid+"";
				formindex = formindex.replace("-", "");
				formindex = formindex.replace("+", "");
				String detailindex = formtype.replace("detail", "");
				String detailtableName = "";
				if("".equals(tablename)){
					detailtableName = "formtable_main_"+formindex+"_dt"+detailindex;
				}else{
					detailtableName = tablename+"_dt"+detailindex;
				}
				// 是系统表单 且非老表单
				if(wf_isbill==1&&wf_formid>0){
					rs.executeQuery("  select tablename from  workflow_billdetailtable  where billid = ? and orderid= ?",wf_formid,detailindex.replace("-",""));
					if(rs.next()){
						detailtableName = Util.null2String(rs.getString("tablename"));
					}
				}
				ArrayList<HashMap> list = (ArrayList)detailFieldValuesMaps.get(detailtableName.toLowerCase());
				if(list==null||list.size()<0){
					//流程转数据，明细表触发，但是流程明细不写值的情况
					workFlowToModeLogService.updateSuccesslogByWtlid(wtlid,execStatus ,"{389397}","");
				}
				if(list!=null&&list.size()>0){
					String  errmsg=execError+"<br/>"+execSeparator+"<br/>{389392}<br/>"+execSeparator+"<br/>{389389}:<br/>";
					String  successMsg=executeSuccess+"<br/>"+execSeparator+"<br/>{389393}<br/>"+execSeparator+"<br/>{389394}:<br/>";
					//String mssg="";
					//String emsg="";
					for (HashMap rowMap : list) {
						if(!StringHelper.isEmpty(conditionsql)){
							String checkSql = "";
							int detailid = 0;
							try {
								//触发表单为主表，先检测触发条件，为明细时，针对每一条明细进行检测
								Map param = new HashMap();
								if(rowMap.containsKey("id")){
									detailid = Util.getIntValue(Util.null2String(rowMap.get("id")));
								}
								param.put("requestid", request.getRequestid());
								param.put("isdetailform", isdetailform+"");
								param.put("detailid", detailid);
								param.put("formtype", formtype);
								JSONObject json = checkConditionSQL(id, wf_user, param);
								checkSql = json.getString("sql");
								int count = json.getInt("count");
								if(count<=0){
									execStatus="2";
									String flag = json.getString("flag");
									String msg = "";
									if(flag.equals("0")){
										msg = SystemEnv.getHtmlLabelName(503535,weaver.general.Util.threadVarLanguage())+",";//触发条件存在语法错误
									}
									//mssg="流程转数据【"+this.workflowtomodename+"】不满足触发条件而终止,子表"+detailtableName+"的id为"+detailid+",流程转数据设置id为："+id+","+msg+"<br/>对应SQL为："+checkSql;
									updateModebyDetail_SuccessfulLog.add("<br/>{389395}"+detailtableName+"{389396}"+detailid+"."+msg+"<br/>{389389}:<br/><span style=\"color:red; word-wrap: break-word;\">"+checkSql+"</span>");
									writeLog(baseStr+"流程转数据【"+this.workflowtomodename+"】不满足触发条件而终止,子表"+detailtableName+"的id为"+detailid+",流程转数据设置id为："+id+","+msg+"<br/>对应SQL为："+checkSql);
									continue;
								}
							} catch (Exception e) {
								//emsg = "流程转数据【"+this.workflowtomodename+"】"+emsg+"而终止,子表"+detailtableName+"的id为"+detailid+",流程转数据设置id为："+id+","+emsg+"对应SQL为："+checkSql;
								execStatus="2";
								updateModebyDetail_SuccessfulLog.add("<br/>{389395}"+detailtableName+"{389396}"+detailid+".<br/>{389389}:<br/><span style=\"color:red; word-wrap: break-word;\">"+checkSql+"</span>");
								writeLog("流程转数据【"+this.workflowtomodename+"】"+e.getMessage()+"而终止,子表"+detailtableName+"的id为"+detailid+",流程转数据设置id为："+id+","+e.getMessage()+"对应SQL为："+checkSql);
								continue;
							}
						}
						if(!mtotIsUpdate){
							if("3".equals(maintableopttype)){
								int detailid=0;
								if(rowMap.containsKey("id")){
									detailid = Util.getIntValue(Util.null2String(rowMap.get("id")));
								}
								String basedfield = Util.null2String(rs.getString("basedfield"),"0");
								rs2.executeSql("select fieldname,fieldhtmltype,type,viewtype from workflow_billfield where id = "+basedfield);
								if(rs2.next()) {
									String fieldname = Util.null2String(rs2.getString("fieldname")).toLowerCase();
									String fieldhtmltype = Util.null2String(rs2.getString("fieldhtmltype"));
									String fieldtype = Util.null2String(rs2.getString("type"));
									String viewtype = Util.null2String(rs2.getString("viewtype"));
									String fieldvalue = "";
									if("0".equals(viewtype)) {
										fieldvalue = Util.null2String(mainFieldValuesMap.get(fieldname));
									} else {
										fieldvalue = Util.null2String(rowMap.get(fieldname));
									}
									int batchAdd = 0;
									if("1".equals(fieldhtmltype)&&"2".equals(fieldtype)){
										batchAdd = Util.getIntValue(fieldvalue);
									}else if(FormModeBrowserUtil.isMultiBrowser(fieldhtmltype, fieldtype)){
										//依据字段为多选browser框处理
										String[] fieldvalue_arr = Util.TokenizerString2(fieldvalue, ",");
										batchAdd = fieldvalue_arr.length;
									}
									if(batchAdd>1){//要插入的数据大于1条，判断是否设置重复验证。
										bathAddStop = isSetVerify(modeid);
										if(bathAddStop){
											execStatus="2";
											workFlowToModeLogService.updateSuccesslogByWtlid(wtlid,execStatus,execError+"<br/>"+execSeparator+"<br/>"+bathAddMessage,"");//验证重复字段，有数据重复时，日志显示失败的日志
											return "-3";
										}
									}
									if("".equals(fieldvalue)){
										updateModebyDetail_SuccessfulLog.add("<br/>{84595}"+detailtableName+" {389470}："+detailid+"{389457}");
									}
									Map basedfieldMap = new HashMap<String, Object>();
									basedfieldMap.put("fieldid", basedfield);
									if("1".equals(fieldhtmltype)&&"2".equals(fieldtype)){
										//依据字段为整数处理
										int fieldvalue_int = Util.getIntValue(fieldvalue);
										for(int i=0;i<fieldvalue_int;i++){
											String basedfieldvlaue = "1";
											basedfieldMap.put("fieldvalue", basedfieldvlaue);
											//创建模块
											m_billid = Util.getIntValue(createModebyDetail(RequestInfo,rowMap,basedfieldMap,wtlid),0);
											patchadd_id.add(m_billid);
											writeLog(baseStr+",m_billid:"+m_billid);
										}
									}else if(FormModeBrowserUtil.isMultiBrowser(fieldhtmltype, fieldtype)){
										//依据字段为多选browser框处理
										String[] fieldvalue_arr = Util.TokenizerString2(fieldvalue, ",");
										for(int i=0;i<fieldvalue_arr.length;i++){
											String basedfieldvlaue = fieldvalue_arr[i];
											if("".equals(basedfieldvlaue))continue;
											basedfieldMap.put("fieldvalue", basedfieldvlaue);
											basedfieldMap.put("ismultibrowser", "true");
											//创建模块
											m_billid = Util.getIntValue(createModebyDetail(RequestInfo,rowMap,basedfieldMap,wtlid),0);
											patchadd_id.add(m_billid);
											writeLog(baseStr+",m_billid:"+m_billid);
										}
									}
								}

								try {
									//回写数据id的权限
									if(resetdataid>0&&patchadd_id.size()>0) {// 此处只处理明细表的单选,多选 树形 单选,多选 一条明细可能批量插入多条
										String fieldname = Util.null2String((String) Wf_Field_Name_Map.get(resetdataid + ""));//流程字段名
										String m_fieldvalue = Util.null2String((String) rowMap.get(fieldname.toLowerCase()));
										if ("".equals(m_fieldvalue)) {//流程该字段没写值
											if(!Wf_ManTableFieldIds.contains(resetdataid+"")){//明细字段
												int detailnum =Util.getIntValue(formtype.replace("detail",""))-1;
												if(detailnum>=0){
													String fieldtype = "";
													String fielddbtype = "";
													ArrayList DetailTableFieldIds = (ArrayList)Wf_DetailTableFieldIds.get(detailnum);
													ArrayList DetailFieldDBTypes = (ArrayList)Wf_DetailFieldDBTypes.get(detailnum);
													ArrayList DetailFieldTypes = (ArrayList)Wf_DetailFieldTypes.get(detailnum);
													ArrayList DetailTableFieldNames = (ArrayList)Wf_DetailDBFieldNames.get(detailnum);
													ArrayList DetailFieldHtmlTypes = (ArrayList)Wf_DetailFieldHtmlTypes.get(detailnum);
													for(int i=0;i<DetailTableFieldIds.size();i++){
														String _fieldname = (String)DetailTableFieldNames.get(i);
														if(_fieldname.toLowerCase().equals(fieldname.toLowerCase())){
															fieldtype = (String)DetailFieldTypes.get(i);
															if("256".equals(fieldtype) || "257".equals(fieldtype)){
																String _fielddbtype = (String)DetailFieldDBTypes.get(i);
																rs2.executeSql("select id from mode_customtreedetail where mainid='"+_fielddbtype+"' and sourceid="+modeid);
																if(rs2.next()){
																	fielddbtype = rs2.getString("id");
																}
															}else{
																fielddbtype = (String)DetailFieldDBTypes.get(i);
															}
															break;
														}
													}


													String mainbillid = "";
													for(int i=0;i<patchadd_id.size();i++){
														if("".equals(mainbillid)){
															if("256".equals(fieldtype) || "257".equals(fieldtype)){ //树形
																mainbillid = fielddbtype+"_"+patchadd_id.get(i);
															}else{
																//浏览框主键值
																String keyValue = getKeyFieldValue(fielddbtype,patchadd_id.get(i)+"");
																mainbillid = keyValue+"";
															}
														}else{
															if("256".equals(fieldtype) || "257".equals(fieldtype)){ //树形
																mainbillid += ","+fielddbtype+"_"+patchadd_id.get(i);
															}else{
																//浏览框主键值
																String keyValue = getKeyFieldValue(fielddbtype,patchadd_id.get(i)+"");
																mainbillid += ","+keyValue;
															}
														}
													}
													if(!"".equals(mainbillid)){
														int detailId = Util.getIntValue((String)rowMap.get("id"));
														//1.回写数据
														String _sql = "update "+tablename+"_dt"+(detailnum+1)+" set "+fieldname+"='"+mainbillid+"' where id='"+detailId+"'";
														rs3.executeSql(_sql);
													}
												}
											}
										}
									}
								} catch (Exception e) {
									writeLog(e);
								}
							} else {
								//创建模块
								m_billid = Util.getIntValue(createModebyDetail(RequestInfo,rowMap,wtlid),0);
							}
						}else{
							updateModebyDetail(RequestInfo, rowMap,wtlid);
						}
					}

					//	if(!"4".equals(maintableopttype)){}// 插入，更新，批量插入
					// 批量插入
					if ("3".equals(maintableopttype)) {
						errmsg=execError+"<br/>"+execSeparator+"<br/>{389471}<br/>"+execSeparator+"<br/>{25700}:<br/>";
						successMsg=executeSuccess+"<br/>"+execSeparator+"<br/>{389472}<br/>"+execSeparator+"<br/>{389473}:<br/>";
					}else if("4".equals(maintableopttype)){//插入并更新
						errmsg = execError + "<br/>" + execSeparator + "<br/>{389477}<br/>" + execSeparator
								+ "<br/>{389389}:<br/>";
						successMsg = executeSuccess + "<br/>" + execSeparator + "<br/>{389478}<br/>"
								+ execSeparator + "<br/>{389394}:<br/>";
					}else if("2".equals(maintableopttype)&&"1".equals(isinsert)){//更新的时候， 勾选了更新不到执行插入。
						errmsg = execError + "<br/>" + execSeparator + "<br/>{510188}<br/>" + execSeparator
								+ "<br/>{389389}:<br/>";
						successMsg = executeSuccess + "<br/>" + execSeparator + "<br/>{510187}<br/>"
								+ execSeparator + "<br/>{389394}:<br/>";

					}
					String billids="";
					String msg="";
					for (int i = 0; i < billidList.size(); i++) {
						if(!billidList.get(i).equals("")){
							if (billids.equals("")) {
								billids = billidList.get(i);
							} else {
								billids += "," + billidList.get(i);
							}
						}
					}
					for (int i = 0; i < updateModebyDetail_SuccessfulLog.size(); i++) {
						msg+=updateModebyDetail_SuccessfulLog.get(i);
					}
					String trueMsg="";
					if(this.execStatus.equals("2")){
						trueMsg=errmsg+msg;
					}else{
						trueMsg=successMsg+msg;
					}
					if(updateModebyDetail_SuccessfulLog.size()>0){
						workFlowToModeLogService.updateSuccesslogByWtlid(wtlid,execStatus ,trueMsg,billids);
					}
				}
			}else{//老方式 适用于  主表和抽象表的情形
				String fieldvalue1="";
				if(!mtotIsUpdate){
					String basedfield = Util.null2String(rs.getString("basedfield"),"0");
					if("3".equals(maintableopttype)){
						if(wf_isbill==0){
							rs2.executeSql("select fieldname,fieldhtmltype,type from workflow_formdict where id = "+basedfield);
						}else{
							rs2.executeSql("select fieldname,fieldhtmltype,type from workflow_billfield where id = "+basedfield);
						}
						if(rs2.next()){
							String fieldname = Util.null2String(rs2.getString("fieldname"));
							String fieldhtmltype = Util.null2String(rs2.getString("fieldhtmltype"));
							String fieldtype = Util.null2String(rs2.getString("type"));
							String fieldvalue = Util.null2String(mainFieldValuesMap.get(fieldname.toLowerCase()));
							fieldvalue1=fieldvalue;
							int batchAdd = 0;
							if("1".equals(fieldhtmltype)&&"2".equals(fieldtype)){
								batchAdd = Util.getIntValue(fieldvalue);
							}else if(FormModeBrowserUtil.isMultiBrowser(fieldhtmltype, fieldtype)){
								//依据字段为多选browser框处理
								String[] fieldvalue_arr = Util.TokenizerString2(fieldvalue, ",");
								batchAdd = fieldvalue_arr.length;
							}
							if(batchAdd>1){//要插入的数据大于1条，判断是否设置重复验证。
								bathAddStop = isSetVerify(modeid);
								if(bathAddStop){
									execStatus="2";
									workFlowToModeLogService.updateSuccesslogByWtlid(wtlid,execStatus,execError+"<br/>"+execSeparator+"<br/>"+bathAddMessage,"");//验证重复字段，有数据重复时，日志显示失败的日志
									return "-3";
								}
							}
							Map basedfieldMap = new HashMap<String, Object>();
							basedfieldMap.put("fieldid", basedfield);
							if("1".equals(fieldhtmltype)&&"2".equals(fieldtype)){
								//依据字段为整数处理
								int fieldvalue_int = Util.getIntValue(fieldvalue);
								for(int i=0;i<fieldvalue_int;i++){
									String basedfieldvlaue = "1";
									basedfieldMap.put("fieldvalue", basedfieldvlaue);
									//创建模块
									m_billid = Util.getIntValue(createMode(RequestInfo,basedfieldMap,wtlid),0);
									patchadd_id.add(m_billid);
									writeLog(baseStr+"批量插入依据字段为整数处理创建数据：m_billid:"+m_billid);
								}
							}else if(FormModeBrowserUtil.isMultiBrowser(fieldhtmltype, fieldtype)){
								//依据字段为多选browser框处理
								String[] fieldvalue_arr = Util.TokenizerString2(fieldvalue, ",");
								for(int i=0;i<fieldvalue_arr.length;i++){
									String basedfieldvlaue = fieldvalue_arr[i];
									if("".equals(basedfieldvlaue))continue;
									basedfieldMap.put("fieldvalue", basedfieldvlaue);
									basedfieldMap.put("ismultibrowser", "true");
									//创建模块
									m_billid = Util.getIntValue(createMode(RequestInfo,basedfieldMap,wtlid),0);
									patchadd_id.add(m_billid);
									writeLog(baseStr+"批量插入依据字段为多选browser框创建数据：m_billid:"+m_billid);
								}
							}
						}
					}else{
						//执行方式，1-插入，2-更新，3-批量插入，4-插入并更新
						//创建模块
						m_billid = Util.getIntValue(createMode(RequestInfo,wtlid),0);
						writeLog(baseStr+",执行方式："+maintableopttype+"(1-插入，2-更新，3-批量插入，4-插入并更新),创建数据m_billid:"+m_billid);
					}
				}else{
					writeLog(baseStr+",主表执行更新");
					mainbillids = updateMode(RequestInfo,wtlid,request);
				}
				//插入
				if (!mtotIsUpdate) {
					String billids = "";
					String msg = "";
					for (int i = 0; i < billidList.size(); i++) {
						if(!billidList.get(i).equals("")){
							if (billids.equals("")) {
								billids = billidList.get(i);
							} else {
								billids += "," + billidList.get(i);
							}
						}
					}
					if ("3".equals(maintableopttype)) {// 批量插入
						String errmsg = execError + "<br/>" + execSeparator + "<br/>{389463}<br/>" + execSeparator
								+ "<br/>{389389}:<br/>";
						String successMsg = executeSuccess + "<br/>" + execSeparator + "<br/>{389464}<br/>"
								+ execSeparator + "<br/>{389394}:<br/>";
						if ("".equals(fieldvalue1)) {
							// 流程转数据批量插入,依据字段不填写
							workFlowToModeLogService.updateSuccesslogByWtlid(wtlid, execStatus, "{389457}", "");
						} else {
							for (int i = 0; i < updateModebyDetail_SuccessfulLog.size(); i++) {
								msg += updateModebyDetail_SuccessfulLog.get(i);
							}
							String trueMsg = "";
							if (this.execStatus.equals("2")) {
								trueMsg = errmsg + msg;
							} else {
								trueMsg = successMsg + msg;
							}
							if(updateModebyDetail_SuccessfulLog.size()>0){
								workFlowToModeLogService.updateSuccesslogByWtlid(wtlid, execStatus, trueMsg, billids);
							}
						}

					} else if("1".equals(maintableopttype)) {//插入
						String errmsg = execError + "<br/>" + execSeparator + "<br/>{389466}<br/>" + execSeparator
								+ "<br/>{389389}：<br/>";
						String successMsg = executeSuccess + "<br/>" + execSeparator + "<br/>{389401}<br/>" + execSeparator
								+ "<br/>{389394}：<br/>";
						String main_dt_success_msg = "";
						for (int k = 0; k < updateModebyDetail_SuccessfulLog.size(); k++) {
							main_dt_success_msg += updateModebyDetail_SuccessfulLog.get(k);
						}
						String trueMsg = "";
						if (this.execStatus.equals("2")) {
							trueMsg = errmsg + main_dt_success_msg;
						} else {
							trueMsg = successMsg + main_dt_success_msg;
						}
						if(updateModebyDetail_SuccessfulLog.size()>0){
							workFlowToModeLogService.updateSuccesslogByWtlid(wtlid, execStatus, trueMsg, billids);
						}
					}

				}else if("4".equals(maintableopttype)){//主表插入并更新
					String errmsg = execError + "<br/>" + execSeparator + "<br/>{389477}<br/>" + execSeparator
							+ "<br/>{389389}:<br/>";
					String successMsg = executeSuccess + "<br/>" + execSeparator + "<br/>{389478}<br/>"
							+ execSeparator + "<br/>{389394}:<br/>";
					String billids = "";
					String msg = "";
					for (int i = 0; i < billidList.size(); i++) {
						if(!billidList.get(i).equals("")){
							if (billids.equals("")) {
								billids = billidList.get(i);
							} else {
								billids += "," + billidList.get(i);
							}
						}
					}
					for (int i = 0; i < updateModebyDetail_SuccessfulLog.size(); i++) {
						msg += updateModebyDetail_SuccessfulLog.get(i);
					}
					String trueMsg = "";
					if (this.execStatus.equals("2")) {
						trueMsg = errmsg + msg;
					} else {
						trueMsg = successMsg + msg;
					}
					if(updateModebyDetail_SuccessfulLog.size()>0){
						workFlowToModeLogService.updateSuccesslogByWtlid(wtlid, execStatus, trueMsg, billids);
					}
				}else if("2".equals(maintableopttype)&&"1".equals(isinsert)){//更新的时候， 勾选了更新不到执行插入。

					String billids = "";
					for (int i = 0; i < billidList.size(); i++) {
						if(!billidList.get(i).equals("")){
							if (billids.equals("")) {
								billids = billidList.get(i);
							} else {
								billids += "," + billidList.get(i);
							}
						}
					}

					String errmsg = execError + "<br/>" + execSeparator + "<br/>{510185}<br/>" + execSeparator
							+ "<br/>{389389}：<br/>";
					String successMsg = executeSuccess + "<br/>" + execSeparator + "<br/>{510183}<br/>" + execSeparator
							+ "<br/>{389394}：<br/>";
					String main_dt_success_msg = "";
					for (int k = 0; k < updateModebyDetail_SuccessfulLog.size(); k++) {
						main_dt_success_msg += updateModebyDetail_SuccessfulLog.get(k);
					}
					String trueMsg = "";
					if (this.execStatus.equals("2")) {
						trueMsg = errmsg + main_dt_success_msg;
					} else {
						trueMsg = successMsg + main_dt_success_msg;
					}
					if(updateModebyDetail_SuccessfulLog.size()>0){
						workFlowToModeLogService.updateSuccesslogByWtlid(wtlid, execStatus, trueMsg, billids);
					}


				}
			}

			rs.executeQuery("select 1 from workflow_bill where tablename='uf_cubeset'");
			if(rs.next()){
				rs.executeQuery("select * from uf_cubeset where bs='workflowToMode:"+workflowtomodesetid+"'");
				if(rs.next()){
					writeLog("执行接口动作");
					String z=Util.null2String(rs.getString("z"));
					Map<String, Object> param =new HashMap<String, Object>();
					RequestInfo requestInfoDetail =new RequestInfo();
					for (int i=0; i< billidList.size();i++) {
						String rtBillid = billidList.get(i);
						requestInfoDetail.setRequestid(rtBillid);
						param.put("RequestInfo", requestInfoDetail);
						String javacondition = Util.null2String(CustomJavaCodeRun.run(z, param)).trim();
					}
				}
			}

			if(statusflag==Action.FAILURE_AND_CONTINUE){
				request.getRequestManager().setMessagecontent(messagecontent);
				return statusflag;
			}
			verifyData.clearFormid(m_formid);
			//回写数据id的权限
			if(resetdataid>0){
				//detailFieldValuesMaps
				String fieldname = Util.null2String((String)Wf_Field_Name_Map.get(resetdataid+""));//流程字段名
				String m_fieldvalue = Util.null2String((String)mainFieldValuesMap.get(fieldname.toLowerCase()));
				if("".equals(m_fieldvalue)){
					if(Wf_ManTableFieldIds.contains(resetdataid+"")){ //主表值
						String fieldtype = "";
						String fielddbtype = "";
						for(int i=0;i<Wf_ManTableFieldIds.size();i++){
							String _fieldname = (String)Wf_ManTableFieldFieldNames.get(i);
							if(_fieldname.toLowerCase().equals(fieldname.toLowerCase())){
								fieldtype = (String)Wf_ManTableFieldTypes.get(i);
								if("256".equals(fieldtype) || "257".equals(fieldtype)){
									String _fielddbtype = (String)Wf_ManTableFieldDBTypes.get(i);
									rs2.executeSql("select id from mode_customtreedetail where mainid='"+_fielddbtype+"' and sourceid="+modeid);
									if(rs2.next()){
										fielddbtype = rs2.getString("id");
									}
								}else{
									fielddbtype = (String)Wf_ManTableFieldDBTypes.get(i);
								}
								break;
							}
						}

						if ("1".equals(maintableopttype)) {//插入
							if(m_billid > 0){
								String v_billid = "";
								if("256".equals(fieldtype) || "257".equals(fieldtype)){ //树形
									v_billid = fielddbtype+"_"+m_billid;
								}else{
									//浏览框主键值
									String keyValue = getKeyFieldValue(fielddbtype,m_billid+"");
									v_billid = keyValue+"";
								}

								if(formtype.indexOf("detail") >= -1 && !"256".equals(fieldtype) && !"161".equals(fieldtype)) { // 触发表为明细表,回写多个id, 不能为单选
									for (int i=0; i< billidList.size()-1;i++) {
										String rtBillid = billidList.get(i);
										if ("256".equals(fieldtype) || "257".equals(fieldtype)) { //树形
											rtBillid = fielddbtype + "_" + rtBillid;
										}else{
											//浏览框主键值
											rtBillid = getKeyFieldValue(fielddbtype,billidList.get(i));
										}
										v_billid = rtBillid + "," + v_billid;
									}
								}

								//1.回写数据
								String _sql = "update "+tablename+" set "+fieldname+"='"+v_billid+"' where requestid='"+RequestManager.getRequestid()+"'";
								rs3.executeSql(_sql);
							}
						}else if ("2".equals(maintableopttype)) {// 更新
							String mainbillid = "";
							for(int i=0;i<mainbillids.size();i++){
								if("".equals(mainbillid)){
									if("256".equals(fieldtype) || "257".equals(fieldtype)){ //树形
										mainbillid = fielddbtype+"_"+mainbillids.get(i);
									}else{
										//浏览框主键值
										String keyValue = getKeyFieldValue(fielddbtype,mainbillids.get(i));
										mainbillid = keyValue;
									}
								}else{
									if("256".equals(fieldtype) || "257".equals(fieldtype)){ //树形
										mainbillid += ","+fielddbtype+"_"+mainbillids.get(i);
									}else{
										//浏览框主键值
										String keyValue = getKeyFieldValue(fielddbtype,mainbillids.get(i));
										mainbillid += ","+keyValue;
									}
								}
							}
							billidList.removeAll(mainbillids);
							if(formtype.indexOf("detail") >= -1) { // 触发表为明细表,回写多个id, 不能为单选
								for (int i=0; i< billidList.size();i++) {
									String rtBillid = billidList.get(i);
									if ("256".equals(fieldtype) || "257".equals(fieldtype)) { //树形
										rtBillid = fielddbtype + "_" + rtBillid;
									}else{
										rtBillid = getKeyFieldValue(fielddbtype,rtBillid);
									}
									mainbillid += "," + rtBillid;
								}

								mainbillid = mainbillid.startsWith(",")?mainbillid.substring(1):mainbillid;
							}

							if(!"".equals(mainbillid)){
								//1.回写数据
								String _sql = "update "+tablename+" set "+fieldname+"='"+mainbillid+"' where requestid='"+RequestManager.getRequestid()+"'";
								rs3.executeSql(_sql);
							}
						}else if ("3".equals(maintableopttype)) {//批量插入
							String mainbillid = "";
							for(int i=0;i<patchadd_id.size();i++){
								if("".equals(mainbillid)){
									if("256".equals(fieldtype) || "257".equals(fieldtype)){ //树形
										mainbillid = fielddbtype+"_"+patchadd_id.get(i);
									}else{
										//浏览框主键值
										String keyValue = getKeyFieldValue(fielddbtype,patchadd_id.get(i)+"");
										mainbillid = keyValue+"";
									}
								}else{
									if("256".equals(fieldtype) || "257".equals(fieldtype)){ //树形
										mainbillid += ","+fielddbtype+"_"+patchadd_id.get(i);
									}else{
										//浏览框主键值
										String keyValue = getKeyFieldValue(fielddbtype,patchadd_id.get(i)+"");
										mainbillid += ","+keyValue;
									}
								}
							}
							if(!"".equals(mainbillid)){
								//1.回写数据
								String _sql = "update "+tablename+" set "+fieldname+"='"+mainbillid+"' where requestid='"+RequestManager.getRequestid()+"'";
								rs3.executeSql(_sql);
							}
						}else if ("4".equals(maintableopttype)) {//插入并更新
							String v_billid = "";
							if(!"".equals(this.add_update_id)){
								//1.回写数据
								if("256".equals(fieldtype) || "257".equals(fieldtype)){ //树形
									v_billid = fielddbtype+"_"+this.add_update_id;
								}else{
									//浏览框主键值
									String keyValue = getKeyFieldValue(fielddbtype,this.add_update_id+"");
									//v_billid = this.add_update_id;
									v_billid = keyValue;
								}

							}
							if(formtype.indexOf("detail") >= -1) { // 触发表为明细表,回写多个id, 不能为单选
								billidList.remove(this.add_update_id);
								for (int i=0; i< billidList.size();i++) {
									String rtBillid = billidList.get(i);
									if("".equals(rtBillid)) continue;
									if ("256".equals(fieldtype) || "257".equals(fieldtype)) { //树形
										rtBillid = fielddbtype + "_" + rtBillid;
									}else{
										rtBillid = getKeyFieldValue(fielddbtype,rtBillid);
									}
									v_billid += "," + rtBillid;
								}
								v_billid = v_billid.startsWith(",")?v_billid.substring(1):v_billid;
							}

							if(!"".equals(v_billid)) {
								String _sql = "update " + tablename + " set " + fieldname + "='" + v_billid + "' where requestid='" + RequestManager.getRequestid() + "'";
								rs3.executeSql(_sql);
							}
						}
					}
				}
			}
		}

		return statusflag;
	}
	public boolean isSetVerify(int modeid){
		boolean flag = false;
		RecordSet rs = new RecordSet();
		rs.executeQuery("select 1 from modeformverify a left join modeinfo b on a.formid=b.formid where b.id=? and a.operate=0 ",modeid);
		if(rs.next()){
			flag = true;
			bathAddMessage = SystemEnv.getHtmlLabelName(519337,weaver.general.Util.threadVarLanguage());
		}
		return flag;
	}




	public String getfieldLabelname(String detailtable,String fieldname) {
		String fieldLabelNameString=fieldname;
		if(detailtable.equals("")){//主表字段
			for(int i = 0;i<Wf_ManTableFieldFieldNames.size();i++){
				if(fieldname.equals(Util.null2String(Wf_ManTableFieldFieldNames.get(i))) && Wf_ManTableFieldFieldNames.size()>i){
					fieldLabelNameString = Util.null2String(Wf_ManTableFieldNames.get(i));
				}
			}
		}else{//明细表字段
			for(int i =0;i<Wf_DetailTableNames.size();i++){
				if(detailtable.equals(Util.null2String(Wf_DetailTableNames.get(i)))){
					ArrayList detailDBFieldNames=(ArrayList) Wf_DetailDBFieldNames.get(i);
					ArrayList detialfieldnames=(ArrayList) Wf_DetailTableFieldNames.get(i);
					for(int j=0;j<detailDBFieldNames.size();j++){
						if(fieldname.equals(Util.null2String(detailDBFieldNames.get(j))) &&detialfieldnames.size()>j){
							fieldLabelNameString = Util.null2String(detialfieldnames.get(j));
						}
					}

				}
			}
		}
		return fieldLabelNameString;

	}
	private void setDetailTableMap(RecordSet rs1, int id) {
		String sql="select * from mode_workflowtomodesetopt where mainid = " + id;
		rs1.executeSql(sql);
		while(rs1.next()){
			String detailtablename = Util.null2String(rs1.getString("detailtablename"));
			String detailtableopttype = Util.null2String(rs1.getString("opttype"));
			String detailtableupdatecondition = Util.null2String(rs1.getString("updatecondition"));
			detailtableupdatecondition = detailtableupdatecondition.replace("''","\'");
			String detailtablewherecondition = Util.null2String(rs1.getString("wherecondition"));
			detailtablewherecondition = detailtablewherecondition.replace("''","\'");
			//如果"4".equals(maintableopttype)，也就是为插入并更新的时候，根据当前的值去做一个判断来给mtotIsUpdate赋值是插入还是更新。并增加条件给maintableupdatecondition
			if ("4".equals(maintableopttype)) { //插入并更新
				detailtableupdatecondition = detailtablewherecondition;
			}
			detailtableupdatecondition = Util.toScreenToEdit(detailtableupdatecondition,wf_user.getLanguage());
			List<String> detailTableFieldList = getFieldNames(detailtableupdatecondition);
			String wDetailTableName = "";
			List<String> wDetailFieldNameList = new ArrayList<String>();//子表更新条件中流程明细字段

			for(int i=0;i<detailTableFieldList.size();i++){
				String wDetailField = detailTableFieldList.get(i);
				if(wDetailField.indexOf(".")>-1){
					String[] wDetailFieldArr = wDetailField.split("\\.");
					String wDetailTableNameTmp = wDetailFieldArr[0];
					String wDetailFieldName = wDetailFieldArr[1];
					if(!"".equals(wDetailTableName)&&!wDetailTableName.equals(wDetailTableNameTmp))continue;//如果包含不同的流程子表，则条件设置错误，肯定无法处理
					wDetailTableName = wDetailTableNameTmp;
					if(!wDetailFieldNameList.contains(wDetailFieldName)){
						wDetailFieldNameList.add(wDetailFieldName);
					}
				}else{
					String wFieldName = wDetailField;
					String wFieldValue = Util.null2String(mainFieldValuesMap.get(wFieldName));
					if(!wFieldValue.trim().equals("")){
						detailtableupdatecondition = detailtableupdatecondition.replaceAll("\\$"+wFieldName+"\\$", wFieldValue.replace("\'","''"));
					}
				}

				detailtableupdatecondition =  detailtableupdatecondition.trim();
				if(detailtableupdatecondition.toLowerCase().startsWith("where ")){
					detailtableupdatecondition =" " + detailtableupdatecondition;
				}else if(!"".equals(detailtableupdatecondition)){
					detailtableupdatecondition = " where "+ detailtableupdatecondition;
				}
			}
			if (detailtableupdatecondition.toLowerCase().indexOf("where")<0) {
				detailtableupdatecondition = " where "+ detailtableupdatecondition;
			}
			Map<String,Object> optMap = new HashMap<String,Object>();
			optMap.put("opttype", detailtableopttype);
			optMap.put("updatecondition",detailtableupdatecondition);
			optMap.put("wdetailtablename", wDetailTableName);
			optMap.put("wdetailfieldnamelist", wDetailFieldNameList);
			detailtableoptMap.put(detailtablename,optMap);
		}
	}

	/**
	 * 获得模块相关信息
	 * @param modeid
	 */
	public void getModeInfo(int modeid){
		String sql = "";
		RecordSet rs = new RecordSet();
		sql = "select formid from modeinfo where id = " + modeid;
		rs.executeSql(sql);
		while(rs.next()){
			m_formid = rs.getInt("formid");
		}
	}


	/**
	 * 流程相关主字段信息
	 */
	private ArrayList Wf_ManTableFieldIds;
	private ArrayList Wf_ManTableFieldTypes;
	private ArrayList Wf_ManTableFieldHtmltypes;
	private ArrayList Wf_ManTableFieldFieldNames;
	private ArrayList Wf_ManTableFieldNames;
	private ArrayList Wf_ManTableFieldDBTypes;

	private ArrayList Wf_DetailTableFieldIds;
	private ArrayList Wf_DetailFieldDBTypes;
	private ArrayList Wf_DetailFieldTypes;
	private ArrayList Wf_DetailDBFieldNames;
	private ArrayList Wf_DetailFieldHtmlTypes;
	private ArrayList Wf_DetailTableNames;
	private ArrayList Wf_DetailTableKeys;
	private ArrayList Wf_DetailTableFieldNames;

	private HashMap Wf_Field_Table_Map = new HashMap();
	private HashMap Wf_Field_Name_Map = new HashMap();



	/**
	 * 获取流程相关字段
	 * @param isbill
	 * @param formid
	 */
	public void getWfFieldInfo(int isbill,int formid){
		int languageid = 7;
		FieldInfo FieldInfo =  new FieldInfo();
		FieldInfo.setUser(wf_user);
		FieldInfo.GetManTableField(formid,isbill,languageid);
		Wf_ManTableFieldIds = FieldInfo.getManTableFieldIds();
		Wf_ManTableFieldFieldNames = FieldInfo.getManTableFieldFieldNames();
		Wf_ManTableFieldDBTypes = FieldInfo.getManTableFieldDBTypes();
		Wf_ManTableFieldTypes = FieldInfo.getManTableFieldTypes();
		Wf_ManTableFieldHtmltypes = FieldInfo.getManTableFieldHtmltypes();
		Wf_ManTableFieldNames = FieldInfo.getManTableFieldNames();
		Wf_Field_Table_Map = FieldInfo.getField_Table_Map();
		Wf_Field_Name_Map = FieldInfo.getField_Name_Map();

		for(int i=0;i<Wf_ManTableFieldIds.size();i++){
			String id = (String)Wf_ManTableFieldIds.get(i);
			String fieldname = (String)Wf_ManTableFieldFieldNames.get(i);
			String fielddbtype = (String)Wf_ManTableFieldDBTypes.get(i);
			String fieldtype = (String)Wf_ManTableFieldTypes.get(i);
			String fieldhtmltype = String.valueOf(Wf_ManTableFieldHtmltypes.get(i));
//			System.out.println(Wf_Field_Name_Map.get(id));
//			System.out.println(id+"	"+Wf_Field_Table_Map.get(id)+"	"+fieldname+"	"+fielddbtype+"	"+fieldtype+"	"+fieldhtmltype);
		}

		FieldInfo.GetNewDetailTableField(formid,isbill,languageid);
		Wf_DetailTableFieldIds = FieldInfo.getDetailTableFieldIds();
		Wf_DetailFieldDBTypes = FieldInfo.getDetailFieldDBTypes();
		Wf_DetailFieldTypes = FieldInfo.getDetailFieldTypes();
		Wf_DetailDBFieldNames = FieldInfo.getDetailDBFieldNames();
		Wf_DetailFieldHtmlTypes = FieldInfo.getDetailFieldHtmlTypes();
		Wf_DetailTableNames = FieldInfo.getDetailTableNames();
		Wf_DetailTableKeys = FieldInfo.getDetailTableKeys();
		Wf_DetailTableFieldNames = FieldInfo.getDetailTableFieldNames();

		Wf_Field_Table_Map = FieldInfo.getField_Table_Map();
		Wf_Field_Name_Map = FieldInfo.getField_Name_Map();

//		System.out.println("Wf_DetailTableFieldIds.size():"+Wf_DetailTableFieldIds.size());

		for(int i=0;i<Wf_DetailTableFieldIds.size();i++){

			ArrayList DetailTableFieldIds = (ArrayList)Wf_DetailTableFieldIds.get(i);
			ArrayList DetailFieldDBTypes = (ArrayList)Wf_DetailFieldDBTypes.get(i);
			ArrayList DetailFieldTypes = (ArrayList)Wf_DetailFieldTypes.get(i);
			ArrayList DetailTableFieldNames = (ArrayList)Wf_DetailDBFieldNames.get(i);
			ArrayList DetailFieldHtmlTypes = (ArrayList)Wf_DetailFieldHtmlTypes.get(i);
			//String detailtable = (String)Wf_DetailTableNames.get(i);
			//String keyfield = (String)Wf_DetailTableKeys.get(i);
//			System.out.println(detailtable+"	"+keyfield);
			//writeLog(detailtable+"	"+keyfield);
			for(int j=0;j<DetailTableFieldIds.size();j++){
				String id = (String)DetailTableFieldIds.get(j);
				String fieldname = (String)DetailTableFieldNames.get(j);
				String fielddbtype = (String)DetailFieldDBTypes.get(j);
				String fieldtype = (String)DetailFieldTypes.get(j);
				String fieldhtmltype = String.valueOf(DetailFieldHtmlTypes.get(j));
//				System.out.println(Wf_Field_Name_Map.get(id));
//				System.out.println(id+"	"+Wf_Field_Table_Map.get(id)+"	"+fieldname+"	"+fielddbtype+"	"+fieldtype+"	"+fieldhtmltype);
			}
		}

	}

	/**
	 * 模块相关主字段信息
	 */
	private ArrayList M_ManTableFieldIds;
	private ArrayList M_ManTableFieldTypes;
	private ArrayList M_ManTableFieldHtmltypes;
	private ArrayList M_ManTableFieldFieldNames;
	private ArrayList M_ManTableFieldDBTypes;

	private ArrayList M_DetailTableFieldIds;
	private ArrayList M_DetailFieldDBTypes;
	private ArrayList M_DetailFieldTypes;
	private ArrayList M_DetailDBFieldNames;
	private ArrayList M_DetailFieldHtmlTypes;
	private ArrayList M_DetailTableNames;
	private ArrayList M_DetailTableKeys;

	private HashMap M_Field_Table_Map = new HashMap();
	private HashMap M_Field_Name_Map = new HashMap();

	public void getModeFieldInfo(int formid,int isbill){
		int languageid = 7;
		FieldInfo FieldInfo =  new FieldInfo();
		FieldInfo.setUser(wf_user);
		FieldInfo.GetManTableField(formid,isbill,languageid);
		M_ManTableFieldIds = FieldInfo.getManTableFieldIds();
		M_ManTableFieldFieldNames = FieldInfo.getManTableFieldFieldNames();
		M_ManTableFieldDBTypes = FieldInfo.getManTableFieldDBTypes();
		M_ManTableFieldTypes = FieldInfo.getManTableFieldTypes();
		M_ManTableFieldHtmltypes = FieldInfo.getManTableFieldHtmltypes();

		M_Field_Table_Map = FieldInfo.getField_Table_Map();
		M_Field_Name_Map = FieldInfo.getField_Name_Map();

		for(int i=0;i<M_ManTableFieldIds.size();i++){
			String id = (String)M_ManTableFieldIds.get(i);
			String fieldname = (String)M_ManTableFieldFieldNames.get(i);
			String fielddbtype = (String)M_ManTableFieldDBTypes.get(i);
			String fieldtype = (String)M_ManTableFieldTypes.get(i);
			String fieldhtmltype = String.valueOf(M_ManTableFieldHtmltypes.get(i));

//			System.out.println(M_Field_Name_Map.get(id));
//			System.out.println(id+"	"+M_Field_Table_Map.get(id)+"	"+fieldname+"	"+fielddbtype+"	"+fieldtype+"	"+fieldhtmltype);
		}

		FieldInfo.GetNewDetailTableField(formid,isbill,languageid);
		M_DetailTableFieldIds = FieldInfo.getDetailTableFieldIds();
		M_DetailFieldDBTypes = FieldInfo.getDetailFieldDBTypes();
		M_DetailFieldTypes = FieldInfo.getDetailFieldTypes();
		M_DetailDBFieldNames = FieldInfo.getDetailDBFieldNames();
		M_DetailFieldHtmlTypes = FieldInfo.getDetailFieldHtmlTypes();
		M_DetailTableNames = FieldInfo.getDetailTableNames();
		M_DetailTableKeys = FieldInfo.getDetailTableKeys();

		M_Field_Table_Map = FieldInfo.getField_Table_Map();
		M_Field_Name_Map = FieldInfo.getField_Name_Map();

//		System.out.println("M_DetailTableFieldIds.size():"+M_DetailTableFieldIds.size());

		for(int i=0;i<M_DetailTableFieldIds.size();i++){
			ArrayList DetailTableFieldIds = (ArrayList)M_DetailTableFieldIds.get(i);
			ArrayList DetailFieldDBTypes = (ArrayList)M_DetailFieldDBTypes.get(i);
			ArrayList DetailFieldTypes = (ArrayList)M_DetailFieldTypes.get(i);
			ArrayList DetailTableFieldNames = (ArrayList)M_DetailDBFieldNames.get(i);
			ArrayList DetailFieldHtmlTypes = (ArrayList)M_DetailFieldHtmlTypes.get(i);
			//String detailtable = (String)Wf_DetailTableNames.get(i);
			//String keyfield = (String)Wf_DetailTableKeys.get(i);
//			System.out.println(detailtable+"	"+keyfield);
			for(int j=0;j<DetailTableFieldIds.size();j++){
				String id = (String)DetailTableFieldIds.get(j);
				String fieldname = (String)DetailTableFieldNames.get(j);
				String fielddbtype = (String)DetailFieldDBTypes.get(j);
				String fieldtype = (String)DetailFieldTypes.get(j);
				String fieldhtmltype = String.valueOf(DetailFieldHtmlTypes.get(j));
//				System.out.println(M_Field_Name_Map.get(id));
//				System.out.println(id+"	"+M_Field_Table_Map.get(id)+"	"+fieldname+"	"+fielddbtype+"	"+fieldtype+"	"+fieldhtmltype);
			}
		}
	}

	private HashMap mainFieldValuesMap = new HashMap();//主字段的值
	private HashMap detailFieldValuesMaps = new HashMap();//所有明细表的值

	/**
	 * 重新组装流程的值
	 * @return
	 */
	public void getWorkflowDataValue(RequestInfo RequestInfo){

		mainFieldValuesMap.put("-1", RequestInfo.getDescription());//流程标题
		mainFieldValuesMap.put("requestname", RequestInfo.getDescription());//流程标题
		mainFieldValuesMap.put("-2", RequestInfo.getRequestid());//流程id\
		mainFieldValuesMap.put("requestid", RequestInfo.getRequestid());//流程id
		mainFieldValuesMap.put("-3", String.valueOf(wf_creater));//流程创建人
		mainFieldValuesMap.put("-200", String.valueOf(wf_creatDate));//流程创建时间
		mainFieldValuesMap.put("-201", String.valueOf(wf_createrDepartment));//流程创建人部门
		mainFieldValuesMap.put("-202", wf_nodeName);//流程当前节点名称
		mainFieldValuesMap.put("-203", wf_nodeLinkName);//流程当前出口名称




		Property[] properties = RequestInfo.getMainTableInfo().getProperty();// 获取表单主字段信息
		for (int i = 0; i < properties.length; i++) {// 主表数据
			String name = properties[i].getName().toLowerCase();
			String value = Util.null2String(properties[i].getValue());
			value = EncryptConfigBiz.getDecryptData(value);
//			log.info("主字段："+name+" "+value);
			if (value.indexOf(WorkflowToModeUtil.SPLIT_LOCATION) != -1 || value.indexOf(WorkflowToModeUtil.SPLIT_FIELD) != -1) {//判断是否存在地址分割符，如果存在就进行转换
				value = WorkflowToModeUtil.toList(value);
			}
			mainFieldValuesMap.put(name, value);


		}

		FormManager fManager = new FormManager();

		DetailTableInfo dti = RequestInfo.getDetailTableInfo();
		DetailTable[] detailtable = null;
		if (dti == null) {
		} else {
			detailtable = dti.getDetailTable();// 获取明细表
		}
		if (detailtable!=null&&detailtable.length > 0) {
			for (int i = 0; i < detailtable.length; i++) {
				String tablename = "";
				ArrayList rowList = new ArrayList();
				DetailTable dt = detailtable[i];
				Row[] s = dt.getRow();
				for (int j = 0; j < s.length; j++) {
					HashMap rowMap = new HashMap();
					Row r = s[j];
					rowMap.put("id", r.getId());
					Cell c[] = r.getCell();
					for (int k = 0; k < c.length; k++) {
						Cell c1 = c[k];
						String name = c1.getName().toLowerCase();
						String value = c1.getValue();
						tablename = c1.getCol1();
						if(wf_isbill==0&&(tablename==null||"".equals(tablename))){
							tablename = i+"";
						}
						value = EncryptConfigBiz.getDecryptData(value);
						//添加对地址信息的解析逻辑
						if (value.indexOf(WorkflowToModeUtil.SPLIT_LOCATION) != -1 || value.indexOf(WorkflowToModeUtil.SPLIT_FIELD) != -1) {//判断是否存在地址分割符，如果存在就进行转换
							value = WorkflowToModeUtil.toList(value);
						}
						rowMap.put(name, value);


//						log.info("明细字段："+name+" "+value);
					}

					if(rowMap.size()>0){
						rowList.add(rowMap);
					}
				}
				//String tablename = fManager.getDetailTablename(wf_formid, (i+1));
				//String tablename = "formtable_main_"+Math.abs(wf_formid)+"_dt"+(i+1);
				detailFieldValuesMaps.put(tablename.toLowerCase(), rowList);
//				System.out.println("---------------------------------------------------------------");
//				System.out.println("tablename:"+tablename+"	rowList:"+rowList.size());
			}
		}
	}

	/**
	 * 组装创建模块的WorkflowRequestInfo
	 * @param modeid
	 * @param modecreater
	 * @param modecreaterfieldid
	 * @return
	 */
	public WorkflowRequestInfo getWorkflowRequestInfo(int modeid,int modecreater,int modecreaterfieldid){

		Wf_Field_Name_Map.put("-1", "-1");//流程标题
		Wf_Field_Name_Map.put("-2", "-2");//流程id
		Wf_Field_Name_Map.put("-3", "-3");//流程创建人
		Wf_Field_Name_Map.put("-200", "-200");//流程创建时间
		Wf_Field_Name_Map.put("-201", "-201");//流程创建人部门
		Wf_Field_Name_Map.put("-202", "-202");//流程节点名称
		Wf_Field_Name_Map.put("-203", "-203");//流程出口名称

		int creater = 0;
		if(modecreater==1){//当前用户
			creater = wf_user.getUID();
		}else if(modecreater==2){//流程创建人
			creater = Util.getIntValue((String)mainFieldValuesMap.get("-3"),1);
		}else if(modecreater==3){//模块中的人力资源字段
			String fieldvalue = "";
			String m_fieldname = "";
			//取该主字段对应的模块字段
//			String m_fieldid = Util.null2String((String)mf_wf_map.get(String.valueOf(modecreaterfieldid)));
			String m_fieldid = String.valueOf(modecreaterfieldid);//数据库中保存的字段即为流程字段，无须再转换
			if(!m_fieldid.equals("")){
				m_fieldname = Util.null2String((String)Wf_Field_Name_Map.get(m_fieldid));//流程字段名
				fieldvalue = Util.null2String((String)mainFieldValuesMap.get(m_fieldname.toLowerCase()));
				creater = Util.getIntValue(fieldvalue.split(",")[0]);
			}
		}
		if(creater<=0){
			creater = wf_user.getUID();
		}

		String requestname = "";
		if(requestname.equals("")){
			String fieldvalue = "";
			String m_fieldname = "";
			//取该主字段对应的模块字段
			String m_fieldid = Util.null2String((String)mf_wf_map.get("-1"));
			if(!m_fieldid.equals("")){
				m_fieldname = Util.null2String((String)Wf_Field_Name_Map.get(m_fieldid));//流程字段名
				fieldvalue = Util.null2String((String)mainFieldValuesMap.get(m_fieldname.toLowerCase()));
				requestname = fieldvalue;
			}
		}

		String requestlevel = "";
		if(requestlevel.equals("")){
			String fieldvalue = "";
			String m_fieldname = "";
			//取该主字段对应的模块字段
			String m_fieldid = Util.null2String((String)mf_wf_map.get("-1"));
			if(!m_fieldid.equals("")){
				m_fieldname = Util.null2String((String)Wf_Field_Name_Map.get(m_fieldid));//流程字段名
				fieldvalue = Util.null2String((String)mainFieldValuesMap.get(m_fieldname.toLowerCase()));
				requestlevel = String.valueOf(Util.getIntValue(fieldvalue,0));
			}
		}

		//主字段
		WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[M_ManTableFieldIds.size()]; //字段信息
		int index = 0;
		for(int i=0;i<M_ManTableFieldIds.size();i++){
			String id = Util.null2String((String)M_ManTableFieldIds.get(i));
			String fieldname = Util.null2String((String)M_ManTableFieldFieldNames.get(i));
			String fielddbtype = Util.null2String((String)M_ManTableFieldDBTypes.get(i));
			String fieldtype = Util.null2String((String)M_ManTableFieldTypes.get(i));
			String fieldhtmltype = Util.null2String(String.valueOf(M_ManTableFieldHtmltypes.get(i)));
			String fieldvalue = "";
			String m_fieldname = "";

			//取该主字段对应的模块字段
			String m_fieldid = Util.null2String((String)mf_wf_map.get(id));
			if(!m_fieldid.equals("")){
				m_fieldname = Util.null2String((String)Wf_Field_Name_Map.get(m_fieldid));//流程字段名
				fieldvalue = Util.null2String((String)mainFieldValuesMap.get(m_fieldname.toLowerCase()));

				if(fieldMap.containsKey(id)) {//此字段配置了字段赋值
					fieldvalue = processData("maintable",fieldname,fieldMap.get(id), "main", fieldvalue, new HashMap(),"",fieldhtmltype,fieldtype);
					//mainFieldValuesMap.put(m_fieldname,fieldvalue);  之前是明细触发时候，需要，现在明细触发单独走下面逻辑。
				}
				wrti[index] = new WorkflowRequestTableField();
				wrti[index].setFieldName(fieldname);//字段名
				wrti[index].setFieldValue(fieldvalue);//字段的值
				if (wfiMainfieldmap.containsKey(m_fieldid)) {//给流程字段赋值
					wfiMainfieldmap.put(m_fieldid, fieldvalue);
				}
				wrti[index].setView(true);//字段是否可见
				wrti[index].setEdit(true);//字段是否可编辑
				index++;
			}
//			System.out.println(Wf_Field_Name_Map.get(id));
//			System.out.println(id+"	"+Wf_Field_Table_Map.get(id)+"	"+m_fieldname+"	"+fieldvalue+"	"+fieldname+"	"+fielddbtype+"	"+fieldtype+"	"+fieldhtmltype);
		}

		WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据
		wrtri[0] = new WorkflowRequestTableRecord();
		wrtri[0].setWorkflowRequestTableFields(wrti);

		WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();
		wmi.setRequestRecords(wrtri);

		//明细字段
		WorkflowDetailTableInfo wdti[] = new WorkflowDetailTableInfo[M_DetailTableFieldIds.size()];//两个明细表0明细表1,1明细表2
//		System.out.println("Wf_DetailTableFieldIds.size():"+Wf_DetailTableFieldIds.size());
		for(int i=0;i<M_DetailTableFieldIds.size();i++){

			ArrayList DetailTableFieldIds = (ArrayList)M_DetailTableFieldIds.get(i);
			ArrayList DetailFieldDBTypes = (ArrayList)M_DetailFieldDBTypes.get(i);
			ArrayList DetailFieldTypes = (ArrayList)M_DetailFieldTypes.get(i);
			ArrayList DetailTableFieldNames = (ArrayList)M_DetailDBFieldNames.get(i);
			ArrayList DetailFieldHtmlTypes = (ArrayList)M_DetailFieldHtmlTypes.get(i);
			String detailtable = Util.null2String((String)M_DetailTableNames.get(i));
			String keyfield = Util.null2String((String)M_DetailTableKeys.get(i));
//			System.out.println(detailtable+"	"+keyfield);

			//获取当前明细表取哪个明细表的数据
			String m_detailtable = "";
			boolean isset=false;//标识模块明细是否设置了流程字段(如果全为空,则没有设置)
			for(int j=0;j<DetailTableFieldIds.size();j++){
				String id = Util.null2String((String)DetailTableFieldIds.get(j));
				String fieldname = Util.null2String((String)DetailTableFieldNames.get(j));
				String fielddbtype = Util.null2String((String)DetailFieldDBTypes.get(j));
				String fieldtype = Util.null2String((String)DetailFieldTypes.get(j));
				String fieldhtmltype = String.valueOf(DetailFieldHtmlTypes.get(j));

				String m_fieldid = Util.null2String((String)mf_wf_map.get(id));
				if (!"".equals(m_fieldid)) {
					isset=true;
				}
				m_detailtable = Util.null2String((String)Wf_Field_Table_Map.get(m_fieldid));
//				System.out.println("m_detailtable:"+m_detailtable);
				if(!m_detailtable.equals("")&&!"maintable".equals(m_detailtable)){
					break;
				}
//				System.out.println(Wf_Field_Name_Map.get(id));
//				System.out.println(id+"	"+Wf_Field_Table_Map.get(id)+"	"+fieldname+"	"+fielddbtype+"	"+fieldtype+"	"+fieldhtmltype);
			}
//			System.out.println("m_detailtable:"+m_detailtable);
			ArrayList rowList;
			//如果主表为更新,模块明细字段对应的全部为流程主字段,则不管流程明细表有几条记录,最终模块明细表只要插入一条记录
			if ("2".equals(maintableopttype)&&!wfiDetailfields.contains(m_detailtable)) {
				rowList = new ArrayList();
				if (isset) {
					HashMap rowMap = new HashMap();
					rowMap.put("exp", "exp");
					rowList.add(rowMap);
				}
			}else {
				//获得对应明细的数据
				rowList = (ArrayList)detailFieldValuesMaps.get(m_detailtable.toLowerCase());
			}
			wrtri = new WorkflowRequestTableRecord[0];//数据 行数
			if(rowList!=null && rowList.size()>0){
//				System.out.println("rowList:"+rowList.size());
				wrtri = new WorkflowRequestTableRecord[rowList.size()];//数据 行数

				for(int j=0;j<rowList.size();j++){
					HashMap rowMap = (HashMap)rowList.get(j);
					if(rowMap!=null&&rowMap.size()>0){
						wrti = new WorkflowRequestTableField[DetailTableFieldIds.size()]; //每行字段个数
//						System.out.println("DetailTableFieldIds.size():"+DetailTableFieldIds.size());

						for(int k=0;k<DetailTableFieldIds.size();k++){
							String id = Util.null2String((String)DetailTableFieldIds.get(k));
							String fieldname = Util.null2String((String)DetailTableFieldNames.get(k));
							String fielddbtype = Util.null2String((String)DetailFieldDBTypes.get(k));
							String fieldtype = Util.null2String((String)DetailFieldTypes.get(k));
							String fieldhtmltype = String.valueOf(DetailFieldHtmlTypes.get(k));

							String fieldvalue = "";
							String m_fieldname = "";

							//取该主字段对应的模块字段
							String m_fieldid = Util.null2String((String)mf_wf_map.get(id));
//							System.out.println(k+"	"+"m_fieldid:"+m_fieldid);

							if(!m_fieldid.equals("")){
								if (Util.getIntValue(m_fieldid, 0) < -2) {
									m_fieldname = "id";
								} else {
									m_fieldname = Util.null2String((String)Wf_Field_Name_Map.get(m_fieldid));//流程字段名
								}
								if ("2".equals(maintableopttype)&&wfiMainfieldmap.containsKey(m_fieldid)) {//如果主表操作是更新,且当前明细表字段设置的对应字段是流程主表字段
									fieldvalue = Util.null2String(wfiMainfieldmap.get(m_fieldid));
								}else {
									fieldvalue = Util.null2String((String)rowMap.get(m_fieldname.toLowerCase()));
								}
								//==========处理明细的字段公式
								if(fieldMap.containsKey(id)) {//此字段配置了字段赋值
									fieldvalue = processData(detailtable,fieldname,fieldMap.get(id), "detail", fieldvalue,rowMap,m_detailtable,fieldhtmltype,fieldtype);
								}


								wrti[k] = new WorkflowRequestTableField();
								wrti[k].setFieldName(fieldname);//字段名
								wrti[k].setFieldValue(fieldvalue);//字段的值
								wrti[k].setView(true);//字段是否可见
								wrti[k].setEdit(true);//字段是否可编辑
//								System.out.println(Wf_Field_Name_Map.get(id));
//								System.out.println(id+"	"+Wf_Field_Table_Map.get(id)+"	"+m_fieldname+"	"+fieldvalue+"	"+fieldname+"	"+fielddbtype+"	"+fieldtype+"	"+fieldhtmltype);
							}
						}
						wrtri[j] = new WorkflowRequestTableRecord();
						wrtri[j].setWorkflowRequestTableFields(wrti);
					}
				}
			}
			wdti[i] = new WorkflowDetailTableInfo();
			wdti[i].setTableDBName(detailtable);
			wdti[i].setWorkflowRequestTableRecords(wrtri);//加入明细表1的数据
		}

		WorkflowBaseInfo wbi = new WorkflowBaseInfo();
		wbi.setWorkflowId(String.valueOf(modeid));

		WorkflowRequestInfo wri = new WorkflowRequestInfo();//流程基本信息
		wri.setCreatorId(String.valueOf(creater));//创建人id
		wri.setRequestLevel(requestlevel);//0 正常，1重要，2紧急
		wri.setRequestName(requestname);//流程标题
		wri.setWorkflowMainTableInfo(wmi);//添加主字段数据
		wri.setWorkflowBaseInfo(wbi);
		wri.setWorkflowDetailTableInfos(wdti);

		return wri;
	}

	/**
	 * 组装重复验证的WorkflowRequestInfo
	 * @param workflowid
	 * @param wfcreater
	 * @param wfcreaterfieldid
	 * @return
	 */
	public WorkflowRequestInfo getWorkflowRequestInfo1(int modeid,int modecreater,int modecreaterfieldid){

		Wf_Field_Name_Map.put("-1", "-1");//流程标题
		Wf_Field_Name_Map.put("-2", "-2");//流程id
		Wf_Field_Name_Map.put("-3", "-3");//流程创建人
		Wf_Field_Name_Map.put("-200", "-200");//流程创建时间
		Wf_Field_Name_Map.put("-201", "-201");//流程创建人部门
		Wf_Field_Name_Map.put("-202", "-202");//流程节点名称
		Wf_Field_Name_Map.put("-203", "-203");//流程出口名称

		int creater = 0;
		if(modecreater==1){//当前用户
			creater = wf_user.getUID();
		}else if(modecreater==2){//流程创建人
			creater = Util.getIntValue((String)mainFieldValuesMap.get("-3"),1);
		}else if(modecreater==3){//模块中的人力资源字段
			String fieldvalue = "";
			String m_fieldname = "";
			//取该主字段对应的模块字段
//			String m_fieldid = Util.null2String((String)mf_wf_map.get(String.valueOf(modecreaterfieldid)));
			String m_fieldid = String.valueOf(modecreaterfieldid);//数据库中保存的字段即为流程字段，无须再转换
			if(!m_fieldid.equals("")){
				m_fieldname = Util.null2String((String)Wf_Field_Name_Map.get(m_fieldid));//流程字段名
				fieldvalue = Util.null2String((String)mainFieldValuesMap.get(m_fieldname.toLowerCase()));
				creater = Util.getIntValue(fieldvalue.split(",")[0]);
			}
		}
		if(creater<=0){
			creater = wf_user.getUID();
		}

		String requestname = "";
		if(requestname.equals("")){
			String fieldvalue = "";
			String m_fieldname = "";
			//取该主字段对应的模块字段
			String m_fieldid = Util.null2String((String)mf_wf_map.get("-1"));
			if(!m_fieldid.equals("")){
				m_fieldname = Util.null2String((String)Wf_Field_Name_Map.get(m_fieldid));//流程字段名
				fieldvalue = Util.null2String((String)mainFieldValuesMap.get(m_fieldname.toLowerCase()));
				requestname = fieldvalue;
			}
		}

		String requestlevel = "";
		if(requestlevel.equals("")){
			String fieldvalue = "";
			String m_fieldname = "";
			//取该主字段对应的模块字段
			String m_fieldid = Util.null2String((String)mf_wf_map.get("-1"));
			if(!m_fieldid.equals("")){
				m_fieldname = Util.null2String((String)Wf_Field_Name_Map.get(m_fieldid));//流程字段名
				fieldvalue = Util.null2String((String)mainFieldValuesMap.get(m_fieldname.toLowerCase()));
				requestlevel = String.valueOf(Util.getIntValue(fieldvalue,0));
			}
		}

		//判断映射字段来自于主表还是明细表，还是两个都有
		Boolean mainFlag = false;
		Boolean detailFlag = false;
		int maxSize = 0;
		for(int i=0; i<M_ManTableFieldIds.size(); i++){
			String id = Util.null2String((String)M_ManTableFieldIds.get(i));
			String m_fieldid = Util.null2String((String)mf_wf_map.get(id));
			String m_table = Util.null2String((String)Wf_Field_Table_Map.get(m_fieldid));
			if(!"".equals(m_table) && !"null".equals(m_table) && "maintable".equals(m_table)){
				mainFlag = true;
			}else if(!"".equals(m_table) && !"null".equals(m_table) && !"maintable".equals(m_table)){
				detailFlag = true;
				//for(int x =0; x<M_DetailTableFieldIds.size() && !m_fieldid.equals(""); x++) {
				ArrayList rowList = (ArrayList) detailFieldValuesMaps.get(m_table.toLowerCase());
				if("".equals(rowList)||rowList==null) continue;
				maxSize = maxSize > rowList.size() ? maxSize : rowList.size();
				//}
			}
			if( mainFlag && detailFlag){
				break;
			}
		}

		//主字段
		WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[M_ManTableFieldIds.size()];; //字段信息
		//WorkflowRequestTableField[] wrti1 = new WorkflowRequestTableField[M_ManTableFieldIds.size()];
		WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];
		int index = 0;
		if(mainFlag && !detailFlag){//仅主表
			wrti = new WorkflowRequestTableField[M_ManTableFieldIds.size()];
			for(int i=0;i<M_ManTableFieldIds.size();i++) {
				String id = Util.null2String((String) M_ManTableFieldIds.get(i));
				String fieldname = Util.null2String((String) M_ManTableFieldFieldNames.get(i));
				String fielddbtype = Util.null2String((String) M_ManTableFieldDBTypes.get(i));
				String fieldtype = Util.null2String((String) M_ManTableFieldTypes.get(i));
				String fieldhtmltype = Util.null2String(String.valueOf(M_ManTableFieldHtmltypes.get(i)));
				String fieldvalue = "";
				String m_fieldname = "";

				//取该主字段对应的模块字段
				String m_fieldid = Util.null2String((String) mf_wf_map.get(id));
				m_fieldname = Util.null2String((String) Wf_Field_Name_Map.get(m_fieldid));//流程字段名
				fieldvalue = Util.null2String((String) mainFieldValuesMap.get(m_fieldname.toLowerCase()));

				wrti[index] = new WorkflowRequestTableField();
				wrti[index].setFieldName(fieldname);//字段名
				wrti[index].setFieldValue(fieldvalue);//字段的值
				if (wfiMainfieldmap.containsKey(m_fieldid)) {//给流程字段赋值
					wfiMainfieldmap.put(m_fieldid, fieldvalue);
				}
				wrti[index].setView(true);//字段是否可见
				wrti[index].setEdit(true);//字段是否可编辑
				index++;
			}
			wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据
			wrtri[0] = new WorkflowRequestTableRecord();
			wrtri[0].setWorkflowRequestTableFields(wrti);
		}else if(detailFlag){//仅明细,主明都有不需要单独处理
			wrtri = new WorkflowRequestTableRecord[maxSize];
			for(int j = 0; j<maxSize ; j++){
				wrti = new WorkflowRequestTableField[M_ManTableFieldIds.size()];
				for(int i=0;i<M_ManTableFieldIds.size();i++) {
					String id = Util.null2String((String) M_ManTableFieldIds.get(i));
					String fieldname = Util.null2String((String) M_ManTableFieldFieldNames.get(i));
					String fielddbtype = Util.null2String((String) M_ManTableFieldDBTypes.get(i));
					String fieldtype = Util.null2String((String) M_ManTableFieldTypes.get(i));
					String fieldhtmltype = Util.null2String(String.valueOf(M_ManTableFieldHtmltypes.get(i)));
					String fieldvalue = "";
					String m_fieldname = "";

					//取该主字段对应的模块字段
					String m_fieldid = Util.null2String((String) mf_wf_map.get(id));

					//for(int x =0; x<M_DetailTableFieldIds.size() && !m_fieldid.equals(""); x++) {
					String m_detailtable = Util.null2String((String) Wf_Field_Table_Map.get(m_fieldid));
					ArrayList rowList = (ArrayList)detailFieldValuesMaps.get(m_detailtable.toLowerCase());
					if (Util.getIntValue(m_fieldid, 0) < -2) {
						m_fieldname = "id";
					} else {
						m_fieldname = Util.null2String((String)Wf_Field_Name_Map.get(m_fieldid));//流程字段名
					}
					if(rowList!=null && rowList.size()>0) {
						if(rowList.size() >= j){
							HashMap rowMap = (HashMap) rowList.get(j);
							if(m_detailtable==null||"".equals(m_detailtable)){//主表字段
								if (-200 < Util.getIntValue(m_fieldname, -2) && Util.getIntValue(m_fieldname.toLowerCase(), -2) < -3) { //是明细ID的时候
									fieldvalue = Util.null2String(rowMap.get("id"));
								} else {
									fieldvalue = Util.null2String(mainFieldValuesMap.get(m_fieldname.toLowerCase()));
								}
							}else{
								fieldvalue = Util.null2String(rowMap.get(m_fieldname.toLowerCase()));
							}
							//fieldvalue = Util.null2String((String)rowMap.get(m_fieldname.toLowerCase()));
						}else{//明细表行数不够，用""补齐，主表依旧取主表值
							if(m_detailtable==null||"".equals(m_detailtable)){//主表字段
								if (-200 < Util.getIntValue(m_fieldname, -2) && Util.getIntValue(m_fieldname.toLowerCase(), -2) < -3) { //是明细ID的时候
									fieldvalue = Util.null2String("");
								} else {
									fieldvalue = Util.null2String(mainFieldValuesMap.get(m_fieldname.toLowerCase()));
								}
							}else{
								fieldvalue = Util.null2String("");
							}
						}
					}else{
						if(mainFlag){//处理流程主表字段  用于 流程明细触发时 主表字段和明细表字段同时转到建模主表  建模主表做组合验证的情况
							m_fieldname = Util.null2String((String) Wf_Field_Name_Map.get(m_fieldid));//流程字段名
							fieldvalue = Util.null2String((String) mainFieldValuesMap.get(m_fieldname.toLowerCase()));
						}else{
							fieldvalue = Util.null2String("");
						}
					}
					wrti[i] = new WorkflowRequestTableField();
					wrti[i].setFieldName(fieldname);//字段名
					wrti[i].setFieldValue(fieldvalue);//字段的值
					//if (wfiMainfieldmap.containsKey(m_fieldid)) {//给流程字段赋值
					//	wfiMainfieldmap.put(m_fieldid, fieldvalue);
					//}
					wrti[i].setView(true);//字段是否可见
					wrti[i].setEdit(true);//字段是否可编辑
					//}
				}
				wrtri[j] = new WorkflowRequestTableRecord();
				wrtri[j].setWorkflowRequestTableFields(wrti);
			}
		}
		WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();
		wmi.setRequestRecords(wrtri);

		//明细字段
		WorkflowDetailTableInfo wdti[] = new WorkflowDetailTableInfo[M_DetailTableFieldIds.size()];//两个明细表0明细表1,1明细表2
//		System.out.println("Wf_DetailTableFieldIds.size():"+Wf_DetailTableFieldIds.size());
		for(int i=0;i<M_DetailTableFieldIds.size();i++){

			ArrayList DetailTableFieldIds = (ArrayList)M_DetailTableFieldIds.get(i);
			ArrayList DetailFieldDBTypes = (ArrayList)M_DetailFieldDBTypes.get(i);
			ArrayList DetailFieldTypes = (ArrayList)M_DetailFieldTypes.get(i);
			ArrayList DetailTableFieldNames = (ArrayList)M_DetailDBFieldNames.get(i);
			ArrayList DetailFieldHtmlTypes = (ArrayList)M_DetailFieldHtmlTypes.get(i);
			String detailtable = Util.null2String((String)M_DetailTableNames.get(i));
			String keyfield = Util.null2String((String)M_DetailTableKeys.get(i));
//			System.out.println(detailtable+"	"+keyfield);

			//获取当前明细表取哪个明细表的数据
			String m_detailtable = "";
			boolean isset=false;//标识模块明细是否设置了流程字段(如果全为空,则没有设置)
			for(int j=0;j<DetailTableFieldIds.size();j++){
				String id = Util.null2String((String)DetailTableFieldIds.get(j));
				String fieldname = Util.null2String((String)DetailTableFieldNames.get(j));
				String fielddbtype = Util.null2String((String)DetailFieldDBTypes.get(j));
				String fieldtype = Util.null2String((String)DetailFieldTypes.get(j));
				String fieldhtmltype = String.valueOf(DetailFieldHtmlTypes.get(j));

				String m_fieldid = Util.null2String((String)mf_wf_map.get(id));
				if (!"".equals(m_fieldid)) {
					isset=true;
				}
				m_detailtable = Util.null2String((String)Wf_Field_Table_Map.get(m_fieldid));
//				System.out.println("m_detailtable:"+m_detailtable);
				if(!m_detailtable.equals("")&&!"maintable".equals(m_detailtable)){
					break;
				}
//				System.out.println(Wf_Field_Name_Map.get(id));
//				System.out.println(id+"	"+Wf_Field_Table_Map.get(id)+"	"+fieldname+"	"+fielddbtype+"	"+fieldtype+"	"+fieldhtmltype);
			}
//			System.out.println("m_detailtable:"+m_detailtable);
			ArrayList rowList;
			//如果主表为更新,模块明细字段对应的全部为流程主字段,则不管流程明细表有几条记录,最终模块明细表只要插入一条记录
			if ("2".equals(maintableopttype)&&!wfiDetailfields.contains(m_detailtable)) {
				rowList = new ArrayList();
				if (isset) {
					HashMap rowMap = new HashMap();
					rowMap.put("exp", "exp");
					rowList.add(rowMap);
				}
			}else {
				//获得对应明细的数据
				rowList = (ArrayList)detailFieldValuesMaps.get(m_detailtable.toLowerCase());
			}
			wrtri = new WorkflowRequestTableRecord[0];//数据 行数
			if(rowList!=null && rowList.size()>0){
//				System.out.println("rowList:"+rowList.size());
				wrtri = new WorkflowRequestTableRecord[rowList.size()];//数据 行数

				for(int j=0;j<rowList.size();j++){
					HashMap rowMap = (HashMap)rowList.get(j);
					if(rowMap!=null&&rowMap.size()>0){
						wrti = new WorkflowRequestTableField[DetailTableFieldIds.size()]; //每行字段个数
//						System.out.println("DetailTableFieldIds.size():"+DetailTableFieldIds.size());

						for(int k=0;k<DetailTableFieldIds.size();k++){
							String id = Util.null2String((String)DetailTableFieldIds.get(k));
							String fieldname = Util.null2String((String)DetailTableFieldNames.get(k));
							String fielddbtype = Util.null2String((String)DetailFieldDBTypes.get(k));
							String fieldtype = Util.null2String((String)DetailFieldTypes.get(k));
							String fieldhtmltype = String.valueOf(DetailFieldHtmlTypes.get(k));

							String fieldvalue = "";
							String m_fieldname = "";

							//取该主字段对应的模块字段
							String m_fieldid = Util.null2String((String)mf_wf_map.get(id));
//							System.out.println(k+"	"+"m_fieldid:"+m_fieldid);

							if(!m_fieldid.equals("")){
								if (Util.getIntValue(m_fieldid, 0) < -2) {
									m_fieldname = "id";
								} else {
									m_fieldname = Util.null2String((String)Wf_Field_Name_Map.get(m_fieldid));//流程字段名
								}
								if ("2".equals(maintableopttype)&&wfiMainfieldmap.containsKey(m_fieldid)) {//如果主表操作是更新,且当前明细表字段设置的对应字段是流程主表字段
									fieldvalue = Util.null2String(wfiMainfieldmap.get(m_fieldid));
								}else {
									fieldvalue = Util.null2String((String)rowMap.get(m_fieldname.toLowerCase()));
								}

								wrti[k] = new WorkflowRequestTableField();
								wrti[k].setFieldName(fieldname);//字段名
								wrti[k].setFieldValue(fieldvalue);//字段的值
								wrti[k].setView(true);//字段是否可见
								wrti[k].setEdit(true);//字段是否可编辑
//								System.out.println(Wf_Field_Name_Map.get(id));
//								System.out.println(id+"	"+Wf_Field_Table_Map.get(id)+"	"+m_fieldname+"	"+fieldvalue+"	"+fieldname+"	"+fielddbtype+"	"+fieldtype+"	"+fieldhtmltype);
							}
						}
						wrtri[j] = new WorkflowRequestTableRecord();
						wrtri[j].setWorkflowRequestTableFields(wrti);
					}
				}
			}
			wdti[i] = new WorkflowDetailTableInfo();
			wdti[i].setTableDBName(detailtable);
			wdti[i].setWorkflowRequestTableRecords(wrtri);//加入明细表1的数据
		}

		WorkflowBaseInfo wbi = new WorkflowBaseInfo();
		wbi.setWorkflowId(String.valueOf(modeid));

		WorkflowRequestInfo wri = new WorkflowRequestInfo();//流程基本信息
		wri.setCreatorId(String.valueOf(creater));//创建人id
		wri.setRequestLevel(requestlevel);//0 正常，1重要，2紧急
		wri.setRequestName(requestname);//流程标题
		wri.setWorkflowMainTableInfo(wmi);//添加主字段数据
		wri.setWorkflowBaseInfo(wbi);
		wri.setWorkflowDetailTableInfos(wdti);

		return wri;
	}

	public String createMode(RequestInfo request,int wtlid) throws Exception{
		return createMode(request,null,wtlid);
	}

	/**
	 * 创建模块数据
	 * @param request
	 * @param formmodeid
	 * @return
	 * @throws Exception
	 */
	public String createMode(RequestInfo request,Map basedfieldMap,int wtlid) throws Exception {
		RecordSet rs = new RecordSet();
        RecordSet rs2 = new RecordSet();
		InterfacesUtil InterfacesUtil = new InterfacesUtil();
		int formmodeid = Util.getIntValue(request.getWorkflowid());
		WorkflowBillComInfo wbci = new WorkflowBillComInfo();
		String billtablename = wbci.getTablename(String.valueOf(m_formid));
		int modecreater = Util.getIntValue(request.getCreatorid());
		int modecreatertype = 0;
		String currentdate = TimeUtil.getCurrentDateString();
		String currenttime = TimeUtil.getOnlyCurrentTimeString();
		String isbill = String.valueOf(m_isbill);
		int seclevel = Util.getIntValue(request.getSecLevel(),0);

		String secValidityWorkflow = request.getSecValidity();


		int billid = ModeDataIdUpdate.getModeDataNewIdByUUID(billtablename,formmodeid,modecreater,modecreatertype,currentdate,currenttime, null);
		createBillMap.put(formmodeid+"_"+billid, modecreater);
		//主表处理
		String sql1 = "";
		String sql2 = "";
		WorkFlowToModeLogService  workFlowToModeLogService = new WorkFlowToModeLogService();
		if (billid < 1){
			execStatus="2";
			workFlowToModeLogService.updateSuccesslogByWtlid(wtlid,execStatus,execError+"<br/>"+execSeparator+"<br/>{389398}<br/>"+execSeparator+"<br/><span style=\"color:red; word-wrap: break-word;\">{389399}</span>","-1");
			return "-3";//fail to create requestid
		}
		billidList.add(""+billid);
		String sql0 = "update " + billtablename + " set ";
		sql1 = "";
		sql2 = " where id=" + billid;
		MainTableInfo minfo = request.getMainTableInfo();
		rs.executeSql("select * from workflow_billfield where viewtype=0 and billid=" + m_formid);

		HashMap filenameMap = new HashMap();
		HashMap filevalueMap = new HashMap();
		//长字段值不能直接拼接sql，包括html文本、多选
		List<Object> clobValueList = new ArrayList<Object>();
		while (rs.next()) {
			String fieldid = rs.getString("id");
			String fieldname = rs.getString("fieldname");
			String fieldhtmltype = Util.null2String(rs.getString("fieldhtmltype"));
			String fieldtype = Util.null2String(rs.getString("type"));
			String fielddbtype=Util.null2String(rs.getString("fielddbtype"));
			boolean issinglebrowser =  false;
			if("3".equals(fieldhtmltype)&&!FormModeBrowserUtil.isMultiBrowser(fieldhtmltype, fieldtype)){
				issinglebrowser = true;
			}

			List pl = getPropertyByName(minfo, fieldname);
			Iterator iter = pl.iterator();
			if(defaultValueMap.containsKey(fieldid)){
				String defaultValue = Util.null2String(defaultValueMap.get(fieldid));
				String splitFlag = "".equals(sql1)?"":",";
				if("".equals(defaultValue) || !"3".equals(fieldhtmltype))continue;
				defaultValue=CubeCipherUitl.getFieldValue(fieldid, defaultValue, billtablename, fieldname, fielddbtype, fieldtype, fieldhtmltype);
				if("17".equals(fieldtype) || "57".equals(fieldtype) || "194".equals(fieldtype)){//多选
					sql1 += splitFlag + fieldname + "=?";
					clobValueList.add(defaultValue);
				}else{
					sql1 += splitFlag + fieldname + "=?";
					clobValueList.add(defaultValue);
				}
			}else if(iter.hasNext()){
				Property p = (Property) iter.next();
				String wf_fieldid = Util.null2String(mf_wf_map.get(fieldid));
				String fieldvalue = p.getValue();
				if(basedfieldMap!=null){
					String basedfieldid = Util.null2String(basedfieldMap.get("fieldid"));
					if(wf_fieldid.equals(basedfieldid)){
						String ismultibrowser = Util.null2String(basedfieldMap.get("ismultibrowser"));
						//如果依据字段为整数 或者 依据字段为多选、模块字段为单选，则使用传递过来的单个值
						if(!Boolean.valueOf(ismultibrowser) || issinglebrowser){
							fieldvalue = Util.null2String(basedfieldMap.get("fieldvalue"));
						}
					}
				}
				p.setValue(InterfacesUtil.getSubStringValue(fielddbtype.toLowerCase(),fieldvalue));
				fielddbtype = fielddbtype.toUpperCase();
				String splitFlag = "".equals(sql1)?"":",";
				fieldvalue = Util.null2String(p.getValue());
				fieldvalue=CubeCipherUitl.getFieldValue(fieldid, fieldvalue, billtablename, fieldname, fielddbtype, fieldtype, fieldhtmltype);
				if("2".equals(fieldhtmltype)||FormModeBrowserUtil.isMultiBrowser(fieldhtmltype,fieldtype)){
					sql1 += splitFlag + fieldname + "=?";
					//英文双引号转换
					fieldvalue = fieldvalue.replace("&quot;","\"").replace("&lt;","<");
					clobValueList.add(fieldvalue);
				}else{
					if (fielddbtype.indexOf("INT")>=0||fielddbtype.indexOf("NUMBER") >= 0||fielddbtype.indexOf("DECIMAL") >= 0||fielddbtype.indexOf("FLOAT") >= 0){
						//如果字段值为空，则置为null，防止sql报错
						if("".equals(fieldvalue)){
							fieldvalue="null";
						}
						sql1 += splitFlag + fieldname + "=?";
						clobValueList.add(fieldvalue);
					} else {
						//对特殊符号进行转义处理
						fieldvalue = parseSpecialChar(rs.getDBType(),fieldvalue);
						sql1 += splitFlag + fieldname + "=?";
						clobValueList.add(fieldvalue);
					}
				}
                if ("3".equals(fieldhtmltype)&&"17".equals(fieldtype)&&!fieldvalue.isEmpty()) {//多人力资源选择组织架构时要特殊处理
                    String getWfBrowExinfSql="select type,typeid,ids  from workflow_reqbrowextrainfo where requestid=? and fieldid=?";
                    rs2.executeQuery(getWfBrowExinfSql,requestid,wf_fieldid);
                    List<String> fieldgroupList = new ArrayList<String>();
                    while (rs2.next()){
                        fieldgroupList.add(rs2.getString("type")+"|"+rs2.getString("typeid")+"|"+rs2.getString("ids"));
                    }
                    if(fieldgroupList.size()>0){
                        handleBrowserGroupInfo(billid+"",formmodeid,fieldid,fieldgroupList,fieldvalue);
                    }
                }
            }
        }
		Object[] clobObjects = new Object[clobValueList.size()];
		for (int i=0;i<clobValueList.size();i++) {
			clobObjects[i] = clobValueList.get(i);
		}
		RecordSet rs1 = new RecordSet();
		boolean flag = rs1.executeSql(sql0 + sql1 + sql2,false,clobObjects);
		writeLog("主字段:"+sql0 + sql1 + sql2);
		String trueSql =getSql(sql0 + sql1 + sql2,clobObjects);  //获取替换？后的主表sql
		if (!flag){
			execStatus="2";
			updateModebyDetail_SuccessfulLog.add("<br/><span style=\"color:red;word-wrap: break-word;\">"+trueSql+"</span>");
			return "-4";//fail to create main table
		}else{
			updateModebyDetail_SuccessfulLog.add("<br/><span style=\"word-wrap: break-word;\">"+trueSql+"</span>");
		}
		DetailTableInfo dti = request.getDetailTableInfo();
		if (dti == null){
			return "" + billid;
		}
		DetailTable[] dts = dti.getDetailTable();

		String detailKeyfield = wbci.getDetailkeyfield(String.valueOf(m_formid));
		if (detailKeyfield.equals("")){
			detailKeyfield = "mainid";
		}
		RecordSet rs0 = new RecordSet();
		String sql = "select tablename as detailtablename from workflow_billdetailtable where billid=" + m_formid + " order by orderid";
		rs0.executeSql(sql);
		int groupcount = rs0.getCounts();
		boolean isold = false;//是否老式单据(单明细)
		boolean nodetail = false;  //无明细
		if (groupcount == 0) {
			isold = true;
			sql = "select detailtablename from workflow_bill where id=" + m_formid;
			rs0.executeSql(sql);
			groupcount = rs0.getCounts();
			rs0.next();
			String detailtablename = rs0.getString("detailtablename");
			if (detailtablename.equals("")){
				nodetail = true;
			}
		}
		ArrayList<String> detailtables = new ArrayList<String>();
		if (nodetail) {
			//do nothing
		} else {
			ArrayList detailtablelist = new ArrayList();
			rs0.beforFirst();
			while (rs0.next()) {
				detailtablelist.add(rs0.getString("detailtablename"));
			}
			String main_dt_success_msg="";
			for (int i = 0; i < dts.length; i++) {
				DetailTable dt = dts[i];
				int tableOrder = Util.getIntValue(dt.getId());

				if (tableOrder < 1)
					continue;
				try {
					String detailTablename = (String) detailtablelist.get(tableOrder - 1);
					detailtables.add(detailTablename);
					//get detailtable field
					if (isold)
						sql = "select * from workflow_billfield where billid=" + m_formid + " and viewtype='1' ";
					else
						sql = "select * from workflow_billfield where billid=" + m_formid + " and viewtype='1' and detailtable='" + detailTablename + "'";

					rs0.executeSql(sql);
					Row[] rows = dt.getRow();

					for (int j = 0; j < rows.length; j++) {
						String dt_parame="";
						String dt_parame1="";
						//长字段值不能直接拼接sql，包括html文本、多选
						List<Object> detailClobValueList = new ArrayList<Object>();

						Row row = rows[j];
						sql1 = "insert into " + detailTablename + "(" + detailKeyfield;
						sql2 = " values("  + billid;
						rs0.beforFirst();
						while (rs0.next()) {
							String fieldid = rs0.getString("id");
							String fieldname = rs0.getString("fieldname");
							String fieldhtmltype = Util.null2String(rs0.getString("fieldhtmltype"));
							String fieldtype = Util.null2String(rs0.getString("type"));
							Cell c = getCellByName(row, fieldname);
							if (c != null) {
								sql1 = sql1 + "," + fieldname;
								String fielddbtype=Util.null2String(rs0.getString("fielddbtype"));
								String wf_fieldid = Util.null2String(mf_wf_map.get(fieldid));
								String fieldvalue = c.getValue();
								if(defaultValueMap.containsKey(fieldid)){
									String defaultValue = Util.null2String(defaultValueMap.get(fieldid));
									fieldvalue=defaultValue;
								}
								fieldvalue=CubeCipherUitl.getFieldValue(fieldid, fieldvalue, detailTablename, fieldname, fielddbtype, fieldtype, fieldhtmltype);
								c.setValue(InterfacesUtil.getSubStringValue(fielddbtype.toLowerCase(),fieldvalue));
								fielddbtype = fielddbtype.toUpperCase();
								fieldvalue = Util.null2String(c.getValue());
								if("2".equals(fieldhtmltype)||FormModeBrowserUtil.isMultiBrowser(fieldhtmltype, fieldtype)){
									sql2 += ",?";
									//英文双引号转换
									fieldvalue = fieldvalue.replace("&quot;","\"").replace("&lt;","<");
									detailClobValueList.add(fieldvalue);
								}else{
									if (fielddbtype.indexOf("INT")>=0|| fielddbtype.indexOf("NUMBER") >= 0||fielddbtype.indexOf("DECIMAL") >= 0||fielddbtype.indexOf("FLOAT") >= 0){
										//如果字段值为空，则置为null，防止sql报错
										if("".equals(fieldvalue)){
											fieldvalue="null";
										}
										sql2 +=  ",?";
										detailClobValueList.add(fieldvalue);
									}else{
										//对特殊符号进行转义处理
										fieldvalue = parseSpecialChar(rs.getDBType(),fieldvalue);
										sql2 +=  ",?";
										detailClobValueList.add(fieldvalue);
									}
								}
							}
						}
						sql1 = sql1 + ")";
						sql2 = sql2 + ")";
						Object[] detailClobObjects = new Object[detailClobValueList.size()];
						for(int k=0;k<detailClobValueList.size();k++){
							detailClobObjects[k]=detailClobValueList.get(k);
						}
						boolean flag_dt=  rs1.executeSql(sql1 + sql2,false,detailClobObjects);
						String true_dt_Sql =getSql(sql1 + sql2,detailClobObjects);  //获取替换？后的明细sql
						if(!flag_dt){
							execStatus="2";
							true_dt_Sql ="<br/><span style=\"color:red;word-wrap: break-word;\">"+true_dt_Sql+"</span>";  //获取替换？后的主表sql
							updateModebyDetail_SuccessfulLog.add(true_dt_Sql);  //统一放一个list，失败的sql标红
							writeLog("明细"+(i+1)+":"+sql1 + sql2);
						}else{
							updateModebyDetail_SuccessfulLog.add("<br/><span style=\"word-wrap: break-word;\">"+true_dt_Sql+"</span>");
						}
					}
				} catch (Exception e) {
					writeLog(e);
					execStatus="2";
					updateModebyDetail_SuccessfulLog.add("<br/><span style=\"word-wrap: break-word;\">{513560}</span><br/>");
				}

			}
		}
		//待数据生成后，再进行编码
		boolean existCode = isExistCode(formmodeid);
		if (existCode) {
			CodeBuilder cbuild = new CodeBuilder(Util.getIntValue(formmodeid));
			cbuild.getModeCodeStr(m_formid, billid);//生成编号
		}
		//如果有草稿状态，那么流程转数据直接设置为0正式的
		ModeSetUtil modeSetUtil = new ModeSetUtil();
		modeSetUtil.setModedatastatusValue(rs,formmodeid,billid,billtablename,0);
		//添加共享
		if(billid>0){
			//verifyData.addVerify(m_formid, billid, billtablename, detailtables, wf_user.getLanguage(),opt);
			HashMap<String, Object> map = new HashMap<String, Object>();
			FormInfoService formInfoService = new FormInfoService();
			List<Map<String, Object>> needlogFieldList = formInfoService.getNeedlogField(m_formid);
			Map<String, Object> oldData = getLogFieldData(billtablename, billid, needlogFieldList);
			map.put("sql", " select id, modedatacreater, formmodeid, 1 flag, '"+m_formid+"' formid  from "+billtablename+" where id = '"+billid+"'");
			map.put("olddata", oldData);
			Map<String, Map<String, Map<String, Object>>> oldData_detail = new HashMap<String, Map<String,Map<String,Object>>>();//新增的话无明细历史数据
			map.put("oldData_detail", oldData_detail);
			this.rightArray.add(map);
			//添加置顶计算
			String formId = m_formid + "";
			SaveConditionUtil.buildTopDataFlowToModeCreateOrUpdate(billid,billtablename,formId,formmodeid);
			//判断密级字段，新建的时候按照流程那边的数据密级计算
			Map<String,Object> params = new HashMap<>();
			params.put("billid",billid);
			params.put("billtablename",billtablename);
			params.put("formId",formId);
			params.put("formmodeid",formmodeid);
			params.put("seclevel",seclevel);
			params.put("modesecrettime",secValidityWorkflow);

			AddSeclevelUtil addSeclevelUtil = new AddSeclevelUtil();
			addSeclevelUtil.updateSeclevelForWorkflowToMode(params);

		}

		return "" + billid;
	}

    /*
     * 处理主表 多人力字段
     * */
    public void handleBrowserGroupInfo(String  billid,int modeid,String fieldid,List<String> fieldgroupList,String fieldvalue){
        RecordSet rs = new RecordSet();
        boolean haveRepeat=false;
        //先删除
        String sql = "delete from mode_browsergroupinfo where billid=? and modeid=? and fieldid=?";
        rs.executeUpdate(sql, billid,modeid,fieldid);
        if (fieldvalue.indexOf(",")>0) {//重复数据去重
            String[] split = fieldvalue.split(",");
            List<String> list = Arrays.asList(split);
            list = list.stream().distinct().collect(Collectors.toList());
            haveRepeat=list.size()!=split.length;
            fieldvalue= StringUtils.join(list,",");
        }
        if (haveRepeat) {//如果有重复数据,不分组
            return;
        }
        if (fieldgroupList != null && fieldgroupList.size() > 0) {
            for (int i = 0; i < fieldgroupList.size(); i++) {
                String fieldgroupstr = fieldgroupList.get(i);
                String[] grouparry = fieldgroupstr.split("\\|");
                if (grouparry.length < 3) {
                    continue;
                }
                if ("9".equals(grouparry[0])) {//所有人
                    grouparry[1]="-1";
                }
                sql="insert into mode_browsergroupinfo(billid,modeid,fieldid,type,typeid,ids) values(?,?,?,?,?,?)";
                rs.executeUpdate(sql, billid,modeid,fieldid,grouparry[0],grouparry[1],grouparry[2]);
            }
            //将原始的fieldvalue存储下来,防止在别的地方修改了表单值后造成分组显示错误 对应的type为-1
            sql="insert into mode_browsergroupinfo(billid,modeid,fieldid,type,typeid,ids) values(?,?,?,?,?,?)";
            rs.executeUpdate(sql, billid,modeid,fieldid,-1,null,fieldvalue);
        }

    }
	/**
	 * 更新模块数据
	 * @param request
	 * @param formmodeid
	 * @return
	 * @throws Exception
	 */
	public List<String> updateMode(RequestInfo request,int wtlid,RequestInfo request_wf) throws Exception {
		RecordSet rs = new RecordSet();
        RecordSet rstemp = new RecordSet();
		InterfacesUtil InterfacesUtil = new InterfacesUtil();
		int formmodeid = Util.getIntValue(request.getWorkflowid());
		WorkflowBillComInfo wbci = new WorkflowBillComInfo();
		String billtablename = wbci.getTablename(String.valueOf(m_formid));
		int modecreater = Util.getIntValue(request.getCreatorid());
		int modecreatertype = 0;
		String currentdate = TimeUtil.getCurrentDateString();
		String currenttime = TimeUtil.getOnlyCurrentTimeString();
		String isbill = String.valueOf(m_isbill);
		List<String> mainbillids = new ArrayList<String>();//更新主表数据ID
		WorkFlowToModeLogService   workFlowToModeLogService = new WorkFlowToModeLogService();
		//主表处理
		String sql1 = "";
		String sql2 = "";
		if(!"1".equals(isbill))return mainbillids;
		//单据
		String sql0 = "update " + billtablename + " set ";
		sql1 = "";
		writeLog("==WorkflowToMode.updateMode()==主表更新条件maintableupdatecondition==>"+maintableupdatecondition);
		if("".equals(maintableupdatecondition)){
			workFlowToModeLogService.updateSuccesslogByWtlid(wtlid,execStatus,executeSuccess+"<br/>"+execSeparator+"<br/>{389404}<br/>"+execSeparator+"<br/><span word-wrap: break-word;\">{389405}</span>","");
			return mainbillids;
		}else{
			if(maintableupdatecondition.toLowerCase().startsWith("where ")){
				sql2 = " "+maintableupdatecondition;
			}else{
				sql2 = " where "+maintableupdatecondition + " and formmodeid =" + formmodeid;
			}
		}
//		for(String fieldname :wf_mtuc_list){
//			String value = maintableupdateconditionMap.get(fieldname);
//			updateConditionValueList.add(value);
//		}
		MainTableInfo minfo = request.getMainTableInfo();
		rs.executeSql("select * from workflow_billfield where viewtype=0 and billid=" + m_formid);

        String mainbillSql = "select id from "+billtablename+sql2;
        this.writeLog("需要更新的主表数据："+mainbillSql);
        RecordSet rs2 = new RecordSet();
        boolean selFlag = rs2.executeQuery(mainbillSql);
//		Object[] updateConditionValueObjects = new Object[updateConditionValueList.size()];
//		for (int i=0;i<updateConditionValueList.size();i++) {
//			updateConditionValueObjects[i] = updateConditionValueList.get(i);
//		}
        if(!selFlag) { //改造，更新不了的时候，如果动作是插入并更新，则执行插入操作
            execStatus="2";
            workFlowToModeLogService.updateSuccesslogByWtlid(wtlid,execStatus,execError+"<br/>"+execSeparator+"<br/>{389406}<br/>"+execSeparator+"<br/>{389407}:<span style=\"color:red;word-wrap: break-word;\">"+getSql(mainbillSql)+"</span>","");
            if ("4".equals(maintableopttype)) {
                this.add_update_id = createMode(request,wtlid);
            }
            return mainbillids;
        }
        while(rs2.next()){
            String id = rs2.getString("id");
            mainbillids.add(id);
            billidList.add(id);
            this.add_update_id = id;
        }
		HashMap filenameMap = new HashMap();
		HashMap filevalueMap = new HashMap();
		//长字段值不能直接拼接sql，包括html文本、多选
		List<Object> clobValueList = new ArrayList<Object>();
		boolean ismodifypromptfield = false;
        List<String> fieldgroupList = new ArrayList<String>();//存储多人力字段的相关信息
		while (rs.next()) {
			String fieldid = rs.getString("id");
			String fieldname = rs.getString("fieldname");
			String fieldhtmltype = Util.null2String(rs.getString("fieldhtmltype"));
			String fieldtype = Util.null2String(rs.getString("type"));
			String fielddbtype=Util.null2String(rs.getString("fielddbtype"));
			List pl = getPropertyByName(minfo, fieldname);
			Iterator iter = pl.iterator();
			if(defaultValueMap.containsKey(fieldid)){
				String defaultValue = Util.null2String(defaultValueMap.get(fieldid));
				String splitFlag = "".equals(sql1)?"":",";
				if("".equals(defaultValue) || !"3".equals(fieldhtmltype))continue;
				defaultValue=CubeCipherUitl.getFieldValue(fieldid, defaultValue, billtablename, fieldname, fielddbtype, fieldtype, fieldhtmltype);
				if("17".equals(fieldtype) || "57".equals(fieldtype) || "194".equals(fieldtype)){//多选
					sql1 += splitFlag + fieldname + "=?";
					clobValueList.add(defaultValue);
				}else{
					sql1 += splitFlag + fieldname + "=?";
					clobValueList.add(defaultValue);
				}
			}else if(iter.hasNext()){
                Property p = (Property) iter.next();
                String wf_fieldid = Util.null2String(mf_wf_map.get(fieldid));
                String htmltype = Util.null2String(rs.getString("fieldhtmltype"));
                String type = Util.null2String(rs.getString("type"));
                p.setValue(InterfacesUtil.getSubStringValue(fielddbtype.toLowerCase(),p.getValue()));
                fielddbtype = fielddbtype.toUpperCase();
                String splitFlag = "".equals(sql1)?"":",";
                String fieldvalue = Util.null2String(p.getValue());
                fieldvalue=CubeCipherUitl.getFieldValue(fieldid, fieldvalue, billtablename, fieldname, fielddbtype, fieldtype, fieldhtmltype);
                if("2".equals(htmltype)||FormModeBrowserUtil.isMultiBrowser(htmltype,type)){
                    sql1 += splitFlag + fieldname + "=?";
                    clobValueList.add(fieldvalue);
                }else{
                    if (fielddbtype.indexOf("INT")>=0||fielddbtype.indexOf("NUMBER") >= 0||fielddbtype.indexOf("DECIMAL") >= 0||fielddbtype.indexOf("FLOAT") >= 0){
                        //如果字段值为空，则置为null，防止sql报错
                        if("".equals(fieldvalue)){
                            fieldvalue="null";
                        }
                        sql1 += splitFlag + fieldname + "=?";
                        clobValueList.add(fieldvalue);
                    } else {
                        //对特殊符号进行转义处理
                        fieldvalue = parseSpecialChar(rs.getDBType(),fieldvalue);
                        sql1 += splitFlag + fieldname + "=?";
                        clobValueList.add(fieldvalue);
                    }
                }
                if ("3".equals(fieldhtmltype)&&"17".equals(fieldtype)&&!fieldvalue.isEmpty()&&mainbillids.size()>0) {//多人力资源选择组织架构时要特殊处理
                    String getWfBrowExinfSql="select type,typeid,ids  from workflow_reqbrowextrainfo where requestid=? and fieldid=?";
                    rstemp.executeQuery(getWfBrowExinfSql,requestid,wf_fieldid);
                    while (rstemp.next()){
                        fieldgroupList.add(rstemp.getString("type")+"|"+rstemp.getString("typeid")+"|"+rstemp.getString("ids"));
                    }
                    for(int i=0;i<mainbillids.size();i++){
                        if(fieldgroupList.size()>0){
                            handleBrowserGroupInfo(mainbillids.get(i),formmodeid,fieldid,fieldgroupList,fieldvalue);
                        }
                    }

                }
            }
        }




		if(mainbillids.size()==0) {//如果没有更新到主表数据，则直接返回，无需处理明细表数据；改造，更新不了的时候，如果动作是插入并更新，则执行插入操作
			workFlowToModeLogService.updateSuccesslogByWtlid(wtlid,execStatus,executeSuccess+"<br/>"+execSeparator+"<br/>{389408}<br/>"+execSeparator+"<br/><span style=\"word-wrap: break-word;\">{389409}:"+getSql(mainbillSql)+"</span>","");
			if ("4".equals(maintableopttype)||"1".equals(isinsert)) {
				this.add_update_id = createMode(request,wtlid);
			}
			return mainbillids;
		}
		String billids = "";
		for (int a = 0; a < mainbillids.size(); a++) {
			if (billids.equals("")) {
				billids = mainbillids.get(a);
			} else {
				billids += "," + mainbillids.get(a);
			}
		}
		if ("4".equals(maintableopttype) && mainbillids.size() > 1) { //大于一个的时候流程给提示
			statusflag = Action.FAILURE_AND_CONTINUE;
			messagecontent = SystemEnv.getHtmlLabelName(127797,weaver.general.Util.threadVarLanguage());//流程转数据为插入并更新时，存在多条相同数据，不允许更新 ，流程提交失败！
			execStatus="2";
			workFlowToModeLogService.updateSuccesslogByWtlid(wtlid,execStatus,execError+"<br/>"+execSeparator+"<br/>{389406}<br/>"+execSeparator+"<br/>{389410}:<span style=\"color:red;word-wrap: break-word;\">"+getSql(mainbillSql)+"</span>",billids);
			return mainbillids;
		}
		Object[] clobObjects = new Object[clobValueList.size()];
		for (int i=0;i<clobValueList.size();i++) {
			clobObjects[i] = clobValueList.get(i);
		}
//		for(int i = 0;i<updateConditionValueList.size();i++){
//			clobObjects[i+clobValueList.size()] = updateConditionValueList.get(i);
//		}
		//--------------权限重构------------------------
		FormInfoService formInfoService = new FormInfoService();
		List<Map<String, Object>> needlogFieldList = formInfoService.getNeedlogField(m_formid);
		for(int i=0;i<mainbillids.size();i++){
			int mainbillid = Util.getIntValue(mainbillids.get(i));
			//verifyData.addVerify(m_formid, mainbillid, billtablename, detailtablelist, wf_user.getLanguage(),opt);
			Map<String,Object> oldData = getLogFieldData(billtablename, mainbillid, needlogFieldList);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("sql", " select id, modedatacreater, formmodeid, 0 flag, '"+m_formid+"' formid  from "+billtablename+" where id = '"+mainbillid+"'");
			map.put("olddata", oldData);
			Map<String, Map<String, Map<String, Object>>> oldData_detail = getLogFieldData_detail(mainbillid, needlogFieldList);
			map.put("oldData_detail", oldData_detail);
			map.put("tablename", billtablename);
			this.rightArray.add(map);
		}

		boolean flag= rs2.executeSql(sql0 + sql1 + sql2,false,clobObjects);
		String trueSql =getSql(sql0 + sql1 + sql2,clobObjects);  //获取替换？后的主表sql
		String  errmsg=execError+"<br/>"+execSeparator+"<br/>{389406}<br/>"+execSeparator+"<br/>{389389}：<br/>";
		String  successMsg=executeSuccess+"<br/>"+execSeparator+"<br/>{389404}<br/>"+execSeparator+"<br/>{389394}：<br/>";
		if(!flag){
			execStatus="2";
			updateModebyDetail_SuccessfulLog.add("<br/><span style=\"color:red;word-wrap: break-word;\">"+trueSql+"</span>");//错误和正确信息统一放一个list
		}else{
			updateModebyDetail_SuccessfulLog.add("<br/><span style=\"word-wrap: break-word;\">"+trueSql+"</span>");
			String formId = m_formid +"";
			//添加置顶逻辑
			for(int i=0;i<mainbillids.size();i++){
				int mainbillid = Util.getIntValue(mainbillids.get(i));
				SaveConditionUtil.buildTopDataFlowToModeCreateOrUpdate(mainbillid,billtablename,formId,formmodeid);
			}
		}
		writeLog("主字段:"+sql0 + sql1 + sql2);
		//明细表处理
		//单据
		//DetailTableInfo dti = request_wf.getDetailTableInfo();
		DetailTableInfo dti = request.getDetailTableInfo();
		DetailTable[] dts = null;
		if (dti == null) {

		} else {
			dts = dti.getDetailTable();
		}

		String detailKeyfield = wbci.getDetailkeyfield(String.valueOf(m_formid));
		if (detailKeyfield.equals("")){
			detailKeyfield = "mainid";
		}
		RecordSet rs0 = new RecordSet();
		RecordSet rs1 = new RecordSet();
		String sql = "select tablename as detailtablename from workflow_billdetailtable where billid=" + m_formid + " order by orderid";
		rs0.executeSql(sql);
		int groupcount = rs0.getCounts();
		boolean isold = false;//是否老式单据(单明细)
		boolean nodetail = false;  //无明细
		if (groupcount == 0) {
			isold = true;
			sql = "select detailtablename from workflow_bill where id=" + m_formid;
			rs0.executeSql(sql);
			groupcount = rs0.getCounts();
			rs0.next();
			String detailtablename = rs0.getString("detailtablename");
			if (detailtablename.equals("")){
				nodetail = true;
			}
		}
		//if(nodetail)return;

		ArrayList detailtablelist = new ArrayList();
		rs0.beforFirst();
		while (rs0.next()) {
			if(StringHelper.isEmpty(rs0.getString("detailtablename")))continue;
			detailtablelist.add(rs0.getString("detailtablename"));
		}
		String main_dt_success_msg = "";
		String dt_msg="";//明细表错误信息
		for (int i = 0; !nodetail && dts != null && i < dts.length; i++) {
			DetailTable dt = dts[i];
			int tableOrder = Util.getIntValue(dt.getId());

			if (tableOrder < 1)
				continue;
			try {
				String detailTablename = (String) detailtablelist.get(tableOrder - 1);
				String detailtableopttype = "1";
				String detailtableupdatecondition = "";
				String wDetailTableName = "";
				List<String> wDetailFieldNameList = new ArrayList<String>();//子表更新条件中流程明细字段
				boolean isencrypt = false;// 插入并更新情况下，明细id对应字段是否开启了加密
				if(detailtableoptMap.containsKey(detailTablename)){
					Map<String,Object> optMap = (Map<String,Object>)detailtableoptMap.get(detailTablename);
					detailtableopttype = Util.null2String(optMap.get("opttype"));
					detailtableupdatecondition = Util.null2String(optMap.get("updatecondition"));
					wDetailTableName = Util.null2String(optMap.get("wdetailtablename"));
					wDetailFieldNameList = (List<String>)optMap.get("wdetailfieldnamelist");
				}

				if ("4".equals(maintableopttype)) { //如果为插入并更新，则条件重新给过，执行明细操作默认为更新（追加）
					detailtableopttype = "4";

					//判断明细id 的对应字段是否加密
					RecordSet rSet = new RecordSet();
					rSet.executeQuery("select a.id,a.billid from workflow_billfield a  left join mode_workflowtomodesetdetail b on a.id=b.modefieldid where b.mainid=? and b.wffieldid = ?", curSetid,-(3+tableOrder));
					if(rSet.next()){
						String modefieldid = Util.null2String(rSet.getString("id"));
						String formid = Util.null2String(rSet.getString("billid"));
						rSet.executeQuery("select 1 from ModeFormFieldEncrypt where formid = ? and fieldid=?", formid,modefieldid);
						if(rSet.next()){
							isencrypt = true;
						}
					}
				}
				if("1".equals(detailtableopttype))continue;//如果操作类型为默认，则不做任何处理
				if(("3".equals(detailtableopttype)||"4".equals(detailtableopttype))&&"".equals(detailtableupdatecondition))continue;//如果为更新操作，而且更新条件为空，则不做处理

				//get detailtable field
				if (isold)
					sql = "select * from workflow_billfield where billid=" + m_formid + " and viewtype='1' ";
				else
					sql = "select * from workflow_billfield where billid=" + m_formid + " and viewtype='1' and detailtable='" + detailTablename + "'";

				rs0.executeSql(sql);
				Row[] rows = dt.getRow();

				String detailtableoptSql_all="";
				for(int k=0;k<mainbillids.size();k++){
					String mainbillid = mainbillids.get(k);
					String mainidCondiction="";
					if("5".equals(detailtableopttype)){
						String delDetailTableSql = "delete from "+detailTablename+" where mainid="+mainbillid;
						rs2.executeSql(delDetailTableSql);//先删除对应的子表数据，再进行插入
						updateModebyDetail_SuccessfulLog.add("<br/><span style=\"word-wrap: break-word;\">"+delDetailTableSql+"</span>");
					}
					if("3".equals(detailtableopttype)||"4".equals(detailtableopttype)){
						mainidCondiction=" and mainid = "+mainbillid;//添加主表关联字段
					}
					for (int j = 0; j < rows.length; j++) {

						//长字段值不能直接拼接sql，包括html文本、多选
						List<Object> detailClobValueList = new ArrayList<Object>();
						Row row = rows[j];
						String detailtableSql1 = "";
						String detailtableSql2 = "";
						String udpateconditionSql = detailtableupdatecondition+mainidCondiction;//更新条件原始模型
						List<String> dtconditionfield = getFieldNames(udpateconditionSql);
						//长字段值不能直接拼接sql，包括html文本、多选
						//List<Object> detailconditionfieldValueList = new ArrayList<Object>();
						int upcount = 0;
						if("2".equals(detailtableopttype)||"5".equals(detailtableopttype)){//如果为追加、覆盖
							detailtableSql1 = "insert into " + detailTablename + "(" + detailKeyfield;
							detailtableSql2 = " values("  + mainbillid;
						}else if("3".equals(detailtableopttype)||"4".equals(detailtableopttype)){//如果为更新、更新(追加)
							if(!"".equals(wDetailTableName)){
								ArrayList<HashMap> wRowsList = (ArrayList)detailFieldValuesMaps.get(wDetailTableName.toLowerCase());
								HashMap wRow = wRowsList.get(j);
								udpateconditionSql=udpateconditionSql.toLowerCase();
								for(int m=0;m<wDetailFieldNameList.size();m++){
									String wDetailFieldName = wDetailFieldNameList.get(m);
									String wDetailFieldValue = Util.null2String(wRow.get(wDetailFieldName.toLowerCase()));
									if(!wDetailFieldName.trim().equals("")){
										udpateconditionSql=udpateconditionSql.replaceAll(("\\$"+wDetailTableName+"\\."+wDetailFieldName+"\\$").toLowerCase(), wDetailFieldValue.replace("\'","''"));
									}
								}
//								for(String dtfieldString : dtconditionfield){
//									if(dtfieldString.indexOf(".")>-1){
//										String[] wdtFieldArr = dtfieldString.split("\\.");
//										//String mucDetailTableNameTmp = wdtFieldArr[0];
//										String mucDetailFieldName = wdtFieldArr[1];
//										String wDetailFieldValue = Util.null2String(wRow.get(mucDetailFieldName.toLowerCase()));
//										if("id".equalsIgnoreCase(mucDetailFieldName)&&isencrypt){
//											wDetailFieldValue = CubeCipherUitl.encrypt(wDetailFieldValue);
//										}
//										//detailconditionfieldValueList.add(wDetailFieldValue);
//									}
//								}
							}
							String selSql = "select count(1) as upcount from "+detailTablename+ udpateconditionSql;
							this.writeLog("需要更新的明细数据："+selSql);
							boolean selResult = rs2.executeQuery(selSql);
							if(!selResult){
								execStatus="2";
								updateModebyDetail_SuccessfulLog.add("<br/>{389411}:<span style=\"color:red;word-wrap: break-word;\">"+getSql(selSql)+"</span><br/>");
								continue;
							}
							if(rs2.next()){
								upcount = Util.getIntValue(rs2.getString("upcount"));
								if(upcount==0){
									if("3".equals(detailtableopttype))continue;//如果为更新，但是没有满足更新条件子表数据，则无需处理
								}
							}

							if(upcount==0){//此条件表示，操作类型为更新(追加)中的追加，没有满足条件的子表数据，则追加进去
								detailtableSql1 = "insert into " + detailTablename + "(" + detailKeyfield;
								detailtableSql2 = " values("  + mainbillid;
							}else{
								detailtableSql1 = "update " + detailTablename + " set ";
								detailtableSql2 = "";
							}
						}

						rs0.beforFirst();
						RecordSet rsSet = new RecordSet();
						while (rs0.next()) {
							String fieldid = rs0.getString("id");
							String fieldname = rs0.getString("fieldname");
							//取该明细字段对应的流程字段，如果没有设置，不用处理
							String wf_fieldid = Util.null2String((String)mf_wf_map.get(fieldid));
							if("".equals(wf_fieldid))continue;
							String wf_fieldname = "";
							rsSet.executeQuery("select fieldname from workflow_billfield where id=?", wf_fieldid);
							if(rsSet.next()){
								wf_fieldname = rsSet.getString("fieldname");
							}
							//Cell c = getCellByName(row, wf_fieldname);
							Cell c = getCellByName(row, fieldname);
							if(Util.getIntValue(wf_fieldid)<-3&&Util.getIntValue(row.getId())>0&&"".equals(c.getValue())){//明细id
								c = new Cell();
								c.setName("id");
								c.setValue(row.getId());
							}
							if (c != null) {
								if("2".equals(detailtableopttype)||"5".equals(detailtableopttype)||upcount==0){
									detailtableSql1 = detailtableSql1 + "," + fieldname;
								}

								String fielddbtype=Util.null2String(rs0.getString("fielddbtype"));
								c.setValue(InterfacesUtil.getSubStringValue(fielddbtype.toLowerCase(),c.getValue()));
								fielddbtype = fielddbtype.toUpperCase();
								String fieldhtmltype = Util.null2String(rs0.getString("fieldhtmltype"));
								String fieldtype = Util.null2String(rs0.getString("type"));
								String fieldvalue = Util.null2String(c.getValue());
								//明细表默认值
								if(defaultValueMap.containsKey(fieldid)){
									String defaultValue = Util.null2String(defaultValueMap.get(fieldid));
									if("".equals(defaultValue) )continue;
									fieldvalue = defaultValue;
								}
								fieldvalue=CubeCipherUitl.getFieldValue(fieldid, fieldvalue, detailTablename, fieldname, fielddbtype, fieldtype, fieldhtmltype);
								String splitFlag = "".equals(detailtableSql2)?"":",";
								if("2".equals(fieldhtmltype)||FormModeBrowserUtil.isMultiBrowser(fieldhtmltype, fieldtype)){
									if("2".equals(detailtableopttype)||"5".equals(detailtableopttype)||upcount==0){
										detailtableSql2 += ",?";
									}else if("3".equals(detailtableopttype)||"4".equals(detailtableopttype)){
										detailtableSql2 += splitFlag + fieldname + "=?";
									}
									//英文双引号转换
									fieldvalue = fieldvalue.replace("&quot;","\"").replace("&lt;","<");
									detailClobValueList.add(fieldvalue);
								}else{
									if (fielddbtype.indexOf("INT")>=0||fielddbtype.indexOf("NUMBER") >= 0||fielddbtype.indexOf("DECIMAL") >= 0||fielddbtype.indexOf("FLOAT") >= 0){
										//如果字段值为空，则置为null，防止sql报错
										if("".equals(fieldvalue)){
											fieldvalue="null";
										}
										if("2".equals(detailtableopttype)||"5".equals(detailtableopttype)||upcount==0){
											detailtableSql2 += ",?";
											detailClobValueList.add(fieldvalue);
										}else if("3".equals(detailtableopttype)||"4".equals(detailtableopttype)){
											detailtableSql2 += splitFlag + fieldname + "=?";
											detailClobValueList.add(fieldvalue);
										}
									}else{
										fieldvalue = parseSpecialChar(rs.getDBType(),fieldvalue);
										if("2".equals(detailtableopttype)||"5".equals(detailtableopttype)||upcount==0){
											detailtableSql2 += ",?";
											detailClobValueList.add(fieldvalue);
										}else if("3".equals(detailtableopttype)||"4".equals(detailtableopttype)){
											detailtableSql2 += splitFlag + fieldname + "=?";
											detailClobValueList.add(fieldvalue);
										}
									}
								}
							}
						}
						Object[] detailClobObjects ;
						if(upcount==0){
							detailClobObjects = new Object[detailClobValueList.size()];
							for(int m=0;m<detailClobValueList.size();m++){
								detailClobObjects[m]=detailClobValueList.get(m);
							}
						}else{
							detailClobObjects = new Object[detailClobValueList.size()];
							for(int m=0;m<detailClobValueList.size();m++){
								detailClobObjects[m]=detailClobValueList.get(m);
							}
//							for(int n = 0;n<detailconditionfieldValueList.size();n++){
//								detailClobObjects[detailClobValueList.size()+n]=detailconditionfieldValueList.get(n);
//							}
						}
						if("2".equals(detailtableopttype)||"5".equals(detailtableopttype)||upcount==0){
							detailtableSql1 = detailtableSql1 + ")";
							detailtableSql2 = detailtableSql2 + ")";
							String detailtableoptSql = detailtableSql1 + detailtableSql2;
							boolean flag_dt=rs2.executeSql(detailtableoptSql,false,detailClobObjects);
							String  dt_insert_sql=getSql(detailtableoptSql, detailClobObjects);
							if(!flag_dt){
								execStatus="2";
								updateModebyDetail_SuccessfulLog.add("<br/><span style=\"color:red;word-wrap: break-word;\">"+dt_insert_sql+"</span>");
							}else{
								updateModebyDetail_SuccessfulLog.add("<br/><span style=\"word-wrap: break-word;\">"+dt_insert_sql+"</span>");
							}
							writeLog("明细:"+detailtableoptSql);
						}else if("3".equals(detailtableopttype)||"4".equals(detailtableopttype)){
							String detailtableoptSql = detailtableSql1 + detailtableSql2 + udpateconditionSql;
							boolean	flag_dt=rs2.executeSql(detailtableoptSql,false,detailClobObjects);
							String  dt_update_sql=getSql(detailtableoptSql, detailClobObjects);
							if(!flag_dt){
								execStatus="2";
								updateModebyDetail_SuccessfulLog.add("<br/><span style=\"color:red;word-wrap: break-word;\">"+dt_update_sql+"</span>");
							}else{
								updateModebyDetail_SuccessfulLog.add("<br/><span style=\"word-wrap: break-word;\">"+dt_update_sql+"</span>");
							}
							writeLog("明细:"+detailtableoptSql);
						}
					}
				}

			} catch (Exception e) {
				writeLog(e);
				execStatus="2";
				updateModebyDetail_SuccessfulLog.add("<br/><span style=\"word-wrap: break-word;\">{513560}</span><br/>");
			}
		}
		for (int k = 0; k < updateModebyDetail_SuccessfulLog.size(); k++) {
			main_dt_success_msg+=updateModebyDetail_SuccessfulLog.get(k);
		}

		String trueMsg="";
		if(this.execStatus.equals("2")){
			trueMsg=errmsg+main_dt_success_msg;
		}else{
			trueMsg=successMsg+main_dt_success_msg;
		}
		workFlowToModeLogService.updateSuccesslogByWtlid(wtlid,execStatus ,trueMsg,billids);

		Operate opt = new Operate();
		opt.modeid = ""+formmodeid;
		opt.type = this.workflowtomodename;
		opt.workflowtomodeset = this.curSetid;

		return mainbillids;
	}
	public String createModebyDetail(RequestInfo request,HashMap map,int wtlid) throws Exception {
		return this.createModebyDetail(request, map, null,wtlid);
	}
	/**
	 * 根据流程数据创建子表数据
	 * @param request
	 * @param formmodeid
	 * @return
	 * @throws Exception
	 */
	public String createModebyDetail(RequestInfo request,HashMap map, Map basedfieldMap,int wtlid) throws Exception {
		RecordSet rs = new RecordSet();
		InterfacesUtil InterfacesUtil = new InterfacesUtil();
		int formmodeid = Util.getIntValue(request.getWorkflowid());
		WorkflowBillComInfo wbci = new WorkflowBillComInfo();
		String billtablename = wbci.getTablename(String.valueOf(m_formid));
		int modecreater = Util.getIntValue(request.getCreatorid());
		int modecreatertype = 0;
		String currentdate = TimeUtil.getCurrentDateString();
		String currenttime = TimeUtil.getOnlyCurrentTimeString();
		String isbill = String.valueOf(m_isbill);

		int billid = ModeDataIdUpdate.getModeDataNewIdByUUID(billtablename,formmodeid,modecreater,modecreatertype,currentdate,currenttime);
		createBillMap.put(formmodeid+"_"+billid, modecreater);

		//主表处理
		String sql1 = "";
		String sql2 = "";
		if (!isbill.equals("1")) {

		} else {//单据
			if (billid < 1){
				execStatus="2"; //失败
				updateModebyDetail_SuccessfulLog.add("<br/><span style=\"color:red;word-wrap: break-word;\">{389399}</span>");//错误和正确信息统一放一个list
				return "-3";//fail to create requestid
			}
			billidList.add(""+billid);

			if(getFieldAignExpNum(this.curSetid)>0){//存在条件

				//触发表单为主表，先检测触发条件，为明细时，针对每一条明细进行检测
				Map param = new HashMap();
				//需要判断此行数据是否满足条件
				int detailid=0;
				if(map.containsKey("id")){
					detailid = Util.getIntValue(Util.null2String(map.get("id")));
				}
				param.put("requestid", request.getRequestid());
				param.put("detailid", detailid);
				param.put("formtype", formtype);

				field_assign_exp = new HashMap<String,Boolean>();
				fieldMap =new HashMap<String,Map<String,Object>>();
				//循环执行条件表达式
				checkCondition(curSetid,param);
				if(!field_assign_exp.isEmpty()){
					for(String mainid:field_assign_exp.keySet()){
						//this.field_newValue=getFieldExpInfo(id);
						//获取当前满足条件的 字段赋值信息
						fieldMap = getFieldInfo(mainid);
					}
				}
			}
			String sql0 = "update " + billtablename + " set ";
			sql1 = "";
			sql2 = " where id=" + billid;
			MainTableInfo minfo = request.getMainTableInfo();
			rs.executeSql("select * from workflow_billfield where viewtype=0 and billid=" + m_formid);

			HashMap filenameMap = new HashMap();
			HashMap filevalueMap = new HashMap();
			List<Map> list = new ArrayList<Map>();
			//长字段值不能直接拼接sql，包括html文本、多选
			List<Object> clobValueList = new ArrayList<Object>();
			while (rs.next()) {
				Map<String, String> newmap = new HashMap<String, String>();
				String fieldname = rs.getString("fieldname");
				String id = rs.getString("id");
				String fielddbtype=Util.null2String(rs.getString("fielddbtype"));
				String fieldhtmltype = Util.null2String(rs.getString("fieldhtmltype"));
				String fieldtype = Util.null2String(rs.getString("type"));
				newmap.put("fieldname", fieldname);
				newmap.put("id", id);
				newmap.put("fielddbtype", fielddbtype);
				newmap.put("fieldhtmltype", fieldhtmltype);
				newmap.put("fieldtype",fieldtype);
				list.add(newmap);
			}

			for (Map map2 : list) {
				String fieldname = Util.null2String(map2.get("fieldname"));
				String fieldhtmltype = Util.null2String(map2.get("fieldhtmltype"));
				String fieldtype = Util.null2String(map2.get("fieldtype"));
				String fielddbtype=Util.null2String(map2.get("fielddbtype"));
				String id = Util.null2String(map2.get("id"));
				String wffieldid = Util.null2String(mf_wf_map.get(id));
				String mf_fieldid = Util.null2String(map2.get("id"));//建模字段
				boolean issinglebrowser =  false;
				if("3".equals(fieldhtmltype)&&!FormModeBrowserUtil.isMultiBrowser(fieldhtmltype, fieldtype)){
					issinglebrowser = true;
				}
				if(defaultValueMap.containsKey(mf_fieldid)){
					String defaultValue = Util.null2String(defaultValueMap.get(mf_fieldid));
					String splitFlag = "".equals(sql1)?"":",";
					if("".equals(defaultValue) || !"3".equals(fieldhtmltype))continue;
					defaultValue=CubeCipherUitl.getFieldValue(mf_fieldid, defaultValue, billtablename, fieldname, fielddbtype, fieldtype, fieldhtmltype);
					if("17".equals(fieldtype) || "57".equals(fieldtype) || "194".equals(fieldtype)){//多选
						sql1 += splitFlag + fieldname + "=?";
						clobValueList.add(defaultValue);
					}else{
						sql1 += splitFlag + fieldname + "=?";
						clobValueList.add(defaultValue);
					}
				}else if(!"".equals(wffieldid)){
					String fieldvalue = "";
					String splitFlag = "".equals(sql1)?"":",";
					//==========处理明细的字段公式(此时是明细表插入, 公式只能作用于主表字段)
					if(fieldMap.containsKey(id)) {//此字段配置了字段赋值
						String sql = "select fieldname,detailtable from workflow_billfield where id =?";
						rs.executeQuery(sql,wffieldid);
						String detailtable = "";
						if(rs.next()){
							detailtable = Util.null2String(rs.getString("detailtable")).toLowerCase();
						}
						fieldvalue = processData("",fieldname,fieldMap.get(id), "main", fieldvalue,map,detailtable,fieldhtmltype,fieldtype);
					}else{
						String sql = "select fieldname,detailtable from workflow_billfield where id ="+wffieldid;
						rs.executeSql(sql);
						String workflowfieldname = "";
						String detailtable = "";
						if(rs.next()){
							workflowfieldname = Util.null2String(rs.getString("fieldname")).toLowerCase();
							detailtable = Util.null2String(rs.getString("detailtable")).toLowerCase();
						}else{
							workflowfieldname = wffieldid;
						}
						if(detailtable==null||"".equals(detailtable)){//主表字段
							if (-200 < Util.getIntValue(workflowfieldname, -2) && Util.getIntValue(workflowfieldname, -2) < -3) { //是明细ID的时候
								fieldvalue = Util.null2String(map.get("id"));
							} else {
								fieldvalue = Util.null2String(mainFieldValuesMap.get(workflowfieldname));
							}
						}else{
							fieldvalue = Util.null2String(map.get(workflowfieldname));
						}
						if(basedfieldMap!=null){
							String basedfieldid = Util.null2String(basedfieldMap.get("fieldid"));
							if(wffieldid.equals(basedfieldid)){
								String ismultibrowser = Util.null2String(basedfieldMap.get("ismultibrowser"));
								//如果依据字段为整数 或者 依据字段为多选、模块字段为单选，则使用传递过来的单个值
								if(!Boolean.valueOf(ismultibrowser) || issinglebrowser){
									fieldvalue = Util.null2String(basedfieldMap.get("fieldvalue"));
								}
							}
						}
						fieldvalue = (InterfacesUtil.getSubStringValue(fielddbtype.toLowerCase(),fieldvalue));
						fielddbtype = fielddbtype.toUpperCase();
						fieldvalue=CubeCipherUitl.getFieldValue(mf_fieldid, fieldvalue, billtablename, fieldname, fielddbtype, fieldtype, fieldhtmltype);

					}

					if("2".equals(fieldhtmltype)||FormModeBrowserUtil.isMultiBrowser(fieldhtmltype, fieldtype)){
						sql1 += splitFlag + fieldname + "=?";
						//英文双引号转换
						fieldvalue = fieldvalue.replace("&quot;","\"").replace("&lt;","<");
						clobValueList.add(fieldvalue);
					}else{
						if (fielddbtype.indexOf("INT")>=0||fielddbtype.indexOf("NUMBER") >= 0||fielddbtype.indexOf("DECIMAL") >= 0||fielddbtype.indexOf("FLOAT") >= 0){
							//如果字段值为空，则置为null，防止sql报错
							if("".equals(fieldvalue)){
								fieldvalue="null";
							}
							sql1 += splitFlag + fieldname + "=?";
							clobValueList.add(fieldvalue);
						} else {
							//对特殊符号进行转义处理
							fieldvalue = parseSpecialChar(rs.getDBType(),fieldvalue);
							sql1 += splitFlag + fieldname + "=?";
							clobValueList.add(fieldvalue);
						}
					}
				}
			}
			Object[] clobObjects = new Object[clobValueList.size()];
			for (int i=0;i<clobValueList.size();i++) {
				clobObjects[i] = clobValueList.get(i);
			}
			//公式存在错误,转数据就不需要执行
			if(conditionLog.size()>0){
				//写入公式日志
				writeConditionAssignmentLog(billid+"");
				return "" + billid;
			}

			boolean flag = rs.executeSql(sql0 + sql1 + sql2,false,clobObjects);
			String trueSql =getSql(sql0 + sql1 + sql2,clobObjects);  //获取替换？后的主表sql
			writeLog("主字段:"+sql0 + sql1 + sql2);
			if (!flag){
				execStatus="2";
				updateModebyDetail_SuccessfulLog.add("<br/><span style=\"color:red;word-wrap: break-word;\">"+trueSql+"</span>");//错误和正确信息统一放一个list
				return "-4";//fail to create main table
			}else{
				updateModebyDetail_SuccessfulLog.add("<br/><span style=\"word-wrap: break-word;\">"+trueSql+"</span>");//错误和正确信息统一放一个list

				try {
					//回写数据id的权限
					if(resetdataid>0&&!"3".equals(maintableopttype)&&billid>0) {// 此处只处理明细表的单选,多选 树形 单选,多选 (批量插入除外)
						RecordSet rs2 = new RecordSet();
						RecordSet rs3 = new RecordSet();
						//detailFieldValuesMaps
						String fieldname = Util.null2String((String) Wf_Field_Name_Map.get(resetdataid + ""));//流程字段名
						String m_fieldvalue = Util.null2String((String) map.get(fieldname.toLowerCase()));
						if ("".equals(m_fieldvalue)) {//流程该字段没写值
							if(!Wf_ManTableFieldIds.contains(resetdataid+"")){//明细字段
								int detailnum =Util.getIntValue(formtype.replace("detail",""))-1;
								if(detailnum>=0){
									String fieldtype = "";
									String fielddbtype = "";
									ArrayList DetailTableFieldIds = (ArrayList)Wf_DetailTableFieldIds.get(detailnum);
									ArrayList DetailFieldDBTypes = (ArrayList)Wf_DetailFieldDBTypes.get(detailnum);
									ArrayList DetailFieldTypes = (ArrayList)Wf_DetailFieldTypes.get(detailnum);
									ArrayList DetailTableFieldNames = (ArrayList)Wf_DetailDBFieldNames.get(detailnum);
									ArrayList DetailFieldHtmlTypes = (ArrayList)Wf_DetailFieldHtmlTypes.get(detailnum);
									for(int i=0;i<DetailTableFieldIds.size();i++){
										String _fieldname = (String)DetailTableFieldNames.get(i);
										if(_fieldname.toLowerCase().equals(fieldname.toLowerCase())){
											fieldtype = (String)DetailFieldTypes.get(i);
											if("256".equals(fieldtype) || "257".equals(fieldtype)){
												String _fielddbtype = (String)DetailFieldDBTypes.get(i);
												rs2.executeSql("select id from mode_customtreedetail where mainid='"+_fielddbtype+"' and sourceid="+formmodeid);
												if(rs2.next()){
													fielddbtype = rs2.getString("id");
												}
											}else{
												fielddbtype = (String)DetailFieldDBTypes.get(i);
											}
											break;
										}
									}
									String v_billid = "";
									if("256".equals(fieldtype) || "257".equals(fieldtype)){ //树形
										v_billid = fielddbtype+"_"+billid;
									}else{
										//浏览框主键值
										String keyValue = getKeyFieldValue(fielddbtype,billid+"");
										//v_billid = billid+"";
										v_billid = keyValue;
									}
									int detailId = Util.getIntValue((String)map.get("id"));
									//1.回写数据
									String _sql = "update "+tablename+"_dt"+(detailnum+1)+" set "+fieldname+"='"+v_billid+"' where id='"+detailId+"'";
									rs3.executeSql(_sql);
								}
							}
						}
					}
				} catch (Exception e) {
					writeLog(e);
				}
			}
		}
		//待数据生成后，再进行编码
		boolean existCode = isExistCode(formmodeid);
		if (existCode) {
			CodeBuilder cbuild = new CodeBuilder(Util.getIntValue(formmodeid));
			cbuild.getModeCodeStr(m_formid, billid);//生成编号
		}
		//如果有草稿状态，那么流程转数据直接设置为0正式的
		ModeSetUtil modeSetUtil = new ModeSetUtil();
		modeSetUtil.setModedatastatusValue(rs,formmodeid,billid,billtablename,0);
		writeLog("m_billid:"+billid);
		//添加共享
		if(billid>0){
			//String detailmodeid = Util.null2String(rowMap.get("id"));
			//String insertSql = "insert into workflowtomodelog(typename,modeid,billid,mainid,workflowid) values('"+formtype+"',"+detailmodeid+","+billid+","+id+","+formmodeid+")";
			//rsdetail.executeSql(insertSql);
			//获取插入前，需要记录日志字段值
			FormInfoService formInfoService =  new FormInfoService();
			List<Map<String, Object>> needlogFieldList = formInfoService.getNeedlogField(m_formid);
			Map<String,Object> oldData = getLogFieldData(billtablename, billid, needlogFieldList);

			Operate opt = new Operate();
			opt.modeid = ""+formmodeid;
			opt.type = this.workflowtomodename;
			opt.workflowtomodeset = this.curSetid;
			//verifyData.addVerify(m_formid, billid, billtablename, new ArrayList(), wf_user.getLanguage(),opt);
			HashMap<String, Object> tmpmap = new HashMap<String, Object>();
			tmpmap.put("sql", " select id, formmodeid, modedatacreater,'"+m_formid+"' formid, '1' flag from "+billtablename+" where id = "+billid);
			tmpmap.put("olddata", oldData);
			Map<String, Map<String, Map<String, Object>>> oldData_detail = new HashMap<String, Map<String,Map<String,Object>>>();//新增的话无明细历史数据;
			tmpmap.put("oldData_detail", oldData_detail);
			this.rightArray.add(tmpmap);
			//明细表判断密级字段
			Map<String,Object> params = new HashMap<>();
			params.put("billid",billid);
			params.put("billtablename",billtablename);
			String formId = m_formid + "";
			params.put("formId",formId);
			params.put("formmodeid",formmodeid);
			int seclevel = Util.getIntValue(request.getSecLevel(),0);
			params.put("seclevel",seclevel);
			String secValidityWorkflow = request.getSecValidity();
			params.put("modesecrettime",secValidityWorkflow);

			AddSeclevelUtil addSeclevelUtil = new AddSeclevelUtil();
			addSeclevelUtil.updateSeclevelForWorkflowToMode(params);
		}

		return "" + billid;
	}


	public String getKeyFieldValue(String fielddbtype,String billid){
		String value = billid;//直接传空，下边如果没有改动过主键，会导致取不到数据，回写不到
		String showname=fielddbtype.replace("browser.","");
		RecordSet rs = new RecordSet();
		String sql = "select a.fieldid,d.tablename,c.detailtable,c.fieldname from mode_CustombrowserDspField a left join mode_browser b on a.customid=b.customid " +
				"left join workflow_billfield c on a.fieldid=c.id left join workflow_bill d on c.billid=d.id" +
				" where a.ispk=1 and b.showname=?";
		rs.executeQuery(sql,showname);
		if(rs.next()){
			String tablename = rs.getString("tablename");
			String detailtable = rs.getString("detailtable");
			String fieldname = rs.getString("fieldname");
			if(detailtable.equals("")){
				sql = "select "+fieldname+" from "+tablename+" where id=?";
			}else{
				sql = "select "+fieldname+" from "+detailtable+" where mainid=?";
			}
			rs.executeQuery(sql,billid);
			if(rs.next()){
				value = rs.getString(fieldname);
			}

		}
		return value;
	}

	/**
	 * 根据流程数据更新子表数据
	 * @param request
	 * @param
	 * @return
	 * @throws Exception
	 */
	public void updateModebyDetail(RequestInfo request,HashMap map,int wtlid) throws Exception {
		RecordSet rs = new RecordSet();
		RecordSet rs1 = new RecordSet();
		InterfacesUtil InterfacesUtil = new InterfacesUtil();
		int formmodeid = Util.getIntValue(request.getWorkflowid());
		WorkflowBillComInfo wbci = new WorkflowBillComInfo();
		String billtablename = wbci.getTablename(String.valueOf(m_formid));
		int modecreater = Util.getIntValue(request.getCreatorid());
		int modecreatertype = 0;
		String currentdate = TimeUtil.getCurrentDateString();
		String currenttime = TimeUtil.getOnlyCurrentTimeString();
		String isbill = String.valueOf(m_isbill);
		List<String> mainbillids = new ArrayList<String>();
		WorkFlowToModeLogService workFlowToModeLogService = new WorkFlowToModeLogService();
		//主表处理
		String sql1 = "";
		String sql2 = "";
		if (!isbill.equals("1")) {

		} else {//单据
			String sql0 = "update " + billtablename + " set ";
			sql1 = "";
			if("".equals(maintableupdatecondition)){
				return ;
			}else{
				if(maintableupdatecondition.toLowerCase().startsWith("where ")){
					sql2 = " "+maintableupdatecondition;
				}else{
					sql2 = " where "+maintableupdatecondition + " and formmodeid =" + formmodeid;
				}
			}
			String mucDetailTableName = Util.null2String(mucDetailTableMap.get("wdetailtablename"));
			if(!"".equals(mucDetailTableName)){
				List<String> mucDetailFieldNameList = (List<String>)mucDetailTableMap.get("wdetailfieldnamelist");
				if(mucDetailFieldNameList!=null&&mucDetailFieldNameList.size()>0){
					for(int i=0;i<mucDetailFieldNameList.size();i++){
						String wDetailFieldName = mucDetailFieldNameList.get(i);
						String wDetailFieldValue = Util.null2String(map.get(wDetailFieldName.toLowerCase()));
						if(!wDetailFieldValue.trim().equals("")){
							wDetailFieldValue = wDetailFieldValue.replace("\'", "''");
							wDetailFieldValue = wDetailFieldValue.replace("\\", "\\\\");
							sql2=sql2.replaceAll("\\$"+mucDetailTableName+"\\."+wDetailFieldName+"\\$", wDetailFieldValue);
						} else {
							//如果操作类型为更新，并且启用条件不满足时插入
							if("2".equals(maintableopttype)||"1".equals(isinsert)) {
								sql2=sql2.replaceAll("\\$"+mucDetailTableName+"\\."+wDetailFieldName+"\\$", wDetailFieldValue);
							}
						}
					}
				}
			}
			//updateConditionValueList.clear();
//			for(String fieldname :wf_mtuc_list){
//				if(fieldname.indexOf(".")>-1){
//					String[] wMainFieldArr = fieldname.split("\\.");
//					String mucDetailTableNameTmp = wMainFieldArr[0];
//					String mucDetailFieldName = wMainFieldArr[1];
//					String wDetailFieldValue = Util.null2String(map.get(mucDetailFieldName.toLowerCase()));
//					//updateConditionValueList.add(wDetailFieldValue);
//				}else{
//					String value = maintableupdateconditionMap.get(fieldname);
//					//updateConditionValueList.add(value);
//				}
//			}
			String reqStr = "WorkflowToMode.updateModebyDetail()===requestid:"+request.getRequestid()+"===";
			String mainbillSql = "select id from "+billtablename + sql2;
			boolean selFlag = rs1.executeQuery(mainbillSql);
//			Object[] updateConditionValueObjects = new Object[updateConditionValueList.size()];
//			for (int i=0;i<updateConditionValueList.size();i++) {
//				updateConditionValueObjects[i] = updateConditionValueList.get(i);
//			}
			String mainErrmsg="";
			String mainSuccessmsg="";
			String paramemsg="";
			if(!selFlag) { //改造，更新不了的时候，如果动作是插入并更新，则执行插入操作
				this.writeLog(reqStr+"插入并更新，更新语句存在语法错误，执行插入动作,maintableopttype:"+maintableopttype+"==mainbillSql=="+getSql(mainbillSql));
				if ("4".equals(maintableopttype)) {
					createModebyDetail(request, map,wtlid);
				}else{
					execStatus="2";
					updateModebyDetail_SuccessfulLog.add("<br/>{389414}:<span style=\" color:red;word-wrap: break-word;\">"+getSql(mainbillSql)+"</span><br/>");//错误和正确信息统一放一个list
				}
				return;
			}
			while(rs1.next()){
				String id = rs1.getString("id");
				mainbillids.add(id);
			}
			String billids = "";
			for (int a = 0; a < mainbillids.size(); a++) {
				if (billids.equals("")) {
					billids = mainbillids.get(a);
				} else {
					billids += "," + mainbillids.get(a);
				}
			}
			billidList.add(billids);
			if(mainbillids.size()==0) {//如果没有满足更新条件的数据，则直接返回；改造，更新不了的时候，如果动作是插入并更新，则执行插入操作
				this.writeLog(reqStr+"插入并更新，SQL语句未找到要更新的记录，执行插入动作,maintableopttype:"+maintableopttype+"错误sql:"+getSql(mainbillSql));
				if ("4".equals(maintableopttype)||"1".equals(isinsert)) {
					createModebyDetail(request, map,wtlid);
				}else{
					execStatus="2";
					updateModebyDetail_SuccessfulLog.add("<br/>{389415}:<span style=\"color:red;word-wrap: break-word;\">"+getSql(mainbillSql)+"</span><br/>");//错误和正确信息统一放一个list
				}
				return;
			}

			if ("4".equals(maintableopttype) && mainbillids.size() > 1) { //大于一个的时候流程给提示
				execStatus="2";
				updateModebyDetail_SuccessfulLog.add("<br/>{389416}:<span style=\"color:red;word-wrap: break-word;\">"+getSql(mainbillSql)+"</span><br/>");//错误和正确信息统一放一个list
				this.writeLog(reqStr+"插入并更新，SQL语句查找到多条数据，不允许更，执行返回,maintableopttype:"+maintableopttype+"==mainbillSql=="+getSql(mainbillSql));
				//statusflag = Action.FAILURE_AND_CONTINUE;
				//messagecontent = "流程转数据为插入并更新时，存在多条相同数据，不允许更新 ，流程提交失败！";
				return;
			}


			if(getFieldAignExpNum(this.curSetid)>0){//存在条件

				//触发表单为主表，先检测触发条件，为明细时，针对每一条明细进行检测
				Map param = new HashMap();
				//需要判断此行数据是否满足条件
				int detailid=0;
				if(map.containsKey("id")){
					detailid = Util.getIntValue(Util.null2String(map.get("id")));
				}
				param.put("requestid", request.getRequestid());
				param.put("detailid", detailid);
				param.put("formtype", formtype);

				field_assign_exp = new HashMap<String,Boolean>();
				fieldMap =new HashMap<String,Map<String,Object>>();
				//循环执行条件表达式
				checkCondition(curSetid,param);
				if(!field_assign_exp.isEmpty()){
					for(String mainid:field_assign_exp.keySet()){
						//this.field_newValue=getFieldExpInfo(id);
						//获取当前满足条件的 字段赋值信息
						fieldMap = getFieldInfo(mainid);
					}
				}
			}


			MainTableInfo minfo = request.getMainTableInfo();
			rs.executeSql("select * from workflow_billfield where viewtype=0 and billid=" + m_formid);

			HashMap filenameMap = new HashMap();
			HashMap filevalueMap = new HashMap();
			List<Map> list = new ArrayList<Map>();
			//长字段值不能直接拼接sql，包括html文本、多选
			List<Object> clobValueList = new ArrayList<Object>();
			while (rs.next()) {
				Map<String, String> newmap = new HashMap<String, String>();
				String fieldname = rs.getString("fieldname");
				String id = rs.getString("id");
				String fielddbtype=Util.null2String(rs.getString("fielddbtype"));
				String fieldhtmltype = Util.null2String(rs.getString("fieldhtmltype"));
				String fieldtype = Util.null2String(rs.getString("type"));
				newmap.put("fieldname", fieldname);
				newmap.put("id", id);
				newmap.put("fielddbtype", fielddbtype);
				newmap.put("fieldhtmltype", fieldhtmltype);
				newmap.put("fieldtype",fieldtype);
				list.add(newmap);
			}

			for (Map map2 : list) {
				String fieldname = Util.null2String(map2.get("fieldname"));
				String fieldhtmltype = Util.null2String(map2.get("fieldhtmltype"));
				String fieldtype = Util.null2String(map2.get("fieldtype"));
				String fielddbtype=Util.null2String(map2.get("fielddbtype"));
				String id = Util.null2String(map2.get("id"));
				String wffieldid = Util.null2String(mf_wf_map.get(id));
				String mf_fieldid = Util.null2String(map2.get("id"));//建模字段
				if(defaultValueMap.containsKey(mf_fieldid)){
					String defaultValue = Util.null2String(defaultValueMap.get(mf_fieldid));
					String splitFlag = "".equals(sql1)?"":",";
					if("".equals(defaultValue) || !"3".equals(fieldhtmltype))continue;
					defaultValue=CubeCipherUitl.getFieldValue(mf_fieldid, defaultValue, billtablename, fieldname, fielddbtype, fieldtype, fieldhtmltype);
					if("17".equals(fieldtype) || "57".equals(fieldtype) || "194".equals(fieldtype)){//多选
						sql1 += splitFlag + fieldname + "=?";
						clobValueList.add(defaultValue);
					}else{
						sql1 += splitFlag + fieldname + "=?";
						clobValueList.add(defaultValue);
					}
				}else if(!"".equals(wffieldid)){
					String fieldvalue = "";
					String splitFlag = "".equals(sql1)?"":",";
					//==========处理明细的字段公式(viewtype=0 公式作用于主表字段)
					if(fieldMap.containsKey(id)) {//此字段配置了字段赋值
						String sql = "select fieldname,detailtable from workflow_billfield where id =?";
						rs.executeQuery(sql,wffieldid);
						String detailtable = "";
						if(rs.next()){
							detailtable = Util.null2String(rs.getString("detailtable")).toLowerCase();
						}
						fieldvalue = processData("",fieldname,fieldMap.get(id), "main", fieldvalue,map,detailtable,fieldhtmltype,fieldtype);
					}else{
						String sql = "select fieldname,detailtable from workflow_billfield where id ="+wffieldid;
						rs.executeSql(sql);
						String workflowfieldname = "";
						String detailtable = "";
						if(rs.next()){
							workflowfieldname = Util.null2String(rs.getString("fieldname")).toLowerCase();
							detailtable = Util.null2String(rs.getString("detailtable")).toLowerCase();
						}else{
							workflowfieldname = wffieldid;
						}
						if(detailtable==null||"".equals(detailtable)){//主表字段
							if (-200 < Util.getIntValue(workflowfieldname, -2) && Util.getIntValue(workflowfieldname, -2) < -3) { //是明细ID的时候
								fieldvalue = Util.null2String(map.get("id"));
							} else {
								fieldvalue = Util.null2String(mainFieldValuesMap.get(workflowfieldname));
							}

						}else{
							fieldvalue = Util.null2String(map.get(workflowfieldname));
						}
						fieldvalue = (InterfacesUtil.getSubStringValue(fielddbtype.toLowerCase(),fieldvalue));
						fieldvalue=CubeCipherUitl.getFieldValue(mf_fieldid, fieldvalue, billtablename, fieldname, fielddbtype, fieldtype, fieldhtmltype);
						fielddbtype = fielddbtype.toUpperCase();
					}
					if("2".equals(fieldhtmltype)||FormModeBrowserUtil.isMultiBrowser(fieldhtmltype, fieldtype)){
						sql1 += splitFlag + fieldname + "=?";
						//英文双引号转换
						fieldvalue = fieldvalue.replace("&quot;","\"").replace("&lt;","<");
						clobValueList.add(fieldvalue);
					}else{
						if (fielddbtype.indexOf("INT")>=0||fielddbtype.indexOf("NUMBER") >= 0||fielddbtype.indexOf("DECIMAL") >= 0||fielddbtype.indexOf("FLOAT") >= 0){
							//如果字段值为空，则置为null，防止sql报错
							if("".equals(fieldvalue)){
								fieldvalue="null";
							}
							sql1 += splitFlag + fieldname + "=?";
							clobValueList.add(fieldvalue);
						} else {
							//对特殊符号进行转义处理
							fieldvalue = parseSpecialChar(rs.getDBType(),fieldvalue);
							sql1 += splitFlag + fieldname + "=?";
							clobValueList.add(fieldvalue);
						}
					}

				}
			}
			//公式存在错误,转数据就不需要执行
			if(conditionLog.size()>0){
				//写入公式日志
				writeConditionAssignmentLog(billids);
				return ;
			}
			Object[] clobObjects = new Object[clobValueList.size()];
			for (int i=0;i<clobValueList.size();i++) {
				clobObjects[i] = clobValueList.get(i);
			}
//			for(int i = 0;i<updateConditionValueList.size();i++){
//				clobObjects[i+clobValueList.size()] = updateConditionValueList.get(i);
//			}
			FormInfoService formInfoService = new FormInfoService();
			List<Map<String, Object>> needlogFieldList = formInfoService.getNeedlogField(m_formid);
			Operate opt = new Operate();
			opt.modeid = ""+formmodeid;
			opt.type = this.workflowtomodename;
			opt.workflowtomodeset = this.curSetid;
			for(int i=0;i<mainbillids.size();i++){
				int mainbillid = Util.getIntValue(mainbillids.get(i));
				Map<String, Object> oldData = getLogFieldData(billtablename, mainbillid, needlogFieldList);
				HashMap<String, Object> tmpmap = new HashMap<String, Object>();
				tmpmap.put("sql", " select id, modedatacreater, formmodeid, 0 flag, '"+m_formid+"' formid  from "+billtablename+" where id = '"+mainbillid+"'");
				tmpmap.put("olddata", oldData);
				tmpmap.put("tablename", billtablename);
				Map<String, Map<String, Map<String, Object>>> oldData_detail = getLogFieldData_detail(mainbillid, needlogFieldList);
				tmpmap.put("oldData_detail", oldData_detail);
				this.rightArray.add(tmpmap);
			}


			boolean flag = rs1.executeSql(sql0 + sql1 + sql2,false,clobObjects);
			String trueSql=getSql(sql0 + sql1 + sql2, clobObjects);
			writeLog("主字段:"+sql0 + sql1 + sql2);
			if (!flag){
				execStatus="2";
				updateModebyDetail_SuccessfulLog.add("<br/><span style=\"color:red;word-wrap: break-word;\">"+trueSql+"</span>");
			}else{
				updateModebyDetail_SuccessfulLog.add("<br/><span style=\"word-wrap: break-word;\">"+trueSql+"</span>");


				try {
					//回写数据id的权限
					if(resetdataid>0&&!"".equals(billids)) {// 此处只处理明细表的单选,多选 树形 单选,多选 一条明细更新多条
						RecordSet rs2 = new RecordSet();
						RecordSet rs3 = new RecordSet();
						String fieldname = Util.null2String((String) Wf_Field_Name_Map.get(resetdataid + ""));//流程字段名
						String m_fieldvalue = Util.null2String((String) map.get(fieldname.toLowerCase()));
						if ("".equals(m_fieldvalue)) {//流程该字段没写值
							if(!Wf_ManTableFieldIds.contains(resetdataid+"")){//明细字段
								int detailnum =Util.getIntValue(formtype.replace("detail",""))-1;
								if(detailnum>=0){
									String fieldtype = "";
									String fielddbtype = "";
									ArrayList DetailTableFieldIds = (ArrayList)Wf_DetailTableFieldIds.get(detailnum);
									ArrayList DetailFieldDBTypes = (ArrayList)Wf_DetailFieldDBTypes.get(detailnum);
									ArrayList DetailFieldTypes = (ArrayList)Wf_DetailFieldTypes.get(detailnum);
									ArrayList DetailTableFieldNames = (ArrayList)Wf_DetailDBFieldNames.get(detailnum);
									ArrayList DetailFieldHtmlTypes = (ArrayList)Wf_DetailFieldHtmlTypes.get(detailnum);
									for(int i=0;i<DetailTableFieldIds.size();i++){
										String _fieldname = (String)DetailTableFieldNames.get(i);
										if(_fieldname.toLowerCase().equals(fieldname.toLowerCase())){
											fieldtype = (String)DetailFieldTypes.get(i);
											if("256".equals(fieldtype) || "257".equals(fieldtype)){
												String _fielddbtype = (String)DetailFieldDBTypes.get(i);
												rs2.executeSql("select id from mode_customtreedetail where mainid='"+_fielddbtype+"' and sourceid="+formmodeid);
												if(rs2.next()){
													fielddbtype = rs2.getString("id");
												}
											}else{
												fielddbtype =  (String)DetailFieldDBTypes.get(i);
											}
											break;
										}
									}


									String mainbillid = "";
									for(int i=0;i<mainbillids.size();i++){
										if("".equals(mainbillid)){
											if("256".equals(fieldtype) || "257".equals(fieldtype)){ //树形
												mainbillid = fielddbtype+"_"+mainbillids.get(i);
											}else{
												//获取主键值
												String keyValue = getKeyFieldValue(fielddbtype,mainbillids.get(i));
												mainbillid = keyValue+"";
											}
										}else{
											if("256".equals(fieldtype) || "257".equals(fieldtype)){ //树形
												mainbillid += ","+fielddbtype+"_"+mainbillids.get(i);
											}else{
												//获取主键值
												String keyValue = getKeyFieldValue(fielddbtype,mainbillids.get(i));
												mainbillid += ","+keyValue;
											}
										}
									}
									if(!"".equals(mainbillid)){
										int detailId = Util.getIntValue((String)map.get("id"));
										//1.回写数据
										String _sql = "update "+tablename+"_dt"+(detailnum+1)+" set "+fieldname+"='"+mainbillid+"' where id='"+detailId+"'";
										rs3.executeSql(_sql);
									}
								}
							}
						}
					}
				} catch (Exception e) {
					writeLog(e);
				}


			}

			//主表条件为更新，明细表可以选择流程主表字段，这里要添加明细表处理
			//明细表处理
			//单据
			DetailTableInfo dti = request.getDetailTableInfo();
			DetailTable[] dts = null;
			if (dti == null) {

			} else {
				dts = dti.getDetailTable();
			}

			String detailKeyfield = wbci.getDetailkeyfield(String.valueOf(m_formid));
			if (detailKeyfield.equals("")){
				detailKeyfield = "mainid";
			}
			RecordSet rs0 = new RecordSet();
			String sql = "select tablename as detailtablename from workflow_billdetailtable where billid=" + m_formid + " order by orderid";
			rs0.executeSql(sql);
			int groupcount = rs0.getCounts();
			boolean isold = false;//是否老式单据(单明细)
			boolean nodetail = false;  //无明细
			if (groupcount == 0) {
				isold = true;
				sql = "select detailtablename from workflow_bill where id=" + m_formid;
				rs0.executeSql(sql);
				groupcount = rs0.getCounts();
				rs0.next();
				String detailtablename = rs0.getString("detailtablename");
				if (detailtablename.equals("")){
					nodetail = true;
				}
			}
			//if(nodetail)return;

			ArrayList detailtablelist = new ArrayList();
			rs0.beforFirst();
			while (rs0.next()) {
				if(StringHelper.isEmpty(rs0.getString("detailtablename")))continue;
				detailtablelist.add(rs0.getString("detailtablename"));
			}
			String main_dt_success_msg = "";
			String dt_msg="";//明细表错误信息
			for (int i = 0; !nodetail && dts != null && i < dts.length; i++) {
				DetailTable dt = dts[i];
				int tableOrder = Util.getIntValue(dt.getId());

				if (tableOrder < 1)
					continue;
				try {
					String detailTablename = (String) detailtablelist.get(tableOrder - 1);
					String detailtableopttype = "1";
					String detailtableupdatecondition = "";
					String wDetailTableName = "";
					List<String> wDetailFieldNameList = new ArrayList<String>();//子表更新条件中流程明细字段

					if(detailtableoptMap.containsKey(detailTablename)){
						Map<String,Object> optMap = (Map<String,Object>)detailtableoptMap.get(detailTablename);
						detailtableopttype = Util.null2String(optMap.get("opttype"));
						detailtableupdatecondition = Util.null2String(optMap.get("updatecondition"));
						wDetailTableName = Util.null2String(optMap.get("wdetailtablename"));
						wDetailFieldNameList = (List<String>)optMap.get("wdetailfieldnamelist");
					}

					if ("4".equals(maintableopttype)) { //如果为插入并更新，则条件重新给过，执行明细操作默认为更新（追加）
						detailtableopttype = "4";
					}

					if("1".equals(detailtableopttype))continue;//如果操作类型为默认，则不做任何处理
					if(("3".equals(detailtableopttype)||"4".equals(detailtableopttype))&&"".equals(detailtableupdatecondition))continue;//如果为更新操作，而且更新条件为空，则不做处理

					//get detailtable field
					if (isold)
						sql = "select * from workflow_billfield where billid=" + m_formid + " and viewtype='1' ";
					else
						sql = "select * from workflow_billfield where billid=" + m_formid + " and viewtype='1' and detailtable='" + detailTablename + "'";

					rs0.executeSql(sql);
					Row[] rows = dt.getRow();

					String detailtableoptSql_all="";
					for(int k=0;k<mainbillids.size();k++){
						String mainbillid = mainbillids.get(k);
						String mainidCondiction="";
						if("5".equals(detailtableopttype)){
							String delDetailTableSql = "delete from "+detailTablename+" where mainid="+mainbillid;
							rs1.executeSql(delDetailTableSql);//先删除对应的子表数据，再进行插入
							updateModebyDetail_SuccessfulLog.add("<br/><span style=\"word-wrap: break-word;\">"+delDetailTableSql+"</span>");
						}
						if("3".equals(detailtableopttype)||"4".equals(detailtableopttype)){
							mainidCondiction=" and mainid = "+mainbillid;//添加主表关联字段
						}
						for (int j = 0; j < rows.length; j++) {

							//长字段值不能直接拼接sql，包括html文本、多选
							List<Object> detailClobValueList = new ArrayList<Object>();
							Row row = rows[j];
							String detailtableSql1 = "";
							String detailtableSql2 = "";
							String udpateconditionSql = detailtableupdatecondition+mainidCondiction;//更新条件原始模型
							List<String> dtconditionfield = getFieldNames(udpateconditionSql);
							//长字段值不能直接拼接sql，包括html文本、多选
							//List<Object> detailconditionfieldValueList = new ArrayList<Object>();
							int upcount = 0;
							if("2".equals(detailtableopttype)||"5".equals(detailtableopttype)){//如果为追加、覆盖
								detailtableSql1 = "insert into " + detailTablename + "(" + detailKeyfield;
								detailtableSql2 = " values("  + mainbillid;
							}else if("3".equals(detailtableopttype)||"4".equals(detailtableopttype)){//如果为更新、更新(追加)
								if(!"".equals(wDetailTableName)){
									ArrayList<HashMap> wRowsList = (ArrayList)detailFieldValuesMaps.get(wDetailTableName.toLowerCase());
									HashMap wRow = wRowsList.get(j);
									for(int m=0;m<wDetailFieldNameList.size();m++){
										String wDetailFieldName = wDetailFieldNameList.get(m);
										String wDetailFieldValue = Util.null2String(map.get(wDetailFieldName.toLowerCase()));
										if(!wDetailFieldValue.trim().equals("")){
											udpateconditionSql=udpateconditionSql.toLowerCase().replaceAll(("\\$"+wDetailTableName+"\\."+wDetailFieldName+"\\$").toLowerCase(), wDetailFieldValue.replace("\'","''"));
										}

									}
//									for(String dtfieldString : dtconditionfield){
//										if(dtfieldString.indexOf(".")>-1){
//											String[] wdtFieldArr = dtfieldString.split("\\.");
//											//String mucDetailTableNameTmp = wdtFieldArr[0];
//											String mucDetailFieldName = wdtFieldArr[1];
//											String wDetailFieldValue = Util.null2String(wRow.get(mucDetailFieldName.toLowerCase()));
//											detailconditionfieldValueList.add(wDetailFieldValue);
//										}
//									}
								}
								String selSql = "select count(1) as upcount from "+detailTablename+ udpateconditionSql;
								this.writeLog("需要更新的明细数据："+selSql);
								boolean selResult = rs1.executeQuery(selSql);
								if(!selResult){
									execStatus="2";
									updateModebyDetail_SuccessfulLog.add("<br/>{389411}:<span style=\"color:red;word-wrap: break-word;\">"+getSql(selSql)+"</span><br/>");
									continue;
								}
								if(rs1.next()){
									upcount = Util.getIntValue(rs1.getString("upcount"));
									if(upcount==0){
										if("3".equals(detailtableopttype))continue;//如果为更新，但是没有满足更新条件子表数据，则无需处理
									}
								}

								if(upcount==0){//此条件表示，操作类型为更新(追加)中的追加，没有满足条件的子表数据，则追加进去
									detailtableSql1 = "insert into " + detailTablename + "(" + detailKeyfield;
									detailtableSql2 = " values("  + mainbillid;
								}else{
									detailtableSql1 = "update " + detailTablename + " set ";
									detailtableSql2 = "";
								}
							}

							rs0.beforFirst();

							while (rs0.next()) {
								String fieldid = rs0.getString("id");
								String fieldname = rs0.getString("fieldname");
								//取该明细字段对应的流程字段，如果没有设置，不用处理
								String wf_fieldid = Util.null2String((String)mf_wf_map.get(fieldid));
								if("".equals(wf_fieldid))continue;

								Cell c = getCellByName(row, fieldname);
								if(Util.getIntValue(wf_fieldid)<-3&&Util.getIntValue(row.getId())>0){//明细id
									c = new Cell();
									c.setName("id");
									c.setValue(row.getId());
								}
								if (c != null) {
									if("2".equals(detailtableopttype)||"5".equals(detailtableopttype)||upcount==0){
										detailtableSql1 = detailtableSql1 + "," + fieldname;
									}

									String fielddbtype=Util.null2String(rs0.getString("fielddbtype"));
									c.setValue(InterfacesUtil.getSubStringValue(fielddbtype.toLowerCase(),c.getValue()));
									fielddbtype = fielddbtype.toUpperCase();
									String fieldhtmltype = Util.null2String(rs0.getString("fieldhtmltype"));
									String fieldtype = Util.null2String(rs0.getString("type"));
									String fieldvalue = Util.null2String(c.getValue());
									String splitFlag = "".equals(detailtableSql2)?"":",";
									boolean isFormula=false; // 是否开启公式赋值
									boolean isDefault=false; // 是否来自默认值赋值
									//明细表默认值
									if(defaultValueMap.containsKey(fieldid)){
										String defaultValue = Util.null2String(defaultValueMap.get(fieldid));
										if("".equals(defaultValue) )continue;
										fieldvalue = defaultValue;
										isDefault = true;
									}
									fieldvalue=CubeCipherUitl.getFieldValue(fieldid, fieldvalue, detailTablename, fieldname, fielddbtype, fieldtype, fieldhtmltype);
									if(fieldMap.containsKey(fieldid)){
										isFormula = true;// 此字段设置了公式
									}
									if(!isDefault&&isFormula){//非默认值(兼容老数据,老数据默认值那一列还是存在的)
										fieldvalue = processData("",fieldname,fieldMap.get(fieldid), "main", fieldvalue,map,"",fieldhtmltype,fieldtype);
										//公式存在错误,转数据就不需要执行
										if(conditionLog.size()>0){
											//写入公式日志
											writeConditionAssignmentLog(billids);
											return ;
										}
									}

									if("2".equals(fieldhtmltype)||FormModeBrowserUtil.isMultiBrowser(fieldhtmltype, fieldtype)){
										if("2".equals(detailtableopttype)||"5".equals(detailtableopttype)||upcount==0){
											detailtableSql2 += ",?";
										}else if("3".equals(detailtableopttype)||"4".equals(detailtableopttype)){
											detailtableSql2 += splitFlag + fieldname + "=?";
										}
										//英文双引号转换
										fieldvalue = fieldvalue.replace("&quot;","\"").replace("&lt;","<");
										detailClobValueList.add(fieldvalue);
									}else{
										if (fielddbtype.indexOf("INT")>=0||fielddbtype.indexOf("NUMBER") >= 0||fielddbtype.indexOf("DECIMAL") >= 0||fielddbtype.indexOf("FLOAT") >= 0){
											//如果字段值为空，则置为null，防止sql报错
											if("".equals(fieldvalue)){
												fieldvalue="null";
											}
											if("2".equals(detailtableopttype)||"5".equals(detailtableopttype)||upcount==0){
												detailtableSql2 += ",?";
											}else if("3".equals(detailtableopttype)||"4".equals(detailtableopttype)){
												detailtableSql2 += splitFlag + fieldname + "=?";
											}
											detailClobValueList.add(fieldvalue);
										}else{
											fieldvalue = parseSpecialChar(rs.getDBType(),fieldvalue);
											if("2".equals(detailtableopttype)||"5".equals(detailtableopttype)||upcount==0){
												detailtableSql2 += ",?";
											}else if("3".equals(detailtableopttype)||"4".equals(detailtableopttype)){
												detailtableSql2 += splitFlag + fieldname + "=?";
											}
											detailClobValueList.add(fieldvalue);
										}
									}
								}
							}
							Object[] detailClobObjects ;
							if(upcount==0){
								detailClobObjects = new Object[detailClobValueList.size()];
								for(int m=0;m<detailClobValueList.size();m++){
									detailClobObjects[m]=detailClobValueList.get(m);
								}
							}else{
								detailClobObjects = new Object[detailClobValueList.size()];
								for(int m=0;m<detailClobValueList.size();m++){
									detailClobObjects[m]=detailClobValueList.get(m);
								}
//								for(int n = 0;n<detailconditionfieldValueList.size();n++){
//									detailClobObjects[detailClobValueList.size()+n]=detailconditionfieldValueList.get(n);
//								}
							}
							if("2".equals(detailtableopttype)||"5".equals(detailtableopttype)||upcount==0){
								detailtableSql1 = detailtableSql1 + ")";
								detailtableSql2 = detailtableSql2 + ")";
								String detailtableoptSql = detailtableSql1 + detailtableSql2;
								boolean flag_dt=rs1.executeSql(detailtableoptSql,false,detailClobObjects);
								String  dt_insert_sql=getSql(detailtableoptSql, detailClobObjects);
								if(!flag_dt){
									execStatus="2";
									updateModebyDetail_SuccessfulLog.add("<br/><span style=\"color:red;word-wrap: break-word;\">"+dt_insert_sql+"</span>");
								}else{
									updateModebyDetail_SuccessfulLog.add("<br/><span style=\"word-wrap: break-word;\">"+dt_insert_sql+"</span>");
								}
								writeLog("明细:"+detailtableoptSql);
							}else if("3".equals(detailtableopttype)||"4".equals(detailtableopttype)){
								String detailtableoptSql = detailtableSql1 + detailtableSql2 + udpateconditionSql;
								boolean	flag_dt=rs1.executeSql(detailtableoptSql,false,detailClobObjects);
								String  dt_update_sql=getSql(detailtableoptSql, detailClobObjects);
								if(!flag_dt){
									execStatus="2";
									updateModebyDetail_SuccessfulLog.add("<br/><span style=\"color:red;word-wrap: break-word;\">"+dt_update_sql+"</span>");
								}else{
									updateModebyDetail_SuccessfulLog.add("<br/><span style=\"word-wrap: break-word;\">"+dt_update_sql+"</span>");
								}
								writeLog("明细:"+detailtableoptSql);
							}
						}
					}

				} catch (Exception e) {
					writeLog(e);
					execStatus="2";
					updateModebyDetail_SuccessfulLog.add("<br/><span style=\"word-wrap: break-word;\">{513560}</span><br/>");
				}
			}
			for (int k = 0; k < updateModebyDetail_SuccessfulLog.size(); k++) {
				main_dt_success_msg+=updateModebyDetail_SuccessfulLog.get(k);
			}
			String  errmsg=execError+"<br/>"+execSeparator+"<br/>{389406}<br/>"+execSeparator+"<br/>{389389}：<br/>";
			String  successMsg=executeSuccess+"<br/>"+execSeparator+"<br/>{389404}<br/>"+execSeparator+"<br/>{389394}：<br/>";
			String trueMsg="";
			if(this.execStatus.equals("2")){
				trueMsg=errmsg+main_dt_success_msg;
			}else{
				trueMsg=successMsg+main_dt_success_msg;
			}
			workFlowToModeLogService.updateSuccesslogByWtlid(wtlid,execStatus ,trueMsg,billids);

		}
	}

	/**
	 * 将WorkflowRequestInfo转换为RequestInfo
	 *
	 * @param WorkflowRequestInfo
	 * @return RequestInfo
	 */
	private RequestInfo toRequestInfo(WorkflowRequestInfo wri){
		if(wri==null) return null;

		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setRequestid(wri.getRequestId());
		requestInfo.setWorkflowid(wri.getWorkflowBaseInfo().getWorkflowId());
		requestInfo.setCreatorid(wri.getCreatorId());
		requestInfo.setDescription(wri.getRequestName());
		requestInfo.setRequestlevel(wri.getRequestLevel());
		requestInfo.setRemindtype(wri.getMessageType());

		MainTableInfo mainTableInfo = new MainTableInfo();
		List fields = new ArrayList();

		WorkflowMainTableInfo wmti = wri.getWorkflowMainTableInfo();
		if(wmti!=null) {
			WorkflowRequestTableRecord[] wrtrs = wmti.getRequestRecords();
			if(wrtrs!=null&&wrtrs[0]!=null) {
				for(int i=0;i<wrtrs[0].getWorkflowRequestTableFields().length;i++) {
					WorkflowRequestTableField wrtf = wrtrs[0].getWorkflowRequestTableFields()[i];
					if(wrtf!=null){
						if(wrtf.getFieldName()!=null&&!"".equals(wrtf.getFieldName())&&wrtf.isView()&&wrtf.isEdit()){
							Property field = new Property();
							field.setName(wrtf.getFieldName());
							field.setValue(wrtf.getFieldValue());
							fields.add(field);
						}
					}
				}
			}
		}
		Property[] fieldarray = (Property[]) fields.toArray(new Property[fields.size()]);
		mainTableInfo.setProperty(fieldarray);
		requestInfo.setMainTableInfo(mainTableInfo);

		DetailTableInfo detailTableInfo = new DetailTableInfo();
		WorkflowDetailTableInfo[] wdtis = wri.getWorkflowDetailTableInfos();

		List detailTables = new ArrayList();

		for(int i=0;wdtis!=null&&i<wdtis.length;i++){
			DetailTable detailTable = new DetailTable();
			detailTable.setId((i+1)+"");

			WorkflowDetailTableInfo wdti = wdtis[i];

			WorkflowRequestTableRecord[] wrtrs = wdti.getWorkflowRequestTableRecords();

			List rows = new ArrayList();

			for(int j=0;wrtrs!=null&&j<wrtrs.length;j++) {

				Row row = new Row();
				row.setId(j+"");

				WorkflowRequestTableRecord wrtr = wrtrs[j];

				WorkflowRequestTableField[] wrtfs = wrtr.getWorkflowRequestTableFields();

				List cells = new ArrayList();

				for(int k=0;wrtfs!=null&&k<wrtfs.length;k++) {

					WorkflowRequestTableField wrtf = wrtfs[k];

					if(wrtf!=null) {

						if(wrtf.getFieldName()!=null&&!"".equals(wrtf.getFieldName())&&wrtf.isView()&&wrtf.isEdit()){

							Cell cell = new Cell();

							cell.setName(wrtf.getFieldName());
							cell.setValue(wrtf.getFieldValue());

							cells.add(cell);
						}

					}
				}

				if(cells!=null&&cells.size()>0) {
					Cell[] cellarray = (Cell[])cells.toArray(new Cell[cells.size()]);
					row.setCell(cellarray);
				}
				rows.add(row);
			}

			if(rows!=null&&rows.size()>0) {
				Row[] rowarray = (Row[])rows.toArray(new Row[rows.size()]);
				detailTable.setRow(rowarray);
			}
			detailTables.add(detailTable);
		}
		DetailTable[] detailTablearray = (DetailTable[])detailTables.toArray(new DetailTable[detailTables.size()]);
		detailTableInfo.setDetailTable(detailTablearray);
		requestInfo.setDetailTableInfo(detailTableInfo);

		return requestInfo;
	}

	private List getPropertyByName(MainTableInfo minfo, String pname) {
		ArrayList l = new ArrayList();
		Property[] props = minfo.getProperty();
		if (props != null) {
			for (int i = 0; i < props.length; i++) {
				Property prop = props[i];
				if (prop.getName().equalsIgnoreCase(pname)) {
					l.add(prop);
				}
			}
		}
		return l;
	}

	/**
	 * 处理特殊字符
	 * @param dbtype 数据库类型
	 * @param s 字符串
	 * @return
	 */
	private static String parseSpecialChar(String dbtype,String s){
		String str = Util.null2String(s);
		if(!s.equals("")){
			/*if(str.indexOf("'")>-1){
				str = str.replaceAll("\'", "''");
			}*/
			if(dbtype.equalsIgnoreCase("oracle")){
				if(str.indexOf("&")>-1){
					//str = str.replaceAll("&", "'||'&'||'");
				}
			}
		}
		return str;
	}

	private Cell getCellByName(Row row, String cname) {
		Cell[] cells = row.getCell();
		if (cells != null) {
			for (int i = 0; i < cells.length; i++) {
				Cell cell = cells[i];
				if (cell.getName().equalsIgnoreCase(cname)) {
					return cell;
				}
			}
		}
		return null;
	}

	private List<String> getFieldNames(String updatecondition){
		List fieldNames = new ArrayList<String>();
		Pattern pattern = Pattern.compile("\\$(.+?)\\$");
		Matcher matcher = pattern.matcher(updatecondition);
		while(matcher.find()){
			String fieldName = matcher.group(1);
			fieldNames.add(fieldName);
		}
		return fieldNames;
	}

	/**
	 * @param sourceworkflowid   老流程id
	 * @param targetworkflowid	  新流程id
	 * @param nodemap			 nodemap key/value  key=老nodeid/value=新nodeid
	 * @return
	 */
	public boolean copyWorkflowToModeSet(int sourceworkflowid,int targetworkflowid,Map nodemap){
		String sql="";
		RecordSet rs = new RecordSet();
		RecordSet RecordSet = new RecordSet();
		RecordSet rst = new RecordSet();
		try{
			sql="select count(*) as datacount from mode_workflowtomodeset s where workflowid="+sourceworkflowid;
			rst.executeSql(sql);
			if(rst.next()){
				if(rst.getInt("datacount")<1){
					return false;
				}
				sql="select * from mode_workflowtomodeset where mainid =0 and workflowid="+sourceworkflowid;
				rs.executeSql(sql);
				while(rs.next()){
					int sourceid = rs.getInt("id");
					int sourcetriggernodeid = rs.getInt("triggernodeid");
					int isenable = rs.getInt("isenable");
					int triggerType = rs.getInt("triggerType");
					sql="select * from workflowactionset where workflowid="+targetworkflowid+" and actionname='WorkflowToMode' and nodeid="+Util.getIntValue(Util.null2String(nodemap.get(sourcetriggernodeid+"")),0);
					rst.executeSql(sql);
					int targetactionid=-1;
					if(rst.next()){
						targetactionid = rst.getInt("id");
					}
					int triggernodeid = Util.getIntValue(Util.null2String(nodemap.get(sourcetriggernodeid+"")),0);

					sql="insert into mode_workflowtomodeset(modeid,workflowid,modecreater,modecreaterfieldid,triggernodeid,triggertype,isenable,formtype,actionid,maintableopttype,maintableupdatecondition,basedfield,mainid)";
					sql+="select modeid,'"+targetworkflowid+"',modecreater,modecreaterfieldid,"+triggernodeid+",triggertype,isenable,formtype,"+targetactionid+",maintableopttype,maintableupdatecondition,basedfield,mainid from mode_workflowtomodeset where id="+sourceid;
					rst.executeSql(sql);
					rst.executeSql("select max(id) as idmax from mode_workflowtomodeset ");
					int mainid=0;
					if(rst.next()){
						mainid = rst.getInt("idmax");
					}
					sql="insert into mode_workflowtomodeset(modeid,workflowid,modecreater,modecreaterfieldid,triggernodeid,triggertype,isenable,formtype,actionid,maintableopttype,maintableupdatecondition,basedfield,mainid)";
					sql+="select modeid,'"+targetworkflowid+"',modecreater,modecreaterfieldid,"+triggernodeid+",triggertype,isenable,formtype,"+targetactionid+",maintableopttype,maintableupdatecondition,basedfield,"+mainid+" from mode_workflowtomodeset where mainid="+sourceid;
					rst.executeSql(sql);
					sql="insert into mode_workflowtomodesetdetail( mainid , modefieldid ,wffieldid , defaultvalue)";
					sql+="select "+mainid+" , modefieldid ,wffieldid,defaultvalue from mode_workflowtomodesetdetail where mainid ="+sourceid;
					rst.executeSql(sql);

					sql="insert into mode_workflowtomodesetopt( mainid,detailtablename,opttype,updatecondition )";
					sql+="select "+mainid+" ,detailtablename,opttype,updatecondition from mode_workflowtomodesetopt where mainid ="+sourceid;
					rst.executeSql(sql);


				}
			}
		}catch(Exception e){
			writeLog("复制流程转数据配置失败！原流程id:"+sourceworkflowid+",新流程id:"+targetworkflowid);
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * @param sourceworkflowid   老流程id
	 * @param targetworkflowid	  新流程id
	 * @param nodemap			 nodemap key/value  key=老nodeid/value=新nodeid
	 * @param nodelinkmap 出口map
	 * @return
	 */
	public boolean copyWorkflowToModeSet(int sourceworkflowid,int targetworkflowid,Map nodemap,Map nodelinkmap,Map actionmap){
		String sql="";
		RecordSet rs = new RecordSet();
		RecordSet rs1 = new RecordSet();
		RecordSet rs2 = new RecordSet();
		RecordSet RecordSet = new RecordSet();
		RecordSet rst = new RecordSet();
		RecordSet recordSet = new RecordSet();
		try{
			sql="select count(*) as datacount from mode_workflowtomodeset s where mainid = 0 and workflowid="+sourceworkflowid;
			rst.executeSql(sql);
			if(rst.next()){
				if(rst.getInt("datacount")<1){
					return false;
				}
				sql="select * from mode_workflowtomodeset where mainid = 0 and workflowid="+sourceworkflowid;
				rs.executeSql(sql);
				while(rs.next()){
					int sourceid = rs.getInt("id");
					int triggerMethod = Util.getIntValue(rs.getString("triggerMethod"),1);//触发方式：1--节点触发；2--出口触发
					int sourceworkflowExport = rs.getInt("workflowExport");
					int sourcetriggernodeid = rs.getInt("triggernodeid");
					int isenable = rs.getInt("isenable");
					int triggerType = rs.getInt("triggerType");
					int resetdataid = rs.getInt("resetdataid");
					int actionid = rs.getInt("actionid");
					String conditiontype = rs.getString("conditiontype");
					String conditionsql = rs.getString("conditionsql");
					int targetactionid=-1;
					targetactionid = Util.getIntValue(Util.null2String(actionmap.get(actionid)),0);
					int triggernodeid = Util.getIntValue(Util.null2String(nodemap.get(sourcetriggernodeid+"")),0);
					int workflowExport = Util.getIntValue(Util.null2String(nodelinkmap.get(sourceworkflowExport+"")),0);

					sql="insert into mode_workflowtomodeset(workflowtomodename,modeid,workflowid,modecreater,modecreaterfieldid,triggerMethod,triggernodeid,triggertype,workflowExport,isenable,formtype,actionid,maintableopttype,maintableupdatecondition,maintablewherecondition,basedfield,resetdataid,conditiontype,conditionsql,conditiontext,dsporder,isinsert,mainid,remark)";
					sql+="select workflowtomodename,modeid,'"+targetworkflowid+"',modecreater,modecreaterfieldid,"+triggerMethod+","+triggernodeid+",triggertype,"+workflowExport+",isenable,formtype,"+targetactionid+",maintableopttype,maintableupdatecondition,maintablewherecondition,basedfield,resetdataid,conditiontype,conditionsql,conditiontext,dsporder,isinsert,mainid,remark from mode_workflowtomodeset where id="+sourceid;
					rst.executeSql(sql);
					rst.executeSql("select max(id) as idmax from mode_workflowtomodeset ");
					int mainid=0;
					if(rst.next()){
						mainid = rst.getInt("idmax");
					}
					rs1.executeQuery("select * from mode_workflowtomodeset where mainid = ? and workflowid=?",sourceid,sourceworkflowid);
					while(rs1.next()){
						int triggerMethoddetail = Util.getIntValue(rs.getString("triggerMethod"),1);//触发方式：1--节点触发；2--出口触发
						int sourcedetailid = Util.getIntValue(Util.null2String(rs.getString("id")));
						int targetactiondetailid=-1;
						targetactiondetailid = Util.getIntValue(Util.null2String(actionmap.get(rs1.getInt("actionid"))),0);
						int triggernodedetailid = Util.getIntValue(Util.null2String(nodemap.get(rs1.getInt("triggernodeid")+"")),0);
						int workflowExportdetail = Util.getIntValue(Util.null2String(nodelinkmap.get(rs1.getInt("workflowExport")+"")),0);
						sql="insert into mode_workflowtomodeset(workflowtomodename,modeid,workflowid,modecreater,modecreaterfieldid,triggerMethod,triggernodeid,triggertype,workflowExport,isenable,formtype,actionid,maintableopttype,maintableupdatecondition,maintablewherecondition,basedfield,resetdataid,conditiontype,conditionsql,conditiontext,dsporder,isinsert,mainid,remark)";
						sql+="select workflowtomodename,modeid,'"+targetworkflowid+"',modecreater,modecreaterfieldid,"+triggerMethoddetail+","+triggernodedetailid+",triggertype,"+workflowExportdetail+",isenable,formtype,"+targetactiondetailid+",maintableopttype,maintableupdatecondition,maintablewherecondition,basedfield,resetdataid,conditiontype,conditionsql,conditiontext,dsporder,isinsert,"+mainid+",remark from mode_workflowtomodeset where id="+sourcedetailid;
						rst.executeSql(sql);
					}
					//-------------复制条件中的规则----------------------
					if(!StringHelper.isEmpty(conditionsql)&&"1".equals(conditiontype)){//插入并重新计算条件规则关系
						sql="select id,fieldid,fieldname,fieldlabel,compareopion,compareopionlabel,htmltype,fieldtype,fielddbtype,fieldvalue,fieldtext,relationtype,valetype,workflowtomodeid,uuid from mode_expressionbase where workflowtomodeid="+sourceid;
						rs1.execute(sql);
						Map<String,String> baseMap = new HashMap<String,String>();
						Map<String,String> expMap = new HashMap<String,String>();
						while(rs1.next()){
							String uuid = getUUID();
							String id = rs1.getString("id");
							int new_id = getdbid("mode_expressionbase");
							String insertSql = "insert into mode_expressionbase(id,fieldid,fieldname,fieldlabel,compareopion,compareopionlabel,htmltype,fieldtype,fielddbtype,fieldvalue,fieldtext,relationtype,valetype,workflowtomodeid,uuid) "+
									" select "+new_id+",fieldid,fieldname,fieldlabel,compareopion,compareopionlabel,htmltype,fieldtype,fielddbtype,fieldvalue,fieldtext,relationtype,valetype,"+mainid+",'"+uuid+"' from mode_expressionbase where id="+id;
							rs2.execute(insertSql);
							String sql1 = "select id from mode_expressionbase where uuid='"+uuid+"'";
							rs2.execute(sql1);
							String newid = "";
							if(rs2.next()){
								newid = rs2.getString("id");
							}
							baseMap.put(id, newid);
						}

						sql="select id,relation,expids,expbaseid,workflowtomodeid,uuid from mode_expressions where workflowtomodeid="+sourceid;
						rs1.execute(sql);
						while(rs1.next()){
							String uuid = uuid = getUUID();
							String id = rs1.getString("id");
							int new_id = getdbid("mode_expressions");
							String insertSql = "insert into mode_expressions(id,relation,expids,expbaseid,workflowtomodeid,uuid) "+
									" select "+new_id+",relation,expids,expbaseid,"+mainid+",'"+uuid+"' from mode_expressions where id="+id;
							rs2.execute(insertSql);
							String sql1 = "select id from mode_expressions where uuid='"+uuid+"'";
							rs2.execute(sql1);
							String newid = "";
							if(rs2.next()){
								newid = rs2.getString("id");
							}
							expMap.put(id, newid);
						}

						sql="select id,expids,expbaseid from mode_expressions where workflowtomodeid="+mainid;
						rs1.execute(sql);
						while(rs1.next()){
							String id = rs1.getString("id");
							String expids = rs1.getString("expids");
							String expbaseid = rs1.getString("expbaseid");
							String newexpids = "";
							String newbaseids = "";
							boolean f = false;
							if(!StringHelper.isEmpty(expids)){
								String[] expidsArr = expids.split(",");
								for(int i=0;i<expidsArr.length;i++){
									String s = expidsArr[i];
									if(expMap.containsKey(s)){
										f = true;
										newexpids += ","+expMap.get(s);
									}
								}
							}
							if(!StringHelper.isEmpty(expbaseid)){
								String[] baseArr = expbaseid.split(",");
								for(int i=0;i<baseArr.length;i++){
									String s = baseArr[i];
									if(baseMap.containsKey(s)){
										f = true;
										newbaseids += ","+baseMap.get(s);
									}
								}
							}
							if(!StringHelper.isEmpty(newexpids)){
								newexpids = newexpids.substring(1);
							}else{
								newexpids=null;
							}
							if(!StringHelper.isEmpty(newbaseids)){
								newbaseids = newbaseids.substring(1);
							}else{
								newbaseids=null;
							}
							if(f){
								String updateSql = "update mode_expressions set expids=?,expbaseid=? where id=?";
								rs2.executeUpdate(updateSql,newexpids,newbaseids,id);
							}
						}
					}
					sql="insert into mode_workflowtomodesetdetail( mainid , modefieldid ,wffieldid,defaultvalue )";
					sql+="select "+mainid+" , modefieldid ,wffieldid,defaultvalue from mode_workflowtomodesetdetail where mainid ="+sourceid;
					rst.executeSql(sql);

					sql="insert into mode_workflowtomodesetopt( mainid,detailtablename,opttype,updatecondition,wherecondition )";
					sql+="select "+mainid+" ,detailtablename,opttype,updatecondition,wherecondition from mode_workflowtomodesetopt where mainid ="+sourceid;
					rst.executeSql(sql);

					//======================复制 cube_field_assign_exp

					sql="select * from  cube_field_assign_exp  where wftomodeid=? order by id asc";
					RecordSet.executeQuery(sql,sourceid);
					while(RecordSet.next()){
						int fid =Util.getIntValue(RecordSet.getString("id"));
						String sql1="insert into cube_field_assign_exp(conditionname,conditiontype,conditionkey,exttype,conditionfieldexp,modeid,workflowid,wftomodeid)"
								+"select conditionname,conditiontype,conditionkey,exttype,conditionfieldexp,modeid,workflowid,? from  cube_field_assign_exp where id=?";
						recordSet.executeUpdate(sql1,mainid,fid);
						int fmainid=0;

						sql="SELECT max(id) id  from cube_field_assign_exp where wftomodeid=? ";
						recordSet.executeQuery(sql,mainid);
						if(recordSet.next()) {
							fmainid = recordSet.getInt("id");
						}
						sql1="SELECT * FROM cube_conditioninfo where mainid=? order by id asc";//条件
						recordSet.executeQuery(sql1,fid);
						while(recordSet.next()){
							String cid = recordSet.getString("id");
							sql1="insert into cube_conditioninfo(mainid,conditioninkey,formtype,relation,relationto,targetValue,targetValueSpan,isfirst,targetFieldid,wftomodeid) " +
									"select "+fmainid+",conditioninkey,formtype,relation,relationto,targetValue,targetValueSpan,isfirst,targetFieldid,"+mainid+" from cube_conditioninfo where id=?";
							rs2.executeUpdate(sql1,cid);
						}

						sql1="SELECT * FROM cube_fieldexpinfo where mainid=? order by id asc";//字段赋值
						recordSet.executeQuery(sql1,fid);
						while(recordSet.next()){
							String fxid = recordSet.getString("id");
							sql1="insert  into cube_fieldexpinfo(mainid,fieldexpkey,fieldid,fieldformula,targetValue,targetValueSpan,formuladesc,tablefrom,wftomodeid) " +
									"select "+fmainid+",fieldexpkey,fieldid,fieldformula,targetValue,targetValueSpan,formuladesc,tablefrom,"+mainid+" from cube_fieldexpinfo where id =?";
							rs2.executeUpdate(sql1,fxid);
						}
					}





				}
			}
		}catch(Exception e){
			writeLog("复制流程转数据配置失败！原流程id:"+sourceworkflowid+",新流程id:"+targetworkflowid);
			e.printStackTrace();
		}
		return true;
	}

	public String getUUID(){
		UUID uuid  =  UUID.randomUUID();
		String id = uuid.toString().replaceAll("-", "");
		return id;
	}

	public synchronized static int getdbid(String tablename) throws Exception {
		int result = 0;
		RecordSet rs = new RecordSet();
		rs.executeSql("select max(id) as id from "+tablename);
		if (rs.next()) {
			result = Util.getIntValue(rs.getString("id"), 0);
		}
		return (result + 1);
	}


	/**
	 * 获取日志字段值
	 * @param tableName
	 * @param billid
	 * @param needlogFieldList
	 * @return
	 */
	public Map<String,Object> getLogFieldData(String tableName,int billid,List<Map<String, Object>> needlogFieldList){
		Map<String, Object> logFieldData = new HashMap<String, Object>();
		if(needlogFieldList.size() > 0){
			String columnNames = "";
			for(int i = 0; i < needlogFieldList.size(); i++){
				Map<String, Object> needlogField = needlogFieldList.get(i);
				String fieldname = Util.null2String(needlogField.get("fieldname"));
				String detailtable = Util.null2String(needlogField.get("detailtable"));
				if(!detailtable.equals(""))continue;//过滤掉明细字段
				columnNames += fieldname+",";
			}
			if(columnNames.endsWith(",")){
				columnNames=columnNames.substring(0,columnNames.length()-1);
			}
			FormInfoService formInfoService = new FormInfoService();
			logFieldData = formInfoService.getTableData(tableName, billid, columnNames);
		}
		return logFieldData;
	}

	public Map<String,Map<String,Map<String,Object>>> getLogFieldData_detail(int billid,List<Map<String, Object>> needlogFieldList){
		Map<String, Map<String, Map<String,Object>>> oldData_detail = new HashMap<String, Map<String, Map<String,Object>>>();
		Map<String,String> columnNames =new HashMap<String, String>();
		if(needlogFieldList.size() > 0){
			for(int i = 0; i < needlogFieldList.size(); i++){
				Map<String, Object> needlogField = needlogFieldList.get(i);
				String fieldname = Util.null2String(needlogField.get("fieldname"));
				String fieldid = Util.null2String(needlogField.get("id"));
				String detailtable = Util.null2String(needlogField.get("detailtable"));
				if(detailtable.equals(""))continue;
				if(!columnNames.containsKey(detailtable)){
					columnNames.put(detailtable, "");
				}
				String columnName=columnNames.get(detailtable);
				if(columnName.equals("")){
					columnName=fieldname;
				}else{
					columnName+=","+fieldname;
				}
				columnNames.put(detailtable, columnName);
			}
			FormInfoService formInfoService = new FormInfoService();
			for(String key:columnNames.keySet()){
				oldData_detail.put(key,formInfoService.getTableData(key, billid, columnNames.get(key),"id","mainid"));
			}
		}
		return oldData_detail;
	}


	/**
	 *  保存字段日志信息
	 * @param formmodeid
	 * @param billid
	 * @param user
	 * @param clientaddress
	 * @param operatetype
	 * @param needlogFieldList
	 * @param oldData
	 * @param nowData
	 */
	public void saveModeViewLog(int formmodeid,int billid,User user,String clientaddress,String operatetype,List<Map<String, Object>> needlogFieldList,Map oldData,Map nowData,Map<String, Map<String, Map<String, Object>>> oldData_detail,Map<String, Map<String, Map<String, Object>>> nowData_detail){
		FormInfoService formInfoService = new FormInfoService();
		RecordSet rs=new RecordSet();
		String operatedesc = "";
		int htmllabelindex = 0;//#887673
		if("1".equals(operatetype)){
			operatedesc = SystemEnv.getHtmlLabelName(365,user.getLanguage())+"("+SystemEnv.getHtmlLabelName(30055,user.getLanguage())+")";//
			htmllabelindex = 522420;
		}else if("2".equals(operatetype)){
			operatedesc = SystemEnv.getHtmlLabelName(33797,user.getLanguage())+"("+SystemEnv.getHtmlLabelName(30055,user.getLanguage())+")";//修改
			htmllabelindex = 522421;
		}
		try{
			int userid = user.getUID();
			if(createBillMap.containsKey(formmodeid+"_"+billid)){
				userid = createBillMap.get(formmodeid+"_"+billid);
			}
			ModeViewLog modeViewLog = new ModeViewLog();
			modeViewLog.resetParameter();
			modeViewLog.setClientaddress(clientaddress);
			modeViewLog.setModeid(formmodeid);
			modeViewLog.setOperatetype(operatetype);
			modeViewLog.setOperatedesc(operatedesc);
			modeViewLog.setHtmllabelindex(htmllabelindex);
			modeViewLog.setOperateuserid(userid);
			modeViewLog.setRelatedid(billid);
			modeViewLog.setRelatedname("");
			int viewlogid = modeViewLog.setSysLogInfo();
			Map<String,String> columnNames =new HashMap<String, String>();
			Map<String,String> columnFieldids =new HashMap<String, String>();//根据表名及字段名获取字段id
			if(needlogFieldList.size() > 0){
				for(int i = 0; i < needlogFieldList.size(); i++){
					Map<String, Object> needlogField = needlogFieldList.get(i);
					String fieldid = Util.null2String(needlogField.get("id"));
					String fieldname = Util.null2String(needlogField.get("fieldname"));
					String detailtable = Util.null2String(needlogField.get("detailtable"));
					if(detailtable.equals("")){//主表保存日志信息
						String oldFieldValue = Util.null2String(oldData.get(fieldname));
						String nowFieldValue = Util.null2String(nowData.get(fieldname));
						if(!oldFieldValue.equals(nowFieldValue)&&"2".equals(operatetype)){
							Map<String, Object> logDetailMap = new HashMap<String,	Object>();
							logDetailMap.put("viewlogid", viewlogid);
							logDetailMap.put("fieldid", fieldid);
							logDetailMap.put("fieldvalue", nowFieldValue);
							logDetailMap.put("prefieldvalue", oldFieldValue);
							logDetailMap.put("modeid", formmodeid);
							formInfoService.saveFieldLogDetail(logDetailMap);
						}
					}else{//明细
						if(!columnNames.containsKey(detailtable)){
							columnNames.put(detailtable, "");
						}
						String columnName=columnNames.get(detailtable);
						if(columnName.equals("")){
							columnName=fieldname;
						}else{
							columnName+=","+fieldname;
						}
						columnNames.put(detailtable, columnName);
						columnFieldids.put(detailtable+"_"+fieldname, fieldid);
					}
				}
				//明细字段保存信息
				for(String tablename:columnNames.keySet()){
					Set<String> intersectionKey=new HashSet<String>();
					String[] columnsArray=columnNames.get(tablename).split(",");
					Map<String, Map<String, Object>> od_detail=oldData_detail.get(tablename);
					Map<String, Map<String, Object>> no_detail=nowData_detail.get(tablename);
					od_detail=od_detail==null?new HashMap<String, Map<String,Object>>():od_detail;
					no_detail=no_detail==null?new HashMap<String, Map<String,Object>>():no_detail;
					for(String detailid:no_detail.keySet()){
						if(od_detail.containsKey(detailid)){//编辑的明细数据
							Map<String, Object> od=od_detail.get(detailid);
							Map<String, Object> nd=no_detail.get(detailid);
							for(String column:columnsArray){
								String fieldvalue=Util.null2String(nd.get(column));
								String prefieldvalue=Util.null2String(od.get(column));
								if(!prefieldvalue.equals(fieldvalue)){
									rs.executeUpdate("insert into ModeLogFieldDetail(viewlogid,fieldid,fieldvalue,prefieldvalue,modeid,detaildataid,operateType,detailtable) values(?,?,?,?,?,?,?,?)", viewlogid,
											columnFieldids.get(tablename+"_"+column),fieldvalue,prefieldvalue,formmodeid,detailid,"EDIT",tablename);
								}
							}
							intersectionKey.add(detailid);
						}else{//新增的明细数据
							Map<String, Object> nd=no_detail.get(detailid);
							for(String column:columnsArray){
								String fieldvalue=Util.null2String(nd.get(column));
								rs.executeUpdate("insert into ModeLogFieldDetail(viewlogid,fieldid,fieldvalue,modeid,detaildataid,operateType,detailtable) values(?,?,?,?,?,?,?)", viewlogid,
										columnFieldids.get(tablename+"_"+column),fieldvalue,formmodeid,detailid,"ADD",tablename);
							}
						}
					}
					for(String detailid:od_detail.keySet()){
						if(!intersectionKey.contains(detailid)){//被删除的明细数据
							Map<String, Object> od=od_detail.get(detailid);
							for(String column:columnsArray){
								String prefieldvalue=Util.null2String(od.get(column));
								rs.executeUpdate("insert into ModeLogFieldDetail(viewlogid,fieldid,prefieldvalue,modeid,detaildataid,operateType,detailtable) values(?,?,?,?,?,?,?)", viewlogid,
										columnFieldids.get(tablename+"_"+column),prefieldvalue,formmodeid,detailid,"DEL",tablename);
							}
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取唯一性字段信息
	 * @param formId
	 * @return
	 */
	private Map<String,Object> getPromptField(int formId){
		FormInfoService formInfoService = new FormInfoService();
		Map<String,Object> promptField = new HashMap<String, Object>();
		List<Map<String,Object>> promptFieldList = formInfoService.getPromptField(formId);
		for(Map<String,Object> promptFieldMap:promptFieldList){
			String fieldid = Util.null2String(promptFieldMap.get("id"));
			String fieldname = Util.null2String(promptFieldMap.get("fieldname"));
			String labelname = Util.null2String(promptFieldMap.get("labelname"));
			JSONObject jsonObject =  new JSONObject();
			jsonObject.put("fieldid", fieldid);
			jsonObject.put("fieldname", fieldname);
			jsonObject.put("labelname", labelname);
			promptField.put(fieldid, jsonObject);
		}
		return promptField;
	}

	public String getAdd_update_id() {
		return add_update_id;
	}

	public void setAdd_update_id(String add_update_id) {
		this.add_update_id = add_update_id;
	}

	public List<Integer> getPatchadd_id() {
		return patchadd_id;
	}

	public void setPatchadd_id(List<Integer> patchadd_id) {
		this.patchadd_id = patchadd_id;
	}

	/**
	 *
	 * @param maintableName
	 * @param sourceId
	 * @param subtableList
	 * @param conditionsqlStr
	 * @param primarykey
	 * @param vbillid  虚拟表单的 主键可能是字符串
	 * @return
	 */
	public Map getSqlParam(String maintableName, int sourceId,List subtableList, String conditionsqlStr,String primarykey,String vbillid,int requestid,int detailid) {
		String billidStr = ""+sourceId;
		if(!StringHelper.isEmpty(vbillid)){
			billidStr = vbillid;
		}
		Map map = new HashMap();
		String tablenameStr = maintableName +" "+maintableName;
		String sqlwhereStr = maintableName+"."+primarykey+"='"+billidStr+"'";
		if(sourceId==0&&(StringHelper.isEmpty(vbillid)||vbillid.equals("0"))){
			sqlwhereStr = "";
		}
		if(requestid>0){
			sqlwhereStr = maintableName+".requestid="+requestid;
		}
		for(int i=0;i<subtableList.size();i++){
			String detailtablename = StringHelper.null2String(subtableList.get(i));
			if(conditionsqlStr.toLowerCase().indexOf(detailtablename.toLowerCase())!=-1){
				if(detailid>0){
					if(!StringHelper.isEmpty(sqlwhereStr)){
						sqlwhereStr += " and ";
					}
					sqlwhereStr +=  detailtablename+".id="+detailid;
				}
				tablenameStr = tablenameStr+" , "+detailtablename+" "+detailtablename;
				sqlwhereStr = sqlwhereStr+" and "+detailtablename+".mainid="+maintableName+"."+primarykey+" ";
			}
		}

		if(sqlwhereStr.startsWith(" and ")){
			sqlwhereStr = sqlwhereStr.replaceFirst(" and ", " ");
		}
		map.put("tablenameStr", tablenameStr);
		map.put("sqlwhereStr", sqlwhereStr);
		return map;
	}

	public JSONObject checkConditionSQL(int objid,User user,Map map) {
		JSONObject jsonObj = new JSONObject();
		if(wf_isbill==0){//老表单
			String formtype ="";
			int requestid = 0;
			if(map!=null&&map.containsKey("formtype")){
				formtype = Util.null2String(map.get("formtype"));
				if(map.containsKey("requestid")){
					requestid = Util.getIntValue(Util.null2String(map.get("requestid")),0);
				}
			}
			RecordSet rs1 = new RecordSet();
			String sql1 = "select c.modeid,c.conditionsql from mode_workflowtomodeset c where  c.id="+objid;
			rs1.executeSql(sql1);
			String conditionsql = "";
			if(rs1.next()){
				conditionsql = rs1.getString("conditionsql");
			}
			if(StringHelper.isEmpty(conditionsql)){
				jsonObj.put("msg", SystemEnv.getHtmlLabelName(390010,user.getLanguage()));//条件为空
				jsonObj.put("flag", "0");
				jsonObj.put("sql", "");
				jsonObj.put("count", 0);
				return jsonObj;
			}else{
				CustomTreeData customTreeData = new CustomTreeData();
				customTreeData.setUser(user);
				conditionsql = customTreeData.replaceParam(conditionsql);//替换变量
			}

			boolean flag = false;
			String befStr = "1=2";
			if(requestid>0){
				befStr = "1=1";
			}
			//老表单 groupId 区分明细1 。明细2
			String checkSql ="select 1  from workflow_form where "+befStr;
			if("maintable".equals(formtype)){
				checkSql = "select 1  from workflow_form where requestid="+requestid+"  and ("+conditionsql+") and "+befStr;
			}else if(!StringHelper.isEmpty(formtype)&&formtype.startsWith("detail")){
				formtype = formtype.replace("detail","");
				checkSql = "select 1  from workflow_formdetail where requestid="+requestid+"  and groupId="+formtype+"  and ("+conditionsql+") and "+befStr;
			}
			RecordSet rs2 = new RecordSet();
			flag = rs2.executeSql(checkSql);
			if(flag){
				jsonObj.put("msg", SystemEnv.getHtmlLabelName(387162, user.getLanguage()));//"条件检测通过"
				jsonObj.put("flag", "1");
				jsonObj.put("sql", checkSql);
				jsonObj.put("count", rs2.getCounts());
			}else{
				jsonObj.put("msg", SystemEnv.getHtmlLabelName(390011,user.getLanguage()));//"错误：条件检测未通过"
				jsonObj.put("flag", "0");
				jsonObj.put("sql", checkSql);
				jsonObj.put("count", 0);
			}
		}else{
			int requestid = 0;
			int detailid = 0;
			if(map!=null&&map.containsKey("requestid")){
				requestid = Util.getIntValue(Util.null2String(map.get("requestid")),0);
				if(map.containsKey("detailid")){
					detailid = Util.getIntValue(Util.null2String(map.get("detailid")),0);
				}
			}

			RecordSet rs1 = new RecordSet();
			String sql1 = "select a.formid,b.tablename,c.modeid,c.conditionsql,formtype from workflow_base a,workflow_bill b,mode_workflowtomodeset c where a.id=c.workflowid and a.formid=b.id and c.id="+objid;
			rs1.executeSql(sql1);
			int formid = 0;
			String maintableName = "";
			String conditionsql = "";
			String formtype = "";
			if(map!=null&&map.containsKey("formtype")){
				formtype = Util.null2String(map.get("formtype"));
			}
			if(rs1.next()){
				formid = rs1.getInt("formid");
				maintableName =  rs1.getString("tablename");
				if(VirtualFormHandler.isVirtualForm(formid)){
					maintableName = VirtualFormHandler.getRealFromName(maintableName);
				}
				conditionsql = rs1.getString("conditionsql");
			}
			if(StringHelper.isEmpty(conditionsql)){
				jsonObj.put("msg", SystemEnv.getHtmlLabelName(390010,user.getLanguage()));//条件为空
				jsonObj.put("flag", "0");
				jsonObj.put("sql", "");
				jsonObj.put("count", 0);
				return jsonObj;
			}else{
				CustomTreeData customTreeData = new CustomTreeData();
				customTreeData.setUser(user);
				conditionsql = customTreeData.replaceParam(conditionsql);//替换变量
			}

			//触发表单，maintable：主表；detail1：  明细表1；detail2：  明细表2
			String sqlwhere = "";
			String Newformtype = "";
			if("maintable".equals(formtype)){
				Newformtype=formtype;
				formtype = "";
			}else if(!StringHelper.isEmpty(formtype)&&formtype.startsWith("detail")){
				formtype = formtype.replace("detail","");
				formtype = maintableName+"_dt"+formtype;
				sqlwhere = " and tablename='"+formtype+"' ";
			}

			sql1 = "select tablename from Workflow_billdetailtable where billid="+formid+" "+sqlwhere+" order by orderid";
			rs1.executeSql(sql1);
			String tablenameStr = maintableName +" "+maintableName;
			String sqlwhereStr = "";
			List subtableList = new ArrayList();
			while(rs1.next()){
				String detailtablename = rs1.getString("tablename");
				subtableList.add(detailtablename);
			}
			String vprimarykey = "id";
			String vdatasource = "";

			if(VirtualFormHandler.isVirtualForm(formid)){	//虚拟表单
				Map<String, Object> vFormInfo = VirtualFormHandler.getVFormInfo(formid);
				vprimarykey = Util.null2String(vFormInfo.get("vprimarykey"));
				vdatasource = Util.null2String(vFormInfo.get("vdatasource"));
			}


			Map tempMap = getSqlParam(maintableName,0,subtableList,conditionsql,vprimarykey,"0",requestid,detailid);
			tablenameStr = StringHelper.null2String(tempMap.get("tablenameStr"));
			String sqlwhereStrTemp = StringHelper.null2String(tempMap.get("sqlwhereStr"));
			if(!sqlwhereStrTemp.equals("")){
				sqlwhereStrTemp = " and ("+sqlwhereStrTemp+")";
			}

			boolean flag = false;
			String befStr = "1=2";
			if(requestid>0){
				befStr = "1=1";
			}
			String checkSql ="";
			if("maintable".equals(Newformtype)){
				if(subtableList.size() == 0) {
					checkSql = "select 1  from " + tablenameStr + " where " + befStr + " " + sqlwhereStrTemp + " and (" + conditionsql + ")";
				} else {
					for(int i=0;i<subtableList.size();i++) {
						String detailtablename = StringHelper.null2String(subtableList.get(i));
						if (conditionsql.toLowerCase().indexOf(detailtablename.toLowerCase()) != -1) {
							sqlwhereStrTemp=sqlwhereStrTemp.replaceFirst("and","").replaceAll("[\\(\\)]","");
							String sqlwhereStrTemp1=sqlwhereStrTemp.replace(sqlwhereStrTemp.substring(sqlwhereStrTemp.indexOf("and")),"");
							String sqlwhereStrTemp2=sqlwhereStrTemp.substring(sqlwhereStrTemp.indexOf("and")).replace("and","");
							checkSql="select 1  from " + tablenameStr.replace(","," LEFT JOIN ") + " ON " + sqlwhereStrTemp2 + " where "+sqlwhereStrTemp1+" and (" + conditionsql + ")";
							continue;
						} else {
							checkSql = "select 1  from " + tablenameStr + " where " + befStr + " " + sqlwhereStrTemp + " and (" + conditionsql + ")";
							continue;
						}
					}
				}

			}else {
				checkSql = "select 1  from " + tablenameStr + " where " + befStr + " " + sqlwhereStrTemp + " and (" + conditionsql + ")";
			}
			RecordSet rs2 = new RecordSet();
			if(vdatasource.equals("")){
				flag = rs2.executeSql(checkSql);
			}else{
				flag = rs2.executeSql(checkSql,vdatasource);
			}
			if(flag){
				jsonObj.put("msg", SystemEnv.getHtmlLabelName(387162, user.getLanguage()));//"条件检测通过"
				jsonObj.put("flag", "1");
				jsonObj.put("sql", checkSql);
				jsonObj.put("vdatasource", vdatasource);
				jsonObj.put("count", rs2.getCounts());
			}else{
				jsonObj.put("msg", SystemEnv.getHtmlLabelName(390011,user.getLanguage()));//"错误：条件检测未通过"
				jsonObj.put("flag", "0");
				jsonObj.put("sql", checkSql);
				jsonObj.put("vdatasource", vdatasource);
				jsonObj.put("count", 0);
			}
		}
		return jsonObj;
	}

	public String getSql(String sql,Object... params) {
		try {
			List parmList = new ArrayList();
			EncodingUtils edu = new EncodingUtils();
			int i=1;
			for(Object param : params){
				if (((param instanceof Integer)) || ((param instanceof Long))){
					parmList.add(Util.getIntValue(""+param));
				}
				else if (param == null)
					parmList.add("null");
				else if ((param instanceof Float))
					parmList.add(Util.getFloatValue(""+param));
				else if (((param instanceof BigDecimal)) || ((param instanceof Double)))
					parmList.add((param instanceof BigDecimal) ? (BigDecimal)param : new BigDecimal(Util.getDoubleValue(""+param)));
				else if ((param instanceof java.sql.Date))
					parmList.add("'"+(java.sql.Date)param+"'");
				else if (((param instanceof String)) || ((param instanceof Character))){
					if("null".equals((""+param).trim().toLowerCase())){
						parmList.add("null");
					}else{
						parmList.add("'"+param+"'");
					}
				}else if ((param instanceof Clob)){
					//oracle clob类型的存储，先将clob转换成String
					String str = "";
					str = edu.ClobToString(param);
					parmList.add("'"+str+"'");
				} else if ((param instanceof Blob)){
					String str = "";
					str = edu.ClobToString(param);
					parmList.add("'"+str+"'");
				}else {
					parmList.add("'"+param+"'");
				}
			}
			for (int a=0;a<parmList.size();a++) {
				//sql=sql.replaceFirst("\\?", Util.null2String(parmList.get(a))); //更换sql字符串中出现的第一个?
				String s = Util.null2String(parmList.get(a));//直接使用replaceFirst()方法替换？，会导致parmList.get(a)中原本的反斜杠丢失，保险起见直接使用substring函数替换。
				int index = sql.indexOf("?");
				String substring = sql.substring(0,index);
				String substring1 = sql.substring(index+1);
				sql = substring+s+substring1;
			}
		} catch (Exception e) {
			writeLog("WorkflowToMode==getSql异常"+e);
		}
		return sql;
	}

	public static String getTrace(Throwable t) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		t.printStackTrace(writer);
		StringBuffer buffer = stringWriter.getBuffer();
		return buffer.toString();
	}



	//循环执行条件表达式
	private  boolean checkCondition(int workflowtomodesetid,Map map){
		RecordSet rs = new RecordSet();
		RecordSet rs1 = new RecordSet();

		String sql =" select tablename from workflow_bill where id =?";
		String maintablename="";
		rs.executeQuery(sql,wf_formid);
		if(rs.next()){
			maintablename = Util.null2String(rs.getString("tablename"));
		}
		sql="select * from  cube_field_assign_exp where wftomodeid=? order by id asc";
		rs1.executeQuery(sql,workflowtomodesetid);
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		boolean  isComply =false;// 是否 满足 if 或者 else if
		while (rs1.next()){// if /else if / else
			String id = Util.null2String(rs1.getString("id"));
			String conditionname = Util.null2String(rs1.getString("conditionname"));
			String conditiontype =Util.null2String(rs1.getString("conditiontype"));// 0 if  1 else if  2 else
			String exttype	 = Util.null2String(rs1.getString("exttype"));// 1 所有条件满足  2 任一条件满足
			String conditionSql="";
			String elseconditionSql="";
			if("2".equals(conditiontype)){//else
				if(!isComply){// if  或者 else if  都没满足
					field_assign_exp.put(id,true);
					break;// 满足其中一个条件，即返回
				}
				//else 对应条件sql
//					for (int i = 0; i <this.elseSqlList.size() ; i++) {
//						if(conditionSql.equals("")){
//							conditionSql=this.elseSqlList.get(i);
//						}else{
//							conditionSql+=" and "+ this.elseSqlList.get(i);
//						}
//					}
			}else{
				String conSql="select  * from  cube_conditioninfo  where  mainid =?";
				rs.executeQuery(conSql,id);
				if(rs.getCounts()>0){
					while(rs.next()){
						String formtype = Util.null2String(rs.getString("formtype"));// 条件表达式 字段来源表单（流程表单）
						String targetFieldid =Util.null2String(rs.getString("targetFieldid"));//条件表达式 if(xxx=) xxx
						int relation = Util.getIntValue(rs.getString("relation"));//条件表达式 关系  等于，不等于等
						String relationto= Util.null2String(rs.getString("relationto")); // 条件表达式  1 固定值 2 动态参数  来源与表单 主表。明细1.。。
						String targetValue = Util.null2String(rs.getString("targetValue"));// 条件表达式 if(xxx=yyy) 指代 yyy
						String tablename ="";
						int modedetailno = Util.getIntValue(formtype.replace("detail", ""),0);
						tablename=modedetailno<=0?maintablename:maintablename+"_dt"+modedetailno;
						String linkStr=("1".equals(exttype))?"and":"or";
						String elseLinkStr=("1".equals(exttype))?"or":"and";//else 没有条件，全部取反

						Map<String,String> data = getsql(targetFieldid,relation,relationto,targetValue,tablename);
						String trueSql = data.get("trueSql");
						String elseSql = data.get("elseSql");
						try {
							if("-1".equals(targetFieldid)){//请求标题
								trueSql=trueSql.replace(maintablename+".requestname","'"+wfiMainfieldmap.get(targetFieldid)+"'");
								elseSql=elseSql.replace(maintablename+".requestname","'"+wfiMainfieldmap.get(targetFieldid)+"'");
							}else if("-202".equals(targetFieldid)){//节点名称
								String nodename =Util.formatMultiLang(Util.null2String(wfiMainfieldmap.get(targetFieldid)),String.valueOf(wf_user.getLanguage()));
								trueSql=trueSql.replace(maintablename+".nodename","'"+nodename+"'");
								elseSql=elseSql.replace(maintablename+".nodename","'"+nodename+"'");
							}
						} catch (Exception e) {
							e.printStackTrace();
							writeLog("转数据赋值表达式>>>>请求标题,节点名称替换异常");
						}
						if(conditionSql.equals("")){
							conditionSql=trueSql;
						}else{
							conditionSql+=" "+linkStr+" "+ trueSql;
						}
						if(elseconditionSql.equals("")){
							elseconditionSql=elseSql;
						}else{
							elseconditionSql+=" "+elseLinkStr+" "+ elseSql;
						}
					}
					elseSqlList.add("("+elseconditionSql+")");
				}else{
					if("0".equals(conditiontype)){//配置了if,但是没写条件
						conditionSql=" 1=1 ";
					}
				}
			}
			writeLog("conditionSql:","("+conditionSql+")");
			int requestid = this.requestid;
			int detailid = 0;
			if(map!=null&&map.containsKey("requestid")){
				if(map.containsKey("detailid")){
					detailid = Util.getIntValue(Util.null2String(map.get("detailid")),0);
				}
			}
			String formtype = "";
			if(map!=null&&map.containsKey("formtype")){
				formtype = Util.null2String(map.get("formtype"));
			}


			RecordSet rs2 = new RecordSet();
			if(StringHelper.isEmpty(conditionSql)){

			}else{
				CustomTreeData customTreeData = new CustomTreeData();
				customTreeData.setUser(wf_user);
				conditionSql = customTreeData.replaceParam(conditionSql);//替换变量
			}



			//触发表单，maintable：主表；detail1：  明细表1；detail2：  明细表2
			String sqlwhere = "";
			if("maintable".equals(formtype)){
				formtype = "";
			}else if(!StringHelper.isEmpty(formtype)&&formtype.startsWith("detail")){
				formtype = formtype.replace("detail","");
				formtype = maintablename+"_dt"+formtype;
				sqlwhere = " and tablename='"+formtype+"' ";
			}
			String sql1 = "select tablename from Workflow_billdetailtable where billid=?  "+sqlwhere+"   order by orderid";
			rs2.executeQuery(sql1,wf_formid);
			String tablenameStr = maintablename +" "+maintablename;
			String sqlwhereStr = "";
			List subtableList = new ArrayList();
			while(rs2.next()){
				String detailtablename = rs2.getString("tablename");
				subtableList.add(detailtablename);
			}
			String vprimarykey = "id";
			String vdatasource = "";

			if(VirtualFormHandler.isVirtualForm(wf_formid)){	//虚拟表单
				Map<String, Object> vFormInfo = VirtualFormHandler.getVFormInfo(wf_formid);
				vprimarykey = Util.null2String(vFormInfo.get("vprimarykey"));
				vdatasource = Util.null2String(vFormInfo.get("vdatasource"));
			}

			Map tempMap = getSqlParam(maintablename,0,subtableList,conditionSql,vprimarykey,"0",requestid,detailid);
			tablenameStr = StringHelper.null2String(tempMap.get("tablenameStr"));
			String sqlwhereStrTemp = StringHelper.null2String(tempMap.get("sqlwhereStr"));
			if(!sqlwhereStrTemp.equals("")){
				sqlwhereStrTemp = " and ("+sqlwhereStrTemp+")";
			}

			boolean flag = false;
			String befStr = "1=2";
			if(requestid>0){
				befStr = "1=1";
			}
			String checkSql = "select 1  from "+tablenameStr+" where "+befStr+" "+sqlwhereStrTemp+" and ("+conditionSql+")";

			writeLog("checkSql:","("+checkSql+")");

			RecordSet rs3 = new RecordSet();
			if(vdatasource.equals("")){
				flag = rs3.executeSql(checkSql);
			}else{
				flag = rs3.executeSql(checkSql,vdatasource);
			}
			if(flag){//检验通过
				if(rs3.getCounts()>0){
					field_assign_exp.put(id,true);
					isComply = true;
					//获取此条件下的字段赋值情况
					//	this.field_newValue=getFieldExpInfo(id);
					break;// 满足其中一个条件，即返回
				}
			}else{
				writeLog(workflowtomodesetid+">>"+conditionname+"校验不通过："+checkSql);
			}
		}
		return  true;
	}



	private Map<String,String> getsql(String targetFieldid,int relation,String relationto,String targetValue,String tablename) {
		RecordSet rs = new RecordSet();
		Map<String,String> data  = new HashMap<String,String>();
		String sql = " select * from  workflow_billfield where id = ?";
		rs.executeQuery(sql, targetFieldid);
		String fieldname = "";
		String htmltype = "";
		String fieldtype = "";
		String dbtype = "";
		if (rs.next()) {
			fieldname = Util.null2String(rs.getString("fieldname"));
			htmltype = Util.null2String(rs.getString("fieldhtmltype"));
			fieldtype = Util.null2String(rs.getString("type"));
			dbtype = rs.getDBType();
		}
		if("-1".equals(targetFieldid)){//请求标题
			fieldname ="requestname";
			htmltype  = "1";
			fieldtype ="1";
			dbtype = rs.getDBType();
		}else if("-2".equals(targetFieldid)){//请求id
			fieldname ="requestid";
			htmltype  = "1";
			fieldtype ="1";
			dbtype = rs.getDBType();
		}else if("-202".equals(targetFieldid)){//节点名称
			fieldname ="nodename";
			htmltype  = "1";
			fieldtype ="1";
			dbtype = rs.getDBType();
		}
		String truesql = "";
		String elsesql="";//else 对应sql
		// relationto 1 固定值  2 动态参数  来源与表单 maintablename  detail1  detail2
		if (Util.getIntValue(relationto, 0) == 0) { // 此时 targetValue 存储的是表单的字段id
			String targettablename="";
			sql =" select tablename from workflow_bill where id =?";
			String maintablename="";
			rs.executeQuery(sql,wf_formid);
			if(rs.next()){
				maintablename = Util.null2String(rs.getString("tablename"));
			}
			int modedetailno = Util.getIntValue(relationto.replace("detail", ""),0);
			//if(流程主表.dhwb=流程明细1.dhwb) 指代 流程明细1
			targettablename=modedetailno<=0?maintablename:maintablename+"_dt"+modedetailno;
			sql = " select * from  workflow_billfield where id = ?";
			rs.executeQuery(sql, targetValue);
			String tfieldname = "";
			String thtmltype = "";
			String tfieldtype = "";
			String tdbtype = "";
			if (rs.next()) {
				tfieldname = Util.null2String(rs.getString("fieldname"));
				thtmltype = Util.null2String(rs.getString("fieldhtmltype"));
				tfieldtype = Util.null2String(rs.getString("type"));
				tdbtype = rs.getDBType();
			}
			String relationAndVal="";
			String compareoptionStr = "";
			String elserelationAndVal="";
			String elsecompareoptionStr="";// else
			if (relation == 6||relation == 7) {//包含或 不包含
				compareoptionStr =(relation == 6)?" like ":" not like ";
				elsecompareoptionStr=(relation == 6)?" not like ":" like ";
				if (dbtype.startsWith("mysql")) {
					relationAndVal = compareoptionStr  + " '%'"+" isnull(cast(" + targettablename + "." + tfieldname + " as varchar(4000)),'')"+"'%'";
					elserelationAndVal = elsecompareoptionStr  + " '%'"+" isnull(cast(" + targettablename + "." + tfieldname + " as varchar(4000)),'')"+"'%'";
				}
				if (dbtype.startsWith("oracle") || dbtype.startsWith("db2")) {
					relationAndVal = compareoptionStr  + " '%' ||nvl(cast(" + targettablename + "." + tfieldname + " as varchar2(4000)),'')|| '%' ";
					elserelationAndVal = elsecompareoptionStr  + " '%' ||nvl(cast(" + targettablename + "." + tfieldname + " as varchar2(4000)),'')|| '%' ";
				}
				if (dbtype.startsWith("postgresql") ) {
					relationAndVal = compareoptionStr  + " '%' ||isnull(cast(" + targettablename + "." + tfieldname + " as varchar(4000)),'')|| '%' ";
					elserelationAndVal = elsecompareoptionStr  + " '%' ||isnull(cast(" + targettablename + "." + tfieldname + " as varchar(4000)),'')|| '%' ";
				}
				if (dbtype.startsWith("sqlserver")) {
					relationAndVal = compareoptionStr + " '%' + isnull(cast(" + targettablename + "." + tfieldname + " as varchar(4000)),'')+ '%' ";
					elserelationAndVal = elsecompareoptionStr + " '%' + isnull(cast(" + targettablename + "." + tfieldname + " as varchar(4000)),'')+ '%' ";
				}
				truesql += tablename + "." + fieldname + " " + relationAndVal;
				elsesql += tablename + "." + fieldname + " " + elserelationAndVal;
			}else if(relation == 8||relation == 9){// 属于  不属于
				compareoptionStr =(relation == 8)?" in ":" not in ";
				elsecompareoptionStr = (relation == 8)?" not in ":" in ";
				relationAndVal += compareoptionStr +" (" + targettablename + "." + tfieldname + ")";
				elserelationAndVal += elsecompareoptionStr +" (" + targettablename + "." + tfieldname + ")";
				truesql += tablename + "." + fieldname + " " + relationAndVal;
				elsesql += tablename + "." + fieldname + " " + elserelationAndVal;
			}else{
				if (relation == 0) {
					compareoptionStr = ">";
					elsecompareoptionStr ="<=";
				} else if (relation == 1) {
					compareoptionStr = ">=";
					elsecompareoptionStr ="<";
				} else if (relation == 2) {
					compareoptionStr = "<";
					elsecompareoptionStr =">=";
				} else if (relation == 3) {
					compareoptionStr = "<=";
					elsecompareoptionStr =">";
				} else if (relation == 4) {
					compareoptionStr = "=";
					elsecompareoptionStr ="!=";
				} else if (relation == 5) {
					compareoptionStr = "!=";
					elsecompareoptionStr ="=";
				}
				if (htmltype.equals("2") && fieldtype.equals("1") && dbtype.startsWith("sqlserver")) {//多行文本
					//relationAndVal = " isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'') " + compareoptionStr + " '" + targetValue + "'";
					relationAndVal = " isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'') " + compareoptionStr + " " + "isnull(cast(" + targettablename + "." + tfieldname + " as varchar(4000)),'')";
					elserelationAndVal = " isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'') " + elsecompareoptionStr + " " + "isnull(cast(" + targettablename + "." + tfieldname + " as varchar(4000)),'')";
					truesql +=relationAndVal;
					elsesql +=elserelationAndVal;
				}else{
					relationAndVal = compareoptionStr + " " + targettablename + "." + tfieldname ;
					elserelationAndVal = elsecompareoptionStr + " " + targettablename + "." + tfieldname ;
					truesql += tablename + "." + fieldname + " " + relationAndVal;
					elsesql += tablename + "." + fieldname + " " + elserelationAndVal;
				}

			}

		}else {
			String relationAndVal = "";
			String elserelationAndVal="";
			if (targetValue.equals("null") && (htmltype.equals("5") || htmltype.equals("4"))) { //select或者checkbox 框为null
				if (relation == 4) {//等于
					relationAndVal = " is null ";
					elserelationAndVal = " is not null ";
				} else if (relation == 5) {//不等于
					relationAndVal = " is not null ";
					elserelationAndVal ="is null";
				} else if (relation == 6) {//包含
					relationAndVal = " is null ";
					elserelationAndVal = " is not null ";
				} else {//不包含
					relationAndVal = " is not null ";
					elserelationAndVal = " is null ";
				}
				truesql += tablename + "." + fieldname + " " + relationAndVal;
				elsesql += tablename + "." + fieldname + " " + elserelationAndVal;
			} else {
				if ((htmltype.equals("5") && fieldtype.equals("2")) || (htmltype.equals("3") && (fieldtype.equals("17")
						|| fieldtype.equals("18") || fieldtype.equals("37") || fieldtype.equals("57")
						|| fieldtype.equals("65") || fieldtype.equals("135") || fieldtype.equals("152") || fieldtype.equals("162")
						|| fieldtype.equals("166") || fieldtype.equals("168") || fieldtype.equals("170") || fieldtype.equals("184")
						|| fieldtype.equals("194") || fieldtype.equals("257") || fieldtype.equals("261")
						|| fieldtype.equals("269") || fieldtype.equals("278")))) {
					String[] fieldvaluevar = targetValue.split(",");
					if (relation == 6) { //包含
						for (int i = 0; i < fieldvaluevar.length; i++) {
							if (i == 0) {
								if (dbtype.startsWith("mysql")) {
									relationAndVal += " concat(','," + tablename + "." + fieldname + ",',') like '%," + fieldvaluevar[i] + ",%'";
									elserelationAndVal += " concat(','," + tablename + "." + fieldname + ",',') not like '%," + fieldvaluevar[i] + ",%'";
								}
								if (dbtype.startsWith("oracle") || dbtype.startsWith("db2")||dbtype.startsWith("postgresql")) {
									relationAndVal += " ','||" + tablename + "." + fieldname + "||',' like '%," + fieldvaluevar[i] + ",%'";
									elserelationAndVal += " ','||" + tablename + "." + fieldname + "||',' not like '%," + fieldvaluevar[i] + ",%'";
								}
								if (dbtype.startsWith("sqlserver")) {
									relationAndVal += " ','+isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'')+',' like '%," + fieldvaluevar[i] + ",%'";
									elserelationAndVal += " ','+isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'')+',' not like '%," + fieldvaluevar[i] + ",%'";
								}
							} else {
								if (dbtype.startsWith("mysql")) {
									relationAndVal += " AND concat(','," + tablename + "." + fieldname + ",',') like '%," + fieldvaluevar[i] + ",%'";
									elserelationAndVal += " AND concat(','," + tablename + "." + fieldname + ",',')  not like '%," + fieldvaluevar[i] + ",%'";
								}
								if (dbtype.startsWith("oracle") || dbtype.startsWith("db2")) {
									relationAndVal += " AND ','||" + tablename + "." + fieldname + "||',' like '%," + fieldvaluevar[i] + ",%'";
									elserelationAndVal += " AND ','||" + tablename + "." + fieldname + "||',' not like '%," + fieldvaluevar[i] + ",%'";
								}
								if (dbtype.startsWith("postgresql")) {
									relationAndVal += " AND ','||" + tablename + "." + fieldname + "||',' like '%," + fieldvaluevar[i] + ",%'";
									elserelationAndVal += " AND ','||" + tablename + "." + fieldname + "||',' not like '%," + fieldvaluevar[i] + ",%'";
								}
								if (dbtype.startsWith("sqlserver")) {
									relationAndVal += " AND ','+isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'')+',' like '%," + fieldvaluevar[i] + ",%'";
									elserelationAndVal += " AND ','+isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'')+',' not like '%," + fieldvaluevar[i] + ",%'";
								}
							}
//							if (dbtype.startsWith("oracle") || dbtype.startsWith("db2")) {
//								relationAndVal += " ','||" + tablename + "." + fieldname + "||',' like '%," + fieldvaluevar[i] + ",%'";
//								elserelationAndVal += " ','||" + tablename + "." + fieldname + "||',' not like '%," + fieldvaluevar[i] + ",%'";
//							}
//							if (dbtype.startsWith("postgresql")) {
//								relationAndVal += " ','||" + tablename + "." + fieldname + "||',' like '%," + fieldvaluevar[i] + ",%'";
//								elserelationAndVal += " ','||" + tablename + "." + fieldname + "||',' not like '%," + fieldvaluevar[i] + ",%'";
//							}
//							if (dbtype.startsWith("sqlserver")) {
//								relationAndVal += " ','+isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'')+',' like '%," + fieldvaluevar[i] + ",%'";
//								elserelationAndVal += " ','+isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'')+',' not like '%," + fieldvaluevar[i] + ",%'";
//							}
						}
					} else if (relation == 7) { //不包含
						for (int i = 0; i < fieldvaluevar.length; i++) {
							if (i == 0) {
								if (dbtype.startsWith("mysql")) {
									relationAndVal += " concat(','," + tablename + "." + fieldname + ",',') not like '%," + fieldvaluevar[i] + ",%'";
									elserelationAndVal += " concat(','," + tablename + "." + fieldname + ",',') like '%," + fieldvaluevar[i] + ",%'";
								}
								if (dbtype.startsWith("oracle") || dbtype.startsWith("db2")) {
									relationAndVal += " ','||" + tablename + "." + fieldname + "||',' not like '%," + fieldvaluevar[i] + ",%'";
									elserelationAndVal += " ','||" + tablename + "." + fieldname + "||',' like '%," + fieldvaluevar[i] + ",%'";
								}
								if (dbtype.startsWith("sqlserver")) {
									relationAndVal += " ','+isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'')+',' not like '%," + fieldvaluevar[i] + ",%'";
									elserelationAndVal += " ','+isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'')+','  like '%," + fieldvaluevar[i] + ",%'";
								}
							} else {
								if (dbtype.startsWith("mysql")) {
									relationAndVal += " AND concat(','," + tablename + "." + fieldname + ",',') not like '%," + fieldvaluevar[i] + ",%'";
									elserelationAndVal += " AND concat(','," + tablename + "." + fieldname + ",',')  like '%," + fieldvaluevar[i] + ",%'";
								}
								if (dbtype.startsWith("oracle") || dbtype.startsWith("db2")) {
									relationAndVal += " AND ','||" + tablename + "." + fieldname + "||',' not like '%," + fieldvaluevar[i] + ",%'";
									elserelationAndVal += " AND ','||" + tablename + "." + fieldname + "||','  like '%," + fieldvaluevar[i] + ",%'";
								}
								if (dbtype.startsWith("postgresql")) {
									relationAndVal += " AND ','||" + tablename + "." + fieldname + "||',' not like '%," + fieldvaluevar[i] + ",%'";
									elserelationAndVal += " AND ','||" + tablename + "." + fieldname + "||','  like '%," + fieldvaluevar[i] + ",%'";
								}
								if (dbtype.startsWith("sqlserver")) {
									relationAndVal += " AND ','+isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'')+',' not like '%," + fieldvaluevar[i] + ",%'";
									elserelationAndVal += " AND ','+isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'')+',' like '%," + fieldvaluevar[i] + ",%'";
								}
							}
//							if (dbtype.startsWith("oracle") || dbtype.startsWith("db2")) {
//								relationAndVal += " ','||" + tablename + "." + fieldname + "||',' not like '%," + fieldvaluevar[i] + ",%'";
//								elserelationAndVal += " ','||" + tablename + "." + fieldname + "||',' like '%," + fieldvaluevar[i] + ",%'";
//							}
//							if (dbtype.startsWith("postgresql")) {
//								relationAndVal += " ','||" + tablename + "." + fieldname + "||',' not like '%," + fieldvaluevar[i] + ",%'";
//								elserelationAndVal += " ','||" + tablename + "." + fieldname + "||',' like '%," + fieldvaluevar[i] + ",%'";
//							}
//							if (dbtype.startsWith("sqlserver")) {
//								relationAndVal += " ','+isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'')+',' not like '%," + fieldvaluevar[i] + ",%'";
//								elserelationAndVal += " ','+isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'')+','  like '%," + fieldvaluevar[i] + ",%'";
//							}
						}
					} else {
						boolean isneedf = true;
						if (htmltype.equals("4")) {//checkbox
							isneedf = false;
						} else if (htmltype.equals("1") && (fieldtype.equals("2") || fieldtype.equals("3"))) {//整数浮点数
							isneedf = false;
						} else if (htmltype.equals("5") && fieldtype.equals("2")) {
							isneedf = true;
						} else if (htmltype.equals("5")) {//select框
							isneedf = false;
						}

						String compareoptionStr = "";
						String elsecompareoptionStr = "";
						if (relation == 0) {
							compareoptionStr = ">";
							elsecompareoptionStr="<=";
						} else if (relation == 1) {
							compareoptionStr = ">=";
							elsecompareoptionStr="<";
						} else if (relation == 2) {
							compareoptionStr = "<";
							elsecompareoptionStr=">=";
						} else if (relation == 3) {
							compareoptionStr = "<=";
							elsecompareoptionStr=">";
						} else if (relation == 4) {
							compareoptionStr = "=";
							elsecompareoptionStr="!=";
						} else if (relation == 5) {
							compareoptionStr = "!=";
							elsecompareoptionStr="=";
						}
						if (isneedf) {
							relationAndVal = compareoptionStr + " '" + targetValue + "'";
							elserelationAndVal =  elsecompareoptionStr + " '" + targetValue + "'";
						} else {
							relationAndVal = compareoptionStr + " " + targetValue;
							elserelationAndVal = elsecompareoptionStr + " " + targetValue;
						}
					}
					if (relation == 6 || relation == 7) {
						truesql += "( " + relationAndVal + " )";
						elsesql += "( " + elserelationAndVal + " )";
					} else {
						truesql += tablename + "." + fieldname + " " + relationAndVal;
						elsesql += tablename + "." + fieldname + " " + elserelationAndVal;
					}
				} else {

					String fieldvalueObj = "";
					if (fieldtype.equals("256")) {//自定义树形
						String[] fieldvaluevar = targetValue.split(",");
						for (int i = 0; i < fieldvaluevar.length; i++) {
							fieldvalueObj += "'" + fieldvaluevar[i] + "',";
						}
						fieldvalueObj = fieldvalueObj.substring(0, fieldvalueObj.length() - 1);
					}
					if (relation == 6) {//包含
						relationAndVal = " like " + "'%" + targetValue + "%'";
						elserelationAndVal = " not like " + "'%" + targetValue + "%'";
					} else if (relation == 7) {//不包含
						relationAndVal = " not like '%" + targetValue + "%'";
						elserelationAndVal = " like '%" + targetValue + "%'";
					} else if (relation == 8) {//属于
						if (fieldtype.equals("256")) {
							relationAndVal += " in (" + fieldvalueObj + ")";
							elserelationAndVal += " not in (" + fieldvalueObj + ")";
						} else {
							relationAndVal += " in (" + targetValue + ")";
							elserelationAndVal += " not in (" + targetValue + ")";
						}
					} else if (relation == 9) {//不属于
						if (fieldtype.equals("256")) {
							relationAndVal += " not  in (" + fieldvalueObj + ")";
							elserelationAndVal += " in (" + fieldvalueObj + ")";
						} else {
							relationAndVal += " not  in (" + targetValue + ")";
							elserelationAndVal += " in (" + targetValue + ")";
						}
					} else {
						boolean isneedf = true;
						if (htmltype.equals("4")) {//checkbox
							isneedf = false;
						} else if (htmltype.equals("1") && (fieldtype.equals("2") || fieldtype.equals("3") || fieldtype.equals("4"))) {//整数浮点数金额转换
							isneedf = false;
						} else if (htmltype.equals("5")) {//select框
							isneedf = false;
						}

						String compareoptionStr = "";
						String elsecompareoptionStr = "";
						if (relation == 0) {
							compareoptionStr = ">";
							elsecompareoptionStr ="<=";
						} else if (relation == 1) {
							compareoptionStr = ">=";
							elsecompareoptionStr="<";
						} else if (relation == 2) {
							compareoptionStr = "<";
							elsecompareoptionStr=">=";
						} else if (relation == 3) {
							compareoptionStr = "<=";
							elsecompareoptionStr=">";
						} else if (relation == 4) {
							compareoptionStr = "=";
							elsecompareoptionStr="!=";
						} else if (relation == 5) {
							compareoptionStr = "!=";
							elsecompareoptionStr ="=";
						}

						if (htmltype.equals("2") && fieldtype.equals("1") && dbtype.startsWith("sqlserver")) {//多行文本
							relationAndVal = " isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'') " + compareoptionStr + " '" + targetValue + "'";
							elserelationAndVal = " isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'') " + elsecompareoptionStr + " '" + targetValue + "'";
						}else if(htmltype.equals("6")&& dbtype.startsWith("sqlserver")){//附件,图片
							relationAndVal = " isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'') " + compareoptionStr + " '" + targetValue + "'";
							elserelationAndVal = " isnull(cast(" + tablename + "." + fieldname + " as varchar(4000)),'') " + elsecompareoptionStr + " '" + targetValue + "'";
						}else {
							if ("".equals(targetValue)) {//添加可以为空
								String ifnull = "is null";
								String elsenull ="is null";
								if (relation == 4) {
									ifnull = "is null";
									elsenull ="is not null";
								} else if (relation == 5) {
									ifnull = "is not null";
									elsenull ="is null";
								}
								if (isneedf) {
									String str = (dbtype.startsWith("oracle") || dbtype.startsWith("db2"))?" or ":" and ";
									if(relation == 5){
										relationAndVal = compareoptionStr + " '" + targetValue + "'" + str + tablename + "." + fieldname + " " + ifnull;
										elserelationAndVal = elsecompareoptionStr + " '" + targetValue + "'" + " and " + tablename + "." + fieldname + " " + elsenull;
									}else if(relation == 4){
										relationAndVal = compareoptionStr + " '" + targetValue + "'" + " or " + tablename + "." + fieldname + " " + ifnull;
										elserelationAndVal = elsecompareoptionStr + " '" + targetValue + "'" + " and " + tablename + "." + fieldname + " " + elsenull;
									}
								} else {
									relationAndVal = ifnull;
									elserelationAndVal = elsenull;
								}
							} else {
								if (isneedf) {
									relationAndVal = compareoptionStr + " '" + targetValue + "'";
									elserelationAndVal = elsecompareoptionStr + " '" + targetValue + "'";
								} else {
									relationAndVal = compareoptionStr + " " + targetValue;
									elserelationAndVal = elsecompareoptionStr + " " + targetValue;
								}
							}

						}
					}
					if ("".equals(targetValue)) {//添加可以为空

						if (htmltype.equals("2") && fieldtype.equals("1") && dbtype.startsWith("sqlserver")) {//多行文本
							truesql += relationAndVal;
							elsesql += elserelationAndVal;
						}else if(htmltype.equals("6")&& dbtype.startsWith("sqlserver")){//附件,图片
							truesql += relationAndVal;
							elsesql += elserelationAndVal;
						}else {
							boolean isneedf = true;
							if (htmltype.equals("4")) {//checkbox
								isneedf = false;
							} else if (htmltype.equals("1") && (fieldtype.equals("2") || fieldtype.equals("3"))) {//整数浮点数
								isneedf = false;
							} else if (htmltype.equals("5")) {//select框
								isneedf = false;
							}
							if (isneedf) {
								truesql += "(";
								elsesql += "(";
							}
							truesql += tablename + "." + fieldname + " " + relationAndVal;
							elsesql += tablename + "." + fieldname + " " + elserelationAndVal;
							if (isneedf) {
								truesql += ")";
								elsesql += ")";
							}
						}
					} else {
						if (htmltype.equals("2") && fieldtype.equals("1") && dbtype.startsWith("sqlserver")) {//多行文本
							truesql += relationAndVal;
							elsesql += elserelationAndVal;
						}else if(htmltype.equals("6")&& dbtype.startsWith("sqlserver")){//附件,图片
							truesql += relationAndVal;
							elsesql += elserelationAndVal;
						}else {
							truesql += tablename + "." + fieldname + " " + relationAndVal;
							elsesql += tablename + "." + fieldname + " " + elserelationAndVal;
						}
					}
				}
			}
		}
		data.put("trueSql",truesql);
		data.put("elseSql",elsesql);
		return data;



	}



	//获取此条件下的字段赋值情况
	private Map<String,Map<String,Object>> getFieldInfo(String id){
		Map<String,Map<String,Object>>  resultmap = new HashMap<String,Map<String,Object>>();

		RecordSet rs = new RecordSet();
		RecordSet rs1 = new RecordSet();
		String sql="select  * from  cube_fieldexpinfo  where mainid =?";
		rs.executeQuery(sql,id);
		while (rs.next()){
			String targetValue = Util.null2String(rs.getString("targetValue"));// 字段表达式 if(xxx=yyy) 指代 yyy
			String tablefrom = Util.null2String(rs.getString("tablefrom"));//字段赋值 字段来源表单(建模表单) 0主表 1明细1 2 x...
			String fieldid =  Util.null2String(rs.getString("fieldid"));// 字段表达式 xxx=yyy  指代xxx
			String fieldformula = Util.null2String(rs.getString("fieldformula"));//字段表达式 1 常量 2系统变量 3 业务公式
			Map<String,Object>  data = BrowserHelper.constructMap("fieldid",fieldid,"fieldformula",fieldformula,"targetValue",
					targetValue,"tablefrom",tablefrom);
			resultmap.put(fieldid,data);
		}
		return resultmap;
	}


	private String processData(String tablename,String fieldname,Map<String,Object> data,String fromTab,String oldValue,Map rowMap,String changeDetail,String tarfieldhtmltype,String tarfieldtype){

		String targetValue = Util.null2String(data.get("targetValue"));// 条件表达式 if(xxx=yyy) 指代 yyy
		//targetValue = targetValue.replace(" ","");
		String tablefrom = Util.null2String(data.get("tablefrom"));//字段赋值 字段来源表单(建模表单) 0主表 1明细1 2 x...
		String fieldid =  Util.null2String(data.get("fieldid"));// 字段表达式 xxx=yyy  指代xxx
		String fieldformula = Util.null2String(data.get("fieldformula"));//字段表达式 1 常量 2系统变量 3 业务公式

		String newtargetValue="";

		if("1".equals(fieldformula)){//常量
			newtargetValue =targetValue;
		}else if("2".equals(fieldformula)){//系统变量
			CustomTreeData customTreeData = new CustomTreeData();
			customTreeData.setUser(wf_user);
			newtargetValue = customTreeData.replaceParam(targetValue);//替换变量
		}else{//业务公式
			String type ="";
			//本来是考虑 SUM() 左右的括号的，现在是字段名中也有括号，所以不能替换
			//targetValue = targetValue.replaceAll("\\（","(").replaceAll("\\）",")");
			WorkFlowToModeLogService workFlowToModeLogService = new WorkFlowToModeLogService();
			tablename="maintable".equals(tablename)?"{21778}":"{516854}";
			try {
				if(targetValue.startsWith("SUM")){//合计公式
					type="sum";
					//String tempvalue=targetValue.replace("SUM(","").replace(")","").replace("，",",");
					String tempvalue = targetValue.replace("SUM(","").substring(0,targetValue.replace("SUM(","").lastIndexOf(")")).replace("，",",");
					//判断是否数字
					List<Double> list = new ArrayList<Double>();
					String[] valueArr = tempvalue.split(",");
					boolean isInt= false;
					boolean isfloat= false;
					int qfws=0;
					if(valueArr!=null && valueArr.length>0){
						for(String value : valueArr){
							if(isNum(value)){
								list.add(Util.getDoubleValue(value,0));
							}else{//非数字先进行转换
								String tem = changeData(value,fromTab,type,rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype);
								if(tem.startsWith("fieldhtmltype_1_type_2_")){// 参数值是整数。
									isInt = true;
									tem = tem.replace("fieldhtmltype_1_type_2_","");
								}
								if(tem.indexOf("_qfws")>0){//参数值是浮点数
									isfloat  = true;
									qfws = Util.getIntValue(tem.split("_qfws")[0],2);
									tem = tem.split("_qfws")[1];
								}
								double dvalue = Util.getDoubleValue(tem,-1008611);
								String  trueValue = tem;
								String  trueParm = value;
								if(dvalue==-1008611){//值异常
									execStatus="2";
									//	conditionLog.add("<br/><span style=\"word-wrap: break-word;\">建模表单：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">字段：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">公式：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> 公式参数：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> 公式参数值：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
									conditionLog.add("<br/><span style=\"word-wrap: break-word;\">{516402}：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">{33331}：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">{18125}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516403}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516404}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
								}
								list.add(dvalue);
							}
						}
					}
					//求和
					newtargetValue=WorkflowToModeFormulaUtil.sum(list);
					if("1".equals(tarfieldhtmltype)&&"2".equals(tarfieldtype)){ // 建模接收字段是整数
						newtargetValue =  newtargetValue.split("\\.")[0];
					}
					if(isfloat&&"1".equals(tarfieldhtmltype)&&"1".equals(tarfieldtype)){
						newtargetValue = Util.toDecimalDigits(newtargetValue,qfws);
					}
				}else if(targetValue.startsWith("ABS")){// 取绝对值
					type="ABS";
					//String tempvalue=targetValue.replace("ABS(","").replace(")","").replace("，",",");
					String tempvalue = targetValue.replace("ABS(","").substring(0,targetValue.replace("ABS(","").lastIndexOf(")")).replace("，",",");
					boolean isInt= false;
					boolean isfloat= false;
					int qfws=0;
					if(isNum(tempvalue)){//是数字直接取绝对值
						newtargetValue=WorkflowToModeFormulaUtil.abs(Util.getDoubleValue(tempvalue,0));
					}else{//非数字先进行转换
						//此函数必须是1个数字类型字段
						if(tempvalue.split(",").length>=2){
							String  trueValue = "{516411}";
							String  trueParm = tempvalue;
							execStatus="2";
							conditionLog.add("<br/><span style=\"word-wrap: break-word;\">{516402}：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">{33331}：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">{18125}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516403}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516404}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
							return oldValue;
						}
						String tem = changeData(tempvalue,fromTab,type,rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype);
						if(tem.startsWith("fieldhtmltype_1_type_2_")){// 参数值是整数。
							isInt = true;
							tem = tem.replace("fieldhtmltype_1_type_2_","");
						}
						if(tem.indexOf("_qfws")>0){//参数值是浮点数
							isfloat  = true;
							qfws = Util.getIntValue(tem.split("_qfws")[0],2);
							tem = tem.split("_qfws")[1];
						}
						double dvalue = Util.getDoubleValue(tem,-1008611);
						String  trueValue = tem;
						String  trueParm = tempvalue;
						if(dvalue==-1008611){//值异常
							execStatus="2";
							conditionLog.add("<br/><span style=\"word-wrap: break-word;\">{516402}：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">{33331}：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">{18125}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516403}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516404}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
						}
						newtargetValue=WorkflowToModeFormulaUtil.abs(dvalue);
						if(isInt){
							newtargetValue =  newtargetValue.split("\\.")[0];
						}
						if(isfloat&&"1".equals(tarfieldhtmltype)&&"1".equals(tarfieldtype)){
							newtargetValue = Util.toDecimalDigits(newtargetValue,qfws);
						}
					}
				}else if(targetValue.startsWith("AVERAGE")){// 平均值
					type="AVERAGE";
					//String tempvalue=targetValue.replace("AVERAGE(","").replace(")","").replace("，",",");
					String tempvalue = targetValue.replace("AVERAGE(","").substring(0,targetValue.replace("AVERAGE(","").lastIndexOf(")")).replace("，",",");
					List<Double> list = new ArrayList<Double>();
					String[] valueArr = tempvalue.split(",");
					boolean isInt= false;
					boolean isfloat= false;
					int qfws=0;
					if(valueArr!=null && valueArr.length>0){
						for(String value : valueArr){
							if(isNum(value)){
								list.add(Util.getDoubleValue(value,0));
							}else{//非数字先进行转换
								String tem = changeData(value,fromTab,type,rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype);
								if(tem.startsWith("fieldhtmltype_1_type_2_")){// 参数值是整数。
									isInt = true;
									tem = tem.replace("fieldhtmltype_1_type_2_","");
								}
								if(tem.indexOf("_qfws")>0){//参数值是浮点数
									isfloat  = true;
									qfws = Util.getIntValue(tem.split("_qfws")[0],2);
									tem = tem.split("_qfws")[1];
								}
								double dvalue = Util.getDoubleValue(tem,-1008611);
								String  trueValue = tem;
								String  trueParm = value;
								if(dvalue==-1008611){//值异常
									execStatus="2";
									conditionLog.add("<br/><span style=\"word-wrap: break-word;\">{516402}：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">{33331}：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">{18125}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516403}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516404}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
								}
								list.add(dvalue);
							}
						}
					}
					newtargetValue=WorkflowToModeFormulaUtil.average(list);
					if(isInt){
						newtargetValue =  newtargetValue.split("\\.")[0];
					}
					if(isfloat&&"1".equals(tarfieldhtmltype)&&"1".equals(tarfieldtype)){
						newtargetValue = Util.toDecimalDigits(newtargetValue,qfws);
					}
				}else if(targetValue.startsWith("JJCC")){// 用法：JJCC(数字1*数字2)

				}else if(targetValue.startsWith("YEAR")){// 示例：YEAR(2019-08-29)返回2019
					type="YEAR";
					//String tempvalue=targetValue.replace("YEAR(","").replace(")","").replace("，",",");
					String tempvalue = targetValue.replace("YEAR(","").substring(0,targetValue.replace("YEAR(","").lastIndexOf(")")).replace("，",",");
					//判断是否是日期值，或者字段值
					if(isDate(tempvalue)){
						newtargetValue = WorkflowToModeFormulaUtil.year(tempvalue);
					}else{
						//此函数必须是1个日期值
						if(tempvalue.split(",").length>=2){
							String  trueValue = "{516408}";
							String  trueParm = tempvalue;
							execStatus="2";
							conditionLog.add("<br/><span style=\"word-wrap: break-word;\">{516402}：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">{33331}：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">{18125}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516403}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516404}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
							return oldValue;
						}
						newtargetValue = changeData(tempvalue,fromTab,type,rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype);
						String  trueValue = "".equals(newtargetValue)?"{385284}":newtargetValue;
						String  trueParm = tempvalue;
						if(!isDate(newtargetValue)){//字段返回值不是日期格式
							execStatus="2";
							conditionLog.add("<br/><span style=\"word-wrap: break-word;\">{516402}：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">{33331}：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">{18125}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516403}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516404}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
						}
						newtargetValue = "".equals(newtargetValue)?newtargetValue:(isDate(newtargetValue)?WorkflowToModeFormulaUtil.year(newtargetValue):oldValue);
					}
				}else if(targetValue.startsWith("MONTH")){// 示例：MONTH(2019-08-29)返回08
					type="MONTH";
					//String tempvalue=targetValue.replace("MONTH(","").replace(")","").replace("，",",");
					String tempvalue = targetValue.replace("MONTH(","").substring(0,targetValue.replace("MONTH(","").lastIndexOf(")")).replace("，",",");
					//判断是否是日期值，或者字段值
					if(isDate(tempvalue)){
						newtargetValue = WorkflowToModeFormulaUtil.month(tempvalue);
					}else{
						//此函数必须是1个日期值
						if(tempvalue.split(",").length>=2){
							String  trueValue = "{516408}";
							String  trueParm = tempvalue;
							execStatus="2";
							conditionLog.add("<br/><span style=\"word-wrap: break-word;\">{516402}：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">{33331}：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">{18125}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516403}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516404}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
							return oldValue;
						}
						newtargetValue = changeData(tempvalue,fromTab,type,rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype);
						String  trueValue = "".equals(newtargetValue)?"{385284}":newtargetValue;
						String  trueParm = tempvalue;
						if(!isDate(newtargetValue)){//字段返回值不是日期格式
							execStatus="2";
							conditionLog.add("<br/><span style=\"word-wrap: break-word;\">{516402}：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">{33331}：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">{18125}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516403}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516404}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
						}
						newtargetValue = "".equals(newtargetValue)?newtargetValue:(isDate(newtargetValue)?WorkflowToModeFormulaUtil.month(newtargetValue):oldValue);
					}
				}else if(targetValue.startsWith("DAYS")){// 示例：DAYS(2019-08-29,2019-08-30)返回1
					type="DAYS";
					//String tempvalue=targetValue.replace("DAYS(","").replace(")","").replace("，",",");
					String tempvalue = targetValue.replace("DAYS(","").substring(0,targetValue.replace("DAYS(","").lastIndexOf(")")).replace("，",",");
					//此函数必须是两个日期值
					if(tempvalue.split(",").length!=2){
						String  trueValue = "{516410}";
						String  trueParm = tempvalue;
						execStatus="2";
						conditionLog.add("<br/><span style=\"word-wrap: break-word;\">{516402}：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">{33331}：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">{18125}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516403}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516404}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
						return oldValue;
					}
					List<String> list = new ArrayList<String>();
					String[] valueArr = tempvalue.split(",");
					if(valueArr!=null && valueArr.length>0){
						for(String value : valueArr){
							if(isDate(value)){
								list.add(Util.null2String(value));
							}else{//非日期常量先进行转换
								String tem = changeData(value,fromTab,type,rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype);
								if(isDate(Util.null2String(tem))){
									list.add(Util.null2String(tem));
								}else{
									String  trueValue = "".equals(tem)?"{385284}":tem;;
									String  trueParm = value;
									if(!isDate(newtargetValue)){//字段返回值不是日期格式
										execStatus="2";
										conditionLog.add("<br/><span style=\"word-wrap: break-word;\">{516402}：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">{33331}：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">{18125}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516403}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516404}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
									}
								}
							}
						}
					}
					if(list.size()==2){
						newtargetValue = WorkflowToModeFormulaUtil.days(list);
					}
				}else if(targetValue.startsWith("DAY")){//示例：DAY(2019-08-29)返回29
					type="DAY";
					//String tempvalue=targetValue.replace("DAY(","").replace(")","").replace("，",",");
					String tempvalue = targetValue.replace("DAY(","").substring(0,targetValue.replace("DAY(","").lastIndexOf(")")).replace("，",",");
					//判断是否是日期值，或者字段值
					if(isDate(tempvalue)){
						newtargetValue = WorkflowToModeFormulaUtil.day(tempvalue);
					}else{
						//此函数必须是1个日期值
						if(tempvalue.split(",").length>=2){
							String  trueValue = "{516408}";
							String  trueParm = tempvalue;
							execStatus="2";
							conditionLog.add("<br/><span style=\"word-wrap: break-word;\">{516402}：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">{33331}：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">{18125}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516403}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516404}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
							return oldValue;
						}
						newtargetValue = changeData(tempvalue,fromTab,type,rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype);
						String  trueValue = "".equals(newtargetValue)?"{385284}":newtargetValue;
						String  trueParm = tempvalue;
						if(!isDate(newtargetValue)){//字段返回值不是日期格式
							execStatus="2";
							conditionLog.add("<br/><span style=\"word-wrap: break-word;\">{516402}：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">{33331}：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">{18125}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516403}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516404}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
						}
						newtargetValue = "".equals(newtargetValue)?newtargetValue:(isDate(newtargetValue)?WorkflowToModeFormulaUtil.day(newtargetValue):oldValue);
					}
				}else if(targetValue.startsWith("DATEDELTA")){// 示例：DATEDELTA(2019-08-29,1)返回2019-08-30
					type="DATEDELTA";
					//String tempvalue=targetValue.replace("DATEDELTA(","").replace(")","").replace("，",",");
					String tempvalue = targetValue.replace("DATEDELTA(","").substring(0,targetValue.replace("DATEDELTA(","").lastIndexOf(")")).replace("，",",");
					//此函数必须是第一个为日期，第二个为数字
					if(tempvalue.split(",").length!=2){
						String  trueValue = "{516409}";
						String  trueParm = tempvalue;
						execStatus="2";
						conditionLog.add("<br/><span style=\"word-wrap: break-word;\">{516402}：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">{33331}：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">{18125}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516403}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516404}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
						return oldValue;
					}
					String[] valueArr = tempvalue.split(",");
					if(valueArr!=null && valueArr.length>0){
						String date="";
						int num=0;
						if(isDate(valueArr[0])&&isNum(valueArr[1])){
							newtargetValue = WorkflowToModeFormulaUtil.datedelta(valueArr[0],Util.getIntValue(valueArr[1],0));
						}else{
							if(!isDate(valueArr[0])){//非日期
								date = changeData(valueArr[0],fromTab,type,rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype);
								if(!isDate(date)){//字段返回值不是日期格式
									String  trueValue ="".equals(date)?"{385284}":date; //"".equals(date)?"为空":date;;
									String  trueParm = valueArr[0];
									execStatus="2";
									conditionLog.add("<br/><span style=\"word-wrap: break-word;\">{516402}：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">{33331}：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">{18125}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516403}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516404}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
								}
							}else{
								date = valueArr[0];
							}
							if(!isNum(valueArr[1])){// 不是数字
								num = Util.getIntValue(changeData(valueArr[1],fromTab,type,rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype),0);
								if(!isNum(String.valueOf(num))){
									String  trueValue = num+"";
									String  trueParm = valueArr[1];
									execStatus="2";
									conditionLog.add("<br/><span style=\"word-wrap: break-word;\">{516402}：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">{33331}：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">{18125}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516403}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516404}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
								}
							}else{
								num = Util.getIntValue(valueArr[1]);
							}
							newtargetValue = WorkflowToModeFormulaUtil.datedelta(date,num);
						}
					}
				}else if(targetValue.startsWith("CONCATENATE")){//示例：CONCATENATE("李","小明")返回李小明
					type="CONCATENATE";
					//String tempvalue=targetValue.replace("CONCATENATE(","").replace(")","").replace("，",",");
					String tempvalue = targetValue.replace("CONCATENATE(","").substring(0,targetValue.replace("CONCATENATE(","").lastIndexOf(")")).replace("，",",");
					//每一项只要不是主表 ,明细表开头，都认为是字符串直接拼接
					List<String> list = new ArrayList<String>();
					String[] valueArr = tempvalue.split(",");
					if(valueArr!=null && valueArr.length>0){
						for(String value : valueArr){
							if(value.indexOf(SystemEnv.getHtmlLabelName(21778, 7))>-1||value.indexOf(SystemEnv.getHtmlLabelName(17463, 7))>-1){//主表 明细
								list.add(Util.null2String(changeData(value,fromTab,type,rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype)));
							}else{
								list.add(value);
							}
						}
					}
					newtargetValue = WorkflowToModeFormulaUtil.concatenate(list);
				}else if(targetValue.startsWith("REPLACE")){//用法：REPLACE(oldtext, startnum, numchars, newtext） 示例：REPLACE(1235,1,3,234)返回2345
					type="REPLACE";
					//String tempvalue=targetValue.replace("REPLACE(","").replace(")","").replace("，",",");
					String tempvalue = targetValue.replace("REPLACE(","").substring(0,targetValue.replace("REPLACE(","").lastIndexOf(")")).replace("，",",");
					//此函数必须是第一个为oldtext，第二个为数字,第三个为数字,第四个为newtext
					if(tempvalue.split(",").length!=4){
						String  trueValue = "{516412}";
						String  trueParm = tempvalue;
						execStatus="2";
						conditionLog.add("<br/><span style=\"word-wrap: break-word;\">{516402}：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">{33331}：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">{18125}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516403}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516404}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
					}
					String[] valueArr = tempvalue.split(",");
					if(!isNum(valueArr[1])&&!isNum(valueArr[2])){// 参数2，参数3 必须是数字
						String  trueValue = "{516413}";
						String  trueParm = tempvalue;
						execStatus="2";
						conditionLog.add("<br/><span style=\"word-wrap: break-word;\">{516402}：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">{33331}：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">{18125}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516403}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516404}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
					}
					String oldtext = valueArr[0];
					String newtext = valueArr[3];
					if(oldtext.indexOf(SystemEnv.getHtmlLabelName(21778, 7))>-1||oldtext.indexOf(SystemEnv.getHtmlLabelName(17463, 7))>-1){//主表 明细
						oldtext = Util.null2String(changeData(oldtext,fromTab,type,rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype));
					}
					if(newtext.indexOf(SystemEnv.getHtmlLabelName(21778, 7))>-1||newtext.indexOf(SystemEnv.getHtmlLabelName(17463, 7))>-1){//主表 明细
						newtext = Util.null2String(changeData(newtext,fromTab,type,rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype));
					}
					newtargetValue = WorkflowToModeFormulaUtil.replace(oldtext,Util.getIntValue(valueArr[1],0),Util.getIntValue(valueArr[2],0),newtext);
				}else if(targetValue.startsWith("IF")){//用法 IF(条件表达式,为true时返回的值,为false时返回的值) IF(SUM(主表.整数,1),"我是真的","我是假的")
					type="IF";

					String firstParam = targetValue.substring(targetValue.indexOf("(") + 1, targetValue.indexOf(","));
					String rightParam = targetValue.substring(targetValue.indexOf(",") + 1);
					rightParam = rightParam.replaceAll("\\)","");

					if(firstParam.indexOf("SUM")>-1||firstParam.indexOf("ABS")>-1||firstParam.indexOf("AVERAGE")>-1||firstParam.indexOf("JJCC")>-1
							||firstParam.indexOf("YEAR")>-1||firstParam.indexOf("MONTH")>-1||firstParam.indexOf("DAY")>-1||firstParam.indexOf("DAYS")>-1
							||firstParam.indexOf("DATEDELTA")>-1||firstParam.indexOf("CONCATENATE")>-1||firstParam.indexOf("REPLACE")>-1){

						String fh =getFh(Util.null2String(rightParam.split(",")[0]));
						String compareStr = Util.null2String(rightParam.split(",")[0].split(fh)[1]);
						String trueValue = Util.null2String(rightParam.split(",")[1]);
						String falseValue = Util.null2String(rightParam.split(",")[2]);

						compareStr=getIsFieldName(compareStr)?changeData(compareStr,fromTab,"",rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype):compareStr;
						trueValue =getIsFieldName(trueValue)?changeData(trueValue,fromTab,"",rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype):trueValue;
						falseValue=getIsFieldName(falseValue)?changeData(falseValue,fromTab,"",rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype):falseValue;

						String firstStr = firstParam+ Util.null2String(rightParam.split(",")[0].split(fh)[0])+")";

						Map<String,Object> map = new HashMap<String,Object>();
						map.put("targetValue",firstStr);
						map.put("fieldformula",3);
						firstStr= processData(tablename,fieldname,map, fromTab, oldValue, rowMap, changeDetail,"","");
						newtargetValue = getIfValue(firstStr,fh,compareStr,trueValue,falseValue);

					}else{//第一部分不包含公式
						//判断第一部分包含什么符号
						String fh =getFh(Util.null2String(firstParam));
						String firstStr =Util.null2String(firstParam.split(fh)[0]);
						String compareStr =Util.null2String(firstParam.split(fh)[1]);
						String trueValue = Util.null2String(rightParam.split(",")[0]);
						String falseValue = Util.null2String(rightParam.split(",")[1]);
						// 如果 if公式 取值部分是明细字段，但是赋值字段是主表，那么取明细的最后一行的该字段值
						firstStr  =getIsFieldName(firstStr)?changeData(firstStr,fromTab,"",rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype):firstStr;
						compareStr=getIsFieldName(compareStr)?changeData(compareStr,fromTab,"",rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype):compareStr;
						trueValue =getIsFieldName(trueValue)?changeData(trueValue,fromTab,"",rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype):trueValue;
						falseValue=getIsFieldName(falseValue)?changeData(falseValue,fromTab,"",rowMap,changeDetail,targetValue,tarfieldhtmltype,tarfieldtype):falseValue;

						newtargetValue = getIfValue(firstStr,fh,compareStr,trueValue,falseValue);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				String  trueValue = "{516414}";
				String  trueParm = targetValue;
				execStatus="2";
				conditionLog.add("<br/><span style=\"word-wrap: break-word;\">{516402}：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">{33331}：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">{18125}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516403}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> {516404}：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
			}
		}
		return newtargetValue;
	}

	//判断是否是数字
	private Boolean isNum(String value){
		boolean isNumber = false;
		Pattern pattern = Pattern.compile("^(-)?[1-9][0-9]*$"); //验证正数或负数
		Matcher isNum = pattern.matcher(value.trim());
		if( isNum.matches() ){
			isNumber = true;
		}
		return  isNumber;
	}
	/**
	 * 判断日期格式和范围
	 */
	private static boolean isDate(String date){
		String rexp = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		Pattern pat = Pattern.compile(rexp);
		Matcher mat = pat.matcher(date);
		boolean dateType = mat.matches();
		return dateType;
	}


	private String changeData(String value,String fromTab,String type,Map detailRowMap,String changeDetail,String targetValue,String tarfieldhtmltype,String tarfieldtype) {//value 目前是字段的显示名，因为这个客户可以自己输入
		if (value.split("\\.").length != 2) {
			return "";
		}

		value = value.replace("[","").replace("]","");
		RecordSet rs = new RecordSet();
		String tablename = "";
		String sql = "select * from workflow_bill  where id = " + wf_formid;
		rs.executeSql(sql);
		if (rs.next()) {
			tablename = Util.null2String(rs.getString("tablename")).toLowerCase();
		}
		sql = "select w.* from workflow_billfield w ,HtmlLabelInfo h  where billid = ?  and w.fieldlabel =h.indexid and h.labelname=?";
		// c.a=d.b+c    c.a=d.b+d.c 等
		if (value.indexOf(SystemEnv.getHtmlLabelName(21778, 7)) > -1) {//主表
			sql+=" and (detailtable is null  or  detailtable ='')";
			String newValue = "";
			String fieldhtmltype="";
			String ftype="";
			int qfws=2;
			value = value.replace(SystemEnv.getHtmlLabelName(21778, 7)+".", "").trim();//主表.
			rs.executeQuery(sql, wf_formid, value);
			if (rs.next()) {
				fieldhtmltype =rs.getString("fieldhtmltype");
				ftype	=rs.getString("type");
				qfws = Util.getIntValue(rs.getString("qfws"),2);
				newValue = (String) mainFieldValuesMap.get(rs.getString("fieldname"));
				if("".equals(newValue)&&"1".equals(fieldhtmltype)&&("2".equals(ftype)||"3".equals(ftype)||"4".equals(ftype)||"5".equals(ftype))){//整数。浮点数等
					newValue ="0";
				}
			}
			if("1".equals(fieldhtmltype)&&"5".equals(ftype)&&("sum".equals(type)||"ABS".equals(type)||"AVERAGE".equals(type))){//金额千分为
				newValue = newValue.replaceAll(",","");
			}
			if("1".equals(fieldhtmltype)&&"2".equals(ftype)&&("sum".equals(type)||"ABS".equals(type)||"AVERAGE".equals(type))){//整数合计等只要整数部分
				return  "fieldhtmltype_1_type_2_"+newValue;
			}
			if("1".equals(tarfieldhtmltype)&&"1".equals(tarfieldtype)){// 目标字段是单行文本
				if("1".equals(fieldhtmltype)&&"3".equals(ftype)&&("sum".equals(type)||"ABS".equals(type)||"AVERAGE".equals(type))){// 浮点数
					return  qfws+"_qfws"+newValue;
				}
			}
			return newValue;
		} else if (value.indexOf(SystemEnv.getHtmlLabelName(17463, 7)) > -1) {
			try {
				String labelname = value.split("\\.")[1];
				int num = Util.getIntValue(value.split("\\.")[0].replace(SystemEnv.getHtmlLabelName(19325, 7),""));
				String fieldid = "";
				String fieldname = "";
				String fieldhtmltype="";
				String ftype="";
				int qfws=2;
				sql+=" and detailtable =?";
				rs.executeQuery(sql, wf_formid, labelname,tablename+"_dt"+num);
				if (rs.next()) {
					fieldid = rs.getString("id");
					fieldname = rs.getString("fieldname");
					fieldhtmltype =rs.getString("fieldhtmltype");
					ftype	=rs.getString("type");
					qfws = Util.getIntValue(rs.getString("qfws"),2);
				}
				if("".equals(fieldid)){
					return "";
				}

				if("detail".equals(fromTab)){//明细字段 返回当前行
					if(!changeDetail.equals(tablename+"_dt"+num)){//判断rowList 属于那个明细表， 跟公式中写的 明细表是否一致。 不一致直接返回
						return "";
					}
					String fieldvalue = Util.null2String(detailRowMap.get(fieldname));
					if ("sum".equals(type) || "ABS".equals(type) || "AVERAGE".equals(type)) {
						if("".equals(fieldvalue)&&"1".equals(fieldhtmltype)&&("2".equals(ftype)||"3".equals(ftype)||"4".equals(ftype)||"5".equals(ftype))){//整数。浮点数等
							fieldvalue ="0";
						}
						if("1".equals(fieldhtmltype)&&"5".equals(ftype)){//金额千分位
							fieldvalue = fieldvalue.replaceAll(",","");
						}
					}
					return  fieldvalue;
				}else{
					String detailtable = Util.null2String((String) Wf_Field_Table_Map.get(fieldid));
					ArrayList<HashMap> list = (ArrayList) detailFieldValuesMaps.get(detailtable.toLowerCase());
					if ("sum".equals(type) || "ABS".equals(type) || "AVERAGE".equals(type)) {//是合计/绝对值/平均值  c= a+d.b   那么取这条流程的这个明细表的，这个字段所在列的和
						double newValue = 0;
						if(list!=null && list.size()>0) {
							for (HashMap rowMap : list) {
								String fieldvalue = Util.null2String(rowMap.get(fieldname));
								if("".equals(fieldvalue)&&"1".equals(fieldhtmltype)&&("2".equals(ftype)||"3".equals(ftype)||"4".equals(ftype)||"5".equals(ftype))){//整数。浮点数等
									fieldvalue ="0";
								}
								if("1".equals(fieldhtmltype)&&"5".equals(ftype)){//金额千分位
									fieldvalue = fieldvalue.replaceAll(",","");
								}
								double dvalue = Util.getDoubleValue(fieldvalue,-1008611);
								String  trueValue = fieldvalue;
								String  trueParm = value;
								if(dvalue==-1008611){//值异常
									execStatus="2";
									//	conditionLog.add("<br/><span style=\"word-wrap: break-word;\">建模表单：</span><span style=\"color:red;word-wrap: break-word;\">"+tablename+"</span>&nbsp;<span style=\"word-wrap: break-word;\">字段：</span><span style=\"color:red;word-wrap: break-word;\">"+fieldname+"</span>&nbsp; <span style=\"word-wrap: break-word;\">公式：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+targetValue+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> 公式参数：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueParm+"</span>&nbsp;<span style=\"word-wrap: break-word;\"> 公式参数值：</span>&nbsp;<span style=\"color:red;word-wrap: break-word;\">"+trueValue+"</span><br/>");
									return String.valueOf(fieldvalue);
								}
								newValue += dvalue;
							}
						}
						if("1".equals(fieldhtmltype)&&"2".equals(ftype)){//整数合计等只要整数部分
							return  "fieldhtmltype_1_type_2_"+String.valueOf(newValue);
							//return String.valueOf(newValue).split("\\.")[0];
						}
						if("1".equals(tarfieldhtmltype)&&"1".equals(tarfieldtype)){// 目标字段是单行文本
							if("1".equals(fieldhtmltype)&&"3".equals(ftype)){// 浮点数
								return  qfws+"_qfws"+newValue;
							}
						}
						return String.valueOf(newValue);
					} else if ("YEAR".equals(type) || "MONTH".equals(type) || "DAY".equals(type)||"DAYS".equals(type)
							||"DATEDELTA".equals(type)||"CONCATENATE".equals(type)||"REPLACE".equals(type)
							||"".equals(type)) {// YEAR,MONTH,DAY,DAYS,DATEDELTA,CONCATENATE,REPLACE 函数，明细返回最后一条的此字段的值
						if(list==null||list.size()<0){
							return "";
						}
						if(formtype.indexOf("detail")>-1){//明细表触发 还是要返回当前行
							return Util.null2String(detailRowMap.get(fieldname));
						}else{
							HashMap rowMap = list.get(list.size() - 1);
							return Util.null2String(rowMap.get(fieldname));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
		return value;
	}

	private  String getIfValue(String firstStr,String fh,String compareStr,String trueValue,String falseValue) {

		if(isNum(firstStr)&&isNum(compareStr)){
			if(fh.indexOf("!=")>-1 || fh.indexOf("<>")>-1){
                return Util.getIntValue(firstStr)!=Util.getIntValue(compareStr)?trueValue:falseValue;
            }else if(fh.indexOf(">=")>-1){
				return Util.getIntValue(firstStr)>=Util.getIntValue(compareStr)?trueValue:falseValue;
			}else if(fh.indexOf(">")>-1){
				return Util.getIntValue(firstStr)>Util.getIntValue(compareStr)?trueValue:falseValue;
			}else if(fh.indexOf("<=")>-1){
				return Util.getIntValue(firstStr)<=Util.getIntValue(compareStr)?trueValue:falseValue;
			}else if(fh.indexOf("<")>-1){
				return Util.getIntValue(firstStr)<Util.getIntValue(compareStr)?trueValue:falseValue;
			}else if(fh.indexOf("=")>-1){
				return Util.getIntValue(firstStr)==Util.getIntValue(compareStr)?trueValue:falseValue;
			}
		}else{
			if(fh.indexOf("!=")>-1 || fh.indexOf("<>")>-1){
                return (firstStr.compareTo(compareStr)!=0)?trueValue:falseValue;
            }else if(fh.indexOf(">=")>-1){
				return (firstStr.compareTo(compareStr)>=0)?trueValue:falseValue;
			}else if(fh.indexOf(">")>-1){
				return (firstStr.compareTo(compareStr)>0)?trueValue:falseValue;
			}else if(fh.indexOf("<=")>-1){
				return (firstStr.compareTo(compareStr)<0)?trueValue:falseValue;
			}else if(fh.indexOf("<")>-1){
				return (firstStr.compareTo(compareStr)<0)?trueValue:falseValue;
			}else if(fh.indexOf("=")>-1){
				return (firstStr.compareTo(compareStr)==0)?trueValue:falseValue;
			}
		}
		return "";
	}


	private  String getFh(String str) {
		if(str.indexOf("!=")>-1 || str.indexOf("<>")>-1){
            return "!=";
        }else if(str.indexOf(">=")>-1){
			return ">=";
		}else if(str.indexOf(">")>-1){
			return ">";
		}else if(str.indexOf("<=")>-1){
			return "<=";
		}else if(str.indexOf("<")>-1){
			return "<";
		}else if(str.indexOf("=")>-1){
			return "=";
		}
		return "";
	}
	//判断该字段是否是常量
	private  boolean getIsFieldName(String str) {
		if(str.indexOf(SystemEnv.getHtmlLabelName(21778, 7))>-1||str.indexOf(SystemEnv.getHtmlLabelName(17463, 7))>-1){//主表 明细
			return true;
		}
		return  false;
	}


	private void writeConditionAssignmentLog(String billid){
		String msg="";
		String trueMsg="";
		String 	errmsg = execError + "<br/>" + execSeparator + "<br/>{516405}<br/>" + execSeparator +"<br/>";
		WorkFlowToModeLogService workFlowToModeLogService = new WorkFlowToModeLogService();
		for (int i = 0; i < conditionLog.size() ; i++) {
			msg+=conditionLog.get(i);
		}
		if(this.execStatus.equals("2")){
			trueMsg=errmsg+msg;
		}
		if(!"".equals(msg)){
			workFlowToModeLogService.updateSuccesslogByWtlid(wtlid,execStatus,trueMsg,billid);
		}

	}

	private int getFieldAignExpNum( int curSetid){
		RecordSet  rs = new RecordSet();
		rs.executeQuery("select count(*) as num from  cube_field_assign_exp where wftomodeid=?",curSetid);
		int num =0;
		if(rs.next()){
			num = rs.getInt("num");
		}
		return num;
	}
    private boolean isExistCode(int modeid){
		RecordSet  rs = new RecordSet();
		String codeSql = "select count(a.id) as num from ModeCode a left join workflow_billfield b on a.codeFieldId=b.id where a.modeid="+modeid;
		boolean existCode = false;
		try {
			rs.executeQuery(codeSql);
			while (rs.next()) {
				int num = Util.getIntValue(rs.getString("num"));
				if (num>0){
					existCode = true;
					}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return existCode;
	}

}