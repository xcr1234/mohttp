package mohttp;


import com.mo.mohttp.Http;
import com.mo.mohttp.Request;
import com.mo.mohttp.Response;
import com.mo.mohttp.constant.Agents;
import com.mo.mohttp.constant.Headers;
import com.mo.mohttp.constant.StatusCode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URISyntaxException;

public class TestPost {

    public static void main(String[] args) throws IOException, URISyntaxException {
        //模拟登录一个网站

        //http://www.layoutit.com/en/login/do

        Request request = Http.newClient().agent(Agents.Chrome)
                .uri("http://www.layoutit.com/en/login/do").method(Http.Method.POST)
                .header(Headers.host,"www.layoutit.com")
                .header(Headers.referer,"http://www.layoutit.com/en/login/do")
                .header(Headers.connection,"keep-alive")
                .param("login[email]","abcd@qq.com")
                .param("login[password]","ABCD@QQ.COM");
        Response response = request.execute();
        String html = response.string();
        int code = response.statusCode();
        if(code == StatusCode.SC_OK){
            System.out.println("登录失败！");  //模拟登录失败，用错误的用户和密码登录
            Document document = Jsoup.parse(html);
            System.out.println(document.getElementById("boxLoginForm").getElementsByTag("div").get(0).text());
        }

        //重新登录
        request.clearParams().param("login[email]","abcd@qq.com")
                .param("login[password]","abcd@qq.com"); //改为正确的用户名和密码。
        Response response2 = request.execute();
        if(response2.statusCode()==StatusCode.SC_FOUND){
            System.out.println("登录成功！");
            Response response3 = request.getClient().GET("http://www.layoutit.com/en/dashboard").execute();//登录后维持cookie，访问另一个页面
            System.out.println(response3.string());
            System.out.println(response3.statusCode());
        }

    }
}
