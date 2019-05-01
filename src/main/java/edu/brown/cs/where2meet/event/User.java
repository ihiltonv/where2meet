package edu.brown.cs.where2meet.event;

import java.util.HashSet;
import java.util.Set;

/**
 * A class to model users for the application.
 *
 */
public class User {

  private String name;
  private Long id;
  private Set<Long> events;
  private int price;
  private double dist;
  private double rating;
  private String category;
  private Suggestion[] suggestions;

  /**
   * Constructor for a user.
   *
   * @param name
   *          the name of the user
   */
  public User(String name) {
    this.name = name;
    this.id = System.currentTimeMillis();
    this.events = new HashSet<>();

    resetFilters();

    instantiateSuggestions();
  }

  /**
   * Constructor for the user.
   *
   * @param name
   *          the name of the user
   * @param events
   *          the events that the user is in.
   */
  public User(String name, Set<Long> events) {
    this.name = name;
    this.id = System.currentTimeMillis();
    this.events = events;
    instantiateSuggestions();
    resetFilters();

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
   */
  public User(Long id, String name, Set<Long> events) {
    this.name = name;
    this.id = id;
    this.events = events;
    instantiateSuggestions();
    resetFilters();
  }

  /**
   * Resets the filter values for a user.
   */
  public void resetFilters() {
    this.category = "";
    this.price = 1;
    this.rating = 5;
    this.dist = 1;
  }

  public void updateFilters(int price, double dist, int rating) {
    this.price = price;
    this.dist = dist;
    this.rating = rating;
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
  public Long getId() {
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
   * Gets the price filter.
   *
   * @return the value for the price filter.
   */
  public int getPrice() {
    return price;
  }

  /**
   * Sets the value for the price filter.
   *
   * @param price
   *          the new value for the price filter
   */
  public void setPrice(int price) {
    this.price = price;
  }

  /**
   * Gets the value for the dist filter.
   *
   * @return the value for the distance filter
   */
  public double getDist() {
    return dist;
  }

  /**
   * Sets the value of the dist filter.
   *
   * @param dist
   *          the new value of the dist filter.
   */
  public void setDist(double dist) {
    this.dist = dist;
  }

  /**
   * Gets the value of the rating filter.
   *
   * @return the value of the rating filter.
   */
  public double getRating() {
    return rating;
  }

  /**
   * Sets the value of the rating filter.
   *
   * @param rating
   *          the new value of the rating filter.
   */
  public void setRating(double rating) {
    this.rating = rating;
  }

  /**
   * Gets the value of the category filter.
   *
   * @return the value of the category filter.
   */
  public String getCategory() {
    return category;
  }

  /**
   * Sets the value of the category filter.
   *
   * @param category
   *          the new value of the category filter.
   */
  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * Gets the suggestion at a specified rank (1 through 3).
   *
   * @param rank
   *          the rank of the suggestion
   * @return the suggestion at the specified rank.
   */
  public Suggestion getSuggestion(int rank) {
    return suggestions[rank];
  }

  /**
   * Gets the suggestion list.
   *
   * @return the list of suggestions for the event.
   */
  public Suggestion[] getSuggestions() {
    return this.suggestions;
  }

  /**
   * Sets the suggestion list.
   *
   * @param suggestions
   *          the array to which suggestions is set.
   */
  public void setSuggestions(Suggestion[] suggestions) {
    this.suggestions = suggestions;
  }

  /**
   *
   * Sets a specific suggestion in the array.
   *
   * @param s
   *          the new suggestion.
   * @param rank
   *          the position in the array to replace.
   */
  public void setSuggestion(Suggestion s, int rank) {
    this.suggestions[rank] = s;
  }

  /**
   * Instantiates the suggestion array.
   */
  private void instantiateSuggestions() {
    this.suggestions = new Suggestion[3];
    this.suggestions[0] = new Suggestion();
    this.suggestions[1] = new Suggestion();
    this.suggestions[2] = new Suggestion();
  }

}
