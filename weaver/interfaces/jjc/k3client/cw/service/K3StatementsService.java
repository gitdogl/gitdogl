package weaver.interfaces.jjc.k3client.cw.service;


import com.alibaba.fastjson.JSONObject;
import weaver.interfaces.jjc.k3client.cw.dto.K3StatementsBill;

public class K3StatementsService {
    public String exec(K3StatementsBill k3StatementsBill) throws Exception {

        K3Service k3Service = new K3Service();
        String data = JSONObject.toJSON(k3StatementsBill).toString();
        String bdid = "AR_RECEIVEBILL";
        System.out.println("推送数据体:" + data);


        return k3Service.SavePurRKD(bdid, data);
    }
}
