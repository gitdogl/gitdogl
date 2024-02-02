import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.formmode.virtualform.UUIDPKVFormDataSave;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.general.BaseBean;
import weaver.general.BaseBean;
import weaver.soa.workflow.request.RequestInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


/**
 * @BelongsProject: WEAVER
 * @BelongsPackage: weaver
 * @Author: luotianchen
 * @CreateTime: 2024-01-07  16:44
 * @Description: TODO
 * @Version: 1.0
 */
public class Action20240107044343 extends BaseBean implements Action {
    String error = "";

    /**
     * 流程路径节点后选择aciton后,会在节点提交后执行此方法。
     */
    public String execute(RequestInfo request) {
        System.out.println("111111");
        String requestId = request.getRequestid();
        String tablename = request.getRequestManager().getBillTableName();
        RecordSet rs = new RecordSet();
        String sql="select * from " + tablename + "_dt1 where mainid=(select id from " + tablename + " where requestid=" + requestId + ")";
        rs.executeQuery(sql);
        writeLog("sql的值是："+sql);
        while (rs.next()) {
            String jd = rs.getString("jd");//据点
            String gkdm = rs.getString("gkdm");//顾客代码
            String cqff = rs.getString("cqff");//偿却方法
            String dqfs = rs.getString("dqfs");//打切方式
            String djllspf = rs.getString("djllspf");//单价联络书品番
            String ks = rs.getString("ks");//开始日期
            String js = rs.getString("js");//结束日期
            String mbdjllspf = rs.getString("mbdjllspf");//目标单价联络书品番
            double whsje = rs.getDouble("whsje");//未回收金额
            double dqje = rs.getDouble("dqje");//打切金额
            List<String> qjmxids = new ArrayList<>();//期间明细id
            int zyf = getMonthsDifference(ks, js);
            ArrayList<String> months = getMonthsAndYears(ks, js);
            String lastMonth = "";//模具费明细id
            writeLog("cqff值="+cqff);
            writeLog("dqfs值="+dqfs);
            writeLog("whsje值="+whsje);
            //常规打切
            if ("2".equals(cqff) && "2".equals(dqfs) && whsje > 0) {
                writeLog("-------------------------------------------进入常规打切---------------------------------------------");
                lastMonth = getmjlastMonth(djllspf, jd, gkdm);
                writeLog("lastMonth="+lastMonth);
                updateInvalid(djllspf, lastMonth,"ny>?");
//                qjmxids=getysmxid(djllspf,jd,gkdm);
                //获取到打切金额以后平均分配给months里面的每一个数据，如果有余数放到最后一个数据上面
                BigDecimal totalAmount = new BigDecimal(dqje);
                BigDecimal parts = new BigDecimal(months.size()); // 总分隔大小
                // 计算余数
                BigDecimal remainder = totalAmount.remainder(parts);
                // 计算总金额减去余数后的值
                BigDecimal totalMinusRemainder = totalAmount.subtract(remainder);
                // 计算每份的金额，保留4位小数
                BigDecimal averageAmount = totalMinusRemainder.divide(parts, 4, RoundingMode.HALF_UP);

                for (int j = 0; j < months.size(); j++) {
                    if (j == months.size() - 1) {
                        updateQjmxidAmount(months.get(j), (averageAmount.add(remainder)).setScale(4,
                                RoundingMode.HALF_UP).toPlainString(), jd, gkdm, djllspf);
                    } else {
                        updateQjmxidAmount(months.get(j), averageAmount.setScale(4, RoundingMode.HALF_UP).toPlainString(),jd,gkdm,djllspf);
                    }
                }
                updateMJFLL(jd, gkdm, djllspf, js, zyf,ks);



            } else if ("2".equals(cqff) && "1".equals(dqfs)) {
                writeLog("-------------------------------------------打切转移---------------------------------------------");
                //打切转移
                //将单价联络书品番的期间偿却月应收明细按月份从后往前更新为无效
                //（已回收的月份的应收数据不变化），如果打切金额小于未回收金额将差额的
                // 应收明细保留；将打切金额根据打切/转移回收期间等额均分到目标单价联络品
                // 番的应收明细，如果目标品番已经有自己的应收明细，那么需要按月份加起来

                BigDecimal totalAmount = new BigDecimal(dqje);
                BigDecimal whsAmount = new BigDecimal(whsje);
                BigDecimal parts = new BigDecimal(months.size());         //总分隔大小
                // 计算余数
                BigDecimal remainder = totalAmount.remainder(parts);
                // 计算总金额减去余数后的值
                BigDecimal totalMinusRemainder = totalAmount.subtract(remainder);
                // 计算每份的金额，保留4位小数
                BigDecimal averageAmount = totalMinusRemainder.divide(parts, 4, RoundingMode.HALF_UP);
                for (int j = 0; j < months.size(); j++) {
                    if (j == months.size() - 1) {
                        updateQjmxidAmount(months.get(j), averageAmount.add(remainder).setScale(4, RoundingMode.HALF_UP).toPlainString(), jd, gkdm, mbdjllspf);
                        totalAmount=totalAmount.subtract(averageAmount.add(remainder));
                        if(totalAmount.compareTo(whsAmount)==0){
                            writeLog("大于时更新"+totalAmount.setScale(4, RoundingMode.HALF_UP).toPlainString());
                            updateInvalid(djllspf, months.get(j),"ny=?");
                        }else if(totalAmount.compareTo(whsAmount)==
                                -1&&totalAmount.add(averageAmount).add(remainder).compareTo(whsAmount)==0){
                            writeLog("小于时更新"+totalAmount.setScale(4, RoundingMode.HALF_UP).toPlainString());
                            BigDecimal syje=totalAmount.subtract(whsAmount);
                            updateQjmxidAmount(months.get(j),syje.setScale(4, RoundingMode.HALF_UP).toPlainString() , jd, gkdm, mbdjllspf);
                        }
                    } else {
                        updateQjmxidAmount(months.get(j), averageAmount.setScale(4, RoundingMode.HALF_UP).toPlainString(),jd,gkdm,mbdjllspf);
                        totalAmount=totalAmount.subtract(averageAmount);
                        writeLog("比较"+totalAmount.compareTo(whsAmount));
                        if(totalAmount.compareTo(whsAmount)==0){
                            writeLog("大于时更新"+totalAmount.setScale(4, RoundingMode.HALF_UP).toPlainString());
                            updateInvalid(djllspf, months.get(j),"ny=?");
                        }else if(totalAmount.compareTo(whsAmount)==-1&&totalAmount.add(averageAmount).compareTo(whsAmount)==0){
                            writeLog("小于时更新"+totalAmount.setScale(4, RoundingMode.HALF_UP).toPlainString());
                            BigDecimal syje=totalAmount.subtract(whsAmount);
                            updateQjmxidAmount(months.get(j),syje.setScale(4, RoundingMode.HALF_UP).toPlainString() , jd, gkdm, mbdjllspf);
                        }
                    }
                }

            }
        }
        return Action.SUCCESS;
    }

