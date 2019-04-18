package edu.brown.cs.where2meet.event;

import java.util.Set;

public class Event {
  
  private String id;
  private String name;
  private Set<User> users;
  
  public Event(String id, String name, Set<User> users) {
    this.id = id;
    this.name = name;
    this.users = users;
  }

}
