//package weaver.interfaces.jjc.oaclient.cw.util;
//
//import DBstep.iMsgServer2000;
//import com.engine.common.util.ServiceUtil;
//import com.engine.workflow.service.HtmlToPdfService;
//import com.engine.workflow.service.impl.HtmlToPdfServiceImpl;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.WriterException;
//import com.google.zxing.client.j2se.MatrixToImageWriter;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.pdf.PdfContentByte;
//import com.itextpdf.text.pdf.PdfGState;
//import com.itextpdf.text.pdf.PdfReader;
//import com.itextpdf.text.pdf.PdfStamper;
//import com.lowagie.text.Element;
//import com.lowagie.text.Rectangle;
//import org.apache.axis.encoding.Base64;
//import org.apache.poi.xwpf.usermodel.Document;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFPictureData;
//import org.apache.xmlbeans.XmlOptions;
//import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
//import weaver.conn.RecordSet;
//import weaver.docs.webservices.DocAttachment;
//import weaver.docs.webservices.DocInfo;
//import weaver.docs.webservices.DocServiceImpl;
//import weaver.general.BaseBean;
//import weaver.general.GCONST;
//import weaver.general.Util;
//import weaver.hrm.User;
//import weaver.hrm.company.DepartmentComInfo;
//import weaver.hrm.resource.ResourceComInfo;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.*;
//import java.net.MalformedURLException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
//
//public class DocumentBookmarkRepalceUtil {
//    public String toPdf(User user, String requestid, String modeid) {
//        BaseBean bb = new BaseBean();
//        Map<String, Object> params = new HashMap<>();
//        params.put("requestid", requestid);    //必传
//        params.put("modeid", modeid);    //模板id(传模板id则根据模板生成.不传则默认使用显示模板)
//        params.put("path", "/data/weaver/ecology/filesystem/wkhtmltopdf");  //存储路径(不传则windows默认D:/testpdf;linux默认/usr/testpdf)
//        params.put("onlyHtml", "0");    //0:转pdf  1:只转成html  2:转html和pdf  (不传则默认=0)
//        params.put("keepsign", "0");   //1:保留底部签字意见 0：不保留 (不传则默认=1)
//        params.put("pageSize", "100"); //底部签字意见最大显示数量  (默认=100)
//        params.put("isTest", "1");    //外部调用必传isTest=1
//        params.put("limitauth", "0"); //不校验权限
//        HtmlToPdfService htmlToPdfService = (HtmlToPdfService) ServiceUtil.getService(HtmlToPdfServiceImpl.class, user);
//        Map<String, Object> pathMap = htmlToPdfService.getFormDatas(params);
//        String path = (String) pathMap.get("path");
//        String filename = (String) pathMap.get("filename");
//        return path + File.separator + filename;
//    }
//
//    /**
//     * 生成二维码
//     *
//     * @param text     二维码内容
//     * @param width    宽度
//     * @param height   高度
//     * @param filePath 生成保存路径
//     * @throws WriterException
//     * @throws IOException
//     */
//    public void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
//        MatrixToImageWriter.writeToFile(bitMatrix, "PNG", new File(filePath));
//
//    }
//
//    /**
//     * 通过docid获取imageFileid附件id
//     *
//     * @param modeid
//     * @return
//     */
//    public int getModeData(int modeid) {
//        RecordSet rs = new RecordSet();
//        int imageFileId = 0;
//        rs.execute("select imageFileid from docImageFile where docid=" + modeid);
//        if (rs.next()) {
//            imageFileId = Util.getIntValue(rs.getString("imageFileId"), 0);
//        }
//        return imageFileId;
//    }
//
//    /**
//     * 通过docid获取imageFileid附件id
//     *
//     * @param modeid
//     * @return
//     */
//    public String getFileName(int modeid) {
//        RecordSet rs = new RecordSet();
//        String FileName = "";
//        rs.execute("select IMAGEFILENAME from docImageFile where docid=" + modeid);
//        if (rs.next()) {
//            FileName = rs.getString("IMAGEFILENAME");
//        }
//        return FileName;
//    }
//
//    /**
//     * 生成存放目录 没有创建
//     *
//     * @param targetfolder
//     * @return
//     */
//    public String getTargetFolderPath(String targetfolder) {
//        String targetFilePath = GCONST.getRootPath();
//        targetFilePath = targetFilePath + "filesystem" + File.separatorChar + targetfolder;
//        File file = new File(targetFilePath);
//        //如果文件夹不存在则创建
//        if (!file.exists()) {
//            file.mkdir();
//        }
//        return targetFilePath;
//    }
//
//
//    /**
//     * 拷贝附件到指定位置
//     *
//     * @param destPath
//     * @param is
//     * @return
//     */
//    public boolean copyFile(String destPath, InputStream is) {
//
//        // 定义输出流
//        ByteArrayOutputStream bout = null;
//        OutputStream outputStream = null;
//        try {
//            bout = new ByteArrayOutputStream();
//            outputStream = new BufferedOutputStream(new FileOutputStream(destPath));
//            byte data[] = new byte[1024];
//            int len = 0;
//            while ((len = is.read(data)) != -1) {
//                bout.write(data, 0, len);
//                bout.flush();
//            }
//            byte[] fileBody = bout.toByteArray();
//            iMsgServer2000 MsgObj = new iMsgServer2000();
//            MsgObj.MsgFileBody(fileBody);
//            fileBody = MsgObj.ToDocument(MsgObj.MsgFileBody());
//            is = new ByteArrayInputStream(fileBody);
//            bout.close();
//
//            len = 0;
//            data = new byte[1024];
//            while ((len = is.read(data)) != -1) {
//                outputStream.write(data, 0, len);
//                outputStream.flush();
//            }
//            return true;
//        } catch (Exception e) {
//            new BaseBean().writeLog("==============copy file error:", e);
//            return false;
//        } finally {
//            if (bout != null) {
//                try {
//                    bout.close();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//            if (outputStream != null) {
//                try {
//                    outputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (Exception e2) {
//                }
//            }
//        }
//    }
//
//
//    /**
//     * @param input pdf地址
//     * @param pdf   输出pdf
//     * @param img   图片地址
//     * @throws DocumentException
//     * @throws MalformedURLException
//     * @throws IOException
//     */
//    public void setWatermark(String input, String pdf, String img)
//            throws DocumentException, MalformedURLException, IOException {
//        PdfReader reader = new PdfReader(input);
//        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(pdf)));
//        PdfStamper stamper = new PdfStamper(reader, bos);
//        int total = reader.getNumberOfPages() + 1;
//        PdfContentByte content;
//        PdfGState gs = new PdfGState();
//        float pageWidth = 0;
//        float pageHeight = 0;
//        for (int i = 1; i < total; i++) {
//            content = stamper.getOverContent(i);// 在内容上方加水印
//
//            gs.setFillOpacity(1f);
//            content.setGState(gs);
//            content.beginText();
//            content.setColorFill(BaseColor.LIGHT_GRAY);
//
//
//            Image image = Image.getInstance(img);
//            pageWidth = reader.getPageSize(i).getWidth();
//            pageHeight = reader.getPageSize(i).getHeight();
//
//            image.setAbsolutePosition(pageWidth - 180, pageHeight - 570); // set the first background
//
//            image.scaleToFit(119, 119);
//            content.addImage(image);
//            content.setColorFill(BaseColor.BLACK);
//            content.endText();
//        }
//        stamper.close();
//    }
//
//    public int setOAdoc(String dz, String name, User yhm)
//            throws Exception {
//
//
//        DocServiceImpl service = new DocServiceImpl();
//
//        byte[] content = null;
//        DocInfo doc = new DocInfo();
//        DocAttachment da = new DocAttachment();
//
//        try {
//            int byteread;
//            byte data[] = new byte[1024];
//            InputStream input = new FileInputStream(new File(dz));
//
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            while ((byteread = input.read(data)) != -1) {
//                out.write(data, 0, byteread);
//                out.flush();
//            }
//            content = out.toByteArray();
//            input.close();
//            out.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        da.setDocid(Integer.valueOf(0));
//        da.setImagefileid(Integer.valueOf(0));
//        da.setFilecontent(Base64.encode(content));
//        da.setFilerealpath("");
//        da.setFilename(name);
//        // da.sets
//        // da.setImagefilename(requestid);
//        da.setIszip(Integer.valueOf(0));
//        doc.setSeccategory(44);
//        doc.setId(Integer.valueOf(0));
//        doc.setDocSubject(name);
//        doc.setDoccontent("");
//        doc.setAttachments(new DocAttachment[]{da});
//        int pdfid = service.createDocByUser(doc, yhm);
//
//        System.out.println(pdfid);
//        System.out.println("dz:" + dz);
//        return pdfid;
//
//    }
//
//    public User getUser(int userid) {
//        User user = new User();
//        try {
//            ResourceComInfo rc = new ResourceComInfo();
//            DepartmentComInfo dc = new DepartmentComInfo();
//
//            user.setUid(userid);
//            user.setLoginid(rc.getLoginID("" + userid));
//            user.setFirstname(rc.getFirstname("" + userid));
//            user.setLastname(rc.getLastname("" + userid));
//            user.setLogintype("1");
//
//            user.setSex(rc.getSexs("" + userid));
//            user.setLanguage(7);
//
//            user.setEmail(rc.getEmail("" + userid));
//
//            user.setLocationid(rc.getLocationid("" + userid));
//            user.setResourcetype(rc.getResourcetype("" + userid));
//
//            user.setJobtitle(rc.getJobTitle("" + userid));
//
//            user.setJoblevel(rc.getJoblevel("" + userid));
//            user.setSeclevel(rc.getSeclevel("" + userid));
//            user.setUserDepartment(Util.getIntValue(rc.getDepartmentID("" + userid), 0));
//            user.setUserSubCompany1(Util.getIntValue(dc.getSubcompanyid1(user.getUserDepartment() + ""), 0));
//
//            user.setManagerid(rc.getManagerID("" + userid));
//            user.setAssistantid(rc.getAssistantID("" + userid));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return user;
//    }
//
//
//    public int upDoc(File dz, DocInfo doc, DocAttachment da, DocServiceImpl service, User user, String name) throws Exception {
//
//        byte[] content = null;
//
//        try {
//            int byteread;
//            byte data[] = new byte[1024];
//            InputStream input = new FileInputStream(dz);
//
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            while ((byteread = input.read(data)) != -1) {
//                out.write(data, 0, byteread);
//                out.flush();
//            }
//            content = out.toByteArray();
//            input.close();
//            out.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        da.setFilecontent(Base64.encode(content));
//        da.setImagefilename(name);
//        doc.setAttachments(new DocAttachment[]{da});
//
//        return service.updateDocByUser(doc, user);
//
//    }
//
//    private static int interval = 50;
//
//    /**
//     * 添加文字水印
//     *
//     * @param inputFile
//     * @param outputFile
//     * @param waterMarkName
//     */
//    public void waterMark(String inputFile, String outputFile,
//                          String waterMarkName) {
//        try {
//            com.lowagie.text.pdf.PdfReader reader = new com.lowagie.text.pdf.PdfReader(inputFile);
//            com.lowagie.text.pdf.PdfStamper stamper = new com.lowagie.text.pdf.PdfStamper(reader, new FileOutputStream(
//                    outputFile));
//
//            com.lowagie.text.pdf.BaseFont base = com.lowagie.text.pdf.BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", true);
//
//            Rectangle pageRect = null;
//            com.lowagie.text.pdf.PdfGState gs = new com.lowagie.text.pdf.PdfGState();
//            gs.setFillOpacity(0.3f);
//            gs.setStrokeOpacity(0.3f);
//            int total = reader.getNumberOfPages() + 1;
//
//            JLabel label = new JLabel();
//            FontMetrics metrics;
//
//
//            com.lowagie.text.pdf.PdfContentByte under;
//            int textH = 0;
//            int textW = 0;
//            label.setText(waterMarkName);
//            metrics = label.getFontMetrics(label.getFont());
//            textH = metrics.getHeight();//字符串的高,   只和字体有关
//            textW = metrics.stringWidth(label.getText()) + 100;//字符串的宽
//
//            for (int i = 1; i < total; i++) {
//                pageRect = reader.getPageSizeWithRotation(i);
//                under = stamper.getOverContent(i);
//                under.saveState();
//                under.setGState(gs);
//                under.beginText();
//                under.setFontAndSize(base, 20);
//
//                int gd = 200;
//                for (int height = interval + textH; height < pageRect.getHeight();
//                     height = height + textH * 8) {
//                    for (int width = interval + textW; width < pageRect.getWidth() + textW;
//                         width = width + textW) {
//                        under.showTextAligned(Element.ALIGN_LEFT
//                                , waterMarkName, width - textW,
//                                height - textH, 40);
//                    }
//                }
//
//                // 添加水印文字
//                under.endText();
//            }
//            stamper.close();
//            reader.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 两个docx合并
//     *
//     * @param src
//     * @param append
//     * @throws Exception
//     */
//    public static void appendBody(XWPFDocument src, XWPFDocument append) throws Exception {
//        CTBody src1Body = src.getDocument().getBody();
//        CTBody src2Body = append.getDocument().getBody();
//
//        List<XWPFPictureData> allPictures = append.getAllPictures();
//        // 记录图片合并前及合并后的ID
//        Map<String, String> map = new HashMap();
//        for (XWPFPictureData picture : allPictures) {
//            String before = append.getRelationId(picture);
//            //将原文档中的图片加入到目标文档中
//            String after = src.addPictureData(picture.getData(), Document.PICTURE_TYPE_PNG);
//            map.put(before, after);
//        }
//
//        appendBody(src1Body, src2Body, map);
//
//    }
//
//    private static void appendBody(CTBody src, CTBody append, Map<String, String> map) throws Exception {
//        XmlOptions optionsOuter = new XmlOptions();
//        optionsOuter.setSaveOuter();
//        String appendString = append.xmlText(optionsOuter);
//
//        String srcString = src.xmlText();
//        String prefix = srcString.substring(0, srcString.indexOf(">") + 1);
//        String mainPart = srcString.substring(srcString.indexOf(">") + 1, srcString.lastIndexOf("<"));
//        String sufix = srcString.substring(srcString.lastIndexOf("<"));
//        String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));
//
//        if (map != null && !map.isEmpty()) {
//            //对xml字符串中图片ID进行替换
//            for (Map.Entry<String, String> set : map.entrySet()) {
//                addPart = addPart.replace(set.getKey(), set.getValue());
//            }
//        }
//        //将两个文档的xml内容进行拼接
//        CTBody makeBody = CTBody.Factory.parse(prefix + mainPart + addPart + sufix);
//
//        src.set(makeBody);
//    }
//
//    public static void createZip(String sourcePath, String zipPath) {
//        FileOutputStream fos = null;
//        ZipOutputStream zos = null;
//        File file = null;
//        try {
//            fos = new FileOutputStream(zipPath);
//            zos = new ZipOutputStream(fos);
//            file = new File(sourcePath);
//            if (file.exists() && file.isDirectory()) {
//                File[] files = file.listFiles();
//                for (File f : files) {
//                    writeZip(f, "", zos);
//                }
//            }
//        } catch (FileNotFoundException e) {
//        } finally {
//            try {
//                if (zos != null) {
//                    zos.close();
//                }
//            } catch (IOException e) {
//            }
//
//        }
//    }
//
//    public static void writeZip(File file, String parentPath, ZipOutputStream zos) {
//        if (file.exists()) {
//            if (file.isDirectory()) {
//                parentPath += file.getName() + File.separator;
//                File[] files = file.listFiles();
//                for (File f : files) {
//                    writeZip(f, parentPath, zos);
//                }
//            } else {
//                FileInputStream fis = null;
//                DataInputStream dis = null;
//                try {
//                    fis = new FileInputStream(file);
//                    dis = new DataInputStream(new BufferedInputStream(fis));
//                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
//                    zos.putNextEntry(ze);
//                    byte[] content = new byte[1024];
//                    int len;
//                    while ((len = fis.read(content)) != -1) {
//                        zos.write(content, 0, len);
//                        zos.flush();
//                    }
//                } catch (FileNotFoundException e) {
//                } catch (IOException e) {
//                } finally {
//                    try {
//                        if (dis != null) {
//                            dis.close();
//                        }
//                    } catch (IOException e) {
//                    }
//                }
//            }
//        }
//    }
//
//    public static void deleteFile(File file) {
//        if (file.exists()) {//判断文件是否存在
//            if (file.isFile()) {//判断是否是文件
//                file.delete();//删除文件
//            } else if (file.isDirectory()) {//否则如果它是一个目录
//                File[] files = file.listFiles();//声明目录下所有的文件 files[];
//                for (int i = 0; i < files.length; i++) {//遍历目录下所有的文件
//                    deleteFile(files[i]);//把每个文件用这个方法进行迭代
//                }
//                file.delete();//删除文件夹
//            }
//        }
//    }
//}
