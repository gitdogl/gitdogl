package weaver.interfaces.zkd.dn2023.oa.dao.ht;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.zkd.dn2023.bhl.entity.DataCountBill;
import weaver.interfaces.zkd.dn2023.oa.entity.ht.GnhtEntity;
import weaver.interfaces.zkd.dn2023.oa.entity.ht.GwhtEntity;
import weaver.interfaces.zkd.dn2023.oa.entity.ht.KyhtEntity;
import weaver.interfaces.zkd.dn2023.oa.entity.ht.XdhzEntity;
import weaver.interfaces.zkd.dn2023.oa.util.BaseDao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: ZKD_DA_2023
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.rs.dao.ht
 * @Author: hf_zhangyuan
 * @CreateTime: 2023-03-28  18:38
 * @Description: TODO
 * @Version: 1.0
 */

public class HtDao {


    /**
     * 根据requestId和表名获取科研项目合同表单数据
     *
     * @param requestId
     * @param tableName
     * @return
     */
    public KyhtEntity getKyhtEntity( String tableName,String requestId){
        KyhtEntity kyhtEntity = new KyhtEntity();
        String sql = "select lcbh,rq,jbr,sqdw,gkbm,xmfzr,xmlx,htje,yf,jf,dwxgbb,fwcw,gkxg,htwj,htfj,fjsc,ybwj,tbwj,zbwj,xghdhtwjsc,sxhtsmjsc,gkfzrcwxdbbsc,cwxdbbsc,fwxgbb,jwjcsjxgbb,bmbgsxgbb,zlglbgsxgbb,ghwsxgbbsc,bwyxyglsc ,dzbgsscxgbb,sptgdhtzzbb,tbcns from " + tableName + " where requestid=" + requestId;
        RecordSet oa = new RecordSet();
        oa.execute(sql);
        if (oa.next()) {
            kyhtEntity.setLcbh(oa.getString("lcbh"));
            kyhtEntity.setRq(oa.getString("rq"));
            kyhtEntity.setJbr(oa.getString("jbr"));
            kyhtEntity.setSqdw(oa.getString("sqdw"));
            kyhtEntity.setGkbm(oa.getString("gkbm"));
            kyhtEntity.setXmfzr(oa.getString("xmfzr"));
            kyhtEntity.setXmlx(oa.getString("xmlx"));
            kyhtEntity.setHtje(oa.getString("htje"));
            kyhtEntity.setYf(oa.getString("yf"));
            kyhtEntity.setJf(oa.getString("jf"));
            kyhtEntity.setDwxgbb(oa.getString("dwxgbb"));
            kyhtEntity.setFwcw(oa.getString("fwcw"));
            kyhtEntity.setGkxg(oa.getString("gkxg"));
            kyhtEntity.setHtwj(oa.getString("htwj"));
            kyhtEntity.setHtfj(oa.getString("htfj"));
            kyhtEntity.setFjsc(oa.getString("fjsc"));
            kyhtEntity.setYbwj(oa.getString("ybwj"));
            kyhtEntity.setTbwj(oa.getString("tbwj"));
            kyhtEntity.setZbwj(oa.getString("zbwj"));
            kyhtEntity.setXghdhtwjsc(oa.getString("xghdhtwjsc"));
            kyhtEntity.setSxhtsmjsc(oa.getString("sxhtsmjsc"));
            kyhtEntity.setGkfzrcwxdbbsc(oa.getString("gkfzrcwxdbbsc"));
            kyhtEntity.setCwxdbbsc(oa.getString("cwxdbbsc"));
            kyhtEntity.setFwxgbb(oa.getString("fwxgbb"));
            kyhtEntity.setJwjcsjxgbb(oa.getString("jwjcsjxgbb"));
            kyhtEntity.setBmbgsxgbb(oa.getString("bmbgsxgbb"));
            kyhtEntity.setZlglbgsxgbb(oa.getString("zlglbgsxgbb"));
            kyhtEntity.setGhwsxgbbsc(oa.getString("ghwsxgbbsc"));
            kyhtEntity.setBwyxyglsc(oa.getString("bwyxyglsc"));
            kyhtEntity.setDzbgsscxgbb(oa.getString("dzbgsscxgbb"));
            kyhtEntity.setSptgdhtzzbb(oa.getString("sptgdhtzzbb"));
            kyhtEntity.setTbcns(oa.getString("tbcns"));
            kyhtEntity.setRequestid(requestId);

        }
        return kyhtEntity;
    }

