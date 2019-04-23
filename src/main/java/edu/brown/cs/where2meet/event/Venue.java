package edu.brown.cs.where2meet.event;

public class Venue {

  String name;
  private double popularity;
  private double distance;
  private int price;
  private Long id;

  public Venue(String name, double popularity, double distance, int price, Long id) {
    this.name = name;
    this.popularity = popularity;
    this.distance = distance;
    this.price = price;
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public double getPopularity() {
    return this.popularity;
  }

  public int getPrice() {
    return this.price;
  }

  public double getDistance() {
    return this.distance;
  }

  public void setPopularity(double popularity) {
    this.popularity = popularity;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }

  public void setPrice(int price) {
    this.price = price;
  }

}
