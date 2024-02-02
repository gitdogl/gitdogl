package weaver.interfaces.hbky.oa.meet.service;

import java.net.URLEncoder;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.weaver.general.Util;

import weaver.general.TimeUtil;
import weaver.interfaces.hbky.oa.meet.dao.BaseDao;
import weaver.interfaces.hbky.oa.meet.dao.OAMeetingDao;
import weaver.interfaces.hbky.oa.meet.dto.MeetUser;
import weaver.interfaces.hbky.oa.meet.dto.Meeting;
import weaver.interfaces.hbky.oa.meet.dto.OAMeeting;
import weaver.interfaces.hbky.oa.meet.dto.OAUser;
import weaver.interfaces.hbky.oa.meet.util.HashUtil;
import weaver.interfaces.hbky.oa.meet.util.SPMeetUtils;

/**
 * 视屏会议会议审批
 * 
 * @author Administrator
 * 
 */
public class MeetingService {

	// 基数数据访问
	BaseDao baseDao = new BaseDao();
	// 视屏会议
	OAMeetingDao cgddDao = new OAMeetingDao();
	// 视屏会议
	SPMeetingService spmeet = new SPMeetingService();

	SPMeetUtils czyutl = new SPMeetUtils();

	/**
	 * 验证发货数量
	 * 
	 * @param requestid
	 *            发货单请求数量
	 * @return 是否发货成功
	 * @throws Exception
	 *             发货异常
	 */
	public boolean SySPMeeting(String requestid) throws Exception {
		// 获取流程数据表
		String tableName = baseDao.getTableNameByResquestID(requestid);
		// 查询发货数据
		OAMeeting oaMeeting = cgddDao.getOAMeeting(requestid, tableName);

		// 取消会议
		String sphyzt = oaMeeting.getSphyzt();
		String ysphy = Util.null2String(oaMeeting.getYsphy()).trim();
		if ("1".equals(sphyzt) && !"".equals(ysphy) && ysphy != null) {
			// 取消会议 删除视屏会议同时删除原会议人员
			// 修改会议
			String token = spmeet.login();
			Meeting metting = new Meeting();
			metting.setId(oaMeeting.getYsphy());
			try {
				List<String> userlist = spmeet.GetMeetUserIDs(token,
						oaMeeting.getYsphy());
				spmeet.DelMeet(token, metting);
				for (String userid : userlist) {
					System.out.println("删除用户" + userid);
					try {
						spmeet.Deluser(token, userid);
					} catch (Exception s) {
						s.printStackTrace();
					}
				}
			} catch (Exception s) {
				s.printStackTrace();
			}
			
		}
		else if ((sphyzt==null||"0".equals(sphyzt))&& !"".equals(ysphy)&&ysphy!=null) {
			// 修改会议
			String token = spmeet.login();
			Meeting metting = new Meeting();
			metting.setConfpassword("123456");
			metting.setName(oaMeeting.getName()+".");
			metting.setRemarks(oaMeeting.getDesc_n()+".");
			metting.setStarttime(TimeUtil.getCalendar(
					oaMeeting.getBegindate() + " " + oaMeeting.getBegintime()
							+ ":00").getTimeInMillis());
			metting.setUserids(getUseIds(token, oaMeeting.getHrmmembers()));
			metting.setDuration(TimeUtil.getCalendar(
					oaMeeting.getEnddate() + " " + oaMeeting.getEndtime()
							+ ":00").getTimeInMillis()
					- TimeUtil.getCalendar(
							oaMeeting.getBegindate() + " "
									+ oaMeeting.getBegintime() + ":00")
							.getTimeInMillis());
			metting.setContact(baseDao.getOAUser(oaMeeting.getContacter())
					.getLoginid());
			metting.setId(oaMeeting.getYsphy());
					
			String sphyzj =spmeet.EditMeet(token, metting);	
			String sphydz = "<a href=\"" + czyutl.getSpmeetUrl()
					+ czyutl.getMeeturl() + sphyzj + "\" target=\"_blank\" title=\""+ metting.getName()+"\" textvalue=\""+ metting.getName()+"\"> " + metting.getName()
					+ "</a> ";
			cgddDao.updateStatus(requestid, tableName, sphyzj, sphydz);
		} else if ("".equals(ysphy) || ysphy == null) {

			// 新增会议
			String token = spmeet.login();
			Meeting metting = new Meeting();
			metting.setConfpassword("123456");
			metting.setName(oaMeeting.getName()+".");
			metting.setRemarks(oaMeeting.getDesc_n()+".");
			metting.setStarttime(TimeUtil.getCalendar(
					oaMeeting.getBegindate() + " " + oaMeeting.getBegintime()
							+ ":00").getTimeInMillis());
			metting.setUserids(getUseIds(token, oaMeeting.getHrmmembers()));
			metting.setDuration(TimeUtil.getCalendar(
					oaMeeting.getEnddate() + " " + oaMeeting.getEndtime()
							+ ":00").getTimeInMillis()
					- TimeUtil.getCalendar(
							oaMeeting.getBegindate() + " "
									+ oaMeeting.getBegintime() + ":00")
							.getTimeInMillis());
			metting.setContact(baseDao.getOAUser(oaMeeting.getContacter())
					.getLoginid());
			String sphyzj = spmeet.AddMeet(token, metting);
			String sphydz = "<a href=\"" + czyutl.getSpmeetUrl()
					+ czyutl.getMeeturl() + sphyzj + "\" target=\"_blank\" title=\""+ metting.getName()+"\" textvalue=\""+ metting.getName()+"\"> " + metting.getName()
					+ "</a> ";
			cgddDao.updateStatus(requestid, tableName, sphyzj, sphydz);
		}
		return true;
	}

	/**
	 * 
	 * @param chryOA人员ID
	 * @return 视屏会议系统的用户ID
	 */

	public String getUseIds(String token, String chry) {
		StringBuffer meetuser = new StringBuffer("[");
		// 返回格式"[635,1026,1034]"
		OAUser user = null;
		MeetUser muser = null;
		for (String oauid : chry.split(",")) {
			// 每个参会人员idOA用户id
			if (oauid != null && !"".equals(oauid)) {
				try {
					user = baseDao.getOAUser(oauid);
					try {
						muser = null;
						muser = spmeet.Getuser(token, user.getLoginid());
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 用户不存在新建用户
					if (muser == null || "".equals(muser.getId())) {
						String sipUserName = spmeet.idleSipNumber(token);
						muser = new MeetUser();
						muser.setName(user.getLoginid());
						muser.setCellphone(user.getCallNumber());
						muser.setCompany(user.getCompany());
						muser.setDescription("OA自动创建");
						muser.setDevicetype("WEB");
						muser.setDisplayname(user.getLastname());
						muser.setEmail(user.getEmail());
						muser.setPassword(HashUtil.hash("123456", "SHA1"));
						muser.setSippassword("123456");
						muser.setSipusername(sipUserName);
						muser.setStatus("0");
						muser.setTelephone(user.getTelephone());
						muser.setSipAuthName(sipUserName);						
						System.out.println("muser=="+JSONObject.toJSON(muser).toString());
						String mid = spmeet.Adduser(token, muser);
						muser.setId(mid);
					}
					// 获取会议人员ID
					if ("[".equals(meetuser.toString())) {
						meetuser.append(muser.getId());
					} else {
						meetuser.append("," + muser.getId());
					}
System.out.println("oauid"+oauid+meetuser.toString()+"user.getLoginid()"+user.getLoginid());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		meetuser.append("]");
		return meetuser.toString();
	}

}
