//package test;
//
//import weaver.file.Prop;
//import weaver.general.Util;
//import weaver.interfaces.erp.PostWorkflowInfoThread;
//
//public class test {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		String sql="/SSOLogin.aspx?toSystemID=16d79677-ffd3-226a-70e1-48cd35a3d15c&gopage=/workflow/request/ViewRequest.jsp?requestid=cs_requestid|_workflowid=cs_workflowid|_workflowtype=|f_weaver_belongto_userid=cs_belongto_userid|f_weaver_belongto_usertype=0|isovertime=0";
//		String sqls=getWf_Url("6500","12","56");
//System.out.println("sddfwwadfss=="+sqls);
//
//	}
//	/**
//	 *
//	 * 获取PC端打开流程地址
//	 *
//	 * @param id
//	 * @param requestid
//	 * @return
//	 */
//	private static String getWf_Url(String requestid,String userid,String wfid) {
//		String pc_url ="/SSOLogin.aspx?toSystemID=16d79677-ffd3-226a-70e1-48cd35a3d15c&gopage=/workflow/request/ViewRequest.jsp?requestid=cs_requestid|_workflowid=cs_workflowid|_workflowtype=|f_weaver_belongto_userid=cs_belongto_userid|f_weaver_belongto_usertype=0|isovertime=0";
//
//		System.out.println("配置读取pc_url="+pc_url);
//		String tempurl = pc_url.replaceAll("cs_belongto_userid", userid).replaceAll("cs_requestid", requestid).replaceAll("cs_workflowid", wfid) ;
//		System.out.println("最终tempurl="+tempurl);
//		return tempurl;
//	}
//}
