package io.student.rangiffler.model;

import java.time.LocalDate;
import java.util.Objects;

public class Like {
  private String user;

  private String username;

  private LocalDate creationDate;

  public Like() {
  }

  public Like(String user, String username, LocalDate creationDate) {
    this.user = user;
    this.username = username;
    this.creationDate = creationDate;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }

  @Override
  public String toString() {
    return "Like{user='" + user + "', username='" + username + "', creationDate='" + creationDate + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Like that = (Like) o;
    return Objects.equals(user, that.user) &&
        Objects.equals(username, that.username) &&
        Objects.equals(creationDate, that.creationDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, username, creationDate);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private String user;

    private String username;

    private LocalDate creationDate;

    public Like build() {
      Like result = new Like();
      result.user = this.user;
      result.username = this.username;
      result.creationDate = this.creationDate;
      return result;
    }

    public Builder user(String user) {
      this.user = user;
      return this;
    }

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder creationDate(LocalDate creationDate) {
      this.creationDate = creationDate;
      return this;
    }
  }
}
