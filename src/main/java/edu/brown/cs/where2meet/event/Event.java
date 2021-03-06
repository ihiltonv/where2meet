package edu.brown.cs.where2meet.event;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.brown.cs.where2meet.VenueRanker.VenueRanker;
import edu.brown.cs.where2meet.database.W2MDatabase;
import edu.brown.cs.where2meet.networking.YelpConnection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class to hold data for the events.
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
  // private Suggestion[] suggestions;
  private List<Suggestion> suggestions;
  //default radius to search in, (in meters)
  private static final int DEFAULT_RADIUS = 16100;

  /**
   * Constructor for an Event.
   * @param name        the name of the event.
   * @param coordinates the coordinates that the event should be near
   * @param date        a String representing the date of the event
   * @param time        a String representing the time of the event
   */
  public Event(String name, List<Double> coordinates, String date,
               String time) {
    this.id = System.currentTimeMillis();
    this.name = name;
    this.users = new HashSet<>();
    this.coordinates = coordinates;
    this.date = date;
    this.time = time;

    this.venues = new HashSet<>();
    this.topRanker = new VenueRanker();
  }

  /**
   * A constructor for the Event.
   * @param name        the name of the event.
   * @param users       the users in the event.
   * @param coordinates the coordinates of the event's location.
   * @param date        a String representing the event's date
   * @param time        a String representing the event's time
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

    this.venues = new HashSet<>();
    this.topRanker = new VenueRanker();

  }

  /**
   * A constructor for the Event.
   * @param id          the id of the event.
   * @param name        the name of the event.
   * @param users       the users in the event.
   * @param coordinates the coordinates of the event's location.
   * @param date        a String representing the event's date
   * @param time        a String representing the event's time
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

  /**
   * Instantiates the suggestion list.
   * @param categories the categories of the venues to search
   */

  public void instantiateSuggestions(List<String> categories) {
    this.suggestions = YelpConnection
        .exploreQuery(this.coordinates.get(0), this.coordinates.get(1),
            categories, Event.DEFAULT_RADIUS);
    for (Suggestion sug : this.suggestions) {
      sug.setDistFromEvent(this);
      sug.getDist();
    }
  }

  /**
   * Queries for new suggestions and adds them to the events suggestion list.
   * @param categories - categories to query new suggestions
   * @return - the new suggestions.
   */
  public List<Suggestion> updateSuggestions(List<String> categories) {
    List<Suggestion> newSuggs = YelpConnection
        .exploreQuery(this.coordinates.get(0), this.coordinates.get(1),
            categories, Event.DEFAULT_RADIUS);
    List<Suggestion> filtered = new ArrayList<>();
    for (Suggestion sug : newSuggs) {
      sug.setDistFromEvent(this);
      sug.getDist();
      if (!this.suggestions.contains(sug)) {
        this.suggestions.add(sug);
        filtered.add(sug);
      }
    }
    return filtered;
  }


  /**
   * This function returns a json array of
   * all of the categories that are present in the
   * suggestions list.
   * @return a json array of categories
   */
  public JsonArray getAllCats() {
    HashSet<String> allCats = new HashSet<>();
    JsonArray ja = new JsonArray();
    for (Suggestion sug : this.suggestions) {
      if (!allCats.contains(sug.getCategory())) {
        allCats.add(sug.getCategory());
        JsonObject newEntry = new JsonObject();
        newEntry.addProperty("value", sug.getCatValue());
        newEntry.addProperty("label", sug.getCategory());
        ja.add(newEntry);
      }
    }
    return ja;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
    result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
    result =
        prime * result + ((this.users == null) ? 0 : this.users.hashCode());
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
    if (this.id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!this.id.equals(other.id)) {
      return false;
    }
    if (this.name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!this.name.equals(other.name)) {
      return false;
    }
    if (this.users == null) {
      if (other.users != null) {
        return false;
      }
    } else if (!this.users.equals(other.users)) {
      return false;
    }
    return true;
  }

  /**
   * Adds a user to the event.
   * @param u the user to add
   */
  public void addUser(Long u) {
    if (!this.users.contains(u)) {
      this.users.add(u);
      User user = W2MDatabase.getUser(u);
      user.addEvent(this.id);

      W2MDatabase.addUserToEvent(user, this.id);
    }
  }

  /**
   * This method ranks each of the suggestions
   * and returns and ordered list of them.
   * @return a list of suggestions, from best to worst
   */
  public List<Suggestion> getBestSuggestions() {

    VenueRanker vr = new VenueRanker();
    for (Suggestion sug : this.suggestions) {
      vr.updateRank(sug, sug.suggScore(this));
    }
    return vr.getRanked();
  }

  /**
   * Gets the user set of the event.
   * @return the set of users for the event.
   */
  public Set<Long> getUsers() {
    return this.users;
  }

  /**
   * Gets the id of the event.
   * @return the id of the event
   */
  public Long getId() {
    return this.id;
  }

  /**
   * Gets the name of the event.
   * @return the name of the event.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Gets the coordinates of the event's location.
   * @return a list of the coordinates of the event.
   */
  public List<Double> getLocation() {
    return this.coordinates;
  }

  /**
   * Gets the date of the event.
   * @return a string with the date of the event.
   */
  public String getDate() {
    return this.date;
  }

  /**
   * Gets the time of the event.
   * @return a string with the time of the event.
   */
  public String getTime() {
    return this.time;
  }

  /**
   * Gets the suggestion at a specified rank (1 through 3).
   * @param rank the rank of the suggestion
   * @return the suggestion at the specified rank.
   */
  public Suggestion getSuggestion(int rank) {
    return this.suggestions.get(rank);
  }

  /**
   * Gets the suggestion list.
   * @return the list of suggestions for the event.
   */
  public List<Suggestion> getSuggestions() {
    return this.suggestions;
  }

  /**
   * Sets the suggestion list.
   * @param suggestions the array to which suggestions is set.
   */
  public void setSuggestions(List<Suggestion> suggestions) {
    this.suggestions = suggestions;
  }

  /**
   * Sets a specific suggestion in the array.
   * @param s    the new suggestion.
   * @param rank the position in the array to replace.
   */
  public void setSuggestion(Suggestion s, int rank) {
    this.suggestions.set(rank, s);
  }

  public void addSuggestion(Suggestion s) {
    this.suggestions.add(s);
    this.suggestions.sort(new SuggestionComparator());
  }
}

/**
 * TODO delete this at some point
 * Meeting notes:
 * <p>
 * Query with additional filter for more results
 * Potentially add a password
 * Add a map view
 * Don't scroll for leaderboard/picks
 * Link opens in a new window (location = _tab?)
 * Modal with link in it?
 */