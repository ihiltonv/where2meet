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
import edu.brown.cs.where2meet.event.Suggestion;
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
  public static void addEvent(Event event) {
    Long id = event.getId();
    String name = event.getName();
    List<Double> coords = event.getLocation();
    String date = event.getDate();
    String time = event.getTime();
    try (PreparedStatement prep = conn
        .prepareStatement("INSERT INTO events VALUES(?,?,?,?,?,?,?)")) {
      prep.setLong(1, id);
      prep.setString(2, name);
      prep.setDouble(3, coords.get(0));
      prep.setDouble(4, coords.get(1));
      prep.setString(5, date);
      prep.setString(6, time);
      List<Suggestion> s = event.getSuggestions();
      prep.setString(7, Suggestion.suggToString(s));
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

      try (PreparedStatement prep = conn
          .prepareStatement("SELECT s1 FROM events WHERE id = ?")) {
        prep.setLong(1, id);
        try (ResultSet rs = prep.executeQuery()) {
          while (rs.next()) {
            e.setSuggestions(Suggestion.getStringAsList(rs.getString(1)));
          }
        }
      } catch (SQLException err) {

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

    try (PreparedStatement prep = conn
        .prepareStatement("INSERT INTO users VALUES(?,?)")) {
      prep.setLong(1, id);
      prep.setString(2, name);
      prep.execute();
    } catch (SQLException e) {
      System.out.println(e);
      e.printStackTrace();
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

      try (PreparedStatement prep = conn.prepareStatement(
          "SELECT event_id FROM events_users WHERE user_id = ?;")) {
        prep.setLong(1, id);

        try (ResultSet rs = prep.executeQuery()) {
          while (rs.next()) {
            u.addEvent(rs.getLong(1));
          }
        }
      } catch (SQLException e) {
        System.out.println(e);
        e.printStackTrace();
      }
      return u;
    } catch (ExecutionException e) {
      System.out.print("ERROR: could not retrieve user");
      return null;
    }
  }

  /**
   * Gets the id of a name associated with an event.
   *
   * @param name
   *          the name of the user.
   * @param event
   *          the id of the event.
   * @return the id of the user with the corresponding name.
   */
  public static User getUserFromName(String name, Long event) {
    Long uid = null;
    try (PreparedStatement prep = conn.prepareStatement(
        "SELECT u.id FROM users AS u, events_users AS eu WHERE eu.event_id = ? "
            + "AND u.name = ?;")) {
      prep.setLong(1, event);
      prep.setString(2, name);
      try (ResultSet rs = prep.executeQuery()) {
        uid = rs.getLong(1);

      }
    } catch (SQLException e) {
      System.out.println(e);

    }

    if (uid == null) {
      return null;
    }
    return W2MDatabase.getUser(uid);

  }

  /**
   * returns true if there a user with the given name associated with the given
   * event, false otherwise.
   *
   * @param name
   *          the name to check.
   * @param event
   *          the id of the event to search.
   * @return true if the user exists in the event, false otherwise.
   */
  public static boolean userExists(String name, Long event) {
    User user = getUserFromName(name, event);
    if (user == null) {
      return false;
    }
    return true;
  }

  /**
   * Gets the user associated with an event.
   *
   * @param uid
   *          the id of the user to get.
   * @param eid
   *          the id of the event to get.
   * @return the user associated with the event.
   */
  public static User getUserWithEvent(Long uid, Long eid) {
    User u = getUser(uid);

    try (PreparedStatement prep = conn
        .prepareStatement("SELECT price, rating, distance,category,s1,s2,s3 "
            + "FROM events_users WHERE (user_id = ? AND event_id = ?);")) {
      prep.setLong(1, uid);
      prep.setLong(2, eid);

      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {

          u.setPrice(rs.getInt(1));
          u.setRating(rs.getInt(2));
          u.setDist(rs.getDouble(3));
          u.setCategory(rs.getString(4));
          Suggestion[] sugg = new Suggestion[3];
          sugg[0] = Suggestion.toSugg(rs.getString(5));
          sugg[1] = Suggestion.toSugg(rs.getString(6));
          sugg[2] = Suggestion.toSugg(rs.getString(7));
          u.setSuggestions(sugg);
        }
      }

    } catch (SQLException e) {
      System.out.println(e);
    }

    return u;
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
    List<Suggestion> suggestions = new ArrayList<>();

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
          suggestions = Suggestion.getStringAsList(rs.getString(7));

        }
      }
    } catch (SQLException e) {
      System.out.println(e);
    }

    List<Double> coords = new ArrayList<>();
    coords.add(lat);
    coords.add(lng);
    Set<Long> users = new HashSet<>();
    Event newEvent = new Event(id, name, users, coords, date, time);
    newEvent.setSuggestions(suggestions);
    return newEvent;
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

    try (PreparedStatement prep = conn
        .prepareStatement("SELECT name FROM users WHERE id = ?;")) {
      prep.setLong(1, id);

      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          name = rs.getString(1);

        }
      }

    } catch (SQLException e) {
      System.out.println(e);
      e.printStackTrace();
    }
    Set<Long> events = new HashSet<>();

    return new User(id, name, events);
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

    try (PreparedStatement prep = conn.prepareStatement(
        "INSERT INTO events_users VALUES(?,?,?,?,?,?,?,?,?);")) {
      prep.setLong(1, event);
      prep.setLong(2, user.getId());
      prep.setInt(3, user.getPrice());
      prep.setDouble(5, user.getDist());
      prep.setDouble(4, user.getRating());
      prep.setString(6, user.getCategory());
      Suggestion[] s = user.getSuggestions();
      prep.setString(7, s[0].toString());
      prep.setString(8, s[1].toString());
      prep.setString(9, s[2].toString());
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
            + "category = ?, s1 = ?, s2 = ?, s3 = ? WHERE "
            + "(user_id = ? AND event_id = ?);")) {
      prep.setInt(1, user.getPrice());
      prep.setDouble(2, user.getRating());
      prep.setDouble(3, user.getDist());
      prep.setString(4, user.getCategory());
      prep.setLong(8, user.getId());
      prep.setLong(9, event);
      Suggestion[] s = user.getSuggestions();
      prep.setString(5, s[0].toString());
      prep.setString(6, s[1].toString());
      prep.setString(7, s[2].toString());
      prep.execute();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  /**
   * Updates the values of an event in the database.
   *
   * @param event
   *          the event to update.
   */
  public static void updateEvent(Event event) {
    Long id = event.getId();
    String name = event.getName();
    List<Double> coords = event.getLocation();
    String date = event.getDate();
    String time = event.getTime();
    try (PreparedStatement prep = conn.prepareStatement(
        "UPDATE events SET name = ?, latitude = ?, longitude = ?, "
            + "date = ?, time = ?, s1 = ? WHERE id = ?")) {
      prep.setLong(7, id);
      prep.setString(1, name);
      prep.setDouble(2, coords.get(0));
      prep.setDouble(3, coords.get(1));
      prep.setString(4, date);
      prep.setString(5, time);
      List<Suggestion> s = event.getSuggestions();
      prep.setString(6, Suggestion.suggToString(s));

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
            + "'name' TEXT);")) {
      prep.execute();
    } catch (SQLException e) {
      System.out.println(e);
    }

    try (PreparedStatement prep = conn
        .prepareStatement("CREATE TABLE IF NOT EXISTS 'events'"
            + "('id' INTEGER, 'name' TEXT, 'latitude' INTEGER, "
            + "'longitude' INTEGER, 'date' TEXT, 'time' TEXT, 's1'"
            + " TEXT);")) {
      prep.execute();
    } catch (SQLException e) {
      System.out.println();
    }

    try (PreparedStatement prep = conn
        .prepareStatement("CREATE TABLE IF NOT EXISTS 'events_users'"
            + "('event_id' INTEGER, 'user_id' INTEGER, 'price' INTEGER, "
            + "'rating' INTEGER, 'distance' INTEGER, category TEXT,'s1' "
            + "TEXT, 's2' TEXT, 's3' TEXT);")) {
      prep.execute();
    } catch (SQLException e) {
      System.out.println(e);
    }

    try (PreparedStatement prep = conn.prepareStatement(
        "CREATE TABLE IF NOT EXISTS 'suggestions'('id' TEXT, 'suggestion' TEXT);")) {
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

    try (PreparedStatement prep = conn
        .prepareStatement("DROP TABLE IF EXISTS suggestions")) {
      prep.execute();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  /**
   * deletes an event from the events and events_users tables.
   *
   * @param event
   *          the id of the event to delete.
   */
  public void deleteEvent(Long event) {
    try (PreparedStatement prep = conn
        .prepareStatement("DELETE FROM events WHERE id = ?;")) {
      prep.setLong(1, event);
      prep.execute();
    } catch (SQLException e) {
      System.out.println(e);
    }

    try (PreparedStatement prep = conn
        .prepareStatement("DELETE FROM events_users WHERE event_id = ?;")) {
      prep.setLong(1, event);
      prep.execute();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  /**
   * Gets the connection for this database for testing purposes.
   *
   * @return the connection of this database.
   */
  public Connection getConn() {
    return W2MDatabase.conn;
  }

  public static void addSuggestion(Suggestion s) {
    try (PreparedStatement prep = conn
        .prepareStatement("INSERT INTO suggestions VALUES(?,?);")) {
      prep.setString(1, s.getId());
      prep.setString(2, s.toString());
      prep.execute();

    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  public static Suggestion getSuggestion(String id) {
    Suggestion ret = null;
    try (PreparedStatement prep = conn
        .prepareStatement("SELECT suggestion FROM suggestions WHERE id=?;")) {
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          ret = Suggestion.toSugg(rs.getString(1));
        }
      }
    } catch (SQLException e) {
      System.out.println(e);
    }
    return ret;
  }

}
