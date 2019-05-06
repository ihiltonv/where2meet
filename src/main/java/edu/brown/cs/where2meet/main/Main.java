package edu.brown.cs.where2meet.main;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.brown.cs.where2meet.event.Event;
import edu.brown.cs.where2meet.event.Suggestion;
import edu.brown.cs.where2meet.event.User;
import edu.brown.cs.where2meet.websockets.EventWebSocket;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Main {

  private static W2MUniverse wmu;

  private static final int DEFAULT_PORT = 4567;

  public static final Gson GSON = new Gson();

  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // create W2MUniverse
    this.wmu = new W2MUniverse();
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(Main.DEFAULT_PORT);
    OptionSet options = parser.parse(this.args);

    runSparkServer((int) options.valueOf("port"));
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out
          .printf("ERROR: Unable use %s for template loading.%n", templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    Spark.webSocket("/voting", EventWebSocket.class);
    Spark.port(port);

    // Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders =
          request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers",
            accessControlRequestHeaders);
      }

      String accessControlRequestMethod =
          request.headers("Access-Control-Request-Method");
      if (accessControlRequestMethod != null) {
        response
            .header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });

    Spark.before((request, response) -> response
        .header("Access-Control-Allow-Origin", "*"));

    // Spark.after((Filter) (request, response) -> {
    // response.header("Access-Control-Allow-Origin", "*");
    // response.header("Access-Control-Allow-Methods", "GET");
    // response.header("Access-Control-Allow-Methods", "POST");
    // });
    // FreeMarkerEngine freeMarker = Main.createEngine();

    // Setup Spark Routes
    Spark.post("/event", new EventHandler(Main.wmu));
    Spark.get("/event/:id", new GetEventDataHandler(Main.wmu));
    Spark.post("/newuser", new UserHandler(Main.wmu));
    Spark.post("/update/:id", new UpdateSuggestions(Main.wmu));

  }

  /**
   * This class handles the creation of new events.
   */
  public static class EventHandler implements Route {

    W2MUniverse wmu;

    public EventHandler(W2MUniverse wmu) {
      this.wmu = wmu;
    }

    @Override
    public String handle(Request req, Response res) {
      // get the JSON String
      String data = req.body();

      // get the string as an object
      JsonObject json = Main.GSON.fromJson(data, JsonObject.class);

      // get the actual data
      String name = json.get("name").getAsString();
      double lat = json.get("lat").getAsDouble();
      double lon = json.get("lon").getAsDouble();
      String date = json.get("date").getAsString();
      String time = json.get("time").getAsString();
      JsonArray categories = json.getAsJsonArray("categories");
      List<String> cats = new ArrayList<>();
      categories.forEach(x -> cats.add(x.getAsString()));
      List<Double> coordinates = new ArrayList<>();
      coordinates.add(lat);
      coordinates.add(lon);

      // create an event and store it in the database
      Event event = new Event(name, coordinates, date, time);
      event.instantiateSuggestions(cats);
      this.wmu.wmd.addEvent(event);

      List suggestions = new ArrayList();

      // TODO: Build the json

      Map<String, Object> variables = ImmutableMap
          .of("id", event.getId(), "suggestionsList", suggestions, "error",
              false, "errorMsg", "");

      return Main.GSON.toJson(variables);
    }
  }


  public static class UpdateSuggestions implements Route {
    W2MUniverse wmu;

    public UpdateSuggestions(W2MUniverse wmu) {
      this.wmu = wmu;
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
      // get the JSON String
      String data = req.body();
      boolean error = false;
      String errorMsg = "";
      // get the id from the url
      String id = req.params(":id");

      Event event = this.wmu.wmd.getEvent(Long.parseLong(id));

      String name = "";
      String time = "";
      String date = "";
      double distance = 0.0;
      List<Suggestion> updatedSuggestionsList = new ArrayList<>();
      List<Double> location = new ArrayList<>();
      JsonArray cats = new JsonArray();
      if (event == null) {
        error = true;
        errorMsg = "No event found with ID " + id;
      } else {
        // get the string as an object
        JsonObject json = Main.GSON.fromJson(data, JsonObject.class);
        cats = json.getAsJsonArray("categories");
        List<String> catList = new ArrayList<>();
        for (JsonElement e : cats) {
          catList.add(e.getAsJsonObject().get("value").getAsString());
        }

        updatedSuggestionsList = event.updateSuggestions(catList);
        this.wmu.wmd.updateEvent(event);
      }

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("suggestionsList", updatedSuggestionsList).put("error", error)
          .put("errorMsg", errorMsg).build();

      return Main.GSON.toJson(variables);
    }
  }

  /**
   * This class handles data retrieval for existing events.
   */
  public static class GetEventDataHandler implements Route {

    W2MUniverse wmu;

    public GetEventDataHandler(W2MUniverse wmu) {
      this.wmu = wmu;
    }

    @Override
    public String handle(Request req, Response res) {
      boolean error = false;
      String errorMsg = "";
      // get the id from the url
      String id = req.params(":id");

      Event event = this.wmu.wmd.getEvent(Long.parseLong(id));

      String name = "";
      String time = "";
      String date = "";
      double distance = 0.0;
      List<Suggestion> initialSuggestionsList = new ArrayList<>();
      List<Double> location = new ArrayList<>();
      JsonArray cats = new JsonArray();
      if (event == null) {
        error = true;
        errorMsg = "No event found with ID " + id;
      } else {

        name = event.getName(); // get the name of the group
        time = event.getTime();
        date = event.getDate();
        location = event.getLocation();

        initialSuggestionsList = event.getBestSuggestions();
        // give a default range of suggestions, will do
        // filtering in client

        cats = event.getAllCats();
      }

      Map<String, Object> variables =
          new ImmutableMap.Builder<String, Object>().put("eventID", id)
              .put("groupName", name).put("meetingTime", time)
              .put("meetingDate", date)
              .put("suggestionsList", initialSuggestionsList)
              .put("location", location).put("cats", cats).put("error", error)
              .put("errorMsg", errorMsg).build();

      return Main.GSON.toJson(variables);
    }
  }

  /**
   * This class handles the creation of a new user for a specific event.
   */
  public static class UserHandler implements Route {

    W2MUniverse wmu;

    public UserHandler(W2MUniverse wmu) {
      this.wmu = wmu;
    }

    @Override
    public String handle(Request req, Response res) {
      // get the JSON String
      String data = req.body();

      // get the string as an object
      JsonObject json = Main.GSON.fromJson(data, JsonObject.class);

      Long eventID = json.get("event").getAsLong();
      String username = json.get("user").getAsString();

      boolean error = false;
      String errorMsg = "";
      Long uID = 0L;
      boolean existingUser = true;

      Event event = this.wmu.wmd.getEvent(eventID);

      if (event == null) {
        error = true;
        errorMsg = "No event found with ID " + eventID;
      } else {
        User u = this.wmu.wmd.getUserFromName(username, eventID);
        if (u == null) {
          existingUser = false;
          u = new User(username);
        }
        uID = u.getId();
      }

      Map<String, Object> variables = ImmutableMap
          .of("userID", uID, "existingUser", existingUser, "error", error,
              "errorMsg", errorMsg);

      return Main.GSON.toJson(variables);
    }
  }

  /**
   * Display an error page when an exception occurs in the server.
   * @author jj
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
