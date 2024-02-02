package weaver.interfaces.zkd.dn2023.oa.job;


import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.zkd.dn2023.oa.dao.DispatchDao;
import weaver.interfaces.zkd.dn2023.oa.util.BaseDao;

import java.util.List;

/**
 * @BelongsProject: ZKD_DA_2023
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.oa.job
 * @Author: hf_zhangyuan
 * @CreateTime: 2023-03-09  17:24
 * @Description: TODO
 * @Version: 1.0
 */

public class InsertDataJob extends BaseCronJob {

    @Override
    public void execute() {
        BaseDao baseDao = new BaseDao();

        List<String> requestidList = baseDao.getFilingRequestId("formtable_main_2");
        for (String requestid : requestidList){
            boolean ist = baseDao.insertData(requestid,"formtable_main_2","8281","1");
            if (ist){
                baseDao.updateBs(requestid,"formtable_main_2");
            }
        }
        List<String> requestidList2 = baseDao.getFilingRequestId("formtable_main_3");
        for (String requestid : requestidList2){
            boolean ist = baseDao.insertData(requestid,"formtable_main_3","8281","1");
            if (ist){
                baseDao.updateBs(requestid,"formtable_main_3");
            }
        }



    }
}
