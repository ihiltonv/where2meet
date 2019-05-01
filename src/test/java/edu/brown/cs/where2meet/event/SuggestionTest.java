package edu.brown.cs.where2meet.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.JsonObject;

public class SuggestionTest {

  @Test
  public void testConstructor() {
    Suggestion test = new Suggestion();
    assertNotNull(test);

    test = new Suggestion("id", 1, 1, 1.0, "url", "cat", "photo", "ven");
    test.setLatLonLoc(1.0, 1.0, "loc");
    assertNotNull(test);
  }

  @Test
  public void testSetAndGetPrice() {
    Suggestion test = new Suggestion("id", 1, 1, 1.0, "url", "cat", "photo",
        "ven");
    test.setLatLonLoc(1.0, 1.0, "loc");

    assertEquals(test.getPrice(), 1);
    test.setPrice(2);
    assertEquals(test.getPrice(), 2);
  }

  @Test
  public void testSetAndGetVotes() {
    Suggestion test = new Suggestion("id", 1, 1, 1.0, "url", "cat", "photo",
        "ven");
    test.setLatLonLoc(1.0, 1.0, "loc");
    assertEquals(test.getVotes(), 1);
    test.setVotes(2);
    assertEquals(test.getVotes(), 2);
  }

  @Test
  public void testSetAndGetRating() {
    Suggestion test = new Suggestion("id", 1, 1, 1.0, "url", "cat", "photo",
        "ven");
    test.setLatLonLoc(1.0, 1.0, "loc");
    assert test.getRating() == 1.0;
    test.setRating(2);
    assert test.getRating() == 2.0;
  }

  @Test
  public void testSetAndGetLocation() {
    Suggestion test = new Suggestion("id", 1, 1, 1.0, "url", "cat", "photo",
        "ven");
    test.setLatLonLoc(1.0, 1.0, "loc");
    assertEquals(test.getLocation(), "loc");
    test.setLocation("loc2");
    assertEquals(test.getLocation(), "loc2");
  }

  @Test
  public void testSetAndGetUrl() {
    Suggestion test = new Suggestion("id", 1, 1, 1.0, "url", "cat", "photo",
        "ven");
    test.setLatLonLoc(1.0, 1.0, "loc");
    assertEquals(test.getUrl(), "url");
    test.setUrl("url2");
    assertEquals(test.getUrl(), "url2");
  }

  @Test
  public void testSetAndGetCategory() {
    Suggestion test = new Suggestion("id", 1, 1, 1.0, "url", "cat", "photo",
        "ven");
    test.setLatLonLoc(1.0, 1.0, "loc");
    assertEquals(test.getCategory(), "cat");
    test.setCategory("cat2");
    assertEquals(test.getCategory(), "cat2");
  }

  @Test
  public void testSetAndGetVenue() {
    Suggestion test = new Suggestion("id", 1, 1, 1.0, "url", "cat", "photo",
        "ven");
    test.setLatLonLoc(1.0, 1.0, "loc");
    assertEquals(test.getVenue(), "ven");
    test.setVenue("ven2");
    assertEquals(test.getVenue(), "ven2");
  }

  @Test
  public void testSetAndGetPhoto() {
    Suggestion test = new Suggestion("id", 1, 1, 1.0, "url", "cat", "photo",
        "ven");
    test.setLatLonLoc(1.0, 1.0, "loc");
    assertEquals(test.getPhoto(), "photo");
    test.setPhoto("photo2");
    assertEquals(test.getPhoto(), "photo2");
  }

  @Test
  public void testToString() {
    Suggestion test = new Suggestion("id", 1, 1, 1.0, "url", "cat", "photo",
        "ven");
    test.setLatLonLoc(1.0, 1.0, "loc");
    String tString = test.toString();
    assertEquals(tString,
        "{\"id\":\"id\",\"price\":1,\"votes\":1,\"rating\":1.0,\"location\":\"loc\",\"url\":\"url\","
            + "\"category\":\"cat\",\"photo\":\"photo\",\"venue\":\"ven\",\"lat\":1.0,\"lon\":1.0}");
  }

  @Test
  public void testGetAsJsonObject() {
    Suggestion test = new Suggestion("id", 1, 1, 1.0, "url", "cat", "photo",
        "ven");
    test.setLatLonLoc(1.0, 1.0, "loc");
    JsonObject obj = new JsonObject();
    obj.addProperty("id", "id");
    obj.addProperty("price", test.getPrice());
    obj.addProperty("votes", test.getVotes());
    obj.addProperty("rating", test.getRating());
    obj.addProperty("location", test.getLocation());
    obj.addProperty("url", test.getUrl());
    obj.addProperty("category", test.getCategory());
    obj.addProperty("photo", test.getPhoto());
    obj.addProperty("venue", test.getVenue());
    obj.addProperty("lat", 1.0);
    obj.addProperty("lon", 1.0);
    assertEquals(obj, test.getAsJsonObject());
  }

  @Test
  public void testToSugg() {
    String s = "{\"id\":\"id\",\"price\":1,\"votes\":1,\"rating\":1.0,\"location\":\"loc\",\"url\":\"url\","
        + "\"category\":\"cat\",\"photo\":\"photo\",\"venue\":\"ven\",\"lat\":1.0,\"lon\":1.0}";
    Suggestion sugg = Suggestion.toSugg(s);
    Suggestion test = new Suggestion("id", 1, 1, 1.0, "url", "cat", "photo",
        "ven");
    test.setLatLonLoc(1.0, 1.0, "loc");
    assertEquals(sugg, test);
  }

  @Test
  public void testSuggToString() {
    List<Suggestion> suggs = new ArrayList<>();
    Suggestion test1 = new Suggestion("id", 1, 1, 1.0, "url", "cat", "photo",
        "ven");
    test1.setLatLonLoc(1.0, 1.0, "loc");
    Suggestion test2 = new Suggestion("id2", 2, 2, 2.0, "url2", "cat2",
        "photo2", "ven2");
    test2.setLatLonLoc(2.0, 2.0, "loc2");

    suggs.add(test1);
    suggs.add(test2);
    String res = Suggestion.suggToString(suggs);
    String comp = "[\"{\\\"id\\\":\\\"id\\\",\\\"price\\\":1,\\\"votes\\\":1,\\\"rating\\\":1.0,\\\"location\\\":\\\"loc\\\",\\\"url\\\":\\\"url\\\","
        + "\\\"category\\\":\\\"cat\\\",\\\"photo\\\":\\\"photo\\\",\\\"venue\\\":\\\"ven\\\",\\\"lat\\\":1.0,\\\"lon\\\":1.0}\","
        + "\"{\\\"id\\\":\\\"id2\\\",\\\"price\\\":2,\\\"votes\\\":2,\\\"rating\\\":2.0,\\\"location\\\":\\\"loc2\\\",\\\"url\\\":\\\"url2\\\","
        + "\\\"category\\\":\\\"cat2\\\",\\\"photo\\\":\\\"photo2\\\",\\\"venue\\\":\\\"ven2\\\",\\\"lat\\\":2.0,\\\"lon\\\":2.0}\"]";
    assertEquals(res, comp);
  }

}
