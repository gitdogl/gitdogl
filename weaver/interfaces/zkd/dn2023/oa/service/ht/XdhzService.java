package weaver.interfaces.zkd.dn2023.oa.service.ht;

import com.alibaba.fastjson.JSON;
import weaver.file.ImageFileManager;
import weaver.interfaces.zkd.dn2023.bhl.entity.*;
import weaver.interfaces.zkd.dn2023.bhl.serivce.DatePushService;
import weaver.interfaces.zkd.dn2023.oa.dao.DatsjlDao;
import weaver.interfaces.zkd.dn2023.oa.dao.ht.HtDao;
import weaver.interfaces.zkd.dn2023.oa.entity.*;
import weaver.interfaces.zkd.dn2023.oa.entity.ht.XdhzEntity;
import weaver.interfaces.zkd.dn2023.oa.util.BaseDao;
import weaver.interfaces.zkd.dn2023.oa.util.DocxPdfGetPagesUtil;
import weaver.interfaces.zkd.dn2023.oa.util.StaticCode;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @BelongsProject: ZKD_DA_2023
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.oa.service.ht
 * @Author: hf_zhangyuan
 * @CreateTime: 2023-03-28  18:38
 * @Description: 校地协议
 * @Version: 1.0
 */

public class XdhzService {
    private final String url = "http://hetong.ustc.edu.cn";
    DatsjlDao datsjlDao = new DatsjlDao();
    HtDao htDao = new HtDao();
    BaseDao baseDao = new BaseDao();
    int fjys = 0;

    public void exec(String id) {
        String fwtableName = "formtable_main_19";
        List<DatsjlEntity> datsjlEntities = datsjlDao.getDatsjlEntity(id);
        for (DatsjlEntity datsjlEntity : datsjlEntities) {
            String bt = baseDao.getRequestName(datsjlEntity.getLc());
            HeadDataBill headDataBill = new HeadDataBill();
            headDataBill.setArch_type("3");
            headDataBill.setCheck_name("OA");
            headDataBill.setSys_name("OA");
            List<DataBill> data = new ArrayList<>();
            DataBill dataBill = new DataBill();
            XdhzEntity xdhzEntity = htDao.getXdhzEntity(fwtableName, datsjlEntity.getLc());
            HTDataBill htDataBill = getHTDataBill(xdhzEntity);
            List<BussDataDto> buss_datas = getBussDataDtos(datsjlEntity.getLc());
            List<OriginFileDto> origin_files = getOriginFileDtos(xdhzEntity, datsjlEntity.getLcym());
            htDataBill.setYs(fjys + "");
            dataBill.setArch_data(htDataBill);
            dataBill.setBuss_datas(buss_datas);
            dataBill.setOrigin_files(origin_files);
            dataBill.setOrigin_id(datsjlEntity.getLc());
            data.add(dataBill);
            headDataBill.setData(data);
            System.out.println("发文发送格式:" + JSON.toJSONString(headDataBill));
            DatePushService datePushService = new DatePushService();
            baseDao.updateData(datsjlEntity.getLc(), "0", "");
            List<FhBill> errorRequestid = datePushService.postData(headDataBill);
            for (FhBill requestid : errorRequestid) {
                baseDao.updateData(requestid.getDataId(), "1", requestid.getMessage());
            }
        }
    }

    public void exec() {
        String fwtableName = "formtable_main_19";
        List<DatsjlEntity> datsjlEntities = datsjlDao.getDatsjlEntitys(fwtableName);
        for (DatsjlEntity datsjlEntity : datsjlEntities) {
            try {
                String bt = baseDao.getRequestName(datsjlEntity.getLc());
                HeadDataBill headDataBill = new HeadDataBill();
                headDataBill.setArch_type("3");
                headDataBill.setCheck_name("OA");
                headDataBill.setSys_name("OA");
                List<DataBill> data = new ArrayList<>();
                DataBill dataBill = new DataBill();
                XdhzEntity xdhzEntity = htDao.getXdhzEntity(fwtableName, datsjlEntity.getLc());
                HTDataBill htDataBill = getHTDataBill(xdhzEntity);
                List<BussDataDto> buss_datas = getBussDataDtos(datsjlEntity.getLc());
                List<OriginFileDto> origin_files = getOriginFileDtos(xdhzEntity, datsjlEntity.getLcym());
                htDataBill.setYs(fjys + "");
                dataBill.setArch_data(htDataBill);
                dataBill.setBuss_datas(buss_datas);
                dataBill.setOrigin_files(origin_files);
                dataBill.setOrigin_id(datsjlEntity.getLc());
                data.add(dataBill);
                headDataBill.setData(data);
                System.out.println("发文发送格式:" + JSON.toJSONString(headDataBill));
                DatePushService datePushService = new DatePushService();
                baseDao.updateData(datsjlEntity.getLc(), "0", "");
                List<FhBill> errorRequestid = datePushService.postData(headDataBill);
                for (FhBill requestid : errorRequestid) {
                    baseDao.updateData(requestid.getDataId(), "1", requestid.getMessage());
                }
            } catch (Exception e) {
                baseDao.updateData(datsjlEntity.getLc(), "1", e.getMessage());
            }
        }
    }

