package weaver.interfaces.jjc.oaclient.cw.dao;

import weaver.conn.RecordSet;
import weaver.interfaces.jjc.oaclient.cw.dto.ReimbursementBill;
import weaver.interfaces.jjc.oaclient.cw.dto.ReimbursementDt2;
import weaver.interfaces.jjc.oaclient.cw.dto.ReimbursementDt4;

import java.util.ArrayList;
import java.util.List;

public class ReimbursementDao {
    RecordSet rs = new RecordSet();

    public ReimbursementBill getReimbursementBill(String requestid, String tablename) {
        ReimbursementBill reimbursementBill = new ReimbursementBill();
        String sql = "select * from " + tablename + " where requestid = " + requestid;
        rs.execute(sql);
        if (rs.next()) {
            reimbursementBill.setId(rs.getString("id"));
            reimbursementBill.setRequestid(rs.getString("requestid"));
            reimbursementBill.setDcryxxan(rs.getString("dcryxxan"));
            reimbursementBill.setMxdr(rs.getString("mxdr"));
            reimbursementBill.setLcbh(rs.getString("lcbh"));
            reimbursementBill.setSqrq(rs.getString("sqrq"));
            reimbursementBill.setBxry(rs.getString("bxry"));
            reimbursementBill.setBxfs(rs.getString("bxfs"));
            reimbursementBill.setZxdw(rs.getString("zxdw"));
            reimbursementBill.setXgfj(rs.getString("xgfj"));
            reimbursementBill.setSkjehjy(rs.getString("skjehjy"));
            reimbursementBill.setSkjehjdx(rs.getString("skjehjdx"));
            reimbursementBill.setBxyt(rs.getString("bxyt"));
            reimbursementBill.setXclw(rs.getString("xclw"));
            reimbursementBill.setYsr(rs.getString("ysr"));
            reimbursementBill.setYsrgh(rs.getString("ysrgh"));
            reimbursementBill.setXmfzr(rs.getString("xmfzr"));
            reimbursementBill.setXmfzrgh(rs.getString("xmfzrgh"));
            reimbursementBill.setScqtcl(rs.getString("scqtcl"));
            reimbursementBill.setCxjehjy(rs.getString("cxjehjy"));
            reimbursementBill.setThjehjy(rs.getString("thjehjy"));
            reimbursementBill.setBxjehjy(rs.getString("bxjehjy"));
            reimbursementBill.setXmbh(rs.getString("xmbh"));
            reimbursementBill.setK3fkdid(rs.getString("k3fkdid"));
            reimbursementBill.setSfxyhq(rs.getString("sfxyhq"));
            reimbursementBill.setXzhqr(rs.getString("xzhqr"));
            reimbursementBill.setSfxyxgje(rs.getString("sfxyxgje"));
            reimbursementBill.setYt(rs.getString("yt"));
            reimbursementBill.setSfxymsczrhq(rs.getString("sfxymsczrhq"));
            reimbursementBill.setXzmsczr(rs.getString("xzmsczr"));
            reimbursementBill.setSfxyhqe(rs.getString("sfxyhqe"));
            reimbursementBill.setXzhqre(rs.getString("xzhqre"));
            reimbursementBill.setXmmc(rs.getString("xmmc"));
            reimbursementBill.setSfndys(rs.getString("sfndys"));
            reimbursementBill.setNdyse(rs.getString("ndyse"));
            reimbursementBill.setXmmcwb(rs.getString("xmmcwb"));
            reimbursementBill.setLsbxje(rs.getString("lsbxje"));
            reimbursementBill.setLjbxzehbc(rs.getString("ljbxzehbc"));
            reimbursementBill.setRyfffhj(rs.getString("ryfffhj"));
            reimbursementBill.setFymxbxhj(rs.getString("fymxbxhj"));
            reimbursementBill.setJkjehj(rs.getString("jkjehj"));
            reimbursementBill.setMx2szhj(rs.getString("mx2szhj"));
            reimbursementBill.setSfrygz(rs.getString("sfrygz"));
            reimbursementBill.setYcryxx(rs.getString("ycryxx"));
            reimbursementBill.setYcskmx(rs.getString("ycskmx"));
            reimbursementBill.setFfhjdx(rs.getString("ffhjdx"));
            reimbursementBill.setZcjehj(rs.getString("zcjehj"));
            reimbursementBill.setZcjehjdx(rs.getString("zcjehjdx"));
            reimbursementBill.setZdylj(rs.getString("zdylj"));
            reimbursementBill.setBxyt1(rs.getString("bxyt1"));
            reimbursementBill.setHqlcqqid(rs.getString("hqlcqqid"));

        }


        return reimbursementBill;
    }


