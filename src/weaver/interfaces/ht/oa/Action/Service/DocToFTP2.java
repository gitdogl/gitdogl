//package weaver.interfaces.ht.oa.Action.Service;
//
//import com.itextpdf.text.Image;
//import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.*;
//import com.sun.star.io.IOException;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import org.apache.axis.encoding.Base64;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//import weaver.docs.webservices.DocAttachment;
//import weaver.docs.webservices.DocInfo;
//import weaver.docs.webservices.DocServiceImpl;
//import weaver.general.BaseBean;
//import weaver.hrm.User;
//import weaver.interfaces.ht.oa.Action.util.DocumentBookmarkRepalceUtil;
//import weaver.interfaces.kd.oa.base.BaseDao;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.*;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.nio.charset.Charset;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.UUID;
//
//
//public class DocToFTP2 {
//	BaseBean bb = new BaseBean();
//	BaseDao baseDao = new BaseDao();
//
//	DocumentBookmarkRepalceUtil documentBookmarkRepalceUtil = new DocumentBookmarkRepalceUtil();
//	/**
//	 * 获取附件到程序
//	 *
//	 * @param requestid
//	 * @param sftp 是否需要图片水印
//	 * @return
//	 * @throws Exception
//	 */
//	public int getOAdoc(int id, String username, String userpwd,String requestid,String htksrq,boolean sftp,String bh)
//			throws Exception {
//		int fjid = 0;
//
//		DocServiceImpl service = new DocServiceImpl();
//		User yhm = baseDao.getUserById(1);
//		DocInfo doc = service.getDocByUser(id, yhm, "");
//		// 取得该文档的第一个附件
//		DocAttachment da = doc.getAttachments()[0];
//
//		// 得到附件内容
//		byte[] content = Base64.decode(da.getFilecontent());
//		// 将附件内容转存至D:\
//
//		String hclj = "";
//		if (hclj == null || hclj.equals("")) {
//			hclj = bb.getPropValue("DOCtoPDF", "hclj");
//		}
//		if (hclj == null || hclj.equals("")) {
//			hclj = "/home/";
//		}
//		String name = da.getFilename();
//		String[] strArray = name.split("\\.");
//		int suffixIndex = strArray.length -1;
//		String fileType = strArray[suffixIndex];
//
//
//		File file = new File(hclj + da.getFilename());
//
//			int byteread;
//			byte data[] = new byte[1024];
//			InputStream imagefile  = new ByteArrayInputStream(content);
//			OutputStream out = new FileOutputStream(file);
//			while ((byteread = imagefile.read(data)) != -1) {
//				out.write(data, 0, byteread);
//				out.flush();
//			}
//			out.close();
//			imagefile.close();
//			File outputFile = new File(hclj + id + ".pdf");
//			String sourceFileName = file.getPath();
//			String targetFileName = outputFile.getPath();
//			if ("doc".equals(fileType) || "docx".equals(fileType)|| "DOC".equals(fileType)|| "DOCX".equals(fileType) ){
//				convertMStoPDF (sourceFileName, targetFileName);
//			}
//			if ("PDF".equals(fileType) || "pdf".equals(fileType) ){
//				targetFileName = sourceFileName;
//			}
//			File outputFile_1 = new File(hclj + id + "_1.pdf");
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
//			String currentDate =   dateFormat.format( new Date() );
////			String synr = "中国科学技术大学&"+baseDao.getRequestNameByResquestID(requestid)+"&"+currentDate;
//			String synr = "USTC&     "+currentDate;
//			String uuid = UUID.randomUUID().toString();
//			String mouldSavepath =  documentBookmarkRepalceUtil.getTargetFolderPath("pdfMouldPath");
//			String filetemppath = mouldSavepath + File.separator + uuid;
//			documentBookmarkRepalceUtil.generateFile(requestid, filetemppath+".png");
//			documentBookmarkRepalceUtil.create(bh,filetemppath+".png",filetemppath+".png");
//			if(sftp){
//				setWatermark(targetFileName, outputFile_1.getPath());
//			}else{
//				waterMark(targetFileName, outputFile_1.getPath(), synr);
//			}
//			documentBookmarkRepalceUtil.setWatermark(outputFile_1.getPath(),filetemppath + "new.pdf",filetemppath+".png");
//
//			File file1 = new File(filetemppath + "new.pdf");
//			fjid = setOAdoc(file1, da.getFilename() + "");
//			file1.delete();
//			file.delete();
//			outputFile_1.delete();
//			outputFile.delete();
//			new File(filetemppath+".png").delete();
//
//		return fjid;
//
//	}
//
//
//	public static boolean deleteFile( File file) {
//
//        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
//        if (file.exists() && file.isFile()) {
//            if (file.delete()) {
//                System.out.println("删除单个文件" + file.getPath() + "成功！");
//                return true;
//            } else {
//                System.out.println("删除单个文件" + file.getPath() + "失败！");
//                return false;
//            }
//        } else {
//            System.out.println("删除单个文件失败：" + file.getPath() + "不存在！");
//            return false;
//        }
//    }
//
//
//	/**
//	 * 插入附件到OA
//	 *
//	 * @param dz
//	 * @throws Exception
//	 */
//	public int setOAdoc(File dz, String name)
//			throws Exception {
//
//		DocServiceImpl service = new DocServiceImpl();
//		User yhm = baseDao.getUserById(1);
//		byte[] content = null;
//		DocInfo doc = new DocInfo();
//		DocAttachment da = new DocAttachment();
//
//			int byteread;
//			byte data[] = new byte[1024];
//			InputStream input = new FileInputStream(dz);
//
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			while ((byteread = input.read(data)) != -1) {
//				out.write(data, 0, byteread);
//				out.flush();
//			}
//			content = out.toByteArray();
//			input.close();
//			out.close();
//
//
//		da.setDocid(Integer.valueOf(0));//新建默认给0
//		da.setImagefileid(Integer.valueOf(0));//新建默认给0
//		da.setFilecontent(Base64.encode(content));//附件实体类
//		da.setFilerealpath("");//新建默认给空
//		da.setFilename(name + ".pdf");//附件名称
//		da.setIszip(Integer.valueOf(1));//是否压缩
//		doc.setSeccategory(361);//目录id
//		doc.setId(Integer.valueOf(0));//新建默认给0
//		doc.setDocSubject(name);//文档名称
//		doc.setDoccontent("");//文档内容  附件类型为空
//		doc.setAttachments(new DocAttachment[] { da });//附件对象
//		int pdfid = service.createDocByUser(doc, yhm);//写入
//
//		System.out.println(pdfid);
//		System.out.println("dz:" + dz);
//		return pdfid;
//
//	}
//
//	/**
//	 * 添加图片水印
//	 * @param input
//	 * @param pdf
//	 * @throws DocumentException
//	 * @throws IOException
//	 * @throws MalformedURLException
//	 * @throws java.io.IOException
//	 */
//	 public static void setWatermark(String input,String pdf)
//	            throws DocumentException, IOException, MalformedURLException, java.io.IOException {
//	        PdfReader reader = new PdfReader(input);
//	        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(pdf)));
//	        PdfStamper stamper = new PdfStamper(reader, bos);
//	        int total = reader.getNumberOfPages() + 1;
//	        PdfContentByte content;
//	        BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
//	        PdfGState gs = new PdfGState();
//	        for (int i = 1; i < total; i++) {
//	            content = stamper.getOverContent(i);// 在内容上方加水印
//	            // content = stamper.getUnderContent(i);//在内容下方加水印
//	            gs.setFillOpacity(0.2f);
//	            content.setGState(gs);
//	            content.beginText();
//	            content.setColorFill(BaseColor.LIGHT_GRAY);
//	            content.setFontAndSize(base, 50);
////	            content.showTextAligned(Element.ALIGN_CENTER, tpdz, 300, 350, 55);
//	            Image image = Image.getInstance("D:\\111111.png");
//	            /*
//	              img.setAlignment(Image.LEFT | Image.TEXTWRAP);
//	              img.setBorder(Image.BOX); img.setBorderWidth(10);
//	              img.setBorderColor(BaseColor.WHITE); img.scaleToFit(100072);//大小
//	              img.setRotationDegrees(-30);//旋转
//	             */
//	            image.setAbsolutePosition(150, 300); // set the first background
//	                                                    // image of the absolute
//	            image.scaleToFit(300, 300);
//	            content.addImage(image);
//	            content.setColorFill(BaseColor.BLACK);
////	            content.setFontAndSize(base, 8);
////	            content.showTextAligned(Element.ALIGN_CENTER, "下载时间：" + waterMarkName + "", 300, 10, 0);
//	            content.endText();
//
//	        }
//	        stamper.close();
//	    }
//	/**
//	 * 添加文字水印
//	 * @param inputFile
//	 * @param outputFile
//	 * @param waterMarkName
//	 */
//	public static void waterMark(String inputFile, String outputFile,
//			String waterMarkName) throws java.io.IOException, DocumentException {
//
//			PdfReader reader = new PdfReader(inputFile);
//			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
//					outputFile));
//
//			BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
//					BaseFont.EMBEDDED);
//			PdfGState gs = new PdfGState();
//			gs.setFillOpacity(0.1f);
//			gs.setStrokeOpacity(0.3f);
//			int total = reader.getNumberOfPages() + 1;
//			JLabel label = new JLabel();
//			FontMetrics metrics;
//			metrics = label.getFontMetrics(label.getFont());
//			PdfContentByte under;
//			for (int i = 1; i < total; i++) {
//				under = stamper.getOverContent(i);
//				under.saveState();
//				under.setGState(gs);
//				under.beginText();
//				under.setFontAndSize(base, 100);
//				String[] watstr = waterMarkName.split("&");
//				int gd = 300;
//				for(int j = watstr.length-1 ; j>=0 ; j--){
//					under.showTextAligned(Element.ALIGN_CENTER,watstr[j],300,gd,30);
//					gd +=metrics.getHeight()+100;
//				}
////				int gd2 = 500;
////				for(int j = watstr.length-1 ; j>=0 ; j--){
////					under.showTextAligned(Element.ALIGN_CENTER,watstr[j],300,gd2,30);
////					gd2 +=metrics.getHeight()+100;
////				}
//				// 添加水印文字
//				under.endText();
//			}
//			stamper.close();
//			reader.close();
//
//	}
//
//	private static String unicodeToCn(String unicode) {
//        /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格*/
//        String[] strs = unicode.split("\\\\u");
//        String returnStr = "";
//        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
//        for (int i = 1; i < strs.length; i++) {
//          returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
//        }
//        return returnStr;
//    }
//
//	public void convertMStoPDF(String sourceFileName , String targetFileName) throws Exception {
//		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
////		HttpPost httpPost = new HttpPost("http://192.168.35.21:8080/dcs.web/upload");
//		HttpPost httpPost = new HttpPost("http://192.168.35.21:8080/dcs.web/upload");
//		FileBody fileBody = new FileBody(new File(sourceFileName));
//		MultipartEntity multipartEntity =  new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, (String)null, Charset.forName("UTF-8"));
//		multipartEntity.addPart("file", fileBody);
//		multipartEntity.addPart("convertType", new StringBody("3", Charset.forName("UTF-8")));
//		httpPost.setEntity(multipartEntity);
//		HttpResponse httpResponse;
//		try{
//
//			 httpResponse = defaultHttpClient.execute(httpPost);
//
//		}catch (Exception e){
//			throw new Exception("转换pdf服务连接失败!请联系管理员排除转换服务是否正常!");
//		}
//
//		int statusCode = httpResponse.getStatusLine().getStatusCode();
//		String frStr = "";
//		System.out.println("永中返回:"+frStr);
//		System.out.println(statusCode);
//		if (statusCode == 200) {
//			HttpEntity httpEntity = httpResponse.getEntity();
//			frStr = EntityUtils.toString(httpEntity);
//			EntityUtils.consume(httpEntity);
//		}else{
//			throw new Exception(statusCode+"转换pdf服务连接失败!请联系管理员排除转换服务是否正常!");
//		}
//		if (!"".equals(frStr)) {
//			JSONObject jsonObject = JSONObject.fromObject(frStr);
//			int result = jsonObject.getInt("result");
//			if(result != 0){
//				throw new Exception(jsonObject.getString("message"));
//			}
//			JSONArray jsonArray = jsonObject.getJSONArray("data");
//			String pdfUrl = jsonArray.getString(0);
////			String newPdfUrl = pdfUrl.replaceAll("hetong.ustc.edu.cn","192.168.35.21");
//			downloadNet(pdfUrl,targetFileName);
//		}else{
//			throw new Exception("转换pdf服务连接失败!请联系管理员排除转换服务是否正常!");
//		}
//
//	}
//
//	public void downloadNet(String urldz,String bcdz) throws java.io.IOException {
//		// 下载网络文件
//		int bytesum = 0;
//		int byteread = 0;
//		URL url = new URL(urldz);
//
//			URLConnection conn = url.openConnection();
//			InputStream inStream = conn.getInputStream();
//			FileOutputStream fs = new FileOutputStream(bcdz);
//			byte[] buffer = new byte[1204];
//			while ((byteread = inStream.read(buffer)) != -1) {
//				bytesum += byteread;
//				fs.write(buffer, 0, byteread);
//			}
//
//	}
//}
