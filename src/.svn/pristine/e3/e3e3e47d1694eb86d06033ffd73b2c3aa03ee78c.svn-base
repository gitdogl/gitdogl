//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package weaver.interfaces.interfaces.mail4.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.activation.MimetypesFileTypeMap;
import sun.misc.BASE64Decoder;
import weaver.conn.ConnStatement;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.email.MailCommonUtils;
import weaver.general.BaseBean;
import weaver.interfaces.interfaces.base.BaseDao;
import weaver.interfaces.interfaces.base.LogBill;
import weaver.interfaces.interfaces.mail4.dao.MailDao;
import weaver.interfaces.interfaces.mail4.dto.MailUserBill;
import weaver.interfaces.interfaces.mail4.dto.OAMaillBill;
import weaver.interfaces.interfaces.mail4.dto.OAMaillFileBill;
import weaver.interfaces.interfaces.mail4.dto.WhMailBill;
import weaver.interfaces.interfaces.mail4.dto.WhMailFileBill;

public class MailService extends Thread {
    private static final BaseBean logger = new BaseBean();
    private List<WhMailBill> whMailBills;
    MailDao mailDao = new MailDao();
    RecordSet RS = new RecordSet();
    RecordSetDataSource rs = new RecordSetDataSource("WH");
    Map<String, String> SYSTEM_CACHE = new HashMap();
    BaseDao baseDao = new BaseDao();
    String content_uuid = "";
    String mit_uuid = "";
    String tablename = "";

    public MailService(List<WhMailBill> whMailBills, String tablename) {
        this.whMailBills = whMailBills;
        this.tablename = tablename;
    }

