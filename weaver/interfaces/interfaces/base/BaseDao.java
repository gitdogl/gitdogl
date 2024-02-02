//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package weaver.interfaces.interfaces.base;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.resource.ResourceComInfo;

public class BaseDao {
    private static final BaseBean logger = new BaseBean();

    public BaseDao() {
    }

    public void setId(String tableName, String id) {
        RecordSet rs = new RecordSet();
        String sql = "insert into " + tableName + " (sjid) values ('" + id + "')";
        rs.executeUpdate(sql, new Object[0]);
    }

    public boolean getSfytb(String tableName, String id) {
        RecordSet rs = new RecordSet();
        boolean isTrue = true;
        String sql = "select sjid from " + tableName + " where sjid = " + id;
        rs.execute(sql);
        if (rs.next()) {
            isTrue = false;
        }

        return isTrue;
    }

    public int createInvalidPerson(String xm, String whid, String sjh) {
        RecordSet rs = new RecordSet();
        String oaid = "";
        rs.execute("select id from cus_fielddata where field3='" + whid + "'");
        if (rs.next()) {
            oaid = rs.getString("id");
        }

        if ("".equals(oaid)) {
            System.out.println("whid：" + whid + "不匹配");
            return 0;
        } else {
            return Integer.parseInt(oaid);
        }
    }

    public User getUserById(int var1) {
        RecordSet var2 = new RecordSet();
        User var3 = null;
        String var4 = "select * from HrmResource where id=" + var1 + "";
        var2.executeSql(var4);
        if (var2.next()) {
            var3 = new User();
            var3.setUid(var2.getInt("id"));
            var3.setLoginid("loginid");
            var3.setFirstname(var2.getString("firstname"));
            var3.setLastname(var2.getString("lastname"));
            var3.setAliasname(var2.getString("aliasname"));
            var3.setTitle(var2.getString("title"));
            var3.setTitlelocation(var2.getString("titlelocation"));
            var3.setSex(var2.getString("sex"));
            String var5 = var2.getString("systemlanguage");
            var3.setLanguage(Util.getIntValue(var5, 7));
            var3.setTelephone(var2.getString("telephone"));
            var3.setMobile(var2.getString("mobile"));
            var3.setMobilecall(var2.getString("mobilecall"));
            var3.setEmail(var2.getString("email"));
            var3.setCountryid(var2.getString("countryid"));
            var3.setLocationid(var2.getString("locationid"));
            var3.setResourcetype(var2.getString("resourcetype"));
            var3.setContractdate(var2.getString("contractdate"));
            var3.setJobtitle(var2.getString("jobtitle"));
            var3.setJobgroup(var2.getString("jobgroup"));
            var3.setJobactivity(var2.getString("jobactivity"));
            var3.setJoblevel(var2.getString("joblevel"));
            var3.setSeclevel(var2.getString("seclevel"));
            var3.setUserDepartment(Util.getIntValue(var2.getString("departmentid"), 0));
            var3.setUserSubCompany1(Util.getIntValue(var2.getString("subcompanyid1"), 0));
            var3.setUserSubCompany2(Util.getIntValue(var2.getString("subcompanyid2"), 0));
            var3.setUserSubCompany3(Util.getIntValue(var2.getString("subcompanyid3"), 0));
            var3.setUserSubCompany4(Util.getIntValue(var2.getString("subcompanyid4"), 0));
            var3.setManagerid(var2.getString("managerid"));
            var3.setAssistantid(var2.getString("assistantid"));
            var3.setPurchaselimit(var2.getString("purchaselimit"));
            var3.setCurrencyid(var2.getString("currencyid"));
            var3.setLogintype("1");
            var3.setAccount(var2.getString("account"));
        }

        var2.executeSql(" SELECT id,loginid,firstname,lastname,systemlanguage,seclevel FROM HrmResourceManager WHERE id=" + var1);
        if (var2.next()) {
            var3 = new User();
            var3.setUid(var1);
            var3.setLoginid(var2.getString("loginid"));
            var3.setFirstname(var2.getString("firstname"));
            var3.setLastname(var2.getString("lastname"));
            var3.setLanguage(Util.getIntValue(var2.getString("systemlanguage"), 7));
            var3.setSeclevel(var2.getString("seclevel"));
            var3.setLogintype("1");
        }

        return var3;
    }

