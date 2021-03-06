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

@WebSocket
public class EventWebSocket {

  private static final Gson GSON = new Gson();
  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
  private static final Map<Long, ConcurrentLinkedQueue<Session>> eventMap = new ConcurrentHashMap<>();

  private static enum MESSAGE_TYPE {
    CONNECT, UPDATE, SCORING
  }

  @OnWebSocketConnect
  public void connection(Session session) throws IOException {
    sessions.add(session);
    JsonObject obj = new JsonObject();
    obj.addProperty("type", MESSAGE_TYPE.CONNECT.ordinal());
    session.getRemote().sendString(GSON.toJson(obj));
    System.out.println("CONNECTED");
  }

  /**
   * Handles sending initial data to the client.
   *
   * @param session
   *          the connecting session.
   *
   * @param message
   *          the message with the connection.
   *
   * @throws IOException
   */
  public void connected(Session session, String message) throws IOException {
    JsonObject received = GSON.fromJson(message, JsonObject.class);
    assert received.get("type").getAsInt() == MESSAGE_TYPE.CONNECT.ordinal();
    Long eid = received.get("event_id").getAsLong();
    System.out.println(eid);

    Event event = W2MDatabase.getEvent(eid);
    List<Suggestion> suggestions = event.getSuggestions();
    JsonObject leaderboard = new JsonObject();
    leaderboard.addProperty("type", MESSAGE_TYPE.SCORING.ordinal());

    int len = suggestions.size();
    System.out.println("suggestion size:" + len);
    Suggestion s1 = suggestions.get(0);
    Suggestion s2 = suggestions.get(1);
    Suggestion s3 = suggestions.get(2);

    // populates response object with initial data.
    if (s1.getVotes() > 0) {
      leaderboard.addProperty("s1", GSON.toJson(s1.getAsJsonObject()));
    } else {
      leaderboard.addProperty("s1", GSON.toJson(new JsonObject()));
    }
    if (s2.getVotes() > 0) {
      leaderboard.addProperty("s2", GSON.toJson(s2.getAsJsonObject()));
    } else {
      leaderboard.addProperty("s2", GSON.toJson(new JsonObject()));
    }
    if (s3.getVotes() > 0) {
      leaderboard.addProperty("s3", GSON.toJson(s3.getAsJsonObject()));
    } else {
      leaderboard.addProperty("s3", GSON.toJson(new JsonObject()));
    }

    leaderboard.addProperty("oldSugg", GSON.toJson(new JsonObject()));
    session.getRemote().sendString(GSON.toJson(leaderboard));

    if (eventMap.get(eid) == null) {
      eventMap.put(eid, new ConcurrentLinkedQueue<Session>());
    }
    eventMap.get(eid).add(session);

  }

  /**
   * Updates the event suggestions according to what is voted for.
   *
   * @param session
   *          the session the data was sent from.
   * @param received
   *          the message sent by the client.
   * @throws IOException
   *           if the response cannot be sent.
   */
  private void updateLeaderBoard(Session session, JsonObject received)
      throws IOException {
    int userVotes = received.get("votes").getAsInt();

    Long eid = received.get("event").getAsLong();
    Event event = W2MDatabase.getEvent(eid);
    List<Suggestion> eventSuggestions = event.getSuggestions();
    String sId = received.get("suggestion").getAsString();
    String sId2 = received.get("oldSuggestion").getAsString();

    Suggestion newSugg = null;
    Suggestion oldSugg = null;
    for (Suggestion s : eventSuggestions) {
      if (s.getId().equals(sId)) {
        newSugg = s;
      }
      if (s.getId().equals(sId2)) {
        oldSugg = s;
      }
    }
    // update new suggestion.
    if (newSugg == null) {
      newSugg = W2MDatabase
          .getSuggestion(received.get("suggestion").getAsString());
    }
    newSugg.setVotes(newSugg.getVotes() + userVotes);

    int ind = eventSuggestions.indexOf(newSugg);
    if (ind >= 0) {
      eventSuggestions.set(ind, newSugg);
    } else {
      eventSuggestions.add(newSugg);
    }

    // update old suggestion.
    try {
      oldSugg.setVotes(oldSugg.getVotes() - userVotes);
      ind = eventSuggestions.indexOf(oldSugg);
      if (ind >= 0) {
        eventSuggestions.set(ind, oldSugg);
      } else {
        eventSuggestions.add(oldSugg);
      }
    } catch (NullPointerException n) {
      System.out.println("Old sugg isn't real");
    }

    // sort suggestions.
    SuggestionComparator comp = new SuggestionComparator();
    eventSuggestions.sort(comp);
    event.setSuggestions(eventSuggestions);
    W2MDatabase.updateEvent(event);

    // build response object.
    Suggestion s1 = eventSuggestions.get(0);
    Suggestion s2 = eventSuggestions.get(1);
    Suggestion s3 = eventSuggestions.get(2);
    JsonObject response = new JsonObject();
    response.addProperty("type", MESSAGE_TYPE.SCORING.ordinal());
    if (s1.getVotes() > 0) {
      response.addProperty("s1", GSON.toJson(s1.getAsJsonObject()));
    } else {
      response.addProperty("s1", GSON.toJson(new JsonObject()));
    }
    if (s2.getVotes() > 0) {
      response.addProperty("s2", GSON.toJson(s2.getAsJsonObject()));
    } else {
      response.addProperty("s2", GSON.toJson(new JsonObject()));
    }
    if (s3.getVotes() > 0) {
      response.addProperty("s3", GSON.toJson(s3.getAsJsonObject()));
    } else {
      response.addProperty("s3", GSON.toJson(new JsonObject()));
    }
    if (oldSugg == null) {
      response.addProperty("oldSugg", GSON.toJson(new JsonObject()));
    } else {
      response.addProperty("oldSugg", GSON.toJson(oldSugg.getAsJsonObject()));
    }

    // send response to every client in the event.
    for (Session s : eventMap.get(eid)) {
      s.getRemote().sendString(GSON.toJson(response));
    }
  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    sessions.remove(session);
    Collection<ConcurrentLinkedQueue<Session>> values = eventMap.values();
    Iterator<ConcurrentLinkedQueue<Session>> iter = values.iterator();
    while (iter.hasNext()) {
      ConcurrentLinkedQueue<Session> q = iter.next();
      q.remove(session);
    }
  }

  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException {
    JsonObject received = GSON.fromJson(message, JsonObject.class);
    System.out.println(received.get("type").getAsString());
    if (received.get("type").getAsInt() == MESSAGE_TYPE.UPDATE.ordinal()) {
      updateLeaderBoard(session, received);
    } else if (received.get("type").getAsInt() == MESSAGE_TYPE.CONNECT
        .ordinal()) {
      connected(session, message);
    }

  }

}
