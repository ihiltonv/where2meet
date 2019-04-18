package edu.brown.cs.where2meet.database;

import java.io.PrintWriter;
import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

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
    
  }
  
  public void addUser(User user) {
    DBCollection userColl = eventDatabase.getCollection("users");
    DBObject person = new BasicDBObject("id", user.getId()).append("name", user.getName());
    userColl.insert(person);
  }
  
  public User getUser(String id) {
    DBCollection userColl = eventDatabase.getCollection("users");
    DBObject query = new BasicDBObject("id", id);
    DBCursor cursor = userColl.find(query);
    String name = (String)cursor.one().get("name");
    return new User(id,name);
  }

}
