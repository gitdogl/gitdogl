package weaver.email;

import com.alibaba.fastjson.JSON;
import com.api.doc.util.DocEncryptUtil;
import com.api.email.bean.EmailSettingBean;
import com.api.email.constant.EmailConstant;
import com.api.email.constant.EmailPoolSubTypeEnum;
import com.api.email.service.EmailSettingService;
import com.api.email.util.EmailCommonUtils;
import com.cloudstore.dev.api.service.ServiceMessageCustomImpl;
import com.engine.email.biz.sendSessionUUid.EmailSendSessionUUidBiz;
import com.engine.email.entity.EmailDeleteType;
import com.engine.email.entity.EmailInternalBrowserEntity;
import com.engine.email.entity.EmailInternalToEntity;
import com.engine.email.util.EmailDateTimeUtil;
import com.engine.hrm.biz.HrmClassifiedProtectionBiz;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.*;
import weaver.conn.BatchRecordSet;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetTrans;
import weaver.email.externalmail.EmailContentTypeUtils;
import weaver.email.externalmail.send.EmailSend;
import weaver.email.externalmail.send.entity.EmailSendEntity;
import weaver.email.externalmail.send.entity.EmailSendFileOldEntity;
import weaver.email.externalmail.send.entity.EmailSenderEntity;
import weaver.email.po.MailAccountComInfo;
import weaver.email.service.*;
import weaver.encrypt.EncryptUtil;
import weaver.file.FileUpload;
import weaver.file.ImageFileManager;
import weaver.file.Prop;
import weaver.filter.login.AESCoder;
import weaver.general.BaseBean;
import weaver.general.GCONST;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.hrm.appdetach.AppDetachComInfo;
import weaver.mobile.plugin.ecology.service.HrmResourceService;
import weaver.systeminfo.SystemEnv;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.ZipInputStream;

/**
 * 邮件发送
 */
public class MailSend extends BaseBean {

    private String priority = "";
    private String sendfrom = "";
    private String CC = "";
    private String BCC = "";
    private String TO = "";
    private String subject = "";
    private String texttype = "";
    private String content = "";
    private ArrayList filenames = new ArrayList();
    private ArrayList filetypes = new ArrayList();
    private ArrayList filecontents = new ArrayList();
    private int sf = 0; //发送不存已发送邮件的标志
    private int isInternal = 0;
    private String mailid = "";
    private String savedraft = "";
    private String timingdate = "";// 定时发送时间
    private String needReceipt = "";// 是否需要回执
    private String ccdpids = ""; // 抄送部门id
    private String bccdpids = ""; // 秘送部门id
    private String todpids = ""; // 接收人部门id
    private String toall = ""; // 接收人为所有人[1--为所有人，其他相反]
    private String ccall = ""; // 抄送人为所有人[1--为所有人，其他相反]
    private String bccall = ""; // 密送人为所有人[1--为所有人，其他相反]
    private String toids = ""; // 接收人ids
    private String ccids = ""; // 抄送人ids
    private String bccids = ""; // 密送人ids
    private int languageid = 7;
    private String internalccnewIds = ""; // 抄送人ids
    private String internaltonewIds = ""; // 接收人ids
    private String internalbccnewIds = ""; // 密送人ids
    Map<String, EmailInternalToEntity> sendtoNewMap = new HashMap<>(); //人力资源框数据集
    Map<String, List<EmailInternalBrowserEntity>> sendtoNoResourceNewMap = new HashMap<>();//过滤掉resource类型后的人力资源框数据集

    private String finalFileIds = ""; //最终的附件ids，有序

    private String allids = "";
    private boolean isUseAppDetach = false;//是否分权

    private boolean isSendApart = false; //是否是分别发送

    private String autoSave = "";   //自动保存到草稿
    private String timingsubmitType = "";


    public String getTimingsubmitType() {
        return timingsubmitType;
    }

    public void setTimingsubmitType(String timingsubmitType) {
        this.timingsubmitType = timingsubmitType;
    }

    public String getAutoSave() {
        return autoSave;
    }

    public void setAutoSave(String autoSave) {
        this.autoSave = autoSave;
    }

    public String getFinalFileIds() {
        return finalFileIds;
    }

    public void setFinalFileIds(String finalFileIds) {
        this.finalFileIds = finalFileIds;
    }

    /**
     * 发送失败时，返回给前台的错误信息
     */
    private MailErrorMessageInfo errorMessInfo = null;

    public MailErrorMessageInfo getErrorMessInfo() {
        return errorMessInfo;
    }

    public void setErrorMessInfo(String errorMess) {
        if ("".equals(errorMess)) {
            this.errorMessInfo = new MailErrorFormat(errorMess).getMailErrorMessageInfo();
        } else {
            try {
                errorMess = errorMess.replace("\n", "<br/>").replace("\r", "");
                JSONObject errorMessJson = JSONObject.fromObject(errorMess);
                this.errorMessInfo = (MailErrorMessageInfo) JSONObject.toBean(errorMessJson, MailErrorMessageInfo.class);
            } catch (Exception e) {
                writeLog(e);
                this.errorMessInfo = new MailErrorFormat("").getMailErrorMessageInfo();
            }
        }
    }


    public int getIsInternal() {
        return isInternal;
    }

    public void setIsInternal(int isInternal) {
        this.isInternal = isInternal;
    }

    public String getMailId() {
        return this.mailid;
    }

    public String getSavedraft() {
        return this.savedraft;
    }

    public String getTimingdate() {
        return timingdate;
    }

    /**
     * 解决字符串中，前面，中间，后面多余的逗号，返回左右无逗号的字符串
     * 如：",,,,2,3" 输出 2,3
     * 如：",,,,2,,3" 输出 2,3
     * 如：",,,,2,3,,,,," 输出 2,3
     * 如：",,2,,3,," 输出 2,3
     * 注意：此方法效率高于list后再join。
     *
     * @param str
     * @return
     */
    public static String trim(String str) {
        return MailCommonUtils.trim(str);
    }

    /**
     * 1、过滤htmlStr中的所有script,style,及<>标签内容为空字符串；
     * 2、过滤所有&nbsp;为" "空格；
     * 3、返回过滤处理后trim字符串。
     *
     * @param htmlStr
     * @return
     */
    public static String stripHtml(String htmlStr) {
        return MailCommonUtils.stripHtml(htmlStr);
    }

    /**
     * 获取最终的入库收件人，部门信息。
     *
     * @param internalto
     * @return
     */
    private String getRealInternalTo(String internalto) {
        internalto = MailCommonUtils.trim(Util.null2String(internalto));
        if ("0".equals(internalto)) {
            internalto = "";
        }
        return internalto;
    }

    /**
     * 获取内部邮件真实的hrmAccountId参数值。
     * 规则：1、外部邮件等于前台传递值。
     * 2.内部邮件，判断此值是否是当前人及当前人此帐号。如果不是则置为当前人。（放置修改前台参数导致发件人变更漏洞）
     *
     * @param isInternal
     * @param userId
     * @param hrmAccountid
     * @return
     */
    public int getRealHrmAccountId(int isInternal, int userId, int hrmAccountid) {
        int realHrmAccountid = hrmAccountid;
        if (isInternal == 1 && userId > 0) {
            String resourceids = "," + MailManagerService.getAllResourceids(String.valueOf(userId)) + ",";
            if (!resourceids.contains("," + hrmAccountid + ",")) {
                realHrmAccountid = userId;
            }
        }
        return realHrmAccountid;
    }

