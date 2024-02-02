package weaver.interfaces.jjc.oaclient.cw.service;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.jjc.oaclient.cw.dao.SourceFundsDao;
import weaver.interfaces.jjc.oaclient.cw.dto.CapitalSourceDetailDto;
import weaver.interfaces.jjc.oaclient.cw.dto.PaymentInformationDto;

import java.util.ArrayList;
import java.util.List;

public class VirtualExpenditureService {
    SourceFundsDao sourceFundsDao = new SourceFundsDao();

    public void exec(String requestid, String tableName) {
        RecordSet rs = new RecordSet();
        RecordSet rs2 = new RecordSet();
        //查询项目回传id
        String sql = "select id,xmzx from " + tableName + " where requestid = " + requestid;
        rs.execute(sql);
        int id = 0;
        int xmzx = 0;
        if (rs.next()) {
            id = rs.getInt("id");
            xmzx = rs.getInt("xmzx");
        }
        double xnzccshy = 0;
        sql = "select sum(xnzccshy) as xnzccshy from " + tableName + "_dt1 where mainid = " + id;
        rs.execute(sql);
        if (rs.next()) {
            xnzccshy = rs.getDouble("xnzccshy");
        }

        List<PaymentInformationDto> paymentInformationDtoList = new ArrayList<>();

        if (xnzccshy > 0) {
            List<CapitalSourceDetailDto> capitalSourceDetailDtoList = sourceFundsDao.getYsmx(xmzx);
            double zje = capitalSourceDetailDtoList.get(capitalSourceDetailDtoList.size() - 1).getLje();
            for (CapitalSourceDetailDto capitalSourceDetailDto : capitalSourceDetailDtoList) {
                //预算剩余金额
                double yssyje = capitalSourceDetailDto.getJe();
                System.out.println("预算金额:" + yssyje);
                System.out.println("使用金额" + xnzccshy);
                if (yssyje > 0) {//该科目还有预算余额
                    if (yssyje >= xnzccshy) {//情况一:科目余额大于等于需要的金额
                        System.out.println("直接扣减");
                        PaymentInformationDto newPaymentInformationDto = new PaymentInformationDto();
                        newPaymentInformationDto.setZjlyid(capitalSourceDetailDto.getId());
                        newPaymentInformationDto.setSkje(xnzccshy);
                        newPaymentInformationDto.setSjje(xnzccshy);
                        newPaymentInformationDto.setZckm(capitalSourceDetailDto.getZckm());
                        newPaymentInformationDto.setKmh(capitalSourceDetailDto.getKmh());
                        paymentInformationDtoList.add(newPaymentInformationDto);
                        System.out.println("直接扣减" + paymentInformationDtoList.size());
                        break;
                    } else {
                        System.out.println("扣减多个预算");
                        double syje = xnzccshy - yssyje;
                        System.out.println("预算金额盘算:" + syje);
                        PaymentInformationDto newPaymentInformationDto2 = new PaymentInformationDto();
                        newPaymentInformationDto2.setKmh(capitalSourceDetailDto.getKmh());
                        newPaymentInformationDto2.setSkje(yssyje);
                        newPaymentInformationDto2.setSjje(yssyje);
                        newPaymentInformationDto2.setZjlyid(capitalSourceDetailDto.getId());
                        newPaymentInformationDto2.setZckm(capitalSourceDetailDto.getZckm());
                        paymentInformationDtoList.add(newPaymentInformationDto2);
                        capitalSourceDetailDto.setJe(0);

                        PaymentInformationDto newPaymentInformationDto = new PaymentInformationDto();
                        newPaymentInformationDto.setZjlyid(capitalSourceDetailDto.getId());
                        newPaymentInformationDto.setSkje(syje);
                        newPaymentInformationDto.setKmh(capitalSourceDetailDto.getKmh());
                        newPaymentInformationDto.setZckm(capitalSourceDetailDto.getZckm());
                        paymentInformationDtoList.add(newPaymentInformationDto);
                        break;
                    }


                }


            }
            System.out.println("准备插入" + paymentInformationDtoList.size());
            sourceFundsDao.insetJlGd(paymentInformationDtoList, requestid, xmzx);

            String sql2 = "insert into uf_xmzxbd_dt5 (mainid,rq,fy,yt,bxlc) VALUES ('" + xmzx + "',to_char(sysdate,'yyyy-MM-dd'),'" + xnzccshy + "','虚拟支出-初始化','" + requestid + "')";
            rs.executeUpdate(sql2);
        }
        sql = "select yskm,xnzccshy from " + tableName + "_dt1 where mainid = " + id;
        rs.execute(sql);
        while (rs.next()) {
            String yskm = rs.getString("yskm");
            double xnzccshy1 = Util.getDoubleValue(rs.getString("xnzccshy"), 0);
            if (xnzccshy1 > 0) {
                String sql2 = "INSERT INTO uf_bxys ( REQUESTID, LC, XM, YSFX, JE, ZT, FORMMODEID, MODEDATACREATER, MODEDATACREATERTYPE, MODEDATACREATEDATE, MODEDATACREATETIME, MODEDATAMODIFIER, MODEDATAMODIFYDATETIME, MODEUUID) VALUES ( NULL, '" + requestid + "', '" + xmzx + "', '" + yskm + "', '" + xnzccshy1 + "', '2', '2501', '3726', '0', NULL,NULL, NULL, NULL, NULL)";
                rs2.executeUpdate(sql2);

            }


        }


    }

}
