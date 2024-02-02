package weaver.interfaces.zkd.dn2023.oa.job;

import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.zkd.dn2023.oa.service.DispatchGetCountService;
import weaver.interfaces.zkd.dn2023.oa.service.ReceiveGetCountService;

/**
 * @BelongsProject: WEAVER
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.oa.job
 * @Author: luotianchen
 * @CreateTime: 2024-01-24  11:36
 * @Description: TODO
 * @Version: 1.0
 */

public class DispatchGetCountjob extends BaseCronJob {
    @Override
    public void execute() {
        DispatchGetCountService dispatchcService = new DispatchGetCountService();
        dispatchcService.exec();
    }
}
