import net.minidev.asm.ConvertDate;
import org.artofsolving.jodconverter.cli.Convert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class inputstreamTofile {
    public static void main(String[] args) throws IOException {
//        // 读取txt文件的内容
//        BufferedReader reader = new BufferedReader(new FileReader("D://test.txt"));
//        String content = reader.readLine();
//        reader.close();
//
//        // 将字符串转成文件流
//        FileWriter writer = new FileWriter("D://output.jpg");
//        writer.write(content);
//        writer.close();
        String a=convertDate("20230202");
        System.out.println(a);
    }


    public static String convertDate(String date){
        String afterConvertDate="";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date d = sdf.parse(date);
            afterConvertDate = sdf2.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return afterConvertDate;
    }
}
