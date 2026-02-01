package io.student.rangiffler.model;

import java.time.LocalDate;
import java.util.Objects;

public class Photo {
  private String id;

  private String src;

  private Country country;

  private String description;

  private LocalDate creationDate;

  private Likes likes;

  private boolean isOwner;

  public Photo() {
  }

  public Photo(String id, String src, Country country, String description, LocalDate creationDate,
               Likes likes, boolean isOwner) {
    this.id = id;
    this.src = src;
    this.country = country;
    this.description = description;
    this.creationDate = creationDate;
    this.likes = likes;
    this.isOwner = isOwner;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSrc() {
    return src;
  }

  public void setSrc(String src) {
    this.src = src;
  }

  public Country getCountry() {
    return country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }

  public Likes getLikes() {
    return likes;
  }

  public void setLikes(Likes likes) {
    this.likes = likes;
  }

  public boolean getIsOwner() {
    return isOwner;
  }

  public void setIsOwner(boolean isOwner) {
    this.isOwner = isOwner;
  }

  @Override
  public String toString() {
    return "Photo{id='" + id + "', src='" + src + "', country='" + country + "', description='" + description + "', creationDate='" + creationDate + "', likes='" + likes + "', isOwner='" + isOwner + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Photo that = (Photo) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(src, that.src) &&
        Objects.equals(country, that.country) &&
        Objects.equals(description, that.description) &&
        Objects.equals(creationDate, that.creationDate) &&
        Objects.equals(likes, that.likes) &&
        isOwner == that.isOwner;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, src, country, description, creationDate, likes, isOwner);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private String id;

    private String src;

    private Country country;

    private String description;

    private LocalDate creationDate;

    private Likes likes;

    private boolean isOwner;

    public Photo build() {
      Photo result = new Photo();
      result.id = this.id;
      result.src = this.src;
      result.country = this.country;
      result.description = this.description;
      result.creationDate = this.creationDate;
      result.likes = this.likes;
      result.isOwner = this.isOwner;
      return result;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder src(String src) {
      this.src = src;
      return this;
    }

    public Builder country(Country country) {
      this.country = country;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder creationDate(LocalDate creationDate) {
      this.creationDate = creationDate;
      return this;
    }

    public Builder likes(Likes likes) {
      this.likes = likes;
      return this;
    }

    public Builder isOwner(boolean isOwner) {
      this.isOwner = isOwner;
      return this;
    }
  }
}
