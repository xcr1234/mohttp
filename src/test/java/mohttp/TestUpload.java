package mohttp;


import com.mo.mohttp.Http;
import com.mo.mohttp.Request;
import com.mo.mohttp.Response;
import com.mo.mohttp.constant.Agents;
import com.mo.mohttp.constant.StatusCode;
import com.mo.mohttp.misc.TextUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class TestUpload {

    public static void main(String[] args) throws IOException, URISyntaxException {
        //上传文件

        //上传一张图片到服务器。

        //http://chuantu.biz

        URL url = TestUpload.class.getResource("/银色.png");

        File file = new File(url.toURI());
        System.out.println(file);
        Request request = Http.newClient().uri("http://chuantu.biz/upload.php").method(Http.Method.POST)
                .param("MAX_FILE_SIZE","200000000")
                .file("uploadimg",file);

        Response response = request.execURL();

        int code = response.statusCode();
        String html = response.string("UTF-8");

        if(code == StatusCode.SC_OK){
            Document document = Jsoup.parse(html);
            Element div = document.getElementById("page-bg");
            String text = div.html();
            String imgUrl = middle(text,"(<a href=\"","\"");
            System.out.println("您上传的图片URL："+imgUrl);
        }else{
            System.out.println(code);
            System.out.println(html);
        }


    }

    public static String middle(String main,String left,String right){
        int n1 = main.indexOf(left);
        int n2 = main.indexOf(right,n1>0?n1+left.length():0);
        if(n1<0){
            return "";
        }
        if(n2<0){
            return main.substring(n1+left.length());
        }
        return main.substring(n1+left.length(),n2);
    }

}
