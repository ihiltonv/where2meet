package edu.brown.cs.where2meet.event;

import java.util.HashSet;
import java.util.Set;

import edu.brown.cs.where2meet.database.W2MDatabase;

public class Event {
  
  private String id;
  private String name;
  private Set<String> users;
  
  public Event(String id, String name) {
    this.id = id;
    this.name = name;
    this.users = new HashSet<>();
  }
  
  public Event(String id, String name, Set<String> users) {
    this.id = id;
    this.name = name;
    this.users = users;
    for(String u: users) {
     addUser(u);
    }
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((users == null) ? 0 : users.hashCode());
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
    Event other = (Event) obj;
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
    if (users == null) {
      if (other.users != null)
        return false;
    } else if (!users.equals(other.users))
      return false;
    return true;
  }

  public void addUser(String u) {
    if(!users.contains(u)) {
      users.add(u);
      User user = W2MDatabase.getUser(u);
      user.addEvent(this.id);
      W2MDatabase.updateUser(user);
    }
  }
  
  public Set<String> getUsers(){
    return this.users;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }

}
