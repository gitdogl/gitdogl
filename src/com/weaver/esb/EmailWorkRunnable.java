package com.weaver.esb;

import com.api.email.constant.EmailPoolSubTypeEnum;
import com.api.email.util.EmailCommonUtils;
import org.apache.commons.lang.StringUtils;
import weaver.conn.RecordSet;
import weaver.email.MailCommonUtils;
import weaver.email.MailReciveStatusUtils;
import weaver.email.externalmail.send.EmailSend;
import weaver.email.externalmail.send.entity.EmailSendEntity;
import weaver.email.externalmail.send.entity.EmailSenderEntity;
import weaver.email.po.Mailconfigureinfo;
import weaver.email.service.EmailWorkRemindService;
import weaver.file.ImageFileManager;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;

import javax.mail.SendFailedException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

/**
 * 邮件提醒  公共现场类
 */
public class EmailWorkRunnable
        extends com.weaver.util.threadPool.entity.LocalRunnable {

    private BaseBean logBean = new BaseBean();

    private String sendTo = ""; //收件人，邮箱地址，多地址时用英文逗号分隔
    private String sendCc = ""; //抄送人，邮箱地址，多地址时用英文逗号分隔
    private String sendBcc = ""; //密送人，邮箱地址，多地址时用英文逗号分隔
    private String subject = ""; //主题，纯文本
    private String content = ""; //内容。纯文本或html字符串。（换行用<br/>）
    private String priority = "3"; // 邮件的重要性参数 3：普通 2：重要 4：紧急
    private int char_set = 3;//编码方式 1：iso-8859-1 2：big5 3：UTF-8

    private boolean sendEmailByHrmCard = false;//是否以人力卡片邮箱作为收件方 (true以下方三参数发信为准)
    private String hrmTo = ""; //收件人，人力id，多人时用英文逗号分隔
    private String hrmCc = ""; //抄送人，人力id，多人时用英文逗号分隔
    private String hrmBcc = ""; //密送人，人力id，多人时用英文逗号分隔

    private Map<String, String> filename_path = new HashMap<String, String>();//附件map<附件名，附件路径>
    private Map<String, InputStream> filename_stream = new HashMap<String, InputStream>();//附件map<附件名，附件流>
    private String docIds = "";//文档id，多个时英文逗号分隔
    private String imagefileids = "";//imagefile表记录id，多个时英文逗号分隔

    private EmailSenderEntity senderEntity;//调用方使用发件人实例
    private boolean isSystemSender = true;//默认使用系统群发邮件，若不适用，则置为false

    private boolean detach = false; //是否开启分权。默认fals。(兼容历史数据)，如果需要分权，必须要传true
    private String sendFromUserid = ""; //基于分权开启后，调用方的发起人userid
    private String sendFromSubCompanyid = ""; //基于分权开启后，调用方的发起分部id（分权优先级高于上述发起人userid）


    public boolean isDetach() {
        return detach;
    }

    public void setDetach(boolean detach) {
        this.detach = detach;
    }

    public String getSendFromUserid() {
        return sendFromUserid;
    }

    public void setSendFromUserid(String sendFromUserid) {
        this.sendFromUserid = sendFromUserid;
    }

    public String getSendFromSubCompanyid() {
        return sendFromSubCompanyid;
    }

    public void setSendFromSubCompanyid(String sendFromSubCompanyid) {
        this.sendFromSubCompanyid = sendFromSubCompanyid;
    }


    // 主键
    private String mwrl_uuid = null;

    public String getMwrl_uuid() {
        return mwrl_uuid;
    }

    public void setMwrl_uuid(String mwrl_uuid) {
        this.mwrl_uuid = mwrl_uuid;
    }


    public EmailSenderEntity getSenderEntity() {
        return senderEntity;
    }

    public void setSenderEntity(EmailSenderEntity senderEntity) {
        this.senderEntity = senderEntity;
    }

    public boolean isSystemSender() {
        return isSystemSender;
    }

    public void setSystemSender(boolean isSystemSender) {
        this.isSystemSender = isSystemSender;
    }

    public Map<String, String> getFilename_path() {
        return filename_path;
    }

    public void setFilename_path(Map<String, String> filename_path) {
        this.filename_path = filename_path;
    }

    public Map<String, InputStream> getFilename_stream() {
        return filename_stream;
    }

    public void setFilename_stream(Map<String, InputStream> filename_stream) {
        this.filename_stream = filename_stream;
    }

    public String getDocIds() {
        return docIds;
    }

    public void setDocIds(String docIds) {
        this.docIds = docIds;
    }

    public String getImagefileids() {
        return imagefileids;
    }

    public void setImagefileids(String imagefileids) {
        this.imagefileids = imagefileids;
    }

    public EmailWorkRunnable() {

    }

    /**
     * 构造方法
     *
     * @param sendTo  收件人邮箱地址（多个值之间用英文逗号分隔）
     * @param subject 主题
     * @param content 内容
     */
    public EmailWorkRunnable(String sendTo, String subject, String content) {
        this.sendTo = sendTo;
        this.subject = subject;
        this.content = content;
    }

    /**
     * 构造方法
     *
     * @param sendTo  收件人邮箱地址（多个值之间用英文逗号分隔）
     * @param sendCc  抄送人邮箱地址（多个值之间用英文逗号分隔）
     * @param sendBcc 密送人邮箱地址（多个值之间用英文逗号分隔）
     * @param subject 主题
     * @param content 内容
     */
    public EmailWorkRunnable(String sendTo, String sendCc, String sendBcc, String subject, String content) {
        this.sendTo = sendTo;
        this.sendCc = Util.null2String(sendCc);
        this.sendBcc = Util.null2String(sendBcc);
        this.subject = subject;
        this.content = content;
    }

    /**
     * 构造方法
     *
     * @param sendTo   收件人邮箱地址（多个值之间用英文逗号分隔）
     * @param sendCc   抄送人邮箱地址（多个值之间用英文逗号分隔）
     * @param sendBcc  密送人邮箱地址（多个值之间用英文逗号分隔）
     * @param subject  主题
     * @param content  内容
     * @param char_set
     * @param priority
     */
    public EmailWorkRunnable(String sendTo, String sendCc, String sendBcc, String subject, String content, int char_set, String priority) {
        this.sendTo = sendTo;
        this.sendCc = Util.null2String(sendCc);
        this.sendBcc = Util.null2String(sendBcc);
        this.subject = subject;
        this.content = content;
        this.char_set = char_set;
        this.priority = priority;
    }

    /**
     * 构造方法（收件人为人力资源id方式）
     *
     * @param hrmTo              收件人（多个值之间用英文逗号分隔）
     * @param hrmCc              抄送人（多个值之间用英文逗号分隔）
     * @param hrmBcc             密送人（多个值之间用英文逗号分隔）
     * @param subject            主题
     * @param content            内容
     * @param sendEmailByHrmCard 是否以人力资源方式获取邮箱地址
     */
    public EmailWorkRunnable(String hrmTo, String hrmCc, String hrmBcc, String subject, String content, boolean sendEmailByHrmCard) {
        this.hrmTo = hrmTo;
        this.hrmCc = Util.null2String(hrmCc);
        this.hrmBcc = Util.null2String(hrmBcc);
        this.subject = subject;
        this.content = content;
        this.sendEmailByHrmCard = sendEmailByHrmCard;
    }


    /**
     * 构造方法
     *
     * @param sendTo               收件人邮箱地址（多个值之间用英文逗号分隔）
     * @param sendCc               抄送人邮箱地址（多个值之间用英文逗号分隔）
     * @param sendBcc              密送人邮箱地址（多个值之间用英文逗号分隔）
     * @param subject              主题
     * @param content              内容
     * @param detach               是否开启分权。默认fals。(兼容历史数据)，如果需要分权，必须要传true
     * @param sendFromUserid       基于分权开启后，调用方的发起人userid
     * @param sendFromSubCompanyid 基于分权开启后，调用方的发起分部id（分权优先级高于上述发起人userid）
     */
    public EmailWorkRunnable(String sendTo, String sendCc, String sendBcc, String subject, String content, boolean detach, String sendFromUserid, String sendFromSubCompanyid) {
        this.sendTo = sendTo;
        this.sendCc = Util.null2String(sendCc);
        this.sendBcc = Util.null2String(sendBcc);
        this.subject = subject;
        this.content = content;
        this.detach = detach;
        this.sendFromUserid = sendFromUserid;
        this.sendFromSubCompanyid = sendFromSubCompanyid;
    }

    /**
     * 执行发送方法
     *
     * @throws Exception
     */
    @Override
    public void execute() throws Exception {
        emailCommonRemind();
    }

    /**
     * 线程方式发送(有线程池，适用于异步、量大情况)
     *
     * @param sendTo               收件人
     * @param subject              主题
     * @param content              内容
     * @param detach               是否开启分权。默认fals。(兼容历史数据)，如果需要分权，必须要传true
     * @param sendFromUserid       基于分权开启后，调用方的发起人userid
     * @param sendFromSubCompanyid 基于分权开启后，调用方的发起分部id（分权优先级高于上述发起人userid）
     */
    public static void threadModeReminder(String sendTo, String subject, String content, boolean detach, String sendFromUserid, String sendFromSubCompanyid) {
        threadModeReminder(sendTo, "", "", subject, content, detach, sendFromUserid, sendFromSubCompanyid);
    }

    /**
     * 线程方式发送(有线程池，适用于异步、量大情况)
     *
     * @param sendTo  收件人
     * @param subject 主题
     * @param content 内容
     */
    public static void threadModeReminder(String sendTo, String subject, String content) {
        threadModeReminder(sendTo, "", "", subject, content);
    }

    /**
     * 线程方式发送(有线程池，适用于异步、量大情况)
     *
     * @param sendTo  收件人
     * @param sendCc  抄送人
     * @param sendBcc 密送人
     * @param subject 主题
     * @param content 内容
     */
    public static void threadModeReminder(String sendTo, String sendCc, String sendBcc, String subject, String content) {
        //这里有个特殊使用场景。比如：邮箱服务器并行发送量限制了。可以通过限制线程池并发大小达到效果。
        //MailCommonUtils.SYS_MAIL_ALERT_POOL.execute(new EmailWorkRunnable(sendTo, sendCc, sendBcc, subject, content));

        EmailWorkRunnable ewr = new EmailWorkRunnable(sendTo, sendCc, sendBcc, subject, content);
        MailCommonUtils.executeThreadPool(EmailPoolSubTypeEnum.EMAIL_SYS_ALTER.toString(), ewr);
    }

    /**
     * 线程方式发送(有线程池，适用于异步、量大情况)
     *
     * @param sendTo               收件人
     * @param sendCc               抄送人
     * @param sendBcc              密送人
     * @param subject              主题
     * @param content              内容
     * @param detach               是否开启分权。默认fals。(兼容历史数据)，如果需要分权，必须要传true
     * @param sendFromUserid       基于分权开启后，调用方的发起人userid
     * @param sendFromSubCompanyid 基于分权开启后，调用方的发起分部id（分权优先级高于上述发起人userid）
     */
    public static void threadModeReminder(String sendTo, String sendCc, String sendBcc, String subject, String content, boolean detach, String sendFromUserid, String sendFromSubCompanyid) {
        //这里有个特殊使用场景。比如：邮箱服务器并行发送量限制了。可以通过限制线程池并发大小达到效果。
        //MailCommonUtils.SYS_MAIL_ALERT_POOL.execute(new EmailWorkRunnable(sendTo, sendCc, sendBcc, subject, content));

        EmailWorkRunnable ewr = new EmailWorkRunnable(sendTo, sendCc, sendBcc, subject, content, detach, sendFromUserid, sendFromSubCompanyid);
        MailCommonUtils.executeThreadPool(EmailPoolSubTypeEnum.EMAIL_SYS_ALTER.toString(), ewr);
    }

    /**
     * 线程方式发送(有线程池，适用于异步、量大情况)
     *
     * @param sendTo       收件人
     * @param sendCc       抄送人
     * @param sendBcc      密送人
     * @param subject      主题
     * @param content      内容
     * @param imagefileids imagefile表记录id，多个时英文逗号分隔
     */
    public static void threadModeReminder(String sendTo, String sendCc, String sendBcc, String subject, String content, String imagefileids) {
        //这里有个特殊使用场景。比如：邮箱服务器并行发送量限制了。可以通过限制线程池并发大小达到效果。
        //MailCommonUtils.SYS_MAIL_ALERT_POOL.execute(new EmailWorkRunnable(sendTo, sendCc, sendBcc, subject, content));

        EmailWorkRunnable ewr = new EmailWorkRunnable(sendTo, sendCc, sendBcc, subject, content);
        ewr.setImagefileids(imagefileids);
        MailCommonUtils.executeThreadPool(EmailPoolSubTypeEnum.EMAIL_SYS_ALTER.toString(), ewr);
    }

    /**
     * 线程方式发送(有线程池，适用于异步、量大情况)
     *
     * @param sendTo               收件人
     * @param sendCc               抄送人
     * @param sendBcc              密送人
     * @param subject              主题
     * @param content              内容
     * @param imagefileids         imagefile表记录id，多个时英文逗号分隔
     * @param detach               是否开启分权。默认fals。(兼容历史数据)，如果需要分权，必须要传true
     * @param sendFromUserid       基于分权开启后，调用方的发起人userid
     * @param sendFromSubCompanyid 基于分权开启后，调用方的发起分部id（分权优先级高于上述发起人userid）
     */
    public static void threadModeReminder(String sendTo, String sendCc, String sendBcc, String subject, String content, String imagefileids, boolean detach, String sendFromUserid, String sendFromSubCompanyid) {
        //这里有个特殊使用场景。比如：邮箱服务器并行发送量限制了。可以通过限制线程池并发大小达到效果。
        //MailCommonUtils.SYS_MAIL_ALERT_POOL.execute(new EmailWorkRunnable(sendTo, sendCc, sendBcc, subject, content));

        EmailWorkRunnable ewr = new EmailWorkRunnable(sendTo, sendCc, sendBcc, subject, content, detach, sendFromUserid, sendFromSubCompanyid);
        ewr.setImagefileids(imagefileids);
        MailCommonUtils.executeThreadPool(EmailPoolSubTypeEnum.EMAIL_SYS_ALTER.toString(), ewr);
    }

    /**
     * 邮件提醒公共实现方法
     *
     * @return true：投递成功。false：投递失败
     */
    public boolean emailCommonRemind() {

        boolean flag = true;
        EmailWorkRemindService logService = new EmailWorkRemindService();

        if (sendEmailByHrmCard) {
            logService.startLog(this.subject, this.hrmTo, this.hrmCc, this.hrmBcc, this.content, this.detach ? "1" : "0", this.sendFromSubCompanyid, this.sendFromUserid); //记录日志
        } else {
            logService.startLog(this.subject, this.sendTo, this.sendCc, this.sendBcc, this.content, this.detach ? "1" : "0", this.sendFromSubCompanyid, this.sendFromUserid); //记录日志
        }
        this.setMwrl_uuid(logService.getMwrl_uuid());

        String defmailfrom = "";
        StringBuffer errorInfo = new StringBuffer(); //日志错误详情

        String subcompanyid = "0";
        if (detach) {
            if (!"".equals(sendFromSubCompanyid)) {
                subcompanyid = sendFromSubCompanyid;
            } else if (!"".equals(sendFromUserid)) {
                try {
                    ResourceComInfo rc = new ResourceComInfo();
                    subcompanyid = rc.getSubCompanyID(sendFromUserid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                logService.updateFinalSubcompanyid(""); // 记录日志
                errorInfo.append("无有效分权数据" + "\n");
                logService.setError(errorInfo.toString());
                return flag = false;
            }
            logService.updateFinalSubcompanyid(subcompanyid);
        }
        EmailSenderEntity sender = EmailSend.getEmailSenderOfSystemSet(subcompanyid);
        if (!isSystemSender) {
            sender = this.senderEntity;
        }
        defmailfrom = sender.getEmailAddress();
        if ("".equals(Util.null2String(defmailfrom))) {
            errorInfo.append("" + weaver.systeminfo.SystemEnv.getHtmlLabelName(10003448, weaver.general.ThreadVarLanguage.getLang()) + "\n");
            logService.setError(errorInfo.toString());
            return flag = false;
        }
        logService.updateSendFromInfo(defmailfrom); // 记录日志

        // 采用人力资源id发信
        if (sendEmailByHrmCard) {
            // 取人员对应卡片邮箱地址
            this.sendTo = this.getEmailFromHrm(this.hrmTo);
            this.sendCc = this.getEmailFromHrm(this.hrmCc);
            this.sendBcc = this.getEmailFromHrm(this.hrmBcc);
        }

        String finalSubject = MailCommonUtils.toHtmlMode(Util.formatMultiLang(this.subject)); // 主题（转为html模式，并过滤非正常的多语言标签）
        String finalContent = MailCommonUtils.filterContentForRemind(Util.formatMultiLang(this.content)); // 邮件内容（转为html模式，并过滤非正常的多语言标签）
        String finalTo = this.sendTo; // 收件人地址
        String finalCc = this.sendCc; // 抄送人地址
        String finalBcc = this.sendBcc; // 密送人地址

        // 对收件人错误地址进行过滤
        List<String> finalAddressList = new ArrayList<String>();
        for (String address : this.sendTo.split(",")) {
            if (Util.isEmail(address)) {
                finalAddressList.add(address);
            } else {
                errorInfo.append("" + weaver.systeminfo.SystemEnv.getHtmlLabelName(10003449, weaver.general.ThreadVarLanguage.getLang()) + "" + address + "。\n");
            }
        }
        finalTo = StringUtils.join(finalAddressList, ",");
        logService.updateFinalToInfo(1, finalTo);

        // 对抄送人错误地址进行过滤
        if (!"".equals(this.sendCc)) {
            List<String> finalCcAddressList = new ArrayList<String>();
            for (String address : this.sendCc.split(",")) {
                if (Util.isEmail(address)) {
                    finalCcAddressList.add(address);
                } else {
                    errorInfo.append("" + weaver.systeminfo.SystemEnv.getHtmlLabelName(10003450, weaver.general.ThreadVarLanguage.getLang()) + "" + address + "。\n");
                }
            }
            finalCc = StringUtils.join(finalCcAddressList, ",");
            logService.updateFinalToInfo(2, finalCc);
        }

        // 对密送人错误地址进行过滤
        if (!"".equals(this.sendBcc)) {
            List<String> finalBccAddressList = new ArrayList<String>();
            for (String address : this.sendBcc.split(",")) {
                if (Util.isEmail(address)) {
                    finalBccAddressList.add(address);
                } else {
                    errorInfo.append("" + weaver.systeminfo.SystemEnv.getHtmlLabelName(10003451, weaver.general.ThreadVarLanguage.getLang()) + "" + address + "。\n");
                }
            }
            finalBcc = StringUtils.join(finalBccAddressList, ",");
            logService.updateFinalToInfo(3, finalBcc);
        }

        // 校验是否有收件人
        if (finalTo.isEmpty()) {
            errorInfo.append("" + weaver.systeminfo.SystemEnv.getHtmlLabelName(10003452, weaver.general.ThreadVarLanguage.getLang()) + "\n");
            logService.setError(errorInfo.toString());
            return flag = false;
        }

        // subject邮件标题，content邮件正文
        if ("".equals(finalCc)) {
            finalCc = null;
        }
        if ("".equals(finalBcc)) {
            finalBcc = null;
        }
        Map<String, ArrayList> file_map = new HashMap<String, ArrayList>();
        try {
            imagefileids = MailCommonUtils.trim(imagefileids);
            file_map = this.getFileMap(filename_path, filename_stream, docIds, imagefileids);

            EmailSend emailSend = new EmailSend();
            EmailSendEntity entity = emailSend.packParameterToEntity(
                    sender.getEmailAddress(), finalTo, finalCc, finalBcc,
                    finalSubject, char_set, finalContent, null,
                    file_map.get("filename"), file_map.get("filestream"), 0,
                    null, false, priority, "0", true);
            entity.getFrom().setNickname(sender.getNickName());

            boolean re = emailSend.sendMail(sender, entity);

            // 发送成功时
            if (re) {
                if (isRecordSuccessLog(subcompanyid)) {
                    logBean.writeLog("是否开启记录发送成功日志成功=" + subcompanyid);
                    logService.setSuccess(errorInfo.toString());
                } else {
                    logBean.writeLog("是否开启记录发送成功日志失败=" + subcompanyid);
                    logService.removeSuccess();
                }
            } else {
                flag = false;
                errorInfo.append("" + weaver.systeminfo.SystemEnv.getHtmlLabelName(10003453, weaver.general.ThreadVarLanguage.getLang()) + "false，" + weaver.systeminfo.SystemEnv.getHtmlLabelName(10003454, weaver.general.ThreadVarLanguage.getLang()) + "null" + weaver.systeminfo.SystemEnv.getHtmlLabelName(10003455, weaver.general.ThreadVarLanguage.getLang()) + "");
                logService.setError(errorInfo.toString());
            }
        } catch (SendFailedException e) {
            logBean.writeLog("EmailWorkRunnable:群发提醒失败，包含无效的收件地址。, from=" + defmailfrom + ",subject=" + Util.toHtmlMode(this.subject));
            // 非法地址
            javax.mail.Address[] invalid = e.getInvalidAddresses();
            List<String> invalidAddressList = new ArrayList<String>();
            if (invalid != null) {
                logBean.writeLog("invalid:" + invalid.length);
                for (int i = 0; i < invalid.length; i++) {
                    invalidAddressList.add(invalid[i].toString());
                }
                String inv = StringUtils.join(invalidAddressList, ",");
                errorInfo.append("" + weaver.systeminfo.SystemEnv.getHtmlLabelName(10003456, weaver.general.ThreadVarLanguage.getLang()) + "" + inv);
                logService.setError(errorInfo.toString());
            }

            // 合法未发送地址
            javax.mail.Address[] validAddresses = e.getValidUnsentAddresses();
            List<String> finalAddressList_twice = new ArrayList<String>();
            if (validAddresses != null) {
                for (int i = 0; i < validAddresses.length; i++) {
                    finalAddressList_twice.add(validAddresses[i].toString());
                }
                if (finalAddressList_twice.size() > 0) {
                    finalTo = StringUtils.join(finalAddressList_twice, ",");
                    logBean.writeLog("收件人合法未发送地址：" + finalTo);
                    try {
                        EmailSend emailSend = new EmailSend();
                        EmailSendEntity entity = emailSend
                                .packParameterToEntity(
                                        sender.getEmailAddress(), finalTo,
                                        finalCc, finalBcc, finalSubject,
                                        char_set, finalContent, null,
                                        file_map.get("filename"),
                                        file_map.get("filestream"), 0, null,
                                        false, priority, "0", true);
                        entity.setTrackingInfo("EmailWorkRunnable");
                        entity.getFrom().setNickname(sender.getNickName());
                        boolean re = emailSend.sendMail(sender, entity);
                        // 发送成功时
                        if (re) {
                            if (isRecordSuccessLog(subcompanyid)) {
                                logService.setSuccess(errorInfo.toString());
                            } else {
                                logService.removeSuccess();
                            }
                        }
                    } catch (Exception e1) {
                        flag = false;
                        errorInfo.append("  " + weaver.systeminfo.SystemEnv.getHtmlLabelName(10003457, weaver.general.ThreadVarLanguage.getLang()) + "" + MailCommonUtils.getStackTrace(e1) + "\n");

                        //第二次发送失败时，记录失败日志以及过滤的邮箱
                        logService.setError(errorInfo.toString());
                    }

                } else {
                    flag = false;
                    errorInfo.append(" " + weaver.systeminfo.SystemEnv.getHtmlLabelName(10003458, weaver.general.ThreadVarLanguage.getLang()) + "");
                    logService.setError(errorInfo.toString());
                }

            }
        } catch (Throwable e) {
            flag = false;
            logBean.writeLog("EmailWorkRunnable:群发提醒失败。, from=" + defmailfrom + ",subject=" + Util.toHtmlMode(this.subject));
            logBean.writeLog(e.getMessage());

            // 发送失败时，记录日志
            errorInfo.append(MailCommonUtils.getStackTrace(e) + "\n");
            logService.setError(errorInfo.toString());
        }
        return flag;
    }

    /**
     * 是否开启记录发送成功日志。
     *
     * @return
     */
    private boolean isRecordSuccessLog(String subcompanyid) {
        boolean result = false;
        try {
            Mailconfigureinfo mailconfigureinfo = MailReciveStatusUtils.getMailconfigureinfoFromCache(EmailCommonUtils.getSubcompanyidByDetach(Util.getIntValue(subcompanyid)));
            int isRecordSuccessMailRemindLog = mailconfigureinfo.getIsRecordSuccessMailRemindLog();
            result = isRecordSuccessMailRemindLog == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据传入的附件参数获取附件map
     *
     * @param filename_path
     * @param filename_stream
     * @param docIds
     * @param imagefileids
     * @return
     * @throws FileNotFoundException
     */
    private Map<String, ArrayList> getFileMap(Map<String, String> filename_path,
                                              Map<String, InputStream> filename_stream, String docIds, String imagefileids) throws FileNotFoundException {
        Map file_map = new HashMap();

        ArrayList filenames = new ArrayList();
        ArrayList filecontents = new ArrayList();
        List<String> imagefileid_list = new ArrayList<String>();
        if (!imagefileids.isEmpty()) {
            imagefileid_list = Arrays.asList(imagefileids.split(","));
        }

        // 传路径获取附件
        if (!filename_path.isEmpty()) {
            Iterator<Entry<String, String>> filename_path_entries = filename_path.entrySet().iterator();
            String filename = "";
            String filepath = "";
            while (filename_path_entries.hasNext()) {
                Entry<String, String> entry = filename_path_entries.next();
                filename = entry.getKey();
                filepath = entry.getValue();
                File file = new File(filepath);
                InputStream fis = new FileInputStream(file);
                filenames.add(filename);
                filecontents.add(fis);
            }
        }

        // 传文件流获取附件
        if (!filename_stream.isEmpty()) {
            Iterator<Entry<String, InputStream>> filename_stream_entries = filename_stream.entrySet().iterator();
            String filename = "";
            while (filename_stream_entries.hasNext()) {
                Entry<String, InputStream> entry = filename_stream_entries.next();
                filename = entry.getKey();
                InputStream fis = entry.getValue();
                filenames.add(filename);
                filecontents.add(fis);
            }
        }

        // 传docids流获取附件，目前只兼容一个文档id对应一个附件的情况,并且发送versionid最新的附件
        if (!docIds.isEmpty()) {
            RecordSet rs = new RecordSet();
            rs.executeQuery(" select  imagefileid,docid from  docimagefile  where docid in (" + docIds + ") order by versionId desc");
            Set<String> docid_set = new HashSet<String>();
            while (rs.next()) {
                String docid = rs.getString("docid");
                if (docid_set.contains(docid)) {
                    continue;
                }
                String imagefileid = rs.getString("imagefileid");
                imagefileid_list.add(imagefileid);
            }
        }

        // 传imagefileid获取附件
        if (imagefileid_list.size() > 0) {
            for (String fileid : imagefileid_list) {
                if (!"".equals(fileid)) {
                    ImageFileManager imageFileManager = new ImageFileManager();
                    imageFileManager.getImageFileInfoById(Integer.parseInt(fileid));
                    filenames.add(imageFileManager.getImageFileName());
                    filecontents.add(getInputStreamByImagefileId(Integer.parseInt(fileid)));
                }
            }
        }

        file_map.put("filename", filenames);
        file_map.put("filestream", filecontents);
        return file_map;
    }

    /**
     * 根据imagefileid获取文件流
     *
     * @param imagefileid
     * @return
     */
    public InputStream getInputStreamByImagefileId(int imagefileid) {
        InputStream inputStream = null;
        if (imagefileid > 0) {
            ImageFileManager imageFileManager = new ImageFileManager();
            imageFileManager.getImageFileInfoById(imagefileid);
            inputStream = imageFileManager.getInputStream();
        }
        return inputStream;
    }

    /**
     * 用人力资源id获取 email字段邮箱地址
     *
     * @param hrmIds
     * @return
     */
    private String getEmailFromHrm(String hrmIds) {
        String ids = MailCommonUtils.trim(Util.null2String(hrmIds));
        if ("".equals(ids)) {
            return "";
        }
        StringBuilder cardEmail = new StringBuilder();
        RecordSet rs = new RecordSet();
        rs.executeQuery("select email from hrmresource where " + MailCommonUtils.getSubINClause(hrmIds, "id", "in", 999));
        while (rs.next()) {
            cardEmail.append("," + rs.getString("email"));
        }
        return cardEmail.toString();
    }
}



