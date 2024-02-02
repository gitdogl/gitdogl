package weaver.interfaces.zkd.dn2023.oa.job;

import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.zkd.dn2023.oa.service.ReceiveGetCountService;
import weaver.interfaces.zkd.dn2023.oa.service.ReceiveService;

/**
 * @BelongsProject: WEAVER
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.oa.job
 * @Author: luotianchen
 * @CreateTime: 2024-01-22  17:00
 * @Description: TODO
 * @Version: 1.0
 */

public class ReceiveGetCountJob extends BaseCronJob {
    @Override
    public void execute() {
        ReceiveGetCountService receivegcService = new ReceiveGetCountService();
        receivegcService.exec();
    }
}
