package edu.brown.cs.where2meet.database;

import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

public class W2MDatabase {
  
  private MongoClient mongoClient;
  private DB eventDatabase;
  private PrintWriter pw;
  
  public W2MDatabase(String dbname) {
    pw = new PrintWriter(System.out);
    try {
    mongoClient = new MongoClient();
    eventDatabase = mongoClient.getDB(dbname);
    }catch(UnknownHostException e){
      pw.println("ERROR: cannot connect to mongo");
      
    }
  }
  
  public void addEvent(Event event) {
    DBCollection eventColl = eventDatabase.getCollection("events");
    eventColl.drop();
    
    Set<User> users = event.getUsers();
    BasicDBList dbusers = new BasicDBList();
    for(User u: users) {
      dbusers.add(u.getId());
    }
    String userarray = new Gson().toJson(dbusers);
    //System.out.println(dbusers);
    DBObject query = new BasicDBObject("_id",event.getId()).append("users",userarray).append("name", event.getName());
    try {
    eventColl.insert(query);
    }catch(MongoException m){
      
    }
  }
  
  public Event getEvent(String id) {
    DBCollection eventColl = eventDatabase.getCollection("events");
    DBObject query = new BasicDBObject("_id", id);
    DBCursor cursor = eventColl.find(query);
    String name = (String)cursor.one().get("name");
    String dbusers = (String)cursor.one().get("users");
    JsonElement jusers = new JsonParser().parse(dbusers);
    JsonArray jarray = jusers.getAsJsonArray();
    System.out.println(dbusers);
    
    //builds the list of users
    Set<User> users = new HashSet<>();
    for(JsonElement s: jarray) {
      System.out.println(s.toString());
      String sId = s.toString();
      String actId = sId.split("\"")[1];
      System.out.println(actId);
      users.add(getUser(actId));
    }
    
    return new Event(id,name,users);
  }
  
  
  
  public void addUser(User user) {
    DBCollection userColl = eventDatabase.getCollection("users");
    userColl.drop();
    DBObject person = new BasicDBObject("_id", user.getId()).append("name", user.getName());
    try{
    userColl.insert(person);
    }catch(MongoException m) {
      
    }
  }
  
  public User getUser(String id) {
    DBCollection userColl = eventDatabase.getCollection("users");
    DBObject query = new BasicDBObject("_id", id);
    DBCursor cursor = userColl.find(query);
    String name = (String)cursor.one().get("name");
    return new User(id,name);
  }

}
