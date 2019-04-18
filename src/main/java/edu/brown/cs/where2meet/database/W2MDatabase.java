package edu.brown.cs.where2meet.database;

import java.io.PrintWriter;
import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class W2MDatabase {
  
  private MongoClient mongoClient;
  private DB eventDatabase;
  private PrintWriter pw;
  
  public W2MDatabase() {
    pw = new PrintWriter(System.out);
    try {
    mongoClient = new MongoClient();
    eventDatabase = mongoClient.getDB("W2MDatabase");
    }catch(UnknownHostException e){
      pw.println("ERROR: cannot connect to mongo");
      
    }
  }
  
  public DBCollection getCollection(String name) {
     return eventDatabase.getCollection(name);
  }
  
  public void addEvent(String id, String name) {
    
  }

}
