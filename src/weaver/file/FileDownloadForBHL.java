package weaver.file;

import com.weaver.general.Util;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @BelongsProject: ZKD_DA_2023
 * @BelongsPackage: weaver.file
 * @Author: hf_zhangyuan
 * @CreateTime: 2023-03-13  00:28
 * @Description: TODO
 * @Version: 1.0
 */

public class FileDownloadForBHL extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String imagefileid = Util.null2String(request.getParameter("imagefileid"));//系统标识
        RecordSet rs = new RecordSet();
        int fileid = 0;
        String sql = "select imagefileid,imagefilename,filerealpath,filesize from imagefile b  where imagefileid  = " + imagefileid;
        rs.execute(sql);
        String filename = "";
        if (rs.next()) {
            fileid =Util.getIntValue(rs.getString("imagefileid"),0);
            filename = Util.null2String(rs.getString("imagefilename"));
        }
        new BaseBean().writeLog("---------- FileDownloadForNews ---------fileid=" + fileid);
        if(fileid <= 0) return;
        InputStream imagefile = null;
        try {
            ImageFileManager ifm = new ImageFileManager();
            ifm.getImageFileInfoById(fileid);
            imagefile = ifm.getInputStream();

            String contenttype = "";
            if(filename.toLowerCase().endsWith(".gif")) {
                contenttype = "image/gif";
                response.addHeader("Cache-Control", "private, max-age=8640000");
            }else if(filename.toLowerCase().endsWith(".png")) {
                contenttype = "image/png";
                response.addHeader("Cache-Control", "private, max-age=8640000");
            }else if(filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
                contenttype = "image/jpg";
                response.addHeader("Cache-Control", "private, max-age=8640000");
            }else if(filename.toLowerCase().endsWith(".bmp")) {
                contenttype = "image/bmp";
                response.addHeader("Cache-Control", "private, max-age=8640000");
            }
            ServletOutputStream out = response.getOutputStream();
            response.setContentType(contenttype);
            response.setHeader("content-disposition", "attachment; filename=" +  new String(filename.replaceAll("<", "").replaceAll(">", "").replaceAll("&lt;", "").replaceAll("&gt;", "").getBytes("UTF-8"),"ISO-8859-1"));
            int byteread;
            byte data[] = new byte[1024];
            while ((byteread = imagefile.read(data)) != -1) {
                out.write(data, 0, byteread);
                out.flush();
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            new BaseBean().writeLog(e);
        } finally {
            if(imagefile != null){
                try{
                    imagefile.close();
                }catch(Exception e){
                    new BaseBean().writeLog(e);
                }
            }
        }
    }
}