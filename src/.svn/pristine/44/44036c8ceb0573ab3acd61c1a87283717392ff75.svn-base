package weaver.interfaces.jjc.oaclient.cw.dao;

import weaver.conn.RecordSet;
import weaver.interfaces.jjc.oaclient.cw.dto.ReceiptsBill;
import weaver.interfaces.jjc.oaclient.cw.dto.ReceiptsDt8;

import java.util.ArrayList;
import java.util.List;

public class ReceiptsDao {
    RecordSet rs = new RecordSet();

    public ReceiptsBill getReceiptsBill(String requestid, String tablename) {
        ReceiptsBill receiptsBill = new ReceiptsBill();
        String sql = "select * from " + tablename + " where requestid = " + requestid;
        rs.execute(sql);
        if (rs.next()) {
            receiptsBill.setId(rs.getString("id"));
            receiptsBill.setRequestid(rs.getString("requestid"));
            receiptsBill.setJzdbh(rs.getString("jzdbh"));
            receiptsBill.setJzdrq(rs.getString("jzdrq"));
            receiptsBill.setHkdw(rs.getString("hkdw"));
            receiptsBill.setJzje(rs.getString("jzje"));
            receiptsBill.setFy(rs.getString("fy"));
            receiptsBill.setFj(rs.getString("fj"));
            receiptsBill.setXzpjzs(rs.getString("xzpjzs"));
            receiptsBill.setJzyx(rs.getString("jzyx"));
            receiptsBill.setJzzh(rs.getString("jzzh"));
            receiptsBill.setJzdkphx(rs.getString("jzdkphx"));
            receiptsBill.setSfxydjdcwxt(rs.getString("sfxydjdcwxt"));
            receiptsBill.setHkyx(rs.getString("hkyx"));
            receiptsBill.setBz(rs.getString("bz"));
            receiptsBill.setSrxz(rs.getString("srxz"));
            receiptsBill.setSfxyhq(rs.getString("sfxyhq"));
            receiptsBill.setXzhqr(rs.getString("xzhqr"));
            receiptsBill.setJe(rs.getString("je"));
            receiptsBill.setLb(rs.getString("lb"));
            receiptsBill.setQtsm(rs.getString("qtsm"));
            receiptsBill.setJzzedx(rs.getString("jzzedx"));
            receiptsBill.setYcmx(rs.getString("ycmx"));

        }
        return receiptsBill;
    }


    public List<ReceiptsDt8> getReceiptsDt8s(String mainid, String tablename) {
        List<ReceiptsDt8> receiptsDt8s = new ArrayList<>();

        String sql = "select * from " + tablename + "_dt8 where mainid = " + mainid;
        rs.execute(sql);
        while (rs.next()) {
            ReceiptsDt8 receiptsDt8 = new ReceiptsDt8();
            receiptsDt8.setId(rs.getString("id"));
            receiptsDt8.setRequestid(rs.getString("requestid"));
            receiptsDt8.setJzkm(rs.getString("jzkm"));
            receiptsDt8.setKmh(rs.getString("kmh"));
            receiptsDt8.setJzje(rs.getString("jzje"));
            receiptsDt8.setJzxy(rs.getString("jzxy"));
            receiptsDt8.setJzf(rs.getString("jzf"));
            receiptsDt8.setXyzxdw(rs.getString("xyzxdw"));
            receiptsDt8.setSrxz(rs.getString("srxz"));
            receiptsDt8.setJzkmstr(rs.getString("jzkmstr"));
            receiptsDt8.setXm(rs.getString("xm"));
            receiptsDt8.setBz(rs.getString("bz"));
            receiptsDt8.setJzdhx(rs.getString("jzdhx"));
            receiptsDt8.setJzbh(rs.getString("jzbh"));
            receiptsDt8.setLb(rs.getString("lb"));
            receiptsDt8.setQtsm(rs.getString("qtsm"));
            receiptsDt8s.add(receiptsDt8);


        }
        return receiptsDt8s;
    }
}
