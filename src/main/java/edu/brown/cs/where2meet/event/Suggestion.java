package edu.brown.cs.where2meet.event;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Wrapper class for data for a suggestion.
 */
public class Suggestion {

  private String id;
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
  // private static final int MAX_LEN = 7;

  /**
   * Empty constructor.
   */
  public Suggestion() {
    this.id = "id";
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
   *
   * @param price
   *          the price value.
   * @param votes
   *          the votes value
   * @param rating
   *          the rating value
   * @param url
   *          the url
   * @param category
   *          the category string
   * @param photo
   *          the url of the photo
   * @param venue
   *          the name of the venue
   */
  // TODO fix checkstyle error!
  public Suggestion(String id, int price, int votes, double rating, String url,
      String category, String photo, String venue) {
    this.id = id;
    this.price = price;
    this.votes = votes;
    this.rating = rating;
    this.url = url;
    this.category = category;
    this.photo = photo;
    this.venue = venue;
  }

  public void setLatLonLoc(double lat, double lon, String loc) {
    this.lat = lat;
    this.lon = lon;
    this.location = loc;
  }

  /**
   * gets the price.
   *
   * @return the price
   */
  public int getPrice() {
    return this.price;
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
    return this.votes;
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
    return this.rating;
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
   * gets the latitude
   *
   * @return the latitude
   */
  public double getLat() {
    return this.lat;
  }

  /**
   * sets the latitude
   *
   * @param lat
   *          the latitude
   */
  public void setLat(double lat) {
    this.lat = lat;
  }

  /**
   * gets the longitude
   *
   * @return the longitude
   */
  public double getLon() {
    return this.lon;
  }

  /**
   * sets the longitude
   *
   * @param lon
   *          the longitude
   */
  public void setLon(double lon) {
    this.lon = lon;
  }

  /**
   * gets the location.
   *
   * @return the location
   */
  public String getLocation() {
    return this.location;
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
    return this.url;
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
    return this.category;
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
    return this.photo;
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

    JsonObject obj = this.getAsJsonObject();
    return obj.toString();
  }

  /**
   * Gets a suggestion represented as a JsonObject.
   *
   * @return a JsonObject with the data from the caller.
   */
  public JsonObject getAsJsonObject() {
    JsonObject obj = new JsonObject();
    obj.addProperty("id", this.id);
    obj.addProperty("price", this.price);
    obj.addProperty("votes", this.votes);
    obj.addProperty("rating", this.rating);
    obj.addProperty("location", this.location);
    obj.addProperty("url", this.url);
    obj.addProperty("category", this.category);
    obj.addProperty("photo", this.photo);
    obj.addProperty("venue", this.venue);
    obj.addProperty("lat", this.lat);
    obj.addProperty("lon", this.lon);
    return obj;
  }

  /**
   * Turns a list of suggestions into a string.
   *
   * @param s
   *          the suggestion list to translate.
   * @return a string representing a JsonArray of the contents of the list.
   */
  public static String suggToString(List<Suggestion> s) {
    JsonArray jarray = new JsonArray();
    for (Suggestion sugg : s) {
      jarray.add(sugg.toString());
    }

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
    res.setId(ret.get("id").getAsString());
    res.setPrice(ret.get("price").getAsInt());
    res.setVotes(ret.get("votes").getAsInt());
    res.setRating(ret.get("rating").getAsDouble());
    res.setCategory(ret.get("category").getAsString());
    res.setLocation(ret.get("location").getAsString());
    res.setUrl(ret.get("url").getAsString());
    res.setPhoto(ret.get("photo").getAsString());
    res.setVenue(ret.get("venue").getAsString());
    res.setLat(ret.get("lat").getAsDouble());
    res.setLon(ret.get("lon").getAsDouble());

    return res;

  }

  /**
   * Translates a string representing a list of suggestions into a list of
   * suggestions.
   *
   * @param array
   *          the string to translate
   * @return A list of suggestions.
   */
  public static List<Suggestion> getStringAsList(String array) {
    List<Suggestion> suggestions = new ArrayList<>();

    JsonParser jparse = new JsonParser();
    JsonElement jel = jparse.parse(array);
    JsonArray jarray = jel.getAsJsonArray();
    for (JsonElement e : jarray) {
      suggestions.add(Suggestion.toSugg(e));
    }

    return suggestions;
  }

  /**
   * Translates a JsonElement into a Suggestion object.
   *
   * @param s
   *          the JsonElement to translate.
   * @return A suggestion Object with the data from s.
   */
  private static Suggestion toSugg(JsonElement s) {

    String stripped = s.getAsString();
    return Suggestion.toSugg(stripped);
  }

  /**
   * @return the venue
   */
  public String getVenue() {
    return this.venue;
  }

  /**
   * @param venue
   *          the venue to set
   */
  public void setVenue(String venue) {
    this.venue = venue;
  }

  public double suggScore(Event event) {

    //Haversine distance to venue from event
    double dist = Suggestion.haversineDist(this.lat, this.lon, event.getLocation().get(0), event.getLocation().get(1));
    //Value of the venue in terms of price per cost
    double value = this.rating / (1.0 * this.price);
    //A weight for the rating of the restaurant
    double c = 0.25 * value;
    //The distance is perceived logarithmically, so it is adjusted and inverted
    //The adjusted distance is multiplied by a value/quality metric
    double score = (1.0 / Math.log(dist)) * (value + c * this.rating);

    return score;
  }

  public static double haversineDist(double lat1, double lon1, double lat2, double lon2) {
    final int radius = 6371; //kilometers

//    left part of the square root in haversine's
    double left =  Math.pow(Math.sin((lat2 - lat1) / 2.0), 2.0);
//    right part of the square root in haversine's
    double right = Math.cos(lat1) * Math.cos(lat2)
            * Math.pow(Math.sin((lon2 - lon1) / 2.0), 2.0);


    double dist = 2.0 * radius * Math.asin(Math.sqrt(left + right));

    return dist;
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

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

}
