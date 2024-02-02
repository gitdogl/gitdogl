package weaver.workflow.qiyuesuo.servlet;

import org.apache.commons.fileupload.FileItem;
import weaver.general.Util;
import weaver.conn.RecordSet;
import weaver.hrm.User;
import weaver.workflow.qiyuesuo.QYSInterface;
import weaver.workflow.qiyuesuo.bean.QYSResponse;
import weaver.workflow.qiyuesuo.companyAuth.QYSCompanyAuthInterface;
import weaver.workflow.qiyuesuo.constant.QYSCategoryType;
import weaver.workflow.qiyuesuo.constant.QYSConstant;
import weaver.workflow.qiyuesuo.constant.QYSLogType;
import weaver.workflow.qiyuesuo.constant.QYSOperateType;
import weaver.workflow.qiyuesuo.sealApply.QYSSealApplyInterface;
import weaver.workflow.qiyuesuo.util.QYSLogUtil;
import weaver.workflow.qiyuesuo.util.QYSUtil;
import weaver.workflow.qiyuesuo.util.QYSWorkflowUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 契约锁私有云系统回调
 *
 * @author ywr
 */
public class QYSPrivateCallbackServlet extends HttpServlet {
	
	public void doPost(final HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		QYSLogUtil log = new QYSLogUtil(getClass().getName());
		log.info("doPost--start");

		final QYSResponse response = new QYSResponse();
		
		String characterEncoding = Util.null2String(req.getCharacterEncoding());
		String contentType = Util.null2String(req.getHeader("Content-Type")).toLowerCase();
		log.info("doPost--characterEncoding:" + characterEncoding + "--contentType:" + contentType);

		final Map<String, Object> params = QYSUtil.request2Map(req); // 默认 application/x-www-form-urlencoded
		if (contentType.startsWith("multipart/")) { // multipart/form-data
			params.putAll(new QYSUtil().requestMultipart2Map(req));
		} else if (contentType.startsWith("application/json")) { // application/json
			params.putAll(new QYSUtil().requestJSON2Map(req));
		}

		final String callbackEventType = Util.null2String(params.get("callbackEventType")); // 契约锁集成回调标识
		final String ecologyOperate = Util.null2String(params.get("ecologyOperate")); // 契约锁集成回调操作
		log.info("doPost--callbackEventType:" + callbackEventType + "--ecologyOperate:" + ecologyOperate);

		final QYSCategoryType categoryType=QYSConstant.CALLBACK_EVENT_TYPE_PHYSICS.equalsIgnoreCase(callbackEventType)?QYSCategoryType.PHYSICS:QYSConstant.CALLBACK_EVENT_TYPE_COMPANY_AUTH.equalsIgnoreCase(callbackEventType)?QYSCategoryType.COMPANYAUTH:QYSCategoryType.ELECTRONIC;
		log.info("dopost--categoryType:"+categoryType.getType());
		String operatorMobile = Util.null2String(params.get("operatorMobile"));
		final int operatorUserId = QYSUtil.getUserIdByMobile(operatorMobile);
		log.info("doPost--operatorUserId:" + operatorUserId+"--operatorMobile:"+operatorMobile);
		final int bizId = Util.getIntValue(Util.null2String(params.get("bizId")),0);
		log.info("doPost--bizId:" + bizId);

		final QYSPrivateCallbackServlet   qysPrivateCallbackServlet=new QYSPrivateCallbackServlet();
		RecordSet   rs=new RecordSet();
		String SynReqMenu= "";
		rs.executeQuery("select   SynReqMenu  from "+ QYSConstant.TABLE_GLOBAL_CONFIG);
		while(rs.next()){
			SynReqMenu= Util.null2String(rs.getString("SynReqMenu"));
			log.info("doPost--SynReqMenu:" + SynReqMenu);
		}
		if("1".equals(SynReqMenu)){
			response.setCodeAndMessage(0, "");
			new Thread(new Runnable() {
				@Override
				public void run() {
					qysPrivateCallbackServlet.callback(ecologyOperate,req,callbackEventType,params,operatorUserId,response,categoryType,bizId);
				}
			}).start();
		}else {
			QYSResponse  newResponse=qysPrivateCallbackServlet.callback(ecologyOperate,req,callbackEventType,params,operatorUserId,response,categoryType,bizId);
			response.setCode(newResponse.getCode());
		}

		log.info("doPost--end--" + response.toJSONString());
		//修改没有传递ecologyOperate时，回调直接响应成功
		if (0 == response.getCode()||ecologyOperate.isEmpty()) {
			res.setStatus(200);
		} else {
			ServletOutputStream outputStream = res.getOutputStream();
			res.setHeader("Content-type","text/html;charset=UTF-8");
			res.setContentType("text/html; chartset=UTF-8");
			res.setStatus(500);
			outputStream.write(response.getMessage().getBytes("UTF-8"));
			//res.sendError(500, response.getMessage());
		}
	}


