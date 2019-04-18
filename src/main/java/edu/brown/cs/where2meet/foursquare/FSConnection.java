package edu.brown.cs.where2meet.foursquare;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

  private static boolean validType(String type) {
    return (type.equals("food") || type.equals("drinks") || type
        .equals("coffee") || type.equals("shops") || type.equals("arts") || type
        .equals("outdoors") || type.equals("sights") || type.equals("trending")
        || type.equals("nextVenues") || type.equals("topPicks"));
  }

  /**
   * Method to get the venues nearest a given point.
   * @param lat  - the lat of the point
   * @param lon  - the lon of the point
   * @param type - the type of the venue to search for, input
   *             "topPicks" to have foursquare return suggested venues.
   * @return - a list of JsonObjects representing the venues.
   */
  public static List<JsonObject> makeQuery(double lat, double lon,
                                           String type) {
    assert FSConnection.validType(type);
    long time = 0;
    try {
      URL url = new URL(
          "https://api.foursquare.com/v2/venues/explore?client_id="
              + FSConnection.CLIENT_ID + "&client_secret="
              + FSConnection.CLIENT_SECRET + "&v=20180323"
//&limit=" + maxResults
              + "&intent=browse&ll=" + lat + "," + lon + "&time=any&day=any"
              + "&section=" + type);
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
      String res = response.toString();
      JsonObject root =
          Main.GSON.fromJson(response.toString(), JsonObject.class);
      JsonObject jsonResponse = root.get("response").getAsJsonObject();
      JsonArray groups = jsonResponse.get("groups").getAsJsonArray();
      List<JsonObject> items = new ArrayList<>();
      for (JsonElement e : groups) {
        e.getAsJsonObject().get("items").getAsJsonArray()
            .forEach(x -> items.add(x.getAsJsonObject()));
      }
      List<JsonObject> venues = new ArrayList<>();
      for (JsonObject obj : items) {
        venues.add(obj.get("venue").getAsJsonObject());
      }
      System.out.println(venues.size());
      return venues;
    } catch (IOException e) {
      System.out.println("ERROR: Could not interface with traffic server");
      return null;
    }
  }
}
