package ru.job4j.tracker.action;

import java.time.LocalDateTime;
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

public class FindByIdActionTest {

    @Test
    public void whenFindByIdExecuteThenItemFound() {
        Output out = new StubOutput();
        Input input = mock(Input.class);
        FindByIdAction action = new FindByIdAction(out);
        Store tracker = new MemTracker();
        String ls = System.lineSeparator();
        LocalDateTime creationTime = LocalDateTime.of(2023, 5, 28, 17, 15);
        tracker.add(new Item(0, "Test item", creationTime));
        when(input.askInt(any(String.class))).thenReturn(1);
        boolean result = action.execute(input, tracker);
        assertThat(result, is(true));
        assertThat(out.toString(), is("id: 1, name: Test item, created: 28-05-2023 17:15:00" + ls));
    }

    @Test
    public void whenTryToFindWrongIdThenNoItemFound() {
        Output out = new StubOutput();
        Input input = mock(Input.class);
        FindByIdAction action = new FindByIdAction(out);
        Store tracker = new MemTracker();
        String ls = System.lineSeparator();
        LocalDateTime creationTime = LocalDateTime.of(2023, 5, 28, 17, 15);
        tracker.add(new Item(0, "Test item", creationTime));
        when(input.askInt(any(String.class))).thenReturn(9);
        boolean result = action.execute(input, tracker);
        assertThat(result, is(true));
        assertThat(out.toString(), is("Wrong id! Not found" + ls));
    }


}