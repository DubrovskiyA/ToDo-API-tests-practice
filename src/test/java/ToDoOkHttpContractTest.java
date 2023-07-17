import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ToDoOkHttpContractTest {
    private static final String URL="https://todo-app-sky.herokuapp.com/";
    private OkHttpClient client;
    @BeforeEach
    public void setUp(){
        client=new OkHttpClient.Builder().build();
    }
    @Test
    @DisplayName("Получение списка задач. Проверяем статус-код и заголовок Content-Type")
    @Tag("positive")
    public void shouldReceive200OnGetRequest() throws IOException {
        Request request=new Request.Builder().get().url(URL).build();
        Response response=client.newCall(request).execute();
        String bodyOfResponse=response.body().string();
        assertEquals(200,response.code());
        assertEquals(1, response.headers("Content-Type").size());
        assertEquals("application/json; charset=utf-8",response.header("Content-Type"));
        assertTrue(bodyOfResponse.startsWith("["));
        assertTrue(bodyOfResponse.endsWith("]"));
    }
    @Test
    @DisplayName("Получение одной задачи по id")
    @Tag("positive")
    public void shouldReceive200OnGetTheTaskRequest() throws IOException {
        //        Создание задачи
        String bodyToPost="{\"title\":\"the task\"}";
        MediaType APPLICATION_JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody bodyToPostReq=RequestBody.create(bodyToPost,APPLICATION_JSON);
        Request requestPost=new Request.Builder().post(bodyToPostReq).url(URL).build();
        Response responsePost=client.newCall(requestPost).execute();
        String url=responsePost.body().string().substring(6,11);
        //        Получение задачи по id
        Request requestGet=new Request.Builder().get().url(URL+url).build();
        Response responseGet=client.newCall(requestGet).execute();
        String bodyResp=responseGet.body().string();
        assertEquals(200,responseGet.code());
        assertEquals(1,responseGet.headers("Content-Type").size());
        assertEquals("application/json; charset=utf-8",responseGet.header("Content-Type"));
        assertEquals(url,bodyResp.substring(6,11));
        assertEquals("the task",bodyResp.substring(21,29));

    }

    @Test
    @DisplayName("Создание задачи. Проверяем статус-код, заголовок Content-Type и тело ответа содержин json")
    @Tag("positive")
    public void shouldReceive201OnPostRequest() throws IOException {
        String body="{\"title\":\"NEW TASK\"}";
        MediaType APPLICATION_JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody bodyToPost=RequestBody.create(body,APPLICATION_JSON);
        Request request=new Request.Builder().post(bodyToPost).url(URL).build();
        Response response=client.newCall(request).execute();
        String bodyOfResponse=response.body().string();
        assertEquals(201,response.code());
        assertEquals(1,response.headers("Content-Type").size());
        assertEquals("application/json; charset=utf-8",response.header("Content-Type"));
        assertTrue(bodyOfResponse.startsWith("{"));
        assertTrue(bodyOfResponse.endsWith("}"));
    }
    @Test
    @DisplayName("Создание задачи с пустым телом запроса, Статас-код=400, в теле ответа есть сообщение об ошибке")
    public void shouldReceive400OnEmptyPostRequest() throws IOException {
        String body="";
        MediaType APPLICATION_JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody bodyToPost=RequestBody.create(body,APPLICATION_JSON);
        Request request=new Request.Builder().post(bodyToPost).url(URL).build();
        Response response=client.newCall(request).execute();
        String expectedBody="{\"status\":400,\"message\":\"Invalid JSON\"}";
        String responseBody=response.body().string();
        assertEquals(400,response.code());
        assertEquals(1,response.headers("Content-Type").size());
        assertEquals("application/json; charset=utf-8",response.header("Content-Type"));
        assertEquals(expectedBody,responseBody);
    }
    @Test
    @DisplayName("Отметка задачи выполненной, статус код=200, тело ответа содержинт json")
    public void shouldReceive200OnCompletedPatch() throws IOException {
        //        Создание задачи
        String bodyToPost="{\"title\":\"task\"}";
        MediaType APPLICATION_JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody bodyToPostReq=RequestBody.create(bodyToPost,APPLICATION_JSON);
        Request requestPost=new Request.Builder().post(bodyToPostReq).url(URL).build();
        Response responsePost=client.newCall(requestPost).execute();
        String bodyPostResp=responsePost.body().string();
        assertEquals("false",bodyPostResp.substring(39,44));
        String url=bodyPostResp.substring(66,71);
        //        Отметка задачи выполненной
        String bodyToPatch="{\"completed\":true}";
        RequestBody bodyToPatchReq=RequestBody.create(bodyToPatch,APPLICATION_JSON);
        Request requestPatch=new Request.Builder().patch(bodyToPatchReq).url(URL+url).build();
        Response responsePatch=client.newCall(requestPatch).execute();
        String bodyPatchResp=responsePatch.body().string();
        assertEquals(200,responsePatch.code());
        assertTrue(bodyPatchResp.startsWith("{"));
        assertTrue(bodyPatchResp.endsWith("}"));
        assertEquals("true",bodyPatchResp.substring(39,43));
    }
    @Test
    @DisplayName("Переименование задачи, Статус-код=200")
    public void shouldReceive200OnPatchRequestRename() throws IOException {
        //        Создание задачи
        String bodyToPost="{\"title\":\"task\"}";
        MediaType APPLICATION_JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody bodyToPostReq=RequestBody.create(bodyToPost,APPLICATION_JSON);
        Request requestPost=new Request.Builder().post(bodyToPostReq).url(URL).build();
        Response responsePost=client.newCall(requestPost).execute();
        String url=responsePost.body().string().substring(66,71);
        //        Переименование
        String bodyToRename="{\"title\":\"NEW task\"}";
        RequestBody bodyToRenameReq=RequestBody.create(bodyToRename,APPLICATION_JSON);
        Request requestRename=new Request.Builder().patch(bodyToRenameReq).url(URL+url).build();
        Response responseRename=client.newCall(requestRename).execute();
        String bodyRespRename=responseRename.body().string();
        assertEquals(200,responseRename.code());
        assertEquals("NEW task",bodyRespRename.substring(21,29));
    }
    @Test
    @DisplayName("Удаление задачи, Статус-код=204")
    public void shouldReceive204OnDelete() throws IOException {
        //        Создание задачи
        String bodyToPost="{\"title\":\"task\"}";
        MediaType APPLICATION_JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody bodyToPostReq=RequestBody.create(bodyToPost,APPLICATION_JSON);
        Request requestPost=new Request.Builder().post(bodyToPostReq).url(URL).build();
        Response response=client.newCall(requestPost).execute();
        String url=response.body().string().substring(66,71);
        //        Удалние задачи по id
        Request requestDelete=new Request.Builder().delete().url(URL+url).build();
        Response responseDelete=client.newCall(requestDelete).execute();
        assertEquals(204,responseDelete.code());
        assertEquals("0",responseDelete.header("Content-Length"));
        Request getDeletedTask=new Request.Builder().get().url(URL+url).build();
        Response responseGetDeletedTask=client.newCall(getDeletedTask).execute();
        assertEquals(404,responseGetDeletedTask.code());
    }
}

