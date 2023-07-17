import apache.MyRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ToDoApacheContractTest {
    private static final String URL="https://todo-app-sky.herokuapp.com/";
    private HttpClient client;
    @BeforeEach
    public void setUp(){
        client=HttpClientBuilder.
                create().
                addInterceptorLast(new MyRequestInterceptor()).
                build();
    }
    @Test
    @DisplayName("Получение списка задач. Проверяем статус-код и заголовок Content-Type")
    public void shouldReceive200OnGetRequest() throws IOException {
        HttpGet getList=new HttpGet(URL);
        HttpResponse response=client.execute(getList);
        String body= EntityUtils.toString(response.getEntity());
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(1, response.getHeaders("Content-type").length);
        assertEquals("application/json; charset=utf-8",response.getHeaders("Content-Type")[0].getValue());
        assertTrue(body.startsWith("["));
        assertTrue(body.endsWith("]"));
    }
    @Test
    @DisplayName("Создание задачи. Проверяем статус-код, заголовок Content-Type и тело ответа содержит json")
    public void shouldReceive201OnPostRequest() throws IOException {
        HttpPost post=new HttpPost(URL);

        String myContent="{\"title\":\"Homework\"}";
        StringEntity entity=new StringEntity(myContent, ContentType.APPLICATION_JSON);
        post.setEntity(entity);

        HttpResponse response=client.execute(post);
        String body=EntityUtils.toString(response.getEntity());

        assertEquals(201,response.getStatusLine().getStatusCode());
        assertEquals(1,response.getHeaders("Content-Type").length);
        assertEquals("application/json; charset=utf-8",response.getHeaders("Content-Type")[0].getValue());
        assertTrue(body.startsWith("{"));
        assertTrue(body.endsWith("}"));

    }
    @Test
    @DisplayName("Создание задачи с пустым телом запроса. Статус-код=400, в теле ответа есть сообщение об ошибке")
    public void shouldReceive400OnEmptyPost() throws IOException {
        HttpPost post=new HttpPost(URL);

        String bodyToBe="{\"status\":400,\"message\":\"Invalid JSON\"}";

        HttpResponse response=client.execute(post);
        String bodyAsIs=EntityUtils.toString(response.getEntity());

        assertEquals(400,response.getStatusLine().getStatusCode());
        assertEquals(1,response.getHeaders("Content-Type").length);
        assertEquals("application/json; charset=utf-8",response.getHeaders("Content-Type")[0].getValue());
        assertEquals(bodyToBe,bodyAsIs);
    }
    @Test
    @DisplayName("Удаление задачи, Статус-код=204, Content-Length=0")
    public void shouldReceive204OnDelete() throws IOException {
//        Создание задачи
        HttpPost post=new HttpPost(URL);
        String postBody="{\"title\":\"New homework\"}";
        StringEntity entity=new StringEntity(postBody, ContentType.APPLICATION_JSON);
        post.setEntity(entity);

        HttpResponse postReq=client.execute(post);
        String bodyPostReq=EntityUtils.toString(postReq.getEntity());
        String idPost="/"+bodyPostReq.substring(6,11);
        System.out.println(idPost);

//        Удаление задачи
        HttpDelete delete=new HttpDelete(URL+idPost);
        HttpResponse response=client.execute(delete);
        assertEquals(204,response.getStatusLine().getStatusCode());
        assertEquals(0,response.getHeaders("Content-Length")[0].getValue());
    }

}
