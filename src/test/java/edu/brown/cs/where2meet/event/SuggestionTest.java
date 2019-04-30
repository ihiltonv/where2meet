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

    test = new Suggestion(1, 1, 1.0, "loc", "url", "cat", "photo", "ven");
    assertNotNull(test);
  }

  @Test
  public void testSetAndGetPrice() {
    Suggestion test = new Suggestion(1, 1, 1.0, "loc", "url", "cat", "photo",
        "ven");
    assertEquals(test.getPrice(), 1);
    test.setPrice(2);
    assertEquals(test.getPrice(), 2);
  }

  @Test
  public void testSetAndGetVotes() {
    Suggestion test = new Suggestion(1, 1, 1.0, "loc", "url", "cat", "photo",
        "ven");
    assertEquals(test.getVotes(), 1);
    test.setVotes(2);
    assertEquals(test.getVotes(), 2);
  }

  @Test
  public void testSetAndGetRating() {
    Suggestion test = new Suggestion(1, 1, 1.0, "loc", "url", "cat", "photo",
        "ven");
    assert test.getRating() == 1.0;
    test.setRating(2);
    assert test.getRating() == 2.0;
  }

  @Test
  public void testSetAndGetLocation() {
    Suggestion test = new Suggestion(1, 1, 1.0, "loc", "url", "cat", "photo",
        "ven");
    assertEquals(test.getLocation(), "loc");
    test.setLocation("loc2");
    assertEquals(test.getLocation(), "loc2");
  }

  @Test
  public void testSetAndGetUrl() {
    Suggestion test = new Suggestion(1, 1, 1.0, "loc", "url", "cat", "photo",
        "ven");
    assertEquals(test.getUrl(), "url");
    test.setUrl("url2");
    assertEquals(test.getUrl(), "url2");
  }

  @Test
  public void testSetAndGetCategory() {
    Suggestion test = new Suggestion(1, 1, 1.0, "loc", "url", "cat", "photo",
        "ven");
    assertEquals(test.getCategory(), "cat");
    test.setCategory("cat2");
    assertEquals(test.getCategory(), "cat2");
  }

  @Test
  public void testSetAndGetVenue() {
    Suggestion test = new Suggestion(1, 1, 1.0, "loc", "url", "cat", "photo",
        "ven");
    assertEquals(test.getVenue(), "ven");
    test.setVenue("ven2");
    assertEquals(test.getVenue(), "ven2");
  }

  @Test
  public void testSetAndGetPhoto() {
    Suggestion test = new Suggestion(1, 1, 1.0, "loc", "url", "cat", "photo",
        "ven");
    assertEquals(test.getPhoto(), "photo");
    test.setPhoto("photo2");
    assertEquals(test.getPhoto(), "photo2");
  }

  @Test
  public void testToString() {
    Suggestion test = new Suggestion(1, 1, 1.0, "loc", "url", "cat", "photo",
        "ven");
    String tString = test.toString();
    assertEquals(tString,
        "{\"price\":1,\"votes\":1,\"rating\":1.0,\"location\":\"loc\",\"url\":\"url\","
            + "\"category\":\"cat\",\"photo\":\"photo\",\"venue\":\"ven\"}");
  }

  @Test
  public void testGetAsJsonObject() {
    Suggestion test = new Suggestion(1, 1, 1.0, "loc", "url", "cat", "photo",
        "ven");
    JsonObject obj = new JsonObject();
    obj.addProperty("price", test.getPrice());
    obj.addProperty("votes", test.getVotes());
    obj.addProperty("rating", test.getRating());
    obj.addProperty("location", test.getLocation());
    obj.addProperty("url", test.getUrl());
    obj.addProperty("category", test.getCategory());
    obj.addProperty("photo", test.getPhoto());
    obj.addProperty("venue", test.getVenue());
    assertEquals(obj, test.getAsJsonObject());
  }

  @Test
  public void testToSugg() {
    String s = "{\"price\":1,\"votes\":1,\"rating\":1.0,\"location\":\"loc\",\"url\":\"url\","
        + "\"category\":\"cat\",\"photo\":\"photo\",\"venue\":\"ven\"}";
    Suggestion sugg = Suggestion.toSugg(s);
    Suggestion test = new Suggestion(1, 1, 1.0, "loc", "url", "cat", "photo",
        "ven");
    assertEquals(sugg, test);
  }

  @Test
  public void testSuggToString() {
    List<Suggestion> suggs = new ArrayList<>();
    suggs.add(new Suggestion(1, 1, 1.0, "loc", "url", "cat", "photo", "ven"));
    suggs.add(
        new Suggestion(2, 2, 2.0, "loc2", "url2", "cat2", "photo2", "ven2"));
    String res = Suggestion.suggToString(suggs);
    String comp = "[\"{\\\"price\\\":1,\\\"votes\\\":1,\\\"rating\\\":1.0,\\\"location\\\":\\\"loc\\\",\\\"url\\\":\\\"url\\\","
        + "\\\"category\\\":\\\"cat\\\",\\\"photo\\\":\\\"photo\\\",\\\"venue\\\":\\\"ven\\\"}\","
        + "\"{\\\"price\\\":2,\\\"votes\\\":2,\\\"rating\\\":2.0,\\\"location\\\":\\\"loc2\\\",\\\"url\\\":\\\"url2\\\","
        + "\\\"category\\\":\\\"cat2\\\",\\\"photo\\\":\\\"photo2\\\",\\\"venue\\\":\\\"ven2\\\"}\"]";
    assertEquals(res, comp);
  }

}
