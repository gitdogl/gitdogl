//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package weaver.interfaces.interfaces.mail4.job;

import org.apache.commons.lang.StringEscapeUtils;
import weaver.conn.RecordSet;
import weaver.interfaces.schedule.BaseCronJob;

public class HTMLJob extends BaseCronJob {
    public HTMLJob() {
    }

    public void execute() {
        String sql = "select * from mailcontent";
        RecordSet rs = new RecordSet();
        RecordSet rs2 = new RecordSet();
        rs.execute(sql);

        while(rs.next()) {
            String content_uuid = rs.getString("content_uuid");
            String MAILCONTENT = rs.getString("MAILCONTENT");
            MAILCONTENT = StringEscapeUtils.unescapeXml(MAILCONTENT);
            String up = "update mailcontent set MAILCONTENT = ? where content_uuid = '" + content_uuid + "'";
            rs2.executeUpdate(up, new Object[]{MAILCONTENT});
        }

    }
}
