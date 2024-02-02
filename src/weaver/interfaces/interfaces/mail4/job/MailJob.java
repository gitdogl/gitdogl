//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package weaver.interfaces.interfaces.mail4.job;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.api.networkdisk.logging.LoggerFactory;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.interfaces.mail4.dao.MailDao;
import weaver.interfaces.interfaces.mail4.dto.WhMailBill;
//import weaver.interfaces.interfaces.mail4.service.MailService;
import weaver.interfaces.interfaces.mail4.service.MailService;
import weaver.interfaces.schedule.BaseCronJob;

public class MailJob extends BaseCronJob {
    Log logger1= LogFactory.getLog("testlog");
    private static final BaseBean logger = new BaseBean();
    public MailJob() {
    }

    public void execute() {
        BaseBean bb = new BaseBean();
        String tablename = "";
        String xcsStr = "";
        String wheresql = "";
        if (tablename == null || tablename.equals("")) {
            tablename = bb.getPropValue("QYBaseSet", "mailtablename");
        }

        if (tablename == null || tablename.equals("")) {
            tablename = "ezoffice.oa_mail_h_interior";
        }

        if (xcsStr == null || xcsStr.equals("")) {
            xcsStr = bb.getPropValue("QYBaseSet", "Mailxcs");
        }

        if (xcsStr == null || xcsStr.equals("")) {
            xcsStr = "12";
        }

        if (wheresql == null || wheresql.equals("")) {
            wheresql = bb.getPropValue("QYBaseSet", "wheresql");
        }

        if (wheresql == null || wheresql.equals("")) {
            wheresql = " and 1=1";
        }

        MailDao mailDao = new MailDao();
        int xcs = Util.getIntValue(xcsStr, 1);
        int sl = mailDao.getMun(tablename, wheresql);
        logger.writeLog("总条数:" + sl);
        int rws = sl / xcs;
        logger.writeLog("任务数:" + rws);
        List<WhMailBill> whMailBills2 = mailDao.getWhMailBill(tablename, wheresql);
        logger.writeLog("实际查询出任务数：" + whMailBills2.size());
        List<List<WhMailBill>> wList = splitList(whMailBills2, rws);
        logger.writeLog("线程数:" + wList.size());
        Iterator var11 = wList.iterator();

//        while(var11.hasNext()) {
//            List<WhMailBill> whMailBills = (List)var11.next();
//            (new MailService(whMailBills, tablename)).start();
//        }

        long startTime = System.currentTimeMillis(); // 记录开始时间

        while(var11.hasNext()) {
            List<WhMailBill> whMailBills = (List)var11.next();
            (new MailService(whMailBills, tablename)).start();
        }

        long endTime = System.currentTimeMillis(); // 记录结束时间
        long executionTime = endTime - startTime; // 计算时间差

        long hours = executionTime / (60 * 60 * 1000);
        long minutes = (executionTime % (60 * 60 * 1000)) / (60 * 1000);
        long seconds = (executionTime % (60 * 1000)) / 1000;

        String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        System.out.println("线程运行时间：" + formattedTime);



    }

    public static <T> List<List<T>> splitList(List<T> list, int groupSize) {
        int length = list.size();
        int num = (length + groupSize - 1) / groupSize;
        List<List<T>> newList = new ArrayList(num);

        for(int i = 0; i < num; ++i) {
            int fromIndex = i * groupSize;
            int toIndex = (i + 1) * groupSize < length ? (i + 1) * groupSize : length;
            newList.add(list.subList(fromIndex, toIndex));
        }

        return newList;
    }
}
