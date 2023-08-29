package ru.job4j.tracker.store;

import org.junit.*;

import java.util.List;
import ru.job4j.tracker.model.Item;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HbmTrackerTest {

    @After
    public void tableClean() {
        try (var tracker = new HbmTracker()) {
            tracker.findAll().forEach(item -> tracker.delete(item.getId()));
        }
    }

    @Test
    public void whenSaveItemAndFindByGeneratedIdThenMustBeTheSame() {
        try (var tracker = new HbmTracker()) {
            Item item = tracker.add(new Item("test"));
            assertEquals(item, tracker.findById(item.getId()));
        }
    }

    @Test
    public void whenAddAndDeleteThenFindByIdIsNull() {
        try (var tracker = new HbmTracker()) {
            Item item = tracker.add(new Item("test"));
            tracker.delete(item.getId());
            assertNull(tracker.findById(item.getId()));
        }
    }

    @Test
    public void whenAddAndReplaceThenFindByIdSecondItem() {
        try (var tracker = new HbmTracker()) {
            Item itemOne = tracker.add(new Item("One"));
            int id = itemOne.getId();
            Item itemTwo = new Item("Two");
            itemTwo.setId(id);
            tracker.replace(id, itemTwo);
            assertEquals(itemTwo, tracker.findById(id));
        }
    }

    @Test
    public void whenAddTwoAndFindAllThenGetTwoItems() {
        try (var tracker = new HbmTracker()) {
            Item itemOne = tracker.add(new Item("One"));
            Item itemTwo = tracker.add(new Item("Two"));
            assertEquals(List.of(itemOne, itemTwo), tracker.findAll());
        }
    }

    @Test
    public void whenAddItemAndFindByNameThenGetItem() {
        try (var tracker = new HbmTracker()) {
            Item item = tracker.add(new Item("One"));
            assertEquals(List.of(item), tracker.findByName(item.getName()));
        }
    }

    @Test
    public void whenAddMultipleAndDeleteOneThenFindAllWithoutDeletedItem() {
        try (var tracker = new HbmTracker()) {
            Item itemOne = tracker.add(new Item("One"));
            Item itemTwo = tracker.add(new Item("Two"));
            Item itemThree = tracker.add(new Item("Three"));
            tracker.delete(itemTwo.getId());
            assertEquals(List.of(itemOne, itemThree), tracker.findAll());
        }
    }

}