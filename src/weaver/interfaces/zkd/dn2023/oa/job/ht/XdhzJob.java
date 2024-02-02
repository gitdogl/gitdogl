package weaver.interfaces.zkd.dn2023.oa.job.ht;

import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.zkd.dn2023.oa.service.ht.XdhzService;

/**
 * @BelongsProject: ZKD_DA_2023
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.oa.job.ht
 * @Author: hf_zhangyuan
 * @CreateTime: 2023-03-28  18:37
 * @Description: TODO
 * @Version: 1.0
 */

public class XdhzJob extends BaseCronJob {

    @Override
    public void execute() {
        XdhzService xdhzService = new XdhzService();
        xdhzService.exec();
    }
}
