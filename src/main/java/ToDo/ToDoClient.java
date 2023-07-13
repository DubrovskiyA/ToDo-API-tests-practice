package ToDo;

import ToDo.CreateToDo;
import ToDo.ToDoItem;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

public interface ToDoClient {
    List<ToDoItem> getAll() throws IOException;
    ToDoItem getItemById(int id) throws IOException;
    ToDoItem createItem(String title) throws IOException;
    void deleteById(int id) throws IOException;
    ToDoItem renameById(int id, String newName) throws IOException;
    ToDoItem markCompleted(int id, boolean completed) throws IOException;
    void deleteAll() throws IOException;
}
