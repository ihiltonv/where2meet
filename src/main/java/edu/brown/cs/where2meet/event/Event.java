package edu.brown.cs.where2meet.event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.brown.cs.where2meet.database.W2MDatabase;
import edu.brown.cs.where2meet.VenueRanker.VenueRanker;

/**
 * A class to hold data for the events.
 *
 */
public class Event {

  private Long id;
  private String name;
  private Set<Long> users;
  private List<Double> coordinates;
  private String date;
  private String time;
  private Set<Venue> venues;
  private VenueRanker topRanker;

  /**
   * Constructor for an Event.
   *
   * @param name
   *          the name of the event.
   * @param coordinates
   *          the coordinates that the event should be near
   * @param date
   *          a String representing the date of the event
   * @param time
   *          a String representing the time of the event
   */
  public Event(String name, List<Double> coordinates, String date,
      String time) {
    this.id = System.currentTimeMillis();
    this.name = name;
    this.users = new HashSet<>();
    this.coordinates = coordinates;
    this.date = date;
    this.time = time;

    venues = new HashSet<>();
    topRanker = new VenueRanker();
  }

  /**
   * A constructor for the Event.
   *
   * @param name
   *          the name of the event.
   * @param users
   *          the users in the event.
   * @param coordinates
   *          the coordinates of the event's location.
   * @param date
   *          a String representing the event's date
   * @param time
   *          a String representing the event's time
   */
  public Event(String name, Set<Long> users, List<Double> coordinates,
      String date, String time) {
    this.id = System.currentTimeMillis();
    this.name = name;
    this.users = new HashSet<>();
    for (Long u : users) {
      addUser(u);
    }
    this.coordinates = coordinates;
    this.date = date;
    this.time = time;

    venues = new HashSet<>();
    topRanker = new VenueRanker();
  }

  /**
   * A constructor for the Event.
   *
   * @param id
   *          the id of the event.
   * @param name
   *          the name of the event.
   * @param users
   *          the users in the event.
   * @param coordinates
   *          the coordinates of the event's location.
   * @param date
   *          a String representing the event's date
   * @param time
   *          a String representing the event's time
   */
  public Event(Long id, String name, Set<Long> users, List<Double> coordinates,
      String date, String time) {
    this.id = id;
    this.name = name;
    this.users = new HashSet<>();
    for (Long u : users) {
      addUser(u);
    }
    this.coordinates = coordinates;
    this.date = date;
    this.time = time;
  }

  public void updateVotes(Venue o1, Venue o2, Venue o3, Venue n1, Venue n2, Venue n3) {
    this.topRanker.updateRankRelative(o1, -5.0);
    this.topRanker.updateRankRelative(o2, -3.0);
    this.topRanker.updateRankRelative(o2, -1.0);
    this.topRanker.updateRankRelative(o2, 1.0);
    this.topRanker.updateRankRelative(o2, 3.0);
    this.topRanker.updateRankRelative(o2, 5.0);

  }

  public Set<Venue> filterVenues(User u) {
    Set<Venue> result = new HashSet<>();
    for (Venue venue : venues) {
      if (venue.getPrice() <= u.getPrice() && venue.getDistance() <= u.getDist() && venue.getPopularity() >= u.getRating()){
        result.add(venue);
      }
    }
    return result;
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
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Event other = (Event) obj;
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
    if (users == null) {
      if (other.users != null) {
        return false;
      }
    } else if (!users.equals(other.users)) {
      return false;
    }
    return true;
  }

  /**
   * Adds a user to the event.
   *
   * @param u
   *          the user to add
   */
  public void addUser(Long u) {
    if (!users.contains(u)) {
      users.add(u);
      User user = W2MDatabase.getUser(u);
      user.addEvent(this.id);
      W2MDatabase.addUserToEvent(user, this.id);
    }
  }

  /**
   * Gets the user set of the event.
   *
   * @return the set of users for the event.
   */
  public Set<Long> getUsers() {
    return this.users;
  }

  /**
   * Gets the id of the event.
   *
   * @return the id of the event
   */
  public Long getId() {
    return this.id;
  }

  /**
   * Gets the name of the event.
   *
   * @return the name of the event.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Gets the coordinates of the event's location.
   *
   * @return a list of the coordinates of the event.
   */
  public List<Double> getLocation() {
    return this.coordinates;
  }

  /**
   * Gets the date of the event.
   *
   * @return a string with the date of the event.
   */
  public String getDate() {
    return this.date;
  }

  /**
   * Gets the time of the event.
   *
   * @return a string with the time of the event.
   */
  public String getTime() {
    return this.time;
  }
}
