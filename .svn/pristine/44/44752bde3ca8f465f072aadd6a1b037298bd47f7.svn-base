//package weaver.interfaces.erp;
//
//import javax.xml.namespace.QName;
//
//import org.apache.axis.client.Call;
//import org.apache.axis.client.Service;
//
//import weaver.general.BaseBean;
//import weaver.general.Util;
//
//public class ERPHelper extends BaseBean {
//
//	/**
//	 *
//	 * 新增信息
//	 * @param params
//	 * @return
//	 */
//	public String sendTODO(Object[] params)  throws Exception {
//		Service service = new Service();
//		String result = "";
//		try {
//			String url = Util.null2String(getPropValue("ERPSetting", "erp_url"));
//			String namespace = "http://tempuri.org/";
//			// 要调用的方法，新增信息
//			String method = "AddAllString";
//			Call call = (Call) service.createCall();
//			call.setTargetEndpointAddress(new java.net.URL(url));
//			call.setUseSOAPAction(true);
//			call.setSOAPActionURI(namespace + method);
//			call.setOperationName(new QName(namespace, method));
//
//			// 设置参数名称
//			call.addParameter(new QName(namespace, "systemID"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "systemName"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "itemType"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "itemID"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "itemName"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "activityName"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "startedDateTime"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "receiveTime"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "creator"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "creatorName"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "sourceUserID"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "itemSourceType"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "itemUrl"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "state"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "secLevel"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "secLevelName"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "description"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "description2"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "attr_1"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "attr_2"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "attr_3"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			// 要返回的数据类型
//			call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
//			// 返回结果
//			result = (String) call.invoke(params);
//			writeLog("新增信息，返回结果：" + result);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			writeLog("-----新增信息出错：-----", e);
//			throw new Exception("-----新增信息出错：-----", e);
//		}
//		return result;
//	}
//
//	/**
//	 *
//	 * 更新状态
//	 * @param params
//	 * @return
//	 */
//	public String updateTODO(Object[] params)  throws Exception {
//		Service service = new Service();
//		String result = "";
//		try {
//			String url = Util.null2String(getPropValue("ERPSetting", "erp_url"));
//			String namespace = "http://tempuri.org/";
//			// 要调用的方法， 更新状态
//			String method = "UpdateTaskState";
//			Call call = (Call) service.createCall();
//			call.setTargetEndpointAddress(new java.net.URL(url));
//			call.setUseSOAPAction(true);
//			call.setSOAPActionURI(namespace + method);
//			call.setOperationName(new QName(namespace, method));
//
//			// 设置参数名称
//			call.addParameter(new QName(namespace, "itemID"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "systemId"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "state"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			// 要返回的数据类型
//			call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
//			// 返回结果
//			result = (String) call.invoke(params);
//			writeLog("更新状态，返回结果：" + result);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			writeLog("-----更新状态出错：-----", e);
//			throw new Exception("-----更新状态出错：-----", e);
//		}
//		return result;
//	}
//
//	/**
//	 *
//	 * 删除信息
//	 * @param params
//	 * @return
//	 */
//	public String deleteTODO(Object[] params) throws Exception {
//		Service service = new Service();
//		String result = "";
//		try {
//			String url = Util.null2String(getPropValue("ERPSetting", "erp_url"));
//			String namespace = "http://tempuri.org/";
//			// 要调用的方法，删除信息
//			String method = "Delete";
//			Call call = (Call) service.createCall();
//			call.setTargetEndpointAddress(new java.net.URL(url));
//			call.setUseSOAPAction(true);
//			call.setSOAPActionURI(namespace + method);
//			call.setOperationName(new QName(namespace, method));
//
//			// 设置参数名称
//			call.addParameter(new QName(namespace, "itemID"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			call.addParameter(new QName(namespace, "systemId"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
//			// 要返回的数据类型
//			call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
//			// 返回结果
//			result = (String) call.invoke(params);
//			writeLog("删除信息，返回结果：" + result);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			writeLog("-----删除信息出错：-----", e);
//			throw new Exception("-----删除信息出错：-----", e);
//		}
//		return result;
//	}
//
//}