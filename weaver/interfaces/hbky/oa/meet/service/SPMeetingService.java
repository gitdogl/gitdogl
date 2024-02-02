package weaver.interfaces.hbky.oa.meet.service;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import weaver.general.Util;
import weaver.interfaces.hbky.oa.meet.dto.MeetUser;
import weaver.interfaces.hbky.oa.meet.dto.Meeting;
import weaver.interfaces.hbky.oa.meet.util.SPMeetUtils;
import weaver.interfaces.hbky.oa.meet.util.HttpUtils;

public class SPMeetingService {
	SPMeetUtils czyUtils = new SPMeetUtils();
	private String PACKAGE = czyUtils.getPackageurl(); // "rest/app/pay/";

	/**
	 * 3.6登录
	 * 
	 * @throws IOException
	 */

	public String login() throws IOException {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("account", "admin");
		paramsMap.put("password", "d033e22ae348aeb5660fc2140aec35850c4da997");
		paramsMap.put("deviceType", "WEB");
		PACKAGE = "api/rest/v2.0";
		String actionName = "/login";
		String requestMethod = "PUT";
		String res = HttpUtils.getHttpClientConnection(paramsMap, actionName,
				PACKAGE, requestMethod);

		System.out.println("******login****res:" + res);
		JSONObject jsonObject = JSONObject.parseObject(res);
		String token = jsonObject.get("token").toString();
		System.out.println("token" + token);
		return token;
		// System.out.println("***************res:" + res);
	}

	/**
	 * 3.6登录
	 * 
	 * @throws IOException
	 */

	public String idleSipNumber(String token) throws IOException {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		// paramsMap.put("token", token);
		PACKAGE = "api/rest/v2.0";
		String actionName = "/idleSipNumber?token=" + token;
		String requestMethod = "GET";
		String res = HttpUtils.getHttpClientConnection(paramsMap, actionName,
				PACKAGE, requestMethod);
		System.out.println("idleSipNumberres:" + res);
		JSONObject jsonObject = JSONObject.parseObject(res);
		String sipUrl = jsonObject.get("sipUrl").toString();
		System.out.println("sipUrl" + sipUrl);
		return sipUrl;

	}

	/**
	 * 3.6添加用户
	 * 
	 * @throws IOException
	 */

	public String Adduser(String token, MeetUser muser) throws IOException {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("name", muser.getName());
		paramsMap.put("password", muser.getPassword());
		paramsMap.put("displayName", muser.getDisplayname() + " ");
		paramsMap.put("email", muser.getEmail());
		paramsMap.put("telephone", muser.getTelephone());
		paramsMap.put("cellphone", muser.getCellphone());
		paramsMap.put("description", muser.getDescription());
		paramsMap.put("sipUserName", muser.getSipusername());
		paramsMap.put("sipPassword", muser.getSippassword());
		paramsMap.put("deviceType", muser.getDevicetype());
		paramsMap.put("company", muser.getCompany());
		paramsMap.put("systemManager", "false");
		paramsMap.put("status", muser.getStatus());
		PACKAGE = "api/rest/v2.0";
		String actionName = "/users?token=" + token;
		String requestMethod = "POST";
		String res = HttpUtils.getHttpClientConnection(paramsMap, actionName,
				PACKAGE, requestMethod);
		System.out.println("Adduserres:" + res);
		JSONObject jsonObject = JSONObject.parseObject(res);
		String id = jsonObject.get("id").toString();
		System.out.println("id" + id);
		return id;
	}

	/**
	 * 3.6添加用户
	 * 
	 * @throws IOException
	 */

