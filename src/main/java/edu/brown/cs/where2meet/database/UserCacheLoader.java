package edu.brown.cs.where2meet.database;

import com.google.common.cache.CacheLoader;

import edu.brown.cs.where2meet.event.User;

/**
 *
 * @author jhuddle1
 *
 *         Class uses to hold a cache that contains the edges associated with a
 *         given MapNode
 *
 */
public class UserCacheLoader extends CacheLoader<String, User> {

  @Override
  public User load(String id) {
    try {
      return W2MDatabase.loadUser(id);
    } catch (Exception e) {
      System.out.println("ERROR: Could not load user to cache");
      return null;
    }
  }

}
