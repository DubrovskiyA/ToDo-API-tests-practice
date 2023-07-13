package apache;

import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class MyRequestInterceptor implements HttpRequestInterceptor {
    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        System.out.println(httpRequest.getRequestLine());
        for (Header allHeader : httpRequest.getAllHeaders()) {
            System.out.println(allHeader);
        }
        if (httpRequest.getRequestLine().getMethod()=="POST"
                || httpRequest.getRequestLine().getMethod()=="PATCH"){
            HttpEntityEnclosingRequest req=(HttpEntityEnclosingRequest)httpContext.getAttribute("http.request");
            System.out.println("BODY===>");
            if (req.getEntity()==null){
                System.out.println("NO BODY");
            } else{
                System.out.println(EntityUtils.toString(req.getEntity()));
            }



        }
    }
}