    /**
     * @description:更新已回收最后一月之后的应收明细数据为无效
     * @author: gitdog
     * @date: 2024/1/7 12:03
     * @param: djllspf
     * @param: lastmonth
     * @return:
     **/
    public void updateInvalid(String djllspf, String lastmonth,String paramsql) {
        RecordSet rs = new RecordSet();
        String sql = "update uf_qjcqysmx set sjzt=1 where djllspf=? and "+paramsql;
        if (!rs.executeUpdate(sql, djllspf, lastmonth)) {
            error += sql + ":执行失败</br>";
        }
    }

    /**
     * @description:获取模具台账本品番最后一个月份
     * @author: gitdog
     * @date: 2024/1/7 12:05
     * @param: djllspf
     * @param: jd
     * @param: gkdm
     * @return: ny
     **/
    public String getmjlastMonth(String djllspf, String jd, String gkdm) {
        RecordSet rs = new RecordSet();
        writeLog("djllspf是"+djllspf);
        writeLog("jd是"+jd);
        writeLog("gkdm是"+gkdm);
        String sql = "select top(1) ny from uf_mjfyhsmx where djllspf=? and jd=? and gkdm=? order by ny desc";
        rs.executeQuery(sql, djllspf, jd, gkdm);
        rs.next();
        return rs.getString("ny");
    }