    private List<OriginFileDto> getOriginFileDtos(XdhzEntity xdhzEntity, String lcym) {
        List<OriginFileDto> originFileDtos = new ArrayList<>();
        List<OADocEntity> lcymdocEntitys = baseDao.getZWEntityPdffj(lcym);
        if (lcymdocEntitys.size() > 0) {
            OADocEntity lcymdocEntity = lcymdocEntitys.get(0);
            String bldname = "办理单.pdf";
            lcymdocEntity.setImageFileName(bldname);
            OriginFileDto bldOriginFileDto = new OriginFileDto();
            bldOriginFileDto.setOrder_type("4");
            bldOriginFileDto.setFile_name(lcymdocEntity.getImageFileName());
            bldOriginFileDto.setFile_type(lcymdocEntity.getImageFileName().substring(lcymdocEntity.getImageFileName().lastIndexOf(".")));
            bldOriginFileDto.setFile_url(url + "/weaver/weaver.file.FileDownloadForBHL?imagefileid=" + lcymdocEntity.getImageFileId());
            originFileDtos.add(bldOriginFileDto);
        }
        List<String> list = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        list.add(xdhzEntity.getZzht());
        list.add(xdhzEntity.getHtwj());
        list.add(xdhzEntity.getHtfj());
        list.add(xdhzEntity.getHtwjzzbb());

        for (String string : list) {
            if (string != null && !string.startsWith("0") && !string.startsWith("-2") && !"".equals(string)) {
                sb.append("," + string);
                System.out.println(string);
            }
        }
        String attachedFileId = sb.toString().substring(1);
        List<OADocEntity> list0 = baseDao.getImageFileId(attachedFileId);

        DocxPdfGetPagesUtil docxPdfGetPagesUtil = new DocxPdfGetPagesUtil();
        int docxPages = 0;
        int pdfPages = 0;
        for (OADocEntity docEntity : list0) {
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

        if (list0.size() > 0) {//有附件
            for (OADocEntity fjdocEntityfj : list0) {
                String imageFileName = fjdocEntityfj.getImageFileName();
                String nameIndex = "";
                String[] strArray = imageFileName.split("\\.");
                int suffixIndex = strArray.length - 1;
                nameIndex = strArray[suffixIndex];
                String name = imageFileName.substring(0, imageFileName.lastIndexOf("."));
                String fileName = "(附件)" + name + "." + nameIndex;
                int imageFileId = Integer.parseInt(fjdocEntityfj.getImageFileId());
                OriginFileDto zwOriginFileDto = new OriginFileDto();
                zwOriginFileDto.setOrder_type("3");
                zwOriginFileDto.setFile_name(fileName);
                zwOriginFileDto.setFile_type(fileName.substring(fileName.lastIndexOf(".")));
                zwOriginFileDto.setFile_url(url + "/weaver/weaver.file.FileDownloadForBHL?imagefileid=" + imageFileId);
                originFileDtos.add(zwOriginFileDto);
            }
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

    private HTDataBill getHTDataBill(XdhzEntity xdhzEntity) {
        HTDataBill htDataBill = new HTDataBill();

        htDataBill.setSou_id(xdhzEntity.getRequestid());
        htDataBill.setHtbh(xdhzEntity.getBh());
//        htDataBill.setHtbh("");
        htDataBill.setHtlb("党政办合同");
        if (xdhzEntity.getSqrq() != null && !"".equals(xdhzEntity.getSqrq())) {
            htDataBill.setNd(xdhzEntity.getSqrq().substring(0, xdhzEntity.getSqrq().indexOf("-")));
        } else {
            htDataBill.setNd(new SimpleDateFormat("yyyy").format(new Date()));
        }
        htDataBill.setTm(baseDao.getRequestName(xdhzEntity.getRequestid()) + "(合同编号:" + xdhzEntity.getBh() + ")");
        htDataBill.setZrz(xdhzEntity.getJfn() + "," + xdhzEntity.getYf());
        if (xdhzEntity.getSqrq() != null && !"".equals(xdhzEntity.getSqrq())) {
            htDataBill.setRq(getSplited(xdhzEntity.getSqrq(), "-"));
        } else {
            htDataBill.setRq("");
        }
        htDataBill.setSqr(baseDao.getHumanNameById(xdhzEntity.getSqr()));
        htDataBill.setSqbm(baseDao.getInstitutionById(xdhzEntity.getBm()));
        if (xdhzEntity.getSqrq() != null && !"".equals(xdhzEntity.getSqrq())) {
            htDataBill.setSqrq(getSplited(xdhzEntity.getSqrq(), "-"));
        } else {
            htDataBill.setSqrq("");
        }
        if (xdhzEntity.getGkbm() == null || "".equals(xdhzEntity.getGkbm())) {
            htDataBill.setGddw("党政办公室");
        } else {
            htDataBill.setGddw(baseDao.getSelectName("10467", xdhzEntity.getGkbm()));
        }
        htDataBill.setGdrq(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        htDataBill.setHtje(String.valueOf(0) + "(RMB)");
        htDataBill.setWmhtbh("");
        htDataBill.setQfr("");
        htDataBill.setHtlx("校地合作协议");
        htDataBill.setXmlx("");
        htDataBill.setCgbh("");
        htDataBill.setCgfs("");
        return htDataBill;
    }

    public static String getSplited(String originString, String symbol) {
        StringBuffer sb = new StringBuffer();
        String[] strArr = originString.split(symbol);
        for (String string : strArr) {
            sb.append(string);
        }
        return sb.toString();
    }
}
