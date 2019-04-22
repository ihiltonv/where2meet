package edu.brown.cs.where2meet.database;

import com.google.common.cache.CacheLoader;

import edu.brown.cs.where2meet.event.Event;

/**
 * A class to handle loading in events to the cache.
 *
 */
public class EventCacheLoader extends CacheLoader<Long, Event> {

  @Override
  public Event load(Long id) {
    try {
      return W2MDatabase.loadEvent(id);
    } catch (Exception e) {
      System.out.println("ERROR: could not load Event to cache");
      return null;
    }
  }

}
