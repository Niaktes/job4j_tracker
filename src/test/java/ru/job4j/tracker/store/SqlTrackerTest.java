package ru.job4j.tracker.store;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.tracker.model.Item;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class SqlTrackerTest {

    private static Connection connection;
    private static SqlTracker tracker;

    @BeforeClass
    public static void initConnection() {
        try (InputStream in = SqlTrackerTest.class.getClassLoader().getResourceAsStream("test.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        tracker = new SqlTracker(connection);
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @After
    public void wipeTable() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM items")) {
            statement.execute();
        }
    }

    @Test
    public void whenSaveItemAndFindByGeneratedIdThenMustBeTheSame() {
        Item item = new Item("item");
        tracker.add(item);
        assertThat(tracker.findById(item.getId()), is(item));
    }

    @Test
    public void whenAddAndDeleteThenFindByIdMustBeNull() {
        Item item = new Item("item");
        tracker.add(item);
        assertThat(tracker.findById(item.getId()), is(item));
        tracker.delete(item.getId());
        assertThat(tracker.findById(item.getId()), nullValue());
    }

    @Test
    public void whenAddAndReplaceThenMustBeSecondItem() {
        Item item = new Item("item");
        tracker.add(item);
        tracker.replace(item.getId(), new Item("replaced"));
        assertThat(tracker.findById(item.getId()).getName(), is("replaced"));
    }

    @Test
    public void whenAddTwoAndFindALLThenMustBeTwoItems() {
        Item itemOne = new Item("itemOne");
        Item itemTwo = new Item("itemTwo");
        tracker.add(itemOne);
        tracker.add(itemTwo);
        List<Item> items = new ArrayList<>();
        items.add(itemOne);
        items.add(itemTwo);
        assertThat(tracker.findAll(), is(items));
    }

    @Test
    public void whenAddItemAndFindByNameThenMustBeTheSame() {
        Item item = new Item("item");
        tracker.add(item);
        List<Item> expected = new ArrayList<>();
        expected.add(item);
        assertThat(tracker.findByName(item.getName()), is(expected));
    }

    @Test
    public void whenMultipleAddAndDeleteOneThenFindAllMustBeWithoutItem() {
        Item itemOne = new Item("itemOne");
        Item itemTwo = new Item("itemTwo");
        Item itemThree = new Item("itemThree");
        List<Item> expected = new ArrayList<>();
        expected.add(itemOne);
        expected.add(itemThree);
        tracker.add(itemOne);
        tracker.add(itemTwo);
        tracker.add(itemThree);
        assertNotEquals(tracker.findAll(), expected);
        tracker.delete(itemTwo.getId());
        assertThat(tracker.findAll(), is(expected));
    }

}
