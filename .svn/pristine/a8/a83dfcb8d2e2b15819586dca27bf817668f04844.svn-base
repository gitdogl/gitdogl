package weaver.interfaces.hbky.oa.meet.dao;



import weaver.interfaces.hbky.oa.meet.dto.OAMeeting;
import weaver.interfaces.hbky.oa.meet.util.OADBUtil;

/**
 * 采购订货单数据库访问层
 * 
 * @author admistrator
 * 
 */
public class OAMeetingDao {

	/**
	 * 获取订单订货申请主表信息记录根据requestid
	 * 
	 * @description
	 * @author liuwe_000
	 * @create 2015年8月3日上午10:06:17
	 * @version 1.0
	 * @param requestid
	 *            请求ID
	 * @return 获取订单订货主表记录根据requestid
	 * @throws Exception
	 *             执行异常
	 */
	public OAMeeting getOAMeeting(String requestid, String tableName)
			throws Exception {
		OAMeeting oaMeeting = new OAMeeting();
		StringBuffer addSql = new StringBuffer("");
		addSql.append("select * from  ").append(tableName).append(" where ")
				.append(" requestid=? ");
		OADBUtil rs = new OADBUtil();
		try {
			rs.setStatementSql(addSql.toString());
			rs.setString(1, requestid);
			rs.executeQuery();
			if (rs.next()) {
				oaMeeting.setId(rs.getString("id"));
				oaMeeting.setRequestid(rs.getString("requestid"));
				oaMeeting.setMeetingtype(rs.getString("meetingtype"));
				oaMeeting.setName(rs.getString("name"));
				oaMeeting.setCaller(rs.getString("caller"));
				oaMeeting.setContacter(rs.getString("contacter"));
				oaMeeting.setAddress(rs.getString("address"));
				oaMeeting.setCustomizeaddress(rs.getString("customizeAddress"));
				oaMeeting.setDesc_n(rs.getString("desc_n"));
				oaMeeting.setRepeattype(rs.getString("repeatType"));
				oaMeeting.setRepeatdays(rs.getString("repeatdays"));
				oaMeeting.setRepeatweeks(rs.getString("repeatweeks"));
				oaMeeting.setRptweekdays(rs.getString("rptWeekDays"));
				oaMeeting.setRepeatmonths(rs.getString("repeatmonths"));
				oaMeeting.setRepeatmonthdays(rs.getString("repeatmonthdays"));
				oaMeeting.setRepeatstrategy(rs.getString("repeatStrategy"));
				oaMeeting.setBegindate(rs.getString("begindate"));
				oaMeeting.setBegintime(rs.getString("begintime"));
				oaMeeting.setEnddate(rs.getString("enddate"));
				oaMeeting.setEndtime(rs.getString("endtime"));
				oaMeeting.setApproveid(rs.getString("ApproveID"));
				oaMeeting.setRemindtypenew(rs.getString("remindTypeNew"));
				oaMeeting.setRemindimmediately(rs.getString("remindImmediately"));
				oaMeeting.setRemindbeforestart(rs.getString("remindBeforeStart"));
				oaMeeting.setRemindbeforeend(rs.getString("remindBeforeEnd"));
				oaMeeting.setRemindhoursbeforestart(rs.getString("remindHoursBeforeStart"));
				oaMeeting.setRemindtimesbeforestart(rs.getString("remindTimesBeforeStart"));
				oaMeeting.setRemindhoursbeforeend(rs.getString("remindHoursBeforeEnd"));
				oaMeeting.setRemindtimesbeforeend(rs.getString("remindTimesBeforeEnd"));
				oaMeeting.setHrmmembers(rs.getString("hrmmembers"));
				oaMeeting.setOthermembers(rs.getString("othermembers"));
				oaMeeting.setTotalmember(rs.getString("totalmember"));
				oaMeeting.setCrmmembers(rs.getString("crmmembers"));
				oaMeeting.setCrmtotalmember(rs.getString("crmtotalmember"));
				oaMeeting.setProjectid(rs.getString("projectid"));
				oaMeeting.setAccessorys(rs.getString("accessorys"));
				oaMeeting.setSfjldsp(rs.getString("sfjldsp"));
				oaMeeting.setXzjld(rs.getString("xzjld"));
				oaMeeting.setSqbm(rs.getString("sqbm"));
				oaMeeting.setSqdw(rs.getString("sqdw"));
				oaMeeting.setHyzcr(rs.getString("hyzcr"));
				oaMeeting.setSqrq(rs.getString("sqrq"));
				oaMeeting.setSfzdyhys(rs.getString("sfzdyhys"));
				oaMeeting.setXzldsp(rs.getString("xzldsp"));
				oaMeeting.setSfxzjtld(rs.getString("sfxzjtld"));
				oaMeeting.setChdw(rs.getString("chdw"));
				oaMeeting.setChdwws(rs.getString("chdwws"));
				oaMeeting.setHysb(rs.getString("hysb"));
				oaMeeting.setHyssfsf(rs.getString("hyssfsf"));
				oaMeeting.setSpld(rs.getString("spld"));
				oaMeeting.setHybh(rs.getString("hybh"));
				oaMeeting.setHzchr(rs.getString("hzchr"));
				oaMeeting.setSfhz(rs.getString("sfhz"));
				oaMeeting.setSphyzj(rs.getString("sphyzj"));
				oaMeeting.setSphydz(rs.getString("sphydz"));
				oaMeeting.setYsphy(rs.getString("ysphy"));
				oaMeeting.setSphyzt(rs.getString("sphyzt"));


			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("获取订货单申请主表信息错误");
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		return oaMeeting;
	}

	
	/**
	 * 更新加班单据状态记录根据requestid
	 * 
	 * @description
	 * @author liuwe_000
	 * @create 2015年8月3日上午10:06:17
	 * @version 1.0
	 * @param requestid
	 *            请求ID
	 * @return 更新是否已经同步sap数据
	 * @throws Exception
	 *             执行异常
	 */
	public boolean updateStatus(String requestid, String tableName,String sphyzj,String 	sphydz)
			throws Exception {
		boolean isok = true;
		StringBuffer addSql = new StringBuffer("");
		addSql.append("update  ").append(tableName)
				.append(" set sphyzj='"+sphyzj+"',	sphydz='"+	sphydz+"' where ").append(" requestid=? ");
		OADBUtil rs = new OADBUtil();
		try {
			rs.setStatementSql(addSql.toString());
			rs.setString(1, requestid);
			isok = rs.executeUpdate() == 1 ? true : false;
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("更新视屏会议订单信息错误");
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		return isok;
	}

	
}
