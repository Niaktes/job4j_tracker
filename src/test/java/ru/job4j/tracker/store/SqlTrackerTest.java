package ru.job4j.tracker.store;

import org.junit.*;
import ru.job4j.tracker.model.Item;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        Item item = tracker.add(new Item("item"));
        assertThat(tracker.findById(item.getId()), is(item));
        tracker.delete(item.getId());
        assertThat(tracker.findById(item.getId()), nullValue());
    }

    @Test
    public void whenAddAndReplaceThenMustBeSecondItem() {
        Item item = tracker.add(new Item("item"));
        tracker.replace(item.getId(), new Item("replaced"));
        assertThat(tracker.findById(item.getId()).getName(), is("replaced"));
    }

    @Test
    public void whenAddTwoAndFindALLThenMustBeTwoItems() {
        Item itemOne = tracker.add(new Item("itemOne"));
        Item itemTwo = tracker.add(new Item("itemTwo"));
        assertThat(tracker.findAll(), is(List.of(itemOne, itemTwo)));
    }

    @Test
    public void whenAddItemAndFindByNameThenMustBeTheSame() {
        Item item = tracker.add(new Item("item"));
        assertThat(tracker.findByName("item"), is(List.of(item)));
    }

    @Test
    public void whenMultipleAddAndDeleteOneThenFindAllMustBeWithoutItem() {
        Item itemOne = tracker.add(new Item("itemOne"));
        Item itemTwo = tracker.add(new Item("itemTwo"));
        Item itemThree = tracker.add(new Item("itemThree"));
        assertNotEquals(tracker.findAll(), List.of(itemOne, itemThree));
        tracker.delete(itemTwo.getId());
        assertThat(tracker.findAll(), is(List.of(itemOne, itemThree)));
    }

}