    public int setRydyError(String emp_id) {
        RecordSetDataSource rs = new RecordSetDataSource("WH");
        RecordSet oa = new RecordSet();
        String sql = "select c.ORGNAMESTRING,a.empname,a.emp_id,a.empmobilephone from ezoffice.ORG_EMPLOYEE a left join ezoffice.ORG_ORGANIZATION_USER b on a.emp_id = b.emp_id left join ezoffice.ORG_ORGANIZATION c on c.org_id = b.org_id where a.emp_id = '" + emp_id + "'";
        rs.execute(sql);
        int userid = 0;
        if (rs.next()) {
            String bmmc = rs.getString("ORGNAMESTRING");
            String xm = rs.getString("empname");
            String ryid = rs.getString("emp_id");
            String sjh = rs.getString("empmobilephone");
            String oasql = "insert into uf_hrmerror (bmmc,xm,ryid,sjh) values ('" + bmmc + "','" + xm + "','" + ryid + "','" + sjh + "')";
            oa.executeUpdate(oasql, new Object[0]);
            userid = this.createInvalidPerson(xm, emp_id, sjh);
        }

        return userid;
    }

    public void setBs(String tableName, String name, String value, String z) {
        RecordSetDataSource rs = new RecordSetDataSource("WH");
        String sql = "update " + tableName + " set oabs = " + z + " where " + name + " = '" + value + "'";
        rs.executeSql(sql);
    }

    public String getFcUserid(String emp_id) {
        if ("".equals(emp_id)) {
            return "";
        } else {
            RecordSetDataSource rs = new RecordSetDataSource("WH");
            RecordSet oa = new RecordSet();
            String sql = "select c.ORGNAMESTRING,a.empname,a.emp_id,a.USERACCOUNTS from ezoffice.ORG_EMPLOYEE a left join ezoffice.ORG_ORGANIZATION_USER b on a.emp_id = b.emp_id left join ezoffice.ORG_ORGANIZATION c on c.org_id = b.org_id where a.emp_id = '" + emp_id + "'";
            rs.execute(sql);
            String dlm = "";
            if (rs.next()) {
                dlm = rs.getString("USERACCOUNTS");
            }

            String oaUserId = "";
            sql = "select id from hrmresource where jobactivitydesc = '" + dlm + "' and departmentid = 7479";
            oa.execute(sql);
            if (oa.next()) {
                oaUserId = oa.getString("id");
            }

            return oaUserId;
        }
    }

    public String getPhone(String EMP_ID) {
        if ("".equals(EMP_ID)) {
            return "";
        } else {
            String empmobilephone = "";
            RecordSetDataSource rs = new RecordSetDataSource("WH");
            String sql = "select empmobilephone from ezoffice.ORG_EMPLOYEE where EMP_ID = " + EMP_ID;
            rs.execute(sql);
            if (rs.next()) {
                empmobilephone = rs.getString("empmobilephone");
            }

            return empmobilephone;
        }
    }

    public String getOAUserId(String whid) {
        if ("".equals(whid)) {
            return "";
        } else {
            String oaUserId = "";
            RecordSet rs = new RecordSet();
            String sql = "select id from cus_fielddata where field3 = '" + whid + "'";
            rs.execute(sql);
            if (rs.next()) {
                oaUserId = rs.getString("id");
            }

            if ("".equals(oaUserId)) {
                sql = "select id from hrmresourcemanager where id = '" + whid + "'";
                rs.execute(sql);
                if (rs.next()) {
                    oaUserId = rs.getString("id");
                }
            }

            return oaUserId;
        }
    }

    public void setLog(LogBill logBill) {
        RecordSet rs = new RecordSet();
        String sql = "insert into uf_log (cwlx,cwyy,cwerr,sjid) values ('" + logBill.getCwlx() + "','" + logBill.getCwyy() + "','" + logBill.getCwerr() + "','" + logBill.getSjid() + "')";
        logger.writeLog(sql);
        rs.executeUpdate(sql, new Object[0]);
    }

    public User getUser(int userid) {
        User user = new User();

        try {
            ResourceComInfo rc = new ResourceComInfo();
            DepartmentComInfo dc = new DepartmentComInfo();
            user.setUid(userid);
            user.setLoginid(rc.getLoginID("" + userid));
            user.setFirstname(rc.getFirstname("" + userid));
            user.setLastname(rc.getLastname("" + userid));
            user.setLogintype("1");
            user.setSex(rc.getSexs("" + userid));
            user.setLanguage(7);
            user.setEmail(rc.getEmail("" + userid));
            user.setLocationid(rc.getLocationid("" + userid));
            user.setResourcetype(rc.getResourcetype("" + userid));
            user.setJobtitle(rc.getJobTitle("" + userid));
            user.setJoblevel(rc.getJoblevel("" + userid));
            user.setSeclevel(rc.getSeclevel("" + userid));
            user.setUserDepartment(Util.getIntValue(rc.getDepartmentID("" + userid), 0));
            user.setUserSubCompany1(Util.getIntValue(dc.getSubcompanyid1(user.getUserDepartment() + ""), 0));
            user.setManagerid(rc.getManagerID("" + userid));
            user.setAssistantid(rc.getAssistantID("" + userid));
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return user;
    }
}
