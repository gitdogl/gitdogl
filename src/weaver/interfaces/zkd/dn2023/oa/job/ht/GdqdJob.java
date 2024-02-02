package weaver.interfaces.zkd.dn2023.oa.job.ht;

import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.zkd.dn2023.oa.service.ht.GdqdService;

public class GdqdJob extends BaseCronJob {
    @Override
    public void execute() {
        GdqdService gdqdService = new GdqdService();
        gdqdService.exec();
    }
}
