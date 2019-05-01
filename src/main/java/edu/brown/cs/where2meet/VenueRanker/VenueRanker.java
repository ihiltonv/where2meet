package edu.brown.cs.where2meet.VenueRanker;

import edu.brown.cs.where2meet.event.Suggestion;

import java.util.*;

public class VenueRanker {

  private List<VenueScore> rankings;

  public VenueRanker() {
    this.rankings = new ArrayList<>();
  }

  public List<Suggestion> getRanked() {
    List<Suggestion> result = new ArrayList<>();
    for(VenueScore v : this.rankings) {
      result.add(v.getVenue());
    }
    return result;
  }

  /**
   * This method allows a user to change the score of
   * a particular venue and recalculates the rankings.
   * @param v
   * @param s
   */
  public void updateRank(Suggestion v, Double s) {
    VenueScore update = new VenueScore(v, s);
    for(VenueScore ven : this.rankings) {
      if(ven.equals(update)) {
        this.rankings.remove(ven);
      }
    }
    this.rankings.add(update);
    Collections.sort(this.rankings);
    Collections.reverse(this.rankings);
  }


  private class VenueScore implements Comparable<VenueScore>{
    private double score;
    private Suggestion venue;
    private String id;

    public VenueScore(Suggestion venue, double score) {
      this.score = score;
      this.venue = venue;
      this.id = venue.getId();
    }

    public double score() {
      return this.score;
    }

    public String getId() {
      return id;
    }

    public Suggestion getVenue() {
      return this.venue;
    }

    @Override
    public int compareTo(VenueScore o) {
      if (this.score() > o.score()) {
        return 1;
      } else if (this.score() < o.score()) {
        return -1;
      } else {
        return 0;
      }
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }

      if (!(o instanceof VenueScore)) {
        return false;
      }

      VenueScore n = (VenueScore) o;

      return n.getId().equals(this.id);
    }
  }


}
