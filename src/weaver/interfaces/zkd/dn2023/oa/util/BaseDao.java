package weaver.interfaces.zkd.dn2023.oa.util;

import com.alibaba.fastjson.JSON;
import com.api.doc.detail.service.DocSaveService;
import com.engine.common.util.ServiceUtil;
import com.engine.workflow.service.HtmlToPdfService;
import com.engine.workflow.service.impl.HtmlToPdfServiceImpl;
import weaver.conn.RecordSet;
import weaver.file.FileUpload;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.action.WorkflowToDoc;
import weaver.interfaces.zkd.dn2023.oa.entity.OADocEntity;
import weaver.interfaces.zkd.dn2023.oa.entity.OAHumanMesEntity;
import weaver.interfaces.zkd.dn2023.oa.entity.OAWorkFlowRequestLogEntity;
import weaver.system.SystemComInfo;
import weaver.workflow.workflow.WorkflowConfigComInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @BelongsProject: ZKD_DA_2023
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.oa.util
 * @Author: hf_zhangyuan
 * @CreateTime: 2023-03-08  22:25
 * @Description: TODO
 * @Version: 1.0
 */

public class BaseDao {
    RecordSet rs = new RecordSet();
    /**
     * 根据人员id获取到人员姓名，支持多人员id查询
     * @param humanId
     * @return
     * @throws Exception
     */
    public String getHumanNameById(String humanId) {
        String humanName="";
        if(humanId!=null&&!"".equals(humanId)){
            String sql="select listagg(t.lastname,',') within group(order by t.id) lastname from Hrmresource t where id in("+humanId+")";


                rs.execute(sql);
                if (rs.next()) {
                    humanName=Util.null2String(rs.getString("lastname"));
                }

        }
        return humanName;
    }