    public void run() {
        logger.writeLog("进入线程");
        Iterator var1 = this.whMailBills.iterator();

        while(var1.hasNext()) {
            WhMailBill whMailBill = (WhMailBill)var1.next();
            this.mailDao.clearMailData(whMailBill.getMail_id());
            logger.writeLog("已完成清理:" + whMailBill.getMail_id() + "," + whMailBill.getMailsubject());
            List<MailUserBill> userids = this.mailDao.getmailUserIds(whMailBill.getMail_id());
            int size = 0;
            String error = "";

            try {
                String mit_uuid = UUID.randomUUID().toString();
                String content_uuid = UUID.randomUUID().toString();
                this.content_uuid = content_uuid;
                this.mit_uuid = mit_uuid;
                if (whMailBill != null) {
                    logger.writeLog("数据whMailBill不为空，正常运行");
                } else {
                    logger.writeLog("数据whMailBill为空，有误");
                }

                logger.writeLog("whMailBill的值为" + whMailBill);
                OAMaillBill oaMaillBill = this.getFjrOAMaillBill(whMailBill, mit_uuid, content_uuid);
                if (oaMaillBill != null) {
                    logger.writeLog("数据oaMaillBill不为空，正常运行");
                } else {
                    logger.writeLog("数据oaMaillBill为空，有误");
                }

                String sql;
                if (oaMaillBill != null) {
                    sql = whMailBill.getMailcontent();
                    BASE64Decoder decoder = new BASE64Decoder();
                    String content = new String(decoder.decodeBuffer(sql), "UTF-8");
                    this.insertOAMaillBillFj(oaMaillBill);
                    String primarykey = oaMaillBill.getMr_uuid();
                    String fjMailId = this.getFjId(primarykey);
                    String fjr = oaMaillBill.getSendfrom();
                    List<OAMaillBill> oaMaillBillList = this.getOAMaillBillList(whMailBill, mit_uuid, content_uuid, fjMailId, fjr, userids);
                    this.insertOAMaillBill(oaMaillBillList);
                    this.setMailcontent(fjMailId, content, content_uuid);
                    String sfbhfj = whMailBill.getMailhasaccessory();
                    if (!"1".equals(sfbhfj)) {
                        logger.writeLog("没有附件sfbhfj:" + sfbhfj);
                    } else {
                        List<String> maiIds = this.getMailIds(mit_uuid);
                        long hqyjxxstartTime = System.currentTimeMillis();
                        List<WhMailFileBill> whMailFileBills = this.mailDao.getWhMailFileBills(whMailBill.getMail_id());
                        long hqyjxxendTime = System.currentTimeMillis();
                        logger.writeLog("获取邮件信息耗时：" + (hqyjxxendTime - hqyjxxstartTime) + "ms");
                        List<OAMaillFileBill> oaMaillFileBillList = new ArrayList();
                        Iterator var24 = whMailFileBills.iterator();

                        label69:
                        while(true) {
                            while(true) {
                                while(var24.hasNext()) {
                                    WhMailFileBill mailFileBill = (WhMailFileBill)var24.next();
                                    String mrf_uuid = UUID.randomUUID().toString();
                                    if (!"".equals(mailFileBill.getAccessorysavename())) {
                                        String datePath = mailFileBill.getAccessorysavename().substring(0, 6);
                                        String filepath = "/data/weaver/whfj/inner/innerMailbox/" + datePath + "/" + mailFileBill.getAccessorysavename();
                                        File fj = new File(filepath);
                                        logger.writeLog("原文件路径" + filepath);
                                        if (fj.exists()) {
                                            size = (int)((long)size + fj.length());
                                            String str = String.valueOf((char)((int)(65.0 + Math.random() * 26.0)));
                                            String newFilepath = "/data/weaver/whfjnewnew/inner/innerMailbox/" + datePath + "/" + str + "/" + mailFileBill.getAccessorysavename();
                                            long fjkbstartTime = System.currentTimeMillis();
                                            copyFile(filepath, newFilepath, false, false);
                                            long fjkbendTime = System.currentTimeMillis();
                                            logger.writeLog("拷贝附件耗时：" + (fjkbendTime - fjkbstartTime) + "ms , 附件大小 :" + fj.length());
                                            logger.writeLog("现文件路径" + newFilepath);
                                            Iterator var36 = maiIds.iterator();

                                            while(var36.hasNext()) {
                                                String mailid = (String)var36.next();
                                                OAMaillFileBill oaMaillFileBill = new OAMaillFileBill();
                                                oaMaillFileBill.setMrf_uuid(mrf_uuid);
                                                oaMaillFileBill.setMailid(mailid);
                                                oaMaillFileBill.setFilename(mailFileBill.getAccessoryname());
                                                oaMaillFileBill.setFilerealpath(newFilepath);
                                                oaMaillFileBill.setFilesize(fj.length() + "");
                                                oaMaillFileBill.setFiletype((new MimetypesFileTypeMap()).getContentType(fj));
                                                oaMaillFileBillList.add(oaMaillFileBill);
                                            }
                                        } else {
                                            LogBill logBill = new LogBill();
                                            logBill.setCwlx("mail");
                                            logBill.setCwyy("附件:" + mailFileBill.getAccessory_id() + "未找到!请自行补充大小,类型数据");
                                            logBill.setSjid(whMailBill.getMail_id());
                                            this.baseDao.setLog(logBill);
                                        }
                                    } else {
                                        logger.writeLog("文件名为空");
                                    }
                                }

                                this.indestFile(oaMaillFileBillList);
                                break label69;
                            }
                        }
                    }

                    logger.writeLog("邮件附件写完了");
                    this.baseDao.setBs(this.tablename, "mail_id", whMailBill.getMail_id(), "1");
                }

                if (!"".equals(error)) {
                    logger.writeLog(error);
                }

                sql = "update mailresource set Size_n = " + size + " where content_uuid = '" + content_uuid + "'";
                this.RS.executeUpdate(sql, new Object[0]);
            } catch (Exception var39) {
                this.deleteYj(this.mit_uuid);
                LogBill logBill = new LogBill();
                logBill.setCwlx("mail");
                logBill.setCwyy(var39.getMessage());
                logBill.setSjid(whMailBill.getMail_id());
                this.baseDao.setLog(logBill);
                this.baseDao.setBs(this.tablename, "mail_id", whMailBill.getMail_id(), "2");
                continue;
            }

            logger.writeLog("已重新写入mailid:" + whMailBill.getMail_id() + "," + whMailBill.getMailsubject());
        }

    }

    private void deleteYj(String Mit_uuid) {
        List<String> maiIds = this.getMailIds(Mit_uuid);
        String sql = "delete from mailresource where CONTENT_UUID = '" + this.content_uuid + "'";
        this.RS.executeUpdate(sql, new Object[0]);
        sql = "delete from mailcontent where CONTENT_UUID = '" + this.content_uuid + "'";
        this.RS.executeUpdate(sql, new Object[0]);
        Iterator var4 = maiIds.iterator();

        while(var4.hasNext()) {
            String mailid = (String)var4.next();
            sql = "delete from mailresourcefile where MAILID = " + mailid;
            this.RS.executeUpdate(sql, new Object[0]);
        }

    }

