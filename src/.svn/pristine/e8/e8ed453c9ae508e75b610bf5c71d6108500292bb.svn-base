package com.api.login.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.api.login.service.LoginService;
import com.api.login.service.impl.LoginServiceImpl;
import com.api.login.util.LoginLogUtil;
import com.api.login.util.LoginUtil;
import com.engine.common.util.ParamUtil;
import com.engine.common.util.ServiceUtil;
import com.engine.hrm.biz.HrmSanyuanAdminBiz;
import weaver.conn.RecordSet;
import weaver.file.Prop;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.hrm.settings.ChgPasswdReminder;
import weaver.hrm.settings.RemindSettings;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.rsa.security.RSA;
import weaver.sm.SM4Utils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统登录
 *
 * @author lvyi
 */
@Path("/hrm/login")
public class LoginAction extends BaseBean{

  private LoginService getService(String... loginid) {
    return (LoginService) ServiceUtil.getService(LoginServiceImpl.class);
  }

  private static Object lock = new Object();

  private static String key = "";

  static{
    BaseBean bb = new BaseBean();
    key = Util.null2String(bb.getPropValue("weaver_client_pwd","key"));
    SM4Utils sm4 = new SM4Utils();
    if("".equals(key)){
      key = sm4.createSecretKey();
      Prop.setPropValue("weaver_client_pwd", "key", key);
    }

  }

  /**
   * 获取登录表单
   *
   * @param request
   * @param response
   * @return
   */
  @POST
  @Path("/getLoginForm")
  @Produces(MediaType.TEXT_PLAIN)
  public String getLoginForm(@Context HttpServletRequest request, @Context HttpServletResponse response) {
    Map<String, Object> apidatas = new HashMap<String, Object>();
    try {
      apidatas = getService().getLoginForm(ParamUtil.request2Map(request), request);
    } catch (Exception e) {
      writeLog(e);
      apidatas.put("status", "-1");
    }
    return JSONObject.toJSONString(apidatas);
  }


  /**
   * 获取登录表单
   *
   * @param request
   * @param response
   * @return
   */
  @POST
  @Path("/checkLogin")
  @Produces(MediaType.TEXT_PLAIN)
  public String checkLogin(@Context HttpServletRequest request, @Context HttpServletResponse response) {
    Map<String, Object> apidatas = new HashMap<String, Object>();

    Map<String,Object> result =  getService().beforeDoLogin(ParamUtil.request2Map(request)) ;
    if(result != null && !"1".equals(result.get("status"))){
      return JSONObject.toJSONString(result);
    }

    try {
      RecordSet rs = new RecordSet();

      ServletContext application = request.getSession().getServletContext();
      ChgPasswdReminder reminder = new ChgPasswdReminder();
      RemindSettings settings = reminder.getRemindSettings();
      LoginUtil loginUtil = new LoginUtil();
      String[] usercheck = loginUtil.checkLogin(application,request,response);

      String loginid = Util.null2String(request.getParameter("loginid"));
      String user_password = Util.null2String(request.getParameter("userpassword"));
      //是否是手机端登录
      String isMobile = Util.null2String(request.getParameter("ismobile"));

      String isrsaopen = Util.null2String(rs.getPropValue("openRSA","isrsaopen"));
      List<String> decriptList = new ArrayList<>() ;

      RSA rsa = new RSA();
      if("1".equals(isrsaopen)){
        decriptList.add(loginid) ;
        decriptList.add(user_password) ;
        List<String> resultList = rsa.decryptList(request,decriptList) ;
        if(!rsa.getMessage().equals("0")){
          apidatas.put("status", "-1");
          writeLog("RSA 解密失败>>"+resultList+";from;"+decriptList);
          return JSONObject.toJSONString(apidatas);
        }

        loginid = resultList.get(0) ;
        user_password = resultList.get(1) ;

      }

      String sql = "select sumpasswordwrong from hrmresource where loginid=?";
      rs.executeQuery(sql,loginid);
      if(rs.next()){
        usercheck[3]=""+Util.getIntValue(rs.getString(1), 0);
      }else{
        usercheck[3]="0";
      }
      if(usercheck[1].equals("101")){
        apidatas.put("validitySec",settings.getValiditySec());
      }

      loginUtil.setIpAddress(Util.getIpAddr(request));
      if(isMobile.equals("1")){
        loginUtil.setClientType(2);
      }else{
        loginUtil.setClientType(1);
      }
      if(usercheck[0].equals("true")){
        SM4Utils sm4 = new SM4Utils();

        if(!user_password.endsWith("_random_")) {
          apidatas.put("user_token", sm4.encrypt(user_password, key)+"_random_");
        }
        User user = (User) request.getSession(true).getAttribute("weaver_user@bean");
        apidatas.put("userid",user.getUID());

        //三员管理员--参数 start
        boolean hasRight=HrmSanyuanAdminBiz.hasSanYuanRight(user);
        if(hasRight){
          apidatas.put("needJumpToBackstage","true");
        }
        //over

      }else{//记录登录失败日志
        if(!usercheck[1].equals("101")&&!usercheck[1].equals("57")) {
          //记录登录日志前找出登录账号对应的人员ID
          int userId = LoginLogUtil.getUserIdByLoginId(loginid, request);
          loginUtil.recordFailedLogin(userId, loginid, Util.null2String(usercheck[2]));
        }
      }
      apidatas.put("loginstatus",usercheck[0]);
      apidatas.put("msgcode",usercheck[1]);
      apidatas.put("msg",usercheck[2]);
      apidatas.put("sumpasswordwrong",usercheck[3]);
      apidatas.put("access_token",usercheck[4]);
    } catch (Exception e) {
      e.printStackTrace();
      apidatas.put("status", "-1");
    }
    return JSONObject.toJSONString(apidatas);
  }