    /**
     * 根据requestId和表名获取进口采购合同表单数据
     *
     * @param requestId
     * @param tableName
     * @return
     * @throws Exception
     */
    public GwhtEntity getGwhtEntity( String tableName,String requestId){
        GwhtEntity gwhtEntity = new GwhtEntity();
        String sql = "select bh,cbrq,cbr,gkbm,cbbm,xmfzr,htlx,htlb,bbzexx,cgbhnew,wmhtbh,htlbn,bzn,yf,jf,htwj,htfj,sxhtsmj,qtwj,dlht,wmht,xjbabqzgzb,sjjysbjd,jzxtpbab,cbjjxgcl,jzxcsbab,zbtzs,jsxy,htxysfbxx,qtwjrwdljgzzhxtjcjtzslybzjhzd,cbjdfbjxgcl,htwjzzbb,wmhtzzbb,cgfs,yfhtbh from " + tableName + " where requestId=" + requestId;
        RecordSet oa = new RecordSet();

        System.out.println(sql);
        oa.execute(sql);
        if (oa.next()) {
            gwhtEntity.setBh(Util.null2String(oa.getString("bh")));
            gwhtEntity.setCbrq(Util.null2String(oa.getString("cbrq")));
            gwhtEntity.setCbr(Util.null2String(oa.getString("cbr")));
            gwhtEntity.setCbbm(Util.null2String(oa.getString("cbbm")));
            gwhtEntity.setGkbm(Util.null2String(oa.getString("gkbm")));
            gwhtEntity.setXmfzr(Util.null2String(oa.getString("xmfzr")));
            gwhtEntity.setHtlx(Util.null2String(oa.getString("htlx")));
            gwhtEntity.setHtlb(Util.null2String(oa.getString("htlb")));
            gwhtEntity.setBbzexx(Util.null2String(oa.getString("bbzexx")));
            gwhtEntity.setCgbhnew(Util.null2String(oa.getString("cgbhnew")));
            gwhtEntity.setWmhtbh(Util.null2String(oa.getString("wmhtbh")));
            gwhtEntity.setHtlbn(Util.null2String(oa.getString("htlbn")));
            gwhtEntity.setBzn(Util.null2String(oa.getString("bzn")));
            gwhtEntity.setYf(Util.null2String(oa.getString("yf")));
            gwhtEntity.setJf(Util.null2String(oa.getString("jf")));
            gwhtEntity.setHtwj(Util.null2String(oa.getString("htwj")));
            gwhtEntity.setHtfj(Util.null2String(oa.getString("htfj")));
            gwhtEntity.setSxhtsmj(Util.null2String(oa.getString("sxhtsmj")));
            gwhtEntity.setQtwj(Util.null2String(oa.getString("qtwj")));
            gwhtEntity.setDlht(Util.null2String(oa.getString("dlht")));
            gwhtEntity.setWmht(Util.null2String(oa.getString("wmht")));
            gwhtEntity.setXjbabqzgzb(Util.null2String(oa.getString("xjbabqzgzb")));
            gwhtEntity.setSjjysbjd(Util.null2String(oa.getString("sjjysbjd")));
            gwhtEntity.setJzxtpbab(Util.null2String(oa.getString("jzxtpbab")));
            gwhtEntity.setCbjjxgcl(Util.null2String(oa.getString("cbjjxgcl")));
            gwhtEntity.setJzxcsbab(Util.null2String(oa.getString("jzxcsbab")));
            gwhtEntity.setZbtzs(Util.null2String(oa.getString("zbtzs")));
            gwhtEntity.setJsxy(Util.null2String(oa.getString("jsxy")));
            gwhtEntity.setHtxysfbxx(Util.null2String(oa.getString("htxysfbxx")));
            gwhtEntity.setQtwjrwdljgzzhxtjcjtzslybzjhzd(Util.null2String(oa.getString("qtwjrwdljgzzhxtjcjtzslybzjhzd")));
            gwhtEntity.setCbjdfbjxgcl(Util.null2String(oa.getString("cbjdfbjxgcl")));
            gwhtEntity.setHtwjzzbb(Util.null2String(oa.getString("htwjzzbb")));
            gwhtEntity.setWmhtzzbb(Util.null2String(oa.getString("wmhtzzbb")));
            gwhtEntity.setCgfs(Util.null2String(oa.getString("cgfs")));
            gwhtEntity.setYfhtbh(Util.null2String(oa.getString("yfhtbh")));
            gwhtEntity.setRequestid(requestId);
        }
        return gwhtEntity;
    }

