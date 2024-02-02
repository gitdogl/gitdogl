package weaver.interfaces.jjc.oaclient.cw.dao;

import weaver.conn.RecordSet;
import weaver.interfaces.jjc.oaclient.cw.dto.OAStatementsBill;
import weaver.interfaces.jjc.oaclient.cw.dto.OAStatementsDto2;

import java.util.ArrayList;
import java.util.List;

public class OAStatementsDao2 {
    RecordSet rs = new RecordSet();

    /**
     * 返回进账单主表数据
     *
     * @param billid
     * @return
     */
    public OAStatementsBill getOAStatementsBill(String requestid, String tableName) {
        OAStatementsBill oaStatementsBill = new OAStatementsBill();
        StringBuffer sqlBuff = new StringBuffer("select * from " + tableName + " where requestid = " + requestid);
        rs.execute(sqlBuff.toString());
        if (rs.next()) {
            oaStatementsBill.setId(rs.getString("id"));//id
            oaStatementsBill.setJzrq(rs.getString("jzrq"));//进账日期
            oaStatementsBill.setJzdbh(rs.getString("jzdbh"));//进账单编号
            oaStatementsBill.setJzje(rs.getString("jzje"));//进账总额
            oaStatementsBill.setBz(rs.getString("bz"));//币种
            oaStatementsBill.setHkdw(rs.getString("hkdw"));//汇款单位
            oaStatementsBill.setHkyx(rs.getString("hkyx"));//汇款银行
            oaStatementsBill.setYsfj(rs.getString("ysfj"));//原始附件
            oaStatementsBill.setFy(rs.getString("fy"));//附言
            oaStatementsBill.setScfj(rs.getString("scfj"));//上传附件
            oaStatementsBill.setJzzh(rs.getString("jzzh"));//进账账户
            oaStatementsBill.setJzzh1(rs.getString("jzzh1"));//进账账号
            oaStatementsBill.setYqrje(rs.getString("yqrje"));//已确认金额
            oaStatementsBill.setDqrje(rs.getString("dqrje"));//待确认金额
            oaStatementsBill.setSfxydjdcwxt(rs.getString("sfxydjdcwxt"));//是否需要对接到财务系统
        }

        return oaStatementsBill;
    }

    public List<OAStatementsDto2> getOAStatementsDto2List(String id, String tableName) {
        List<OAStatementsDto2> oaStatementsDto2List = new ArrayList<>();
        StringBuffer sqlBuff = new StringBuffer("select * from " + tableName + "_dt2 where mainid = ").append(id);
        rs.execute(sqlBuff.toString());
        while (rs.next()) {
            OAStatementsDto2 oaStatementsDto2 = new OAStatementsDto2();

            oaStatementsDto2.setId(rs.getString("id"));//id
            oaStatementsDto2.setMainid(rs.getString("mainid"));//mainid
            oaStatementsDto2.setJzkm(rs.getString("jzkm"));//入账科目
            oaStatementsDto2.setKmh(rs.getString("kmh"));//科目号
            oaStatementsDto2.setJzje(rs.getString("jzje"));//进账金额

            oaStatementsDto2List.add(oaStatementsDto2);
        }


        return oaStatementsDto2List;

    }


}
