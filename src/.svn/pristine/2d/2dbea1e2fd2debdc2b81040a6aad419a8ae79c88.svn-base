package weaver.interfaces.jjc.k3client.cw.service;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import weaver.interfaces.jjc.k3client.cw.util.K3Util;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class K3Service {

    private String sessionkey = "kdservice-sessionid";
    private String aspnetsessionkey = "ASP.NET_SessionId";

    private String sessionValue = "";
    private String aspnetsessionValue = "";
    K3Util kutil = new K3Util();
    String K3CloudURL = kutil.GetK3CloudURL();//
    String dbId = kutil.getDbId();//
    String uid = kutil.getUid();//
    String pwd = kutil.getPwd();//
    int lang = kutil.getLang();

    public String SavePurRKD(String sFormId, String data) throws Exception {
// 定义httpClient的实例
        HttpClient httpclient = new DefaultHttpClient();
        JSONObject jsonResult = null;
        String Number = "";
        /********** 用户登录Begin ************************/
        try {
            // 登录，校验用户的API接口地址
            String Login_URL = K3CloudURL
                    + "Kingdee.BOS.WebApi.ServicesStub.AuthService.ValidateUser.common.kdsvc";
            URI uri = new URI(Login_URL);
            HttpPost method = new HttpPost(uri);
            // 登录请求参数"5b974b86bbfac7","Administrator","888888",2052);
            String jsonParam = "{\"acctID\":\"" + dbId + "\",\"userName\":\""
                    + uid + "\",\"password\":\"" + pwd + "\",\"lcid\":" + lang
                    + "}";
            StringEntity entity = new StringEntity(jsonParam, "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            method.setEntity(entity);
            HttpResponse result = httpclient.execute(method);
            // 请求发送成功，并得到响应
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                // 读取服务器返回过来的json字符串数据
                str = EntityUtils.toString(result.getEntity());
                // 把json字符串转换成json对象
                jsonResult = JSONObject.fromObject(str);
                // 判断登录是否成功
                if (jsonResult.getInt("LoginResultType") == 1) {
                    System.out.println("登录成功！");
                    // 获取Cookie
                    Header[] headers = result.getHeaders("Set-Cookie");
                    for (int i = 1; i < headers.length; i++) {
                        Header header = headers[i];
                        String headerValue = header.getValue();
                        // 登录成功返回的登录session信息，保存下来，下面再调用接口的时候传给服务端
                        if (headerValue.trim().startsWith(sessionkey)) {
                            int endIndex = headerValue.indexOf(';');
                            sessionValue = headerValue.substring(20, endIndex);
                        } else if (headerValue.trim().startsWith(
                                aspnetsessionkey)) {
                            int endIndex = headerValue.indexOf(';');
                            aspnetsessionValue = headerValue.substring(18,
                                    endIndex);
                        }
                    }
                }
                // 登录失败，不继续
                else {
                    System.out.println("登录失败！");
                    return "";
                }
            } else {
                System.out.println("登录异常！");
            }
        } catch (Exception e) {
            System.out.println("post请求提交失败:" + e);
            e.printStackTrace();
            return "";
        }
        try {
            // 数据保存接口地址
            String Save_URL = K3CloudURL
                    + "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Save.common.kdsvc";
            URI save_uri = new URI(Save_URL);
            HttpPost method = new HttpPost(save_uri);
            // 采购入库单保存参数

            // String jsonParam =
            // "{\"formid\":\"STK_InStock\",\"obj\":{\"Creator\":\"IOM\",\"NeedUpDateFields\":[],\"Model\":{\"FID\":0,\"FBillTypeID\":{\"FNumber\":\"RKD01_SYS\"},\"FBusinessType\":\"CG\",\"FDate\":\"2015-05-28\",\"FStockOrgId\":{\"FNUMBER\":\"101.1\"},\"FDemandOrgId\":{\"FNUMBER\":\"101.1\"},\"FPurchaseOrgId\":{\"FNUMBER\":\"101.1\"},\"FCorrespondOrgId\":{\"FNUMBER\":\"101.1\"},\"FSupplierId\":{\"FNUMBER\":\"VEN00003\"},\"FSupplyId\":{\"FNUMBER\":\"VEN00003\"},\"FSupplyContact\":\"\",\"FSupplyAddress\":\"\",\"FSettleId\":{\"FNUMBER\":\"VEN00003\"},\"FChargeId\":{\"FNUMBER\":\"VEN00003\"},\"FCreatorId\":{\"FUserAccount\":\"demo\"},\"FCreateDate\":\"2015-05-28\",\"FApproveDate\":\"\",\"FIsInterLegalPerson\":false,\"FInStockFin\":{\"FSettleOrgId\":{\"FNUMBER\":\"101.1\"},\"FPayOrgId\":{\"FNUMBER\":\"101.1\"},\"FSettleCurrId\":{\"FNumber\":\"PRE001\"},\"FIsIncludedTax\":false,\"FBillAmount\":1000,\"FBillTaxAmount\":170,\"FBillAllAmount\":1170,\"FBillCostAmount\":1970,\"FBillCost\":800,\"FLocalCurrId\":{\"FNumber\":\"PRE001\"},\"FExchangeRate\":1,\"FBillAmount_LC\":1000,\"FBillTaxAmount_LC\":170,\"FBillAllAmount_LC\":1170,\"FBilCostAmount_LC\":1970,\"FISGENFORIOS\":false},\"STK_InStock__FInStockEntry\":[{\"FEntryID\":0,\"FMaterialId\":{\"FNumber\":\"1.01.001.0070\"},\"FUnitID\":{\"FNumber\":\"Pcs\"},\"FBaseUnitQty\":10,\"FREALQTY\":10,\"FPrice\":100,\"FAmount\":1000,\"FEntryTaxRate\":17,\"FTaxPrice\":117,\"FEntryTaxAmount\":170,\"FAllAmount\":1170,\"FAmount_LC\":1000,\"FTaxAmount_LC\":170,\"FAllAmount_LC\":1170,\"FCostPrice\":100,\"FPROCESSFEE\":800,\"FMaterialCosts\":1170,\"FEntryCostAmount\":1970,\"FCostAmount_LC\":1970,\"FStockId\":{\"FNumber\":\"CK001\"},\"FDiscount\":0,\"FDiscountRate\":0,\"FContractlNo\":\"\",\"FNote\":\"\"}]}}}";

            Map<String, String> map = new HashMap<String, String>();
            // String jsonParam =
            // "{\"formid\":\"STK_InStock\",\"obj\":{\"Creator\":\"IOM\",\"NeedUpDateFields\":[],\"Model\":{\"FID\":0,\"FBillTypeID\":{\"FNumber\":\"RKD01_SYS\"},\"FBusinessType\":\"CG\",\"FDate\":\"2015-05-28\",\"FStockOrgId\":{\"FNUMBER\":\"101.1\"},\"FDemandOrgId\":{\"FNUMBER\":\"101.1\"},\"FPurchaseOrgId\":{\"FNUMBER\":\"101.1\"},\"FCorrespondOrgId\":{\"FNUMBER\":\"101.1\"},\"FSupplierId\":{\"FNUMBER\":\"VEN00003\"},\"FSupplyId\":{\"FNUMBER\":\"VEN00003\"},\"FSupplyContact\":\"\",\"FSupplyAddress\":\"\",\"FSettleId\":{\"FNUMBER\":\"VEN00003\"},\"FChargeId\":{\"FNUMBER\":\"VEN00003\"},\"FCreatorId\":{\"FUserAccount\":\"demo\"},\"FCreateDate\":\"2015-05-28\",\"FApproveDate\":\"\",\"FIsInterLegalPerson\":false,\"FInStockFin\":{\"FSettleOrgId\":{\"FNUMBER\":\"101.1\"},\"FPayOrgId\":{\"FNUMBER\":\"101.1\"},\"FSettleCurrId\":{\"FNumber\":\"PRE001\"},\"FIsIncludedTax\":false,\"FBillAmount\":1000,\"FBillTaxAmount\":170,\"FBillAllAmount\":1170,\"FBillCostAmount\":1970,\"FBillCost\":800,\"FLocalCurrId\":{\"FNumber\":\"PRE001\"},\"FExchangeRate\":1,\"FBillAmount_LC\":1000,\"FBillTaxAmount_LC\":170,\"FBillAllAmount_LC\":1170,\"FBilCostAmount_LC\":1970,\"FISGENFORIOS\":false},\"STK_InStock__FInStockEntry\":[{\"FEntryID\":0,\"FMaterialId\":{\"FNumber\":\"1.01.001.0070\"},\"FUnitID\":{\"FNumber\":\"Pcs\"},\"FBaseUnitQty\":10,\"FREALQTY\":10,\"FPrice\":100,\"FAmount\":1000,\"FEntryTaxRate\":17,\"FTaxPrice\":117,\"FEntryTaxAmount\":170,\"FAllAmount\":1170,\"FAmount_LC\":1000,\"FTaxAmount_LC\":170,\"FAllAmount_LC\":1170,\"FCostPrice\":100,\"FPROCESSFEE\":800,\"FMaterialCosts\":1170,\"FEntryCostAmount\":1970,\"FCostAmount_LC\":1970,\"FStockId\":{\"FNumber\":\"CK001\"},\"FDiscount\":0,\"FDiscountRate\":0,\"FContractlNo\":\"\",\"FNote\":\"\"}]}}}";

            map.put("formid", sFormId);
            map.put("data", data);

            String jsonParam = jsonToJson(map);
            System.out.println("jsonParam" + jsonParam);
            // String jsonParam=json;//
            // "{\"formid\":\"CN_PAYAPPLY\",\"obj\":{\"Creator\":\"\",\"NeedUpDateFields\":[],\"NeedReturnFields\":[],\"IsDeleteEntry\":\"True\",\"SubSystemId\":\"\",\"IsVerifyBaseDataField\":\"false\",\"IsEntryBatchFill\":\"True\",\"ValidateFlag\":\"True\",\"NumberSearch\":\"True\",\"InterationFlags\":\"\",\"IsAutoSubmitAndAudit\":\"false\",\"Model\":{\"FID\":\"0\",\"FBILLTYPEID\":{\"FNUMBER\":\"\"},\"FBillNo\":\"\",\"FDATE\":\"1900-01-01\",\"FCONTACTUNITTYPE\":\"\",\"FCONTACTUNIT\":{\"FNumber\":\"\"},\"FRECTUNITTYPE\":\"\",\"FRECTUNIT\":{\"FNumber\":\"\"},\"FCURRENCYID\":{\"FNumber\":\"\"},\"FPAYORGID\":{\"FNumber\":\"\"},\"FSETTLEORGID\":{\"FNumber\":\"\"},\"FPURCHASEORGID\":{\"FNumber\":\"\"},\"FPURCHASEDEPTID\":{\"FNumber\":\"\"},\"FPURCHASERGROUPID\":{\"FNumber\":\"\"},\"FSALEORGID\":{\"FNumber\":\"\"},\"FPURCHASERID\":{\"FNumber\":\"\"},\"FSALEDEPTID\":{\"FNUMBER\":\"\"},\"FSALEGROUPID\":{\"FNumber\":\"\"},\"FSALEERID\":{\"FNUMBER\":\"\"},\"FDEPARTMENT\":{\"FNumber\":\"\"},\"FDOCUMENTSTATUS\":\"\",\"FScanPoint\":{\"FNUMBER\":\"\"},\"FCANCELSTATUS\":\"\",\"FSOURCESYSTEM\":\"\",\"FIsCredit\":\"false\",\"FBankActID\":{\"FNUMBER\":\"\"},\"FAPPLYORGID\":{\"FNumber\":\"\"},\"FSETTLECUR\":{\"FNUMBER\":\"\"},\"FPAYAPPLYENTRY\":[{\"FEntryID\":\"0\",\"FCOSTID\":{\"FNUMBER\":\"\"},\"FSETTLETYPEID\":{\"FNumber\":\"\"},\"FPAYPURPOSEID\":{\"FNumber\":\"\"},\"FARPURPOSEID\":{\"FNUMBER\":\"\"},\"FENDDATE\":\"1900-01-01\",\"FEXPECTPAYDATE\":\"1900-01-01\",\"FAPPLYAMOUNTFOR\":\"0\",\"FTAXAMOUNT\":\"0\",\"FApplyPclAmount\":\"0\",\"FApplyInstAmount\":\"0\",\"FEACHBANKACCOUNT\":\"\",\"FEACHCCOUNTNAME\":\"\",\"FEACHBANKNAME\":\"\",\"FSwiftCode\":\"\",\"FDescription\":\"\",\"FPRICE\":\"0\",\"FQTY\":\"0\",\"FPRICEUNITID\":{\"FNumber\":\"\"}}]}}";

            StringEntity entity = new StringEntity(jsonParam, "utf-8");

            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");

            // 把成功登录的Session信息传进去，获取连接信息
            method.setHeader(sessionkey, sessionValue);
            method.setHeader(aspnetsessionkey, aspnetsessionValue);
            // 方法参数
            method.setEntity(entity);
            HttpResponse result = httpclient.execute(method);
            // 请求发送成功，并得到响应
            if (result.getStatusLine().getStatusCode() == 200) {
                System.out.println("请求成功");
                String str = "";
                // 读取服务器返回过来的json字符串数据
                str = EntityUtils.toString(result.getEntity());
                System.out.println(str);
                // 成功的字符串类似
                // {"Result":{"ResponseStatus":{"IsSuccess":true},"Id":100040,"Number":"CGRK00016"}}
                // 失败 的字符串类似
                // {"Result":{"ResponseStatus":{"ErrorCode":500,"IsSuccess":false,"Errors":[{"FieldName":"FStockId","Message":"单据体“明细信息”第1行字段“仓库”是必填项"},{"FieldName":"AbstractInteractionResult","Message":"AbstractInteractionResult.InteractionContext is null"}]},"Id":""}}

                // 把json字符串转换成json对象，方便操作
                Number = str;

            }
        } catch (Exception e) {
            System.out.println("post请求提交失败:" + e);
        }
        /********** 保存采购入库单End ************************/
        return Number;
    }

    public String jsonToJson(Map<String, String> map) throws Exception {
        Object object = JSON.toJSON(map);
        String string = object.toString();
        return string;
    }
}
