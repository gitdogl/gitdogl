package weaver.interfaces.hbky.oa.meet.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("uncheck")
public class HttpUtils {

	// private static String URL = "http://192.168.0.146:8087/";//146内网
	// private static String URL = "http://114.247.220.10:8087/";//146外网
	// private static String URL = "https://114.113.112.166:7443/";//云快报生产
	// private static String URL = "http://114.113.112.166:9432/";//云快报生产
	// private static String URL = "http://localhost:9999/cbsp-cibclient/";
	// private static String URL = "http://192.168.0.140:8839/clientnew/";
	// private static String URL = "http://1.202.246.56:8001/client/";
	private static SPMeetUtils czyUtil = new SPMeetUtils();
	// private String URL =czyUtil.getCzjUrl();
	private static String PACKAGE = czyUtil.getPackageurl();

	/**
	 * @param paramsMap
	 * @param methodName
	 * @param params
	 * @return
	 */
	public static String getHttpClientConnection(Map<String, Object> paramsMap,
			String actionName, String packageName, String requestMethod) {
		PACKAGE = packageName;

		String paramStr = JSON.toJSONString(paramsMap);
		 System.out.println("paramStr" + paramStr);
		if ("GET".equals(requestMethod) || "DELETE".equals(requestMethod)) {
			if (paramsMap.size() == 0) {
				paramStr = "";
			}

		}
		String res = send_httpput(actionName, paramStr, requestMethod);
		return res;
	}

	private static String send_httpput(String actionName, String params,
			String requestMethod) {
		String resp = "";

		String urladd = czyUtil.getSpmeetUrl() + PACKAGE + actionName;
		try {
			URL url = new URL(urladd); // url地址
			// System.out.println("url"+urladd);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod(requestMethod);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Charsert", "UTF-8"); // 设置请求编码
			connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.connect();
			if (!"".equals(params)) {
				//params=	new String(params.getBytes(),"UTF-8");
				OutputStream os = connection.getOutputStream();
				os.write(params.getBytes("UTF-8"));
				os.flush();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			StringBuffer sbf = new StringBuffer();
			String lines;
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "UTF-8");
				sbf.append(lines);
			}
			resp = sbf.toString();
			connection.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// JSONObject json = (JSONObject) JSON.toJSON(resp);
		}
		return resp;

	}

}