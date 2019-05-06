package edu.brown.cs.where2meet.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

public class UserTest {

  @Test
  public void testConstructor() {
    HashSet<Long> events = new HashSet<>();
    events.add(10L);
    events.add(32L);
    events.add(10000L);

    User u1 = new User("Fred");
    User u2 = new User("John", events);
    User u3 = new User(12345L, "Jane", events);

    assertNotNull(u1);
    assertNotNull(u2);
    assertNotNull(u3);
  }

}
