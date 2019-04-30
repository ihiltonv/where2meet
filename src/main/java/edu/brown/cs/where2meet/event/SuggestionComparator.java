package edu.brown.cs.where2meet.event;

import java.util.Comparator;

public class SuggestionComparator implements Comparator<Suggestion> {

  @Override
  public int compare(Suggestion s1, Suggestion s2) {
    int votes1 = s1.getVotes();
    int votes2 = s2.getVotes();
    if (votes1 > votes2) {
      return -1;
    } else if (votes1 < votes2) {
      return 1;
    }
    return 0;
  }

}
