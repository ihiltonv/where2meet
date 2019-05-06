package edu.brown.cs.where2meet.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.cs.where2meet.event.Event;
import edu.brown.cs.where2meet.event.Suggestion;
import edu.brown.cs.where2meet.event.User;

public class W2MDatabaseTest {

  public static W2MDatabase db;

  @BeforeClass
  public static void SetUpDb() {
    db = new W2MDatabase("data/testdb1.sqlite3");
    db.createdb();
  }

  @AfterClass
  public static void dropDb() {
    db.cleardb();
  }

  @Test
  public void testConstructor() {
    System.out.println("TestConstructor\n");
    db = new W2MDatabase("data/testdb2.sqlite3");
    assertNotNull(db);
  }

  @Test
  public void testAddUser() {
    System.out.println("TestAddUser\n");

    User test = new User("/n/1");
    W2MDatabase.addUser(test);
    User ret = W2MDatabase.getUser(test.getId());
    assertEquals(ret.getName(), "/n/1");
    assertEquals(ret.getId(), test.getId());

  }

  @Test
  public void testAddEvent() {
    System.out.println("TestAddEvent\n");

    User uTest1 = new User("/n/2");
    User uTest2 = new User("/n/3");

    while (uTest2.getId().equals(uTest1.getId())) {
      uTest2 = new User("/n/3");
    }

    Set<Long> userList = new HashSet<>();

    userList.add(uTest1.getId());
    userList.add(uTest2.getId());

    List<Double> ecoords = new ArrayList<>();
    ecoords.add(1.0);
    ecoords.add(2.0);
    Event test = new Event("/n/4", userList, ecoords, "date", "time");
    Suggestion s1 = new Suggestion();
    s1.setLatLonLoc(1.0, 1.0, "loc");
    List<Suggestion> suggList = new ArrayList<>();
    suggList.add(s1);
    test.setSuggestions(suggList);
    Long eid = test.getId();
    W2MDatabase.addEvent(test);

    Event ret = W2MDatabase.getEvent(eid);
    assertEquals(ret.getName(), "/n/4");
    assertEquals(ret.getId(), eid);
    Set<Long> users = ret.getUsers();

    assertEquals(users.size(), 2);
    assertTrue(users.contains(uTest2.getId()));
    assertTrue(users.contains(uTest1.getId()));
    List<Double> retCoords = ret.getLocation();
    assert retCoords.get(0) == 1.0;
    assert retCoords.get(1) == 2.0;
  }

  @Test
  public void testAddUserWithEvent() {
    System.out.println("TestAddUserWithEvent\n");
    List<Double> coords = new ArrayList<>();
    coords.add(1.0);
    coords.add(2.0);
    User uTest = new User("/n/5");
    Long uid = uTest.getId();
    W2MDatabase.addUser(uTest);

    Event eTest1 = new Event("/n/6", coords, "date1", "time1");
    Event eTest2 = new Event("/n/7", coords, "date2", "time2");
    while (eTest2.getId().equals(eTest1.getId())) {
      eTest2 = new Event("/n/7", coords, "date2", "time2");
    }

    List<Suggestion> suggList = new ArrayList<>();
    eTest1.setSuggestions(suggList);
    eTest2.setSuggestions(suggList);

    Long eid1 = eTest1.getId();
    Long eid2 = eTest2.getId();
    eTest1.addUser(uTest.getId());
    eTest2.addUser(uTest.getId());
    W2MDatabase.addEvent(eTest1);
    W2MDatabase.addEvent(eTest2);
    W2MDatabase.addUser(uTest);

    User ret = W2MDatabase.getUser(uid);
    assertEquals(ret.getName(), "/n/5");
    assertEquals(ret.getId(), uid);
    Set<Long> events = ret.getEvents();
    Set<Long> userSet = new HashSet<>();
    userSet.add(uTest.getId());
    assertEquals(events.size(), 2);
    assertTrue(events.contains(eid1));
    assertTrue(events.contains(eid2));
    Event event1 = W2MDatabase.getEvent(eid1);
    Event event2 = W2MDatabase.getEvent(eid2);
    assertEquals(event1.getName(), "/n/6");
    assertEquals(event2.getName(), "/n/7");
    Set<Long> eusers1 = event1.getUsers();
    Set<Long> eusers2 = event2.getUsers();
    assertEquals(eusers1.size(), 1);
    assertEquals(eusers2.size(), 1);
    assertTrue(eusers1.contains(uid));
    assertTrue(eusers2.contains(uid));
  }

