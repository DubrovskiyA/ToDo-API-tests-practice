import Extantions.ToDoProvider;
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
import org.junit.jupiter.api.extension.ExtendWith;

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

    @Test
    @DisplayName("Проверяем, что задача создается")
    @Tag("Positive")
    public void shouldCreateTask() throws IOException {
    //Получить список задач
        List<ToDoItem> listBefore=client.getAll();
    //Создать задачу
        String title="Super job";
        ToDoItem newItem=client.createItem(title);
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
    @ExtendWith(ToDoProvider.class)
    public void shouldRenameTask(ToDoItem newItem) throws IOException {
        String newTitle="wash my car";
        ToDoItem renamedItem=client.renameById(newItem.getId(),newTitle);
        assertEquals(newTitle,renamedItem.getTitle());
    }
    @Test
    @DisplayName("Проверяем, что задачу можно отметить выполненной")
    @Tag("Positive")
    @ExtendWith(ToDoProvider.class)
    public void shouldMarkTaskAsCompleted(ToDoItem item) throws IOException {
        assertFalse(item.isCompleted());
        ToDoItem markItem=client.markCompleted(item.getId(), true);
        assertTrue(markItem.isCompleted());
    }
    @Test
    @DisplayName("Проверяем, что выполенную задачу можно отметить снова невыполненной")
    @Tag("Positive")
    @ExtendWith(ToDoProvider.class)
    public void shouldMarkTaskAsNonCompleted(ToDoItem item) throws IOException {
        assertFalse(item.isCompleted());
        ToDoItem itemTrue=client.markCompleted(item.getId(), true);
        assertTrue(itemTrue.isCompleted());
        ToDoItem itemFalse=client.markCompleted(item.getId(), false);
        assertFalse(itemFalse.isCompleted());
    }
    @Test
    @DisplayName("Проверяем, что задачу можно удалить")
    @Tag("Positive")
    @ExtendWith(ToDoProvider.class)
    public void shouldDeleteTask(ToDoItem item) throws IOException {
        List<ToDoItem> listBefore=client.getAll();
        client.deleteById(item.getId());
        List<ToDoItem> listAfter=client.getAll();
        assertEquals(1,listBefore.size()-listAfter.size());
        assertThrows(UnrecognizedPropertyException.class, ()->client.getItemById(item.getId()));
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
    @DisplayName("Проверяем, что при повторной отметке задачи выполненной, задача остается отмеченной как выполненная")
    @Tag("Negative")
    @ExtendWith(ToDoProvider.class)
    public void shouldNotChangeCompletedIfAlreadyTrue(ToDoItem item) throws IOException {
        assertFalse(item.isCompleted());
        ToDoItem itemTrue=client.markCompleted(item.getId(),true);
        assertTrue(itemTrue.isCompleted());
        ToDoItem itemTrueTwice=client.markCompleted(item.getId(),true);
        assertTrue(itemTrueTwice.isCompleted());
    }

    private ToDoItem createNewTask() throws IOException {
        ToDoItem newItem=client.createItem("wash my dog");
        return newItem;
    }
}