	public String AddMeet(String token, Meeting metting) throws Exception {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("startTime", metting.getStarttime());
		paramsMap.put("duration", metting.getDuration());
		paramsMap.put("name", metting.getName());
		paramsMap.put("confPassword", metting.getConfpassword());
		paramsMap.put("confDefinition", metting.getConfdefinition());
		paramsMap.put("remarks", metting.getRemarks());
		paramsMap.put("layout", metting.getLayout());
		paramsMap.put("userIds", JSONArray.parseArray(metting.getUserids()));
		paramsMap.put("confType", metting.getConftype());
		paramsMap.put("maxBandwidth", metting.getMaxbandwidth());
		paramsMap.put("autoRedialing", metting.isAutoredialing());
		paramsMap.put("endpoints", new JSONArray());
		paramsMap.put("confflictEndpointIds", new JSONArray());
		paramsMap.put("numericId", 0);
		paramsMap.put("enableLiveStreaming", false);
		paramsMap.put("enableRecording", false);
		paramsMap.put("recordingLayout", 0);
		paramsMap.put("masterEndpointId", 0);
		paramsMap.put("contact", metting.getContact());
		paramsMap.put("contactPhone", "");
		paramsMap.put("liveStreamingUrl", "");

		PACKAGE = "api/rest/v2.0";
		String actionName = "/meetings?token=" + token;
		String requestMethod = "POST";
		String res = HttpUtils.getHttpClientConnection(paramsMap, actionName,
				PACKAGE, requestMethod);
		String id = "";
		try {
			System.out.println("AddMeetres:" + res);
			JSONObject jsonObject = JSONObject.parseObject(res);
			id = jsonObject.get("id").toString();
			System.out.println("id" + id);
		} catch (Exception s) {
			s.printStackTrace();
			throw new Exception("新建会议出错，请联系系统管理员");
		}
		return id;
	}

	/**
	 * 3.6添加用户
	 * 
	 * @throws IOException
	 */

	public String EditMeet(String token, Meeting metting) throws Exception {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("id", metting.getId());
		paramsMap.put("startTime", metting.getStarttime());
		paramsMap.put("duration", metting.getDuration());
		paramsMap.put("name", metting.getName());
		paramsMap.put("confPassword", metting.getConfpassword());
		paramsMap.put("confDefinition", metting.getConfdefinition());
		paramsMap.put("remarks", metting.getRemarks());
		paramsMap.put("layout", metting.getLayout());
		paramsMap.put("userIds", JSONArray.parseArray(metting.getUserids()));
		paramsMap.put("confType", metting.getConftype());
		paramsMap.put("maxBandwidth", metting.getMaxbandwidth());
		paramsMap.put("autoRedialing", metting.isAutoredialing());
		paramsMap.put("endpoints", new JSONArray());
		paramsMap.put("confflictEndpointIds", new JSONArray());
		paramsMap.put("numericId", 0);
		paramsMap.put("enableLiveStreaming", false);
		paramsMap.put("enableRecording", false);
		paramsMap.put("recordingLayout", 0);
		paramsMap.put("masterEndpointId", 0);
		paramsMap.put("contact", metting.getContact());
		paramsMap.put("contactPhone", "");
		paramsMap.put("liveStreamingUrl", "");

		PACKAGE = "api/rest/v2.0";
		String actionName = "/meetings/" + metting.getId() + "?token=" + token;
		String requestMethod = "PUT";
		String res = HttpUtils.getHttpClientConnection(paramsMap, actionName,
				PACKAGE, requestMethod);
		String id = metting.getId();
		try {
			System.out.println("EditMeetres:" + res);
			JSONObject jsonObject = JSONObject.parseObject(res);
			id = jsonObject.get("id").toString();
			System.out.println("id" + id);
		} catch (Exception s) {
			s.printStackTrace();
			throw new Exception("会议：" + metting.getName() + "已经开始！不允许修改。");
		}
		return id;
	}

	/**
	 * 3.6添加用户
	 * 
	 * @throws IOException
	 */

	public String DelMeet(String token, Meeting metting) throws IOException {
		Map<String, Object> paramsMap = new HashMap<String, Object>();

		PACKAGE = "api/rest/v2.0";
		String actionName = "/meetings/" + metting.getId() + "?token=" + token;
		String requestMethod = "DELETE";
		String res = HttpUtils.getHttpClientConnection(paramsMap, actionName,
				PACKAGE, requestMethod);
		System.out.println("DelMeetres:" + res);
		JSONObject jsonObject = JSONObject.parseObject(res);
		String result = jsonObject.get("result").toString();
		System.out.println("result" + result);
		return result;
	}

