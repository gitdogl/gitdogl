//package com.weaver.esb.package_20231225055750;
//
//import cn.hutool.core.io.FileUtil;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.api.doc.detail.service.DocSaveService;
//import com.sap.mw.jco.JCO;
//import com.wbi.util.Util;
//import org.apache.commons.lang3.StringEscapeUtils;
//import org.jetbrains.annotations.NotNull;
//import weaver.conn.RecordSet;
//import weaver.formmode.virtualform.UUIDPKVFormDataSave;
//import weaver.hrm.User;
//import weaver.file.ImageFileManager;
//
//import java.io.*;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//import static cn.hutool.core.io.IoUtil.readBytes;
//import static org.apache.commons.io.IOUtils.toByteArray;
//
//public class class_20231225055750 {
//    static DocSaveService docSaveService = new DocSaveService();
//    /**
//     * @param: param(Map collections)
//     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
//     */
//    public Map execute(Map<String, Object> params) throws IOException {
//        String data = Util.null2String(params.get("data"));
//        Map<String, Object> ret = new HashMap<>();
//        JSONObject jsonObject = JSONObject.parseObject(data);
//
//        JSONObject ctrlobj = jsonObject.getJSONObject("CTRL");
//        String MSGTY = ctrlobj.getString("MSGTY");
//        String MSAGE = ctrlobj.getString("MSAGE");
////        if("S".equals(MSGTY)){
//        JSONObject DATA = jsonObject.getJSONObject("DATA");
//
//        //人员基本信息表
//        JSONArray oT_P0001 = DATA.getJSONArray("OT_P0001");
//        if(oT_P0001!=null){
//            if(oT_P0001.size()>0){
//                for (int i = 0; i < oT_P0001.size(); i++) {
//                    JSONObject obj1 = oT_P0001.getJSONObject(i);
//                    String PERNR = obj1.getString("PERNR");
//                    String ZZHR_GWLW = obj1.getString("ZZHR_GWLW");
//                    String PLSTX = obj1.getString("PLSTX");
//                    String ZZHR_GGJT = obj1.getString("ZZHR_GGJT");
//                    String ZZHR_GWTW = obj1.getString("ZZHR_GWTW");
//                    String PHONE = obj1.getString("PHONE");
//                    String EMAIL = obj1.getString("EMAIL");
//                    String PHONE_F = obj1.getString("PHONE_F");
//                    String GBDAT = convertDate(obj1.getString("GBDAT"));
//                    String GESCH = obj1.getString("GESCH");
//                    if("男".equals(GESCH)){
//                        GESCH="0";
//                    }else if("女".equals(GESCH)){
//                        GESCH="1";
//                    }
//                    String LTEXT = obj1.getString("LTEXT");
//                    String ZZHR_JIGN = obj1.getString("ZZHR_JIGN");
//                    String ZZHR_HJSZ = obj1.getString("ZZHR_HJSZ");
//                    String PTEXT = obj1.getString("PTEXT");
//                    String ZZHR_XULI = obj1.getString("ZZHR_XULI");
//                    String ZZHR_XUWE = obj1.getString("ZZHR_XUWE");
//                    String ADRESS = obj1.getString("ADRESS");
//                    String ZZHR_ZYTC = obj1.getString("ZZHR_ZYTC");
//                    String RSTXT = obj1.getString("RSTXT");
//                    String FWDAT = convertDate(obj1.getString("FWDAT"));
//                    String RWDAT = convertDate(obj1.getString("RWDAT"));
//                    String HWDAT = convertDate(obj1.getString("HWDAT"));
//
//
//                    executeDefiUpdateSQL("field40", "0", PERNR, "-1");
//
//                    executeDefiUpdateSQL("field42", ZZHR_GWLW, PERNR, "-1");
//                    executeDefiUpdateSQL("field37", PLSTX, PERNR, "-1");
//                    executeDefiUpdateSQL("field38", ZZHR_GGJT, PERNR, "-1");
//                    executeDefiUpdateSQL("field39", ZZHR_GWTW, PERNR, "-1");
//
//                    executeHrmUpdateSQL("mobile", PHONE, PERNR);
//                    executeHrmUpdateSQL("email", EMAIL, PERNR);
//                    executeHrmUpdateSQL("homeaddress", PHONE_F, PERNR);
//                    ret.put("birthday",GBDAT);
//                    executeHrmUpdateSQL("birthday", GBDAT, PERNR);
//                    executeHrmUpdateSQL("sex", GESCH, PERNR);
//                    executeHrmUpdateSQL("folk", LTEXT, PERNR);
//                    executeHrmUpdateSQL("nativeplace", ZZHR_JIGN, PERNR);
//                    executeHrmUpdateSQL("regresidentplace", ZZHR_HJSZ, PERNR);
//                    executeHrmUpdateSQL("policy", PTEXT, PERNR);
//
//                    executeDefiUpdateSQL("field4", ZZHR_XULI, PERNR, "1");
//                    executeDefiUpdateSQL("field39", ZZHR_XULI, PERNR, "1");
//
//                    executeHrmUpdateSQL("degree", ZZHR_XUWE, PERNR);
//                    executeHrmUpdateSQL("residentplace", ADRESS, PERNR);
//                    executeDefiUpdateSQL("field37", ZZHR_ZYTC, PERNR, "1");
//                    executeDefiUpdateSQL("field6", RSTXT, PERNR, "1");
//                    executeDefiUpdateSQL("field38", RSTXT, PERNR, "1");
//                    executeHrmUpdateSQL("workstartdate", FWDAT, PERNR);
//                    executeHrmUpdateSQL("companystartdate", RWDAT, PERNR);
//                    executeHrmUpdateSQL("startdate", HWDAT, PERNR);
//                }
//            }
//        }
//
//
//        //人员照片
//        JSONArray oT_PHOTO = DATA.getJSONArray("OT_PHOTO");
//        if(oT_PHOTO!=null){
//            if(oT_PHOTO.size()>0) {
//                for (int i = 0; i < oT_PHOTO.size(); i++) {
//                    JSONObject obj2 = oT_PHOTO.getJSONObject(i);
//                    String PERNR = obj2.getString("PERNR");
//                    String ryid = getHrmidbyWorkcode(PERNR);
//                    String LINE = obj2.getString("LINE");
//                    String out="";
//                    if(!"".equals(LINE)){
//                        out="/app/weaver/ecology/file/"+PERNR+"的头像.jpg";
//                        save(LINE.toUpperCase(), out);
//                    }
//                    int imagefileid = 0;
//                    if(!"".equals(ryid)){
//                        imagefileid=saveImageFile(out, PERNR + "的头像.jpg", Integer.parseInt(ryid));
//                        String filerealpath = getRealPath(imagefileid + "");
//                        ret.put("imagefileid",imagefileid);
//                        ret.put("filerealpath",filerealpath);
//                        executeHrmUpdateSQL("resourceimageid", imagefileid + "", PERNR);
//                        executeHrmUpdateSQL("messagerurl", filerealpath, PERNR);
//                    }
//                    //
//                    File directory = new File(out);
//                    if (directory.exists()) {
//                        deleteDirectory(directory);
//                    }
//                }
//            }
//        }
//
//
//        //教育背景明细
//        JSONArray oT_P9103 = DATA.getJSONArray("OT_P9103");
//        if(oT_P9103!=null){
//            if(oT_P9103.size()>0) {
//                for (int i = 0; i < oT_P9103.size(); i++) {
//                    JSONObject obj3 = oT_P9103.getJSONObject(i);
//                    String PERNR = obj3.getString("PERNR");
//                    String ATEXT = obj3.getString("ATEXT");
//                    String ZZHR_XYMC = obj3.getString("ZZHR_XYMC");
//                    String ZZHR_ZYMC = obj3.getString("ZZHR_ZYMC");
//                    String ZZHR_XUWW = obj3.getString("ZZHR_XUWW");
//                    String name = getHrmNameByWorkcode(PERNR);
//                    Object[] values = new Object[6];
//                    values[0]=PERNR;
//                    values[1]=ATEXT;
//                    values[2]=ZZHR_XYMC;
//                    values[3]=ZZHR_ZYMC;
//                    values[4]=ZZHR_XUWW;
//                    values[5]=name;
//                    executeInsertSQL("uf_jybjmx","gh,jylx,yxmc,zymc,xw,xm",values,"40822",name);
//                }
//            }
//        }
//
//
//        //任职明细
//        JSONArray oT_P9023 = DATA.getJSONArray("OT_P9023");
//        if(oT_P9023!=null){
//            if(oT_P9023.size()>0) {
//                for (int i = 0; i < oT_P9023.size(); i++) {
//                    JSONObject obj4 = oT_P9023.getJSONObject(i);
//                    String PERNR = obj4.getString("PERNR");
//                    String ZZHR_RZSP = convertDate(obj4.getString("ZZHR_RZSP"));
//                    String ZZHR_RZGW = obj4.getString("ZZHR_RZGW");
//                    String ZZHR_RZDW = obj4.getString("ZZHR_RZDW");
//                    String ZZHR_XIYY = obj4.getString("ZZHR_XIYY");
//                    String ZZHR_MZSJ = convertDate(obj4.getString("ZZHR_MZSJ"));
//                    String ZZHR_MZGW = obj4.getString("ZZHR_MZGW");
//                    String ZZHR_MZDW = obj4.getString("ZZHR_MZDW");
//                    String ZZHR_MXYY = obj4.getString("ZZHR_MXYY");
//                    String name = getHrmNameByWorkcode(PERNR);
//                    Object[] values = new Object[10];
//                    values[0]=PERNR;
//                    values[1]=ZZHR_RZSP;
//                    values[2]=ZZHR_RZGW;
//                    values[3]=ZZHR_RZDW;
//                    values[4]=ZZHR_XIYY;
//                    values[5]=ZZHR_MZSJ;
//                    values[6]=ZZHR_MZGW;
//                    values[7]=ZZHR_MZDW;
//                    values[8]=ZZHR_MXYY;
//                    values[9]=name;
//                    executeInsertSQL("uf_rzmx","gh,rzspsj,rzgw,rzdw,rzxxyy,mzsj,mzgw,mzdw,mzxxxx,xm",values,"40823",name);
//                }
//            }
//        }
//
//
//        //专业技术职称聘任信息明细
//        JSONArray oT_P9137 = DATA.getJSONArray("OT_P9137");
//        if(oT_P9137!=null){
//            if(oT_P9137.size()>0) {
//                for (int i = 0; i < oT_P9137.size(); i++) {
//                    JSONObject obj5 = oT_P9137.getJSONObject(i);
//                    String PERNR = obj5.getString("PERNR");
//                    String TLINE = obj5.getString("TLINE");
//                    String ZZHR_PRGW = obj5.getString("ZZHR_PRGW");
//                    String ZZHR_PRDW = obj5.getString("ZZHR_PRDW");
//                    String name = getHrmNameByWorkcode(PERNR);
//                    Object[] values = new Object[5];
//                    values[0]=PERNR;
//                    values[1]=TLINE;
//                    values[2]=ZZHR_PRGW;
//                    values[3]=ZZHR_PRDW;
//                    values[4]=name;
//                    executeInsertSQL("uf_zyjszc","gh,przyjszwmc,przyjszw,prdw,xm",values,"40824",name);
//                }
//            }
//        }
//
//
//        //从业、执业、职业资格信息明细
//        JSONArray oT_P9109 = DATA.getJSONArray("OT_P9109");
//        if (oT_P9109!=null){
//            if(oT_P9109.size()>0) {
//                for (int i = 0; i < oT_P9109.size(); i++) {
//                    JSONObject obj6 = oT_P9109.getJSONObject(i);
//                    String PERNR = obj6.getString("PERNR");
//                    String ZZHR_ZGMW = obj6.getString("ZZHR_ZGMW");
//                    String ZZHR_ZSMC = obj6.getString("ZZHR_ZSMC");
//                    String name = getHrmNameByWorkcode(PERNR);
//                    Object[] values = new Object[4];
//                    values[0]=PERNR;
//                    values[1]=ZZHR_ZGMW;
//                    values[2]=ZZHR_ZSMC;
//                    values[3]=name;
//                    executeInsertSQL("uf_czyzg","gh,zgmc,zsmc,xm",values,"40825",name);
//                }
//            }
//        }
//
//
//        //奖励明细
//        JSONArray oT_P9107 = DATA.getJSONArray("OT_P9107");
//        if(oT_P9107!=null){
//            if(oT_P9107.size()>0) {
//                for (int i = 0; i < oT_P9107.size(); i++) {
//                    JSONObject obj7 = oT_P9107.getJSONObject(i);
//                    String PERNR = obj7.getString("PERNR");
//                    String BEGDA = convertDate(obj7.getString("BEGDA"));
//                    ret.put("BEGDA",BEGDA);
//                    String ZZHR_JLJW = obj7.getString("ZZHR_JLJW");
//                    String ZZHR_JLLW = obj7.getString("ZZHR_JLLW");
//                    String ZZHR_JLLT = obj7.getString("ZZHR_JLLT");
//                    String ZZHR_JLMC = obj7.getString("ZZHR_JLMC");
//                    String ZZHR_JLDW = obj7.getString("ZZHR_JLDW");
//                    String name = getHrmNameByWorkcode(PERNR);
//                    Object[] values = new Object[8];
//                    values[0]=PERNR;
//                    values[1]=BEGDA;
//                    values[2]=ZZHR_JLJW;
//                    values[3]=ZZHR_JLLW;
//                    values[4]=ZZHR_JLLT;
//                    values[5]=ZZHR_JLMC;
//                    values[6]=ZZHR_JLDW;
//                    values[7]=name;
//                    executeInsertSQL("uf_jlmx","gh,hjrq,jljb,jllbd,jllbx,jlmc,jlpzdw,xm",values,"40826",name);
//                }
//            }
//        }
//
//
//        //处罚明细
//        JSONArray OT_P9108 = DATA.getJSONArray("OT_P9107");
//        if(OT_P9108!=null){
//            if(OT_P9108.size()>0) {
//                for (int i = 0; i < OT_P9108.size(); i++) {
//                    JSONObject obj8 = oT_P9107.getJSONObject(i);
//                    String PERNR = obj8.getString("PERNR");
//                    String BEGDA = convertDate(obj8.getString("BEGDA"));
//                    String ZZHR_CFYY = obj8.getString("ZZHR_CFYY");
//                    String ZZHR_CFNR = obj8.getString("ZZHR_CFNR");
//                    String ZZHR_CFDW = obj8.getString("ZZHR_CFDW");
//                    String name = getHrmNameByWorkcode(PERNR);
//                    Object[] values = new Object[6];
//                    values[0]=PERNR;
//                    values[1]=BEGDA;
//                    values[2]=ZZHR_CFYY;
//                    values[3]=ZZHR_CFNR;
//                    values[4]=ZZHR_CFDW;
//                    values[5]=name;
//                    executeInsertSQL("uf_cfmx","gh,cfrq,cfyy,cfnr,cfpzdw,xm",values,"40827",name);
//                }
//            }
//        }
//
//
//        //绩效考核明细
//        JSONArray OT_P9120 = DATA.getJSONArray("OT_P9120");
//        if(OT_P9120!=null){
//            if(OT_P9120.size()>0) {
//                for (int i = 0; i < OT_P9120.size(); i++) {
//                    JSONObject obj9 = OT_P9120.getJSONObject(i);
//                    String PERNR = obj9.getString("PERNR");
//                    String BEGDA = convertDate(obj9.getString("BEGDA"));
//                    String ZZHR_KHLW = obj9.getString("ZZHR_KHLW");
//                    String ZZHR_KHDF = obj9.getString("ZZHR_KHDF");
//                    String ZZHR_KHJG = obj9.getString("ZZHR_KHJG");
//                    String name = getHrmNameByWorkcode(PERNR);
//                    Object[] values = new Object[6];
//                    values[0]=PERNR;
//                    values[1]=BEGDA;
//                    values[2]=ZZHR_KHLW;
//                    values[3]=ZZHR_KHDF;
//                    values[4]=ZZHR_KHJG;
//                    values[5]=name;
//                    executeInsertSQL("uf_jxkhmx","gh,khrq,khlb,khdf,khjg,xm",values,"40828",name);
//                }
//            }
//        }
//
//
//        //工作履历
//        JSONArray OT_P9023 = DATA.getJSONArray("OT_P9023");
//        if(OT_P9023!=null){
//            if(OT_P9023.size()>0) {
//                for (int i = 0; i < OT_P9023.size(); i++) {
//                    JSONObject obj10 = OT_P9023.getJSONObject(i);
//                    String PERNR = obj10.getString("PERNR");
//                    String BEGDA = convertDate(obj10.getString("BEGDA"));
//                    String ENDDA = convertDate(obj10.getString("ENDDA"));
//                    String ZZHR_XZZW = obj10.getString("ZZHR_XZZW");
//                    String ARBGB = obj10.getString("ARBGB");
//                    String name = getHrmNameByWorkcode(PERNR);
//                    Object[] values = new Object[6];
//                    values[0]=PERNR;
//                    values[1]=BEGDA;
//                    values[2]=ENDDA;
//                    values[3]=ZZHR_XZZW;
//                    values[4]=ARBGB;
//                    values[5]=name;
//                    executeInsertSQL("uf_gzll","gh,ksrq,jsrq,zw,gz,xm",values,"40829",name);
//                }
//            }
//        }
//
//
//        //家庭成员
//        JSONArray OT_P9021 = DATA.getJSONArray("OT_P9021");
//        if(OT_P9021!=null){
//            if(OT_P9021.size()>0) {
//                for (int i = 0; i < OT_P9021.size(); i++) {
//                    JSONObject obj11 = OT_P9021.getJSONObject(i);
//                    String PERNR = obj11.getString("PERNR");
//                    String STEXT = obj11.getString("STEXT");
//                    String FCNAM = obj11.getString("FCNAM");
//                    String FGBDT = convertDate(obj11.getString("FGBDT"));
//                    String PTEXT = obj11.getString("PTEXT");
//                    String ZZHR_GHDW = obj11.getString("ZZHR_GHDW");
//                    String ZZHR_ZHWU = obj11.getString("ZZHR_ZHWU");
//                    String name = getHrmNameByWorkcode(PERNR);
//                    Object[] values = new Object[8];
//                    values[0]=PERNR;
//                    values[1]=STEXT;
//                    values[2]=FCNAM;
//                    values[3]=FGBDT;
//                    values[4]=PTEXT;
//                    values[5]=ZZHR_GHDW;
//                    values[6]=ZZHR_ZHWU;
//                    values[7]=name;
//                    executeInsertSQL("uf_jtcy","gh,cw,jtcyxm,csrq,zzmm,gzdw,zw,xm",values,"40830",name);
//                }
//            }
//        }
//
//
////        }
//        ret.put("JsonObj", jsonObject);
//        ret.put("code", "S");
//        return ret;
//    }
//
//    public String getHrmidbyWorkcode(String workcode){
//        RecordSet rs = new RecordSet();
//        String ryid="";
//        rs.executeQuery("select id from hrmresource where workcode=? and status=1",workcode);
//        if(rs.next()){
//            ryid=rs.getString("id");
//        }
//        return ryid;
//    }
//
//    public String getHrmNameByWorkcode(String workcode){
//        RecordSet rs = new RecordSet();
//        String name="";
//        rs.executeQuery("select id from hrmresource where workcode=? and status=1",workcode);
//        if(rs.next()){
//            name=rs.getString("id");
//        }
//        return name;
//    }
//
//    public String getRealPath(String imagefileid){
//        RecordSet rs = new RecordSet();
//        String filerealpath="";
//        rs.executeQuery("select filerealpath from imagefile where imagefileid=?",imagefileid);
//        if(rs.next()){
//            filerealpath=rs.getString("filerealpath") ;
//        }
//        return filerealpath;
//    }
//
//    public boolean executeHrmUpdateSQL(String param, String value, String workcode) {
//        RecordSet rs = new RecordSet();
//        String sql = "update hrmresource set " + param + "='" + value +"' where workcode=? and status=1";
//        return rs.executeUpdate(sql, workcode);
//    }
//
//    public boolean executeDefiUpdateSQL(String param, String value, String workcode, String scope) {
//        RecordSet rs = new RecordSet();
//        RecordSet rs1 = new RecordSet();
//        String selectsql=
//                "select 1 from cus_fielddata where id=(select id from hrmresource where workcode='"+workcode+"'" +
//                        " and status=1) and scopeid='"+scope+"' and scope='HrmCustomFieldByInfoType'";
//        rs.executeQuery(selectsql);
//        if(rs.next()){
//            String sql = "update cus_fielddata set " + param + "='" + value + "' where id=(select id from hrmresource where workcode=? and status=1) and scopeid=? and scope='HrmCustomFieldByInfoType'";
//            System.out.println("执行的sql "+sql+" "+workcode+" "+scope);
//            return rs1.executeUpdate(sql, workcode, scope);
//        }else{
//            String sql="insert into cus_fielddata(id,scopeid,scope,"+param+") values((select id from hrmresource " +
//                    "where workcode='"+workcode+"' and status=1),'"+scope+"','HrmCustomFieldByInfoType','"+value+"')";
//            return rs1.executeUpdate(sql);
//        }
//    }
//
//    public boolean executeInsertSQL(String tablename,String params, Object[] values,String formmodeid,String id) {
//        RecordSet rs = new RecordSet();
//        rs.executeQuery("select 1 from "+tablename+" where id=?",id);
//        if(rs.next()){
//            String setstr="";
//           String[] paramlist=params.split(",");
//           for(String param:paramlist){
//               setstr+=param+"=?,";
//           }
//           if(setstr.length()>0){
//               setstr.substring(0,setstr.length()-1);
//           }
//           String sql="update "+tablename+" set "+setstr+" where id='"+id+"'";
//           return rs.executeUpdate(sql,values);
//        }else{
//            UUIDPKVFormDataSave var64 = new UUIDPKVFormDataSave();
//            String modeuuid = (String) var64.generateID((Map) null);
//            String param="";
//            for(int i=0;i<values.length;i++){
//                param+="?,";
//            }
//            String sql = "insert into "+tablename+"("+params+",formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,modeuuid) values("+param+" "+formmodeid + ",1,0,'" + this.getCurrentDate() + "','" + this.getCurrentTime() + "','" + modeuuid + "')";
//            System.out.println("params的值是"+params);
//            System.out.println("values的值是"+values);
//            return rs.executeUpdate(sql, values);
//        }
//    }
//
//
//    public int saveImageFile(String filePath, String fileName, Integer userId) {
//
//        ImageFileManager imageFileManager = new ImageFileManager();
//        //  初始化用户信息
//        User user = new User(userId);
//
//        File file = new File(filePath);
//        BufferedInputStream inputStream = FileUtil.getInputStream(file);
//        byte[] bytes = readBytes(inputStream);
//
//
//        //  调用存储文件方法，返回文件id
//        int result = 0;
//        //  存储文件名称
//        imageFileManager.setImagFileName(fileName);
//        //  保存文件数据
//        imageFileManager.setData(bytes);
//        //  保存文件返回文件id
//        result = imageFileManager.saveImageFile();
////        int docId=-1;
////        try {
////            //  文件存储到docimage
////            docId = docSaveService.accForDoc(61073, result, user);
////        } catch (Exception e) {
////            throw new RuntimeException(e);
////        }
//
//        return result;
//    }
//
//
//    private String getCurrentDate() {
//        SimpleDateFormat var1 = new SimpleDateFormat("yyyy-MM-dd");
//        return var1.format(new Date());
//    }
//
//
//    private String getCurrentMonth() {
//        SimpleDateFormat var1 = new SimpleDateFormat("MM");
//        return var1.format(new Date());
//    }
//
//    private String getCurrentTime() {
//        SimpleDateFormat var1 = new SimpleDateFormat("HH:mm:ss");
//        return var1.format(new Date());
//    }
//
//    public void save(String src, String out) {
//        if (src == null || src.length() == 0) {
//            return;
//        }
//        try {
//            File outFile = new File(out);
//            if (!outFile.getParentFile().exists()) {
//                outFile.getParentFile().mkdirs(); // 创建输出目录
//            }
//            FileOutputStream fos = new FileOutputStream(outFile);
//            byte[] by = src.getBytes();
//            for (int i = 0; i < by.length; i += 2) {
//                fos.write(charToInt(by[i]) * 16 + charToInt(by[i + 1]));
//            }
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public int charToInt(byte ch){
//        int val = 0;
//        if (ch >= 0x30 && ch <= 0x39) {
//            val = ch - 0x30;
//        } else if (ch >= 0x41 && ch <= 0x46) {
//            val = ch - 0x41 + 10;
//        }
//        return val;
//    }
//
//    public String convertDate(String date){
//        String afterConvertDate="";
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
//
//        try {
//            Date d = sdf.parse(date);
//            afterConvertDate = sdf2.format(d);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return afterConvertDate;
//    }
//
//    private void deleteDirectory(File directory) {
//        File[] files = directory.listFiles();
//
//        if (files != null) {
//            for (File file : files) {
//                if (file.isDirectory()) {
//                    deleteDirectory(file);
//                } else {
//                    file.delete();
//                }
//            }
//        }
//
//        directory.delete();
//    }
//}
