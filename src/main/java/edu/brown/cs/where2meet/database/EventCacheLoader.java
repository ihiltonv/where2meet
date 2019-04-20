package edu.brown.cs.where2meet.database;

import com.google.common.cache.CacheLoader;

import edu.brown.cs.where2meet.event.Event;

/**
 *
 * @author jhuddle1
 *
 *         Class uses to hold a cache that contains the edges associated with a
 *         given MapNode
 *
 */
public class EventCacheLoader extends CacheLoader<String, Event> {

  @Override
  public Event load(String id) {
    try {
      return W2MDatabase.loadEvent(id);
    } catch (Exception e) {
      System.out.println("ERROR: could not load Event to cache");
      return null;
    }
  }

}
