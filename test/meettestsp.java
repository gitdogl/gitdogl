package test;

import java.util.List;

import weaver.general.TimeUtil;
import weaver.interfaces.hbky.oa.meet.dto.Meeting;
import weaver.interfaces.hbky.oa.meet.service.SPMeetingService;

public class meettestsp {
	public static void main(String[] args) {
		// System.setProperty("javax.net.debug","ssl");
		SPMeetingService t = new SPMeetingService();
		try {
			String token = t.login();
			// String sipUserName = t.test_idleSipNumber(token);
			// MeetUser muser = new MeetUser();
			// muser.setName("test02");

			// String musername = muser.getName();
			// muser = t.test_Getuser(token, musername);
			// muser.setId(userid);
			// muser.setCellphone("1243");
			// muser.setCompany("730");
			// muser.setDescription("接口创建通过接口修改名称");
			// muser.setDevicetype("WEB");
			// muser.setDisplayname("普通户子3");
			// muser.setEmail("12368@qq.com");
			// muser.setName("test05");
			// muser.setPassword(HashUtil.hash("123456", "SHA1"));
			// muser.setSippassword("123456");
			// muser.setSipusername(sipUserName);
			// muser.setStatus("0");
			// muser.setTelephone("123456708");
			// muser.setVmr("8001001");
			// muser.setSipAuthName("1001");
			// t.test_Adduser(token, muser);
			// t.test_Edituser(token, muser);
			// t.test_Getoneuser(token, muser.getId());
			// t.test_Deluser(token, muser);
			// musername = muser.getName();
			// muser = t.test_Getuser(token, musername);
			Meeting metting = new Meeting();
			metting.setConfpassword("123456");
			metting.setName("接口新建会议new更新");
			metting.setRemarks("测试");
			metting.setStarttime(TimeUtil.getCalendar("2018-07-24 17:50:00")
					.getTimeInMillis());
			metting.setUserids("[635,1026,1034]");
			metting.setDuration(TimeUtil.getCalendar("2018-07-24 17:56:00")
					.getTimeInMillis()
					- TimeUtil.getCalendar("2018-07-24 17:20:00")
							.getTimeInMillis());

			String mid = t.AddMeet(token, metting);
			metting.setId(mid);
			// t.EditMeet(token, metting);
			// t.DelMeet(token, metting);
			List<String> userlist = t.GetMeetUserIDs(token, mid);
			for (String userid : userlist) {
				System.out.println("删除用户" + userid);
				t.Deluser(token, userid);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
