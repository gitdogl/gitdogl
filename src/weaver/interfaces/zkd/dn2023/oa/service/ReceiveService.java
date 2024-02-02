package weaver.interfaces.zkd.dn2023.oa.service;

import com.alibaba.fastjson.JSON;
import weaver.file.ImageFileManager;
import weaver.general.BaseBean;
import weaver.interfaces.zkd.dn2023.bhl.entity.*;
import weaver.interfaces.zkd.dn2023.bhl.serivce.DatePushService;
import weaver.interfaces.zkd.dn2023.oa.dao.DatsjlDao;
import weaver.interfaces.zkd.dn2023.oa.dao.ReceiveDao;
import weaver.interfaces.zkd.dn2023.oa.entity.*;
import weaver.interfaces.zkd.dn2023.oa.util.BaseDao;
import weaver.interfaces.zkd.dn2023.oa.util.DocxPdfGetPagesUtil;
import weaver.interfaces.zkd.dn2023.oa.util.StaticCode;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @BelongsProject: ZKD_DA_2023
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.oa.service
 * @Author: hf_zhangyuan
 * @CreateTime: 2023-03-07  17:55
 * @Description: 收文推送
 * @Version: 1.0
 */

public class ReceiveService extends BaseBean {
    private final String url = "http://e.ustc.edu.cn";

    ReceiveDao receiveDao = new ReceiveDao();
    DatsjlDao datsjlDao = new DatsjlDao();
    BaseDao baseDao = new BaseDao();
    private List<OADocEntity> list;

    public void exec(String id){
        String swtableName = "formtable_main_3";

        List<DatsjlEntity> datsjlEntities = datsjlDao.getDatsjlEntity(id);
        for (DatsjlEntity datsjlEntity : datsjlEntities) {
            DataBill dataBill = new DataBill();
            OAReceiveEntity oaReceiveEntity = receiveDao.getReceiveMes(swtableName, datsjlEntity.getLc());
            SWDataBill swDataBill = getSWDataBill(oaReceiveEntity, datsjlEntity.getLc());
            List<BussDataDto> buss_datas = getBussDataDtos(datsjlEntity.getLc());
            List<OriginFileDto> origin_files = getOriginFileDtos(datsjlEntity.getLcym());
            dataBill.setArch_data(swDataBill);
            dataBill.setBuss_datas(buss_datas);
            dataBill.setOrigin_files(origin_files);
            dataBill.setOrigin_id(datsjlEntity.getLc());

            HeadDataBill headDataBill = new HeadDataBill();
            headDataBill.setArch_type("2");
            headDataBill.setCheck_name("OA");
            headDataBill.setSys_name("OA");
            List<DataBill> data = new ArrayList<>();
            data.add(dataBill);
            headDataBill.setData(data);
            System.out.println("收文发送格式:" + JSON.toJSONString(headDataBill));
            DatePushService datePushService = new DatePushService();
            baseDao.updateData(datsjlEntity.getLc(),"0","");
            List<FhBill>  errorRequestid = datePushService.postData(headDataBill);
            for (FhBill requestid : errorRequestid){
                baseDao.updateData(requestid.getDataId(),"1",requestid.getMessage());
            }

        }
    }

    public void exec() {
        String swtableName = "formtable_main_3";

        List<DatsjlEntity> datsjlEntities = datsjlDao.getDatsjlEntitys(swtableName);
        for (DatsjlEntity datsjlEntity : datsjlEntities) {
            DataBill dataBill = new DataBill();
            OAReceiveEntity oaReceiveEntity = receiveDao.getReceiveMes(swtableName, datsjlEntity.getLc());
            SWDataBill swDataBill = getSWDataBill(oaReceiveEntity, datsjlEntity.getLc());
            List<BussDataDto> buss_datas = getBussDataDtos(datsjlEntity.getLc());
            List<OriginFileDto> origin_files = getOriginFileDtos(datsjlEntity.getLcym());
            dataBill.setArch_data(swDataBill);
            dataBill.setBuss_datas(buss_datas);
            dataBill.setOrigin_files(origin_files);
            dataBill.setOrigin_id(datsjlEntity.getLc());

            HeadDataBill headDataBill = new HeadDataBill();
            headDataBill.setArch_type("2");
            headDataBill.setCheck_name("OA");
            headDataBill.setSys_name("OA");
            List<DataBill> data = new ArrayList<>();
            data.add(dataBill);
            headDataBill.setData(data);
            System.out.println("收文发送格式:" + JSON.toJSONString(headDataBill));
            DatePushService datePushService = new DatePushService();
            baseDao.updateData(datsjlEntity.getLc(),"0","");
            List<FhBill>  errorRequestid = datePushService.postData(headDataBill);
            for (FhBill requestid : errorRequestid){
                baseDao.updateData(requestid.getDataId(),"1",requestid.getMessage());
            }

        }


    }

