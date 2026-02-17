package io.student.rangiffler.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class User {
  private UUID id;
  private String username;
  private String firstname;
  private String surname;
  private String avatar;
  private FriendStatus friendStatus;
  private Page<User> friends;
  private Page<User> incomeInvitations;
  private Page<User> outcomeInvitations;
  private Country location;

}
