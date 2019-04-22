package edu.brown.cs.where2meet.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

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
    W2MDatabase db = new W2MDatabase("data/testdb.sqlite3");
    db.cleardb();
    db.createdb();
    List<Double> coords = new ArrayList<>();
    coords.add(1.0);
    coords.add(2.0);
    User test = new User("/n/1", coords);
    W2MDatabase.addUser(test);
    User ret = W2MDatabase.loadUser(test.getId());
    assertEquals(ret.getName(), "/n/1");
    assertEquals(ret.getId(), test.getId());

  }

  @Test
  public void testAddEvent() {
    System.out.println("TestAddEvent\n");
    W2MDatabase db = new W2MDatabase("data/testdb.sqlite3");
    db.cleardb();
    db.createdb();
    List<Double> coords = new ArrayList<>();
    coords.add(1.0);
    coords.add(2.0);
    User uTest1 = new User("/n/2", coords);
    User uTest2 = new User("/n/3", coords);

    while (uTest2.getId().equals(uTest1.getId())) {
      uTest2 = new User("/n/3", coords);
    }

    Set<Long> userList = new HashSet<>();

    userList.add(uTest1.getId());
    userList.add(uTest2.getId());

    List<Double> ecoords = new ArrayList<>();
    ecoords.add(1.0);
    ecoords.add(2.0);
    Event test = new Event("/n/4", userList, ecoords, "date", "time");
    Long eid = test.getId();
    db.addEvent(test);

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
    W2MDatabase db = new W2MDatabase("data/testdb.sqlite3");
    db.cleardb();
    db.createdb();
    List<Double> coords = new ArrayList<>();
    coords.add(1.0);
    coords.add(2.0);
    User uTest = new User("/n/5", coords);
    Long uid = uTest.getId();
    W2MDatabase.addUser(uTest);

    Event eTest1 = new Event("/n/6", coords, "date1", "time1");
    Event eTest2 = new Event("/n/7", coords, "date2", "time2");
    while (eTest2.getId().equals(eTest1.getId())) {
      eTest2 = new Event("/n/7", coords, "date2", "time2");
    }

    Long eid1 = eTest1.getId();
    Long eid2 = eTest2.getId();
    eTest1.addUser(uTest.getId());
    eTest2.addUser(uTest.getId());
    db.addEvent(eTest1);
    db.addEvent(eTest2);
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
    W2MDatabase db = new W2MDatabase("data/testdb.sqlite3");
    db.cleardb();
    db.createdb();
    List<Double> coords = new ArrayList<>();
    coords.add(1.0);
    coords.add(2.0);
    User uTest1 = new User("/n/1", coords);
    W2MDatabase.addUser(uTest1);
    Long uid = uTest1.getId();
    Event eTest1 = new Event("/e/1", coords, "date", "time");
    db.addEvent(eTest1);
    eTest1.addUser(uid);
    User ret = W2MDatabase.getUser(uid);
    assertEquals(ret.getCategory(), "");
    assertEquals(ret.getPrice(), 1);
    assertEquals(ret.getRating(), 5);
    assert ret.getDist() == 1;

    uTest1.setCategory("test");
    uTest1.setDist(2.0);
    uTest1.setPrice(2);
    uTest1.setRating(4);
    W2MDatabase.updateUser(uTest1, eTest1.getId());
    ret = W2MDatabase.getUser(uid);

    assertEquals(ret.getCategory(), "test");
    assertEquals(ret.getPrice(), 2);
    assertEquals(ret.getRating(), 4);
    assert ret.getDist() == 2.0;
  }

}
