//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package weaver.interfaces.interfaces.mail4.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.interfaces.mail4.dto.MailUserBill;
import weaver.interfaces.interfaces.mail4.dto.WhMailBill;
import weaver.interfaces.interfaces.mail4.dto.WhMailFileBill;

public class MailDao {
    private static final BaseBean logger = new BaseBean();

    public MailDao() {
    }

    public void clearMailData(String mail_id) {
        RecordSet rs = new RecordSet();
        RecordSet rs2 = new RecordSet();
        String sql = "SELECT id, MIT_UUID FROM mailresource WHERE CONCAT('', content)='" + mail_id + "'";
        logger.writeLog(sql);
        rs.execute(sql);

        while(rs.next()) {
            String mailid = rs.getString("id");
            logger.writeLog("找到id:" + mailid);
            String mit_uuid = rs.getString("MIT_UUID");
            String sql2 = "select filerealpath from mailresourcefile where mailid = " + mailid;
            logger.writeLog(sql2);
            rs2.execute(sql2);

            while(rs2.next()) {
                String filerealpath = rs2.getString("filerealpath");
                File file = new File(filerealpath);
                if (file.exists()) {
                    file.delete();
                    logger.writeLog(filerealpath + "附件已找到 物理文件已删除!");
                }
            }

            sql2 = "delete from mailcontent where mailid = " + mailid;
            logger.writeLog(sql2);
            boolean mailcontent = rs2.executeUpdate(sql2, new Object[0]);
            if (mailcontent) {
                logger.writeLog(mailid + "mailcontent删除成功!");
            } else {
                logger.writeLog(mailid + "mailcontent删除失败!");
            }

            sql2 = "delete from mailresourcefile where mailid = " + mailid;
            logger.writeLog(sql2);
            boolean mailresourcefile = rs2.executeUpdate(sql2, new Object[0]);
            if (mailresourcefile) {
                logger.writeLog(mailid + "mailresourcefile删除成功!");
            } else {
                logger.writeLog(mailid + "mailresourcefile删除失败!");
            }

            sql2 = "delete from mailresource where id = " + mailid;
            logger.writeLog(sql2);
            boolean mailresource = rs2.executeUpdate(sql2, new Object[0]);
            if (mailresource) {
                logger.writeLog(mailid + "mailresource删除成功!");
            } else {
                logger.writeLog(mailid + "mailresource删除失败!");
            }

            sql2 = "delete from mail_internalto where MIT_UUID = '" + mit_uuid + "'";
            logger.writeLog(sql2);
            boolean mail_internalto = rs2.executeUpdate(sql2, new Object[0]);
            if (mail_internalto) {
                logger.writeLog(mit_uuid + "mail_internalto删除成功!");
            } else {
                logger.writeLog(mit_uuid + "mail_internalto删除失败!");
            }
        }

    }

    public List<MailUserBill> getmailUserIds(String mail_id) {
        List<MailUserBill> userids = new ArrayList();
        RecordSetDataSource rs = new RecordSetDataSource("WH");
        String sql = "select MAILUSER_ID,MAIL_ID,EMP_ID,MAILSTATUS,MAILRECEIVETIME,MAILRETURNED,DOMAIN_ID,BOX_ID,NOTREAD from ezoffice.OA_MAIL_H_USER where MAIL_ID = '" + mail_id + "' and MAILSTATUS != 3";
        rs.execute(sql);

        while(rs.next()) {
            MailUserBill mailUserBill = new MailUserBill();
            mailUserBill.setMail_id(rs.getString("MAIL_ID"));
            mailUserBill.setMailreceivetime(rs.getString("MAILRECEIVETIME"));
            mailUserBill.setMailstatus(rs.getString("MAILSTATUS"));
            mailUserBill.setEmp_id(rs.getString("emp_id"));
            mailUserBill.setNotread(rs.getString("NOTREAD"));
            userids.add(mailUserBill);
        }

        return userids;
    }

    public int getMun(String tablename, String wheresql) {
        int sl = 0;
        RecordSetDataSource rs = new RecordSetDataSource("WH");
        String sql = "select count(1) as sl from " + tablename + "  where  oabs is null " + wheresql;
        rs.execute(sql);
        logger.writeLog(sql);
        if (rs.next()) {
//            sl = rs.getInt("sl");
        sl=10000;
        }


        return sl;
    }

