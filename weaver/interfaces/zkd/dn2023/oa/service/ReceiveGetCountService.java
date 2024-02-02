package weaver.interfaces.zkd.dn2023.oa.service;

import com.alibaba.fastjson.JSON;
import org.mapdb.Atomic;
import weaver.general.BaseBean;
import weaver.interfaces.zkd.dn2023.bhl.entity.*;
import weaver.interfaces.zkd.dn2023.bhl.serivce.DatePushService;
import weaver.interfaces.zkd.dn2023.oa.dao.ReceiveDao;
import weaver.interfaces.zkd.dn2023.oa.entity.DatsjlEntity;
import weaver.interfaces.zkd.dn2023.oa.entity.OAReceiveEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: WEAVER
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.oa.service
 * @Author: luotianchen
 * @CreateTime: 2024-01-22  17:02
 * @Description: TODO
 * @Version: 1.0
 */

public class ReceiveGetCountService extends BaseBean {
    ReceiveDao receiveDao=new ReceiveDao();
    public void exec(){
        int sl = Integer.parseInt(receiveDao.getReceiveCount());
        String date=receiveDao.getCurDate();
        List<DataCountBill> countBillList=receiveDao.getDataCountBill(date);
        DataCountEntity dataCount = new DataCountEntity();
        dataCount.setSys_name("OA");
        dataCount.setArch_type("2");
        dataCount.setTotal(sl);
        dataCount.setSynch_time(date);
        dataCount.setSys_name("OA");
        dataCount.setData_list(countBillList);

        System.out.println("发文发送格式:" + JSON.toJSONString(dataCount));
        DatePushService datePushService = new DatePushService();

        Map<String, String> resultParam = datePushService.postDataCount(dataCount);
        System.out.println("发送推送清单返回报文:" + JSON.toJSONString(resultParam));
        }

    public String exec(String date){
        int sl = Integer.parseInt(receiveDao.getReceiveCount());
        List<DataCountBill> countBillList=receiveDao.getDataCountBill(date);
        DataCountEntity dataCount = new DataCountEntity();
        dataCount.setSys_name("OA");
        dataCount.setArch_type("2");
        dataCount.setTotal(sl);
        dataCount.setSynch_time(date);
        dataCount.setSys_name("OA");
        dataCount.setData_list(countBillList);

        System.out.println("发文发送格式:" + JSON.toJSONString(dataCount));
        DatePushService datePushService = new DatePushService();

        Map<String, String> resultParam = datePushService.postDataCount(dataCount);
        System.out.println("发送推送清单返回报文:" + JSON.toJSONString(resultParam));
        return JSON.toJSONString(resultParam);
    }
    }
