package io.student.rangiffler.model;

import java.util.Objects;

public class PhotoInput {
  private String id;

  private String src;

  private CountryInput country;

  private String description;

  private LikeInput like;

  public PhotoInput() {
  }

  public PhotoInput(String id, String src, CountryInput country, String description,
                    LikeInput like) {
    this.id = id;
    this.src = src;
    this.country = country;
    this.description = description;
    this.like = like;
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

  public CountryInput getCountry() {
    return country;
  }

  public void setCountry(CountryInput country) {
    this.country = country;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LikeInput getLike() {
    return like;
  }

  public void setLike(LikeInput like) {
    this.like = like;
  }

  @Override
  public String toString() {
    return "PhotoInput{id='" + id + "', src='" + src + "', country='" + country + "', description='" + description + "', like='" + like + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PhotoInput that = (PhotoInput) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(src, that.src) &&
        Objects.equals(country, that.country) &&
        Objects.equals(description, that.description) &&
        Objects.equals(like, that.like);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, src, country, description, like);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private String id;

    private String src;

    private CountryInput country;

    private String description;

    private LikeInput like;

    public PhotoInput build() {
      PhotoInput result = new PhotoInput();
      result.id = this.id;
      result.src = this.src;
      result.country = this.country;
      result.description = this.description;
      result.like = this.like;
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

    public Builder country(CountryInput country) {
      this.country = country;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder like(LikeInput like) {
      this.like = like;
      return this;
    }
  }
}
