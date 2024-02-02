package weaver.interfaces.jjc.oaclient.cw.dao;

import weaver.conn.RecordSet;
import weaver.interfaces.jjc.oaclient.cw.dto.OAPaymentNoteBill;
import weaver.interfaces.jjc.oaclient.cw.dto.OAPaymentNoteDto2;

import java.util.ArrayList;
import java.util.List;

public class OAPaymentNoteDao {
    RecordSet rs = new RecordSet();

    /**
     * 获取报销主表
     *
     * @param requestid
     * @param tableName
     * @return
     */
    public OAPaymentNoteBill getOAPaymentNoteBill(String requestid, String tableName) {
        OAPaymentNoteBill oaPaymentNoteBill = new OAPaymentNoteBill();
        StringBuffer sqlBuff = new StringBuffer("select * from " + tableName + " where requestid = " + requestid);
        rs.execute(sqlBuff.toString());
        if (rs.next()) {
            oaPaymentNoteBill.setId(rs.getString("id"));//id
            oaPaymentNoteBill.setRequestid(rs.getString("requestid"));//requestid
            oaPaymentNoteBill.setBh(rs.getString("lcbh"));//编号
            oaPaymentNoteBill.setBxry(rs.getString("bxry"));//报销人员
            oaPaymentNoteBill.setBxsy(rs.getString("bxyt"));//报销用途
            oaPaymentNoteBill.setXgxm(rs.getString("xmmc"));//相关项目
            oaPaymentNoteBill.setXmbh(rs.getString("xmbh"));
        }

        return oaPaymentNoteBill;
    }

    /**
     * 获取付款明细
     *
     * @param requestid
     * @return
     */
    public List<OAPaymentNoteDto2> getOAPaymentNoteDto2List(String requestid) {
        List<OAPaymentNoteDto2> oaPaymentNoteDto2List = new ArrayList<>();
        StringBuffer sqlBuff = new StringBuffer("select * from uf_zjlysy where lc = " + requestid);
        rs.execute(sqlBuff.toString());
        while (rs.next()) {
            OAPaymentNoteDto2 oaPaymentNoteDto2 = new OAPaymentNoteDto2();
            oaPaymentNoteDto2.setJe(rs.getString("je"));//金额
            oaPaymentNoteDto2.setZjly(rs.getString("zjly"));//资金来源
            oaPaymentNoteDto2.setSkr(rs.getString("skr"));//收款人
            oaPaymentNoteDto2.setSkzh(rs.getString("skzh"));//收款账号
            oaPaymentNoteDto2.setKhx(rs.getString("khx"));//开户行
            oaPaymentNoteDto2.setZckm(rs.getString("zckm"));
            oaPaymentNoteDto2List.add(oaPaymentNoteDto2);
        }

        return oaPaymentNoteDto2List;
    }


}
