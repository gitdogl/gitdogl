package weaver.interfaces.zkd.dn2023.oa.service;

import weaver.conn.RecordSet;

public class GWService {
    public void exec(String id){
        RecordSet rs = new RecordSet();
        String sql = "select bm from uf_datsjl where id = "+id;
        rs.execute(sql);
        if (rs.next()){
            String tablename = rs.getString("bm");

            if(tablename.equals("formtable_main_2")){
                DispatchService dispatchService = new DispatchService();
                dispatchService.exec(id);
            }else if(tablename.equals("formtable_main_3")){
                ReceiveService receiveService = new ReceiveService();
                receiveService.exec(id);
            }
        }

    }
}
