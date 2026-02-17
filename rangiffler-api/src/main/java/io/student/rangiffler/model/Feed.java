package io.student.rangiffler.model;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

public class Feed {
  private String username;

  private boolean withFriends;

  private Page<Photo> photos;

  private List<Stat> stat;

  public Feed() {
  }

  public Feed(String username, boolean withFriends, Page<Photo> photos, List<Stat> stat) {
    this.username = username;
    this.withFriends = withFriends;
    this.photos = photos;
    this.stat = stat;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public boolean getWithFriends() {
    return withFriends;
  }

  public void setWithFriends(boolean withFriends) {
    this.withFriends = withFriends;
  }

  public Page<Photo> getPhotos() {
    return photos;
  }

  public void setPhotos(Page<Photo> photos) {
    this.photos = photos;
  }

  public List<Stat> getStat() {
    return stat;
  }

  public void setStat(List<Stat> stat) {
    this.stat = stat;
  }

  @Override
  public String toString() {
    return "Feed{username='" + username + "', withFriends='" + withFriends + "', photos='" + photos + "', stat='" + stat + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Feed that = (Feed) o;
    return Objects.equals(username, that.username) &&
        withFriends == that.withFriends &&
        Objects.equals(photos, that.photos) &&
        Objects.equals(stat, that.stat);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, withFriends, photos, stat);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private String username;

    private boolean withFriends;

    private Page<Photo> photos;

    private List<Stat> stat;

    public Feed build() {
      Feed result = new Feed();
      result.username = this.username;
      result.withFriends = this.withFriends;
      result.photos = this.photos;
      result.stat = this.stat;
      return result;
    }

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder withFriends(boolean withFriends) {
      this.withFriends = withFriends;
      return this;
    }

    public Builder photos(Page<Photo> photos) {
      this.photos = photos;
      return this;
    }

    public Builder stat(List<Stat> stat) {
      this.stat = stat;
      return this;
    }
  }
}
