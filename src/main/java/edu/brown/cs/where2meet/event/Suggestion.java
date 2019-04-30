package edu.brown.cs.where2meet.event;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Wrapper class for data for a suggestion.
 */
public class Suggestion {

  private int price;
  private int votes;
  private double rating;
  private double lat;
  private double lon;
  private String location;
  private String url;
  private String category;
  private String photo;
  private String venue;
  private static final int MAX_LEN = 7;

  /**
   * Empty constructor.
   */
  public Suggestion() {
    this.price = 0;
    this.votes = 0;
    this.rating = 0;
    this.lat = 0;
    this.lon = 0;
    this.location = "loc";
    this.url = "url";
    this.category = "cat";
    this.photo = "photo";
    this.venue = "venue";
  }

  /**
   * Constructor for a suggestion.
   * @param price    the price value.
   * @param votes    the votes value
   * @param rating   the rating value
   * @param lat      the latitude of the suggestion
   * @param lon      the longitude of the venue
   * @param location the location string
   * @param url      the url
   * @param category the category string
   * @param photo    the url of the photo
   * @param venue    the name of the venue
   */
  //TODO fix checkstyle error!
  public Suggestion(int price, int votes, double rating, double lat, double lon,
                    String location, String url, String category, String photo,
                    String venue) {
    this.price = price;
    this.votes = votes;
    this.rating = rating;
    this.lat = lat;
    this.lon = lon;
    this.location = location;
    this.url = url;
    this.category = category;
    this.photo = photo;
    this.venue = venue;
  }

  /**
   * gets the price.
   * @return the price
   */
  public int getPrice() {
    return this.price;
  }

  /**
   * sets the price.
   * @param price the price to set
   */
  public void setPrice(int price) {
    this.price = price;
  }

  /**
   * gets the votes.
   * @return the votes
   */
  public int getVotes() {
    return this.votes;
  }

  /**
   * sets the votes.
   * @param votes the votes to set
   */
  public void setVotes(int votes) {
    this.votes = votes;
  }

  /**
   * gets the rating.
   * @return the rating
   */
  public double getRating() {
    return this.rating;
  }


  /**
   * sets the rating.
   * @param rating the rating to set
   */
  public void setRating(double rating) {
    this.rating = rating;
  }

  /**
   * gets the latitude
   * @return the latitude
   */
  public double getLat() {
    return this.lat;
  }

  /**
   * sets the latitude
   * @param lat the latitude
   */
  public void setLat(double lat) {
    this.lat = lat;
  }


  /**
   * gets the longitude
   * @return the longitude
   */
  public double getLon() {
    return this.lon;
  }

  /**
   * sets the longitude
   * @param lon the longitude
   */
  public void setLon(double lon) {
    this.lon = lon;
  }

  /**
   * gets the location.
   * @return the location
   */
  public String getLocation() {
    return this.location;
  }

  /**
   * sets the location.
   * @param location the location to set
   */
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * gets the url.
   * @return the url
   */
  public String getUrl() {
    return this.url;
  }

  /**
   * sets the url.
   * @param url the url to set
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * gets the category.
   * @return the category
   */
  public String getCategory() {
    return this.category;
  }

  /**
   * sets the category.
   * @param category the category to set
   */
  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * gets the photo.
   * @return the photo
   */
  public String getPhoto() {
    return this.photo;
  }

  /**
   * sets the photo.
   * @param photo the photo to set
   */
  public void setPhoto(String photo) {
    this.photo = photo;
  }

  @Override
  public String toString() {

    JsonObject obj = new JsonObject();
    obj.addProperty("price", this.price);
    obj.addProperty("votes", this.votes);
    obj.addProperty("rating", this.rating);
    obj.addProperty("location", this.location);
    obj.addProperty("url", this.url);
    obj.addProperty("category", this.category);
    obj.addProperty("photo", this.photo);
    obj.addProperty("venue", this.venue);

    return obj.toString();
  }

  /**
   * Takes a string produced to a json object by Suggestion.toString() and turns
   * it into a Suggestion object.
   * @param sugg the string to be translated into a Suggestion.
   * @return a Suggestion object with the data from the string.
   */
  public static Suggestion toSugg(String sugg) {

    Suggestion res = new Suggestion();
    JsonObject ret = (JsonObject) new JsonParser().parse(sugg);
    res.setPrice(ret.get("price").getAsInt());
    res.setVotes(ret.get("votes").getAsInt());
    res.setRating(ret.get("rating").getAsDouble());
    res.setCategory(ret.get("category").getAsString());
    res.setLocation(ret.get("location").getAsString());
    res.setUrl(ret.get("url").getAsString());
    res.setPhoto(ret.get("photo").getAsString());
    res.setVenue(ret.get("venue").getAsString());

    return res;

  }

  /**
   * @return the venue
   */
  public String getVenue() {
    return this.venue;
  }

  /**
   * @param venue the venue to set
   */
  public void setVenue(String venue) {
    this.venue = venue;
  }

}
