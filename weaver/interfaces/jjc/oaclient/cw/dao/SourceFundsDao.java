package weaver.interfaces.jjc.oaclient.cw.dao;

import weaver.conn.RecordSet;
import weaver.general.StaticObj;
import weaver.interfaces.datasource.DataSource;
import weaver.interfaces.jjc.oaclient.cw.dto.CapitalSourceDetailDto;
import weaver.interfaces.jjc.oaclient.cw.dto.PaymentInformationDto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SourceFundsDao {
    RecordSet rs = new RecordSet();

    public double getPzje(String paybillid) throws SQLException {

        DataSource ds = (DataSource) StaticObj.getServiceByFullname(("datasource.CRM"), DataSource.class); //获取CRM数据源 在集成中心配置 CRM 等于数据源名称
        Connection conn = ds.getConnection();
        Statement stm = null;
        ResultSet rs = null;
        double MoGrantId = 0;
        try {
            stm = conn.createStatement();
            String sql = "select debitamount from v_ap_paymentvoucher where paybillid = '" + paybillid + "'  and  documentstatus = 'C'";
            System.out.println("sql:" + sql);

            rs = stm.executeQuery(sql); //执行sql

            if (rs.next()) {
                MoGrantId = rs.getDouble("MoGrantId");
            }
            stm.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        }


        return MoGrantId;
    }

    /**
     * 查询报销项目
     *
     * @param requestid
     * @param tableName
     * @return
     */
    public int getXmid(String requestid, String tableName) {
        int xmid = 0;
        String sql = "select xmmc from " + tableName + " where requestid = " + requestid;
        rs.execute(sql);

        if (rs.next()) {
            xmid = rs.getInt("xmmc");
        }

        return xmid;
    }

    /**
     * 返回当前项目预算金额
     *
     * @param xmid
     * @return
     */
    public List<CapitalSourceDetailDto> getYsmx(int xmid) {
        List<CapitalSourceDetailDto> capitalSourceDetailDtoList = new ArrayList<>();
        String sql = "SELECT a.id, a.mainid , NVL(a.je, 0) - NVL(e.je, 0) AS je , c.jzkm, c.kmh, f.xyzckm FROM uf_xmzxbd_dt2 a LEFT JOIN uf_jzsrbd_dt2 b ON b.id = a.jzjzkm LEFT JOIN uf_jzd_dt2 c ON b.jzdbhjxq = c.id LEFT JOIN uf_jzd d ON d.id = c.mainid LEFT JOIN ( SELECT sum(je) AS je, xm, kmid, ZJLY FROM uf_zjlysy GROUP BY xm, kmid, ZJLY ) e ON e.xm = a.mainid AND e.kmid = c.kmh AND e.ZJLY = a.id LEFT JOIN uf_crzkmdy f ON c.kmh = f.jzkm WHERE a.mainid = " + xmid + " ORDER BY d.jzrq";
        rs.execute(sql);
        double lje = 0;
        while (rs.next()) {
            CapitalSourceDetailDto capitalSourceDetailDto = new CapitalSourceDetailDto();
            capitalSourceDetailDto.setId(rs.getInt("id"));
            capitalSourceDetailDto.setXmid(rs.getInt("mainid"));
            double je = rs.getDouble("je");
            lje += je;
            capitalSourceDetailDto.setJe(je);
            capitalSourceDetailDto.setLje(lje);
            capitalSourceDetailDto.setKmh(rs.getString("kmh"));
            capitalSourceDetailDto.setZckm(rs.getString("xyzckm"));
            capitalSourceDetailDtoList.add(capitalSourceDetailDto);
        }

        return capitalSourceDetailDtoList;
    }

    /**
     * 获取付款信息
     *
     * @param requestid
     * @param tableName
     * @return
     */
    public List<PaymentInformationDto> getPaymentInformationDtoList(String requestid, String tableName) {

        List<PaymentInformationDto> paymentInformationDtoList = new ArrayList<>();


        String sql = "select id ,skr ,skzh,skzh1,skje from " + tableName + "_dt2 where mainid = (select id from " + tableName + " where requestid = " + requestid + ")";
        rs.execute(sql);
        double lskje = 0;
        while (rs.next()) {
            PaymentInformationDto paymentInformationDto = new PaymentInformationDto();
            paymentInformationDto.setId(rs.getInt("id"));
            paymentInformationDto.setSkr(rs.getString("skr"));
            paymentInformationDto.setSkzh(rs.getString("skzh"));
            paymentInformationDto.setSkzh1(rs.getString("skzh1"));
            double skje = rs.getDouble("skje");
            lskje += skje;
            paymentInformationDto.setSkje(skje);
            paymentInformationDto.setLskje(lskje);
            paymentInformationDto.setLcmxid(rs.getInt("id"));
            paymentInformationDtoList.add(paymentInformationDto);


        }


        return paymentInformationDtoList;
    }

    public void deleteJl(String requestid) {
        String sql = "delete from UF_ZJLYSY where lc = " + requestid;
    }

    public void insetJl(List<PaymentInformationDto> paymentInformationDtoList, String requestid, int xmid) {
        System.out.println("进入插入逻辑了!" + "--" + paymentInformationDtoList.size());

        for (PaymentInformationDto paymentInformationDto : paymentInformationDtoList) {
            String sql = "INSERT INTO OA.UF_ZJLYSY( LC, JE, ZT, ZJLY, KMID,ZCKM, FORMMODEID, MODEDATACREATER, MODEDATACREATERTYPE, MODEDATACREATEDATE, MODEDATACREATETIME, MODEDATAMODIFIER, MODEDATAMODIFYDATETIME, MODEUUID, XM,lcmxid,skr,skzh,khx)" +
                    " VALUES (  '" + requestid + "', '" + paymentInformationDto.getSjje() + "', 0, '" + paymentInformationDto.getZjlyid() + "', '" + paymentInformationDto.getKmh() + "', '" + paymentInformationDto.getZckm() + "',3001, 1, 0, to_char(sysdate,'yyyy-MM-dd'), to_char(sysdate,'HH24:mi:ss'), NULL, NULL, NULL, '" + xmid + "','" + paymentInformationDto.getLcmxid() + "','" + paymentInformationDto.getSkr() + "','" + paymentInformationDto.getSkzh() + "','" + paymentInformationDto.getSkzh1() + "')";
            System.out.println(sql);
            rs.executeUpdate(sql);
        }

    }

    public void insetJlGd(List<PaymentInformationDto> paymentInformationDtoList, String requestid, int xmid) {
        System.out.println("进入插入逻辑了!" + "--" + paymentInformationDtoList.size());

        for (PaymentInformationDto paymentInformationDto : paymentInformationDtoList) {
            String sql = "INSERT INTO OA.UF_ZJLYSY( LC, JE, ZT, ZJLY, KMID,ZCKM, FORMMODEID, MODEDATACREATER, MODEDATACREATERTYPE, MODEDATACREATEDATE, MODEDATACREATETIME, MODEDATAMODIFIER, MODEDATAMODIFYDATETIME, MODEUUID, XM,lcmxid,skr,skzh,khx)" +
                    " VALUES (  '" + requestid + "', '" + paymentInformationDto.getSjje() + "', 1, '" + paymentInformationDto.getZjlyid() + "', '" + paymentInformationDto.getKmh() + "', '" + paymentInformationDto.getZckm() + "',3001, 1, 0, to_char(sysdate,'yyyy-MM-dd'), to_char(sysdate,'HH24:mi:ss'), NULL, NULL, NULL, '" + xmid + "','" + paymentInformationDto.getLcmxid() + "','" + paymentInformationDto.getSkr() + "','" + paymentInformationDto.getSkzh() + "','" + paymentInformationDto.getSkzh1() + "')";
            System.out.println(sql);
            rs.executeUpdate(sql);
        }

    }
}
