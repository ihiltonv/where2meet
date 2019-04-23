package edu.brown.cs.where2meet.event;

import static org.junit.Assert.assertEquals;

/**
 * Wrapper class for data for a suggestion.
 *
 */
public class Suggestion {

  private int price;
  private int votes;
  private double rating;
  private String location;
  private String url;
  private String category;
  private String photo;
  private static final int MAX_LEN = 7;

  /**
   * Empty constructor.
   */
  public Suggestion() {
    this.price = 0;
    this.votes = 0;
    this.rating = 0;
    this.location = "loc";
    this.url = "url";
    this.category = "cat";
    this.photo = "photo";
  }

  /**
   * Constructor for a suggestion.
   *
   * @param price
   *          the price value.
   * @param votes
   *          the votes value
   * @param rating
   *          the rating value
   * @param location
   *          the location string
   * @param url
   *          the url
   * @param category
   *          the category string
   * @param photo
   *          the url of the photo
   */
  public Suggestion(int price, int votes, double rating, String location,
      String url, String category, String photo) {
    this.price = price;
    this.votes = votes;
    this.rating = rating;
    this.location = location;
    this.url = url;
    this.category = category;
    this.photo = photo;
  }

  /**
   * gets the price.
   *
   * @return the price
   */
  public int getPrice() {
    return price;
  }

  /**
   * sets the price.
   *
   * @param price
   *          the price to set
   */
  public void setPrice(int price) {
    this.price = price;
  }

  /**
   * gets the votes.
   *
   * @return the votes
   */
  public int getVotes() {
    return votes;
  }

  /**
   * sets the votes.
   *
   * @param votes
   *          the votes to set
   */
  public void setVotes(int votes) {
    this.votes = votes;
  }

  /**
   * gets the rating.
   *
   * @return the rating
   */
  public double getRating() {
    return rating;
  }

  /**
   * sets the rating.
   *
   * @param rating
   *          the rating to set
   */
  public void setRating(double rating) {
    this.rating = rating;
  }

  /**
   * gets the location.
   *
   * @return the location
   */
  public String getLocation() {
    return location;
  }

  /**
   * sets the location.
   *
   * @param location
   *          the location to set
   */
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * gets the url.
   *
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * sets the url.
   *
   * @param url
   *          the url to set
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * gets the category.
   *
   * @return the category
   */
  public String getCategory() {
    return category;
  }

  /**
   * sets the category.
   *
   * @param category
   *          the category to set
   */
  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * gets the photo.
   *
   * @return the photo
   */
  public String getPhoto() {
    return photo;
  }

  /**
   * sets the photo.
   *
   * @param photo
   *          the photo to set
   */
  public void setPhoto(String photo) {
    this.photo = photo;
  }

  @Override
  public String toString() {
    String res = this.price + "\"" + this.votes + "\"" + this.rating + "\""
        + this.location + "\"" + this.url + "\"" + this.category + "\""
        + this.photo;

    return res;
  }

  /**
   * Takes a string produced by Suggestion.toString() and turns it into a
   * Suggestion object.
   *
   * @param sugg
   *          the string to be translated into a Suggestion.
   * @return a Suggestion object with the data from the string.
   */
  public static Suggestion toSugg(String sugg) {

    Suggestion res = new Suggestion();
    String[] fields = sugg.split("\"");
    try {
      assertEquals(fields.length, MAX_LEN);
      res.setPrice(Integer.valueOf(fields[0]));
      res.setVotes(Integer.valueOf(fields[1]));
      res.setRating(Double.valueOf(fields[2]));
      res.setLocation(fields[3]);
      res.setUrl(fields[4]);
      res.setCategory(fields[5]);
      res.setPhoto(fields[6]);
    } catch (AssertionError a) {
      return res;
    }
    return res;

  }

}
