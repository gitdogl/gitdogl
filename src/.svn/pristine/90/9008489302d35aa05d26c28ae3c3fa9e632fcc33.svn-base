//package weaver.interfaces.hbky.tydb;
//
//import com.api.formmode.page.util.Util;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.methods.RequestEntity;
//import org.apache.commons.httpclient.methods.StringRequestEntity;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import weaver.AddAllWithCode;
//import weaver.AddAllWithCodeResponse;
//import weaver.conn.RecordSet;
//import weaver.ofs.interfaces.SendRequestStatusDataInterfaces;
//import weaver.ofs.interfaces.TestSendImpl;
//import weaver.workflow.request.todo.DataObj;
//import weaver.workflow.request.todo.RequestStatusObj;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import weaver.workflow.webservices.*;
//
///**
// * 本示例，只将接受的数据打印在log/ecology中，具体的接口调用操作，请在相关数据遍历中执行。
// * 实现weaver.ofs.interfaces.SendRequestStatusDataInterfaces接口中SendRequestStatusData
// */
//public class SendTodolist implements SendRequestStatusDataInterfaces {
//
//    private static final Log log = LogFactory.getLog(TestSendImpl.class);
//
//    /**
//     * 后台设置id
//     */
//    public String id ;
//    /**
//     * 设置的系统编号
//     */
//    public String syscode ;
//    /**
//     * 服务器URL
//     */
//    public String serverurl ;
//    /**
//     * 流程白名单
//     */
//    public ArrayList<String> workflowwhitelist ;
//    /**
//     * 人员白名单
//     */
//    public ArrayList<String> userwhitelist ;
//
//
//    public String getId() {
//        return id;
//    }
//    public String getSyscode() {
//        return syscode;
//    }
//    public String getServerurl() {
//        return serverurl;
//    }
//    public ArrayList<String> getWorkflowwhitelist() {
//        return workflowwhitelist;
//    }
//    public ArrayList<String> getUserwhitelist() {
//        return userwhitelist;
//    }
//
//    /**
//     * 实现消息推送的具体方法
//     * @param datas 传入的请求数据对象数据集
//     */
//    public void SendRequestStatusData(ArrayList<DataObj> datas) {
//
//        for(DataObj dobj : datas){
//            JSONArray jsonArray = new JSONArray();
//            JSONObject mainboject = new JSONObject();
//            String requestname  = "";
//            JSONObject donejson = new JSONObject();
//            JSONObject deljson = new JSONObject();
//            ArrayList<RequestStatusObj> tododatas = dobj.getTododatas();
//            if(tododatas.size()>0){//处理推送的待办数据
//                JSONArray todolist = new JSONArray();
//                for(RequestStatusObj rso : tododatas){//遍历当前发送的待办数据
//                    AddAllWithCode addAllWithCode = new AddAllWithCode();
//                    addAllWithCode.setSystemID("064c65f2-04e6-32d5-3655-258a00ae50ae");
//                    addAllWithCode.setSystemName("YWKSDJ");
//                    addAllWithCode.setItemType("48");
//                    addAllWithCode.setItemID(Util.null2String(rso.getRequestid()));
//                    addAllWithCode.setItemName(Util.null2String(rso.getRequestnamenew()));
//                    addAllWithCode.setActivityName(rso.getNodename());
//                    addAllWithCode.setActivityName(rso.getNodename());
//                    addAllWithCode.setStartedDateTime(rso.getSendTime());
//                    addAllWithCode.setStartedDateTime(rso.getSendTime());
//                    RecordSet rs = new RecordSet();
//                    String workcode="";
//                    String lastname="";
//                    rs.executeQuery("select workcode,lastname from hrmresource where id='"+Util.null2String(rso.getCreatorid())+"'");
//                    if(rs.next()){workcode=rs.getString("workcode");lastname=rs.getString("lastname");}
//                    addAllWithCode.setCreator(workcode);
//                    addAllWithCode.setCreatorName(lastname);
//                    addAllWithCode.setSourceUserID(workcode);
//                    LocalDate currentDate = LocalDate.now();
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//                    String formattedDate = currentDate.format(formatter);
//                    addAllWithCode.setItemUrl("http://10.1.18.121:81/interface/loginssoresultdb.jsp?state=3&workcode="+formattedDate+workcode+"&requestid="+rso.getRequestid());
//                    addAllWithCode.setState("0");
//                    addAllWithCode.setSecLevel(0);
//                    WorkflowService service = new WorkflowService();
//                    int count = service.getWorkflowServiceHttpPort().getAllWorkflowRequestCount(userid, arrayOfString);
//
//                }
//            }
////            ArrayList<RequestStatusObj> donedatas = dobj.getDonedatas();
////            if(donedatas.size()>0){//处理推送的已办数据
////                JSONArray todolist = new JSONArray();
////                for(RequestStatusObj rso : donedatas){//遍历当前发送的已办数据
////                    JSONObject rsojson = new JSONObject();
////                    requestname = rso.getRequstname() ;
////                    rsojson.put("cid",rso.getCid());
////                    rsojson.put("username",rso.getUser().getLastname()+"("+rso.getUser().getUID()+")");
////                    rsojson.put("isremark",rso.getIsremark());
////                    rsojson.put("viewtype",rso.getViewtype());
////                    rsojson.put("receivedatetime",rso.getReceivedate()+" "+rso.getReceivetime()+"/"+rso.getOperatedate()+" "+rso.getOperatetime());
////                    rsojson.put("nodename",rso.getNodename());
////                    rsojson.put("iscomplete",rso.getIscomplete());
////                    todolist.add(rsojson);
////                }
////                donejson.put("DoneCount",donedatas.size());
////                donejson.put("DoneDatas",todolist);
////                jsonArray.add(donejson);
////            }
////            ArrayList<RequestStatusObj> deldatas = dobj.getDeldatas();
////            if(deldatas.size()>0){//处理推送的删除数据
////                JSONArray todolist = new JSONArray();
////                for(RequestStatusObj rso : deldatas){//遍历当前发送的删除数据
////                    requestname = rso.getRequstname() ;
////                    JSONObject rsojson = new JSONObject();
////                    rsojson.put("cid",rso.getCid());
////                    rsojson.put("username",rso.getUser().getLastname()+"("+rso.getUser().getUID()+")");
////                    todolist.add(rsojson);
////                }
////                deljson.put("DelCount",deldatas.size());
////                deljson.put("DelDatas",todolist);
////                jsonArray.add(deljson);
////            }
////
////            mainboject.put("syscode",syscode);
////            mainboject.put("requestid",dobj.getRequestid());
////            mainboject.put("requestname",requestname);
////            mainboject.put("sendtimestamp",dobj.getSendtimestamp());
////            mainboject.put("RequestDatas",jsonArray);
////
////            //输入内容信息到日志文件中 /log/ecology
////            log.error("统一待办的JSON数据为：\n"+formatJson(mainboject.toString()));
//        }
//
//    }
//
//    /**
//     * 格式化JSON格式输出
//     *
//     * @param jsonStr
//     * @return 返回指定格式化的数据
//     */
//    public static String formatJson(String jsonStr) {
//        if (null == jsonStr || "".equals(jsonStr))
//            return "";
//        StringBuilder sb = new StringBuilder();
//        char last = '\0';
//        char current = '\0';
//        int indent = 0;
//        boolean isInQuotationMarks = false;
//        for (int i = 0; i < jsonStr.length(); i++) {
//            last = current;
//            current = jsonStr.charAt(i);
//            switch (current) {
//                case '"':
//                    if (last != '\\'){
//                        isInQuotationMarks = !isInQuotationMarks;
//                    }
//                    sb.append(current);
//                    break;
//                case '{':
//                case '[':
//                    sb.append(current);
//                    if (!isInQuotationMarks) {
//                        sb.append('\n');
//                        indent++;
//                        addIndentBlank(sb, indent);
//                    }
//                    break;
//                case '}':
//                case ']':
//
//                    if (!isInQuotationMarks) {
//                        sb.append('\n');
//                        indent--;
//                        addIndentBlank(sb, indent);
//                    }
//                    sb.append(current);
//                    break;
//                case ',':
//                    sb.append(current);
//                    break;
//                default:
//                    sb.append(current);
//            }
//        }
//
//        return sb.toString();
//    }
//
//    /**
//     * 添加space(缩进)
//     * @param sb
//     * @param indent
//     */
//    private static void addIndentBlank(StringBuilder sb, int indent) {
//        for (int i = 0; i < indent; i++) {
//            sb.append('\t');
//        }
//    }
//
//
//
//}