    /**
     * 根据requestId和表名获取国内采购合同表单数据
     *
     * @param requestId
     * @param tableName
     * @return
     * @throws Exception
     */
    public GnhtEntity getGnhtEntity( String tableName,String requestId){
        GnhtEntity gnhtEntity = new GnhtEntity();
        String sql = "select bh,cbrq,cbr,cbbm,gkbm,xmfzr,htlx,htlb,bbzexx,cgbhnew,htlbn,bzn,htwj,htfj,sxhtsmj,qtwj,xjbabqzgzb,sjjysbjd,jzxtpbab,cbjjxgcl,jzxcsbab,cbjdfbjxgcl,zbtzs,lybzjhzd,zbwj,tbwj,htfjn,htwjzzbb,yzcns,yf,jf,htbh,bh from " + tableName + " where requestId=" + requestId;
        RecordSet rs = new RecordSet();
        rs.execute(sql);
        if (rs.next()) {
            gnhtEntity.setBh(Util.null2String(rs.getString("bh")));
            gnhtEntity.setCbrq(Util.null2String(rs.getString("cbrq")));
            gnhtEntity.setCbr(Util.null2String(rs.getString("cbr")));
            gnhtEntity.setCbbm(Util.null2String(rs.getString("cbbm")));
            gnhtEntity.setGkbm(Util.null2String(rs.getString("gkbm")));
            gnhtEntity.setXmfzr(Util.null2String(rs.getString("xmfzr")));
            gnhtEntity.setHtlx(Util.null2String(rs.getString("htlx")));
            gnhtEntity.setHtlb(Util.null2String(rs.getString("htlb")));
            gnhtEntity.setBbzexx(Util.null2String(rs.getString("bbzexx")));
            gnhtEntity.setCgbhnew(Util.null2String(rs.getString("cgbhnew")));
            gnhtEntity.setHtlbn(Util.null2String(rs.getString("htlbn")));
            gnhtEntity.setBzn(Util.null2String(rs.getString("bzn")));
            gnhtEntity.setHtwj(Util.null2String(rs.getString("htwj")));
            gnhtEntity.setHtfj(Util.null2String(rs.getString("htfj")));
            gnhtEntity.setSxhtsmj(Util.null2String(rs.getString("sxhtsmj")));
            gnhtEntity.setQtwj(Util.null2String(rs.getString("qtwj")));
            gnhtEntity.setXjbabqzgz(Util.null2String(rs.getString("xjbabqzgzb")));
            gnhtEntity.setSjjysbjd(Util.null2String(rs.getString("sjjysbjd")));
            gnhtEntity.setJzxtpbab(Util.null2String(rs.getString("jzxtpbab")));
            gnhtEntity.setCbjjxgcl(Util.null2String(rs.getString("cbjjxgcl")));
            gnhtEntity.setJzxcsbab(Util.null2String(rs.getString("jzxcsbab")));
            gnhtEntity.setCbjdfbjxgc(Util.null2String(rs.getString("cbjdfbjxgcl")));
            gnhtEntity.setZbtzs(Util.null2String(rs.getString("zbtzs")));
            gnhtEntity.setLybzjhzd(Util.null2String(rs.getString("lybzjhzd")));
            gnhtEntity.setZbwj(Util.null2String(rs.getString("zbwj")));
            gnhtEntity.setTbwj(Util.null2String(rs.getString("tbwj")));
            gnhtEntity.setHtfjn(Util.null2String(rs.getString("htfjn")));
            gnhtEntity.setHtwjzzbb(Util.null2String(rs.getString("htwjzzbb")));
            gnhtEntity.setYzcns(Util.null2String(rs.getString("yzcns")));
            gnhtEntity.setYf(Util.null2String(rs.getString("yf")));
            gnhtEntity.setJf(Util.null2String(rs.getString("jf")));
            gnhtEntity.setHtbh(Util.null2String(rs.getString("bh")));
            gnhtEntity.setRequestid(requestId);
        }
        return gnhtEntity;
    }

