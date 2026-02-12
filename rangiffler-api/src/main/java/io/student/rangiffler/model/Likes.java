package io.student.rangiffler.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@Accessors(chain = true)
public class Likes {
  private int total;
  private List<Like> likes;

}
