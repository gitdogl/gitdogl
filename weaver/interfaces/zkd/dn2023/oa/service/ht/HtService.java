package weaver.interfaces.zkd.dn2023.oa.service.ht;

import weaver.conn.RecordSet;

public class HtService {
    public void exec(String id){
        RecordSet rs = new RecordSet();
        String sql = "select bm from uf_datsjl where id = "+id;
        rs.execute(sql);
        if (rs.next()){
            String tablename = rs.getString("bm");

            if(tablename.equals("formtable_main_36")){
                GnhtService gnhtService = new GnhtService();
                gnhtService.exec(id);
            }else if(tablename.equals("formtable_main_48")){
                GwhtService gwhtService = new GwhtService();
                gwhtService.exec(id);
            }else if(tablename.equals("formtable_main_41")){
                KyhtService kyhtService = new KyhtService();
                kyhtService.exec(id);
            }else if(tablename.equals("formtable_main_19")){
                XdhzService xdhzService = new XdhzService();
                xdhzService.exec(id);
            }



        }

    }
}
