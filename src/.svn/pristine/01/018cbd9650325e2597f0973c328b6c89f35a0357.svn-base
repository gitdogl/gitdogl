//package com.weaver.esb.package_20231110042816;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import weaver.general.Util;
//
//import java.util.*;
//
//public class class_20231110042816 {
//
//    /**
//     * @param:  param(Map collections)
//     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
//     */
//    public Map execute(Map<String,Object> params) {
//        String data = Util.null2String(params.get("data"));
//        JSONObject dataobj=JSONObject.parseObject(data);
//        HashMap<String, String> resmap = new HashMap<>();
//        Map<String,Object> ret = new HashMap<>();
//        JSONArray DWST=dataobj.getJSONArray("DWST");
//        JSONArray JBST=dataobj.getJSONArray("JBST");
//        JSONArray FLST=dataobj.getJSONArray("FLST");
//        JSONArray XSST=dataobj.getJSONArray("XSST");
//        JSONArray CGST=dataobj.getJSONArray("CGST");
//        JSONArray MRPST=dataobj.getJSONArray("MRPST");
//        JSONArray GZJHST=dataobj.getJSONArray("GZJHST");
//        JSONArray CCST=dataobj.getJSONArray("CCST");
//        JSONArray KJST=dataobj.getJSONArray("KJST");
//        JSONArray CBST=dataobj.getJSONArray("CBST");
//        dataobj.remove("DWST");
//        dataobj.remove("JBST");
//        dataobj.remove("FLST");
//        dataobj.remove("XSST");
//        dataobj.remove("CGST");
//        dataobj.remove("MRPST");
//        dataobj.remove("GZJHST");
//        dataobj.remove("CCST");
//        dataobj.remove("KJST");
//        dataobj.remove("CBST");
//
//        //UMREN,MEINH,UMREZ
//        if(DWST!=null){
//            resmap.put("UMREN",DWST.getJSONObject(0).getString("UMREN"));
//            resmap.put("MEINH", DWST.getJSONObject(0).getString("MEINH"));
//            resmap.put("UMREZ", DWST.getJSONObject(0).getString("UMREZ"));
//        }
//
//        //MEINS, MATKL, BISMT, SPART, GROES, NTGEW, BRGEW, GEWEI, VOLUM, VOLEH, ZZTODS, ZZDLDJ, ZZFHDJ, ZZTYPE, ZZXZHI, ZZ_FUNC_UNO, ZZTHICK, ZZWIDTH, ZZMIDU, ZZLEHTH, ZZ_MAX_LEN, ZZ_UNIT_TYPE, ZZ_HBGZ_IDX, ZZDNIDX, ZZ_CTDN_IDX, ZZ_TCK_UOM, ZZ_LEN_UOM, ZZ_WTH_UOM, ZZ_MID_UOM, ZZ_MAXLEN_UOM, ZZXXU, MSTAE, KZWSM, ZCIMNO, ZJDUAN
//        if(JBST!=null){
//            resmap.put("MEINS", JBST.getJSONObject(0).getString("MEINS"));
//            resmap.put("MATKL", JBST.getJSONObject(0).getString("MATKL"));
//            resmap.put("BISMT", JBST.getJSONObject(0).getString("BISMT"));
//            resmap.put("SPART", JBST.getJSONObject(0).getString("SPART"));
//            resmap.put("GROES", JBST.getJSONObject(0).getString("GROES"));
//            resmap.put("NTGEW", JBST.getJSONObject(0).getString("NTGEW"));
//            resmap.put("BRGEW", JBST.getJSONObject(0).getString("BRGEW"));
//            resmap.put("GEWEI", JBST.getJSONObject(0).getString("GEWEI"));
//            resmap.put("VOLUM", JBST.getJSONObject(0).getString("VOLUM"));
//            resmap.put("VOLEH", JBST.getJSONObject(0).getString("VOLEH"));
//            resmap.put("ZZTODS", JBST.getJSONObject(0).getString("ZZTODS"));
//            resmap.put("ZZDLDJ", JBST.getJSONObject(0).getString("ZZDLDJ"));
//            resmap.put("ZZFHDJ", JBST.getJSONObject(0).getString("ZZFHDJ"));
//            resmap.put("ZZTYPE", JBST.getJSONObject(0).getString("ZZTYPE"));
//            resmap.put("ZZXZHI", JBST.getJSONObject(0).getString("ZZXZHI"));
//            resmap.put("ZZ_FUNC_UNO", JBST.getJSONObject(0).getString("ZZ_FUNC_UNO"));
//            resmap.put("ZZTHICK", JBST.getJSONObject(0).getString("ZZTHICK"));
//            resmap.put("ZZWIDTH", JBST.getJSONObject(0).getString("ZZWIDTH"));
//            resmap.put("ZZMIDU", JBST.getJSONObject(0).getString("ZZMIDU"));
//            resmap.put("ZZLEHTH", JBST.getJSONObject(0).getString("ZZLEHTH"));
//            resmap.put("ZZ_MAX_LEN", JBST.getJSONObject(0).getString("ZZ_MAX_LEN"));
//            resmap.put("ZZ_UNIT_TYPE", JBST.getJSONObject(0).getString("ZZ_UNIT_TYPE"));
//            resmap.put("ZZ_HBGZ_IDX", JBST.getJSONObject(0).getString("ZZ_HBGZ_IDX"));
//            resmap.put("ZZDNIDX", JBST.getJSONObject(0).getString("ZZDNIDX"));
//            resmap.put("ZZ_CTDN_IDX", JBST.getJSONObject(0).getString("ZZ_CTDN_IDX"));
//            resmap.put("ZZ_TCK_UOM", JBST.getJSONObject(0).getString("ZZ_TCK_UOM"));
//            resmap.put("ZZ_LEN_UOM", JBST.getJSONObject(0).getString("ZZ_LEN_UOM"));
//            resmap.put("ZZ_WTH_UOM", JBST.getJSONObject(0).getString("ZZ_WTH_UOM"));
//            resmap.put("ZZ_MID_UOM", JBST.getJSONObject(0).getString("ZZ_MID_UOM"));
//            resmap.put("ZZ_MAXLEN_UOM", JBST.getJSONObject(0).getString("ZZ_MAXLEN_UOM"));
//            resmap.put("ZZXXU", JBST.getJSONObject(0).getString("ZZXXU"));
//            resmap.put("MSTAE", JBST.getJSONObject(0).getString("MSTAE"));
//            resmap.put("KZWSM", JBST.getJSONObject(0).getString("KZWSM"));
//            resmap.put("ZCIMNO", JBST.getJSONObject(0).getString("ZCIMNO"));
//            resmap.put("ZJDUAN", JBST.getJSONObject(0).getString("ZJDUAN"));
//        }
//
//        //KLART，CLASS
//        if(FLST!=null){
//            resmap.put("KLART", FLST.getJSONObject(0).getString("KLART"));
//            resmap.put("CLASS", FLST.getJSONObject(0).getString("CLASS"));
//        }
//
//        //VRKME, DWERK, TAXM1, KTGRM, MTPOS_MARA, MVGR1, MVGR2, MVGR3, MTPOS, LADGR, TRAGR, MTVFP1
//        if(XSST!=null){
//            resmap.put("VRKME", XSST.getJSONObject(0).getString("VRKME"));
//            resmap.put("DWERK", XSST.getJSONObject(0).getString("DWERK"));
//            resmap.put("TAXM1", XSST.getJSONObject(0).getString("TAXM1"));
//            resmap.put("KTGRM", XSST.getJSONObject(0).getString("KTGRM"));
//            resmap.put("MTPOS_MARA", XSST.getJSONObject(0).getString("MTPOS_MARA"));
//            resmap.put("MVGR1", XSST.getJSONObject(0).getString("MVGR1"));
//            resmap.put("MVGR2", XSST.getJSONObject(0).getString("MVGR2"));
//            resmap.put("MVGR3", XSST.getJSONObject(0).getString("MVGR3"));
//            resmap.put("MTPOS", XSST.getJSONObject(0).getString("MTPOS"));
//            resmap.put("LADGR", XSST.getJSONObject(0).getString("LADGR"));
//            resmap.put("TRAGR", XSST.getJSONObject(0).getString("TRAGR"));
//            resmap.put("MTVFP1", XSST.getJSONObject(0).getString("MTVFP1"));
//        }
//
//        //EKGRP, BSTME, UMREN1, UMREZ1, INSMK, KORDB
//        if(CGST!=null){
//            resmap.put("EKGRP", CGST.getJSONObject(0).getString("EKGRP"));
//            resmap.put("BSTME", CGST.getJSONObject(0).getString("BSTME"));
//            resmap.put("UMREN1", CGST.getJSONObject(0).getString("UMREN1"));
//            resmap.put("UMREZ1", CGST.getJSONObject(0).getString("UMREZ1"));
//            resmap.put("INSMK", CGST.getJSONObject(0).getString("INSMK"));
//            resmap.put("KORDB", CGST.getJSONObject(0).getString("KORDB"));
//        }
//
//        //MAABC, DISMM, MINBE, DISPO, DISLS, BSTMI, BSTMA, BSTFE, MABST, AUSSS, BSTRF, BESKZ, SOBSL, LGPRO, RGEKZ, LGFSB, DZEIT, PLIFZ, WEBAZ, EISBE, STRGR, MISKZ, VRMOD, VINT1, VINT2, MTVFP2, KAUSF, SBDKZ, SAUFT, SFEPR
//        if(MRPST!=null){
//            resmap.put("MAABC", MRPST.getJSONObject(0).getString("MAABC"));
//            resmap.put("DISMM", MRPST.getJSONObject(0).getString("DISMM"));
//            resmap.put("MINBE", MRPST.getJSONObject(0).getString("MINBE"));
//            resmap.put("DISPO", MRPST.getJSONObject(0).getString("DISPO"));
//            resmap.put("DISLS", MRPST.getJSONObject(0).getString("DISLS"));
//            resmap.put("BSTMI", MRPST.getJSONObject(0).getString("BSTMI"));
//            resmap.put("BSTMA", MRPST.getJSONObject(0).getString("BSTMA"));
//            resmap.put("BSTFE", MRPST.getJSONObject(0).getString("BSTFE"));
//            resmap.put("MABST", MRPST.getJSONObject(0).getString("MABST"));
//            resmap.put("AUSSS", MRPST.getJSONObject(0).getString("AUSSS"));
//            resmap.put("BSTRF", MRPST.getJSONObject(0).getString("BSTRF"));
//            resmap.put("BESKZ", MRPST.getJSONObject(0).getString("BESKZ"));
//            resmap.put("SOBSL", MRPST.getJSONObject(0).getString("SOBSL"));
//            resmap.put("LGPRO", MRPST.getJSONObject(0).getString("LGPRO"));
//            resmap.put("RGEKZ", MRPST.getJSONObject(0).getString("RGEKZ"));
//            resmap.put("LGFSB", MRPST.getJSONObject(0).getString("LGFSB"));
//            resmap.put("DZEIT", MRPST.getJSONObject(0).getString("DZEIT"));
//            resmap.put("PLIFZ", MRPST.getJSONObject(0).getString("PLIFZ"));
//            resmap.put("WEBAZ", MRPST.getJSONObject(0).getString("WEBAZ"));
//            resmap.put("EISBE", MRPST.getJSONObject(0).getString("EISBE"));
//            resmap.put("STRGR", MRPST.getJSONObject(0).getString("STRGR"));
//            resmap.put("MISKZ", MRPST.getJSONObject(0).getString("MISKZ"));
//            resmap.put("VRMOD", MRPST.getJSONObject(0).getString("VRMOD"));
//            resmap.put("VINT1", MRPST.getJSONObject(0).getString("VINT1"));
//            resmap.put("VINT2", MRPST.getJSONObject(0).getString("VINT2"));
//            resmap.put("MTVFP2", MRPST.getJSONObject(0).getString("MTVFP2"));
//            resmap.put("KAUSF", MRPST.getJSONObject(0).getString("KAUSF"));
//            resmap.put("SBDKZ", MRPST.getJSONObject(0).getString("SBDKZ"));
//            resmap.put("SAUFT", MRPST.getJSONObject(0).getString("SAUFT"));
//            resmap.put("SFEPR", MRPST.getJSONObject(0).getString("SFEPR"));
//        }
//
//        //FEVOR, UEETK, UEETO, UNETO, SFCPF
//        if(GZJHST!=null){
//            resmap.put("FEVOR", GZJHST.getJSONObject(0).getString("FEVOR"));
//            resmap.put("UEETK", GZJHST.getJSONObject(0).getString("UEETK"));
//            resmap.put("UEETO", GZJHST.getJSONObject(0).getString("UEETO"));
//            resmap.put("UNETO", GZJHST.getJSONObject(0).getString("UNETO"));
//            resmap.put("SFCPF", GZJHST.getJSONObject(0).getString("SFCPF"));
//        }
//
//        //CCST
//        if(CCST!=null){
//            resmap.put("XCHPF", CCST.getJSONObject(0).getString("XCHPF"));
//            resmap.put("SERNP", CCST.getJSONObject(0).getString("SERNP"));
//            resmap.put("AUSME", CCST.getJSONObject(0).getString("AUSME"));
//            resmap.put("ZQGPS", CCST.getJSONObject(0).getString("ZQGPS"));
//        }
//
//        //KJST
//        if(KJST!=null){
//            resmap.put("PEINH", KJST.getJSONObject(0).getString("PEINH"));
//            resmap.put("MLAST", KJST.getJSONObject(0).getString("MLAST"));
//            resmap.put("VPRSV", KJST.getJSONObject(0).getString("VPRSV"));
//            resmap.put("BWTTY", KJST.getJSONObject(0).getString("BWTTY"));
//            resmap.put("BKLAS", KJST.getJSONObject(0).getString("BKLAS"));
//            resmap.put("EKLAS", KJST.getJSONObject(0).getString("EKLAS"));
//        }
//
//        //CBST
//        if(CBST!=null){
//            resmap.put("NCOST", CBST.getJSONObject(0).getString("NCOST"));
//            resmap.put("HKMAT", CBST.getJSONObject(0).getString("HKMAT"));
//            resmap.put("LOSGR", CBST.getJSONObject(0).getString("LOSGR"));
//            resmap.put("EKALR", CBST.getJSONObject(0).getString("EKALR"));
//            resmap.put("MMSTA", CBST.getJSONObject(0).getString("MMSTA"));
//            resmap.put("HRKFT", CBST.getJSONObject(0).getString("HRKFT"));
//            resmap.put("AWSLS", CBST.getJSONObject(0).getString("AWSLS"));
//            resmap.put("PRCTR", CBST.getJSONObject(0).getString("PRCTR"));
//            resmap.put("ZPLP1", CBST.getJSONObject(0).getString("ZPLP1"));
//            resmap.put("ZPLD1", CBST.getJSONObject(0).getString("ZPLD1"));
//            resmap.put("ZPLP2", CBST.getJSONObject(0).getString("ZPLP2"));
//            resmap.put("ZPLD2", CBST.getJSONObject(0).getString("ZPLD2"));
//            resmap.put("ZPLP3", CBST.getJSONObject(0).getString("ZPLP3"));
//            resmap.put("ZPLD3", CBST.getJSONObject(0).getString("ZPLD3"));
//        }
//
//        for (Map.Entry<String, String> entry : resmap.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            dataobj.put(key, value);
//        }
//
//        ret.put("res",dataobj);
//        ret.put("code","1");
//        return ret;
//    }
//}
