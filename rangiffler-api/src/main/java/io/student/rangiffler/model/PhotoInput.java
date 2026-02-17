package io.student.rangiffler.model;

import lombok.Data;

import java.util.UUID;

@Data
public class PhotoInput {
  private UUID id;
  private String src;
  private CountryInput country;
  private String description;
  private LikeInput like;

}