    /**
     * 根据docid获取到正文id和正文名称
     * @param docId
     * @return
     * @throws Exception
     */
    public List<OADocEntity> getImageFileId(String docId) {
        List<OADocEntity> list=new ArrayList<>();
        String sql="select a.imagefileid,b.imagefilename,b.filerealpath,b.filesize from Docimagefile a left join imagefile b on a.imagefileid=b.imagefileid where docid in("+docId+")";


            rs.execute(sql);
            while(rs.next()) {
                OADocEntity docEntity=new OADocEntity();
                docEntity.setImageFileId(Util.null2String(rs.getString("imagefileid")));
                docEntity.setImageFileName(Util.null2String(rs.getString("imagefilename")));
                docEntity.setFileRealPath(Util.null2String(rs.getString("filerealpath")));
                docEntity.setFileSize(Util.null2String(rs.getString("filesize")));
                list.add(docEntity);
            }
        return list;
    }
    /**
     * 根据人员id  获取部门id    人员id可以是多个以逗号分隔
     * @param humId
     * @return
     * @throws Exception
     */
    public String getDepartmentIdByHumId(String humId)  {
        String departmentId="";
        if(humId!=null&&!"".equals(humId)){
            String sql="select listagg(t.departmentid,',') within group(order by t.id) departmentid from Hrmresource t where id in("+humId+")";


                rs.execute(sql);
                if (rs.next()) {
                    departmentId=Util.null2String(rs.getString("departmentid"));
                }

        }
        return departmentId;
    }
    /**
     * 根据请求id获取流程创建人
     * @param requestId
     * @return
     * @throws Exception
     */
    public String getCreatePersonId(String requestId){
        String createPersonId="";

        String sql="select creater from workflow_requestbase where requestid="+requestId;

            rs.execute(sql);
            if (rs.next()) {
                createPersonId=Util.null2String(rs.getString("creater"));
            }

        return createPersonId;
    }
    public  List<OADocEntity> getZWEntityPdffj(String docid)  {
        System.out.println("办理单id:"+docid);
        List<OADocEntity> list = new ArrayList<>();
        if ("".equals(docid)){
            return list;
        }
        String sql = "select imagefileid,imagefilename,docfiletype,filerealpath,filesize from (select imagefileid,imagefilename,docfiletype,filerealpath,filesize from (select a.imagefileid,b.imagefilename,a.docfiletype,b.filerealpath,b.filesize from Docimagefile a left join imagefile b on a.imagefileid=b.imagefileid where docid in(" + docid + ")) order by imagefileid desc) where docfiletype != 12";
        rs.execute(sql);
        while (rs.next()) {
            OADocEntity docEntity = new OADocEntity();
            docEntity.setImageFileId(Util.null2String(rs.getString("imagefileid")));
            docEntity.setImageFileName(Util.null2String(rs.getString("imagefilename")));
            docEntity.setFileRealPath(Util.null2String(rs.getString("filerealpath")));
            docEntity.setFileSize(Util.null2String(rs.getString("filesize")));
            docEntity.setDocfiletype(Util.null2String(rs.getString("docfiletype")));
            list.add(docEntity);

        }
        return list;
    }
    public OADocEntity getZWEntityPdf(String docid)  {
        if ("".equals(docid)){
            return null;
        }
        String sql = "select imagefileid,imagefilename,docfiletype,filerealpath,filesize from (select imagefileid,imagefilename,docfiletype,filerealpath,filesize from (select a.imagefileid,b.imagefilename,a.docfiletype,b.filerealpath,b.filesize from Docimagefile a left join imagefile b on a.imagefileid=b.imagefileid where docid in(" + docid + ")) order by imagefileid desc) where docfiletype = 12";
        OADocEntity docEntity = null;
        rs.execute(sql);
        if (rs.next()) {
            docEntity = new OADocEntity();
            docEntity.setImageFileId(Util.null2String(rs.getString("imagefileid")));
            docEntity.setImageFileName(Util.null2String(rs.getString("imagefilename")));
            docEntity.setFileRealPath(Util.null2String(rs.getString("filerealpath")));
            docEntity.setFileSize(Util.null2String(rs.getString("filesize")));
            docEntity.setDocfiletype(Util.null2String(rs.getString("docfiletype")));

        }
        return docEntity;
    }
    /**
     * 根据正文字段id获取正文文档imagefileid大小前2的文档信息内容
     *
     * @param docid
     * @return
     * @
     */
    public List<OADocEntity> getZWEntity(String docid)  {
        List<OADocEntity> list = new ArrayList<>();
        String sql = "select imagefileid,imagefilename,docfiletype,filerealpath,filesize from (select imagefileid,imagefilename,docfiletype,filerealpath,filesize from (select a.imagefileid,b.imagefilename,a.docfiletype,b.filerealpath,b.filesize from Docimagefile a left join imagefile b on a.imagefileid=b.imagefileid where docid in(" + docid + ")) order by imagefileid desc) where docfiletype !=2 and rownum<=2";
        rs.execute(sql);
        while (rs.next()) {
            OADocEntity docEntity = new OADocEntity();
            docEntity.setImageFileId(Util.null2String(rs.getString("imagefileid")));
            docEntity.setImageFileName(Util.null2String(rs.getString("imagefilename")));
            docEntity.setFileRealPath(Util.null2String(rs.getString("filerealpath")));
            docEntity.setFileSize(Util.null2String(rs.getString("filesize")));
            docEntity.setDocfiletype(Util.null2String(rs.getString("docfiletype")));
            list.add(docEntity);
        }
        return list;
    }
    /**
     * 根据标识分隔时间字符串
     * @param originString
     * @param symbol
     * @return
     */
    public static String getSplited(String originString,String symbol){
        StringBuffer sb=new StringBuffer();
        String[] strArr=originString.split(symbol);
        for(String string:strArr){
            sb.append(string);
        }
        return sb.toString();
    }
    /**
     * 根据客户id获取客户的名称
     *
     * @param customerId
     * @return
     * @
     */
    public String getCustomerName(String customerId)  {
        String name = "";
        String sql = "select name from crm_customerinfo where id=" + customerId;
        rs.execute(sql);
        if (rs.next()) {
            name = Util.null2String(rs.getString("name"));
        }
        return name;
    }
    /**
     * 根据请求id和流程id获取流程流转情况字段数据
     * @param requestid
     * @return
     * @throws Exception
     */
    public List<OAWorkFlowRequestLogEntity> getWorkFlowRequestLogMes(String requestid)  {
        List<OAWorkFlowRequestLogEntity> list=new ArrayList<>();
        String sql="select requestid,operatedate,operatetime,operator,logtype,remark,operatortype from workflow_requestlog where requestid="+requestid;
        System.out.println(sql);
        rs.execute(sql);
        while(rs.next()) {
            OAWorkFlowRequestLogEntity oaWorkFlowRequestLogEntity=new OAWorkFlowRequestLogEntity();
            oaWorkFlowRequestLogEntity.setRequestId(Util.null2String(rs.getString("requestid")));
            oaWorkFlowRequestLogEntity.setOperateDate(Util.null2String(rs.getString("operatedate")));
            oaWorkFlowRequestLogEntity.setOperateTime(Util.null2String(rs.getString("operatetime")));
            oaWorkFlowRequestLogEntity.setOperator(Util.null2String(rs.getString("operator")));
            oaWorkFlowRequestLogEntity.setLogType(Util.null2String(rs.getString("logtype")));
            oaWorkFlowRequestLogEntity.setRemark(Util.null2String(rs.getString("remark")));
            oaWorkFlowRequestLogEntity.setOperateType(Util.null2String(rs.getString("operatortype")));
            list.add(oaWorkFlowRequestLogEntity);
        }
        return list;
    }
    public String getGDDate(String requestId)  {
        String date=null;
        String sql="select lastoperatedate from workflow_requestbase where requestId='"+requestId+"'";
        rs.execute(sql);
        if (rs.next()) {
            date=Util.null2String(rs.getString("lastoperatedate"));
        }

        return date;
    }
    /**
     * 根据人员id获取人员信息
     *
     * @param id
     * @return
     * @
     */
    public OAHumanMesEntity getHumanMes(String id) {
        OAHumanMesEntity humanMesEntity = new OAHumanMesEntity();
        String sql = "select lastname,departmentid from Hrmresource where id='" + id + "'";
        rs.execute(sql);
        rs.next();
        humanMesEntity.setName(Util.null2String(rs.getString("lastname")));
        humanMesEntity.setDepartmentId(Util.null2String(rs.getString("departmentid")));
        return humanMesEntity;
    }
    /**
     * 根据id查询部门名称 多部门浏览按钮
     *
     * @param id
     * @return 部门名称
     * @
     */
    public String getInstitutionById(String id) {
        String institutionName = "";
        if (id != null && !"".equals(id)) {

            String sql = "select listagg(t.departmentname,',') within group(order by t.id) departmentname from HrmDepartment t where id in(" + id + ")";
            rs.execute(sql);
            rs.next();
            institutionName = Util.null2String(rs.getString("departmentname"));
        }
        return institutionName;
    }

