//package weaver.interfaces.kd.oa.base;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import weaver.conn.RecordSet;
//import weaver.general.Util;
//import weaver.hrm.User;
//import weaver.interfaces.kd.oa.dto.DepartmentBill;
//import weaver.interfaces.kd.oa.dto.DepartmentDiyBill;
//import weaver.interfaces.kd.oa.dto.ResourceBill;
//import weaver.interfaces.kd.oa.dto.SubcompanyBill;
//import weaver.interfaces.kd.oa.util.OADBUtil;
//
//public class BaseDao {
//	 public User getUserById(int var1) {
//	        RecordSet var2 = new RecordSet();
//	        User var3 = null;
//	        String var4 = "select * from HrmResource where id=" + var1 + "";
//	        var2.executeSql(var4);
//	        if (var2.next()) {
//	            var3 = new User();
//	            var3.setUid(var2.getInt("id"));
//	            var3.setLoginid("loginid");
//	            var3.setFirstname(var2.getString("firstname"));
//	            var3.setLastname(var2.getString("lastname"));
//	            var3.setAliasname(var2.getString("aliasname"));
//	            var3.setTitle(var2.getString("title"));
//	            var3.setTitlelocation(var2.getString("titlelocation"));
//	            var3.setSex(var2.getString("sex"));
//	            String var5 = var2.getString("systemlanguage");
//	            var3.setLanguage(Util.getIntValue(var5, 7));
//	            var3.setTelephone(var2.getString("telephone"));
//	            var3.setMobile(var2.getString("mobile"));
//	            var3.setMobilecall(var2.getString("mobilecall"));
//	            var3.setEmail(var2.getString("email"));
//	            var3.setCountryid(var2.getString("countryid"));
//	            var3.setLocationid(var2.getString("locationid"));
//	            var3.setResourcetype(var2.getString("resourcetype"));
//	            var3.setContractdate(var2.getString("contractdate"));
//	            var3.setJobtitle(var2.getString("jobtitle"));
//	            var3.setJobgroup(var2.getString("jobgroup"));
//	            var3.setJobactivity(var2.getString("jobactivity"));
//	            var3.setJoblevel(var2.getString("joblevel"));
//	            var3.setSeclevel(var2.getString("seclevel"));
//	            var3.setUserDepartment(Util.getIntValue(var2.getString("departmentid"), 0));
//	            var3.setUserSubCompany1(Util.getIntValue(var2.getString("subcompanyid1"), 0));
//	            var3.setUserSubCompany2(Util.getIntValue(var2.getString("subcompanyid2"), 0));
//	            var3.setUserSubCompany3(Util.getIntValue(var2.getString("subcompanyid3"), 0));
//	            var3.setUserSubCompany4(Util.getIntValue(var2.getString("subcompanyid4"), 0));
//	            var3.setManagerid(var2.getString("managerid"));
//	            var3.setAssistantid(var2.getString("assistantid"));
//	            var3.setPurchaselimit(var2.getString("purchaselimit"));
//	            var3.setCurrencyid(var2.getString("currencyid"));
//	            var3.setLogintype("1");
//	            var3.setAccount(var2.getString("account"));
//	        }
//	        var2.executeSql(" SELECT id,loginid,firstname,lastname,systemlanguage,seclevel FROM HrmResourceManager WHERE id=" + var1);
//         if (var2.next()) {
//             var3 = new User();
//             var3.setUid(var1);
//             var3.setLoginid(var2.getString("loginid"));
//             var3.setFirstname(var2.getString("firstname"));
//             var3.setLastname(var2.getString("lastname"));
//             var3.setLanguage(Util.getIntValue(var2.getString("systemlanguage"), 7));
//             var3.setSeclevel(var2.getString("seclevel"));
//             var3.setLogintype("1");
//         }
//			return var3;
//	       }
//	/**
//	 * 附件转正文
//	 * @throws Exception
//	 */
//	public void FjToZw(String docid) throws Exception{
//		StringBuffer tablesql1 = new StringBuffer("update DocDetail set seccategory = 1 , doctype = 2,parentids=null,accessorycount = 0 ,cancopy = null,canremind = null,docExtendName = 'doc',docedition = null,doceditionid = null,maindoc=id where id =").append(docid);
//		StringBuffer tablesql2 = new StringBuffer("update DocImageFile set imagefilename = '正文.doc',docfiletype = 3 ,isextfile='' where docid =").append(docid);
//		OADBUtil oa1 = new OADBUtil();
//		OADBUtil oa2 = new OADBUtil();
//		try {
//			oa1.setStatementSql(tablesql1.toString());
//			oa1.executeUpdate();
//			oa1.close();
//			oa2.setStatementSql(tablesql2.toString());
//			oa2.executeUpdate();
//			oa2.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		} finally {
//			if (oa1 != null) {
//				oa1.close();
//			}
//			if (oa2 != null) {
//				oa2.close();
//			}
//		}
//	}
//
//	public void DeleteTable(String tableName) throws Exception {
//		StringBuffer tablesql = new StringBuffer("delete from ").append(
//				tableName).append(" ");
//		OADBUtil oa = new OADBUtil();
//		try {
//			oa.setStatementSql(tablesql.toString());
//			oa.executeUpdate();
//			oa.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		} finally {
//			if (oa != null) {
//				oa.close();
//			}
//		}
//	}
//
//	/**
//	 * 通过部门id获取自定义信息
//	 *
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	public DepartmentDiyBill getDepartmentDiyBill(String id) throws Exception {
//		DepartmentDiyBill departmentDiyBill = new DepartmentDiyBill();
//		String sql = "select * from hrmdepartmentdefined where  deptid = " + id;
//		OADBUtil oa = new OADBUtil();
//		try {
//			System.out.println(sql);
//			oa.setStatementSql(sql);
//			oa.executeQuery();
//			if (oa.next()) {
//				departmentDiyBill.setBmfgld(oa.getString("Bmfgld"));
//				departmentDiyBill.setBmfzr(oa.getString("bmfzr"));
//				departmentDiyBill.setJzglbmfgld(oa.getString("jzglbmfgld"));
//				departmentDiyBill.setJzglbmfzr(oa.getString("jzglbmfzr"));
//				departmentDiyBill.setU8bmbm(oa.getString("u8bmbm"));
//				departmentDiyBill.setZzjgbmfgld(oa.getString("zzjgbmfgld"));
//				departmentDiyBill.setZzjgbmfzr(oa.getString("zzjgbmfzr"));
//			}
//
//			return departmentDiyBill;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		} finally {
//			if (oa != null) {
//				oa.close();
//			}
//		}
//
//	}
//
//	/**
//	 * 根据请求编号返回该请求编号对应的存储表
//	 *
//	 * @description
//	 * @author 卫
//	 * @create 2015年3月27日下午4:43:53
//	 * @version 1.0
//	 * @param requestid
//	 *            请求编号
//	 * @return 根据请求编号返回该请求编号对应的存储表
//	 * @throws Exception
//	 *             获取数据异常
//	 */
//	public String getTableNameByResquestID(String requestid) throws Exception {
//		String tableName = "";
//		StringBuffer tablesql = new StringBuffer(
//				"select tablename from workflow_bill where id in (select formid from workflow_base where id in (select workflowid from workflow_requestbase where requestid='")
//				.append(requestid).append("'))");
//		OADBUtil oa = new OADBUtil();
//		try {
//			oa.setStatementSql(tablesql.toString());
//			oa.executeQuery();
//			if (oa.next()) {
//				tableName = oa.getString("tableName");
//			}
//
//			return tableName;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		} finally {
//			if (oa != null) {
//				oa.close();
//			}
//		}
//
//	}
//
//	/**
//	 * 获取选择框的值
//	 *
//	 * @param id
//	 *            oa值
//	 * @param fieldid
//	 *            oa字段
//	 * @return 获取选择框的值
//	 * @throws Exception
//	 */
//	public String getSelectNameByID(String id, String fieldid) throws Exception {
//		String selectname = "";
//		StringBuffer tablesql = new StringBuffer(
//				"select selectname from Workflow_Selectitem where fieldid='")
//				.append(fieldid).append("' and selectvalue='").append(id)
//				.append("'");
//		OADBUtil oa = new OADBUtil();
//		try {
//			oa.setStatementSql(tablesql.toString());
//			oa.executeQuery();
//			if (oa.next()) {
//				selectname = oa.getString("selectname");
//			}
//
//			return selectname;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		} finally {
//			if (oa != null) {
//				oa.close();
//			}
//		}
//
//	}
//
//	/**
//	 * 获取选择框的值
//	 *
//	 * @param id
//	 *            oa值
//	 * @param fieldid
//	 *            oa字段
//	 * @return 获取选择框的值
//	 * @throws Exception
//	 */
//	public Map<String, String> getSelectNameByFieldID(String fieldid)
//			throws Exception {
//		Map<String, String> map = new HashMap<String, String>();
//
//		StringBuffer tablesql = new StringBuffer(
//				"select selectvalue, selectname from Workflow_Selectitem where fieldid='")
//				.append(fieldid).append("'");
//		OADBUtil oa = new OADBUtil();
//		try {
//			oa.setStatementSql(tablesql.toString());
//			oa.executeQuery();
//			while (oa.next()) {
//				map.put(oa.getString("selectvalue"), oa.getString("selectname"));
//			}
//
//			return map;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		} finally {
//			if (oa != null) {
//				oa.close();
//			}
//		}
//
//	}
//
//	/**
//	 * 根据请求编号获取当前节点用户用于提交使用
//	 *
//	 * @param requestid
//	 *            请求编号
//	 * @return
//	 * @throws Exception
//	 */
//	public String getUseidByResquestID(String requestid) throws Exception {
//		String userid = "";
//		StringBuffer tablesql = new StringBuffer(
//				"select userid from workflow_currentoperator where requestid='")
//				.append(requestid)
//				.append("' and nodeid in (select nownodeid from workflow_nownode where requestid='")
//				.append(requestid).append("') and isremark in(0,2)");
//		OADBUtil oa = new OADBUtil();
//		try {
//			oa.setStatementSql(tablesql.toString());
//			oa.executeQuery();
//			if (oa.next()) {
//				userid = oa.getString("userid");
//			}
//
//			return userid;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		} finally {
//			if (oa != null) {
//				oa.close();
//			}
//		}
//
//	}
//
//	/**
//	 * 根据请求编号返回该请求编号对应的请求标题
//	 *
//	 * @description
//	 * @author 卫
//	 * @create 2015年3月27日下午4:43:53
//	 * @version 1.0
//	 * @param requestid
//	 *            请求编号
//	 * @return 根据请求编号返回该请求编号对应的请求标题
//	 * @throws Exception
//	 *             获取数据异常
//	 */
//	public String getWorkflowidByResquestID(String requestid) throws Exception {
//		String tableName = "";
//		StringBuffer tablesql = new StringBuffer(
//				"select workflowid from workflow_requestbase where requestid='")
//				.append(requestid).append("'");
//		OADBUtil oa = new OADBUtil();
//		try {
//			oa.setStatementSql(tablesql.toString());
//			oa.executeQuery();
//			if (oa.next()) {
//				tableName = oa.getString("workflowid");
//			}
//
//			return tableName;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		} finally {
//			if (oa != null) {
//				oa.close();
//			}
//		}
//
//	}
//
//	/**
//	 * 根据请求编号返回该请求编号对应的请求标题
//	 *
//	 * @description
//	 * @author 卫
//	 * @create 2015年3月27日下午4:43:53
//	 * @version 1.0
//	 * @param requestid
//	 *            请求编号
//	 * @return 根据请求编号返回该请求编号对应的请求标题
//	 * @throws Exception
//	 *             获取数据异常
//	 */
//	public String getRequestNameByResquestID(String requestid) throws Exception {
//		String requetname = "";
//		StringBuffer tablesql = new StringBuffer(
//				"select requestname from workflow_requestbase where requestid='")
//				.append(requestid).append("'");
//		OADBUtil oa = new OADBUtil();
//		try {
//			oa.setStatementSql(tablesql.toString());
//			oa.executeQuery();
//			if (oa.next()) {
//				requetname = oa.getString("requestname");
//			}
//
//			return requetname;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		} finally {
//			if (oa != null) {
//				oa.close();
//			}
//		}
//
//	}
//
//	/**
//	 * 返回部门信息
//	 *
//	 * @param id
//	 *            部门id
//	 * @return
//	 * @throws Exception
//	 */
//	public DepartmentBill getDepCodeByID(String id) throws Exception {
//		DepartmentBill departmentBill = new DepartmentBill();
//		StringBuffer tablesql = new StringBuffer(
//				"select id,departmentname, departmentcode,outkey from HrmDepartment where id='")
//				.append(id).append("'");
//		OADBUtil oa = new OADBUtil();
//		try {
//			oa.setStatementSql(tablesql.toString());
//			oa.executeQuery();
//			if (oa.next()) {
//				departmentBill.setId(oa.getInt("id"));
//				departmentBill
//						.setDepartmentname(oa.getString("Departmentname"));
//				departmentBill.setDepartmemntcode(oa
//						.getString("departmentcode"));
//				departmentBill.setOutkey(oa.getString("Outkey"));
//			}
//
//			return departmentBill;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		} finally {
//			if (oa != null) {
//				oa.close();
//			}
//		}
//
//	}
//
//	/**
//	 * 返回人员信息
//	 *
//	 * @param id
//	 *            人员id
//	 * @return
//	 * @throws Exception
//	 */
//	public ResourceBill getResByID(String id) throws Exception {
//		ResourceBill resourceBill = new ResourceBill();
//		StringBuffer tablesql = new StringBuffer(
//				"select id,lastname, workcode,outkey,departmentid from HrmResource where id='")
//				.append(id).append("'");
//		OADBUtil oa = new OADBUtil();
//		try {
//			oa.setStatementSql(tablesql.toString());
//			oa.executeQuery();
//			if (oa.next()) {
//				resourceBill.setId(oa.getInt("id"));
//				resourceBill.setLastname(oa.getString("lastname"));
//				resourceBill.setWorkcode(oa.getString("workcode"));
//				resourceBill.setOutkey(oa.getString("Outkey"));
//				resourceBill.setDepartmentid(oa.getString("departmentid"));
//			}
//
//			return resourceBill;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		} finally {
//			if (oa != null) {
//				oa.close();
//			}
//		}
//
//	}
//
//	/**
//	 * 返回公司信息
//	 *
//	 * @param id
//	 *            公司id
//	 * @return
//	 * @throws Exception
//	 */
//	public SubcompanyBill getSubByID(String id) throws Exception {
//		SubcompanyBill subcompanyBill = new SubcompanyBill();
//		StringBuffer tablesql = new StringBuffer(
//				"select id,subcompanyname, subcompanycode,outkey from HrmSubcompany where id='")
//				.append(id).append("'");
//		OADBUtil oa = new OADBUtil();
//		try {
//			oa.setStatementSql(tablesql.toString());
//			oa.executeQuery();
//			if (oa.next()) {
//				subcompanyBill.setId(oa.getInt("id"));
//				subcompanyBill
//						.setSubcompanyname(oa.getString("subcompanyname"));
//				subcompanyBill
//						.setSubcompanycode(oa.getString("subcompanycode"));
//				subcompanyBill.setOutkey(oa.getString("Outkey"));
//			}
//
//			return subcompanyBill;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		} finally {
//			if (oa != null) {
//				oa.close();
//			}
//		}
//
//	}
//
//	/**
//	 * 返回SAP科目编号信息
//	 *
//	 * @param id
//	 *            科目id
//	 * @return
//	 * @throws Exception
//	 */
//	public String getSapKMBHByID(String id) throws Exception {
//		String kmbh = "";
//		StringBuffer tablesql = new StringBuffer(
//				"select id,	KMBH from uf_KMZD where id='").append(id).append(
//				"'");
//		OADBUtil oa = new OADBUtil();
//		try {
//			oa.setStatementSql(tablesql.toString());
//			oa.executeQuery();
//			if (oa.next()) {
//				kmbh = oa.getString("KMBH");
//			}
//
//			return kmbh;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		} finally {
//			if (oa != null) {
//				oa.close();
//			}
//		}
//
//	}
//
//	/**
//	 * 返回SAP现金流量项目编号信息
//	 *
//	 * @param id
//	 *            现金流量id
//	 * @return
//	 * @throws Exception
//	 */
//	public String getSapXMBHByID(String id) throws Exception {
//		String xmbh = "";
//		StringBuffer tablesql = new StringBuffer(
//				"select id,xmbh from 	uf_xjxmzd where id='").append(id).append(
//				"'");
//		OADBUtil oa = new OADBUtil();
//		try {
//			oa.setStatementSql(tablesql.toString());
//			oa.executeQuery();
//			if (oa.next()) {
//				xmbh = oa.getString("xmbh");
//			}
//
//			return xmbh;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		} finally {
//			if (oa != null) {
//				oa.close();
//			}
//		}
//
//	}
//
//}
