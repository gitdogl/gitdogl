package weaver.interfaces.zkd.dn2023.oa.job.ht;


import weaver.interfaces.schedule.BaseCronJob;
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

public class HTInsertDataJob extends BaseCronJob {

    @Override
    public void execute() {
        BaseDao baseDao = new BaseDao();

        List<String> requestidList = baseDao.getFilingRequestId("formtable_main_19");
        for (String requestid : requestidList){
            boolean ist = baseDao.insertData(requestid,"formtable_main_19","6881","0");
            if (ist){
                baseDao.updateBs(requestid,"formtable_main_19");
            }
        }
        List<String> requestidList2 = baseDao.getFilingRequestId("formtable_main_36");
        for (String requestid : requestidList2){
            boolean ist = baseDao.insertData(requestid,"formtable_main_36","6881","0");
            if (ist){
                baseDao.updateBs(requestid,"formtable_main_36");
            }
        }
        List<String> requestidList3 = baseDao.getFilingRequestId("formtable_main_48");
        for (String requestid : requestidList3){
            boolean ist = baseDao.insertData(requestid,"formtable_main_48","6881","0");
            if (ist){
                baseDao.updateBs(requestid,"formtable_main_48");
            }
        }
        List<String> requestidList4 = baseDao.getFilingRequestId("formtable_main_41");
        for (String requestid : requestidList4){
            boolean ist = baseDao.insertData(requestid,"formtable_main_41","6881","0");
            if (ist){
                baseDao.updateBs(requestid,"formtable_main_41");
            }
        }


    }
}
