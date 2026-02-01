package io.student.rangiffler.model;

import org.springframework.data.domain.Page;

import java.util.Objects;

public class User {
  private String id;

  private String username;

  private String firstname;

  private String surname;

  private String avatar;

  private FriendStatus friendStatus;

  private Page<User> friends;

  private Page<User> incomeInvitations;

  private Page<User> outcomeInvitations;

  private Country location;

  public User() {
  }

  public User(String id, String username, String firstname, String surname, String avatar,
              FriendStatus friendStatus, Page<User> friends, Page<User> incomeInvitations,
              Page<User> outcomeInvitations, Country location) {
    this.id = id;
    this.username = username;
    this.firstname = firstname;
    this.surname = surname;
    this.avatar = avatar;
    this.friendStatus = friendStatus;
    this.friends = friends;
    this.incomeInvitations = incomeInvitations;
    this.outcomeInvitations = outcomeInvitations;
    this.location = location;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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

  public FriendStatus getFriendStatus() {
    return friendStatus;
  }

  public void setFriendStatus(FriendStatus friendStatus) {
    this.friendStatus = friendStatus;
  }

  public Page<User> getFriends() {
    return friends;
  }

  public void setFriends(Page<User> friends) {
    this.friends = friends;
  }

  public Page<User> getIncomeInvitations() {
    return incomeInvitations;
  }

  public void setIncomeInvitations(Page<User> incomeInvitations) {
    this.incomeInvitations = incomeInvitations;
  }

  public Page<User> getOutcomeInvitations() {
    return outcomeInvitations;
  }

  public void setOutcomeInvitations(Page<User> outcomeInvitations) {
    this.outcomeInvitations = outcomeInvitations;
  }

  public Country getLocation() {
    return location;
  }

  public void setLocation(Country location) {
    this.location = location;
  }

  @Override
  public String toString() {
    return "User{id='" + id + "', username='" + username + "', firstname='" + firstname + "', surname='" + surname + "', avatar='" + avatar + "', friendStatus='" + friendStatus + "', friends='" + friends + "', incomeInvitations='" + incomeInvitations + "', outcomeInvitations='" + outcomeInvitations + "', location='" + location + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User that = (User) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(username, that.username) &&
        Objects.equals(firstname, that.firstname) &&
        Objects.equals(surname, that.surname) &&
        Objects.equals(avatar, that.avatar) &&
        Objects.equals(friendStatus, that.friendStatus) &&
        Objects.equals(friends, that.friends) &&
        Objects.equals(incomeInvitations, that.incomeInvitations) &&
        Objects.equals(outcomeInvitations, that.outcomeInvitations) &&
        Objects.equals(location, that.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, firstname, surname, avatar, friendStatus, friends, incomeInvitations, outcomeInvitations, location);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private String id;

    private String username;

    private String firstname;

    private String surname;

    private String avatar;

    private FriendStatus friendStatus;

    private Page<User> friends;

    private Page<User> incomeInvitations;

    private Page<User> outcomeInvitations;

    private Country location;

    public User build() {
      User result = new User();
      result.id = this.id;
      result.username = this.username;
      result.firstname = this.firstname;
      result.surname = this.surname;
      result.avatar = this.avatar;
      result.friendStatus = this.friendStatus;
      result.friends = this.friends;
      result.incomeInvitations = this.incomeInvitations;
      result.outcomeInvitations = this.outcomeInvitations;
      result.location = this.location;
      return result;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder username(String username) {
      this.username = username;
      return this;
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

    public Builder friendStatus(FriendStatus friendStatus) {
      this.friendStatus = friendStatus;
      return this;
    }

    public Builder friends(Page<User> friends) {
      this.friends = friends;
      return this;
    }

    public Builder incomeInvitations(Page<User> incomeInvitations) {
      this.incomeInvitations = incomeInvitations;
      return this;
    }

    public Builder outcomeInvitations(Page<User> outcomeInvitations) {
      this.outcomeInvitations = outcomeInvitations;
      return this;
    }

    public Builder location(Country location) {
      this.location = location;
      return this;
    }
  }
}
