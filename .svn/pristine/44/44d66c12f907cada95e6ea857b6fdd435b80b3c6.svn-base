//package com.weaver.esb.package_20240121052117;
//
//import io.swagger.models.auth.In;
//import org.mapdb.Atomic;
//import weaver.conn.RecordSet;
//import weaver.general.BaseBean;
//import weaver.hrm.User;
//import weaver.interfaces.interfaces.mail4.dto.OAMaillFileBill;
//
//import javax.activation.MimetypesFileTypeMap;
//import java.io.File;
//import java.io.Writer;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//public class class_20240121052117 {
//    RecordSet RS=new RecordSet();
//    String error="";
//    /**
//     * @param:  param(Map collections)
//     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
//     */
//    public Map execute(Map<String,Object> params) {
//        Map<String,Object> ret = new HashMap<>();
//        BaseBean bb = new BaseBean();
//        RecordSet rs = new RecordSet();
//        RecordSet rs1 = new RecordSet();
//        RecordSet rs2 = new RecordSet();
//        //查mailreousrce表里面2022 11，12两个月，并且在mailresourcefile表里面查不到记录的数据，记录下邮件在我们oa的id,发送人,发送时间，邮件标题，
//        String sql="select id,sendfrom,senddate,subject  from mailresource where id not in(select mailid from " +
//                "mailresourcefile) and " +
//                "senddate " +
//                "BETWEEN '2022-11-01' and '2022-12-31'";
//        rs.executeQuery(sql);
//        while(rs.next()){
////            bb.writeLog("------------------------------------ 进入查询-----------------------------");
//            String id=rs.getString("id");
//            String sendfrom=rs.getString("sendfrom");
//            User user = new User(Integer.parseInt(sendfrom));
//            String name=user.getLastname();
//            String senddate=rs.getString("senddate");
//            String subject=rs.getString("subject");
//            //根据发送时间，发送人，邮件标题在万户表里面查邮件数据，如果能找到对应的数据，查万户邮件附件表，查附件名称，然后查oa的inner目录下面有没有这个附件名称，有就把目录和上面存的id插入到我们的附件表里面
//            String sql1="select * from test where MAILSUBJECT=? and MAILPOSTTIME like '"+senddate+"%' and MAILPOSTERNAME=?";
//            rs1.executeQuery(sql1,subject,name);
//            String MAIL_ID="";
//            if(rs1.next()){
//                MAIL_ID=rs1.getString("MAIL_ID");
//            }
//            rs2.executeQuery("select * from testfj where mail_id=?",MAIL_ID);
//            if(rs2.next()){
//                String filepathname=rs2.getString("accessorysavename");
//                String filename=rs2.getString("accessoryname");
//                String filepath ="";
//                if(isinMonth(11,senddate)){
//                    filepath="/data/weaver/whfjnewnew/202211/"+filepathname;
//                }else if(isinMonth(12,senddate)){
//                    filepath="/data/weaver/whfjnewnew/202212/"+filepathname;
//                }
//                File fj = new File(filepath);
//                if (fj.exists()) {
//                    bb.writeLog("开始写入附件"+filename);
//                    String size= fj.length() + "";
//                    String filetype=(new MimetypesFileTypeMap()).getContentType(fj);
//                    String mrf_uuid = UUID.randomUUID().toString();
//                    if(!insertFile(id,filename,filepath,size,filetype,mrf_uuid)){
//                        error+=id+filename+"插入失败";
//                    }
//                    if(!updatesize((int)fj.length(),id)){
//                        error+=id+filename+"更新失败";
//                    }
//                }
//            }
//        }
//
//        if("".equals(error)){
//            ret.put("code","S");
//            ret.put("msg",error);
//        }else{
//            ret.put("code","E");
//            ret.put("msg",error);
//        }
//        return ret;
//    }
//    public boolean insertFile(String mailid,String filename,String filepath,String size,String filetype,String mrf_uuid){
//        String sql = "INSERT INTO mailresourcefile( MAILID, FILENAME, FILETYPE, FILEREALPATH, ISZIP, ISENCRYPT, " +
//                "ISFILEATTRACHMENT, FILESIZE, ISAESENCRYPT, AESCODE, MRF_UUID, STORAGESTATUS, SECRETLEVEL) VALUES" +
//                " ( '" + mailid + "', '" + filename + "', '" + filetype + "', '" + filepath + "', '0', '0', '1', " +
//                "'" + size + "', '0', 'JgPu7X8Jyrr4O', '" + mrf_uuid + "', '2', '4')";
//        return RS.executeUpdate(sql);
//    }
//
//    private boolean isinMonth(int month,String dateStr){
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date date = dateFormat.parse(dateStr);
//            int currentmonth = date.getMonth() + 1;  // 月份是从0开始计数的，所以要加1
//            return currentmonth == month;
//        } catch (ParseException e) {
//            return false;
//        }
//    }
//
//    private boolean updatesize(int size,String id){
//        String sql="update mailresource set Size_n=? where id=?";
//        return RS.executeUpdate(sql,size,id);
//    }
//}
