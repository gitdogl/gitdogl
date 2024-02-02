package test;

import weaver.interfaces.hbky.oa.meet.service.MeetingService;

public class meettest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String requestid = "4213";
		MeetingService ms = new MeetingService();
		try {
			boolean isok = ms.SySPMeeting(requestid);
			System.out.println("isok" + isok);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
