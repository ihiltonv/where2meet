package edu.brown.cs.where2meet.websockets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
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

  public void connected(Session session, String message) {
    JsonObject received = GSON.fromJson(message, JsonObject.class);
    assert received.get("type").getAsInt() == MESSAGE_TYPE.CONNECT.ordinal();
    Long eid = received.get("event_id").getAsLong();
    String eName = received.get("event_name").getAsString();
    Double e_lat = received.get("event_lat").getAsDouble();
    Double e_lng = received.get("event_lng").getAsDouble();
    String date = received.get("event_date").getAsString();
    String time = received.get("event_time").getAsString();

    String uName = received.get("user_name").getAsString();
    // Long uId = received.get("user_id").getAsLong();
    Double u_lat = received.get("user_lat").getAsDouble();
    Double u_lng = received.get("user_lng").getAsDouble();

    // sets up event
    List<Double> ecoords = new ArrayList<>();
    ecoords.add(e_lat);
    ecoords.add(e_lng);
    Event newEvent = null;
    if (eventMap.keySet().contains(eid)) {
      newEvent = W2MDatabase.getEvent(eid);
      eventMap.get(eid).add(session);
    } else {
      newEvent = new Event(eName, ecoords, date, time);
      eid = newEvent.getId();
      eventMap.put(eid, new ConcurrentLinkedQueue<Session>());
      eventMap.get(eid).add(session);
      W2MDatabase.addEvent(newEvent);
    }

    // sets up user
    Long uid = W2MDatabase.getIdFromName(uName, eid);
    User newUser = null;
    if (uid == null) {
      List<Double> uCoords = new ArrayList<>();
      uCoords.add(u_lat);
      uCoords.add(u_lng);
      newUser = new User(uName, uCoords);
      uid = newUser.getId();
    } else {
      newUser = W2MDatabase.getUser(uid);
    }

    newEvent.addUser(uid);
    W2MDatabase.updateEvent(newEvent);

    sessions.add(session);
    // TODO: start running the thread for the user
    // Thread thread = new Thread();
    // threadMap.put(session, thread);

  }

  private void updateLeaderBoard(Session session, JsonObject received)
      throws IOException {
    int userVotes = received.get("votes").getAsInt();
    Long uid = received.get("user").getAsLong();
    Long eid = received.get("event").getAsLong();
    User user = W2MDatabase.getUserWithEvent(uid, eid);
    Event event = W2MDatabase.getEvent(eid);
    Suggestion newSugg = Suggestion
        .toSugg(received.get("suggestion").getAsString());
    newSugg.setVotes(newSugg.getVotes() + userVotes);

    Suggestion[] userSuggestions = user.getSuggestions();

    int rank = (userVotes + 1) / 2;
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

    Suggestion s1 = eventSuggestions.get(0);
    Suggestion s2 = eventSuggestions.get(1);
    Suggestion s3 = eventSuggestions.get(2);

    JsonObject response = new JsonObject();
    response.addProperty("type", MESSAGE_TYPE.SCORING.ordinal());
    response.addProperty("s1", GSON.toJson(s1.getAsJsonObject()));
    response.addProperty("s2", GSON.toJson(s2.getAsJsonObject()));
    response.addProperty("s3", GSON.toJson(s3.getAsJsonObject()));
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
