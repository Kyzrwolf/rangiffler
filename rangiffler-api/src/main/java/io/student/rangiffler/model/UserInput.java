package io.student.rangiffler.model;

import java.util.Objects;

public class UserInput {
  private String firstname;

  private String surname;

  private String avatar;

  private CountryInput location;

  public UserInput() {
  }

  public UserInput(String firstname, String surname, String avatar, CountryInput location) {
    this.firstname = firstname;
    this.surname = surname;
    this.avatar = avatar;
    this.location = location;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public CountryInput getLocation() {
    return location;
  }

  public void setLocation(CountryInput location) {
    this.location = location;
  }

  @Override
  public String toString() {
    return "UserInput{firstname='" + firstname + "', surname='" + surname + "', avatar='" + avatar + "', location='" + location + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserInput that = (UserInput) o;
    return Objects.equals(firstname, that.firstname) &&
        Objects.equals(surname, that.surname) &&
        Objects.equals(avatar, that.avatar) &&
        Objects.equals(location, that.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstname, surname, avatar, location);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private String firstname;

    private String surname;

    private String avatar;

    private CountryInput location;

    public UserInput build() {
      UserInput result = new UserInput();
      result.firstname = this.firstname;
      result.surname = this.surname;
      result.avatar = this.avatar;
      result.location = this.location;
      return result;
    }

    public Builder firstname(String firstname) {
      this.firstname = firstname;
      return this;
    }

    public Builder surname(String surname) {
      this.surname = surname;
      return this;
    }

    public Builder avatar(String avatar) {
      this.avatar = avatar;
      return this;
    }

    public Builder location(CountryInput location) {
      this.location = location;
      return this;
    }
  }
}
