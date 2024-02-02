package weaver.interfaces.zkd.dn2023.oa.job;

import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.zkd.dn2023.oa.service.DispatchService;

/**
 * @BelongsProject: ZKD_DA_2023
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.oa.job
 * @Author: hf_zhangyuan
 * @CreateTime: 2023-03-12  18:36
 * @Description: TODO
 * @Version: 1.0
 */

public class DispatchJob extends BaseCronJob {

    @Override
    public void execute() {
        DispatchService dispatchService = new DispatchService();
        dispatchService.exec();
    }
}
