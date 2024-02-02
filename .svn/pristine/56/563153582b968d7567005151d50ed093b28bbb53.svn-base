package weaver.interfaces.jjc.oaclient.cw.service;

import weaver.conn.RecordSet;
import weaver.interfaces.jjc.oaclient.cw.dao.SourceFundsDao;
import weaver.interfaces.jjc.oaclient.cw.dto.CapitalSourceDetailDto;
import weaver.interfaces.jjc.oaclient.cw.dto.PaymentInformationDto;

import java.util.List;

/**
 * 预算分派逻辑
 */
public class SourceFundsService2 {
    SourceFundsDao sourceFundsDao = new SourceFundsDao();

    public void exec(String requestid, String tableName) throws Exception {
        String k3fkdid = "";
        double jehj = 0.00;
        RecordSet rs = new RecordSet();
        String sql = "select k3fkdid,jehj from " + tableName + " where requestid = " + requestid;
        rs.execute(sql);
        if (rs.next()) {
            k3fkdid = rs.getString("k3fkdid");
            jehj = rs.getDouble("jehj");
        }
        double pzje = sourceFundsDao.getPzje(k3fkdid);
        if (pzje <= 0) {
            throw new Exception("未查询到该付款单生成的审核完成凭证!");
        }
        if (jehj == pzje) {
            return;
        }
        rs.executeUpdate("delete from uf_zjlysy where lc = " + requestid);
        rs.executeUpdate("delete from uf_bxys where lc = " + requestid);
        int xmid = sourceFundsDao.getXmid(requestid, tableName);
        List<CapitalSourceDetailDto> capitalSourceDetailDtoList = sourceFundsDao.getYsmx(xmid);
        List<PaymentInformationDto> paymentInformationDtoList = sourceFundsDao.getPaymentInformationDtoList(requestid, tableName);

        double zje = capitalSourceDetailDtoList.get(capitalSourceDetailDtoList.size() - 1).getLje();
        double zskje = paymentInformationDtoList.get(paymentInformationDtoList.size() - 1).getLskje();
        if (zskje > zje) {
            throw new Exception("报销金额超出项目资金预算.");
        }

        for (int i = 0; i < paymentInformationDtoList.size(); i++) {
            //需要金额
            double xyje = paymentInformationDtoList.get(i).getSkje();
            for (CapitalSourceDetailDto capitalSourceDetailDto : capitalSourceDetailDtoList) {
                //预算剩余金额
                double yssyje = capitalSourceDetailDto.getJe();
                if (yssyje > 0) {//该科目还有预算余额
                    if (yssyje >= xyje) {//情况一:科目余额大于等于需要的金额
                        paymentInformationDtoList.get(i).setKmh(capitalSourceDetailDto.getKmh());
                        paymentInformationDtoList.get(i).setSjje(xyje);
                        capitalSourceDetailDto.setJe(yssyje - xyje);
                        break;
                    } else {
                        double syje = xyje - yssyje;
                        paymentInformationDtoList.get(i).setKmh(capitalSourceDetailDto.getKmh());
                        paymentInformationDtoList.get(i).setSkje(yssyje);
                        paymentInformationDtoList.get(i).setSjje(yssyje);
                        capitalSourceDetailDto.setJe(0);
                        PaymentInformationDto newPaymentInformationDto = new PaymentInformationDto();

                        newPaymentInformationDto.setId(capitalSourceDetailDto.getId());
                        newPaymentInformationDto.setSkje(syje);
                        newPaymentInformationDto.setSkr(paymentInformationDtoList.get(i).getSkr());
                        newPaymentInformationDto.setSkzh(paymentInformationDtoList.get(i).getSkzh());
                        newPaymentInformationDto.setSkzh1(paymentInformationDtoList.get(i).getSkzh1());
                        paymentInformationDtoList.add(newPaymentInformationDto);
                        break;
                    }


                }

            }
        }

        sourceFundsDao.insetJl(paymentInformationDtoList, requestid, xmid);
    }


}
