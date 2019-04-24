package edu.brown.cs.where2meet.main;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.where2meet.event.Event;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.*;
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
    //create W2MUniverse
    this.wmu = new W2MUniverse();
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

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
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    //Calls we want:
    //
    // new event
    // new votes
    //
    //
    //

    // Setup Spark Routes
    Spark.post("/event", new EventHandler());
    Spark.post("/vote", new EventHandler());
  }

  /**
   * This class handles the creation of new events.
   */
  public static class EventHandler implements Route {

    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String name = qm.value("name");
      double lat = Double.parseDouble(qm.value("lat"));
      double lon = Double.parseDouble(qm.value("lon"));
      String date = qm.value("date");
      String time = qm.value("time");

      List<Double> coordinates = new ArrayList<>();
      coordinates.add(lat);
      coordinates.add(lon);

      Event event = new Event(name, coordinates, date, time);

      //return empty json array for leaderboard and picks
      //return a list (ranked of all suggestions)
      //return id


      //TODO: Build the json
      Map<String, Object> variables =
              ImmutableMap.of("testKeyEvent", "testValEvent");


      return GSON.toJson(variables);
    }
  }

  /**
   * This class handles a user voting in a specific
   * event.
   */
  public static class VoteHandler implements Route {

    //return empty json array for leaderboard and picks
    //return a list (ranked of all suggestions)
    //return id
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();


      //TODO: build the json
      Map<String, Object> variables =
              ImmutableMap.of("testKeyVote", "testValVote");


      return GSON.toJson(variables);
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
