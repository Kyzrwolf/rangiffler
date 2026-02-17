package io.student.rangiffler.model;

import lombok.Data;

@Data
public class FriendshipInput {
  private String user;
  private FriendshipAction action;

}
