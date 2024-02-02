package weaver.formmode.customjavacode.modeexpand;

import java.util.HashMap;
import java.util.Map;

import weaver.formmode.customjavacode.AbstractModeExpandJavaCodeNew;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.soa.workflow.request.RequestInfo;


/**
 * 说明
 * 修改时
 * 类名要与文件名保持一致
 * class文件存放位置与路径保持一致。
 * 请把编译后的class文件，放在对应的目录中才能生效
 * 注意 同一路径下java名不能相同。
 * @author Administrator
 *
 */
public class testmode extends AbstractModeExpandJavaCodeNew {
    /**
     * 执行模块扩展动作
     * @param param
     *  param包含(但不限于)以下数据
     *  user 当前用户
     *  importtype 导入方式(仅在批量导入的接口动作会传输) 1 追加，2覆盖,3更新，获取方式(int)param.get("importtype")
     *  导入链接中拼接的特殊参数(仅在批量导入的接口动作会传输)，比如a=1，可通过param.get("a")获取参数值
     *  页面链接拼接的参数，比如b=2,可以通过param.get("b")来获取参数
     * @return
     */
    public Map<String, String> doModeExpand(Map<String, Object> param) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            User user = (User)param.get("user");
            int billid = -1;//数据id
            int modeid = -1;//模块id
            RequestInfo requestInfo = (RequestInfo)param.get("RequestInfo");
            if(requestInfo!=null){
                billid = Util.getIntValue(requestInfo.getRequestid());
                modeid = Util.getIntValue(requestInfo.getWorkflowid());
                if(billid>0&&modeid>0){
                    System.out.println("获取到的billid是"+billid);
                    //------请在下面编写业务逻辑代码------
                }
            }
        } catch (Exception e) {
            result.put("errmsg","自定义出错信息");
            result.put("flag", "false");
        }
        return result;
    }

}