package edu.brown.cs.where2meet.networking;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.brown.cs.where2meet.event.Suggestion;
import edu.brown.cs.where2meet.main.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.google.common.net.HttpHeaders.USER_AGENT;

public class YelpConnection {
  private static final String AUTHORIZATION =
      "Bearer DIB0x1Rjag33R3LeyiipZgX_2jyq89zTfIv4OScpg5UvkcxB89GNWEI420QwQgILVtWZQ0-R3Y3u4Z8MalOnDDcWULLi8DkzugzInYyn23TCWEdWX9oqTGSXTCzHXHYx";

  private static String makeTypeString(List<String> types) {
    String acc = "";
    Iterator<String> iter = types.iterator();
    while (iter.hasNext()) {
      String curr = iter.next();
      if (iter.hasNext()) {
        acc = acc + curr + ",";
      } else {
        acc = acc + curr;
      }
    }
    return acc;
  }

  private static String parseLocation(JsonObject loc) {
    if (loc.has("display_address")) {
      JsonArray arr = loc.getAsJsonArray("display_address");
      String acc = "";
      for (JsonElement e : arr) {
        acc = acc + e + "\n";
      }
      return acc;
    } else {
      return "";
    }
  }

  private static String parseCategories(JsonObject root) {
    JsonArray cats = root.getAsJsonArray("categories");
    if (cats.size() <= 0) {
      return "";
    }
    return cats.get(0).getAsJsonObject().get("title").getAsString();
  }

  /**
   * Method to get the venues nearest a given point.
   * @param lat    - the lat of the point
   * @param lon    - the lon of the point
   * @param types  - the types of the venue to search for
   * @param radius - the radius (in meters) to search within
   * @return - a list of JsonObjects representing the venues.
   */
  public static List<Suggestion> exploreQuery(double lat, double lon,
                                              List<String> types, int radius) {
    String type = YelpConnection.makeTypeString(types);
    try {
      URL url = new URL(
          "https://api.yelp.com/v3/businesses/search?limit=50&latitude=" + lat
              + "&longitude=" + lon + "&categories=" + type + "&radius="
              + radius);
      JsonObject root = YelpConnection.makeQuery(url);
      JsonArray businesses = root.getAsJsonArray("businesses");
      List<Suggestion> results = new ArrayList<>();
      for (JsonElement b : businesses) {
        JsonObject curr = b.getAsJsonObject();
        Suggestion s = new Suggestion();
        if (curr.has("price")) {
          s.setPrice(curr.get("price").getAsString().length());
        }
        if (curr.has("rating")) {
          s.setRating(curr.get("rating").getAsDouble());
        }
        if (curr.has("coordinates")) {
          JsonObject coords = curr.getAsJsonObject("coordinates");
          s.setLat(coords.get("latitude").getAsDouble());
          s.setLon(coords.get("longitude").getAsDouble());
        }
        if (curr.has("location")) {
          s.setLocation(
              YelpConnection.parseLocation(curr.getAsJsonObject("location")));
        }
        if (curr.has("url")) {
          s.setUrl(curr.get("url").getAsString());
        }
        if (curr.has("categories")) {
          s.setCategory(YelpConnection.parseCategories(curr));
        }
        if (curr.has("image_url")) {
          s.setPhoto(curr.get("image_url").getAsString());
        }
        if (curr.has("name")) {
          s.setVenue(curr.get("name").getAsString());
        }
        if (curr.has("id")) {
          s.setId(curr.get("id").getAsString());
        }
        s.getId();
        results.add(s);
      }
      return results;
    } catch (IOException e) {
      System.out.println("ERROR: Could not interface with foursquare API");
      return new ArrayList<>();
    }

  }

  /**
   * Method to make a query to the foursquare API.
   * @param url the URL of the query
   * @return the query as a JsonObject
   * @throws IOException if there is an error networking with the API
   */
  public static JsonObject makeQuery(URL url) throws IOException {
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("User-Agent", USER_AGENT);
    con.setRequestProperty("Authorization", YelpConnection.AUTHORIZATION);
    int responseCode = con.getResponseCode();
    BufferedReader in =
        new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();
    JsonObject root = Main.GSON.fromJson(response.toString(), JsonObject.class);
    return root;
  }
}