	/**
	 * 3.6添加用户
	 * 
	 * @throws IOException
	 */

	public List<String> GetMeetUserIDs(String token, String mettingId)
			throws IOException {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		List<String> userlist = new ArrayList<String>();
		PACKAGE = "api/rest/v2.0";
		String actionName = "/meetings/" + mettingId + "?token=" + token;
		String requestMethod = "GET";
		String res = HttpUtils.getHttpClientConnection(paramsMap, actionName,
				PACKAGE, requestMethod);
		System.out.println("GetMeetUserIDsres:" + res);
		JSONObject jsonObject = JSONObject.parseObject(res);
		String users = jsonObject.get("users").toString();
		JSONArray jsonArray = JSONArray.parseArray(users);
		JSONObject usobj = null;
		for (int i = 0; i < jsonArray.size(); i++) {
			usobj = jsonArray.getJSONObject(i);
			userlist.add(usobj.get("id").toString());
			System.out.println("userid=" + usobj.get("id").toString());
		}

		return userlist;
	}

	/**
	 * 3.6修改用户
	 * 
	 * @throws IOException
	 */

	public void Edituser(String token, MeetUser muser) throws IOException {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("id", muser.getId());
		paramsMap.put("name", muser.getName());
		paramsMap.put("vmr", muser.getVmr());
		paramsMap.put("sipAuthName", muser.getSipAuthName());
		paramsMap.put("displayName", muser.getDisplayname());
		paramsMap.put("email", muser.getEmail());
		paramsMap.put("telephone", muser.getTelephone());
		paramsMap.put("cellphone", muser.getCellphone());
		paramsMap.put("description", muser.getDescription());
		paramsMap.put("sipUserName", muser.getSipusername());
		paramsMap.put("sipPassword", muser.getSippassword());
		paramsMap.put("deviceType", muser.getDevicetype());
		paramsMap.put("company", muser.getCompany());
		paramsMap.put("systemManager", "false");
		paramsMap.put("status", muser.getStatus());
		PACKAGE = "api/rest/v2.0";
		String actionName = "/users/" + muser.getId() + "?token=" + token;
		String requestMethod = "PUT";
		String res = HttpUtils.getHttpClientConnection(paramsMap, actionName,
				PACKAGE, requestMethod);
		// System.out.println("***************res:" + res);
		JSONObject jsonObject = JSONObject.parseObject(res);
		String id = jsonObject.get("id").toString();
		// System.out.println("result" + id);

	}

	/**
	 * 3.6修改用户
	 * 
	 * @throws IOException
	 */

	public void Deluser(String token, String userid) throws IOException {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		PACKAGE = "api/rest/v2.0";
		String actionName = "/users/" + userid + "?token=" + token;
		String requestMethod = "DELETE";
		String res = HttpUtils.getHttpClientConnection(paramsMap, actionName,
				PACKAGE, requestMethod);
		// System.out.println("Deluserres:" + res);
		JSONObject jsonObject = JSONObject.parseObject(res);
		String result = jsonObject.get("result").toString();
		// System.out.println("result" + result);

	}

	/**
	 * 3.6登录
	 * 
	 * @throws IOException
	 */