    private List<OriginFileDto> getOriginFileDtos(String lcym) {
        System.out.println("流程页面id:"+lcym);
        List<OriginFileDto> originFileDtos = new ArrayList<>();
        for (OADocEntity docEntity : list) {
            OriginFileDto bldOriginFileDto = new OriginFileDto();
            bldOriginFileDto.setOrder_type("3");
            bldOriginFileDto.setFile_name(docEntity.getImageFileName());
            bldOriginFileDto.setFile_type(docEntity.getImageFileName().substring(docEntity.getImageFileName().lastIndexOf(".")));
            bldOriginFileDto.setFile_url(url + "/weaver/weaver.file.FileDownloadForBHL?imagefileid=" + docEntity.getImageFileId());
            originFileDtos.add(bldOriginFileDto);
        }
        List<OADocEntity> lcymdocEntitys = baseDao.getZWEntityPdffj(lcym);
        System.out.println("lcymdocEntitys:"+JSON.toJSONString(lcymdocEntitys));
        if (lcymdocEntitys.size() > 0) {
            OADocEntity lcymdocEntity = lcymdocEntitys.get(0);
            String bldname = "办理单.pdf";
            OriginFileDto bldOriginFileDto = new OriginFileDto();
            bldOriginFileDto.setOrder_type("1");
            bldOriginFileDto.setFile_name(bldname);
            bldOriginFileDto.setFile_type(bldname.substring(bldname.lastIndexOf(".")));
            bldOriginFileDto.setFile_url(url + "/weaver/weaver.file.FileDownloadForBHL?imagefileid=" + lcymdocEntity.getImageFileId());
            originFileDtos.add(bldOriginFileDto);
        }


        return originFileDtos;
    }

    private List<BussDataDto> getBussDataDtos(String requestid) {
        List<BussDataDto> bhlBusinessEntities = new ArrayList<>();

        List<OAWorkFlowRequestLogEntity> oaWorkFlowRequestLogEntities = baseDao.getWorkFlowRequestLogMes(requestid);
        for (OAWorkFlowRequestLogEntity oaWorkFlowRequestLogEntity : oaWorkFlowRequestLogEntities) {
            BussDataDto bussDataDto = new BussDataDto();

            bussDataDto.setAction_sort(oaWorkFlowRequestLogEntity.getRequestId());
            if ("0".equals(oaWorkFlowRequestLogEntity.getOperateType())) {
                OAHumanMesEntity oaHumanMesEntity = baseDao.getHumanMes(oaWorkFlowRequestLogEntity.getOperator());
                bussDataDto.setPerson(oaHumanMesEntity.getName());
            } else if ("1".equals(oaWorkFlowRequestLogEntity.getOperateType())) {
                bussDataDto.setPerson(baseDao.getCustomerName(oaWorkFlowRequestLogEntity.getOperator()));
            } else {
                bussDataDto.setPerson("管理员");
            }
            String operateDate = oaWorkFlowRequestLogEntity.getOperateDate();
            String operateTime = oaWorkFlowRequestLogEntity.getOperateTime();
            String date = baseDao.getSplited(operateDate, "-");
            String time = baseDao.getSplited(operateTime, ":");
            bussDataDto.setDate_time(date + time);
            String logType = oaWorkFlowRequestLogEntity.getLogType();
            bussDataDto.setAction(StaticCode.getLogType(logType));
            String remark = oaWorkFlowRequestLogEntity.getRemark();
            if (remark != null && !"".equals(remark)) {
                bussDataDto.setDescription(remark.replaceAll("\\<.*?>", ""));
            } else {
                bussDataDto.setDescription("");
            }
            bussDataDto.setBasis("");
            bhlBusinessEntities.add(bussDataDto);
        }
        return bhlBusinessEntities;
    }


