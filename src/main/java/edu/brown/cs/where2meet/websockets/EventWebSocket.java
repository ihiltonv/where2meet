package edu.brown.cs.where2meet.websockets;

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
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.brown.cs.where2meet.database.W2MDatabase;
import edu.brown.cs.where2meet.event.Event;
import edu.brown.cs.where2meet.event.Suggestion;
import edu.brown.cs.where2meet.event.User;

@WebSocket
public class EventWebSocket {

  private static final Gson GSON = new Gson();
  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
  private static int nextId = 0;
  private static final Map<Long, ConcurrentLinkedQueue<Session>> eventMap = new ConcurrentHashMap<>();
  private static final Map<Session, Thread> threadMap = new ConcurrentHashMap();

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
    CONNECT, UPDATE
  }

  @OnWebSocketConnect
  public void connected(Session session, String message) {
    JsonObject received = GSON.fromJson(message, JsonObject.class);
    assert received.get("type").getAsInt() == MESSAGE_TYPE.CONNECT.ordinal();
    Long eid = received.get("event_id").getAsLong();
    String eName = received.get("event_name").getAsString();
    Double e_lat = received.get("event_lat").getAsDouble();
    Double e_lng = received.get("event_lng").getAsDouble();
    String date = received.get("event_date").getAsString();
    String time = received.get("event_time").getAsString();
    List<Double> ecoords = new ArrayList<>();
    ecoords.add(e_lat);
    ecoords.add(e_lng);
    Event newEvent = null;
    if (eventMap.keySet().contains(eid)) {
      newEvent = W2MDatabase.getEvent(eid);
      eventMap.get(eid).add(session);
    } else {
      newEvent = new Event(eName, ecoords, date, time);
      eventMap.put(eid, new ConcurrentLinkedQueue<Session>());
      eventMap.get(eid).add(session);
    }

    sessions.add(session);
    // TODO: start running the thread for the user
    Thread thread = new Thread();
    threadMap.put(session, thread);

  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    sessions.remove(session);
    threadMap.remove(session);
    Collection<ConcurrentLinkedQueue<Session>> values = eventMap.values();
    Iterator<ConcurrentLinkedQueue<Session>> iter = values.iterator();
    while (iter.hasNext()) {
      ConcurrentLinkedQueue<Session> q = iter.next();
      q.remove(session);
    }
    // TODO: close the thread for the user
  }

  @OnWebSocketMessage
  public void message(Session session, String message) {
    JsonObject received = GSON.fromJson(message, JsonObject.class);
    if (received.get("type").getAsInt() == MESSAGE_TYPE.UPDATE.ordinal()) {
      int userVotes = received.get("votes").getAsInt();
      Long uid = received.get("user").getAsLong();
      Long eid = received.get("event").getAsLong();
      User user = W2MDatabase.getUserWithEvent(uid, eid);
      Event event = W2MDatabase.getEvent(eid);
      Suggestion newSugg = Suggestion
          .toSugg(received.get("suggestion").getAsString());
      newSugg.setVotes(newSugg.getVotes() + userVotes);

      Suggestion[] userSuggestions = user.getSuggestions();
      Suggestion[] eventSuggestions = event.getSuggestions();
      int rank = (userVotes + 1) / 2;
      user.setSuggestion(newSugg, rank);
      Suggestion oldSugg = userSuggestions[rank];
      oldSugg.setVotes(oldSugg.getVotes() - userVotes);

      if (eventSuggestions[0].getVotes() < newSugg.getVotes()) {
        event.setSuggestion(newSugg, 0);
        event.setSuggestion(eventSuggestions[0], 1);
        event.setSuggestion(eventSuggestions[1], 2);

      } else if (eventSuggestions[1].getVotes() < newSugg.getVotes()) {
        event.setSuggestion(newSugg, 1);
        event.setSuggestion(eventSuggestions[1], 2);
      } else if (eventSuggestions[2].getVotes() < newSugg.getVotes()) {
        event.setSuggestion(newSugg, 2);
      }

      // TODO: Send updated list of suggestions to the frontend
    }

  }

}
