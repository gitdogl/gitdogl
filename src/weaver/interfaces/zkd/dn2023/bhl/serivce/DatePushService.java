package weaver.interfaces.zkd.dn2023.bhl.serivce;



import com.alibaba.fastjson.JSON;
import com.cloudstore.dev.api.util.HttpManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import weaver.general.BaseBean;
import weaver.interfaces.zkd.dn2023.bhl.entity.DataBill;
import weaver.interfaces.zkd.dn2023.bhl.entity.DataCountEntity;
import weaver.interfaces.zkd.dn2023.bhl.entity.FhBill;
import weaver.interfaces.zkd.dn2023.bhl.entity.HeadDataBill;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: ZKD_DA_2023
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.bhl.serivce
 * @Author: hf_zhangyuan
 * @CreateTime: 2023-03-07  14:02
 * @Description: TODO
 * @Version: 1.0
 */

public class DatePushService extends BaseBean {
    String url = "http://210.45.120.77:8180";

    public Map<String, String> getQuery(String type, String requestid) {
        Map<String, String> map = new HashMap<>();
        Map<String, String> returnmap = new HashMap<>();
        map.put("arch_type", type);
        map.put("origin_id", requestid);
        Map<String, String> head = new HashMap<>();
        HttpManager httpManager = new HttpManager();
        try {
            System.out.println("OA推送地址：" + url + "/oa-service/edoc/query");
            System.out.println("OA推送数据：" + JSON.toJSONString(map));
            String dataStr = httpManager.postJsonDataSSL(url + "/oa-service/edoc/query", JSON.toJSONString(map), head);
            System.out.println("对方返回数据：" + dataStr);
            JSONObject json = JSONObject.fromObject(dataStr);
            String code = json.getString("code");
            String msg = json.getString("msg");
            returnmap.put("code", code);
            returnmap.put("msg", msg);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return returnmap;

    }

    /**
     * 推送数据
     *
     * @param headDataBill
     * @return
     */
    public List<FhBill> postData(HeadDataBill headDataBill) {


        List<FhBill> stringList = new ArrayList<>();//传输失败的数据数组
        HttpManager httpManager = new HttpManager();
        Map<String, String> head = new HashMap<>();
        String code = "";
        JSONObject json = new JSONObject();
        try {
            System.out.println("OA推送地址：" + url + "/oa-service/edoc/archiving");
            System.out.println("OA推送数据：" + JSON.toJSONString(headDataBill));
            String dataStr = httpManager.postJsonDataSSL(url + "/oa-service/edoc/archiving", JSON.toJSONString(headDataBill), head);
            System.out.println("对方返回数据：" + dataStr);
            json = JSONObject.fromObject(dataStr);


        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        JSONArray jsonArr = json.getJSONArray("failedIds");
        for (int i = 0; i < jsonArr.size(); i++) {
            FhBill fhBill = new FhBill();
            // 在获取失败信息的内容
            String failedIdStr = jsonArr.getString(i);
            // 对字符串进行分隔，获取唯一标识符和错误信息
            String[] parts = failedIdStr.split("\\|", 2);
            String dataId = parts[0].trim();
            String message = parts[1].trim();
            fhBill.setDataId(dataId);
            fhBill.setMessage(message);
            System.out.println("dataId: " + dataId);
            System.out.println("message: " + message);

            stringList.add(fhBill);
        }
        return stringList;
    }


    public Map<String,String> postDataCount(DataCountEntity datacountentity) {
        HashMap<String, String> res = new HashMap<>();//传输失败的数据
        HttpManager httpManager = new HttpManager();
        Map<String, String> head = new HashMap<>();
        String code = "";
        JSONObject json = new JSONObject();
        try {
            System.out.println("OA推送地址：" + url + "/oa-service/edoc/arch-list");
            System.out.println("(新)OA推送数据：" + JSON.toJSONString(datacountentity));
            String dataStr = httpManager.postJsonDataSSL(url + "/oa-service/edoc/arch-list", JSON.toJSONString(datacountentity), head);
            System.out.println("对方返回数据：" + dataStr);
            json = JSONObject.fromObject(dataStr);

        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        res.put("code",json.getString("code"));
        res.put("msg",json.getString("msg"));
        return res;
    }
}
