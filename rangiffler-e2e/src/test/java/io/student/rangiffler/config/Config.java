package io.student.rangiffler.config;

import javax.annotation.Nonnull;

public interface Config {

  @Nonnull
  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  @Nonnull
  String browser();

  @Nonnull
  String browserSize();

  @Nonnull
  String frontUrl();

  @Nonnull
  String authUrl();

  @Nonnull
  String apiUrl();

  @Nonnull
  String registerUrl();

  @Nonnull
  String authJdbcUrl();

  @Nonnull
  String userdataJdbcUrl();

  @Nonnull
  String dbUsername();

  @Nonnull
  String dbPassword();

  @Nonnull
  String githubUrl();
}
