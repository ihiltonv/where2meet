package edu.brown.cs.where2meet.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SuggestionComparatorTest {

  @Test
  public void testConstructor() {
    SuggestionComparator comp = new SuggestionComparator();
    assertNotNull(comp);
  }

  @Test
  public void testCompareAndSort() {
    System.out.println("testCompareAndSort");

    Suggestion s1 = new Suggestion();
    s1.setVenue("one");
    Suggestion s2 = new Suggestion();
    s2.setVenue("two");
    Suggestion s3 = new Suggestion();
    s3.setVenue("three");
    SuggestionComparator comp = new SuggestionComparator();

    s1.setVotes(1);
    s2.setVotes(2);
    s3.setVotes(3);

    List<Suggestion> sugg = new ArrayList<>();
    sugg.add(s1);
    sugg.add(s2);
    sugg.add(s3);

    sugg.sort(comp);

    assertEquals(sugg.get(2).getVenue(), "one");
    assertEquals(sugg.get(1).getVenue(), "two");
    assertEquals(sugg.get(0).getVenue(), "three");
  }

}