	public MeetUser Getuser(String token, String musername) throws IOException {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		PACKAGE = "api/rest/v2.0";
		String actionName = "/users?token=" + token + "&userName=" + musername;
		String requestMethod = "GET";
		String res = HttpUtils.getHttpClientConnection(paramsMap, actionName,
				PACKAGE, requestMethod);
		// System.out.println("Getuserres:" + res);
		JSONArray jsonArray = JSONArray.parseArray(res);
		MeetUser muser = new MeetUser();
		String id = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			id = jsonObject.get("id").toString();
			muser.setId(Util.null2String(jsonObject.get("id")).toString());
			muser.setName(Util.null2String(jsonObject.get("name")).toString());
			muser.setPassword(Util.null2String(jsonObject.get("password"))
					.toString());
			muser.setDisplayname(Util
					.null2String(jsonObject.get("displayName")).toString()
					+ " ");
			muser.setEmail(Util.null2String(jsonObject.get("email")).toString());
			muser.setTelephone(Util.null2String(jsonObject.get("telephone"))
					.toString());
			muser.setCellphone(Util.null2String(jsonObject.get("cellphone"))
					.toString());
			muser.setDescription(Util
					.null2String(jsonObject.get("description")).toString()
					+ " ");
			muser.setCallNumber(Util.null2String(jsonObject.get("callNumber"))
					.toString());
			muser.setImageURL(Util.null2String(jsonObject.get("imageURL"))
					.toString());
			muser.setLastModifiedTime(Util.null2String(
					jsonObject.get("lastModifiedTime")).toString());
			muser.setSystemmanager(Util.null2String(
					jsonObject.get("systemmanager")).toString());
			muser.setVmr(Util.null2String(jsonObject.get("vmr")).toString());
			muser.setSipusername(Util
					.null2String(jsonObject.get("sipUserName")).toString());
			muser.setSipAuthName(Util
					.null2String(jsonObject.get("sipAuthName")).toString());
			muser.setSippassword(Util
					.null2String(jsonObject.get("sipPassword")).toString());
			muser.setStatus(Util.null2String(jsonObject.get("status"))
					.toString());
			muser.setPasswordModifiedByUser(Util.null2String(
					jsonObject.get("passwordModifiedByUser")).toString());
			muser.setCompany(Util.null2String(jsonObject.get("company"))
					.toString());
			muser.setCompanyShortName(Util.null2String(
					jsonObject.get("companyShortName")).toString());
			muser.setEndpointId(Util.null2String(jsonObject.get("endpointId"))
					.toString());

			// System.out.println("id" + id);
		}

		return muser;
	}

	/**
	 * 3.6登录
	 * 
	 * @throws IOException
	 */

	public MeetUser Getoneuser(String token, String usid) throws IOException {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		PACKAGE = "api/rest/v2.0";
		String actionName = "/users/" + usid + "?token=" + token;
		String requestMethod = "GET";
		String res = HttpUtils.getHttpClientConnection(paramsMap, actionName,
				PACKAGE, requestMethod);
		// System.out.println("***************res:" + res);
		JSONObject jsonObject = JSONObject.parseObject(res);
		MeetUser muser = new MeetUser();
		muser.setId(jsonObject.get("id").toString());
		muser.setName(jsonObject.get("name").toString());
		muser.setPassword(jsonObject.get("password").toString());
		muser.setDisplayname(jsonObject.get("displayName").toString());
		muser.setEmail(jsonObject.get("email").toString());
		muser.setTelephone(jsonObject.get("telephone").toString());
		muser.setCellphone(jsonObject.get("cellphone").toString());
		muser.setDescription(jsonObject.get("description").toString());
		muser.setCallNumber(jsonObject.get("callNumber").toString());
		muser.setImageURL(jsonObject.get("imageURL").toString());
		muser.setLastModifiedTime(jsonObject.get("lastModifiedTime").toString());
		muser.setSystemmanager(jsonObject.get("systemmanager").toString());
		muser.setVmr(jsonObject.get("vmr").toString());
		muser.setSipusername(jsonObject.get("sipUserName").toString());
		muser.setSipAuthName(jsonObject.get("sipAuthName").toString());
		muser.setSippassword(jsonObject.get("sipPassword").toString());
		muser.setStatus(jsonObject.get("status").toString());
		muser.setPasswordModifiedByUser(jsonObject
				.get("passwordModifiedByUser").toString());
		muser.setCompany(jsonObject.get("company").toString());
		// muser.setCompanyShortName(jsonObject.get("companyShortName").toString());
		muser.setEndpointId(jsonObject.get("endpointId").toString());

		return muser;
	}

}