    /**
     * 根据请求id，流程id和套红节点id获取套红节点操作者的operatedate
     * @param requestId
     * @param nodeid 266
     * @return
     */
    public String getNodeOperateDate(String requestId,String nodeid){
        String date=null;
        String sql="select operatedate from workflow_requestlog where nodeid='"+nodeid+"' and logtype='0' and requestId='"+requestId+"'";
        rs.execute(sql);
        if (rs.next()) {
            date=Util.null2String(rs.getString("operatedate"));
        }

        return date;
    }
    /**
     * 根据字段id和选择框值获取选择框名称
     *
     * @param fieldId
     * @param selectValue
     * @return 选择框名称
     * @
     */
    public String getSelectName(String fieldId, String selectValue) {
        String selectName = "";
        if (selectValue != null && !"".equals(selectValue)) {

            String sql = "select selectname from  Workflow_Selectitem where fieldid=" + fieldId + " and selectvalue=" + selectValue;
            rs.execute(sql);
            rs.next();
            selectName = Util.null2String(rs.getString("selectname"));
        }
        return selectName;
    }
    /**
     * 根据请求id获取请求标题
     *
     * @param requestId
     * @return 请求标题
     * @
     */
    public String getRequestName(String requestId) {
        String requestName = "";
        String sql = "select requestname from workflow_requestbase where requestid=" + requestId;
        rs.execute(sql);
        rs.next();
        requestName = Util.null2String(rs.getString("requestname"));
        return requestName;
    }

