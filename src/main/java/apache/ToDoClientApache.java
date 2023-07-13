package apache;

import ToDo.CreateToDo;
import ToDo.ToDoClient;
import ToDo.ToDoItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ToDoClientApache implements ToDoClient {
    private final String URL;
    private final HttpClient client;
    private ObjectMapper mapper;

    public ToDoClientApache(String url) {
        URL = url;
        client= HttpClientBuilder.
                create().
                addInterceptorLast(new MyRequestInterceptor()).
                addInterceptorFirst(new MyResponseInterceptor()).
                build();
        mapper=new ObjectMapper();
    }

    @Override
    public List<ToDoItem> getAll() throws IOException {
        HttpGet get=new HttpGet(URL);
        HttpResponse response=client.execute(get);
        List<ToDoItem> list=mapper.readValue(EntityUtils.toString(response.getEntity()), new TypeReference<List<ToDoItem>>() {
        });
        return list;
    }

    @Override
    public ToDoItem getItemById(int id) throws IOException {
        HttpGet get=new HttpGet(URL+"/"+id);
        HttpResponse response=client.execute(get);
        ToDoItem item=mapper.readValue(EntityUtils.toString(response.getEntity()), ToDoItem.class);
        return item;
    }

    @Override
    public ToDoItem createItem(String title) throws IOException {
        CreateToDo toDo=new CreateToDo();
        toDo.setTitle(title);
        HttpPost post=new HttpPost(URL);
        String body= mapper.writeValueAsString(toDo);
        StringEntity entity=new StringEntity(body, ContentType.APPLICATION_JSON);
        post.setEntity(entity);
        HttpResponse response=client.execute(post);
        ToDoItem item=mapper.readValue(EntityUtils.toString(response.getEntity()), ToDoItem.class);
        return item;
    }

    @Override
    public void deleteById(int id) throws IOException {
        HttpDelete delete=new HttpDelete(URL+"/"+id);
        client.execute(delete);
    }

    @Override
    public ToDoItem renameById(int id, String newName) throws IOException {
        HttpPatch patch=new HttpPatch(URL+"/"+id);
        CreateToDo toDo=new CreateToDo();
        toDo.setTitle(newName);
//        String body="{\"title\":\""+newName+"\"}";
        String body= mapper.writeValueAsString(toDo);
        StringEntity entity=new StringEntity(body);
        patch.setEntity(entity);
        HttpResponse response=client.execute(patch);
        ToDoItem item=mapper.readValue(EntityUtils.toString(response.getEntity()), ToDoItem.class);
        return item;
    }

    @Override
    public ToDoItem markCompleted(int id, boolean completed) throws IOException {
        HttpPatch patch=new HttpPatch(URL+"/"+id);
        String body="{\"completed\":"+completed+"}";
        StringEntity entity=new StringEntity(body);
        patch.setEntity(entity);
        HttpResponse response=client.execute(patch);
        ToDoItem item=mapper.readValue(EntityUtils.toString(response.getEntity()), ToDoItem.class);
        return item;
    }

    @Override
    public void deleteAll() throws IOException {
        HttpDelete delete=new HttpDelete(URL);
        client.execute(delete);
    }
}
