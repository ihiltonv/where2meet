package edu.brown.cs.where2meet.websockets;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.brown.cs.where2meet.database.W2MDatabase;
import edu.brown.cs.where2meet.event.Event;
import edu.brown.cs.where2meet.event.Suggestion;
import edu.brown.cs.where2meet.event.SuggestionComparator;
import edu.brown.cs.where2meet.event.User;

@WebSocket
public class EventWebSocket {

  private static final Gson GSON = new Gson();
  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
  // private static int nextId = 0;
  private static final Map<Long, ConcurrentLinkedQueue<Session>> eventMap = new ConcurrentHashMap<>();
  // private static final Map<Session, Thread> threadMap = new
  // ConcurrentHashMap();

  /**
   * CONNECT: event_id: event id event_name: event name event_lat: event lat
   * event_lng: event longitude event_time: event's time event_date: event's
   * date user_name: user name user_lat: user latitude user_lng: user longitude
   *
   * UPDATE: event_id: event id, user_id: user id, votes: the votes to assign to
   * the new suggestion, suggestion: the new suggestion to which votes are
   * assigned.
   */

  private static enum MESSAGE_TYPE {
    CONNECT, UPDATE, SCORING
  }

  @OnWebSocketConnect
  public void connection(Session session) {
    sessions.add(session);
    System.out.println("CONNECTED");
  }

  /**
   *
   * @param session
   *          the connecting session.
   *
   * @param message
   *          the message with the connection.
   * @throws IOException
   */
  public void connected(Session session, String message) throws IOException {
    JsonObject received = GSON.fromJson(message, JsonObject.class);
    assert received.get("type").getAsInt() == MESSAGE_TYPE.CONNECT.ordinal();
    Long eid = received.get("event_id").getAsLong();

    Event event = W2MDatabase.getEvent(eid);
    List<Suggestion> suggestions = event.getSuggestions();
    JsonObject leaderboard = new JsonObject();
    leaderboard.addProperty("type", MESSAGE_TYPE.SCORING.ordinal());

    int len = suggestions.size();
    Suggestion s1 = null;
    Suggestion s2 = null;
    Suggestion s3 = null;

    if (len >= 1) {
      s1 = suggestions.get(0);
      leaderboard.addProperty("s1", GSON.toJson(s1.getAsJsonObject()));
    } else {
      leaderboard.addProperty("s1", "no value");
    }
    if (len >= 2) {
      s2 = suggestions.get(1);
      leaderboard.addProperty("s2", GSON.toJson(s2.getAsJsonObject()));
    } else {
      leaderboard.addProperty("s2", "no value");
    }
    if (len >= 3) {
      s3 = suggestions.get(2);
      leaderboard.addProperty("s3", GSON.toJson(s3.getAsJsonObject()));
    } else {
      leaderboard.addProperty("s3", "no value");
    }

    session.getRemote().sendString(GSON.toJson(leaderboard));

  }

  private void updateLeaderBoard(Session session, JsonObject received)
      throws IOException {
    int userVotes = received.get("votes").getAsInt();
    Long uid = received.get("user").getAsLong();
    Long eid = received.get("event").getAsLong();
    User user = W2MDatabase.getUserWithEvent(uid, eid);
    Event event = W2MDatabase.getEvent(eid);
    // get suggestion id rather than suggestion as a json object
    Suggestion newSugg = W2MDatabase
        .getSuggestion(received.get("suggestion").getAsString());
    newSugg.setVotes(newSugg.getVotes() + userVotes);

    Suggestion[] userSuggestions = user.getSuggestions();

    int rank = 3 - ((userVotes + 1) / 2);
    user.setSuggestion(newSugg, rank);
    W2MDatabase.updateUser(user, eid);

    Suggestion oldSugg = userSuggestions[rank];
    oldSugg.setVotes(oldSugg.getVotes() - userVotes);

    List<Suggestion> eventSuggestions = event.getSuggestions();
    int ind = eventSuggestions.indexOf(newSugg);
    if (ind >= 0) {
      eventSuggestions.set(ind, newSugg);
    } else {
      eventSuggestions.add(newSugg);
    }

    ind = eventSuggestions.indexOf(oldSugg);
    if (oldSugg.getVotes() == 0 && ind >= 0) {
      eventSuggestions.remove(ind);

    } else if (ind >= 0) {
      eventSuggestions.set(ind, oldSugg);
    } else {
      eventSuggestions.add(oldSugg);
    }

    SuggestionComparator comp = new SuggestionComparator();
    eventSuggestions.sort(comp);
    event.setSuggestions(eventSuggestions);
    W2MDatabase.updateEvent(event);

    Suggestion s1 = null;
    Suggestion s2 = null;
    Suggestion s3 = null;
    int length = eventSuggestions.size();
    if (length >= 1) {
      s1 = eventSuggestions.get(0);
    }
    if (length >= 2) {
      s2 = eventSuggestions.get(1);
    }
    if (length >= 3) {
      s3 = eventSuggestions.get(2);
    }

    JsonObject response = new JsonObject();
    response.addProperty("type", MESSAGE_TYPE.SCORING.ordinal());
    if (s1 != null) {
      response.addProperty("s1", GSON.toJson(s1.getAsJsonObject()));
    } else {
      response.addProperty("s1", "no value");
    }
    if (s2 != null) {
      response.addProperty("s2", GSON.toJson(s2.getAsJsonObject()));
    } else {
      response.addProperty("s2", "no value");
    }
    if (s3 != null) {
      response.addProperty("s3", GSON.toJson(s3.getAsJsonObject()));
    } else {
      response.addProperty("s3", "no value");
    }

    for (Session s : eventMap.get(eid)) {
      s.getRemote().sendString(GSON.toJson(response));
    }
  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    sessions.remove(session);
    // threadMap.remove(session);
    Collection<ConcurrentLinkedQueue<Session>> values = eventMap.values();
    Iterator<ConcurrentLinkedQueue<Session>> iter = values.iterator();
    while (iter.hasNext()) {
      ConcurrentLinkedQueue<Session> q = iter.next();
      q.remove(session);
    }
    // TODO: close the thread for the user
  }

  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException {
    JsonObject received = GSON.fromJson(message, JsonObject.class);
    if (received.get("type").getAsInt() == MESSAGE_TYPE.UPDATE.ordinal()) {
      updateLeaderBoard(session, received);
    } else if (received.get("type").getAsInt() == MESSAGE_TYPE.CONNECT
        .ordinal()) {
      connected(session, message);
    }

  }

}
