package weaver.interfaces.schedule;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipInputStream;

import weaver.conn.RecordSet;
import weaver.docs.change.FtpClientUtil;
import weaver.docs.webservicesformsg.AESCoder;
import weaver.general.BaseBean;
import weaver.general.GCONST;
import weaver.general.Util;

/**
 *
 * @author mac
 *
 */
public class BaoljtUtil extends BaseBean {



	/**
	 * 加密方法
	 * @param s
	 * @return
	 */
	public static final String standardMD5(String s)
	{
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try
		{
			byte[] strTemp = s.getBytes("GBK");
			MessageDigest mdTemp = MessageDigest.getInstance(("m" + hexDigits[13] + hexDigits[5]).toUpperCase());

			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] str = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++)
			{
				byte byte0 = md[i];
				str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
				str[(k++)] = hexDigits[(byte0 & 0xF)];
			}
			return new String(str);
		}
		catch (Exception  e) {}
		return null;
	}

	/**
	 * 上传文件到ftp
	 * @param docid  文档ID
	 * @param filename  文件名称
	 * @param ftpfilepath  ftp目标目录
	 * @return
	 */
	public String uploadfiletoFTP(String docid, String filename, String ftpfilepath) {

		String reflag = "";
		//
		String ftpHost = Util.null2String(getPropValue("bljt20200318", "ftpHost"));
		int ftpPort = Util.getIntValue(getPropValue("bljt20200318", "ftpPort"));
		String ftpUserName = Util.null2String(getPropValue("bljt20200318", "ftpUserName"));
		String ftpPassword = Util.null2String(getPropValue("bljt20200318", "ftpPassword"));
		writeLog("ftpHost："+ftpHost);
		writeLog("ftpPort："+ftpPort);
		writeLog("ftpUserName："+ftpUserName);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentdate = sdf.format(new Date());
		String localDirectoryAndFileName = getFolderNameDirOutBox() + currentdate + File.separator;
		Map map = new HashMap<String, Object>();
		Map mappath = new HashMap<String, Object>();
		try {
			// 获取ftp客户端
			FtpClientUtil ftpClientUtil = new FtpClientUtil(ftpHost, ftpPort, ftpUserName,
					ftpPassword);
			boolean flag = ftpClientUtil.open();
			RecordSet rstemp = new RecordSet();
			if (flag) {
				writeLog("连接FTP成功。");
				ftpClientUtil.cd(ftpfilepath);

				String uuid = getUUID();
				// 创建路径 OAdata/32位随机数/
				String topath = ftpfilepath + File.separator + currentdate + File.separator + uuid + File.separator;
				String topathtemp = "" + File.separator + currentdate + File.separator + uuid + File.separator;
				writeLog("topath:" + topath);
				//
				boolean ret = false;
				List<Map<String,String>> list= new ArrayList<Map<String,String>>();
				getImageFileName(list, localDirectoryAndFileName, docid);
				Map maptemp = null;
				for(int i=0;i<list.size();i++) {
					maptemp = (Map)list.get(i);
					ret = ftpClientUtil.upload(localDirectoryAndFileName + maptemp.get("filerandomUUID").toString(),maptemp.get("filename").toString(), topath);
					// 上传是是否成功
					if (ret) {
						writeLog("文件：imagefileid-" + maptemp.get("filename").toString() + "上传Ftp成功。");

					} else {
						writeLog("文件：imagefileid-" + maptemp.get("filename").toString() + "上传Ftp失败，请联系管理员。");
					}
				}
			} else {
				writeLog("连接FTP失败，请查看ftp配置信息或网络连接。");
			}
			ftpClientUtil.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reflag;

	}

	private void getImageFileName(List<Map<String,String>> list, String localDirectoryAndFileName, String docid) {
		InputStream imagefile = null;
		ZipInputStream zin = null;
		OutputStream os = null;
		try {
			RecordSet rs = new RecordSet();
			// docid = docidarr[i];
			String sql = "select dif.imagefileid, ii.iszip,ii.isaesencrypt,ii.aescode, dif.imagefilename, ii.filerealpath, dif.docid,dif.imagefileid from docimagefile dif left join imagefile ii on ii.imagefileid=dif.imagefileid where dif.docid in ("
					+ docid
					+ ")    and dif.versionid in (select max(d2.versionId) as mversionId from docimagefile d2 where d2.id=dif.id ) ";
			writeLog("sql:" + sql);
			rs.execute(sql);
			String imagefilename = "";
			String filerealpath = "";
			String filetooath = "";
			//String docid = "";
			String imagefileid = "";
			int q = 0;
			String hz = "";
			while (rs.next()) {
				String nameid = getUUID();

				imagefileid = Util.null2String(rs.getString("imagefileid")).trim();
				imagefilename = Util.null2String(rs.getString("imagefilename")).trim();
				filerealpath = Util.null2String(rs.getString("filerealpath")).trim();
				imagefileid = Util.null2String(rs.getString("imagefileid")).trim();
				docid = Util.null2String(rs.getString("docid")).trim();
				String iszip = Util.null2String(rs.getString("iszip")).trim();
				String isaesencrypt = Util.null2String(rs.getString("isaesencrypt")).trim();
				String aescode = Util.null2String(rs.getString("aescode")).trim();
				if ("".equals(filerealpath)) {
					continue;
				}
				writeLog("imagefilename:" + imagefilename);
				writeLog("filerealpath:" + filerealpath);

				if (!"".equals(imagefilename)) {
					String[] arrfilename = imagefilename.split("\\.");
					hz = "." + arrfilename[arrfilename.length - 1];
				} else {
					hz = ".";
				}

				filetooath = localDirectoryAndFileName + nameid + hz;
				writeLog(filetooath);
				File outFile = new File(filetooath);
				File fileParent = outFile.getParentFile();
				if (!fileParent.exists()) {
					fileParent.mkdirs();
				}
				if (!outFile.exists()) {
					outFile.createNewFile();
				}

				File thefile = new File(filerealpath);
				if (iszip.equals("1")) {
					zin = new ZipInputStream(new FileInputStream(thefile));

					if (zin.getNextEntry() != null) {
						imagefile = new BufferedInputStream(zin);
					}
				} else {
					imagefile = new BufferedInputStream(new FileInputStream(thefile));
				}

				if (isaesencrypt.equals("1")) {
					imagefile = AESCoder.decrypt(imagefile, aescode);
				}
				writeLog("docid::" + docid);
				os = new BufferedOutputStream(new FileOutputStream(outFile));
				int bytesRead = 0;
				byte[] buffer = new byte[2048];
				while ((bytesRead = imagefile.read(buffer, 0, 2048)) != -1) {
					os.write(buffer, 0, bytesRead);
					os.flush();
				}
				Map map = new  HashMap<String, String>();
				map.put("filename", imagefilename);
				map.put("filerandomUUID", (nameid + hz));
				list.add(map);
				zin.close();
				imagefile.close();
				os.close();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			writeLog("Msg:" + ex.getMessage());
			writeLog("Msg:" + ex);
		} finally {
			if (zin != null) {
				try {
					zin.close();
					imagefile.close();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
					writeLog("Msg:" + e.getMessage());
					writeLog("Msg:" + e);
				}
			}
		}
	}

	/**
	 * 获取文件夹目录
	 */
	public static String getFolderNameDirOutBox() {
		String xmlFolderNameDir = GCONST.getSysFilePath();
		xmlFolderNameDir = Util.null2String(xmlFolderNameDir);
		if (!xmlFolderNameDir.trim().equals("")) {
			xmlFolderNameDir += "oadata" + File.separator;
		}
		return xmlFolderNameDir;
	}

	/**
	 * 32 位随机数
	 *
	 * @return
	 */
	public static String getUUID() {
		String UUID_String = UUID.randomUUID().toString().toUpperCase();
		UUID_String = UUID_String.replace("-", "");
		return UUID_String;
	}

	public static void main(String[] args) {
		System.out.println(Util.getIntValue("1.0"));
		//此处用付款单位、收款人、       收款账号、              金额进行MD5算法加密后转大写
		//保利集团       上海黄金交易所  100655213350010001    100              =>125ECC201097A41DAD99B49DC597A5C8
		//PAY_NAME      RECE_ACC_NAME RECE_ACC_NO           PAY_AMOUNT
		String bb = "保利集团测试姓名010001100024391610010001100";
		System.out.println("md5==>"+standardMD5(bb));
		String FINGERPRINT_CODE = standardMD5(bb).toUpperCase();
		System.out.println("FINGERPRINT_CODE==>"+FINGERPRINT_CODE);


		String PAY_AMOUNT = "100";
		double d =Double.parseDouble(PAY_AMOUNT);
		DecimalFormat formt =new DecimalFormat("#.00");
		PAY_AMOUNT =formt.format(d);
		System.out.println("PAY_AMOUNT==>"+PAY_AMOUNT);

		//0F7138D9CE7F9126904D3E582FD5C694  保利集团测试姓名010001100024391610010001100.00
		//F567B5C5086AA544626AD3EC48F02B16  保利集团测试姓名010001100024391610010001100
	}

}
