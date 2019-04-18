package edu.brown.cs.where2meet.main;

import com.google.gson.Gson;
import edu.brown.cs.where2meet.database.W2MDatabase;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public final class Main {

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
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);
    W2MDatabase wmdb = new W2MDatabase();
    System.out.println("Finished");

  }
}
