package weaver.interfaces.jjc.oaclient.cw.dao;


import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.User;

public class BaseDao {
    public static String getWorkCode(int var1) {
        String workCode = "";
        RecordSet rs = new RecordSet();
        String var4 = "select workcode from HrmResource where id=" + var1 + "";
        rs.executeSql(var4);
        if (rs.next()) {
            workCode = rs.getString("workcode");
        }
        return workCode;
    }

    public static String getLastName(int var1) {
        String lastname = "";
        RecordSet rs = new RecordSet();
        String var4 = "select lastname from HrmResource where id=" + var1 + "";
        rs.executeSql(var4);
        if (rs.next()) {
            lastname = rs.getString("lastname");
        }
        return lastname;
    }

    public static String getDepartmentName(int id) {
        String DEPARTMENTNAME = "";
        RecordSet rs = new RecordSet();
        String var4 = "SELECT b.DEPARTMENTNAME FROM HRMRESOURCE a LEFT JOIN hrmdepartment b ON a.departmentid=b.id WHERE a.id=" + id;
        rs.executeSql(var4);
        if (rs.next()) {
            DEPARTMENTNAME = rs.getString("DEPARTMENTNAME");
        }
        return DEPARTMENTNAME;
    }

    /**
     * 返回User对象
     *
     * @param var1 人员id
     * @return
     */
    public static User getUserById(int var1) {
        RecordSet rs = new RecordSet();
        User user = null;
        String var4 = "select * from HrmResource where id=" + var1 + "";
        rs.executeSql(var4);
        if (rs.next()) {
            user = new User();
            user.setUid(rs.getInt("id"));
            user.setLoginid("loginid");
            user.setFirstname(rs.getString("firstname"));
            user.setLastname(rs.getString("lastname"));
            user.setAliasname(rs.getString("aliasname"));
            user.setTitle(rs.getString("title"));
            user.setTitlelocation(rs.getString("titlelocation"));
            user.setSex(rs.getString("sex"));
            String var5 = rs.getString("systemlanguage");
            user.setLanguage(Util.getIntValue(var5, 7));
            user.setTelephone(rs.getString("telephone"));
            user.setMobile(rs.getString("mobile"));
            user.setMobilecall(rs.getString("mobilecall"));
            user.setEmail(rs.getString("email"));
            user.setCountryid(rs.getString("countryid"));
            user.setLocationid(rs.getString("locationid"));
            user.setResourcetype(rs.getString("resourcetype"));
            user.setContractdate(rs.getString("contractdate"));
            user.setJobtitle(rs.getString("jobtitle"));
            user.setJobgroup(rs.getString("jobgroup"));
            user.setJobactivity(rs.getString("jobactivity"));
            user.setJoblevel(rs.getString("joblevel"));
            user.setSeclevel(rs.getString("seclevel"));
            user.setUserDepartment(Util.getIntValue(rs.getString("departmentid"), 0));
            user.setUserSubCompany1(Util.getIntValue(rs.getString("subcompanyid1"), 0));
            user.setUserSubCompany2(Util.getIntValue(rs.getString("subcompanyid2"), 0));
            user.setUserSubCompany3(Util.getIntValue(rs.getString("subcompanyid3"), 0));
            user.setUserSubCompany4(Util.getIntValue(rs.getString("subcompanyid4"), 0));
            user.setManagerid(rs.getString("managerid"));
            user.setAssistantid(rs.getString("assistantid"));
            user.setPurchaselimit(rs.getString("purchaselimit"));
            user.setCurrencyid(rs.getString("currencyid"));
            user.setLogintype("1");
            user.setAccount(rs.getString("account"));
        }
        rs.executeSql(" SELECT id,loginid,firstname,lastname,systemlanguage,seclevel FROM HrmResourceManager WHERE id=" + var1);
        if (rs.next()) {
            user = new User();
            user.setUid(var1);
            user.setLoginid(rs.getString("loginid"));
            user.setFirstname(rs.getString("firstname"));
            user.setLastname(rs.getString("lastname"));
            user.setLanguage(Util.getIntValue(rs.getString("systemlanguage"), 7));
            user.setSeclevel(rs.getString("seclevel"));
            user.setLogintype("1");
        }
        return user;
    }


    /**
     * 根据请求编号返回该请求编号对应的存储表
     *
     * @param requestid 请求编号
     * @return 根据请求编号返回该请求编号对应的存储表
     * @throws Exception 获取数据异常
     * @description
     * @author 卫
     * @create 2015年3月27日下午4:43:53
     * @version 1.0
     */
    public String getTableNameByResquestID(String requestid) throws Exception {
        String tableName = "";
        StringBuffer tablesql = new StringBuffer(
                "select tablename from workflow_bill where id in (select formid from workflow_base where id in (select workflowid from workflow_requestbase where requestid='")
                .append(requestid).append("'))");
        RecordSet oa = new RecordSet();
        try {
            oa.execute(tablesql.toString());
            if (oa.next()) {
                tableName = oa.getString("tableName");
            }

            return tableName;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public static String getRequestname(String requestid) {
        String requetname = "";
        StringBuffer tablesql = new StringBuffer(
                "select requestname from workflow_requestbase where requestid='")
                .append(requestid).append("'");
        RecordSet oa = new RecordSet();

        oa.execute(tablesql.toString());

        if (oa.next()) {
            requetname = oa.getString("requestname");
        }

        return requetname;

    }
}