  @Test
  public void testUpdateUser() {
    System.out.println("TestUpdateUser\n");

    List<Double> coords = new ArrayList<>();
    coords.add(1.0);
    coords.add(2.0);
    User uTest1 = new User("/n/1");

    Suggestion s1 = new Suggestion();
    s1.setLatLonLoc(1.0, 1.0, "loc");
    List<Suggestion> suggList = new ArrayList<>();
    suggList.add(s1);

    W2MDatabase.addUser(uTest1);
    Long uid = uTest1.getId();
    Event eTest1 = new Event("/e/1", coords, "date", "time");
    eTest1.setSuggestions(suggList);
    Long eid = eTest1.getId();
    W2MDatabase.addEvent(eTest1);
    eTest1.addUser(uid);
    User ret = W2MDatabase.getUserWithEvent(uid, eid);
    assertEquals(ret.getCategory(), "");
    assertEquals(ret.getPrice(), 1);
    assert ret.getRating() == 5;
    assert ret.getDist() == 1;

    uTest1.setCategory("test");
    uTest1.setDist(2.0);
    uTest1.setPrice(2);
    uTest1.setRating(4);
    W2MDatabase.updateUser(uTest1, eTest1.getId());
    ret = W2MDatabase.getUserWithEvent(uid, eid);

    assertEquals(ret.getCategory(), "test");
    assertEquals(ret.getPrice(), 2);
    assert ret.getRating() == 4;
    assert ret.getDist() == 2.0;
  }

  @Test
  public void testGetUserFromName() {
    System.out.println("TestGetUserFromName\n");

    List<Double> coords = new ArrayList<>();
    coords.add(1.0);
    coords.add(2.0);
    User uTest1 = new User("username");
    Long uid = uTest1.getId();
    Event eTest1 = new Event("/e/1", coords, "date", "time");
    Suggestion s1 = new Suggestion();
    s1.setLatLonLoc(1.0, 1.0, "loc");
    List<Suggestion> suggList = new ArrayList<>();
    suggList.add(s1);

    Long eid = eTest1.getId();

    while (uid.equals(eid)) {
      eTest1 = new Event("/e/1", coords, "date", "time");
      eid = eTest1.getId();
    }
    eTest1.setSuggestions(suggList);
    W2MDatabase.addEvent(eTest1);
    W2MDatabase.addUser(uTest1);
    // W2MDatabase.addEvent(eTest1);

    User id = W2MDatabase.getUserFromName("username", eid);
    assertNull(id);

    eTest1.addUser(uid);
    id = W2MDatabase.getUserFromName("username", eid);
    assertEquals(id, uTest1);

  }

