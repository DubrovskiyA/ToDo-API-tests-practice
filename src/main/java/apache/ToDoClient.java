package apache;

import ToDo.ToDoItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class ToDoClient {
    public static String URL="https://todo-app-sky.herokuapp.com/";
    public static void main(String[] args) throws IOException {
//        HttpClient client= HttpClientBuilder.create().build();
//        ObjectMapper mapper=new ObjectMapper();
//
//
////        Get list
//        HttpGet request1=new HttpGet(URL);
//        HttpResponse response1=client.execute(request1);
//        System.out.println(response1.getStatusLine().getProtocolVersion());
//        System.out.println(response1.getStatusLine().getStatusCode());
//        System.out.println(response1.getStatusLine().getReasonPhrase());
//
//        for (Header header : response1.getAllHeaders()) {
//            System.out.println(header);
//        }
//
//
//        System.out.println();
////        System.out.println(response1.getEntity().getContent());
//
//        String body=EntityUtils.toString(response1.getEntity());
//        System.out.println(body);
//        System.out.println();
//
////        Get item
//        HttpGet request2=new HttpGet(URL+"84846");
//        HttpResponse response2=client.execute(request2);
//        if (response2.getStatusLine().getStatusCode()==200){
//            String body2=EntityUtils.toString(response2.getEntity());
//            System.out.println(body2);
//        } else {
//            System.err.println("Error");
//        }
//        System.out.println();
//
////        Post
//        HttpPost createItemRequest=new HttpPost(URL);
//        createItemRequest.addHeader("Content-Type","application/json");
//        String myContent="{\"title\":\"My java task\"}";
//        StringEntity entity=new StringEntity(myContent);
//        createItemRequest.setEntity(entity);
//
//        HttpResponse newItem=client.execute(createItemRequest);
//        System.out.println(newItem.getStatusLine());
////        String body3=EntityUtils.toString(newItem.getEntity());
////        System.out.println(body3);
//
//        ToDoItem item=mapper.readValue(EntityUtils.toString(newItem.getEntity()), ToDoItem.class);
//        System.out.println(item);
//
////        Get list (typed)
//        HttpResponse listAsString=client.execute(request1);
////        ToDoItem[] list=mapper.readValue(EntityUtils.toString(listAsString.getEntity()), ToDoItem[].class);
////        System.out.println(list.length);
//        List<ToDoItem> list=mapper.readValue(EntityUtils.toString(listAsString.getEntity()), List.class);
//        System.out.println(list.size());
//
////        Post create item (typed)
//        ToDoItem itemToCreate=new ToDoItem();
//        itemToCreate.setTitle("My new task");
//        String s= mapper.writeValueAsString(itemToCreate);
//        entity=new StringEntity(s);
//        createItemRequest.setEntity(entity);
//        HttpResponse newItemTyped=client.execute(createItemRequest);
//        System.out.println(newItemTyped.getStatusLine());
//
////        Patch
//        HttpPatch markTaskAsCompleted=new HttpPatch(URL+item.getUrl());
//        ToDoItem ItemToMark=new ToDoItem();
//        ItemToMark.setCompleted(true);
//        String bodyToPatchReq= mapper.writeValueAsString(ItemToMark);
//        entity=new StringEntity(bodyToPatchReq);
//        markTaskAsCompleted.setEntity(entity);
//        HttpResponse patchTask=client.execute(markTaskAsCompleted);
//        System.out.println(patchTask.getStatusLine());
//
////        Delete
//        HttpDelete deleteItem=new HttpDelete(URL+item.getUrl());
//        HttpResponse deleteResponse=client.execute(deleteItem);
//        System.out.println(deleteResponse.getStatusLine());
        ObjectMapper m=new ObjectMapper();
        ToDoItem item= new ToDoItem();
        boolean completed = false;
        item.setCompleted(completed);
        String body=m.writeValueAsString(item);
        System.out.println(body);
    }
}
