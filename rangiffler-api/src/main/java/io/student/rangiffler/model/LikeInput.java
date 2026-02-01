package io.student.rangiffler.model;

import java.util.Objects;

public class LikeInput {
  private String user;

  public LikeInput() {
  }

  public LikeInput(String user) {
    this.user = user;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "LikeInput{user='" + user + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LikeInput that = (LikeInput) o;
    return Objects.equals(user, that.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private String user;

    public LikeInput build() {
      LikeInput result = new LikeInput();
      result.user = this.user;
      return result;
    }

    public Builder user(String user) {
      this.user = user;
      return this;
    }
  }
}