    private void updateQjmxidAmount(String s, String averageAmount, String jd, String gkdm, String djllspf) {
        RecordSet rs = new RecordSet();
        RecordSet rs1 = new RecordSet();
        RecordSet rs2 = new RecordSet();
        String params="";
        String values="";
        rs.executeQuery("select id,yhse from uf_qjcqysmx where jd=? and gkdm=? and djllspf=? and ny=?", jd, gkdm, djllspf
                ,s);
        if(rs.next()){
            String id=rs.getString("id");
            double yhse=rs.getDouble("yhse");
            BigDecimal yhseAmount=new BigDecimal(yhse);
            BigDecimal average=new BigDecimal(averageAmount);
            String result=yhseAmount.add(average).setScale(4, RoundingMode.HALF_UP).toPlainString();
            String sql = "update uf_qjcqysmx set yhse=" + result + " where ny=? and id=? and sjzt=0";
            rs1.executeUpdate(sql, s,id);
        }else{
            rs2.executeQuery("select * from uf_MJFLLBCS_dt1 where mainid=(select id from uf_MJFLLBCS where " +
                    "bm='uf_qjcqysmx')");
            while (rs2.next()) {
                if("ny".equals(rs2.getString("cs"))){
                    params += "ny" + ",";
                    values +="'"+s+"',";
                }else if("yhse".equals(rs2.getString("cs"))){
                    params += "yhse" + ",";
                    values +="'"+averageAmount+"',";
                }else if("jd".equals(rs2.getString("cs"))){
                    params += "jd" + ",";
                    values +="'"+jd+"',";
                }else if("gkdm".equals(rs2.getString("cs"))){
                    params += "gkdm" + ",";
                    values +="'"+gkdm+"',";
                }else if("djllspf".equals(rs2.getString("cs"))){
                    params += "djllspf" + ",";
                    values +="'"+djllspf+"',";
                }else {
                    params += rs2.getString("cs") + ",";
                    values += "'"+rs2.getString("z") + "',";
                }

            }

            params = params.substring(0, params.length() - 1);
            values = values.substring(0, values.length() - 1);


            //插入
            UUIDPKVFormDataSave var64 = new UUIDPKVFormDataSave();
            String modeuuid = (String) var64.generateID((Map) null);
            rs1.executeUpdate("insert into uf_qjcqysmx(" + params +",modedatacreater,modedatacreatertype," +
                    "modedatacreatedate,modedatacreatetime,modeuuid"+ ") values(" + values +",1,0,'" + this.getCurrentDate() + "','" + this.getCurrentTime() + "','" + modeuuid + "')");
        }
    }

    public List<String> getysmxid(String djllspf, String jd, String gkdm) {
        RecordSet rs = new RecordSet();
        ArrayList<String> ids = new ArrayList<>();
        String sql = "select id from uf_qjcqysmx where djllspf=? and jd=? and gkdm=? and sjzt=0 order by ny";
        rs.executeQuery(sql, djllspf, jd, gkdm);
        while (rs.next()) {
            ids.add(rs.getString("id"));
        }
        return ids;
    }