  /**
   * 绑定动态令牌
   *
   * @param request
   * @param response
   * @return
   */
  @GET
  @Path("/getBindTokenKeyForm")
  @Produces(MediaType.TEXT_PLAIN)
  public String getBindTokenKeyForm(@Context HttpServletRequest request, @Context HttpServletResponse response) {
    Map<String, Object> apidatas = new HashMap<String, Object>();
    try {
      User user = (User) request.getSession(true).getAttribute("weaver_user@bean");
      apidatas = getService().getBindTokenKeyForm(ParamUtil.request2Map(request), request ,user);
    } catch (Exception e) {
      apidatas.put("status", "-1");
    }
    return JSONObject.toJSONString(apidatas);
  }

  /**
   * 保存绑定动态令牌
   *
   * @param request
   * @param response
   * @return
   */
  @POST
  @Path("/saveBindTokenKey")
  @Produces(MediaType.TEXT_PLAIN)
  public String saveBindTokenKey(@Context HttpServletRequest request, @Context HttpServletResponse response) {
    Map<String, Object> apidatas = new HashMap<String, Object>();
    try {
      User user = (User) request.getSession(true).getAttribute("weaver_user@bean");
      apidatas = getService().saveBindTokenKey(ParamUtil.request2Map(request), user);
    } catch (Exception e) {
      apidatas.put("status", "-1");
    }
    return JSONObject.toJSONString(apidatas);
  }

  /**
   * 是否需要修改密码
   *
   * @param request
   * @param response
   * @return
   */
  @POST
  @Path("/remindLogin")
  @Produces(MediaType.TEXT_PLAIN)
  public String remindLogin(@Context HttpServletRequest request, @Context HttpServletResponse response) {
    Map<String, Object> apidatas = new HashMap<String, Object>();
    try {
      User user = HrmUserVarify.getUser(request, response);
      apidatas = getService().remindLogin(ParamUtil.request2Map(request), user);
    } catch (Exception e) {
      apidatas.put("status", "-1");
    }
    return JSONObject.toJSONString(apidatas);
  }

  /**
   * 登出系统
   *
   * @param request
   * @param response
   * @return
   */
  @POST
  @Path("/checkLogout")
  @Produces(MediaType.TEXT_PLAIN)
  public String checkLogout(@Context HttpServletRequest request, @Context HttpServletResponse response) {
    Map<String, Object> apidatas = new HashMap<String, Object>();
    try {
      ServletContext application = request.getSession().getServletContext();
      Map<String, Object> result = new HashMap<>();
      new LoginUtil().checkLogout(application,request,response);

      //cas相关
      if (request.getAttribute("casLogoutUrl") != null) {
        apidatas.put("casLogoutUrl",(String)request.getAttribute("casLogoutUrl"));
      }

      apidatas.put("status", "1");
    } catch (Exception e) {
      apidatas.put("status", "-1");
    }
    return JSONObject.toJSONString(apidatas);
  }

