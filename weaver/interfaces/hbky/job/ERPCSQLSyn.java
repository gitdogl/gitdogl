/* 
 *
 * Copyright (C) 1999-2012 IFLYTEK Inc.All Rights Reserved. 
 * 
 * FileName：PersonOrgSyn.java
 * 
 * Description：人员组织同步任务计划类
 * 
 * History：
 * Version   Author      Date            Operation 
 * 1.0    卫   2015年1月23日上午10:54:14         Create   
 */
package weaver.interfaces.hbky.job;

import java.util.ArrayList;
import java.util.List;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weaver.general.TimeUtil;
import weaver.interfaces.schedule.BaseCronJob;

/**
 * 定时执行同步操作
 * 
 * @author 卫
 * @version 1.0
 * 
 */
public class ERPCSQLSyn extends BaseCronJob {

	/**
	 * 日志输出
	 */
	private Log log = LogFactory.getLog(ERPCSQLSyn.class.getName());

	/**
	 * 重写 execute方法 执行同步操作 {@inheritDoc}
	 */
	@Override
	public void execute() {

		log.info("自动清理抄送数据报表" + TimeUtil.getCurrentTimeString());

		// 开始获取考勤报表
		try {

			// String sql =
			// "delete from epintegratedinfo@oa_erp s  where  s.itemid in ( select requestid||'_'||id  itemid  from workflow_currentoperator  where  isremark in (2,4))";
			List<String> oalist = new ArrayList<String>();
			List<String> erplist = new ArrayList<String>();
			String sql = " select requestid||'_'||id  itemid  from workflow_currentoperator  where  isremark in (2,4) and operatedate>='"+TimeUtil.getCurrentDateString()+"'";

			RecordSet rs = new RecordSet();

			rs.execute(sql);

			while (rs.next()) {
				oalist.add(rs.getString("itemid"));
			}

			String sqldata = " select itemid from epintegratedinfo where systemname='OA'  and receivetime> to_char (sysdate-7,'YYYY-MM-DD')  and  state =0 ";

			RecordSetDataSource rsdata = new RecordSetDataSource("LCZSDB");
			rsdata.execute(sqldata);
			while (rsdata.next()) {
				erplist.add(rsdata.getString("itemid"));
			}
			
			for(String oaitem:oalist){
				//存在记录删除
				if(erplist.indexOf(oaitem)>-1)
				{
					sqldata = "delete from epintegratedinfo where itemid='"+oaitem+"'  ";
					rsdata.execute(sqldata);
				}
				
			}
			

		} catch (Exception s) {
			s.printStackTrace();
		}

	}

}
