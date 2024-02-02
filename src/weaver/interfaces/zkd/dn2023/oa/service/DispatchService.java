package weaver.interfaces.zkd.dn2023.oa.service;

import com.alibaba.fastjson.JSON;
import weaver.file.ImageFileManager;
import weaver.general.BaseBean;
import weaver.hrm.User;
import weaver.interfaces.zkd.dn2023.bhl.entity.*;
import weaver.interfaces.zkd.dn2023.bhl.serivce.DatePushService;
import weaver.interfaces.zkd.dn2023.oa.dao.DatsjlDao;
import weaver.interfaces.zkd.dn2023.oa.dao.DispatchDao;
import weaver.interfaces.zkd.dn2023.oa.dao.ReceiveDao;
import weaver.interfaces.zkd.dn2023.oa.entity.*;
import weaver.interfaces.zkd.dn2023.oa.util.BaseDao;
import weaver.interfaces.zkd.dn2023.oa.util.DocxPdfGetPagesUtil;
import weaver.interfaces.zkd.dn2023.oa.util.StaticCode;
import weaver.workflow.request.ResourceConditionManager;

import java.io.File;
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
 * @Description: 发文推送
 * @Version: 1.0
 */

public class DispatchService extends BaseBean {
    private final String url = "http://e.ustc.edu.cn";
    DispatchDao dispatchDao = new DispatchDao();

    DatsjlDao datsjlDao = new DatsjlDao();
    BaseDao baseDao = new BaseDao();
    private List<OADocEntity> list;
    private int fjys = 1;

    public void exec(String id){
        String fwtableName = "formtable_main_2";
        List<DatsjlEntity> datsjlEntities = datsjlDao.getDatsjlEntity(id);
        for (DatsjlEntity datsjlEntity : datsjlEntities) {

            String bt = baseDao.getRequestName(datsjlEntity.getLc());
            HeadDataBill headDataBill = new HeadDataBill();
            headDataBill.setArch_type("1");
            headDataBill.setCheck_name("OA");
            headDataBill.setSys_name("OA");

            List<DataBill> data = new ArrayList<>();
            DataBill dataBill = new DataBill();

            OADispatchEntity oaDispatchEntity = dispatchDao.getOADispatchEntitys(fwtableName, datsjlEntity.getLc());
            FWDataBill fwDataBill = getFWDataBill(oaDispatchEntity);
            List<BussDataDto> buss_datas = getBussDataDtos(datsjlEntity.getLc());
            List<OriginFileDto> origin_files = getOriginFileDtos(bt, oaDispatchEntity, datsjlEntity.getLcym());
            fwDataBill.setFjys(fjys + "");
            dataBill.setArch_data(fwDataBill);
            dataBill.setBuss_datas(buss_datas);
            dataBill.setOrigin_files(origin_files);
            dataBill.setOrigin_id(oaDispatchEntity.getRequestid());


            data.add(dataBill);
            headDataBill.setData(data);
            System.out.println("发文发送格式:" + JSON.toJSONString(headDataBill));
            DatePushService datePushService = new DatePushService();
            baseDao.updateData(datsjlEntity.getLc(),"0","");
            List<FhBill>  errorRequestid = datePushService.postData(headDataBill);
            for (FhBill requestid : errorRequestid){
                baseDao.updateData(requestid.getDataId(),"1",requestid.getMessage());
            }

        }

    }

    public void exec() {
        String fwtableName = "formtable_main_2";
        List<DatsjlEntity> datsjlEntities = datsjlDao.getDatsjlEntitys(fwtableName);
        for (DatsjlEntity datsjlEntity : datsjlEntities) {

            String bt = baseDao.getRequestName(datsjlEntity.getLc());
            HeadDataBill headDataBill = new HeadDataBill();
            headDataBill.setArch_type("1");
            headDataBill.setCheck_name("OA");
            headDataBill.setSys_name("OA");

            List<DataBill> data = new ArrayList<>();
            DataBill dataBill = new DataBill();

            OADispatchEntity oaDispatchEntity = dispatchDao.getOADispatchEntitys(fwtableName, datsjlEntity.getLc());
            FWDataBill fwDataBill = getFWDataBill(oaDispatchEntity);
            List<BussDataDto> buss_datas = getBussDataDtos(datsjlEntity.getLc());
            List<OriginFileDto> origin_files = getOriginFileDtos(bt, oaDispatchEntity, datsjlEntity.getLcym());
            fwDataBill.setFjys(fjys + "");
            dataBill.setArch_data(fwDataBill);
            dataBill.setBuss_datas(buss_datas);
            dataBill.setOrigin_files(origin_files);
            dataBill.setOrigin_id(oaDispatchEntity.getRequestid());


            data.add(dataBill);
            headDataBill.setData(data);
            System.out.println("发文发送格式:" + JSON.toJSONString(headDataBill));
            DatePushService datePushService = new DatePushService();
            baseDao.updateData(datsjlEntity.getLc(),"0","");
            List<FhBill>  errorRequestid = datePushService.postData(headDataBill);
            for (FhBill requestid : errorRequestid){
                baseDao.updateData(requestid.getDataId(),"1",requestid.getMessage());
            }

        }


    }

