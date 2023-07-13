package apache;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class MyResponseInterceptor implements HttpResponseInterceptor {
    @Override
    public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        System.out.println(httpResponse.getStatusLine());
        for (Header allHeader : httpResponse.getAllHeaders()) {
            System.out.println(allHeader);
        }
//        System.out.println("BODY===>");
//        if (httpResponse.getEntity()==null){
//            System.out.println("NO BODY");
//        }else{
//            System.out.println(EntityUtils.toString(httpResponse.getEntity()));
//        }

    }
}
