package weaver.interfaces.hbky.oa.meet.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.general.BaseBean;

public class SPMeetUtils {

	private BaseBean bb = new BaseBean();

	// 获取财政云URL地址
	private String spmeetUrl = "";
	//获取财政云包地址
	private String packageurl="";
	//会议的url
	private String meeturl="";
	/**
	 * @return 财政云URL地址
	 * 
	 */
	public String getSpmeetUrl() {
		if (!"".equals(spmeetUrl)) {
			if (spmeetUrl == null || "".equals(spmeetUrl)) {
				spmeetUrl = bb.getPropValue("SPMEETSET", "spmeetUrl");
			}
		}
		if (spmeetUrl == null || "".equals(spmeetUrl)) {
			spmeetUrl = bb.getPropValue("SPMEETSET", "spmeetUrl");
		}
		if (spmeetUrl == null || "".equals(spmeetUrl)) {
			//czjUrl = "https://114.247.220.10:8839/";
			spmeetUrl = "http://172.30.1.42/";
		}
		return spmeetUrl;
	}
	/**
	 * @return 财政云URL地址
	 * 
	 */
	public String getMeeturl() {
		if (!"".equals(meeturl)) {
			if (meeturl == null || "".equals(meeturl)) {
				meeturl = bb.getPropValue("SPMEETSET", "meeturl");
			}
		}
		if (meeturl == null || "".equals(meeturl)) {
			meeturl = bb.getPropValue("SPMEETSET", "meeturl");
		}
		if (meeturl == null || "".equals(meeturl)) {
			//czjUrl = "https://114.247.220.10:8839/";
			meeturl = "/toControlMeeting.html?confid=";
		}
		return meeturl;
	}
	
	/**
	 * @return 财政云包路径地址
	 * 
	 */
	public String getPackageurl() {
		if (!"".equals(packageurl)) {
			if (packageurl == null || "".equals(packageurl)) {
				packageurl = bb.getPropValue("SPMEETSET", "packageurl");
			}
		}
		if (packageurl == null || "".equals(packageurl)) {
			packageurl = bb.getPropValue("SPMEETSET", "packageurl");
		}
		if (packageurl == null || "".equals(packageurl)) {
			packageurl = "api/rest/v2.0/";
		}
		return packageurl;
	}
	
	
	/**
	 * 日期转换
	 * @param date
	 * @param string
	 * @return
	 */
	public static String toString(Date date, String string) {
		// TODO Auto-generated method stub
		SimpleDateFormat time = new SimpleDateFormat(string);
		return time.format(date);
	}
	
	/**
	 * 日期转换
	 * @param date
	 * @param string
	 * @return
	 */
	public static String IsNullString( String string) {
		// TODO Auto-generated method stub
		if(string==null)
		{
			string="";
		}
		return string;
	}
}