    private List<OriginFileDto> getOriginFileDtos(String bt, OADispatchEntity oaDispatchEntity, String lcym) {
        List<OriginFileDto> originFileDtos = new ArrayList<>();

        String zw = oaDispatchEntity.getZw();
        List<OADocEntity> oaDocEntitiesAll = new ArrayList<>();

        List<OADocEntity> oaDocEntities = baseDao.getZWEntity(zw);
        OADocEntity zwdocEntity = baseDao.getZWEntityPdf(oaDispatchEntity.getZwpdfgs());
        List<OADocEntity> docEntityfjs = baseDao.getZWEntityPdffj(oaDispatchEntity.getXgfj());

        List<OADocEntity> lcymdocEntitys = baseDao.getZWEntityPdffj(lcym);
        if (lcymdocEntitys.size() > 0) {
            OADocEntity lcymdocEntity = lcymdocEntitys.get(0);
            String bldname = "办理单.pdf";
            if (oaDocEntities.size() <= 1) {
                bldname = "办理单(无底稿).pdf";
            }
            lcymdocEntity.setImageFileName(bldname);
            OriginFileDto bldOriginFileDto = new OriginFileDto();
            bldOriginFileDto.setOrder_type("4");
            bldOriginFileDto.setFile_name(lcymdocEntity.getImageFileName());
            bldOriginFileDto.setFile_type(lcymdocEntity.getImageFileName().substring(lcymdocEntity.getImageFileName().lastIndexOf(".")));
            bldOriginFileDto.setFile_url(url + "/weaver/weaver.file.FileDownloadForBHL?imagefileid=" + lcymdocEntity.getImageFileId());
            originFileDtos.add(bldOriginFileDto);
        }
        //办理单处理完成 开始处理正文
        List<OADocEntity> oaDocEntities2 = new ArrayList<>();
        if (zwdocEntity != null) {
            oaDocEntities2.add(zwdocEntity);
            if (oaDocEntities.size() > 1) {
                oaDocEntities2.add(oaDocEntities.get(oaDocEntities.size() - 1));
            }

        } else {
            oaDocEntities2.addAll(oaDocEntities);
        }
        int index = 1;
        int filetype = 1;
        for (OADocEntity oaDocEntity : oaDocEntities2) {
            String imageFileName = oaDocEntity.getImageFileName();
            String name = bt;
            String nameIndex = "";

            String[] strArray = imageFileName.split("\\.");
            int suffixIndex = strArray.length - 1;
            nameIndex = strArray[suffixIndex];
            String fileName;
            if (index == 1) {
                fileName = "(正本)" + name + "." + nameIndex;

            } else {
                fileName = "(底稿)" + name + "." + nameIndex;
            }

            OriginFileDto zwOriginFileDto = new OriginFileDto();
            zwOriginFileDto.setOrder_type(filetype + "");
            filetype += 1;
            zwOriginFileDto.setFile_name(fileName);
            zwOriginFileDto.setFile_type(fileName.substring(fileName.lastIndexOf(".")));
            zwOriginFileDto.setFile_url(url + "/weaver/weaver.file.FileDownloadForBHL?imagefileid=" + oaDocEntity.getImageFileId());
            index++;
            originFileDtos.add(zwOriginFileDto);
        }

        if (docEntityfjs.size() > 0) {//有附件
            for (OADocEntity fjdocEntityfj : docEntityfjs) {
                String imageFileName = fjdocEntityfj.getImageFileName();

                String nameIndex = "";

                String[] strArray = imageFileName.split("\\.");
                int suffixIndex = strArray.length - 1;
                nameIndex = strArray[suffixIndex];
                String name = imageFileName.substring(0, imageFileName.lastIndexOf("."));
                String filePath = fjdocEntityfj.getFileRealPath();
                String upPath = new File(filePath).getParent() + File.separator + oaDispatchEntity.getRequestid();
                String fileName;
                fileName = "(附件)" + name + "." + nameIndex;
                int imageFileId = Integer.parseInt(fjdocEntityfj.getImageFileId());
                OriginFileDto zwOriginFileDto = new OriginFileDto();
                zwOriginFileDto.setOrder_type("3");
                zwOriginFileDto.setFile_name(fileName);
                zwOriginFileDto.setFile_type(fileName.substring(fileName.lastIndexOf(".")));
                zwOriginFileDto.setFile_url(url + "/weaver/weaver.file.FileDownloadForBHL?imagefileid=" + imageFileId);
                index++;
                originFileDtos.add(zwOriginFileDto);
            }
        }

        int docxPages = 0;
        int pdfPages = 0;
        DocxPdfGetPagesUtil docxPdfGetPagesUtil = new DocxPdfGetPagesUtil();
        for (OADocEntity docEntity : oaDocEntities2) {
            String imageFileId = docEntity.getImageFileId();
            InputStream is3 = ImageFileManager.getInputStreamById(Integer.parseInt(imageFileId));
            String imageFileName = docEntity.getImageFileName();
            int lastIndex = imageFileName.lastIndexOf(".");
            String suffix = imageFileName.substring(lastIndex + 1);
            if ("docx".equals(suffix)) {
                docxPages += docxPdfGetPagesUtil.getDocxPages(is3);
            } else if ("doc".equals(suffix)) {
                docxPages += docxPdfGetPagesUtil.getDocPages(is3);
            } else if ("pdf".equals(suffix)) {
                pdfPages += docxPdfGetPagesUtil.getPdfPages(is3);
            } else {
                continue;
            }

        }
        this.fjys = docxPages + pdfPages + 1;
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
    private FWDataBill getFWDataBill(OADispatchEntity oaDispatchEntity) {
        FWDataBill fwDataBill = new FWDataBill();
//        fwDataBill.setZwpdfgs(oaDispatchEntity.getZwpdfgs());
        fwDataBill.setSou_id(oaDispatchEntity.getRequestid());
        fwDataBill.setWjbh(oaDispatchEntity.getWh());
        String requestName = baseDao.getRequestName(oaDispatchEntity.getRequestid());
        fwDataBill.setTm(requestName);
        String departmentid = oaDispatchEntity.getZbdw();
        String istitutionName = baseDao.getInstitutionById(departmentid);
        fwDataBill.setJg(istitutionName);
        //成文日期
        String date = baseDao.getNodeOperateDate(oaDispatchEntity.getRequestid(), "266");
        if ("".equals(date) || "null".equals(date) || date == null) {
            date = baseDao.getGDDate(oaDispatchEntity.getRequestid());
        }
        fwDataBill.setCwrq(date.replace("-", ""));
        System.out.println("成文日期date:" + date);
        String selectValue = oaDispatchEntity.getWjmj();
        String selectName = baseDao.getSelectName("6381", selectValue);
        fwDataBill.setMj(selectName);
        //年度

        String operateDate = baseDao.getNodeOperateDate(oaDispatchEntity.getRequestid(), "266");
        if ("".equals(operateDate) || operateDate == null)
            operateDate = baseDao.getGDDate(oaDispatchEntity.getRequestid());
        fwDataBill.setNd(operateDate.substring(0, 4));
        fwDataBill.setJjcd(baseDao.getSelectName("6382", oaDispatchEntity.getJjcd()));
        fwDataBill.setGksx(baseDao.getSelectName("6383", oaDispatchEntity.getGwsx()));
        //责任者
        String gwzl = baseDao.getSelectName("6386", oaDispatchEntity.getGwzl());
        String gwzlzxx = baseDao.getSelectName("6461", oaDispatchEntity.getGwzlzxx());
        if (gwzl.contains("党")) {
            fwDataBill.setZrz("中共中国科学技术大学委员会");
        } else {
            fwDataBill.setZrz("中国科学技术大学");
        }
        //所属部门名称
        String createHumanId = oaDispatchEntity.getCjr();
        OAHumanMesEntity humanMesEntity = baseDao.getHumanMes(createHumanId);
        fwDataBill.setSsbmmc(baseDao.getInstitutionById(humanMesEntity.getDepartmentId()));
        //归档日期
        fwDataBill.setGdrq(baseDao.getGDDate(oaDispatchEntity.getRequestid()).replaceAll("-", ""));
        fwDataBill.setIndbisnew("1");
        fwDataBill.setNgr((baseDao.getHumanMes(oaDispatchEntity.getNgr())).getName());
        fwDataBill.setZs(oaDispatchEntity.getZsdw());
        if (oaDispatchEntity.getCsdw() != null && !"".equals(oaDispatchEntity.getCsdw())) {
            fwDataBill.setCs(oaDispatchEntity.getCsdw());
        } else {
            fwDataBill.setCs("");
        }
        //公文种类
        fwDataBill.setGwzl(gwzl + " " + gwzlzxx);
        //传阅范围
        String showName = "";
        ResourceConditionManager resourceConditionManager = new ResourceConditionManager();
        showName += resourceConditionManager.getFormShowName(oaDispatchEntity.getCyfw(), 7);
        if (showName != null && !"".equals(showName)) {
            fwDataBill.setCyfw(showName.replaceAll("\\<.*?>", ""));
        } else {
            fwDataBill.setCyfw("");
        }
        fwDataBill.setIndbisnew("1");


        return fwDataBill;
    }


}
