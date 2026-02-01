package io.student.rangiffler.model;

import java.util.List;
import java.util.Objects;

public class Likes {
  private int total;

  private List<Like> likes;

  public Likes() {
  }

  public Likes(int total, List<Like> likes) {
    this.total = total;
    this.likes = likes;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public List<Like> getLikes() {
    return likes;
  }

  public void setLikes(List<Like> likes) {
    this.likes = likes;
  }

  @Override
  public String toString() {
    return "Likes{total='" + total + "', likes='" + likes + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Likes that = (Likes) o;
    return total == that.total &&
        Objects.equals(likes, that.likes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(total, likes);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private int total;

    private List<Like> likes;

    public Likes build() {
      Likes result = new Likes();
      result.total = this.total;
      result.likes = this.likes;
      return result;
    }

    public Builder total(int total) {
      this.total = total;
      return this;
    }

    public Builder likes(List<Like> likes) {
      this.likes = likes;
      return this;
    }
  }
}
