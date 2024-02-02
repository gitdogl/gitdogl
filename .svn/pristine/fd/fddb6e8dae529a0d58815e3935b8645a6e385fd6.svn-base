//package test;
//
//import org.json.JSONObject;
//
//
//
//public class TestInterface {
//	public static void main(String[] args) {
//		try {
//			String data = "{code:\"4\",data:\"34930此用户在IDM中没有关联账号\"}";
//			parseResponseData(data);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	// {code:"1",data:"保存成功"}
//	// {code:"3",data:"没有查询到相关数据"}
//	// {code:"4",data:"34930此用户在IDM中没有关联账号"}
//	public static String parseResponseData(String data) {
//		String result = "";
//		String code = "";
//		String message = "";
//		JSONObject jsonData = null;
//		try {
//			jsonData = new JSONObject(data);
//			code = jsonData.get("code").toString();
//			message = jsonData.get("data").toString();
//
//			if ("1".equals(code)) {// 成功
//				result = message;
//				System.err.println(message);
//			} else {// 失败
//				result = "操作失败，" + message;
//				System.err.println(result);
//			}
//		} catch (Exception e) {
//			System.err.println();
//			e.printStackTrace();
//		}
//
//		return result;
//	}
//}
