package edu.brown.cs.where2meet.event;

import java.util.HashSet;
import java.util.Set;

/**
 * A class to model users for the application.
 *
 */
public class User {
  
  private String name;
  private String id;
  private Set<String> events;
  private Set<String> filters;
  
  /**
   * Constructor for a user.
   * 
   * @param id
   * @param name
   */
  public User(String id, String name) {
    this.name = name;
    this.id = id;
    this.events = new HashSet<>();
    this.filters = new HashSet<>();
    
  }
  
  public User(String id, String name, Set<String> events) {
    this.name = name;
    this.id = id;
    this.events = events;
    this.filters = new HashSet<>();
    
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getId() {
    return this.id;
  }
  
  public void addEvent(String e) {
    if(!events.contains(e)) {
      events.add(e);
    }
  }
  
  public Set<String> getEvents(){
    return this.events;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    User other = (User) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }

}
