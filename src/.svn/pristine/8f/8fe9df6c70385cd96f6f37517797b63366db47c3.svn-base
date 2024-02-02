package weaver.interfaces.zkd.dn2023.oa.service;

import com.alibaba.fastjson.JSON;
import weaver.general.BaseBean;
import weaver.interfaces.zkd.dn2023.bhl.entity.DataCountBill;
import weaver.interfaces.zkd.dn2023.bhl.entity.DataCountEntity;
import weaver.interfaces.zkd.dn2023.bhl.serivce.DatePushService;
import weaver.interfaces.zkd.dn2023.oa.dao.DispatchDao;
import weaver.interfaces.zkd.dn2023.oa.dao.ReceiveDao;

import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: WEAVER
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.oa.service
 * @Author: luotianchen
 * @CreateTime: 2024-01-24  11:29
 * @Description: TODO
 * @Version: 1.0
 */

public class DispatchGetCountService extends BaseBean {
    DispatchDao dispatchDao=new DispatchDao();
    public void exec() {
        int sl = Integer.parseInt(dispatchDao.getDispatchCount());
        String date=dispatchDao.getCurDate();
        List<DataCountBill> countBillList=dispatchDao.getDataCountBill(date);
        DataCountEntity dataCount = new DataCountEntity();
        dataCount.setSys_name("OA");
        dataCount.setArch_type("1");
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
        int sl = Integer.parseInt(dispatchDao.getDispatchCount());
        List<DataCountBill> countBillList=dispatchDao.getDataCountBill(date);
        DataCountEntity dataCount = new DataCountEntity();
        dataCount.setSys_name("OA");
        dataCount.setArch_type("1");
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