    public List<String> getFilingRequestId(String tableName)  {
        List<String> list = new ArrayList<>();
        String sql = "select a.requestid from workflow_requestbase a right join " + tableName + " b on a.requestid=b.requestid where b.isinsert is null and a.currentnodetype = 3 and a.status != '强制归档' and rownum<=1000";
        rs.execute(sql);
        while (rs.next()) {
            list.add(rs.getString("requestid"));
        }
        return list;
    }

    public void updateBs(String requestid, String tablename) {
        String sql = "update " + tablename + " set isinsert = 1 where requestid = " + requestid;
        rs.executeUpdate(sql);
    }
    /**
     * @description:更新传输状态
     * @author: hf_zhangyuan
     * @date: 2023/3/31 10:58
     * @param: 
     * @return: 
     **/
    public void updateData(String requestid , String sjzt,String sbyy){

        SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hhmmss = new SimpleDateFormat("HH:mm");
        String sql = "update uf_datsjl set tsrq = ? , tssj = ? , sjzt = ?  , sbyy = ? where lc = ? ";

        rs.executeUpdate(sql,yyyymmdd.format(new Date()),hhmmss.format(new Date()),sjzt,sbyy,requestid);
        

    }
    /**
     * @description:写入建模数据
     * @author: hf_zhangyuan
     * @date: 2023/3/9 17:06
     * @param:流程id,办理单名称
     * @return:
     **/
    public boolean insertData(String requestid, String tablename,String modeid,String type) {
        String ssql = "select currentnodeid from workflow_requestbase where requestid = " + requestid;
        rs.execute(ssql);
        String nodeid = "";
        if (rs.next()) {
            nodeid = rs.getString("currentnodeid");
        }
        int pdfdocid = setWorkFlowPdf(requestid, nodeid,type);
        if (pdfdocid == 0){
            return false;
        }
        ModeRightInfo modeRightInfo = new ModeRightInfo();
        Date date = new Date();
        SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hhmmss = new SimpleDateFormat("hh:mm:ss");
        String uuid = UUID.randomUUID().toString();
        String sql = "insert into uf_datsjl (FORMMODEID,MODEDATACREATER,MODEDATACREATERTYPE,MODEDATACREATEDATE,MODEDATACREATETIME,MODEUUID,lc,jsrq,sjzt,lcym,bm) " +
                "values (?,1,0,?,?,?,?,?,4,?,?)";
        boolean ist = rs.executeUpdate(sql, modeid,yyyymmdd.format(date), hhmmss.format(date), uuid, requestid, yyyymmdd.format(date), pdfdocid + "", tablename);
        sql = "select id from uf_datsjl where MODEUUID = '" + uuid + "' ";
        rs.execute(sql);
        if (rs.next()) {
            modeRightInfo.setNewRight(true);
            int billid = rs.getInt("id");
            modeRightInfo.editModeDataShare(1, Util.getIntValue(modeid), billid);
        }
        return ist;
    }

