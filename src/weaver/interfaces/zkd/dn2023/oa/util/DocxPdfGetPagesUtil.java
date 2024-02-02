package weaver.interfaces.zkd.dn2023.oa.util;

import com.itextpdf.text.pdf.PdfReader;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.io.InputStream;

/**
 * @BelongsProject: ZKD_DA_2023
 * @BelongsPackage: weaver.interfaces.zkd.dn2023.oa.util
 * @Author: hf_zhangyuan
 * @CreateTime: 2023-03-12  17:38
 * @Description: TODO
 * @Version: 1.0
 */

public class DocxPdfGetPagesUtil {
    /**
     * 根据docx文件路径获取文档的页数
     * @param
     * @return
     * @throws IOException
     */
    public int getDocxPages(InputStream is)  {
        int pages=0;
        try{
            XWPFDocument docx=new XWPFDocument(is);
            pages = docx.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
        }catch (Exception e){

        }

        return pages;
    }
    /**
     * 根据doc文件路径获取文档的页数
     * @param
     * @return
     * @throws IOException
     */
    public int getDocPages(InputStream is)  {
        int pages=0;
        try {
            HWPFDocument wordDoc = new HWPFDocument(is);
            pages = wordDoc.getSummaryInformation().getPageCount();
        }catch (Exception e){

        }
        return pages;
    }
    /**\
     * 根据pdf文件路径获取文档的页数
     * @param
     * @return
     * @throws IOException
     */
    public int getPdfPages(InputStream is) {
        int pages=0;
        try{
        PdfReader pdf=new PdfReader(is);
        pages=pdf.getNumberOfPages();
        }catch (Exception e){

        }
        return pages;
    }


}
