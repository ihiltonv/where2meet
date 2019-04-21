package edu.brown.cs.where2meet.event;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class to model users for the application.
 *
 */
public class User {

  private String name;
  private String id;
  private Set<Long> events;
  private List<Double> coordinates;

  /**
   * Constructor for a user.
   *
   * @param id
   *          the id of the user
   * @param name
   *          the name of the user
   * @param coords
   *          the coordinates of the user's location
   */
  public User(String id, String name, List<Double> coords) {
    this.name = name;
    this.id = id;
    this.events = new HashSet<>();
    this.coordinates = coords;

  }

  /**
   * Constructor for the user.
   *
   * @param id
   *          the id of the user
   * @param name
   *          the name of the user
   * @param events
   *          the events that the user is in.
   * @param coords
   *          the coordinates of the user's location.
   */
  public User(String id, String name, Set<Long> events, List<Double> coords) {
    this.name = name;
    this.id = id;
    this.events = events;
    this.coordinates = coords;

  }

  /**
   * gets the name of the user.
   *
   * @return the name of the user.
   */
  public String getName() {
    return this.name;
  }

  /**
   * gets the id of the user.
   *
   * @return the id of the user.
   */
  public String getId() {
    return this.id;
  }

  /**
   * adds an event to the user's set of events.
   *
   * @param e
   *          the event to add.
   */
  public void addEvent(Long e) {
    if (!events.contains(e)) {
      events.add(e);
    }
  }

  /**
   * gets the event set of the user.
   *
   * @return the events of the user.
   */
  public Set<Long> getEvents() {
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

  /**
   * gets the coordinates of the user's location.
   *
   * @return the coordinates of the user's location.
   */
  public List<Double> getLocation() {
    return this.coordinates;
  }

}
