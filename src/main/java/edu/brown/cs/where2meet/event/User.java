package edu.brown.cs.where2meet.event;

import java.util.Set;

/**
 * A class to model users for the application.
 *
 */
public class User {
  
  private String name;
  private String id;
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
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getId() {
    return this.id;
  }

}