	public QYSResponse callback(String ecologyOperate,HttpServletRequest req,String callbackEventType,Map<String, Object> params,int operatorUserId, QYSResponse response,QYSCategoryType categoryType,int bizId){
		QYSLogUtil  log = new QYSLogUtil(getClass().getName());
		if (!ecologyOperate.isEmpty()) {
			QYSInterface QYSInterface = null;
			QYSSealApplyInterface QYSSealApplyInterface = null;
			QYSCompanyAuthInterface QYSCompanyAuthInterface;

			User user = new User(1);
			String loingIp = "";
			try {
				loingIp=  Util.null2String(req.getRemoteAddr());
			}catch (Exception  e){
				e.printStackTrace();
			}
			user.setLoginip(loingIp);

			int requestid = 0;
			if (QYSConstant.CALLBACK_EVENT_TYPE_PHYSICS.equalsIgnoreCase(callbackEventType)) { // 物理签章
				String businessId = Util.null2String(params.get("businessId")).trim();
				log.info("doPost--businessId:" + businessId);

				if (!businessId.isEmpty()) {
					QYSSealApplyInterface = new QYSSealApplyInterface(user, QYSLogType.CALLBACK);
					requestid = QYSSealApplyInterface.getRequestidbyBusinessId(Long.valueOf(businessId));
					if(requestid>0){
						QYSSealApplyInterface.setCallbackParams(params,requestid);
					}
				}
			} else if (QYSConstant.CALLBACK_EVENT_TYPE_COMPANY_AUTH.equalsIgnoreCase(callbackEventType)) { // 企业认证
				String companyId = Util.null2String(params.get("companyId")).trim();
				log.info("doPost--companyId:" + companyId);

				if (!companyId.isEmpty()) {
					QYSCompanyAuthInterface = new QYSCompanyAuthInterface(user, QYSLogType.CALLBACK);
					requestid = QYSCompanyAuthInterface.getRequestIdByCompanyId(Long.valueOf(companyId));
				}
			} else { // 默认电子签章
				String contractId = Util.null2String(params.get("contractId")).trim();
				log.info("doPost--contractId:" + contractId);

				if (!contractId.isEmpty()) {
					QYSInterface = new QYSInterface(user, QYSLogType.CALLBACK);
					requestid = QYSInterface.getRequestidbyContractId(Long.valueOf(contractId));
				}
			}
			log.info("doPost--requestid:" + requestid);
			if(requestid <= 0 && bizId > 0){
				requestid = bizId;
			}
			if (requestid > 0) {
				if (QYSConstant.CALLBACK_OPERATE_DOWNLOAD.equalsIgnoreCase(ecologyOperate)) {
					if (QYSConstant.CALLBACK_EVENT_TYPE_PHYSICS.equalsIgnoreCase(callbackEventType)) { // 物理签章
						if (params.containsKey("images")) { // 用印照片回调
							List<FileItem> images = params.get("images") == null ? new ArrayList<FileItem>() : (List<FileItem>) params.get("images");
							log.info("doPost--images.size:" + images.size());

							ArrayList<Map> imageMaps = new ArrayList<Map>();
							for (FileItem image : images) {
								if (image == null) {
									continue;
								}

								Map imageMap = new HashMap();
								imageMap.put("filename", image.getName()); // 文件名
								imageMap.put("content", image.get()); // 文件 byte[]
								imageMaps.add(imageMap);
							}
							log.info("doPost--imageMaps.size:" + imageMaps.size());

							response = new QYSResponse(QYSSealApplyInterface.downloadSealApplyImage(requestid, imageMaps));
						} else {
							response = new QYSResponse(QYSSealApplyInterface.downloadSealApplyImageFromCallback(requestid));
						}
					} else if (QYSConstant.CALLBACK_EVENT_TYPE_COMPANY_AUTH.equalsIgnoreCase(callbackEventType)) { // 企业认证
						response.setCodeAndMessage(0, "");
					} else { // 默认电子签章
						int downloaderId = new QYSWorkflowUtil(new User(1)).getDownloadUserId(requestid,operatorUserId);
						log.info("doPost--downloaderId:"+downloaderId);
						if(downloaderId>0) {
							User downloader = new User(downloaderId);
							downloader.setLoginip(loingIp);
							QYSInterface.setUser(downloader);
						}else{
							QYSInterface.setUser(new User(1));
						}
						response = new QYSResponse(QYSInterface.downloadContractFromCallback(requestid));
					}
					if(response.getCode() == 0){//更新操作时间
						int downloadFieldId = Util.getIntValue(Util.null2String(response.getString("downloadField")));
						QYSWorkflowUtil.updateWfOperateTime(requestid,downloadFieldId);
					}
				} else if (QYSConstant.CALLBACK_OPERATE_SUBMIT.equalsIgnoreCase(ecologyOperate) || QYSConstant.CALLBACK_OPERATE_REJECT.equalsIgnoreCase(ecologyOperate)) {
					String remark = Util.null2String(params.get("ecologyRemark"));
					String signedByEc = Util.null2String(params.get("signedByEc"));

					log.info("doPost--remark:" + remark + "--signedByEc:" + signedByEc);
					if(signedByEc.equals("1") && QYSConstant.CALLBACK_OPERATE_SUBMIT.equalsIgnoreCase(ecologyOperate)) {//从OA这边完成合同签署的时候，回调提交不执行
						response.setCodeAndMessage(0, "");
					}else{
						String result = QYSWorkflowUtil.submitOrRejectRequest(requestid, ecologyOperate, remark, operatorUserId, categoryType,params);
						if ("SUCCESS".equalsIgnoreCase(result)) {
							response.setCodeAndMessage(0, "");
						} else if ("ERRORUSER".equalsIgnoreCase(result)) {
							response.setMessage("该签署人不是当前节点的操作者，不能提交或退回流程");
						} else if("failed".equals(result)){
						} else{
							response.setMessage(result);
						}
					}
				} else if (QYSConstant.CALLBACK_OPERATE_UPDATE.equalsIgnoreCase(ecologyOperate)) {
					String prefix = "fieldname_";
					ArrayList<Map> list = new ArrayList<Map>();
					for (String element : params.keySet()) {
						if (!element.toLowerCase().startsWith(prefix)) {
							continue;
						}

						String fieldName = element.substring(prefix.length()).trim();
						if (fieldName.isEmpty()) {
							continue;
						}

						Map map = new HashMap();
						map.put("fieldName", fieldName);
						map.put("fieldValue", Util.null2String(params.get(element)));
						list.add(map);
					}
					log.info("doPost--list.size:" + list.size() + "--list:" + list);

					QYSInterface=new QYSInterface(new User(operatorUserId==0?1:operatorUserId),QYSLogType.CALLBACK);
					QYSInterface.setOperateLog(categoryType, QYSOperateType.CALLBACK_UPDATE_REQUESTFORM,requestid);
					response = new QYSResponse(QYSWorkflowUtil.updateRequestForm(requestid, list));
					QYSInterface.saveOperateLog(response.getCode()==0);
					if (0 == response.getCode()) {
						response.setCodeAndMessage(0, "");
					}
				}else if(QYSConstant.CALLBACK_OPERATE_SYNCVERTIFYCODE.equalsIgnoreCase(ecologyOperate)){ //同步物理用印授权码
					if(QYSConstant.CALLBACK_EVENT_TYPE_PHYSICS.equalsIgnoreCase(callbackEventType)){//物理签章
						String auths = Util.null2String(params.get("auths")).trim();
						response = new QYSResponse(new QYSSealApplyInterface(new User(operatorUserId==0?1:operatorUserId),QYSLogType.CALLBACK).syncVertifyCode(requestid,auths));
					}else{
						response.setCodeAndMessage(0,"");
					}
				} else {
					response.setCodeAndMessage(0, "");
				}
			} else {
				response.setCodeAndMessage(0, "");
			}
		}
		return   response;
	}
}
