package edu.brown.cs.where2meet.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import edu.brown.cs.where2meet.event.Event;
import edu.brown.cs.where2meet.event.User;

public class W2MDatabaseTest {
  
  @Test
  public void testConstructor() {
    W2MDatabase db = new W2MDatabase("testdb");
    assertNotNull(db);
  }
  
  @Test
  public void testAddUser() {
    W2MDatabase db = new W2MDatabase("testdb");
    User test = new User("id","name");
    db.addUser(test);
    User ret = db.getUser("id");
    assertEquals(ret.getName(),"name");
    assertEquals(ret.getId(),"id");
    
  }
  
  @Test
  public void testAddEvent() {
    W2MDatabase db = new W2MDatabase("testdb");
    User uTest = new User("uid","uname");
    db.addUser(uTest);
    Set<User> userList = new HashSet<User>();
    userList.add(uTest);
    Event test = new Event("id","name",userList);
    db.addEvent(test);
    Event ret = db.getEvent("id");
    assertEquals(ret.getName(),"name");
    assertEquals(ret.getId(),"id");
    Set<User> users = ret.getUsers();
    assertEquals(users.size(),1);
    assertEquals(users.toArray()[0],new User("uid","uname"));
  }

}
