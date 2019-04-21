package edu.brown.cs.where2meet.database;

import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import edu.brown.cs.where2meet.event.Event;
import edu.brown.cs.where2meet.event.User;

/**
 * A class to facilitate interacting with the mongodb database.
 *
 */
public class W2MDatabase {

  private MongoClient mongoClient;
  private static DB eventDatabase;
  private static LoadingCache<String, Event> eventCache;
  private static LoadingCache<String, User> userCache;
  private static final int MAX_CACHE = 1000;
  private static PrintWriter pw;
  private static final Gson GSON = new Gson();

  /**
   * Constructor for the W2MDatabase.
   *
   * @param dbname
   *          the name of the database to use.
   */
  public W2MDatabase(String dbname) {
    pw = new PrintWriter(System.out);
    try {
      mongoClient = new MongoClient();
      eventDatabase = mongoClient.getDB(dbname);
      eventCache = CacheBuilder.newBuilder().maximumSize(MAX_CACHE)
          .build(new EventCacheLoader());
      userCache = CacheBuilder.newBuilder().maximumSize(MAX_CACHE)
          .build(new UserCacheLoader());

    } catch (UnknownHostException e) {
      pw.println("ERROR: cannot connect to mongo");

    }
  }

  /**
   * Adds an event to the database.
   *
   * @param event
   *          the event whose data is to be added.
   */
  public void addEvent(Event event) {
    DBCollection eventColl = eventDatabase.getCollection("events");
    String userarray = setToString(event.getUsers());
    String coordsarray = setToString(event.getLocation());
    DBObject query = new BasicDBObject("_id", event.getId())
        .append("users", userarray).append("name", event.getName())
        .append("location", coordsarray);
    try {
      eventColl.insert(query);
    } catch (MongoException m) {

    }
  }

  /**
   * Gets an event from the database.
   *
   * @param id
   *          the id of the event to be retrieved
   * @return an event object holding the data from the database.
   */
  public static Event getEvent(String id) {
    try {
      return eventCache.get(id);
    } catch (ExecutionException e) {
      pw.print("ERROR: could not retrieve event");
      return null;
    }
  }

  /**
   * Converts an iterable object to a JSON string of its elements.
   *
   * @param i
   *          the iterable object
   * @return a string representing the elements
   */
  private static String setToString(Iterable i) {
    BasicDBList dblist = new BasicDBList();
    Iterator iter = i.iterator();
    while (iter.hasNext()) {
      dblist.add(iter.next());
    }
    return GSON.toJson(dblist);
  }

  /**
   * Adds a user to the user collection in the database.
   *
   * @param user
   *          the user whose data is added to the collection
   */
  public static void addUser(User user) {
    DBCollection userColl = eventDatabase.getCollection("users");

    String eventarray = setToString(user.getEvents());
    String coords = setToString(user.getLocation());
    DBObject person = new BasicDBObject("_id", user.getId())
        .append("name", user.getName()).append("events", eventarray)
        .append("location", coords);
    try {
      userColl.insert(person);
    } catch (MongoException m) {
      pw.println("ERROR: could not insert user");
    }
  }

  /**
   * Gets a user from the database.
   *
   * @param id
   *          the id of the user to be retrieved.
   * @return a User object with the data retrieved from the database.
   */
  public static User getUser(String id) {
    try {
      return userCache.get(id);
    } catch (ExecutionException e) {
      pw.print("ERROR: could not retrieve user");
      return null;
    }
  }

  /**
   * Builds a set from a string of its elements.
   *
   * @param dbset
   *          the string to turn into a set
   * @return a set with the elements represented in the string.
   */
  private static Set<String> buildSubSet(String dbset) {
    JsonElement jevents = new JsonParser().parse(dbset);
    JsonArray jarray = jevents.getAsJsonArray();
    // builds the list of users
    Set<String> set = new HashSet<>();
    for (JsonElement s : jarray) {
      String sId = s.toString();
      String actId = sId.split("\"")[1];
      System.out.println("event id: " + actId);
      set.add(actId);
    }
    return set;
  }

  /**
   * gets a collection from the database.
   *
   * @param name
   *          the name of the collection to get
   * @return a collection from the database.
   */
  public DBCollection getCollection(String name) {
    return eventDatabase.getCollection(name);
  }

  /**
   * Loads an event to be stored in the cache.
   *
   * @param id
   *          the id of the event to be stored.
   * @return the event to be stored.
   */
  public static Event loadEvent(String id) {
    DBCollection eventColl = eventDatabase.getCollection("events");
    DBObject query = new BasicDBObject("_id", id);
    DBCursor cursor = eventColl.find(query);
    String name = (String) cursor.one().get("name");
    String dbusers = (String) cursor.one().get("users");
    String dbcoords = (String) cursor.one().get("location");
    Set<String> users = buildSubSet(dbusers);
    List<Double> coords = buildCoords(dbcoords);
    return new Event(id, name, users, coords);
  }

  /**
   * Builds a list of coordinates from a string.
   *
   * @param coords
   *          The string with the coordinates.
   * @return A list of coordinates.
   */
  private static List<Double> buildCoords(String coords) {
    List<Double> loc = new ArrayList<>();
    System.out.println("coords " + coords);
    JsonElement jcoords = new JsonParser().parse(coords);
    JsonArray jarray = jcoords.getAsJsonArray();
    // builds the list of users

    for (JsonElement s : jarray) {
      String coord = s.toString();

      System.out.println("actCoords: " + coord);
      try {
        loc.add(Double.valueOf(coord));
      } catch (NumberFormatException n) {
        System.out.println(n);
      }
    }
    return loc;
  }

  /**
   * Loads a user to be stored in the cache.
   *
   * @param id
   *          the id of the user to be stored
   * @return the user to be stored.
   */
  public static User loadUser(String id) {
    DBCollection userColl = eventDatabase.getCollection("users");
    DBObject query = new BasicDBObject("_id", id);
    DBCursor cursor = userColl.find(query);
    String name = (String) cursor.one().get("name");
    String dbevents = (String) cursor.one().get("events");
    String dbcoords = (String) cursor.one().get("location");
    Set<String> events = buildSubSet(dbevents);
    System.out.println("found events");
    System.out.println("dbcoords: " + dbcoords);
    List<Double> coords = buildCoords(dbcoords);
    System.out.println("done finding things");
    return new User(id, name, events, coords);
  }

  /**
   * Updates the data for a user in the database.
   *
   * @param u
   *          user with updated information.
   */
  public static void updateUser(User u) {
    DBCollection userColl = eventDatabase.getCollection("users");
    DBObject query = new BasicDBObject("_id", u.getId());

    Set<String> events = u.getEvents();
    BasicDBList dbevents = new BasicDBList();
    for (String e : events) {
      dbevents.add(e);
    }
    String eventarray = new Gson().toJson(dbevents);

    DBObject person = new BasicDBObject("_id", u.getId())
        .append("name", u.getName()).append("events", eventarray);
    userColl.update(query, person, true, true);
  }

}
