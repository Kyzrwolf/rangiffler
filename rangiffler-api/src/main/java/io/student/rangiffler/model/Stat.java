package io.student.rangiffler.model;

import java.util.Objects;

public class Stat {
  private int count;

  private Country country;

  public Stat() {
  }

  public Stat(int count, Country country) {
    this.count = count;
    this.country = country;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public Country getCountry() {
    return country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  @Override
  public String toString() {
    return "Stat{count='" + count + "', country='" + country + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Stat that = (Stat) o;
    return count == that.count &&
        Objects.equals(country, that.country);
  }

  @Override
  public int hashCode() {
    return Objects.hash(count, country);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private int count;

    private Country country;

    public Stat build() {
      Stat result = new Stat();
      result.count = this.count;
      result.country = this.country;
      return result;
    }

    public Builder count(int count) {
      this.count = count;
      return this;
    }

    public Builder country(Country country) {
      this.country = country;
      return this;
    }
  }
}