  @Test
  public void testDeleteEvents() {
    System.out.println("TestDeleteEvents\n");
    Connection conn = db.getConn();

    List<Double> coords = new ArrayList<>();
    coords.add(1.0);
    coords.add(2.0);
    Event test1 = new Event("test1", coords, "date", "time");
    Suggestion s1 = new Suggestion();
    s1.setLatLonLoc(1.0, 1.0, "loc");
    List<Suggestion> suggList = new ArrayList<>();
    suggList.add(s1);
    test1.setSuggestions(suggList);

    Long e1 = test1.getId();

    User uTest1 = new User("username");
    Long u = uTest1.getId();
    test1.addUser(u);
    W2MDatabase.addEvent(test1);
    int count = 0;
    try (PreparedStatement prep = conn
        .prepareStatement("SELECT COUNT(*) FROM events WHERE id = ?")) {
      prep.setLong(1, e1);
      try (ResultSet rs = prep.executeQuery()) {
        count = rs.getInt(1);
      }
    } catch (SQLException e) {
      count = 0;
    }
    assertEquals(count, 1);

    try (PreparedStatement prep = conn.prepareStatement(
        "SELECT COUNT(*) FROM events_users WHERE event_id = ?")) {
      prep.setLong(1, e1);
      try (ResultSet rs = prep.executeQuery()) {
        count = rs.getInt(1);
      }
    } catch (SQLException e) {
      count = 0;
    }

    assertEquals(count, 1);

    db.deleteEvent(e1);

    try (PreparedStatement prep = conn
        .prepareStatement("SELECT COUNT(*) FROM events WHERE id = ?")) {
      prep.setLong(1, e1);
      try (ResultSet rs = prep.executeQuery()) {
        count = rs.getInt(1);
      }
    } catch (SQLException e) {
      count = 0;
    }
    assertEquals(count, 0);

    try (PreparedStatement prep = conn.prepareStatement(
        "SELECT COUNT(*) FROM events_users WHERE event_id = ?")) {
      prep.setLong(1, e1);
      try (ResultSet rs = prep.executeQuery()) {
        count = rs.getInt(1);
      }
    } catch (SQLException e) {
      count = 0;
    }

    assertEquals(count, 0);
  }

  @Test
  public void testSuggestionPreservation() {
    System.out.println("testSuggestionPreservation\n");

    List<Double> ecoords = new ArrayList<>();
    ecoords.add(1.0);
    ecoords.add(2.0);
    Event test = new Event("/n/4", ecoords, "date", "time");
    Suggestion s1 = new Suggestion();
    s1.setLatLonLoc(1.0, 1.0, "loc");
    List<Suggestion> suggList = new ArrayList<>();
    // suggList.add(s1);
    test.setSuggestions(suggList);

    Long eid = test.getId();
    W2MDatabase.addEvent(test);
    Event ret = W2MDatabase.getEvent(eid);
    List<Suggestion> sugg = ret.getSuggestions();
    assertEquals(sugg.size(), 0);

    Suggestion s = new Suggestion();
    s.setVenue("A");
    s.setVotes(1);

    test.addSuggestion(s);
    assertEquals(test.getSuggestions().size(), 1);
    W2MDatabase.updateEvent(test);
    ret = W2MDatabase.getEvent(eid);
    sugg = ret.getSuggestions();
    assertEquals(sugg.size(), 1);
    assertEquals(sugg.get(0).getVenue(), "A");

    s = new Suggestion();
    s.setVenue("B");
    s.setVotes(2);

    test.addSuggestion(s);
    assertEquals(test.getSuggestions().size(), 2);
    assertEquals(test.getSuggestions().get(1).getVenue(), "A");
    assertEquals(test.getSuggestions().get(0).getVenue(), "B");

    W2MDatabase.updateEvent(test);
    ret = W2MDatabase.getEvent(eid);
    sugg = ret.getSuggestions();
    assertEquals(sugg.size(), 2);
    assertEquals(sugg.get(0).getVenue(), "B");
    assertEquals(sugg.get(1).getVenue(), "A");

  }

  @Test
  public void testUserExists() {
    System.out.println("TestUserExists\n");

    List<Double> coords = new ArrayList<>();
    coords.add(1.0);
    coords.add(2.0);
    User uTest1 = new User("username");
    Long uid = uTest1.getId();
    Event eTest1 = new Event("/e/1", coords, "date", "time");
    Suggestion s1 = new Suggestion();
    s1.setLatLonLoc(1.0, 1.0, "loc");
    List<Suggestion> suggList = new ArrayList<>();
    suggList.add(s1);

    Long eid = eTest1.getId();

    while (uid.equals(eid)) {
      eTest1 = new Event("/e/1", coords, "date", "time");
      eid = eTest1.getId();
    }
    eTest1.setSuggestions(suggList);
    W2MDatabase.addEvent(eTest1);
    W2MDatabase.addUser(uTest1);

    boolean inDb = W2MDatabase.userExists("username", eid);
    assertFalse(inDb);

    eTest1.addUser(uid);
    inDb = W2MDatabase.userExists("username", eid);
    assertTrue(inDb);

  }

}
