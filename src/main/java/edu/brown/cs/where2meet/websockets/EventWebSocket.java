package edu.brown.cs.where2meet.websockets;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
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

@WebSocket
public class EventWebSocket {

  private static final Gson GSON = new Gson();
  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
  private static int nextId = 0;

  /**
   * CONNECT: event_id: event id event_name: event name event_lat: event lat
   * event_lng: event longitude event_time: event's time event_date: event's
   * date user_name: user name user_lat: user latitude user_lng: user longitude
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
    if (eid.intValue() == 0) {
      newEvent = new Event(eName, ecoords, date, time);
      W2MDatabase.addEvent(newEvent);
    } else {
      newEvent = W2MDatabase.getEvent(eid);
    }
    sessions.add(session);

  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    sessions.remove(session);
  }

  @OnWebSocketMessage
  public void message(Session session, String message) {

  }

}
