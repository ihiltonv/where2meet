package edu.brown.cs.where2meet.event;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
  private String venue;
  // private static final int MAX_LEN = 7;

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
    this.venue = "venue";
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
   * @param venue
   *          the name of the venue
   */
  public Suggestion(int price, int votes, double rating, String location,
      String url, String category, String photo, String venue) {
    this.price = price;
    this.votes = votes;
    this.rating = rating;
    this.location = location;
    this.url = url;
    this.category = category;
    this.photo = photo;
    this.venue = venue;
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

  public static String suggToString(List<Suggestion> s) {
    JsonArray jarray = new JsonArray();
    for (Suggestion sugg : s) {
      jarray.add(sugg.toString());
    }
    System.out.println(jarray.toString());

    return jarray.toString();
  }

  /**
   * Takes a string produced to a json object by Suggestion.toString() and turns
   * it into a Suggestion object.
   *
   * @param sugg
   *          the string to be translated into a Suggestion.
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

  public static List<Suggestion> getStringAsList(String array) {
    List<Suggestion> suggestions = new ArrayList<>();

    JsonParser jparse = new JsonParser();
    JsonElement jel = jparse.parse(array);
    JsonArray jarray = jel.getAsJsonArray();
    for (JsonElement e : jarray) {
      suggestions.add(Suggestion.toSugg(e.toString()));
    }

    return suggestions;
  }

  /**
   * @return the venue
   */
  public String getVenue() {
    return venue;
  }

  /**
   * @param venue
   *          the venue to set
   */
  public void setVenue(String venue) {
    this.venue = venue;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((category == null) ? 0 : category.hashCode());
    result = prime * result + ((location == null) ? 0 : location.hashCode());
    result = prime * result + ((photo == null) ? 0 : photo.hashCode());
    result = prime * result + price;
    long temp;
    temp = Double.doubleToLongBits(rating);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((url == null) ? 0 : url.hashCode());
    result = prime * result + ((venue == null) ? 0 : venue.hashCode());
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
    Suggestion other = (Suggestion) obj;
    if (category == null) {
      if (other.category != null) {
        return false;
      }
    } else if (!category.equals(other.category)) {
      return false;
    }
    if (location == null) {
      if (other.location != null) {
        return false;
      }
    } else if (!location.equals(other.location)) {
      return false;
    }
    if (photo == null) {
      if (other.photo != null) {
        return false;
      }
    } else if (!photo.equals(other.photo)) {
      return false;
    }
    if (price != other.price) {
      return false;
    }
    if (Double.doubleToLongBits(rating) != Double
        .doubleToLongBits(other.rating)) {
      return false;
    }
    if (url == null) {
      if (other.url != null) {
        return false;
      }
    } else if (!url.equals(other.url)) {
      return false;
    }
    if (venue == null) {
      if (other.venue != null) {
        return false;
      }
    } else if (!venue.equals(other.venue)) {
      return false;
    }
    return true;
  }

}
