package Extantions;

import ToDo.ToDoClient;
import ToDo.ToDoItem;
import apache.ToDoClientApache;
import org.junit.jupiter.api.extension.*;

import java.io.IOException;

public class ToDoProvider implements ParameterResolver, AfterEachCallback {
    private final ToDoClient client=new ToDoClientApache("https://todo-app-sky.herokuapp.com/");
    private int id;

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(ToDoItem.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        try {
            ToDoItem item=client.createItem("New task to delete");
            id=item.getId();
            return item;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        client.deleteById(id);
    }
}
