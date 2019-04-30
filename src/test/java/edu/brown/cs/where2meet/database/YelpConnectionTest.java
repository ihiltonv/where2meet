package edu.brown.cs.where2meet.database;

import edu.brown.cs.where2meet.networking.YelpConnection;

import java.util.Arrays;

public class YelpConnectionTest {

  //@Test
  public void testConnect() {
    YelpConnection
        .exploreQuery(41.826664, -71.404852, Arrays.asList("coffee", "food"),
            500).forEach(x -> System.out.println(x.getVenue()));
  }
}