  /**
   * 获取动态密码
   *
   * @param request
   * @param response
   * @return
   */
  @POST
  @Path("/getDynamicPassword")
  @Produces(MediaType.TEXT_PLAIN)
  public String getSMSPwd(@Context HttpServletRequest request, @Context HttpServletResponse response) {
    Map<String, Object> apidatas = new HashMap<String, Object>();
    try{
      Map<String, Object> params = ParamUtil.request2Map(request);

      String loginid = Util.null2String(request.getParameter("loginid"));
      String userpassword = Util.null2String(request.getParameter("userpassword"));
      String isrsaopen = Util.null2String(Prop.getInstance().getPropValue("openRSA", "isrsaopen"));
      List<String> decriptList = new ArrayList<>() ;
      if("1".equals(isrsaopen)){
        RSA rsa = new RSA();
        decriptList.add(loginid) ;
        decriptList.add(userpassword) ;
        List<String> resultList = rsa.decryptList(request,decriptList) ;
        loginid = resultList.get(0) ;
        userpassword = resultList.get(1) ;
        if(!rsa.getMessage().equals("0")){
          new BaseBean().writeLog("getDynamicPassword>>>>>>>>rsa.getMessage()", rsa.getMessage());
          return "184";
        }
      }
      if (userpassword.endsWith("_random_")) {
        SM4Utils sm4 = new SM4Utils();
        BaseBean bb = new BaseBean();
        String key = Util.null2String(bb.getPropValue("weaver_client_pwd", "key"));
        if (!"".equals(key)) {
          userpassword = userpassword.substring(0, userpassword.lastIndexOf("_random_"));
          userpassword = sm4.decrypt(userpassword, key);
        }
      }
      params.put("loginid",loginid);
      params.put("userpassword",userpassword);
      apidatas =  getService().getDynamicPassword(params,request);
    } catch (Exception e) {
      apidatas.put("status", "-1");
    }
    return JSONObject.toJSONString(apidatas);
  }

  /**
   * 切换主次账号
   *
   * @param request
   * @param response
   * @return
   */
  @POST
  @Path("/identityShift")
  @Produces(MediaType.TEXT_PLAIN)
  public String identityShift(@Context HttpServletRequest request, @Context HttpServletResponse response,@Context ServletContext application) {
    Map<String, Object> apidatas = new HashMap<String, Object>();
    try {
      apidatas =  getService().identityShift(ParamUtil.request2Map(request),request, response, application);
    } catch (Exception e) {
      apidatas.put("status", "-1");
    }
    return JSONObject.toJSONString(apidatas);
  }

  /**
   * 是否在免密时间内
   *
   * @param request
   * @return
   */
  @POST
  @Path("/getSecondAuthFreeTime")
  @Produces(MediaType.TEXT_PLAIN)
  public String getSecondAuthFreeTime(@Context HttpServletRequest request, @Context HttpServletResponse response) {
    Map<String, Object> apidatas = new HashMap<String, Object>();
    try {
      User user = HrmUserVarify.getUser(request, response);
      apidatas = getService().getSecondAuthFreeTime(ParamUtil.request2Map(request),user);
      apidatas.put("status", "1");
    } catch (Exception e) {
      writeLog(e);
      apidatas.put("status", "-1");
    }
    return JSONObject.toJSONString(apidatas);
  }

  /**
   * 获取二次验证表单
   *
   * @param request
   * @return
   */
  @POST
  @Path("/getSecondAuthForm")
  @Produces(MediaType.TEXT_PLAIN)
  public String getSecondAuthForm(@Context HttpServletRequest request, @Context HttpServletResponse response) {
    Map<String, Object> apidatas = new HashMap<String, Object>();
    try {
      User user = HrmUserVarify.getUser(request, response);
      apidatas = getService().getSecondAuthForm(ParamUtil.request2Map(request),user);
      apidatas.put("status", "1");
    } catch (Exception e) {
      writeLog(e);
      apidatas.put("status", "-1");
    }
    return JSONObject.toJSONString(apidatas);
  }

  /**
   * 二次验证
   *
   * @param request
   * @return
   */
  @POST
  @Path("/doSecondAuth")
  @Produces(MediaType.TEXT_PLAIN)
  public String doSecondAuth(@Context HttpServletRequest request, @Context HttpServletResponse response) {
    Map<String, Object> apidatas = new HashMap<String, Object>();
    try {
      User user = HrmUserVarify.getUser(request, response);
      apidatas = getService().doSecondAuth(ParamUtil.request2Map(request),request,user);
      if((boolean)apidatas.get("api_status")){
        request.getSession(true).setAttribute("doSecondAuthStatus","1");
      }
      apidatas.put("status", "1");
    } catch (Exception e) {
      writeLog(e);
      apidatas.put("status", "-1");
    }
    return JSONObject.toJSONString(apidatas);
  }

