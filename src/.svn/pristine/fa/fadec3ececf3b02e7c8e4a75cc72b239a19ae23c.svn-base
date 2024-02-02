package weaver.interfaces.zkd.dn2023.oa.dao;

import weaver.conn.RecordSet;
import weaver.interfaces.zkd.dn2023.oa.entity.DatsjlEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: ZKD_DA_2023
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.oa.dao
 * @Author: hf_zhangyuan
 * @CreateTime: 2023-03-12  14:26
 * @Description: TODO
 * @Version: 1.0
 */

public class DatsjlDao {

    public  List<DatsjlEntity> getDatsjlEntity(String billids){
        List<DatsjlEntity> datsjlEntities = new ArrayList<>();
        RecordSet rs = new RecordSet();
        String sql = "select lc ,sjzt ,lcym, bm from  uf_datsjl where id in ("+billids+")";
        rs.execute(sql);
        while (rs.next()){
            DatsjlEntity datsjlEntity = new  DatsjlEntity();
            datsjlEntity.setLc(rs.getString("lc"));
            datsjlEntity.setSjzt(rs.getString("sjzt"));
            datsjlEntity.setLcym(rs.getString("lcym"));
            datsjlEntity.setBm(rs.getString("bm"));
            datsjlEntities.add(datsjlEntity);
        }

        return datsjlEntities;
    }
    public List<DatsjlEntity> getDatsjlEntitys(String tableName){
        List<DatsjlEntity> datsjlEntities = new ArrayList<>();
        RecordSet rs = new RecordSet();
        String sql = "select lc ,sjzt ,lcym, bm from  uf_datsjl where sjzt  = 4 and bm = '"+tableName+"' ";
        rs.execute(sql);
        while (rs.next()){
            DatsjlEntity datsjlEntity = new  DatsjlEntity();
            datsjlEntity.setLc(rs.getString("lc"));
            datsjlEntity.setSjzt(rs.getString("sjzt"));
            datsjlEntity.setLcym(rs.getString("lcym"));
            datsjlEntity.setBm(rs.getString("bm"));
            datsjlEntities.add(datsjlEntity);
        }

        return datsjlEntities;


    }


}
