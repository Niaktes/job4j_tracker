package ru.job4j.tracker.action;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Test;
import ru.job4j.tracker.Store;
import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.output.StubOutput;
import ru.job4j.tracker.store.MemTracker;

public class DeleteActionTest {

    @Test
    public void whenDeleteExecuteThenNoMoreItemInStore() {
        Output out = new StubOutput();
        Input input = mock(Input.class);
        DeleteAction action = new DeleteAction(out);
        Store tracker = new MemTracker();
        String ls = System.lineSeparator();
        tracker.add(new Item("Test item"));
        when(input.askInt(any(String.class))).thenReturn(1);
        action.execute(input, tracker);
        assertThat(out.toString(), is("Item is successfully deleted!" + ls));
        assertThat(tracker.findAll(), empty());
    }

    @Test
    public void whenDeleteWrongIdThenNoMoreItemInStore() {
        Output out = new StubOutput();
        Input input = mock(Input.class);
        DeleteAction action = new DeleteAction(out);
        Store tracker = new MemTracker();
        String ls = System.lineSeparator();
        tracker.add(new Item("Test item"));
        when(input.askInt(any(String.class))).thenReturn(9);
        action.execute(input, tracker);
        assertThat(out.toString(), is("Wrong id!" + ls));
        assertThat(tracker.findById(1).getName(), is("Test item"));
    }


}