    /**
     * @description: 转义发文主表数据
     * @author: hf_zhangyuan
     * @date: 2023/3/12 16:05
     * @param:
     * @return:
     **/
    private SWDataBill getSWDataBill(OAReceiveEntity oaReceiveEntity, String requestId) {
        SWDataBill swDataBill = new SWDataBill();


        if ("无".equals(oaReceiveEntity.getLwwh()) || oaReceiveEntity.getLwwh() == null) {
            swDataBill.setDabh("无");
        } else {
            swDataBill.setDabh(oaReceiveEntity.getLwwh());
        }

        swDataBill.setTm(baseDao.getRequestName(requestId));
        swDataBill.setNd(oaReceiveEntity.getSwrq().substring(0, oaReceiveEntity.getSwrq().indexOf("-")));
        swDataBill.setJjcd(baseDao.getSelectName("6407", oaReceiveEntity.getJjcd()));
        swDataBill.setLwrq(baseDao.getSplited(oaReceiveEntity.getSwrq(), "-"));
        swDataBill.setGksx(baseDao.getSelectName("6409", oaReceiveEntity.getGwsx()));
        swDataBill.setZrz(oaReceiveEntity.getLwdw());
        //归档人部门
        String humanId = baseDao.getCreatePersonId(requestId);
        String departmentId = baseDao.getDepartmentIdByHumId(humanId);
        swDataBill.setSsdwmc(baseDao.getInstitutionById(departmentId));
        swDataBill.setGdrq(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        swDataBill.setIndbisnew("1");
        String fj = oaReceiveEntity.getFj();
        list = baseDao.getImageFileId(fj);
        if ((list != null)) {
            try {
                swDataBill.setFjys(String.valueOf(getAttachedFilePages()));
            } catch (Exception e) {
                e.printStackTrace();
                swDataBill.setFjys("1");
            }
        } else {
            swDataBill.setFjys("1");
        }
        swDataBill.setMj(baseDao.getSelectName("6408", oaReceiveEntity.getMj()));
        swDataBill.setGwzl(baseDao.getSelectName("6421", oaReceiveEntity.getGwzlxx()));
        swDataBill.setSwbh(oaReceiveEntity.getSwbh());
        //传阅范围
        String cyfwyName = "";
        String cyfweName = "";
        if (oaReceiveEntity.getCyfwy() != null && !"".equals(oaReceiveEntity.getCyfwy())) {
            cyfwyName = baseDao.getHumanNameById(oaReceiveEntity.getCyfwy());
        }
        if (oaReceiveEntity.getCyfwe() != null && !"".equals(oaReceiveEntity.getCyfwe())) {
            cyfweName = baseDao.getHumanNameById(oaReceiveEntity.getCyfwe());
        }
        String finalName = cyfwyName + "," + cyfweName;
        String finalName0 = "";
        if (finalName.startsWith(",")) {
            finalName0 = finalName.substring(1);
        } else {
            finalName0 = finalName;
        }
        swDataBill.setCyfw(finalName0);

        if (oaReceiveEntity.getBz() == null || "".equals(oaReceiveEntity.getBz())) {
            swDataBill.setBz("");
        } else {
            swDataBill.setBz(oaReceiveEntity.getBz());
        }
        swDataBill.setIndbisnew("1");


        return swDataBill;
    }

    /**
     * 获取附件docx和pdf文档的总页数+1
     *
     * @return
     * @throws IOException
     */
    private int getAttachedFilePages() {
        int pages = 0;
        int docxPages = 0;
        int pdfPages = 0;
        DocxPdfGetPagesUtil docxPdfGetPagesUtil = new DocxPdfGetPagesUtil();
        System.out.println("list size: " + list.size());
        for (OADocEntity docEntity : list) {
            String imageFileId = docEntity.getImageFileId();
            InputStream is = ImageFileManager.getInputStreamById(Integer.parseInt(imageFileId));
            System.out.println("inputStream: " + is);
            String imageFileName = docEntity.getImageFileName();
            int lastIndex = imageFileName.lastIndexOf(".");
            String suffix = imageFileName.substring(lastIndex + 1);
            if ("docx".equals(suffix)) {
                docxPages += docxPdfGetPagesUtil.getDocxPages(is);
            } else if ("doc".equals(suffix)) {
                docxPages += docxPdfGetPagesUtil.getDocPages(is);
            } else if ("pdf".equals(suffix)) {
                pdfPages += docxPdfGetPagesUtil.getPdfPages(is);
            } else {
                continue;
            }
        }
        pages = docxPages + pdfPages + 1;
        return pages;
    }
}