    /**
     * 邮件模块页面，发送邮件总入口
     *
     * @param request
     * @param user
     * @return
     * @throws Exception
     */
    public String sendMail(HttpServletRequest request, User user) throws Exception {
        EmailDateTimeUtil edt = new EmailDateTimeUtil();
        FileUpload fu = new FileUpload(request, false);
        RecordSet rs = new RecordSet();

        //防止url炸弹攻击,方案改为数据库机制
        String sessionUUid = Util.null2String(fu.getParameter("sessionUUid"));
        boolean cansend = EmailSendSessionUUidBiz.getSendSessionUUid(user, sessionUUid);
        if (!cansend) {
            writeLog("当前sessionUUid为：" + sessionUUid + ";当前用户为：" + user.getUID());
            return "false2";
        }

        this.languageid = user.getLanguage();

        // 获取邮件内容中需要上传的图片信息

        String _accountId = "";
        int _mailAccountId = Util.getIntValue(fu.getParameter("mailAccountId")); // 邮件账户id
        int _hrmAccountid = Util.getIntValue(fu.getParameter("hrmAccountid")); // 人力资源账户id
        int _folderId = Util.getIntValue(fu.getParameter("folderid"), -1);// -1:SentBox -2:DraftBox -3:TrashBox

        String _status = "1"; // 0:未读 1:已读

        // 是否内部邮件，为1是内部邮件，否则是外部邮件
        isInternal = Util.getIntValue(fu.getParameter("isInternal"));

        //保存到发件箱(1--保存到发件箱，""为没有勾选【保存到发件箱】)
        String savesend = Util.null2String(fu.getParameter("savesend"));
        if ("0".equals(savesend)) {
            savesend = "";
        }
        String _tempsavesend = savesend;
        //保存到草稿箱【1--保存到草稿箱，0---不保存到草稿箱】
        String savedraft = Util.null2String(fu.getParameter("savedraft"));
        //自动保存草稿
        String autoSave = Util.null2String(fu.getParameter("autoSave"));
        this.autoSave = autoSave;

        timingsubmitType = Util.null2String(fu.getParameter("timingsubmitType")); //submit定时发送，save保存的定时发送邮件，后期修改

        // E9添加
        int opIsTimingSend = Util.getIntValue(request.getParameter("op_isTimingSend"));
        if (opIsTimingSend == 1) {
            if ("1".equals(savedraft)) {
                timingsubmitType = "save";
            } else {
                timingsubmitType = "submit";
            }
            savedraft = "1";
        }

        //定时发送时间
        this.timingdate = Util.null2String(fu.getParameter("timingdate"));
        //转换为服务器时间（多时区处理）
        this.timingdate = edt.getServerDateTime(this.timingdate);

        // 是否需要回执
        this.needReceipt = Util.null2o(fu.getParameter("needReceipt"));

        //是否是分别发送
        this.isSendApart = "1".equals(Util.null2o(fu.getParameter("isSendApart")));

        this.savedraft = savedraft;
        priority = Util.null2String(fu.getParameter("priority"));  // 邮件重要级别
        priority = "1".equals(priority) ? priority : "3";

        int all = 0;//发送给所有人标记  0为非

        // 邮件资源密级 机密1;秘密2;内部3;公开4
        String classification = Util.null2o(fu.getParameter("classification"));
        HrmClassifiedProtectionBiz hrmcp = new HrmClassifiedProtectionBiz();
        boolean isOpenClassification = hrmcp.isOpenClassification(); //是否开启密级
        if (!isOpenClassification) {
            //密级没开的时候置为空
            classification = "";
        }
        if (isInternal == 1) {

            //开启密级的时候，强校验密级权限，防止密级越权
            if (isOpenClassification) {
                //取当事人所能获取到的最高的资源密级(兼容了主次账号)

                String maxResourceClassification = hrmcp.getMaxResourceSecLevelById(user.getUID() + "");
                if (classification.compareTo(maxResourceClassification) < 0) {
                    writeLog("防止密级越权：" + ";当前密级为：" + classification + ";当前用户最高资源密级为：" + maxResourceClassification);
                    this.errorMessInfo = new MailErrorMessageInfo();
                    this.errorMessInfo.setErrorHint("" + SystemEnv.getHtmlLabelName(10003483, weaver.general.ThreadVarLanguage.getLang()) + ""); //密级越权
                    this.errorMessInfo.setSolution("" + SystemEnv.getHtmlLabelName(10003483, weaver.general.ThreadVarLanguage.getLang()) + ""); //密级越权
                    this.errorMessInfo.setErrorString("" + SystemEnv.getHtmlLabelName(10003484, weaver.general.ThreadVarLanguage.getLang()) + "");//"+weaver.systeminfo.SystemEnv.getHtmlLabelName(10003484,weaver.general.ThreadVarLanguage.getLang())+"
                    return "false6";
                }
            }
            _mailAccountId = -1;
            // E9页面的发送方式
            String internaltonew = Util.null2String(fu.getParameter("internaltonew")); //(新)内部邮件收件人
            String internalccnew = Util.null2String(fu.getParameter("internalccnew")); //(新)内部邮件抄送人
            String internalbccnew = Util.null2String(fu.getParameter("internalbccnew")); //(新)内部邮件密送人

            if (!internaltonew.isEmpty() || !internalccnew.isEmpty() || !internaltonew.isEmpty()) {
                //密级过滤后的完整人员集合数据
                sendtoNewMap = EmailCommonUtils.getInternalToNewByClassification(internaltonew, internalccnew, internalbccnew, classification);
                //之前人员id串是和人员数据json分开取的，现在整个合在一起，通过json解析获取
                //（所有人也一并传过来了） 所以不需要额外通过所有人标志去实时再取了
                internaltonewIds = sendtoNewMap.get(EmailConstant.TOIDS).getInternalUserids();  //(新)内部邮件收件人ids
                internalccnewIds = sendtoNewMap.get(EmailConstant.CCIDS).getInternalUserids(); //(新)内部邮件抄送人ids
                internalbccnewIds = sendtoNewMap.get(EmailConstant.BCCIDS).getInternalUserids(); //(新)内部邮件密送人ids


                //取过滤掉人员 resource 类型的浏览框数集合，只含有分部，部门，组等信息
                sendtoNoResourceNewMap = EmailCommonUtils.getInternalToNewNoResourceType(sendtoNewMap);
            }

            int count = Util.TokenizerString(MailCommonUtils.trim(internaltonewIds), ",").size()
                    + Util.TokenizerString(MailCommonUtils.trim(internalccnewIds), ",").size()
                    + Util.TokenizerString(MailCommonUtils.trim(internalbccnewIds), ",").size();

            //判断是否触发收件人数规则 -1代表无限制
            EmailCommonUtils ecm = new EmailCommonUtils();
            int sendcount = ecm.isNeedSendcountRule(user.getUID() + "");
            if (sendcount != -1 && count > sendcount) {
                writeLog("发件人数:" + count + "过多,触发发信限制,最大允许人数:" + sendcount);
                return "false3";
            }

            int realHrmAccountid = getRealHrmAccountId(isInternal, user.getUID(), _hrmAccountid);
            sendfrom = String.valueOf(realHrmAccountid);
        } else {
            // 外部邮件
            TO = this.getRealMailStr(Util.null2String(fu.getParameter("to"))); // TO
            CC = this.getRealMailStr(Util.null2String(fu.getParameter("cc"))); // CC
            BCC = this.getRealMailStr(Util.null2String(fu.getParameter("bcc"))); // BCC

            //取外部邮件发件人
            MailAccountComInfo info = new MailAccountComInfo();
            sendfrom = info.getAccountMailAddress(String.valueOf(_mailAccountId));
            String _mailAccountIdUserid = info.getUserId(String.valueOf(_mailAccountId));

            //外部邮件防止串号的问题
            if (_mailAccountId != -1 && !String.valueOf(user.getUID()).equals(_mailAccountIdUserid)) {
                writeLog("外部邮件防止串号,发送账号sendfrom:" + sendfrom + ";账号所属人_mailAccountIdUserid:" + _mailAccountIdUserid + ";当前发送人:" + user.getUID());
                this.errorMessInfo = new MailErrorMessageInfo();
                this.errorMessInfo.setErrorHint("" + SystemEnv.getHtmlLabelName(10003485, weaver.general.ThreadVarLanguage.getLang()) + ""); //"+weaver.systeminfo.SystemEnv.getHtmlLabelName(10003485,weaver.general.ThreadVarLanguage.getLang())+"
                this.errorMessInfo.setSolution("" + SystemEnv.getHtmlLabelName(10003486, weaver.general.ThreadVarLanguage.getLang()) + ""); //外部邮件串号，请退出其他账号后重新发信
                this.errorMessInfo.setErrorString("" + SystemEnv.getHtmlLabelName(10003487, weaver.general.ThreadVarLanguage.getLang()) + "");//"+weaver.systeminfo.SystemEnv.getHtmlLabelName(10003487,weaver.general.ThreadVarLanguage.getLang())+"
                return "false4";
            }

        }
        // 排查邮件地址是否含有分号结尾的情况,转变;为,
        //主要是手机版本的邮件传过来的数据含有分号
        TO = MailCommonUtils.trim(TO.replace(";", ","));
        CC = MailCommonUtils.trim(CC.replace(";", ","));
        BCC = MailCommonUtils.trim(BCC.replace(";", ","));

        subject = MailCommonUtils.filterSpecialCharFOfSubject(fu.getParameter("subject")); //主题

        texttype = fu.getParameter("texttype");  //文本类型，texttype=1为纯文本形式发送。否则为html模式
        boolean isHtmlMode = !"1".equals(texttype);

        content = fu.getParameter("mouldtext"); //邮件内容

        if (!"0".equals(texttype)) {
            content = content.replace("&#160;", " "); // google浏览器洗发送邮件超文本转为纯文本提交会包含&#160;
        }

        if (!isHtmlMode) { //纯文本是应该转为转移后内容
            content = MailCommonUtils.escapeHtmlClosure(content);
        }

        if (isHtmlMode) {
            //对邮件内容进行处理，若包含FileDownloadLocation,则使用文档的图片上传方法，并替换正文中的链接
            PatternMatcher matcher;
            PatternCompiler compiler;
            Pattern pattern;
            PatternMatcherInput input;
            MatchResult result;

            compiler = new Perl5Compiler();
            matcher = new Perl5Matcher();

            pattern = compiler.compile("<img.*?src=['\"\\s]?(/.*?weaver.email.FileDownloadLocation\\?fileid=(\\d*)).*?>",
                    Perl5Compiler.CASE_INSENSITIVE_MASK);
            input = new PatternMatcherInput(content);

            String imgFileSql = "";
            String fileId = "";

            while (matcher.contains(input, pattern)) {
                ImageFileManager ifm = new ImageFileManager();
                result = matcher.getMatch();
                fileId = result.group(2);
                imgFileSql = " select filename,filetype,filerealpath from MailResourceFile where id=?";
                RecordSet rst = new RecordSet();
                rst.executeQuery(imgFileSql, fileId);
                if (rst.next()) {
                    ifm.setComefrom("email");
                    ifm.setImageFileType(rst.getString("filetype"));
                    ifm.setImagFileName(rst.getString("filename"));
                    ifm.setData(toByteArray(rst.getString("filerealpath")));
                    int image_fileid = ifm.saveImageFile();
                    if (image_fileid != 0) {
                        content = content.replace(".email.FileDownloadLocation?fileid=" + fileId, ".file.FileDownload?fileid=" + image_fileid);
                    }
                }
            }
        }

        //===========================================================
        MailRule mRule = new MailRule();
        boolean hasSendRule = mRule.getSendRule(isInternal, _mailAccountId, user.getUID());
        String sendtype = "0";
        if (savesend.equals("1") || hasSendRule) {
            sendtype = "1";
        }
        if (savedraft.equals("1")) {
            sendtype = "2";
        }
        if (!savedraft.equals("1") && !savesend.equals("1")) {
            savesend = "1";
            sf = 1;
        }

        //生成邮件部分
        String senddate = MailCommonUtils.getTodaySendDate();
        int mailid = Util.getIntValue(fu.getParameter("mailid"), -1);
        String oldmailid = Util.null2s(Util.null2String(fu.getParameter("oldmailid")), "");//回复，回复全部等带入的被回复邮件ID
        boolean isnewmail = false;
        char separator = Util.getSeparator();
        String uuid = MailCommonUtils.getRandomUUID();  //新增加的行的唯一标志，为randomUUID值。通过此值查找刚插入的记录以获得id主键值
        if (mailid == -1) {
            isnewmail = true;
            //生成新的邮件
            String userid = "" + user.getUID();
            if (isInternal == 1) {
                userid = sendfrom; // 内部邮件邮件所属人等于发件人
            }
            String params = uuid + separator + userid + separator + priority + separator + sendfrom + separator + CC
                    + separator + BCC + separator + TO + separator + senddate
                    + separator + "0" + separator + subject + separator + "" + separator + texttype + separator + ""
                    + separator + _mailAccountId + separator + _status + separator + _folderId;
            mailid = MailCommonUtils.mailResourceInsert(params, content);
            if (mailid < 0) {
                writeLog("mailid<0,主记录插入失败，发送即失败-uuid-" + uuid);
                return "false"; //主记录插入失败，发送即失败
            }
        } else if (mailid != -1) {
            //表示修改邮件的内容
            String userid = "" + user.getUID();

            //判断mailid邮件归属人
            String resourceids = MailManagerService.getAllResourceids(String.valueOf(userid));
            String sql = "SELECT id FROM MailResource WHERE (" + Util.getSubINClause(resourceids, "resourceid", "in") + ") AND id = ?";
            rs.executeQuery(sql, mailid);
            if (rs.getCounts() != 1) {
                writeLog("邮件所属人串号，当前人员：" + resourceids + "，当前邮件id：" + mailid);
                return "false4";
            }

            if (isInternal == 1) {
                userid = sendfrom; // 内部邮件邮件所属人等于发件人
            }
            String params = mailid + "" + separator + "" + userid + separator + priority + separator + sendfrom
                    + separator + CC + separator + BCC + separator + TO
                    + separator + senddate + separator + "0" + separator + subject + separator + "" + separator
                    + texttype + separator + "" + separator + _mailAccountId + separator + _status + separator
                    + _folderId;
            MailCommonUtils.mailResourceUpdate(mailid, params, content);
        }

        //定时发送，若email_timingsend没有记录生成一条记录
        if (opIsTimingSend == 1) {
            rs.executeQuery("select mailid from email_timingsend where mailid = ?", mailid);
            if (!rs.next()) {
                rs.executeUpdate("insert into email_timingsend (mailid) values (?)", mailid);
            }
        }

        if (isInternal != 1) { // 外部邮件，更新收件人，抄送人，密送人字段
            MailCommonUtils.updateMailTo(mailid, TO, CC, BCC);
        }

        if (isInternal == 1) { //内部邮件，更新收件人明细；更新邮件密级
            String secretDeadline = Util.null2String(fu.getParameter("secretDeadline")); //保密期限
            if (!"1".equals(classification) && !"2".equals(classification)) {
                //公开和内部无密级期限，置空，防止乱传数据
                secretDeadline = "";
            }
            String mit_uuid = MailCommonUtils.getRandomUUID(); //新增加的行的唯一标志，为randomUUID值。通过此值查找刚插入的记录以获得id主键值
            if ("".equals(classification)) {
                //内部邮件，哪怕没开密级功能也需要给上默认密级(防止后续开了密级，但是开启密级动作不在我们这，导致密级数据未刷,导致开启密级下列表查询逻辑不对)
                //或者可在列表查询逻辑中兼容此种数据，但为了保证数据一致性，采用此种方案。
                classification = "4";
            }
            EncryptUtil encryptUtil = new EncryptUtil();
            Map crcMap = encryptUtil.getLevelCRC(mailid + "", classification);
            String encKey = Util.null2String(crcMap.get("encKey"));
            String crc = Util.null2String(crcMap.get("crc"));
            rs.executeUpdate("update MailResource set isInternal=?, mit_uuid=?,classification=?,encKey=?,crc=?,secretDeadline =? where id=?", isInternal, mit_uuid, classification, encKey, crc, secretDeadline, mailid);
            EmailCommonUtils.insertMailInternalTo(mit_uuid, internaltonewIds, internalccnewIds, internalbccnewIds, sendtoNoResourceNewMap, user);
        } else {
            //外部邮件，更新实时的邮件标志
            rs.executeUpdate("update MailResource set isInternal=? where id=?", isInternal, mailid);
        }

        fu.setMailid(mailid);
        this.mailid = String.valueOf(mailid);

        //设置邮件定时发送时间,定时发送状态timingdatestate【0为未发送，1为发送成功，-1为发送失败,-2为不需要发送】【是否需要回执】
        int timingdatestate = "submit".equals(timingsubmitType) ? 0 : -2;
        RecordSet rstime = new RecordSet();
        rstime.executeQuery("select timingdatestate from email_timingsend where mailid = ?", mailid);
        if (rstime.next()) {
            int timestate = rstime.getInt("timingdatestate");
            if (timestate == 0) {
                timingdatestate = 0; // 如果已经是定时发送 自动保存草稿时不在更新此状态
            } else if (timestate == 1) {
                timingdatestate = 1;
            }
        }
        //更新定时发送信息，是否分别发送
        rs.executeUpdate("update MailResource set needReceipt = ?, isSendApart = ? where id = ?", needReceipt, this.isSendApart ? "1" : "0", mailid);

        rs.executeUpdate("update email_timingsend set timingdate = ?, timingdatestate = ? where mailid = ?",
                this.timingdate, timingdatestate, mailid);

        int canview = ("1".equals(savesend) || "1".equals(savedraft) || hasSendRule) ? 1 : 0;
        // alter table MailResource add canview integer canview【字段是否可被检索，1-可以，0不可以】
        rs.executeUpdate("update MailResource set canview = ? where id = ?", canview, mailid);

        String accids = fu.getParameter("accids"); //现有的附件id
        String delaccids = fu.getParameter("delaccids"); //页面操作时，删除的附件id

        //获取附件流
        filecontents = this.getFileContent(accids, mailid + "", delaccids, MailManagerService.getAllResourceids(String.valueOf(user.getUID())));
        rs.executeUpdate("UPDATE MailResource SET attachmentNumber=? WHERE id=?", filecontents.size(), mailid);
        filenames = this.getFileName(accids, String.valueOf(mailid));
        filetypes = this.getFileType(accids, String.valueOf(mailid));
        String sendstatus = "false";

        // 计算此封邮件大小
        rs.executeQuery("SELECT sum(filesize) FROM MailResourceFile WHERE mailid = ?", mailid);
        rs.next();
        int size = rs.getInt(1);
        float totalsize = content.getBytes().length + subject.getBytes().length + size;
        rs.executeUpdate("UPDATE MailResource SET size_n = ? WHERE id = ?", totalsize, mailid);

        //处理昵称明细
        if (isInternal != 1) {
            this.saveMailContactInfo(mailid, _mailAccountId + "", user.getUID() + "", Util.null2String(fu.getParameter("usernameTo")), Util.null2String(fu.getParameter("usernameCc")), Util.null2String(fu.getParameter("usernameBcc")));
        }

        if ("1".equals(savedraft)) {
            RecordSet tmpRs = new RecordSet();
            tmpRs.executeUpdate("update mailresource set folderid = -2 where id = ?", mailid);
            //sessionUUidList.remove(sessionUUid);

            //删除邮件发送失败记录的失败信息
            rs.executeUpdate("DELETE FROM email_sendErrorInfo WHERE mailid = ?", mailid);
            MailCommonUtils.SYS_MAIL_ALERT_POOL.execute(new MailSpaceUpdateThread(mailid + ""));
            return "draftSaved";  //只保存(存草稿)
        }

        if (hasSendRule) {
            if (!"1".equals(savesend)) {
                rs.executeUpdate("UPDATE MailResource SET isTemp='1' WHERE id=?", mailid);
            }
        }

        int bodyfilesize = filenames.size();
        List<EmailSendFileOldEntity> fileList = new ArrayList<EmailSendFileOldEntity>();  //防止顺序有问题
        if (isInternal != 1) {  // 外部邮件需要获取附件流，内部邮件不需要
            for (int i = 0; i < bodyfilesize; i++) {
                InputStream is = (InputStream) filecontents.get(i);
                // 附件已不存在
                if (is == null) {
                    this.errorMessInfo = new MailErrorMessageInfo();
                    this.errorMessInfo.setErrorHint(SystemEnv.getHtmlLabelName(520182, this.languageid)); //该邮件附件实体文件已在服务器上丢失，无法发信
                    this.errorMessInfo.setSolution(SystemEnv.getHtmlLabelName(125347, this.languageid)); //无法在服务器上找到附件，或附件已丢失
                    this.errorMessInfo.setErrorString(SystemEnv.getHtmlLabelName(125348, this.languageid));//找不到附件
                    EmailSendSessionUUidBiz.removeSendSessionUUid(user, sessionUUid);
                    //更新到草稿箱中
                    boolean updatestatus = rs.executeUpdate("update mailresource set folderid = -2 where id = ?", mailid);
                    if (updatestatus) {
                        //记录发信错误异常信息
                        EmailCommonUtils.recordEmailSendErrorinfo(mailid, this.errorMessInfo);
                    }

                    return "false1";
                }
                String filename = (String) filenames.get(i);
                String filetype = filetypes.get(i) == null ? "" : (String) filetypes.get(i);
                String ctype = Util.null2s(filetype, EmailContentTypeUtils.getContentTypeByFileName(filename));
//                String ctype = FileTypeMap.getDefaultFileTypeMap().getContentType(filename);
//                // javax.mail需要最高！1.5.2！,1.5.3有些附件名称会导致市面其他邮箱解析不出来
//                filename = "=?UTF-8?B?" + MailCommonUtils.encodeBase64String(filename.getBytes("UTF-8")) + "?=";
//                filename = filename.replace("\n", ""); // 当文件名过长时
//                StringBuffer sb = new StringBuffer(filename.length()); // 需要过滤换行和无用符号
//                for (int j = 0; j < filename.length(); j++) {
//                    if (filename.getBytes()[j] == 13) {
//                        continue;
//                    }
//                    sb.append(filename.charAt(j));
//                }
                DataHandler da = new DataHandler(new javax.mail.util.ByteArrayDataSource(is, ctype));
                EmailSendFileOldEntity entity = new EmailSendFileOldEntity();
                entity.setFileName(filename);
                entity.setDataHandler(da);
                fileList.add(entity);
            }
        }
        if (isInternal != 1) {
            subject = MailCommonUtils.toHtmlMode(subject); //外发前，转为html形式。
            // 发送外部邮件
            // texttype=1:前台选择了纯文本发送,=0或为空，为html编辑器发送
            if (this.isSendApart) { // 分别发送，初始化发送状态
                MailSendApartLogService msals = new MailSendApartLogService();
                msals.addMailSendApartLog(mailid, _accountId, TO);
            }

            EmailSend emailSend = new EmailSend();
            // 测试新发送方法  发送
            //sendstatus = emailSend.sendByMailAccount(_mailAccountId, mailid) + ""; //一种发送方法，在方法内，重新读取数据库组装

            //另一种，这种方式，上面的附件不存在时错误判断，就可以沿用。
            EmailSenderEntity sender = EmailSend.getEmailSenderByAccountId(_mailAccountId);
            EmailSendEntity entity = emailSend.packParameterToEntity(sendfrom, TO, CC, BCC, subject, 3, content, null, null, null,
                    mailid, fileList, isSendApart, priority, needReceipt, isHtmlMode);
            entity.setSenddate(senddate);

            //兼容新方法的额外设置
            entity.getFrom().setNickname(sender.getNickName()); //发件人昵称
            entity.setMailAccountId(_mailAccountId);
            entity.setTrackingInfo(mailid + "#" + _mailAccountId + "#" + request.getRemoteAddr()); //跟踪信息

            sendstatus = emailSend.sendMailNoEx(sender, entity) + "";

            if ("false".equals(sendstatus)) {
                setErrorMessInfo(emailSend.getErrorMess());
            }

            /*
            if (!texttype.equals("1")) {
                if (filenames.isEmpty()) {
                    sendstatus = String.valueOf(sm.sendhtml(sendfrom, TO, CC, BCC, subject, content, 3, priority, null, mailid));
                } else {
                    sendstatus = String.valueOf(sm.sendMiltipartHtml(sendfrom, TO, CC, BCC, subject, content, 3, filenames, filecontents, priority, null, mailid, map));
                }
            } else {
                if (filenames.isEmpty()) {
                    sm.setBindMailId(mailid); //设置邮件id
                    sendstatus = String.valueOf(sm.send(sendfrom, TO, CC, BCC, subject, content, priority));
                } else {
                    sendstatus = String.valueOf(sm.sendMiltipartText(sendfrom, TO, CC, BCC, subject, content, filenames, filecontents, priority, map));
                }
            }
            */
        } else {

            //暂时放在此处，防止密级过滤人员发送失败后未保存到草稿箱
            //如果没收件人，但是有抄送密送，禁止发信
            if (!"1".equals(savedraft) && Util.TokenizerString(MailCommonUtils.trim(internaltonewIds), ",").size() <= 0) {
                writeLog("SendMail sendMail internaltonewIds - 无有效收件人员");
                this.errorMessInfo = new MailErrorMessageInfo();
                this.errorMessInfo.setErrorHint("" + SystemEnv.getHtmlLabelName(10003488, weaver.general.ThreadVarLanguage.getLang()) + ""); //所选人员均不符合密级要求，无有效发件人员
                this.errorMessInfo.setSolution("" + SystemEnv.getHtmlLabelName(10003488, weaver.general.ThreadVarLanguage.getLang()) + ""); //所选人员均不符合密级要求，无有效发件人员
                this.errorMessInfo.setErrorString("" + SystemEnv.getHtmlLabelName(10003489, weaver.general.ThreadVarLanguage.getLang()) + "");//无有效发件人员

                //更新到草稿箱中
                boolean updatestatus = rs.executeUpdate("update mailresource set folderid = -2 where id = ?", mailid);
                if (updatestatus) {
                    //记录发信错误异常信息
                    EmailCommonUtils.recordEmailSendErrorinfo(mailid, this.errorMessInfo);
                }

                return "false7";
            }

            // 发送内部邮件
            sendstatus = sendInternalMailNew(mailid + "", internaltonewIds, internalccnewIds, internalbccnewIds, all);

            if ("true".equals(sendstatus)) {
                sendInternalMailFile(mailid + "");
            }
            if ("true".equals(sendstatus)) {
                setAutoRemindMail(mailid + "");
            }
        }

        if ("true".equals(sendstatus)) {
            String flag = fu.getParameter("flag");//-1：新建邮件;1：回复;2：回复全部;3：转发;4：草稿;5：再次编辑;6：回复带附件;7：回复全部带附件;
            int tagetmailid = Util.getIntValue(fu.getParameter("oldmailid"), -1);
            updateMailFlag(tagetmailid + "", flag);

            //删除邮件发送失败记录的失败信息
            rs.executeUpdate("DELETE FROM email_sendErrorInfo WHERE mailid = ?", mailid);
        }

        if ("false".equals(sendstatus) && sf == 1) {
            RecordSet rr = new RecordSet();
            rr.executeUpdate("UPDATE MailResource SET folderID=-2 WHERE id=?", mailid);
            MailCommonUtils.updateMailContent(mailid, content);
        }


        if ("true".equals(sendstatus) && sf == 0) {
            // 邮件发送成功
            RecordSet rr02 = new RecordSet();
            if (rr02.execute("select folderId from mailresource where id=" + mailid) && rr02.next()) {
                // 判断该邮件的状态为草稿
                if ("-2".equals(rr02.getString("folderId")) && ("-2".equals(_folderId))) {//此处避免邮件规则中的移动到草稿箱冲突
                    // 修改草稿为已发送的邮件
                    String up_sql = "update mailresource set folderId='-1' where id=" + mailid;
                    rr02.execute(up_sql);
                }
            }
        } else if ("false".equals(sendstatus)) {
            // 如果邮件发送失败，判断它是不是勾选“保存到已发送”
            RecordSet rr02 = new RecordSet();
            // 修改已发送的邮件为草稿
            String up_sql = "update  mailresource set folderId='-2' where id=" + this.mailid;
            rr02.execute(up_sql);

        }
        if ("true".equals(sendstatus) && "".equals(_tempsavesend)) {
            //发送成功并且没有勾选保存到已发送
            MailResourceService mrs = new MailResourceService();
            //mrs.deleteMail("" + mailid, user.getUID(), "");
            mrs.deleteMail(EmailDeleteType.Personal_Send_NotSave.toString(), String.valueOf(user.getUID()), fu.getRemoteAddr(),
                    "" + mailid, user.getUID(), "", "", "");
        }

        if ("false".equals(sendstatus) && sf == 0) {
            sendstatus = "false1";// 发送失败，保存到发件箱
        }

        // 关闭附件流信息
        for (int i = 0; i < bodyfilesize; i++) {
            InputStream is = (InputStream) filecontents.get(i);
            try {
                is.close();
            } catch (Exception ec) {
                writeLog(ec);
            }
        }

        // 创建一个线程 在线程中去更新邮件空间
        MailCommonUtils.SYS_MAIL_ALERT_POOL.execute(new MailSpaceUpdateThread(mailid + ""));

        //执行邮件发信规则
        //这块移到最后去执行，不然规则中间执行和发信有冲突（发信数据还没完全落实）
        if (hasSendRule && "true".equals(sendstatus)) {
            mRule.apply(isInternal, String.valueOf(mailid), user, _mailAccountId, request, "1");
        }

        //执行内部邮件接收规则
        if (isInternal == 1 && "true".equals(sendstatus)) {
            this.executeInternalReciveRule(mailid);
        }
        EmailSendSessionUUidBiz.removeSendSessionUUid(user, sessionUUid);
        return sendstatus;
    }

