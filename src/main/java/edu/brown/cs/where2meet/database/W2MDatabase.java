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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
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
  
  /**
   * Constructor for the W2MDatabase. 
   * 
   * @param dbname the name of the database to use.
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
    
    }catch(UnknownHostException e){
      pw.println("ERROR: cannot connect to mongo");
      
    }
  }
  
  /**
   * Adds an event to the database
   * 
   * @param event the event whose data is to be added.
   */
  public void addEvent(Event event) {
    DBCollection eventColl = eventDatabase.getCollection("events");
    
    Set<String> users = event.getUsers();
    BasicDBList dbusers = new BasicDBList();
    for(String u: users) {
      dbusers.add(u);
    }
    String userarray = new Gson().toJson(dbusers);
    DBObject query = new BasicDBObject("_id",event.getId()).append("users",userarray).append("name", event.getName());
    try {
    eventColl.insert(query);
    }catch(MongoException m){
      
    }
  }
  
  /**
   * Gets an event from the database
   * 
   * @param id the id of the event to be retrieved
   * @return an event object holding the data from the database.
   */
  public static Event getEvent(String id) {
    try {
      return eventCache.get(id);
    }catch(ExecutionException e) {
      pw.print("ERROR: could not retrieve event");
      return null;
    }
  }
  
  /**
   * Builds a set of users from a String representing a JsonArray.
   * 
   * @param dbusers the string of the user list as retrieved from the database
   * @return a set of users from the user collection.
   */
  private static Set<String> buildUserSet(String dbusers){
    JsonElement jusers = new JsonParser().parse(dbusers);
    JsonArray jarray = jusers.getAsJsonArray();
    
    //builds the list of users
    Set<String> users = new HashSet<>();
    for(JsonElement s: jarray){
      String sId = s.toString();
      String actId = sId.split("\"")[1];
      users.add(actId);
    }
    return users;
  }
  
  
  /**
   * Adds a user to the user collection in the database
   * 
   * @param user the user whose data is added to the collection
   */
  public static void addUser(User user) {
    DBCollection userColl = eventDatabase.getCollection("users");
    Set<String> events = user.getEvents();
    BasicDBList dbevents = new BasicDBList();
    for(String e: events) {
      dbevents.add(e);
    }
    String eventarray = new Gson().toJson(dbevents);
    
    DBObject person = new BasicDBObject("_id", user.getId()).append("name", user.getName()).append("events",eventarray);
    try{
      userColl.insert(person);
    }catch(MongoException m) {
      pw.println("ERROR: could not insert user");
    }
  }
  
  /**
   * Gets a user from the database
   * 
   * @param id the id of the user to be retrieved.
   * @return a User object with the data retrieved from the database.
   */
  public static User getUser(String id) {
    try {
      return userCache.get(id);
    }catch(ExecutionException e) {
      pw.print("ERROR: could not retrieve user");
      return null;
    }
  }
  
  private static Set<String> buildEventSet(String dbevents){
    JsonElement jevents = new JsonParser().parse(dbevents);
    JsonArray jarray = jevents.getAsJsonArray();
    //builds the list of users
    Set<String> events = new HashSet<>();
    for(JsonElement s: jarray){
      String sId = s.toString();
      String actId = sId.split("\"")[1];
      System.out.println("event id: " + actId);
      events.add(actId);
    }
    return events;
  }
  
  /**
   * gets a collection from the database.
   * 
   * @param name the name of the collection to get
   * @return a collection from the database. 
   */
  public DBCollection getCollection(String name) {
    return eventDatabase.getCollection(name);
  }
  
  public static Event loadEvent(String id) {
    DBCollection eventColl = eventDatabase.getCollection("events");
    DBObject query = new BasicDBObject("_id", id);
    DBCursor cursor = eventColl.find(query);
    String name = (String)cursor.one().get("name");
    String dbusers = (String)cursor.one().get("users");
    Set<String> users = buildUserSet(dbusers);
    
    return new Event(id,name,users);
  }
  
  public static User loadUser(String id) {
    DBCollection userColl = eventDatabase.getCollection("users");
    DBObject query = new BasicDBObject("_id", id);
    DBCursor cursor = userColl.find(query);
    String name = (String)cursor.one().get("name");
    String dbevents = (String)cursor.one().get("events");
    Set<String> events = buildEventSet(dbevents);
    return new User(id,name,events);
  }
  
  public static void updateUser(User u) {
    DBCollection userColl = eventDatabase.getCollection("users");
    DBObject query = new BasicDBObject("_id", u.getId());
    
    Set<String> events = u.getEvents();
    BasicDBList dbevents = new BasicDBList();
    for(String e: events) {
      dbevents.add(e);
    }
    String eventarray = new Gson().toJson(dbevents);
    
    DBObject person = new BasicDBObject("_id", u.getId()).append("name", u.getName()).append("events",eventarray);
    userColl.update(query, person);
  }

}
