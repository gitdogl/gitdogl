package weaver.formmode.interfaces.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.formmode.data.ModeDataIdUpdate;
import weaver.formmode.log.FormmodeLog;
import weaver.formmode.service.FormInfoService;
import weaver.formmode.setup.ModeRightInfo;
import weaver.formmode.task.TaskService;
import weaver.formmode.virtualform.VirtualFormHandler;
import weaver.general.Util;
import weaver.hrm.User;

import com.engine.cube.biz.CodeBuilder;
import com.weaver.formmodel.util.DateHelper;
import com.weaver.formmodel.util.StringHelper;

public class WorkflowToModeAfter extends FormmodeLog implements Runnable {
	private String action = "";
	private WorkflowToMode workflowToMode = null;
	private User user = null;
	private String ip = "";
	
	
	public WorkflowToModeAfter(User user,WorkflowToMode workflowToMode) {
		super();
		this.user = user;
		this.workflowToMode = workflowToMode;
	}

	@Override
	public void run() {
		if("doAfter".equals(action)){
			doAfter();
		}
	}
	
	public void doAfter(){
		RecordSet rs = new RecordSet();
		FormInfoService formInfoService = new FormInfoService();
		for(Map<String,Object> map : workflowToMode.getRightArray()) {
			String sql = Util.null2String(map.get("sql"));
			String tablename = Util.null2String(map.get("tablename"));
			if(StringHelper.isEmpty(sql)) continue;
			rs.executeSql(sql);
			if(rs.next()) {
				int billid = Util.getIntValue(rs.getString("id"));
				int creater = Util.getIntValue(rs.getString("modedatacreater"));
				int formmodeid = Util.getIntValue(rs.getString("formmodeid"));
				int formid = Util.getIntValue(rs.getString("formid"));
				int flag = Util.getIntValue(rs.getString("flag"));
				ModeRightInfo modeRightInfo = new ModeRightInfo();
				List<Map<String, Object>> needlogFieldList = formInfoService.getNeedlogField(formid);
				Map<String, Object> nowData =tablename.equals("")?new HashMap<String, Object>():workflowToMode.getLogFieldData(tablename, billid, needlogFieldList);
				Map<String, Object> oldData = (Map<String, Object>) map.get("olddata");
				Map<String, Map<String, Map<String, Object>>> nowData_detail=workflowToMode.getLogFieldData_detail(billid, needlogFieldList);
				Map<String, Map<String, Map<String, Object>>> oldData_detail=(Map<String, Map<String, Map<String, Object>>>) map.get("oldData_detail");		
				if(flag==1) { // 新建数据
					//CodeBuilder cbuild = new CodeBuilder(formmodeid);
					//cbuild.getModeCodeStr(formid,billid);//生成编号
					
					//记录日志
					workflowToMode.saveModeViewLog(formmodeid,billid,user,ip,"1",needlogFieldList,oldData,nowData,oldData_detail,nowData_detail);
					
					modeRightInfo.setNewRight(true);
					modeRightInfo.editModeDataShare(creater,formmodeid,billid);//新建的时候添加共享
					modeRightInfo.addDocShare(creater, formmodeid, billid);
					
					TaskService taskService = new TaskService();
					taskService.setModeid(formmodeid);
					taskService.setBillid(billid);
					taskService.setAction("create");
                    taskService.setCurrentUser(user);
					Thread task = new Thread(taskService);
					task.start();
				} else {
					//更新最后修改人和修改时间
		            ModeDataIdUpdate modeDataIdUpdate = new ModeDataIdUpdate();
		            RecordSet rSet = new RecordSet();
		    		if(!VirtualFormHandler.isVirtualForm(formid)){	//如果是虚拟表单，则不需要创建这些字段。
		    			String modifydatatime = DateHelper.getCurrentDate()+" "+DateHelper.getCurrentTime();
		    			modeDataIdUpdate.updateModifyInfo(tablename);
		    			if(billid>0){
		    				//更新最后修改人和最后修改时间
		    				String modifysql = "update "+tablename+" set modedatamodifier=?,modedatamodifydatetime=? where id=?";
		    				rSet.executeUpdate(modifysql, user.getUID(),modifydatatime,billid);
		    			}
		    		}
					workflowToMode.saveModeViewLog(formmodeid,billid,user,ip,"2",needlogFieldList,oldData,nowData,oldData_detail,nowData_detail);
					modeRightInfo.rebuildModeDataShareByEdit(creater, formmodeid, billid);
					modeRightInfo.addDocShare(creater, formmodeid, billid);
				}
			}
		}
	}


	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}