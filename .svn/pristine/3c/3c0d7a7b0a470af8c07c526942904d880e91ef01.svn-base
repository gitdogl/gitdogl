//package weaver.workflow.action;
//
//import weaver.conn.RecordSet;
//import weaver.formmode.interfaces.action.WorkflowToMode;
//import weaver.general.BaseBean;
//import weaver.general.StaticObj;
//import weaver.general.Util;
//import weaver.integration.logging.Logger;
//import weaver.integration.logging.LoggerFactory;
//import weaver.interfaces.workflow.action.Action;
//import weaver.soa.workflow.request.RequestInfo;
//import weaver.soa.workflow.request.RequestService;
//import weaver.workflow.dmlaction.commands.actions.DMLAction;
//
//public class BaseAction extends BaseBean implements Action{
//
//    private Logger newlog = LoggerFactory.getLogger(BaseAction.class);
//	private int isTriggerReject;
//	public int getIsTriggerReject() {
//		return isTriggerReject;
//	}
//	public void setIsTriggerReject(int isTriggerReject) {
//		this.isTriggerReject = isTriggerReject;
//	}
//	private int workflowid;
//	private int nodeid;
//	private int nodelinkid;
//	private int ispreoperator;
//	private int isnewsap;
//
//	public int getIsnewsap() {
//		return isnewsap;
//	}
//	public void setIsnewsap(int isnewsap) {
//		this.isnewsap = isnewsap;
//	}
//	public int getWorkflowid() {
//		return workflowid;
//	}
//	public void setWorkflowid(int workflowid) {
//		this.workflowid = workflowid;
//	}
//	public int getNodeid() {
//		return nodeid;
//	}
//	public void setNodeid(int nodeid) {
//		this.nodeid = nodeid;
//	}
//	public int getNodelinkid() {
//		return nodelinkid;
//	}
//	public void setNodelinkid(int nodelinkid) {
//		this.nodelinkid = nodelinkid;
//	}
//	public int getIspreoperator() {
//		return ispreoperator;
//	}
//	public void setIspreoperator(int ispreoperator) {
//		this.ispreoperator = ispreoperator;
//	}
//	/*
//	 * 3种Acton都在这里总的预处理
//	 * 根据类型再走到其他类里面去具体处理
//	 */
//	public String execute(RequestInfo requestInfo){
//
//	    newlog.info("--------------------进入节点前变化了、后操作动作-----------------------------------");
//
//	    try{
//	    	 newlog.error("workflowid="+this.workflowid+"this.nodeid="+this.nodeid+"this.ispreoperator"+this.ispreoperator+"this.nodelinkid"+this.nodelinkid);
//
//			 //老版本的sap解析
//			 if("0".equals(isnewsap+"")){
//				String sql = "select * from workflowactionview where workflowid="+this.workflowid;
//				if(this.nodeid > 0){
//					sql = sql + " and nodeid="+this.nodeid+" and ispreoperator="+this.ispreoperator;
//				}else{
//					sql = sql + " and nodelinkid="+this.nodelinkid;
//				}
//				sql = sql + " order by actionorder";
//				System.out.println("sql : "+sql);
//				RecordSet rs = new RecordSet();
//				rs.execute(sql);
//				//同一个节点设置了多个流程转数据，在此只循环一次，因为在workflowtToMode中已经循环了
//				boolean workflowtomde_flag = true;
//				while(rs.next()){
//
//					newlog.info("触发老的接口");
//					int actiontype_t = Util.getIntValue(rs.getString("actiontype"));
//					String actionid_t = Util.null2String(rs.getString("id"));
//					int isused_t = Util.getIntValue(rs.getString("isused"));
//					try{
//					if(isused_t!=1)
//					{
//						continue;
//					}
//					if(actiontype_t == 0){//dml
//						newlog.info("&&&&  执行dmlaction 流程信息 workflowid : "+workflowid+" requestid :"+requestInfo.getRequestid()+"  start ....................");
//						DMLAction action = new DMLAction();
//	                	action.setWorkflowid(workflowid);
//	                	//objtype 1: 节点自动赋值 0 :出口自动赋值
//	                	action.setNodeid(nodeid);
//	                	action.setNodelinkid(nodelinkid);
//	                	action.setIspreoperator(ispreoperator);
//	                	action.setActionid(Util.getIntValue(actionid_t,0));
//	                    String msg = action.execute(requestInfo);
//	                    newlog.info("&&&&  执行dmlaction 流程信息 workflowid : "+workflowid+" requestid :"+requestInfo.getRequestid()+"  end ....................");
//	                    try{
//	    					ActionLogService actionlogservice=new ActionLogService();
//	    					actionlogservice.save(actionid_t, actiontype_t+"");
//	    					}catch(Exception e){
//	    						 newlog.info("保存action执行日志出错！",e);
//	    					}
//	                    //dml 执行报错。
//	                    if(msg.startsWith("-1"))
//	                    	return msg;
//					}else if(actiontype_t == 1){//webservice
//						newlog.info("&&&&  执行webserviceAction 流程信息 workflowid : "+workflowid+" requestid :"+requestInfo.getRequestid()+"  start ....................");
//						WebServiceAction action = new WebServiceAction();
//						//newlog.info("workflowid : "+workflowid+" nodeid : "+nodeid+" nodelinkid : "+nodelinkid+" actionid_t : "+actionid_t);
//	                	action.setWorkflowid(workflowid);
//	                	//objtype 1: 节点自动赋值 0 :出口自动赋值
//	                	action.setNodeid(nodeid);
//	                	action.setNodelinkid(nodelinkid);
//	                	action.setIspreoperator(ispreoperator);
//	                	action.setActionid(Util.getIntValue(actionid_t,0));
//	                    String msg = action.execute(requestInfo);
//	                    newlog.info("&&&&  执行webserviceAction 流程信息 workflowid : "+workflowid+" requestid :"+requestInfo.getRequestid()+"  msg:"+msg+"  end ....................");
//	                    try{
//	    					ActionLogService actionlogservice=new ActionLogService();
//	    					actionlogservice.save(actionid_t, actiontype_t+"");
//	    					}catch(Exception e){
//	    						 newlog.info("保存action执行日志出错！",e);
//	    					}
//	                    if(!msg.equals("1"))
//	                    	return msg;
//					}else if(actiontype_t == 2){//sap
//						newlog.info("老版本的sap解析------BaseAction-----------");
//						//System.out.println("&&&&  执行SAPAction 流程信息 workflowid : "+workflowid+" requestid :"+requestInfo.getRequestid()+"  start ....................");
//							SapAction action = new SapAction();
//		                	action.setWorkflowid(workflowid);
//		                	action.setIsnewsap(isnewsap);
//		                	//objtype 1: 节点自动赋值 0 :出口自动赋值
//		                	action.setNodeid(nodeid);
//		                	action.setNodelinkid(nodelinkid);
//		                	action.setIspreoperator(ispreoperator);
//		                	action.setActionid(Util.getIntValue(actionid_t,0));
//		                    String msg = action.execute(requestInfo);
//		                    newlog.info("&&&&  执行SAPAction 流程信息 workflowid : "+workflowid+" requestid :"+requestInfo.getRequestid()+" msg:"+msg+" end ....................");
//		                    try{
//		    					ActionLogService actionlogservice=new ActionLogService();
//		    					actionlogservice.save(actionid_t, actiontype_t+"");
//		    					}catch(Exception e){
//		    						 newlog.info("保存action执行日志出错！",e);
//		    					}
//		                    if(!msg.equals("1"))
//		                    	return msg;
//					}
//					else if(actiontype_t == 3){//老的action
//					    newlog.info("老版本的action解析------BaseAction-----------");
//						//System.out.println("&&&&  执行Action 流程信息 workflowid : "+workflowid+" requestid :"+requestInfo.getRequestid()+"  start ....................");
//						if(actionid_t.indexOf("action.")==-1)
//						{
//							actionid_t = "action."+actionid_t;
//						}
//						if(!actionid_t.equals("action.WorkflowToDoc")){
//							if(actionid_t.equals("action.WorkflowToMode")){
//								if(workflowtomde_flag){
//									WorkflowToMode action = new WorkflowToMode();
//									action.setNodeid(this.nodeid);
//									action.setNodelinkid(this.nodelinkid);
//			                        RequestService requestService=new  RequestService();
//			                        String msg="";
//		                          	msg=action.execute(requestInfo);
//									workflowtomde_flag = false;
//		                          	try{
//		            					ActionLogService actionlogservice=new ActionLogService();
//		            					actionlogservice.save(actionid_t, actiontype_t+"");
//		            					}catch(Exception e){
//		            						 newlog.info("保存action执行日志出错！",e);
//		            					}
//		                          	//action执行失败，则返回
//		                          	if(!msg.equals("1")){
//		                          	    newlog.info("ACTION：[" + actionid_t + "]执行失败，它返回了错误的信息并阻止了流程继续向下流转！");
//		                          	    return msg;
//		                          	}
//								}else{
//									continue;
//								}
//							}else{
//								//System.out.println("+++***");
//		                        Action action= (Action)StaticObj.getServiceByFullname(actionid_t, Action.class);
//		                        RequestService requestService=new  RequestService();
//		                        //String msg=action.execute(requestService.getRequest(requestid));
//		                        String msg="";
//	                          	msg=action.execute(requestInfo);
//
//	                          	try{
//	            					ActionLogService actionlogservice=new ActionLogService();
//	            					actionlogservice.save(actionid_t, actiontype_t+"");
//	            					}catch(Exception e){
//	            						 newlog.info("保存action执行日志出错！",e);
//	            					}
//
//	                          	//action执行失败，则返回
//	                          	if(!msg.equals("1")){
//	                          	    newlog.info("ACTION：[" + actionid_t + "]执行失败，它返回了错误的信息并阻止了流程继续向下流转！");
//	                          	    return msg;
//	                          	}
//
//		                        /*if(!msg.equals("1")){
//		                            throw new Exception(msg);
//		                        }*/
//	                    	}
//							}else{
//								try{
//	            					ActionLogService actionlogservice=new ActionLogService();
//	            					actionlogservice.save(actionid_t, actiontype_t+"");
//	            					}catch(Exception e){
//	            						 newlog.info("保存action执行日志出错！",e);
//	            					}
//							}
//
//						newlog.info("&&&&  执行SAPAction 流程信息 workflowid : "+workflowid+" requestid :"+requestInfo.getRequestid()+"  end ....................");
//					}
//					else if(actiontype_t == 4){//新版本的sap
//						newlog.info("新版本的sap解析------BaseAction-----------");
//						 sql="select * from int_BrowserbaseInfo where mark='"+actionid_t+"'";
//						RecordSet rs02 = new RecordSet();
//						rs02.execute(sql);
//						if(rs02.next()){
//							newlog.info("触发新的接口");
//							String baseid=rs02.getString("id");
//							String mark=rs02.getString("mark");
//							String hpid=rs02.getString("hpid");//所属异构系统
//							String sid="";//数据交互方式
//							//依据异构系统的id,得到异构系统的数据交互方式
//							RecordSet rs03 = new RecordSet();
//							sql="select sid from int_heteProducts where id='"+hpid+"'";
//							if(rs03.execute(sql)&&rs03.next()){
//								 sid=rs03.getString("sid");
//							}
//							 if("3".equals(sid))//RFC的方式---sap的数据源,sap操作
//							{
//								 newlog.info("&&&&  执行SAPAction 流程信息 workflowid : "+workflowid+" requestid :"+requestInfo.getRequestid()+"  start ....................");
//								SapAction action = new SapAction();
//					        	action.setWorkflowid(workflowid);
//					        	//objtype 1: 节点自动赋值 0 :出口自动赋值
//					        	action.setNodeid(nodeid);
//					        	action.setNodelinkid(nodelinkid);
//					        	action.setIspreoperator(ispreoperator);
//					        	action.setIsnewsap(1);
//					        	action.setIsNewMark(mark);//表示是70新的节点后动作开发的标识
//					        	if(this.nodeid > 0){
//					        		action.setLogtype(ispreoperator+"");//新版本的sap日志类型(0是节点后，1是节点前，2浏览按钮，3是出口线)
//					        	}else{
//					        		action.setLogtype("3");//新版本的sap日志类型(0是节点后，1是节点前，2浏览按钮，3是出口线)
//					        	}
//					        	action.setBaseid(baseid);
//					            String msg = action.execute(requestInfo);
//
//					            newlog.info("&&&&  执行SAPAction 流程信息 workflowid : "+workflowid+" requestid :"+requestInfo.getRequestid()+"  end .................... msg="+msg);
//					            try{
//									ActionLogService actionlogservice=new ActionLogService();
//									actionlogservice.save(actionid_t, actiontype_t+"");
//									}catch(Exception e){
//										 newlog.info("保存action执行日志出错！",e);
//									}
//							}
//						}
//						//return Action.SUCCESS;
//					}
//					}catch (Exception e){
//						 newlog.info("执行接口异常！",e);
//					}
//
//
//				}
//
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//			writeLog(e);
//			return "";
//		}
//		 try {
//				//新版本的sap解析
//				if("1".equals(isnewsap+"")){
//					return Action.SUCCESS;
//					/*
//					newlog.info("新版本的sap解析------BaseAction-----------");
//					//zzl---start
//					//ispreoperator=是否节点后附件操作(用于判断是在节点前界面展示，还是节点后界面展示)0—表示是节点后操作，1—表示是节点前操作
//					//nodelinkid出口id
//					String sql="select * from int_BrowserbaseInfo where w_fid="+this.workflowid;
//					if(this.nodeid > 0){
//						sql = sql + " and w_nodeid="+this.nodeid+" and w_enable=1 and ispreoperator="+this.ispreoperator;
//					}else{
//						sql = sql + " and w_enable=1 and nodelinkid="+this.nodelinkid;
//					}
//					sql+=" order by  w_actionorder,id";//按执行顺序和id排序，越小的数据越先执行
//					//System.out.println("nodeid="+this.nodeid);
//					//System.out.println("nodelinkid="+this.nodelinkid);
//					//System.out.println("ispreoperator="+this.ispreoperator);
//					//System.out.println("查找节点后动作执行的集成浏览按钮信息"+sql);
//					RecordSet rs02 = new RecordSet();
//					rs02.execute(sql);
//					while(rs02.next()){
//						newlog.info("触发新的接口");
//						String baseid=rs02.getString("id");
//						String mark=rs02.getString("mark");
//						String hpid=rs02.getString("hpid");//所属异构系统
//						String sid="";//数据交互方式
//						//依据异构系统的id,得到异构系统的数据交互方式
//						RecordSet rs03 = new RecordSet();
//						sql="select sid from int_heteProducts where id='"+hpid+"'";
//						if(rs03.execute(sql)&&rs03.next()){
//							 sid=rs03.getString("sid");
//						}
//						 if("3".equals(sid))//RFC的方式---sap的数据源,sap操作
//						{
//							 newlog.info("&&&&  执行SAPAction 流程信息 workflowid : "+workflowid+" requestid :"+requestInfo.getRequestid()+"  start ....................");
//							SapAction action = new SapAction();
//				        	action.setWorkflowid(workflowid);
//				        	//objtype 1: 节点自动赋值 0 :出口自动赋值
//				        	action.setNodeid(nodeid);
//				        	action.setNodelinkid(nodelinkid);
//				        	action.setIspreoperator(ispreoperator);
//				        	action.setIsnewsap(isnewsap);
//				        	action.setIsNewMark(mark);//表示是70新的节点后动作开发的标识
//				        	if(this.nodeid > 0){
//				        		action.setLogtype(ispreoperator+"");//新版本的sap日志类型(0是节点后，1是节点前，2浏览按钮，3是出口线)
//				        	}else{
//				        		action.setLogtype("3");//新版本的sap日志类型(0是节点后，1是节点前，2浏览按钮，3是出口线)
//				        	}
//				        	action.setBaseid(baseid);
//				            String msg = action.execute(requestInfo);
//				            newlog.info("&&&&  执行SAPAction 流程信息 workflowid : "+workflowid+" requestid :"+requestInfo.getRequestid()+"  end ....................");
//						}
//					}*/
//					//zzl-----end
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				newlog.error(e);
//				return "";
//			}
//
//		/*
//		 * 那么多接口，而且都各不一样。如果只是一个出错了，流程就不能走下去。
//		 * 再次提交流程就会重复调用接口。所以这里统一处理为接口即使出错，也可以让流程走下去。
//		 */
//		return Action.SUCCESS;
//	}
//
//	/*
//	 * 查视图，如果有action，返回true，否则false
//	 * 顺便处理表workflow_addinoperate
//	 * alter table workflow_addinoperate add isnewsap varchar2(50)
//	 * 这块不能升级，
//	 */
//	public boolean checkActionOnNodeOrLink(int workflowid, int nodeid, int nodelinkid, int ispreoperator,int isnewsap){
//		boolean retflag = false;
//		String nullstr=" isnull";
//		RecordSet rs = new RecordSet();
//		if(rs.getDBType().equals("oracle")){
//			nullstr=" nvl";
//		}
//		String type=isnewsap+"";
//		if("1".equals(type))
//		{
//			//System.out.println("新的");
//			try{
//				String sql = "select * from int_BrowserbaseInfo where ";
//				boolean hasAction = false;
//				if(nodeid > 0){
//					if(ispreoperator != 1){
//						ispreoperator = 0;
//					}
//					sql = sql + " w_fid="+workflowid+"  and w_enable=1 and w_nodeid="+nodeid+" and ispreoperator="+ispreoperator;
//				}else{
//					sql = sql + " w_fid="+workflowid+"  and w_enable=1  and nodelinkid="+nodelinkid;
//				}
//				rs.execute(sql);
//				if(rs.next()){
//					hasAction = true;
//				}
//				if(hasAction == true){
//					//System.out.println("存在action");
//					if(nodeid > 0){
//						sql = "select count(*) s from workflow_addinoperate where objid="+nodeid+" and isnode=1 and type=3 and ispreadd="+ispreoperator+" and isnewsap=1";
//					}else{
//						sql = "select count(*) s from workflow_addinoperate where objid="+nodelinkid+" and isnode=0 and type=3  and isnewsap=1";
//					}
//					rs.executeSql(sql);
//					rs.next();
//					if(rs.getInt("s")< 1){
//						if(nodeid > 0){
//							sql = "insert into workflow_addinoperate (objid,workflowid,isnode,type,ispreadd,customervalue,isnewsap,isTriggerReject) values ("+nodeid+","+workflowid+",1,3,"+ispreoperator+",'1','1','"+isTriggerReject+"')";
//						}else{
//							sql = "insert into workflow_addinoperate (objid,workflowid,isnode,type,ispreadd,customervalue,isnewsap,isTriggerReject) values ("+nodelinkid+","+workflowid+",0,3,0,'1','1','"+isTriggerReject+"')";
//						}
//						rs.execute(sql);
//					}else{
//						//验证重复的数据，并且删除掉重复的数据，只保留一条有效数据
//						if(rs.getInt("s")> 1){
//							if(nodeid > 0){
//								sql = "delete workflow_addinoperate where objid="+nodeid+" and isnode=1 and type=3 and ispreadd="+ispreoperator+" and isnewsap=1 and id not in(select max(id) from workflow_addinoperate where objid="+nodeid+" and isnode=1 and type=3 and ispreadd="+ispreoperator+" and isnewsap=1)";
//							}else{
//								sql = "delete  workflow_addinoperate where objid="+nodelinkid+" and isnode=0 and type=3  and isnewsap=1 and id not in(select max(id) from workflow_addinoperate where objid="+nodelinkid+" and isnode=0 and type=3  and isnewsap=1)";
//							}
//							rs.execute(sql);
//						}
//					}
//				}else
//				{
//					//如果不存在Action，直接删就行了，不用再判断了
//					if(nodeid > 0){
//						sql = "delete from workflow_addinoperate where objid="+nodeid+" and isnode=1 and type=3 and ispreadd="+ispreoperator+" and isnewsap=1";
//					}else{
//						sql = "delete from workflow_addinoperate where objid="+nodelinkid+" and isnode=0 and type=3  and  isnewsap=1";
//					}
//					rs.executeSql(sql);
//				}
//			}catch(Exception e){
//				retflag = false;
//				newlog.error(e);
//			}
//			}else{
//				//System.out.println("老的");
//			try{
//					boolean hasAction = false;
//					String sql = "select * from workflowactionview where ";
//					if(nodeid > 0){
//						if(ispreoperator != 1){
//							ispreoperator = 0;
//						}
//						sql = sql + " workflowid="+workflowid+" and nodeid="+nodeid+" and ispreoperator="+ispreoperator;
//					}else{
//						sql = sql + " workflowid="+workflowid+" and nodelinkid="+nodelinkid;
//					}
//					rs.execute(sql);
//					if(rs.next()){
//						hasAction = true;
//					}
//					if(hasAction == true){
//						if(nodeid > 0){
//							sql = "select count(*) s from workflow_addinoperate where objid="+nodeid+" and isnode=1 and type=3 and ispreadd="+ispreoperator+" and  "+nullstr+"(isnewsap,0)=0";
//						}else{
//							sql = "select count(*) s from workflow_addinoperate where objid="+nodelinkid+" and isnode=0 and type=3  and  "+nullstr+"(isnewsap,0)=0";
//						}
//						rs.executeSql(sql);
//						rs.next();
//						if(rs.getInt("s")< 1){
//							if(nodeid > 0){
//								sql = "insert into workflow_addinoperate (objid,workflowid,isnode,type,ispreadd,customervalue,isnewsap,isTriggerReject) values ("+nodeid+","+workflowid+",1,3,"+ispreoperator+",'1','0','"+isTriggerReject+"')";
//							}else{
//								sql = "insert into workflow_addinoperate (objid,workflowid,isnode,type,ispreadd,customervalue,isnewsap,isTriggerReject) values ("+nodelinkid+","+workflowid+",0,3,0,'1','0','"+isTriggerReject+"')";
//							}
//							rs.execute(sql);
//						}else{
//							//验证重复的数据，并且删除掉重复的数据，只保留一条有效数据
//							if(rs.getInt("s")> 1){
//								if(nodeid > 0){
//									sql = "delete workflow_addinoperate where objid="+nodeid+" and isnode=1 and type=3 and ispreadd="+ispreoperator+" and isnewsap=0 and id not in(select max(id) from workflow_addinoperate where objid="+nodeid+" and isnode=1 and type=3 and ispreadd="+ispreoperator+" and  "+nullstr+"(isnewsap,0)=0)";
//								}else{
//									sql = "delete  workflow_addinoperate where objid="+nodelinkid+" and isnode=0 and type=3  and isnewsap=0 and id not in(select max(id) from workflow_addinoperate where objid="+nodelinkid+" and isnode=0 and type=3  and  "+nullstr+"(isnewsap,0)=0)";
//								}
//								rs.execute(sql);
//							}
//						}
//					}else{
//						//如果不存在Action，直接删就行了，不用再判断了
//						if(nodeid > 0){
//							sql = "delete from workflow_addinoperate where objid="+nodeid+" and isnode=1 and type=3 and ispreadd="+ispreoperator+" and "+nullstr+"(isnewsap,0)=0";
//						}else{
//							sql = "delete from workflow_addinoperate where objid="+nodelinkid+" and isnode=0 and type=3  and "+nullstr+"(isnewsap,0)=0";
//						}
//						rs.executeSql(sql);
//					}
//			}catch(Exception e){
//				retflag = false;
//				newlog.error(e);
//			}
//		}
//		return retflag;
//	}
//	/**
//	 * 检查action是否被使用
//	 * @param actionid
//	 * @param formtype
//	 * @return
//	 */
//	public boolean checkFromActionUsed(String actionid,String fromtype)
//	{
//		RecordSet rs = new RecordSet();
//		if(!"".equals(actionid)&&!"".equals(fromtype))
//		{
//			String checksql = "";
//			if(!"3".equals(fromtype))
//			{
//				checksql = "select 1 from (select d.id, "+
//						   "	      '1' as fromtype "+
//						   "	 from formactionset d  "+
//						   "	 union all  "+
//						   "	 select s.id, "+
//						   "	       '2' as fromtype "+
//						   "	  from wsformactionset s) r,workflowactionset s where cast(r.id as varchar(200))=s.interfaceid "+
//						   "	   and r.fromtype=s.interfacetype and r.id="+actionid+" and r.fromtype='"+fromtype+"'";
//				if(rs.getDBType().equals("oracle"))
//				{
//					checksql = "select 1 from (" +
//							   "	  select d.id, "+
//							   "	      '1' as fromtype "+
//							   "	 from formactionset d  "+
//							   "	 union all  "+
//							   "	 select s.id, "+
//							   "	       '2' as fromtype "+
//							   "	  from wsformactionset s) r,workflowactionset s where to_char(r.id)=s.interfaceid "+
//							   "	   and r.fromtype=s.interfacetype and r.id="+actionid+" and r.fromtype='"+fromtype+"'";
//				}
//				newlog.info("checksql : "+checksql);
//				rs.executeSql(checksql);
//				if(rs.next())
//				{
//					return true;
//				}
//			}
//			else
//			{
//				checksql = "select 1 from (select s.id,s.actionname,'3' as fromtype "+
//						   "		  from actionsetting s) r, "+
//						   "	workflowactionset s where r.actionname=s.interfaceid "+
//						   "	and r.fromtype=s.interfacetype and r.actionname='"+actionid+"' and r.fromtype='"+fromtype+"'";
//				newlog.info("checksql : "+checksql);
//				rs.executeSql(checksql);
//				if(rs.next())
//				{
//					return true;
//				}
//			}
//			return false;
//		}
//		return false;
//	}
//}
