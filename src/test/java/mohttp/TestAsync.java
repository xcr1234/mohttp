package mohttp;


import com.mo.mohttp.Http;
import com.mo.mohttp.Request;
import com.mo.mohttp.Response;
import com.mo.mohttp.async.HttpCallback;
import com.mo.mohttp.constant.Agents;
import com.mo.mohttp.constant.Headers;

import java.io.IOException;
import java.util.concurrent.Future;

public class TestAsync {
    public static void main(String[] args) {
        Request request = Http.GET("http://apis.baidu.com/heweather/weather/free")
                .header(Headers.host, "apis.baidu.com")
                .header("apikey", "5eca5cdb5c9b5f390fb24f7f2e3f0148").agent(Agents.Chrome)
                .param("city=北京");

        final long t = System.currentTimeMillis();

        Future<Response> future = request.execute(new HttpCallback() {
            @Override
            public void complete(Response response) throws IOException {
                System.out.println(response.string());
                long time = System.currentTimeMillis() - t;
                System.out.println("request cost "+time+" ms.");
            }

            @Override
            public void failed(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void cancelled() {
                System.out.println("cancelled!");
            }
        });
        Http.getExecutorService().shutdown(); //关闭线程池资源

    }
}