    //常规打切更新模具费履历表
    private void updateMJFLL(String jd, String gkdm, String djllspf, String lastDate, int zyf,String ksrq) {
        RecordSet rs = new RecordSet();
        RecordSet rs1 = new RecordSet();
        RecordSet rs2 = new RecordSet();
        RecordSet rs3 = new RecordSet();
        String params = "";
        String values = "";
        String sql = "select max(id) id from uf_mjfllb where jd=? and gkdm=? and djllspf=?";
        rs.executeQuery(sql, jd, gkdm, djllspf);
        if (rs.next()) {
            String id = rs.getString("id");
            writeLog("id是" + id);
            rs1.executeQuery("select * from uf_mjfllb where id=?", id);
            rs1.next();
            String ks = rs1.getString("hsksrq");
            String js = rs1.getString("hsjsrq");
            zyf += getMonthsDifference(ks, js);
            rs2.executeUpdate("update uf_mjfllb set hsjsrq=?,hszys=? where id=?", lastDate, zyf, id);
        } else {
            rs3.executeQuery("select * from uf_MJFLLBCS_dt1 where mainid=(select id from uf_MJFLLBCS where " +
                    "bm='uf_mjfllb')");
            while (rs3.next()) {
                if ("jd".equals(rs3.getString("cs"))) {
                    params += "jd"+",";
                    values += "'"+jd + "',";
                } else if ("gkdm".equals(rs3.getString("cs"))) {
                    params += "gkdm"+",";
                    values += "'"+gkdm + "',";
                } else if ("djllspf".equals(rs3.getString("cs"))) {
                    params += "djllspf"+",";
                    values += "'"+djllspf + "',";
                } else if ("hsksrq".equals(rs3.getString("cs"))) {
                    params += "hsksrq"+",";
                    values += "'"+ksrq + "',";
                } else if ("hsjsrq".equals(rs3.getString("cs"))) {
                    params += "hsjsrq"+",";
                    values += "'"+lastDate + "',";
                } else if ("hszys".equals(rs3.getString("cs"))) {
                    params += "hszys"+",";
                    values += "'"+zyf + "',";
                } else {
                    params += rs3.getString("cs") + ",";
                    values += "'"+rs3.getString("z") + "',";
                }
            }
            params = params.substring(0, params.length() - 1);
            values = values.substring(0, values.length() - 1);
            //插入
//            rs1.executeUpdate("insert into uf_mjfllb(" + params + ") values(" + values + ")");
            UUIDPKVFormDataSave var64 = new UUIDPKVFormDataSave();
            String modeuuid = (String) var64.generateID((Map) null);
            rs1.executeUpdate("insert into uf_mjfllb(" + params + ",modedatacreater,modedatacreatertype," +
                    "modedatacreatedate,modedatacreatetime,modeuuid" + ") values(" + values + ",1,0,'" + this.getCurrentDate() + "','" + this.getCurrentTime() + "','" + modeuuid + "')");

        }
    }

    private int getMonthsDifference(String date1, String date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(date1, formatter);
        LocalDate endDate = LocalDate.parse(date2, formatter);

        long monthsDiff = ChronoUnit.MONTHS.between(startDate, endDate);
        return (int) Math.abs(monthsDiff);
    }

    private ArrayList<String> getMonthsAndYears(String date1, String date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(date1, formatter);
        LocalDate endDate = LocalDate.parse(date2, formatter);

        ArrayList<String> monthYearList = new ArrayList<>();
        while (!startDate.isAfter(endDate)) {
            String monthYear = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            monthYearList.add(monthYear);
            startDate = startDate.plusMonths(1);
        }
        return monthYearList;
    }

    private String getCurrentDate() {
        SimpleDateFormat var1 = new SimpleDateFormat("yyyy-MM-dd");
        return var1.format(new Date());
    }

    private String getCurrentMonth() {
        SimpleDateFormat var1 = new SimpleDateFormat("MM");
        return var1.format(new Date());
    }

    private String getCurrentTime() {
        SimpleDateFormat var1 = new SimpleDateFormat("HH:mm:ss");
        return var1.format(new Date());
    }
}