    public List<ReimbursementDt2> getReimbursementDt2List(String mainid, String tablename) {
        List<ReimbursementDt2> reimbursementDt2s = new ArrayList<>();

        String sql = "select * from " + tablename + "_dt2 where mainid = " + mainid+" order by id";
        rs.execute(sql);
        while (rs.next()) {
            ReimbursementDt2 reimbursementDt2 = new ReimbursementDt2();
            reimbursementDt2.setMainid(rs.getString("mainid"));
            reimbursementDt2.setId(rs.getString("id"));
            reimbursementDt2.setSkr(rs.getString("skr"));
            reimbursementDt2.setSkzh(rs.getString("skzh"));
            reimbursementDt2.setKhx(rs.getString("khx"));
            reimbursementDt2.setSkjey(rs.getString("skjey"));
            reimbursementDt2.setLhh(rs.getString("lxh"));
            reimbursementDt2s.add(reimbursementDt2);
        }

        return reimbursementDt2s;

    }

    public List<ReimbursementDt4> getReimbursementDt4List(String mainid, String tablename) {
        List<ReimbursementDt4> reimbursementDt4s = new ArrayList<>();
        String sql = "select * from " + tablename + "_dt4 where mainid = " + mainid+" order by id";
        rs.execute(sql);
        while (rs.next()) {
            ReimbursementDt4 reimbursementDt4 = new ReimbursementDt4();
            reimbursementDt4.setMainid(rs.getString("mainid"));
            reimbursementDt4.setId(rs.getString("id"));
            reimbursementDt4.setJzkm(rs.getString("jzkm"));
            reimbursementDt4.setKmh(rs.getString("kmh"));
            reimbursementDt4.setJzxy(rs.getString("jzxy"));
            reimbursementDt4.setJzbhwb(rs.getString("jzbhwb"));
            reimbursementDt4.setJzdjl(rs.getString("jzdjl"));
            reimbursementDt4.setJe(rs.getDouble("je"));
            reimbursementDt4.setJzbh(rs.getString("jzbh"));
            reimbursementDt4.setZckm(rs.getString("zckm"));
            reimbursementDt4s.add(reimbursementDt4);
        }
        return reimbursementDt4s;

    }

    public List<ReimbursementDt2> getReimbursementDt5List(String mainid, String tablename) {
        List<ReimbursementDt2> reimbursementDt5s = new ArrayList<>();

        String sql = "select * from " + tablename + "_dt5 where mainid = " + mainid+" order by id";
        rs.execute(sql);
        while (rs.next()) {
            ReimbursementDt2 reimbursementDt5 = new ReimbursementDt2();
            reimbursementDt5.setMainid(rs.getString("mainid"));
            reimbursementDt5.setId(rs.getString("id"));
            reimbursementDt5.setSkr(rs.getString("xm"));
            reimbursementDt5.setKhx(rs.getString("yxjkhx"));
            reimbursementDt5.setSkjey(rs.getString("shje"));
            reimbursementDt5.setSe(rs.getString("ks"));
            reimbursementDt5.setSkzh(rs.getString("yxkh"));
            reimbursementDt5.setLhh(rs.getString("lxh"));
            reimbursementDt5s.add(reimbursementDt5);

        }

        return reimbursementDt5s;

    }

}
