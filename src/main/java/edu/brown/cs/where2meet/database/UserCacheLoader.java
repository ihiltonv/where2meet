package edu.brown.cs.where2meet.database;

import com.google.common.cache.CacheLoader;

import edu.brown.cs.where2meet.event.User;

/**
 * A class to load in users for the user cache.
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
