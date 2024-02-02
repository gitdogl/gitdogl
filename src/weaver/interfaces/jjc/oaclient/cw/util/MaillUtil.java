package weaver.interfaces.jjc.oaclient.cw.util;

import weaver.general.BaseBean;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * 发送邮件
 */
public class MaillUtil {
//    @Test
//    public void test() throws Exception {
//        testSend("347408605@qq.com","smtp","smtp.ustc.edu.cn","ef@ustc.edu.cn","Ef@1993");
//    }

    /**
     * 邮件发送
     *
     * @param sjr 收件人
     * @throws Exception
     */
    public void testSend(String sjr, String protocol, String host, String user, String pwd) throws Exception {
        BaseBean bb = new BaseBean();
        Properties prop = new Properties();
        prop.put("mail.transport.protocol", protocol); // 指定协议
        prop.put("mail.smtp.host", host); // 主机 stmp.qq.com
        prop.put("mail.smtp.auth", "true"); // 用户密码认证

        // 1. 创建邮件会话
        Session session = Session.getDefaultInstance(prop);
        // 2. 创建邮件对象
        MimeMessage message = new MimeMessage(session);
        String subject = "";

        if (subject == null || subject.equals("")) {
            subject = bb.getPropValue("CWYZ", "subject");
        }
        if (subject == null || subject.equals("")) {
            subject = "您的报销申请已完成审批.";
        }
        // 3. 设置参数：标题、发件人、收件人、发送时间、内容
        message.setSubject(subject);
        message.setSender(new InternetAddress(user));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(sjr));
        message.setSentDate(new Date());
        /*
         * 带附件(图片)邮件开发
         */
        // 构建一个总的邮件块
        MimeMultipart mixed = new MimeMultipart("mixed");
        // ---> 总邮件快，设置到邮件对象中
        message.setContent(mixed);
        // 左侧： （文本+图片资源）
        MimeBodyPart left = new MimeBodyPart();
        // 右侧： 附件
//        MimeBodyPart right = new MimeBodyPart();
        // 设置到总邮件块
        mixed.addBodyPart(left);
//        mixed.addBodyPart(right);
        /*************** 设置邮件内容: 多功能用户邮件 (related) *******************/
        // 4.1 构建一个多功能邮件块
        MimeMultipart related = new MimeMultipart("related");
        // ----> 设置到总邮件快的左侧中
        left.setContent(related);

        // 4.2 构建多功能邮件块内容 = 左侧文本 + 右侧图片资源
        MimeBodyPart content = new MimeBodyPart();
//        MimeBodyPart resource = new MimeBodyPart();

//        // 设置具体内容: a.资源(图片)
//        String filePath = "D://1.png";
//        DataSource ds = new FileDataSource(new File(filePath));
//        DataHandler handler = new DataHandler(ds);
//        resource.setDataHandler(handler);
//        resource.setContentID("1.png"); // 设置资源名称，给外键引用

        // 设置具体内容: b.文本
        String nr = "";

        if (nr == null || nr.equals("")) {
            nr = bb.getPropValue("CWYZ", "nr");
        }
        if (nr == null || nr.equals("")) {
            nr = "<p>您的报销申请已完成审批，请您携打印出的纸质报销单、发票和相关附件到东区师生活动中心财务核算大厅基金会财务柜台处办理报销。</p><p>注意事项：</p><p>1、 发放奖学金/助学金、奖教金、劳务/评审/聘用人员工资等人员费用须提交执行单位盖章后的《中国科学技术大学教育基金会费用发放明细表》原件；</p><p>2、 差旅费用报销须提交出差人签字后的《中国科学技术大学教育基金会差旅信息明细表》原件和审批后的学校出差审批单；</p><p>3、 会议费报销须提交审批后的学校会议申请表。</p><p>4、 单笔费用超过5万元（含）的，应提交合同原件，单张发票金额或连号发票合计金额超过1万元（含）的，应提交发票查验结果；</p><p>5、 使用电子发票报销的，应在发票上手写“承诺不重复报销”并签名；</p><p>6、 报销转账到个人账户且合计金额超过1000元（含）的，应提交支付记录证明；</p><p>7、 报销市内交通费的，应在票据上注明往返地及事由并签名；</p><p>8、 根据教育基金会财务及固定资产管理规定，涉及固定资产采购的报销应提前与教育基金会沟通。</p>";
        }
        content.setContent(nr, "text/html;charset=UTF-8");

        related.addBodyPart(content);
//        related.addBodyPart(right);
        message.setContent(related);
        // 5. 发送
        Transport trans = session.getTransport();
        trans.connect(user, pwd);
        trans.sendMessage(message, message.getAllRecipients());
        trans.close();
    }
}
