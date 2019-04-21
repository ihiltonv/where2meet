package edu.brown.cs.where2meet.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.mongodb.DBCollection;

import edu.brown.cs.where2meet.event.Event;
import edu.brown.cs.where2meet.event.User;

public class W2MDatabaseTest {

  @Test
  public void testConstructor() {
    System.out.println("TestConstructor\n");
    W2MDatabase db = new W2MDatabase("testdb");
    assertNotNull(db);
  }

  @Test
  public void testAddUser() {
    System.out.println("TestAddUser\n");
    clearCollections();
    List<Double> coords = new ArrayList<>();
    coords.add(1.0);
    coords.add(2.0);
    User test = new User("id", "name", coords);
    W2MDatabase.addUser(test);
    User ret = W2MDatabase.getUser("id");
    assertEquals(ret.getName(), "name");
    assertEquals(ret.getId(), "id");

  }

  @Test
  public void testAddEvent() {
    System.out.println("TestAddEvent\n");
    W2MDatabase db = new W2MDatabase("testdb");
    clearCollections();
    List<Double> coords = new ArrayList<>();
    coords.add(1.0);
    coords.add(2.0);
    User uTest1 = new User("uid1", "uname1", coords);
    User uTest2 = new User("uid2", "uname2", coords);
    Set<String> userList = new HashSet<>();
    userList.add(uTest1.getId());
    userList.add(uTest2.getId());
    List<Double> ecoords = new ArrayList<>();
    ecoords.add(1.0);
    ecoords.add(2.0);
    Event test = new Event("eid", "name", userList, ecoords);
    db.addEvent(test);
    Event ret = W2MDatabase.getEvent("eid");
    assertEquals(ret.getName(), "name");
    assertEquals(ret.getId(), "eid");
    Set<String> users = ret.getUsers();
    assertEquals(users.size(), 2);
    assertEquals(users.toArray()[0], "uid2");
    assertEquals(users.toArray()[1], "uid1");
    List<Double> retCoords = ret.getLocation();
    assert retCoords.get(0) == 1.0;
    assert retCoords.get(1) == 2.0;
  }

  @Test
  public void testAddUserWithEvent() {
    System.out.println("TestAddUserWithEvent\n");
    W2MDatabase db = new W2MDatabase("testdb");
    clearCollections();

    List<Double> coords = new ArrayList<>();
    coords.add(1.0);
    coords.add(2.0);
    User uTest = new User("userid1", "uname1", coords);
    W2MDatabase.addUser(uTest);
    Event eTest1 = new Event("eid1", "ename1");
    Event eTest2 = new Event("eid2", "ename2");
    eTest1.addUser(uTest.getId());
    eTest2.addUser(uTest.getId());
    db.addEvent(eTest1);
    db.addEvent(eTest2);
    W2MDatabase.addUser(uTest);

    User ret = W2MDatabase.getUser("userid1");
    assertEquals(ret.getName(), "uname1");
    assertEquals(ret.getId(), "userid1");
    Set<String> events = ret.getEvents();
    Set<String> userSet = new HashSet<>();
    userSet.add(uTest.getId());
    assertEquals(events.size(), 2);
    String e1 = (String) events.toArray()[1];
    String e2 = (String) events.toArray()[0];
    assertEquals(e2, "eid2");
    assertEquals(e1, "eid1");
    Event event1 = W2MDatabase.getEvent(e1);
    Event event2 = W2MDatabase.getEvent(e2);
    assertEquals(event1.getName(), "ename1");
    assertEquals(event2.getName(), "ename2");
    Set<String> eusers1 = event1.getUsers();
    Set<String> eusers2 = event2.getUsers();
    assertEquals(eusers1.size(), 1);
    assertEquals(eusers2.size(), 1);
    assertTrue(eusers1.contains("userid1"));
    assertTrue(eusers2.contains("userid1"));
  }

  private void clearCollections() {
    W2MDatabase db = new W2MDatabase("testdb");
    DBCollection eventColl = db.getCollection("events");
    eventColl.drop();
    DBCollection userColl = db.getCollection("users");
    userColl.drop();
  }

}
