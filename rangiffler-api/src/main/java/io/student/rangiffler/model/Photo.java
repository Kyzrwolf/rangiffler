package io.student.rangiffler.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class Photo {

  private UUID id;
  private String src;
  private Country country;
  private String description;
  private LocalDate creationDate;
  private Likes likes;
  private boolean isOwner;

}