  /**
   * CA验证
   *
   * @param request
   * @return
   */
  @POST
  @Path("/saveCAAuth")
  @Produces(MediaType.TEXT_PLAIN)
  public String saveCAAuth(@Context HttpServletRequest request, @Context HttpServletResponse response) {
    Map<String, Object> apidatas = new HashMap<String, Object>();
    try {
      User user = HrmUserVarify.getUser(request, response);
      apidatas = getService().saveCAAuth(ParamUtil.request2Map(request),request,user);
      apidatas.put("status", "1");
    } catch (Exception e) {
      writeLog(e);
      apidatas.put("status", "-1");
    }
    return JSONObject.toJSONString(apidatas);
  }

  @POST
  @Path("/getSecDynamicPassword")
  @Produces(MediaType.TEXT_PLAIN)
  public String getSecDynamicPassword(@Context HttpServletRequest request, @Context HttpServletResponse response) {
    Map<String, Object> apidatas = new HashMap<String, Object>();
    try {
      User user = HrmUserVarify.getUser(request, response);
      apidatas = getService().getSecDynamicPassword(ParamUtil.request2Map(request),user);
      apidatas.put("status", "1");
    } catch (Exception e) {
      writeLog(e);
      apidatas.put("status", "-1");
    }

    return JSON.toJSONString(apidatas);
  }

  @POST
  @Path("/getPwdByWorkCode")
  @Produces(MediaType.TEXT_PLAIN)
  public String getPwdByWorkCode(@Context HttpServletRequest request, @Context HttpServletResponse response) {
    Map<String, Object> apidatas = new HashMap<String, Object>();
    RecordSet rs = new RecordSet();
    try {
      String workcode = Util.null2String(request.getParameter("workcode"));
      String password = "";
      String sql = " select password from hrmresource where loginid=? and status in (0,1,2,3) ";
      writeLog("getPwdByWorkCode>>>>>>>>>>>>>"+sql+" "+workcode);
      rs.executeQuery(sql,workcode);
      if(rs.next()){
        password = Util.null2String(rs.getString("password"));
      }
      if(password.length()>0){
        apidatas.put("password", password);
        apidatas.put("status", "1");
      }else{
        apidatas.put("msg", "获取密码失败");
        apidatas.put("status", "-1");
      }
    } catch (Exception e) {
      writeLog(e);
      apidatas.put("msg", "获取密码失败 error");
      apidatas.put("status", "-1");
    }
    return JSON.toJSONString(apidatas);
  }

  /**
   * 获取次账号信息
   * @param request
   * @param response
   * @return
   */
  @GET
  @Path("/getAccountList")
  @Produces(MediaType.TEXT_PLAIN)
  public String getAccountList(@Context HttpServletRequest request, @Context HttpServletResponse response) {
    Map<String, Object> apidatas = new HashMap<String, Object>();
    try {
      User user = HrmUserVarify.getUser(request, response);
      apidatas = getService().getAccountList(ParamUtil.request2Map(request),user);
      apidatas.put("status", "1");
    } catch (Exception e) {
      writeLog(e);
      apidatas.put("status", "-1");
    }

    return JSON.toJSONString(apidatas);
  }
  /**
   * 获取超时弹出登录界面弹框的开关
   * @param request
   * @param response
   * @return
   */
  @GET
  @Path("/getTimeOutSwitch")
  @Produces(MediaType.TEXT_PLAIN)
  public String isNeedDialog(@Context HttpServletRequest request, @Context HttpServletResponse response) {
    Map<String, Object> apidatas = new HashMap<String, Object>();
    try {
      ChgPasswdReminder reminder = new ChgPasswdReminder();
      RemindSettings settings = reminder.getRemindSettings();
      boolean isNeedDialog = "1".equals(settings.getTimeOutSwitch());
      if(isNeedDialog){
        apidatas.put("isNeedDialog", "true");
      }else{
        apidatas.put("isNeedDialog", "false");
      }
      apidatas.put("status", "1");
    } catch (Exception e) {
      writeLog(e);
      apidatas.put("status", "-1");
      apidatas.put("isNeedDialog", "false");
    }
    return JSON.toJSONString(apidatas);
  }
}
