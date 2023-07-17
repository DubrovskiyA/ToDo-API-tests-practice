package apache;

import ToDo.CreateToDo;
import ToDo.ToDoClient;
import ToDo.ToDoItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.List;

public class ToDoClientOkHttp implements ToDoClient {
    private final String URL;
    private final OkHttpClient client;
    private ObjectMapper mapper;

    public ToDoClientOkHttp(String url) {
        URL = url;
        client = new OkHttpClient.Builder().build();
        mapper=new ObjectMapper();
    }

    @Override
    public List<ToDoItem> getAll() throws IOException {
        Request request=new Request.Builder().get().url(URL).build();
        Response response=client.newCall(request).execute();
        List<ToDoItem> list=mapper.readValue(response.body().string(), new TypeReference<List<ToDoItem>>() {
        });
        return list;
    }

    @Override
    public ToDoItem getItemById(int id) throws IOException {
        Request request=new Request.Builder().get().url(URL+"/"+id).build();
        Response response=client.newCall(request).execute();
        ToDoItem item=mapper.readValue(response.body().string(), ToDoItem.class);
        return item;
    }

    @Override
    public ToDoItem createItem(String title) throws IOException {
        CreateToDo toDo=new CreateToDo();
        toDo.setTitle(title);
        String body=mapper.writeValueAsString(toDo);
        MediaType APPLICATION_JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody bodyToPost=RequestBody.create(body,APPLICATION_JSON);
        Request request=new Request.Builder().post(bodyToPost).url(URL).build();
        Response response=client.newCall(request).execute();
        ToDoItem newItem=mapper.readValue(response.body().string(),ToDoItem.class);
        return newItem;
    }

    @Override
    public void deleteById(int id) throws IOException {
        Request request=new Request.Builder().delete().url(URL+"/"+id).build();
        client.newCall(request).execute();
    }

    @Override
    public ToDoItem renameById(int id, String newName) throws IOException {
        CreateToDo toDo=new CreateToDo();
        toDo.setTitle(newName);
        String body=mapper.writeValueAsString(toDo);
        MediaType APPLICATION_JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody bodyToPatch=RequestBody.create(body,APPLICATION_JSON);
        Request request=new Request.Builder().patch(bodyToPatch).url(URL+"/"+id).build();
        Response response=client.newCall(request).execute();
        ToDoItem item=mapper.readValue(response.body().string(),ToDoItem.class);
        return item;
    }

    @Override
    public ToDoItem markCompleted(int id, boolean completed) throws IOException {
        String body="{\"completed\":"+completed+"}";
        MediaType APPLICATION_JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody bodyToPatch=RequestBody.create(body,APPLICATION_JSON);
        Request request=new Request.Builder().patch(bodyToPatch).url(URL+"/"+id).build();
        Response response=client.newCall(request).execute();
        ToDoItem item=mapper.readValue(response.body().string(),ToDoItem.class);
        return item;
    }

    @Override
    public void deleteAll() throws IOException {
        Request request=new Request.Builder().delete().url(URL).build();
        client.newCall(request).execute();
    }
}
