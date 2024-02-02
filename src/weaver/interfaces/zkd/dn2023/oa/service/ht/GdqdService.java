package weaver.interfaces.zkd.dn2023.oa.service.ht;

import com.alibaba.fastjson.JSON;
import weaver.interfaces.zkd.dn2023.bhl.entity.*;
import weaver.interfaces.zkd.dn2023.bhl.serivce.DatePushService;
import weaver.interfaces.zkd.dn2023.oa.dao.DatsjlDao;
import weaver.interfaces.zkd.dn2023.oa.dao.ht.HtDao;
import weaver.interfaces.zkd.dn2023.oa.util.BaseDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: WEAVER
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.oa.service.ht
 * @Author: luotianchen
 * @CreateTime: 2023-12-08  15:08
 * @Description: 推送清单
 * @Version: 1.0
 */
public class GdqdService {
    private final String url = "http://hetong.ustc.edu.cn";
    DatsjlDao datsjlDao = new DatsjlDao();
    HtDao htDao = new HtDao();
    BaseDao baseDao = new BaseDao();
    int fjys = 0;

    public void exec() {
        int sl = Integer.parseInt(htDao.getHtCount());
        String date=htDao.getCurDate();
        List<DataCountBill> countBillList=htDao.getDataCountBill(date);
        DataCountEntity dataCount = new DataCountEntity();
        dataCount.setSys_name("OA");
        dataCount.setArch_type("3");
        dataCount.setTotal(sl);
        dataCount.setSynch_time(date);
        dataCount.setSys_name("OA");
        dataCount.setData_list(countBillList);

        System.out.println("发文发送格式:" + JSON.toJSONString(dataCount));
        DatePushService datePushService = new DatePushService();

        Map<String, String> resultParam = datePushService.postDataCount(dataCount);
        System.out.println("发送推送清单返回报文:" + JSON.toJSONString(resultParam));
    }

    public void exec(String date){
        int sl = Integer.parseInt(htDao.getHtCount(date));
        List<DataCountBill> countBillList=htDao.getDataCountBill(date);
        DataCountEntity dataCount = new DataCountEntity();
        dataCount.setSys_name("OA");
        dataCount.setArch_type("3");
        dataCount.setTotal(sl);
        dataCount.setSynch_time(date);
        dataCount.setSys_name("OA");
        dataCount.setData_list(countBillList);

        System.out.println("发文发送格式:" + JSON.toJSONString(dataCount));
        DatePushService datePushService = new DatePushService();

        Map<String, String> resultParam = datePushService.postDataCount(dataCount);
        System.out.println("发送推送清单返回报文:" + JSON.toJSONString(resultParam));
    }
}
