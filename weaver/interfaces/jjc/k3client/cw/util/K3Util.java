package weaver.interfaces.jjc.k3client.cw.util;


import weaver.general.BaseBean;

public class K3Util {
    BaseBean bb = new BaseBean();
    private String K3CloudURL = "";
    private String K3Cloudip = "";
    private String dbId = "";
    private String uid = "";
    private String pwd = "";
    private int lang = 2052;

    public String GetK3Cloudip() {
        if (K3Cloudip == null || K3Cloudip.equals(""))
            K3Cloudip = bb.getPropValue("OAToK3URLSet", "K3Cloudip");
        if (K3Cloudip == null || K3Cloudip.equals(""))
            // driver = "oracle.jdbc.driver.OracleDriver";
            K3Cloudip = "http://210.45.120.165:8090/";
        return K3Cloudip;
    }

    public String GetK3CloudURL() {
        if (K3CloudURL == null || K3CloudURL.equals(""))
            K3CloudURL = bb.getPropValue("OAToK3URLSet", "K3CloudURL");
        if (K3CloudURL == null || K3CloudURL.equals(""))
            // driver = "oracle.jdbc.driver.OracleDriver";
            K3CloudURL = "http://210.45.120.165:8090/k3cloud/";
        return K3CloudURL;
    }

    public String getDbId() {
        if (dbId == null || dbId.equals(""))
            dbId = bb.getPropValue("OAToK3URLSet", "dbId");
        if (dbId == null || dbId.equals(""))
            // oaUrl = "jdbc:oracle:thin:@172.20.103.33:1521:orcl";
            // oaUrl = "jdbc:oracle:thin:@192.168.6.111:1521:ecology";
            dbId = "5f18e0e78672b8";
        return dbId;
    }

    public String getUid() {
        if (uid == null || uid.equals(""))
            uid = bb.getPropValue("OAToK3URLSet", "uid");
        if (uid == null || uid.equals(""))
            // uid = "Administrator";
            uid = "Administrator";
        return uid;
    }

    public String getPwd() {
        if (pwd == null || pwd.equals(""))
            pwd = bb.getPropValue("OAToK3URLSet", "pwd");
        if (pwd == null || pwd.equals(""))
            // pwd = "888888";
            pwd = "888888";
        return pwd;
    }

    public int getLang() {

        String langstr = bb.getPropValue("OAToK3URLSet", "lang");

        if ("".equals(langstr)) {
            langstr = "2052";
        }

        return Integer.parseInt(langstr);
    }
}