    public List<WhMailBill> getWhMailBill(String tablename, String wheresql) {
        RecordSetDataSource rs = new RecordSetDataSource("WH");
        RecordSetDataSource rs2 = new RecordSetDataSource("WH");
        new RecordSetDataSource("WH");
        List<WhMailBill> whMailBills = new ArrayList();

        String sql = "select * from " + tablename + " a  where  a.oabs is null  " + wheresql;
        rs.execute(sql);
        logger.writeLog(sql);
        Map<String, String> orgmap = new HashMap();
        String cxsql = "select MAIL_ID, EMP_ID ,ORG_ID from Vew_mailemp ";
        rs2.execute(cxsql);

        while(rs2.next()) {
            orgmap.put(Util.null2String(rs2.getString("MAIL_ID")), Util.null2String(rs2.getString("ORG_ID")));
        }

        String orgid = "";
        List<String> mailids = new ArrayList();

        while(rs.next()) {
            boolean flag = false;
            String mail_id = rs.getString("MAIL_ID");
            if (!mailids.contains(mail_id)) {
                String ORG_ID = Util.null2String((String)orgmap.get(mail_id));
                if (!"".equals(ORG_ID)) {
                    flag = true;
                }

                mailids.add(mail_id);
            }

            if (flag) {
                WhMailBill whMailBill = new WhMailBill();
                whMailBill.setMail_id(rs.getString("MAIL_ID"));
                whMailBill.setMailsubject(rs.getString("MAILSUBJECT"));
                whMailBill.setMailcontenttype(rs.getString("MAILCONTENTTYPE"));
                whMailBill.setMailneedrevert(rs.getString("MAILNEEDREVERT"));
                whMailBill.setMailcontent(rs.getString("MAILCONTENT"));
                whMailBill.setMailhasaccessory(rs.getString("MAILHASACCESSORY"));
                whMailBill.setMaillevel(rs.getString("MAILLEVEL"));
                whMailBill.setMailisdraft(rs.getString("MAILISDRAFT"));
                whMailBill.setMailposttime(rs.getString("MAILPOSTTIME"));
                whMailBill.setMailreceivetime(rs.getString("MAILRECEIVETIME"));
                whMailBill.setMailpostername(rs.getString("MAILPOSTERNAME"));
                whMailBill.setMailposterid(rs.getString("MAILPOSTERID"));
                whMailBill.setAnonymous(rs.getString("ANONYMOUS"));
                whMailBill.setMailtosimple(rs.getString("MAILTOSIMPLE"));
                whMailBill.setMailccsimple(rs.getString("MAILCCSIMPLE"));
                whMailBill.setMailbccsimple(rs.getString("MAILBCCSIMPLE"));
                whMailBill.setMailsign(rs.getString("MAILSIGN"));
                whMailBill.setEncrypt(rs.getString("ENCRYPT"));
                whMailBill.setDomain_id(rs.getString("DOMAIN_ID"));
                whMailBill.setAccessorysize(rs.getString("ACCESSORYSIZE"));
                whMailBill.setMailrtmessage(rs.getString("MAILRTMESSAGE"));
                whMailBill.setCodetype(rs.getString("CODETYPE"));
                whMailBill.setMailto_c(rs.getString("MAILTO_C"));
                whMailBill.setMailtoid_c(rs.getString("MAILTOID_C"));
                whMailBill.setMailcc_c(rs.getString("MAILCC_C"));
                whMailBill.setMailccid_c(rs.getString("MAILCCID_C"));
                whMailBill.setMailbcc_c(rs.getString("MAILBCC_C"));
                whMailBill.setMailbccid_c(rs.getString("MAILBCCID_C"));
                whMailBills.add(whMailBill);
            }
        }

        return whMailBills;
    }

    public List<WhMailFileBill> getWhMailFileBills(String mailId) {
        List<WhMailFileBill> whMailFileBills = new ArrayList();
        RecordSetDataSource rs = new RecordSetDataSource("WH");
        String sql = "select * from ezoffice.oa_mailhaccessory where MAIL_ID = '" + mailId + "'";
        rs.execute(sql);

        while(rs.next()) {
            WhMailFileBill whMailFileBill = new WhMailFileBill();
            whMailFileBill.setAccessory_id(rs.getString("ACCESSORY_ID"));
            whMailFileBill.setMail_id(rs.getString("MAIL_ID"));
            whMailFileBill.setAccessoryname(rs.getString("ACCESSORYNAME"));
            whMailFileBill.setAccessorysavename(rs.getString("ACCESSORYSAVENAME"));
            whMailFileBill.setAccessorydescribe(rs.getString("ACCESSORYDESCRIBE"));
            whMailFileBill.setDomain_id(rs.getString("DOMAIN_ID"));
            whMailFileBills.add(whMailFileBill);
        }

        return whMailFileBills;
    }
}