    /**
     * 根据requestid获取表名
     *
     * @param requestid
     * @return
     * @throws Exception
     */
    public String getTableNameByRequestId(String requestid) throws Exception {
        String tableName = "";
        StringBuffer tablesql = new StringBuffer("select tablename from workflow_bill where id in (select formid from workflow_base where id in (select workflowid from workflow_requestbase where requestid='")
                .append(requestid).append("'))");
        try {
            rs.execute(tablesql.toString());
            if (rs.next()) {
                tableName = Util.null2String(rs.getString("tablename"));
            }
            return tableName;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private int setWorkFlowPdf(String requestid, String nodeid,String type) {
        int docid = 0;
        String modeid = "";
        String sql = "select id from workflow_nodehtmllayout where type = "+type+" and nodeid = " + nodeid;
        rs.execute(sql);
        if (rs.next()) {
            modeid = rs.getString("id");

        }
        int userid = 1;
        sql = "select creater from workflow_requestbase where requestid = " + requestid;
        rs.execute(sql);
        if (rs.next()) {
            userid = Util.getIntValue(rs.getString("creater"),1);

        }


        String temppath = getFileSavePath();
        Map<String, Object> params = new HashMap<>();
        WorkflowConfigComInfo workflowConfigComInfo = new WorkflowConfigComInfo();
        String useWk = workflowConfigComInfo.getValue("htmltopdf_usewk");
        params.put("useWk", useWk);    //是否使用wkhtmltopdf插件转pdf 1：是  0：否  不传则默认使用Itext插件
        params.put("requestid", requestid);    //必传
        params.put("modeid", modeid);    //模板id(传模板id则根据模板生成.不传则默认使用显示模板)
        params.put("path", temppath);  //存储路径(不传则windows默认D:/testpdf;linux默认/usr/testpdf)/usr/weaver/pdffile
        params.put("onlyHtml", "0");    //0:转pdf  1:只转成html  2:转html和pdf  (不传则默认=0)
        params.put("keepsign", "1");   //1:保留底部签字意见 0：不保留 (不传则默认=1)
        params.put("pageSize", "100"); //底部签字意见最大显示数量  (默认=100)
        params.put("isTest", "1");    //外部调用必传isTest=1
        params.put("limitauth", "0"); //不校验权限
        HtmlToPdfService htmlToPdfService = ServiceUtil.getService(HtmlToPdfServiceImpl.class, getUser(userid));
        Map<String, Object> pathMap = new HashMap<>();
        try{
           pathMap = htmlToPdfService.getFormDatas(params);
        }catch (Exception e){
            System.out.println("转pdf失败:"+ JSON.toJSONString(params));
            return 0;
        }

        WorkflowToDoc workflowToDoc = new WorkflowToDoc();

        String path = (String) pathMap.get("path");
        String filename = (String) pathMap.get("filename");
        File f = new File(path+filename);
        long size = 0l;
        if (f.exists()) {
            size = f.length();
        }
        int fileid = workflowToDoc.saveImageFile("办理单", path+filename, size, true);
        DocSaveService saveService = new DocSaveService();
        try {
            docid = saveService.accForDoc(1, fileid, getUser(1));
            //System.out.println(docid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return docid;
    }
    public String getFileSavePath() {
        SystemComInfo syscominfo = new SystemComInfo();
        String createdir = FileUpload.getCreateDir(syscominfo.getFilesystem());
        return createdir;
    }
    public int getsecid(String allwfdocpath) {
        String wfdocpath[] = Util.TokenizerString2(allwfdocpath, ",");
        int maincategory = 0;
        int subcategory = 0;
        int seccategory = 0;
        if (wfdocpath.length != 3) {
            return -1;
        } else {

            for (int i = 0; i < wfdocpath.length; i++) {
                if (Util.null2String(wfdocpath[i]).equals("")
                        || Util.null2String(wfdocpath[i]).equals("0")) {
                    return -1;
                }
            }
            maincategory = Util.getIntValue(wfdocpath[0], 0);
            subcategory = Util.getIntValue(wfdocpath[1], 0);
            seccategory = Util.getIntValue(wfdocpath[2], 0);
        }
        return seccategory;
    }

    public static User getUser(int userid) {
        User user = new User();
        try {
            ResourceComInfo rc = new ResourceComInfo();
            DepartmentComInfo dc = new DepartmentComInfo();

            user.setUid(userid);
            user.setLoginid(rc.getLoginID("" + userid));
            user.setFirstname(rc.getFirstname("" + userid));
            user.setLastname(rc.getLastname("" + userid));
            user.setLogintype("1");

            user.setSex(rc.getSexs("" + userid));
            user.setLanguage(7);

            user.setEmail(rc.getEmail("" + userid));

            user.setLocationid(rc.getLocationid("" + userid));
            user.setResourcetype(rc.getResourcetype("" + userid));

            user.setJobtitle(rc.getJobTitle("" + userid));

            user.setJoblevel(rc.getJoblevel("" + userid));
            user.setSeclevel(rc.getSeclevel("" + userid));
            user.setUserDepartment(Util.getIntValue(rc.getDepartmentID("" + userid), 0));
            user.setUserSubCompany1(Util.getIntValue(dc.getSubcompanyid1(user.getUserDepartment() + ""), 0));

            user.setManagerid(rc.getManagerID("" + userid));
            user.setAssistantid(rc.getAssistantID("" + userid));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
