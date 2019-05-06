package edu.brown.cs.where2meet.database;

import edu.brown.cs.where2meet.event.Suggestion;
import edu.brown.cs.where2meet.networking.YelpConnection;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class YelpConnectionTest {

  @Test
  public void testConnect() {
    List<Suggestion> results = YelpConnection
        .exploreQuery(41.826664, -71.404852, Arrays.asList(""), 500);
    assertNotNull(results);
    //testing that some results are returned
    assertTrue(results.size() > 0);
  }
}
