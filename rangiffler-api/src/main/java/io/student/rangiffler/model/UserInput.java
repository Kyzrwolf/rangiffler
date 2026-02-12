package io.student.rangiffler.model;

import lombok.Data;

@Data
public class UserInput {
  private String firstname;
  private String surname;
  private String avatar;
  private CountryInput location;

}