    /**
     * 写信后，保存联系人明细。（先删除这个maildi对应的明细，再保存前台传过来的值。）
     *
     * @param mailId
     * @param usernameTo
     */
    private void saveMailContactInfo(int mailId, String mailAccountId, String userid, String... usernameTo) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        //发件人
        MailAccountComInfo comInfo = new MailAccountComInfo();
        map.put(comInfo.getAccountMailAddress(mailAccountId), comInfo.getAccountName(mailAccountId));

        //收件人
        for (String info : usernameTo) {
            map.putAll(this.dismantlingContactInfo(info));
        }
        if (!map.isEmpty()) {
            //删除无用数据
            map.remove(null);
            map.remove("");

            RecordSetTrans rst = new RecordSetTrans();

            try {
                rst.setAutoCommit(false);
                rst.executeUpdate("delete from mailcontactinfo where mailid = ?", mailId);

                List<List<Object>> params = new ArrayList<List<Object>>();
                for (Entry<String, String> entry : map.entrySet()) {
                    String address = entry.getKey();
                    String name = entry.getValue();

                    List<Object> list = new ArrayList<Object>();
                    list.add(mailId);
                    list.add(address);
                    list.add(name);
                    list.add(userid);

                    params.add(list);
                }
                rst.executeBatchSql("insert into mailcontactinfo(mailid, mailaddress, mailusername,userid) values (?, ?, ?, ?)", params);

                rst.commit();
            } catch (Exception e) {
                rst.rollback();
                writeLog(e);
            }
        }
    }

    /**
     * 拆解 前台数据。前台格式如：[ {mailusername:"", mailaddress:""}, {mailusername:"", mailaddress:""} ]
     *
     * @param contactInfo
     * @return
     */
    private LinkedHashMap<String, String> dismantlingContactInfo(String contactInfo) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        if (!Util.null2String(contactInfo).isEmpty()) {
            try {
                com.alibaba.fastjson.JSONArray array = com.alibaba.fastjson.JSONArray.parseArray(contactInfo);
                for (int i = 0, n = array.size(); i < n; i++) {
                    com.alibaba.fastjson.JSONObject object = array.getJSONObject(i);
                    String mailusername = object.getString("mailusername");
                    String mailaddress = object.getString("mailaddress");
                    if (Util.isEmail(mailaddress)) {
                        map.put(mailaddress, mailusername);
                    }
                }
            } catch (Exception e) {
                writeLog("昵称解析失败，contactInfo=" + contactInfo);
                writeLog(e);
            }
        }
        return map;
    }


    /**
     * 执行内部邮件接收完成规则
     *
     * @param mailId
     */
    private void executeInternalReciveRule(int mailId) {
        try {

            MailRule mailRule = new MailRule();
            RecordSet rs = new RecordSet();
            HrmResourceService hrs = new HrmResourceService();

            //此次接收人中哪些人有邮件接收规则
            String sql = "select a.id as ruleId, b.id as mailId, b.resourceid from mailrule a, mailresource b " +
                    "where a.userid = b.resourceid and a.mailType = 1 and a.isActive='1' and a.applyTime='0'and b.originalmailid = ?";
            rs.executeQuery(sql, mailId);
            while (rs.next()) {
                String receivedMailIds = rs.getString("mailId");
                User user = hrs.getUserById(rs.getInt("resourceid"));
                int mailAccountId = -1;
                mailRule.apply(1, receivedMailIds, user, mailAccountId, null, "0");
            }
        } catch (Exception e) {
            writeLog("内部邮件接收规则执行错误");
            writeLog(e);
        }
    }

    /**
     * 获得附件流对象
     *
     * @param fieldids  附件id，mailresourcefile表主键
     * @param mailid    邮件主键
     * @param delaccids 要删除的主键IDs
     * @return
     */
    private ArrayList getFileContent(String fieldids, String mailid, String delaccids, String userIds) {
        ArrayList list = new ArrayList();
        if ("-1".equals(mailid)) {
            writeLog("mailid为-1,过滤非法邮件附件-fieldids-" + fieldids + "-delaccids-" + delaccids);
            return list;
        }

        RecordSet rs = new RecordSet();
        // 删除页面删去的附件记录（delaccids这个参数E9已经不再传给后台。fieldids即为当前页面上能看到的所有附件id）
//        if (!delaccids.equals("")) {
//            rs.executeUpdate("delete from MailResourceFile where id not in (" + fieldids + ") and mailid= ?",  mailid);
//        }

        //删除页面上不再传到后台的id。（用户可能删除掉了）
        if (!"".equals(MailCommonUtils.trim(fieldids))) {
            rs.executeUpdate("delete from MailResourceFile where id not in (" + MailCommonUtils.trim(fieldids) + ") and mailid= ?", mailid);
        } else {
            rs.executeUpdate("delete from MailResourceFile where mailid= ?", mailid);
            return list;
        }

        // 重置mailid的邮件附件记录（要保持前台传来的顺序，只执行多个sql）
        String[] ids = Util.TokenizerString2(MailCommonUtils.trim(fieldids), ",");
        RecordSet rsUpdate = new RecordSet();
        Map<String, String> mailIdAndResourceIdMap = new HashMap<String, String>();
        for (int i = 0; i < ids.length; i++) {
            String id = ids[i].trim();
            rs.executeQuery("select mailid from MailResourceFile where id = ?", id);
            if (rs.next()) {
                String oldmailid = Util.null2String(rs.getString("mailid"));
                if ("-1".equals(oldmailid)) {
                    writeLog("oldmailid为-1,过滤非法邮件附件-id-" + id);
                    continue;
                }
                if (oldmailid.isEmpty() || "0".equals(oldmailid)) {
                    rsUpdate.executeUpdate("update MailResourceFile set mailid = ? where id =?", mailid, id);
                } else {
                    if (!mailid.equals(oldmailid)) {
                        boolean needCopy = false; //是否需要复制 附件记录
                        String resourceid = Util.null2String(mailIdAndResourceIdMap.get(oldmailid));
                        if ("".equals(resourceid)) {
                            rs.executeQuery("select resourceid from mailresource where id = ?", oldmailid);
                            if (rs.next()) {
                                resourceid = rs.getString("resourceid");
                                if (("," + userIds + ",").contains("," + resourceid + ",")) { //有权限操作的附件
                                    mailIdAndResourceIdMap.put(oldmailid, resourceid);
                                    needCopy = true;
                                } else {
                                    writeLog("过滤非法附件，存在无权限操作的附件，附件id=" + id);
                                }
                            } else {
                                writeLog("过滤非法附件，存在附件所属原邮件不存在情况，oldmailid=" + oldmailid + ",附件id=" + id);
                            }
                        } else {
                            needCopy = true;
                        }
                        if (needCopy) {
                            String sql = "INSERT INTO MailResourceFile(mailid,filename,filetype,filerealpath,iszip,isencrypt,isfileattrachment,fileContentId,isEncoded,filesize,isaesencrypt,aescode,mrf_uuid,htmlcode,pdfcode,storageStatus,tokenKey,secretLevel,secretDeadline) " +
                                    " select " + mailid + ",filename,filetype,filerealpath,iszip,isencrypt,isfileattrachment,fileContentId,isEncoded,filesize,isaesencrypt,aescode,mrf_uuid,htmlcode,pdfcode,storageStatus,tokenKey,secretLevel,secretDeadline " +
                                    " from MailResourceFile where id=" + id + " and mailid='" + oldmailid + "'";
                            rsUpdate.execute(sql);
                        }
                    }
                }
            } else {
                writeLog("过滤非法附件，存在附件不存在情况，mailid=" + mailid + ",附件id=" + id);
            }
        }

        // 获取正确的附件流信息
        List<String> finalFileIdsList = new ArrayList<String>();
        rs.executeQuery("select id,isaesencrypt,aescode,filerealpath,iszip,storageStatus,tokenKey,signinfo,hashinfo from MailResourceFile where mailid=? order by id", mailid);
        while (rs.next()) {
            finalFileIdsList.add(rs.getString("id"));
            InputStream source = null;
            int mailFileId = rs.getInt("id");
            String isaesencrypt = rs.getString("isaesencrypt");
            String aescode = rs.getString("aescode");
            String storageStatus = rs.getString("storageStatus");
            String tokenKey = rs.getString("tokenKey");
            String signInfo = Util.null2String(rs.getString("signinfo"));
            String hashInfo = Util.null2String(rs.getString("hashinfo"));
            try {
                if (MailAliOSSService.isAliOSSEnable() && "1".equals(storageStatus) && !"".equals(tokenKey)) { //如果是阿里oss存储
                    source = MailAliOSSService.downloadFile(tokenKey);
                } else {
                    File thefile = new File(rs.getString("filerealpath"));
                    rs.writeLog("MailSend 开始预览 ");
                    if (signInfo.isEmpty() && hashInfo.isEmpty() && DocEncryptUtil.allowEncrypt(thefile.getName())) {
                        String sqlDoc = "select signinfo,hashinfo from MailResourceFile where filerealpath = ? and signinfo is not null ";
                        RecordSet recordSet = new RecordSet();
                        recordSet.executeQuery(sqlDoc, rs.getString("filerealpath"));
                        if (recordSet.next()) {
                            writeLog("查到了signinfo");
                            signInfo = recordSet.getString(1);
                            hashInfo = recordSet.getString(2);
                        }
                    }
                    if (rs.getString("iszip").equals("1")) {
                        ZipInputStream zin = new ZipInputStream(new FileInputStream(thefile));
                        if (zin.getNextEntry() != null) {
                            source = new BufferedInputStream(zin);
                        }
                    } else {
                        source = new BufferedInputStream(new FileInputStream(thefile));
                    }
                    if (isaesencrypt.equals("1")) {
                        source = AESCoder.decryptForEamil(source, mailFileId);
                    }
                    if (!signInfo.isEmpty() && !hashInfo.isEmpty()) {
                        // 开始校验签名值
                        boolean signResult = false;
                        signResult = DocEncryptUtil.verifyFile(hashInfo, signInfo, rs.getString("filerealpath"));
                        if (!signResult) {
                            writeLog("MailSend邮件完整性校验失败 失败路径：" + rs.getString("filerealpath"));
                        } else {
                            source = DocEncryptUtil.decryptInput(source);
                        }
                    }

                }
            } catch (Exception e) {
                writeLog("getFileContent 获取附件留信息错误!");
                writeLog(e);
            }
            list.add(source);
        }
        if (!finalFileIdsList.isEmpty()) {
            this.finalFileIds = StringUtils.join(finalFileIdsList, ",");
        }

        return list;
    }

    /**
     * 获得附件名称
     *
     * @param fieldids mailresourcefile表主键
     * @param mailid   邮件主键
     * @return
     */
    private ArrayList getFileName(String fieldids, String mailid) {
        ArrayList list = new ArrayList();
        RecordSet rs = new RecordSet();
        rs.executeQuery("select filename from MailResourceFile where mailid = ? order by id", mailid);
        while (rs.next()) {
            list.add(rs.getString("filename"));
        }
        return list;
    }

    private ArrayList getFileType(String fieldids, String mailid) {
        ArrayList list = new ArrayList();
        RecordSet rs = new RecordSet();
        rs.executeQuery("select filetype from MailResourceFile where mailid = ? order by id", mailid);
        while (rs.next()) {
            list.add(rs.getString("filetype"));
        }
        return list;
    }

    private String getRealMailStr(String strMails) {
        String retrunStr = "";
        if (!"".equals(strMails)) {
            ArrayList mailStrList = Util.TokenizerString(strMails, ",");
            for (int i = 0; i < mailStrList.size(); i++) {
                String aMail = (String) mailStrList.get(i);
                int pos1 = aMail.indexOf("<");
                int pos2 = aMail.lastIndexOf(">");
                if (pos1 != -1 && pos2 != -1) {
                    retrunStr += "," + aMail.substring(pos1 + 1, pos2);
                } else {
                    if (aMail.indexOf("@") != -1) {
                        retrunStr += "," + aMail;
                    }
                }
            }
        }

        if (!"".equals(retrunStr)) {
            retrunStr = retrunStr.substring(1);
        }
        return retrunStr;
    }


    /**
     * 发送内部邮件系统提醒
     *
     * @param fromid  发送人
     * @param subject 邮件标题
     * @param content 邮件内容
     * @param toids   接收人 1,2,3,4
     * @param ccids   抄送人 1,2,3,4
     * @return
     */
    public boolean sendSysInternalMail(String fromid, String toids, String ccids, String subject, String content) {
        // 生成新的邮件
        RecordSet rt = new RecordSet();
        int userid = Util.getIntValue(fromid); // 发送人
        String sendfrom = fromid; // 发送人
        String senddate = TimeUtil.getCurrentTimeString();
        int _mailAccountId = 0;
        int _status = 0;
        int _folderId = -1;
        int mailid = -1;
        String priority = "0";
        String texttype = "0";
        int toall = 0;
        int ccall = 0;
        int bccall = 0;
        int isInternal = 1;
        char separator = Util.getSeparator();

        String classification = "";
        HrmClassifiedProtectionBiz hrmcp = new HrmClassifiedProtectionBiz();
        boolean isOpenClassification = hrmcp.isOpenClassification(); //是否开启密级
        if (!isOpenClassification) {
            //密级没开的时候置为空
            classification = "4";
        }
        //开启密级的时候，强校验密级权限，防止密级越权
        if (isOpenClassification) {
            //取当事人所能获取到的最高的资源密级(兼容了主次账号)
            String maxResourceClassification = hrmcp.getMaxResourceSecLevelById(fromid);
            classification = maxResourceClassification;
        }

        if (!toids.isEmpty() || !ccids.isEmpty()) {
            //密级过滤后的完整人员
            toids = EmailCommonUtils.getResourceByClassification(toids, classification);
            ccids = EmailCommonUtils.getResourceByClassification(ccids, classification);
        }

        try {
            String uuid = MailCommonUtils.getRandomUUID();  //新增加的行的唯一标志，为randomUUID值。通过此值查找刚插入的记录以获得id主键值
            String params = uuid + separator + "" + userid + separator + priority + separator +
                    sendfrom + separator + CC + separator + BCC + separator +
                    TO + separator + senddate + separator + "0" + separator +
                    subject + separator + "" + separator + texttype + separator + "" +
                    separator + _mailAccountId + separator + _status + separator + _folderId;
            mailid = MailCommonUtils.mailResourceInsert(params, content);
            // 处理收件人，抄送人，密送人问题
            MailCommonUtils.updateMailTo(mailid, TO, CC, BCC);

            //数据库操作异常，发送失败
            if (mailid == -1) {
                writeLog("sendSysInternalMail-数据库操作异常，发送失败-userid-" + userid + "-subject-" + subject + "-uuid-" + uuid);
                return false;
            }

            //根据密级获取默认保密期限
            String secretDeadline = hrmcp.getResourceClassificationValidity(classification, new User(userid));

            User user = new User(userid);
            String mit_uuid = MailCommonUtils.getRandomUUID();  //新增加的行的唯一标志，为randomUUID值。通过此值查找刚插入的记录以获得id主键值

            //密级加密
            EncryptUtil encryptUtil = new EncryptUtil();
            Map crcMap = encryptUtil.getLevelCRC(mailid + "", classification);
            String encKey = Util.null2String(crcMap.get("encKey"));
            String crc = Util.null2String(crcMap.get("crc"));

            rt.executeUpdate("update MailResource set folderid=-1,canview=1,isInternal=?,mit_uuid=?,classification=?,encKey=?,crc=?,secretDeadline=? where id= ?", isInternal, mit_uuid, classification, encKey, crc, secretDeadline, mailid);
            EmailCommonUtils.insertMailInternalTo(mit_uuid, toids, ccids, "", user);
            String sendstatus = sendInternalMailNew(mailid + "", toids, ccids, "", toall + ccall + bccall);

            if ("true".equals(sendstatus)) {
                this.setAutoRemindMail(mailid + "");
            }
            return sendstatus.equals("true");
        } catch (Exception e) {
            writeLog("sendSysInternalMail 发生错误！fromid=" + fromid + ",subject=" + subject + ",content=" + content);
            writeLog(e);
            return false;
        }
    }

    /**
     * @param fromid  发送人
     * @param subject 邮件标题
     * @param content 邮件内容
     * @param toids   接收人 1,2,3,4
     * @param ccids   抄送人 1,2,3,4
     * @Author: lizhen
     * @Description: 发送内部邮件并返回邮件id
     * @Date: 2019/11/7 10:38
     * @param:
     * @param: content
     * @return:
     * @return: int
     */
    public int sendSysInternalMailAndReturnId(String fromid, String toids, String ccids, String subject, String content) {
        // 生成新的邮件
        RecordSet rt = new RecordSet();
        int userid = Util.getIntValue(fromid); // 发送人
        String sendfrom = fromid; // 发送人
        String senddate = TimeUtil.getCurrentTimeString();
        int _mailAccountId = 0;
        int _status = 0;
        int _folderId = -1;
        int mailid = -1;
        String priority = "0";
        String texttype = "0";
        int toall = 0;
        int ccall = 0;
        int bccall = 0;
        int isInternal = 1;
        char separator = Util.getSeparator();

        try {
            String uuid = MailCommonUtils.getRandomUUID();  //新增加的行的唯一标志，为randomUUID值。通过此值查找刚插入的记录以获得id主键值
            String params = uuid + separator + "" + userid + separator + priority + separator +
                    sendfrom + separator + CC + separator + BCC + separator +
                    TO + separator + senddate + separator + "0" + separator +
                    subject + separator + "" + separator + texttype + separator + "" +
                    separator + _mailAccountId + separator + _status + separator + _folderId;
            mailid = MailCommonUtils.mailResourceInsert(params, content);
            // 处理收件人，抄送人，密送人问题
            MailCommonUtils.updateMailTo(mailid, TO, CC, BCC);

            //数据库操作异常，发送失败
            if (mailid == -1) {
                return mailid;
            }
            User user = new User(userid);
            String mit_uuid = MailCommonUtils.getRandomUUID();  //新增加的行的唯一标志，为randomUUID值。通过此值查找刚插入的记录以获得id主键值
            rt.executeQuery("update MailResource set folderid=-1,canview=1,isInternal=?,mit_uuid=? where id= ?", isInternal, mit_uuid, mailid);
            EmailCommonUtils.insertMailInternalTo(mit_uuid, toids, ccids, "", user);
            String sendstatus = sendInternalMailNew(mailid + "", toids, ccids, "", toall + ccall + bccall);

            if ("true".equals(sendstatus)) {
                if ("oracle".equals(rt.getDBType())) {
                    this.updateInternalMailContent(mailid + "");
                }

                this.setAutoRemindMail(mailid + "");
            }
        } catch (Exception e) {
            mailid = -1;
            writeLog("sendSysInternalMail 发生错误！fromid=" + fromid + ",subject=" + subject + ",content=" + content);
            writeLog(e);
        }
        return mailid;
    }

    public boolean sendSysInternalMailNew(String fromid, String toids, String ccids, String bccids, String subject, String content, String[] paths) {
        // 生成新的邮件
        RecordSet rt = new RecordSet();
        int userid = Util.getIntValue(fromid); // 发送人
        String sendfrom = fromid; // 发送人
        String senddate = TimeUtil.getCurrentTimeString();
        int _mailAccountId = 0;
        int _status = 0;
        int _folderId = -1;
        int mailid = -1;
        String priority = "0";
        String texttype = "0";
        int toall = 0;
        int ccall = 0;
        int bccall = 0;
        int isInternal = 1;
        char separator = Util.getSeparator();

        String classification = "";
        HrmClassifiedProtectionBiz hrmcp = new HrmClassifiedProtectionBiz();
        boolean isOpenClassification = hrmcp.isOpenClassification(); //是否开启密级
        if (!isOpenClassification) {
            //密级没开的时候置为空
            classification = "4";
        }
        //开启密级的时候，强校验密级权限，防止密级越权
        if (isOpenClassification) {
            //取当事人所能获取到的最高的资源密级(兼容了主次账号)
            String maxResourceClassification = hrmcp.getMaxResourceSecLevelById(fromid);
            classification = maxResourceClassification;
        }

        if (!toids.isEmpty() || !ccids.isEmpty()) {
            //密级过滤后的完整人员
            toids = EmailCommonUtils.getResourceByClassification(toids, classification);
            ccids = EmailCommonUtils.getResourceByClassification(ccids, classification);
            bccids = EmailCommonUtils.getResourceByClassification(bccids, classification);
        }

        try {
            String uuid = MailCommonUtils.getRandomUUID();  //新增加的行的唯一标志，为randomUUID值。通过此值查找刚插入的记录以获得id主键值
            String params = uuid + separator + "" + userid + separator + priority + separator +
                    sendfrom + separator + CC + separator + BCC + separator +
                    TO + separator + senddate + separator + "0" + separator +
                    subject + separator + "" + separator + texttype + separator + "" +
                    separator + _mailAccountId + separator + _status + separator + _folderId;
            mailid = MailCommonUtils.mailResourceInsert(params, content);
            // 处理收件人，抄送人，密送人问题
            MailCommonUtils.updateMailTo(mailid, TO, CC, BCC);

            //数据库操作异常，发送失败
            if (mailid == -1) {
                writeLog("sendSysInternalMailNew-数据库操作异常，发送失败-userid-" + userid + "-subject-" + subject + "-uuid-"
                        + uuid);
                return false;
            }

            //根据密级获取默认保密期限
            String secretDeadline = hrmcp.getResourceClassificationValidity(classification, new User(userid));

            //密级加密
            EncryptUtil encryptUtil = new EncryptUtil();
            Map crcMap = encryptUtil.getLevelCRC(mailid + "", classification);
            String encKey = Util.null2String(crcMap.get("encKey"));
            String crc = Util.null2String(crcMap.get("crc"));

            User user = new User(userid);
            String mit_uuid = MailCommonUtils.getRandomUUID(); //新增加的行的唯一标志，为randomUUID值。通过此值查找刚插入的记录以获得id主键值
            rt.executeUpdate("update MailResource set folderid=-1,canview=1,isInternal=?,mit_uuid=?,classification=?,encKey=?,crc=?,secretDeadline=? where id= ?", isInternal, mit_uuid, classification, encKey, crc, secretDeadline, mailid);
            EmailCommonUtils.insertMailInternalTo(mit_uuid, toids, ccids, bccids, user);

            if (paths != null && paths.length > 0) {
                rt.executeUpdate("update MailResource set attachmentNumber=? where id=?", paths.length, mailid);
                saveMailFile(mailid + "", paths);
            }

            String sendstatus = sendInternalMailNew(mailid + "", toids, ccids, bccids, toall + ccall + bccall);

            if ("true".equals(sendstatus)) {
                if (paths != null && paths.length > 0) {
                    sendInternalMailFile(mailid + "");
                }
                this.setAutoRemindMail(mailid + "");
            }
            return sendstatus.equals("true");
        } catch (Exception e) {
            writeLog("sendSysInternalMail 发生错误！fromid=" + fromid + ",subject=" + subject + ",content=" + content);
            writeLog(e);
            return false;
        }
    }

    private void saveMailFile(String mailid, String[] paths) {
        if (Integer.parseInt(mailid) < 0) {
            writeLog("saveMailFile-mailid<0,非法插入");
            return;
        }
        String filename = "";
        String filerealPath = "";
        String fileType = "";
        int iszip = 0;
        int isencrypt = 0;
        int isfileattrachment = 1;
        String fileContentId = "";
        String isEncoded = "";
        int filesize = 100;
        int isaesencrypt = 0;
        String mrf_uuid = "";
        String sql = "";
        RecordSet rt = new RecordSet();
        for (int i = 0; i < paths.length; i++) {
            filerealPath = paths[i];
            if ("".equals(filerealPath)) {
                continue;
            }
            File file = new File(filerealPath);
            if (file.exists()) {
                filename = file.getName();
                fileType = EmailContentTypeUtils.getContentTypeByFileName(filename);
                filesize = Integer.parseInt(String.valueOf(file.length()));
                mrf_uuid = MailCommonUtils.getRandomUUID();
                sql = "INSERT INTO MailResourceFile(mailid,filename,filetype,filerealpath,iszip,isencrypt,isfileattrachment,fileContentId,isEncoded,filesize,isaesencrypt,mrf_uuid) "
                        + " values(?,?,?,?,?,?,?,?,?,?,?,?)";
                rt.executeUpdate(sql, mailid, filename, fileType, filerealPath, iszip, isencrypt, isfileattrachment, fileContentId, isEncoded, filesize, isaesencrypt, mrf_uuid);
            } else {
                writeLog("saveMailFile失败，附件不存在或者路径无法访问，File:" + filerealPath);
            }
        }
    }

    /**
     * 发送内部邮件的方法，就是在本地数据库的表中插入数据，而不需要从别人的服务器上去拿数据
     *
     * @param mailid 邮件的id
     * @param all
     * @return
     */
    public String sendInternalMailNew(String mailid, String internaltonewIds, String internalccnewIds, String internalbccnewIds, int all) {
        String ids = MailCommonUtils.trim(internaltonewIds + "," + internalccnewIds + "," + internalbccnewIds);
        String mode = Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), "authentic")); // 是否为ldap用户
        String sqlWhereStr = "";
        /*
         * if(!mode.equals("ldap")) {//非ldap用户
         *    sqlWhereStr=" and (b.loginid !='' or b.loginid is not null)";
         * }
         */

        if (all == 0) {  //非发送给所有人
            String subWhere = "";

            if (!"".equals(ids)) {
                subWhere += " " + (!"".equals(subWhere) ? "or" : "") + " (" + Util.getSubINClause(ids, "b.id", "in") + ") ";
            } else {
                writeLog("ids为空，发送邮件失败" + mailid);
                return "false";
            }

            sqlWhereStr += " and (" + subWhere + ")";

        } else if (isUseAppDetach) { // 如果是分权下的所有人 则获取分权下的所有人
            allids = MailCommonUtils.trim(allids);
            if (!"".equals(allids)) {
                sqlWhereStr += " and (" + Util.getSubINClause(allids, "b.id", "in") + ") ";
            }
        }
        sqlWhereStr += " and a.id=" + mailid;

        String sql = "";
        if (this.isSendApart) {
            // 分别发送时，部门，抄送人，密送人均为空，只有收件人toids为resourceid。
            sql = "INSERT INTO MailResource (resourceid,priority,sendfrom,senddate,size_n,subject,mailtype,hasHtmlImage,mailAccountId,status,folderId,isInternal,originalMailId,canview,attachmentNumber,mit_uuid,isSendApart,isNewContent,content_uuid,receiveNeedReceipt,classification,secretDeadline,encKey,crc) "
                    + " select b.id,priority,sendfrom,senddate,size_n,subject,mailtype,hasHtmlImage,'0','0','0',isInternal," + mailid + ",1 ,attachmentNumber,'' , '1',isNewContent,content_uuid,needReceipt,a.classification,a.secretDeadline,a.encKey,a.crc"
                    + " from MailResource a, HrmResource b where b.status in (0,1,2,3) " + sqlWhereStr;

        } else {
            sql = "INSERT INTO MailResource (resourceid,priority,sendfrom,senddate,size_n,subject,mailtype,hasHtmlImage,mailAccountId,status,folderId,isInternal,originalMailId,canview,attachmentNumber,mit_uuid,isSendApart,isNewContent,content_uuid,receiveNeedReceipt,classification,secretDeadline,encKey,crc) "
                    + " select b.id,priority,sendfrom,senddate,size_n,subject,mailtype,hasHtmlImage,'0','0','0',isInternal," + mailid + ",1 ,attachmentNumber,mit_uuid , '',isNewContent,content_uuid,needReceipt,a.classification,a.secretDeadline,a.encKey,a.crc"
                    + " from MailResource a, HrmResource b where b.status in (0,1,2,3) " + sqlWhereStr;
        }

        RecordSetTrans rst = new RecordSetTrans();
        try {
            rst.setAutoCommit(false);
            rst.execute(sql);
            rst.commit();


            if (this.isSendApart) { //如果是分别发送
                RecordSet recordSet = new RecordSet();
                List<List<Object>> updateparams = new ArrayList<List<Object>>();
                List<List<Object>> insertparams = new ArrayList<List<Object>>();
                recordSet.executeQuery("select resourceid,id from mailresource where originalmailid = ?", mailid);
                while (recordSet.next()) {
                    String mit_uuid = MailCommonUtils.getRandomUUID();
                    List<Object> list = new ArrayList<Object>();
                    List<Object> insertlist = new ArrayList<Object>();
                    list.add(mit_uuid);
                    insertlist.add(mit_uuid);
                    list.add(recordSet.getString("id"));
                    insertlist.add(recordSet.getString("resourceid"));
                    updateparams.add(list);
                    insertparams.add(insertlist);
                }
                String update_mailresource_sql = "update mailresource set mit_uuid= ? where id= ?";
                String insert_mail_internalto_sql = "insert into mail_internalto (mit_uuid, toids) values(?, ?)";
                if (updateparams.size() > 0) {
                    BatchRecordSet mailresource_br = new BatchRecordSet();
                    mailresource_br.executeBatchSql(update_mailresource_sql, updateparams);
                }
                if (insertparams.size() > 0) {
                    BatchRecordSet mail_internalto_br = new BatchRecordSet();
                    mail_internalto_br.executeBatchSql(insert_mail_internalto_sql, insertparams);
                }
            }

            return "true";
        } catch (Exception e) {
            rst.rollback();

            writeLog("sendInternalMailNew.mailid=" + mailid + ",sql=" + sql);
            writeLog(e);
            return "false";
        }
    }

    /**
     * 发送内部邮件，复制收件人附件信息。
     *
     * @param mailid 邮件ID
     */
    public void sendInternalMailFile(String mailid) {
        RecordSet rs = new RecordSet();
        String hasFilesql = "select id from mailresourcefile where mailid = ? order by id";
        rs.executeQuery(hasFilesql, mailid);
        if (rs.next()) {
            String sql = "INSERT INTO MailResourceFile(mailid,filename,filetype,filerealpath,iszip,isencrypt,isfileattrachment,fileContentId,isEncoded,filesize,isaesencrypt,aescode,mrf_uuid,htmlcode,pdfcode,storageStatus,tokenKey,secretLevel,secretDeadline)  " +
                    "select b.id,filename,filetype,filerealpath,iszip,isencrypt,isfileattrachment,fileContentId,isEncoded,filesize,isaesencrypt,aescode,mrf_uuid,htmlcode,pdfcode,storageStatus,tokenKey,a.secretLevel,a.secretDeadline " +
                    "from MailResourceFile a,MailResource b where b.originalMailId=? and a.mailid=? order by b.id asc,a.id asc";
            rs.executeUpdate(sql, mailid, mailid);
        }
    }

    /**
     * 发送内部邮件，oracle数据库，复制收件人邮件内容信息。
     *
     * @param mailid 邮件ID
     */
    public void updateInternalMailContent(String mailid) {
        String sql = "insert into mailcontent (mailid,mailcontent) select a.id,b.mailcontent from MailResource a, MailContent b where a.originalMailId= ? and b.mailid = ?";
        RecordSet rs = new RecordSet();
        rs.executeUpdate(sql, mailid, mailid);
    }

    /**
     * 添加新邮件到达系统提醒SysPoppupRemindInfoNew。并发送手机提醒。
     *
     * @param mailid 邮件ID
     */
    public void setAutoRemindMail(String mailid) {
        MailArriveRemind remind = new MailArriveRemind(mailid, "1");
        MailCommonUtils.executeThreadPool(EmailPoolSubTypeEnum.EMAIL_ARRIVE_REMIND.toString(), remind);
    }


    /**
     * 快捷回复邮件内容
     *
     * @param receivemailId 被回复邮件主键值
     * @param replycontent  快捷回复内容
     * @param type          类型：fastReply 快捷回复；receiveNeedReceipt 回执
     * @return
     */
    public String fastReply(String receivemailId, String replycontent, User user, String type) {
        String sendstatus = "true";
        //获取个人配置信息
        EmailSettingService emailSettingService = new EmailSettingService();
        EmailSettingBean emailSettingBean = emailSettingService.getUserMailSetting(user.getUID());
        try {

            //判断mailid邮件归属人
            String resourceids = MailManagerService.getAllResourceids(String.valueOf(user.getUID()));
            RecordSet rt = new RecordSet();
            rt.executeQuery("SELECT * FROM MailResource WHERE (" + Util.getSubINClause(resourceids, "resourceid", "in") + ") AND id = ?", receivemailId);
            if (rt.next()) {
                int userid = rt.getInt("resourceid");
                int isInternal = rt.getInt("isInternal");
                String _mailAccountId = rt.getString("mailAccountId");
                String mailSendfrom = rt.getString("sendfrom");
                String classification = Util.null2s(rt.getString("classification"), "4");//密级(快捷回复和回执，密级以原邮件密级为准)
                if (isInternal == 1) {
                    //只支持收件箱快捷回复，已发送等已经去除快捷回复说法 收件箱邮件
                    this.internaltonewIds = rt.getString("sendfrom");
                    //取快捷回复及回执收件方能看到的最高资源密级
                    HrmClassifiedProtectionBiz hrmcp = new HrmClassifiedProtectionBiz();
                    String maxResourceClassification = hrmcp.getMaxResourceSecLevelById2(this.internaltonewIds);
                    if (classification.compareTo(maxResourceClassification) < 0) {
                        sendstatus = "false5";
                        writeLog("sendMailByMailid方法:不符合密级要求的收件人被过滤，无有效收件人。被回复邮件receivemailId:" + receivemailId + "的密级为:" + classification + ";不符合密级要求的收件人:" + this.internaltonewIds + "能看到的最高资源密级为：" + maxResourceClassification);
                        return sendstatus;
                    }
                    this.sendfrom = userid + "";
                } else {
                    if ("-99".equals(_mailAccountId) && mailSendfrom.indexOf("@") == -1) {
                        //导入eml的，并且是OA自身的内部邮件
                        return "false6";
                    }
                    RecordSet accountRS = new RecordSet();
                    accountRS.executeQuery("SELECT accountmailaddress FROM MailAccount where id = ?", _mailAccountId);
                    accountRS.first();
                    String accountid = accountRS.getString("accountmailaddress");
                    if (mailSendfrom.equals(accountRS.getString("accountmailaddress"))) {// 已发送邮箱
                        this.TO = rt.getString("sendto");
                        this.CC = rt.getString("sendcc");
                        this.BCC = rt.getString("sendbcc");
                        this.sendfrom = mailSendfrom;
                    } else {// 收件箱邮件
                        this.sendfrom = accountid;
                        this.TO = mailSendfrom;
                    }

                    if (this.sendfrom.isEmpty()) {
                        sendstatus = "false2";
                        return sendstatus;
                    }
                    //外部邮件无密级概念
                    classification = "";
                }

                // 获取邮件回复邮件内容+邮件标题
                MailManagerService mms = new MailManagerService();
                mms.getReplayMailInfoNew(receivemailId, user, emailSettingBean);
                this.subject = mms.getSubject();

                String senddate = MailCommonUtils.getTodaySendDate();

                if ("fastReply".equals(type)) {// 快捷回复
                    this.content = replycontent + "<br/>" + mms.getContent();
                } else {// 回执
                    this.content = replycontent;
                }
                this.priority = "3";

                String _status = "1";
                String _folderId = "-1";

                // 先保存发件箱中的邮件信息
                char separator = Util.getSeparator();
                String uuid = MailCommonUtils.getRandomUUID();  //新增加的行的唯一标志，为randomUUID值。通过此值查找刚插入的记录以获得id主键值
                String params = uuid + separator + String.valueOf(userid) + separator + priority + separator +
                        sendfrom + separator + CC + separator + BCC + separator +
                        TO + separator + senddate + separator + "0" + separator +
                        subject + separator + "" + separator + texttype + separator + "" + separator +
                        _mailAccountId + separator + _status + separator + _folderId;
                int mailid = MailCommonUtils.mailResourceInsert(params, content);
                // 处理收件人，抄送人，密送人问题
                MailCommonUtils.updateMailTo(mailid, TO, CC, BCC);

                //根据密级获取默认保密期限
                HrmClassifiedProtectionBiz hrmcp = new HrmClassifiedProtectionBiz();
                String secretDeadline = hrmcp.getResourceClassificationValidity(classification, new User(userid));

                //密级加密
                EncryptUtil encryptUtil = new EncryptUtil();
                Map crcMap = encryptUtil.getLevelCRC(mailid + "", classification);
                String encKey = Util.null2String(crcMap.get("encKey"));
                String crc = Util.null2String(crcMap.get("crc"));

                String mit_uuid = MailCommonUtils.getRandomUUID(); //新增加的行的唯一标志，为randomUUID值。通过此值查找刚插入的记录以获得id主键值
                rt.executeUpdate("update MailResource set isInternal=?,mit_uuid=?,classification=?,encKey=?,crc=?,secretDeadline=? where id=?", isInternal, mit_uuid, classification, encKey, crc, secretDeadline, mailid);
                if (isInternal == 1) {
                    EmailCommonUtils.insertMailInternalTo(mit_uuid, internaltonewIds, "toids", internaltonewIds, "resource", internaltonewIds, user);
                }

                //计算邮件大小
                float totalsize = this.content.getBytes().length + this.subject.getBytes().length;
                rt.executeUpdate("UPDATE MailResource SET size_n = ? WHERE id = ?", totalsize, mailid);

                // 进行邮件发送
                /*if (!this.sendMailByMailid(mailid, type)) {
                    sendstatus = "false";
                }*/

                //邮件审批改造 start qc1871600
                BaseBean baseBean = new BaseBean();
                int classificationLevel = Integer.parseInt(baseBean.getPropValue("qc1871600", "classification_level"));
                if (Integer.parseInt(classification) <= classificationLevel && !"0".equals(user.getManagerid())) {
                    RecordSet rs = new RecordSet();
                    String sendFrom = String.valueOf(user.getUID());
                    String sendTo = TO;
                    String manager = user.getManagerid();
                    rs.executeUpdate("insert into mail_approve(status, mail_id, send_from, send_to, manager) values ('0', ?, ?, ?, ?)", mailid, sendFrom, sendTo, manager);
                    rs.executeUpdate("update mailresource set folderid = -17,resourceid = ? where id = ?", manager, mailid);
                } else {
                    boolean b = this.sendMailByMailid(mailid, type);
                    if (!b) {
                        sendstatus = "false";
                    }
                }
                //邮件审批改造 end qc1871600

                // 邮件发送失败，保存到草稿箱
                if ("false".equals(sendstatus)) {
                    rt.executeUpdate("update mailresource set folderId='-2', canview=1 where id = ?", mailid);
                } else {
                    rt.executeUpdate("update mailresource set canview=1 where id = ?", mailid);

                    //更新邮件回复状态
                    updateMailFlag(receivemailId, "2");

                    //删除邮件发送失败记录的失败信息
                    rt.executeUpdate("DELETE FROM email_sendErrorInfo WHERE mailid = ?", mailid);

                }
            } else {
                sendstatus = "false";
            }
        } catch (Exception e) {
            writeLog("fastReply 快捷回复邮件失败！receivemailId=" + receivemailId + ", type=" + type + ", userid=" + user.getUID() + ", lastname=" + user.getLastname());
            writeLog(e);

            sendstatus = "false";
        }
        return sendstatus;
    }

    /**
     * 根据邮件主键进行邮件发送
     *
     * @param mailid
     * @return
     */
    public boolean sendMailByMailid(int mailid) {
        this.writeLog("执行到sendMailByMailId(int mailid)");
        return sendMailByMailid(mailid, "");
    }

    /**
     * 根据邮件主键进行邮件发送
     *
     * @param mailid
     * @param type   操作类型，如回执操作：receiveNeedReceipt
     * @return
     */
    public boolean sendMailByMailid(int mailid, String type) {
        boolean sendstatus = true;
        try {
            RecordSet rs = new RecordSet();
            rs.executeQuery("select * from MailResource where id = ?", mailid);
            rs.first();
            String mit_uuid = rs.getString("mit_uuid");
            String isNewContent = rs.getString("isNewContent");
            String content_uuid = rs.getString("content_uuid");
            this.sendfrom = rs.getString("sendfrom");
            this.CC = rs.getString("sendcc");
            this.BCC = rs.getString("sendbcc");
            this.TO = rs.getString("sendto");
            this.subject = rs.getString("subject");
            this.needReceipt = rs.getString("needReceipt");
            this.isSendApart = "1".equals(rs.getString("isSendApart")) ? true : false;
            AppDetachComInfo adci = new AppDetachComInfo();
            this.isUseAppDetach = adci.isUseAppDetach();

            if ("1".equals(isNewContent)) {
                RecordSet rsContent = new RecordSet();
                rsContent.executeQuery("select mailcontent from mailcontent where content_uuid=?", content_uuid);
                if (rsContent.next()) {
                    this.content = rsContent.getString("mailcontent");
                }
            } else {
                if ("oracle".equals(rs.getDBType())) {
                    RecordSet rsContent = new RecordSet();
                    rsContent.executeQuery("select mailcontent from mailcontent where mailid=?", mailid);
                    if (rsContent.next()) {
                        this.content = rsContent.getString("mailcontent");
                    }
                } else {
                    this.content = rs.getString("content");
                }
            }

            this.content = this.content.replace("&#160;", " "); //google浏览器洗发送邮件超文本转为纯文本提交会包含&#160;

            this.isInternal = rs.getInt("isInternal");
            this.texttype = Util.null2o(rs.getString("mailtype"));
            this.priority = rs.getString("priority");
            int userid = rs.getInt("resourceid");

            this.internaltonewIds = EmailCommonUtils.getEmailInternalIds(mit_uuid, "toids", 1);
            this.internalccnewIds = EmailCommonUtils.getEmailInternalIds(mit_uuid, "ccids", 1);
            this.internalbccnewIds = EmailCommonUtils.getEmailInternalIds(mit_uuid, "bccids", 1);


            if (isInternal != 1) {
                int mailAccountId = rs.getInt("mailAccountId");

                // 附件信息
                ArrayList filenames = this.getFileNameFromSendByMailid(mailid);
                ArrayList filetypes = this.getFileType("", String.valueOf(mailid));
                ArrayList filecontents = this.getFileContentFromSendByMailid(mailid);
                List<EmailSendFileOldEntity> fileList = new ArrayList<EmailSendFileOldEntity>(); //防止顺序有问题
                for (int i = 0, bodyfilesize = filenames.size(); i < bodyfilesize; i++) {
                    InputStream is = (InputStream) filecontents.get(i);
                    String filename = (String) filenames.get(i);
                    String filetype = filetypes.get(i) == null ? "" : (String) filetypes.get(i);
                    String ctype = Util.null2s(filetype, EmailContentTypeUtils.getContentTypeByFileName(filename));
                    /*
                    String ctype = FileTypeMap.getDefaultFileTypeMap().getContentType(filename);
                    filename = "=?UTF-8?B?" + MailCommonUtils.encodeBase64String(filename.getBytes("UTF-8")) + "?=";
                    filename = filename.replace("\n", ""); // 当文件名过长时
                    StringBuffer sb = new StringBuffer(filename.length()); // 需要
                    for (int j = 0; j < filename.length(); j++) { // 过滤换行和无用符号
                        if (filename.getBytes()[j] == 13) {
                            continue;
                        }
                        sb.append(filename.charAt(j));
                    }
                    */
                    DataHandler da = new DataHandler(new javax.mail.util.ByteArrayDataSource(is, ctype));

                    EmailSendFileOldEntity entity = new EmailSendFileOldEntity();
                    entity.setFileName(filename);
                    entity.setDataHandler(da);
                    fileList.add(entity);
                }


                if (this.isSendApart) { // 分别发送，初始化发送状态
                    MailSendApartLogService msals = new MailSendApartLogService();
                    msals.addMailSendApartLog(mailid, this.sendfrom, TO);
                }

                // 外部邮件
                // 测试新发送方法  发送
                EmailSend emailSend = new EmailSend();
                //sendstatus = emailSend.sendByMailAccount(_mailAccountId, mailid) + ""; //一种发送方法，在方法内，重新读取数据库组装

                //另一种，这种方式，上面的附件不存在时错误判断，就可以沿用。
                EmailSenderEntity sender = EmailSend.getEmailSenderByAccountId(mailAccountId);
                EmailSendEntity entity = emailSend.packParameterToEntity(sendfrom, TO, CC, BCC, subject, 3, content, null, null, null,
                        mailid, fileList, isSendApart, priority, needReceipt, !"1".equals(texttype));

                //兼容新方法的额外设置
                entity.getFrom().setNickname(sender.getNickName()); //发件人昵称
                entity.setMailAccountId(mailAccountId);
                entity.setTrackingInfo(mailid + "#" + mailAccountId + "#" + "sendMailByMailid"); //跟踪信息

                sendstatus = emailSend.sendMailNoEx(sender, entity);

            } else {
                // 内部邮件
                //取发信人数
                if (!"receiveNeedReceipt".equals(type)) {
                    //回执不触发人数规则
                    int count = Util.TokenizerString(MailCommonUtils.trim(internaltonewIds), ",").size()
                            + Util.TokenizerString(MailCommonUtils.trim(internalccnewIds), ",").size()
                            + Util.TokenizerString(MailCommonUtils.trim(internalbccnewIds), ",").size();

                    //判断是否触发收件人数规则 -1代表无限制
                    EmailCommonUtils ecm = new EmailCommonUtils();
                    int sendcount = ecm.isNeedSendcountRule(String.valueOf(userid));
                    if (-1 != sendcount && count > sendcount) {
                        writeLog("sendMailByMailid方法，发件人数:" + count + "过多,触发发信限制,最大允许人数:" + sendcount);
                        return false;
                    }
                }
                String sendstatusStr = sendInternalMailNew(mailid + "", internaltonewIds, internalccnewIds, internalbccnewIds, 0);
                sendstatus = "true".equals(sendstatusStr);

                if (sendstatus) {
                    this.sendInternalMailFile(mailid + "");

                    this.setAutoRemindMail(mailid + "");

                    //删除邮件发送失败记录的失败信息
                    rs.executeUpdate("DELETE FROM email_sendErrorInfo WHERE mailid = ?", mailid);

                }
            }

            if (sendstatus) {
                // 创建一个线程 在线程中去更新邮件空间
                MailCommonUtils.SYS_MAIL_ALERT_POOL.execute(new MailSpaceUpdateThread(mailid + ""));
            }
        } catch (Exception e) {
            writeLog("sendMailByMailid 错误，mailid=" + mailid + ", type=" + type);
            writeLog(e);
            sendstatus = false;
        }
        return sendstatus;
    }

    /**
     * sendMailByMailid 方法中获取附件名称方法。
     *
     * @param mailid 邮件ID
     * @return
     */
    private ArrayList getFileNameFromSendByMailid(int mailid) {
        ArrayList list = new ArrayList();
        RecordSet rs = new RecordSet();
        rs.executeQuery("select filename from MailResourceFile where mailid=? and isfileattrachment=1 order by id", mailid);
        while (rs.next()) {
            list.add(rs.getString("filename"));
        }
        return list;
    }

    /**
     * sendMailByMailid 方法中获取附件流信息方法。
     *
     * @param mailid 邮件ID
     * @return
     */
    public ArrayList getFileContentFromSendByMailid(int mailid) {
        InputStream source = null;
        RecordSet rs = new RecordSet();
        ArrayList list = new ArrayList();
        rs.executeQuery("select id from MailResourceFile where mailid=? and isfileattrachment=1 order by id", mailid);
        while (rs.next()) {
            try {
                MailFilePreviewService mailFilePreviewService = new MailFilePreviewService();
                source = mailFilePreviewService.getInputStreamByMailFileId(rs.getString("id"));
            } catch (Exception e) {
                writeLog("getFileContentFromSendByMailid 获取附件留信息错误! mailid=" + mailid);
                writeLog(e);
            }
            list.add(source);
        }

        return list;
    }

    /**
     * 更新邮件转发，回复等发送状态
     *
     * @param mailid
     * @param flag   //-1：新建邮件;1：回复;2：回复全部;3：转发;4：草稿;5：再次编辑;6：回复带附件;7：回复全部带附件;
     */
    private void updateMailFlag(String mailid, String flag) {

        int newflag = Util.getIntValue(flag);
        if (!(newflag == 1 || newflag == 2 || newflag == 3 || newflag == 6 || newflag == 7)) {
            return;
        }

        //获取当前状态 flag:1 已回复;flag:3 已转发;flag:4 已转发且已回复;
        RecordSet rs = new RecordSet();
        String sql = "select flag from MailResource where id = ?";
        rs.executeQuery(sql, mailid);
        int oldflag = 0;
        if (rs.next()) {
            oldflag = rs.getInt("flag");
            if (oldflag < 0) {
                oldflag = 0;
            }
        }

        if (0 == oldflag) {
            //原始邮件
            if (newflag == 1 || newflag == 2 || newflag == 6 || newflag == 7) {
                //回复等操作都置为1
                newflag = 1;
            }
        } else if (1 == oldflag) {
            //如果处在已回复状态，则再次回复等操作都刷为1
            if (newflag == 1 || newflag == 2 || newflag == 6 || newflag == 7) {
                newflag = 1;
            } else {
                newflag = newflag + oldflag;
            }
        } else if (3 == oldflag) {
            //如果处在已转发状态，则再次回复等操作都刷为1
            if (newflag == 1 || newflag == 2 || newflag == 6 || newflag == 7) {
                newflag = 1 + oldflag;
            } else if (newflag == 3) {
                //如果再次转发，则不作操作
                newflag = 3;
            }
        } else if (4 == oldflag || oldflag > 4) {
            //如果处在已转发且已回复状态，则再次回复不做更新处理
            newflag = oldflag;
        } else {
            if (newflag == 1 || newflag == 2 || newflag == 6 || newflag == 7) {
                //回复等操作都置为1
                newflag = 1;
            }
            newflag = newflag + oldflag;
        }

        rs.executeUpdate("update MailResource set flag = ? where id = ?", newflag, mailid);
    }

    private byte[] toByteArray(String filePath) {
        try {
            InputStream in = new FileInputStream(filePath);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int n = 0;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            in.close();
            return out.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 审批邮件
     * qc1871600
     */
    public String approveMail(HttpServletRequest request, User user) throws Exception {

        EmailDateTimeUtil edt = new EmailDateTimeUtil();
        FileUpload fu = new FileUpload(request, false);
        RecordSet rs = new RecordSet();

        //防止url炸弹攻击,方案改为数据库机制
        String sessionUUid = Util.null2String(fu.getParameter("sessionUUid"));
        boolean cansend = EmailSendSessionUUidBiz.getSendSessionUUid(user, sessionUUid);
        if (!cansend) {
            writeLog("当前sessionUUid为：" + sessionUUid + ";当前用户为：" + user.getUID());
            return "false2";
        }

        this.languageid = user.getLanguage();

        // 获取邮件内容中需要上传的图片信息

        String _accountId = "";
        int _mailAccountId = Util.getIntValue(fu.getParameter("mailAccountId")); // 邮件账户id
        int _hrmAccountid = Util.getIntValue(fu.getParameter("hrmAccountid")); // 人力资源账户id
        int _folderId = Util.getIntValue(fu.getParameter("folderid"), -1);// -1:SentBox -2:DraftBox -3:TrashBox

        String _status = "0"; // 0:未读 1:已读

        // 是否内部邮件，为1是内部邮件，否则是外部邮件
        isInternal = Util.getIntValue(fu.getParameter("isInternal"));

        //保存到发件箱(1--保存到发件箱，""为没有勾选【保存到发件箱】)
        String savesend = Util.null2String(fu.getParameter("savesend"));
        if ("0".equals(savesend)) {
            savesend = "";
        }
        String _tempsavesend = savesend;
        //保存到草稿箱【1--保存到草稿箱，0---不保存到草稿箱】
        String savedraft = Util.null2String(fu.getParameter("savedraft"));
        //自动保存草稿
        String autoSave = Util.null2String(fu.getParameter("autoSave"));
        this.autoSave = autoSave;

        timingsubmitType = Util.null2String(fu.getParameter("timingsubmitType")); //submit定时发送，save保存的定时发送邮件，后期修改

        // E9添加
        int opIsTimingSend = Util.getIntValue(request.getParameter("op_isTimingSend"));
        if (opIsTimingSend == 1) {
            if ("1".equals(savedraft)) {
                timingsubmitType = "save";
            } else {
                timingsubmitType = "submit";
            }
            savedraft = "1";
        }

        //定时发送时间
        this.timingdate = Util.null2String(fu.getParameter("timingdate"));
        //转换为服务器时间（多时区处理）
        this.timingdate = edt.getServerDateTime(this.timingdate);

        // 是否需要回执
        this.needReceipt = Util.null2o(fu.getParameter("needReceipt"));

        //是否是分别发送
        this.isSendApart = "1".equals(Util.null2o(fu.getParameter("isSendApart")));

        this.savedraft = savedraft;
        priority = Util.null2String(fu.getParameter("priority"));  // 邮件重要级别
        priority = "1".equals(priority) ? priority : "3";

        int all = 0;//发送给所有人标记  0为非

        // 邮件资源密级 机密1;秘密2;内部3;公开4
        String classification = Util.null2o(fu.getParameter("classification"));
        HrmClassifiedProtectionBiz hrmcp = new HrmClassifiedProtectionBiz();
        boolean isOpenClassification = hrmcp.isOpenClassification(); //是否开启密级
        if (!isOpenClassification) {
            //密级没开的时候置为空
            classification = "";
        }
        if (isInternal == 1) {

            //开启密级的时候，强校验密级权限，防止密级越权
            if (isOpenClassification) {
                //取当事人所能获取到的最高的资源密级(兼容了主次账号)
                String maxResourceClassification = hrmcp.getMaxResourceSecLevelById(user.getUID() + "");
                if (classification.compareTo(maxResourceClassification) < 0) {
                    writeLog("防止密级越权：" + ";当前密级为：" + classification + ";当前用户最高资源密级为：" + maxResourceClassification);
                    this.errorMessInfo = new MailErrorMessageInfo();
                    this.errorMessInfo.setErrorHint("" + SystemEnv.getHtmlLabelName(10003483, weaver.general.ThreadVarLanguage.getLang()) + ""); //密级越权
                    this.errorMessInfo.setSolution("" + SystemEnv.getHtmlLabelName(10003483, weaver.general.ThreadVarLanguage.getLang()) + ""); //密级越权
                    this.errorMessInfo.setErrorString("" + SystemEnv.getHtmlLabelName(10003484, weaver.general.ThreadVarLanguage.getLang()) + "");//"+weaver.systeminfo.SystemEnv.getHtmlLabelName(10003484,weaver.general.ThreadVarLanguage.getLang())+"
                    return "false6";
                }
            }
            _mailAccountId = -1;
            // E9页面的发送方式
            String internaltonew = Util.null2String(fu.getParameter("internaltonew")); //(新)内部邮件收件人
            String internalccnew = Util.null2String(fu.getParameter("internalccnew")); //(新)内部邮件抄送人
            String internalbccnew = Util.null2String(fu.getParameter("internalbccnew")); //(新)内部邮件密送人

            if (!internaltonew.isEmpty() || !internalccnew.isEmpty() || !internaltonew.isEmpty()) {
                //密级过滤后的完整人员集合数据
                sendtoNewMap = EmailCommonUtils.getInternalToNewByClassification(internaltonew, internalccnew, internalbccnew, classification);
                //之前人员id串是和人员数据json分开取的，现在整个合在一起，通过json解析获取
                //（所有人也一并传过来了） 所以不需要额外通过所有人标志去实时再取了
                internaltonewIds = sendtoNewMap.get(EmailConstant.TOIDS).getInternalUserids();  //(新)内部邮件收件人ids
                internalccnewIds = sendtoNewMap.get(EmailConstant.CCIDS).getInternalUserids(); //(新)内部邮件抄送人ids
                internalbccnewIds = sendtoNewMap.get(EmailConstant.BCCIDS).getInternalUserids(); //(新)内部邮件密送人ids


                //取过滤掉人员 resource 类型的浏览框数集合，只含有分部，部门，组等信息
                sendtoNoResourceNewMap = EmailCommonUtils.getInternalToNewNoResourceType(sendtoNewMap);
            }

            int count = Util.TokenizerString(MailCommonUtils.trim(internaltonewIds), ",").size() + Util.TokenizerString(MailCommonUtils.trim(internalccnewIds), ",").size() + Util.TokenizerString(MailCommonUtils.trim(internalbccnewIds), ",").size();

            //判断是否触发收件人数规则 -1代表无限制
            EmailCommonUtils ecm = new EmailCommonUtils();
            int sendcount = ecm.isNeedSendcountRule(user.getUID() + "");
            if (sendcount != -1 && count > sendcount) {
                writeLog("发件人数:" + count + "过多,触发发信限制,最大允许人数:" + sendcount);
                return "false3";
            }

            int realHrmAccountid = getRealHrmAccountId(isInternal, user.getUID(), _hrmAccountid);
            sendfrom = String.valueOf(realHrmAccountid);
        }
        // 排查邮件地址是否含有分号结尾的情况,转变;为,
        //主要是手机版本的邮件传过来的数据含有分号
        TO = MailCommonUtils.trim(TO.replace(";", ","));
        CC = MailCommonUtils.trim(CC.replace(";", ","));
        BCC = MailCommonUtils.trim(BCC.replace(";", ","));

        subject = MailCommonUtils.filterSpecialCharFOfSubject(fu.getParameter("subject")); //主题

        texttype = fu.getParameter("texttype");  //文本类型，texttype=1为纯文本形式发送。否则为html模式
        boolean isHtmlMode = !"1".equals(texttype);

        content = fu.getParameter("mouldtext"); //邮件内容

        if (!"0".equals(texttype)) {
            content = content.replace("&#160;", " "); // google浏览器洗发送邮件超文本转为纯文本提交会包含&#160;
        }

        if (!isHtmlMode) { //纯文本是应该转为转移后内容
            content = MailCommonUtils.escapeHtmlClosure(content);
        }

        if (isHtmlMode) {
            //对邮件内容进行处理，若包含FileDownloadLocation,则使用文档的图片上传方法，并替换正文中的链接
            PatternMatcher matcher;
            PatternCompiler compiler;
            Pattern pattern;
            PatternMatcherInput input;
            MatchResult result;

            compiler = new Perl5Compiler();
            matcher = new Perl5Matcher();

            pattern = compiler.compile("<img.*?src=['\"\\s]?(/.*?weaver.email.FileDownloadLocation\\?fileid=(\\d*)).*?>", Perl5Compiler.CASE_INSENSITIVE_MASK);
            input = new PatternMatcherInput(content);

            String imgFileSql = "";
            String fileId = "";

            while (matcher.contains(input, pattern)) {
                ImageFileManager ifm = new ImageFileManager();
                result = matcher.getMatch();
                fileId = result.group(2);
                imgFileSql = " select filename,filetype,filerealpath from MailResourceFile where id=?";
                RecordSet rst = new RecordSet();
                rst.executeQuery(imgFileSql, fileId);
                if (rst.next()) {
                    ifm.setComefrom("email");
                    ifm.setImageFileType(rst.getString("filetype"));
                    ifm.setImagFileName(rst.getString("filename"));
                    ifm.setData(toByteArray(rst.getString("filerealpath")));
                    int image_fileid = ifm.saveImageFile();
                    if (image_fileid != 0) {
                        content = content.replace(".email.FileDownloadLocation?fileid=" + fileId, ".file.FileDownload?fileid=" + image_fileid);
                    }
                }
            }
        }

        //===========================================================
        MailRule mRule = new MailRule();
        boolean hasSendRule = mRule.getSendRule(isInternal, _mailAccountId, user.getUID());
        String sendtype = "0";
        if (savesend.equals("1") || hasSendRule) {
            sendtype = "1";
        }
        if (savedraft.equals("1")) {
            sendtype = "2";
        }
        if (!savedraft.equals("1") && !savesend.equals("1")) {
            savesend = "1";
            sf = 1;
        }

        //生成邮件部分
        String senddate = MailCommonUtils.getTodaySendDate();
        int mailid = Util.getIntValue(fu.getParameter("mailid"), -1);
        String oldmailid = Util.null2s(Util.null2String(fu.getParameter("oldmailid")), "");//回复，回复全部等带入的被回复邮件ID
        boolean isnewmail = false;
        char separator = Util.getSeparator();
        String uuid = MailCommonUtils.getRandomUUID();  //新增加的行的唯一标志，为randomUUID值。通过此值查找刚插入的记录以获得id主键值
        if (mailid == -1) {
            isnewmail = true;
            //生成新的邮件
            String userid = "" + user.getUID();
            if (isInternal == 1) {
                userid = sendfrom; // 内部邮件邮件所属人等于发件人
            }
            String params = uuid + separator + userid + separator + priority + separator + sendfrom + separator + CC + separator + BCC + separator + TO + separator + senddate + separator + "0" + separator + subject + separator + "" + separator + texttype + separator + "" + separator + _mailAccountId + separator + _status + separator + _folderId;
            mailid = MailCommonUtils.mailResourceInsert(params, content);
            if (mailid < 0) {
                writeLog("mailid<0,主记录插入失败，发送即失败-uuid-" + uuid);
                return "false"; //主记录插入失败，发送即失败
            }
        } else if (mailid != -1) {
            //表示修改邮件的内容
            String userid = "" + user.getUID();

            //判断mailid邮件归属人
            String resourceids = MailManagerService.getAllResourceids(String.valueOf(userid));
            String sql = "SELECT id FROM MailResource WHERE (" + Util.getSubINClause(resourceids, "resourceid", "in") + ") AND id = ?";
            rs.executeQuery(sql, mailid);
            if (rs.getCounts() != 1) {
                writeLog("邮件所属人串号，当前人员：" + resourceids + "，当前邮件id：" + mailid);
                return "false4";
            }

            if (isInternal == 1) {
                userid = sendfrom; // 内部邮件邮件所属人等于发件人
            }
            String params = mailid + "" + separator + "" + userid + separator + priority + separator + sendfrom + separator + CC + separator + BCC + separator + TO + separator + senddate + separator + "0" + separator + subject + separator + "" + separator + texttype + separator + "" + separator + _mailAccountId + separator + _status + separator + _folderId;
            MailCommonUtils.mailResourceUpdate(mailid, params, content);
        }

        //定时发送，若email_timingsend没有记录生成一条记录
        if (opIsTimingSend == 1) {
            rs.executeQuery("select mailid from email_timingsend where mailid = ?", mailid);
            if (!rs.next()) {
                rs.executeUpdate("insert into email_timingsend (mailid) values (?)", mailid);
            }
        }

        if (isInternal == 1) { //内部邮件，更新收件人明细；更新邮件密级
            String secretDeadline = Util.null2String(fu.getParameter("secretDeadline")); //保密期限
            if (!"1".equals(classification) && !"2".equals(classification)) {
                //公开和内部无密级期限，置空，防止乱传数据
                secretDeadline = "";
            }
            String mit_uuid = MailCommonUtils.getRandomUUID(); //新增加的行的唯一标志，为randomUUID值。通过此值查找刚插入的记录以获得id主键值
            if ("".equals(classification)) {
                //内部邮件，哪怕没开密级功能也需要给上默认密级(防止后续开了密级，但是开启密级动作不在我们这，导致密级数据未刷,导致开启密级下列表查询逻辑不对)
                //或者可在列表查询逻辑中兼容此种数据，但为了保证数据一致性，采用此种方案。
                classification = "4";
            }
            EncryptUtil encryptUtil = new EncryptUtil();
            Map crcMap = encryptUtil.getLevelCRC(mailid + "", classification);
            String encKey = Util.null2String(crcMap.get("encKey"));
            String crc = Util.null2String(crcMap.get("crc"));
            rs.executeUpdate("update MailResource set isInternal=?, mit_uuid=?,classification=?,encKey=?,crc=?,secretDeadline =? where id=?", isInternal, mit_uuid, classification, encKey, crc, secretDeadline, mailid);
            EmailCommonUtils.insertMailInternalTo(mit_uuid, internaltonewIds, internalccnewIds, internalbccnewIds, sendtoNoResourceNewMap, user);
        }

        fu.setMailid(mailid);
        this.mailid = String.valueOf(mailid);

        //设置邮件定时发送时间,定时发送状态timingdatestate【0为未发送，1为发送成功，-1为发送失败,-2为不需要发送】【是否需要回执】
        int timingdatestate = "submit".equals(timingsubmitType) ? 0 : -2;
        RecordSet rstime = new RecordSet();
        rstime.executeQuery("select timingdatestate from email_timingsend where mailid = ?", mailid);
        if (rstime.next()) {
            int timestate = rstime.getInt("timingdatestate");
            if (timestate == 0) {
                timingdatestate = 0; // 如果已经是定时发送 自动保存草稿时不在更新此状态
            } else if (timestate == 1) {
                timingdatestate = 1;
            }
        }
        //更新定时发送信息，是否分别发送
        rs.executeUpdate("update MailResource set needReceipt = ?, isSendApart = ? where id = ?", needReceipt, this.isSendApart ? "1" : "0", mailid);

        rs.executeUpdate("update email_timingsend set timingdate = ?, timingdatestate = ? where mailid = ?", this.timingdate, timingdatestate, mailid);

        int canview = ("1".equals(savesend) || "1".equals(savedraft) || hasSendRule) ? 1 : 0;
        // alter table MailResource add canview integer canview【字段是否可被检索，1-可以，0不可以】
        rs.executeUpdate("update MailResource set canview = ? where id = ?", canview, mailid);

        String accids = fu.getParameter("accids"); //现有的附件id
        String delaccids = fu.getParameter("delaccids"); //页面操作时，删除的附件id

        //获取附件流
        filecontents = this.getFileContent(accids, mailid + "", delaccids, MailManagerService.getAllResourceids(String.valueOf(user.getUID())));
        rs.executeUpdate("UPDATE MailResource SET attachmentNumber=? WHERE id=?", filecontents.size(), mailid);
        filenames = this.getFileName(accids, String.valueOf(mailid));
        filetypes = this.getFileType(accids, String.valueOf(mailid));
        String sendstatus = "true";

        // 计算此封邮件大小
        rs.executeQuery("SELECT sum(filesize) FROM MailResourceFile WHERE mailid = ?", mailid);
        rs.next();
        int size = rs.getInt(1);
        float totalsize = content.getBytes().length + subject.getBytes().length + size;
        rs.executeUpdate("UPDATE MailResource SET size_n = ? WHERE id = ?", totalsize, mailid);

        //处理昵称明细
        if (isInternal != 1) {
            this.saveMailContactInfo(mailid, _mailAccountId + "", user.getUID() + "", Util.null2String(fu.getParameter("usernameTo")), Util.null2String(fu.getParameter("usernameCc")), Util.null2String(fu.getParameter("usernameBcc")));
        }

        if ("1".equals(savedraft)) {
            RecordSet tmpRs = new RecordSet();
            tmpRs.executeUpdate("update mailresource set folderid = -2 where id = ?", mailid);
            //sessionUUidList.remove(sessionUUid);

            //删除邮件发送失败记录的失败信息
            rs.executeUpdate("DELETE FROM email_sendErrorInfo WHERE mailid = ?", mailid);
            MailCommonUtils.SYS_MAIL_ALERT_POOL.execute(new MailSpaceUpdateThread(mailid + ""));
            return "draftSaved";  //只保存(存草稿)
        }

        if (hasSendRule) {
            if (!"1".equals(savesend)) {
                rs.executeUpdate("UPDATE MailResource SET isTemp='1' WHERE id=?", mailid);
            }
        }
        int bodyfilesize = filenames.size();

        //暂时放在此处，防止密级过滤人员发送失败后未保存到草稿箱
        //如果没收件人，但是有抄送密送，禁止发信
        if (!"1".equals(savedraft) && Util.TokenizerString(MailCommonUtils.trim(internaltonewIds), ",").size() <= 0) {
            writeLog("SendMail sendMail internaltonewIds - 无有效收件人员");
            this.errorMessInfo = new MailErrorMessageInfo();
            this.errorMessInfo.setErrorHint("" + SystemEnv.getHtmlLabelName(10003488, weaver.general.ThreadVarLanguage.getLang()) + ""); //所选人员均不符合密级要求，无有效发件人员
            this.errorMessInfo.setSolution("" + SystemEnv.getHtmlLabelName(10003488, weaver.general.ThreadVarLanguage.getLang()) + ""); //所选人员均不符合密级要求，无有效发件人员
            this.errorMessInfo.setErrorString("" + SystemEnv.getHtmlLabelName(10003489, weaver.general.ThreadVarLanguage.getLang()) + "");//无有效发件人员

            //更新到草稿箱中
            boolean updatestatus = rs.executeUpdate("update mailresource set folderid = -2 where id = ?", mailid);
            if (updatestatus) {
                //记录发信错误异常信息
                EmailCommonUtils.recordEmailSendErrorinfo(mailid, this.errorMessInfo);
            }

            return "false7";
        }
        // 关闭附件流信息
        for (int i = 0; i < bodyfilesize; i++) {
            InputStream is = (InputStream) filecontents.get(i);
            try {
                is.close();
            } catch (Exception ec) {
                writeLog(ec);
            }
        }
        // 更新到待审批邮件
        String manager = user.getManagerid();
        rs.executeUpdate("update mailresource set status=0,folderid = -17,need_approve = 1,approver = ? where id = ?", manager, mailid);


        Map<String, Object> map = new HashMap();
        map.put("code", "522");
        map.put("title", "邮件提醒");
        map.put("context", "你有待审批的邮件");
        map.put("linkUrl", "http://10.8.10.1:9000/spa/email/static/index.html#/main/email/approve?menu%20folderid=-178folderid=-17&%20key=kf4bss");
        map.put("linkMobileUrl", "");
        Set<String> userlist = new HashSet<String>();
        userlist.add(manager);
        map.put("userIdList", userlist);
        map.put("creater", manager);
        Map map1 = new HashMap<>();
        Map shareMap = new HashMap<>();
        map1.put("share",shareMap);
        shareMap.put("content","");
        map.put("emParams",map1);
        ServiceMessageCustomImpl serviceMessageCustom = new ServiceMessageCustomImpl();
        boolean status = serviceMessageCustom.sendCustomMessageSingle(JSON.toJSONString(map));
        System.out.printf("收到提醒测试"+manager);
//        if(status){
//            ret.put("code","1");
//            ret.put("msg","客资提醒发送成功");
//        }else{
//            ret.put("code","1");
//            ret.put("msg","客资提醒发送失败");
//        }
//        return ret;


        // 创建一个线程 在线程中去更新邮件空间
        MailCommonUtils.SYS_MAIL_ALERT_POOL.execute(new MailSpaceUpdateThread(mailid + ""));

        //执行邮件发信规则
        //这块移到最后去执行，不然规则中间执行和发信有冲突（发信数据还没完全落实）
        if (hasSendRule && "true".equals(sendstatus)) {
            mRule.apply(isInternal, String.valueOf(mailid), user, _mailAccountId, request, "1");
        }

        //执行内部邮件接收规则
        if (isInternal == 1 && "true".equals(sendstatus)) {
            this.executeInternalReciveRule(mailid);
        }
        EmailSendSessionUUidBiz.removeSendSessionUUid(user, sessionUUid);
        return sendstatus;

    }
}
