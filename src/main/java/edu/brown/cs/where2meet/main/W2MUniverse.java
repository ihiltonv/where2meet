package edu.brown.cs.where2meet.main;

import edu.brown.cs.where2meet.database.W2MDatabase;
import edu.brown.cs.where2meet.networking.YelpConnection;

public class W2MUniverse {

  W2MDatabase wmd;
  YelpConnection fsc;


  public W2MUniverse() {
    String db = "data/w2m.sqlite3";
    this.fsc = new YelpConnection();
    this.wmd = new W2MDatabase(db);
    this.wmd.createdb();
  }


}
