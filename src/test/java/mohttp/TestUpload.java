package mohttp;


import com.mo.mohttp.Http;
import com.mo.mohttp.Request;
import com.mo.mohttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class TestUpload {

    public static void main(String[] args) throws IOException, URISyntaxException {
        //上传文件

        //上传一张图片到服务器。
        URL url = TestUpload.class.getResource("/银色.png");

        File file = new File(url.toURI());
        System.out.println(file);
        Request request = Http.newClient().uri("http://chuantu.biz/upload.php").method(Http.Method.POST)
                .param("MAX_FILE_SIZE","200000000")
                .file("uploadimg",file);

        Response response = request.execute();

        System.out.println(response.string());

    }

}
