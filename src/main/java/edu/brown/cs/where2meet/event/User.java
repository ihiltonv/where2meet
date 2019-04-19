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
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    User other = (User) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

}
