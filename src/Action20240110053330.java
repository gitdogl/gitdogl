//package weaver.interfaces.workflow.action.javacode;
//
//
//import com.cloudstore.dev.api.util.HttpManager;
//import org.json.JSONObject;
//import weaver.interfaces.workflow.action.Action;
//import weaver.general.BaseBean;
//import weaver.soa.workflow.request.RequestInfo;
//
//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 在线自定义action接口
// */
//public class Action20240110061652 extends BaseBean implements Action{
//    /**
//     * 流程路径节点后选择aciton后,会在节点提交后执行此方法。
//     */
//    public String execute(RequestInfo request)  {
//        Map<String, String> data = new HashMap<>();
//           data.put("contractId","123");
//          data.put("advice","同意");
//           data.put("checkCode","123123123");
//           String param = new JSONObject(data).toString();
//           writeLog(param);
//          String result=doPost("http://cggl.ustc.edu.cn/sfw2/e?page=contract.purchase.open.approve&window_=json",param);
//        writeLog("result="+result);
//        return Action.SUCCESS;
//    }
//
//    public String doPost(String requestUrl, String param) {
//        String dataStr="";
//        try {
//            HttpManager httpManager = new HttpManager();
//            Map<String, String> head = new HashMap<>();
//             dataStr = httpManager.postJsonDataSSL(requestUrl, param, head);
//        } catch (KeyManagementException e) {
//            throw new RuntimeException(e);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//        return  dataStr;
//    }
//}