    /**
     * 根据requestId和表名获取党政办公室合作协议表单内容
     *
     * @param requestId
     * @param tableName
     * @return
     * @throws Exception
     */
    public XdhzEntity getXdhzEntity( String tableName,String requestId){
        XdhzEntity xdhzEntity = new XdhzEntity();
        String sql = "select bh,sqrq,sqr,bm,gkbm,htje,jf,jfn,zzht,htwj,htfj,htwjzzbb from " + tableName + " where requestid=" + requestId;
        RecordSet rs = new RecordSet();
        rs.execute(sql);
        if (rs.next()) {
            xdhzEntity.setBh(Util.null2String(rs.getString("bh")));
            xdhzEntity.setSqrq(Util.null2String(rs.getString("sqrq")));
            xdhzEntity.setSqr(Util.null2String(rs.getString("sqr")));
            xdhzEntity.setBm(Util.null2String(rs.getString("bm")));
            xdhzEntity.setGkbm(Util.null2String(rs.getString("gkbm")));
            xdhzEntity.setHtje(Util.null2String(rs.getString("htje")));
            xdhzEntity.setYf(Util.null2String(rs.getString("jf")));
            xdhzEntity.setJfn(Util.null2String(rs.getString("jfn")));
            xdhzEntity.setZzht(Util.null2String(rs.getString("zzht")));
            xdhzEntity.setHtwj(Util.null2String(rs.getString("htwj")));
            xdhzEntity.setHtfj(Util.null2String(rs.getString("htfj")));
            xdhzEntity.setHtwjzzbb(Util.null2String(rs.getString("htwjzzbb")));
            xdhzEntity.setRequestid(requestId);
        }

        return xdhzEntity;
    }


    /**
     * 获取当天合同档案推送总数
     *
     * @return
     * @throws Exception
     */
    public String getHtCount() {
        String count="";
        String date=getCurDate();
        String sql="select count(1) zs from uf_datsjl where tsrq=?";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,date);
        if(rs.next()){
            count=rs.getString("zs");
        }
        return count;
    }

    public String getHtCount(String date) {
        String count="";
        String sql="select count(1) zs from uf_datsjl where tsrq=?";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,date);
        if(rs.next()){
            count=rs.getString("zs");
        }
        return count;
    }

    /**
     * 获取当天日期YYYY-MM-DD
     *
     * @return
     * @throws Exception
     */
    public String getCurDate(){
        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return todayStr;
    }



    public List<DataCountBill> getDataCountBill(String date){
        List<DataCountBill> countBills = new ArrayList<>();
        RecordSet rs = new RecordSet();
        BaseDao baseDao = new BaseDao();
        String sql="select id,lc,sjzt,sbyy from uf_datsjl where tsrq=?";
        rs.executeQuery(sql,date);
        while(rs.next()){
            DataCountBill countBill = new DataCountBill();
            countBill.setOrigin_id(Util.null2String(rs.getString("id")));
            countBill.setTitle(Util.null2String(baseDao.getRequestName(rs.getString("lc"))));
            int status;
            String msg="";
            if(!"0".equals(rs.getString("sjzt"))&&!"1".equals(rs.getString("sjzt"))){
                status=1;
                msg="推送异常";
            }else{
                status=(rs.getInt("sjzt"));
                msg=Util.null2String(rs.getString("sbyy"));
            }
            countBill.setStatus(status);
            countBill.setFailed_msg(msg);
            countBills.add(countBill);
        }
        return countBills;
    }
}
