package edu.brown.cs.where2meet.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.brown.cs.where2meet.event.Event;
import edu.brown.cs.where2meet.event.Suggestion;
import edu.brown.cs.where2meet.websockets.EventWebSocket;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

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
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.webSocket("/leaderboard", EventWebSocket.class);
    // Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request
          .headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers",
            accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request
          .headers("Access-Control-Request-Method");
      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods",
            accessControlRequestMethod);
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

    // Calls we want:
    //
    // new event
    // new votes
    //
    //
    //

    // Setup Spark Routes
    Spark.post("/event", new EventHandler(wmu));
    Spark.get("/event/:id", new GetEventDataHandler(wmu));
    Spark.post("/newuser", new UserHandler(wmu));

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
      Map<String, Object> variables = ImmutableMap.of("id", event.getId(),
          "suggestionsList", suggestions);

      return Main.GSON.toJson(variables);
    }
  }

  /**
   * This class handles data retrieval for existing events
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

      String name = event.getName(); // get the name of the group
      String time = event.getTime(); // make sure the time is in this format, in
                                     // military
      // time so.. 11pm will be 23:00
      String date = event.getDate(); // again, need to be in this form

      List<Suggestion> initialSuggestionsList = new ArrayList<>(); // give a
                                                                   // default
                                                                   // range of
                                                                   // suggestions,
                                                                   // will do
      // filtering in client

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("eventID", id).put("groupName", name).put("meetingTime", time)
          .put("meetingDate", date)
          .put("suggestionsList", initialSuggestionsList).put("error", error)
          .put("errorMsg", errorMsg).build();

      return Main.GSON.toJson(variables);
    }
  }

  /**
   * This class handles a user voting in a specific event.
   */
  public static class UserHandler implements Route {

    W2MUniverse wmu;

    public UserHandler(W2MUniverse wmu) {
      this.wmu = wmu;
    }

    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String eventID = qm.value("event");
      String userID = qm.value("user");

      boolean error = false;
      String errorMsg = "";

      Map<String, Object> variables = ImmutableMap.of("testKeyVote",
          "testValVote");

      return Main.GSON.toJson(variables);
    }
  }

  /**
   * Display an error page when an exception occurs in the server.
   *
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
