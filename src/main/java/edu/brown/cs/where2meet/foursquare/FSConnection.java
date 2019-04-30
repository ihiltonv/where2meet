package edu.brown.cs.where2meet.foursquare;

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
import java.util.List;

import static com.google.common.net.HttpHeaders.USER_AGENT;

public class FSConnection {
  private static final String CLIENT_ID =
      "PRQJQ4EXH4XGR001IYVA4U0HNG1ZVYSEYTPDH5LOSREUT3X4";
  private static final String CLIENT_SECRET =
      "BUA1MNZRDAZAK3KHUUKHMJQRRHMPC1NIEWSEFTBVCO5V5VUN";
  private static final String PHOTO_SIZE = "original";

  private static boolean validType(String type) {
    return (type.equals("food") || type.equals("drinks") || type
        .equals("coffee") || type.equals("shops") || type.equals("arts") || type
        .equals("outdoors") || type.equals("sights") || type.equals("trending")
        || type.equals("nextVenues") || type.equals("topPicks"));
  }

  /**
   * Method to extract all IDs from the response Json of an explore query.
   * @param root - the Json object
   * @return - a list of venue IDs.
   */
  private static List<String> getVenueIDs(JsonObject root) {
    JsonObject jsonResponse = root.get("response").getAsJsonObject();
    JsonArray groups = jsonResponse.get("groups").getAsJsonArray();
    List<JsonObject> items = new ArrayList<>();
    for (JsonElement e : groups) {
      e.getAsJsonObject().get("items").getAsJsonArray()
          .forEach(x -> items.add(x.getAsJsonObject()));
    }
    List<String> ids = new ArrayList<>();
    for (JsonObject obj : items) {
      JsonObject venue = obj.get("venue").getAsJsonObject();
      ids.add(venue.get("id").toString());
    }
    return ids;
  }

  /**
   * Method to get the venues nearest a given point.
   * @param lat  - the lat of the point
   * @param lon  - the lon of the point
   * @param type - the type of the venue to search for, input
   *             "topPicks" to have foursquare return suggested venues.
   * @return - a list of JsonObjects representing the venues.
   */
  public static List<Suggestion> exploreQuery(double lat, double lon,
                                              String type) {
    assert FSConnection.validType(type);
    try {
      URL url = new URL(
          "https://api.foursquare.com/v2/venues/explore?client_id="
              + FSConnection.CLIENT_ID + "&client_secret="
              + FSConnection.CLIENT_SECRET + "&v=20180323" + "&intent=browse"
              + "&ll=" + lat + "," + lon + "&time=any&day=any" + "&section="
              + type);
      List<String> venueIDs =
          FSConnection.getVenueIDs(FSConnection.makeQuery(url));
      System.out.println(venueIDs);
      return null;
    } catch (IOException e) {
      System.out.println("ERROR: Could not interface with foursquare API");
      return new ArrayList<>();
    }

  }

  public static Suggestion getSuggestionInfo(String id) {
    try {
      URL url = new URL(
          "https://api.foursquare.com/v2/venues/" + id + "?client_id="
              + FSConnection.CLIENT_ID + "&client_secret="
              + FSConnection.CLIENT_SECRET + "&v=20180323");
      Suggestion s = new Suggestion();
      JsonObject root = FSConnection.makeQuery(url);
      JsonObject jsonResponse = root.getAsJsonObject("response");
      JsonObject venue = jsonResponse.getAsJsonObject("venue");
      s.setVenue(venue.get("name").getAsString());
      String location = "";
      location += venue.get("address").getAsString() + "\n";
      return null;
    } catch (IOException e) {
      System.out.println("ERROR: Could not get event info from foursquare");
      return null;
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

  private static Suggestion toSuggestions(JsonObject venue) {
    Suggestion s = new Suggestion();
    return null;
  }
}
