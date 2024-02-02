package weaver.interfaces.jjc.k3client.cw.service;

import com.alibaba.fastjson.JSONObject;
import weaver.interfaces.jjc.k3client.cw.dto.K3PaymentNoteBill;

public class K3PaymentNoteService {
    public String exec(K3PaymentNoteBill k3PaymentNoteBill) throws Exception {

        K3Service k3Service = new K3Service();
        String data = JSONObject.toJSON(k3PaymentNoteBill).toString();
        String bdid = "AP_PAYBILL";
        System.out.println("推送数据体:" + data);


        return k3Service.SavePurRKD(bdid, data);
    }
}