    public static boolean copyFile(String srcFilePath, String destFilePath, boolean overflag, boolean deletesourcefile) {
        File srcFile = new File(srcFilePath);
        File destFile = new File(destFilePath);
        if (destFile.exists()) {
            if (overflag) {
                destFile.delete();
            }
        } else if (!destFile.getParentFile().exists() && !destFile.getParentFile().mkdirs()) {
            System.out.println("复制文件失败：创建目标文件所在目录失败");
            return false;
        }

        int byteread = 0;
        InputStream in = null;
        OutputStream out = null;

        boolean var10;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }

            var10 = true;
            return var10;
        } catch (FileNotFoundException var22) {
            var22.printStackTrace();
            var10 = false;
        } catch (IOException var23) {
            var23.printStackTrace();
            var10 = false;
            return var10;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

                if (out != null) {
                    out.close();
                }

                if (deletesourcefile && srcFile.exists()) {
                    srcFile.delete();
                }
            } catch (IOException var21) {
                var21.printStackTrace();
                return false;
            }

        }

        return var10;
    }

    public void setMailcontent(String mailid, String mailcontent, String content_uuid) throws Exception {
        mailcontent = MailCommonUtils.escapeHtmlClosure(mailcontent);
        boolean isTrue = true;
        String sql = "insert into mailcontent (mailid, mailcontent, content_uuid) values(?,?,?) ";
        if ("oracle".equalsIgnoreCase(this.RS.getOrgindbtype())) {
            ConnStatement var8 = new ConnStatement();

            try {
                var8.setStatementSql(sql);
                var8.setInt(1, mailid);
                var8.setCharacterStream(2, mailcontent);
                var8.setString(3, content_uuid);
                int i = var8.executeUpdate();
                if (i <= 0) {
                    isTrue = false;
                }
            } finally {
                var8.close();
            }
        } else {
            isTrue = this.RS.executeUpdate(sql, new Object[]{mailid, mailcontent, content_uuid});
        }

        if (!isTrue) {
            throw new Exception("写入邮件内容报错sql:" + sql);
        }
    }

    public List<String> getMailIds(String Mit_uuid) {
        RecordSet rs = new RecordSet();
        List<String> maiIds = new ArrayList();
        String sql = "select id from mailresource where Mit_uuid = '" + Mit_uuid + "'";
        rs.execute(sql);

        while(rs.next()) {
            maiIds.add(rs.getString("id"));
        }

        return maiIds;
    }

    public OAMaillBill getFjrOAMaillBill(WhMailBill whMailBill, String mit_uuid, String content_uuid) {
        String useraccounts = whMailBill.getMailposterid();
        logger.writeLog("whMailBill.getMailposterid()的值为" + whMailBill.getMailposterid());
        logger.writeLog("获取到的useraccounts的值为" + useraccounts);
        String userid = "";
        userid = this.baseDao.getOAUserId(useraccounts);
        logger.writeLog("获取到的userid的值为" + userid);
        OAMaillBill oaMaillBill = null;
        String empname = "";
        if ("".equals(userid)) {
            new BaseDao();
            this.rs.execute("select empname from  ezoffice.ORG_EMPLOYEE where emp_id='" + useraccounts + "'");
            if (this.rs.next()) {
                empname = this.rs.getString("empname");
            }

            if (empname != null && !"".equals(empname)) {
                System.out.println("人员名称:" + empname + "不存在");
            }
        }

        if (!"".equals(userid)) {
            oaMaillBill = new OAMaillBill();
            String mr_uuid = UUID.randomUUID().toString();
            oaMaillBill.setMr_uuid(mr_uuid);
            oaMaillBill.setIssendapart("0");
            oaMaillBill.setResourceid(userid);
            oaMaillBill.setPriority("3");
            oaMaillBill.setSendfrom(userid);
            oaMaillBill.setSenddate(whMailBill.getMailposttime());
            oaMaillBill.setSize_n("10");
            oaMaillBill.setSubject(whMailBill.getMailsubject());
            oaMaillBill.setMailtype(whMailBill.getMailcontenttype().equals("1") ? "0" : "1");
            oaMaillBill.setHashtmlimage("");
            oaMaillBill.setAttachmentnumber("");
            oaMaillBill.setStatus("1");
            oaMaillBill.setFolderid("-1");
            oaMaillBill.setMailaccountid("-1");
            oaMaillBill.setIsinternal("1");
            oaMaillBill.setCanview("1");
            oaMaillBill.setNeedreceipt("0");
            oaMaillBill.setReceiveneedreceipt("");
            oaMaillBill.setHaseml("-1");
            oaMaillBill.setOriginalmailid("");
            oaMaillBill.setReaddate("0");
            oaMaillBill.setTimingdatestate("2");
            oaMaillBill.setMit_uuid(mit_uuid);
            oaMaillBill.setIsnewcontent("1");
            oaMaillBill.setContent_uuid(content_uuid);
            oaMaillBill.setClassification("4");
            oaMaillBill.setContent(whMailBill.getMail_id());
        }

        return oaMaillBill;
    }

    public void insertOAMaillBillFj(OAMaillBill oaMaillBill) throws Exception {
        String sql = "INSERT INTO mailresource (mr_uuid,issendapart,resourceid,priority,sendfrom,senddate,size_n,subject,mailtype,hashtmlimage,attachmentnumber,status,folderid,mailaccountid,isinternal,canview,needreceipt,receiveneedreceipt,haseml,originalmailid,readdate,timingdatestate,MIT_UUID,ISNEWCONTENT,CONTENT_UUID,CLASSIFICATION,Content)VALUES (" + this.getSqlStr(oaMaillBill.getMr_uuid()) + "," + this.getSqlStr(oaMaillBill.getIssendapart()) + "," + this.getSqlStr(oaMaillBill.getResourceid()) + "," + this.getSqlStr(oaMaillBill.getPriority()) + "," + this.getSqlStr(oaMaillBill.getSendfrom()) + "," + this.getSqlStr(oaMaillBill.getSenddate().substring(0, 19)) + "," + this.getSqlStr(oaMaillBill.getSize_n()) + "," + this.getSqlStr(oaMaillBill.getSubject()) + "," + this.getSqlStr(oaMaillBill.getMailtype()) + "," + this.getSqlStr(oaMaillBill.getHashtmlimage()) + "," + this.getSqlStr(oaMaillBill.getAttachmentnumber()) + "," + this.getSqlStr(oaMaillBill.getStatus()) + "," + this.getSqlStr(oaMaillBill.getFolderid()) + "," + this.getSqlStr(oaMaillBill.getMailaccountid()) + "," + this.getSqlStr(oaMaillBill.getIsinternal()) + "," + this.getSqlStr(oaMaillBill.getCanview()) + "," + this.getSqlStr(oaMaillBill.getNeedreceipt()) + "," + this.getSqlStr(oaMaillBill.getReceiveneedreceipt()) + "," + this.getSqlStr(oaMaillBill.getHaseml()) + "," + this.getSqlStr(oaMaillBill.getOriginalmailid()) + "," + this.getSqlStr(oaMaillBill.getReaddate()) + "," + this.getSqlStr(oaMaillBill.getTimingdatestate()) + "," + this.getSqlStr(oaMaillBill.getMit_uuid()) + "," + this.getSqlStr(oaMaillBill.getIsnewcontent()) + "," + this.getSqlStr(oaMaillBill.getContent_uuid()) + "," + this.getSqlStr(oaMaillBill.getClassification()) + " , " + this.getSqlStr(oaMaillBill.getContent()) + " )";
        logger.writeLog("开始执行插入sql" + sql);
        if (!this.RS.executeUpdate(sql, new Object[0])) {
            throw new Exception("写入发件人信息报错sql:" + sql);
        }
    }

    public String getFjId(String mr_uuid) {
        RecordSet rs = new RecordSet();
        String sql2 = "select id from mailresource where mr_uuid = '" + mr_uuid + "'";
        rs.execute(sql2);
        String id = "";
        if (rs.next()) {
            id = rs.getString("id");
        }

        return id;
    }

    public List<OAMaillBill> getOAMaillBillList(WhMailBill whMailBill, String mit_uuid, String content_uuid, String mainid, String fjr, List<MailUserBill> userids) throws Exception {
        List<OAMaillBill> oaMaillBillList = new ArrayList();
        Iterator var8 = userids.iterator();

        String NOTREAD;
        while(var8.hasNext()) {
            MailUserBill userid = (MailUserBill)var8.next();
            NOTREAD = userid.getNotread();
            String ydwd = "0";
            if (!"1".equals(NOTREAD)) {
                ydwd = "1";
            }

            String useraccounts = userid.getEmp_id();
            String oauserid = this.baseDao.getOAUserId(useraccounts);
            if ("".equals(oauserid)) {
                oauserid = this.baseDao.setRydyError(userid.getEmp_id()) + "";
            }

            if (!"0".equals(oauserid)) {
                OAMaillBill oaMaillBill = new OAMaillBill();
                oaMaillBill.setMr_uuid("");
                oaMaillBill.setIssendapart("");
                oaMaillBill.setResourceid(oauserid);
                oaMaillBill.setPriority("3");
                oaMaillBill.setSendfrom(fjr);
                oaMaillBill.setSenddate(whMailBill.getMailposttime());
                oaMaillBill.setSize_n("10");
                oaMaillBill.setSubject(whMailBill.getMailsubject());
                oaMaillBill.setMailtype(whMailBill.getMailcontenttype());
                oaMaillBill.setHashtmlimage("");
                oaMaillBill.setAttachmentnumber("");
                oaMaillBill.setStatus(ydwd);
                oaMaillBill.setFolderid("0");
                oaMaillBill.setMailaccountid("0");
                oaMaillBill.setIsinternal("1");
                oaMaillBill.setCanview("1");
                oaMaillBill.setNeedreceipt("");
                oaMaillBill.setReceiveneedreceipt("");
                oaMaillBill.setHaseml("-1");
                oaMaillBill.setOriginalmailid(mainid);
                oaMaillBill.setReaddate("0");
                oaMaillBill.setTimingdatestate("");
                oaMaillBill.setMit_uuid(mit_uuid);
                oaMaillBill.setIsnewcontent("1");
                oaMaillBill.setContent_uuid(content_uuid);
                oaMaillBill.setClassification("4");
                oaMaillBill.setContent(whMailBill.getMail_id());
                oaMaillBillList.add(oaMaillBill);
            }
        }

        String sjr = whMailBill.getMailtoid_c();
        String csr = whMailBill.getMailccid_c();
        NOTREAD = whMailBill.getMailbccid_c();
        List<String> sjrList = new ArrayList();
        List<String> csrList = new ArrayList();
        List<String> msrList = new ArrayList();
        NOTREAD = NOTREAD.replace("*", "$");
        NOTREAD = NOTREAD.replace("@", "$");
        NOTREAD = NOTREAD.replace("$$", "$");
        String[] msrs = NOTREAD.split("[$]");
        String[] csrs = msrs;
        int var16 = msrs.length;

        int var17;
        String sjrStr;
        for(var17 = 0; var17 < var16; ++var17) {
            String msrStr = csrs[var17];
            if (!"".equals(msrStr)) {
                sjrStr = this.baseDao.getOAUserId(msrStr);
                if ("".equals(sjrStr)) {
                    sjrStr = this.baseDao.setRydyError(msrStr) + "";
                }

                if (!"".equals(sjrStr)) {
                    msrList.add(sjrStr);
                }
            }
        }

        csr = csr.replace("*", "$");
        csr = csr.replace("@", "$");
        csr = csr.replace("$$", "$");
        csrs = csr.split("[$]");
        String[] sjrs = csrs;
        var17 = csrs.length;

        int var31;
        for(var31 = 0; var31 < var17; ++var31) {
            String csrStr = sjrs[var31];
            if (!"".equals(csrStr)) {
                String userid = this.baseDao.getOAUserId(csrStr);
                if ("".equals(userid)) {
                    userid = this.baseDao.setRydyError(csrStr) + "";
                }

                if (!"".equals(userid)) {
                    csrList.add(userid);
                }
            }
        }

        sjr = sjr.replace("*", "$");
        sjr = sjr.replace("@", "$");
        sjr = sjr.replace("$$", "$");
        sjrs = sjr.split("[$]");
        String[] var30 = sjrs;
        var31 = sjrs.length;

        for(int var32 = 0; var32 < var31; ++var32) {
            sjrStr = var30[var32];
            if (!"".equals(sjrStr)) {
                String userid = this.baseDao.getOAUserId(sjrStr);
                if ("".equals(userid)) {
                    userid = this.baseDao.setRydyError(sjrStr) + "";
                }

                if (!"".equals(userid)) {
                    sjrList.add(userid);
                }
            }
        }

        return oaMaillBillList;
    }

    public void insertOAMaillBill(List<OAMaillBill> oaMaillBillList) throws Exception {
        Iterator var2 = oaMaillBillList.iterator();

        String sql;
        do {
            if (!var2.hasNext()) {
                return;
            }

            OAMaillBill oaMaillBill = (OAMaillBill)var2.next();
            sql = "INSERT INTO mailresource (mr_uuid,issendapart,resourceid,priority,sendfrom,senddate,size_n,subject,mailtype,hashtmlimage,attachmentnumber,status,folderid,mailaccountid,isinternal,canview,needreceipt,receiveneedreceipt,haseml,originalmailid,readdate,timingdatestate,MIT_UUID,ISNEWCONTENT,CONTENT_UUID,CLASSIFICATION,Content)VALUES (" + this.getSqlStr(oaMaillBill.getMr_uuid()) + "," + this.getSqlStr(oaMaillBill.getIssendapart()) + "," + this.getSqlStr(oaMaillBill.getResourceid()) + "," + this.getSqlStr(oaMaillBill.getPriority()) + "," + this.getSqlStr(oaMaillBill.getSendfrom()) + "," + this.getSqlStr(oaMaillBill.getSenddate().substring(0, 19)) + "," + this.getSqlStr(oaMaillBill.getSize_n()) + "," + this.getSqlStr(oaMaillBill.getSubject()) + "," + this.getSqlStr(oaMaillBill.getMailtype()) + "," + this.getSqlStr(oaMaillBill.getHashtmlimage()) + "," + this.getSqlStr(oaMaillBill.getAttachmentnumber()) + "," + this.getSqlStr(oaMaillBill.getStatus()) + "," + this.getSqlStr(oaMaillBill.getFolderid()) + "," + this.getSqlStr(oaMaillBill.getMailaccountid()) + "," + this.getSqlStr(oaMaillBill.getIsinternal()) + "," + this.getSqlStr(oaMaillBill.getCanview()) + "," + this.getSqlStr(oaMaillBill.getNeedreceipt()) + "," + this.getSqlStr(oaMaillBill.getReceiveneedreceipt()) + "," + this.getSqlStr(oaMaillBill.getHaseml()) + "," + this.getSqlStr(oaMaillBill.getOriginalmailid()) + "," + this.getSqlStr(oaMaillBill.getReaddate()) + "," + this.getSqlStr(oaMaillBill.getTimingdatestate()) + "," + this.getSqlStr(oaMaillBill.getMit_uuid()) + "," + this.getSqlStr(oaMaillBill.getIsnewcontent()) + "," + this.getSqlStr(oaMaillBill.getContent_uuid()) + "," + this.getSqlStr(oaMaillBill.getClassification()) + "," + this.getSqlStr(oaMaillBill.getContent()) + ")";
        } while(this.RS.executeUpdate(sql, new Object[0]));

        throw new Exception("写入收件人信息报错sql:" + sql);
    }

    public void insertMailInternalto(String MIT_UUID, String TOIDS, String CCIDS, String BCCIDS) throws Exception {
        String sql = "INSERT INTO mail_internalto (MIT_UUID, TOIDS, CCIDS, BCCIDS) VALUES ('" + MIT_UUID + "', '" + TOIDS + "', '" + CCIDS + "', '" + BCCIDS + "')";
        if (!this.RS.executeUpdate(sql, new Object[0])) {
            throw new Exception("写入邮件人员信息报错sql:" + sql);
        }
    }

    public String getSqlStr(String nr) {
        return nr != null && !"".equals(nr) ? "'" + nr.replace("'", "''") + "'" : "NULL";
    }

    public void indestFile(List<OAMaillFileBill> oaMaillFileBillList) throws Exception {
        Iterator var2 = oaMaillFileBillList.iterator();

        String sql;
        do {
            if (!var2.hasNext()) {
                return;
            }

            OAMaillFileBill oaMaillBill = (OAMaillFileBill)var2.next();
            sql = "INSERT INTO mailresourcefile( MAILID, FILENAME, FILETYPE, FILEREALPATH, ISZIP, ISENCRYPT, ISFILEATTRACHMENT, FILESIZE, ISAESENCRYPT, AESCODE, MRF_UUID, STORAGESTATUS, SECRETLEVEL) VALUES ( '" + oaMaillBill.getMailid() + "', '" + oaMaillBill.getFilename() + "', '" + oaMaillBill.getFiletype() + "', '" + oaMaillBill.getFilerealpath() + "', '0', '0', '1', '" + oaMaillBill.getFilesize() + "', '0', 'JgPu7X8Jyrr4O', '" + oaMaillBill.getMrf_uuid() + "', '2', '4')";
        } while(this.RS.executeUpdate(sql, new Object[0]));

        throw new Exception("写入附件信息报错sql:" + sql);
    }
}
