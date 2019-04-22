package edu.brown.cs.where2meet.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import edu.brown.cs.where2meet.event.Event;
import edu.brown.cs.where2meet.event.User;

/**
 * A class to facilitate interacting with the mongodb database.
 *
 */
public class W2MDatabase {

  private static Connection conn;

  private static LoadingCache<Long, Event> eventCache;
  private static LoadingCache<Long, User> userCache;
  private static final int MAX_CACHE = 1000;

  /**
   * Constructor for the W2MDatabase.
   *
   * @param dbname
   *          the name of the database to use.
   */
  public W2MDatabase(String dbname) {
    conn = connectToDB(dbname);

    userCache = CacheBuilder.newBuilder().maximumSize(MAX_CACHE)
        .build(new UserCacheLoader());

    eventCache = CacheBuilder.newBuilder().maximumSize(MAX_CACHE)
        .build(new EventCacheLoader());
  }

  /**
   * Establishes a connection to the database.
   *
   * @param dbname
   *          the name of the database to connect to.
   * @return a connection to the database.
   */
  private Connection connectToDB(String dbname) {
    Connection setup = null;
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException c) {
      System.out.println("ERROR:");
    }

    String url = "jdbc:sqlite:" + dbname;
    try {
      setup = DriverManager.getConnection(url);
      try (Statement stat = setup.createStatement()) {
        stat.executeUpdate("PRAGMA foreign_keys = ON;");
      } catch (SQLException e) {
        System.out.println("ERROR:");
        return null;
      }
    } catch (SQLException e) {
      System.out.println("ERROR:");
      return null;
    }
    return setup;
  }

  /**
   * Adds an event to the database.
   *
   * @param event
   *          the event whose data is to be added.
   */
  public void addEvent(Event event) {
    Long id = event.getId();
    String name = event.getName();
    List<Double> coords = event.getLocation();
    String date = event.getDate();
    String time = event.getTime();
    try (PreparedStatement prep = conn
        .prepareStatement("INSERT INTO events VALUES(?,?,?,?,?,?)")) {
      prep.setLong(1, id);
      prep.setString(2, name);
      prep.setDouble(3, coords.get(0));
      prep.setDouble(4, coords.get(1));
      prep.setString(5, date);
      prep.setString(6, time);
      prep.execute();
    } catch (SQLException e) {
      System.out.println(e);
    }

  }

  /**
   * Gets an event from the database.
   *
   * @param id
   *          the id of the event to be retrieved
   * @return an event object holding the data from the database.
   */
  public static Event getEvent(Long id) {
    try {
      Event e = eventCache.get(id);
      try (PreparedStatement prep = conn.prepareStatement(
          "SELECT user_id FROM events_users WHERE event_id = ?;")) {
        prep.setLong(1, id);
        try (ResultSet rs = prep.executeQuery()) {
          while (rs.next()) {
            e.addUser(rs.getLong(1));
          }
        }
      } catch (SQLException err) {
        System.out.println(err);
      }
      return e;
    } catch (ExecutionException e) {
      System.out.print("ERROR: could not retrieve event");
      return null;
    }
  }

  /**
   * Adds a user to the user collection in the database.
   *
   * @param user
   *          the user whose data is added to the collection
   */
  public static void addUser(User user) {
    Long id = user.getId();
    String name = user.getName();
    List<Double> coords = user.getLocation();
    double lat = coords.get(0);
    double lng = coords.get(1);
    try (PreparedStatement prep = conn
        .prepareStatement("INSERT INTO users VALUES(?,?,?,?)")) {
      prep.setLong(1, id);
      prep.setString(2, name);
      prep.setDouble(3, lat);
      prep.setDouble(4, lng);
      prep.execute();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  /**
   * Gets a user from the database.
   *
   * @param id
   *          the id of the user to be retrieved.
   * @return a User object with the data retrieved from the database.
   */
  public static User getUser(Long id) {
    try {
      User u = userCache.get(id);

      try (PreparedStatement prep = conn
          .prepareStatement("SELECT event_id, price, rating, distance, "
              + "category FROM events_users WHERE user_id = ?;")) {
        prep.setLong(1, id);

        try (ResultSet rs = prep.executeQuery()) {
          while (rs.next()) {
            u.addEvent(rs.getLong(1));
            u.setPrice(rs.getInt(2));
            u.setRating(rs.getInt(3));
            u.setDist(rs.getDouble(4));
            u.setCategory(rs.getString(5));
          }
        }
      } catch (SQLException e) {
        System.out.println(e);
      }
      return u;
    } catch (ExecutionException e) {
      System.out.print("ERROR: could not retrieve user");
      return null;
    }
  }

  /**
   * Loads an event to be stored in the cache.
   *
   * @param id
   *          the id of the event to be stored.
   * @return the event to be stored.
   */
  public static Event loadEvent(Long id) {

    String name = "";
    double lat = 0;
    double lng = 0;
    String date = "";
    String time = "";

    try (PreparedStatement prep = conn
        .prepareStatement("SELECT * FROM events WHERE id = ?;")) {
      prep.setLong(1, id);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          name = rs.getString(2);
          lat = rs.getDouble(3);
          lng = rs.getDouble(4);
          date = rs.getString(5);
          time = rs.getString(6);
        }
      }
    } catch (SQLException e) {
      System.out.println(e);
    }

    List<Double> coords = new ArrayList<>();
    coords.add(lat);
    coords.add(lng);
    Set<Long> users = new HashSet<>();

    return new Event(id, name, users, coords, date, time);
  }

  /**
   * Loads a user to be stored in the cache.
   *
   * @param id
   *          the id of the user to be stored
   * @return the user to be stored.
   */
  public static User loadUser(Long id) {

    String name = "";
    double lat = 0;
    double lng = 0;

    try (PreparedStatement prep = conn
        .prepareStatement("SELECT * FROM users WHERE id = ?;")) {
      prep.setLong(1, id);

      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          name = rs.getString(2);
          lat = rs.getDouble(3);
          lng = rs.getDouble(4);
        }
      } catch (SQLException e) {
        System.out.println(e);
      }
    } catch (SQLException e) {
      System.out.println(e);
    }
    Set<Long> events = new HashSet<>();

    List<Double> coords = new ArrayList<>();
    coords.add(lat);
    coords.add(lng);
    return new User(id, name, events, coords);
  }

  /**
   * Adds a user to an event in the events_users table of the database.
   *
   * @param user
   *          the user to add
   * @param event
   *          the id of the event with which the user is associated.
   */
  public static void addUserToEvent(User user, Long event) {

    try (PreparedStatement prep = conn
        .prepareStatement("INSERT INTO events_users VALUES(?,?,?,?,?,?);")) {
      prep.setLong(1, event);
      prep.setLong(2, user.getId());
      prep.setInt(3, user.getPrice());
      prep.setDouble(5, user.getDist());
      prep.setInt(4, user.getRating());
      prep.setString(6, user.getCategory());
      prep.execute();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  /**
   * Updates the filters for a user in the events_users table.
   *
   * @param user
   *          the user whose data is to be updated.
   * @param event
   *          the event with which the user is associated.
   */
  public static void updateUser(User user, Long event) {
    try (PreparedStatement prep = conn.prepareStatement(
        "UPDATE events_users SET price = ?, rating = ?, distance = ?, "
            + "category = ? WHERE (user_id = ? AND event_id = ?);")) {
      prep.setInt(1, user.getPrice());
      prep.setInt(2, user.getRating());
      prep.setDouble(3, user.getDist());
      prep.setString(4, user.getCategory());
      prep.setLong(5, user.getId());
      prep.setLong(6, event);
      prep.execute();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  /**
   * Creates the tables for the db. Primarily used for testing.
   */
  public void createdb() {
    try (PreparedStatement prep = conn
        .prepareStatement("CREATE TABLE IF NOT EXISTS 'users'('id' INTEGER, "
            + "'name' TEXT, 'latitude' INTEGER, 'longitude' INTEGER);")) {
      prep.execute();
    } catch (SQLException e) {
      System.out.println(e);
    }

    try (PreparedStatement prep = conn
        .prepareStatement("CREATE TABLE IF NOT EXISTS 'events'"
            + "('id' INTEGER, 'name' TEXT, 'latitude' INTEGER, "
            + "'longitude' INTEGER, 'date' STRING, 'time' STRING);")) {
      prep.execute();
    } catch (SQLException e) {
      System.out.println();
    }

    try (PreparedStatement prep = conn
        .prepareStatement("CREATE TABLE IF NOT EXISTS 'events_users'"
            + "('event_id' INTEGER, 'user_id' INTEGER, 'price' INTEGER, "
            + "'rating' INTEGER, 'distance' INTEGER, category TEXT);")) {
      prep.execute();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  /**
   * Clears the tables of the database. Primarily used for testing to keep the
   * testdb size down.
   */
  public void cleardb() {
    try (PreparedStatement prep = conn
        .prepareStatement("DROP TABLE IF EXISTS users")) {
      prep.execute();
    } catch (SQLException e) {
      System.out.println(e);
    }

    try (PreparedStatement prep = conn
        .prepareStatement("DROP TABLE IF EXISTS events")) {
      prep.execute();
    } catch (SQLException e) {
      System.out.println(e);
    }

    try (PreparedStatement prep = conn
        .prepareStatement("DROP TABLE IF EXISTS events_users")) {
      prep.execute();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

}
