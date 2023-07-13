import ToDo.CreateToDo;
import ToDo.ToDoClient;
import ToDo.ToDoItem;
import apache.MyRequestInterceptor;
import apache.MyResponseInterceptor;
import apache.ToDoClientApache;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToDoBusinessTest {
    ToDoClient client;
    @BeforeEach
    public void setUp(){
        client=new ToDoClientApache("https://todo-app-sky.herokuapp.com/");
    }

    @DisplayName("Проверяем, что задача создается")
    @Tag("Positive")
    public void shouldCreateTask() throws IOException {
    //Получить список задач
        List<ToDoItem> listBefore=client.getAll();
    //Создать задачу
        CreateToDo toDo=new CreateToDo();
        String title="New extra super task";
        toDo.setTitle(title);
        ToDoItem newItem=client.createItem(toDo);
    //Проверить, что задача отображается в списке
        assertFalse(newItem.getUrl().isBlank());
        assertFalse(newItem.isCompleted());
        assertTrue(newItem.getId()>0);
        assertEquals(title,newItem.getTitle());
        assertEquals(0,newItem.getOrder());

        //Задач стало на 1 больше
        List<ToDoItem>listAfter=client.getAll();
        assertEquals(1,listAfter.size()-listBefore.size());

        //Проверить еще и по id
        ToDoItem single=client.getItemById(newItem.getId());
        assertEquals(title,single.getTitle());
    }
    @Test
    @DisplayName("Проверяем, что можно получить список задач")
    @Tag("Positive")
    public void shouldReceiveList() throws IOException {
        client.deleteAll();
        List<ToDoItem> listBefore=client.getAll();
        assertTrue(listBefore.size()==0);
        ToDoItem item1=createNewTask();
        ToDoItem item2=createNewTask();
        ToDoItem item3=createNewTask();
        List<ToDoItem> listAfter=client.getAll();
        assertTrue(listAfter.size()==3);
    }
    @Test
    @DisplayName("Проверяем, что можно получить список из одной задачи")
    @Tag("Positive")
    public void shouldReceiveListWithOneTask() throws IOException {
        client.deleteAll();
        List<ToDoItem> listBefore=client.getAll();
        assertTrue(listBefore.size()==0);
        ToDoItem item1=createNewTask();
        List<ToDoItem> listAfter=client.getAll();
        assertTrue(listAfter.size()==1);
    }
    @Test
    @DisplayName("Проверяем, что можно получить пустой список")
    @Tag("Positive")
    public void shouldReceiveEmptyList() throws IOException {
        client.deleteAll();
        List<ToDoItem> listBefore=client.getAll();
        assertTrue(listBefore.size()==0);
    }
    @Test
    @DisplayName("Проверяем, что задачу можно переименовать")
    @Tag("Positive")
    public void shouldRenameTask() throws IOException {
        ToDoItem newItem = createNewTask();
        String newTitle="wash my cat";
        ToDoItem renamedItem=client.renameById(newItem.getId(),newTitle);
        assertEquals(newTitle,renamedItem.getTitle());
    }
    @Test
    @DisplayName("Проверяем, что задачу можно отметить выполненной")
    @Tag("Positive")
    public void shouldMarkTaskAsCompleted() throws IOException {
        ToDoItem newItem=createNewTask();
        assertFalse(newItem.isCompleted());
        ToDoItem markItem=client.markCompleted(newItem.getId(), true);
        assertTrue(markItem.isCompleted());
    }
    @Test
    @DisplayName("Проверяем, что выполенную задачу можно отметить снова невыполненной")
    @Tag("Positive")
    public void shouldMarkTaskAsNonCompleted() throws IOException {
        ToDoItem item=createNewTask();
        assertFalse(item.isCompleted());
        ToDoItem itemTrue=client.markCompleted(item.getId(), true);
        assertTrue(itemTrue.isCompleted());
        ToDoItem itemFalse=client.markCompleted(item.getId(), false);
        assertFalse(itemFalse.isCompleted());
    }
    @Test
    @DisplayName("Проверяем, что задачу можно удалить")
    @Tag("Positive")
    public void shouldDeleteTask() throws IOException {
        ToDoItem newItem=createNewTask();
        List<ToDoItem> listBefore=client.getAll();
        client.deleteById(newItem.getId());
        List<ToDoItem> listAfter=client.getAll();
        assertEquals(1,listBefore.size()-listAfter.size());
        assertThrows(UnrecognizedPropertyException.class, ()->client.getItemById(newItem.getId()));
    }
    @Test
    @DisplayName("Проверяем, что можно удалить все задачи")
    @Tag("Positive")
    public void shouldDeleteAll() throws IOException {
        client.deleteAll();
        List<ToDoItem> list1=client.getAll();
        assertTrue(list1.size()==0);
        ToDoItem item1=createNewTask();
        ToDoItem item2=createNewTask();
        ToDoItem item3=createNewTask();
        List<ToDoItem> list2=client.getAll();
        assertTrue(list2.size()==3);
        client.deleteAll();
        List<ToDoItem> list3=client.getAll();
        assertTrue(list3.size()==0);
    }
    @Test
    @DisplayName("Проверяем, что при повторной отметке задачи выполненной задача остается отмеченной как выполненная")
    @Tag("Negative")
    public void should() throws IOException {
        ToDoItem item=createNewTask();
        assertFalse(item.isCompleted());
        ToDoItem itemTrue=client.markCompleted(item.getId(),true);
        assertTrue(itemTrue.isCompleted());
        ToDoItem itemTrueTwice=client.markCompleted(item.getId(),true);
        assertTrue(itemTrueTwice.isCompleted());
    }

    private ToDoItem createNewTask() throws IOException {
        CreateToDo toDo=new CreateToDo();
        toDo.setTitle("wash my dog");
        ToDoItem newItem=client.createItem(toDo);
        return newItem;
    }
}
