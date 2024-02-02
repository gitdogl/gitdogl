/*
 *
 * Copyright (c) 2001-2016 泛微软件.
 * 泛微协同商务系统,版权所有.
 *
 */
package com.engine.license.web;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ln.LN;
import net.sf.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.file.FileManage;
import weaver.file.FileUpload;
import weaver.general.BaseBean;
import weaver.general.GCONST;
import weaver.general.PageIdConst;
import weaver.general.SecurityHelper;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.ln.HttpClientUtil;
import weaver.ln.LicenseList;
import weaver.login.LicenseCheckLogin;
import weaver.systeminfo.SystemEnv;

import com.api.BaseAction;
import com.cloudstore.dev.api.util.Util_TableMap;
import com.engine.common.util.ServiceUtil;
import com.engine.license.service.LicenseServiceImpl;
import com.weaver.upgrade.FunctionUpgrade;


/**
 * @version 1.0
 * @author hh 授权信息
 */
public class LicenseAction extends BaseAction {

	private LicenseServiceImpl lsi = null;

	/**
	 * 授权信息
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@POST
	@Path("/licenseInfo")
	@Produces(MediaType.TEXT_PLAIN)
	public String getLicenseInfo(@Context HttpServletRequest request, @Context HttpServletResponse response) {

		LN ln = new LN();
		JSONObject json = new JSONObject();
		if(!isLogin(request, response)){
			return msgObject.toString();
		}
		boolean canEdit = HrmUserVarify.checkUserRight("SystemSetEdit:Edit", user);

		LN license = new LN();
		license.CkHrmnum();
		String companyName = license.getCompanyname();
		String licenseCode = license.getLicensecode();
		String hrmNum = license.getHrmnum();
		String scType = Util.null2String(license.getScType());
		String scCount = scType.equals("1") ? "0" : Util.null2String(license.getScCount());
		scCount = scCount.equals("0") ? SystemEnv.getHtmlLabelName(82497, user.getLanguage()) : scCount;
		String concurrentFlag = Util.null2String(license.getConcurrentFlag()); // 用户并发数标识
		LicenseCheckLogin verifylogin = new LicenseCheckLogin();
		int onlineusercount = verifylogin.checkUserLoginCount();

		int licensenum = license.CkUsedHrmnum(); // 已使用的license数量
		int unusedlice = license.CkUnusedHrmnum(); // 未使用的license数量
		if (hrmNum.equals("999999")) {
			hrmNum = SystemEnv.getHtmlLabelName(18637, user.getLanguage());
		}
		String expireDate = license.getExpiredate();
		if (expireDate.equals("9999-99-99")) {
			expireDate = SystemEnv.getHtmlLabelName(18638, user.getLanguage());
		}

		JSONObject returnObj = new JSONObject();
		returnObj.put("companyName", companyName);
		returnObj.put("licenseCode", licenseCode);
		returnObj.put("hrmNum", hrmNum);
		returnObj.put("scCount", scCount);
		returnObj.put("concurrentFlag", concurrentFlag);
		returnObj.put("onlineusercount", onlineusercount);
		returnObj.put("licensenum", licensenum);
		if("1".equals(concurrentFlag)){
			returnObj.put("unusedlice", Util.getIntValue(hrmNum) - onlineusercount);
		}else{
			returnObj.put("unusedlice", license.getHrmnum().equals("999999") ? hrmNum : unusedlice);
		}
		returnObj.put("expireDate", expireDate);
		returnObj.put("canEdit", canEdit);
		return returnObj.toString();
	}

	/**
	 * 获取识别码，无需登录验证
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@POST
	@Path("/InLicense")
	@Produces(MediaType.TEXT_PLAIN)
	public String getLicenseCode(@Context HttpServletRequest request, @Context HttpServletResponse response) {

		LN license = new LN();
		JSONObject json = new JSONObject();

		String message = "0";
		try {
			message = license.CkLicense(TimeUtil.getCurrentTimeString());
		}catch (Exception e){}
		if("1".equals(message)){
			if(!isLogin(request, response)){
				return msgObject.toString();
			}
			if(user != null){
				boolean canEdit = HrmUserVarify.checkUserRight("SystemSetEdit:Edit", user) ;
				if(!canEdit){
					json.put("showType", "msg");
					json.put("status", false);
					json.put("error", SystemEnv.getHtmlLabelName(512047,user == null ? weaver.general.ThreadVarLanguage.getLang() : user.getLanguage()));
					return json.toString();
				}
			}
		}
		lsi = (LicenseServiceImpl) ServiceUtil.getService(LicenseServiceImpl.class, user);
		Map<String, Object> data = lsi.inLicense(request, response);
		//return com.alibaba.fastjson.JSONObject.toJSONString(data);
		return JSONObject.fromObject(data).toString();
//		return returnObj.toString();
	}



	/**
	 * 提交license，无需登录验证
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@POST
	@Path("/LicenseOperation")
	@Produces(MediaType.TEXT_PLAIN)
	public String submitLicense(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		FileUpload fu = new FileUpload(request, false);
		FileManage fm = new FileManage();
		String code = Util.null2String(fu.getParameter("code")).trim();
		char[] c_code = new char[16];
		FileReader fr = null;

		String message = "1";
		JSONObject returnObj = new JSONObject();

		LN ln = new LN();
		JSONObject json = new JSONObject();

		try {
			message = ln.CkLicense(TimeUtil.getCurrentTimeString());
		}catch (Exception e){}
		if("1".equals(message)){
			if(!isLogin(request, response)){
				return msgObject.toString();
			}
			if(user != null){
				boolean canEdit = HrmUserVarify.checkUserRight("SystemSetEdit:Edit", user) ;
				if(!canEdit){
					json.put("showType", "msg");
					json.put("status", false);
					json.put("error", SystemEnv.getHtmlLabelName(512047,user == null ? weaver.general.ThreadVarLanguage.getLang() : user.getLanguage()));
					return json.toString();
				}
			}
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("fileExtendName", "");
		data.put("fileid", "");
		data.put("filelink", "");
		data.put("filename", "");
		data.put("filesize", "");
		data.put("imgSrc", "");
		data.put("loadlink", "");
		data.put("showLoad", false);
		data.put("showDelete", false);
		data.put("isImg", false);
		RecordSet rs = new RecordSet();
		try {
			fr = new FileReader(GCONST.getRootPath() + File.separator + "WEB-INF" + File.separator + "code.key");
			fr.read(c_code);
			String realcode = new String(c_code).trim();
			if (code.equals("")) { // message=7 表示code为空或者upload出错
				message = "7";
				data.put("message", message);
				returnObj.put("data", data);
				return returnObj.toString();
			}
			if (!realcode.equals(code) && !code.equals("")) {
				message = SystemEnv.getHtmlLabelName(129308, 7);
				data.put("message", message);
				returnObj.put("data", data);
				return returnObj.toString();
			}


			FunctionUpgrade functionUpgrade = new FunctionUpgrade();

			// message=0 表示License信息错误；message=1 表示成功；message=2 表示数据库连接或者执行出错；message=3 表示License文件上传出错；
			// message=4 表示License信息错误，License过期；message=5 表示License信息错误，注册用户数大于License申请人数；message=6 表示选择的License文件不正确
			int fileid = 0;
			fileid = Util.getIntValue(fu.uploadFiles("file"), 0);

			String sql = "select isaesencrypt,aescode,filerealpath from imagefile where imagefileid = ?";
			boolean r1 = rs.executeQuery(sql, fileid);

			if (!r1) {
				message = "2";
			}

			String uploadfilepath = "";
			String isaesencrypt = "";
			String aescode = "";
			if (rs.next()) {
				uploadfilepath = rs.getString("filerealpath");
				isaesencrypt = rs.getString("isaesencrypt");
				aescode = rs.getString("aescode");
			}
			if (!uploadfilepath.equals("")) {
				try{
					String cid = Util.null2String(ln.getCid());
					//if (!cid.equals("")) {
					String licensefilepath2 = GCONST.getRootPath()+"license"+File.separatorChar+ln.OutLicensecode()+".license" ;
					String licensefilepathOriBak = GCONST.getRootPath()+"license"+File.separatorChar+ln.OutLicensecode()+"_bak.license" ;
					try{
						fm.copy(licensefilepath2,licensefilepathOriBak);
					}catch(Exception e){}
					fm.copy(uploadfilepath,licensefilepath2,isaesencrypt,aescode);
					String ncid = ln.ReadFromFile2(licensefilepath2);
					if(!cid.equals("") && (!ncid.equals(cid) || ncid.equals(""))){
						message = "-99";
					}else{
						ln.removeLicenseComInfo();
						ln.ReadFromFile(licensefilepath2);
						message = ln.InLicense();
					}
					// fm.DeleteFile(licensefilepath2);
					//	}
					//	if (!message.equals("-99")) {
					if(message.equals("1")){
						String licensefilepath = GCONST.getRootPath() + "license" + File.separatorChar + ln.OutLicensecode() + ".license";
						fm.copy(uploadfilepath, licensefilepath, isaesencrypt, aescode);
						fm.DeleteFile(uploadfilepath);
						ln.removeLicenseComInfo();
						ln.ReadFromFile(licensefilepath);
						message = ln.InLicense();

						functionUpgrade.doUpgrade();
					} else {
						try{
							fm.copy(licensefilepathOriBak,licensefilepath2);
						}catch(Exception e){}
						ln.removeLicenseComInfo();
						ln.ReadFromFile(licensefilepath2);
						if("".equals(message) || "-99".equals(message)){
							//System.out.println(".22222..............................message------>"+message);
							rs.writeLog("...............................message------>"+message);
							//message = "0";
						}
					}
				}finally{
					try{
						fm.DeleteFile(uploadfilepath);
						//rs.executeUpdate("delete from imagefile where imagefileid = ?", fileid);
					}catch(Exception e){}
				}
			}

			data.put("message", message);
			returnObj.put("data", data);
		} catch (Exception e) {
			rs.writeLog(e);
			message = "3"; // message=3 表示License文件上传出错
			data.put("message", message);
			returnObj.put("data", data);
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return returnObj.toString();
	}

	/**
	 * 更改验证码，无需登录验证
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@POST
	@Path("/CodeOperation")
	@Produces(MediaType.TEXT_PLAIN)
	public String changeCode(@Context HttpServletRequest request, @Context HttpServletResponse response) {

		LN ln = new LN();
		JSONObject json = new JSONObject();

		String message = "0";
		try {
			message = ln.CkLicense(TimeUtil.getCurrentTimeString());
		}catch (Exception e){}
		if("1".equals(message)){
			if(!isLogin(request, response)){
				return msgObject.toString();
			}
			if(user != null){
				boolean canEdit = HrmUserVarify.checkUserRight("SystemSetEdit:Edit", user) ;
				if(!canEdit){
					json.put("showType", "msg");
					json.put("status", false);
					json.put("error", SystemEnv.getHtmlLabelName(512047,user == null ? weaver.general.ThreadVarLanguage.getLang() : user.getLanguage()));
					return json.toString();
				}
			}
		}

		String code = Util.null2String(request.getParameter("passwordold"));
		String newcode = Util.null2String(request.getParameter("passwordnew")).trim();
		char[] c_code = new char[16];
		FileReader fr = null;
		FileWriter fw = null;

		JSONObject returnObj = new JSONObject();
		try {
			fr = new FileReader(GCONST.getRootPath() + File.separator + "WEB-INF" + File.separator + "code.key");
			fr.read(c_code);
			String realcode = new String(c_code).trim();
			if (!realcode.equals(code)) {
				returnObj.put("message", "1");
			} else {
				fw = new FileWriter(GCONST.getRootPath() + File.separator + "WEB-INF" + File.separator + "code.key");
				fw.write(newcode);
				returnObj.put("message", ""+weaver.systeminfo.SystemEnv.getHtmlLabelName(10005415,weaver.general.ThreadVarLanguage.getLang())+"!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return returnObj.toString();
	}

	@GET
	@Path("/downloadVersion")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public void downloadVersion(@Context HttpServletRequest request, @Context HttpServletResponse response){
		User user = HrmUserVarify.getUser(request, response);
		if(user == null){
			return ;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = sdf.format(new Date());
		String sql = "select companyname,cversion from license";
		RecordSet rs = new RecordSet();
		rs.executeQuery(sql);
		String companyname = "";
		String cversion = "";
		if(rs.next()){
			companyname =  rs.getString("companyname");
			cversion = rs.getString("cversion");
		}
		String fileName = companyname+""+weaver.systeminfo.SystemEnv.getHtmlLabelName(19587,weaver.general.ThreadVarLanguage.getLang())+""+date;
		try {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (request.getHeader("USER-AGENT").toLowerCase().indexOf("firefox")>=0) {
			try {
				fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		String info = companyname+"#&VERSION_SPLITTAG&#"+cversion;
		String encodeInfo =  SecurityHelper.encrypt(SecurityHelper.KEY,info);
		OutputStream output = null;
		try {
			response.reset();
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.addHeader("Content-Disposition", "attachment;filename="+fileName);
			response.addHeader("Content-Length", "" + encodeInfo.length());
			response.setContentType("application/octet-stream;charset=UTF-8");
			output = response.getOutputStream();
			output.write(encodeInfo.getBytes());
		} catch (IOException ex) {
			ex.printStackTrace();
		}finally{
			if(output!=null){
				try{
					output.close();
					output.flush();
				}catch (Exception e){

				}
			}
		}
	}

	/**
	 * 高级搜索条件
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/searchCondition")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSearchConditon(@Context HttpServletRequest request, @Context HttpServletResponse response){
		if(!isLogin(request, response)){
			return msgObject.toString();
		}
		lsi = (LicenseServiceImpl) ServiceUtil.getService(LicenseServiceImpl.class, user);
		return com.alibaba.fastjson.JSONObject.toJSONString(lsi.getCondition(request, response));
//		return JSONObject.fromObject(lsi.getCondition(request, response)).toString();
	}

	/**
	 * 集群节点列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/listLicense")
	@Produces(MediaType.TEXT_PLAIN)
	public String listLicense(@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception{
		JSONObject returnObj = new JSONObject();
		if(!isLogin(request, response)){
			return msgObject.toString();
		}

		boolean canEdit = HrmUserVarify.checkUserRight("SystemSetEdit:Edit", user) ;

		int languageid = weaver.general.ThreadVarLanguage.getLang();
		int uid = 1;

		if(user != null){
			languageid = user.getLanguage();
			uid = user.getUID();
		}

		String ip = Util.null2String(request.getParameter("ip"));
		String licenseCode = Util.null2String(request.getParameter("licenseCode"));
		if(!ip.matches("^[0-9a-zA-Z\\.:]+$")){
			ip = "";
		}
		if(!licenseCode.matches("^[a-zA-Z0-9]{0,32}$")){
			licenseCode = "";
		}
		lsi = (LicenseServiceImpl) ServiceUtil.getService(LicenseServiceImpl.class, user);
		String tabletype="checkbox";
		String  operateString= "";
		String pageId = "License:LicenseList20191008";
		operateString = "<operates width=\"20%\">";
		operateString+=" <popedom transmethod=\"weaver.ln.LicenseList.getOperate\"></popedom> ";
		operateString+="<operate href=\"javascript:submitLicense();\" text=\""+SystemEnv.getHtmlLabelName(18640,languageid)+"\" index=\"0\"/>";
		operateString+="</operates>";
		if(!canEdit){
			operateString = "";
		}

		String tableString=""+
				"<table datasource=\"weaver.ln.LicenseList.getLicenseList\" pageId=\""+pageId+"\" pageUid=\""+pageId+"\" sourceparams=\"ip:"+ip+"+licenseCode:"+licenseCode+"\" pagesize=\""+PageIdConst.getPageSize(pageId,uid,PageIdConst.DOC)+"\" tabletype=\"none\">"+
				"<sql backfields=\"*\" sqlform=\"tmpTable\" sqlsortway=\"asc\" sqlorderby=\"id\" sqlprimarykey=\"id\" />"+
				operateString+
				"<head>";
		if(user!=null && canEdit){
			tableString+=	 "<col width=\"10%\"  text=\""+SystemEnv.getHtmlLabelName(132196,languageid)+"\" column=\"addr\" />";
		}
		tableString+=	 "<col width=\"10%\"  text=\""+SystemEnv.getHtmlLabelName(16898,languageid)+"\" column=\"companyName\" />";
		tableString += "<col width=\"20%\" text=\""+SystemEnv.getHtmlLabelName(18639,languageid)+"\" column=\"licenseCode\" />";
		tableString += "<col width=\"10%\" text=\""+SystemEnv.getHtmlLabelName(511824,languageid)+"\" column=\"concurrentFlag\" />";
		tableString += "<col width=\"10%\" text=\""+SystemEnv.getHtmlLabelName(82496,languageid)+"\" column=\"srcCount\" />";
		tableString += "<col width=\"10%\" text=\""+SystemEnv.getHtmlLabelName(15029,languageid)+"\" column=\"hrmNum\" />";
		tableString += "<col width=\"10%\" text=\""+SystemEnv.getHtmlLabelName(515055,languageid)+"\" column=\"expireDate\" />";
		tableString += "<col width=\"10%\" text=\"license"+SystemEnv.getHtmlLabelName(21522,languageid)+"\" column=\"licensenum\" />";
		if(user!=null && canEdit) {
			tableString += "<col width=\"10%\" text=\"" + SystemEnv.getHtmlLabelName(517206, languageid) + "\" column=\"onCount\" />";
		}
		tableString +=
				"</head>"+
						"</table>";


		String sessionkey = pageId + "_" + Util.getEncrypt(Util.getRandom());
		Util_TableMap.setVal(sessionkey, tableString);
		returnObj.put("sessionkey", sessionkey);
		returnObj.put("status",true);
		returnObj.put("title",SystemEnv.getHtmlLabelName(18014, user.getLanguage()));
		return returnObj.toString();

	}



	/**
	 * 集群提交
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */

	@GET
	@Path("/ln/LNServlet")
	@Produces(MediaType.TEXT_PLAIN)
	public String getLnServlet(@Context HttpServletRequest req, @Context HttpServletResponse res){
		return lnServlet(req, res);
	}

	@POST
	@Path("/ln/LNServlet")
	@Produces(MediaType.TEXT_PLAIN)
	public String lnServlet(@Context HttpServletRequest req, @Context HttpServletResponse res){
		String path = req.getRequestURI();
		JSONObject returnObj = new JSONObject();

		int languageid = weaver.general.ThreadVarLanguage.getLang();
		if(user != null){
			languageid = user.getLanguage();
		}else{
			languageid = Util.getIntValue(req.getParameter("languageId"), weaver.general.ThreadVarLanguage.getLang());
		}
		if(path!=null && path.indexOf("/weaver/ln.")!=-1){
			returnObj.put("status", false);
			returnObj.put("showType", "msg");
			returnObj.put("error", SystemEnv.getHtmlLabelName(500472, languageid));
			return returnObj.toString();
		}
		String clientIp = Util.null2String(req.getRemoteAddr());
		String serverNodes = Util.null2String(new BaseBean().getPropValue("weaver", "initial_hosts"));
		JSONObject json = new JSONObject();
		FileUpload fu = new FileUpload(req,false);
		String method = Util.null2String(fu.getParameter("method"));
		res.setContentType("text/html; charset=utf-8");
		User user = HrmUserVarify.checkUser(req,res);

		if(user!=null){
			languageid = user.getLanguage();
		}
		LicenseList ll = new LicenseList();
		if(serverNodes.equals("")){
			json.put("status", false);
			json.put("showType", "msg");
			json.put("error", SystemEnv.getHtmlLabelName(515526,languageid));
			//res.getWriter().print(json.toString());
			return json.toString();
		}else if("getLicenseInfo".equals(method)||"updateLicenseInfo".equals(method)||"updateCode".equals(method) ){
			if(!ll.isInServerList(clientIp)){
				json.put("status", false);
				json.put("showType", "msg");
				json.put("error", SystemEnv.getHtmlLabelName(515527,languageid));
				//res.getWriter().print(json.toString());
				return json.toString();
			}
		}
		List<String> serverNodeList = Arrays.asList(serverNodes.split(","));
		LN ln = new LN();



		if("getLicenseInfo".equals(method)){//获取license信息
			int usedHrmnum = 0;
			int unUsedHrmnum = 0;
			try{
				ln.CkHrmnum();
				usedHrmnum = ln.CkUsedHrmnum();
				unUsedHrmnum = ln.CkUnusedHrmnum();
			}catch(Exception e){
				new BaseBean().writeLog(e);
			}
			json.put("licenseCode", ln.getLicensecode());
			String hrmNum = ln.getHrmnum();
			int concurrentFlag = Util.getIntValue(ln.getConcurrentFlag(),0);
			json.put("concurrentFlag",concurrentFlag == 1 ? SystemEnv.getHtmlLabelName(512576,languageid):SystemEnv.getHtmlLabelName(31691,languageid));//用户并发数标识
			json.put("licensenum", usedHrmnum); //已使用的license数量
			json.put("unusedlice",unUsedHrmnum) ;//未license已使用数量
			json.put("srcCount", ln.getScCount());
			json.put("srcType", ln.getScType());
			if(hrmNum.equals("999999"))hrmNum=SystemEnv.getHtmlLabelName(18637,languageid);
			json.put("hrmNum", hrmNum);
			json.put("companyName", ln.getCompanyname());
			String expireDate = ln.getExpiredate();
			if(expireDate.equals("9999-99-99"))expireDate=SystemEnv.getHtmlLabelName(18638,languageid);
			json.put("expireDate", expireDate);
			String addr = getIpAddress();

			FunctionUpgrade fup=new FunctionUpgrade();
			json.put("onCount",Util.null2String(fup.getCount().get("onCount")));
			json.put("addr", addr);
			return json.toString();
			//res.getWriter().print(json.toString());
		}else if("submitLicense".equals(method)){//提交license
			//先上传License到共享目录
			HttpSession session = req.getSession();

			String message = "0";
			try {
				message = ln.CkLicense(TimeUtil.getCurrentTimeString());
			}catch (Exception e){}
			if("1".equals(message)){
				if(!isLogin(req, res)){
					return msgObject.toString();
				}
				if(user != null){
					boolean canEdit = HrmUserVarify.checkUserRight("SystemSetEdit:Edit", user) ;
					if(!canEdit){
						//res.sendRedirect("/notice/noright.jsp");
						json.put("showType", "msg");
						json.put("status", false);
						json.put("error", SystemEnv.getHtmlLabelName(512047,languageid));
						return json.toString();
					}
				}
			}

			int fileid = 0 ;
			RecordSet rs = new RecordSet();
			try{
				fileid = Util.getIntValue(fu.uploadFiles("file"),-1);
				if(fileid == -1){
					message = "3";
				}else{
					String sql = "select isaesencrypt,aescode,filerealpath from imagefile where imagefileid = ?";
					boolean r1 = rs.executeQuery(sql,fileid);
					if(!r1){
						message = "2";
					}else{
						try {
							String addr = Util.null2String(fu.getParameter("addr"));
							if(!"".equals(addr)){
								String node = "";
								for(String n : serverNodeList){
									if(n.indexOf(addr)!=-1){
										node = n;
									}
								}
								JSONObject params = new JSONObject();
								params.put("method", "updateLicenseInfo");
								params.put("code", fu.getParameter("code"));
								params.put("fileid", fileid);
								JSONObject info = JSONObject.fromObject(HttpClientUtil.PostMethodTest("http://"+node+"/api/system/license/ln/LNServlet", params));
								JSONObject data = info.getJSONObject("data");
								//System.out.println(">>>>>>>>>>>>>>data>>>>>>>>>>>"+data+">>>>>>info>>>>"+info);
								message = data.getString("message");
								data.put("fileid", fileid);
								returnObj.put("data",data);
							}else{
								message = "3";
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							new BaseBean().writeLog(e);
						}
					}
				}
			}catch(Exception e){
				message = "3";
				JSONObject data = new JSONObject();
				data.put("message",message);
				returnObj.put("data",data);
				new BaseBean().writeLog(e);
			}
			//res.sendRedirect("/system/InLicense.jsp?addr="+xssUtil.put(fu.getParameter("addr"))+"&message="+message+"&fileid="+xssUtil.put(""+fileid)+"&code="+xssUtil.put(fu.getParameter("code")) );
//			returnObj.put("message", message);
//            returnObj.put("fileid", fileid);
			return returnObj.toString();
		}else if("reUpdateLicense".equals(method)){//确定提交license
			//先上传License到共享目录
			HttpSession session = req.getSession();

			String message = "0";
			try {
				message = ln.CkLicense(TimeUtil.getCurrentTimeString());
			}catch (Exception e){}
			if("1".equals(message)){
				if(!isLogin(req, res)){
					return msgObject.toString();
				}
				if(user != null){
					boolean canEdit = HrmUserVarify.checkUserRight("SystemSetEdit:Edit", user) ;
					if(!canEdit){
						json.put("showType", "msg");
						json.put("status", false);
						json.put("error", SystemEnv.getHtmlLabelName(512047,languageid));
						return json.toString();
					}
				}
			}

			String addr = Util.null2String(fu.getParameter("addr"));
			int fileid = Util.getIntValue(fu.getParameter("fileid"));
			RecordSet rs = new RecordSet();
			try{
				//fileid = Util.getIntValue(fu.uploadFiles("license"),-1);
				if(fileid == -1){
					message = "3";
				}else{
					String sql = "select isaesencrypt,aescode,filerealpath from imagefile where imagefileid = ?";
					boolean r1 = rs.executeQuery(sql,fileid);
					if(!r1){
						message = "2";
					}else{
						try {
							if(!"".equals(addr)){
								String node = "";
								for(String n : serverNodeList){
									if(n.indexOf(addr)!=-1){
										node = n;
									}
								}
								JSONObject params = new JSONObject();
								params.put("method", "updateLicenseInfo");
								params.put("code", fu.getParameter("code"));
								params.put("isReUpdate", "1");
								params.put("fileid", fileid);
//								System.out.println(fileid+">>>>>>>"+addr+">>>>>>>>>>"+serverNodeList+"==============http://"+node+"/ln/LNServlet");
								JSONObject info = JSONObject.fromObject(HttpClientUtil.PostMethodTest("http://"+node+"/api/system/license/ln/LNServlet", params));
								JSONObject data = info.getJSONObject("data");
								//System.out.println(">>>>>>>>>>>>>>data>>>>>>>>>>>"+data+">>>>>>info>>>>"+info);
								message = data.getString("message");
							}else{
								message = "3";
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							message = "3";
							new BaseBean().writeLog(e);
						}
					}
				}
			}catch(Exception e){
				message = "3";
				new BaseBean().writeLog(e);
			}
			if("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))){
				JSONObject params = new JSONObject();
				params.put("message", message);
				//res.setContentType("text/html; charset=utf-8");
				//res.getWriter().println(params.toString());
				return params.toString();
			}
		}else if("updateLicenseInfo".equals(method)){//更新license信息
			res.setContentType("text/html; charset=utf-8");
			//res.getWriter().println(new LicenseList().submitLicense(req, res));
			return new LicenseList().submitLicense(req, res);
		}else if("updateCode".equals(method)){//修改验证码
			//res.setContentType("text/html; charset=utf-8");
			//res.getWriter().println(new LicenseList().updateCode(req, res));
			return new LicenseList().updateCode(req, res);
		}else if("modifyCode".equals(method)){//修改验证码

			HttpSession session = req.getSession();
			//String randomCode = Util.null2String(fu.getParameter("IL_random_code"));
			//String sessionRandomCode = Util.null2String(session.getAttribute("IL_random_code"));
			//session.removeAttribute("IL_random_code");
			//if(randomCode.equals("") || !randomCode.equals(sessionRandomCode)){
			//	res.sendError(403);
			//	return;
			//}

			if(!isLogin(req, res)){
				return msgObject.toString();
			}
			if(user != null){
				boolean canEdit = HrmUserVarify.checkUserRight("SystemSetEdit:Edit", user) ;
				if(!canEdit){
					json.put("showType", "msg");
					json.put("status", false);
					json.put("error", SystemEnv.getHtmlLabelName(512047,languageid));
					return json.toString();
				}
			}
			String message = "";
			String addr = Util.null2String(fu.getParameter("addr"));
			try {
				if(!"".equals(addr)){
					String node = "";
					for(String n : serverNodeList){
						if(n.indexOf(addr)!=-1){
							node = n;
						}
					}
					JSONObject params = new JSONObject();
					params.put("method", "updateCode");
					params.put("passwordold", fu.getParameter("passwordold"));
					params.put("passwordnew", fu.getParameter("passwordnew"));
					params.put("confirmpassword", fu.getParameter("confirmpassword"));
					JSONObject info = JSONObject.fromObject(HttpClientUtil.PostMethodTest("http://"+node+"/api/system/license/ln/LNServlet", params));
					//System.out.println(">>>>>>>>>>>>>>data>>>>>>>>>>>"+data+">>>>>>info>>>>"+info);
					message = info.getString("message");
				}else{
					message = "3";
				}
				//res.sendRedirect("/system/ModifyCode.jsp?addr="+new XssUtil().put(addr)+"&message="+message);
				json.put("message", message);
				return json.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				new BaseBean().writeLog(e);

				//res.sendRedirect("/system/ModifyCode.jsp?addr="+new XssUtil().put(addr)+"&message="+message);
				json.put("message", message);
				return json.toString();
			}

		}
		json.put("status", false);
		json.put("showType", "msg");
		json.put("error", SystemEnv.getHtmlLabelName(500472,languageid));
		return json.toString();
	}

	/**
	 * 是否集群
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/isCluster")
	@Produces(MediaType.TEXT_PLAIN)
	public String isCluster(@Context HttpServletRequest req, @Context HttpServletResponse res){
		JSONObject returnObj = new JSONObject();
		if(!isLogin(req, res)){
			return msgObject.toString();
		}
		String serverNodes = Util.null2String(new BaseBean().getPropValue("weaver", "initial_hosts"));
		returnObj.put("isCluster", !serverNodes.equalsIgnoreCase(""));
		return returnObj.toString();
	}

	public static String getIpAddress() {
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			LicenseList ll = new LicenseList();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
					continue;
				} else {
					Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
					while (addresses.hasMoreElements()) {
						ip = addresses.nextElement();
						if (ip != null && ip instanceof Inet4Address) {
							if(ll.isInServerList(ip.getHostAddress())){
								return ip.getHostAddress();
							}

						}
					}
				}
			}
		} catch (Exception e) {
			new BaseBean().writeLog(e);
		}
		return "";
	}




}
