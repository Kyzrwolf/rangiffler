package io.student.rangiffler.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Stat {
  private int count;
  private Country country;

}
