package io.student.rangiffler.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class Like {
  private String user;
  private String username;
  private LocalDate creationDate;

}
