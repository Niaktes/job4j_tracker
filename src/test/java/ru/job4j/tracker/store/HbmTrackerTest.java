package ru.job4j.tracker.store;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.job4j.tracker.model.Item;

class HbmTrackerTest {

    @AfterEach
    void tableClean() {
        try (var tracker = new HbmTracker()) {
            tracker.findAll().forEach(item -> tracker.delete(item.getId()));
        }
    }

    @Test
    void whenSaveItemAndFindByGeneratedIdThenMustBeTheSame() throws Exception {
        try (var tracker = new HbmTracker()) {
            Item item = tracker.add(new Item("test"));
            Assertions.assertEquals(item, tracker.findById(item.getId()));
        }
    }

    @Test
    void whenAddAndDeleteThenFindByIdIsNull() {
        try (var tracker = new HbmTracker()) {
            Item item = tracker.add(new Item("test"));
            tracker.delete(item.getId());
            Assertions.assertNull(tracker.findById(item.getId()));
        }
    }

    @Test
    void whenAddAndReplaceThenFindByIdSecondItem() {
        try (var tracker = new HbmTracker()) {
            Item itemOne = tracker.add(new Item("One"));
            int id = itemOne.getId();
            Item itemTwo = new Item("Two");
            itemTwo.setId(id);
            tracker.replace(id, itemTwo);
            Assertions.assertEquals(itemTwo, tracker.findById(id));
        }
    }

    @Test
    void whenAddTwoAndFindAllThenGetTwoItems() {
        try (var tracker = new HbmTracker()) {
            Item itemOne = tracker.add(new Item("One"));
            Item itemTwo = tracker.add(new Item("Two"));
            Assertions.assertEquals(List.of(itemOne, itemTwo), tracker.findAll());
        }
    }

    @Test
    void whenAddItemAndFindByNameThenGetItem() {
        try (var tracker = new HbmTracker()) {
            Item item = tracker.add(new Item("One"));
            Assertions.assertEquals(List.of(item), tracker.findByName(item.getName()));
        }
    }

    @Test
    void whenAddMultipleAndDeleteOneThenFindAllWithoutDeletedItem() {
        try (var tracker = new HbmTracker()) {
            Item itemOne = tracker.add(new Item("One"));
            Item itemTwo = tracker.add(new Item("Two"));
            Item itemThree = tracker.add(new Item("Three"));
            tracker.delete(itemTwo.getId());
            Assertions.assertEquals(List.of(itemOne, itemThree), tracker.findAll());
        }
    }

}