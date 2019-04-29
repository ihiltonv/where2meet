package edu.brown.cs.where2meet.main;

import edu.brown.cs.where2meet.database.EventCacheLoader;
import edu.brown.cs.where2meet.database.UserCacheLoader;
import edu.brown.cs.where2meet.database.W2MDatabase;
import edu.brown.cs.where2meet.foursquare.FSConnection;

public class W2MUniverse {

  W2MDatabase wmd;
  FSConnection fsc;



  public W2MUniverse() {
    String db = "data/w2m.sqlite3";
    this.fsc = new FSConnection();
    this.wmd = new W2MDatabase(db);
    this.wmd.createdb();
  }


}
