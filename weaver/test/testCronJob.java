package weaver.test;

import com.cloudstore.dev.api.bean.MessageBean;
import com.cloudstore.dev.api.bean.MessageType;
import com.cloudstore.dev.api.util.Util_Message;
import weaver.interfaces.schedule.BaseCronJob;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class testCronJob extends BaseCronJob {
    @Override
    public void execute(){
        MessageType messageType = MessageType.newInstance(2022060961); // 消息来源（见文档第四点补充 必填）
        Set<String> userIdList = new HashSet<>(); // 接收人id 必填\
        userIdList.add("1");
        String title = "这是最终版"; // 标题
        String context = "最终版嘿嘿"; // 内容
        String linkUrl = "/index.html#/main/portal/portal-1-1"; // PC端链接
        String linkMobileUrl = "/index.html#/main/portal/portal-1-1"; // 移动端链接
        try {
            MessageBean messageBean = Util_Message.createMessage(messageType, userIdList, title, context, linkUrl, linkMobileUrl);
            messageBean.setCreater(1);// 创建人id
            //message.setBizState("0");// 需要修改消息为已处理等状态时传入,表示消息最初状态为待处理
            //messageBean.setTargetId("121|22"); //消息来源code +“|”+业务id需要修改消息为已处理等状态时传入
            Util_Message.store(messageBean);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
