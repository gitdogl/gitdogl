package weaver.interfaces.jjc.oaclient.cw.service;

import weaver.interfaces.jjc.oaclient.cw.dao.SourceFundsDao;
import weaver.interfaces.jjc.oaclient.cw.dto.CapitalSourceDetailDto;
import weaver.interfaces.jjc.oaclient.cw.dto.PaymentInformationDto;

import java.util.List;

/**
 * 预算分派逻辑
 */
public class SourceFundsService {
    SourceFundsDao sourceFundsDao = new SourceFundsDao();

    public void exec(String requestid, String tableName) throws Exception {

        int xmid = sourceFundsDao.getXmid(requestid, tableName);
        List<CapitalSourceDetailDto> capitalSourceDetailDtoList = sourceFundsDao.getYsmx(xmid);
        List<PaymentInformationDto> paymentInformationDtoList = sourceFundsDao.getPaymentInformationDtoList(requestid, tableName);

        double zje = capitalSourceDetailDtoList.get(capitalSourceDetailDtoList.size() - 1).getLje();
        double zskje = paymentInformationDtoList.get(paymentInformationDtoList.size() - 1).getLskje();
        System.out.println("资金来源:" + zje);
        System.out.println("付款信息:" + zskje);
        if (zskje > zje) {
            throw new Exception("报销金额超出项目资金预算.");
        }
        for (int i = 0; i < paymentInformationDtoList.size(); i++) {
            //需要金额
            double xyje = paymentInformationDtoList.get(i).getSkje();
            System.out.println("需要金额:" + xyje);
            for (CapitalSourceDetailDto capitalSourceDetailDto : capitalSourceDetailDtoList) {
                //预算剩余金额
                double yssyje = capitalSourceDetailDto.getJe();
                System.out.println("预算金额:" + yssyje);
                if (yssyje > 0) {//该科目还有预算余额
                    if (yssyje >= xyje) {//情况一:科目余额大于等于需要的金额
                        paymentInformationDtoList.get(i).setKmh(capitalSourceDetailDto.getKmh());
                        paymentInformationDtoList.get(i).setSjje(xyje);
                        paymentInformationDtoList.get(i).setZjlyid(capitalSourceDetailDto.getId());
                        paymentInformationDtoList.get(i).setZckm(capitalSourceDetailDto.getZckm());
                        capitalSourceDetailDto.setJe(yssyje - xyje);
                        break;
                    } else {
                        double syje = xyje - yssyje;
                        System.out.println("预算金额盘算:" + syje);
                        paymentInformationDtoList.get(i).setKmh(capitalSourceDetailDto.getKmh());
                        paymentInformationDtoList.get(i).setSkje(yssyje);
                        paymentInformationDtoList.get(i).setSjje(yssyje);
                        paymentInformationDtoList.get(i).setZjlyid(capitalSourceDetailDto.getId());
                        paymentInformationDtoList.get(i).setZckm(capitalSourceDetailDto.getZckm());
                        capitalSourceDetailDto.setJe(0);
                        PaymentInformationDto newPaymentInformationDto = new PaymentInformationDto();
                        newPaymentInformationDto.setZjlyid(capitalSourceDetailDto.getId());
                        newPaymentInformationDto.setSkje(syje);
                        newPaymentInformationDto.setSkr(paymentInformationDtoList.get(i).getSkr());
                        newPaymentInformationDto.setSkzh(paymentInformationDtoList.get(i).getSkzh());
                        newPaymentInformationDto.setSkzh1(paymentInformationDtoList.get(i).getSkzh1());
                        newPaymentInformationDto.setLcmxid(paymentInformationDtoList.get(i).getLcmxid());
                        newPaymentInformationDto.setZckm(capitalSourceDetailDto.getZckm());
                        paymentInformationDtoList.add(newPaymentInformationDto);
                        break;
                    }
                }
            }
        }

        sourceFundsDao.insetJl(paymentInformationDtoList, requestid, xmid);
    }


}
