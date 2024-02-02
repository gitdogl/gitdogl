package weaver.interfaces.hbky.oa.meet.dao;

import java.util.HashMap;
import java.util.Map;

import weaver.interfaces.hbky.oa.meet.dto.OAUser;
import weaver.interfaces.hbky.oa.meet.util.OADBUtil;


public class BaseDao {

	/**
	 * 根据请求编号返回该请求编号对应的存储表
	 * 
	 * @description
	 * @author 卫
	 * @create 2015年3月27日下午4:43:53
	 * @version 1.0
	 * @param requestid
	 *            请求编号
	 * @return 根据请求编号返回该请求编号对应的存储表
	 * @throws Exception
	 *             获取数据异常
	 */
	public String getTableNameByResquestID(String requestid) throws Exception {
		String tableName = "";
		StringBuffer tablesql = new StringBuffer(
				"select tablename from workflow_bill where id in (select formid from workflow_base where id in (select workflowid from workflow_requestbase where requestid='")
				.append(requestid).append("'))");
		OADBUtil oa = new OADBUtil();
		try {
			oa.setStatementSql(tablesql.toString());
			oa.executeQuery();
			if (oa.next()) {
				tableName = oa.getString("tableName");
			}

			return tableName;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (oa != null) {
				oa.close();
			}
		}

	}

	/**
	 * 获取选择框的值
	 * 
	 * @param id
	 *            oa值
	 * @param fieldid
	 *            oa字段
	 * @return 获取选择框的值
	 * @throws Exception
	 */
	public String getSelectNameByID(String id, String fieldid) throws Exception {
		String selectname = "";
		StringBuffer tablesql = new StringBuffer(
				"select selectname from Workflow_Selectitem where fieldid='")
				.append(fieldid).append("' and selectvalue='").append(id)
				.append("'");
		OADBUtil oa = new OADBUtil();
		try {
			oa.setStatementSql(tablesql.toString());
			oa.executeQuery();
			if (oa.next()) {
				selectname = oa.getString("selectname");
			}

			return selectname;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (oa != null) {
				oa.close();
			}
		}

	}

	/**
	 * 获取选择框的值
	 * 
	 * @param id
	 *            oa值
	 * @param fieldid
	 *            oa字段
	 * @return 获取选择框的值
	 * @throws Exception
	 */
	public Map<String, String> getSelectNameByFieldID(String fieldid)
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();

		StringBuffer tablesql = new StringBuffer(
				"select selectvalue, selectname from Workflow_Selectitem where fieldid='")
				.append(fieldid).append("'");
		OADBUtil oa = new OADBUtil();
		try {
			oa.setStatementSql(tablesql.toString());
			oa.executeQuery();
			while (oa.next()) {
				map.put(oa.getString("selectvalue"), oa.getString("selectname"));
			}

			return map;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (oa != null) {
				oa.close();
			}
		}

	}

	/**
	 * 根据请求编号获取当前节点用户用于提交使用
	 * 
	 * @param requestid
	 *            请求编号
	 * @return
	 * @throws Exception
	 */
	public String getUseidByResquestID(String requestid) throws Exception {
		String userid = "";
		StringBuffer tablesql = new StringBuffer(
				"select userid from workflow_currentoperator where requestid='")
				.append(requestid)
				.append("' and nodeid in (select nownodeid from workflow_nownode where requestid='")
				.append(requestid).append("') and isremark in(0,2)");
		OADBUtil oa = new OADBUtil();
		try {
			oa.setStatementSql(tablesql.toString());
			oa.executeQuery();
			if (oa.next()) {
				userid = oa.getString("userid");
			}

			return userid;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (oa != null) {
				oa.close();
			}
		}

	}

	/**
	 * 根据请求编号返回该请求编号对应的请求标题
	 * 
	 * @description
	 * @author 卫
	 * @create 2015年3月27日下午4:43:53
	 * @version 1.0
	 * @param requestid
	 *            请求编号
	 * @return 根据请求编号返回该请求编号对应的请求标题
	 * @throws Exception
	 *             获取数据异常
	 */
	public String getWorkflowidByResquestID(String requestid) throws Exception {
		String tableName = "";
		StringBuffer tablesql = new StringBuffer(
				"select workflowid from workflow_requestbase where requestid='")
				.append(requestid).append("'");
		OADBUtil oa = new OADBUtil();
		try {
			oa.setStatementSql(tablesql.toString());
			oa.executeQuery();
			if (oa.next()) {
				tableName = oa.getString("workflowid");
			}

			return tableName;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (oa != null) {
				oa.close();
			}
		}

	}

	/**
	 * 根据请求编号返回该请求编号对应的请求标题
	 * 
	 * @description
	 * @author 卫
	 * @create 2015年3月27日下午4:43:53
	 * @version 1.0
	 * @param requestid
	 *            请求编号
	 * @return 根据请求编号返回该请求编号对应的请求标题
	 * @throws Exception
	 *             获取数据异常
	 */
	public String getRequestNameByResquestID(String requestid) throws Exception {
		String requetname = "";
		StringBuffer tablesql = new StringBuffer(
				"select requestname from workflow_requestbase where requestid='")
				.append(requestid).append("'");
		OADBUtil oa = new OADBUtil();
		try {
			oa.setStatementSql(tablesql.toString());
			oa.executeQuery();
			if (oa.next()) {
				requetname = oa.getString("requestname");
			}

			return requetname;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (oa != null) {
				oa.close();
			}
		}

	}
	
	/**
	 * 根据请求编号返回该请求编号对应的请求标题
	 * 
	 * @description
	 * @author 卫
	 * @create 2015年3月27日下午4:43:53
	 * @version 1.0
	 * @param requestid
	 *            请求编号
	 * @return 根据请求编号返回该请求编号对应的请求标题
	 * @throws Exception
	 *             获取数据异常
	 */
	public OAUser getOAUser(String usid) throws Exception {
		OAUser oaUser=new OAUser();
		StringBuffer tablesql = new StringBuffer(
				"select * from hrmresource where id='")
				.append(usid).append("'");
		OADBUtil oa = new OADBUtil();
		try {
			oa.setStatementSql(tablesql.toString());
			oa.executeQuery();
			if (oa.next()) {
				oaUser.setCallNumber(oa.getString("telephone"));
				oaUser.setCellphone(oa.getString("telephone"));
				oaUser.setEmail(oa.getString("email"));
				oaUser.setLastname(oa.getString("lastname"));
				oaUser.setLoginid(oa.getString("loginid"));
				oaUser.setTelephone(oa.getString("telephone"));
				oaUser.setCompany("730");
				
			}

			return oaUser;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (oa != null) {
				oa.close();
			}
		}

	}

}
