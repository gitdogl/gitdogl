//package com.engine.interfaces.zxl.utils.file;
//
//import cn.hutool.core.io.FileUtil;
//import com.api.doc.detail.service.DocAccService;
//import com.api.doc.detail.service.DocSaveService;
//import com.weaver.ImageFileManager;
//import lombok.extern.slf4j.Slf4j;
//import weaver.conn.RecordSet;
//import weaver.docs.docs.DocIdUpdate;
//import weaver.upgradetool.wscheck.Util;
//
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.util.HashMap;
//import java.util.Map;
//
//import static cn.hutool.core.io.IoUtil.readBytes;
//
//@Slf4j(topic = "custom")
//public class SaveFileUtil {
//
//    public int miximagefile(String docid) {
//        String[] docids = docid.split(",");
//        DocSaveService docSaveService = new DocSaveService();
//        DocIdUpdate docIdUpdate = new DocIdUpdate();
//        Integer docId = docIdUpdate.getDocNewId();
//        for (String reg : docids) {
//            try {
//                Map<String, String> filemap = getFileMap(reg);
//                int fileid = Integer.parseInt(filemap.get("imagefileid"));
//                DocAccService accService = new DocAccService();
//                accService.buildRelForDoc(fileid, docId);
//            } catch (Exception exception) {
//                throw new RuntimeException(exception);
//            }
//        }
//        return docId;
//    }
//
//
//    public static Map<String, String> getFileMap(String docid) throws Exception {
//        RecordSet rs = new RecordSet();
//        Map<String, String> map = new HashMap();
//        String sql = "select imagefilename,filerealpath,imagefileid from ImageFile where imagefileid in (select imagefileid from DocImageFile where docid='" + docid + "')";
//        rs.execute(sql);
//        while (rs.next()) {
//            map.put("imagefilename", Util.null2String(rs.getString("imagefilename")));
//            map.put("filerealpath", Util.null2String(rs.getString("filerealpath")));
//            map.put("imagefileid", Util.null2String(rs.getString("imagefileid")));
//        }
//        return map;
//    }
//
//    public static int enclosureImageUpload(String filePath, String fileName) {
//        //  初始化用户信息
////        User user = new User(userId);
//        File file = new File(filePath);
//        BufferedInputStream inputStream = FileUtil.getInputStream(file);
//        byte[] bytes = readBytes(inputStream);
//        return enclosureImageUpload(bytes, fileName);
//    }
//
//    private static Integer enclosureImageUpload(byte[] bytes, String fileName) {
//        ImageFileManager imageFileManager = new ImageFileManager();
//        DocSaveService docSaveService = new DocSaveService();
//        Integer docId = -1;
//        //  存储文件名称
//        imageFileManager.setImagFileName(fileName);
//        //  保存文件数据
//        imageFileManager.setData(bytes);
//        //  保存文件返回文件id
//        int imageFileId = imageFileManager.saveImageFile();
////        try {
////            //  文件存储到docimage
////            docId = docSaveService.accForDoc(dirId, imageFileId, user);
////        } catch (Exception e) {
////            throw new RuntimeException(e);
////        }
//        return imageFileId;
//    }
//}