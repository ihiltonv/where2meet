package edu.brown.cs.where2meet.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

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

}
