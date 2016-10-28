package mohttp;


import com.mo.mohttp.Client;
import com.mo.mohttp.Http;
import com.mo.mohttp.Http.Method;
import com.mo.mohttp.Request;
import com.mo.mohttp.Response;
import com.mo.mohttp.constant.Agents;
import com.mo.mohttp.constant.Headers;

import java.io.IOException;
import java.net.URISyntaxException;

public class TestGet {
    public static void main(String[] args) throws Exception {

        Client client = Http.newClient();


        final Request request = client.uri("http://apis.baidu.com/heweather/weather/free").method(Method.GET)
                .header(Headers.host, "apis.baidu.com")
                .header("apikey", "5eca5cdb5c9b5f390fb24f7f2e3f0148").agent(Agents.Chrome)
                .param("city=北京");  // .param("city","北京")

        Response response = request.execute();

        String res = response.string();

        System.out.println(res);




    }



    public static String getUrl(String url) throws IOException, URISyntaxException {
        return Http.GET(url).execute().string();
    }
}
